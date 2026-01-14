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

package org.jscience.computing.game.muehle;

import org.jscience.computing.game.GameGUI;
import org.jscience.computing.game.JDefaultGame;
import org.jscience.computing.game.Player;

import org.jscience.io.ExtensionFileFilter;

import java.awt.*;

import javax.swing.*;


/**
 * enables MuehleGame to be played with JGameFrame
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.computing.game.JGameFrame
 */
public class JMuehle extends JDefaultGame {
    /** DOCUMENT ME! */
    JMuehleBoard jboard;

    /** DOCUMENT ME! */
    JMuehleListener listener;

    /** DOCUMENT ME! */
    String helpText = "<html>Muehle (Nine Men'helpText Morris) instructions<br><br>" +
        "Player X (role 0) is drawn in red.<br>" +
        "Player O (role 1) is drawn in white.<br>" //+ "To perform any take move while 'putting pieces',<br>"
                                                   //+ "you need to double-click the legal move; to perform<br>"
                                                   //+ "a custom take move while 'moving pieces', do the same.<br>"
         +"The following indicates what number<br>" +
        "represents which position:<br><pre>" +
        " 00----------01----------02\n" + "  |           |           |\n" +
        "  |  08------09------10   |\n" + "  |   |       |       |   |\n" +
        "  |   |  16--17--18   |   |\n" + "  |   |   |       |   |   |\n" +
        " 07--15--23      19--11--03\n" + "  |   |   |       |   |   |\n" +
        "  |   |  22--21--20   |   |\n" + "  |   |       |       |   |\n" +
        "  |  14------13------12   |\n" + "  |           |           |\n" +
        " 06----------05----------04\n" + "</pre>";

/**
     * Creates a new JMuehle object.
     */
    public JMuehle() {
        this(new MuehleGame(), new MuehlePlayer("X"), new MuehlePlayer("O"), 3);
        ((MuehlePlayer) play.getPlayers()[0]).setTracking(true);
        ((MuehlePlayer) play.getPlayers()[1]).setTracking(true);
    }

/**
     * Creates a new JMuehle object.
     *
     * @param game    DOCUMENT ME!
     * @param player1 DOCUMENT ME!
     * @param player2 DOCUMENT ME!
     * @param level   DOCUMENT ME!
     */
    public JMuehle(MuehleGame game, Player player1, Player player2, int level) {
        super(game, new Player[] { player1, player2 }, level,
            new ExtensionFileFilter("mle", "Muehle Files (*.mle)"));
        jboard = new JMuehleBoard(this);
        listener = new JMuehleListener(this);
        jboard.addMouseListener(listener);
        jboard.addMouseMotionListener(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStatusMessage() {
        String s = "next player: ";
        s += ((play.getGame().nextPlayer() == 0) ? "red" : "white");
        s += ("; # of moves: " + play.getGame().getMoveHistory().length);
        s += (", available legal moves: " +
        play.getGame().getLegalMoves().length);
        s += ("; X: " + ((MuehleGame) play.getGame()).piecesLeft(0) +
        " pieces, O: " + ((MuehleGame) play.getGame()).piecesLeft(1) +
        " pieces");

        if (play.getGame().getLegalMoves().length == 0) {
            s += " - Game Over!";
        }

        int[] win = play.getGame().getWinner();

        if (win != null) {
            s += " - Winner:";

            for (int i = 0; i < win.length; i++) {
                s += (" " + play.getPlayer(win[i]).getPlayerName());
            }
        }

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param frame DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Container getContainer(GameGUI frame) {
        this.frame = frame;

        return jboard;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Container getHelp() {
        return new JLabel(helpText);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        return "Muehle";
    }

    /**
     * to gain access to protected member in this package
     *
     * @return DOCUMENT ME!
     */
    GameGUI getFrame() {
        return frame;
    }
}
