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
 * used as a proxy for parameter objects that have been read through XML
 *
 * @author Holger Antelmann
 */
class ProxyParameter {
    /** DOCUMENT ME! */
    String className;

    /** DOCUMENT ME! */
    String stringValue;

/**
     * Creates a new ProxyParameter object.
     *
     * @param stringValue DOCUMENT ME!
     * @param className   DOCUMENT ME!
     */
    ProxyParameter(String stringValue, String className) {
        this.stringValue = stringValue;
        this.className = className;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return stringValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getClassName() {
        return className;
    }
}
