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

package org.jscience.mathematics.vector;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.sets.Reals;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Matrix operations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MatrixTest {

        @Test
        public void testMatrixCreation() {
                List<List<Real>> data = Arrays.asList(
                                Arrays.asList(Real.of(1), Real.of(2)),
                                Arrays.asList(Real.of(3), Real.of(4)));
                Matrix<Real> m = new DenseMatrix<>(data, Reals.getInstance());
                assertEquals(2, m.rows());
                assertEquals(2, m.cols());
                assertEquals(Real.of(1), m.get(0, 0));
                assertEquals(Real.of(4), m.get(1, 1));
        }

        @Test
        public void testMatrixAddition() {
                List<List<Real>> data1 = Arrays.asList(
                                Arrays.asList(Real.of(1), Real.of(2)),
                                Arrays.asList(Real.of(3), Real.of(4)));
                List<List<Real>> data2 = Arrays.asList(
                                Arrays.asList(Real.of(5), Real.of(6)),
                                Arrays.asList(Real.of(7), Real.of(8)));

                Matrix<Real> m1 = new DenseMatrix<>(data1, Reals.getInstance());
                Matrix<Real> m2 = new DenseMatrix<>(data2, Reals.getInstance());
                Matrix<Real> sum = m1.add(m2);

                assertEquals(Real.of(6), sum.get(0, 0)); // 1+5
                assertEquals(Real.of(8), sum.get(0, 1)); // 2+6
                assertEquals(Real.of(10), sum.get(1, 0)); // 3+7
                assertEquals(Real.of(12), sum.get(1, 1)); // 4+8
        }

        @Test
        public void testMatrixMultiplication() {
                // A = [1 2], B = [5 6]
                // [3 4] [7 8]
                // A*B = [1*5+2*7 1*6+2*8] = [19 22]
                // [3*5+4*7 3*6+4*8] [43 50]

                List<List<Real>> data1 = Arrays.asList(
                                Arrays.asList(Real.of(1), Real.of(2)),
                                Arrays.asList(Real.of(3), Real.of(4)));
                List<List<Real>> data2 = Arrays.asList(
                                Arrays.asList(Real.of(5), Real.of(6)),
                                Arrays.asList(Real.of(7), Real.of(8)));

                Matrix<Real> m1 = new DenseMatrix<>(data1, Reals.getInstance());
                Matrix<Real> m2 = new DenseMatrix<>(data2, Reals.getInstance());
                Matrix<Real> product = m1.multiply(m2);

                assertEquals(Real.of(19), product.get(0, 0));
                assertEquals(Real.of(22), product.get(0, 1));
                assertEquals(Real.of(43), product.get(1, 0));
                assertEquals(Real.of(50), product.get(1, 1));
        }

        @Test
        public void testTranspose() {
                List<List<Real>> data = Arrays.asList(
                                Arrays.asList(Real.of(1), Real.of(2)),
                                Arrays.asList(Real.of(3), Real.of(4)));
                Matrix<Real> m = new DenseMatrix<>(data, Reals.getInstance());
                Matrix<Real> t = m.transpose();

                assertEquals(Real.of(1), t.get(0, 0));
                assertEquals(Real.of(3), t.get(0, 1));
                assertEquals(Real.of(2), t.get(1, 0));
                assertEquals(Real.of(4), t.get(1, 1));
        }
}