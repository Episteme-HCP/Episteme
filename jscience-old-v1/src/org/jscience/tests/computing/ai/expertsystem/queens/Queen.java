/*
 * org.jscience.tests.computing.ai.expertsystem - The Java Embedded Object Production System
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
