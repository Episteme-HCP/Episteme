package org.jscience.mathematics.analysis.expressions.symbolic;

import org.jscience.mathematics.analysis.expressions.Expression;
import org.jscience.mathematics.analysis.expressions.NamedDataExpression;
import org.jscience.mathematics.analysis.expressions.logical.Logical;


/**
 * DOCUMENT ME!
 *
 * @author Carsten Knudsen
 * @version 1.0
 */
public class IFTE implements Expression {
    /** DOCUMENT ME! */
    private Logical condition;

    /** DOCUMENT ME! */
    private Expression x;

    /** DOCUMENT ME! */
    private Expression y;

/**
     **/
    public IFTE(Logical condition, Expression x, Expression y) {
        this.condition = condition;
        this.x = x;
        this.y = y;
    } // constructor

    /**
     * Returns the value of the Expression object.
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        if (condition.truthValue()) {
            return x.eval();
        } else {
            return y.eval();
        }
    } // eval

    /**
     * Returns whether the object in question is differentiable. Note
     * that whether the object is differentiable or not, it is still required
     * to define the diff method.
     *
     * @see Expression#diff(NamedDataExpression)
     */
    public boolean isDifferentiable() {
        return false;
    } // isDifferentiable

    /**
     * Returns whether the object in question is expandable as a Taylor
     * expansion. Note that whether the object is expandable or not, it is
     * still required to define the diff method.
     *
     * @return DOCUMENT ME!
     */
    public boolean isExpandable() {
        return false;
    } // isExpandable

    /**
     * Returns the differential quotient for the Expression object.
     *
     * @param x The NamedDataExpression object representing the
     *        variable/parameter we wish to differentiate with respect to.
     *
     * @return A new Expression representing the differential quotient.
     *
     * @since 1.0
     */
    public Expression diff(NamedDataExpression x) {
        return null;
    } // diff

    /**
     * Returns a new Expression object that represents the optimized
     * expression.
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        Logical condition = this.condition.optimize();
        Expression x = this.x.optimize();
        Expression y = this.y.optimize();

        return new IFTE(condition, x, y);
    }

    /**
     * Replace all occurences of the Expression a in the current
     * expression with the Expression b.
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

        return new IFTE(condition.replace(a, b), x.replace(a, b),
            y.replace(a, b));
    } // replace

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof IFTE) {
            if (condition.equals(((IFTE) e).condition) &&
                    x.equals(((IFTE) e).x) && y.equals(((IFTE) e).y)) {
                return true;
            }
        } // if

        return false;
    } // equals

    /**
     * Returns String object representing Expression in Java source
     * code.
     *
     * @return String object representing Expression in Java source code.
     *
     * @since 1.0
     */
    public String toJava() {
        return "((" + condition.toJava() + ")?(" + x.toJava() + "):(" +
        y.toJava() + "))";
    } // toJava

    /**
     * Returns String object representing Expression.
     *
     * @return String object representing Expression.
     *
     * @since 1.0
     */
    public String toString() {
        return "ifte(" + condition.toString() + "," + x.toString() + "," +
        y.toString() + ")";
    } // toJava

    /**
     * Returns String object representing {@link
     * org.jscience.mathematics.analysis.expressions.Tmap}
     *
     * @return String object representing {@link
     *         org.jscience.mathematics.analysis.expressions.Tmap}.
     *
     * @since 1.0
     */
    public String toTaylorMap() {
        return null;
    } // toTaylorMap

    /**
     * Returns String XML representation
     *
     * @return String XML representation.
     *
     * @since 1.0
     */
    public String toXML() {
        return "<expression>" + System.getProperty("line.separator") +
        "<ifte>" + System.getProperty("line.separator") + "<logical> " +
        condition.toXML() + " </logical>" + x.toXML() + y.toXML() + "</ifte>" +
        System.getProperty("line.separator") + "</expression>" +
        System.getProperty("line.separator");
    } // toXML

    /**
     * The getElements method must return a java.util.List whose
     * elements are all the subexpressions of the Expression. For example the
     * expression "acos(ct)" should return a java.util.List with six
     * subexpressions: "[,a,cos,,c,t]" (the order of which is unspecified).
     *
     * @return DOCUMENT ME!
     */
    public java.util.List getElements() {
        java.util.List v1 = x.getElements();
        java.util.List v2 = y.getElements();
        java.util.List v = new java.util.ArrayList();
        v.add(this);

        for (int i = 0; i < v1.size(); i++)
            v.add(v1.get(i));

        for (int j = 0; j < v2.size(); j++)
            v.add(v2.get(j));

        return v;
    }

    /**
     * The method contains should return true if the Expression e is
     * found in the expression (this), else false should be returned.
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Expression e) {
        return x.contains(e) || y.contains(e);
    } // contains

    /**
     * The method should attempt to isolate the Expression e in the
     * equation this=0, and return the resulting Expression. If unable to
     * isolate the Expression null should be returned.
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression isolate(Expression e) {
        return null;
    } // isolate

    /**
     * The method should attempt to isolate the expression e in the
     * equation this=f, and return the resulting Expression. If unable to
     * isolate the Expression null should be returned.
     *
     * @param f DOCUMENT ME!
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression isolate(Expression f, Expression e) {
        return null;
    } // isolate

    /**
     * The method should attempt to return the factor of the Expression
     * e in the Expression this. If unable to locate a factor null should be
     * returned.
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression factor(Expression e) {
        return null;
    } // isolate
} // IFTE
