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

package org.jscience.computing.ai.expertsystem.compiler.parser;


/*
 * JEOPS - The Java Embedded Object Production System
 * Copyright (c) 2000   Carlos Figueira Filho
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Contact: Carlos Figueira Filho (csff@cin.ufpe.br)
 */
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;


/**
 * Class used in the parsing of the rules file. It's the first step in the
 * converting of the rules into Java classes.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  04.01.2000
 */
public class Scanner implements TokenConstants {
    /** The list of reserved words. */
    private static final String[] reservedWords = {
            "ruleBase", "rule", "extends", "declarations", "preconditions",
            "actions", "import", "localdecl", "public", "conditions", "package",
            "implements"
        };

    /** The token types of the reserved words. */
    private static final int[] typeReservedWords = {
            RULE_BASE, RULE, EXTENDS, DECLARATIONS, CONDITIONS, ACTIONS, IMPORT,
            LOCALDECL, PUBLIC, CONDITIONS, PACKAGE, IMPLEMENTS
        };

    /** The current token. */
    private Token currentToken;

    /** The name of the rules file. */
    private String ruleFileName;

    /** The reader used to get the characters from the rules file. */
    private BufferedReader reader;

    /** The string buffer used in the scanning of the file. */
    private StringBuffer buffer;

    /** The last char read from the file. */
    private char lastChar;

    /** The line of the first character in the current token. */
    private int tokenLine = 1;

    /** The current line from the file. */
    private int currentLine = 1;

    /** The column of the first character in the current token. */
    private int tokenColumn = 1;

    /** The current column from the file. */
    private int currentColumn = 1;

    /**
     * The last non-whitespace token. A whitespace token is one of type
     * COMMENT or WHITE_SPACE.
     */
    private Token lastNonWhiteSpaceToken;

/**
     * Class constructor.
     *
     * @param ruleFileName the name of the rules file.
     * @throws IOException if some IO error occurs.
     */
    public Scanner(String ruleFileName) throws IOException {
        this.ruleFileName = ruleFileName;

        FileReader fr = new FileReader(ruleFileName);
        reader = new BufferedReader(fr);
        buffer = new StringBuffer();
        lastNonWhiteSpaceToken = null;
        readNextChar();
    }

    /**
     * Checks whether a given identifier is a reserved word.
     *
     * @param id the identifier to be checked.
     *
     * @return the token type relative to the given word, if it's a reserved
     *         word, or <code>-1</code> if it's not a reserved word.
     */
    private int checkReserved(String id) {
        int result = -1;

        for (int i = 0; (result == -1) && (i < reservedWords.length); i++) {
            if (id.equals(reservedWords[i])) {
                result = typeReservedWords[i];
            }
        }

        return result;
    }

    /**
     * Returns the current column from the file.
     *
     * @return the current column from the file.
     */
    public int getCurrentColumn() {
        return tokenColumn;
    }

    /**
     * Returns the current line from the file.
     *
     * @return the current line from the file.
     */
    public int getCurrentLine() {
        return tokenLine;
    }

    /**
     * Returns the last non-whitespace token. A whitespace token is one
     * whose type is COMMENT or WHITE_SPACE.
     *
     * @return the last non-whitespace token.
     */
    public Token getLastNonWhiteSpaceToken() {
        return lastNonWhiteSpaceToken;
    }

    /**
     * Returns a token that can be an identifier or a reserved word.
     *
     * @param str the string that represents either an identifier or a reserved
     *        word.
     *
     * @return a token that can be an identifier or a reserved word.
     */
    private Token idOrReserved(String str) {
        int i = checkReserved(str);

        if (i == -1) {
            return new Token(IDENT, str);
        } else {
            return new Token(i, str);
        }
    }

    /**
     * Test method for this class.
     *
     * @param args command-line arguments. None is needed, but one can pass the
     *        rule file name for the scanning.
     */
    public static void main(String[] args) {
        try {
            String fileName = "transportes.rules";

            if (args.length > 0) {
                fileName = args[0];
            }

            Scanner s = new Scanner(fileName);
            Token t = null;

            do {
                t = s.nextToken();
                System.out.println(t.toString());

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            } while (t.getTokenType() != EOF);
        } catch (IOException e) {
        }
    }

