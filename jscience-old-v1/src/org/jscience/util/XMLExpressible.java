package org.jscience.util;

import java.io.Serializable;


/**
 * The interface XMLExpressible indicates that the implementing class is able
 * to express itself in valid XML format.
 *
 * @author Carsten Knudsen
 * @author Martin Egholm Nielsen
 * @version 1.0
 */
public interface XMLExpressible extends Serializable {
    /**
     * The method should return a String containing a description of
     * the object in valid XML.
     *
     * @return DOCUMENT ME!
     */
    public abstract String toXML();
} // XMLExpressible
