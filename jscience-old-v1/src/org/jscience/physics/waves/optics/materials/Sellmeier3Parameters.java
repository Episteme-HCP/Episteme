/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.physics.waves.optics.materials;

import org.jscience.util.IllegalDimensionException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Sellmeier3Parameters extends Parameter {
    /** DOCUMENT ME! */
    static final int nParameters = 8;

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

    /** DOCUMENT ME! */
    private double k4 = 0;

    /** DOCUMENT ME! */
    private double l4 = 0;

/**
     * Creates a new Sellmeier3Parameters object.
     */
    public Sellmeier3Parameters() {
        super();
    }

/**
     * Creates a new Sellmeier3Parameters object.
     *
     * @param k1 DOCUMENT ME!
     * @param l1 DOCUMENT ME!
     * @param k2 DOCUMENT ME!
     * @param l2 DOCUMENT ME!
     * @param k3 DOCUMENT ME!
     * @param l3 DOCUMENT ME!
     * @param k4 DOCUMENT ME!
     * @param l4 DOCUMENT ME!
     */
    public Sellmeier3Parameters(double k1, double l1, double k2, double l2,
        double k3, double l3, double k4, double l4) {
        super();
        this.k1 = k1;
        this.l1 = l1;
        this.k2 = k2;
        this.l2 = l2;
        this.k3 = k3;
        this.l3 = l3;
        this.k4 = k4;
        this.l4 = l4;
    }

/**
     * Creates a new Sellmeier3Parameters object.
     *
     * @param p DOCUMENT ME!
     */
    public Sellmeier3Parameters(Sellmeier3Parameters p) {
        super();
        this.k1 = p.k1;
        this.l1 = p.l1;
        this.k2 = p.k2;
        this.l2 = p.l2;
        this.k3 = p.k3;
        this.l3 = p.l3;
        this.k4 = p.k4;
        this.l4 = p.l4;
    }

/**
     * Creates a new Sellmeier3Parameters object.
     *
     * @param n DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public Sellmeier3Parameters(double[] n) throws IllegalDimensionException {
        super();
        setArray(n);
    }

    /*public int type()
    {
        return Parameter.Sellmeier3;
    }*/
    public String type() {
        return "Sellmeier3";
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
            (w2 * ((k1 / (w2 - l1)) + (k2 / (w2 - l2)) + (k3 / (w2 - l3)) +
            (k4 / (w2 - l4)))));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] asArray() {
        double[] a = { k1, l1, k2, l2, k3, l3, k4, l4 };

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
        this.k4 = n[6];
        this.l4 = n[7];
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
     * @return DOCUMENT ME!
     */
    public double K4() {
        return k4;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double L4() {
        return l4;
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

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setK4(double n) {
        this.k4 = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setL4(double n) {
        this.l4 = n;
    }
}
