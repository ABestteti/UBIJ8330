package br.com.acaosistemas.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Classe para fornecer rotinas utilitarias para tratamento de XML.
 * 
 * @author Anderson Bestteti
 *
 */
public final class XMLUtils {
	public static String getCnpTransmissor(String pXml) {
		// Construcao da representacao do XML do lote evento no formato
		// DOM para permitir buscar informacoes do documento.		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		Document docXmlLoteEventos = null;

		try {
			docBuilder = dbf.newDocumentBuilder();
			docXmlLoteEventos = docBuilder
					.parse(new ByteArrayInputStream(pXml.toString()
							.getBytes(StandardCharsets.UTF_8)));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return "";
	}
}
