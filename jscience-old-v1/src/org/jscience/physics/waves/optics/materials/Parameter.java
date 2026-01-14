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
abstract public class Parameter extends Object implements Cloneable {
/**
     * Creates a new Parameter object.
     */
    public Parameter() {
    }

/**
     * Creates a new Parameter object.
     *
     * @param n DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public Parameter(double[] n) throws IllegalDimensionException {
    }

    // Every subclass must override this method returning its
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    abstract public String type();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    abstract public double[] asArray();

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    abstract public void setArray(double[] n) throws IllegalDimensionException;

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    abstract public double indexAtWavelength(double w);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
