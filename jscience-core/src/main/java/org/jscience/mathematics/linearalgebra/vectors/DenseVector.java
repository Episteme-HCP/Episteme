package org.jscience.mathematics.linearalgebra.vectors;

import org.jscience.ComputeContext;
import org.jscience.mathematics.linearalgebra.vectors.storage.DenseVectorStorage;
import org.jscience.mathematics.structures.rings.Field;

import java.util.List;

import java.util.ArrayList;

/**
 * A dense vector implementation.
 * Wrapper around GenericVector that enforces DenseVectorStorage.
 * 
 * @param <E> the type of scalar elements
 */
public class DenseVector<E> extends GenericVector<E> {

    /**
     * Creates a DenseVector with automatic storage optimization.
     */
    public DenseVector(List<E> elements, Field<E> field) {
        this(VectorFactory.createDenseStorage(elements, field), field);
    }

    // Internal constructor using storage directly
    protected DenseVector(org.jscience.mathematics.linearalgebra.vectors.storage.VectorStorage<E> storage,
            Field<E> field) {
        super(storage, ComputeContext.current().getDenseLinearAlgebraProvider(field), field);
        // explicit validation
        if (storage instanceof org.jscience.mathematics.linearalgebra.vectors.storage.SparseVectorStorage) {
            throw new IllegalArgumentException("Cannot create DenseVector with Sparse storage");
        }
    }

    // Removed manual createStorage methods in favor of VectorFactory

    public static <E> DenseVector<E> of(List<E> elements, Field<E> field) {
        return new DenseVector<>(elements, field);
    }

    public static <E> DenseVector<E> zeros(int dimension, Field<E> field) {
        List<E> elements = new ArrayList<>(dimension);
        E zero = field.zero();
        for (int i = 0; i < dimension; i++) {
            elements.add(zero);
        }
        return new DenseVector<>(elements, field);
    }

    public static DenseVector<org.jscience.mathematics.numbers.complex.Complex> valueOf(
            List<org.jscience.mathematics.numbers.complex.Complex> elements) {
        return new DenseVector<>(elements, org.jscience.mathematics.numbers.complex.Complex.ZERO);
    }

    // Accessor for raw storage if needed by specialized providers
    // Updated to be safe with RealDoubleVectorStorage which is Dense
    public DenseVectorStorage<E> getDenseStorage() {
        if (storage instanceof DenseVectorStorage)
            return (DenseVectorStorage<E>) storage;
        throw new UnsupportedOperationException("Underlying storage is optimized (" + storage.getClass().getSimpleName()
                + ") and not standard DenseVectorStorage.");
    }

    @Override
    public String toString() {
        // Simple dense printing
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < dimension(); i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}
