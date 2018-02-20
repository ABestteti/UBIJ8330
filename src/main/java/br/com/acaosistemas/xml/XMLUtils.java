package br.com.acaosistemas.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import br.com.acaosistemas.frw.util.CnpjTransmissorException;

/**
 * Classe para fornecer rotinas utilitarias para tratamento de XML.
 * 
 * @author Anderson Bestteti
 *
 */
public final class XMLUtils {
	
	/**
	 * Metodo responsavel por extrair do XML do lote de eventos o CNPJ da empresa resposavel
	 * pela transmissao do lote de eventos. A recuperacao da informacao do XML e feita atraves
	 * de uma consulta XPath.<br>
	 * Xpath: "/eSocial/envioLoteEventos/ideTransmissor/nrInsc"
	 * 
	 * Uma vez obtidia
	 * 
	 * @param pXml - conteundo XML do lote de eventos do eSocial.
	 * @param pCnpjCompleto - CNPJ completo cadastrado na tabela UBI_CNPJ_AUTORIZADOS.
	 * 
	 * @exception {@link CnpjTransmissorException} quando o CNPJ do ideTransmisso, definido
	 * no lote de eventos do eSocial, for diferente do CNPJ completo do cadastro de CNPJs
	 * autorizados.
	 *
	 */
	public static void validaCnpTransmissor(String pXml, String pCnpjCompleto) 
	throws CnpjTransmissorException {
		
		String cnpjIdeTransmissor = null;
		
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
		
		// Cria um objeto XPathFactory
		XPathFactory xpathFactory = XPathFactory.newInstance();

		// Cria um objeto XPath
		XPath xpath = xpathFactory.newXPath();
		
		try {
            XPathExpression expr = 
            		xpath.compile("/eSocial/envioLoteEventos/ideTransmissor/nrInsc/text()");
            
            cnpjIdeTransmissor = 
            		(String) expr.evaluate(docXmlLoteEventos, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
		
		/*
		 * Se os CNPJ do ideTransmissor for diferente do CNPJ completo do CNPJ autorizado
		 * o lote de eventos devera ir para o estados de "Desassociar Eventos" e logar 
		 * essa ocorrencia no log da tabela de lotes do eSocial.
		 */
		if (!cnpjIdeTransmissor.equals(pCnpjCompleto)) {
			throw new CnpjTransmissorException();
		}		
	}
}