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

package org.jscience.computing.game.puzzle;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class JTilePuzzleMouseListener extends MouseAdapter {
    /** DOCUMENT ME! */
    JTilePuzzle jplay;

/**
     * Creates a new JTilePuzzleMouseListener object.
     *
     * @param jplay DOCUMENT ME!
     */
    JTilePuzzleMouseListener(JTilePuzzle jplay) {
        this.jplay = jplay;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ev DOCUMENT ME!
     */
    public void mouseClicked(MouseEvent ev) {
        Point point = ev.getPoint();
        JTable table = (JTable) jplay.jgame;
        int column = table.columnAtPoint(point);
        int row = table.rowAtPoint(point);

        //TilePuzzleMove[] moves = (TilePuzzleMove[]) jplay.game.getLegalMoves();
        //TilePuzzleMove move = null;
        Object[][] puzzle = jplay.game.getPuzzleMatrix();

        try {
            if (puzzle[row][column - 1] == null) {
                jplay.getFrame()
                     .requestGUIMove(new TilePuzzleMove(TilePuzzleMove.LEFT));

                return;
            }
        } catch (ArrayIndexOutOfBoundsException ignore) {
        }

        try {
            if (puzzle[row][column + 1] == null) {
                jplay.getFrame()
                     .requestGUIMove(new TilePuzzleMove(TilePuzzleMove.RIGHT));

                return;
            }
        } catch (ArrayIndexOutOfBoundsException ignore) {
        }

        try {
            if (puzzle[row - 1][column] == null) {
                jplay.getFrame()
                     .requestGUIMove(new TilePuzzleMove(TilePuzzleMove.UP));

                return;
            }
        } catch (ArrayIndexOutOfBoundsException ignore) {
        }

        try {
            if (puzzle[row + 1][column] == null) {
                jplay.getFrame()
                     .requestGUIMove(new TilePuzzleMove(TilePuzzleMove.DOWN));

                return;
            }
        } catch (ArrayIndexOutOfBoundsException ignore) {
        }
    }
}
