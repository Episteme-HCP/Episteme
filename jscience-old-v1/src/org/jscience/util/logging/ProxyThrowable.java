/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;


//import org.jscience.*;
/**
 * used to reconstruct a Throwable object while parsing XML
 *
 * @author Holger Antelmann
 */
class ProxyThrowable extends Throwable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -7520998344325789028L;

    /** DOCUMENT ME! */
    String className;

/**
     * Creates a new ProxyThrowable object.
     *
     * @param message   DOCUMENT ME!
     * @param className DOCUMENT ME!
     */
    ProxyThrowable(String message, String className) {
        super(message);
    }

    /**
     * returns the class name of the original Throwable object
     *
     * @return DOCUMENT ME!
     */
    public String getClassName() {
        return className;
    }
}
