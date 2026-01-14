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
 * Interface used to define the constants used by the tokenization
 * process of the scanning/parser of the rule files.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  04.01.2000
 */
public interface TokenConstants {
    /** Constant used to indicate that the token type is a comment. */
    public static final int COMMENT = 1;

    /**
     * Constant used to indicate that the token type is an open curly
     * bracket.
     */
    public static final int OPEN_CURLY_BRACKET = 2;

    /**
     * Constant used to indicate that the token type is a close curly
     * bracket.
     */
    public static final int CLOSE_CURLY_BRACKET = 3;

    /** Constant used to indicate that the token type is an identifier. */
    public static final int IDENT = 4;

    /** Constant used to indicate that the token type is white spaces. */
    public static final int WHITE_SPACE = 5;

    /** Constant used to indicate that the token type is EOF. */
    public static final int EOF = 6;

    /** Constant used to indicate that the token type is an error. */
    public static final int ERROR = 7;

    /** Constant used to indicate that the token type is a string constant. */
    public static final int STRING = 8;

    /**
     * Constant used to indicate that the token type is the reserved
     * word <code>ruleBase</code>.
     */
    public static final int RULE_BASE = 9;

    /**
     * Constant used to indicate that the token type is the reserved
     * word <code>rule</code>.
     */
    public static final int RULE = 10;

    /**
     * Constant used to indicate that the token type is the reserved
     * word <code>extends</code>.
     */
    public static final int EXTENDS = 11;

    /**
     * Constant used to indicate that the token type is the reserved
     * word <code>declarations</code>.
     */
    public static final int DECLARATIONS = 12;

    /** Constant used to indicate that the token type is a semicolon. */
    public static final int SEMICOLON = 13;

    /** Constant used to indicate that the token type is a comma. */
    public static final int COMMA = 14;

    /**
     * Constant used to indicate that the token type is the reserved
     * word <code>conditions</code>.
     */
    public static final int CONDITIONS = 15;

    /**
     * Constant used to indicate that the token type is the reserved
     * word <code>actions</code>.
     */
    public static final int ACTIONS = 16;

    /**
     * Constant used to indicate that the token type is the reserved
     * word <code>import</code>.
     */
    public static final int IMPORT = 17;

    /** Constant used to indicate that the token type is a dot. */
    public static final int DOT = 18;

    /** Constant used to indicate that the token type is an asterisk. */
    public static final int ASTERISK = 19;

    /**
     * Constant used to indicate that the token type is the reserved
     * word <code>localdecl</code>.
     */
    public static final int LOCALDECL = 20;

    /** Constant used to indicate that the token type is an equals sign. */
    public static final int EQUALS = 21;

    /**
     * Constant used to indicate that the token type is the reserved
     * word <code>public</code>.
     */
    public static final int PUBLIC = 22;

    /**
     * Constant used to indicate that the token type is the reserved
     * word <code>package</code>.
     */
    public static final int PACKAGE = 23;

    /**
     * Constant used to indicate that the token type is the reserved
     * word <code>implements</code>.
     */
    public static final int IMPLEMENTS = 24;

    /** Constant used to indicate that the token type is none of the above. */
    public static final int NONE_ABOVE = 999;
}
