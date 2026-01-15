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

import org.jscience.util.IllegalDimensionException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class HoO1Parameters extends Parameter {
    /** DOCUMENT ME! */
    static final int nParameters = 4;

    /** DOCUMENT ME! */
    private double a = 1;

    /** DOCUMENT ME! */
    private double b = 0;

    /** DOCUMENT ME! */
    private double c = 0;

    /** DOCUMENT ME! */
    private double d = 0;

/**
     * Creates a new HoO1Parameters object.
     */
    public HoO1Parameters() {
        super();
    }

/**
     * Creates a new HoO1Parameters object.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param d DOCUMENT ME!
     */
    public HoO1Parameters(double a, double b, double c, double d) {
        super();
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

/**
     * Creates a new HoO1Parameters object.
     *
     * @param p DOCUMENT ME!
     */
    public HoO1Parameters(HoO1Parameters p) {
        super();
        this.a = p.a;
        this.b = p.b;
        this.c = p.c;
        this.d = p.d;
    }

/**
     * Creates a new HoO1Parameters object.
     *
     * @param n DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public HoO1Parameters(double[] n) throws IllegalDimensionException {
        super();
        setArray(n);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String type() {
        return "HoO1";
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double indexAtWavelength(double w) {
        double w2 = w * w;

        return Math.sqrt((a + (b / (w2 - c))) - (d * w2));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] asArray() {
        double[] arr = { a, b, c, d };

        return arr;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public void setArray(double[] n) throws IllegalDimensionException {
        if (n.length != nParameters) {
            throw new IllegalDimensionException();
        }

        this.a = n[0];
        this.b = n[1];
        this.c = n[2];
        this.d = n[3];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double A() {
        return a;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double B() {
        return b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double C() {
        return c;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double D() {
        return d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setA(double n) {
        this.a = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setB(double n) {
        this.b = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setC(double n) {
        this.c = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setD(double n) {
        this.d = n;
    }
}
