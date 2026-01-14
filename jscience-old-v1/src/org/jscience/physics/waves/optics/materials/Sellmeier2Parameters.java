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
