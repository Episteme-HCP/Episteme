package org.jscience.physics.quantum;

import org.jscience.mathematics.algebraic.AbstractVector;
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.Vector;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.matrices.AbstractComplexVector;
import org.jscience.mathematics.algebraic.matrices.ComplexSquareMatrix;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.modules.VectorSpace.Member;
import org.jscience.mathematics.algebraic.numbers.Complex;

import org.jscience.util.IllegalDimensionException;


/**
 * The KetVector class provides an object for encapsulating Dirac ket
 * vectors.
 *
 * @author Mark Hale
 * @version 1.5
 */
public final class KetVector extends AbstractVector {
    /** DOCUMENT ME! */
    private AbstractComplexVector representation;

/**
     * Constructs a ket vector given a vector representation.
     *
     * @param rep a vector representation
     */
    public KetVector(AbstractComplexVector rep) {
        super(rep.getDimension());
        representation = rep;
    }

    /**
     * Compares two ket vectors for equality.
     *
     * @param a a ket vector
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object a) {
        return representation.equals(((KetVector) a).representation);
    }

    /**
     * Returns a comma delimited string representing the value of this
     * ket vector.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return representation.toString();
    }

    /**
     * Returns a hashcode for this ket vector.
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return representation.hashCode();
    }

    /**
     * Map this ket vector to a bra vector.
     *
     * @return DOCUMENT ME!
     */
    public BraVector toBraVector() {
        return new BraVector(representation.conjugate());
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
        if (v instanceof KetVector) {
            return add((KetVector) v);
        } else {
            throw new IllegalArgumentException(
                "Vector class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param v a ket vector
     *
     * @return DOCUMENT ME!
     */
    public KetVector add(KetVector v) {
        return new KetVector(representation.add(v.representation));
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
        if (v instanceof KetVector) {
            return subtract((KetVector) v);
        } else {
            throw new IllegalArgumentException(
                "Vector class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param v a ket vector
     *
     * @return DOCUMENT ME!
     */
    public KetVector subtract(KetVector v) {
        return new KetVector(representation.subtract(v.representation));
    }

    // MULTIPLICATION
    /**
     * Returns the multiplication of this ket vector by a scalar.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Module.Member scalarMultiply(Ring.Member x) {
        return representation.scalarMultiply(x);
    }

    /**
     * Returns the multiplication of this ket vector and a bra vector.
     *
     * @param bra a bra vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors have different
     *         dimensions.
     */
    public Operator multiply(BraVector bra) {
        final int ketDim = getDimension();

        if (ketDim == bra.getDimension()) {
            AbstractComplexVector braRep = bra.getRepresentation();
            Complex[][] array = new Complex[ketDim][ketDim];

            for (int j, i = 0; i < ketDim; i++) {
                array[i][0] = representation.getElement(i)
                                            .multiply(braRep.getElement(0));

                for (j = 1; j < ketDim; j++)
                    array[i][j] = representation.getElement(i)
                                                .multiply(braRep.getElement(j));
            }

            return new Operator(new ComplexSquareMatrix(array));
        } else {
            throw new IllegalDimensionException(
                "Vectors have different dimensions.");
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
