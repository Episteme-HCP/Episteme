package org.jscience.physics.quantum;

import org.jscience.mathematics.Member;
import org.jscience.mathematics.algebraic.matrices.AbstractComplexSquareMatrix;
import org.jscience.mathematics.algebraic.matrices.AbstractComplexVector;
import org.jscience.mathematics.algebraic.matrices.ComplexVector;
import org.jscience.mathematics.algebraic.numbers.Complex;

import org.jscience.util.IllegalDimensionException;
import org.jscience.util.MaximumIterationsExceededException;


/**
 * The Operator class provides an object for encapsulating quantum
 * mechanical operators.
 *
 * @author Mark Hale
 * @version 1.5
 */
public class Operator extends Object implements Member {
    /** DOCUMENT ME! */
    protected AbstractComplexSquareMatrix representation;

/**
     * Constructs an operator given a matrix representation.
     *
     * @param rep a matrix representation
     */
    public Operator(AbstractComplexSquareMatrix rep) {
        representation = rep;
    }

    /**
     * Compares two operators for equality.
     *
     * @param a an operator
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object a) {
        return representation.equals(((Operator) a).representation);
    }

    /**
     * Returns a string representing this operator.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return representation.toString();
    }

    /**
     * Returns a hashcode for this operator.
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return (int) Math.exp(trace().mod());
    }

    /**
     * Returns the representation.
     *
     * @return DOCUMENT ME!
     */
    public AbstractComplexSquareMatrix getRepresentation() {
        return representation;
    }

    /**
     * Returns true if this operator is self-adjoint.
     *
     * @return DOCUMENT ME!
     */
    public boolean isSelfAdjoint() {
        return representation.isHermitian();
    }

    /**
     * Returns true if this operator is unitary.
     *
     * @return DOCUMENT ME!
     */
    public boolean isUnitary() {
        return representation.isUnitary();
    }

    /**
     * Returns the trace.
     *
     * @return DOCUMENT ME!
     */
    public Complex trace() {
        return representation.trace();
    }

    /**
     * Returns the operator norm.
     *
     * @return DOCUMENT ME!
     */
    public double norm() {
        try {
            return representation.operatorNorm();
        } catch (MaximumIterationsExceededException e) {
            return -1.0;
        }
    }

    /**
     * Returns the dimension.
     *
     * @return DOCUMENT ME!
     */
    public int getDimension() {
        return representation.numColumns();
    }

    //============
    // OPERATIONS
    //============
    // ADDITION
    /**
     * Returns the addition of this operator and another.
     *
     * @param op an operator
     *
     * @return DOCUMENT ME!
     */
    public Operator add(Operator op) {
        return new Operator(representation.add(op.representation));
    }

    // SUBTRACTION
    /**
     * Returns the subtraction of this operator and another.
     *
     * @param op an operator
     *
     * @return DOCUMENT ME!
     */
    public Operator subtract(Operator op) {
        return new Operator(representation.subtract(op.representation));
    }

    // MULTIPLICATION
    /**
     * Returns the multiplication of this operator and another.
     *
     * @param op an operator
     *
     * @return DOCUMENT ME!
     */
    public Operator multiply(Operator op) {
        return new Operator(representation.multiply(op.representation));
    }

    /**
     * Returns the multiplication of this operator and a ket vector.
     *
     * @param ket a ket vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the operator and vector have
     *         different dimensions.
     */
    public KetVector multiply(KetVector ket) {
        int opDim = getDimension();

        if (opDim == ket.getDimension()) {
            AbstractComplexVector ketRep = ket.getRepresentation();
            Complex tmp;
            Complex[] array = new Complex[opDim];

            for (int j, i = 0; i < opDim; i++) {
                tmp = representation.getElement(i, 0)
                                    .multiply(ketRep.getElement(0));

                for (j = 1; j < opDim; j++)
                    tmp = tmp.add(representation.getElement(i, j)
                                                .multiply(ketRep.getElement(j)));

                array[i] = tmp;
            }

            return new KetVector(new ComplexVector(array));
        } else {
            throw new IllegalDimensionException(
                "Operator and vector have different dimensions.");
        }
    }
}
