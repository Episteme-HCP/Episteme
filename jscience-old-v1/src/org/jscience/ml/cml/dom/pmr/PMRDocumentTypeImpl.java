package org.jscience.ml.cml.dom.pmr;

import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;


/**
 * 
 */
public class PMRDocumentTypeImpl extends PMRNodeImpl implements DocumentType {
/**
     * Creates a new PMRDocumentTypeImpl object.
     */
    public PMRDocumentTypeImpl() {
        super();
    }

/**
     * Creates a new PMRDocumentTypeImpl object.
     *
     * @param dt  DOCUMENT ME!
     * @param doc DOCUMENT ME!
     */
    public PMRDocumentTypeImpl(DocumentType dt, PMRDocument doc) {
        super(dt, doc);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getInternalSubset() {
        return ((DocumentType) delegateNode).getInternalSubset();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NamedNodeMap getNotations() {
        return ((DocumentType) delegateNode).getNotations();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return ((DocumentType) delegateNode).getName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NamedNodeMap getEntities() {
        return ((DocumentType) delegateNode).getEntities();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSystemId() {
        return ((DocumentType) delegateNode).getSystemId();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPublicId() {
        return ((DocumentType) delegateNode).getPublicId();
    }
}
