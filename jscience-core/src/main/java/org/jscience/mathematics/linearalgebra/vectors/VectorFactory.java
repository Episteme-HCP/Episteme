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

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.storage.*;
import org.jscience.mathematics.structures.rings.Field;
import java.util.List;
import java.util.Arrays;

/**
 * Factory for creating vectors with specific storage layouts.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class VectorFactory {

    private VectorFactory() {
        // Utility class
    }

    /**
 * Storage layout types.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum Storage {
        AUTO,
        DENSE,
        SPARSE
    }

    /**
     * Creates a vector from varargs elements.
     * Convenience method for Real vectors.
     * 
     * @param elementClass the class of elements (e.g., Real.class)
     * @param elements     the elements of the vector
     * @return a new Vector instance
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <E> Vector<E> of(Class<E> elementClass, E... elements) {
        List<E> list = Arrays.asList(elements);
        if (elementClass == org.jscience.mathematics.numbers.real.Real.class) {
            return create(list, (Field<E>) org.jscience.mathematics.numbers.real.Real.ZERO);
        }
        throw new UnsupportedOperationException("of() only supports Real.class currently");
    }

    /**
     * Creates a vector with automatic storage selection (AUTO).
     */
    public static <E> Vector<E> create(List<E> data, Field<E> field) {
        return create(data, field, Storage.AUTO);
    }

    /**
     * Automatic storage selection based on density.
     */
    public static <E> VectorStorage<E> createAutomaticStorage(List<E> data, Field<E> field) {
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
                int nonZero = 0;
                E zero = field.zero();
                for (E val : data) {
                    if (!val.equals(zero))
                        nonZero++;
                }
                if (dim > 0 && (double) nonZero / dim < 0.2) {
                    return create(data, field, Storage.SPARSE);
                } else {
                    return create(data, field, Storage.DENSE);
                }

            case DENSE:
                return DenseVector.of(data, field);

            case SPARSE:
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

    public static <E> VectorStorage<E> createDenseStorage(List<E> data, Field<E> field) {
        int dim = data.size();
        boolean isReal = (dim > 0 && data.get(0) instanceof org.jscience.mathematics.numbers.real.Real);

        if (isReal) {
            double[] dData = new double[dim];
            for (int i = 0; i < dim; i++) {
                dData[i] = ((org.jscience.mathematics.numbers.real.Real) data.get(i)).doubleValue();
            }
            @SuppressWarnings("unchecked")
            VectorStorage<E> res = (VectorStorage<E>) new HeapRealDoubleVectorStorage(dData);
            return res;
        }

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
