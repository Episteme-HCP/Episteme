package org.episteme.scratch;

import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.nativ.mathematics.linearalgebra.backends.NativeMPFRSparseLinearAlgebraBackend;
import org.episteme.core.mathematics.structures.rings.Ring;

public class VerifySparseFix {
    public static void main(String[] args) {
        try {
            NativeMPFRSparseLinearAlgebraBackend<Complex> provider = new NativeMPFRSparseLinearAlgebraBackend<>();
            if (!provider.isAvailable()) {
                System.out.println("Provider not available");
                return;
            }

            int n = 4;
            Ring<Complex> ring = Complex.of(1.0, 0.0).getScalarRing();
            
            // Create a small diagonal matrix
            Complex[][] data = new Complex[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    data[i][j] = (i == j) ? Complex.of(2.0, 1.0) : Complex.ZERO;
                }
            }
            Matrix<Complex> A = Matrix.of(java.util.Arrays.stream(data).map(java.util.Arrays::asList).toList(), ring);
            Vector<Complex> b = Vector.of(java.util.Arrays.asList(Complex.of(1.0, 0.0), Complex.of(1.0, 0.0), Complex.of(1.0, 0.0), Complex.of(1.0, 0.0)), ring);
            Vector<Complex> x0 = Vector.zeros(n, ring);

            System.out.println("Running GMRES...");
            Vector<Complex> x = provider.gmres(A, b, x0, Complex.of(1e-10, 0.0), 100, 10);
            System.out.println("GMRES Success! Result: " + x.get(0));
            
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
}
