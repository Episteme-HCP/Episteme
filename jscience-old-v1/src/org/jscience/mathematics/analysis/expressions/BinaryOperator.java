package org.jscience.mathematics.analysis.expressions;

/**
 * Abstract class used as superclass for binary operators.
 *
 * @author Martin Egholm Nielsen
 * @version 1.0
 *
 * @see UnaryOperator
 * @see OperatorExpression
 */
public abstract class BinaryOperator extends OperatorExpression {
    /**
     * The second Expression this BinaryOperator works on.
     *
     * @see #firstOperand
     * @since 1.0
     */
    protected Expression secondOperand;

    /** DOCUMENT ME! */
    protected boolean evalValid = false;

    /** DOCUMENT ME! */
    protected double evalValue = Double.NaN;

    /** DOCUMENT ME! */
    protected boolean[] valid = null;

    /** DOCUMENT ME! */
    protected double[] deriv = null;

/**
     * Creates a BinaryOperator object working on two given Expression objects.
     *
     * @param e1 An Expression object.
     * @param e2 Another Expression object.
     * @since 1.0
     */
    public BinaryOperator(Expression e1, Expression e2) {
        super(e1);
        setSecondOperand(e2);
    } // BinaryOperator( , )

    /**
     * Assigns the second operand that the BinaryOperator object works
     * on to a given Expression object.
     *
     * @param e2 An Expression object.
     *
     * @see OperatorExpression#setFirstOperand
     * @since 1.0
     */
    public void setSecondOperand(Expression e2) {
        secondOperand = e2;
    }

    /**
     * Returns the second operand that the BinaryOperator object works
     * on.
     *
     * @return An Expression object that represents the second operand that the
     *         BinaryOperator object works on.
     *
     * @see OperatorExpression#getFirstOperand
     * @see #setSecondOperand
     * @since 1.0
     */
    public Expression getSecondOperand() {
        return secondOperand;
    }

    /**
     * Default implementation of method isDifferentiable which assumes
     * that the BinaryOperator object is differentiable. All nondifferentiable
     * BinaryOperators must overrride this implementation.
     *
     * @see Expression#isDifferentiable
     */
    public boolean isDifferentiable() {
        return firstOperand.isDifferentiable() &&
        secondOperand.isDifferentiable();
    }

    /**
     * Default implementation of method isExpandable which assumes that
     * the BinaryOperator object is expandable. All nonexpandable
     * BinaryOperators must overrride this implementation.
     *
     * @see Expression#isExpandable
     */
    public boolean isExpandable() {
        return firstOperand.isExpandable() && secondOperand.isExpandable();
    }

    /**
     * Class BinaryOperator provides a default diff method for use by
     * nondifferentiable objects.  All differentiable objects must override
     * this diff method.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression diff(NamedDataExpression x) {
        return null;
    }

    /**
     * Dummy implementation for classes that are not expandable.
     *
     * @return DOCUMENT ME!
     */
    public String toTaylorMap() {
        return "";
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    public java.util.List getElements() {
        java.util.List v1 = getFirstOperand().getElements();
        java.util.List v2 = getSecondOperand().getElements();
        java.util.List v = new java.util.ArrayList();
        v.add(this);

        for (int i = 0; i < v1.size(); i++)
            v.add(v1.get(i));

        for (int j = 0; j < v2.size(); j++)
            v.add(v2.get(j));

        return v;
    }

    /**
     * DOCUMENT ME!
     */
    public void resetNumEval() {
        if (evalValid) {
            evalValid = false;
            ((NumericalDifferentiable) getFirstOperand()).resetNumEval();
            ((NumericalDifferentiable) getSecondOperand()).resetNumEval();
        } // if
    } // resetNumEval

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void resetNumDiff(int i) {
        if (valid[i]) {
            valid[i] = false;
            ((NumericalDifferentiable) getFirstOperand()).resetNumDiff(i);
            ((NumericalDifferentiable) getSecondOperand()).resetNumDiff(i);
        } // if
    } // resetNumDiff

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setNumberOfIndependents(int n) {
        if ((valid == null) || ((n + 1) != valid.length)) {
            valid = new boolean[n + 1];
            deriv = new double[n + 1];

            for (int i = 1; i <= n; i++) {
                valid[i] = false;
                deriv[i] = 0.0;
            } // for

            ((NumericalDifferentiable) getFirstOperand()).setNumberOfIndependents(n);
            ((NumericalDifferentiable) getSecondOperand()).setNumberOfIndependents(n);
        } // if
    } // setNumberOfIndependents

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void setIndex(int i) {
    } // setIndex
} // BinaryOperator
