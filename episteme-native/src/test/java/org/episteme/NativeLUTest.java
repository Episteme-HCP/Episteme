package org.episteme;

import org.junit.jupiter.api.Test;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.nativ.mathematics.linearalgebra.backends.NativeFFMBLASBackend;

public class NativeLUTest {
    @Test
    public void testLU() {
        try {
            System.out.println("Starting LU test...");
            try (NativeFFMBLASBackend backend = new NativeFFMBLASBackend()) {
                int n = 3;
                DenseMatrixStorage<Real> storage = new DenseMatrixStorage<>(n, n, Real.ZERO);
                for(int i=0; i<n; i++) {
                    for(int j=0; j<n; j++) {
                        storage.set(i, j, Real.of(Math.random()));
                    }
                }
                Matrix<Real> mat = new GenericMatrix<>(storage, backend, Reals.getInstance());
                System.out.println("Calling LU...");
                backend.lu(mat);
                System.out.println("LU SUCCESS!");

                // Test Colt as well to see NPE
                try (org.episteme.core.mathematics.linearalgebra.backends.ColtBackend<Real> colt = new org.episteme.core.mathematics.linearalgebra.backends.ColtBackend<>(Reals.getInstance())) {
                    System.out.println("Calling Colt LU...");
                    colt.lu(mat);
                    System.out.println("Colt LU SUCCESS!");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
