package org.jscience.mathematics.algebraic.algebras;

import org.jscience.mathematics.algebraic.fields.ComplexField;
import org.jscience.mathematics.algebraic.matrices.*;
import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * The su3Dim3 class encapsulates su(3) algebras using the 3 dimensional
 * (fundamental) representation. Elements are represented by vectors with a
 * matrix basis.
 *
 * @author Mark Hale
 * @version 1.2
 */
public final class su3Dim3 extends LieAlgebra {
    /** Useful complex constants. */
    private final static Complex T8_1 = new Complex(0.5 / Math.sqrt(3.0), 0.0);

    /**
     * DOCUMENT ME!
     */
    private final static Complex T8_2 = new Complex(-1.0 / Math.sqrt(3.0), 0.0);

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t1 = {
            { Complex.ZERO, ComplexField.HALF, Complex.ZERO },
            { ComplexField.HALF, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t2 = {
            { Complex.ZERO, ComplexField.MINUS_HALF_I, Complex.ZERO },
            { ComplexField.HALF_I, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t3 = {
            { ComplexField.HALF, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, ComplexField.MINUS_HALF, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t4 = {
            { Complex.ZERO, Complex.ZERO, ComplexField.HALF },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { ComplexField.HALF, Complex.ZERO, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t5 = {
            { Complex.ZERO, Complex.ZERO, ComplexField.MINUS_HALF_I },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { ComplexField.HALF_I, Complex.ZERO, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t6 = {
            { Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, ComplexField.HALF },
            { Complex.ZERO, ComplexField.HALF, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t7 = {
            { Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, ComplexField.MINUS_HALF_I },
            { Complex.ZERO, ComplexField.HALF_I, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t8 = {
            { T8_1, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, T8_1, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, T8_2 }
        };

    /** Basis. */
    private final static AbstractComplexSquareMatrix[] basisMatrices = {
            new ComplexSquareMatrix(t1), new ComplexSquareMatrix(t2),
            new ComplexSquareMatrix(t3), new ComplexSquareMatrix(t4),
            new ComplexSquareMatrix(t5), new ComplexSquareMatrix(t6),
            new ComplexSquareMatrix(t7), new ComplexSquareMatrix(t8)
        };

    /**
     * DOCUMENT ME!
     */
    private final static su3Dim3 _instance = new su3Dim3();

/**
     * Constructs an su(3) algebra.
     */
    private su3Dim3() {
        super("su(3) [3]");
    }

    /**
     * Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final su3Dim3 getInstance() {
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
        m = m.add(basisMatrices[6].scalarMultiply(v.getPrimitiveElement(6)));
        m = m.add(basisMatrices[7].scalarMultiply(v.getPrimitiveElement(7)));

        return (AbstractComplexSquareMatrix) m.scalarMultiply(Complex.I);
    }

    /**
     * Returns the Lie bracket (commutator) of two elements.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector multiply(final AbstractDoubleVector a,
        final AbstractDoubleVector b) {
        double[] array = new double[8];
        array[0] = (b.getPrimitiveElement(1) * a.getPrimitiveElement(2)) -
            (b.getPrimitiveElement(2) * a.getPrimitiveElement(1)) +
            (0.5 * (((b.getPrimitiveElement(3) * a.getPrimitiveElement(6)) -
            (b.getPrimitiveElement(6) * a.getPrimitiveElement(3)) +
            (b.getPrimitiveElement(5) * a.getPrimitiveElement(4))) -
            (b.getPrimitiveElement(4) * a.getPrimitiveElement(5))));
        array[1] = (b.getPrimitiveElement(2) * a.getPrimitiveElement(0)) -
            (b.getPrimitiveElement(0) * a.getPrimitiveElement(2)) +
            (0.5 * (((b.getPrimitiveElement(3) * a.getPrimitiveElement(5)) -
            (b.getPrimitiveElement(5) * a.getPrimitiveElement(3)) +
            (b.getPrimitiveElement(4) * a.getPrimitiveElement(6))) -
            (b.getPrimitiveElement(6) * a.getPrimitiveElement(4))));
        array[2] = (b.getPrimitiveElement(0) * a.getPrimitiveElement(1)) -
            (b.getPrimitiveElement(1) * a.getPrimitiveElement(0)) +
            (0.5 * (((b.getPrimitiveElement(3) * a.getPrimitiveElement(4)) -
            (b.getPrimitiveElement(4) * a.getPrimitiveElement(3)) +
            (b.getPrimitiveElement(6) * a.getPrimitiveElement(5))) -
            (b.getPrimitiveElement(5) * a.getPrimitiveElement(6))));
        array[3] = (Math.sqrt(0.75) * ((b.getPrimitiveElement(4) * a.getPrimitiveElement(7)) -
            (b.getPrimitiveElement(7) * a.getPrimitiveElement(4)))) +
            (0.5 * ((((b.getPrimitiveElement(6) * a.getPrimitiveElement(0)) -
            (b.getPrimitiveElement(0) * a.getPrimitiveElement(6)) +
            (b.getPrimitiveElement(5) * a.getPrimitiveElement(1))) -
            (b.getPrimitiveElement(1) * a.getPrimitiveElement(5)) +
            (b.getPrimitiveElement(4) * a.getPrimitiveElement(2))) -
            (b.getPrimitiveElement(2) * a.getPrimitiveElement(4))));
        array[4] = (Math.sqrt(0.75) * ((b.getPrimitiveElement(7) * a.getPrimitiveElement(3)) -
            (b.getPrimitiveElement(3) * a.getPrimitiveElement(7)))) +
            (0.5 * ((((b.getPrimitiveElement(0) * a.getPrimitiveElement(5)) -
            (b.getPrimitiveElement(5) * a.getPrimitiveElement(0)) +
            (b.getPrimitiveElement(6) * a.getPrimitiveElement(1))) -
            (b.getPrimitiveElement(1) * a.getPrimitiveElement(6)) +
            (b.getPrimitiveElement(2) * a.getPrimitiveElement(3))) -
            (b.getPrimitiveElement(3) * a.getPrimitiveElement(2))));
        array[5] = (Math.sqrt(0.75) * ((b.getPrimitiveElement(6) * a.getPrimitiveElement(7)) -
            (b.getPrimitiveElement(7) * a.getPrimitiveElement(6)))) +
            (0.5 * ((((b.getPrimitiveElement(4) * a.getPrimitiveElement(0)) -
            (b.getPrimitiveElement(0) * a.getPrimitiveElement(4)) +
            (b.getPrimitiveElement(1) * a.getPrimitiveElement(3))) -
            (b.getPrimitiveElement(3) * a.getPrimitiveElement(1)) +
            (b.getPrimitiveElement(2) * a.getPrimitiveElement(6))) -
            (b.getPrimitiveElement(6) * a.getPrimitiveElement(2))));
        array[6] = (Math.sqrt(0.75) * ((b.getPrimitiveElement(7) * a.getPrimitiveElement(5)) -
            (b.getPrimitiveElement(5) * a.getPrimitiveElement(7)))) +
            (0.5 * ((((b.getPrimitiveElement(0) * a.getPrimitiveElement(3)) -
            (b.getPrimitiveElement(3) * a.getPrimitiveElement(0)) +
            (b.getPrimitiveElement(1) * a.getPrimitiveElement(4))) -
            (b.getPrimitiveElement(4) * a.getPrimitiveElement(1)) +
            (b.getPrimitiveElement(5) * a.getPrimitiveElement(2))) -
            (b.getPrimitiveElement(2) * a.getPrimitiveElement(5))));
        array[7] = Math.sqrt(0.75) * (((b.getPrimitiveElement(3) * a.getPrimitiveElement(4)) -
            (b.getPrimitiveElement(4) * a.getPrimitiveElement(3)) +
            (b.getPrimitiveElement(5) * a.getPrimitiveElement(6))) -
            (b.getPrimitiveElement(6) * a.getPrimitiveElement(5)));

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
