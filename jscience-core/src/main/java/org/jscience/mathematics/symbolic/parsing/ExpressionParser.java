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

package org.jscience.mathematics.symbolic.parsing;

import java.util.*;

/**
 * Expression parser for mathematical text.
 * <p>
 * Parses strings like "sin(x) + 2*y^2" into evaluable expressions.
 * Supports automatic differentiation.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ExpressionParser {

    /**
     * Parse a mathematical expression string.
     * 
     * @param expression Text like "x^2 + sin(y)"
     * @return Parsed expression node
     */
    public static Expr parse(String expression) {
        Parser parser = new Parser(expression);
        return parser.parseExpression();
    }

    // ==================== Expression Nodes ====================

    /**
     * Base interface for parsed expressions.
     */
    public interface Expr {
        double eval(Map<String, Double> vars);

        Expr differentiate(String variable);

        String toText();
    }

    /**
     * Constant value.
     */
    public static class Const implements Expr {
        public final double value;

        public Const(double value) {
            this.value = value;
        }

        @Override
        public double eval(Map<String, Double> vars) {
            return value;
        }

        @Override
        public Expr differentiate(String variable) {
            return new Const(0); // d/dx(c) = 0
        }

        @Override
        public String toText() {
            if (value == (int) value)
                return String.valueOf((int) value);
            return String.valueOf(value);
        }
    }

    /**
     * Variable reference.
     */
    public static class Var implements Expr {
        public final String name;

        public Var(String name) {
            this.name = name;
        }

        @Override
        public double eval(Map<String, Double> vars) {
            Double val = vars.get(name);
            if (val == null)
                throw new IllegalArgumentException("Undefined variable: " + name);
            return val;
        }

        @Override
        public Expr differentiate(String variable) {
            return new Const(name.equals(variable) ? 1 : 0); // d/dx(x) = 1, d/dx(y) = 0
        }

        @Override
        public String toText() {
            return name;
        }
    }

    /**
     * Binary operation: +, -, *, /, ^
     */
    public static class BinaryOp implements Expr {
        public final char op;
        public final Expr left;
        public final Expr right;

        public BinaryOp(char op, Expr left, Expr right) {
            this.op = op;
            this.left = left;
            this.right = right;
        }

        @Override
        public double eval(Map<String, Double> vars) {
            double l = left.eval(vars);
            double r = right.eval(vars);
            return switch (op) {
                case '+' -> l + r;
                case '-' -> l - r;
                case '*' -> l * r;
                case '/' -> l / r;
                case '^' -> Math.pow(l, r);
                default -> throw new IllegalStateException("Unknown operator: " + op);
            };
        }

        @Override
        public Expr differentiate(String variable) {
            Expr dl = left.differentiate(variable);
            Expr dr = right.differentiate(variable);

            return switch (op) {
                case '+' -> simplify(new BinaryOp('+', dl, dr));
                case '-' -> simplify(new BinaryOp('-', dl, dr));
                case '*' -> // Product rule: d(f*g) = f'*g + f*g'
                    simplify(new BinaryOp('+',
                            new BinaryOp('*', dl, right),
                            new BinaryOp('*', left, dr)));
                case '/' -> // Quotient rule: d(f/g) = (f'*g - f*g') / g^2
                    simplify(new BinaryOp('/',
                            new BinaryOp('-',
                                    new BinaryOp('*', dl, right),
                                    new BinaryOp('*', left, dr)),
                            new BinaryOp('^', right, new Const(2))));
                case '^' -> // Power rule (assuming exponent is constant or variable)
                    simplify(new BinaryOp('*',
                            new BinaryOp('*', right, new BinaryOp('^', left, new BinaryOp('-', right, new Const(1)))),
                            dl));
                default -> throw new IllegalStateException("Unknown operator: " + op);
            };
        }

        @Override
        public String toText() {
            return "(" + left.toText() + " " + op + " " + right.toText() + ")";
        }
    }

    /**
     * Function call: sin, cos, tan, exp, log, sqrt, abs
     */
    public static class FuncCall implements Expr {
        public final String name;
        public final Expr arg;

        public FuncCall(String name, Expr arg) {
            this.name = name;
            this.arg = arg;
        }

        @Override
        public double eval(Map<String, Double> vars) {
            double a = arg.eval(vars);
            return switch (name) {
                case "sin" -> Math.sin(a);
                case "cos" -> Math.cos(a);
                case "tan" -> Math.tan(a);
                case "asin", "arcsin" -> Math.asin(a);
                case "acos", "arccos" -> Math.acos(a);
                case "atan", "arctan" -> Math.atan(a);
                case "exp" -> Math.exp(a);
                case "log", "ln" -> Math.log(a);
                case "log10" -> Math.log10(a);
                case "sqrt" -> Math.sqrt(a);
                case "abs" -> Math.abs(a);
                case "sinh" -> Math.sinh(a);
                case "cosh" -> Math.cosh(a);
                case "tanh" -> Math.tanh(a);
                default -> throw new IllegalArgumentException("Unknown function: " + name);
            };
        }

        @Override
        public Expr differentiate(String variable) {
            Expr da = arg.differentiate(variable);

            Expr derivative = switch (name) {
                case "sin" -> new FuncCall("cos", arg); // d/dx sin(u) = cos(u) * u'
                case "cos" -> new BinaryOp('*', new Const(-1), new FuncCall("sin", arg));
                case "tan" ->
                    new BinaryOp('/', new Const(1), new BinaryOp('^', new FuncCall("cos", arg), new Const(2)));
                case "exp" -> new FuncCall("exp", arg);
                case "log", "ln" -> new BinaryOp('/', new Const(1), arg);
                case "sqrt" -> new BinaryOp('/', new Const(0.5), new FuncCall("sqrt", arg));
                case "sinh" -> new FuncCall("cosh", arg);
                case "cosh" -> new FuncCall("sinh", arg);
                default -> throw new IllegalArgumentException("Derivative not implemented for: " + name);
            };

            // Chain rule: d/dx f(u) = f'(u) * u'
            return simplify(new BinaryOp('*', derivative, da));
        }

        @Override
        public String toText() {
            return name + "(" + arg.toText() + ")";
        }
    }

    /**
     * Unary negation.
     */
    public static class Negate implements Expr {
        public final Expr arg;

        public Negate(Expr arg) {
            this.arg = arg;
        }

        @Override
        public double eval(Map<String, Double> vars) {
            return -arg.eval(vars);
        }

        @Override
        public Expr differentiate(String variable) {
            return new Negate(arg.differentiate(variable));
        }

        @Override
        public String toText() {
            return "(-" + arg.toText() + ")";
        }
    }

    // ==================== Simplifier ====================

    public static Expr simplify(Expr e) {
        if (e instanceof BinaryOp b) {
            Expr left = simplify(b.left);
            Expr right = simplify(b.right);

            // Constant folding
            if (left instanceof Const l && right instanceof Const r) {
                return new Const(new BinaryOp(b.op, l, r).eval(Map.of()));
            }

            // Identity rules
            if (b.op == '+') {
                if (isZero(left))
                    return right;
                if (isZero(right))
                    return left;
            } else if (b.op == '-') {
                if (isZero(right))
                    return left;
            } else if (b.op == '*') {
                if (isZero(left) || isZero(right))
                    return new Const(0);
                if (isOne(left))
                    return right;
                if (isOne(right))
                    return left;
            } else if (b.op == '/') {
                if (isZero(left))
                    return new Const(0);
                if (isOne(right))
                    return left;
            } else if (b.op == '^') {
                if (isZero(right))
                    return new Const(1);
                if (isOne(right))
                    return left;
            }

            return new BinaryOp(b.op, left, right);
        }
        return e;
    }

    private static boolean isZero(Expr e) {
        return e instanceof Const c && c.value == 0;
    }

    private static boolean isOne(Expr e) {
        return e instanceof Const c && c.value == 1;
    }

    // ==================== Parser ====================

    private static class Parser {
        private final String input;
        private int pos = 0;

        Parser(String input) {
            this.input = input.replaceAll("\\s+", ""); // Remove whitespace
        }

        Expr parseExpression() {
            return parseAddSub();
        }

        Expr parseAddSub() {
            Expr left = parseMulDiv();
            while (pos < input.length() && (current() == '+' || current() == '-')) {
                char op = consume();
                Expr right = parseMulDiv();
                left = new BinaryOp(op, left, right);
            }
            return left;
        }

        Expr parseMulDiv() {
            Expr left = parsePower();
            while (pos < input.length() && (current() == '*' || current() == '/')) {
                char op = consume();
                Expr right = parsePower();
                left = new BinaryOp(op, left, right);
            }
            return left;
        }

        Expr parsePower() {
            Expr left = parseUnary();
            if (pos < input.length() && current() == '^') {
                consume();
                Expr right = parsePower(); // Right associative
                left = new BinaryOp('^', left, right);
            }
            return left;
        }

        Expr parseUnary() {
            if (pos < input.length() && current() == '-') {
                consume();
                return new Negate(parseUnary());
            }
            return parseAtom();
        }

        Expr parseAtom() {
            if (current() == '(') {
                consume();
                Expr e = parseExpression();
                expect(')');
                return e;
            }

            if (Character.isDigit(current()) || current() == '.') {
                return parseNumber();
            }

            if (Character.isLetter(current())) {
                return parseIdentifier();
            }

            throw new RuntimeException("Unexpected character: " + current() + " at position " + pos);
        }

        Expr parseNumber() {
            StringBuilder sb = new StringBuilder();
            while (pos < input.length() && (Character.isDigit(current()) || current() == '.')) {
                sb.append(consume());
            }
            return new Const(Double.parseDouble(sb.toString()));
        }

        Expr parseIdentifier() {
            StringBuilder sb = new StringBuilder();
            while (pos < input.length() && (Character.isLetterOrDigit(current()) || current() == '_')) {
                sb.append(consume());
            }
            String name = sb.toString();

            // Check for function call
            if (pos < input.length() && current() == '(') {
                consume();
                Expr arg1 = parseExpression();
                Expr arg2 = null;

                if (current() == ',') {
                    consume();
                    arg2 = parseExpression();
                }
                expect(')');

                if (name.equals("diff")) {
                    if (arg2 == null)
                        throw new IllegalArgumentException("diff requires 2 arguments: diff(expr, var)");
                    if (!(arg2 instanceof Var))
                        throw new IllegalArgumentException("Second argument to diff must be a variable");
                    return arg1.differentiate(((Var) arg2).name);
                }

                if (name.equals("int")) {
                    if (arg2 == null)
                        throw new IllegalArgumentException("int requires 2 arguments: int(expr, var)");
                    if (!(arg2 instanceof Var))
                        throw new IllegalArgumentException("Second argument to int must be a variable");
                    return org.jscience.mathematics.symbolic.integration.SymbolicIntegration.integrate(arg1,
                            ((Var) arg2).name);
                }

                if (arg2 != null) {
                    throw new IllegalArgumentException("Function " + name + " does not support 2 arguments");
                }

                // Handle constants
                if (name.equals("pi") || name.equals("PI")) {
                    return new Const(Math.PI);
                } else if (name.equals("e") || name.equals("E")) {
                    return new Const(Math.E);
                }

                return new FuncCall(name, arg1);
            }

            // Constants
            if (name.equals("pi") || name.equals("PI")) {
                return new Const(Math.PI);
            } else if (name.equals("e") || name.equals("E")) {
                return new Const(Math.E);
            }

            return new Var(name);
        }

        char current() {
            return pos < input.length() ? input.charAt(pos) : '\0';
        }

        char consume() {
            return input.charAt(pos++);
        }

        void expect(char c) {
            if (current() != c) {
                throw new RuntimeException("Expected '" + c + "' at position " + pos);
            }
            pos++;
        }
    }

    // ==================== Convenience Methods ====================

    /**
     * Quick evaluation of an expression.
     */
    public static double eval(String expression, Map<String, Double> variables) {
        return parse(expression).eval(variables);
    }

    /**
     * Compute derivative symbolically.
     */
    public static Expr derivative(String expression, String variable) {
        return simplify(parse(expression).differentiate(variable));
    }

    /**
     * Compute numerical derivative using central difference.
     */
    public static double numericalDerivative(String expression, String variable,
            Map<String, Double> point, double h) {
        Expr expr = parse(expression);
        Map<String, Double> pointPlus = new HashMap<>(point);
        Map<String, Double> pointMinus = new HashMap<>(point);

        pointPlus.put(variable, point.get(variable) + h);
        pointMinus.put(variable, point.get(variable) - h);

        return (expr.eval(pointPlus) - expr.eval(pointMinus)) / (2 * h);
    }

    /**
     * Compute gradient for all variables.
     */
    public static Map<String, Expr> gradient(String expression, String... variables) {
        Expr expr = parse(expression);
        Map<String, Expr> grad = new HashMap<>();
        for (String var : variables) {
            grad.put(var, simplify(expr.differentiate(var)));
        }
        return grad;
    }
}


