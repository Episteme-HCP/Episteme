package org.jscience.mathematics.analysis.expressions.symbolic;

import org.jscience.JScience;
import org.jscience.mathematics.analysis.expressions.*;

/**
 * Class representing addition of two Expression objects.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Expression e1 = ...;
 * Expression e2 = ...;
 * Expression e3 = new Addition( e1, e2 );
 * <PRE>
 *
 * @author Martin Egholm Nielsen
 * @author Carsten Knudsen
 * @version 1.0
 * @see Expression
 * @see BinaryOperator
 */
public class Addition extends BinaryOperator implements NumericalDifferentiable {
    /**
     * Creates an Addition object that represent the addition between
     * two Expression objects.
     *
     * @param leftOp  The left hand  side operand Expression object.
     * @param rightOp The righ thand side operand Expression object.
     */
    public Addition(Expression leftOp, Expression rightOp) {
        super(leftOp, rightOp);
    } // Addition( , )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return (getFirstOperand().eval() + getSecondOperand().eval());
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Expression diff(NamedDataExpression x) {
        return new Addition(getFirstOperand().diff(x),
                getSecondOperand().diff(x));
    } // diff()

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

        deriv[i] = ((NumericalDifferentiable) getFirstOperand()).numDiff(i) +
                ((NumericalDifferentiable) getSecondOperand()).numDiff(i);
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

        evalValue = ((NumericalDifferentiable) getFirstOperand()).numEval() +
                ((NumericalDifferentiable) getSecondOperand()).numEval();
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

        return new Addition(op1, op2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Addition) {
            if (getFirstOperand().equals(((Addition) e).getFirstOperand()) &&
                    getSecondOperand().equals(((Addition) e).getSecondOperand())) {
                return true;
            }

            if (getFirstOperand().equals(((Addition) e).getSecondOperand()) &&
                    getSecondOperand().equals(((Addition) e).getFirstOperand())) {
                return true;
            }

            if ((getSecondOperand() instanceof Addition) &&
                    (((Addition) e).getSecondOperand() instanceof Addition)) {
                // this = x+(y+z) and e = a+(b+c)
                Expression x = getFirstOperand();
                Expression y = ((Addition) getSecondOperand()).getFirstOperand();
                Expression z = ((Addition) getSecondOperand()).getSecondOperand();
                Expression a = ((Addition) e).getFirstOperand();
                Expression b = ((Addition) (((Addition) e).getSecondOperand())).getFirstOperand();
                Expression c = ((Addition) (((Addition) e).getSecondOperand())).getSecondOperand();

                if ((x.equals(b)) && (y.equals(a)) && (z.equals(c))) {
                    return true;
                }

                if ((x.equals(b)) && (y.equals(c)) && (z.equals(a))) {
                    return true;
                }

                if ((x.equals(c)) && (y.equals(a)) && (z.equals(b))) {
                    return true;
                }

                if ((x.equals(c)) && (y.equals(b)) && (z.equals(a))) {
                    return true;
                }
            } // if

            if ((getFirstOperand() instanceof Addition) &&
                    (((Addition) e).getFirstOperand() instanceof Addition)) {
                // this = (x+y)+z and e = (a+b)+c
                Expression x = ((Addition) getFirstOperand()).getFirstOperand();
                Expression y = ((Addition) getFirstOperand()).getSecondOperand();
                Expression z = getSecondOperand();
                Expression a = ((Addition) ((Addition) e).getFirstOperand()).getFirstOperand();
                Expression b = ((Addition) ((Addition) e).getFirstOperand()).getSecondOperand();
                Expression c = ((Addition) e).getSecondOperand();

                if ((z.equals(a)) && (x.equals(b)) && (y.equals(c))) {
                    return true;
                }

                if ((z.equals(a)) && (x.equals(c)) && (y.equals(b))) {
                    return true;
                }

                if ((z.equals(b)) && (x.equals(a)) && (y.equals(c))) {
                    return true;
                }

                if ((z.equals(b)) && (x.equals(c)) && (y.equals(a))) {
                    return true;
                }
            } // if
        } // if

