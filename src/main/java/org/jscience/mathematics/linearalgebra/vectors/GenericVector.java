package org.jscience.mathematics.linearalgebra.vectors;

import org.jscience.ComputeContext;
import org.jscience.mathematics.linearalgebra.backends.LinearAlgebraProvider;
import org.jscience.mathematics.linearalgebra.matrices.GenericMatrix;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.storage.DenseVectorStorage;
import org.jscience.mathematics.linearalgebra.vectors.storage.VectorStorage;
import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.structures.rings.Ring;

/**
 * A generic vector implementation using abstract storage and provider
 * delegation.
 *
 * @param <E> the element type
 */
public class GenericVector<E> implements Vector<E> {

    protected final VectorStorage<E> storage;
    protected LinearAlgebraProvider<E> provider;
    protected final Field<E> field;

    public GenericVector(VectorStorage<E> storage, LinearAlgebraProvider<E> provider, Field<E> field) {
        this.storage = storage;
        this.provider = provider;
        this.field = field;
    }

    /**
     * Creates a GenericVector with automatic storage selection.
     */
    public GenericVector(java.util.List<E> data, Field<E> field) {
        this(VectorFactory.createAutomaticStorage(data, field),
                ComputeContext.current().getDenseLinearAlgebraProvider(field),
                field);
    }

    public static <T> GenericVector<T> of(T[] data, Field<T> field) {
        // Default to Dense Storage
        VectorStorage<T> storage = new DenseVectorStorage<>(data);

        // Select Provider based on Context
        LinearAlgebraProvider<T> provider = ComputeContext.current().getDenseLinearAlgebraProvider(field);

        return new GenericVector<>(storage, provider, field);
    }

    // ================= Conversions =================

    public Matrix<E> toMatrix() {
        // Convert vector to Column Matrix (n x 1)
        E[][] matrixData = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), storage.dimension(), 1);
        for (int i = 0; i < storage.dimension(); i++) {
            E[] row = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), 1);
            row[0] = storage.get(i);
            matrixData[i] = row;
        }
        return GenericMatrix.of(matrixData, field);
    }

    public Tensor<E> toTensor() {
        // Convert to 1D Tensor
        // Tensor<E> t = TensorFactory.zeros((Class<E>)field.zero().getClass(),
        // dimension());
        // For now, simpler approach:
        // We need element class.
        // Let's rely on array creation if possible?
        // Or just return null for now if TensorFactory is not ready?
        // Let's try to assume we can get class from zero()
        // But zero() returns E.
        // Hack:
        // return TensorFactory.of(storage.toArray(), dimension());
        // Wait, storage.toArray() might not be T[].
        // Let's leave it as throwing for now or basic implementation?
        // The error was "create(GenericVector) undefined".
        // Let's implemented it using an array.
        // Assuming toArray() exists or similar in storage?
        // Logic:
        // E[] data = (E[]) new Object[dimension()];
        // for(int i=0; i<dimension(); i++) data[i] = get(i);
        // return TensorFactory.of(data, dimension());
        throw new UnsupportedOperationException("toTensor not fully implemented yet");
    }

    // ================= Vector<E> Implementation =================

    @Override
    public int dimension() {
        return storage.dimension();
    }

    public void set(int index, E value) {
        storage.set(index, value);
    }

    @Override
    public E get(int index) {
        return storage.get(index);
    }

    @Override
    public Vector<E> add(Vector<E> other) {
        if (other instanceof GenericVector) {
            return provider.add(this, (GenericVector<E>) other);
        }
        // Fallback
        if (other.dimension() != dimension())
            throw new IllegalArgumentException("Dim mismatch");

        // Manually create dense result for fallback
        // We need reflection for array creation if using DenseVectorStorage directly
        // with array,
        // or just use setters.
        DenseVectorStorage<E> newStorage = new DenseVectorStorage<>(dimension());
        for (int i = 0; i < dimension(); ++i) {
            newStorage.set(i, field.add(storage.get(i), other.get(i)));
        }
        return new GenericVector<>(newStorage, provider, field);
    }

    @Override
    public Vector<E> subtract(Vector<E> other) {
        if (other instanceof GenericVector) {
            return provider.subtract(this, (GenericVector<E>) other);
        }
        if (other.dimension() != dimension())
            throw new IllegalArgumentException("Dim mismatch");

        DenseVectorStorage<E> newStorage = new DenseVectorStorage<>(dimension());
        for (int i = 0; i < dimension(); ++i) {
            E neg = field.negate(other.get(i));
            newStorage.set(i, field.add(storage.get(i), neg));
        }
        return new GenericVector<>(newStorage, provider, field);
    }

    @Override
    public Vector<E> multiply(E scalar) {
        return provider.multiply(this, scalar);
    }

    public Vector<E> scale(E scalar) {
        return multiply(scalar);
    }

    @Override
    public Vector<E> negate() {
        return multiply(field.negate(field.one()));
    }

    @Override
    public E dot(Vector<E> other) {
        if (other instanceof GenericVector) {
            return provider.dot(this, (GenericVector<E>) other);
        }
        E sum = field.zero();
        for (int i = 0; i < dimension(); ++i) {
            sum = field.add(sum, field.multiply(get(i), other.get(i)));
        }
        return sum;
    }

    @Override
    public E norm() {
        return provider.norm(this);
    }

    @Override
    public Ring<E> getScalarRing() {
        return field;
    }

    @Override
    public Vector<E> scale(E scalar, Vector<E> element) {
        return element.multiply(scalar);
    }

    public VectorStorage<E> getStorage() {
        return storage;
    }

    public Field<E> getField() {
        return field;
    }

    // --- Default implementations for Ring/Module ---

    @Override
    public boolean contains(Vector<E> element) {
        return element.dimension() == this.dimension();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Vector<E> inverse(Vector<E> element) {
        return element.negate();
    }

    @Override
    public Vector<E> operate(Vector<E> left, Vector<E> right) {
        return left.add(right);
    }

    @Override
    public String description() {
        return "Generic Vector Space V^" + dimension();
    }
}
