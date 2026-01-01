/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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


