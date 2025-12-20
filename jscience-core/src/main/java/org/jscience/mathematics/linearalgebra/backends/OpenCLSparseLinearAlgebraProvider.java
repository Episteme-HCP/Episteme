package org.jscience.mathematics.linearalgebra.backends;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.technical.backend.ExecutionContext;
import org.jscience.technical.backend.opencl.OpenCLBackend;

/**
 * OpenCL Linear Algebra Provider (Sparse).
 * <p>
 * Placeholder for sparse OpenCL implementation.
 * </p>
 */
public class OpenCLSparseLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private final CPUSparseLinearAlgebraProvider<E> cpuProvider;
    private static final OpenCLBackend backend = new OpenCLBackend();

    public OpenCLSparseLinearAlgebraProvider(Field<E> field) {
        this.cpuProvider = new CPUSparseLinearAlgebraProvider<>(field);
        if (isAvailable()) {
            java.util.logging.Logger.getLogger(getClass().getName()).info(
                    "OpenCLSparseLinearAlgebraProvider initialized (Warning: Sparse GPU ops delegated to CPU in this version)");
        }
    }

    @Override
    public boolean isAvailable() {
        return backend.isAvailable();
    }

    @Override
    public String getName() {
        return "OpenCL (Sparse)";
    }

    @Override
    public ExecutionContext createContext() {
        return backend.createContext();
    }

    @Override
    public int getPriority() {
        return 10;
    }

    // Delegate to CPU Sparse for now
    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        return cpuProvider.add(a, b);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        return cpuProvider.subtract(a, b);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        return cpuProvider.multiply(vector, scalar);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        return cpuProvider.dot(a, b);
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
        return cpuProvider.multiply(a, b);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        return cpuProvider.multiply(a, b);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        return cpuProvider.inverse(a);
    }

    @Override
    public E determinant(Matrix<E> a) {
        return cpuProvider.determinant(a);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        return cpuProvider.solve(a, b); // Sparse solver?
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        return cpuProvider.transpose(a);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        return cpuProvider.scale(scalar, a);
    }
}
