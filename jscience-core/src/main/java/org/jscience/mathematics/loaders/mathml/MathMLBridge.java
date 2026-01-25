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

package org.jscience.mathematics.loaders.mathml;

import org.jscience.mathematics.symbolic.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.mathml.MathMLDocument;
import org.w3c.dom.mathml.MathMLElement;

/**
 * Bridge between MathML DOM and JScience Symbolic Expressions.
 * <p>
 * This utility converts Content MathML structures into the {@link Expression}
 * tree used for symbolic manipulation in JScience.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public final class MathMLBridge {

    private MathMLBridge() {}

    /**
     * Converts a MathML document into a symbolic expression.
     */
    public static Expression<?> convert(MathMLDocument doc) {
        Element root = doc.getDocumentElement();
        // Skip <math> tag
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                return parseElement((Element) children.item(i));
            }
        }
        return null;
    }

    private static Expression<?> parseElement(Element elem) {
        String name = elem.getLocalName();
        if (name == null) name = elem.getTagName();

        return switch (name) {
            case "apply" -> parseApply(elem);
            case "ci" -> new Variable<>(elem.getTextContent().trim());
            case "cn" -> parseNumber(elem);
            default -> null; // Unsupported or Presentation MathML
        };
    }

    private static Expression<?> parseApply(Element elem) {
        NodeList children = elem.getChildNodes();
        Element operatorElem = null;
        java.util.List<Expression<?>> operands = new java.util.ArrayList<>();

        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                Element child = (Element) children.item(i);
                if (operatorElem == null) {
                    operatorElem = child;
                } else {
                    operands.add(parseElement(child));
                }
            }
        }

        if (operatorElem == null) return null;
        String op = operatorElem.getLocalName();

        // Map MathML operators to JScience Expressions
        return switch (op) {
            case "plus" -> combine(operands, (a, b) -> addAny(a, b));
            case "times" -> combine(operands, (a, b) -> multiplyAny(a, b));
            case "divide" -> (operands.size() == 2) ? divideAny(operands.get(0), operands.get(1)) : null;
            case "minus" -> (operands.size() == 2) ? subtractAny(operands.get(0), operands.get(1)) : 
                            (operands.size() == 1) ? operands.get(0).negate() : null;
            default -> null;
        };
    }

    @SuppressWarnings("unchecked")
    private static <T extends org.jscience.mathematics.structures.rings.Ring<T>> Expression<T> addAny(Expression<?> e1, Expression<?> e2) {
        return ((Expression<T>)e1).add((Expression<T>)e2);
    }

    @SuppressWarnings("unchecked")
    private static <T extends org.jscience.mathematics.structures.rings.Ring<T>> Expression<T> multiplyAny(Expression<?> e1, Expression<?> e2) {
        return ((Expression<T>)e1).multiply((Expression<T>)e2);
    }

    @SuppressWarnings("unchecked")
    private static <T extends org.jscience.mathematics.structures.rings.Ring<T>> Expression<T> divideAny(Expression<?> e1, Expression<?> e2) {
        return ((Expression<T>)e1).divide((Expression<T>)e2);
    }

    @SuppressWarnings("unchecked")
    private static <T extends org.jscience.mathematics.structures.rings.Ring<T>> Expression<T> subtractAny(Expression<?> e1, Expression<?> e2) {
        return ((Expression<T>)e1).subtract((Expression<T>)e2);
    }

    private static Expression<?> parseNumber(Element elem) {
        try {
            double val = Double.parseDouble(elem.getTextContent().trim());
            return ConstantExpression.valueOf(val);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Expression<?> combine(java.util.List<Expression<?>> exprs, 
                                       java.util.function.BiFunction<Expression<?>, Expression<?>, Expression<?>> op) {
        if (exprs.isEmpty()) return null;
        Expression result = exprs.get(0);
        for (int i = 1; i < exprs.size(); i++) {
            result = op.apply(result, exprs.get(i));
        }
        return result;
    }
}
