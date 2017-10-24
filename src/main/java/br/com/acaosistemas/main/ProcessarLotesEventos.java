package br.com.acaosistemas.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.com.acaosistemas.db.dao.UBIEsocialEventosStageDAO;
import br.com.acaosistemas.db.dao.UBIEsocialEventosStageLogDAO;
import br.com.acaosistemas.db.dao.UBILotesEsocialDAO;
import br.com.acaosistemas.db.dao.UBILotesEsocialLogDAO;
import br.com.acaosistemas.db.enumeration.StatusLotesEventosEnum;
import br.com.acaosistemas.db.model.UBIEsocialEventosStage;
import br.com.acaosistemas.db.model.UBIEsocialEventosStageLog;
import br.com.acaosistemas.db.model.UBILotesEsocial;
import br.com.acaosistemas.db.model.UBILotesEsocialLog;
import br.com.acaosistemas.frw.util.ExceptionUtils;
import br.com.acaosistemas.wsclientes.ClienteWSAssinarEvento;
import br.com.acaosistemas.wsclientes.ClienteWSEnviarLote;

public class ProcessarLotesEventos {

	public ProcessarLotesEventos() {
	}

	public void lerRegistrosNaoProcessados() {
		ClienteWSEnviarLote   clientWS            = new ClienteWSEnviarLote();
		UBILotesEsocialDAO    ubleDAO             = new UBILotesEsocialDAO();
		List<UBILotesEsocial> listaUbiLoteEventos = new ArrayList<UBILotesEsocial>();
		UBILotesEsocialLog    ubll                = new UBILotesEsocialLog();
		
		listaUbiLoteEventos = ubleDAO.listUBILotesEsocial();
				
		System.out.println("   Processando registros da UBI_LOTES_ESOCIAL...");
		
		for (UBILotesEsocial ubleRow : listaUbiLoteEventos) {
			
			System.out.println("     Processando rowId: "+ubleRow.getRowId());
				
			try {
				clientWS.execWebService(ubleRow);
				
				// Atualiza o status da tabela UBI_POBOX_XML para
				// ENVIADO_COM_SUCESSO (298)
				ubleRow.setStatus(StatusLotesEventosEnum.ENVIADO_COM_SUCESSO);
				ubleDAO.updateStatus(ubleRow);
				
				// Insere no log o resultado da chamada do web service
				ubll.setUbleUbiLoteNumero(ubleRow.getUbiLoteNumero());
				ubll.setDtMov(new Timestamp(System.currentTimeMillis()));
				ubll.setMensagem(Versao.getStringVersao() +
						         "\n"                     +
						         StatusLotesEventosEnum.ENVIADO_COM_SUCESSO.getDescricao());
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
		System.out.println("   Finalizado processomento da UBI_LOTES_ESOCIAL.");
	}
	
	private void gravaExcecaoLog(UBILotesEsocial pUbleRow, Exception pException) {
		UBILotesEsocialDAO ubleDAO = new UBILotesEsocialDAO();
		
		ubleDAO.updateStatus(pUbleRow);
		
		// Grava na tabela UBI_POBOX_XML_LOG a string com a mensagem de
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
