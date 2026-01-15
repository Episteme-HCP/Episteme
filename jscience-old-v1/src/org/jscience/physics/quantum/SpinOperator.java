package org.jscience.physics.quantum;

import org.jscience.mathematics.algebraic.algebras.LieAlgebra;
import org.jscience.mathematics.algebraic.algebras.su2Dim2;
import org.jscience.mathematics.algebraic.algebras.su2Dim3;
import org.jscience.mathematics.algebraic.matrices.AbstractComplexSquareMatrix;
import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * The SpinOperator class provides an object for encapsulating spin
 * operators.
 *
 * @author Mark Hale
 * @version 2.0
 */
public final class SpinOperator extends Operator {
    /** DOCUMENT ME! */
    private static final LieAlgebra spin1_2 = su2Dim2.getInstance();

    /** DOCUMENT ME! */
    private static final LieAlgebra spin1 = su2Dim3.getInstance();

    /** Spin 1/2 operator (x). */
    public static final SpinOperator X1_2 = new SpinOperator(spin1_2.basis()[0]);

    /** Spin 1/2 operator (y). */
    public static final SpinOperator Y1_2 = new SpinOperator(spin1_2.basis()[1]);

    /** Spin 1/2 operator (z). */
    public static final SpinOperator Z1_2 = new SpinOperator(spin1_2.basis()[2]);

    /** Spin 1 operator (x). */
    public static final SpinOperator X1 = new SpinOperator(spin1.basis()[0]);

    /** Spin 1 operator (y). */
    public static final SpinOperator Y1 = new SpinOperator(spin1.basis()[1]);

    /** Spin 1 operator (z). */
    public static final SpinOperator Z1 = new SpinOperator(spin1.basis()[2]);

/**
     * Constructs a spin operator.
     *
     * @param spinMatrix DOCUMENT ME!
     */
    private SpinOperator(AbstractComplexSquareMatrix spinMatrix) {
        super(spinMatrix);
    }

    /**
     * Returns true if this operator is self-adjoint.
     *
     * @return DOCUMENT ME!
     */
    public boolean isSelfAdjoint() {
        return true;
    }

    /**
     * Returns true if this operator is unitary.
     *
     * @return DOCUMENT ME!
     */
    public boolean isUnitary() {
        return true;
    }

    /**
     * Returns the trace.
     *
     * @return DOCUMENT ME!
     */
    public Complex trace() {
        return Complex.ZERO;
    }
}
