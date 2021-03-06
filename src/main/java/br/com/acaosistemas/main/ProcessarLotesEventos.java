package br.com.acaosistemas.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.acaosistemas.db.dao.UBILotesEsocialDAO;
import br.com.acaosistemas.db.dao.UBILotesEsocialLogDAO;
import br.com.acaosistemas.db.enumeration.StatusLotesEventosEnum;
import br.com.acaosistemas.db.model.UBILotesEsocial;
import br.com.acaosistemas.db.model.UBILotesEsocialLog;
import br.com.acaosistemas.frw.util.CnpjTransmissorException;
import br.com.acaosistemas.frw.util.ExceptionUtils;
import br.com.acaosistemas.wsclientes.ClienteWSConsultarLote;
import br.com.acaosistemas.wsclientes.ClienteWSEnviarLote;
import br.com.acaosistemas.xml.XMLUtils;

/**
 * Classe para processar os lotes de eventos do eSocial que estao prontos
 * para serem enviados ou consultados no eSocial.
 * <p>
 * <b>Empresa:</b> Acao Sistemas de Informatica Ltda.
 * <p>
 * Alterações:
 * <p>
 * 2018.03.13 - ABS - Adicionado sistema de log com a biblioteca log4j2.
 *                  - Implementado JavaDoc.
 *
 * @author Anderson Bestteti Santos
 *
 */
public class ProcessarLotesEventos {

	private static final Logger logger = LogManager.getLogger(ProcessarLotesEventos.class);
	
	public ProcessarLotesEventos() {
	}

	/**
	 * Processa todos os lotes que estejam com status A_ENVIAR (201)
	 */
	public void lerLotesProntosEnvio() {
		ClienteWSEnviarLote   clientWS            = new ClienteWSEnviarLote();
		UBILotesEsocialDAO    ubleDAO             = new UBILotesEsocialDAO();
		List<UBILotesEsocial> listaUbiLoteEventos = new ArrayList<UBILotesEsocial>();
		UBILotesEsocialLog    ubll                = new UBILotesEsocialLog();
		String                xmlRetornoLote;
		
		listaUbiLoteEventos = ubleDAO.listUBILotesEsocial(StatusLotesEventosEnum.A_ENVIAR);
				
		logger.info("   Processando registros da UBI_LOTES_ESOCIAL[Envio]...");
		
		for (UBILotesEsocial ubleRow : listaUbiLoteEventos) {
			
			logger.info("     Processando rowId: "+ubleRow.getRowId());
			logger.info("     Numero do lote...: "+ubleRow.getUbiLoteNumero());
				
			try {
				
				// Valida a tag <nrInsc> do grupo <ideTransmissor> do lote de
				// eventos do eSocial. Caso a validacao falhar, ous seja, o CNPJ do
				// ideTransmissor seja diferente do CNPJ completo, a excecao 
				// CnpjTransmissorException sera invocada.
				XMLUtils.validaCnpTransmissor(
						ubleRow.getXmlLote(),
						ubleRow.getCnpjCompleto());
				
				xmlRetornoLote = clientWS.execWebService(ubleRow);
				
				// Atualiza o status da tabela UBI_LOTES_ESOCIAL para
				// ENVIADO_COM_SUCESSO (298)
				ubleRow.setStatus(StatusLotesEventosEnum.ENVIADO_COM_SUCESSO);
				ubleDAO.updateStatus(ubleRow);
				
				// Atualiza a coluna UBI_LOTES_ESOCIAL.XML_RETORNO_LOTE com o
				// xml retornado pelo ws de envio de lote de eventos.
				ubleRow.setXmlRetornoLote(xmlRetornoLote);
				ubleDAO.updateXmlRetornoLote(ubleRow);
				
				// Insere no log o resultado da chamada do web service
				ubll.setUbleUbiLoteNumero(ubleRow.getUbiLoteNumero());
				ubll.setDtMov(new java.sql.Date(new java.util.Date().getTime()));
				ubll.setMensagem(StatusLotesEventosEnum.ENVIADO_COM_SUCESSO.getDescricao());
				ubll.setStatus(StatusLotesEventosEnum.ENVIADO_COM_SUCESSO);
				ubll.setNumErro(0L);
				
				UBILotesEsocialLogDAO ubllDAO = new UBILotesEsocialLogDAO();				
				ubllDAO.insert(ubll);
			
			} catch (CnpjTransmissorException e) {
				// O CNPJ do ideTransmissor diverge do CNPJ completo do cadastro de CNPJ
				// autorizado. Sendo assim, o lote criado devera ser descartado para criar
				// um novo lote com o ideTransmissor correto.
				ubleRow.setStatus(StatusLotesEventosEnum.DESASSOCIACAO_A_DESASSOCIAR);
				gravaExcecaoLog(ubleRow, e);
			} catch (MalformedURLException e) {
				// Caso a chamada do web service do correio retornar a excecao
				// MalformedURLException, faz a atualizacao do status com o
		        // valor apropriado.
				ubleRow.setStatus(StatusLotesEventosEnum.ERRO_ENVIO_IRRECUPERAVEL);
				gravaExcecaoLog(ubleRow, e);
			} catch (SocketTimeoutException e) {
				// Caso a chamada do web service do correio retornar a excecao
				// IOException, faz a atualizacao do status com o
		        // valor apropriado
				ubleRow.setStatus(StatusLotesEventosEnum.ERRO_ENVIO_IRRECUPERAVEL);
				gravaExcecaoLog(ubleRow, e);
			} catch (IOException e) {
				// Caso a chamada do web service do correio retornar a excecao
				// IOException, faz a atualizacao do status com o
		        // valor apropriado
				ubleRow.setStatus(StatusLotesEventosEnum.ERRO_ENVIO_IRRECUPERAVEL);
				gravaExcecaoLog(ubleRow, e);
			}
		}
		
		logger.info("   Finalizado processomento da UBI_LOTES_ESOCIAL[Envio].");
	}
	
