package org.jscience.mathematics.analysis.expressions.symbolic;

import org.jscience.JScience;
import org.jscience.mathematics.analysis.expressions.*;

/**
 * Class representing division of two Expression objects.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Expression e1 = ...;
 * Expression e2 = ...;
 * Expression e3 = new Division( e1, e2 );
 * <PRE>
 *
 * @author Martin Egholm Nielsen
 * @author Carsten Knudsen
 * @version 1.0
 * @see Expression
 * @see BinaryOperator
 */
public class Division extends BinaryOperator implements NumericalDifferentiable {
    /**
     * Creates a Division object that represent the division between
     * two Expression objects.
     *
     * @param nomOp   The nominator Expression object.
     * @param denomOp The denominator Expression object.
     */
    public Division(Expression nomOp, Expression denomOp) {
        super(nomOp, denomOp);
    } // Division( , )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return getFirstOperand().eval() / getSecondOperand().eval();
    } // eval

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Expression diff(NamedDataExpression x) {
        Expression e1 = new Multiplication(getFirstOperand().diff(x),
                getSecondOperand());
        Expression e2 = new Multiplication(getFirstOperand(),
                getSecondOperand().diff(x));
        Expression e3 = new Multiplication(getSecondOperand(),
                getSecondOperand());

        return new Division(new Subtraction(e1, e2), e3);
    } // diff

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double numDiff(int i) {
        if (valid[i]) {
            return deriv[i];
        }

        double f = getFirstOperand().eval();
        double g = getSecondOperand().eval();
        double fprime = ((NumericalDifferentiable) getFirstOperand()).numDiff(i);
        double gprime = ((NumericalDifferentiable) getSecondOperand()).numDiff(i);
        deriv[i] = ((fprime * g) - (f * gprime)) / (g * g);
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

        evalValue = ((NumericalDifferentiable) getFirstOperand()).numEval() / ((NumericalDifferentiable) getSecondOperand()).numEval();
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

        return new Division(op1, op2);
    } // replace

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Division) {
            if (getFirstOperand().equals(((Division) e).getFirstOperand()) &&
                    getSecondOperand().equals(((Division) e).getSecondOperand())) {
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
            return "(" + getFirstOperand().toString() + "/" +
                    getSecondOperand().toString() + ")";
        } else {
            return "/";
        }
    } // toString()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "(" + getFirstOperand().toJava() + "/" +
                getSecondOperand().toJava() + ")";
    } // toJava()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toTaylorMap() {
        return "new TaylorDivide(" + getFirstOperand().toTaylorMap() + "," +
                getSecondOperand().toTaylorMap() + ")";
    } // toTaylorMap()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        Expression firstOptimized = getFirstOperand().optimize();
        Expression secondOptimized = getSecondOperand().optimize();

        // const / const -> const
        if ((firstOptimized instanceof Constant) &&
                (secondOptimized instanceof Constant)) {
            return new Constant(firstOptimized.eval() / secondOptimized.eval());
        } // if

        // if first operand is constant
        if (firstOptimized instanceof Constant) {
            // 0 / a -> 0
            if (firstOptimized.eval() == 0) {
                return new Constant(0);
            }

            // 1 / a -> a^(-1)
            if (firstOptimized.eval() == 1) {
                return new Pow(secondOptimized, new Constant(-1));
            }

            // anyotherconst / a (leave)
            return new Division(firstOptimized, secondOptimized);
        } // if

        // if second operand is constant
        if ((secondOptimized instanceof Constant)) {
            // a / 1 -> a
            if (secondOptimized.eval() == 1) {
                return firstOptimized;
            }

            // a / -1 -> -a
            if (secondOptimized.eval() == -1) {
                return new Minus(firstOptimized);
            }

            // a / anyotherconst -> ( 1 / anyotherconst ) * a
            return new Multiplication(new Constant(1.0 / secondOptimized.eval()),
                    firstOptimized);
        } // if

        // -a / -b -> a / b
        if ((firstOptimized instanceof Minus) &&
                (secondOptimized instanceof Minus)) {
            return new Division(((Minus) firstOptimized).getFirstOperand(),
                    ((Minus) secondOptimized).getFirstOperand());
        }

        // -a / b -> - ( a / b )
        if (firstOptimized instanceof Minus) {
            return new Minus(new Division(((Minus) firstOptimized).getFirstOperand(), secondOptimized));
        }

        // a / -b -> - ( a / b )
        if (secondOptimized instanceof Minus) {
            return new Minus(new Division(firstOptimized,
                    ((Minus) secondOptimized).getFirstOperand()));
        }

        // (a*b)/(c*d)
        if ((firstOptimized instanceof Multiplication) &&
                (secondOptimized instanceof Multiplication)) {
            Multiplication A = (Multiplication) firstOptimized;
            Multiplication B = (Multiplication) secondOptimized;
            Expression a = A.getFirstOperand();
            Expression b = A.getSecondOperand();
            Expression c = B.getFirstOperand();
            Expression d = B.getSecondOperand();

            if (a instanceof Constant) {
                if (c instanceof Constant) {
                    return new Multiplication(new Constant(a.eval() / c.eval()),
                            new Division(b, d));
                } else if (d instanceof Constant) {
                    return new Multiplication(new Constant(a.eval() / d.eval()),
                            new Division(b, c));
                }
            } else if (b instanceof Constant) {
                if (c instanceof Constant) {
                    return new Multiplication(new Constant(b.eval() / c.eval()),
                            new Division(a, d));
                } else if (d instanceof Constant) {
                    return new Multiplication(new Constant(b.eval() / d.eval()),
                            new Division(a, c));
                }
            }
        }

        // a^b / a^c -> a^(b-c)
        if ((firstOptimized instanceof Pow) &&
                (secondOptimized instanceof Pow)) {
            if (((Pow) firstOptimized).getFirstOperand().equals(((Pow) secondOptimized).getFirstOperand())) {
                return new Pow(((Pow) firstOptimized).getFirstOperand(),
                        new Subtraction(((Pow) firstOptimized).getSecondOperand(),
                                ((Pow) secondOptimized).getSecondOperand()));
            }
        }

        // a / a -> 1
        if (firstOptimized.equals(secondOptimized)) {
            return new Constant(1);
        }

        // no special optimization performed
        return new Division(firstOptimized, secondOptimized);
    } // optimize()

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Expression isolate(Expression f, Expression e) {
        boolean inLeft = getFirstOperand().contains(e);
        boolean inRight = getSecondOperand().contains(e);

        if (!(inLeft || inRight)) {
            return null;
        } else if (inLeft && inRight) {
            return null;
        } else if (inLeft) {
            return getFirstOperand().isolate(new Multiplication(f,
                    getSecondOperand()), e);
        } else if (inRight) {
            return getSecondOperand().isolate(new Division(getFirstOperand(), f),
                    e);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Expression factor(Expression e) {
        Expression leftFactor = getFirstOperand().factor(e);
        Expression rightFactor = getSecondOperand().factor(e);

        if ((leftFactor != null) && (rightFactor != null)) {
            return null;
        } else if (leftFactor != null) {
            return new Division(leftFactor, getSecondOperand());
        } else if (rightFactor != null) {
            return null;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<division> " + getFirstOperand().toXML() + " " +
                getSecondOperand().toXML() + " </division>";
    }
} // Division
