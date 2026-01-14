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
/**
 * Class used to store the tokens read from the rules file. It's returned
 * by the scanner to the parser of the rules.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  04.01.2000
 */
public class Token implements TokenConstants {
    /** The EOF token. */
    public static final Token EOF_TOKEN = new Token(EOF, "");

    /**
     * The type of this token.
     *
     * @see org.jscience.computing.ai.expertsystem.compiler.parser.TokenConstants
     */
    private int tokenType;

    /** The lexeme associated with this token. */
    private String lexeme;

/**
     * Class constructor.
     *
     * @param type   the type of this token.
     * @param lexeme the lexeme associated with this token.
     */
    public Token(int type, String lexeme) {
        this.tokenType = type;
        this.lexeme = lexeme;
    }

    /**
     * Returns the lexeme associated with this token.
     *
     * @return the lexeme associated with this token.
     */
    public String getLexeme() {
        return this.lexeme;
    }

    /**
     * Returns the type of this token.
     *
     * @return the type of this token.
     *
     * @see org.jscience.computing.ai.expertsystem.compiler.parser.TokenConstants
     */
    public int getTokenType() {
        return this.tokenType;
    }

    /**
     * Returns a string representing this token. Useful for debugging.
     *
     * @return a string representing this token.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        switch (tokenType) {
        case COMMENT:
            sb.append("COMMENT: ");

            break;

        case OPEN_CURLY_BRACKET:
            sb.append("OPEN_CURLY_BRACKET: ");

            break;

        case CLOSE_CURLY_BRACKET:
            sb.append("CLOSE_CURLY_BRACKET: ");

            break;

        case IDENT:
            sb.append("IDENT: ");

            break;

        case WHITE_SPACE:
            sb.append("WHITE_SPACE: ");

            break;

        case EOF:
            sb.append("EOF: ");

            break;

        case ERROR:
            sb.append("ERROR: ");

            break;

        case STRING:
            sb.append("STRING: ");

            break;

        case RULE_BASE:
            sb.append("RULE_BASE: ");

            break;

        case RULE:
            sb.append("RULE: ");

            break;

        case EXTENDS:
            sb.append("EXTENDS: ");

            break;

        case DECLARATIONS:
            sb.append("DECLARATIONS: ");

            break;

        case SEMICOLON:
            sb.append("SEMICOLON: ");

            break;

        case COMMA:
            sb.append("COMMA: ");

            break;

        case CONDITIONS:
            sb.append("CONDITIONS: ");

            break;

        case ACTIONS:
            sb.append("ACTIONS: ");

            break;

        case IMPORT:
            sb.append("IMPORT: ");

            break;

        case DOT:
            sb.append("DOT: ");

            break;

        case ASTERISK:
            sb.append("ASTERISK: ");

            break;

        case LOCALDECL:
            sb.append("LOCALDECL: ");

            break;

        case EQUALS:
            sb.append("EQUALS: ");

            break;

        case PUBLIC:
            sb.append("PUBLIC: ");

            break;

        case PACKAGE:
            sb.append("PACKAGE: ");

            break;

        case IMPLEMENTS:
            sb.append("IMPLEMENTS: ");

            break;

        default:
            sb.append("NONE_ABOVE: ");

            break;
        }

        sb.append(lexeme);

        return sb.toString();
    }
}
