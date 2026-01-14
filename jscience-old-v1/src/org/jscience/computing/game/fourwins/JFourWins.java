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

package org.jscience.computing.game.fourwins;

import org.jscience.computing.game.*;

import org.jscience.io.ExtensionFileFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.*;


/**
 * JFourWins provides the GUI wrapping for a FourWinsGame to be played with
 * a JGameFrame
 *
 * @author Holger Antelmann
 *
 * @see FourWinsGame
 * @see org.jscience.computing.game.JGameFrame
 */
public class JFourWins extends JDefaultGame implements ActionListener {
    /** DOCUMENT ME! */
    JFourWinsPanel panel;

    /** DOCUMENT ME! */
    MouseListener mouseListener;

/**
     * Creates a new JFourWins object.
     */
    public JFourWins() {
        this(new FourWinsGame(), new FourWinsPlayer("X"),
            new FourWinsPlayer("O"), 6);
        ((FourWinsPlayer) play.getPlayers()[0]).setTracking(true);
        ((FourWinsPlayer) play.getPlayers()[1]).setTracking(true);
    }

/**
     * Creates a new JFourWins object.
     *
     * @param game    DOCUMENT ME!
     * @param player1 DOCUMENT ME!
     * @param player2 DOCUMENT ME!
     * @param level   DOCUMENT ME!
     */
    public JFourWins(FourWinsGame game, Player player1, Player player2,
        int level) {
        super(game, new Player[] { player1, player2 }, level,
            new ExtensionFileFilter("four", "FourWins Files (*.four)"));
        panel = new JFourWinsPanel(this);
        mouseListener = new JFourWinsPanelListener(this);
        panel.addMouseListener(mouseListener);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        return "Connect Four";
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
     *
     * @throws Error DOCUMENT ME!
     */
    public JGamePlay getNewGame() {
        int oldWidth = ((FourWinsGame) play.getGame()).getWidth();
        int oldHeight = ((FourWinsGame) play.getGame()).getHeight();
        String[] option = new String[] {
                "standard  (7*6)", "variant (7*7)", "custom"
            };
        String selected = (String) JOptionPane.showInputDialog(frame.getFrame(),
                "select a game type", "new game dialog",
                JOptionPane.PLAIN_MESSAGE, null, option, null);

        if (selected == null) {
            frame.say("no game selected; old game is maintained");
        } else if (selected.equals("standard  (7*6)")) {
            play = new GameDriver(new FourWinsGame(), play.getPlayers(),
                    play.getLevel());
        } else if (selected.equals("variant (7*7)")) {
            play = new GameDriver(new FourWinsGame("FourWins variant", 7, 7),
                    play.getPlayers(), play.getLevel());
        } else if (selected.equals("custom")) {
            int width = 0;
            int height = 0;

            try {
                width = Integer.parseInt(JOptionPane.showInputDialog(
                            frame.getFrame(), "select width"));
                height = Integer.parseInt(JOptionPane.showInputDialog(
                            frame.getFrame(), "select height"));
            } catch (NumberFormatException ex) {
                frame.say(
                    "selected numbers were not valid; old game is maintained");
            }

            play = new GameDriver(new FourWinsGame("FourWins variant", width,
                        height), play.getPlayers(), play.getLevel());
        } else {
            throw new Error();
        }

        panel.setTileSize(panel.tileSize, panel.tileInset);

        if ((oldWidth != ((FourWinsGame) play.getGame()).getWidth()) ||
                (oldHeight != ((FourWinsGame) play.getGame()).getHeight())) {
            frame.repaint();
        }

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JMenu getMenu() {
        JMenuItem item;
        JMenu specialMenu = new JMenu("FourWins");
        JMenu colorMenu = new JMenu("change colors");
        item = new JMenuItem("for player X");
        item.addActionListener(this);
        colorMenu.add(item);

        item = new JMenuItem("for player O");
        item.addActionListener(this);
        colorMenu.add(item);

        item = new JMenuItem("for empty slot");
        item.addActionListener(this);
        colorMenu.add(item);
        item = new JMenuItem("for background");
        item.addActionListener(this);
        colorMenu.add(item);

        item = new JMenuItem("reset all colors");
        item.addActionListener(this);
        colorMenu.add(item);
        specialMenu.add(colorMenu);

        item = new JMenuItem("change tile size");
        item.addActionListener(this);
        specialMenu.add(item);

        return specialMenu;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStatusMessage() {
        String s = "next player: ";
        s += ((play.getGame().nextPlayer() == 0) ? "yellow" : "red");
        s += ("; " + super.getStatusMessage());

        return s;
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

        if (e.getActionCommand().equals("for player X")) {
            Color color = JColorChooser.showDialog(frame.getFrame(),
                    "select color for player X", panel.colorX);

            if (color != null) {
                panel.colorX = color;
            }

            frame.repaint();

            return;
        }

        if (e.getActionCommand().equals("for player O")) {
            Color color = JColorChooser.showDialog(frame.getFrame(),
                    "select color for player O", panel.colorO);

            if (color != null) {
                panel.colorO = color;
            }

            frame.repaint();

            return;
        }

        if (e.getActionCommand().equals("for empty slot")) {
            Color color = JColorChooser.showDialog(frame.getFrame(),
                    "select color for player O", panel.colorNone);

            if (color != null) {
                panel.colorNone = color;
            }

            frame.repaint();

            return;
        }

        if (e.getActionCommand().equals("for background")) {
            Color color = JColorChooser.showDialog(frame.getFrame(),
                    "select color for player O", panel.colorBackground);

            if (color != null) {
                panel.colorBackground = color;
            }

            panel.setBackground(panel.colorBackground);
            frame.repaint();

            return;
        }

        if (e.getActionCommand().equals("reset all colors")) {
            panel.colorX = Color.yellow;
            panel.colorO = Color.red;
            panel.colorNone = Color.lightGray;
            panel.colorBackground = Color.blue;
            panel.setBackground(panel.colorBackground);
            frame.repaint();

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
    public Container getHelp() {
        String s = "<html>FourWins game help<br><br>" +
            "You can make a move by simply clicking on the column.<br>" +
            "If the colors are not customized, <br>" +
            "player role 0 is yellow and player role 1 is red.<br>";

        return new JLabel(s);
    }
}
