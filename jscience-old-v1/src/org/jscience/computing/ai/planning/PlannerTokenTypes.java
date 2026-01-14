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

// $ANTLR 2.7.6 (2005-12-22): "Planner.g" -> "PlannerLexer.java"$
package org.jscience.computing.ai.planning;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public interface PlannerTokenTypes {
    /** DOCUMENT ME! */
    int EOF = 1;

    /** DOCUMENT ME! */
    int NULL_TREE_LOOKAHEAD = 3;

    /** DOCUMENT ME! */
    int LP = 4;

    /** DOCUMENT ME! */
    int DEFPROBLEM = 5;

    /** DOCUMENT ME! */
    int ID = 6;

    /** DOCUMENT ME! */
    int RP = 7;

    /** DOCUMENT ME! */
    int NIL = 8;

    /** DOCUMENT ME! */
    int DEFPROBLEMSET = 9;

    /** DOCUMENT ME! */
    int DEFDOMAIN = 10;

    /** DOCUMENT ME! */
    int METHOD = 11;

    /** DOCUMENT ME! */
    int OPERATOR = 12;

    /** DOCUMENT ME! */
    int OPID = 13;

    /** DOCUMENT ME! */
    int VARID = 14;

    /** DOCUMENT ME! */
    int PROTECTION = 15;

    /** DOCUMENT ME! */
    int FORALL = 16;

    /** DOCUMENT ME! */
    int AXIOM = 17;

    /** DOCUMENT ME! */
    int UNORDERED = 18;

    /** DOCUMENT ME! */
    int IMMEDIATE = 19;

    /** DOCUMENT ME! */
    int FIRST = 20;

    /** DOCUMENT ME! */
    int SORT = 21;

    /** DOCUMENT ME! */
    int AND = 22;

    /** DOCUMENT ME! */
    int OR = 23;

    /** DOCUMENT ME! */
    int NOT = 24;

    /** DOCUMENT ME! */
    int IMPLY = 25;

    /** DOCUMENT ME! */
    int ASSIGN = 26;

    /** DOCUMENT ME! */
    int CALL = 27;

    /** DOCUMENT ME! */
    int NUM = 28;

    /** DOCUMENT ME! */
    int DOT = 29;

    /** DOCUMENT ME! */
    int DIV = 30;

    /** DOCUMENT ME! */
    int EQUAL = 31;

    /** DOCUMENT ME! */
    int LESS = 32;

    /** DOCUMENT ME! */
    int LESSEQ = 33;

    /** DOCUMENT ME! */
    int MEMBER = 34;

    /** DOCUMENT ME! */
    int MINUS = 35;

    /** DOCUMENT ME! */
    int MORE = 36;

    /** DOCUMENT ME! */
    int MOREEQ = 37;

    /** DOCUMENT ME! */
    int MULT = 38;

    /** DOCUMENT ME! */
    int NOTEQ = 39;

    /** DOCUMENT ME! */
    int PLUS = 40;

    /** DOCUMENT ME! */
    int POWER = 41;

    /** DOCUMENT ME! */
    int STDLIB = 42;

    /** DOCUMENT ME! */
    int WS = 43;

    /** DOCUMENT ME! */
    int COMMENT = 44;
}
