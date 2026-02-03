package org.jscience.core.mathematics.linearalgebra.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.jcublas.JCublas;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.core.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.mathematics.structures.rings.Field;
import org.jscience.core.technical.algorithm.LinearAlgebraProvider;
import org.jscience.core.technical.algorithm.linearalgebra.CPUDenseLinearAlgebraProvider;

/**
 * CUDA Linear Algebra Provider (Dense).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CUDADenseLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private final Field<E> field;
    private final CPUDenseLinearAlgebraProvider<E> cpuProvider;

    public CUDADenseLinearAlgebraProvider(Field<E> field) {
        this.field = field;
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(field);
    }

    /**
     * Public no-arg constructor required by ServiceLoader.
     */
    public CUDADenseLinearAlgebraProvider() {
        this(null);
    }

    private static boolean checkAvailability() {
        try {
            Class.forName("jcuda.jcublas.JCublas");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (UnsatisfiedLinkError e) {
            return false;
        }
    }

    @Override
    public boolean isAvailable() {
        return checkAvailability();
    }

    @Override
    public String getName() {
        return "CUDA (Dense)";
    }

    @Override
    public int getPriority() {
        // High priority if available, otherwise effectively disabled for selection
        return isAvailable() ? 100 : Integer.MIN_VALUE;
    }

    private Map<String, Object> config = new HashMap<>();

    @Override
    public void configure(Map<String, Object> properties) {
        if (properties != null) {
            this.config.putAll(properties);
        }
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (!isAvailable() || !(field.zero() instanceof Real)) {
            return cpuProvider.add(a, b);
        }
        
        int n = a.dimension();
        if (n != b.dimension()) throw new IllegalArgumentException("Vector dimension mismatch");

        try {
            double[] h_A = toDoubleArray(a);
            double[] h_B = toDoubleArray(b);
            double[] h_C = new double[n];

            JCublas.cublasInit();
            Pointer d_A = new Pointer();
            Pointer d_B = new Pointer();

            JCublas.cublasAlloc(n, Sizeof.DOUBLE, d_A);
            JCublas.cublasAlloc(n, Sizeof.DOUBLE, d_B);

            JCublas.cublasSetVector(n, Sizeof.DOUBLE, Pointer.to(h_A), 1, d_A, 1);
            JCublas.cublasSetVector(n, Sizeof.DOUBLE, Pointer.to(h_B), 1, d_B, 1);

            // C = 1.0 * A + B
            JCublas.cublasDaxpy(n, 1.0, d_A, 1, d_B, 1);

            JCublas.cublasGetVector(n, Sizeof.DOUBLE, d_B, 1, Pointer.to(h_C), 1);

            JCublas.cublasFree(d_A);
            JCublas.cublasFree(d_B);
            JCublas.cublasShutdown();

            return toVector(h_C);
        } catch (Throwable t) {
            return cpuProvider.add(a, b);
        }
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (!isAvailable() || !(field.zero() instanceof Real)) {
            return cpuProvider.subtract(a, b);
        }
        
        int n = a.dimension();
        try {
            double[] h_A = toDoubleArray(a);
            double[] h_B = toDoubleArray(b);
            double[] h_C = new double[n];

            JCublas.cublasInit();
            Pointer d_A = new Pointer();
            Pointer d_B = new Pointer();

            JCublas.cublasAlloc(n, Sizeof.DOUBLE, d_A);
            JCublas.cublasAlloc(n, Sizeof.DOUBLE, d_B);

            JCublas.cublasSetVector(n, Sizeof.DOUBLE, Pointer.to(h_A), 1, d_A, 1);
            JCublas.cublasSetVector(n, Sizeof.DOUBLE, Pointer.to(h_B), 1, d_B, 1);

            // B = -1.0 * B + A  => B = A - B
            JCublas.cublasDaxpy(n, -1.0, d_B, 1, d_A, 1);

            JCublas.cublasGetVector(n, Sizeof.DOUBLE, d_A, 1, Pointer.to(h_C), 1);

            JCublas.cublasFree(d_A);
            JCublas.cublasFree(d_B);
            JCublas.cublasShutdown();

            return toVector(h_C);
        } catch (Throwable t) {
            return cpuProvider.subtract(a, b);
        }
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (!isAvailable() || !(field.zero() instanceof Real)) {
            return cpuProvider.multiply(vector, scalar);
        }
        
        int n = vector.dimension();
        double s = ((Real)scalar).doubleValue();
        try {
            double[] h_v = toDoubleArray(vector);
            
            JCublas.cublasInit();
            Pointer d_v = new Pointer();
            JCublas.cublasAlloc(n, Sizeof.DOUBLE, d_v);
            JCublas.cublasSetVector(n, Sizeof.DOUBLE, Pointer.to(h_v), 1, d_v, 1);

            JCublas.cublasDscal(n, s, d_v, 1);

            JCublas.cublasGetVector(n, Sizeof.DOUBLE, d_v, 1, Pointer.to(h_v), 1);
            JCublas.cublasFree(d_v);
            JCublas.cublasShutdown();

            return toVector(h_v);
        } catch (Throwable t) {
            return cpuProvider.multiply(vector, scalar);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E dot(Vector<E> a, Vector<E> b) {
        if (!isAvailable() || !(field.zero() instanceof Real)) {
            return cpuProvider.dot(a, b);
        }
        
        int n = a.dimension();
        try {
            double[] h_A = toDoubleArray(a);
            double[] h_B = toDoubleArray(b);

            JCublas.cublasInit();
            Pointer d_A = new Pointer();
            Pointer d_B = new Pointer();

            JCublas.cublasAlloc(n, Sizeof.DOUBLE, d_A);
            JCublas.cublasAlloc(n, Sizeof.DOUBLE, d_B);

            JCublas.cublasSetVector(n, Sizeof.DOUBLE, Pointer.to(h_A), 1, d_A, 1);
            JCublas.cublasSetVector(n, Sizeof.DOUBLE, Pointer.to(h_B), 1, d_B, 1);

            double result = JCublas.cublasDdot(n, d_A, 1, d_B, 1);

            JCublas.cublasFree(d_A);
            JCublas.cublasFree(d_B);
            JCublas.cublasShutdown();

            return (E) Real.of(result);
        } catch (Throwable t) {
            return cpuProvider.dot(a, b);
        }
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        return cpuProvider.add(a, b);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        return cpuProvider.subtract(a, b);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!isAvailable() || !(field.zero() instanceof Real)) {
            return cpuProvider.multiply(a, b);
        }

        try {
            int m = a.rows();
            int k = a.cols();
            int n = b.cols();

            if (b.rows() != k)
                throw new IllegalArgumentException("Matrix dimension mismatch");

            // Threshold for GPU offload
            if ((long) m * n * k < 500_000) {
                return cpuProvider.multiply(a, b);
            }

            // Convert to double array (CUBLAS uses column-major)
            double[] A = toDoubleArray(a);
            double[] B = toDoubleArray(b);
            double[] C = new double[m * n];

            JCublas.cublasInit();

            Pointer d_A = new Pointer();
            Pointer d_B = new Pointer();
            Pointer d_C = new Pointer();

            JCublas.cublasAlloc(m * k, Sizeof.DOUBLE, d_A);
            JCublas.cublasAlloc(k * n, Sizeof.DOUBLE, d_B);
            JCublas.cublasAlloc(m * n, Sizeof.DOUBLE, d_C);

            JCublas.cublasSetVector(m * k, Sizeof.DOUBLE, Pointer.to(A), 1, d_A, 1);
            JCublas.cublasSetVector(k * n, Sizeof.DOUBLE, Pointer.to(B), 1, d_B, 1);

            // C = A * B
            // Java (Row-Major) A[m][k], B[k][n] => C[m][n]
            // CUBLAS (Col-Major) 
            // We can treat Row-Major A as Col-Major A^T.
            // Row-Major C = A * B  <=>  (Row-Major C)^T = B^T * A^T (in Col-Major)
            JCublas.cublasDgemm('n', 'n', n, m, k, 1.0, d_B, n, d_A, k, 0.0, d_C, n);

            JCublas.cublasGetVector(m * n, Sizeof.DOUBLE, d_C, 1, Pointer.to(C), 1);

            JCublas.cublasFree(d_A);
            JCublas.cublasFree(d_B);
            JCublas.cublasFree(d_C);
            JCublas.cublasShutdown();

            return toMatrix(C, m, n);

        } catch (Throwable t) {
            return cpuProvider.multiply(a, b);
        }
    }

    @Override
    public Vector<E> solve(Matrix<E> A, Vector<E> b) {
        return cpuProvider.solve(A, b);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> A) {
        return cpuProvider.inverse(A);
    }

    @Override
    public E determinant(Matrix<E> A) {
        return cpuProvider.determinant(A);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> A) {
        return cpuProvider.transpose(A);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        return cpuProvider.multiply(a, b);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        return cpuProvider.scale(scalar, a);
    }

    @Override
    public E norm(Vector<E> a) {
        return cpuProvider.norm(a);
    }

    private double[] toDoubleArray(Vector<E> v) {
        int n = v.dimension();
        double[] arr = new double[n];
        for (int i = 0; i < n; i++) {
            Real r = (Real) v.get(i);
            arr[i] = r.doubleValue();
        }
        return arr;
    }

    private double[] toDoubleArray(Matrix<E> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] arr = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Real r = (Real) m.get(i, j);
                arr[i * cols + j] = r.doubleValue();
            }
        }
        return arr;
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> toMatrix(double[] data, int rows, int cols) {
        List<List<Real>> matrixData = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(Real.of(data[i * cols + j]));
            }
            matrixData.add(row);
        }
        return (Matrix<E>) new DenseMatrix<>(matrixData, Reals.getInstance());
    }

    @SuppressWarnings("unchecked")
    private Vector<E> toVector(double[] data) {
        List<Real> list = new ArrayList<>(data.length);
        for (double d : data) {
            list.add(Real.of(d));
        }
        return (Vector<E>) new DenseVector<>(list, Reals.getInstance());
    }
}



