package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NativeFFMBLASComplexTest {

    @Test
    public void testComplexMultiply() {
        try (NativeFFMBLASComplexBackend backend = new NativeFFMBLASComplexBackend()) {
            if (!backend.isAvailable()) {
                System.out.println("Native BLAS not available, skipping test.");
                return;
            }

        Complex aVal = Complex.of(1.0, 1.0);
        Complex bVal = Complex.of(2.0, 2.0);
        
        Complex[][] aData = {{aVal}};
        Complex[][] bData = {{bVal}};
        
        Matrix<Complex> A = new DenseMatrix<>(aData, (org.episteme.core.mathematics.structures.rings.Ring<Complex>)aVal.getScalarRing());
        Matrix<Complex> B = new DenseMatrix<>(bData, (org.episteme.core.mathematics.structures.rings.Ring<Complex>)aVal.getScalarRing());
        
        Matrix<Complex> C = backend.multiply(A, B);
        
        Complex res = C.get(0, 0);
        assertEquals(0.0, res.real(), 1e-9);
        assertEquals(4.0, res.imaginary(), 1e-9);
        }
    }
}
