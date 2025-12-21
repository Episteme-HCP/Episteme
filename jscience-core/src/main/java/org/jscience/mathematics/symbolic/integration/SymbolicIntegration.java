/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.mathematics.symbolic.integration;

import org.jscience.mathematics.symbolic.parsing.ExpressionParser.BinaryOp;
import org.jscience.mathematics.symbolic.parsing.ExpressionParser.Const;
import org.jscience.mathematics.symbolic.parsing.ExpressionParser.Expr;
import org.jscience.mathematics.symbolic.parsing.ExpressionParser.FuncCall;
import org.jscience.mathematics.symbolic.parsing.ExpressionParser.Negate;
import org.jscience.mathematics.symbolic.parsing.ExpressionParser.Var;

import java.util.Map;

/**
 * Symbolic Integration Engine.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SymbolicIntegration {

    /**
     * Computes the indefinite integral of an expression.
     * 
     * @param expr     The expression to integrate
     * @param variable The variable of integration
     * @return The integrated expression
     */
    public static Expr integrate(Expr expr, String variable) {
        if (isConstant(expr, variable)) {
            // int(c, x) = c*x
            return new BinaryOp('*', expr, new Var(variable));
        }

        if (expr instanceof Var v) {
            if (v.name.equals(variable)) {
                // int(x, x) = 0.5 * x^2
                return new BinaryOp('*', new Const(0.5), new BinaryOp('^', v, new Const(2)));
            } else {
                // Treat as constant (handled above)
                return new BinaryOp('*', expr, new Var(variable));
            }
        }

        if (expr instanceof BinaryOp op) {
            if (op.op == '+') {
                return new BinaryOp('+', integrate(op.left, variable), integrate(op.right, variable));
            }
            if (op.op == '-') {
                return new BinaryOp('-', integrate(op.left, variable), integrate(op.right, variable));
            }
            if (op.op == '*') {
                // Constant multiples
                if (isConstant(op.left, variable)) {
                    return new BinaryOp('*', op.left, integrate(op.right, variable));
                }
                if (isConstant(op.right, variable)) {
                    return new BinaryOp('*', op.right, integrate(op.left, variable));
                }

                // Polynomials: x * x = x^2 (Simplification usually handles this before
                // integration, but simple checks here)
            }
            if (op.op == '^') {
                // Power rule: int(u^n, x) if u=x and n is constant != -1
                if (op.left instanceof Var v && v.name.equals(variable) && isConstant(op.right, variable)) {
                    double exponent = op.right.eval(Map.of()); // Evaluate constant exponent
                    if (exponent == -1) {
                        return new FuncCall("ln", op.left);
                    }
                    return new BinaryOp('*',
                            new BinaryOp('/', new Const(1), new Const(exponent + 1)),
                            new BinaryOp('^', op.left, new Const(exponent + 1)));
                }
            }
        }

        if (expr instanceof FuncCall func) {
            if (func.arg instanceof Var v && v.name.equals(variable)) {
                switch (func.name) {
                    case "sin":
                        return new Negate(new FuncCall("cos", func.arg));
                    case "cos":
                        return new FuncCall("sin", func.arg);
                    case "exp":
                        return new FuncCall("exp", func.arg);
                }
            }
        }

        if (expr instanceof Negate n) {
            return new Negate(integrate(n.arg, variable));
        }

        // Return wrapping "int" function if unknown
        // Or throw exception? For now, throw exception to signal limitation
        throw new UnsupportedOperationException("Integration rule not found for: " + expr.toText());
    }

    private static boolean isConstant(Expr expr, String variable) {
        // Naive check: differentiate and see if it is physically 0 or recursively
        // constant.
        // Better: check if variable appears in the tree.
        return !containsVariable(expr, variable);
    }

    private static boolean containsVariable(Expr expr, String variable) {
        if (expr instanceof Var v) {
            return v.name.equals(variable);
        }
        if (expr instanceof BinaryOp op) {
            return containsVariable(op.left, variable) || containsVariable(op.right, variable);
        }
        if (expr instanceof FuncCall func) {
            return containsVariable(func.arg, variable);
        }
        if (expr instanceof Negate n) {
            return containsVariable(n.arg, variable);
        }
        return false; // Const
    }
}
