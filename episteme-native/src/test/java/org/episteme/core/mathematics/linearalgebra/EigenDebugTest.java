package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Test;

public class EigenDebugTest {
    public static void main(String[] args) {
        new EigenDebugTest().testNaN();
    }
    
    @Test
    public void testNaN() {
        int SIZE = 12; // Test size 12
        double[][] data = new double[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                data[i][j] = 1.0;
            }
        }
        RealDoubleMatrix a = RealDoubleMatrix.of(data);
        try (org.episteme.nativ.mathematics.linearalgebra.backends.NativeCPULinearAlgebraBackend backend = new org.episteme.nativ.mathematics.linearalgebra.backends.NativeCPULinearAlgebraBackend()) {
            org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<Real> res = backend.eigen(a);
        
        StringBuilder sb = new StringBuilder("EIGENVALUES:\n");
        for (int i = 0; i < res.getEigenvalues().dimension(); i++) {
            Real val = res.getEigenvalues().get(i);
            sb.append("Eig: ").append(val).append("\n");
        }
        throw new RuntimeException(sb.toString());
        }
    }
}
