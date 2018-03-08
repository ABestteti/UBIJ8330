package br.com.acaosistemas.db.model;

import java.sql.Date;

import br.com.acaosistemas.db.enumeration.StatusLotesEventosEnum;

/**
 * Entidade representando tabela UBI_LOTES_ESOCIAL_LOG
 * <p>
 * <b>Empresa:</b> Acao Sistemas de Informatica Ltda.
 * <p>
 * Alterações:
 * <p>
 * 2018.03.08 - ABS - Alteração da PK da tabela UBI_LOTES_ESOCIAL_LOG, 
 *                    conforme SA 20330.
 *                  - Implementado metodo toString().
 *
 * @author Anderson Bestteti Santos
 */
public class UBILotesEsocialLog {
	
	private Long ubleUbiLoteNumero;
	private Long seqReg;
	private Date dtMov;
	private Long numErro;
	private String mensagem;
	private StatusLotesEventosEnum status;
	private String rowId;
	
	public Long getSeqReg() {
		return seqReg;
	}
	public void setSeqReg(Long seqReg) {
		this.seqReg = seqReg;
	}
	public Long getUbleUbiLoteNumero() {
		return ubleUbiLoteNumero;
	}
	public void setUbleUbiLoteNumero(Long ubleUbiLoteNumero) {
		this.ubleUbiLoteNumero = ubleUbiLoteNumero;
	}
	public Date getDtMov() {
		return dtMov;
	}
	public void setDtMov(Date dtMov) {
		this.dtMov = dtMov;
	}
	public Long getNumErro() {
		return numErro;
	}
	public void setNumErro(Long numErro) {
		this.numErro = numErro;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public StatusLotesEventosEnum getStatus() {
		return status;
	}
	public void setStatus(StatusLotesEventosEnum status) {
		this.status = status;
	}
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	@Override
	public String toString() {
		return "UBILotesEsocialLog [ubleUbiLoteNumero=" + ubleUbiLoteNumero + ", seqReg=" + seqReg + ", dtMov=" + dtMov
				+ ", numErro=" + numErro + ", status=" + status + ", rowId=" + rowId + "]";
	}
}
