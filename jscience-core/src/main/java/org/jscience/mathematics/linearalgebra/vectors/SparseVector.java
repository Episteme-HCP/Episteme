/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
import org.jscience.mathematics.linearalgebra.vectors.storage.SparseVectorStorage;
import org.jscience.mathematics.structures.rings.Ring;

import java.util.List;

/**
 * A sparse vector implementation.
 * Wrapper around GenericVector that enforces SparseVectorStorage.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SparseVector<E> extends GenericVector<E> {

    /**
     * Creates a SparseVector with automatic storage selection.
     */
    public SparseVector(List<E> elements, Ring<E> ring) {
        this(VectorFactory.createSparseStorage(elements, ring), ring);
    }

    public SparseVector(int dimension, Ring<E> ring) {
        this(new SparseVectorStorage<>(dimension, ring.zero()), ring);
    }

    protected SparseVector(org.jscience.mathematics.linearalgebra.vectors.storage.VectorStorage<E> storage,
            Ring<E> ring) {
        super(storage, ComputeContext.current().getSparseLinearAlgebraProvider(ring), ring);
        // explicit validation
        if (storage instanceof org.jscience.mathematics.linearalgebra.vectors.storage.DenseVectorStorage
                || storage instanceof org.jscience.mathematics.linearalgebra.vectors.storage.RealDoubleVectorStorage) {
            throw new IllegalArgumentException("Cannot create SparseVector with Dense storage");
        }
    }

    public static <E> SparseVector<E> of(List<E> elements, Ring<E> ring) {
        return new SparseVector<>(elements, ring);
    }

    public static <E> SparseVector<E> zeros(int dimension, Ring<E> ring) {
        return new SparseVector<>(new SparseVectorStorage<>(dimension, ring.zero()), ring);
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


