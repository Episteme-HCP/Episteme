package org.jscience.mathematics.analysis.expressions.symbolic;

import org.jscience.JScience;
import org.jscience.mathematics.analysis.expressions.*;

/**
 * Class representing one Expression object raised to the power of
 * another Expression object.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Expression e1 = ...;
 * Expression e2 = ...;
 * Expression e3 = new Pow( e1, e2 );
 * <PRE>
 *
 * @author Martin Egholm Nielsen
 * @author Carsten Knudsen
 * @version 1.0
 * @see Expression
 * @see BinaryOperator
 */
public class Pow extends BinaryOperator implements NumericalDifferentiable {
    /**
     * Creates a Power object that represent one Expression object
     * raised to the power of another.
     *
     * @param baseOp  The base Expression object.
     * @param powerOp The power Expression object.
     */
    public Pow(Expression baseOp, Expression powerOp) {
        super(baseOp, powerOp);
    } // Pow( , )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return Math.pow(getFirstOperand().eval(), getSecondOperand().eval());
    } // eval

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Expression diff(NamedDataExpression x) {
        // y = f^g
        // y'=f^g(log(f)*g'+g*f'/f)
        Expression e1 = new Division(getSecondOperand(), getFirstOperand());
        Expression e2 = new Multiplication(getFirstOperand().diff(x), e1);

        Expression e3 = new Log(getFirstOperand());
        Expression e4 = new Multiplication(getSecondOperand().diff(x), e3);

        Expression e5 = new Addition(e2, e4);

        Expression e6 = new Pow(getFirstOperand(), getSecondOperand());

        return new Multiplication(e5, e6);
    } // diff

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double numDiff(int i) {
        // y = f^g
        // y'=f^g(log(f)*g'+g*f'/f)
        if (valid[i]) {
            return deriv[i];
        }

        NumericalDifferentiable fnd = (NumericalDifferentiable) getFirstOperand();
        NumericalDifferentiable gnd = (NumericalDifferentiable) getSecondOperand();
        double f = fnd.numEval();
        double g = gnd.numEval();
        double fp = fnd.numDiff(i);
        double gp = gnd.numDiff(i);

        if (gp != 0.0) {
            deriv[i] = numEval() * ((Math.log(f) * gp) + ((g * fp) / f));
        } else {
            deriv[i] = (numEval() * g * fp) / f;
        }

        valid[i] = true;

        return deriv[i];
    } // numDiff

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double numEval() {
        if (evalValid) {
            return evalValue;
        }

        evalValue = Math.pow(((NumericalDifferentiable) getFirstOperand()).numEval(),
                ((NumericalDifferentiable) getSecondOperand()).numEval());
        evalValid = true;

        return evalValue;
    } // numEval

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Expression replace(Expression a, Expression b) {
        if (this.equals(a)) {
            return b;
        }

        Expression op1 = getFirstOperand().replace(a, b);
        Expression op2 = getSecondOperand().replace(a, b);

        return new Pow(op1, op2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Pow) {
            if (getFirstOperand().equals(((Pow) e).getFirstOperand()) &&
                    getSecondOperand().equals(((Pow) e).getSecondOperand())) {
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
        if (Boolean.valueOf(JScience.getProperty("recursivePrint"))
                .booleanValue()) {
            return "(" + getFirstOperand().toString() + "^" +
                    getSecondOperand().toString() + ")";
        } else {
            return "pow";
        }
    } // toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "Math.pow(" + getFirstOperand().toJava() + "," +
                getSecondOperand().toJava() + ")";
    } // toJava

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toTaylorMap() {
        /*
          return "new TaylorPow(" + getFirstOperand().toTaylorMap() + "," +
          getSecondOperand().toTaylorMap() + ")";
        */
        return "new TaylorExp(new TaylorMultiply(" + getSecondOperand().toTaylorMap() + "," +
                "new TaylorLog(" + getFirstOperand().toTaylorMap() + ")))";
    } // toTaylorMap

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        Expression firstOptimized = getFirstOperand().optimize();
        Expression secondOptimized = getSecondOperand().optimize();

        // const ^ const -> const
        if ((firstOptimized instanceof Constant) &&
                (secondOptimized instanceof Constant)) {
            return new Constant(Math.pow(firstOptimized.eval(),
                    secondOptimized.eval()));
        } // if

        if (firstOptimized instanceof Constant) {
            if (firstOptimized.eval() == 1) {
                return new Constant(1);
            }

            if (firstOptimized.eval() == 0) {
                return new Constant(0);
            }
        } // if, base is constant

        if (secondOptimized instanceof Constant) {
            if (secondOptimized.eval() == 1) {
                return firstOptimized;
            }

            if (secondOptimized.eval() == 0) {
                return new Constant(1);
            }
        } // if, power is constant

        // no special optimization was performed
        return new Pow(firstOptimized, secondOptimized);
    } // optimize()

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Expression isolate(Expression f, Expression e) {
        if (!contains(e)) {
            return null;
        } else if (getFirstOperand().contains(e)) {
            return getFirstOperand().isolate(new Exp(new Division(new Log(f), getSecondOperand())), e);
        } // else if
        else if (getSecondOperand().contains(e)) {
            return getSecondOperand().isolate(new Division(new Log(f),
                    new Log(getFirstOperand())), e);
        } // else if
        else {
            return null;
        }
    } // isolate

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<pow> " + getFirstOperand().toXML() + " " +
                getSecondOperand().toXML() + " </pow>";
    } // toXML
} // Pow
