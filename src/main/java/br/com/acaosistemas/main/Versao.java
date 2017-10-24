package br.com.acaosistemas.main;
/**
 * 
 * @author Anderson Bestteti Santos
 *
 * Classe para retornar a versao do programa
 */
public final class Versao {
    
	private static String empresa   = "Universo Desenvolvimento de Sistemas Ltda\n";
	private static String copyright = "Direitos Autorais (c) 2017-2017\n";
	private static String descricao = "Servico de consumo do web service de assinatura de evento.\n";
	private static String programa  = "UBIJ8320";
	private static String versao    = "2.0.00.24.10.2017";
	
	
	public static String getStringVersao() {
		return programa + " Vrs. " + versao;
	}
	
	public static String ver() {
		return empresa+copyright+descricao+getStringVersao();
	}

}
