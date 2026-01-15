/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.reversi;

import org.jscience.computing.game.GameGUI;
import org.jscience.computing.game.JDefaultGame;
import org.jscience.computing.game.Player;

import org.jscience.io.ExtensionFileFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * enables ReversiGame to be played with JGameFrame
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.computing.game.JGameFrame
 */
public class JReversi extends JDefaultGame implements ActionListener {
    /** DOCUMENT ME! */
    JCheckBoxMenuItem showLegalMoves;

    /** DOCUMENT ME! */
    JReversiPanel panel;

    /** DOCUMENT ME! */
    JReversiPanelListener mouseListener;

    /** DOCUMENT ME! */
    String helpText = "<html>Reversi instructions<br><br>" +
        "Player X (role 0) is yellow; Player O (role 1) is red.<br>" +
        "You can make a move by clicking the right position<br>" +
        "on the board.<br>" +
        "Legal move positions are indicated by the color black.<br>" +
        "To understand the positions displayed by<br>" +
        "the moves available, keep the following in mind:<br>" +
        "the first digit refers to the column and the second<br>" +
        "digit refers to the row.<br>" +
        "In each case, counting beginns in the lower left corner,<br>" +
        "i.e. the position 11 is at the left bottom, 88 is at<br>" +
        "the right top and 18 would be at the right bottom.<br>";

/**
     * Creates a new JReversi object.
     */
    public JReversi() {
        this(new ReversiGame(), new ReversiPlayer("X"), new ReversiPlayer("O"),
            4);
        ((ReversiPlayer) play.getPlayers()[0]).setTracking(true);
        ((ReversiPlayer) play.getPlayers()[1]).setTracking(true);
    }

/**
     * Creates a new JReversi object.
     *
     * @param game    DOCUMENT ME!
     * @param player1 DOCUMENT ME!
     * @param player2 DOCUMENT ME!
     * @param level   DOCUMENT ME!
     */
    public JReversi(ReversiGame game, Player player1, Player player2, int level) {
        super(game, new Player[] { player1, player2 }, level,
            new ExtensionFileFilter("rev", "Reversi Files (*.rev)"));
        panel = new JReversiPanel(this);
        mouseListener = new JReversiPanelListener(this);
        panel.addMouseListener(mouseListener);
        showLegalMoves = new JCheckBoxMenuItem("Show legal moves");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStatusMessage() {
        String s = "next player: ";
        s += ((play.getGame().nextPlayer() == 0) ? "yellow" : "red");
        s += ("; # of moves: " + play.getGame().getMoveHistory().length);
        s += (", available legal moves: " +
        play.getGame().getLegalMoves().length);
        s += ("; X: " + ((ReversiGame) play.getGame()).getCount(0) +
        " pieces, O: " + ((ReversiGame) play.getGame()).getCount(1) +
        " pieces");

        if (play.getGame().getLegalMoves().length == 0) {
            s += " Game Over!";
        }

        int[] win = play.getGame().getWinner();

        if (win != null) {
            s += " Winner:";

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

        return panel;
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
    public JMenu getMenu() {
        JMenu specialMenu = new JMenu("Reversi");
        specialMenu.add(showLegalMoves);
        showLegalMoves.addActionListener(this);

        JMenuItem item = new JMenuItem("change tile size");
        item.addActionListener(this);
        specialMenu.add(item);

        return specialMenu;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("change tile size")) {
            String selected = JOptionPane.showInputDialog(frame.getFrame(),
                    "Enter new tile size (old size: " + panel.tileSize + "):",
                    "", JOptionPane.PLAIN_MESSAGE);
            int number = 0;

            try {
                number = Integer.parseInt(selected);
            } catch (NumberFormatException ex) {
                return;
            }

            panel.setTileSize(number, panel.tileInset);
            frame.repaint();

            return;
        }

        if (e.getActionCommand().equals("Show legal moves")) {
            frame.repaint();
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
}
