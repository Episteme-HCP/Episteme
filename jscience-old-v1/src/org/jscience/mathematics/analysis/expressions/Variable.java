package org.jscience.mathematics.analysis.expressions;

/**
 * Class representing a Variable object.
 *
 * @author Martin Egholm Nielsen
 * @author Carsten Knudsen
 * @version 1.0
 *
 * @see Expression
 * @see NamedDataExpression
 * @see Parameter
 * @see Constant
 */
public class Variable extends NamedDataExpression {
/**
     * Creates a Variable object.
     *
     * @param name The name the Variable object should be characterized by.
     * @param x    The value the Variable object should have if evaluated.
     * @since 1.0
     */
    public Variable(String name, double x) {
        super(name, x);
    } // constructor

/**
     * Creates a Variable object.
     *
     * @param name The name the Variable object should be characterized by.
     * @since 1.0
     */
    public Variable(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression replace(Expression a, Expression b) {
        if (this.equals(a)) {
            return b;
        } else {
            return this;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Variable) {
            if (e.toString().equals(this.toString())) {
                return true;
            }
        }

        return false;
    } // equals

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return new Variable(getName(), eval());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toTaylorMap() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<variable> " + name + " </variable>";
    }
} // Variable
