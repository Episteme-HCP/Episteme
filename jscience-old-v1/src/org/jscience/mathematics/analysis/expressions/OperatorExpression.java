package org.jscience.mathematics.analysis.expressions;

/**
 * Abstract class used as super class for classes representing both unary
 * and binary operators (and more).
 *
 * @author Martin Egholm Nielsen
 * @version 1.0
 *
 * @see Expression
 * @see UnaryOperator
 * @see BinaryOperator
 * @see DataExpression
 */
public abstract class OperatorExpression implements Expression {
    /**
     * The first (and maybe only) Expression object an operator works
     * on.
     *
     * @since 1.0
     */
    protected Expression firstOperand;

/**
     * Creates a new OperatorExpression object, and assigns the operand to the
     * given Expression.
     *
     * @param e1 The Expression object that the operator should work on.
     */
    public OperatorExpression(Expression e1) {
        setFirstOperand(e1);
    }

    /**
     * Assigns the first (and maybe only) operand that the
     * OperatorExpression object works on to a given Expression object.
     *
     * @param e1 An Expression object.
     *
     * @see #getFirstOperand
     * @since 1.0
     */
    public void setFirstOperand(Expression e1) {
        firstOperand = e1;
    }

    /**
     * Returns the first operand that the OperatorExpression object
     * works on.
     *
     * @return An Expression object that represents the first operand that the
     *         OperatorExpression object works on.
     *
     * @see #setFirstOperand
     * @since 1.0
     */
    public Expression getFirstOperand() {
        return firstOperand;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Expression e) {
        java.util.List v = this.getElements();

        for (int i = 0; i < v.size(); i++)
            if (e.equals((Expression) v.get(i))) {
                return true;
            }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression isolate(Expression e) {
        return isolate(new Constant(0), e);
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression isolate(Expression f, Expression e) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression factor(Expression e) {
        return null;
    }
} // OperaterExpression
