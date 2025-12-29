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

package org.jscience.mathematics.linearalgebra;

import org.junit.jupiter.api.Test;

import org.jscience.mathematics.linearalgebra.backends.OpenCLDenseLinearAlgebraProvider;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;

import org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenCLProviderTest {
        // ... (rest of file)

        @Test
        void testDirectBufferMatrixMultiply() {
                if (!OpenCLDenseLinearAlgebraProvider.isOpenCLAvailable()) {
                        return; // Skip if no OpenCL
                }

                System.out.println("Testing DirectBuffer Matrix Multiplication...");

                int n = 4;
                RealDoubleMatrix A = RealDoubleMatrix.direct(n, n);
                RealDoubleMatrix B = RealDoubleMatrix.direct(n, n);

                // Identity * Identity = Identity
                for (int i = 0; i < n; i++) {
                        A.set(i, i, 1.0);
                        B.set(i, i, 1.0);
                }

                // Force OpenCL provider
                Matrix<Real> C = new OpenCLDenseLinearAlgebraProvider<>(Reals.getInstance()).multiply(A, B);

                if (C instanceof RealDoubleMatrix && ((RealDoubleMatrix) C).isDirect()) {
                        System.out.println("Zero-Copy DirectBuffer multiplication successful!");
                        for (int i = 0; i < n; i++) {
                                assertEquals(1.0, C.get(i, i).doubleValue(), 0.001);
                                assertEquals(0.0, C.get(i, (i + 1) % n).doubleValue(), 0.001);
                        }
                } else {
                        System.out.println("OpenCL fallback occurred. Result is " + C.getClass().getSimpleName()
                                        + ". Skipping Zero-Copy assertions.");
                }

                System.out.println(org.jscience.util.PerformanceLogger.getReport());
        }
}
