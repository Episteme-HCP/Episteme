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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.symbolic;

import java.util.Map;
import org.jscience.mathematics.structures.rings.Ring;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a transcendental function as a symbolic expression.
 * <p>
 * Supports sin, cos, tan, exp, log, sqrt, and power functions.
 * Differentiation is not yet fully implemented (requires division operation).
 * </p>
 * 
 * @param <T> the type of number used in evaluation
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class FunctionExpression<T extends Ring<T>> implements Expression<T> {

    /**
     * Enumeration of supported function types.
     */
    public enum FunctionType {
        SIN, COS, TAN, EXP, LOG, SQRT, POW
    }

    private final FunctionType type;
    private final Expression<T> argument;
    private final Expression<T> exponent; // For POW only

    /**
     * Private constructor.
     */
    private FunctionExpression(FunctionType type, Expression<T> argument, Expression<T> exponent) {
        this.type = type;
        this.argument = argument;
        this.exponent = exponent;
    }

    /**
     * Creates a sine function expression.
     */
    public static <T extends Ring<T>> FunctionExpression<T> sin(Expression<T> arg) {
        return new FunctionExpression<>(FunctionType.SIN, arg, null);
    }

    /**
     * Creates a cosine function expression.
     */
    public static <T extends Ring<T>> FunctionExpression<T> cos(Expression<T> arg) {
        return new FunctionExpression<>(FunctionType.COS, arg, null);
    }

    /**
     * Creates a tangent function expression.
     */
    public static <T extends Ring<T>> FunctionExpression<T> tan(Expression<T> arg) {
        return new FunctionExpression<>(FunctionType.TAN, arg, null);
    }

    /**
     * Creates an exponential function expression.
     */
    public static <T extends Ring<T>> FunctionExpression<T> exp(Expression<T> arg) {
        return new FunctionExpression<>(FunctionType.EXP, arg, null);
    }

    /**
     * Creates a natural logarithm function expression.
     */
    public static <T extends Ring<T>> FunctionExpression<T> log(Expression<T> arg) {
        return new FunctionExpression<>(FunctionType.LOG, arg, null);
    }

    /**
     * Creates a square root function expression.
     */
    public static <T extends Ring<T>> FunctionExpression<T> sqrt(Expression<T> arg) {
        return new FunctionExpression<>(FunctionType.SQRT, arg, null);
    }

    /**
     * Creates a power function expression.
     */
    public static <T extends Ring<T>> FunctionExpression<T> pow(Expression<T> base, Expression<T> exp) {
        return new FunctionExpression<>(FunctionType.POW, base, exp);
    }

    @Override
    public Expression<T> add(Expression<T> other) {
        throw new UnsupportedOperationException("Addition requires Ring parameter - use SumExpression directly");
    }

    @Override
    public Expression<T> multiply(Expression<T> other) {
        throw new UnsupportedOperationException(
                "Multiplication requires Ring parameter - use ProductExpression directly");
    }

    @Override
    public Expression<T> differentiate(Variable<T> variable) {
        // Differentiation requires division and multiplication operations
        // which need Ring parameter - not yet fully integrated
        throw new UnsupportedOperationException("Differentiation not yet fully integrated with Ring interface");
    }

    @Override
    public Expression<T> integrate(Variable<T> variable) {
        throw new UnsupportedOperationException("Integration not yet implemented");
    }

    @Override
    public Expression<T> compose(Variable<T> variable, Expression<T> substitution) {
        Expression<T> newArgument = argument.compose(variable, substitution);
        if (type == FunctionType.POW) {
            Expression<T> newExponent = exponent.compose(variable, substitution);
            return pow(newArgument, newExponent);
        }
        return new FunctionExpression<>(type, newArgument, exponent);
    }

    @Override
    public Expression<T> simplify() {
        Expression<T> simpleArg = argument.simplify();
        if (type == FunctionType.POW) {
            Expression<T> simpleExp = exponent.simplify();
            return pow(simpleArg, simpleExp);
        }
        return new FunctionExpression<>(type, simpleArg, exponent);
    }

    @Override
    public String toLatex() {
        if (type == FunctionType.POW) {
            return argument.toLatex() + "^{" + exponent.toLatex() + "}";
        }
        if (type == FunctionType.SQRT) {
            return "\\sqrt{" + argument.toLatex() + "}";
        }
        return "\\" + type.name().toLowerCase() + "(" + argument.toLatex() + ")";
    }

    @Override
    public T evaluate(Map<Variable<T>, T> assignments) {
        T argValue = argument.evaluate(assignments);

        // Special handling for Real type
        if (argValue instanceof Real) {
            Real realArg = (Real) argValue;
            Real result;

            switch (type) {
                case SIN:
                    result = Real.of(Math.sin(realArg.doubleValue()));
                    break;
                case COS:
                    result = Real.of(Math.cos(realArg.doubleValue()));
                    break;
                case TAN:
                    result = Real.of(Math.tan(realArg.doubleValue()));
                    break;
                case EXP:
                    result = Real.of(Math.exp(realArg.doubleValue()));
                    break;
                case LOG:
                    result = Real.of(Math.log(realArg.doubleValue()));
                    break;
                case SQRT:
                    result = Real.of(Math.sqrt(realArg.doubleValue()));
                    break;
                case POW:
                    T expValue = exponent.evaluate(assignments);
                    if (expValue instanceof Real) {
                        Real realExp = (Real) expValue;
                        result = Real.of(Math.pow(realArg.doubleValue(), realExp.doubleValue()));
                    } else {
                        throw new UnsupportedOperationException("POW evaluation requires Real exponent");
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown function type: " + type);
            }

            @SuppressWarnings("unchecked")
            T typedResult = (T) result;
            return typedResult;
        }

        throw new UnsupportedOperationException("Evaluation only supported for Real type");
    }

    @Override
    public String toString() {
        if (type == FunctionType.POW) {
            return "(" + argument + ")^(" + exponent + ")";
        }
        return type.name().toLowerCase() + "(" + argument + ")";
    }

    public FunctionType getType() {
        return type;
    }

    public Expression<T> getArgument() {
        return argument;
    }

    public Expression<T> getExponent() {
        return exponent;
    }
}
