package org.jscience.mathematics.algebraic.algebras;

import org.jscience.mathematics.algebraic.fields.ComplexField;
import org.jscience.mathematics.algebraic.matrices.*;
import org.jscience.mathematics.algebraic.numbers.Complex;

import org.jscience.util.IllegalDimensionException;


/**
 * The sp2_RDim3 class encapsulates sp(2,R) algebras using the 3
 * dimensional (adjoint) representation. Elements are represented by 3-vectors
 * with a matrix basis.
 *
 * @author Mark Hale
 * @version 1.2
 */
public final class sp2_RDim3 extends LieAlgebra {
    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t1 = {
            { Complex.ZERO, Complex.ZERO, ComplexField.MINUS_HALF },
            { Complex.ZERO, Complex.ZERO, ComplexField.HALF },
            { ComplexField.MINUS_TWO, ComplexField.TWO, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t2 = {
            { Complex.ZERO, Complex.ZERO, ComplexField.MINUS_HALF },
            { Complex.ZERO, Complex.ZERO, ComplexField.MINUS_HALF },
            { ComplexField.TWO, ComplexField.TWO, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t3 = {
            { Complex.ONE, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, ComplexField.MINUS_ONE, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO }
        };

    /** Basis. */
    private final static AbstractComplexSquareMatrix[] basisMatrices = {
            new ComplexSquareMatrix(t1), new ComplexSquareMatrix(t2),
            new ComplexSquareMatrix(t3)
        };

    /**
     * DOCUMENT ME!
     */
    private final static sp2_RDim3 _instance = new sp2_RDim3();

/**
     * Constructs an sp(2,R) algebra.
     */
    private sp2_RDim3() {
        super("sp(2,R) [3]");
    }

    /**
     * Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final sp2_RDim3 getInstance() {
        return _instance;
    }

    /**
     * Returns an element as a matrix (vectorbasis).
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractComplexSquareMatrix getElement(final AbstractDoubleVector v) {
        AbstractComplexMatrix m = basisMatrices[0].scalarMultiply(v.getPrimitiveElement(
                    0));
        m = m.add(basisMatrices[1].scalarMultiply(v.getPrimitiveElement(1)));
        m = m.add(basisMatrices[2].scalarMultiply(v.getPrimitiveElement(2)));

        return (AbstractComplexSquareMatrix) m.scalarMultiply(Complex.I);
    }

    /**
     * Returns the Lie bracket (commutator) of two elements. Same as
     * the vector cross product.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractDoubleVector multiply(final AbstractDoubleVector a,
        final AbstractDoubleVector b) {
        if (!(a instanceof Double3Vector) || !(b instanceof Double3Vector)) {
            throw new IllegalDimensionException("Vectors must be 3-vectors.");
        }

        return new Double3Vector((a.getPrimitiveElement(2) * b.getPrimitiveElement(
                1)) - (a.getPrimitiveElement(1) * b.getPrimitiveElement(2)),
            (a.getPrimitiveElement(2) * b.getPrimitiveElement(0)) -
            (a.getPrimitiveElement(0) * b.getPrimitiveElement(2)),
            (a.getPrimitiveElement(1) * b.getPrimitiveElement(0)) -
            (a.getPrimitiveElement(0) * b.getPrimitiveElement(1)));
    }

    /**
     * Returns the basis used to represent the Lie algebra.
     *
     * @return DOCUMENT ME!
     */
    public AbstractComplexSquareMatrix[] basis() {
        return basisMatrices;
    }
}
