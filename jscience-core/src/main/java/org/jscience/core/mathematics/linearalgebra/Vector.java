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

package org.jscience.core.mathematics.linearalgebra;

import org.jscience.core.mathematics.structures.spaces.Module;
import org.jscience.core.mathematics.structures.rings.Field;
import org.jscience.core.technical.algorithm.AlgorithmManager;
import org.jscience.core.technical.algorithm.OperationContext;
import org.jscience.core.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.core.mathematics.linearalgebra.vectors.GenericVector;
import org.jscience.core.mathematics.linearalgebra.vectors.storage.VectorStorage;
import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.core.mathematics.linearalgebra.vectors.SparseVector;

// TensorFactory removed

/**
 * Represents a vector in a vector space.
 * <p>
 * A vector is an element of a vector space, which is a module over a field.
 * This interface provides operations for vector addition, scalar
 * multiplication,
 * and dot product.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Vector<E> extends Module<Vector<E>, E> {

    /**
     * Creates a vector from a list of elements.
     */
    static <E> Vector<E> of(java.util.List<E> data, org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        int dim = data.size();
        
        int nz = 0;
        E zero = ring.zero();
        for (E val : data) if (!val.equals(zero)) nz++;
        double density = (dim > 0) ? (double) nz / dim : 1.0;
        
        org.jscience.core.mathematics.linearalgebra.vectors.storage.VectorStorage<E> storage = AlgorithmManager.getRegistry().createVectorStorage(dim, ring, density);
        for (int i = 0; i < dim; i++) storage.set(i, data.get(i));
        
        LinearAlgebraProvider<E> provider = AlgorithmManager.getRegistry().selectLinearAlgebraProvider(OperationContext.DEFAULT, ring);
        return new org.jscience.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, provider, ring);
    }

    /**
     * Creates a vector from an array of elements.
     */
    static <E> Vector<E> of(E[] data, org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        return of(java.util.Arrays.asList(data), ring);
    }

    /**
     * Creates a vector from a variable number of elements.
     */
    @SafeVarargs
    static <E> Vector<E> of(org.jscience.core.mathematics.structures.rings.Ring<E> ring, E... elements) {
        return of(java.util.Arrays.asList(elements), ring);
    }

    /**
     * Creates a vector of zeros.
     */
    static <E> Vector<E> zeros(int dimension, org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        VectorStorage<E> storage = AlgorithmManager.getRegistry().createVectorStorage(dimension, ring, 1.0);
        LinearAlgebraProvider<E> provider = AlgorithmManager.getRegistry().selectLinearAlgebraProvider(OperationContext.DEFAULT, ring);
        return new org.jscience.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, provider, ring);
    }

    /**
     * Returns the dimension of this vector.
     * 
     * @return the number of elements in this vector
     */
    int dimension();

    /**
     * Returns the element at the specified index.
     * 
     * @param index the index of the element to return (0-based)
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    E get(int index);

    /**
     * Returns the underlying storage of this vector.
     * 
     * @return the vector storage
     */
    org.jscience.core.mathematics.linearalgebra.vectors.storage.VectorStorage<E> getStorage();

    /**
     * Returns the sum of this vector and another.
     * 
     * @param other the vector to add
     * @return this + other
     * @throws IllegalArgumentException if dimensions do not match
     */
    Vector<E> add(Vector<E> other);

    /**
     * Returns the difference of this vector and another.
     * 
     * @param other the vector to subtract
     * @return this - other
     * @throws IllegalArgumentException if dimensions do not match
     */
    Vector<E> subtract(Vector<E> other);

    /**
     * Returns the scalar product of this vector.
     * 
     * @param scalar the scalar multiplier
     * @return this * scalar
     */
    Vector<E> multiply(E scalar);

    /**
     * Returns the negation of this vector (-this).
     * 
     * @return -this
     */
    Vector<E> negate();

    /**
     * Returns the dot product of this vector with another vector.
     * 
     * @param other the other vector
     * @return the dot product
     * @throws IllegalArgumentException if the dimensions do not match
     */
    E dot(Vector<E> other);

    /**
     * Returns the norm (length) of this vector.
     * 
     * @return the norm of this vector
     */
    E norm();

    /**
     * Returns the normalized vector (unit vector).
     * 
     * @return this / norm()
     * @throws ArithmeticException if norm is zero
     */
    @SuppressWarnings("unchecked")
    default Vector<E> normalize() {
        E n = norm();
        // explicit check for zero?
        // Assuming E has inverse() method (e.g. Real, Complex)
        // We cast to Field or similar if possible, or try reflection?
        // JScience scalars usually have 'inverse()' if they are fields.
        try {
            java.lang.reflect.Method inv = n.getClass().getMethod("inverse");
            E invNorm = (E) inv.invoke(n);
            return multiply(invNorm);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Scalar type " + n.getClass().getSimpleName()
                    + " does not support inversion (required for normalization).");
        }
    }

    /**
     * Returns the cross product of this vector and another (only for 3D).
     * 
     * @param other the other vector
     * @return this x other
     * @throws ArithmeticException if dimensions are not 3
     */
    @SuppressWarnings("unchecked")
    default Vector<E> cross(Vector<E> other) {
        if (dimension() != 3 || other.dimension() != 3) {
            throw new ArithmeticException("Cross product only defined for 3D vectors");
        }
        // Generic cross product: u2*v3 - u3*v2, etc.
        E u1 = get(0);
        E u2 = get(1);
        E u3 = get(2);
        E v1 = other.get(0);
        E v2 = other.get(1);
        E v3 = other.get(2);

        // We need a Ring/Field to do multiply/subtract.
        // Assuming E has multiply(), subtract().
        // Since Vector extends Module<..., E>, E likely has these.
        // But we need to CREATE a new Vector.

        try {
            java.lang.reflect.Method mult = u1.getClass().getMethod("multiply", u1.getClass());
            java.lang.reflect.Method sub = u1.getClass().getMethod("subtract", u1.getClass());

            E c1 = (E) sub.invoke(mult.invoke(u2, v3), mult.invoke(u3, v2)); // u2v3 - u3v2
            E c2 = (E) sub.invoke(mult.invoke(u3, v1), mult.invoke(u1, v3)); // u3v1 - u1v3
            E c3 = (E) sub.invoke(mult.invoke(u1, v2), mult.invoke(u2, v1)); // u1v2 - u2v1

            return Vector.of(java.util.Arrays.asList(c1, c2, c3), getScalarRing());
        } catch (Exception e) {
            throw new UnsupportedOperationException("Cross product requires scalar arithmetic operations.", e);
        }
    }

    /**
     * Returns the angle between this vector and another.
     * 
     * @param other the other vector
     * @return the angle in radians (or appropriate unit)
     */
    @SuppressWarnings("unchecked")
    default E angle(Vector<E> other) {
        // acos( dot / (norm * other.norm) )
        E d = dot(other);
        E n1 = norm();
        E n2 = other.norm();

        try {
            java.lang.reflect.Method mult = n1.getClass().getMethod("multiply", n1.getClass());
            java.lang.reflect.Method div = d.getClass().getMethod("divide", d.getClass());
            java.lang.reflect.Method acos = d.getClass().getMethod("acos"); // Only on Real?

            E denom = (E) mult.invoke(n1, n2);
            E ratio = (E) div.invoke(d, denom);

            // If E is Real, acos exists. If Complex?
            return (E) acos.invoke(ratio);

        } catch (Exception e) {
            throw new UnsupportedOperationException("Angle calculation requires Real-like scalars with acos()", e);
        }
    }

    /**
     * Returns the projection of this vector onto another.
     * 
     * @param other the vector to project onto
     * @return proj_other(this)
     */
    @SuppressWarnings("unchecked")
    default Vector<E> projection(Vector<E> other) {
        // (dot(other) / other.dot(other)) * other
        try {
            E dotVal = dot(other);
            E dotOther = other.dot(other);

            java.lang.reflect.Method div = dotVal.getClass().getMethod("divide", dotVal.getClass());
            E scalar = (E) div.invoke(dotVal, dotOther);

            return other.multiply(scalar);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Projection requires division support on scalars", e);
        }
    }

    /**
     * Converts this vector to a column matrix (Nx1).
     * 
     * @return the corresponding column matrix
     * @throws UnsupportedOperationException if the scalar ring is not a Field
     */
    default Matrix<E> toMatrix() {
        if (getScalarRing() instanceof Field) {
            Field<E> field = (Field<E>) getScalarRing();
            java.util.List<java.util.List<E>> rows = new java.util.ArrayList<>(dimension());
            for (int i = 0; i < dimension(); i++) {
                java.util.List<E> row = new java.util.ArrayList<>(1);
                row.add(get(i));
                rows.add(row);
            }
            return org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix.of(rows, field);
        }
        throw new UnsupportedOperationException(
                "toMatrix requires a Field structure, found: " + getScalarRing().getClass().getSimpleName());
    }

    /**
     * Converts this vector to a rank-1 tensor.
     * 
     * @return the corresponding tensor
     * @throws UnsupportedOperationException if the vector is empty or scalars are
     *                                       not compatible
     */
    @SuppressWarnings("unchecked")
    default Tensor<E> toTensor() {
        if (dimension() == 0) {
            throw new UnsupportedOperationException("Cannot convert empty vector to tensor (type inference failed)");
        }

        E sample = get(0);
        Class<E> type = (Class<E>) sample.getClass();

        E[] elements = (E[]) java.lang.reflect.Array.newInstance(type, dimension());
        for (int i = 0; i < dimension(); i++) {
            elements[i] = get(i);
        }
        return Tensor.of(elements, dimension());
    }
}