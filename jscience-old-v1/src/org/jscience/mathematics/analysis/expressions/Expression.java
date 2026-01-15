package org.jscience.mathematics.analysis.expressions;

import org.jscience.util.JavaExpressible;
import org.jscience.util.XMLExpressible;

import java.io.Serializable;


/**
 * Interface for creating symbolic expressions, which may be evaluated.
 * Expressions may be differentiable in which case we can obtain their
 * derivatives in symbolic for such as in: <CODE>Expression derivative =
 * expression.diff( new Variable( "x", 0 ) );</CODE> It is possible to
 * generate Java source code for computing an expression. In addition
 * expressions may be expandable meaning that one can generate a Taylor
 * expansion of the solution of an ordinary differential equation like
 * <CODE>x'=f</CODE> where <CODE>f</CODE> is an expandable function. The
 * latter feature can, of course, only be used if the functions used have
 * Taylor expansions. <BR><B>Example of use:</B><BR>
 * <PRE>
 * Constant c = new Constant( 1 ); // constant with value 1
 * Variable x = new Variable( "x", 3 ); // variable with name x and value 3
 * Variable y = new Variable( "y", 7 ); // variable with name y and value 7
 * Expression e1 = new Multiplication( x, new Addition( y, c ) ); // e1 = (x*(y+1))
 * double value  = e1.eval(); // value = 24
 * Expression e2 = e1.diff( x ); // e2 = ((1.0*(y+1.0))+(x*(0.0+0.0)))
 * Expression e3 = e2.optimize(); // e3 = (y+1.0)
 * Expression e4 = new Addition( e3, new Multiplication( x, new Sin( y ) ) ); // e4 = ((y+1.0)+(x*sin(y)))
 * String javacode = e4.toJava(); // javacode = "((y+1.0)+(x*Math.sin(y)))"
 * String tmapcode = e4.toTaylorMap(); // tmapcode = "new TaylorAdd(new TaylorAdd(y, new TaylorConstant( 1.0 )),new TaylorMultiply(x,new TaylorSin(y)))"
 * </PRE>
 *
 * @author Martin Egholm Nielsen
 * @author Carsten Knudsen
 * @version 1.0
 * @see org.jscience.mathematics.analysis.expressions.Constant
 * @see org.jscience.mathematics.analysis.expressions.Variable
 * @see org.jscience.mathematics.analysis.expressions.symbolic.Addition
 * @see org.jscience.mathematics.analysis.expressions.symbolic.Multiplication
 * @see org.jscience.mathematics.analysis.expressions.symbolic.Sin
 */
public interface Expression extends Serializable, JavaExpressible, XMLExpressible {
    /**
     * Returns the value of the Expression object.
     *
     * @return DOCUMENT ME!
     *
     * @since 1.0
     */
    public double eval();

    /**
     * Returns whether the object in question is differentiable. Note
     * that whether the object is differentiable or not, it is still required
     * to define the diff method.
     *
     * @see Expression#diff(NamedDataExpression)
     */
    public boolean isDifferentiable();

    /**
     * Returns whether the object in question is expandable as a Taylor
     * expansion. Note that whether the object is expandable or not, it is
     * still required to define the diff method.
     *
     * @return DOCUMENT ME!
     */
    public boolean isExpandable();

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
    public Expression diff(NamedDataExpression x);

    /**
     * Returns a new Expression object that represents the optimized
     * expression.
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize();

    /**
     * Replace all occurences of the Expression a in the current
     * expression with the Expression b.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression replace(Expression a, Expression b);

    /**
     * Returns String object representing Expression in Java source
     * code.
     *
     * @return String object representing Expression in Java source code.
     *
     * @since 1.0
     */
    public String toJava();

    /**
     * Returns String object representing {@link
     * org.jscience.mathematics.analysis.taylor.TaylorMap}
     *
     * @return String object representing {@link
     *         org.jscience.mathematics.analysis.taylor.TaylorMap}.
     *
     * @since 1.0
     */
    public String toTaylorMap();

    /**
     * Returns String XML representation
     *
     * @return String XML representation.
     *
     * @since 1.0
     */
    public String toXML();

    /**
     * The getElements method must return a java.util.List whose
     * elements are all the subexpressions of the Expression. For example the
     * expression "acos(ct)" should return a java.util.List with six
     * subexpressions: "[,a,cos,,c,t]" (the order of which is unspecified).
     *
     * @return DOCUMENT ME!
     */
    public java.util.List getElements();

    /**
     * The method contains should return true if the Expression e is
     * found in the expression (this), else false should be returned.
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Expression e);

    /**
     * The method should attempt to isolate the Expression e in the
     * equation this=0, and return the resulting Expression. If unable to
     * isolate the Expression null should be returned.
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression isolate(Expression e);

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
    public Expression isolate(Expression f, Expression e);

    /**
     * The method should attempt to return the factor of the Expression
     * e in the Expression this. If unable to locate a factor null should be
     * returned.
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression factor(Expression e);
} // Expression
