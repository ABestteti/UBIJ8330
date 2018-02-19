package br.com.acaosistemas.db.model;

import br.com.acaosistemas.db.enumeration.LotesTipoAmbienteEnum;
import br.com.acaosistemas.db.enumeration.StatusLotesEventosEnum;

/**
 * Entidade representando tabela UBI_LOTES_ESOCIAL
 *
 * @author Anderson Bestteti Santos
 */
public class UBILotesEsocial {

	private Long ubiLoteNumero;
	private Long ubcaCnpj;
	private Long cnpjCompleto;
	private StatusLotesEventosEnum status;
	private String xmlLote;
	private String xmlRetornoLote;
	private String rowId;
	private LotesTipoAmbienteEnum tipoAmbiente;
	
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
	public LotesTipoAmbienteEnum getTipoAmbiente() {
		return tipoAmbiente;
	}
	public void setTipoAmbiente(LotesTipoAmbienteEnum tipoAmbiente) {
		this.tipoAmbiente = tipoAmbiente;
	}
	public String getXmlLote() {
		return xmlLote;
	}
	public void setXmlLote(String xmlLote) {
		this.xmlLote = xmlLote;
	}
	public Long getCnpjCompleto() {
		return cnpjCompleto;
	}
	public void setCnpjCompleto(Long cnpjCompleto) {
		this.cnpjCompleto = cnpjCompleto;
	}
}
