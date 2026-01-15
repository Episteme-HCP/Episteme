package org.jscience.ml.sbml.math;

/**
 * A class that holds a user or program defined value. This code is
 * licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more
 * details.
 *
 * @author Marc Vass
 */
public class MathElement {
    /** DOCUMENT ME! */
    private String value;

/**
     * Creates a new instance of MathElement
     */
    public MathElement() {
    }

/**
     * Creates a new MathElement object.
     *
     * @param s DOCUMENT ME!
     */
    public MathElement(String s) {
        value = s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void setValue(String s) {
        value = s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return value;
    }
}
