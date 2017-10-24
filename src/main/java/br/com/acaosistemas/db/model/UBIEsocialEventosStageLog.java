package br.com.acaosistemas.db.model;

import java.sql.Timestamp;

import br.com.acaosistemas.db.enumeration.StatusLotesEventosEnum;

public class UBIEsocialEventosStageLog {

	private Timestamp dtMov;
	private Timestamp ubesDtMov;
	private String    mensagem;
	private StatusLotesEventosEnum status;
	private Long      numErro;
	
	public Timestamp getDtMov() {
		return dtMov;
	}
	public void setDtMov(Timestamp dtMov) {
		this.dtMov = dtMov;
	}
	public Timestamp getUbesDtMov() {
		return ubesDtMov;
	}
	public void setUbesDtMov(Timestamp ubesDtMov) {
		this.ubesDtMov = ubesDtMov;
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
	public Long getNumErro() {
		return numErro;
	}
	public void setNumErro(Long numErro) {
		this.numErro = numErro;
	}
}
