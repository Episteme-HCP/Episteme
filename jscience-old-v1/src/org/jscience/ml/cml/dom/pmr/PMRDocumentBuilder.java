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

package org.jscience.ml.cml.dom.pmr;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class PMRDocumentBuilder extends DocumentBuilder {
    /** DOCUMENT ME! */
    protected DocumentBuilder documentBuilder;

/**
     * no-arg
     */
    protected PMRDocumentBuilder() {
    }

/**
     * the default DocumentBuilder
     *
     * @param documentBuilder DOCUMENT ME!
     */
    public PMRDocumentBuilder(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

/**
     * default unimplemented
     *
     * @param className DOCUMENT ME!
     */
    public PMRDocumentBuilder(String className) {
    }

    /**
     * Specify the EntityResolver to be used to resolve entities
     * present in the XML document to be parsed. Setting this to null will
     * result in the underlying implementation using it's own default
     * implementation and behavior. Parameters: er - The EntityResolver to be
     * used to resolve entities present in the XML document to be parsed.
     *
     * @param er DOCUMENT ME!
     */
    public void setEntityResolver(EntityResolver er) {
        documentBuilder.setEntityResolver(er);
    }

    /**
     * Specify the ErrorHandler to be used to report errors present in
     * the XML document to be parsed. Setting this to null will result in the
     * underlying implementation using it's own default implementation and
     * behavior. Parameters: eh - The ErrorHandler to be used to report errors
     * present in the XML document to be parsed.
     *
     * @param eh DOCUMENT ME!
     */
    public void setErrorHandler(ErrorHandler eh) {
        documentBuilder.setErrorHandler(eh);
    }

    /**
     * Parse the content of the given InputStream as an XML document
     * and return a new DOM Document object. Parameters: is - InputStream
     * containing the content to be parsed. Throws: java.io.IOException - If
     * any IO errors occur. SAXException - If any parse errors occur.
     * java.lang.IllegalArgumentException - If the InputStream is null See
     * Also: DocumentHandler
     *
     * @param is DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    public Document parse(java.io.InputStream is)
        throws SAXException, java.io.IOException {
        return new PMRDocumentImpl(documentBuilder.parse(is));
    }

    /**
     * Parse the content of the given InputStream as an XML document
     * and return a new DOM Document object. Parameters: is - InputStream
     * containing the content to be parsed. systemId - Provide a base for
     * resolving relative URIs. Returns: A new DOM Document object. Throws:
     * java.io.IOException - If any IO errors occur. SAXException - If any
     * parse errors occur. java.lang.IllegalArgumentException - If the
     * InputStream is null. See Also: DocumentHandler
     *
     * @param is DOCUMENT ME!
     * @param systemId DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    public Document parse(java.io.InputStream is, java.lang.String systemId)
        throws SAXException, java.io.IOException {
        return new PMRDocumentImpl(documentBuilder.parse(is, systemId));
    }

    /**
     * Parse the content of the given URI as an XML document and return
     * a new DOM Document object. Parameters: uri - The location of the
     * content to be parsed. Returns: A new DOM Document object. Throws:
     * java.io.IOException - If any IO errors occur. SAXException - If any
     * parse errors occur. java.lang.IllegalArgumentException - If the URI is
     * null. See Also: DocumentHandler
     *
     * @param uri DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    public Document parse(java.lang.String uri)
        throws SAXException, java.io.IOException {
        return new PMRDocumentImpl(documentBuilder.parse(uri));
    }

    /**
     * Parse the content of the given file as an XML document and
     * return a new DOM Document object. Parameters: f - The file containing
     * the XML to parse. Returns: A new DOM Document object. Throws:
     * java.io.IOException - If any IO errors occur. SAXException - If any
     * parse errors occur. java.lang.IllegalArgumentException - If the file is
     * null. See Also: DocumentHandler
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    public Document parse(java.io.File f)
        throws SAXException, java.io.IOException {
        return new PMRDocumentImpl(documentBuilder.parse(f));
    }

    /**
     * Parse the content of the given input source as an XML document
     * and return a new DOM Document object. Parameters: is - InputSource
     * containing the content to be parsed. Returns: A new DOM Document
     * object. Throws: java.io.IOException - If any IO errors occur.
     * SAXException - If any parse errors occur.
     * java.lang.IllegalArgumentException - If the InputSource is null. See
     * Also: DocumentHandler
     *
     * @param is DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    public Document parse(InputSource is)
        throws SAXException, java.io.IOException {
        Document doc = documentBuilder.parse(is);

        return new PMRDocumentImpl(doc);
    }

    /**
     * Indicates whether or not this parser is configured to understand
     * namespaces. Returns: true if this parser is configured to understand
     * namespaces; false otherwise.
     *
     * @return DOCUMENT ME!
     */
    public boolean isNamespaceAware() {
        return documentBuilder.isNamespaceAware();
    }

    /**
     * Indicates whether or not this parser is configured to validate
     * XML documents. Returns: true if this parser is configured to validate
     * XML documents; false otherwise.
     *
     * @return DOCUMENT ME!
     */
    public boolean isValidating() {
        return documentBuilder.isValidating();
    }

    /**
     * Obtain a new instance of a DOM PMRDocument object to build a DOM
     * tree with. An alternative way to create a DOM Document object is to use
     * the getDOMImplementation method to get a DOM Level 2 DOMImplementation
     * object and then use DOM Level 2 methods on that object to create a DOM
     * Document object. Returns: A new instance of a DOM PMRDocument object.
     *
     * @return DOCUMENT ME!
     */
    public Document newDocument() {
        Document doc = documentBuilder.newDocument();
        PMRDocumentImpl pmrDoc = new PMRDocumentImpl(doc);
        pmrDoc.delegateNode = doc;

        return pmrDoc;
    }

    /**
     * Obtain an instance of a PMRDOMImplementation object.
     *
     * @return A new instance of a PMRDOMImplementation.
     */
    public DOMImplementation getDOMImplementation() {
        return new PMRDOMImplementationImpl(documentBuilder.getDOMImplementation());
    }
}
