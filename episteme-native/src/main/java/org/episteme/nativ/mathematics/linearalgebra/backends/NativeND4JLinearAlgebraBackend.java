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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.inverse.InvertMatrix;
import org.nd4j.linalg.ops.transforms.Transforms;
import org.episteme.core.mathematics.structures.rings.Ring;

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
public class NativeND4JLinearAlgebraBackend implements LinearAlgebraProvider<Object>, NativeBackend, CPUBackend {
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
        return "ND4J (Native Wrapper)";
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

    private static final boolean IS_AVAILABLE;
    static {
        boolean avail = false;
        if (true) { // Disabling now handled by Backend.isAvailable()
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
        return IS_AVAILABLE && !isExplicitlyDisabled();
    }

    @Override
    public String getId() {
        return "nd4j";
    }

    @Override
    public String getType() {
        return "math";
    }

    @Override
    public void shutdown() {
        // ND4J handles its own lifecycle.
    }

    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        return ring instanceof org.episteme.core.mathematics.sets.Reals || ring instanceof org.episteme.core.mathematics.sets.Complexes;
    }

    @Override
    public double score(org.episteme.core.technical.algorithm.OperationContext context) {
        if (!isAvailable()) return -1.0;
        double score = getPriority();
        if (context.hasHint(org.episteme.core.technical.algorithm.OperationContext.Hint.DENSE)) score += 10.0;
        return score;
    }

