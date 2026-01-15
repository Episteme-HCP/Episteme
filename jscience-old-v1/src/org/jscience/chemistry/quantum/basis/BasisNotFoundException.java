/*
 * BasisNotFoundException.java
 *
 * Created on July 25, 2004, 11:22 AM
 */
package org.jscience.chemistry.quantum.basis;

/**
 * A runtime exception thrown is the requested basis is not found.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class BasisNotFoundException extends java.lang.RuntimeException {
/**
     * Creates a new instance of <code>BasisNotFoundException</code> without
     * detail message.
     */
    public BasisNotFoundException() {
    }

/**
     * Constructs an instance of <code>BasisNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public BasisNotFoundException(String msg) {
        super(msg);
    }
} // end of class BasisNotFoundException
