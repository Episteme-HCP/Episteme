package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

public class NativeCUDADeterminantComplexTest {

    @SuppressWarnings("unused")
    private boolean isCudaAvailable() {
        try (NativeCUDADenseLinearAlgebraDoubleBackend<Complex> backend = new NativeCUDADenseLinearAlgebraDoubleBackend<>()) {
            return backend.isAvailable();
        }
    }

    @Test
    @EnabledIf("isCudaAvailable")
    public void testDeterminantComplex() {
        try (NativeCUDADenseLinearAlgebraDoubleBackend<Complex> backend = new NativeCUDADenseLinearAlgebraDoubleBackend<>()) {
            Ring<Complex> ring = org.episteme.core.mathematics.sets.Complexes.getInstance();

            // A = [[1+i, 2], [3, 4-i]]
            // det = (1+i)(4-i) - 6 = (4 - i + 4i + 1) - 6 = 5 + 3i - 6 = -1 + 3i
            Complex[][] aData = {
                {Complex.of(1, 1), Complex.of(2, 0)},
                {Complex.of(3, 0), Complex.of(4, -1)}
            };
            DenseMatrix<Complex> A = new DenseMatrix<>(aData, ring);
            
            Complex det = backend.determinant(A);

            assertThat(det.real()).isCloseTo(-1.0, offset(1e-10));
            assertThat(det.imaginary()).isCloseTo(3.0, offset(1e-10));
        }
    }
}
