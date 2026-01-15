package org.jscience.ml.cml.dom.pmr;

import org.w3c.dom.DocumentFragment;


/**
 * 
 */
public class PMRDocumentFragmentImpl extends PMRNodeImpl
    implements DocumentFragment {
/**
     * Creates a new PMRDocumentFragmentImpl object.
     */
    public PMRDocumentFragmentImpl() {
        super();
    }

/**
     * Creates a new PMRDocumentFragmentImpl object.
     *
     * @param dt  DOCUMENT ME!
     * @param doc DOCUMENT ME!
     */
    public PMRDocumentFragmentImpl(DocumentFragment dt, PMRDocument doc) {
        super(dt, doc);
    }
}
