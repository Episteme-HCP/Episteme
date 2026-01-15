/*
 * Cream.java
 *
 * Created on 21 July 2003, 19:20
 */
package org.jscience.tests.distributed;

/**
 * DOCUMENT ME!
 *
 * @author mmg20
 */
public class Cream extends java.net.URLClassLoader implements java.io.Serializable {
    /** DOCUMENT ME! */
    static java.net.URL[] uclp0;

/**
     * Creates a new instance of Cream
     *
     * @throws java.net.MalformedURLException DOCUMENT ME!
     */
    public Cream() throws java.net.MalformedURLException {
        super(uclp0);
    }
}
