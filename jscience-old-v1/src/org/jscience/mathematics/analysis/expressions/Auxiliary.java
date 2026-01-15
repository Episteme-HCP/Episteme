package org.jscience.mathematics.analysis.expressions;

import org.jscience.JScience;

import org.jscience.util.Named;


/**
 * Class representing an Auxiliary object. An Auxiliary wraps an Expression
 * and gives it a symbolic name. The optimize method returns a new Auxiliary
 * with the same name but with an optimized version of the underlying
 * Expression.
 *
 * @author Carsten Knudsen
 * @version 1.0
 *
 * @see Expression
 */
public class Auxiliary implements Expression, Named {
    /**
     * The boolean deepDefinition defines how the String returning
     * methods of Auxiliary works. If false only the name is returned.
     */
    public static boolean deepDefinition = true;

    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Expression expr;

/**
     * Creates an Auxiliary object.
     *
     * @param name The name the Auxiliary object should be characterized by.
     * @param expr The Expression the Auxiliary represents.
     */
    public Auxiliary(String name, Expression expr) {
        this.name = name;
        this.expr = expr;
    }

    /**
     * Returns the Expression wrapped by the Auxiliary.
     *
     * @return DOCUMENT ME!
     */
    public Expression getExpression() {
        return expr;
    } // getExpression

    /**
     * Returns the name of the Auxiliary.
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    } // getName

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return getExpression().eval();
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression diff(NamedDataExpression x) {
        return getExpression().diff(x);
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
        }

        Expression op = getExpression().replace(a, b);

        return new Auxiliary(getName(), op);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Auxiliary) {
            return getExpression().equals(((Auxiliary) e).getExpression());
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDifferentiable() {
        return getExpression().isDifferentiable();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isExpandable() {
        return getExpression().isExpandable();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        return new Auxiliary(getName(), getExpression().optimize());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return new Auxiliary(name, expr);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return name;
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
    public String toString() {
        if (deepDefinition) {
            return expr.toString();
        } // if
        else {
            if (!Boolean.valueOf(JScience.getProperty("recursivePrint"))
                            .booleanValue()) {
                return name;
            } else {
                return name;
            }
        } // else
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.util.List getElements() {
        java.util.List v = getExpression().getElements();
        v.add(this);

        return v;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Expression e) {
        java.util.List v = getElements();

        for (int i = 0; i < v.size(); i++)
            if (this.equals(v.get(i))) {
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
        return getExpression().isolate(f, e);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression factor(Expression e) {
        return getExpression().factor(e);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<expression>" + System.getProperty("line.separator") +
        "<auxiliary>" + System.getProperty("line.separator") +
        getExpression().toXML() + System.getProperty("line.separator") +
        "</auxiliary>" + System.getProperty("line.separator") +
        "</expression>" + System.getProperty("line.separator");
    }
} // Auxiliary
