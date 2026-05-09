package org.episteme.nativ;

import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.nativ.mathematics.linearalgebra.backends.NativeMPFRDenseLinearAlgebraBackend;

public class DebugSVD {
    public static void main(String[] args) {
        try {
            MathContext.setCurrent(MathContext.normal());
            NativeMPFRDenseLinearAlgebraBackend backend = new NativeMPFRDenseLinearAlgebraBackend();
            
            int n = 4;
            Real[][] data = new Real[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    data[i][j] = Real.of((i == j) ? 1.0 : 0.1);
                }
            }
            
            Matrix<Real> m = Matrix.of(data, Reals.getInstance());
            System.out.println("Starting SVD...");
            backend.svd(m);
            System.out.println("SVD Successful!");
            
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
