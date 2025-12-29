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

package org.jscience.computing.compiler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A simple DSL compiler that interprets an AST.
 * <p>
 * Provides basic expression evaluation with variable support and
 * extensible function definitions.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DSLCompiler implements Parser.ASTVisitor {

    private final Map<String, Object> variables = new HashMap<>();
    private final Map<String, Function<List<Object>, Object>> functions = new HashMap<>();

    public DSLCompiler() {
        registerBuiltinFunctions();
    }

    private void registerBuiltinFunctions() {
        // Math functions
        functions.put("sin", args -> Math.sin(toDouble(args.get(0))));
        functions.put("cos", args -> Math.cos(toDouble(args.get(0))));
        functions.put("tan", args -> Math.tan(toDouble(args.get(0))));
        functions.put("sqrt", args -> Math.sqrt(toDouble(args.get(0))));
        functions.put("pow", args -> Math.pow(toDouble(args.get(0)), toDouble(args.get(1))));
        functions.put("log", args -> Math.log(toDouble(args.get(0))));
        functions.put("log10", args -> Math.log10(toDouble(args.get(0))));
        functions.put("exp", args -> Math.exp(toDouble(args.get(0))));
        functions.put("abs", args -> Math.abs(toDouble(args.get(0))));
        functions.put("floor", args -> Math.floor(toDouble(args.get(0))));
        functions.put("ceil", args -> Math.ceil(toDouble(args.get(0))));
        functions.put("round", args -> (double) Math.round(toDouble(args.get(0))));
        functions.put("min", args -> Math.min(toDouble(args.get(0)), toDouble(args.get(1))));
        functions.put("max", args -> Math.max(toDouble(args.get(0)), toDouble(args.get(1))));

        // String functions
        functions.put("len", args -> (double) args.get(0).toString().length());
        functions.put("upper", args -> args.get(0).toString().toUpperCase());
        functions.put("lower", args -> args.get(0).toString().toLowerCase());
        functions.put("concat", args -> args.get(0).toString() + args.get(1).toString());

        // Print
        functions.put("print", args -> {
            System.out.println(args.get(0));
            return null;
        });
    }

    /**
     * Registers a custom function.
     */
    public void registerFunction(String name, Function<List<Object>, Object> func) {
        functions.put(name, func);
    }

    /**
     * Sets a variable value.
     */
    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    /**
     * Gets a variable value.
     */
    public Object getVariable(String name) {
        return variables.get(name);
    }

    /**
     * Compiles and executes source code.
     */
    public Object execute(String source) throws Exception {
        Lexer lexer = new Lexer();
        List<Lexer.Token> tokens = lexer.tokenize(source);

        Parser parser = new Parser();
        Parser.ProgramNode ast = parser.parse(tokens);

        return evaluate(ast);
    }

    /**
     * Evaluates an AST.
     */
    public Object evaluate(Parser.ASTNode node) {
        return node.accept(this);
    }

    @Override
    public Object visitProgram(Parser.ProgramNode node) {
        Object result = null;
        for (Parser.ASTNode child : node.getChildren()) {
            result = evaluate(child);
        }
        return result;
    }

    @Override
    public Object visitExpression(Parser.ExpressionNode node) {
        if (node.getChildren().isEmpty())
            return null;
        return evaluate(node.getChildren().get(0));
    }

    @Override
    public Object visitStatement(Parser.StatementNode node) {
        if (node.getChildren().isEmpty())
            return null;
        return evaluate(node.getChildren().get(0));
    }

    @Override
    public Object visitLiteral(Parser.LiteralNode node) {
        return node.value;
    }

    @Override
    public Object visitIdentifier(Parser.IdentifierNode node) {
        if (variables.containsKey(node.name)) {
            return variables.get(node.name);
        }
        // Check for constants
        if ("PI".equalsIgnoreCase(node.name))
            return Math.PI;
        if ("E".equalsIgnoreCase(node.name))
            return Math.E;
        if ("true".equals(node.name))
            return true;
        if ("false".equals(node.name))
            return false;

        throw new RuntimeException("Undefined variable: " + node.name);
    }

    @Override
    public Object visitBinaryOp(Parser.BinaryOpNode node) {
        Object left = evaluate(node.left);
        Object right = evaluate(node.right);

        switch (node.operator) {
            case "+":
                if (left instanceof String || right instanceof String) {
                    return left.toString() + right.toString();
                }
                return toDouble(left) + toDouble(right);
            case "-":
                return toDouble(left) - toDouble(right);
            case "*":
                return toDouble(left) * toDouble(right);
            case "/":
                return toDouble(left) / toDouble(right);
            case "%":
                return toDouble(left) % toDouble(right);
            case "==":
                return left.equals(right);
            case "!=":
                return !left.equals(right);
            case "<":
                return toDouble(left) < toDouble(right);
            case ">":
                return toDouble(left) > toDouble(right);
            case "<=":
                return toDouble(left) <= toDouble(right);
            case ">=":
                return toDouble(left) >= toDouble(right);
            case "&&":
                return toBoolean(left) && toBoolean(right);
            case "||":
                return toBoolean(left) || toBoolean(right);
            default:
                throw new RuntimeException("Unknown operator: " + node.operator);
        }
    }

    @Override
    public Object visitUnaryOp(Parser.UnaryOpNode node) {
        Object operand = evaluate(node.operand);

        switch (node.operator) {
            case "-":
                return -toDouble(operand);
            case "!":
                return !toBoolean(operand);
            default:
                throw new RuntimeException("Unknown unary operator: " + node.operator);
        }
    }

    @Override
    public Object visitCall(Parser.CallNode node) {
        Function<List<Object>, Object> func = functions.get(node.functionName);
        if (func == null) {
            throw new RuntimeException("Undefined function: " + node.functionName);
        }

        List<Object> args = new java.util.ArrayList<>();
        for (Parser.ASTNode arg : node.arguments) {
            args.add(evaluate(arg));
        }

        return func.apply(args);
    }

    private double toDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return Double.parseDouble(value.toString());
    }

    private boolean toBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue() != 0;
        }
        return Boolean.parseBoolean(value.toString());
    }
}
