package org.jscience.mathematics.analysis.expressions;

/**
 * Class representing a Parameter object.
 *
 * @author Martin Egholm Nielsen
 * @author Carsten Knudsen
 * @version 1.0
 *
 * @see Expression
 * @see NamedDataExpression
 * @see Constant
 * @see Variable
 */
public class Parameter extends NamedDataExpression {
/**
     * Creates a Parameter object.
     *
     * @param name The name the Parameter object should be characterized by.
     * @param x    The value the Parameter object should have if evaluated.
     * @since 1.0
     */
    public Parameter(String name, double x) {
        super(name, x);
    } // constructor

/**
     * Creates a Parameter object.
     *
     * @param name The name the Parameter object should be characterized by.
     * @since 1.0
     */
    public Parameter(String name) {
        super(name);
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
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Parameter) {
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
        return new Parameter(getName(), eval());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toTaylorMap() {
        return "Tpar_" + name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<parameter>" + name + "</parameter>";
    }
} // Parameter
