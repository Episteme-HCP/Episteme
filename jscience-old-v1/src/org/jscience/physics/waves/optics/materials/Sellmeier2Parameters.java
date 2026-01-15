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
public class Sellmeier2Parameters extends Parameter {
    /** DOCUMENT ME! */
    static final int nParameters = 5;

    /** DOCUMENT ME! */
    private double a = 0;

    /** DOCUMENT ME! */
    private double b1 = 0;

    /** DOCUMENT ME! */
    private double lambda1 = 0;

    /** DOCUMENT ME! */
    private double b2 = 0;

    /** DOCUMENT ME! */
    private double lambda2 = 0;

/**
     * Creates a new Sellmeier2Parameters object.
     */
    public Sellmeier2Parameters() {
        super();
    }

/**
     * Creates a new Sellmeier2Parameters object.
     *
     * @param a       DOCUMENT ME!
     * @param b1      DOCUMENT ME!
     * @param lambda1 DOCUMENT ME!
     * @param b2      DOCUMENT ME!
     * @param lambda2 DOCUMENT ME!
     */
    public Sellmeier2Parameters(double a, double b1, double lambda1, double b2,
        double lambda2) {
        super();
        this.a = a;
        this.b1 = b1;
        this.lambda1 = lambda1;
        this.b2 = b2;
        this.lambda2 = lambda2;
    }

/**
     * Creates a new Sellmeier2Parameters object.
     *
     * @param p DOCUMENT ME!
     */
    public Sellmeier2Parameters(Sellmeier2Parameters p) {
        super();
        this.a = p.a;
        this.b1 = p.b1;
        this.lambda1 = p.lambda1;
        this.b2 = p.b2;
        this.lambda2 = p.lambda2;
    }

/**
     * Creates a new Sellmeier2Parameters object.
     *
     * @param n DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public Sellmeier2Parameters(double[] n) throws IllegalDimensionException {
        super();
        setArray(n);
    }

    /*public int type()
    {
        return Parameter.Sellmeier2;
    }*/
    public String type() {
        return "Sellmeier2";
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

        return Math.sqrt(1 + a + ((w2 * b1) / (w2 - Math.pow(lambda1, 2))) +
            (b2 / (w2 - Math.pow(lambda2, 2))));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] asArray() {
        double[] arr = { a, b1, lambda1, b2, lambda2 };

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
        this.b1 = n[1];
        this.lambda1 = n[2];
        this.b2 = n[3];
        this.lambda2 = n[4];
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
    public double B1() {
        return b1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double Lambda1() {
        return lambda1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double B2() {
        return b2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double Lambda2() {
        return lambda2;
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
    public void setB1(double n) {
        this.b1 = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setLambda1(double n) {
        this.lambda1 = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setB2(double n) {
        this.b2 = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setLambda2(double n) {
        this.lambda2 = n;
    }
}
