package br.com.acaosistemas.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.acaosistemas.db.connection.ConnectionFactory;
import br.com.acaosistemas.db.model.UBIRuntimes;

/**
 * DAO para recuperar do banco os valores dos runtimes armazenados na
 * tabela UBI_RUNTIMES.
 * <p>
 * <b>Empresa:</b> Acao Sistemas de Informatica Ltda.
 * <p>
 * Altera��es:
 * <p>
 * 2018.03.08 - ABS - Adicionado sistema de log com a biblioteca log4j2.
 * 
 * @author Anderson Bestteti Santos
 *
 */
public class UBIRuntimesDAO {

	private static final Logger logger = LogManager.getLogger(UBIRuntimesDAO.class);
	
	private Connection conn;
	private UBIRuntimes runt;
	
	public UBIRuntimesDAO() {
		conn = new ConnectionFactory().getConnection();
	}
	
	public String getRuntimeValue(final String pRuntimeID) {
		PreparedStatement stmt = null;
		
		runt = new UBIRuntimes();
		
		try {
			stmt = conn.prepareStatement(
					  "SELECT "
					+ "   ubru.valor "
					+ "FROM "
					+ "   ubi_runtimes ubru "
					+ "WHERE "
					+ "   ubru.id = ?");
			
			stmt.setString(1, pRuntimeID);

			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				runt.setValor(rs.getString("valor"));
			}
			
			rs.close();			
		} catch (SQLException e) {
			logger.error(e);
		}  finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
		
		return runt.getValor();
	}
	
	public boolean runtimeIdExists(final String pRuntimeID) {
		boolean runtimeExists = false;
		
		PreparedStatement stmt = null;
		
		runt = new UBIRuntimes();
		
		try {
			stmt = conn.prepareStatement(
					  "SELECT "
					+ "   ubru.id "
					+ "FROM "
					+ "   ubi_runtimes ubru "
					+ "WHERE "
					+ "   ubru.id = ?");
			
			stmt.setString(1, pRuntimeID);
			
			runtimeExists = stmt.executeQuery().next();
			
			stmt.close();			
		} catch (SQLException e) {
			logger.error(e);
		}  finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}

		return runtimeExists;
	}
}
