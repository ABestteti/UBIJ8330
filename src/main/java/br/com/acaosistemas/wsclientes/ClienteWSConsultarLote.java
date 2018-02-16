package br.com.acaosistemas.wsclientes;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import br.com.acaosistemas.db.dao.UBIRuntimesDAO;
import br.com.acaosistemas.db.model.UBILotesEsocial;
import br.com.acaosistemas.frw.util.ExceptionUtils;
import br.com.acaosistemas.frw.util.HttpUtils;

/**
 * Classe de implementacao do metodo de consumo do web service de consulta
 * do lote de eventos no eSocial.
 * 
 * @author Anderson Bestteti Santos
 *
 */
public class ClienteWSConsultarLote {

    /**
     * Metodo de consumo do web service de consulta do lote de eventos
     * no eSocial.
     * 
     * @param pUbleRow
     * @throws MalformedURLException
     * @throws IOException
     */
	public void execWebService(UBILotesEsocial pUbleRow) throws MalformedURLException, IOException {
		String parametros;
		String wsEndPoint;
		String portaCNPJ;
		
		UBIRuntimesDAO runtimeDAO  = new UBIRuntimesDAO();
		
		wsEndPoint = runtimeDAO.getRuntimeValue("UBIWSCONSULTALOTE");
		
		if (runtimeDAO.runtimeIdExists("PORTACNPJ")) {
			// Recupera do banco de dados a informação do runtime que identifica
			// a porta HTTP do web service do UBI para iniciar o processo de
			// envio de lote de eventos, especifíco para o CNPJ em questão.
			// Essa é uma solução de contorno para o problema com a troca de
			// certificado, quando muda o CNPJ do lote de eventos, por conta 
			// do cache criado pelo Apache CXF no WildFly.
			portaCNPJ = runtimeDAO.getRuntimeValue("PORTA".concat(pUbleRow.getUbcaCnpj().toString().trim()));

			// Substitui a porta definida na URL do runtime UBIWSCONSULTALOTE pela
			// porta definida pelo runtime do CNPJ em processamento. 
			// Para tanto, é usada a expressão regular "(?<port>:\\d+)"
			// que faz o match pelo grupo <port> seguido de um ou mais dígitos
			// "\\d+"
			wsEndPoint = wsEndPoint.replaceFirst("(?<port>:\\d+)", ":".concat(portaCNPJ));
			
			System.out.println("     Workaround ativado para o CNPJ");
			System.out.println("     Uso da porta "+portaCNPJ+" definido para o CPNJ raiz "+pUbleRow.getUbcaCnpj());
			System.out.println("     Nova URL do web service do UBI: "+wsEndPoint);
		}
		
		// Fecha a conexao com o banco de dados
		runtimeDAO.closeConnection();
		

		// Monta o parametro de chamada do web service
		parametros  = pUbleRow.getUbiLoteNumero().toString();
		
		try {
			URL url = new URL(wsEndPoint+parametros);
			
			HttpURLConnection request = (HttpURLConnection) url.openConnection();			

			// Define que a requisicao pode obter informacoes de retorno.
		    request.setDoOutput(true);
						
			// Define o metodo da requisicao
			request.setRequestMethod("GET");
			
			// Conecta na URL
			request.connect();
			
			if (request.getResponseCode() != HttpURLConnection.HTTP_OK) {
			    if (request.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
				    throw new MalformedURLException("Codigo HTTP retornado: " + 
			                                        request.getResponseCode() + 
			                                        " [" + wsEndPoint + "]\n" +
			                                        "Parametros: "            + 
			                                        parametros);
			    }
			    else {
			    	    throw new IOException("Codigo HTTP retornado: "     + 
			                              request.getResponseCode() + 
			                              " [" + wsEndPoint + "]\n" +
			                              "Parametros: "            +
			                              parametros);
			    }
			}
			else {
				System.out.println("HTTP code .....: " + request.getResponseMessage());
				System.out.println("Message from ws: " + HttpUtils.readResponse(request) + " [" + wsEndPoint + "]");
			}
						
		} catch (MalformedURLException e) {
			throw new MalformedURLException(e.getMessage()+":\n"+ExceptionUtils.stringStackTrace(e));
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException(e.getMessage()+":\n"+ExceptionUtils.stringStackTrace(e));
		} catch (IOException e) {
			throw new IOException(e.getMessage()+":\n"+ExceptionUtils.stringStackTrace(e));
		}
	}
}
