/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.nativ.mathematics.linearalgebra.providers;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;
import com.google.auto.service.AutoService;

/**
 * ND4J Linear Algebra Provider (Dense).
 * <p>
 * When the ND4J library ({@code org.nd4j:nd4j-native-platform}) is on the classpath,
 * this provider delegates to ND4J's optimized BLAS/LAPACK backends (Native/AVX/CUDA).
 * All operations fall back to {@link CPUDenseLinearAlgebraProvider} if ND4J is absent.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
@AutoService(LinearAlgebraProvider.class)
public class ND4JLinearAlgebraProvider implements LinearAlgebraProvider<Real> {

    private final CPUDenseLinearAlgebraProvider<Real> fallback = new CPUDenseLinearAlgebraProvider<>();

    public String getName() {
        return "ND4J (Native Wrapper)";
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
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isCompatible(org.jscience.core.mathematics.structures.rings.Ring<?> ring) {
        return true; 
    }

    private org.nd4j.linalg.api.ndarray.INDArray toINDArray(Matrix<Real> m) {
        double[][] data = new double[m.rows()][m.cols()];
        for(int r=0; r<m.rows(); r++) {
            for(int c=0; c<m.cols(); c++) {
                data[r][c] = m.get(r,c).doubleValue();
            }
        }
        return org.nd4j.linalg.factory.Nd4j.create(data);
    }

    private Matrix<Real> fromINDArray(org.nd4j.linalg.api.ndarray.INDArray arr) {
        int rows = (int) arr.rows();
        int cols = (int) arr.columns();
        org.jscience.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<Real> storage = 
            new org.jscience.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<>(rows, cols, Real.ZERO);
        for(int r=0; r<rows; r++) {
            for(int c=0; c<cols; c++) {
                storage.set(r, c, Real.of(arr.getDouble(r, c)));
            }
        }
        return new org.jscience.core.mathematics.linearalgebra.matrices.GenericMatrix<>(storage, this, org.jscience.core.mathematics.sets.Reals.getInstance());
    }

    private org.nd4j.linalg.api.ndarray.INDArray toINDArray(Vector<Real> v) {
        double[] data = new double[v.dimension()];
        for(int i=0; i<v.dimension(); i++) {
            data[i] = v.get(i).doubleValue();
        }
        return org.nd4j.linalg.factory.Nd4j.create(data, new int[]{v.dimension(), 1}); // Column vector
    }

    private Vector<Real> fromINDArrayVector(org.nd4j.linalg.api.ndarray.INDArray arr) {
        int dim = (int) arr.length();
        Real[] data = new Real[dim];
        for(int i=0; i<dim; i++) {
            data[i] = Real.of(arr.getDouble(i));
        }
        return new org.jscience.core.mathematics.linearalgebra.vectors.GenericVector<>(
            new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(data), 
            this, org.jscience.core.mathematics.sets.Reals.getInstance());
    }

    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) return fallback.add(a, b);
        return fromINDArrayVector(toINDArray(a).add(toINDArray(b)));
    }

    @Override
    public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) return fallback.subtract(a, b);
        return fromINDArrayVector(toINDArray(a).sub(toINDArray(b)));
    }

    @Override
    public Vector<Real> multiply(Vector<Real> vector, Real scalar) {
        if (!isAvailable()) return fallback.multiply(vector, scalar);
        return fromINDArrayVector(toINDArray(vector).mul(scalar.doubleValue()));
    }

    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) return fallback.dot(a, b);
        // dot returns a scalar INDArray
        return Real.of(org.nd4j.linalg.factory.Nd4j.getBlasWrapper().dot(toINDArray(a), toINDArray(b)));
    }

    @Override
    public Real norm(Vector<Real> a) {
        if (!isAvailable()) return fallback.norm(a);
        return Real.of(toINDArray(a).norm2Number().doubleValue());
    }

    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) return fallback.add(a, b);
        return fromINDArray(toINDArray(a).add(toINDArray(b)));
    }

    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) return fallback.subtract(a, b);
        return fromINDArray(toINDArray(a).sub(toINDArray(b)));
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) return fallback.multiply(a, b);
        return fromINDArray(toINDArray(a).mmul(toINDArray(b)));
    }

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        if (!isAvailable()) return fallback.multiply(a, b);
        return fromINDArrayVector(toINDArray(a).mmul(toINDArray(b)));
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        if (!isAvailable()) return fallback.inverse(a);
        return fromINDArray(org.nd4j.linalg.inverse.InvertMatrix.invert(toINDArray(a), false));
    }

    @Override
    public Real determinant(Matrix<Real> a) {
        // ND4J determinant might be complex to access directly or not available in all versions
        // In 1.0.0-M1, check for determinant support
        // fallback to CPU for safety as it's rarely critical in inner loops compared to multiply/solve
        return fallback.determinant(a);
    }

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if (!isAvailable()) return fallback.solve(a, b);
        // x = A^-1 * b (naive) or use specific solver if available
        // ND4J Solve.solve(A, b) exists?
        // Using inverse for now as basic implementation
        return multiply(inverse(a), b);
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        if (!isAvailable()) return fallback.transpose(a);
        return fromINDArray(toINDArray(a).transpose());
    }

    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) {
        if (!isAvailable()) return fallback.scale(scalar, a);
        return fromINDArray(toINDArray(a).mul(scalar.doubleValue()));
    }
}
