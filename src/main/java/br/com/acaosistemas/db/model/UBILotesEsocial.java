package br.com.acaosistemas.db.model;

import br.com.acaosistemas.db.enumeration.StatusLotesEventosEnum;

/**
 * Entidade representando tabela UBI_LOTES_ESOCIAL
 *
 * @author Anderson Bestteti Santos
 */
public class UBILotesEsocial {

	private Long ubiLoteNumero;
	private Long ubcaCnpj;
	private StatusLotesEventosEnum status;
	private String xmlRetornoLote;
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
	public String getXmlRetornoLote() {
		return xmlRetornoLote;
	}
	public void setXmlRetornoLote(String xmlRetornoLote) {
		this.xmlRetornoLote = xmlRetornoLote;
	}
	public Long getUbcaCnpj() {
		return ubcaCnpj;
	}
	public void setUbcaCnpj(Long ubcaCnpj) {
		this.ubcaCnpj = ubcaCnpj;
	}
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
}
