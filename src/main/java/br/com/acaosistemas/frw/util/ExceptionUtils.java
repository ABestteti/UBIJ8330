package br.com.acaosistemas.frw.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Classe utilitaria manipular informacoes de excecoes
 * <p>
 * <b>Empresa:</b> Acao Sistemas de Informatica Ltda.
 * <p>
 * Altera��es:
 * <p>
 * 2018.03.08 - ABS - Implementado JavaDoc.
 *
 * @author Anderson Bestteti Santos
 *
 */
public final class ExceptionUtils {

	/***
	 * Retorna o stack trace da excecao como uma String.
	 * @param pException
	 * @return String contendo o stack trace
	 */
	public static String stringStackTrace(Exception pException) {
		StringWriter sw = new StringWriter();
		
		// Transforma o stack trace em uma string para
		// sava-la no log
		pException.printStackTrace(new PrintWriter(sw));
		
		return sw.toString();
	}
}
