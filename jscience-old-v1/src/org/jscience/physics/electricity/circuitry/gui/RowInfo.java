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

// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.gui;


// info about each row/column of the matrix for simplification purposes
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class RowInfo {
    /**
     * DOCUMENT ME!
     */
    public static final int ROW_NORMAL = 0; // ordinary value

    /**
     * DOCUMENT ME!
     */
    public static final int ROW_CONST = 1; // value is constant

    /**
     * DOCUMENT ME!
     */
    public static final int ROW_EQUAL = 2; // value is equal to another value

    /**
     * DOCUMENT ME!
     */
    public int nodeEq;

    /**
     * DOCUMENT ME!
     */
    public int type;

    /**
     * DOCUMENT ME!
     */
    public int mapCol;

    /**
     * DOCUMENT ME!
     */
    public int mapRow;

    /**
     * DOCUMENT ME!
     */
    public double value;

    /**
     * DOCUMENT ME!
     */
    public boolean rsChanges; // row's right side changes

    /**
     * DOCUMENT ME!
     */
    public boolean lsChanges; // row's left side changes

    /**
     * DOCUMENT ME!
     */
    public boolean dropRow; // row is not needed in matrix

    /**
     * Creates a new RowInfo object.
     */
    public RowInfo() {
        type = ROW_NORMAL;
    }
}
