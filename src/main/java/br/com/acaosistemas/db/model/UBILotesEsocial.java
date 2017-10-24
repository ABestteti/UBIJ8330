package br.com.acaosistemas.db.model;

import java.sql.Timestamp;

import br.com.acaosistemas.db.enumeration.StatusLotesEventosEnum;

/**
 * Entidade representando tabela UBI_LOTES_ESOCIAL
 *
 * @author Anderson Bestteti Santos
 */
public class UBILotesEsocial {

	private Long ubiLoteNumero;
	private StatusLotesEventosEnum status;
	private String rowId;
	
	public Long getUbiLoteNumero() {
		return ubiLoteNumero;
	}
	public void setUbiLoteNumero(Long ubiLoteNumero) {
		this.ubiLoteNumero = ubiLoteNumero;
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
