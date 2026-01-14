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

package org.jscience.computing.game.checkers;

import org.jscience.computing.game.GameGUI;
import org.jscience.computing.game.JDefaultGame;
import org.jscience.computing.game.Player;

import org.jscience.io.ExtensionFileFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * implements the GUI wrapper around CheckersGame to use with JGameFrame
 *
 * @author Holger Antelmann
 */
public class JCheckers extends JDefaultGame implements ActionListener {
    /** DOCUMENT ME! */
    JCheckBoxMenuItem flipped;

    /** DOCUMENT ME! */
    JCheckersBoard jboard;

    /** DOCUMENT ME! */
    JCheckersListener listener;

    /** DOCUMENT ME! */
    String helpText = "<html>Checkers instructions<br><br>" +
        "Player X is red (role 0); Player O is white (role 1).<br>" +
        "You can make a move by dragging the piece on the board.<br>" +
        "To understand the positions displayed by<br>" +
        "the moves available, keep the following in mind:<br>" +
        "the first digit refers to the column and the second<br>" +
        "digit refers to the row.<br>" +
        "In each case, counting beginns in the lower left corner,<br>" +
        "i.e. the position 11 is at the left bottom, 88 is at<br>" +
        "the right top and 18 would be at the right bottom.<br>";

/**
     * Creates a new JCheckers object.
     */
    public JCheckers() {
        this(new CheckersGame(), new CheckersPlayer("X"),
            new CheckersPlayer("O"), 6);
        ((CheckersPlayer) play.getPlayers()[0]).setTracking(true);
        ((CheckersPlayer) play.getPlayers()[1]).setTracking(true);
        ((CheckersPlayer) play.getPlayers()[0]).setRandomSeed(System.currentTimeMillis());
        ((CheckersPlayer) play.getPlayers()[1]).setRandomSeed(System.currentTimeMillis());
    }

/**
     * Creates a new JCheckers object.
     *
     * @param game    DOCUMENT ME!
     * @param player1 DOCUMENT ME!
     * @param player2 DOCUMENT ME!
     * @param level   DOCUMENT ME!
     */
    public JCheckers(CheckersGame game, Player player1, Player player2,
        int level) {
        super(game, new Player[] { player1, player2 }, level,
            new ExtensionFileFilter("chks", "Checkers game (*.chks)"));
        jboard = new JCheckersBoard(this);
        listener = new JCheckersListener(this);
        jboard.addMouseListener(listener);
        jboard.addComponentListener(new CheckerSizer(this));
        jboard.addMouseMotionListener(listener);
        flipped = new JCheckBoxMenuItem("Flip Board");
        flipped.addActionListener(this);
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
    public JMenu getMenu() {
        JMenu specialMenu = new JMenu("Checkers");
        JMenuItem item;
        specialMenu.add(flipped);

        return specialMenu;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Flip Board")) {
            frame.say("Menu Flip Board selected");
            jboard.repaint();

            return;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    GameGUI getFrame() {
        return frame;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStatusMessage() {
        CheckersGame game = (CheckersGame) play.getGame();
        String s = "next: ";
        s += ((play.getGame().nextPlayer() == 0) ? "red" : "white");
        s += ("; " + super.getStatusMessage());
        s += (" (kingsX: " + game.getBoard().getKingCount(0));
        s += (", kingsO: " + game.getBoard().getKingCount(1));
        s += ("; totalX: " + game.getBoard().getPieceCount(0));
        s += (", totalO: " + game.getBoard().getPieceCount(1) + ")");

        return s;
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
    boolean isFlipped() {
        return flipped.getState();
    }
}
