/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.gomoku;

import org.jscience.computing.game.*;

import org.jscience.io.ExtensionFileFilter;

import java.awt.*;

import javax.swing.*;


/**
 * GUI wrapper around GomokuGame for using a JGameFrame
 *
 * @author Holger Antelmann
 */
public class JGomoku extends JDefaultGame {
    /** DOCUMENT ME! */
    JGomokuPanel panel;

/**
     * Creates a new JGomoku object.
     */
    public JGomoku() {
        this(new GomokuGame(), new GomokuPlayer("X"), new GomokuPlayer("O"), 2);
    }

/**
     * Creates a new JGomoku object.
     *
     * @param game    DOCUMENT ME!
     * @param player1 DOCUMENT ME!
     * @param player2 DOCUMENT ME!
     * @param level   DOCUMENT ME!
     */
    public JGomoku(GomokuGame game, Player player1, Player player2, int level) {
        super(game, new Player[] { player1, player2 }, level,
            new ExtensionFileFilter("gmku", "Go-moku games (*.gmku)"));
        panel = new JGomokuPanel(this);
        panel.addMouseListener(new JGomokuListener(this));
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

        return panel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JGamePlay getNewGame() {
        int oldWidth = ((GomokuGame) play.getGame()).getWidth();
        int oldHeight = ((GomokuGame) play.getGame()).getHeight();
        String s1 = JOptionPane.showInputDialog(frame.getFrame(), "select width");

        if (s1 == null) {
            frame.say("selected numbers were not valid; old game is maintained");

            return this;
        }

        String s2 = JOptionPane.showInputDialog(frame.getFrame(),
                "select height");

        if (s2 == null) {
            frame.say("selected numbers were not valid; old game is maintained");

            return this;
        }

        int width = 0;
        int height = 0;

        try {
            width = Integer.parseInt(s1);
            height = Integer.parseInt(s2);
        } catch (NumberFormatException ex) {
            frame.say(
                "selected numbers were not valid; old game values are maintained");
        }

        if (width == 0) {
            width = oldWidth;
        }

        if (height == 0) {
            height = oldHeight;
        }

        play = new GameDriver(new GomokuGame("Gomoku variant", width, height),
                play.getPlayers(), play.getLevel());
        panel.setTileSize(panel.tileSize, panel.tileInset);

        if ((oldWidth != ((GomokuGame) play.getGame()).getWidth()) ||
                (oldHeight != ((GomokuGame) play.getGame()).getHeight())) {
            frame.repaint();
        }

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStatusMessage() {
        String s = "next player: ";
        s += ((play.getGame().nextPlayer() == 0) ? "black" : "white");
        s += ("; " + super.getStatusMessage());

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Container getHelp() {
        String s = "<html>Go-moku game help<br><br>" +
            "You can make a move by simply clicking on the grid.<br>" +
            "Player role 0 is black and player role 1 is white.<br>";

        return new JLabel(s);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    GameGUI getFrame() {
        return frame;
    }
}
