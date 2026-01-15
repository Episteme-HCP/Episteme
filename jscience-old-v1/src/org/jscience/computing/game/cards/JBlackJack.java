/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.cards;

import org.jscience.computing.game.GameDriver;
import org.jscience.computing.game.JDefaultGame;
import org.jscience.computing.game.JGamePlay;
import org.jscience.computing.game.Player;

import org.jscience.io.ExtensionFileFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * implements the GUI wrapper for BlackJack
 *
 * @author Holger Antelmann
 */
public class JBlackJack extends JDefaultGame implements ActionListener {
/**
     * Creates a new JBlackJack object.
     */
    public JBlackJack() {
        this(new BlackJack(new float[] { 2, 10, 50 }),
            new Player[] { new BJPlayer(), new BJPlayer(), new BJPlayer() }, 0);
    }

/**
     * Creates a new JBlackJack object.
     *
     * @param game   DOCUMENT ME!
     * @param player DOCUMENT ME!
     * @param level  DOCUMENT ME!
     */
    public JBlackJack(BlackJack game, Player[] player, int level) {
        super(game, player, level,
            new ExtensionFileFilter("bj", "BlackJack Files (*.bj)"));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStatusMessage() {
        BlackJack game = (BlackJack) play.getGame();
        String s = "n/a";

        if (game.gameOver()) {
            s = " Game Over!";

            for (int i = 0; i < game.numberOfPlayers(); i++) {
                s += "; ";
                s += ("role " + i + " result: " + game.getResult(i));
            }

            return s;
        }

        BJHand u = game.getCurrentHand(game.nextPlayer());

        if (u != null) {
            s = "bet: " + game.getCurrentHand(game.nextPlayer()).bet;
            s += ("; value: " + BlackJack.getHandValue(u.cards));
        }

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JMenu getMenu() {
        JMenu specialMenu = new JMenu("BlackJack");
        JMenuItem item;
        item = new JMenuItem("print cheat info to Console");
        item.addActionListener(this);
        specialMenu.add(item);

        return specialMenu;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JGamePlay getNewGame() {
        GameDriver d = new GameDriver(new BlackJack(new float[] { 2, 10, 50 }),
                new Player[] { new BJPlayer(), new BJPlayer(), new BJPlayer() },
                0);
        play = d;

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("print cheat info to Console")) {
            frame.say("(Menu option 'print cheat info to Console' selected)");
            frame.say("printing status after hit move:");

            BlackJack g;

            try {
                g = (BlackJack) play.getGame().clone();
            } catch (CloneNotSupportedException ex) {
                g = null;
            }

            BJMove move = new BJMove(g.nextPlayer(), BJMove.HIT);
            g.makeMove(move);
            System.out.println(g);
        }
    }
}
