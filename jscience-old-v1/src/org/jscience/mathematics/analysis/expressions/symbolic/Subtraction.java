/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.analysis.expressions.symbolic;

import org.jscience.JScience;
import org.jscience.mathematics.analysis.expressions.*;

/**
 * Class representing substraction of two Expression objects.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Expression e1 = ...;
 * Expression e2 = ...;
 * Expression e3 = new Subtraction( e1, e2 );
 * <PRE>
 *
 * @author Martin Egholm Nielsen
 * @author Carsten Knudsen
 * @version 1.0
 * @see Expression
 * @see BinaryOperator
 */
public class Subtraction extends BinaryOperator
        implements NumericalDifferentiable {
    /**
     * Creates an Subtraction object that represent the subtraction
     * between two Expression objects.
     *
     * @param leftOp  The lefthand side operand Expression object.
     * @param rightOp The righthand side operand Expression object.
     */
    public Subtraction(Expression leftOp, Expression rightOp) {
        super(leftOp, rightOp);
    } // Subtraction( , )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return getFirstOperand().eval() - getSecondOperand().eval();
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Expression diff(NamedDataExpression x) {
        return new Subtraction(getFirstOperand().diff(x),
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

        deriv[i] = ((NumericalDifferentiable) getFirstOperand()).numDiff(i) -
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

        evalValue = ((NumericalDifferentiable) getFirstOperand()).numEval() -
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

        return new Subtraction(op1, op2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Subtraction) {
            if (getFirstOperand().equals(((Subtraction) e).getFirstOperand()) &&
                    getSecondOperand().equals(((Subtraction) e).getSecondOperand())) {
                return true;
            }
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
            return "(" + getFirstOperand().toString() + "-" +
                    getSecondOperand().toString() + ")";
        } else {
            return "-";
        }
    } // toString()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "(" + getFirstOperand().toJava() + "-" +
                getSecondOperand().toJava() + ")";
    } // toJava()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toTaylorMap() {
        return "new TaylorSubtract(" + getFirstOperand().toTaylorMap() + "," +
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

        // const - const -> const
        if ((firstOptimized instanceof Constant) &&
                (secondOptimized instanceof Constant)) {
            return new Constant(firstOptimized.eval() - secondOptimized.eval());
        } // if

        // 0 - a -> -a
        if (firstOptimized instanceof Constant) {
            if (firstOptimized.eval() == 0) {
                return new Minus(secondOptimized);
            }
        } // if

        // a - 0 -> a
        if (secondOptimized instanceof Constant) {
            if (secondOptimized.eval() == 0) {
                return firstOptimized;
            }
        } // if

        // -a - -b -> b - a
        if ((firstOptimized instanceof Minus) &&
                (secondOptimized instanceof Minus)) {
            return new Subtraction(((Minus) secondOptimized).getFirstOperand(),
                    ((Minus) firstOptimized).getFirstOperand());
        }

        // a - -b -> a + b
        if ((secondOptimized instanceof Minus)) {
            return new Addition(firstOptimized,
                    ((Minus) secondOptimized).getFirstOperand());
        }

        // -a - b -> - ( a + b )
        if ((firstOptimized instanceof Minus)) {
            return new Minus(new Addition(((Minus) firstOptimized).getFirstOperand(), secondOptimized));
        }

        // log( a ) - log( b ) -> log( a / b )
        if ((firstOptimized instanceof Log) &&
                (secondOptimized instanceof Log)) {
            return new Log(new Division(((Log) firstOptimized).getFirstOperand(),
                    ((Log) secondOptimized).getFirstOperand()));
        }

        // a - a -> 0
        if (firstOptimized.equals(secondOptimized)) {
            return new Constant(0);
        }

        // no special optimization was performed
        return new Subtraction(firstOptimized, secondOptimized);
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
            Expression leftFactor = getFirstOperand().factor(e);
            Expression rightFactor = getSecondOperand().factor(e);

            if ((leftFactor != null) && (rightFactor != null)) {
                return new Division(f, new Subtraction(leftFactor, rightFactor));
            } else {
                return null;
            }
        } else if (inLeft) {
            return getFirstOperand().isolate(new Addition(f, getSecondOperand()),
                    e);
        } else if (inRight) {
            return getSecondOperand().isolate(new Subtraction(getFirstOperand(), f), e);
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
            return new Subtraction(leftFactor, rightFactor);
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
        return "<subtraction> " + getFirstOperand().toXML() + " " +
                getSecondOperand().toXML() + " </subtraction>";
    }
} // Subtraction
