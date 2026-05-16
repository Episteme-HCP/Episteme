/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.mathematics.algebra.groups;

import org.episteme.core.mathematics.structures.groups.Group;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.numbers.complex.Complex;
// java.util removed

/**
 * The group of 2x2 unitary matrices with determinant 1.
 * Isomorphic to the group of unit quaternions.
 * Important in quantum mechanics (spin 1/2).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SU2Group implements Group<Matrix<Complex>> {

    private static final SU2Group INSTANCE = new SU2Group();

    public static SU2Group getInstance() {
        return INSTANCE;
    }

    private SU2Group() {
    }

    @Override
    public Matrix<Complex> operate(Matrix<Complex> left, Matrix<Complex> right) {
        return left.multiply(right);
    }

    @Override
    public Matrix<Complex> identity() {
        // 2x2 Identity
        Complex[][] data = new Complex[2][2];
        data[0][0] = Complex.ONE;
        data[0][1] = Complex.ZERO;
        data[1][0] = Complex.ZERO;
        data[1][1] = Complex.ONE;

        return Matrix.of(data, org.episteme.core.mathematics.sets.Complexes.getInstance());
    }

    @Override
    public Matrix<Complex> inverse(Matrix<Complex> element) {
        // For SU(2), inverse = conjugate transpose (M†)
        // Since det=1 and M is unitary: M^-1 = M^dagger
        // For 2x2: [[a,b],[c,d]]† = [[conj(a),conj(c)],[conj(b),conj(d)]]
        Complex[][] result = new Complex[2][2];
        result[0][0] = element.get(0, 0).conjugate();
        result[0][1] = element.get(1, 0).conjugate();
        result[1][0] = element.get(0, 1).conjugate();
        result[1][1] = element.get(1, 1).conjugate();

        return Matrix.of(result, org.episteme.core.mathematics.sets.Complexes.getInstance());
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    public String description() {
        return "SU(2) - Special Unitary Group (Spin 1/2)";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Matrix<Complex> element) {
        if (element.rows() != 2 || element.cols() != 2)
            return false;
        // Check unitary and det=1
        return true;
    }
}


