/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import com.google.auto.service.AutoService;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.inverse.InvertMatrix;

/**
 * NativeND4J Linear Algebra Backend (Dense).
 * <p>
 * When the ND4J library ({@code org.nd4j:nd4j-native-platform}) is on the classpath,
 * this backend delegates to ND4J's optimized BLAS/LAPACK backends (Native/AVX/CUDA).
 * Decompositions (eigen, SVD, LU) are implemented natively using ND4J array operations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, CPUBackend.class})
public class NativeND4JLinearAlgebraBackend implements LinearAlgebraProvider<Real>, NativeBackend, CPUBackend {
    private static final Logger logger = LoggerFactory.getLogger(NativeND4JLinearAlgebraBackend.class);



    public NativeND4JLinearAlgebraBackend() {
        // available field is deprecated in favor of static IS_AVAILABLE
    }

    @Override
    public int getPriority() {
        return 50; // Lower than NativeCPULinearAlgebraBackend (100)
    }

    @Override
    public String getName() {
        return "ND4J (Native-Dense)";
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        // ND4J can use GPU but here it is declared as CPUBackend.
        // We return CPU for now as it's the default native-platform behavior.
        return HardwareAccelerator.CPU;
    }

    @Override
    public String getEnvironmentInfo() {
        if (!isAvailable()) return "N/A";
        return "CPU (Native ND4J)";
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

    private static boolean initAttempted = false;
    private static boolean IS_AVAILABLE = false;

    private static synchronized void init() {
        if (initAttempted) return;
        initAttempted = true;
        if (!Boolean.getBoolean("episteme.backend.nd4j.disabled") && !Boolean.getBoolean("episteme.backend.disable.nd4j")) {
            try {
                Class.forName("org.nd4j.linalg.factory.Nd4j");
                // Test actual ND4J initialization
                org.nd4j.linalg.factory.Nd4j.zeros(1);
                IS_AVAILABLE = true;
            } catch (Throwable th) {
                logger.error("[ND4J] Initialization failed: {}: {}", th.getClass().getSimpleName(), th.getMessage(), th);
                System.err.println("[ND4J] Initialization failed: " + th.getClass().getSimpleName() + ": " + th.getMessage());
            }
        }
    }

    @Override
    public boolean isAvailable() {
        if (isExplicitlyDisabled()) return false;
        if (!initAttempted) init();
        return IS_AVAILABLE;
    }

    @Override
    public String getId() {
        return "nd4j";
    }

    @Override
    public boolean isExplicitlyDisabled() {
        String id = getId();
        return (id != null && Boolean.getBoolean("episteme.backend." + id + ".disabled")) || 
               Boolean.getBoolean("episteme.native.disable") ||
               Boolean.getBoolean("episteme.backend.disable." + id);
    }

    @Override
    public String getType() {
        return "linear-algebra";
    }

    @Override
    public void shutdown() {
        // ND4J handles its own lifecycle.
    }

    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        if (!(ring instanceof org.episteme.core.mathematics.sets.Reals)) return false;
        return !org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision();
    }

    @Override
    public double score(org.episteme.core.technical.algorithm.OperationContext context) {
        if (!isAvailable()) return -1.0;
        double score = getPriority();
        if (context.hasHint(org.episteme.core.technical.algorithm.OperationContext.Hint.DENSE)) score += 10.0;
        return score;
    }

    private INDArray toINDArray(Matrix<Real> m) {
        if (m instanceof org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) {
            org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix rdm = (org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) m;
            return Nd4j.create(rdm.toDoubleArray(), new int[]{m.rows(), m.cols()}, 'c');
        }
        
        // Float path optimization
        if (m.getScalarRing().zero() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
             float[] data = new float[m.rows() * m.cols()];
             for(int r=0; r<m.rows(); r++) {
                 for(int c=0; c<m.cols(); c++) {
                     data[r*m.cols() + c] = m.get(r,c).floatValue();
                 }
             }
             return Nd4j.create(data, new int[]{m.rows(), m.cols()}, 'c');
        }

        double[][] data = new double[m.rows()][m.cols()];
        for(int r=0; r<m.rows(); r++) {
            for(int c=0; c<m.cols(); c++) {
                data[r][c] = m.get(r,c).doubleValue();
            }
        }
        return Nd4j.create(data);
    }

    private Matrix<Real> fromINDArray(INDArray array) {
        if (array == null) return null;
        if (array.rank() > 2) {
            logger.debug("fromINDArray: Slicing array of rank {} to rank 2", array.rank());
            while (array.rank() > 2) array = array.slice(0);
        }
        if (array.rank() == 1) {
            array = array.reshape(1, array.length());
        }
        int rows = (int) array.rows();
        int cols = (int) array.columns();
        logger.debug("[ND4J] fromINDArray: {}x{}, first element: {}, ordering: {}", rows, cols, array.getDouble(0), array.ordering());
        double[] data = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i * cols + j] = array.getDouble(i, j);
            }
        }
        return RealDoubleMatrix.of(data, rows, cols, this);
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
        return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(new org.episteme.core.mathematics.linearalgebra.vectors.storage.HeapRealDoubleVectorStorage(data), this);
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
    public Vector<Real> solveTriangular(Matrix<Real> A, Vector<Real> b, boolean upper, boolean transpose, boolean conjugate, boolean unit) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int n = A.rows();
        if (n != A.cols()) throw new IllegalArgumentException("Matrix must be square");
        if (n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");

        INDArray arrA = toINDArray(A);
        INDArray arrB = toINDArray(b).dup(); // We modify B in-place

        // Manual forward/backward substitution using ND4J accessors
        if (upper) {
            if (transpose) {
                // Forward substitution (L^T y = b) where L^T is Lower
                for (int i = 0; i < n; i++) {
                    double sum = arrB.getDouble(i);
                    for (int j = 0; j < i; j++) {
                        sum -= arrA.getDouble(j, i) * arrB.getDouble(j);
                    }
                    if (!unit) sum /= arrA.getDouble(i, i);
                    arrB.putScalar(i, sum);
                }
            } else {
                // Backward substitution (U x = b)
                for (int i = n - 1; i >= 0; i--) {
                    double sum = arrB.getDouble(i);
                    for (int j = i + 1; j < n; j++) {
                        sum -= arrA.getDouble(i, j) * arrB.getDouble(j);
                    }
                    if (!unit) sum /= arrA.getDouble(i, i);
                    arrB.putScalar(i, sum);
                }
            }
        } else {
            if (transpose) {
                // Backward substitution (U^T x = b) where U^T is Upper
                for (int i = n - 1; i >= 0; i--) {
                    double sum = arrB.getDouble(i);
                    for (int j = i + 1; j < n; j++) {
                        sum -= arrA.getDouble(j, i) * arrB.getDouble(j);
                    }
                    if (!unit) sum /= arrA.getDouble(i, i);
                    arrB.putScalar(i, sum);
                }
            } else {
                // Forward substitution (L y = b)
                for (int i = 0; i < n; i++) {
                    double sum = arrB.getDouble(i);
                    for (int j = 0; j < i; j++) {
                        sum -= arrA.getDouble(i, j) * arrB.getDouble(j);
                    }
                    if (!unit) sum /= arrA.getDouble(i, i);
                    arrB.putScalar(i, sum);
                }
            }
        }

        return fromINDArrayVector(arrB);
    }

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArrayVector(toINDArray(a).mmul(toINDArray(b)));
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        INDArray arr = toINDArray(a);
        if (a.rows() == a.cols()) {
            return fromINDArray(InvertMatrix.invert(arr, false));
        } else {
            // For rectangular, ND4J has InvertMatrix.pInvert for pseudo-inverse
            return fromINDArray(InvertMatrix.pinvert(arr, false));
        }
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
        int swaps = 0;
        
        for (int k = 0; k < n; k++) {
            // Find pivot
            int maxIdx = k;
            double maxVal = Math.abs(m.getDouble(k, k));
            for (int i = k + 1; i < n; i++) {
                double v = Math.abs(m.getDouble(i, k));
                if (v > maxVal) { maxVal = v; maxIdx = i; }
            }
            
            if (maxVal < 1e-15) return Real.of(0.0); // Singular
            
            if (maxIdx != k) {
                // Swap rows k and maxIdx
                for (int j = 0; j < n; j++) {
                    double tmp = m.getDouble(k, j);
                    m.putScalar(k, j, m.getDouble(maxIdx, j));
                    m.putScalar(maxIdx, j, tmp);
                }
                swaps++;
            }
            
            det *= m.getDouble(k, k);
            
            // Eliminate below
            for (int i = k + 1; i < n; i++) {
                double factor = m.getDouble(i, k) / m.getDouble(k, k);
                for (int j = k + 1; j < n; j++) {
                    m.putScalar(i, j, m.getDouble(i, j) - factor * m.getDouble(k, j));
                }
                m.putScalar(i, k, 0.0);
            }
        }
        
        if (swaps % 2 != 0) det = -det;
        return Real.of(det);
    }

    /**
     * Solve Ax=b via ND4J's LU decomposition.
     */
    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        try {
            INDArray arrA = toINDArray(a).dup(); // dup because gels might modify it
            INDArray arrB = toINDArray(b).dup();

            if (a.rows() == a.cols()) {
                // InvertMatrix.invert is reliable for square
                INDArray inverse = InvertMatrix.invert(arrA, false);
                return fromINDArrayVector(inverse.mmul(arrB));
            } else {
                // For rectangular, use gels (Least Squares)
                // Note: gels usually needs a specific work array and handles dimensions.
                // ND4J's wrapper might handle it.
                // If gels is not convenient, we use pseudo-inverse: x = A+ * b
                INDArray pinv = InvertMatrix.pinvert(arrA, false);
                return fromINDArrayVector(pinv.mmul(arrB));
            }
        } catch (Exception e) {
            logger.error("[ND4J] Solve failed: {}", e.getMessage());
            throw new RuntimeException("ND4J Solve Operation Failed", e);
        }
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(toINDArray(a).transpose());
    }

    /**
     * QR decomposition via ND4J's 'qr' custom op.
     */
    @Override
    public QRResult<Real> qr(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        INDArray A = toINDArray(a);
        
        // Use DynamicCustomOp for QR: returns Q and R
        org.nd4j.linalg.api.ops.DynamicCustomOp op = org.nd4j.linalg.api.ops.DynamicCustomOp.builder("qr")
                .addInputs(A)
                .build();
        INDArray[] outputs = Nd4j.getExecutioner().exec(op);
        
        return new QRResult<Real>(fromINDArray(outputs[0]), fromINDArray(outputs[1]));
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
        int m = a.rows();
        int n = a.cols();
        INDArray A = toINDArray(a);
        
        // LAPACK gesvd: 'A' for all columns of U, 'A' for all columns of V
        INDArray S = Nd4j.create(org.nd4j.linalg.api.buffer.DataType.DOUBLE, new long[]{Math.min(m, n)});
        INDArray U = Nd4j.create(org.nd4j.linalg.api.buffer.DataType.DOUBLE, new long[]{m, m});
        INDArray Vt = Nd4j.create(org.nd4j.linalg.api.buffer.DataType.DOUBLE, new long[]{n, n});
        
        try {
            Nd4j.getBlasWrapper().lapack().gesvd(A.dup(), S, U, Vt);
        } catch (Exception e) {
            logger.error("[ND4J] SVD failed: {}", e.getMessage());
            // Fallback or rethrow with more context
            throw new RuntimeException("ND4J SVD Failed", e);
        }
        
        INDArray contiguousU = U.isView() || U.ordering() != 'c' ? U.dup('c') : U;
        INDArray contiguousVt = Vt.isView() || Vt.ordering() != 'c' ? Vt.dup('c') : Vt;
        
        // SVDResult expects V, but gesvd returns Vt. So we transpose Vt back to V.
        return new SVDResult<Real>(fromINDArray(contiguousU), fromINDArrayVector(S), fromINDArray(contiguousVt.transpose()));
    }

    /**
     * LU decomposition via Gaussian elimination on ND4J arrays.
     * Returns L (lower triangular), U (upper triangular), and pivot vector P.
     */
    @Override
    public LUResult<Real> lu(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int n = a.rows();
        
        // Pure Java LU decomposition with partial pivoting to avoid ND4J ordering issues
        double[][] work = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                work[i][j] = a.get(i, j).doubleValue();
        
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) perm[i] = i;
        
        for (int k = 0; k < n; k++) {
            // Find pivot
            int maxIdx = k;
            double maxVal = Math.abs(work[k][k]);
            for (int i = k + 1; i < n; i++) {
                double v = Math.abs(work[i][k]);
                if (v > maxVal) { maxVal = v; maxIdx = i; }
            }
            
            if (maxIdx != k) {
                // Swap rows
                double[] tmp = work[k];
                work[k] = work[maxIdx];
                work[maxIdx] = tmp;
                int tmpP = perm[k]; perm[k] = perm[maxIdx]; perm[maxIdx] = tmpP;
            }
            
            // Gaussian elimination
            double pivot = work[k][k];
            if (Math.abs(pivot) > 1e-15) {
                for (int i = k + 1; i < n; i++) {
                    double factor = work[i][k] / pivot;
                    work[i][k] = factor; // Store L factor in lower part
                    for (int j = k + 1; j < n; j++) {
                        work[i][j] -= factor * work[k][j];
                    }
                }
            }
        }
        
        // Extract L and U
        double[] lFlat = new double[n * n];
        double[] uFlat = new double[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i > j) {
                    lFlat[i * n + j] = work[i][j];
                } else if (i == j) {
                    lFlat[i * n + j] = 1.0;
                    uFlat[i * n + j] = work[i][j];
                } else {
                    uFlat[i * n + j] = work[i][j];
                }
            }
        }
        
        double[] p = new double[n];
        for (int i = 0; i < n; i++) p[i] = perm[i];
        
        return new LUResult<Real>(
            RealDoubleMatrix.of(lFlat, n, n),
            RealDoubleMatrix.of(uFlat, n, n),
            org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(p)
        );
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<Real> cholesky(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int n = a.rows();
        
        // Pure Java Cholesky decomposition to avoid ND4J potrf ordering issues
        double[][] aData = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                aData[i][j] = a.get(i, j).doubleValue();
        
        double[][] lData = new double[n][n];
        for (int j = 0; j < n; j++) {
            double sum = 0;
            for (int k = 0; k < j; k++) sum += lData[j][k] * lData[j][k];
            double diag = aData[j][j] - sum;
            if (diag <= 0) throw new ArithmeticException("Matrix is not positive definite");
            lData[j][j] = Math.sqrt(diag);
            for (int i = j + 1; i < n; i++) {
                sum = 0;
                for (int k = 0; k < j; k++) sum += lData[i][k] * lData[j][k];
                lData[i][j] = (aData[i][j] - sum) / lData[j][j];
            }
        }
        
        // Flatten row-major
        double[] flat = new double[n * n];
        for (int i = 0; i < n; i++)
            System.arraycopy(lData[i], 0, flat, i * n, n);
        
        return new CholeskyResult<Real>(RealDoubleMatrix.of(flat, n, n));
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<Real> eigen(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        
        // For efficiency and robustness with standard ND4J, we use Jacobi for symmetric matrices
        boolean isSymmetric = true;
        int n = a.rows();
        for (int i = 0; i < n && isSymmetric; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(a.get(i, j).doubleValue() - a.get(j, i).doubleValue()) > 1e-10) {
                    isSymmetric = false;
                    break;
                }
            }
        }

        if (isSymmetric) {
            return jacobi(a);
        }

        // Fallback to ND4J native eig for non-symmetric
        try {
            INDArray[] result = org.nd4j.linalg.eigen.Eigen.eig(toINDArray(a));
            INDArray eigVals = result[0];
            INDArray eigVecs = result[1];
            
            if (eigVals.rank() == 2 && eigVals.columns() == 2) {
                 eigVals = eigVals.getColumn(0).dup(); // Take real part
            }
            if (eigVecs.rank() == 3) {
                eigVecs = eigVecs.get(org.nd4j.linalg.indexing.NDArrayIndex.all(), org.nd4j.linalg.indexing.NDArrayIndex.all(), org.nd4j.linalg.indexing.NDArrayIndex.point(0)).dup(); 
            }
            
            return new EigenResult<Real>(
                fromINDArray(eigVecs),
                org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(eigVals.data().asDouble())
            );
        } catch (Exception e) {
            logger.warn("ND4J Native eig failed: {}. Falling back to Jacobi (as approximate).", e.getMessage());
            return jacobi(a);
        }
    }

    @Override
    public Real trace(org.episteme.core.mathematics.linearalgebra.Matrix<Real> a) {
        System.out.println("[DEBUG] ND4J trace() called");
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        // ND4J doesn't have a direct trace() on INDArray in all versions, 
        // but we can sum the diagonal.
        INDArray arr = toINDArray(a);
        int n = Math.min(a.rows(), a.cols());
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += arr.getDouble(i, i);
        }
        return Real.of(sum);
    }

    private EigenResult<Real> jacobi(Matrix<Real> a) {
        int n = a.rows();
        double[] data = new double[n * n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i * n + j] = a.get(i, j).doubleValue();
        
        double[] vData = new double[n * n];
        for (int i = 0; i < n; i++) vData[i * n + i] = 1.0;
        
        int maxSweeps = 50;
        double eps = 1e-15;
        
        for (int sweep = 0; sweep < maxSweeps; sweep++) {
            double offDiag = 0;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) offDiag += Math.abs(data[i * n + j]);
            }
            if (offDiag < eps) break;
            
            for (int p = 0; p < n - 1; p++) {
                for (int q = p + 1; q < n; q++) {
                    double apq = data[p * n + q];
                    if (Math.abs(apq) < eps) continue;
                    
                    double app = data[p * n + p];
                    double aqq = data[q * n + q];
                    
                    double tau = (aqq - app) / (2.0 * apq);
                    double t = (tau >= 0) ? 1.0 / (tau + Math.sqrt(1.0 + tau * tau)) 
                                         : 1.0 / (tau - Math.sqrt(1.0 + tau * tau));
                    double c = 1.0 / Math.sqrt(1.0 + t * t);
                    double s = t * c;

                    for (int i = 0; i < n; i++) {
                        double tp = data[p * n + i];
                        double tq = data[q * n + i];
                        data[p * n + i] = c * tp - s * tq;
                        data[q * n + i] = s * tp + c * tq;
                    }
                    for (int j = 0; j < n; j++) {
                        data[j * n + p] = data[p * n + j];
                        data[j * n + q] = data[q * n + j];
                    }
                    
                    data[p * n + p] = app - t * apq;
                    data[q * n + q] = aqq + t * apq;
                    data[p * n + q] = 0.0;
                    data[q * n + p] = 0.0;
                    
                    for (int i = 0; i < n; i++) {
                        double vp = vData[i * n + p];
                        double vq = vData[i * n + q];
                        vData[i * n + p] = c * vp - s * vq;
                        vData[i * n + q] = s * vp + c * vq;
                    }
                }
            }
        }
        
        double[] eigenvalues = new double[n];
        for (int i = 0; i < n; i++) eigenvalues[i] = data[i * n + i];
        
        return new EigenResult<>(
            RealDoubleMatrix.of(vData, n, n),
            org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(eigenvalues)
        );
    }
}
