package br.com.acaosistemas.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.com.acaosistemas.db.dao.UBILotesEsocialDAO;
import br.com.acaosistemas.db.dao.UBILotesEsocialLogDAO;
import br.com.acaosistemas.db.enumeration.StatusLotesEventosEnum;
import br.com.acaosistemas.db.model.UBILotesEsocial;
import br.com.acaosistemas.db.model.UBILotesEsocialLog;
import br.com.acaosistemas.frw.util.ExceptionUtils;
import br.com.acaosistemas.wsclientes.ClienteWSConsultarLote;
import br.com.acaosistemas.wsclientes.ClienteWSEnviarLote;

/**
 * 
 * @author Anderson Bestteti Santos
 *
 * Classe para processar os lotes de eventos do eSocial que estao prontos
 * para serem enviados ou consultados no eSocial.
 * 
 */
public class ProcessarLotesEventos {

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
				
		System.out.println("   Processando registros da UBI_LOTES_ESOCIAL[Envio]...");
		
		for (UBILotesEsocial ubleRow : listaUbiLoteEventos) {
			
			System.out.println("     Processando rowId: "+ubleRow.getRowId());
				
			try {
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
				ubll.setDtMov(new Timestamp(System.currentTimeMillis()));
				ubll.setMensagem(StatusLotesEventosEnum.ENVIADO_COM_SUCESSO.getDescricao());
				ubll.setStatus(StatusLotesEventosEnum.ENVIADO_COM_SUCESSO);
				ubll.setNumErro(0L);
				
				UBILotesEsocialLogDAO ubllDAO = new UBILotesEsocialLogDAO();				
				ubllDAO.insert(ubll);
				ubllDAO.closeConnection();
				
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
		
		ubleDAO.closeConnection();
		System.out.println("   Finalizado processomento da UBI_LOTES_ESOCIAL[Envio].");
	}
	
	/**
	 * Processa todos os lotes que estejam com status A_CONSULTAR (501)
	 */
	public void lerLotesProntosConsulta() {
		ClienteWSConsultarLote clientWS            = new ClienteWSConsultarLote();
		UBILotesEsocialDAO     ubleDAO             = new UBILotesEsocialDAO();
		List<UBILotesEsocial>  listaUbiLoteEventos = new ArrayList<UBILotesEsocial>();
		UBILotesEsocialLog     ubll                = new UBILotesEsocialLog();
		
		listaUbiLoteEventos = ubleDAO.listUBILotesEsocial(StatusLotesEventosEnum.A_CONSULTAR);
				
		System.out.println("   Processando registros da UBI_LOTES_ESOCIAL[Consulta]...");
		
		for (UBILotesEsocial ubleRow : listaUbiLoteEventos) {
			
			System.out.println("     Processando rowId: "+ubleRow.getRowId());
				
			try {
				clientWS.execWebService(ubleRow);
				
				// Atualiza o status da tabela UBI_LOTES_ESOCIAL para
				// CONSULTADO_COM_SUCESSO (598)
				ubleRow.setStatus(StatusLotesEventosEnum.CONSULTADO_COM_SUCESSO);
				ubleDAO.updateStatus(ubleRow);
				
				// Insere no log o resultado da chamada do web service
				ubll.setUbleUbiLoteNumero(ubleRow.getUbiLoteNumero());
				ubll.setDtMov(new Timestamp(System.currentTimeMillis()));
				ubll.setMensagem(StatusLotesEventosEnum.CONSULTADO_COM_SUCESSO.getDescricao());
				ubll.setStatus(StatusLotesEventosEnum.CONSULTADO_COM_SUCESSO);
				ubll.setNumErro(0L);
				
				UBILotesEsocialLogDAO ubllDAO = new UBILotesEsocialLogDAO();				
				ubllDAO.insert(ubll);
				ubllDAO.closeConnection();
				
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
		
		ubleDAO.closeConnection();
		System.out.println("   Finalizado processomento da UBI_LOTES_ESOCIAL[Consulta].");
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
		ubll.setDtMov(new Timestamp(System.currentTimeMillis()));
		ubll.setStatus(pUbleRow.getStatus());
		ubll.setMensagem(pUbleRow.getStatus().getDescricao() +
				        "\n"                                 +
				        pException.getMessage()              +
				        "\n"                                 +
				        ExceptionUtils.stringStackTrace(pException));
		ubll.setNumErro(new Long(pUbleRow.getStatus().getId()));
		
		ubllDAO.insert(ubll);
		ubllDAO.closeConnection();		
	}
}
