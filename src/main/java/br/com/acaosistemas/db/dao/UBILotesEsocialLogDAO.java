package br.com.acaosistemas.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

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

		final String ORA_DUP_VAL_ON_INDEX_ERROR = "ORA-00001"; // CHAVE DUPLICADA
        final int    RETRIES                    = 3;
        
		PreparedStatement stmt = null;
		
		// Laço para tratar erro ORA_DUP_VAL_ON_INDEX_ERROR. Se não ocorrer o erro na primeira iteração,
		// então o laço será interrompido. Caso, contrário serão feitas mais duas tentativas de inserção
		// com a atualização de pUbllRow.setDtMov com novo Timestamp.
		for (int tentativa = 1; tentativa <= RETRIES; tentativa++) {
			try {
				stmt = conn.prepareStatement(
						"INSERT INTO ubi_lotes_esocial_log (dt_mov,num_erro,mensagem,uble_ubi_lote_numero,status) VALUES (?,?,?,?,?)");

				stmt.setTimestamp(1, pUbllRow.getDtMov());
				stmt.setLong(2, pUbllRow.getNumErro());
				stmt.setString(3, Versao.getStringVersao() + "\n" + pUbllRow.getMensagem());
				stmt.setLong(4, pUbllRow.getUbleUbiLoteNumero());
				stmt.setInt(5, pUbllRow.getStatus().getId());

				stmt.execute();
				break; // cai fora do laço caso a inserção ocorra sem problema.

			} catch (SQLException e) {
				if (e.getMessage().contains(ORA_DUP_VAL_ON_INDEX_ERROR)) {
					if (tentativa < RETRIES) {
						try {							
							// Aguarda 250 milisegundos para atualizar o TimeStamp de
							// pUbllRow.setDtMov.
							Thread.sleep(250);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}

						// Atualiza pUbllRow.setDtMov com o novo TimeStamp para tentar nova
						// inserção na tabela UBI_LOTES_ESOCIAL_LOG.
						pUbllRow.setDtMov(new Timestamp(System.currentTimeMillis()));

					} else {
						System.out.println(
								"Lote: " + pUbllRow.getUbleUbiLoteNumero() +
								" - "    + RETRIES +
								" tentativas de inclusão do log sem êxito.");
						e.printStackTrace();
					}
				} else {
					e.printStackTrace();
				}
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}
		}
	}
}