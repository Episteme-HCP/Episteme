package org.jscience.mathematics.analysis.expressions.symbolic;

import org.jscience.JScience;
import org.jscience.mathematics.analysis.expressions.*;

/**
 * Class representing multiplication of two Expression objects.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Expression e1 = ...;
 * Expression e2 = ...;
 * Expression e3 = new Multiplication( e1, e2 );
 * <PRE>
 *
 * @author Martin Egholm Nielsen
 * @author Carsten Knudsen
 * @version 1.0
 * @see Expression
 * @see BinaryOperator
 */
public class Multiplication extends BinaryOperator
        implements NumericalDifferentiable {
    /**
     * Creates a Multiplication object that represent the
     * multiplication between two Expression objects.
     *
     * @param leftOp  The lefthand side operand Expression object.
     * @param rightOp The righthand side operand Expression object.
     */
    public Multiplication(Expression leftOp, Expression rightOp) {
        super(leftOp, rightOp);
    } // Multiplication( , )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return (getFirstOperand().eval() * getSecondOperand().eval());
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

        return new Addition(e1, e2);
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

        double e1 = ((NumericalDifferentiable) getFirstOperand()).numDiff(i) * ((NumericalDifferentiable) getSecondOperand()).numEval();
        double e2 = ((NumericalDifferentiable) getSecondOperand()).numDiff(i) * ((NumericalDifferentiable) getFirstOperand()).numEval();
        deriv[i] = e1 + e2;
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

        evalValue = ((NumericalDifferentiable) getFirstOperand()).numEval() * ((NumericalDifferentiable) getSecondOperand()).numEval();
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

        return new Multiplication(op1, op2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Multiplication) {
            if (getFirstOperand().equals(((Multiplication) e).getFirstOperand()) &&
                    getSecondOperand().equals(((Multiplication) e).getSecondOperand())) {
                return true;
            }

            if (getFirstOperand().equals(((Multiplication) e).getSecondOperand()) &&
                    getSecondOperand().equals(((Multiplication) e).getFirstOperand())) {
                return true;
            }

            if ((getSecondOperand() instanceof Multiplication) &&
                    (((Multiplication) e).getSecondOperand() instanceof Multiplication)) {
                // this = x*(y*z) and e = a*(b*c)
                Expression x = getFirstOperand();
                Expression y = ((Multiplication) getSecondOperand()).getFirstOperand();
                Expression z = ((Multiplication) getSecondOperand()).getSecondOperand();
                Expression a = ((Multiplication) e).getFirstOperand();
                Expression b = ((Multiplication) (((Multiplication) e).getSecondOperand())).getFirstOperand();
                Expression c = ((Multiplication) (((Multiplication) e).getSecondOperand())).getSecondOperand();

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
            }

            if ((getFirstOperand() instanceof Multiplication) &&
                    (((Multiplication) e).getFirstOperand() instanceof Multiplication)) {
                // this = (x*y)*z and e = (a*b)*c
                Expression x = ((Multiplication) getFirstOperand()).getFirstOperand();
                Expression y = ((Multiplication) getFirstOperand()).getSecondOperand();
                Expression z = getSecondOperand();
                Expression a = ((Multiplication) ((Multiplication) e).getFirstOperand()).getFirstOperand();
                Expression b = ((Multiplication) ((Multiplication) e).getFirstOperand()).getSecondOperand();
                Expression c = ((Multiplication) e).getSecondOperand();

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
            return "(" + getFirstOperand().toString() + "*" +
                    getSecondOperand().toString() + ")";
        } else {
            return "*";
        }
    } // toString()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "(" + getFirstOperand().toJava() + "*" +
                getSecondOperand().toJava() + ")";
    } // toJava()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toTaylorMap() {
        return "new TaylorMultiply(" + getFirstOperand().toTaylorMap() + "," +
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

        // const * const -> const
        if ((firstOptimized instanceof Constant) &&
                (secondOptimized instanceof Constant)) {
            return new Constant(firstOptimized.eval() * secondOptimized.eval());
        }

        //  if first operand is constant
        if (firstOptimized instanceof Constant) {
            // 1 * a -> a
            if (firstOptimized.eval() == 1) {
                return secondOptimized;
            }

            // -1 * a -> -a
            if (firstOptimized.eval() == -1) {
                return new Minus(secondOptimized);
            }

            // 0 * a -> 0
            if (firstOptimized.eval() == 0) {
                return new Constant(0);
            }

            // a*(b*c) where a and either b or c are constant
            if (secondOptimized instanceof Multiplication) {
                Multiplication so = (Multiplication) secondOptimized;

                if (so.getFirstOperand() instanceof Constant) {
                    return new Multiplication(new Constant(firstOptimized.eval() * so.getFirstOperand().eval()),
                            so.getSecondOperand());
                } else if (so.getSecondOperand() instanceof Constant) {
                    return new Multiplication(new Constant(firstOptimized.eval() * so.getSecondOperand().eval()),
                            so.getFirstOperand());
                }
            }
        }

        // if second operand is constant
        if (secondOptimized instanceof Constant) {
            // a * 1 -> a
            if (secondOptimized.eval() == 1) {
                return firstOptimized;
            }

            // a * -1 -> -a
            if (secondOptimized.eval() == -1) {
                return new Minus(firstOptimized);
            }

            // a * 0 -> 0
            if (secondOptimized.eval() == 0) {
                return new Constant(0);
            }

            // (a*b)*c where c and either a or b are constant
            if (firstOptimized instanceof Multiplication) {
                Multiplication fo = (Multiplication) firstOptimized;

                if (fo.getFirstOperand() instanceof Constant) {
                    return new Multiplication(new Constant(secondOptimized.eval() * fo.getFirstOperand().eval()),
                            fo.getSecondOperand());
                } else if (fo.getSecondOperand() instanceof Constant) {
                    return new Multiplication(new Constant(secondOptimized.eval() * fo.getSecondOperand().eval()),
                            fo.getFirstOperand());
                }
            }
        } // if, one constant

        // (a*b)*(c*d)
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
                    return new Multiplication(new Constant(a.eval() * c.eval()),
                            new Multiplication(b, d));
                } else if (d instanceof Constant) {
                    return new Multiplication(new Constant(a.eval() * d.eval()),
                            new Multiplication(b, c));
                }
            } else if (b instanceof Constant) {
                if (c instanceof Constant) {
                    return new Multiplication(new Constant(b.eval() * c.eval()),
                            new Multiplication(a, d));
                } else if (d instanceof Constant) {
                    return new Multiplication(new Constant(b.eval() * d.eval()),
                            new Multiplication(a, c));
                }
            }
        }

        // (a*b)*pow(c,d)
        if ((firstOptimized instanceof Multiplication) &&
                (secondOptimized instanceof Pow)) {
            Expression a = ((Multiplication) firstOptimized).getFirstOperand();
            Expression b = ((Multiplication) firstOptimized).getSecondOperand();
            Pow p = (Pow) secondOptimized;
            Expression c = p.getFirstOperand();
            Expression d = p.getSecondOperand();

            // (a*b)*pow(a,d) -> b*pow(a,d+1)
            if (a.equals(c)) {
                return new Multiplication(b,
                        new Pow(a, new Addition(d, new Constant(1))));
            }
            // (a*b)*pow(b,d) -> a*pow(b,d+1)
            else if (b.equals(c)) {
                return new Multiplication(a,
                        new Pow(b, new Addition(d, new Constant(1))));
            }
        }

        // pow(c,d)*(a*b)
        if ((firstOptimized instanceof Pow) &&
                (secondOptimized instanceof Multiplication)) {
            Expression a = ((Multiplication) secondOptimized).getFirstOperand();
            Expression b = ((Multiplication) secondOptimized).getSecondOperand();
            Pow p = (Pow) firstOptimized;
            Expression c = p.getFirstOperand();
            Expression d = p.getSecondOperand();

            // (a*b)*pow(a,d) -> b*pow(a,d+1)
            if (a.equals(c)) {
                return new Multiplication(b,
                        new Pow(a, new Addition(d, new Constant(1))));
            }
            // (a*b)*pow(b,d) -> a*pow(b,d+1)
            else if (b.equals(c)) {
                return new Multiplication(a,
                        new Pow(b, new Addition(d, new Constant(1))));
            }
        }

        // (a/b)*pow(c,d)
        if ((firstOptimized instanceof Division) &&
                (secondOptimized instanceof Pow)) {
            // (pow(x,y)/b)*pow(c,d)
            if (((Division) firstOptimized).getFirstOperand() instanceof Pow) {
                Pow p = (Pow) ((Division) firstOptimized).getFirstOperand();
                Expression b = ((Division) firstOptimized).getSecondOperand();
                Expression x = p.getFirstOperand();
                Expression y = p.getSecondOperand();
                Expression c = ((Pow) secondOptimized).getFirstOperand();
                Expression d = ((Pow) secondOptimized).getSecondOperand();

                // (pow(x,y)/b)*pow(x,d)
                if (x.equals(c)) {
                    return new Division(new Pow(x, new Addition(y, d)), b);
                }
            }
            // (a/pow(x,y))*pow(c,d)
            else if (((Division) firstOptimized).getSecondOperand() instanceof Pow) {
                Pow p = (Pow) ((Division) firstOptimized).getSecondOperand();
                Expression b = ((Division) firstOptimized).getFirstOperand();
                Expression x = p.getFirstOperand();
                Expression y = p.getSecondOperand();
                Expression c = ((Pow) secondOptimized).getFirstOperand();
                Expression d = ((Pow) secondOptimized).getSecondOperand();

                // (a/pow(x,y))*pow(x,d)
                if (x.equals(c)) {
                    return new Multiplication(new Pow(x, new Subtraction(d, y)),
                            b);
                }
            }
            // (a/b)*pow(c,d)
            else {
                Expression a = ((Division) firstOptimized).getFirstOperand();
                Expression b = ((Division) firstOptimized).getSecondOperand();
                Pow p = (Pow) secondOptimized;
                Expression c = p.getFirstOperand();
                Expression d = p.getSecondOperand();

                // (a/b)*pow(a,d) -> pow(a,d+1)/b
                if (a.equals(c)) {
                    return new Division(new Pow(a,
                            new Addition(d, new Constant(1))), b);
                }
                // (a/b)*pow(b,d) -> a*pow(b,d-1)
                else if (b.equals(c)) {
                    return new Multiplication(a,
                            new Pow(b, new Subtraction(d, new Constant(1))));
                }
            }
        }

        // pow(a,b)*pow(c,d)
        if ((firstOptimized instanceof Pow) &&
                (secondOptimized instanceof Pow)) {
            Pow A = (Pow) firstOptimized;
            Expression a = A.getFirstOperand();
            Expression b = A.getSecondOperand();
            Pow B = (Pow) secondOptimized;
            Expression c = B.getFirstOperand();
            Expression d = B.getSecondOperand();

            // pow(a,b)*pow(a,d) -> b*pow(a,b+d)
            if (a.equals(c)) {
                return new Pow(a, new Addition(b, d));
            }
        }

        // -a * -b -> a * b
        if ((firstOptimized instanceof Minus) &&
                (secondOptimized instanceof Minus)) {
            return new Multiplication(((Minus) firstOptimized).getFirstOperand(),
                    ((Minus) secondOptimized).getFirstOperand());
        }

        // -a * b -> - ( a * b )
        if ((firstOptimized instanceof Minus)) {
            return new Minus(new Multiplication(((Minus) firstOptimized).getFirstOperand(), secondOptimized));
        }

        // a * -b -> - ( a * b )
        if ((secondOptimized instanceof Minus)) {
            return new Minus(new Multiplication(firstOptimized,
                    ((Minus) secondOptimized).getFirstOperand()));
        }

        // a^b * a^c -> a^( b + c )
        if ((firstOptimized instanceof Pow) &&
                (secondOptimized instanceof Pow)) {
            if (((Pow) firstOptimized).getFirstOperand().equals(((Pow) secondOptimized).getFirstOperand())) {
                return new Pow(((Pow) firstOptimized).getFirstOperand(),
                        new Addition(((Pow) firstOptimized).getSecondOperand(),
                                ((Pow) secondOptimized).getSecondOperand()));
            }
        }

        // exp( a ) * exp( b ) -> exp( a + b )
        if ((firstOptimized instanceof Exp) &&
                (secondOptimized instanceof Exp)) {
            return new Exp(new Addition(((Exp) firstOptimized).getFirstOperand(),
                    ((Exp) secondOptimized).getFirstOperand()));
        }

        // sqrt( a ) * sqrt( b ) -> sqrt( a * b )
        if ((firstOptimized instanceof Sqrt) &&
                (secondOptimized instanceof Sqrt)) {
            return new Pow(new Multiplication(((Sqrt) firstOptimized).getFirstOperand(),
                    ((Sqrt) secondOptimized).getFirstOperand()),
                    new Constant(-0.5));
        }

        // a * a -> a^2
        if (firstOptimized.equals(secondOptimized)) {
            return new Pow(firstOptimized, new Constant(2));
        }

        // a^b * a -> a^( b + 1 )
        if ((firstOptimized instanceof Pow)) {
            if (((Pow) firstOptimized).getFirstOperand().equals(secondOptimized)) {
                return new Pow(secondOptimized,
                        new Addition(((Pow) firstOptimized).getSecondOperand(),
                                new Constant(1)));
            }
        }

        // a * a^b -> a^( b + 1 )
        if ((secondOptimized instanceof Pow)) {
            if (((Pow) secondOptimized).getFirstOperand().equals(firstOptimized)) {
                return new Pow(firstOptimized,
                        new Addition(((Pow) secondOptimized).getSecondOperand(),
                                new Constant(1)));
            }
        }

        // no special optimization was performed
        return new Multiplication(firstOptimized, secondOptimized);
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
            return getFirstOperand().isolate(new Division(f, getSecondOperand()),
                    e);
        } else if (inRight) {
            return getSecondOperand().isolate(new Division(f, getFirstOperand()),
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
            return new Multiplication(leftFactor, getSecondOperand());
        } else if (rightFactor != null) {
            return new Multiplication(getFirstOperand(), rightFactor);
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
        return "<multiplication> " + getFirstOperand().toXML() + " " +
                getSecondOperand().toXML() + " </multiplication>";
    }
} // Multiplication
