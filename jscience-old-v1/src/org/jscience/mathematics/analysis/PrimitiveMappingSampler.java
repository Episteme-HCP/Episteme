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

package org.jscience.mathematics.analysis;

import java.io.Serializable;

/**
 * This class is a wrapper allowing to sample a
 * {@link PrimitiveMapping}.
 * <p/>
 * <p>The sample produced is a regular sample. It can be specified by
 * several means :
 * <ul>
 * <li> from an initial point a step and a number of points</li>
 * <li> from a range and a number of points</li>
 * <li> from a range and a step between points.</li>
 * </ul>
 * In the latter case, the step can optionaly be adjusted in order to
 * have the last point exactly at the upper bound of the range.</p>
 * <p/>
 * <p>The sample points are computed on demand, they are not
 * stored. This allow to use this method for very large sample with
 * little memory overhead. The drawback is that if the same sample
 * points are going to be requested several times, they will be
 * recomputed each time. In this case, the user should consider
 * storing the points by some other means.</p>
 *
 * @author L. Maisonobe
 * @version $Id: PrimitiveMappingSampler.java,v 1.2 2007-10-21 17:45:33 virtualcall Exp $
 * @see PrimitiveMapping
 */
public class PrimitiveMappingSampler implements SampledMapping, Serializable {

    /**
     * Underlying computable function.
     */
    private PrimitiveMapping function;

    /**
     * Beginning abscissa.
     */
    private double begin;

    /**
     * Step between points.
     */
    private double step;

    /**
     * Total number of points.
     */
    private int n;

    /**
     * Constructor.
     * <p/>
     * Build a sample from an {@link PrimitiveMapping}. Beware of the
     * classical off-by-one problem !  If you want to have a sample like
     * this : 0.0, 0.1, 0.2 ..., 1.0, then you should specify step = 0.1
     * and n = 11 (not n = 10).
     *
     * @param begin beginning of the range (will be the abscissa of the
     *              first point)
     * @param step  step between points
     * @param n     number of points
     */
    public PrimitiveMappingSampler(PrimitiveMapping function,
                                   double begin, double step, int n) {
        this.function = function;
        this.begin = begin;
        this.step = step;
        this.n = n;
    }

    /**
     * Constructor.
     * Build a sample from an {@link PrimitiveMapping}.
     *
     * @param range abscissa range (from <code>range [0]</code> to
     *              <code>range [1]</code>)
     * @param n     number of points
     */
    public PrimitiveMappingSampler(PrimitiveMapping function,
                                   double[] range, int n) {
        this.function = function;
        begin = range[0];
        step = (range[1] - range[0]) / (n - 1);
        this.n = n;
    }

    /**
     * Constructor.
     * Build a sample from an {@link PrimitiveMapping}.
     *
     * @param range      abscissa range (from <code>range [0]</code> to
     *                   <code>range [1]</code>)
     * @param step       step between points
     * @param adjustStep if true, the step is reduced in order to have
     *                   the last point of the sample exactly at <code>range [1]</code>,
     *                   if false the last point will be between <code>range [1] -
     *                   step</code> and <code>range [1]</code>
     */
    public PrimitiveMappingSampler(PrimitiveMapping function,
                                   double[] range, double step,
                                   boolean adjustStep) {
        this.function = function;
        begin = range[0];
        if (adjustStep) {
            n = (int) Math.ceil((range[1] - range[0]) / step);
            this.step = (range[1] - range[0]) / (n - 1);
        } else {
            n = (int) Math.floor((range[1] - range[0]) / step);
            this.step = step;
        }
    }

    public int size() {
        return n;
    }

    /**
     * Get the dimension of the input values of the function.
     *
     * @return dimension
     */
    public int numInputDimensions() {
        return 1;
    }

    /**
     * Get the dimension of the output values of the function.
     *
     * @return dimension
     */
    public int numOutputDimensions() {
        return 1;
    }

    public ValuedPair samplePointAt(int index)
            throws ArrayIndexOutOfBoundsException {

        if (index < 0 || index >= n) {
            throw new ArrayIndexOutOfBoundsException();
        }

        double x = begin + index * step;
        return new ValuedPair(x, function.map(x));

    }

}