        return false;
    } // equals()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        if (Boolean.valueOf(JScience.getProperty("recursivePrint"))
                .booleanValue()) {
            return "(" + getFirstOperand().toString() + "+" +
                    getSecondOperand().toString() + ")";
        } else {
            return "+";
        }
    } // toString()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "(" + getFirstOperand().toJava() + "+" +
                getSecondOperand().toJava() + ")";
    } // toJava()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toTaylorMap() {
        return "new TaylorAdd(" + getFirstOperand().toTaylorMap() + "," +
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

        // const + const -> const
        if ((firstOptimized instanceof Constant) &&
                (secondOptimized instanceof Constant)) {
            return new Constant(firstOptimized.eval() + secondOptimized.eval());
        } // if

        // 0 + anything -> anything
        if (firstOptimized instanceof Constant) {
            if (firstOptimized.eval() == 0) {
                return secondOptimized;
            }
        } // if

        // anything + 0 -> anything
        if (secondOptimized instanceof Constant) {
            if (secondOptimized.eval() == 0) {
                return firstOptimized;
            }
        } // if

        //  if first operand is constant
        if (firstOptimized instanceof Constant) {
            // a+(b+c) where a and either b or c are constant
            if (secondOptimized instanceof Addition) {
                Addition so = (Addition) secondOptimized;

                if (so.getFirstOperand() instanceof Constant) {
                    return new Addition(new Constant(firstOptimized.eval() +
                            so.getFirstOperand().eval()), so.getSecondOperand());
                } else if (so.getSecondOperand() instanceof Constant) {
                    return new Addition(new Constant(firstOptimized.eval() +
                            so.getSecondOperand().eval()), so.getFirstOperand());
                }
            }
        }

        // if second operand is constant
        if (secondOptimized instanceof Constant) {
            // (a+b)+c where c and either a or b are constant
            if (firstOptimized instanceof Addition) {
                Addition fo = (Addition) firstOptimized;

                if (fo.getFirstOperand() instanceof Constant) {
                    return new Addition(new Constant(secondOptimized.eval() +
                            fo.getFirstOperand().eval()), fo.getSecondOperand());
                } else if (fo.getSecondOperand() instanceof Constant) {
                    return new Addition(new Constant(secondOptimized.eval() +
                            fo.getSecondOperand().eval()), fo.getFirstOperand());
                }
            }
        } // if, one constant

        // -a + -b -> - ( a + b )
        if ((firstOptimized instanceof Minus) &&
                (secondOptimized instanceof Minus)) {
            return new Minus(new Addition(((Minus) firstOptimized).getFirstOperand(),
                    ((Minus) secondOptimized).getFirstOperand()));
        }

        // -a + b -> b - a
        if (firstOptimized instanceof Minus) {
            return new Subtraction(secondOptimized,
                    ((Minus) firstOptimized).getFirstOperand());
        }

        // a + -b -> a - b
        if (secondOptimized instanceof Minus) {
            return new Subtraction(firstOptimized,
                    ((Minus) secondOptimized).getFirstOperand());
        }

        // a + a -> 2 * a
        if (firstOptimized.equals(secondOptimized)) {
            return new Multiplication(new Constant(2), firstOptimized);
        }

        // log( a ) + log ( b ) -> log( a * b )
        if ((firstOptimized instanceof Log) &&
                (secondOptimized instanceof Log)) {
            return new Log(new Multiplication(((Log) firstOptimized).getFirstOperand(),
                    ((Log) secondOptimized).getFirstOperand()));
        }

        // a * b + a -> a * ( 1 + b )
        if (getFirstOperand() instanceof Multiplication) {
            Expression x = ((Multiplication) getFirstOperand()).getFirstOperand();
            Expression y = ((Multiplication) getFirstOperand()).getSecondOperand();

            if (getSecondOperand().equals(x)) {
                return new Multiplication(getSecondOperand(),
                        new Addition(y, new Constant(1)));
            }

            if (getSecondOperand().equals(y)) {
                return new Multiplication(getSecondOperand(),
                        new Addition(x, new Constant(1)));
            }
        }

        // a + a * b -> a * ( 1 + b )
        if (getSecondOperand() instanceof Multiplication) {
            Expression x = ((Multiplication) getSecondOperand()).getFirstOperand();
            Expression y = ((Multiplication) getSecondOperand()).getSecondOperand();

            if (getFirstOperand().equals(x)) {
                return new Multiplication(getFirstOperand(),
                        new Addition(y, new Constant(1)));
            }

            if (getFirstOperand().equals(y)) {
                return new Multiplication(getFirstOperand(),
                        new Addition(x, new Constant(1)));
            }
        }

        if ((getFirstOperand() instanceof Addition) &&
                (getSecondOperand() instanceof Addition)) {
            // (x+y)+(a+b)
            Expression x = ((Addition) getFirstOperand()).getFirstOperand();
            Expression y = ((Addition) getFirstOperand()).getSecondOperand();
            Expression a = ((Addition) getSecondOperand()).getFirstOperand();
            Expression b = ((Addition) getSecondOperand()).getSecondOperand();

            if (x.equals(a)) {
                if (y.equals(b)) {
                    return new Addition(new Multiplication(new Constant(2), x),
                            new Multiplication(new Constant(2), y));
                } else {
                    return new Addition(new Multiplication(new Constant(2), x),
                            new Addition(y, b));
                }
            }

            if (x.equals(b)) {
                if (y.equals(a)) {
                    return new Addition(new Multiplication(new Constant(2), x),
                            new Multiplication(new Constant(2), y));
                } else {
                    return new Addition(new Multiplication(new Constant(2), x),
                            new Addition(y, a));
                }
            }

            if (y.equals(a)) {
                return new Addition(new Multiplication(new Constant(2), y),
                        new Addition(x, b));
            }

            if (y.equals(b)) {
                return new Addition(new Multiplication(new Constant(2), y),
                        new Addition(x, a));
            }
        }

        // no special optimization was performed
        return new Addition(firstOptimized, secondOptimized);
    } // optimize()

    /**
     * Experimental clone support method.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

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
            Expression leftFactor = getFirstOperand().factor(e);
            Expression rightFactor = getSecondOperand().factor(e);

            if ((leftFactor != null) && (rightFactor != null)) {
                return new Division(f, new Addition(leftFactor, rightFactor));
            } else {
                return null;
            }
        } else if (inLeft) {
            return getFirstOperand().isolate(new Subtraction(f,
                    getSecondOperand()), e);
        } else if (inRight) {
            return getSecondOperand().isolate(new Subtraction(f,
                    getFirstOperand()), e);
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
            return new Addition(leftFactor, rightFactor);
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
        return "<addition> " + getFirstOperand().toXML() + " " +
                getSecondOperand().toXML() + " </addition>";
    }
} // Addition
