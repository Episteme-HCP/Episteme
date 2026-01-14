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

package org.jscience.physics.fluids.dynamics.util;

import java.io.StreamTokenizer;
import java.io.StringReader;

import java.util.Hashtable;


/**
 * This class is used to interprete mathematical expressions. The code is
 * mostly from the Aray v7.0 programed by myself. The possibilities of the
 * interpreter are infinite, you can introduce new functions and interconect
 * them with other parts of the program.
 *
 * @author Alejandro "Balrog" Rodriguez Gallego
 * @version 1.1 (06/08/00)
 */
public class EvaluateExpression extends StreamTokenizer {
    // Constant of end of file (EOF)
    /** DOCUMENT ME! */
    private static final int T_eof = -1;

    // Constants of tokens of the type predefined words
    // and edit them too in CompileValue()
    /** DOCUMENT ME! */
    private static final int T_cos = 0;

    // Constants of tokens of the type predefined words
    // and edit them too in CompileValue()
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private static final int T_e = 1;

    // Constants of tokens of the type predefined words
    // and edit them too in CompileValue()
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private static final int T_exp = 2;

    // Constants of tokens of the type predefined words
    // and edit them too in CompileValue()
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private static final int T_log = 3;

    // Constants of tokens of the type predefined words
    // and edit them too in CompileValue()
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private static final int T_pi = 4;

    // Constants of tokens of the type predefined words
    // and edit them too in CompileValue()
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private static final int T_pow = 5;

    // Constants of tokens of the type predefined words
    // and edit them too in CompileValue()
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private static final int T_sin = 6;

    // Constants of tokens of the type predefined words
    // and edit them too in CompileValue()
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private static final int T_sqrt = 7;

    // Constants of tokens of the type predefined words
    // and edit them too in CompileValue()
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private static final int T_t = 8;

    // Constants of tokens of the type predefined words
    // and edit them too in CompileValue()
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private static final int T_tan = 9;

    // Constants of tokens of the type predefined words
    // and edit them too in CompileValue()
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private static final int T_x = 10;

    // Constants of tokens of the type predefined words
    // and edit them too in CompileValue()
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private static final int T_y = 11;

    // Constants of tokens of the type predefined words
    // and edit them too in CompileValue()
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private static final int T_z = 12;

    // Constants of the tokens type character and predefined specials
    /** DOCUMENT ME! */
    static final int T_value = 1000;

    // Constants of the tokens type character and predefined specials
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    static final int T_variable = 1001;

    // Constants of the tokens type character and predefined specials
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    static final int T_plus = 1002;

    // Constants of the tokens type character and predefined specials
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    static final int T_minus = 1003;

    // Constants of the tokens type character and predefined specials
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    static final int T_times = 1004;

    // Constants of the tokens type character and predefined specials
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    static final int T_divided = 1005;

    // Constants of the tokens type character and predefined specials
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    static final int T_parenthesis1 = 1006;

    // Constants of the tokens type character and predefined specials
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    static final int T_parenthesis2 = 1007;

    // Constants of the tokens type character and predefined specials
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    static final int T_exponent = 1008;

    // Constants of the tokens type character and predefined specials
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    static final int T_point = 1009;

    /** version of the class */
    public final String VERSION = "1.2";

    // Add here new tokens in alphabetical order
    // and edit the switch  in CompileValue()
    /** DOCUMENT ME! */
    private String[] tokens = {
            "cos", "e", "exp", "log", "pi", "pow", "sin", "sqrt", "t", "tan",
            "x", "y", "z"
        };

    /** Type of last processed token */
    int typeToken;

    /** Real value of last processed token */
    double valueToken;

    /** String of last processed token */
    String stringToken;

    /** error indicator */
    boolean evaluateError;

    /** hash table with the variables defined by the user */
    Hashtable variables;

    /** The most frequent variables are made built-in */
    double valorX;

    /** The most frequent variables are made built-in */
    double valorY;

    /** The most frequent variables are made built-in */
    double valorZ;

