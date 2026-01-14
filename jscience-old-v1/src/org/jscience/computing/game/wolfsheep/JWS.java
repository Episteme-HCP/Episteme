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

package org.jscience.computing.game.wolfsheep;

import org.jscience.computing.game.GameGUI;
import org.jscience.computing.game.JDefaultGame;
import org.jscience.computing.game.Player;

import org.jscience.io.ExtensionFileFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * implements the GUI wrapper around WolfsheepGame to use with JGameFrame
 *
 * @author Holger Antelmann
 */
public class JWS extends JDefaultGame implements ActionListener {
    /** DOCUMENT ME! */
    JCheckBoxMenuItem flipped;

    /** DOCUMENT ME! */
    JWSBoard jboard;

    /** DOCUMENT ME! */
    JWSListener listener;

/**
     * Creates a new JWS object.
     */
    public JWS() {
        this(new WolfsheepGame(), new WSPlayer("wolf"), new WSPlayer("sheep"), 6);
        ((WSPlayer) play.getPlayers()[0]).setTracking(true);
        ((WSPlayer) play.getPlayers()[1]).setTracking(true);
    }

/**
     * Creates a new JWS object.
     *
     * @param game    DOCUMENT ME!
     * @param player1 DOCUMENT ME!
     * @param player2 DOCUMENT ME!
     * @param level   DOCUMENT ME!
     */
    public JWS(WolfsheepGame game, Player player1, Player player2, int level) {
        super(game, new Player[] { player1, player2 }, level,
            new ExtensionFileFilter("wshp", "Wolfsheep games (*.ttt)"));
        jboard = new JWSBoard(this);
        listener = new JWSListener(this);
        jboard.addMouseListener(listener);
        jboard.addMouseMotionListener(listener);
        jboard.addComponentListener(new JWSSizer(this));
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
        JMenu specialMenu = new JMenu("WSGame");
        JMenuItem item;
        specialMenu.add(flipped);
        item = new JMenuItem("resize board to default");
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
        if (e.getActionCommand().equals("Flip Board")) {
            frame.say("Menu Flip Board selected");
            jboard.repaint();

            return;
        }

        if (e.getActionCommand().equals("resize board to default")) {
            frame.say("resizing board to default size");
            jboard.setTileSize(jboard.defaultTileSize);
            jboard.repaint();
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
    boolean isFlipped() {
        return flipped.getState();
    }
}
