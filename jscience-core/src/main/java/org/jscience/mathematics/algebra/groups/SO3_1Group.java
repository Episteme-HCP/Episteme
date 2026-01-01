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
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.sets.Reals;
import java.util.*;

/**
 * The group of all linear transformations of Minkowski space that preserve the
 * spacetime interval dsÃ‚Â²=-cÃ‚Â²dtÃ‚Â²+dxÃ‚Â²+dyÃ‚Â²+dzÃ‚Â².
 * Elements are represented as 4x4 matrices.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SO3_1Group implements Group<Matrix<Real>> {

    private static final SO3_1Group INSTANCE = new SO3_1Group();

    public static SO3_1Group getInstance() {
        return INSTANCE;
    }

    private SO3_1Group() {
    }

    @Override
    public Matrix<Real> operate(Matrix<Real> left, Matrix<Real> right) {
        return left.multiply(right);
    }

    @Override
    public Matrix<Real> identity() {
        // 4x4 Identity Matrix
        Real[][] data = new Real[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                data[i][j] = (i == j) ? Real.ONE : Real.ZERO;
            }
        }
        // Convert array to List<List<Real>>
        List<List<Real>> rows = new ArrayList<>();
        for (Real[] rowData : data) {
            rows.add(Arrays.asList(rowData));
        }
        return new DenseMatrix<>(rows, Reals.getInstance());
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> element) {
        return element.inverse();
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    public String description() {
        return "SO(3,1) - Lorentz Group";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Matrix<Real> element) {
        if (element.rows() != 4 || element.cols() != 4)
            return false;
        return true;
    }

    /**
     * Creates a boost matrix in the x-direction.
     * 
     * @param beta velocity v/c
     * @return the Lorentz boost matrix
     */
    public Matrix<Real> boostX(double beta) {
        if (Math.abs(beta) >= 1.0)
            throw new IllegalArgumentException("Beta must be < 1");
        double gamma = 1.0 / Math.sqrt(1.0 - beta * beta);

        Real[][] data = new Real[4][4];
        // Initialize to 0
        for (int i = 0; i < 4; i++)
            Arrays.fill(data[i], Real.ZERO);

        Real g = Real.of(gamma);
        Real gb = Real.of(-gamma * beta);

        // t' = ÃŽÂ³t - ÃŽÂ³ÃŽÂ²x
        // x' = -ÃŽÂ³ÃŽÂ²t + ÃŽÂ³x
        data[0][0] = g;
        data[0][1] = gb;
        data[1][0] = gb;
        data[1][1] = g;
        data[2][2] = Real.ONE;
        data[3][3] = Real.ONE;

        // Convert array to List<List<Real>>
        List<List<Real>> rows = new ArrayList<>();
        for (Real[] rowData : data) {
            rows.add(Arrays.asList(rowData));
        }

        return new DenseMatrix<>(rows, Reals.getInstance());
    }
}

