/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.chemistry.loaders.cml.cmlimpl;

import org.episteme.natural.chemistry.loaders.cml.AbstractCMLDocument;
import org.episteme.natural.chemistry.loaders.cml.CMLDocumentFactory;
import org.episteme.natural.chemistry.loaders.cml.CMLException;

import org.w3c.dom.Element;

import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;

import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * manufactures CMLDocuments
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DocumentFactoryImpl implements CMLDocumentFactory {
    /** The singleton instance of the CML document factory. */
    static CMLDocumentFactory theDocumentFactory = null;

    /** The fully qualified class name for the abstract CML document implementation. */
    public final static String ABSTRACT_CMLDOCUMENT = "org.episteme.natural.chemistry.loaders.cml.cmlimpl.AbstractCMLDocumentImpl";

    /** The fully qualified class name for the default CML document implementation. */
    public final static String DEFAULT_CMLDOCUMENT = "org.episteme.natural.chemistry.loaders.cml.cmlimpl.CMLDocumentImpl";

    /** The class name used to instantiate new CML documents. */
    protected String documentClassName;

/**
     * Creates a new DocumentFactoryImpl object with default documentClassName
     */
    protected DocumentFactoryImpl() {
        this.setDocumentClassName(DEFAULT_CMLDOCUMENT);
    }

    /**
     * set the document class name
     *
     * @param documentClassName the classname with which to create new
     *        Documents
     */
    public void setDocumentClassName(String documentClassName) {
        this.documentClassName = documentClassName;
    }

    /**
     * Creates a CMLDocument instance using the current documentClassName.
     *
     * @return the newly created CML document
     */
    public AbstractCMLDocument createDocument() {
        return this.createDocument(documentClassName);
    }

    /**
     * Creates a CMLDocument instance using the specified class name.
     *
     * @param documentClassName the class name of the document to create
     *
     * @return the newly created CML document, or null if creation failed
     */
    public AbstractCMLDocument createDocument(String documentClassName) {
        AbstractCMLDocument doc = null;
        this.setDocumentClassName(documentClassName);

        try {
            doc = (AbstractCMLDocument) Class.forName(documentClassName)
                                             .getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.err.println("Cannot create: " + documentClassName + " (" +
                e + ")");
        }

        return doc;
    }

    /**
     * Creates a new DocumentFactory this creates Documents and
     * elements of the abstract documentClassName NOTE: The default class will
     * create Elements of type PMRElement, not subclassed to fit the schema.
     * For that use newInstance(String documentClassName);
     *
     * @return the CMLDocumentFactory
     */
    public static CMLDocumentFactory newAbstractInstance() {
        return DocumentFactoryImpl.newInstance(ABSTRACT_CMLDOCUMENT);
    }

    /**
     * Creates a new DocumentFactory this creates Documents of the
     * subclassed documentClassName NOTE: The default class will create
     * Elements of type CMLBaseImpl, subclassed to fit the schema. If elements
     * of type PMRElementImpl are required use newAbstractInstance();
     *
     * @return the CMLDocumentFactory
     */
    public static CMLDocumentFactory newInstance() {
        return DocumentFactoryImpl.newInstance(DEFAULT_CMLDOCUMENT);
    }

    /**
     * Creates a new CMLDocument this creates Documents of the
     * subclassed documentClassName NOTE: The default class will create
     * Elements of type CMLBaseImpl, subclassed to fit the schema. If elements
     * of type PMRElementImpl are required use newAbstractInstance();
     *
     * @return the CMLDocumentFactory
     */
    public static AbstractCMLDocument createNewDocument() {
        return DocumentFactoryImpl.newInstance().createDocument();
    }

    /**
     * Creates a new DocumentFactory for the specified document class.
     *
     * @param documentClassName the class name for the factory's documents
     *
     * @return the new document factory instance
     */
    public static CMLDocumentFactory newInstance(String documentClassName) {
        /*
         *  singleton method may be obsolete
         *  if (theDocumentFactory == null) {
         *  theDocumentFactory = new DocumentFactoryImpl();
         *  }
         *  theDocumentFactory.setDocumentClassName(documentClassName);
         *  return theDocumentFactory;
         *  --
         */
        CMLDocumentFactory documentFactory = new DocumentFactoryImpl();
        documentFactory.setDocumentClassName(documentClassName);

        return documentFactory;
    }

    /**
     * Convenience method to parse an XML string into a CMLDocument.
     *
     * @param xmlString the well-formed XML string to parse
     *
     * @return the resulting CML document
     *
     * @throws CMLException if parsing fails or result is invalid
     */
    public AbstractCMLDocument parseString(String xmlString)
        throws CMLException {
        AbstractCMLDocument doc = null;

        try {
            doc = this.parseSAX(new InputSource(new StringReader(xmlString)));
        } catch (IOException ioe) {
            ;
        }

        return doc;
    }

    /**
     * Parses an XML document via SAX (whitespace is ignored).
     *
     * @param is the input source
     *
     * @return the parsed CML document
     *
     * @throws CMLException if a parsing error occurrs
     * @throws IOException  if an I/O error occurs
     */
    public AbstractCMLDocument parseSAX(InputSource is)
        throws org.episteme.natural.chemistry.loaders.cml.CMLException, IOException {
        return parseSAX(is, false);
    }

    /**
     * Parses an XML document via SAX into an existing DOM (whitespace is ignored).
     *
     * @param is  the input source
     * @param doc the target document for the parsed elements
     *
     * @return the root element of the parsed content
     *
     * @throws CMLException if a parsing error occurrs
     * @throws IOException  if an I/O error occurs
     */
    public Element parseSAX(InputSource is, AbstractCMLDocument doc)
        throws org.episteme.natural.chemistry.loaders.cml.CMLException, IOException {
        return parseSAX(is, doc, false);
    }

    /**
     * Parses an XML document via SAX with optional debugging (whitespace is ignored).
     *
     * @param is    the input source
     * @param debug true to enable debug output
     *
     * @return the parsed CML document
     *
     * @throws CMLException if a parsing error occurrs
     * @throws IOException  if an I/O error occurs
     */
    public AbstractCMLDocument parseSAX(InputSource is, boolean debug)
        throws org.episteme.natural.chemistry.loaders.cml.CMLException, IOException {
        if (is == null) {
            throw new IOException("Null input source");
        }

        AbstractCMLDocument cmlDoc = DocumentFactoryImpl.newInstance(documentClassName)
                                                        .createDocument();
        SAXParser parser = null;

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();

            // this seems to be essential if we use SAX2 startElement
            spf.setNamespaceAware(true);
            spf.setValidating(false);
            parser = spf.newSAXParser();
        } catch (Exception e) {
            System.err.println("SAX Parser bug: " + e);
            e.printStackTrace();
        }

        SaxHandlerImpl handler = new SaxHandlerImpl(cmlDoc, debug);
        handler.setIgnoreWhite(true);

        try {
            if (parser != null) {
                parser.parse(is, handler);
            }

            if (handler.hasErrors()) {
                Vector<String> v = handler.getErrorVector();

                for (int i = 0; i < v.size(); i++) {
                    System.err.println("ParseError: " + v.elementAt(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new org.episteme.natural.chemistry.loaders.cml.CMLException("" + e);
        }

        // make sure all content and attribute delegates are set
        cmlDoc.updateDelegates();

        return cmlDoc;
    }

    // /*
    /**
     * Description of the Method whitespace is ignored
     *
     * @param is input
     * @param doc document to which element will belong
     * @param debug if true, debug
     *
     * @return the new root element
     *
     * @throws IOException
     * @throws . any bad CML or XML
     */
    public Element parseSAX(InputSource is, AbstractCMLDocument doc,
        boolean debug) throws org.episteme.natural.chemistry.loaders.cml.CMLException, IOException {
        SAXParser parser = null;

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();

            // this seems to be essential if we use SAX2 startElement
            spf.setNamespaceAware(true);
            parser = spf.newSAXParser();
        } catch (Exception e) {
            System.err.println("SAX Parser bug: " + e);
            e.printStackTrace();
        }

        SaxHandlerImpl handler = new SaxHandlerImpl(doc, debug);
        handler.setIgnoreWhite(true);

        try {
            if (parser != null) {
                parser.parse(is, handler);
            }

            if (handler.hasErrors()) {
                Vector<String> v = handler.getErrorVector();

                for (int i = 0; i < v.size(); i++) {
                    System.err.println("ParseError: " + v.elementAt(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new org.episteme.natural.chemistry.loaders.cml.CMLException("" + e);
        }

        return handler.getNewRootElement();
    }

    //*/

    /*
        /**
         *  Description of the Method
         * the only method which forces whitespace to be incorporated
         *
         *@param  is                               Description of the Parameter
         *@param  doc                              Description of the Parameter
         *@param  debug                            Description of the Parameter
         *@param  ignoreWhite                      is whitespace ignored
         *@return                                  Description of the Return Value
         *@exception  org.episteme.natural.chemistry.loaders.cml.CMLException  Description of the Exception
         *@exception  IOException                  Description of the Exception
         *
        public Element parseSAX(InputSource is, AbstractCMLDocument doc, boolean debug, boolean ignoreWhite) throws org.episteme.natural.chemistry.loaders.cml.CMLException, IOException {
            SAXParser parser = null;
            try {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                // this seems to be essential if we use SAX2 startElement
                spf.setNamespaceAware(true);
                parser = spf.newSAXParser();
            } catch (Exception e) {
                System.err.println("SAX Parser bug: " + e);
                e.printStackTrace();
            }
            SaxHandlerImpl handler = new SaxHandlerImpl(doc, debug);
            handler.setIgnoreWhite(ignoreWhite);
            try {
                parser.parse(is, handler);
                if (handler.hasErrors()) {
                    Vector v = handler.getErrorVector();
                    for (int i = 0; i < v.size(); i++) {
                        System.err.println("ParseError: " + v.elementAt(i));
                    }
    //                throw new org.episteme.natural.chemistry.loaders.cml.CMLException("CMLHandler had errors");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new org.episteme.natural.chemistry.loaders.cml.CMLException("" + e);
            }
            return handler.getNewRootElement();
        }
    */
}