    private INDArray toINDArray(Matrix<Object> m) {
        Ring<?> ring = m.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            Object zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                float[] data = new float[m.rows() * m.cols()];
                for (int i=0; i<m.rows(); i++) for (int j=0; j<m.cols(); j++) data[i*m.cols()+j] = ((Real)m.get(i, j)).floatValue();
                return Nd4j.create(data, new int[]{m.rows(), m.cols()}, 'c');
            }
            double[] data = new double[m.rows() * m.cols()];
            for (int i=0; i<m.rows(); i++) for (int j=0; j<m.cols(); j++) data[i*m.cols()+j] = ((Real)m.get(i, j)).doubleValue();
            return Nd4j.create(data, new int[]{m.rows(), m.cols()}, 'c');
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            Object zero = ring.zero();
            boolean isFloat = (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat);
            if (isFloat) {
                INDArray res = Nd4j.create(org.nd4j.linalg.api.buffer.DataType.valueOf("COMPLEXFLOAT"), m.rows(), m.cols());
                for (int i=0; i<m.rows(); i++) for (int j=0; j<m.cols(); j++) {
                    org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex)m.get(i, j);
                    res.data().put(2*(i*m.cols()+j), (float) c.real());
                    res.data().put(2*(i*m.cols()+j)+1, (float) c.imaginary());
                }
                return res;
            } else {
                double[] data = new double[2 * m.rows() * m.cols()];
                for (int i=0; i<m.rows(); i++) for (int j=0; j<m.cols(); j++) {
                    org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex)m.get(i, j);
                    data[2*(i*m.cols()+j)] = c.real();
                    data[2*(i*m.cols()+j)+1] = c.imaginary();
                }
                INDArray res = Nd4j.create(org.nd4j.linalg.api.buffer.DataType.valueOf("COMPLEXDOUBLE"), m.rows(), m.cols());
                for (int i=0; i<m.rows(); i++) for (int j=0; j<m.cols(); j++) {
                    org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) m.get(i, j);
                    res.data().put(2*(i*m.cols()+j), c.real());
                    res.data().put(2*(i*m.cols()+j)+1, c.imaginary());
                }
                return res;
            }
        }
        throw new UnsupportedOperationException("Unsupported ring: " + ring);
    }

    private Matrix<Object> fromINDArray(INDArray array, Ring<?> ring) {
        if (array == null) return null;
        int rows = (int) array.rows();
        int cols = (int) array.columns();
        
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            Object zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                org.episteme.core.mathematics.numbers.real.Real[] data = new org.episteme.core.mathematics.numbers.real.Real[rows * cols];
                for (int i=0; i<rows; i++) for (int j=0; j<cols; j++) data[i*cols+j] = org.episteme.core.mathematics.numbers.real.RealFloat.of(array.getFloat(i, j));
                return Matrix.of(data, rows, cols, ring);
            }
            double[] data = new double[rows * cols];
            for (int i=0; i<rows; i++) for (int j=0; j<cols; j++) data[i*cols+j] = array.getDouble(i, j);
            return (Matrix<Object>)(Object) RealDoubleMatrix.of(data, rows, cols);
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            Object zero = ring.zero();
            boolean isFloat = (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat);
            org.episteme.core.mathematics.numbers.complex.Complex[] data = new org.episteme.core.mathematics.numbers.complex.Complex[rows * cols];
            for (int i=0; i<rows; i++) for (int j=0; j<cols; j++) {
                if (isFloat) {
                    org.nd4j.linalg.api.complex.IComplexFloat c = array.getComplexFloat((long)i, (long)j);
                    data[i*cols+j] = org.episteme.core.mathematics.numbers.complex.Complex.of(org.episteme.core.mathematics.numbers.real.RealFloat.of(c.realComponent().floatValue()), org.episteme.core.mathematics.numbers.real.RealFloat.of(c.imaginaryComponent().floatValue()));
                } else {
                    org.nd4j.linalg.api.complex.IComplexDouble c = array.getComplexDouble((long)i, (long)j);
                    data[i*cols+j] = org.episteme.core.mathematics.numbers.complex.Complex.of(c.realComponent().doubleValue(), c.imaginaryComponent().doubleValue());
                }
            }
            return Matrix.of(data, rows, cols, ring);
        }
        throw new UnsupportedOperationException("Unsupported ring: " + ring);
    }

    private INDArray toINDArray(Vector<Object> v) {
        Ring<?> ring = v.getScalarRing();
        int n = v.dimension();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            Object zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                float[] data = new float[n];
                for (int i=0; i<n; i++) data[i] = ((Real)v.get(i)).floatValue();
                return Nd4j.create(data, new int[]{n, 1});
            }
            double[] data = new double[n];
            for (int i=0; i<n; i++) data[i] = ((Real)v.get(i)).doubleValue();
            return Nd4j.create(data, new int[]{n, 1});
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            Object zero = ring.zero();
            boolean isFloat = (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat);
            if (isFloat) {
                INDArray res = Nd4j.create(org.nd4j.linalg.api.buffer.DataType.valueOf("COMPLEXFLOAT"), n, 1);
                for (int i=0; i<n; i++) {
                    org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex)v.get(i);
                    res.data().put(2*i, (float) c.real());
                    res.data().put(2*i+1, (float) c.imaginary());
                }
                return res;
            } else {
                INDArray res = Nd4j.create(org.nd4j.linalg.api.buffer.DataType.valueOf("COMPLEXDOUBLE"), n, 1);
                for (int i=0; i<n; i++) {
                    org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex)v.get(i);
                    res.data().put(2*i, c.real());
                    res.data().put(2*i+1, c.imaginary());
                }
                return res;
            }
        }
        throw new UnsupportedOperationException("Unsupported ring: " + ring);
    }

    private Vector<Object> fromINDArrayVector(INDArray arr, Ring<?> ring) {
        if (arr == null) return null;
        int n = (int) arr.length();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            Object zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                org.episteme.core.mathematics.numbers.real.Real[] data = new org.episteme.core.mathematics.numbers.real.Real[n];
                for (int i=0; i<n; i++) data[i] = org.episteme.core.mathematics.numbers.real.RealFloat.of(arr.getFloat(i));
                return Vector.of(java.util.Arrays.asList(data), ring);
            }
            double[] data = arr.isView() || arr.ordering() != 'c' ? arr.dup('c').data().asDouble() : arr.data().asDouble();
            return (Vector<Object>)(Object) org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(data);
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            Object zero = ring.zero();
            boolean isFloat = (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat);
            org.episteme.core.mathematics.numbers.complex.Complex[] data = new org.episteme.core.mathematics.numbers.complex.Complex[n];
            for (int i=0; i<n; i++) {
                if (isFloat) {
                    org.nd4j.linalg.api.complex.IComplexFloat c = arr.getComplexFloat((long)i);
                    data[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(org.episteme.core.mathematics.numbers.real.RealFloat.of(c.realComponent().floatValue()), org.episteme.core.mathematics.numbers.real.RealFloat.of(c.imaginaryComponent().floatValue()));
                } else {
                    org.nd4j.linalg.api.complex.IComplexDouble c = arr.getComplexDouble((long)i);
                    data[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(c.realComponent().doubleValue(), c.imaginaryComponent().doubleValue());
                }
            }
            return Vector.of(java.util.Arrays.asList(data), ring);
        }
        throw new UnsupportedOperationException("Unsupported ring: " + ring);
    }

    @Override
    public Vector<Object> add(Vector<Object> a, Vector<Object> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArrayVector(toINDArray(a).add(toINDArray(b)), a.getScalarRing());
    }

    @Override
    public Vector<Object> subtract(Vector<Object> a, Vector<Object> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArrayVector(toINDArray(a).sub(toINDArray(b)), a.getScalarRing());
    }

    @Override
    public Vector<Object> multiply(Vector<Object> vector, Object scalar) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        INDArray arr = toINDArray(vector);
        Ring<?> ring = vector.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            return fromINDArrayVector(arr.mul(((Real)scalar).doubleValue()), ring);
        } else {
            org.episteme.core.mathematics.numbers.complex.Complex s = (org.episteme.core.mathematics.numbers.complex.Complex)scalar;
            INDArray sc = Nd4j.create(org.nd4j.linalg.api.buffer.DataType.valueOf("COMPLEXDOUBLE"), 1, 1);
            sc.data().put(0, s.real());
            sc.data().put(1, s.imaginary());
            INDArray vRes = arr.mul(sc);
            return fromINDArrayVector(vRes, ring);
        }
    }

    @Override
    public Object dot(Vector<Object> a, Vector<Object> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        INDArray arrA = toINDArray(a);
        INDArray arrB = toINDArray(b);
        INDArray res = arrA.transpose().mmul(arrB);
        
        Ring<?> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            Object zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                return org.episteme.core.mathematics.numbers.real.RealFloat.of(res.getFloat(0));
            }
            return org.episteme.core.mathematics.numbers.real.Real.of(res.getDouble(0));
        } else {
            Object zero = ring.zero();
            boolean isFloat = (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat);
            if (isFloat) {
                return org.episteme.core.mathematics.numbers.complex.Complex.of(org.episteme.core.mathematics.numbers.real.RealFloat.of(res.getFloat(0, 0)), org.episteme.core.mathematics.numbers.real.RealFloat.of(res.getFloat(0, 1)));
            }
            return org.episteme.core.mathematics.numbers.complex.Complex.of(res.getDouble(0, 0), res.getDouble(0, 1));
        }
    }

    @Override
    public Object norm(Vector<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        INDArray arr = toINDArray(a);
        double n = arr.norm2Number().doubleValue();
        
        Ring<?> ring = a.getScalarRing();
        Object zero = ring.zero();
        boolean isFloat = (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) || 
                         (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat);
        
        if (isFloat) return org.episteme.core.mathematics.numbers.real.RealFloat.of((float)n);
        return org.episteme.core.mathematics.numbers.real.Real.of(n);
    }

    @Override
    public Matrix<Object> add(Matrix<Object> a, Matrix<Object> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(toINDArray(a).add(toINDArray(b)), a.getScalarRing());
    }

    @Override
    public Matrix<Object> subtract(Matrix<Object> a, Matrix<Object> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(toINDArray(a).sub(toINDArray(b)), a.getScalarRing());
    }

    @Override
    public Matrix<Object> multiply(Matrix<Object> a, Matrix<Object> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(toINDArray(a).mmul(toINDArray(b)), a.getScalarRing());
    }

    @Override
    public Vector<Object> multiply(Matrix<Object> a, Vector<Object> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArrayVector(toINDArray(a).mmul(toINDArray(b)), a.getScalarRing());
    }

    @Override
    public Matrix<Object> inverse(Matrix<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        INDArray arr = toINDArray(a);
        if (a.rows() == a.cols()) {
            return fromINDArray(org.nd4j.linalg.inverse.InvertMatrix.invert(arr, false), a.getScalarRing());
        } else {
            return fromINDArray(org.nd4j.linalg.inverse.InvertMatrix.pinvert(arr, false), a.getScalarRing());
        }
    }

    /**
     * Determinant via Gaussian elimination on ND4J arrays. O(n^3), no external lib.
     */
    @Override
    public Object determinant(Matrix<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        INDArray m = toINDArray(a);
        INDArray detArr = Nd4j.det(m);
        double det = detArr.getDouble(0);
        Ring<?> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            return Real.of(det);
        } else {
            // Check if complex via name match
            if (detArr.dataType().name().contains("COMPLEX")) {
                org.nd4j.linalg.api.complex.IComplexDouble c = detArr.getComplexDouble(0L);
                return org.episteme.core.mathematics.numbers.complex.Complex.of(c.realComponent().doubleValue(), c.imaginaryComponent().doubleValue());
            }
            return org.episteme.core.mathematics.numbers.complex.Complex.of(det);
        }
    }

    /**
     * Solve Ax=b via ND4J's LU decomposition.
     */
    @Override
    public Vector<Object> solve(Matrix<Object> a, Vector<Object> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        INDArray arrA = toINDArray(a);
        INDArray arrB = toINDArray(b);

        if (a.rows() == a.cols()) {
            INDArray resArr = org.nd4j.linalg.inverse.InvertMatrix.invert(arrA, false);
            resArr = resArr.mmul(arrB);
            return fromINDArrayVector(resArr, a.getScalarRing());
        } else {
            INDArray pinvA = org.nd4j.linalg.inverse.InvertMatrix.pinvert(arrA, false);
            INDArray resArr = pinvA.mmul(arrB);
            return fromINDArrayVector(resArr, a.getScalarRing());
        }
    }

    @Override
    public Matrix<Object> transpose(Matrix<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(toINDArray(a).transpose(), a.getScalarRing());
    }

    /**
     * QR decomposition via ND4J's 'qr' custom op.
     */
    @Override
    public QRResult<Object> qr(Matrix<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        INDArray A = toINDArray(a);
        org.nd4j.linalg.api.ops.DynamicCustomOp op = org.nd4j.linalg.api.ops.DynamicCustomOp.builder("qr").addInputs(A).build();
        INDArray[] outputs = Nd4j.getExecutioner().exec(op);
        return new QRResult<Object>(fromINDArray(outputs[0], a.getScalarRing()), fromINDArray(outputs[1], a.getScalarRing()));
    }

    @Override
    public Matrix<Object> scale(Object scalar, Matrix<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        INDArray arr = toINDArray(a);
        Ring<?> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            return fromINDArray(arr.mul(((Real)scalar).doubleValue()), ring);
        } else {
            org.episteme.core.mathematics.numbers.complex.Complex s = (org.episteme.core.mathematics.numbers.complex.Complex)scalar;
            INDArray sc = Nd4j.create(org.nd4j.linalg.api.buffer.DataType.valueOf("COMPLEXDOUBLE"), 1, 1);
            sc.data().put(0, s.real());
            sc.data().put(1, s.imaginary());
            INDArray mRes = arr.mul(sc);
            return fromINDArray(mRes, ring);
        }
    }

    /**
     * SVD using ND4J's built-in Svd custom op.
     * Returns U, S-diagonal-vector, V.
     */
    @Override
    public SVDResult<Object> svd(Matrix<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int m = a.rows();
        int n = a.cols();
        INDArray A = toINDArray(a);
        
        INDArray S = Nd4j.create(A.dataType(), new long[]{Math.min(m, n)});
        INDArray U = Nd4j.create(A.dataType(), new long[]{m, m});
        INDArray Vt = Nd4j.create(A.dataType(), new long[]{n, n});
        
        Nd4j.getBlasWrapper().lapack().gesvd(A.dup(), S, U, Vt);
        
        return new SVDResult<Object>(fromINDArray(U, a.getScalarRing()), fromINDArrayVector(S, (Ring<Object>)(Object)org.episteme.core.mathematics.sets.Reals.getInstance()), fromINDArray(Vt.transpose(), a.getScalarRing()));
    }

    /**
     * LU decomposition via Gaussian elimination on ND4J arrays.
     * Returns L (lower triangular), U (upper triangular), and pivot vector P.
     */
    @Override
    public LUResult<Object> lu(Matrix<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return LinearAlgebraProvider.super.lu(a); // Direct fallback to default as ND4J LU is double-centric in older wrappers
    }

    @Override
    public CholeskyResult<Object> cholesky(Matrix<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return LinearAlgebraProvider.super.cholesky(a);
    }

    @Override
    public EigenResult<Object> eigen(Matrix<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        INDArray A = toINDArray(a);
        int n = (int) A.rows();
        INDArray S = Nd4j.create(A.dataType(), n);
        Nd4j.getBlasWrapper().lapack().syev('V', 'U', A, S);
        Matrix<Object> V = fromINDArray(A, a.getScalarRing());
        Vector<Object> D = fromINDArrayVector(S, a.getScalarRing());
        return new EigenResult<Object>(V, D);
    }

    // --- Transcendental Functions ---

    @Override
    public Matrix<Object> exp(Matrix<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(Transforms.exp(toINDArray(a), true), a.getScalarRing());
    }

    @Override
    public Matrix<Object> log(Matrix<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(Transforms.log(toINDArray(a), true), a.getScalarRing());
    }

    @Override
    public Matrix<Object> sin(Matrix<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(Transforms.sin(toINDArray(a), true), a.getScalarRing());
    }

    @Override
    public Matrix<Object> cos(Matrix<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(Transforms.cos(toINDArray(a), true), a.getScalarRing());
    }

    @Override
    public Matrix<Object> tan(Matrix<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(Transforms.tan(toINDArray(a), true), a.getScalarRing());
    }

    @Override
    public Matrix<Object> sqrt(Matrix<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        return fromINDArray(Transforms.sqrt(toINDArray(a), true), a.getScalarRing());
    }

    @Override
    public Vector<Object> normalize(Vector<Object> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        INDArray arr = toINDArray(a);
        double n = arr.norm2Number().doubleValue();
        if (n == 0) return a;
        return fromINDArrayVector(arr.div(n), a.getScalarRing());
    }
}
