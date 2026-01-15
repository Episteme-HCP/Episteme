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
public class HerzbergerParameters extends Parameter {
    /** DOCUMENT ME! */
    static final int nParameters = 6;

    /** DOCUMENT ME! */
    private double a = 1;

    /** DOCUMENT ME! */
    private double b = 0;

    /** DOCUMENT ME! */
    private double c = 0;

    /** DOCUMENT ME! */
    private double d = 0;

    /** DOCUMENT ME! */
    private double e = 0;

    /** DOCUMENT ME! */
    private double f = 0;

/**
     * Creates a new HerzbergerParameters object.
     */
    public HerzbergerParameters() {
        super();
    }

/**
     * Creates a new HerzbergerParameters object.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param e DOCUMENT ME!
     * @param f DOCUMENT ME!
     */
    public HerzbergerParameters(double a, double b, double c, double d,
        double e, double f) {
        super();
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }

/**
     * Creates a new HerzbergerParameters object.
     *
     * @param p DOCUMENT ME!
     */
    public HerzbergerParameters(HerzbergerParameters p) {
        super();
        this.a = p.a;
        this.b = p.b;
        this.c = p.c;
        this.d = p.d;
        this.e = p.e;
        this.f = p.f;
    }

/**
     * Creates a new HerzbergerParameters object.
     *
     * @param n DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public HerzbergerParameters(double[] n) throws IllegalDimensionException {
        super();
        setArray(n);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String type() {
        return "Herzberger";
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
        double L = 1 / (w2 - 0.028);

        return a + (b * L) + (c * Math.pow(L, 2)) + (d * w2) +
        (e * Math.pow(w2, 2)) + (f * Math.pow(w2, 3));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] asArray() {
        double[] arr = { a, b, c, d, e, f };

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
        this.e = n[4];
        this.f = n[5];
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
     * @return DOCUMENT ME!
     */
    public double E() {
        return e;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double F() {
        return f;
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

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setE(double n) {
        this.e = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setF(double n) {
        this.f = n;
    }
}
