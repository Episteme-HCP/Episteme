package org.jscience.mathematics.linearalgebra.vectors;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.storage.*;
import org.jscience.mathematics.structures.rings.Field;
import java.util.List;

/**
 * Factory for creating vectors with specific storage layouts.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public final class VectorFactory {

    private VectorFactory() {
        // Utility class
    }

    /**
     * Storage layout types.
     */
    public enum Storage {
        AUTO,
        DENSE,
        SPARSE
    }

    /**
     * Creates a vector with automatic storage selection (AUTO).
     */
    public static <E> Vector<E> create(List<E> data, Field<E> field) {
        return create(data, field, Storage.AUTO);
    }

    public static <E> VectorStorage<E> createAutomaticStorage(List<E> data, Field<E> field) {
        // Re-use logic or call existing create
        // But create returns Vector, we need VectorStorage.
        // Let's implement logic here properly.
        int dim = data.size();
        int nonZero = 0;
        E zero = field.zero();
        for (E val : data) {
            if (!val.equals(zero))
                nonZero++;
        }
        if (dim > 0 && (double) nonZero / dim < 0.2) {
            return createSparseStorage(data, field);
        } else {
            return createDenseStorage(data, field);
        }
    }

    /**
     * Creates a vector with the specified storage layout.
     */
    public static <E> Vector<E> create(List<E> data, Field<E> field, Storage storageType) {
        int dim = data.size();

        switch (storageType) {
            case AUTO:
                // Simple heuristic: check density
                int nonZero = 0;
                E zero = field.zero();
                for (E val : data) {
                    if (!val.equals(zero))
                        nonZero++;
                }
                // If density < 0.2, use Sparse
                if (dim > 0 && (double) nonZero / dim < 0.2) {
                    return create(data, field, Storage.SPARSE);
                } else {
                    return create(data, field, Storage.DENSE);
                }

            case DENSE:
                // Check for RealDouble optimization
                boolean isReal = (dim > 0 && data.get(0) instanceof org.jscience.mathematics.numbers.real.Real);

                if (isReal) {
                    // We will implement RealDoubleVector later in the parity steps.
                    // For now, return DenseVector, but note that DenseVector is being updated to
                    // support RealDoubleVectorStorage soon.
                    // Actually, if we want full parity, we should support it here once available.
                    // Leaving as standard DenseVector for now, consistent with current DenseVector
                    // state.
                }
                return DenseVector.of(data, field);

            case SPARSE:
                // SparseVector
                SparseVector<E> sv = new SparseVector<>(dim, field);
                E z = field.zero();
                for (int i = 0; i < dim; i++) {
                    E val = data.get(i);
                    if (!val.equals(z)) {
                        sv.set(i, val);
                    }
                }
                return sv;

            default:
                throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
    }

    // --- Helper methods for Constructors (Parity with MatrixFactory) ---

    public static <E> VectorStorage<E> createDenseStorage(List<E> data, Field<E> field) {
        int dim = data.size();
        boolean isReal = (dim > 0 && data.get(0) instanceof org.jscience.mathematics.numbers.real.Real);

        if (isReal) {
            double[] dData = new double[dim];
            for (int i = 0; i < dim; i++) {
                dData[i] = ((org.jscience.mathematics.numbers.real.Real) data.get(i)).doubleValue();
            }
            return (VectorStorage<E>) new HeapRealDoubleVectorStorage(dData);
        }

        // Generic Dense Storage
        DenseVectorStorage<E> storage = new DenseVectorStorage<>(dim);
        for (int i = 0; i < dim; i++) {
            storage.set(i, data.get(i));
        }
        return storage;
    }

    public static <E> VectorStorage<E> createSparseStorage(List<E> data, Field<E> field) {
        int dim = data.size();
        SparseVectorStorage<E> storage = new SparseVectorStorage<>(dim, field.zero());
        E zero = field.zero();
        for (int i = 0; i < dim; i++) {
            E val = data.get(i);
            if (!val.equals(zero)) {
                storage.set(i, val);
            }
        }
        return storage;
    }
}
