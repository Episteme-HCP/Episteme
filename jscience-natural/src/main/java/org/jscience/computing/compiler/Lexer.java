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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple lexer (tokenizer) for DSL processing.
 * <p>
 * Converts input text into a stream of tokens based on configurable patterns.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Lexer {

    /**
     * Represents a token type with its pattern.
     */
    public static class TokenType {
        public final String name;
        public final Pattern pattern;
        public final boolean skip;

        public TokenType(String name, String regex) {
            this(name, regex, false);
        }

        public TokenType(String name, String regex, boolean skip) {
            this.name = name;
            this.pattern = Pattern.compile("^(" + regex + ")");
            this.skip = skip;
        }

        // Built-in token types
        public static final TokenType WHITESPACE = new TokenType("WHITESPACE", "\\s+", true);
        public static final TokenType NUMBER = new TokenType("NUMBER", "-?\\d+(\\.\\d+)?([eE][+-]?\\d+)?");
        public static final TokenType STRING = new TokenType("STRING", "\"([^\"\\\\]|\\\\.)*\"");
        public static final TokenType IDENTIFIER = new TokenType("IDENTIFIER", "[a-zA-Z_][a-zA-Z0-9_]*");
        public static final TokenType OPERATOR = new TokenType("OPERATOR", "[+\\-*/=<>!&|^%]+");
        public static final TokenType LPAREN = new TokenType("LPAREN", "\\(");
        public static final TokenType RPAREN = new TokenType("RPAREN", "\\)");
        public static final TokenType LBRACE = new TokenType("LBRACE", "\\{");
        public static final TokenType RBRACE = new TokenType("RBRACE", "\\}");
        public static final TokenType LBRACKET = new TokenType("LBRACKET", "\\[");
        public static final TokenType RBRACKET = new TokenType("RBRACKET", "\\]");
        public static final TokenType COMMA = new TokenType("COMMA", ",");
        public static final TokenType SEMICOLON = new TokenType("SEMICOLON", ";");
        public static final TokenType COLON = new TokenType("COLON", ":");
        public static final TokenType DOT = new TokenType("DOT", "\\.");
        public static final TokenType COMMENT = new TokenType("COMMENT", "//[^\\n]*|/\\*.*?\\*/", true);
    }

    /**
     * Represents a single token.
     */
    public static class Token {
        public final TokenType type;
        public final String value;
        public final int line;
        public final int column;

        public Token(TokenType type, String value, int line, int column) {
            this.type = type;
            this.value = value;
            this.line = line;
            this.column = column;
        }

        @Override
        public String toString() {
            return String.format("Token(%s, '%s', %d:%d)", type.name, value, line, column);
        }
    }

    private final List<TokenType> tokenTypes;

    /**
     * Creates a lexer with default token types.
     */
    public Lexer() {
        this.tokenTypes = new ArrayList<>();
        addDefaultTokenTypes();
    }

    /**
     * Creates a lexer with custom token types.
     */
    public Lexer(List<TokenType> tokenTypes) {
        this.tokenTypes = new ArrayList<>(tokenTypes);
    }

    private void addDefaultTokenTypes() {
        tokenTypes.add(TokenType.WHITESPACE);
        tokenTypes.add(TokenType.COMMENT);
        tokenTypes.add(TokenType.NUMBER);
        tokenTypes.add(TokenType.STRING);
        tokenTypes.add(TokenType.IDENTIFIER);
        tokenTypes.add(TokenType.OPERATOR);
        tokenTypes.add(TokenType.LPAREN);
        tokenTypes.add(TokenType.RPAREN);
        tokenTypes.add(TokenType.LBRACE);
        tokenTypes.add(TokenType.RBRACE);
        tokenTypes.add(TokenType.LBRACKET);
        tokenTypes.add(TokenType.RBRACKET);
        tokenTypes.add(TokenType.COMMA);
        tokenTypes.add(TokenType.SEMICOLON);
        tokenTypes.add(TokenType.COLON);
        tokenTypes.add(TokenType.DOT);
    }

    /**
     * Adds a custom token type (higher priority than defaults).
     */
    public void addTokenType(TokenType type) {
        tokenTypes.add(0, type); // Add at beginning for priority
    }

    /**
     * Tokenizes the input string.
     *
     * @param input Source code to tokenize
     * @return List of tokens
     * @throws LexerException if unrecognized characters are found
     */
    public List<Token> tokenize(String input) throws LexerException {
        List<Token> tokens = new ArrayList<>();
        int pos = 0;
        int line = 1;
        int lineStart = 0;

        while (pos < input.length()) {
            boolean matched = false;

            for (TokenType type : tokenTypes) {
                Matcher matcher = type.pattern.matcher(input.substring(pos));
                if (matcher.find()) {
                    String value = matcher.group(1);

                    if (!type.skip) {
                        tokens.add(new Token(type, value, line, pos - lineStart + 1));
                    }

                    // Update position and line tracking
                    for (char c : value.toCharArray()) {
                        if (c == '\n') {
                            line++;
                            lineStart = pos + 1;
                        }
                        pos++;
                    }

                    matched = true;
                    break;
                }
            }

            if (!matched) {
                char c = input.charAt(pos);
                throw new LexerException(
                        String.format("Unexpected character '%c' at line %d, column %d",
                                c, line, pos - lineStart + 1));
            }
        }

        return tokens;
    }

    /**
     * Exception thrown when lexer encounters invalid input.
     */
    public static class LexerException extends Exception {
        public LexerException(String message) {
            super(message);
        }
    }
}


