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
 * Classe de implementacao do metodo de consumo do web service de envio
 * do lote de eventos para o eSocial.
 * 
 * @author Anderson Bestteti Santos
 *
 */
public class ClienteWSEnviarLote {
	
	public String execWebService(UBILotesEsocial pUbleRow) throws MalformedURLException, IOException {
		String parametros;
		String wsEndPoint;
		String xmlRetornoLote;
		
		UBIRuntimesDAO runtimeDAO  = new UBIRuntimesDAO();
		
		// Recupera do banco de dados a informacao do runtime UBIWSENVIALOTE
		wsEndPoint = runtimeDAO.getRuntimeValue("UBIWSENVIALOTE");
		
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
				    throw new MalformedURLException("Código HTTP retornado: " + 
			                                        request.getResponseCode() + 
			                                        " [" + wsEndPoint + "]\n" +
			                                        "Parâmetros: "            + 
			                                        parametros);
			    }
			    else {
			    	    throw new IOException("Código HTTP retornado: "     + 
			                              request.getResponseCode() + 
			                              " [" + wsEndPoint + "]\n" +
			                              "Parâmetros: "            +
			                              parametros);
			    }
			}
			else {
				xmlRetornoLote = HttpUtils.readResponse(request);
				System.out.println("HTTP code .....: " + request.getResponseMessage());
				System.out.println("Message from ws: " + xmlRetornoLote + " [" + wsEndPoint + "]");
			}
						
		} catch (MalformedURLException e) {
			throw new MalformedURLException(e.getMessage()+":\n"+ExceptionUtils.stringStackTrace(e));
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException(e.getMessage()+":\n"+ExceptionUtils.stringStackTrace(e));
		} catch (IOException e) {
			throw new IOException(e.getMessage()+":\n"+ExceptionUtils.stringStackTrace(e));
		}
		
		return xmlRetornoLote;
	}
}
