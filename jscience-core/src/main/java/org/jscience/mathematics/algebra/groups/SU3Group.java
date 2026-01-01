/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.algebra.groups;

import org.jscience.mathematics.structures.groups.Group;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.sets.Complexes;
import java.util.*;

/**
 * The group of 3x3 unitary matrices with determinant 1.
 * Important in quantum chromodynamics (QCD).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SU3Group implements Group<Matrix<Complex>> {

    private static final SU3Group INSTANCE = new SU3Group();

    public static SU3Group getInstance() {
        return INSTANCE;
    }

    private SU3Group() {
    }

    @Override
    public Matrix<Complex> operate(Matrix<Complex> left, Matrix<Complex> right) {
        return left.multiply(right);
    }

    @Override
    public Matrix<Complex> identity() {
        // 3x3 Identity
        Complex[][] data = new Complex[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                data[i][j] = (i == j) ? Complex.ONE : Complex.ZERO;
            }
        }

        List<List<Complex>> rows = new ArrayList<>();
        for (Complex[] rowData : data)
            rows.add(Arrays.asList(rowData));
        return new DenseMatrix<>(rows, Complexes.getInstance());
    }

    @Override
    public Matrix<Complex> inverse(Matrix<Complex> element) {
        throw new UnsupportedOperationException("Matrix inversion not yet fully exposed");
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    public String description() {
        return "SU(3) - Special Unitary Group (QCD)";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Matrix<Complex> element) {
        if (element.rows() != 3 || element.cols() != 3)
            return false;
        return true;
    }
}

