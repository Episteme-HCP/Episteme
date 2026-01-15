package org.jscience.mathematics.analysis.expressions;

/**
 * Class representing the clip function.
 *
 * @author Carsten Knudsen
 * @version 1.0
 *
 * @see UnaryOperator
 */
public class Clip extends QuartaryOperator {
/**
     * Creates an object that represents the clip function. If the first
     * expression in the argument list evaluates to a value larger than or
     * equal to the second expression, the third expression is returned, else
     * the fourth expression is returned.
     *
     * @param expression1 Any expression
     * @param expression2 Any expression
     * @param first       The value to which the clip function clips if expression1
     *                    is larger that expression2
     * @param second      The value to which the clip function clips if expression1
     *                    is not larger that expression2
     */
    public Clip(Expression expression1, Expression expression2,
        Expression first, Expression second) {
        super(expression1, expression2, first, second);
    } // Clip( )

/**
     * Creates an object that represents the clip function. If the expression
     * is positive first is returned else second is returned.
     *
     * @param expression Any expression
     * @param first      The value to which the clip function clips if the
     *                   expression is positive
     * @param second     The value to which the clip function clips if the
     *                   expression is not positive
     */
    public Clip(Expression expression, Expression first, Expression second) {
        this(expression, new Constant(0), first, second);
    } // Clip( )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return (getFirstOperand().eval() >= getSecondOperand().eval())
        ? (getThirdOperand().eval()) : (getFourthOperand().eval());
    } // eval

    /**
     * The clip function is not differentiable so we override the
     * inherited isDifferentiable method.
     *
     * @return DOCUMENT ME!
     */
    public boolean isDifferentiable() {
        return false;
    }

    /**
     * The clip function is not expandable so we override the inherited
     * isExpandable method.
     *
     * @return DOCUMENT ME!
     */
    public boolean isExpandable() {
        return false;
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

        Expression op1 = getFirstOperand().replace(a, b);
        Expression op2 = getSecondOperand().replace(a, b);
        Expression op3 = getThirdOperand().replace(a, b);
        Expression op4 = getFourthOperand().replace(a, b);

        return new Clip(op1, op2, op3, op4);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Clip) {
            if (getFirstOperand().equals(((Clip) e).getFirstOperand()) &&
                    getSecondOperand().equals(((Clip) e).getSecondOperand()) &&
                    getThirdOperand().equals(((Clip) e).getThirdOperand()) &&
                    getFourthOperand().equals(((Clip) e).getFourthOperand())) {
                return true;
            }
        } // if

        return false;
    } // equals

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "clip(" + getFirstOperand().toString() + "," +
        getSecondOperand().toString() + "," + getThirdOperand() + "," +
        getFourthOperand() + ")";
    } // toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "((" + getFirstOperand().toJava() + ">=" +
        getSecondOperand().toJava() + ")?(" + getThirdOperand().toJava() +
        "):(" + getFourthOperand().toJava() + "))";
    } // toJava

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        Expression firstOptimized = getFirstOperand().optimize();
        Expression secondOptimized = getSecondOperand().optimize();
        Expression thirdOptimized = getThirdOperand().optimize();
        Expression fourthOptimized = getFourthOperand().optimize();

        return new Clip(firstOptimized, secondOptimized, thirdOptimized,
            fourthOptimized);
    } // optimize

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<expression>" + System.getProperty("line.separator") +
        "<clip>" + System.getProperty("line.separator") +
        getFirstOperand().toXML() + getSecondOperand().toXML() +
        getThirdOperand().toXML() + getFourthOperand().toXML() + "</clip>" +
        System.getProperty("line.separator") + "</expression>" +
        System.getProperty("line.separator");
    }
} // Clip
