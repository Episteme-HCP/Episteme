package org.jscience.ml.mathml;

import org.w3c.dom.mathml.MathMLBvarElement;


/**
 * Implements a MathML bounded variable element.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class MathMLBvarElementImpl extends MathMLContentContainerImpl
    implements MathMLBvarElement {
/**
     * Constructs a MathML bounded variable element.
     *
     * @param owner         DOCUMENT ME!
     * @param qualifiedName DOCUMENT ME!
     */
    public MathMLBvarElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
        super(owner, qualifiedName);
    }
}
