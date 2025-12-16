package org.jscience.mathematics.linearalgebra.vectors;

import org.jscience.ComputeContext;
import org.jscience.mathematics.linearalgebra.vectors.storage.SparseVectorStorage;
import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.Vector;
import java.util.List;
import java.util.Map;

/**
 * A sparse vector implementation.
 * Wrapper around GenericVector that enforces SparseVectorStorage.
 * 
 * @param <E> the type of scalar elements
 */
public class SparseVector<E> extends GenericVector<E> {

    /**
     * Creates a SparseVector with automatic storage selection.
     */
    public SparseVector(List<E> elements, Field<E> field) {
        this(VectorFactory.createSparseStorage(elements, field), field);
    }

    public SparseVector(int dimension, Field<E> field) {
        this(new SparseVectorStorage<>(dimension, field.zero()), field);
    }

    protected SparseVector(org.jscience.mathematics.linearalgebra.vectors.storage.VectorStorage<E> storage,
            Field<E> field) {
        super(storage, ComputeContext.current().getSparseLinearAlgebraProvider(field), field);
        // explicit validation
        if (storage instanceof org.jscience.mathematics.linearalgebra.vectors.storage.DenseVectorStorage
                || storage instanceof org.jscience.mathematics.linearalgebra.vectors.storage.RealDoubleVectorStorage) {
            throw new IllegalArgumentException("Cannot create SparseVector with Dense storage");
        }
    }

    public static <E> SparseVector<E> of(List<E> elements, Field<E> field) {
        return new SparseVector<>(elements, field);
    }

    public static <E> SparseVector<E> zeros(int dimension, Field<E> field) {
        return new SparseVector<>(new SparseVectorStorage<>(dimension, field.zero()), field);
    }

    public static SparseVector<org.jscience.mathematics.numbers.complex.Complex> valueOf(
            List<org.jscience.mathematics.numbers.complex.Complex> elements) {
        return new SparseVector<>(elements, org.jscience.mathematics.numbers.complex.Complex.ZERO);
    }

    // Accessor for raw storage if needed by specialized providers
    public SparseVectorStorage<E> getSparseStorage() {
        return (SparseVectorStorage<E>) storage;
    }

    @Override
    public String toString() {
        return "SparseVector[dim=" + dimension() + "]";
    }
}
