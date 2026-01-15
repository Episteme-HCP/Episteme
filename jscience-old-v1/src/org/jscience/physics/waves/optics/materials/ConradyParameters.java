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
public class ConradyParameters extends Parameter implements Cloneable {
    /** DOCUMENT ME! */
    static final int nParameters = 3;

    /** DOCUMENT ME! */
    private double n0 = 1;

    /** DOCUMENT ME! */
    private double a = 0;

    /** DOCUMENT ME! */
    private double b = 0;

/**
     * Creates a new ConradyParameters object.
     */
    public ConradyParameters() {
        super();
    }

/**
     * Creates a new ConradyParameters object.
     *
     * @param n0 DOCUMENT ME!
     * @param a  DOCUMENT ME!
     * @param b  DOCUMENT ME!
     */
    public ConradyParameters(double n0, double a, double b) {
        super();
        this.n0 = n0;
        this.a = a;
        this.b = b;
    }

/**
     * Creates a new ConradyParameters object.
     *
     * @param p DOCUMENT ME!
     */
    public ConradyParameters(ConradyParameters p) {
        super();
        this.n0 = p.n0;
        this.a = p.a;
        this.b = p.b;
    }

/**
     * Creates a new ConradyParameters object.
     *
     * @param n DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public ConradyParameters(double[] n) throws IllegalDimensionException {
        super();
        setArray(n);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String type() {
        return "Conrady";
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double indexAtWavelength(double w) {
        return n0 + (a / w) + (b / Math.pow(w, 3.5));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] asArray() {
        double[] arr = { n0, a, b };

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

        //WrongArraySizeException( type(), n.length, nParameters );
        this.n0 = n[0];
        this.a = n[1];
        this.b = n[2];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double N0() {
        return n0;
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
     * @param n DOCUMENT ME!
     */
    public void setN0(double n) {
        this.n0 = n;
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
}
