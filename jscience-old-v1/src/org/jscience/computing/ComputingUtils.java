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

package org.jscience.computing;

import org.jscience.mathematics.algebraic.matrices.DoubleMatrix;
import org.jscience.mathematics.algebraic.matrices.DoubleVector;


/**
 * A class representing some useful methods for computing science
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class ComputingUtils extends Object {
    /**
     * The Amdahl law for sequential operations, see
     * http://en.wikipedia.org/wiki/Amdahl%27s_law
     *
     * @param p DOCUMENT ME!
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final double getAmdahlSpeedup(double p, double s) {
        return 1 / (1 - p + (p / s));
    }

    /**
     * The Amdahl law for parallel operations, see
     * http://en.wikipedia.org/wiki/Amdahl%27s_law
     *
     * @param f DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final double getAmdahlParallelSpeedup(double f, double n) {
        return 1 / (f + ((1 - f) / n));
    }

    /**
     * The Shannon entropy, see
     * http://en.wikipedia.org/wiki/Information_theory Each value of the
     * Vector should be between 0 and 1, although this is unchecked See also
     * org.jscience.mathematics.statistics.EngineerMathUtils
     *
     * @param vector DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final double getEntropy(DoubleVector vector) {
        double result;

        result = 0;

        for (int i = 0; i < vector.getDimension(); i++) {
            result -= (vector.getElement(i).doubleValue() * Math.log(vector.getElement(
                    i).doubleValue()));
        }

        return result;
    }

    /**
     * The Shannon binary entropy, see
     * http://en.wikipedia.org/wiki/Information_theory, where we use ln
     * instead of log Each value of the Vector should be between 0 and 1,
     * although this is unchecked See also
     * org.jscience.mathematics.statistics.EngineerMathUtils
     *
     * @param vector DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final double getBinaryEntropy(DoubleVector vector) {
        double result;

        result = 0;

        for (int i = 0; i < vector.getDimension(); i++) {
            result -= ((vector.getPrimitiveElement(i) * Math.log(vector.getPrimitiveElement(
                    i))) / Math.log(10));
        }

        return result;
    }

    /**
     * The Shannon mutual information, see
     * http://en.wikipedia.org/wiki/Information_theory Each value of the
     * Matrix should be between 0 and 1, although this is unchecked Each value
     * of each Vector should be between 0 and 1, although this is unchecked
     * See also org.jscience.mathematics.statistics.EngineerMathUtils
     *
     * @param vectorX DOCUMENT ME!
     * @param vectorY DOCUMENT ME!
     * @param matrix DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final double getMutualInformation(DoubleVector vectorX,
        DoubleVector vectorY, DoubleMatrix matrix) {
        double result;

        result = 0;

        for (int i = 0; i < matrix.numRows(); i++) {
            for (int j = 0; j < matrix.numColumns(); j++) {
                result -= (matrix.getElement(i, j).doubleValue() * Math.log(matrix.getElement(
                        i, j).doubleValue()));
            }
        }

        return (getEntropy(vectorX) + getEntropy(vectorY)) - result;
    }
}