    /** The most frequent variables are made built-in */
    double valorT;

/**
     * Constructor of the class. Takes a argument the string that contains the
     * mathematical formula to be evaluated.
     *
     * @param expression
     */
    public EvaluateExpression(String expression) {
        super(new StringReader(expression));
        variables = new Hashtable();
        resetSyntax();
        parseNumbers();
        lowerCaseMode(true);
        slashSlashComments(false);
        slashStarComments(false);
        wordChars(0, 255);
        ordinaryChar('+');
        ordinaryChar('-');
        ordinaryChar('*');
        ordinaryChar('/');
        ordinaryChar('(');
        ordinaryChar(')');
        ordinaryChar('^');
        ordinaryChar(',');
        whitespaceChars(' ', ' ');
        whitespaceChars('\t', '\t');
        eolIsSignificant(false);
    }

    /**
     * Introduce a new variable in the list of variables of the user.
     *
     * @param name string with the name of the variable.
     * @param value numerical value to be assigned.
     */
    public void assignsVariable(String name, double value) {
        // They built-in variables are given faster than those in Hashtable
        if (name.equals("x")) {
            valorX = value;
        } else if (name.equals("y")) {
            valorY = value;
        } else if (name.equals("z")) {
            valorZ = value;
        } else if (name.equals("t")) {
            valorT = value;
        } else {
            variables.put(name, new Double(value));
        }
    }

    /**
     * Processes a new token of the expression. If there was one
     * returned, that one is processed again
     *
     * @return DOCUMENT ME!
     */
    private int takeToken() {
        try {
            int tok;

            switch (tok = nextToken()) {
            case TT_EOF:
                return T_eof;

            // Token word type
            case TT_WORD:

                // search first in predefinined
                for (int i = 0; i < tokens.length; i++)
                    if (sval.equals(tokens[i])) {
                        typeToken = i;
                        stringToken = tokens[i];

                        // System.out.println("tipo="+i+"\t"+tokens[i]);
                        return (i);
                    }

                // If not found, is from user
                typeToken = T_value;

                Double var = (Double) variables.get(sval);
                stringToken = sval;

                if (var == null) {
                    System.out.println(
                        "EvaluateExpression: unknown Variable ! (null)");
                    valueToken = 0.0;
                }

                valueToken = var.doubleValue();

                return (T_variable);

            // Token type number
            case TT_NUMBER:
                typeToken = T_value;
                valueToken = nval;

                return (T_value);

            // Tokens simple
            case '+':
                typeToken = T_plus;

                return (T_plus);

            case '-':
                typeToken = T_minus;

                return (T_minus);

            case '*':
                typeToken = T_times;

                return (T_times);

            case '/':
                typeToken = T_divided;

                return (T_divided);

            case '(':
                typeToken = T_parenthesis1;

                return (T_parenthesis1);

            case ')':
                typeToken = T_parenthesis2;

                return (T_parenthesis2);

            case '^':
                typeToken = T_exponent;

                return (T_exponent);

            case ',':
                typeToken = T_point;

                return (T_point);

            default:
                System.out.println("EvaluateExpression: internal error (Token)");
            }
        } catch (Exception e) {
            System.out.println("EvaluateExpression: Exception " + e);
        }

        return 0;
    }

    /**
     * Returns a token from the buffer.
     */
    private void returnsToken() {
        pushBack();
    }

    /**
     * Processes a token that needs to be of the indicated type. If
     * not, error.
     *
     * @param type DOCUMENT ME!
     */
    private void askForToken(int type) {
        takeToken();

        if (typeToken != type) {
            System.out.println("EvaluateExpression: Error in askForToken()");
        }
    }

