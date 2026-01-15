package org.jscience.mathematics.analysis.expressions;

/**
 * Abstract class used as superclass for classes representing data. Here,
 * data could refer to constants, variables or parameters.
 *
 * @author Martin Egholm Nielsen
 * @version 1.0
 *
 * @see Constant
 * @see NamedDataExpression
 * @see Expression
 */
public abstract class DataExpression implements Expression {
    /**
     * The numerical value representing the DataExpression object.
     *
     * @since 1.0
     */
    protected double value;

/**
     * Creates a DataExpression object and initializes the value that it
     * represents.
     *
     * @param value The value the DataExpression object should hold.
     * @since 1.0
     */
    public DataExpression(double value) {
        setValue(value);
    } // constructor

    /**
     * Sets the value of the DataExpression object.
     *
     * @param x The value the DataExpression object should have.
     *
     * @see #eval
     * @since 1.0
     */
    public void setValue(double x) {
        value = x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return value;
    } // eval

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDifferentiable() {
        return true;
    } // isDifferentiable

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isExpandable() {
        return true;
    } // isExpandable

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.util.List getElements() {
        java.util.List v = new java.util.ArrayList();
        v.add(this);

        return v;
    } // getElements

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Expression e) {
        return this.equals(e);
    } // contains

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression isolate(Expression e) {
        return isolate(new Constant(0), e);
    } // isolate

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression isolate(Expression f, Expression e) {
        if (this.equals(e)) {
            return f;
        } else {
            return null;
        }
    } // isolate

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression factor(Expression e) {
        if (this.equals(e)) {
            return new Constant(1);
        } else {
            return null;
        }
    } // factor
} // DataExpression
