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

package org.jscience.tests.computing.ai.expertsystem.queens;

/**
 * Represents a queen in a chess board.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  06 Apr 2000
 */
public class Queen {
    /** The row of the board (1-8). */
    private int row;

    /** The column of the board (1-8). */
    private int column;

/**
     * Class constructor.
     *
     * @param row    the row of the board.
     * @param column the column of the board.
     */
    public Queen(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Returns the row of this queen.
     *
     * @return the row of this queen.
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column of this queen.
     *
     * @return the column of this queen.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Checks whether this queen can be attacked by the given one.
     *
     * @param q queen that tries to attack this one
     *
     * @return <code>true</code> if this queen can be attacked by the given
     *         one; <code>false</code> otherwise.
     */
    public boolean attacks(Queen q) {
        if ((q.getRow() == this.row) || (q.getColumn() == this.column)) {
            return true;
        }

        int x = Math.abs(this.row - q.getRow());
        int y = Math.abs(this.column - q.getColumn());

        return (x == y);
    }

    /**
     * Prints this queen. Useful for debugging.
     */
    public void dump() {
        dump(0);
    }

    /**
     * Prints this queen with the specified identation. Useful for
     * debugging.
     *
     * @param spaces the desired identation
     */
    public void dump(int spaces) {
        for (int i = 0; i < spaces; i++) {
            System.out.print(" ");
        }

        System.out.println(toString());
    }

    /**
     * Returns a string representation of this queen.  Useful for
     * debugging.
     *
     * @return a string representation of this queen.
     */
    public String toString() {
        return ("Queen [row=" + row + ",column=" + column + "]");
    }
}
