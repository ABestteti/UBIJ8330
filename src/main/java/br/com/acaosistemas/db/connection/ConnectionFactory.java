package br.com.acaosistemas.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	public Connection getConnection() {
	        try {

	            Class.forName("oracle.jdbc.driver.OracleDriver");

	        } catch (ClassNotFoundException e) {

	            System.err.println("Biblioteca JDBC da Oracle n�o encontrada.");
	            throw new RuntimeException(e);
	        }
	        
	        try {
	        	    // Define o tempo de timeout de conexao com o banco em 10 segundos
	            	DriverManager.setLoginTimeout(10);
	            	
	            	
                 // Retorna uma conex�o com o banco de dados.
	         	return DriverManager.getConnection
	         		   (
	                    "jdbc:oracle:thin:@"+DBConnectionInfo.getDbStrConnect(), 
	                    DBConnectionInfo.getDbUserName(), 
	                    DBConnectionInfo.getDbPassWord()
	                   );

	        } catch (SQLException e) {

	            System.err.println("Erro durante a conex�o com o banco de dados.");
	            System.err.println("Revise se os par�metros usu�rio, senha e string"); 
	            System.err.println("de conex�o est�o corretos.");
	            throw new RuntimeException(e);
	        }
		}
	}
