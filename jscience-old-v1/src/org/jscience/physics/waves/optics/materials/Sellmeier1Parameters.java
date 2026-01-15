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
public class Sellmeier1Parameters extends Parameter {
    /** DOCUMENT ME! */
    static final int nParameters = 6;

    /** DOCUMENT ME! */
    private double k1 = 0;

    /** DOCUMENT ME! */
    private double l1 = 0;

    /** DOCUMENT ME! */
    private double k2 = 0;

    /** DOCUMENT ME! */
    private double l2 = 0;

    /** DOCUMENT ME! */
    private double k3 = 0;

    /** DOCUMENT ME! */
    private double l3 = 0;

/**
     * Creates a new Sellmeier1Parameters object.
     */
    public Sellmeier1Parameters() {
        super();
    }

/**
     * Creates a new Sellmeier1Parameters object.
     *
     * @param k1 DOCUMENT ME!
     * @param l1 DOCUMENT ME!
     * @param k2 DOCUMENT ME!
     * @param l2 DOCUMENT ME!
     * @param k3 DOCUMENT ME!
     * @param l3 DOCUMENT ME!
     */
    public Sellmeier1Parameters(double k1, double l1, double k2, double l2,
        double k3, double l3) {
        super();
        this.k1 = k1;
        this.l1 = l1;
        this.k2 = k2;
        this.l2 = l2;
        this.k3 = k3;
        this.l3 = l3;
    }

/**
     * Creates a new Sellmeier1Parameters object.
     *
     * @param p DOCUMENT ME!
     */
    public Sellmeier1Parameters(Sellmeier1Parameters p) {
        super();
        this.k1 = p.k1;
        this.l1 = p.l1;
        this.k2 = p.k2;
        this.l2 = p.l2;
        this.k3 = p.k3;
        this.l3 = p.l3;
    }

/**
     * Creates a new Sellmeier1Parameters object.
     *
     * @param n DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public Sellmeier1Parameters(double[] n) throws IllegalDimensionException {
        super();
        setArray(n);
    }

    /*public int type()
    {
        return Parameter.Sellmeier1;
    }*/
    public String type() {
        return "Sellmeier1";
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

        return Math.sqrt(1 +
            (w2 * ((k1 / (w2 - l1)) + (k2 / (w2 - l2)) + (k3 / (w2 - l3)))));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] asArray() {
        double[] a = { k1, l1, k2, l2, k3, l3 };

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

        this.k1 = n[0];
        this.l1 = n[1];
        this.k2 = n[2];
        this.l2 = n[3];
        this.k3 = n[4];
        this.l3 = n[5];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double K1() {
        return k1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double L1() {
        return l1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double K2() {
        return k2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double L2() {
        return l2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double K3() {
        return k3;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double L3() {
        return l3;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setK1(double n) {
        this.k1 = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setL1(double n) {
        this.l1 = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setK2(double n) {
        this.k2 = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setL2(double n) {
        this.l2 = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setK3(double n) {
        this.k3 = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setL3(double n) {
        this.l3 = n;
    }
}
