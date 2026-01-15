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
public class Sellmeier4Parameters extends Parameter {
    /** DOCUMENT ME! */
    static final int nParameters = 5;

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

/**
     * Creates a new Sellmeier4Parameters object.
     */
    public Sellmeier4Parameters() {
        super();
    }

/**
     * Creates a new Sellmeier4Parameters object.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param e DOCUMENT ME!
     */
    public Sellmeier4Parameters(double a, double b, double c, double d, double e) {
        super();
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }

/**
     * Creates a new Sellmeier4Parameters object.
     *
     * @param p DOCUMENT ME!
     */
    public Sellmeier4Parameters(Sellmeier4Parameters p) {
        super();
        this.a = p.a;
        this.b = p.b;
        this.c = p.c;
        this.d = p.d;
        this.e = p.e;
    }

/**
     * Creates a new Sellmeier4Parameters object.
     *
     * @param n DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public Sellmeier4Parameters(double[] n) throws IllegalDimensionException {
        super();
        setArray(n);
    }

    /*public int type()
    {
        return Parameter.Sellmeier4;
    }*/
    public String type() {
        return "Sellmeier4";
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

        return Math.sqrt(a + (w2 * ((b / (w2 - c)) + (d / (w2 - e)))));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] asArray() {
        double[] arr = { a, b, c, d, e };

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
}
