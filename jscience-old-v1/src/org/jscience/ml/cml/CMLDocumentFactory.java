package org.jscience.ml.cml;

import org.w3c.dom.Element;

import org.xml.sax.InputSource;

import java.io.IOException;


/**
 * Creates DocumentFactory of appropriate subclass
 *
 * @author P.Murray-Rust, 2001
 */
public interface CMLDocumentFactory {
    /**
     * set the document class name
     *
     * @param documentClassName the classname with which to create new
     *        Documents
     */
    void setDocumentClassName(String documentClassName);

    /**
     * create document with current documentClassName
     *
     * @return AbstractCMLDocument the document
     */
    AbstractCMLDocument createDocument();

    /**
     * convenience method; parses a string representing a well-formed
     * XML document
     *
     * @param xmlString the string
     *
     * @return Element
     */
    AbstractCMLDocument parseString(String xmlString)
        throws org.jscience.ml.cml.CMLException;

    // restored, PMR
    /**
     * parses XML document into existing DOM The new document must be
     * well-formewd with a single root element the application will probably
     * wish to relocate this in the DOM.
     *
     * @param is the input
     * @param doc the existing document
     * @param debug
     *
     * @return Element element formed by parsing NOT attached to tree
     *
     * @throws IOException specific or IO
     */
    Element parseSAX(InputSource is, AbstractCMLDocument doc, boolean debug)
        throws IOException, org.jscience.ml.cml.CMLException;

    //*/
    // restored, PMR /*
    /**
     * parses XML document into existing DOM The new document must be
     * well-formed with a single root element the application will probably
     * wish to relocate this in the DOM. debug is set to false
     *
     * @param is - the input
     * @param doc - the existing document
     *
     * @return Element - element formed by parsing NOT attached to tree
     *
     * @throws IOException - domain or IO
     */
    Element parseSAX(InputSource is, AbstractCMLDocument doc)
        throws IOException, org.jscience.ml.cml.CMLException;

    //*/
    /**
     * parses XML document into DOM, creating a new ownerDocument
     *
     * @param is - the input
     * @param debug
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException - domain or IO
     */
    AbstractCMLDocument parseSAX(InputSource is, boolean debug)
        throws IOException, org.jscience.ml.cml.CMLException;

    /**
     * as above but debug is false
     *
     * @param is - the input
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException - domain or IO
     */
    AbstractCMLDocument parseSAX(InputSource is)
        throws IOException, org.jscience.ml.cml.CMLException;
}
