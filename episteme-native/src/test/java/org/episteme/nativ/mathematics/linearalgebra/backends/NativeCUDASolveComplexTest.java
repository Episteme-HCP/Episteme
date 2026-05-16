package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.numbers.real.RealDouble;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.core.mathematics.linearalgebra.vectors.GenericVector;
import org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

public class NativeCUDASolveComplexTest {

    private boolean isCudaAvailable() {
        return new NativeCUDADenseLinearAlgebraDoubleBackend().isAvailable();
    }

    @Test
    @EnabledIf("isCudaAvailable")
    public void testSolveComplex() {
        NativeCUDADenseLinearAlgebraDoubleBackend backend = new NativeCUDADenseLinearAlgebraDoubleBackend();
        Ring<Complex> ring = org.episteme.core.mathematics.sets.Complexes.getInstance();

        // A = [[1+i, 0], [0, 1-i]]
        Complex[][] aData = {
            {Complex.of(1, 1), Complex.of(0, 0)},
            {Complex.of(0, 0), Complex.of(1, -1)}
        };
        DenseMatrix<Complex> A = new DenseMatrix<>(aData, ring);
        
        // b = [1+i, 1-i]
        Complex[] bData = {Complex.of(1, 1), Complex.of(1, -1)};
        Vector<Complex> b = new GenericVector<>(new DenseVectorStorage<>(bData), backend, ring);

        // x should be [1, 1]
        Vector<Complex> x = backend.solve(A, b);

        assertThat(x.get(0).real()).isCloseTo(1.0, offset(1e-10));
        assertThat(x.get(0).imaginary()).isCloseTo(0.0, offset(1e-10));
        assertThat(x.get(1).real()).isCloseTo(1.0, offset(1e-10));
        assertThat(x.get(1).imaginary()).isCloseTo(0.0, offset(1e-10));
    }
}
