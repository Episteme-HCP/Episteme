/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.core.io;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.TransformerFactory;

import org.xml.sax.SAXException;

/**
 * Utility class for creating secure XML parsers.
 * <p>
 * All XML parsers created through this class are configured to prevent
 * XML External Entity (XXE) attacks and other XML-based vulnerabilities.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class SecureXMLFactory {

    private SecureXMLFactory() {
        // Utility class
    }

    /**
     * Creates a secure DocumentBuilderFactory with XXE protection.
     *
     * @return a securely configured DocumentBuilderFactory
     * @throws ParserConfigurationException if security features cannot be set
     */
    public static DocumentBuilderFactory createSecureDocumentBuilderFactory() 
            throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        // Disable DTDs (Document Type Definitions) entirely
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        
        // Disable external entities
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        
        // Disable external DTDs
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        
        // Prevent XInclude
        factory.setXIncludeAware(false);
        
        // Prevent entity expansion attacks
        factory.setExpandEntityReferences(false);
        
        return factory;
    }

    /**
     * Creates a secure DocumentBuilder with XXE protection.
     *
     * @return a securely configured DocumentBuilder
     * @throws ParserConfigurationException if security features cannot be set
     */
    public static DocumentBuilder createSecureDocumentBuilder() 
            throws ParserConfigurationException {
        return createSecureDocumentBuilderFactory().newDocumentBuilder();
    }

    /**
     * Creates a secure namespace-aware DocumentBuilderFactory with XXE protection.
     *
     * @return a securely configured DocumentBuilderFactory
     * @throws ParserConfigurationException if security features cannot be set
     */
    public static DocumentBuilderFactory createSecureNamespaceAwareDocumentBuilderFactory() 
            throws ParserConfigurationException {
        DocumentBuilderFactory factory = createSecureDocumentBuilderFactory();
        factory.setNamespaceAware(true);
        return factory;
    }

    /**
     * Creates a secure SAXParserFactory with XXE protection.
     *
     * @return a securely configured SAXParserFactory
     * @throws ParserConfigurationException if security features cannot be set
     * @throws SAXException if a SAX error occurs
     */
    public static SAXParserFactory createSecureSAXParserFactory() 
            throws ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        
        // Disable DTDs
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        
        // Disable external entities
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        
        // Disable external DTDs  
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        
        factory.setXIncludeAware(false);
        
        return factory;
    }

    /**
     * Creates a secure SAXParser with XXE protection.
     *
     * @return a securely configured SAXParser
     * @throws ParserConfigurationException if security features cannot be set
     * @throws SAXException if a SAX error occurs
     */
    public static SAXParser createSecureSAXParser() 
            throws ParserConfigurationException, SAXException {
        return createSecureSAXParserFactory().newSAXParser();
    }

    /**
     * Creates a secure XMLInputFactory for StAX parsing with XXE protection.
     *
     * @return a securely configured XMLInputFactory
     */
    public static XMLInputFactory createSecureXMLInputFactory() {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        
        // Disable DTD processing
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        
        // Disable external entities
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        
        return factory;
    }

    /**
     * Creates a secure TransformerFactory with XXE protection.
     *
     * @return a securely configured TransformerFactory
     */
    public static TransformerFactory createSecureTransformerFactory() {
        TransformerFactory factory = TransformerFactory.newInstance();
        
        try {
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        } catch (IllegalArgumentException e) {
            // Some implementations may not support these attributes
        }
        
        return factory;
    }
}

