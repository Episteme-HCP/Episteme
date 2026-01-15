package org.jscience.ml.cml.dom.pmr;

import org.w3c.dom.CDATASection;


/**
 * 
 */
public class PMRCDATASectionImpl extends PMRTextImpl implements CDATASection {
/**
     * Creates a new PMRCDATASectionImpl object.
     */
    public PMRCDATASectionImpl() {
        super();
    }

/**
     * Creates a new PMRCDATASectionImpl object.
     *
     * @param dt  DOCUMENT ME!
     * @param doc DOCUMENT ME!
     */
    public PMRCDATASectionImpl(CDATASection dt, PMRDocument doc) {
        super(dt, doc);
    }
}
