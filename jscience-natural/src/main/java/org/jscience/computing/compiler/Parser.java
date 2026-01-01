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

import java.util.ArrayList;
import java.util.List;

/**
 * A recursive descent parser for building Abstract Syntax Trees.
 * <p>
 * Provides infrastructure for parsing token streams into AST nodes.
 * Subclasses implement specific grammar rules.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Parser {

    /**
     * Base class for AST nodes.
     */
    public static abstract class ASTNode {
        public final String type;
        public final int line;
        public final int column;
        protected final List<ASTNode> children = new ArrayList<>();

        public ASTNode(String type, int line, int column) {
            this.type = type;
            this.line = line;
            this.column = column;
        }

        public void addChild(ASTNode child) {
            children.add(child);
        }

        public List<ASTNode> getChildren() {
            return children;
        }

        public abstract Object accept(ASTVisitor visitor);
    }

    /**
     * Visitor pattern for AST traversal.
     */
    public interface ASTVisitor {
        Object visitProgram(ProgramNode node);

        Object visitExpression(ExpressionNode node);

        Object visitStatement(StatementNode node);

        Object visitLiteral(LiteralNode node);

        Object visitIdentifier(IdentifierNode node);

        Object visitBinaryOp(BinaryOpNode node);

        Object visitUnaryOp(UnaryOpNode node);

        Object visitCall(CallNode node);
    }

    // Concrete AST node types

    public static class ProgramNode extends ASTNode {
        public ProgramNode() {
            super("Program", 1, 1);
        }

        @Override
        public Object accept(ASTVisitor visitor) {
            return visitor.visitProgram(this);
        }
    }

    public static class ExpressionNode extends ASTNode {
        public ExpressionNode(int line, int column) {
            super("Expression", line, column);
        }

        @Override
        public Object accept(ASTVisitor visitor) {
            return visitor.visitExpression(this);
        }
    }

    public static class StatementNode extends ASTNode {
        public final String statementType;

        public StatementNode(String statementType, int line, int column) {
            super("Statement", line, column);
            this.statementType = statementType;
        }

        @Override
        public Object accept(ASTVisitor visitor) {
            return visitor.visitStatement(this);
        }
    }

    public static class LiteralNode extends ASTNode {
        public final Object value;
        public final String literalType;

        public LiteralNode(Object value, String literalType, int line, int column) {
            super("Literal", line, column);
            this.value = value;
            this.literalType = literalType;
        }

        @Override
        public Object accept(ASTVisitor visitor) {
            return visitor.visitLiteral(this);
        }
    }

    public static class IdentifierNode extends ASTNode {
        public final String name;

        public IdentifierNode(String name, int line, int column) {
            super("Identifier", line, column);
            this.name = name;
        }

        @Override
        public Object accept(ASTVisitor visitor) {
            return visitor.visitIdentifier(this);
        }
    }

    public static class BinaryOpNode extends ASTNode {
        public final String operator;
        public ASTNode left;
        public ASTNode right;

        public BinaryOpNode(String operator, ASTNode left, ASTNode right, int line, int column) {
            super("BinaryOp", line, column);
            this.operator = operator;
            this.left = left;
            this.right = right;
        }

        @Override
        public Object accept(ASTVisitor visitor) {
            return visitor.visitBinaryOp(this);
        }
    }

    public static class UnaryOpNode extends ASTNode {
        public final String operator;
        public ASTNode operand;

        public UnaryOpNode(String operator, ASTNode operand, int line, int column) {
            super("UnaryOp", line, column);
            this.operator = operator;
            this.operand = operand;
        }

        @Override
        public Object accept(ASTVisitor visitor) {
            return visitor.visitUnaryOp(this);
        }
    }

    public static class CallNode extends ASTNode {
        public final String functionName;
        public final List<ASTNode> arguments = new ArrayList<>();

        public CallNode(String functionName, int line, int column) {
            super("Call", line, column);
            this.functionName = functionName;
        }

        public void addArgument(ASTNode arg) {
            arguments.add(arg);
        }

        @Override
        public Object accept(ASTVisitor visitor) {
            return visitor.visitCall(this);
        }
    }

    // Parser state
    protected List<Lexer.Token> tokens;
    protected int position;

    /**
     * Parses a token stream into an AST.
     */
    public ProgramNode parse(List<Lexer.Token> tokens) throws ParseException {
        this.tokens = tokens;
        this.position = 0;
        return parseProgram();
    }

    protected ProgramNode parseProgram() throws ParseException {
        ProgramNode program = new ProgramNode();
        while (!isAtEnd()) {
            program.addChild(parseStatement());
        }
        return program;
    }

    protected ASTNode parseStatement() throws ParseException {
        // Default: treat as expression statement
        ASTNode expr = parseExpression();
        if (check("SEMICOLON")) {
            advance();
        }
        return new StatementNode("expression", expr.line, expr.column);
    }

    protected ASTNode parseExpression() throws ParseException {
        return parseAdditive();
    }

    protected ASTNode parseAdditive() throws ParseException {
        ASTNode left = parseMultiplicative();

        while (checkOperator("+", "-")) {
            String op = advance().value;
            ASTNode right = parseMultiplicative();
            left = new BinaryOpNode(op, left, right, left.line, left.column);
        }

        return left;
    }

    protected ASTNode parseMultiplicative() throws ParseException {
        ASTNode left = parseUnary();

        while (checkOperator("*", "/", "%")) {
            String op = advance().value;
            ASTNode right = parseUnary();
            left = new BinaryOpNode(op, left, right, left.line, left.column);
        }

        return left;
    }

    protected ASTNode parseUnary() throws ParseException {
        if (checkOperator("-", "!")) {
            Lexer.Token op = advance();
            ASTNode operand = parseUnary();
            return new UnaryOpNode(op.value, operand, op.line, op.column);
        }
        return parsePrimary();
    }

    protected ASTNode parsePrimary() throws ParseException {
        Lexer.Token token = peek();

        if (check("NUMBER")) {
            advance();
            return new LiteralNode(Double.parseDouble(token.value), "number", token.line, token.column);
        }

        if (check("STRING")) {
            advance();
            String value = token.value.substring(1, token.value.length() - 1);
            return new LiteralNode(value, "string", token.line, token.column);
        }

        if (check("IDENTIFIER")) {
            advance();
            if (check("LPAREN")) {
                return parseCall(token);
            }
            return new IdentifierNode(token.value, token.line, token.column);
        }

        if (check("LPAREN")) {
            advance();
            ASTNode expr = parseExpression();
            expect("RPAREN");
            return expr;
        }

        throw new ParseException("Unexpected token: " + token, token.line, token.column);
    }

    protected CallNode parseCall(Lexer.Token nameToken) throws ParseException {
        CallNode call = new CallNode(nameToken.value, nameToken.line, nameToken.column);
        expect("LPAREN");

        if (!check("RPAREN")) {
            call.addArgument(parseExpression());
            while (check("COMMA")) {
                advance();
                call.addArgument(parseExpression());
            }
        }

        expect("RPAREN");
        return call;
    }

    // Helper methods
    protected boolean isAtEnd() {
        return position >= tokens.size();
    }

    protected Lexer.Token peek() {
        return tokens.get(position);
    }

    protected Lexer.Token advance() {
        return tokens.get(position++);
    }

    protected boolean check(String typeName) {
        if (isAtEnd())
            return false;
        return peek().type.name.equals(typeName);
    }

    protected boolean checkOperator(String... operators) {
        if (!check("OPERATOR"))
            return false;
        String value = peek().value;
        for (String op : operators) {
            if (value.equals(op))
                return true;
        }
        return false;
    }

    protected void expect(String typeName) throws ParseException {
        if (!check(typeName)) {
            Lexer.Token token = isAtEnd() ? null : peek();
            throw new ParseException(
                    "Expected " + typeName + " but got " + (token != null ? token.type.name : "EOF"),
                    token != null ? token.line : -1,
                    token != null ? token.column : -1);
        }
        advance();
    }

    /**
     * Exception thrown during parsing.
     */
    public static class ParseException extends Exception {
        public final int line;
        public final int column;

        public ParseException(String message, int line, int column) {
            super(message + " at " + line + ":" + column);
            this.line = line;
            this.column = column;
        }
    }
}


