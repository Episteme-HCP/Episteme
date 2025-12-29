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

package org.jscience.mathematics.analysis;

/**
 * Utility for basic symbolic operations (Differentiation).
 * <p>
 * This is a lightweight implementation for the Function Plotter demo,
 * supporting basic rules for polynomials, trig functions, and exponentials.
 * It is NOT a full Computer Algebra System (CAS).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SymbolicUtil {

    /**
     * Computes the symbolic derivative of a function f(x).
     *
     * @param expression The mathematical expression (e.g., "x^2 + sin(x)")
     * @return The derivative string (e.g., "2*x + cos(x)")
     */
    public static String differentiate(String expression) {
        if (expression == null || expression.trim().isEmpty())
            return "";

        String expr = expression.replaceAll("\\s+", ""); // Remove spaces

        // Handle Sums/Differences first (recursive)
        // Note: splitting by + or - at top level is tricky with parentheses.
        // Simplified approach: split by parts if not inside parens

        // Try to identify top-level operators
        // This is a naive parser for demo purposes.

        if (isNumber(expr))
            return "0";
        if (expr.equals("x"))
            return "1";
        if (expr.equals("-x"))
            return "-1";

        // Power Rule: x^n -> n*x^(n-1)
        if (expr.matches("x\\^\\d+(\\.\\d+)?")) {
            String[] parts = expr.split("\\^");
            double n = Double.parseDouble(parts[1]);
            if (n == 0)
                return "0";
            if (n == 1)
                return "1";
            if (n == 2)
                return "2*x";
            return n + "*x^" + (n - 1);
        }

        // Basic Functions
        if (expr.startsWith("sin(") && expr.endsWith(")")) {
            String inner = getInner(expr);
            // Chain rule: sin(u) -> cos(u) * u'
            String dInner = differentiate(inner);
            if (dInner.equals("0"))
                return "0";
            String cos = "cos(" + inner + ")";
            return dInner.equals("1") ? cos : cos + "*" + wrap(dInner);
        }
        if (expr.startsWith("cos(") && expr.endsWith(")")) {
            String inner = getInner(expr);
            // Chain rule: cos(u) -> -sin(u) * u'
            String dInner = differentiate(inner);
            if (dInner.equals("0"))
                return "0";
            String sin = "-sin(" + inner + ")";
            return dInner.equals("1") ? sin : sin + "*" + wrap(dInner);
        }
        if (expr.startsWith("exp(") && expr.endsWith(")")) {
            String inner = getInner(expr);
            String dInner = differentiate(inner);
            if (dInner.equals("0"))
                return "0";
            String exp = "exp(" + inner + ")";
            return dInner.equals("1") ? exp : exp + "*" + wrap(dInner);
        }
        if (expr.startsWith("log(") && expr.endsWith(")")) { // ln
            String inner = getInner(expr);
            String dInner = differentiate(inner);
            if (dInner.equals("0"))
                return "0";
            return dInner.equals("1") ? "1/(" + inner + ")" : wrap(dInner) + "/(" + inner + ")";
        }
        if (expr.startsWith("sqrt(") && expr.endsWith(")")) {
            String inner = getInner(expr);
            String dInner = differentiate(inner);
            if (dInner.equals("0"))
                return "0";
            // 1/(2*sqrt(u)) * u'
            return dInner.equals("1") ? "1/(2*sqrt(" + inner + "))"
                    : wrap(dInner) + "/(2*sqrt(" + inner + "))";
        }

        // Polynomials / Linear Combinations
        // Detect top-level + or -
        int splitIdx = findSplitIndex(expr);
        if (splitIdx != -1) {
            String left = expr.substring(0, splitIdx);
            String right = expr.substring(splitIdx + 1);
            char op = expr.charAt(splitIdx);

            String dLeft = differentiate(left);
            String dRight = differentiate(right);

            // Clean up 0s
            if (dLeft.equals("0") && op == '+')
                return dRight;
            if (dLeft.equals("0") && op == '-')
                return "-" + wrap(dRight); // Be careful with double negatives
            if (dRight.equals("0"))
                return dLeft;

            return dLeft + " " + op + " " + dRight;
        }

        // Product Rule (simplified check for *)
        int multIdx = findMultIndex(expr);
        if (multIdx != -1) {
            String left = expr.substring(0, multIdx);
            String right = expr.substring(multIdx + 1);
            String dLeft = differentiate(left);
            String dRight = differentiate(right);

            // u'v + uv'
            return wrap(dLeft) + "*" + wrap(right) + " + " + wrap(left) + "*" + wrap(dRight);
        }

        // If unknown structure, assume constant or too complex
        // Or if it's e.g. "2*x", product rule above might handle it if programmed
        // right,
        // but for now let's handle "c*x" specifically?
        // Actually the product rule above logic handles it: d(2)*x + 2*d(x) -> 0*x +
        // 2*1 -> 2.

        return "NaN";
    }

    // Helpers

    private static String getInner(String func) {
        int first = func.indexOf('(');
        int last = func.lastIndexOf(')');
        if (first == -1 || last == -1)
            return func;
        return func.substring(first + 1, last);
    }

    private static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            if (s.equals("pi") || s.equals("e"))
                return true;
            return false;
        }
    }

    private static String wrap(String s) {
        if (s.contains("+") || s.contains("-"))
            return "(" + s + ")";
        return s;
    }

    private static int findSplitIndex(String expr) {
        int depth = 0;
        // Search from right to left to group properly? or left to right?
        // a + b + c -> (a+b) + c
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')')
                depth++;
            else if (c == '(')
                depth--;
            else if (depth == 0 && (c == '+' || c == '-')) {
                // Ensure it's not unary minus at start
                if (i > 0 && !isOperator(expr.charAt(i - 1))) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static int findMultIndex(String expr) {
        int depth = 0;
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')')
                depth++;
            else if (c == '(')
                depth--;
            else if (depth == 0 && (c == '*' || c == '/')) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }
}
