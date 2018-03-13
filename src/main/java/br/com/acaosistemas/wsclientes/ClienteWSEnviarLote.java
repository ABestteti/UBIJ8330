package br.com.acaosistemas.wsclientes;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.acaosistemas.db.dao.UBIRuntimesDAO;
import br.com.acaosistemas.db.enumeration.LotesTipoAmbienteEnum;
import br.com.acaosistemas.db.model.UBILotesEsocial;
import br.com.acaosistemas.frw.util.ExceptionUtils;
import br.com.acaosistemas.frw.util.HttpUtils;

/**
 * Classe de implementacao do metodo de consumo do web service de envio
 * do lote de eventos para o eSocial.
 * <p>
 * <b>Empresa:</b> Acao Sistemas de Informatica Ltda.
 * <p>
 * Alterações:
 * <p>
 * 2018.03.13 - ABS - Adicionado sistema de log com a biblioteca log4j2.
 *                  - Implementado JavaDoc.
 *
 * @author Anderson Bestteti Santos
 *
 */
public class ClienteWSEnviarLote {

	private static final Logger logger = LogManager.getLogger(ClienteWSEnviarLote.class);
	
	public String execWebService(UBILotesEsocial pUbleRow) throws MalformedURLException, IOException {
		String parametros;
		String wsEndPoint;
		String xmlRetornoLote;
		String portaWFAmbiente = null;
		
		UBIRuntimesDAO runtimeDAO  = new UBIRuntimesDAO();
		
		// Recupera do banco de dados a informacao do runtime UBIWSENVIALOTE
	    wsEndPoint = runtimeDAO.getRuntimeValue("UBIWSENVIALOTE");			

	    if (pUbleRow.getTipoAmbiente() == LotesTipoAmbienteEnum.PRODUCAO) {
	    	if (runtimeDAO.runtimeIdExists("PORTAWFPRODUCAO")) {
	    		portaWFAmbiente = runtimeDAO.getRuntimeValue("PORTAWFPRODUCAO");
	    	}
	    } else if (pUbleRow.getTipoAmbiente() == LotesTipoAmbienteEnum.PRODUCAO_RESTRITA) {
	    	if (runtimeDAO.runtimeIdExists("PORTAWFPRODRESTRITA")) {
	    		portaWFAmbiente = runtimeDAO.getRuntimeValue("PORTAWFPRODRESTRITA");
	    	}	    	
	    }

	    if (portaWFAmbiente != null) {
			// Substitui a porta definida na URL do runtime UBIWSCONSULTALOTE pela
			// porta definida para a instância do WilFly usado para produção restrita 
	    	// do eSocial, ou a porta definida para a instância do WildFly usado para 
	    	// produção do eSocial. 
			// Para tanto, é usada a expressão regular "(?<port>:\\d+)"
			// que faz o match pelo grupo <port> seguido de um ou mais dígitos
			// "\\d+"
    		wsEndPoint = wsEndPoint.replaceFirst("(?<port>:\\d+)", ":".concat(portaWFAmbiente));
	    }
		
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
				xmlRetornoLote = HttpUtils.readResponse(request);
				logger.info("HTTP code .....: " + request.getResponseMessage());
				logger.info("Message from ws: " + xmlRetornoLote + " [" + wsEndPoint + "]");
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
