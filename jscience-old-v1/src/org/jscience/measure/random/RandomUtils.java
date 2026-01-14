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

package org.jscience.measure.random;

import org.jscience.mathematics.algebraic.matrices.*;


/**
 * This is a useful collection of random related methods.
 *
 * @author Mark Hale
 */
public final class RandomUtils {
/**
     * Creates a new RandomUtils object.
     */
    private RandomUtils() {
    }

    /**
     * Creates a random generated vector.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static AbstractDoubleVector randomVector(int size) {
        return new DoubleVector(size).mapElements(RandomMap.MAP);
    }

    /**
     * Creates a random generated vector.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static AbstractComplexVector randomComplexVector(int size) {
        return new ComplexVector(size).mapElements(RandomMap.MAP);
    }

    /**
     * Creates a random generated square matrix.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DoubleSquareMatrix randomSquareMatrix(int size) {
        return (DoubleSquareMatrix) new DoubleSquareMatrix(size).mapElements(RandomMap.MAP);
    }

    /**
     * Creates a random generated tridiagonal matrix.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DoubleTridiagonalMatrix randomTridiagonalMatrix(int size) {
        return (DoubleTridiagonalMatrix) new DoubleTridiagonalMatrix(size).mapElements(RandomMap.MAP);
    }

    /**
     * Creates a random generated diagonal matrix.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DoubleDiagonalMatrix randomDiagonalMatrix(int size) {
        return (DoubleDiagonalMatrix) new DoubleDiagonalMatrix(size).mapElements(RandomMap.MAP);
    }

    /**
     * Creates a random generated square matrix.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ComplexSquareMatrix randomComplexSquareMatrix(int size) {
        return (ComplexSquareMatrix) new ComplexSquareMatrix(size).mapElements(RandomMap.MAP);
    }

    /**
     * Creates a random generated tridiagonal matrix.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ComplexTridiagonalMatrix randomComplexTridiagonalMatrix(
        int size) {
        return (ComplexTridiagonalMatrix) new ComplexTridiagonalMatrix(size).mapElements(RandomMap.MAP);
    }

    /**
     * Creates a random generated diagonal matrix.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ComplexDiagonalMatrix randomComplexDiagonalMatrix(int size) {
        return (ComplexDiagonalMatrix) new ComplexDiagonalMatrix(size).mapElements(RandomMap.MAP);
    }
}
