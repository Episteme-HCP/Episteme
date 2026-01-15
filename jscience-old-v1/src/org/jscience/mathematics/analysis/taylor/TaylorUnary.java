package org.jscience.mathematics.analysis.taylor;

/**
 * The abstract superclass for functions of one <b>TaylorDouble</b> variable.
 *
 * @author Tue Lehn-Schioeler
 * @author Laurits H&oslash;jgaard Olesen
 */
public abstract class TaylorUnary extends TaylorDouble {
    /**
     * The operand of this unary operator.
     */
    public TaylorDouble operand;

    /**
     * Default constructor for functions of one variable.
     */
    public TaylorUnary(TaylorDouble op) {
        coeffs = new double[10];
        order = -1;
        operand = op;

        if (op.constant) {
            constant = true;
        } else {
            constant = false;
        }
    }

    /**
     * Signal shift of expansion point.
     * <p/>
     * All current coefficients of <i>this</i> and the operand are expected to be
     * invalid, so <i>order</i> is set to -1.
     * <p/>
     * Notice: if <i>order</i> is -1, corresponding to a newly <i>reset</i> of this,
     * nothing is done, and the operand is thus not guaranteed to be <i>reset</i>!
     */
    public void reset() {
        if (order < 0) {
            return;
        }

        order = -1;
        operand.reset();
    }
}
