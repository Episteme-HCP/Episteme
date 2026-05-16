/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.mathematics.loaders.openmath;

import org.episteme.core.mathematics.symbolic.*;
import java.util.List;
import java.util.ArrayList;
import java.util.function.BiFunction;

/**
 * Bridge between OpenMath structures and Episteme Symbolic Expressions.
 * <p>
 * Converts OpenMath objects (OMS, OMV, OMI, OMF, OMA) into the general 
 * {@link Expression} tree for algebraic computing.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public final class OpenMathBridge {

    private OpenMathBridge() {}

    /**
     * Converts an OpenMath object to an Expression.
     */
    public static Expression<?> convert(Object omObj) {
        if (omObj instanceof OMVariable v) {
            return new Variable<>(v.getName());
        } else if (omObj instanceof OMInteger i) {
            return ConstantExpression.valueOf(Double.parseDouble(i.getInteger()));
        } else if (omObj instanceof OMFloat f) {
            return ConstantExpression.valueOf(f.doubleValue());
        } else if (omObj instanceof Number n) {
            return ConstantExpression.valueOf(n.doubleValue());
        } else if (omObj instanceof OMBinding) {
             return null; 
        } else if (omObj instanceof OMForeign) { 
             return null; 
        } else if (omObj instanceof OMApplication app) {
            return convertApplication(app); 
        } else if (omObj instanceof OMSymbol s) {
            return new Variable<>(s.getName());
        }
        return null;
    }

    private static Expression<?> convertApplication(OMApplication app) {
        if (app.getLength() == 0) return null;
        OMObject operatorObj = app.getElementAt(0);
        if (!(operatorObj instanceof OMSymbol sym)) return null;

        String cd = sym.getCd();
        String name = sym.getName();

        // Operands match app elements from index 1 to end
        List<Expression<?>> operands = new ArrayList<>();
        int length = app.getLength();
        for (int i = 1; i < length; i++) {
            Expression<?> e = convert(app.getElementAt(i));
            if (e != null) operands.add(e);
        }

        if (operands.isEmpty() && length > 1) return null; // Arguments failed to convert

        // Arith1 Content Dictionary
        if ("arith1".equals(cd)) {
            return switch (name) {
                case "plus" -> combine(operands, (a, b) -> addAny(a, b));
                case "times" -> combine(operands, (a, b) -> multiplyAny(a, b));
                case "minus" -> (operands.size() == 2) ? subtractAny(operands.get(0), operands.get(1)) :
                                (operands.size() == 1) ? operands.get(0).negate() : null;
                case "divide" -> (operands.size() == 2) ? divideAny(operands.get(0), operands.get(1)) : null;
                case "power" -> (operands.size() == 2) ? powerAny(operands.get(0), parseInteger(app.getElementAt(2))) : null; 
                default -> null;
            };
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T extends org.episteme.core.mathematics.structures.rings.Ring<T>> Expression<T> addAny(Expression<?> e1, Expression<?> e2) {
        return ((Expression<T>)e1).add((Expression<T>)e2);
    }

    @SuppressWarnings("unchecked")
    private static <T extends org.episteme.core.mathematics.structures.rings.Ring<T>> Expression<T> multiplyAny(Expression<?> e1, Expression<?> e2) {
        return ((Expression<T>)e1).multiply((Expression<T>)e2);
    }

    @SuppressWarnings("unchecked")
    private static <T extends org.episteme.core.mathematics.structures.rings.Ring<T>> Expression<T> divideAny(Expression<?> e1, Expression<?> e2) {
        return ((Expression<T>)e1).divide((Expression<T>)e2);
    }

    @SuppressWarnings("unchecked")
    private static <T extends org.episteme.core.mathematics.structures.rings.Ring<T>> Expression<T> subtractAny(Expression<?> e1, Expression<?> e2) {
        return ((Expression<T>)e1).subtract((Expression<T>)e2);
    }

    @SuppressWarnings("unchecked")
    private static <T extends org.episteme.core.mathematics.structures.rings.Ring<T>> Expression<T> powerAny(Expression<?> e1, int pow) {
        return ((Expression<T>)e1).pow(pow);
    }

    private static int parseInteger(Object obj) {
        if (obj instanceof OMInteger i) return i.intValue();
        if (obj instanceof Number n) return n.intValue();
        return 1;
    }

    private static Expression<?> combine(List<Expression<?>> exprs, 
                                       BiFunction<Expression<?>, Expression<?>, Expression<?>> op) {
        if (exprs.isEmpty()) return null;
        Expression<?> result = exprs.get(0);
        for (int i = 1; i < exprs.size(); i++) {
            result = op.apply(result, exprs.get(i));
        }
        return result;
    }
}

