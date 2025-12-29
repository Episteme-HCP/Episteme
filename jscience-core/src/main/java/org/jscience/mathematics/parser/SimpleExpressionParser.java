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

package org.jscience.mathematics.parser;

import java.util.Map;

/**
 * A lightweight mathematical expression parser and evaluator.
 * Supports standard operators (+, -, *, /, ^) and functions (sin, cos, etc.).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimpleExpressionParser {

    private final String expression;
    private int pos = -1, ch;

    public SimpleExpressionParser(String expression) {
        this.expression = expression;
    }

    private void nextChar() {
        ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
    }

    private boolean eat(int charToEat) {
        while (ch == ' ')
            nextChar();
        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    public double parse(Map<String, Double> variables) {
        pos = -1;
        nextChar();
        double x = parseExpression(variables);
        if (pos < expression.length())
            throw new RuntimeException("Unexpected: " + (char) ch);
        return x;
    }

    // Grammar:
    // expression = term | expression + term | expression - term
    // term = factor | term * factor | term / factor
    // factor = base ^ factor | base
    // base = +factor | -factor | ( expression ) | number | function | variable

    private double parseExpression(Map<String, Double> variables) {
        double x = parseTerm(variables);
        for (;;) {
            if (eat('+'))
                x += parseTerm(variables); // addition
            else if (eat('-'))
                x -= parseTerm(variables); // subtraction
            else
                return x;
        }
    }

    private double parseTerm(Map<String, Double> variables) {
        double x = parseFactor(variables);
        for (;;) {
            if (eat('*'))
                x *= parseFactor(variables); // multiplication
            else if (eat('/'))
                x /= parseFactor(variables); // division
            else
                return x;
        }
    }

    private double parseFactor(Map<String, Double> variables) {
        double x = parseBase(variables);
        if (eat('^'))
            x = Math.pow(x, parseFactor(variables)); // exponentiation
        return x;
    }

    private double parseBase(Map<String, Double> variables) {
        if (eat('+'))
            return parseFactor(variables); // unary plus
        if (eat('-'))
            return -parseFactor(variables); // unary minus

        double x;
        int startPos = this.pos;
        if (eat('(')) { // parentheses
            x = parseExpression(variables);
            eat(')');
        } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
            while ((ch >= '0' && ch <= '9') || ch == '.')
                nextChar();
            x = Double.parseDouble(expression.substring(startPos, pos));
        } else if (Character.isLetter(ch)) { // functions or variables
            while (Character.isLetter(ch) || Character.isDigit(ch))
                nextChar();
            String name = expression.substring(startPos, pos);
            if (eat('(')) { // function call
                x = parseExpression(variables);
                eat(')');
                x = applyFunction(name, x);
            } else { // variable
                if (variables != null && variables.containsKey(name)) {
                    x = variables.get(name);
                } else {
                    // Constants
                    if (name.equalsIgnoreCase("pi"))
                        x = Math.PI;
                    else if (name.equalsIgnoreCase("e"))
                        x = Math.E;
                    else
                        throw new RuntimeException("Unknown variable: " + name);
                }
            }
        } else {
            throw new RuntimeException("Unexpected: " + (char) ch);
        }
        return x;
    }

    private double applyFunction(String func, double x) {
        return switch (func.toLowerCase()) {
            case "sin" -> Math.sin(x);
            case "cos" -> Math.cos(x);
            case "tan" -> Math.tan(x);
            case " asin", "arcsin" -> Math.asin(x);
            case "acos", "arccos" -> Math.acos(x);
            case "atan", "arctan" -> Math.atan(x);
            case "sqrt" -> Math.sqrt(x);
            case "cbrt" -> Math.cbrt(x);
            case "log", "ln" -> Math.log(x);
            case "log10" -> Math.log10(x);
            case "exp" -> Math.exp(x);
            case "abs" -> Math.abs(x);
            case "floor" -> Math.floor(x);
            case "ceil" -> Math.ceil(x);
            default -> throw new RuntimeException("Unknown function: " + func);
        };
    }
}
