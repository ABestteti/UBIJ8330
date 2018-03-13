package br.com.acaosistemas.frw.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;

/**
 * Classe de suporte ao reset do pipe de comunica��o do banco de dados.
 * Ela pode ser utilizada para reinicializar o pipe de banco, descartando,
 * dessa forma, todas as mensagens que estejam enfileiradas no canal de 
 * comunica��o criado no banco.
 * <p>
 * <b>Empresa:</b> Acao Sistemas de Informatica Ltda.
 * <p>
 * Altera��es:
 * <p>
 * 2018.03.13 - ABS - Implementado JavaDoc.
 *
 * @author Anderson Bestteti
 *
 */
public final class ResetPipe {

    public static void reset(Connection pConn, String pPipeName) {
    	CallableStatement stmt;
    	// Prepara a chamada da funcao no banco de dados
		try {
			stmt = pConn.prepareCall("{? = call dbms_pipe.remove_pipe(?)}");

			// Define que o tipo de retorno da funcao sera um NUMBER
			stmt.registerOutParameter(1, OracleTypes.NUMBER);

			// Define o nome do pipe que sera lido do banco.
			stmt.setString(2, pPipeName);

			// Executa a funcao do banco
			stmt.execute();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}   	
    }
}
