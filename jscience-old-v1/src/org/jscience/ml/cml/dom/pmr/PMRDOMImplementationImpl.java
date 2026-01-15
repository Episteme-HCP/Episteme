package org.jscience.ml.cml.dom.pmr;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;


/**
 * 
 */
public class PMRDOMImplementationImpl extends PMRNodeImpl
    implements DOMImplementation {
    /** DOCUMENT ME! */
    protected DOMImplementation domImplementation;

/**
     * Creates a new PMRDOMImplementationImpl object.
     */
    protected PMRDOMImplementationImpl() {
        super();
    }

/**
     * Creates a new PMRDOMImplementationImpl object.
     *
     * @param di DOCUMENT ME!
     */
    protected PMRDOMImplementationImpl(DOMImplementation di) {
        this.domImplementation = di;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     * @param t DOCUMENT ME!
     * @param dt DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Document createDocument(String s, String t, DocumentType dt) {
        Document doc = domImplementation.createDocument(s, t, dt);

        return new PMRDocumentImpl(doc);
    }

    /**
     * DOCUMENT ME!
     *
     * @param feature DOCUMENT ME!
     * @param version DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasFeature(String feature, String version) {
        return domImplementation.hasFeature(feature, version);
    }

    /**
     * DOCUMENT ME!
     *
     * @param feature DOCUMENT ME!
     * @param version DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getFeature(String feature, String version) {
        return domImplementation.getFeature(feature, version);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     * @param t DOCUMENT ME!
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DocumentType createDocumentType(String s, String t, String v) {
        DocumentType dt = domImplementation.createDocumentType(s, t, v);
        Document doc = domImplementation.createDocument(s, t, dt);

        return new PMRDocumentTypeImpl(dt, new PMRDocumentImpl(doc));
    }
}
