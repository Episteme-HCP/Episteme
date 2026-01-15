/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

import org.jscience.io.ExtensionFileFilter;

import java.awt.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;


/**
 * JDefaultGame provides a basic wrapper for GamePlay objects for
 * implementing the JGamePlay interface. JDefaultGame enables any GamePlay to
 * be played with a JGameFrame if the game has no specific requirements for
 * its display (or you can extend this class to ease the implementation of the
 * interface). JDefaultGame relies on proper toString() methods to be set for
 * the game and its moves; it further requires that a default constructor with
 * no arguments is accessible from this class. Also, the GamePlay and all its
 * associated objects must be serializable for game saving/loading.
 *
 * @author Holger Antelmann
 *
 * @see JGameFrame
 * @see JGamePlay
 */
public class JDefaultGame implements JGamePlay {
    /** DOCUMENT ME! */
    protected GameGUI frame;

    /** DOCUMENT ME! */
    protected AutoPlay play;

    /** DOCUMENT ME! */
    protected ExtensionFileFilter filter;

/**
     * Creates a new JDefaultGame object.
     *
     * @param game DOCUMENT ME!
     */
    public JDefaultGame(GamePlay game) {
        this(game, getDefaultPlayers(game), 1,
            new ExtensionFileFilter("game", "Games (*.game)"));
    }

/**
     * Creates a new JDefaultGame object.
     *
     * @param game   DOCUMENT ME!
     * @param player DOCUMENT ME!
     * @param level  DOCUMENT ME!
     * @param filter DOCUMENT ME!
     */
    public JDefaultGame(GamePlay game, Player[] player, int level,
        ExtensionFileFilter filter) {
        play = new GameDriver(game, player, level);
        this.filter = filter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static Player[] getDefaultPlayers(GamePlay game) {
        Player[] players = new Player[game.numberOfPlayers()];

        for (int i = 0; i < players.length; i++) {
            players[i] = new RandomPlayer("default RandomPlayer for role " + i,
                    System.currentTimeMillis(), true,
                    TemplatePlayer.SEARCH_ALPHABETA, false);
        }

        return players;
    }

    /**
     * takes the toString() function of the GamePlay and turns it into
     * formatted html, which is returned as a JLabel
     *
     * @param frame DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Container getContainer(GameGUI frame) {
        this.frame = frame;

        String s = play.getGame().toString();
        s = "<html><pre>" + s + "</pre></html>";
        s = s.replaceAll("\n", "<br>");

        JLabel label = new JLabel(s) {
                static final long serialVersionUID = -7590349547976890634L;

                public void paintComponent(java.awt.Graphics g) {
                    String s = play.getGame().toString();
                    s = "<html><pre>" + s + "</pre></html>";
                    s = s.replaceAll("\n", "<br>");
                    setText(s);
                    super.paintComponent(g);
                }
            };

        label.setBackground(java.awt.Color.white);
        label.setForeground(java.awt.Color.black);
        label.setOpaque(true);

        return label;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AutoPlay getAutoPlay() {
        return play;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FileFilter getFileFilter() {
        return filter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     */
    protected void setFileFilter(ExtensionFileFilter filter) {
        this.filter = filter;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDefaultFileExtension() {
        return filter.getDefaultType();
    }

    /**
     * returns null if not overwritten
     *
     * @return DOCUMENT ME!
     */
    public JMenu getMenu() {
        return null;
    }

    /**
     * depends on a default constructor being accessible for the
     * GamePlay object in question
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public JGamePlay getNewGame() {
        try {
            play = new GameDriver((GamePlay) play.getGame().getClass()
                                                 .newInstance(),
                    play.getPlayers(), play.getLevel());
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @param play DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws GameRuntimeException DOCUMENT ME!
     */
    public JGamePlay setGame(AutoPlay play) throws GameRuntimeException {
        if (play.getGame().getClass() != play.getGame().getClass()) {
            String text = "JDefaultGame cannot play the given game of class ";
            text = text + this.play.getGame().getClass().getName();
            throw (new GameRuntimeException(play.getGame(), text));
        }

        this.play = play;

        return this;
    }

    /**
     * returns getGameName() from the GamePlay object
     *
     * @see GamePlay#getGameName()
     */
    public String getTitle() {
        return play.getGame().getGameName();
    }

    /**
     * indicates the number of moves performed, number of legal moves
     * available, and indicates if someone won the game.
     *
     * @return DOCUMENT ME!
     */
    public String getStatusMessage() {
        String s = "number of moves: " +
            play.getGame().getMoveHistory().length;

        if (play.getGame().getLegalMoves().length == 0) {
            s += " Game Over!";
        } else {
            s += (", available legal moves: " +
            play.getGame().getLegalMoves().length);
        }

        int[] win = play.getGame().getWinner();

        if (win != null) {
            s += " ** Winner: ";

            for (int i = 0; i < win.length; i++) {
                try {
                    s += play.getPlayer(win[i]).getPlayerName();
                } catch (IndexOutOfBoundsException e) {
                    s += "n/a";
                }

                if (i < (win.length - 1)) {
                    s += ", ";
                }
            }
        }

        return s;
    }

    /**
     * returns null if not overwritten
     *
     * @return DOCUMENT ME!
     */
    public Container getHelp() {
        return null;
    }
}
