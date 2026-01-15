package org.jscience.ml.cml.dom.pmr;

import org.w3c.dom.EntityReference;


/**
 * 
 */
public class PMREntityReferenceImpl extends PMRNodeImpl
    implements EntityReference {
/**
     * Creates a new PMREntityReferenceImpl object.
     */
    public PMREntityReferenceImpl() {
        super();
    }

/**
     * Creates a new PMREntityReferenceImpl object.
     *
     * @param er  DOCUMENT ME!
     * @param doc DOCUMENT ME!
     */
    public PMREntityReferenceImpl(EntityReference er, PMRDocument doc) {
        super(er, doc);
    }
}
