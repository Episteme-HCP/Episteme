/**
 * Title:        NewProj
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright:    Copyright (c) imt
 * </p>
 *
 * <p>
 * Company:      imt
 * </p>
 *
 * <p></p>
 */
package org.jscience.physics.waves.optics.materials;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class InvalidMethodTypeException extends Exception {
    /** DOCUMENT ME! */
    public String name = "";

/**
     * Creates a new InvalidMethodTypeException object.
     */
    public InvalidMethodTypeException() {
    }

/**
     * Creates a new InvalidMethodTypeException object.
     *
     * @param s DOCUMENT ME!
     */
    public InvalidMethodTypeException(String s) {
        name = new String(s);
    }
}