    /**
     * Returns the next token from the file.
     *
     * @return the next token from the file.
     *
     * @throws IOException if some IO error occurs.
     */
    public Token nextToken() throws IOException {
        tokenLine = currentLine;
        tokenColumn = currentColumn;

        if ((currentToken != null) && (currentToken.getTokenType() != COMMENT) &&
                (currentToken.getTokenType() != WHITE_SPACE)) {
            lastNonWhiteSpaceToken = currentToken;
        }

        if ((currentToken == null) || (currentToken.getTokenType() != EOF)) {
            if (Character.isWhitespace(lastChar)) {
                buffer.setLength(0);

                do {
                    buffer.append(lastChar);

                    try {
                        readNextChar();
                    } catch (EOFException e) {
                        currentToken = Token.EOF_TOKEN;

                        return new Token(WHITE_SPACE, buffer.toString());
                    }
                } while (Character.isWhitespace(lastChar));

                currentToken = new Token(WHITE_SPACE, buffer.toString());
            } else if (lastChar == '/') { // It may be the start of a comment

                try {
                    readNextChar();
                } catch (EOFException e) {
                    currentToken = Token.EOF_TOKEN;

                    return new Token(ERROR,
                        "/ unexpected at line " + currentLine + ", column " +
                        currentColumn);
                }

                if (lastChar == '/') { // we're in a single-line comment
                    buffer.setLength(0);
                    buffer.append('/');

                    do {
                        buffer.append(lastChar);

                        try {
                            readNextChar();
                        } catch (EOFException e) {
                            currentToken = Token.EOF_TOKEN;

                            return new Token(COMMENT, buffer.toString());
                        }
                    } while (lastChar != '\n');

                    currentToken = new Token(COMMENT, buffer.toString());
                } else if (lastChar == '*') { // We're in a multiline comment.
                    buffer.setLength(0);
                    buffer.append('/');

                    boolean endComment = false;

                    do {
                        buffer.append(lastChar);

                        try {
                            readNextChar();

                            if (lastChar == '*') {
                                buffer.append(lastChar);
                            }
                        } catch (EOFException e) {
                            currentToken = Token.EOF_TOKEN;

                            return new Token(COMMENT, buffer.toString());
                        }

                        while (lastChar == '*') { // It may be the end...

                            try {
                                readNextChar();

                                if (lastChar == '*') {
                                    buffer.append(lastChar);
                                } else if (lastChar == '/') { // It's the end
                                    buffer.append(lastChar);
                                    endComment = true;
                                }
                            } catch (EOFException e) {
                                currentToken = Token.EOF_TOKEN;

                                return new Token(ERROR,
                                    "Unclosed comments at line" + currentLine +
                                    ", column " + currentColumn);
                            }
                        }
                    } while (!endComment);

                    try {
                        readNextChar();
                    } catch (EOFException e) {
                        currentToken = Token.EOF_TOKEN;

                        return new Token(COMMENT, buffer.toString());
                    }

                    currentToken = new Token(COMMENT, buffer.toString());
                } else { // It's not a comment, it's something else
                    currentToken = new Token(NONE_ABOVE, "/");
                }
            } else if (lastChar == '{') {
                currentToken = new Token(OPEN_CURLY_BRACKET, "{");

                try {
                    readNextChar();
                } catch (EOFException e) {
                    currentToken = Token.EOF_TOKEN;

                    return new Token(OPEN_CURLY_BRACKET, "{");
                }
            } else if (lastChar == '}') {
                currentToken = new Token(CLOSE_CURLY_BRACKET, "}");

                try {
                    readNextChar();
                } catch (EOFException e) {
                    currentToken = Token.EOF_TOKEN;

                    return new Token(CLOSE_CURLY_BRACKET, "}");
                }
            } else if (lastChar == ';') {
                currentToken = new Token(SEMICOLON, ";");

                try {
                    readNextChar();
                } catch (EOFException e) {
                    currentToken = Token.EOF_TOKEN;

                    return new Token(SEMICOLON, ";");
                }
            } else if (lastChar == ',') {
                currentToken = new Token(COMMA, ",");

                try {
                    readNextChar();
                } catch (EOFException e) {
                    currentToken = Token.EOF_TOKEN;

                    return new Token(COMMA, ",");
                }
            } else if (lastChar == '=') {
                currentToken = new Token(EQUALS, "=");

                try {
                    readNextChar();
                } catch (EOFException e) {
                    currentToken = Token.EOF_TOKEN;

                    return new Token(EQUALS, "=");
                }
            } else if (lastChar == '.') {
                currentToken = new Token(DOT, ".");

                try {
                    readNextChar();
                } catch (EOFException e) {
                    currentToken = Token.EOF_TOKEN;

                    return new Token(DOT, ".");
                }
            } else if (lastChar == '*') {
                currentToken = new Token(ASTERISK, "*");

                try {
                    readNextChar();
                } catch (EOFException e) {
                    currentToken = Token.EOF_TOKEN;

                    return new Token(ASTERISK, "*");
                }
            } else if (Character.isJavaIdentifierStart(lastChar)) {
                buffer.setLength(0);

                do {
                    buffer.append(lastChar);

                    try {
                        readNextChar();
                    } catch (EOFException e) {
                        currentToken = Token.EOF_TOKEN;

                        return idOrReserved(buffer.toString());
                    }
                } while (Character.isJavaIdentifierPart(lastChar));

                currentToken = idOrReserved(buffer.toString());
            } else if (lastChar == '\"') { // it's a string
                buffer.setLength(0);

                boolean endString = false;
                boolean ignoreNext = false;

                do {
                    buffer.append(lastChar);

                    try {
                        readNextChar();

                        if (lastChar == '\n') {
                            currentToken = Token.EOF_TOKEN;

                            return new Token(ERROR,
                                "String not terminated at end of line.");
                        }

                        if (!ignoreNext) {
                            if (lastChar == '\\') {
                                ignoreNext = true;
                            } else if (lastChar == '\"') {
                                endString = true;
                            }
                        } else {
                            ignoreNext = false;
                        }
                    } catch (EOFException e) {
                        currentToken = Token.EOF_TOKEN;

                        return new Token(ERROR,
                            "String not terminated at end of input");
                    }
                } while (!endString);

                buffer.append(lastChar);

                try {
                    readNextChar();
                } catch (EOFException e) {
                    currentToken = Token.EOF_TOKEN;

                    return new Token(STRING, buffer.toString());
                }

                currentToken = new Token(STRING, buffer.toString());
            } else { // It's none of the above...
                buffer.setLength(0);

                do {
                    buffer.append(lastChar);

                    try {
                        readNextChar();
                    } catch (EOFException e) {
                        currentToken = Token.EOF_TOKEN;

                        return new Token(NONE_ABOVE, buffer.toString());
                    }
                } while (!Character.isWhitespace(lastChar) &&
                        !Character.isJavaIdentifierStart(lastChar) &&
                        (lastChar != '{') && (lastChar != '}') &&
                        (lastChar != '\"') && (lastChar != '/') &&
                        (lastChar != ',') && (lastChar != '=') &&
                        (lastChar != '.') && (lastChar != '*') &&
                        (lastChar != ';'));

                currentToken = new Token(NONE_ABOVE, buffer.toString());
            }
        }

        return currentToken;
    }

    /**
     * Reads the next char in the file, storing it in "lastChar".
     *
     * @throws IOException if some IO error occurs.
     * @throws EOFException DOCUMENT ME!
     */
    private void readNextChar() throws IOException {
        lastChar = (char) reader.read();

        if (lastChar == '\n') {
            currentColumn = 1;
            currentLine++;
        } else if (lastChar != '\r') {
            currentColumn++;
        }

        if (lastChar == '\uFFFF') {
            throw new EOFException();
        }
    }
}
