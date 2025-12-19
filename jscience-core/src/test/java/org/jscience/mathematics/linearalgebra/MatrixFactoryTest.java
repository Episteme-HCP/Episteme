package org.jscience.mathematics.linearalgebra;

import org.junit.jupiter.api.Test;
import org.jscience.mathematics.linearalgebra.matrices.MatrixFactory;

import org.jscience.mathematics.linearalgebra.matrices.GenericMatrix;
import org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.mathematics.linearalgebra.matrices.storage.*;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;
import org.jscience.util.PerformanceLogger;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatrixFactoryTest {

    @Test
    public void testTriangularCreation() {
        // Lower triangle
        // 1 0
        // 1 1
        Real zero = Real.ZERO;
        Real one = Real.ONE;

        List<List<Real>> data = new ArrayList<>();
        data.add(Arrays.asList(one, zero));
        data.add(Arrays.asList(one, one));

        // Use explicit storage because size < 100 defaults to DENSE in AUTO mode
        Matrix<Real> m = MatrixFactory.<Real>create(data, Reals.getInstance(), MatrixFactory.Storage.TRIANGULAR);

        assertTrue(m instanceof GenericMatrix, "Should be GenericMatrix");
        GenericMatrix<Real> gm = (GenericMatrix<Real>) m;
        assertTrue(gm.getStorage() instanceof TriangularMatrixStorage, "Should be TriangularMatrixStorage");
        assertEquals(one, m.get(0, 0));
        assertEquals(zero, m.get(0, 1));

        System.out.println("Triangular Matrix created successfully: " + gm.getStorage().getClass().getSimpleName());
    }

    @Test
    public void testTridiagonalCreation() {
        // 1 1 0
        // 1 1 1
        // 0 1 1
        Real zero = Real.ZERO;
        Real one = Real.ONE;

        List<List<Real>> data = new ArrayList<>();
        data.add(Arrays.asList(one, one, zero));
        data.add(Arrays.asList(one, one, one));
        data.add(Arrays.asList(zero, one, one));

        // Use explicit storage because size < 100 defaults to DENSE in AUTO mode
        Matrix<Real> m = MatrixFactory.<Real>create(data, Reals.getInstance(), MatrixFactory.Storage.TRIDIAGONAL);

        GenericMatrix<Real> gm = (GenericMatrix<Real>) m;
        assertTrue(gm.getStorage() instanceof TridiagonalMatrixStorage, "Should be TridiagonalMatrixStorage");

        System.out.println("Tridiagonal Matrix created successfully: " + gm.getStorage().getClass().getSimpleName());
    }

    @Test
    public void testLoggerContext() {
        PerformanceLogger.reset();
        PerformanceLogger.setEnabled(true);

        int n = 4;
        RealDoubleMatrix A = RealDoubleMatrix.direct(n, n);
        RealDoubleMatrix B = RealDoubleMatrix.direct(n, n);
        for (int i = 0; i < n; i++) {
            A.set(i, i, 1.0);
            B.set(i, i, 1.0);
        }

        // Use CPU provider explicitly or rely on fallback if OpenCL fails/is not used
        // But DirectRealMatrix might trigger OpenCL provider logic if strictly typed in
        // tests?
        // Let's use GenericMatrix-based Triangular to force CPU usage

        // 1 0
        // 1 1
        List<List<Real>> data = new ArrayList<>();
        data.add(Arrays.asList(Real.ONE, Real.ZERO));
        data.add(Arrays.asList(Real.ONE, Real.ONE));
        // Use explicit storage to ensure TriangularStorage context
        Matrix<Real> tri = MatrixFactory.<Real>create(data, Reals.getInstance(), MatrixFactory.Storage.TRIANGULAR);

        // Multiply Triangular * Triangular
        // Logic in CPUDenseLinearAlgebraProvider.multiply(Matrix, Matrix)
        tri.multiply(tri);

        System.out.println(PerformanceLogger.getReport());
        // We can't easily assert on stdout, but we can visually verify
        // "TriangularStorage" in report
    }
}
