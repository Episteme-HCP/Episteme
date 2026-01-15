package org.jscience.mathematics.algebraic.algebras;

import org.jscience.mathematics.algebraic.fields.ComplexField;
import org.jscience.mathematics.algebraic.matrices.*;
import org.jscience.mathematics.algebraic.numbers.Complex;

import org.jscience.util.IllegalDimensionException;


/**
 * The su2Dim3 class encapsulates su(2) algebras using the 3 dimensional
 * (adjoint) representation. Elements are represented by 3-vectors with a
 * matrix basis.
 *
 * @author Mark Hale
 * @version 1.2
 */
public final class su2Dim3 extends LieAlgebra {
    /** Basis array. */
    private final static Complex[][] t1 = {
            { Complex.ZERO, ComplexField.SQRT_HALF, Complex.ZERO },
            { ComplexField.SQRT_HALF, Complex.ZERO, ComplexField.SQRT_HALF },
            { Complex.ZERO, ComplexField.SQRT_HALF, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t2 = {
            { Complex.ZERO, ComplexField.MINUS_SQRT_HALF_I, Complex.ZERO },
            {
                ComplexField.SQRT_HALF_I, Complex.ZERO,
                ComplexField.MINUS_SQRT_HALF_I
            },
            { Complex.ZERO, ComplexField.SQRT_HALF_I, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t3 = {
            { Complex.ONE, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, Complex.ZERO },
            { Complex.ZERO, Complex.ZERO, ComplexField.MINUS_ONE }
        };

    /** Basis. */
    private final static AbstractComplexSquareMatrix[] basisMatrices = {
            new ComplexSquareMatrix(t1), new ComplexSquareMatrix(t2),
            new ComplexSquareMatrix(t3)
        };

    /** Metric array. */
    private final static double[][] g = {
            { -2.0, 0.0, 0.0 },
            { 0.0, -2.0, 0.0 },
            { 0.0, 0.0, -2.0 }
        };

    /** Cartan metric. */
    private final static AbstractDoubleSquareMatrix metricMatrix = new DoubleSquareMatrix(g);

    /**
     * DOCUMENT ME!
     */
    private final static su2Dim3 _instance = new su2Dim3();

/**
     * Constructs an su(2) algebra.
     */
    private su2Dim3() {
        super("su(2) [3]");
    }

    /**
     * Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final su2Dim3 getInstance() {
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

        return ((Double3Vector) b).multiply((Double3Vector) a);
    }

    /**
     * Returns the Killing Form of two elements (scalar product).
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double killingForm(final AbstractDoubleVector a,
        final AbstractDoubleVector b) {
        return a.scalarProduct(metricMatrix.multiply(b));
    }

    /**
     * Returns the basis used to represent the Lie algebra.
     *
     * @return DOCUMENT ME!
     */
    public AbstractComplexSquareMatrix[] basis() {
        return basisMatrices;
    }

    /**
     * Returns the Cartan metric.
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleSquareMatrix cartanMetric() {
        return metricMatrix;
    }
}