    /**
     * Evaluate a factor
     *
     * @return DOCUMENT ME!
     */
    private double evaluateFactor() {
        double value = 0.0;
        double t1;
        double t2;

        takeToken();

        switch (typeToken) {
        case T_parenthesis1:
            value = compileValue();

            break;

        case T_value:
            value = valueToken;

            break;

        // como no llevamos account of los parentesis anidados,
        // lo hacemos asi for acabar al producirse the error...
        case T_e:
            value = 2.7182818284;

            break;

        case T_exp:
            askForToken(T_parenthesis1);
            value = Math.exp(compileValue());

            break;

        case T_log:
            askForToken(T_parenthesis1);
            value = Math.log(compileValue());

            break;

        case T_pi:
            value = 3.1415926535;

            break;

        case T_sin:
            askForToken(T_parenthesis1);
            value = Math.sin(compileValue());

            break;

        case T_sqrt:
            askForToken(T_parenthesis1);
            value = Math.sqrt(compileValue());

            break;

        case T_cos:
            askForToken(T_parenthesis1);
            value = Math.cos(compileValue());

            break;

        case T_t:
            value = valorZ;

            break;

        case T_tan:
            askForToken(T_parenthesis1);
            value = Math.tan(compileValue());

            break;

        case T_x:
            value = valorX;

            break;

        case T_y:
            value = valorY;

            break;

        case T_z:
            value = valorZ;

            break;

        // The potencia es a caso especial.
        case T_pow:
            askForToken(T_parenthesis1);
            t1 = compileValue();
            askForToken(T_point);
            t2 = compileValue();
            value = Math.pow(t1, t2);

            break;

        default:
            System.out.println("EvaluateExpression(): unknown token.");
            evaluateError = true;
        }

        return (value);
    }

    /**
     * Evaluate sum
     *
     * @return DOCUMENT ME!
     */
    private double takeSumTerm() {
        double value;
        double value2;
        boolean flag = true;

        value = evaluateFactor();

        do {
            takeToken();

            switch (typeToken) {
            case T_times:
                value *= evaluateFactor();

                break;

            case T_divided:
                value /= evaluateFactor();

                break;

            default:
                returnsToken();
                flag = false;

                break;
            }
        } while (flag && !evaluateError);

        return (value);
    }

    /**
     * Evaluate a value
     *
     * @return DOCUMENT ME!
     */
    private double compileValue() {
        double value = 0;
        boolean flag = true;
        boolean first = true;

        evaluateError = false;

        do {
            takeToken();

            switch (typeToken) {
            case T_plus:
                value += takeSumTerm();

                break;

            case T_minus:
                value -= takeSumTerm();

                break;

            case T_parenthesis1:
            case T_value:

            // the labels of all built-in mathematical functions and variables have to be placed here
            case T_sin:
            case T_cos:
            case T_log:
            case T_tan:
            case T_pow:
            case T_sqrt:
            case T_e:
            case T_exp:
            case T_pi:
            case T_x:
            case T_y:
            case T_z:
            case T_t:

                if (first) {
                    returnsToken();
                    value = takeSumTerm();
                } else {
                    returnsToken();
                    flag = false;
                }

                break;

            case T_parenthesis2:
                flag = false;

                break;

            default:
                returnsToken();
                flag = false;

                break;
            }

            first = false;
        } while (flag && !evaluateError);

        return (value);
    }

    /**
     * Returns the value of the expression. The mathematical formula
     * would have been passed to the constructor and the value of the
     * variables would be definided in the function
     * <code>assignsVariable</code>.
     *
     * @return DOCUMENT ME!
     */
    public double result() {
        return (compileValue());
    }

    /**
     * Returns the result of a expression type F(x,y). It is an
     * abreviated form of invocation, as this method calls automatically
     * <code>assignsVariable</code> for the indicated values of <code>x</code>
     * and <code>y</code>.
     *
     * @param xv DOCUMENT ME!
     * @param yv DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double result(double xv, double yv) {
        // x, y, z, t son built-in
        assignsVariable("x", xv);
        assignsVariable("y", yv);

        return (compileValue());
    }

    /**
     * Returns the result of a expression type F(x,y,z). It is an
     * abreviated form of invocation, as this method calls automatically
     * <code>assignsVariable</code> for the indicated values of <code>x</code>
     * and <code>y</code><code>z</code>.
     *
     * @param xv
     * @param yv
     * @param zv
     *
     * @return
     */
    public double result(double xv, double yv, double zv) {
        // x, y, z, t son built-in
        assignsVariable("x", xv);
        assignsVariable("y", yv);
        assignsVariable("z", zv);

        return (compileValue());
    }
}
