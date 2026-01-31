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

package org.jscience.natural.chemistry.loaders.cml.dom.pmr;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;


/**
 * A wrapper for {@link DocumentBuilder} that returns {@link PMRDocumentImpl} instances.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PMRDocumentBuilder extends DocumentBuilder {
    /** The underlying generic document builder. */
    protected DocumentBuilder documentBuilder;

/**
     * no-arg
     */
    protected PMRDocumentBuilder() {
    }

    /**
     * Creates a new PMRDocumentBuilder that wraps a default DocumentBuilder.
     *
     * @param documentBuilder the base document builder to wrap
     */
    public PMRDocumentBuilder(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    /**
     * Default unimplemented constructor.
     *
     * @param className the name of the builder class (currently unused)
     */
    public PMRDocumentBuilder(String className) {
    }

    /**
     * Specify the EntityResolver to be used to resolve entities
     * present in the XML document to be parsed.
     *
     * @param er the EntityResolver to be used
     */
    public void setEntityResolver(EntityResolver er) {
        documentBuilder.setEntityResolver(er);
    }

    /**
     * Specify the ErrorHandler to be used to report errors present in
     * the XML document to be parsed.
     *
     * @param eh the ErrorHandler to be used
     */
    public void setErrorHandler(ErrorHandler eh) {
        documentBuilder.setErrorHandler(eh);
    }

    /**
     * Parse the content of the given InputStream as an XML document
     * and return a new DOM Document object.
     *
     * @param is InputStream containing the content to be parsed.
     *
     * @return a new PMRDocument object.
     *
     * @throws SAXException     if any parse errors occur.
     * @throws java.io.IOException if any IO errors occur.
     */
    public Document parse(java.io.InputStream is)
        throws SAXException, java.io.IOException {
        return new PMRDocumentImpl(documentBuilder.parse(is));
    }

    /**
     * Parse the content of the given InputStream as an XML document
     * and return a new DOM Document object.
     *
     * @param is       InputStream containing the content to be parsed.
     * @param systemId base URI for resolving relative URIs.
     *
     * @return a new PMRDocument object.
     *
     * @throws SAXException     if any parse errors occur.
     * @throws java.io.IOException if any IO errors occur.
     */
    public Document parse(java.io.InputStream is, java.lang.String systemId)
        throws SAXException, java.io.IOException {
        return new PMRDocumentImpl(documentBuilder.parse(is, systemId));
    }

    /**
     * Parse the content of the given URI as an XML document and return
     * a new DOM Document object.
     *
     * @param uri the location of the content to be parsed.
     *
     * @return a new PMRDocument object.
     *
     * @throws SAXException     if any parse errors occur.
     * @throws java.io.IOException if any IO errors occur.
     */
    public Document parse(java.lang.String uri)
        throws SAXException, java.io.IOException {
        return new PMRDocumentImpl(documentBuilder.parse(uri));
    }

    /**
     * Parse the content of the given file as an XML document and
     * return a new DOM Document object.
     *
     * @param f the file containing the XML to parse.
     *
     * @return a new PMRDocument object.
     *
     * @throws SAXException     if any parse errors occur.
     * @throws java.io.IOException if any IO errors occur.
     */
    public Document parse(java.io.File f)
        throws SAXException, java.io.IOException {
        return new PMRDocumentImpl(documentBuilder.parse(f));
    }

    /**
     * Parse the content of the given input source as an XML document
     * and return a new DOM Document object.
     *
     * @param is InputSource containing the content to be parsed.
     *
     * @return a new PMRDocument object.
     *
     * @throws SAXException     if any parse errors occur.
     * @throws java.io.IOException if any IO errors occur.
     */
    public Document parse(InputSource is)
        throws SAXException, java.io.IOException {
        Document doc = documentBuilder.parse(is);

        return new PMRDocumentImpl(doc);
    }

    /**
     * Indicates whether or not this parser is configured to understand namespaces.
     *
     * @return true if namespace aware, false otherwise
     */
    public boolean isNamespaceAware() {
        return documentBuilder.isNamespaceAware();
    }

    /**
     * Indicates whether or not this parser is configured to validate XML documents.
     *
     * @return true if validating, false otherwise
     */
    public boolean isValidating() {
        return documentBuilder.isValidating();
    }

    /**
     * Obtain a new instance of a DOM {@link PMRDocument} object to build a DOM tree with.
     *
     * @return a new PMRDocument instance
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

