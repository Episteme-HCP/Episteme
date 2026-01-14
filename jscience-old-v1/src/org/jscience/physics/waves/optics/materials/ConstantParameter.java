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
public class ConstantParameter extends Parameter implements Cloneable {
    /** DOCUMENT ME! */
    static final int nParameters = 1;

    /** DOCUMENT ME! */
    private double n = 1;

/**
     * Creates a new ConstantParameter object.
     */
    public ConstantParameter() {
        super();
    }

/**
     * Creates a new ConstantParameter object.
     *
     * @param n DOCUMENT ME!
     */
    public ConstantParameter(double n) {
        super();
        this.n = n;
    }

/**
     * Creates a new ConstantParameter object.
     *
     * @param p DOCUMENT ME!
     */
    public ConstantParameter(ConstantParameter p) {
        super();
        this.n = p.n;
    }

/**
     * Creates a new ConstantParameter object.
     *
     * @param n DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public ConstantParameter(double[] n) throws IllegalDimensionException {
        super();
        setArray(n);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String type() {
        return "Constant";
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double indexAtWavelength(double w) {
        return n;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] asArray() {
        double[] a = { n };

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

        //WrongArraySizeException( type(), n.length, nParameters );
        this.n = n[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double N() {
        return n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setN(double n) {
        this.n = n;
    }
}
