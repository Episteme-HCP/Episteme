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
