package br.com.acaosistemas.db.dao;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.acaosistemas.db.connection.ConnectionFactory;
import br.com.acaosistemas.db.enumeration.LotesTipoAmbienteEnum;
import br.com.acaosistemas.db.enumeration.StatusLotesEventosEnum;
import br.com.acaosistemas.db.model.UBILotesEsocial;


public class UBILotesEsocialDAO {

	private Connection      conn;
	private UBILotesEsocial uble;
	
	public UBILotesEsocialDAO() {
		conn = new ConnectionFactory().getConnection();
	}
	
//	public void closeConnection () {
//		try {
//			conn.close();
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}
	
	public UBILotesEsocial getUBILotesEsocial(String pRowID ) {
		uble = new UBILotesEsocial();
		
		PreparedStatement stmt = null;
		
		try {
			stmt = conn.prepareStatement(
					  "SELECT "
					+ "   uble.ubi_lote_numero,"
					+ "   uble.ubca_cnpj,"
					+ "   ubca.cnpj_completo "
					+ "   uble.status,"
					+ "   uble.tipo_ambiente,"
					+ "   uble.xml"
					+ "FROM "
					+ "   ubi_lotes_esocial uble, "
					+ "   ubi_cnpj_autorizados ubca "
					+ "WHERE "
					+ "   uble.rowid = ? and "
					+ "   ubca.cnpj  = uble.ubca_cnpj");
			
			stmt.setString(1, pRowID);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				uble.setUbiLoteNumero(rs.getLong("ubi_lote_numero"));
				uble.setUbcaCnpj(rs.getLong("ubca_cnpj"));
				uble.setCnpjCompleto(rs.getString("cnpj_completo"));
				uble.setStatus(StatusLotesEventosEnum.getById(rs.getInt("status")));
				uble.setTipoAmbiente(LotesTipoAmbienteEnum.getById(rs.getInt("tipo_ambiente")));
				uble.setXmlLote(rs.getNString("xml"));
				uble.setRowId(pRowID);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return uble;
	}
	
	public List<UBILotesEsocial> listUBILotesEsocial(StatusLotesEventosEnum pStatus) {
		PreparedStatement stmt             = null;
		List<UBILotesEsocial> listaUBILotesEsocial = new ArrayList<UBILotesEsocial>();
		
		try {
			stmt = conn.prepareStatement(
					  "SELECT "
					+ "   uble.ubi_lote_numero,"
					+ "   ubca_cnpj,"
					+ "   ubca.cnpj_completo,"
					+ "   uble.status,"
					+ "   uble.tipo_ambiente,"
					+ "   uble.xml,"
					+ "   uble.rowid "
					+ "FROM "
					+ "   ubi_lotes_esocial uble,"
					+ "   ubi_cnpj_autorizados ubca "
					+ "WHERE uble.status = ? and ubca.cnpj = uble.ubca_cnpj");
			
			stmt.setInt(1, pStatus.getId());
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				UBILotesEsocial uble = new UBILotesEsocial();
				
				uble.setUbiLoteNumero(rs.getLong("ubi_lote_numero"));
				uble.setUbcaCnpj(rs.getLong("ubca_cnpj"));
				uble.setCnpjCompleto(rs.getString("cnpj_completo"));
				uble.setStatus(StatusLotesEventosEnum.getById(rs.getInt("status")));
				uble.setTipoAmbiente(LotesTipoAmbienteEnum.getById(rs.getInt("tipo_ambiente")));
				uble.setXmlLote(rs.getNString("xml"));
				uble.setRowId(rs.getString("rowId"));
				
				listaUBILotesEsocial.add(uble);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}  finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return listaUBILotesEsocial;
	}
	
	public void updateStatus(UBILotesEsocial pUbleRow) {
		PreparedStatement stmt = null;
		
		try {
			stmt = conn.prepareStatement(
					  "UPDATE "
					+ "   ubi_lotes_esocial uble "
					+ "SET "
					+ "   uble.status = ? "
					+ "WHERE "
					+ "   uble.rowid = ?");
		
			stmt.setInt(1, pUbleRow.getStatus().getId());
			stmt.setString(2, pUbleRow.getRowId());
			
			stmt.execute();
			stmt.close();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}  finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateXmlRetornoLote(UBILotesEsocial pUbleRow) {
		PreparedStatement stmt = null;
		StringReader readerXmlRetornoLote = new StringReader(pUbleRow.getXmlRetornoLote());
		
		try {
			stmt = conn.prepareStatement(
					  "UPDATE "
					+ "   ubi_lotes_esocial uble "
					+ "SET "
					+ "   uble.xml_retorno_lote = ? "
					+ "WHERE "
					+ "   uble.rowid = ?");
		
			stmt.setCharacterStream(1, readerXmlRetornoLote, pUbleRow.getXmlRetornoLote().length());
			stmt.setString(2, pUbleRow.getRowId());
			
			stmt.executeUpdate();
			stmt.close();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}  finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
