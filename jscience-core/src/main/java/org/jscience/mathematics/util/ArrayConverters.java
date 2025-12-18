/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.util;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for converting between primitive arrays and JScience types.
 * <p>
 * Provides batch conversion methods for:
 * <ul>
 * <li>Primitive double arrays to Real arrays</li>
 * <li>2D arrays to Matrix</li>
 * <li>Arrays to Vector</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class ArrayConverters {

    private ArrayConverters() {
        // Utility class - no instantiation
    }

    /**
     * Converts a primitive double array to Real array.
     * 
     * @param arr the input double array
     * @return array of Real values
     */
    public static Real[] toReal(double[] arr) {
        if (arr == null)
            return null;
        Real[] result = new Real[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = Real.of(arr[i]);
        }
        return result;
    }

    /**
     * Converts a Real array to primitive double array.
     * 
     * @param arr the input Real array
     * @return array of primitive double values
     */
    public static double[] toDouble(Real[] arr) {
        if (arr == null)
            return null;
        double[] result = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i].doubleValue();
        }
        return result;
    }

    /**
     * Converts a 2D primitive double array to Matrix<Real>.
     * 
     * @param arr the input 2D double array
     * @return Matrix of Real values
     */
    public static Matrix<Real> toMatrix(double[][] arr) {
        if (arr == null)
            return null;

        List<List<Real>> rowsList = new ArrayList<>();
        for (double[] row : arr) {
            List<Real> rowList = new ArrayList<>();
            for (double val : row) {
                rowList.add(Real.of(val));
            }
            rowsList.add(rowList);
        }

        return DenseMatrix.of(rowsList, Reals.getInstance());
    }

    /**
     * Converts a Matrix<Real> to 2D primitive double array.
     * 
     * @param m the input Matrix
     * @return 2D array of primitive double values
     */
    public static double[][] toDoubleMatrix(Matrix<Real> m) {
        if (m == null)
            return null;
        int rows = m.rows();
        int cols = m.cols();

        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = m.get(i, j).doubleValue();
            }
        }

        return result;
    }

    /**
     * Converts a primitive double array to Vector<Real>.
     * 
     * @param arr the input double array
     * @return Vector of Real values
     */
    public static Vector<Real> toVector(double[] arr) {
        if (arr == null)
            return null;

        List<Real> elements = new ArrayList<>();
        for (double val : arr) {
            elements.add(Real.of(val));
        }

        return DenseVector.of(elements, Reals.getInstance());
    }

    /**
     * Converts a Vector<Real> to primitive double array.
     * 
     * @param v the input Vector
     * @return array of primitive double values
     */
    public static double[] toDoubleVector(Vector<Real> v) {
        if (v == null)
            return null;
        int n = v.dimension();
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = v.get(i).doubleValue();
        }
        return result;
    }
}
