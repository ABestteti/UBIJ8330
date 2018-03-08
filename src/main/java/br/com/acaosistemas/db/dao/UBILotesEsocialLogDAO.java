package br.com.acaosistemas.db.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.acaosistemas.db.connection.ConnectionFactory;
import br.com.acaosistemas.db.model.UBILotesEsocialLog;
import br.com.acaosistemas.main.Versao;
import oracle.jdbc.OracleTypes;

/**
 * DAO para manipulacao da tabela UBI_LOTES_ESOCIAL_LOG
 * <p>
 * <b>Empresa:</b> Acao Sistemas de Informatica Ltda.
 * <p>
 * Alterações:
 * <p>
 * 2018.03.07 - ABS - Alteração da PK da tabela UBI_LOTES_ESOCIAL_LOG, 
 *                    conforme SA 20330.
 *                  - Adicionado sistema de log com a biblioteca log4j2.
 *                  
 * @author Anderson Bestteti Santos
 *
 */
public class UBILotesEsocialLogDAO {

	private static final Logger logger = LogManager.getLogger(UBILotesEsocialLogDAO.class);
	
	private Connection conn;
	
	public UBILotesEsocialLogDAO() {
		conn = new ConnectionFactory().getConnection();
	}
	
	public void insert(UBILotesEsocialLog pUbllRow) {

		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(
					  "INSERT INTO ubi_lotes_esocial_log "
					+ "  (seq_reg,"
					+ "   dt_mov,"
					+ "   num_erro,"
					+ "   mensagem,"
					+ "   uble_ubi_lote_numero,"
					+ "   status) "
					+ "VALUES "
					+ "  (?,"
					+ "   ?,"
					+ "   ?,"
					+ "   ?,"
					+ "   ?,"
					+ "   ?)");

			stmt.setLong(1, getNextSeqReg());
			stmt.setDate(2, pUbllRow.getDtMov());
			stmt.setLong(3, pUbllRow.getNumErro());
			stmt.setString(4, Versao.getStringVersao() + "\n" + pUbllRow.getMensagem());
			stmt.setLong(5, pUbllRow.getUbleUbiLoteNumero());
			stmt.setInt(6, pUbllRow.getStatus().getId());

			stmt.execute();
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}
	
	/***
	 * Retorna a sequencia gerada pelo package de banco ubip8100.gera_seq_chave.
	 * @return Um numero que representa a sequencia gerada pela funcao de banco.
	 * 
	 */
	private Long getNextSeqReg() {
		Long              nextVal = 0L;
		CallableStatement    stmt = null;

		try {
			// Executa a funcao gera_seq_chave do package ubip8100. 
			stmt = conn.prepareCall("{? = call ubip8100.gera_seq_chave}");
			
			// Define que o tipo de retorno da funcao sera um NUMBER
			stmt.registerOutParameter(1, OracleTypes.NUMBER);
			stmt.execute();
			
			nextVal = stmt.getLong(1);
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error(e);
			}			
		}
		return nextVal;
	}
}