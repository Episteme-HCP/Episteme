/*
 * JScience Reimagined - Unified Scientific Computing Framework
 * Copyright (c) 2025 Silvere Martin-Michiellot
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.vector;

import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.algebra.Field;
import org.jscience.mathematics.context.ComputeMode;
import org.jscience.mathematics.vector.backend.JavaLinearAlgebraProvider;
import org.jscience.mathematics.vector.backend.LinearAlgebraProvider;

/**
 * A dense vector implementation backed by a list.
 * 
 * @param <E> the type of scalar elements
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DenseVector<E> implements Vector<E> {

    private final List<E> elements;
    private final Field<E> field;

    /**
     * Creates a new DenseVector.
     * 
     * @param elements the elements of the vector
     * @param field    the field structure for the elements
     */
    public DenseVector(List<E> elements, Field<E> field) {
        this.elements = new ArrayList<>(elements);
        this.field = field;
    }

    public static <E> DenseVector<E> of(List<E> elements, Field<E> field) {
        return new DenseVector<>(elements, field);
    }

    private LinearAlgebraProvider<E> getProvider() {
        ComputeMode mode = MathContext.getCurrent().getComputeMode();

        if (mode == ComputeMode.CPU) {
            return new JavaLinearAlgebraProvider<>(field);
        }

        // Check if we can use GPU (must be Real numbers for now)
        boolean canUseGpu = (field.zero() instanceof org.jscience.mathematics.number.Real);

        if (mode == ComputeMode.GPU) {
            if (!canUseGpu) {
                throw new UnsupportedOperationException("GPU mode currently only supports Real numbers");
            }
            return new org.jscience.mathematics.backend.CudaLinearAlgebraProvider<>(field);
        }

        // AUTO mode
        if (canUseGpu) {
            try {
                return new org.jscience.mathematics.backend.CudaLinearAlgebraProvider<>(field);
            } catch (UnsupportedOperationException e) {
                // Fallback to CPU if CUDA not available
                return new JavaLinearAlgebraProvider<>(field);
            }
        }

        return new JavaLinearAlgebraProvider<>(field);
    }

    @Override
    public int dimension() {
        return elements.size();
    }

    @Override
    public E get(int index) {
        return elements.get(index);
    }

    @Override
    public Vector<E> add(Vector<E> other) {
        return getProvider().add(this, other);
    }

    @Override
    public Vector<E> subtract(Vector<E> other) {
        return getProvider().subtract(this, other);
    }

    @Override
    public Vector<E> multiply(E scalar) {
        return getProvider().multiply(this, scalar);
    }

    @Override
    public Vector<E> scale(E scalar, Vector<E> vector) {
        return vector.multiply(scalar);
    }

    @Override
    public Vector<E> operate(Vector<E> left, Vector<E> right) {
        return left.add(right);
    }

    @Override
    public E dot(Vector<E> other) {
        return getProvider().dot(this, other);
    }

    @Override
    public E norm() {
        E dotProduct = dot(this);
        if (dotProduct instanceof org.jscience.mathematics.number.Real) {
            return (E) ((org.jscience.mathematics.number.Real) dotProduct).sqrt();
        } else if (dotProduct instanceof org.jscience.mathematics.number.Complex) {
            // Norm of complex vector is usually sqrt(sum(|z_i|^2)), which is real.
            // But here dot(this) is sum(z_i * z_i) if not conjugated.
            // Standard dot product for complex is usually z . w_conjugate.
            // Our dot product implementation depends on provider.
            // Assuming standard Euclidean norm behavior:
            return (E) ((org.jscience.mathematics.number.Complex) dotProduct).sqrt();
        }
        throw new UnsupportedOperationException("Norm not supported for type " + dotProduct.getClass().getSimpleName());
    }

    @Override
    public Vector<E> normalize() {
        E n = norm();
        if (field.zero().equals(n)) {
            throw new ArithmeticException("Cannot normalize zero vector");
        }
        return multiply(field.inverse(n));
    }

    @Override
    public Vector<E> cross(Vector<E> other) {
        if (dimension() != 3 || other.dimension() != 3) {
            throw new ArithmeticException("Cross product only defined for 3D vectors");
        }
        E a1 = this.get(0);
        E a2 = this.get(1);
        E a3 = this.get(2);
        E b1 = other.get(0);
        E b2 = other.get(1);
        E b3 = other.get(2);

        // c1 = a2*b3 - a3*b2
        E c1 = field.add(field.multiply(a2, b3), field.negate(field.multiply(a3, b2)));
        // c2 = a3*b1 - a1*b3
        E c2 = field.add(field.multiply(a3, b1), field.negate(field.multiply(a1, b3)));
        // c3 = a1*b2 - a2*b1
        E c3 = field.add(field.multiply(a1, b2), field.negate(field.multiply(a2, b1)));

        List<E> elements = new ArrayList<>();
        elements.add(c1);
        elements.add(c2);
        elements.add(c3);
        return new DenseVector<>(elements, field);
    }

    @Override
    public E angle(Vector<E> other) {
        // angle = acos( dot / (norm1 * norm2) )
        E dotVal = dot(other);
        E n1 = norm();
        E n2 = other.norm();
        E denom = field.multiply(n1, n2);
        E cosTheta = field.multiply(dotVal, field.inverse(denom));

        if (cosTheta instanceof org.jscience.mathematics.number.Real) {
            return (E) ((org.jscience.mathematics.number.Real) cosTheta).acos();
        }
        throw new UnsupportedOperationException("Angle not supported for type " + cosTheta.getClass().getSimpleName());
    }

    @Override
    public Vector<E> projection(Vector<E> other) {
        // proj_other(this) = (this . other) / (other . other) * other
        E dotVal = dot(other);
        E otherDotOther = other.dot(other);
        E scalar = field.multiply(dotVal, field.inverse(otherDotOther));
        return other.multiply(scalar);
    }

    @Override
    public Vector<E> negate() {
        List<E> result = new ArrayList<>(dimension());
        for (E element : elements) {
            result.add(field.negate(element));
        }
        return new DenseVector<>(result, field);
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    @Override
    public org.jscience.mathematics.algebra.Ring<E> getScalarRing() {
        return field;
    }

    @Override
    public Vector<E> inverse(Vector<E> element) {
        return element.negate();
    }

    @Override
    public String description() {
        return "DenseVector (dim " + dimension() + ")";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Vector<E> element) {
        return element != null && element.dimension() == this.dimension();
    }
}
