package org.jscience.mathematics.analysis.taylor;

/**
 * The abstract superclass for functions of two <b>TaylorDouble</b> variables.
 *
 * @author Tue Lehn-Schioeler
 * @author Laurits H&oslash;jgaard Olesen
 */
public abstract class TaylorBinary extends TaylorDouble {
    /**
     * The left operand of this binary operator.
     */
    protected TaylorDouble leftOperand;

    /**
     * The right operand of this binary operator.
     */
    protected TaylorDouble rightOperand;

    /**
     * Default constructor for functions of two variables.
     */
    public TaylorBinary(TaylorDouble leftOp, TaylorDouble rightOp) {
        coeffs = new double[10];
        order = -1;
        leftOperand = leftOp;
        rightOperand = rightOp;

        if (leftOp.constant && rightOp.constant) {
            constant = true;
        } else {
            constant = false;
        }
    }

    /**
     * Signal shift of expansion point.
     * <p/>
     * All current coefficients of <i>this</i>, and operands are expected to
     * be invalid, so <i>order</i> is set to -1.
     * <p/>
     * Notice: if <i>order</i> is -1, corresponding to a newly <i>reset</i> of this,
     * nothing is done, and the operands are thus not guaranteed to be <i>reset</i>!
     */
    public void reset() {
        if (order < 0) {
            return;
        }

        order = -1;
        leftOperand.reset();
        rightOperand.reset();
    }
}
