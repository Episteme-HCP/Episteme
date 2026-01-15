package org.jscience.util;

import java.io.Serializable;


/**
 * The interface JavaExpressible indicates that the implementing class is able
 * to express itself in Java format.
 *
 * @author Carsten Knudsen
 * @author Martin Egholm Nielsen
 * @version 1.0
 */
public interface JavaExpressible extends Serializable {
    /**
     * The method should return a Java version of the object.
     *
     * @return DOCUMENT ME!
     */
    public abstract String toJava();
} // JavaExpressible
