package br.com.acaosistemas.db.model;

import java.sql.Timestamp;

import br.com.acaosistemas.db.enumeration.StatusLotesEventosEnum;

public class UBILotesEsocialLog {
	
	private Long ubleUbiLoteNumero;
	private Timestamp dtMov;
	private Long numErro;
	private String mensagem;
	private StatusLotesEventosEnum status;
	private String rowId;
	
	public Long getUbleUbiLoteNumero() {
		return ubleUbiLoteNumero;
	}
	public void setUbleUbiLoteNumero(Long ubleUbiLoteNumero) {
		this.ubleUbiLoteNumero = ubleUbiLoteNumero;
	}
	public Timestamp getDtMov() {
		return dtMov;
	}
	public void setDtMov(Timestamp dtMov) {
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
}
