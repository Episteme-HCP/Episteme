package org.jscience.util;

/**
 * The interface HTMLExpressible indicates that the implementing class is able
 * to express itself in HTML format.
 *
 * @author Carsten Knudsen
 * @author Martin Egholm Nielsen
 * @version 1.0
 */
public interface HTMLExpressible extends java.io.Serializable {
    /**
     * The method should return an HTML version of the object.
     *
     * @return DOCUMENT ME!
     */
    public abstract String toHTML();
} // HTMLExpressible
