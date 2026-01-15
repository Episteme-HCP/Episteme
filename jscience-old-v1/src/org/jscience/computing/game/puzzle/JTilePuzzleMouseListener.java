/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
