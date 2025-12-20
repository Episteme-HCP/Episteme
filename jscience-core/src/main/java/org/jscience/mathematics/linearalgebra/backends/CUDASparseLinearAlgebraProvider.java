package org.jscience.mathematics.linearalgebra.backends;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;

import org.jscience.technical.backend.ExecutionContext;

/**
 * CUDA Linear Algebra Provider (Sparse).
 */
public class CUDASparseLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private final CPUSparseLinearAlgebraProvider<E> cpuProvider;

    public CUDASparseLinearAlgebraProvider(Field<E> field) {
        // this.field = field; // Unused
        this.cpuProvider = new CPUSparseLinearAlgebraProvider<>(field);
        if (checkAvailability()) {
            java.util.logging.Logger.getLogger(getClass().getName()).info(
                    "CUDASparseLinearAlgebraProvider initialized (Warning: Sparse GPU ops delegated to CPU in this version)");
        }
    }

    // Check if JCusparse is available? Usually assuming if JCuda matches.
    private static boolean checkAvailability() {
        try {
            Class.forName("jcuda.jcusparse.JCusparse");
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
        return "CUDA (Sparse)";
    }

    @Override
    public ExecutionContext createContext() {
        return null;
    }

    @Override
    public int getPriority() {
        return isAvailable() ? 100 : 0;
    }

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
        return cpuProvider.solve(a, b);
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
