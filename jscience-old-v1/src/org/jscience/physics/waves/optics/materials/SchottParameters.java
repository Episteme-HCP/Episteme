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
public class SchottParameters extends Parameter {
    /** DOCUMENT ME! */
    static final int nParameters = 6;

    /** DOCUMENT ME! */
    private double a0 = 1;

    /** DOCUMENT ME! */
    private double a1 = 0;

    /** DOCUMENT ME! */
    private double a2 = 0;

    /** DOCUMENT ME! */
    private double a3 = 0;

    /** DOCUMENT ME! */
    private double a4 = 0;

    /** DOCUMENT ME! */
    private double a5 = 0;

/**
     * Creates a new SchottParameters object.
     */
    public SchottParameters() {
        super();
    }

/**
     * Creates a new SchottParameters object.
     *
     * @param a0 DOCUMENT ME!
     * @param a1 DOCUMENT ME!
     * @param a2 DOCUMENT ME!
     * @param a3 DOCUMENT ME!
     * @param a4 DOCUMENT ME!
     * @param a5 DOCUMENT ME!
     */
    public SchottParameters(double a0, double a1, double a2, double a3,
        double a4, double a5) {
        super();
        this.a0 = a0;
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.a4 = a4;
        this.a5 = a5;
    }

    /*public SchottParameters( SchottParameters p )
    {
        super();
        this.a0 = p.a0;
        this.a1 = p.a1;
        this.a2 = p.a2;
        this.a3 = p.a3;
        this.a4 = p.a4;
        this.a5 = p.a5;
    }*/
    public SchottParameters(double[] n) throws IllegalDimensionException {
        super();
        setArray(n);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String type() {
        return "Schott";
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double indexAtWavelength(double w) {
        return Math.sqrt(a0 + (a1 * w * w) + (a2 * Math.pow(w, -2)) +
            (a3 * Math.pow(w, -4)) + (a4 * Math.pow(w, -6)) +
            (a5 * Math.pow(w, -8)));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] asArray() {
        double[] a = { a0, a1, a2, a3, a4, a5 };

        return a;
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

        this.a0 = n[0];
        this.a1 = n[1];
        this.a2 = n[2];
        this.a3 = n[3];
        this.a4 = n[4];
        this.a5 = n[5];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double A0() {
        return a0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double A1() {
        return a1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double A2() {
        return a2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double A3() {
        return a3;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double A4() {
        return a4;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double A5() {
        return a5;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setA0(double n) {
        this.a0 = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setA1(double n) {
        this.a1 = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setA2(double n) {
        this.a2 = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setA3(double n) {
        this.a3 = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setA4(double n) {
        this.a4 = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setA5(double n) {
        this.a5 = n;
    }
}
