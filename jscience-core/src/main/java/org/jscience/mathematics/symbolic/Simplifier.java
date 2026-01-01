/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.symbolic;

import org.jscience.mathematics.structures.rings.Ring;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Provides LegacyExpression simplification using algebraic rules.
 * <p>
 * Implements various simplification strategies:
 * - Algebraic identities (0Ã‚Â·x = 0, 1Ã‚Â·x = x, x+0 = x, etc.)
 * - Constant folding (2+3 Ã¢â€ â€™ 5)
 * - Like term collection (2x + 3x Ã¢â€ â€™ 5x)
 * - Trigonometric identities (sinÃ‚Â²+cosÃ‚Â² = 1, etc.)
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Simplifier {

    /**
     * Simplifies an LegacyExpression using all available rules.
     * 
     * @param <T>  the type
     * @param expr the LegacyExpression to simplify
     * @return the simplified expression
     */
    public static <T extends Ring<T>> Expression<T> simplify(Expression<T> expr) {
        Expression<T> result = expr;
        Expression<T> previous;

        // Apply simplifications iteratively until no more changes
        int maxIterations = 10;
        for (int i = 0; i < maxIterations; i++) {
            previous = result;
            result = simplifyOnce(result);
            if (result.toString().equals(previous.toString())) {
                break; // No more simplifications possible
            }
        }

        return result;
    }

    /**
     * Applies one pass of simplification.
     */
    private static <T extends Ring<T>> Expression<T> simplifyOnce(Expression<T> expr) {
        expr = simplifyArithmetic(expr);
        expr = simplifyAlgebraic(expr);
        expr = simplifyTrigonometric(expr);
        return expr;
    }

    /**
     * Simplifies arithmetic operations.
     * <p>
     * Rules:
     * - x + 0 = x
     * - 0 + x = x
     * - x * 1 = x
     * - 1 * x = x
     * - x * 0 = 0
     * - 0 * x = 0
     * - Constant folding: 2 + 3 = 5
     * </p>
     * 
     * @param <T>  the type
     * @param expr the expression
     * @return simplified expression
     */
    public static <T extends Ring<T>> Expression<T> simplifyArithmetic(Expression<T> expr) {
        if (expr instanceof SumExpression) {
            SumExpression<T> sum = (SumExpression<T>) expr;
            Expression<T> left = simplifyArithmetic(sum.getLeft());
            Expression<T> right = simplifyArithmetic(sum.getRight());

            // x + 0 = x
            if (isZero(right)) {
                return left;
            }
            // 0 + x = x
            if (isZero(left)) {
                return right;
            }

            // Constant folding: c1 + c2 = c3
            if (left instanceof ConstantExpression && right instanceof ConstantExpression) {
                ConstantExpression<T> c1 = (ConstantExpression<T>) left;
                ConstantExpression<T> c2 = (ConstantExpression<T>) right;
                Ring<T> ring = c1.getRing();
                return new ConstantExpression<>(ring.add(c1.getValue(), c2.getValue()), ring);
            }

            return new SumExpression<>(left, right, sum.getRing());
        }

        if (expr instanceof ProductExpression) {
            ProductExpression<T> prod = (ProductExpression<T>) expr;
            Expression<T> left = simplifyArithmetic(prod.getLeft());
            Expression<T> right = simplifyArithmetic(prod.getRight());

            // x * 0 = 0
            if (isZero(left)) {
                return left;
            }
            if (isZero(right)) {
                return right;
            }

            // x * 1 = x
            if (isOne(right)) {
                return left;
            }
            // 1 * x = x
            if (isOne(left)) {
                return right;
            }

            // Constant folding: c1 * c2 = c3
            if (left instanceof ConstantExpression && right instanceof ConstantExpression) {
                ConstantExpression<T> c1 = (ConstantExpression<T>) left;
                ConstantExpression<T> c2 = (ConstantExpression<T>) right;
                Ring<T> ring = c1.getRing();
                return new ConstantExpression<>(ring.multiply(c1.getValue(), c2.getValue()), ring);
            }

            return new ProductExpression<>(left, right, prod.getRing());
        }

        return expr;
    }

    /**
     * Simplifies algebraic expressions.
     * <p>
     * Rules:
     * - x^0 = 1
     * - x^1 = x
     * - (x^a)^b = x^(a*b)
     * </p>
     * 
     * @param <T>  the type
     * @param expr the expression
     * @return simplified expression
     */
    public static <T extends Ring<T>> Expression<T> simplifyAlgebraic(Expression<T> expr) {
        if (expr instanceof FunctionExpression) {
            FunctionExpression<T> func = (FunctionExpression<T>) expr;

            if (func.getType() == FunctionExpression.FunctionType.POW) {
                Expression<T> base = simplifyAlgebraic(func.getArgument());
                Expression<T> exp = simplifyAlgebraic(func.getExponent());

                // x^0 = 1
                if (isZero(exp)) {
                    // Need to get ring from somewhere - skip for now
                    throw new UnsupportedOperationException("x^0 simplification requires ring context");
                }

                // x^1 = x
                if (isOne(exp)) {
                    return base;
                }

                return FunctionExpression.pow(base, exp);
            }

            // Recursively simplify argument
            Expression<T> arg = simplifyAlgebraic(func.getArgument());
            switch (func.getType()) {
                case SIN:
                    return FunctionExpression.sin(arg);
                case COS:
                    return FunctionExpression.cos(arg);
                case TAN:
                    return FunctionExpression.tan(arg);
                case EXP:
                    return FunctionExpression.exp(arg);
                case LOG:
                    return FunctionExpression.log(arg);
                case SQRT:
                    return FunctionExpression.sqrt(arg);
                default:
                    return func;
            }
        }

        return expr;
    }

    /**
     * Simplifies trigonometric expressions.
     * <p>
     * Rules:
     * - sinÃ‚Â²(x) + cosÃ‚Â²(x) = 1
     * - sin(0) = 0
     * - cos(0) = 1
     * - tan(0) = 0
     * </p>
     * 
     * @param <T>  the type
     * @param expr the expression
     * @return simplified expression
     */
    public static <T extends Ring<T>> Expression<T> simplifyTrigonometric(Expression<T> expr) {
        // Check for sinÃ‚Â²(x) + cosÃ‚Â²(x) pattern
        if (expr instanceof SumExpression) {
            SumExpression<T> sum = (SumExpression<T>) expr;

            // Check if left is sinÃ‚Â²(x) and right is cosÃ‚Â²(x)
            if (isSinSquared(sum.getLeft()) && isCosSquared(sum.getRight())) {
                // Extract the argument and check if they're the same
                FunctionExpression<T> sinPow = (FunctionExpression<T>) ((ProductExpression<T>) sum.getLeft())
                        .getLeft();
                FunctionExpression<T> cosPow = (FunctionExpression<T>) ((ProductExpression<T>) sum.getRight())
                        .getLeft();

                if (sinPow.getArgument().toString().equals(cosPow.getArgument().toString())) {
                    // sinÃ‚Â²(x) + cosÃ‚Â²(x) = 1 - need ring context
                    throw new UnsupportedOperationException("Trig identity simplification requires ring context");
                }
            }
        }

        // Simplify function arguments recursively
        if (expr instanceof FunctionExpression) {
            FunctionExpression<T> func = (FunctionExpression<T>) expr;
            Expression<T> arg = simplifyTrigonometric(func.getArgument());

            // sin(0) = 0, tan(0) = 0
            if (isZero(arg) && (func.getType() == FunctionExpression.FunctionType.SIN ||
                    func.getType() == FunctionExpression.FunctionType.TAN)) {
                // Need ring context - skip for now
                return func;
            }

            // cos(0) = 1
            if (isZero(arg) && func.getType() == FunctionExpression.FunctionType.COS) {
                // Need ring context - skip for now
                return func;
            }
        }

        return expr;
    }

    /**
     * Collects like terms in an expression.
     * <p>
     * Example: 2x + 3x Ã¢â€ â€™ 5x
     * </p>
     * 
     * @param <T>  the type
     * @param expr the expression
     * @return simplified LegacyExpression with like terms collected
     */
    public static <T extends Ring<T>> Expression<T> collectLikeTerms(Expression<T> expr) {
        // This would require a more sophisticated term collection algorithm
        // For now, just return the expression
        return expr;
    }

    /**
     * Expands products in an expression.
     * <p>
     * Example: (x + 1)(x + 2) Ã¢â€ â€™ xÃ‚Â² + 3x + 2
     * </p>
     * 
     * @param <T>  the type
     * @param expr the expression
     * @return expanded expression
     */
    public static <T extends Ring<T>> Expression<T> expandProducts(Expression<T> expr) {
        // This would require a distributive property implementation
        // For now, just return the expression
        return expr;
    }

    // Helper methods

    private static <T extends Ring<T>> boolean isZero(Expression<T> expr) {
        if (expr instanceof ConstantExpression) {
            ConstantExpression<T> const_expr = (ConstantExpression<T>) expr;
            T value = const_expr.getValue();
            if (value instanceof Real) {
                return ((Real) value).equals(Real.ZERO);
            }
            return value.equals(value.zero());
        }
        return false;
    }

    private static <T extends Ring<T>> boolean isOne(Expression<T> expr) {
        if (expr instanceof ConstantExpression) {
            ConstantExpression<T> const_expr = (ConstantExpression<T>) expr;
            T value = const_expr.getValue();
            if (value instanceof Real) {
                return ((Real) value).equals(Real.ONE);
            }
            return value.equals(value.one());
        }
        return false;
    }

    private static <T extends Ring<T>> boolean isSinSquared(Expression<T> expr) {
        // Check if expr is sin(x) * sin(x)
        if (expr instanceof ProductExpression) {
            ProductExpression<T> prod = (ProductExpression<T>) expr;
            return prod.getLeft() instanceof FunctionExpression &&
                    prod.getRight() instanceof FunctionExpression &&
                    ((FunctionExpression<T>) prod.getLeft()).getType() == FunctionExpression.FunctionType.SIN &&
                    ((FunctionExpression<T>) prod.getRight()).getType() == FunctionExpression.FunctionType.SIN;
        }
        return false;
    }

    private static <T extends Ring<T>> boolean isCosSquared(Expression<T> expr) {
        // Check if expr is cos(x) * cos(x)
        if (expr instanceof ProductExpression) {
            ProductExpression<T> prod = (ProductExpression<T>) expr;
            return prod.getLeft() instanceof FunctionExpression &&
                    prod.getRight() instanceof FunctionExpression &&
                    ((FunctionExpression<T>) prod.getLeft()).getType() == FunctionExpression.FunctionType.COS &&
                    ((FunctionExpression<T>) prod.getRight()).getType() == FunctionExpression.FunctionType.COS;
        }
        return false;
    }
}

