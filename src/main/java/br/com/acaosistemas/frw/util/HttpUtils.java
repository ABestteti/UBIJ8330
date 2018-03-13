package br.com.acaosistemas.frw.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Classe utilitaria para a classe Http
 * <p>
 * <b>Empresa:</b> Acao Sistemas de Informatica Ltda.
 * <p>
 * Alterações:
 * <p>
 * 2018.03.08 - ABS - Implementado JavaDoc.
 *
 * @author Anderson Bestteti Santos
 *
 */
public final class HttpUtils {

	/***
	 * Recupera a mensagem de retorno do webservice.
	 * @param request
	 * @return String contendo a mensagem de retorno do webservice
	 * @throws IOException
	 */
	public static String readResponse(HttpURLConnection request) throws IOException {
	    ByteArrayOutputStream os;
	    try (InputStream is = request.getInputStream()) {
	        os = new ByteArrayOutputStream();
	        int b;
	        while ((b = is.read()) != -1) {
	            os.write(b);
	        }
	    }
	    return new String(os.toByteArray());
	}
}
