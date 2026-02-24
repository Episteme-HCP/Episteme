/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.nativ.mathematics.linearalgebra.backends;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.matrices.solvers.EigenResult;
import org.jscience.core.mathematics.linearalgebra.matrices.solvers.SVDResult;
import org.jscience.core.mathematics.linearalgebra.matrices.solvers.LUResult;
import com.google.auto.service.AutoService;
import org.jscience.nativ.technical.backend.nativ.NativeBackend;
import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
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
@AutoService({LinearAlgebraProvider.class, NativeBackend.class, ComputeBackend.class, AlgorithmProvider.class})
public class ND4JLinearAlgebraBackend implements LinearAlgebraProvider<Real>, org.jscience.nativ.technical.backend.nativ.NativeBackend, org.jscience.core.technical.backend.cpu.CPUBackend {

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
    public org.jscience.core.technical.backend.ExecutionContext createContext() {
        return new org.jscience.core.technical.backend.ExecutionContext() {
            @Override
            public <T> T execute(org.jscience.core.technical.backend.Operation<T> operation) {
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
        boolean[] avail = {false};
        Thread t = new Thread(() -> {
            try {
                Class.forName("org.nd4j.linalg.factory.Nd4j");
                org.nd4j.linalg.factory.Nd4j.create(1).add(1);
                avail[0] = true;
            } catch (Throwable th) {
                // Ignore
            }
        });
        t.setDaemon(true);
        t.start();
        try {
            t.join(3000); // 3 seconds timeout for ND4J init
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        IS_AVAILABLE = avail[0];
    }

    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE;
    }

    @Override
    public boolean isCompatible(org.jscience.core.mathematics.structures.rings.Ring<?> ring) {
        return ring instanceof org.jscience.core.mathematics.sets.Reals;
    }

    @Override
    public double score(org.jscience.core.technical.algorithm.OperationContext context) {
        if (!isAvailable()) return -1.0;
        double score = getPriority();
        if (context.hasHint(org.jscience.core.technical.algorithm.OperationContext.Hint.DENSE)) score += 10.0;
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
        int rows = (int) arr.rows();
        int cols = (int) arr.columns();
        double[] data = arr.data().asDouble();
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
        double[] data = arr.data().asDouble();
        return org.jscience.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(data);
    }

    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
        return fromINDArrayVector(toINDArray(a).add(toINDArray(b)));
    }

    @Override
    public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
        return fromINDArrayVector(toINDArray(a).sub(toINDArray(b)));
    }

    @Override
    public Vector<Real> multiply(Vector<Real> vector, Real scalar) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
        return fromINDArrayVector(toINDArray(vector).mul(scalar.doubleValue()));
    }

    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
        return Real.of(org.nd4j.linalg.factory.Nd4j.getBlasWrapper().dot(toINDArray(a), toINDArray(b)));
    }

    @Override
    public Real norm(Vector<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
        return Real.of(toINDArray(a).norm2Number().doubleValue());
    }

    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
        return fromINDArray(toINDArray(a).add(toINDArray(b)));
    }

    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
        return fromINDArray(toINDArray(a).sub(toINDArray(b)));
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
        return fromINDArray(toINDArray(a).mmul(toINDArray(b)));
    }

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
        return fromINDArrayVector(toINDArray(a).mmul(toINDArray(b)));
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
        return fromINDArray(InvertMatrix.invert(toINDArray(a), false));
    }

    /**
     * Determinant via Gaussian elimination on ND4J arrays. O(n^3), no external lib.
     */
    @Override
    public Real determinant(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
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
     * Solve Ax=b via LU with ND4J inverse (A⁻¹ * b). For better performance, 
     * use LU factored form, but A⁻¹·b is sufficient and fully ND4J-native.
     */
    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
        INDArray arrA = toINDArray(a);
        INDArray arrB = toINDArray(b);
        INDArray invA = InvertMatrix.invert(arrA, false);
        return fromINDArrayVector(invA.mmul(arrB));
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
        return fromINDArray(toINDArray(a).transpose());
    }

    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
        return fromINDArray(toINDArray(a).mul(scalar.doubleValue()));
    }

    /**
     * SVD using ND4J's built-in Svd custom op.
     * Returns U, S-diagonal-vector, V.
     */
    @Override
    public SVDResult<Real> svd(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
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
        Vector<Real> S = org.jscience.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(sArr);
        return new SVDResult<>(fromINDArray(U), S, fromINDArray(Vt));
    }

    /**
     * Eigendecomposition via power iteration + deflation (Wielandt deflation).
     * Works for symmetric matrices. Self-contained, uses only ND4J array ops.
     */
    @Override
    public EigenResult<Real> eigen(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
        int n = a.rows();
        INDArray mat = toINDArray(a).dup();
        double[] eigenvalues = new double[n];
        double[][] eigenvectors = new double[n][n];

        INDArray deflated = mat.dup();
        for (int k = 0; k < n; k++) {
            // Power iteration to find dominant eigenvector
            INDArray v = Nd4j.rand(n, 1).sub(0.5);
            v = v.div(v.norm2Number().doubleValue());
            for (int iter = 0; iter < 500; iter++) {
                INDArray w = deflated.mmul(v);
                double norm = w.norm2Number().doubleValue();
                if (norm < 1e-14) break;
                v = w.div(norm);
            }
            // Rayleigh quotient for eigenvalue
            INDArray Av = deflated.mmul(v);
            double lambda = v.transpose().mmul(Av).getDouble(0);
            eigenvalues[k] = lambda;
            double[] evArr = v.data().asDouble();
            for (int i = 0; i < n; i++) eigenvectors[i][k] = evArr[i];

            // Deflate: A = A - lambda * v * v^T
            deflated = deflated.sub(v.mmul(v.transpose()).mul(lambda));
        }

        // Build result
        org.jscience.core.mathematics.linearalgebra.vectors.RealDoubleVector D =
            org.jscience.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(eigenvalues);
        double[] vFlat = new double[n * n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                vFlat[i * n + j] = eigenvectors[i][j];
        Matrix<Real> V = RealDoubleMatrix.of(vFlat, n, n);
        return new EigenResult<Real>(V, D);
    }

    /**
     * LU decomposition via Gaussian elimination on ND4J arrays.
     * Returns L (lower triangular), U (upper triangular), and pivot vector P.
     */
    @Override
    public LUResult<Real> lu(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
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
        org.jscience.core.mathematics.linearalgebra.vectors.RealDoubleVector P =
            org.jscience.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(pivots);
        return new LUResult<>(fromINDArray(L), fromINDArray(U), P);
    }
}
