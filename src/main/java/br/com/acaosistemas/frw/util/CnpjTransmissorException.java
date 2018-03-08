package br.com.acaosistemas.frw.util;

/**
 * Excecao para identificar a divergencia entre o CNPJ (armazenado na tag "nrInsc", da
 * tag de grupo "ideTramissor", definido no lote de eventos do eSocial) e o CNPJ completo 
 * associado ao lote de eventos do UBI.  Caso seja diferente, o programa devera passar o 
 * lote para estados de "Desassociar Eventos" e logar essa ocorrencia no log da tabela 
 * de lotes do eSocial.
 * <p>
 * <b>Empresa:</b> Acao Sistemas de Informatica Ltda.
 * <p>
 * Alterações:
 * <p>
 * 2018.03.08 - ABS - Implementado metodo toString().
 *
 * @author Anderson Bestteti
 *
 */
public class CnpjTransmissorException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String mensagemExcecao;

	/**
	 * @param mensagemExcecao
	 */
	public CnpjTransmissorException(String mensagemExcecao) {
		super();
		this.mensagemExcecao = mensagemExcecao;
	}
	
	public CnpjTransmissorException() {
		super();
		this.mensagemExcecao = "O CNPJ do ideTransmisso difere do CNPJ completo do CNPJ autorizado.";
	}

	@Override
	public String toString() {
		return "CnpjTransmissorException [mensagemExcecao=" + mensagemExcecao + "]";
	}	
}
