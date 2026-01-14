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

package org.jscience.physics.quantum;

import org.jscience.mathematics.algebraic.AbstractVector;
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.Vector;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.matrices.AbstractComplexSquareMatrix;
import org.jscience.mathematics.algebraic.matrices.AbstractComplexVector;
import org.jscience.mathematics.algebraic.matrices.ComplexVector;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.modules.VectorSpace.Member;
import org.jscience.mathematics.algebraic.numbers.Complex;

import org.jscience.util.IllegalDimensionException;


/**
 * The BraVector class provides an object for encapsulating Dirac bra
 * vectors.
 *
 * @author Mark Hale
 * @version 1.5
 */
public final class BraVector extends AbstractVector {
    /** DOCUMENT ME! */
    private AbstractComplexVector representation;

/**
     * Constructs a bra vector given a vector representation.
     *
     * @param rep a vector representation
     */
    public BraVector(AbstractComplexVector rep) {
        super(rep.getDimension());
        representation = rep;
    }

    /**
     * Compares two bra vectors for equality.
     *
     * @param a a bra vector
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object a) {
        return representation.equals(((BraVector) a).representation);
    }

    /**
     * Returns a comma delimited string representing the value of this
     * bra vector.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return representation.toString();
    }

    /**
     * Returns a hashcode for this bra vector.
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return representation.hashCode();
    }

    /**
     * Map this bra vector to a ket vector.
     *
     * @return DOCUMENT ME!
     */
    public KetVector toKetVector() {
        return new KetVector(representation.conjugate());
    }

    /**
     * Returns the representation.
     *
     * @return DOCUMENT ME!
     */
    public AbstractComplexVector getRepresentation() {
        return representation;
    }

    /**
     * Returns the element.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex getElement(int i) {
        return representation.getElement(i);
    }

    /**
     * Returns the norm.
     *
     * @return DOCUMENT ME!
     */
    public double norm() {
        return representation.norm();
    }

    //============
    // OPERATIONS
    //============
    /**
     * Returns the negative of this vector.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member negate() {
        return representation.negate();
    }

    // ADDITION
    /**
     * Returns the addition of this vector and another.
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member add(AbelianGroup.Member v) {
        if (v instanceof BraVector) {
            return add((BraVector) v);
        } else {
            throw new IllegalArgumentException(
                "Vector class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param v a bra vector
     *
     * @return DOCUMENT ME!
     */
    public BraVector add(BraVector v) {
        return new BraVector(representation.add(v.representation));
    }

    // SUBTRACTION
    /**
     * Returns the subtraction of this vector by another.
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member subtract(AbelianGroup.Member v) {
        if (v instanceof BraVector) {
            return subtract((BraVector) v);
        } else {
            throw new IllegalArgumentException(
                "Vector class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param v a bra vector
     *
     * @return DOCUMENT ME!
     */
    public BraVector subtract(BraVector v) {
        return new BraVector(representation.subtract(v.representation));
    }

    // MULTIPLICATION
    /**
     * Returns the multiplication of this bra vector by a scalar.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Module.Member scalarMultiply(Ring.Member x) {
        return representation.scalarMultiply(x);
    }

    /**
     * Returns the multiplication of this bra vector and a ket vector.
     *
     * @param ket a ket vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors have different
     *         dimensions.
     */
    public Complex multiply(KetVector ket) {
        final int braDim = getDimension();

        if (braDim == ket.getDimension()) {
            AbstractComplexVector ketRep = ket.getRepresentation();
            Complex answer = representation.getElement(0)
                                           .multiply(ketRep.getElement(0));

            for (int i = 1; i < braDim; i++)
                answer = answer.add(representation.getElement(i)
                                                  .multiply(ketRep.getElement(i)));

            return answer;
        } else {
            throw new IllegalDimensionException(
                "Vectors have different dimensions.");
        }
    }

    /**
     * Returns the multiplication of this bra vector and an operator.
     *
     * @param op an operator
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the operator and vector have
     *         different dimensions.
     */
    public BraVector multiply(Operator op) {
        final int braDim = getDimension();

        if (braDim == op.getDimension()) {
            AbstractComplexSquareMatrix opRep = op.getRepresentation();
            Complex tmp;
            Complex[] array = new Complex[braDim];

            for (int j, i = 0; i < braDim; i++) {
                tmp = representation.getElement(0)
                                    .multiply(opRep.getElement(0, i));

                for (j = 1; j < braDim; j++)
                    tmp = tmp.add(representation.getElement(j)
                                                .multiply(opRep.getElement(j, i)));

                array[i] = tmp;
            }

            return new BraVector(new ComplexVector(array));
        } else {
            throw new IllegalDimensionException(
                "Operator and vector have different dimensions.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getRow(int i) {
        return representation.getRow(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getColumn(int j) {
        return representation.getColumn(j);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix transpose() {
        return representation.transpose();
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Member scalarDivide(
        org.jscience.mathematics.algebraic.fields.Field.Member f) {
        return representation.scalarDivide(f);
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public org.jscience.mathematics.algebraic.fields.Ring.Member multiply(
        org.jscience.mathematics.algebraic.fields.Ring.Member r) {
        return representation.multiply(r);
    }
}