	/**
	 * Processa todos os lotes que estejam com status A_CONSULTAR (501) 
	 * {@link StatusLotesEventosEnum}
	 */
	public void lerLotesProntosConsulta() {
		ClienteWSConsultarLote clientWS            = new ClienteWSConsultarLote();
		UBILotesEsocialDAO     ubleDAO             = new UBILotesEsocialDAO();
		List<UBILotesEsocial>  listaUbiLoteEventos = new ArrayList<UBILotesEsocial>();
		UBILotesEsocialLog     ubll                = new UBILotesEsocialLog();
		
		listaUbiLoteEventos = ubleDAO.listUBILotesEsocial(StatusLotesEventosEnum.A_CONSULTAR);
				
		logger.info("   Processando registros da UBI_LOTES_ESOCIAL[Consulta]...");
		
		for (UBILotesEsocial ubleRow : listaUbiLoteEventos) {
			
			logger.info("     Processando rowId: "+ubleRow.getRowId());
			logger.info("     Numero do lote...: "+ubleRow.getUbiLoteNumero());
				
			try {
				clientWS.execWebService(ubleRow);
				
				// Atualiza o status da tabela UBI_LOTES_ESOCIAL para
				// CONSULTADO_COM_SUCESSO (598)
				ubleRow.setStatus(StatusLotesEventosEnum.CONSULTADO_COM_SUCESSO);
				ubleDAO.updateStatus(ubleRow);
				
				// Insere no log o resultado da chamada do web service
				ubll.setUbleUbiLoteNumero(ubleRow.getUbiLoteNumero());
				ubll.setDtMov(new java.sql.Date(new java.util.Date().getTime()));
				ubll.setMensagem(StatusLotesEventosEnum.CONSULTADO_COM_SUCESSO.getDescricao());
				ubll.setStatus(StatusLotesEventosEnum.CONSULTADO_COM_SUCESSO);
				ubll.setNumErro(0L);
				
				UBILotesEsocialLogDAO ubllDAO = new UBILotesEsocialLogDAO();				
				ubllDAO.insert(ubll);
				
			} catch (MalformedURLException e) {
				// Caso a chamada do web service do correio retornar a excecao
				// MalformedURLException, faz a atualizacao do status com o
		        // valor apropriado.
				ubleRow.setStatus(StatusLotesEventosEnum.ERRO_CONSULTA_IRRECUPERAVEL);
				gravaExcecaoLog(ubleRow, e);
			} catch (SocketTimeoutException e) {
				// Caso a chamada do web service do correio retornar a excecao
				// IOException, faz a atualizacao do status com o
		        // valor apropriado
				ubleRow.setStatus(StatusLotesEventosEnum.ERRO_CONSULTA_IRRECUPERAVEL);
				gravaExcecaoLog(ubleRow, e);
			} catch (IOException e) {
				// Caso a chamada do web service do correio retornar a excecao
				// IOException, faz a atualizacao do status com o
		        // valor apropriado
				ubleRow.setStatus(StatusLotesEventosEnum.ERRO_CONSULTA_IRRECUPERAVEL);
				gravaExcecaoLog(ubleRow, e);
			}
		}
		
		logger.info("   Finalizado processomento da UBI_LOTES_ESOCIAL[Consulta].");
	}
	
	/**
	 * Metodo responsavel por gravar as mensagens de excecao decorrentes da execucao dos
	 * servicos web de envio e consulta do lote de eventos no eSocial.
	 * 
	 * @param pUbleRow
	 * Representa uma linha da tabela UBI_LOTES_ESOCIAL.
	 * @param pException
	 * Objeto com a excecao gerada na execucao do web service.
	 */
	private void gravaExcecaoLog(UBILotesEsocial pUbleRow, Exception pException) {
		UBILotesEsocialDAO ubleDAO = new UBILotesEsocialDAO();
		
		ubleDAO.updateStatus(pUbleRow);
		
		// Grava na tabela UBI_LOTES_ESOCIAL_LOG a string com a mensagem de
		// erro completa				
		UBILotesEsocialLogDAO ubllDAO = new UBILotesEsocialLogDAO();
		UBILotesEsocialLog    ubll    = new UBILotesEsocialLog();
		
		ubll.setUbleUbiLoteNumero(pUbleRow.getUbiLoteNumero());
		ubll.setDtMov(new java.sql.Date(new java.util.Date().getTime()));
		ubll.setStatus(pUbleRow.getStatus());
		ubll.setMensagem(pUbleRow.getStatus().getDescricao() +
				        "\n"                                 +
				        pException.getMessage()              +
				        "\n"                                 +
				        ExceptionUtils.stringStackTrace(pException));
		ubll.setNumErro(new Long(pUbleRow.getStatus().getId()));
		
		ubllDAO.insert(ubll);
	}
}
