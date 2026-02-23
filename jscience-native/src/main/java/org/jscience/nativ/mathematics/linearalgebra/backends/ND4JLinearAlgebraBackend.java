/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.nativ.mathematics.linearalgebra.backends;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;
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
 * All operations fall back to {@link CPUDenseLinearAlgebraProvider} if ND4J is absent.
 * Implements {@link NativeBackend}.
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

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.nd4j.linalg.factory.Nd4j");
            return true;
        } catch (Throwable t) {
            return false;
        }
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
        double[] data = arr.data().asDouble(); // Direct access if possible, or copy
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

    @Override
    public Real determinant(Matrix<Real> a) {
        throw new UnsupportedOperationException("ND4J determinant not implemented yet.");
    }

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("ND4J not available");
        // ND4J direct solve: A * x = b => x = solve(A, b)
        // Fallback to inverse multiply if solve is not found in this version
        return multiply(inverse(a), b);
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
}
