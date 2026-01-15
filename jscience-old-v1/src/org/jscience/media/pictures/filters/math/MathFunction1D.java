/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters.math;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class MathFunction1D implements Function1D {
    /** DOCUMENT ME! */
    public final static int SIN = 1;

    /** DOCUMENT ME! */
    public final static int COS = 2;

    /** DOCUMENT ME! */
    public final static int TAN = 3;

    /** DOCUMENT ME! */
    public final static int SQRT = 4;

    /** DOCUMENT ME! */
    public final static int ASIN = -1;

    /** DOCUMENT ME! */
    public final static int ACOS = -2;

    /** DOCUMENT ME! */
    public final static int ATAN = -3;

    /** DOCUMENT ME! */
    public final static int SQR = -4;

    /** DOCUMENT ME! */
    private int operation;

/**
     * Creates a new MathFunction1D object.
     *
     * @param operation DOCUMENT ME!
     */
    public MathFunction1D(int operation) {
        this.operation = operation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float evaluate(float v) {
        switch (operation) {
        case SIN:
            return (float) Math.sin(v);

        case COS:
            return (float) Math.cos(v);

        case TAN:
            return (float) Math.tan(v);

        case SQRT:
            return (float) Math.sqrt(v);

        case ASIN:
            return (float) Math.asin(v);

        case ACOS:
            return (float) Math.acos(v);

        case ATAN:
            return (float) Math.atan(v);

        case SQR:
            return v * v;
        }

        return v;
    }
}
