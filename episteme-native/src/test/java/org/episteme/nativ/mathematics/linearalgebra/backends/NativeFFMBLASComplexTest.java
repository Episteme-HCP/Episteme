package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NativeFFMBLASComplexTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testComplexMultiply() {
        NativeFFMBLASBackend backend = new NativeFFMBLASBackend();
        if (!backend.isAvailable()) {
            System.out.println("Native BLAS not available, skipping test.");
            return;
        }

        // A = [1+i]
        // B = [2+2i]
        // C = A*B = (1*2 - 1*2) + (1*2 + 1*2)i = 0 + 4i
        
        Complex aVal = Complex.of(1.0, 1.0);
        Complex bVal = Complex.of(2.0, 2.0);
        
        // Use Object cast trick to fit Complex into Real matrix container
        Real[][] aData = {{(Real)(Object)aVal}};
        Real[][] bData = {{(Real)(Object)bVal}};
        
        // Cast the ring to Ring<Real> for Matrix compatibility
        org.episteme.core.mathematics.structures.rings.Ring<Real> complexRing = 
            (org.episteme.core.mathematics.structures.rings.Ring<Real>)(Object)aVal.getScalarRing();
            
        Matrix<Real> A = new DenseMatrix<Real>(aData, complexRing);
        Matrix<Real> B = new DenseMatrix<Real>(bData, complexRing);
        
        Matrix<Real> C = backend.multiply(A, B);
        
        Complex res = (Complex)(Object)C.get(0, 0);
        assertEquals(0.0, res.real(), 1e-9);
        assertEquals(4.0, res.imaginary(), 1e-9);
    }
}
