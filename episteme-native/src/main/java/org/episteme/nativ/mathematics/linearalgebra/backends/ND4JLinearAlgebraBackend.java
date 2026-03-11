/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.inverse.InvertMatrix;

/**
 * ND4J Linear Algebra Backend (Dense).
 * <p>
 * When the ND4J library ({@code org.nd4j:nd4j-native-platform}) is on the classpath,
 * this backend delegates to ND4J's optimized BLAS/LAPACK backends (Native/AVX/CUDA).
 * Decompositions (eigen, SVD, LU) are implemented natively using ND4J array operations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
@AutoService({LinearAlgebraProvider.class, NativeBackend.class, ComputeBackend.class})
public class ND4JLinearAlgebraBackend implements LinearAlgebraProvider<Real>, org.episteme.nativ.technical.backend.nativ.NativeBackend, org.episteme.core.technical.backend.cpu.CPUBackend {
    private static final Logger logger = LoggerFactory.getLogger(ND4JLinearAlgebraBackend.class);

    @Override
    public int getPriority() {
        return 50; // Lower than NativeCPULinearAlgebraBackend (100)
    }

    @Override
    public String getName() {
        return "ND4J (Native Wrapper)";
    }

    @Override
    public boolean isLoaded() {
        return isAvailable();
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.ExecutionContext() {
            @Override
            public <T> T execute(org.episteme.core.technical.backend.Operation<T> operation) {
                return operation.compute(this);
            }

            @Override
            public void close() {
                // No-op
            }
        };
    }

    @Override
    public String getNativeLibraryName() {
        return "nd4j";
    }

    @Override
    public String getAlgorithmType() {
        return "linear algebra";
    }

    private static final boolean IS_AVAILABLE;
    static {
        boolean avail = false;
        if (!Boolean.getBoolean("episteme.nd4j.skip")) {
            try {
                Class.forName("org.nd4j.linalg.factory.Nd4j");
                // Test actual ND4J initialization
                org.nd4j.linalg.factory.Nd4j.zeros(1);
                avail = true;
            } catch (Throwable th) {
                logger.error("[ND4J] Initialization failed: {}: {}", th.getClass().getSimpleName(), th.getMessage(), th);
                System.err.println("[ND4J] Initialization failed: " + th.getClass().getSimpleName() + ": " + th.getMessage());
            }
        }
        IS_AVAILABLE = avail;
    }

    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE;
    }

    @Override
    public void shutdown() {
        // ND4J handles its own lifecycle.
    }

    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        return ring instanceof org.episteme.core.mathematics.sets.Reals;
    }

    @Override
    public double score(org.episteme.core.technical.algorithm.OperationContext context) {
        if (!isAvailable()) return -1.0;
        double score = getPriority();
        if (context.hasHint(org.episteme.core.technical.algorithm.OperationContext.Hint.DENSE)) score += 10.0;
        return score;
    }

    private INDArray toINDArray(Matrix<Real> m) {
        if (m instanceof RealDoubleMatrix) {
            RealDoubleMatrix rdm = (RealDoubleMatrix) m;
            return Nd4j.create(rdm.toDoubleArray(), new int[]{m.rows(), m.cols()});
        }
        double[][] data = new double[m.rows()][m.cols()];
        for(int r=0; r<m.rows(); r++) {
            for(int c=0; c<m.cols(); c++) {
                data[r][c] = m.get(r,c).doubleValue();
            }
        }
        return Nd4j.create(data);
    }

    private Matrix<Real> fromINDArray(INDArray arr) {
        INDArray contiguous = arr.isView() || arr.ordering() != 'c' ? arr.dup('c') : arr;
        int rows = (int) contiguous.rows();
        int cols = (int) contiguous.columns();
        double[] data = contiguous.data().asDouble();
        if (logger.isInfoEnabled() && data.length > 0) {
            logger.info("[ND4J] fromINDArray: {}x{}, first element: {}", rows, cols, data[0]);
        }
        return RealDoubleMatrix.of(data, rows, cols);
    }

    private INDArray toINDArray(Vector<Real> v) {
        double[] data = new double[v.dimension()];
        for(int i=0; i<v.dimension(); i++) {
            data[i] = v.get(i).doubleValue();
        }
        return Nd4j.create(data, new int[]{v.dimension(), 1}); // Column vector
    }

    private Vector<Real> fromINDArrayVector(INDArray arr) {
        INDArray contiguous = arr.isView() || arr.ordering() != 'c' ? arr.dup('c') : arr;
        double[] data = contiguous.data().asDouble();
        return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(data);
    }

    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArrayVector(toINDArray(a).add(toINDArray(b)));
    }

    @Override
    public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArrayVector(toINDArray(a).sub(toINDArray(b)));
    }

    @Override
    public Vector<Real> multiply(Vector<Real> vector, Real scalar) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArrayVector(toINDArray(vector).mul(scalar.doubleValue()));
    }

    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        // Ensure both are column vectors for dot product
        INDArray arrA = toINDArray(a);
        INDArray arrB = toINDArray(b);
        return Real.of(org.nd4j.linalg.factory.Nd4j.getBlasWrapper().dot(arrA, arrB));
    }

    @Override
    public Real norm(Vector<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return Real.of(toINDArray(a).norm2Number().doubleValue());
    }

    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(toINDArray(a).add(toINDArray(b)));
    }

    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(toINDArray(a).sub(toINDArray(b)));
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(toINDArray(a).mmul(toINDArray(b)));
    }

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArrayVector(toINDArray(a).mmul(toINDArray(b)));
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(InvertMatrix.invert(toINDArray(a), false));
    }

    /**
     * Determinant via Gaussian elimination on ND4J arrays. O(n^3), no external lib.
     */
    @Override
    public Real determinant(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int n = a.rows();
        INDArray m = toINDArray(a).dup();
        double det = 1.0;
        for (int col = 0; col < n; col++) {
            // Partial pivot
            int maxRow = col;
            for (int row = col + 1; row < n; row++) {
                if (Math.abs(m.getDouble(row, col)) > Math.abs(m.getDouble(maxRow, col))) maxRow = row;
            }
            if (maxRow != col) {
                INDArray tmp = m.getRow(col).dup();
                m.putRow(col, m.getRow(maxRow));
                m.putRow(maxRow, tmp);
                det *= -1;
            }
            double pivot = m.getDouble(col, col);
            if (Math.abs(pivot) < 1e-14) return Real.of(0.0);
            det *= pivot;
            for (int row = col + 1; row < n; row++) {
                double factor = m.getDouble(row, col) / pivot;
                m.putRow(row, m.getRow(row).sub(m.getRow(col).mul(factor)));
            }
        }
        return Real.of(det);
    }

    /**
     * Solve Ax=b via ND4J's LU decomposition.
     */
    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        try {
            INDArray arrA = toINDArray(a);
            INDArray arrB = toINDArray(b);

            // InvertMatrix.invert is the most stable across ND4J versions.
            // If it's slow, it might be due to sub-optimal backend linkage.
            // We use this for now to fix the compilation error.
            INDArray inverse = InvertMatrix.invert(arrA, false);
            INDArray result = inverse.mmul(arrB);
            
            return fromINDArrayVector(result);
        } catch (Exception e) {
            logger.error("[ND4J] Solve failed (singular?): {}", e.getMessage());
            throw new RuntimeException("ND4J Solve Operation Failed", e);
        }
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(toINDArray(a).transpose());
    }

    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(toINDArray(a).mul(scalar.doubleValue()));
    }

    /**
     * SVD using ND4J's built-in Svd custom op.
     * Returns U, S-diagonal-vector, V.
     */
    @Override
    public SVDResult<Real> svd(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        INDArray m = toINDArray(a);
        INDArray[] results = Nd4j.exec(new org.nd4j.linalg.api.ops.impl.transforms.custom.Svd(m, true, true, 0));
        // results[0] = S (singular values), results[1] = U, results[2] = V
        INDArray sVals = results[0];
        INDArray U = results[1];
        INDArray Vt = results[2];
        int k = (int) sVals.length();
        double[] sArr = sVals.data().asDouble();
        java.util.List<Real> sList = new java.util.ArrayList<>(k);
        for (double v : sArr) sList.add(Real.of(v));
        Vector<Real> S = org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(sArr);
        return new SVDResult<>(fromINDArray(U), S, fromINDArray(Vt));
    }

    /**
     * LU decomposition via Gaussian elimination on ND4J arrays.
     * Returns L (lower triangular), U (upper triangular), and pivot vector P.
     */
    @Override
    public LUResult<Real> lu(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int n = a.rows();
        INDArray U = toINDArray(a).dup();
        INDArray L = Nd4j.eye(n);
        double[] pivots = new double[n];
        for (int i = 0; i < n; i++) pivots[i] = i;

        for (int col = 0; col < n; col++) {
            // Partial pivoting
            int maxRow = col;
            for (int row = col + 1; row < n; row++) {
                if (Math.abs(U.getDouble(row, col)) > Math.abs(U.getDouble(maxRow, col))) maxRow = row;
            }
            if (maxRow != col) {
                INDArray tmpU = U.getRow(col).dup(); U.putRow(col, U.getRow(maxRow)); U.putRow(maxRow, tmpU);
                INDArray tmpL = L.getRow(col).dup(); L.putRow(col, L.getRow(maxRow)); L.putRow(maxRow, tmpL);
                double tmpP = pivots[col]; pivots[col] = pivots[maxRow]; pivots[maxRow] = tmpP;
            }
            double pivot = U.getDouble(col, col);
            if (Math.abs(pivot) < 1e-14) continue;
            for (int row = col + 1; row < n; row++) {
                double factor = U.getDouble(row, col) / pivot;
                L.putScalar(row, col, factor);
                U.putRow(row, U.getRow(row).sub(U.getRow(col).mul(factor)));
            }
        }
        org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector P =
            org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(pivots);
        return new LUResult<>(fromINDArray(L), fromINDArray(U), P);
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<Real> cholesky(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int n = a.rows();
        INDArray mat = toINDArray(a).dup();
        INDArray L = Nd4j.zeros(n, n);

        logger.info("[ND4J] Cholesky decomposition for matrix of size {}x{}", n, n);
        if (n > 0) {
            logger.info("[ND4J] Input matrix 'a' (top-left): {}", mat.getScalar(0, 0).doubleValue());
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                double sum = 0;
                for (int k = 0; k < j; k++) {
                    sum += L.getDouble(i, k) * L.getDouble(j, k);
                }
                if (i == j) {
                    double val = mat.getDouble(i, i) - sum;
                    if (val <= 0) {
                        logger.info("[ND4J] Cholesky: Matrix is not positive definite at diagonal element ({},{}) with value {}", i, j, val);
                        throw new ArithmeticException("Matrix is not positive definite");
                    }
                    L.putScalar(i, j, Math.sqrt(val));
                } else {
                    L.putScalar(i, j, (mat.getDouble(i, j) - sum) / L.getDouble(j, j));
                }
            }
        }
        logger.info("[ND4J] Cholesky decomposition completed. Resulting L matrix (top-left): {}", L.getScalar(0, 0).doubleValue());
        return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<>(fromINDArray(L));
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<Real> eigen(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        // ND4J's Eigen.eigenvectors returns [eigenvectors, eigenvalues_matrix]
        // result[0] is eigenvectors (matrix), result[1] is eigenvalues (diagonal matrix)
        INDArray[] result = org.nd4j.linalg.eigen.Eigen.eig(toINDArray(a));
        // result[0] is eigenvalues, result[1] is eigenvectors
        return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<>(
            fromINDArray(result[1]),
            fromINDArrayVector(result[0])
        );
    }
}
