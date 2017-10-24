package br.com.acaosistemas.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.acaosistemas.db.connection.ConnectionFactory;
import br.com.acaosistemas.db.model.UBILotesEsocialLog;
import br.com.acaosistemas.main.Versao;

public class UBILotesEsocialLogDAO {

	private Connection conn;
	
	public UBILotesEsocialLogDAO() {
		conn = new ConnectionFactory().getConnection();
	}
	
	public void closeConnection () {
		try {
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void insert(UBILotesEsocialLog pUbllRow) {
		PreparedStatement stmt = null;
		
		try {
			stmt = conn.prepareStatement(
					"INSERT INTO ubi_lotes_esocial_log (dt_mov,num_erro,mensagem,uble_ubi_lote_numero,status) VALUES (?,?,?,?,?)");

			stmt.setTimestamp(1, pUbllRow.getDtMov());
			stmt.setLong(2, pUbllRow.getNumErro());
			stmt.setString(3, Versao.getStringVersao() + "\n" + pUbllRow.getMensagem());
			stmt.setLong(4, pUbllRow.getUbleUbiLoteNumero());
			stmt.setInt(5, pUbllRow.getStatus().getId());

			stmt.execute();
			stmt.close();

			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
