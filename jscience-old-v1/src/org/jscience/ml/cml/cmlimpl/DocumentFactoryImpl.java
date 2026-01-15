package org.jscience.ml.cml.cmlimpl;

import org.jscience.ml.cml.AbstractCMLDocument;
import org.jscience.ml.cml.CMLDocumentFactory;
import org.jscience.ml.cml.CMLException;

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
 * @author (C) P. Murray-Rust, 2000
 * @author 20 August 2003
 */
public class DocumentFactoryImpl implements CMLDocumentFactory {
    /** DOCUMENT ME! */
    static CMLDocumentFactory theDocumentFactory = null;

    /** DOCUMENT ME! */
    public final static String ABSTRACT_CMLDOCUMENT = "org.jscience.ml.cml.cmlimpl.AbstractCMLDocumentImpl";

    /** DOCUMENT ME! */
    public final static String DEFAULT_CMLDOCUMENT = "org.jscience.ml.cml.cmlimpl.CMLDocumentImpl";

    /** Description of the Field */
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
     * create a CMLDocument class of document can be set through
     * setDocumentClassName, default is DEFAULT_CMLDOCUMENT, at present
     * org.jscience.ml.cml.cmlimpl.AbstractCMLDocumentImpl
     *
     * @return Description of the Return Value
     */
    public AbstractCMLDocument createDocument() {
        return this.createDocument(documentClassName);
    }

    /**
     * create a CMLDocument class of document can be set through
     * setDocumentClassName, default is DEFAULT_CMLDOCUMENT, at present
     * org.jscience.ml.cml.cmlimpl.AbstractCMLDocumentImpl
     *
     * @param documentClassName Description of the Parameter
     *
     * @return Description of the Return Value
     */
    public AbstractCMLDocument createDocument(String documentClassName) {
        AbstractCMLDocument doc = null;
        this.setDocumentClassName(documentClassName);

        try {
            doc = (AbstractCMLDocument) Class.forName(documentClassName)
                                             .newInstance();
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
     * Creates a new DocumentFactory this creates Documents and
     * elements determined by the CMLDocumentImpl If elements of type
     * PMRELementImpl are required use newAbstractInstance();
     *
     * @param documentClassName Description of the Parameter
     *
     * @return Description of the Return Value
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
     * Convenience method to parseSAX an XML document into a
     * CMLDocument all classes are CML Objects where possible.
     *
     * @param xmlString represents a well-formed XML document
     *
     * @return CMLDocument corresponding to String
     *
     * @throws CMLException Description of the Exception
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
     * Description of the Method whitespace is ignored
     *
     * @param is Description of the Parameter
     *
     * @return Description of the Return Value
     *
     * @throws IOException Description of the Exception
     */
    public AbstractCMLDocument parseSAX(InputSource is)
        throws org.jscience.ml.cml.CMLException, IOException {
        return parseSAX(is, false);
    }

    // restored, PMR
    /**
     * Description of the Method whitespace is ignored
     *
     * @param is Description of the Parameter
     * @param doc Description of the Parameter
     *
     * @return Description of the Return Value
     *
     * @throws IOException Description of the Exception
     */
    public Element parseSAX(InputSource is, AbstractCMLDocument doc)
        throws org.jscience.ml.cml.CMLException, IOException {
        return parseSAX(is, doc, false);
    }

    /**
     * Description of the Method whitespace is ignored
     *
     * @param is Description of the Parameter
     * @param debug Description of the Parameter
     *
     * @return Description of the Return Value
     *
     * @throws IOException Description of the Exception
     * @throws . Description of the Exception
     */
    public AbstractCMLDocument parseSAX(InputSource is, boolean debug)
        throws org.jscience.ml.cml.CMLException, IOException {
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
            parser.parse(is, handler);

            if (handler.hasErrors()) {
                Vector v = handler.getErrorVector();

                for (int i = 0; i < v.size(); i++) {
                    System.err.println("ParseError: " + v.elementAt(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new org.jscience.ml.cml.CMLException("" + e);
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
        boolean debug) throws org.jscience.ml.cml.CMLException, IOException {
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
            parser.parse(is, handler);

            if (handler.hasErrors()) {
                Vector v = handler.getErrorVector();

                for (int i = 0; i < v.size(); i++) {
                    System.err.println("ParseError: " + v.elementAt(i));
                }

                //                throw new org.jscience.ml.cml.CMLException("CMLHandler had errors");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new org.jscience.ml.cml.CMLException("" + e);
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
         *@exception  org.jscience.ml.cml.CMLException  Description of the Exception
         *@exception  IOException                  Description of the Exception
         *
        public Element parseSAX(InputSource is, AbstractCMLDocument doc, boolean debug, boolean ignoreWhite) throws org.jscience.ml.cml.CMLException, IOException {
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
    //                throw new org.jscience.ml.cml.CMLException("CMLHandler had errors");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new org.jscience.ml.cml.CMLException("" + e);
            }
            return handler.getNewRootElement();
        }
    */
}
