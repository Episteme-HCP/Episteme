package org.jscience.mathematics.algebraic.algebras;

import org.jscience.mathematics.algebraic.fields.ComplexField;
import org.jscience.mathematics.algebraic.matrices.*;
import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * The so3_1Dim4 class encapsulates so(3,1) algebras using the 4
 * dimensional (fundamental) representation. Elements are represented by
 * vectors with a matrix basis.
 *
 * @author Mark Hale
 * @version 1.2
 */
public final class so3_1Dim4 extends LieAlgebra {
    // Rotations
    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t1 = {
            { Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO, ComplexField.MINUS_I },
            { Complex.ZERO, Complex.ZERO, Complex.I, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t2 = {
            { Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.I },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, ComplexField.MINUS_I, Complex.ZERO, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t3 = {
            { Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, ComplexField.MINUS_I, Complex.ZERO },
            { Complex.ZERO, Complex.I, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO }
        };

    // Boosts
    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t4 = {
            { Complex.ZERO, ComplexField.MINUS_I, Complex.ZERO, Complex.ZERO },
            { ComplexField.MINUS_I, Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t5 = {
            { Complex.ZERO, Complex.ZERO, ComplexField.MINUS_I, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { ComplexField.MINUS_I, Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t6 = {
            { Complex.ZERO, Complex.ZERO, Complex.ZERO, ComplexField.MINUS_I },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { ComplexField.MINUS_I, Complex.ZERO, Complex.ZERO, Complex.ZERO }
        };

    /** Basis. */
    private final static AbstractComplexSquareMatrix[] basisMatrices = {
            new ComplexSquareMatrix(t1), new ComplexSquareMatrix(t2),
            new ComplexSquareMatrix(t3), new ComplexSquareMatrix(t4),
            new ComplexSquareMatrix(t5), new ComplexSquareMatrix(t6)
        };

    /**
     * DOCUMENT ME!
     */
    private final static so3_1Dim4 _instance = new so3_1Dim4();

/**
     * Constructs an so(3,1) algebra.
     */
    private so3_1Dim4() {
        super("so(3,1) [4]");
    }

    /**
     * Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final so3_1Dim4 getInstance() {
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
        m = m.add(basisMatrices[3].scalarMultiply(v.getPrimitiveElement(3)));
        m = m.add(basisMatrices[4].scalarMultiply(v.getPrimitiveElement(4)));
        m = m.add(basisMatrices[5].scalarMultiply(v.getPrimitiveElement(5)));

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
     */
    public AbstractDoubleVector multiply(final AbstractDoubleVector a,
        final AbstractDoubleVector b) {
        double[] array = new double[6];
        array[0] = ((a.getPrimitiveElement(2) * b.getPrimitiveElement(1)) -
            (a.getPrimitiveElement(1) * b.getPrimitiveElement(2)) +
            (a.getPrimitiveElement(4) * b.getPrimitiveElement(5))) -
            (a.getPrimitiveElement(5) * b.getPrimitiveElement(4));
        array[1] = ((a.getPrimitiveElement(0) * b.getPrimitiveElement(2)) -
            (a.getPrimitiveElement(2) * b.getPrimitiveElement(0)) +
            (a.getPrimitiveElement(5) * b.getPrimitiveElement(3))) -
            (a.getPrimitiveElement(3) * b.getPrimitiveElement(5));
        array[2] = ((a.getPrimitiveElement(1) * b.getPrimitiveElement(0)) -
            (a.getPrimitiveElement(0) * b.getPrimitiveElement(1)) +
            (a.getPrimitiveElement(3) * b.getPrimitiveElement(4))) -
            (a.getPrimitiveElement(4) * b.getPrimitiveElement(3));
        array[3] = ((a.getPrimitiveElement(2) * b.getPrimitiveElement(4)) -
            (a.getPrimitiveElement(1) * b.getPrimitiveElement(5)) +
            (a.getPrimitiveElement(5) * b.getPrimitiveElement(1))) -
            (a.getPrimitiveElement(4) * b.getPrimitiveElement(2));
        array[4] = ((a.getPrimitiveElement(0) * b.getPrimitiveElement(5)) -
            (a.getPrimitiveElement(2) * b.getPrimitiveElement(3)) +
            (a.getPrimitiveElement(3) * b.getPrimitiveElement(2))) -
            (a.getPrimitiveElement(5) * b.getPrimitiveElement(0));
        array[5] = ((a.getPrimitiveElement(1) * b.getPrimitiveElement(3)) -
            (a.getPrimitiveElement(0) * b.getPrimitiveElement(4)) +
            (a.getPrimitiveElement(4) * b.getPrimitiveElement(0))) -
            (a.getPrimitiveElement(3) * b.getPrimitiveElement(1));

        return new DoubleVector(array);
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
