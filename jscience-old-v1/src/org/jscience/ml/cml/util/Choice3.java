package org.jscience.ml.cml.util;

/**
 * offers the choice of 0, 1 or 2; useful to restrict argument values
 * (rather like enum). The user cannot access the constructor so has to use
 * Choice3.Y, etc.
 *
 * @author (C) P. Murray-Rust, 1996
 */
public class Choice3 {
    /** DOCUMENT ME! */
    public static final Choice3 X = new Choice3(0);

    /** DOCUMENT ME! */
    public static final Choice3 Y = new Choice3(1);

    /** DOCUMENT ME! */
    public static final Choice3 Z = new Choice3(2);

    /** DOCUMENT ME! */
    public static final Choice3 zero = new Choice3(0);

    /** DOCUMENT ME! */
    public static final Choice3 one = new Choice3(1);

    /** DOCUMENT ME! */
    public static final Choice3 two = new Choice3(2);

    /** DOCUMENT ME! */
    int value;

/**
     * Creates a new Choice3 object.
     *
     * @param i DOCUMENT ME!
     */
    Choice3(int i) {
        this.value = i;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getValue() {
        return value;
    }
}
