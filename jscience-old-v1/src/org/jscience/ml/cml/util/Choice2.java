package org.jscience.ml.cml.util;

/**
 * offers the choice of 0 or 1; useful to restrict argument values (rather
 * like enum). The user cannot access the constructor so has to use Choice2.Y,
 * etc.
 *
 * @author (C) P. Murray-Rust, 1996
 */
public class Choice2 {
    /** DOCUMENT ME! */
    public static final Choice2 X = new Choice2(0);

    /** DOCUMENT ME! */
    public static final Choice2 Y = new Choice2(1);

    /** DOCUMENT ME! */
    public static final Choice2 zero = new Choice2(0);

    /** DOCUMENT ME! */
    public static final Choice2 one = new Choice2(1);

    /** DOCUMENT ME! */
    int value;

/**
     * Creates a new Choice2 object.
     *
     * @param i DOCUMENT ME!
     */
    Choice2(int i) {
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
