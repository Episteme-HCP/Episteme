/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.puzzle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;


/**
 * provides a TilePuzzle controller that can be started through JTilePuzzle
 *
 * @author Holger Antelmann
 */
class JTPControl extends JDialog implements ActionListener {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -33865145092905369L;

    /** DOCUMENT ME! */
    JTilePuzzle jplay;

/**
     * Creates a new JTPControl object.
     *
     * @param jplay DOCUMENT ME!
     */
    JTPControl(JTilePuzzle jplay) {
        super((Frame) jplay.frame.getFrame(), false);
        this.jplay = jplay;
        getContentPane().setLayout(new BorderLayout());

        JPanel main = new JPanel(new BorderLayout());
        getContentPane().add(main, BorderLayout.CENTER);
        main.add(new JLabel("<html>use buttons <br>to move tiles"),
            BorderLayout.CENTER);
        main.add(makeButton("left", KeyEvent.VK_LEFT), BorderLayout.WEST);
        main.add(makeButton("right", KeyEvent.VK_RIGHT), BorderLayout.EAST);
        main.add(makeButton("up", KeyEvent.VK_UP), BorderLayout.NORTH);
        main.add(makeButton("down", KeyEvent.VK_DOWN), BorderLayout.SOUTH);
        getContentPane()
            .add(makeButton("close", KeyEvent.VK_C), BorderLayout.SOUTH);
        setLocationRelativeTo(jplay.frame.getFrame());
        pack();
        setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param titleAction DOCUMENT ME!
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    JButton makeButton(String titleAction, int key) {
        JButton myButton = new JButton(titleAction);
        myButton.setActionCommand(titleAction);
        myButton.setMnemonic(key);
        myButton.setMargin(new Insets(0, 0, 0, 0));
        myButton.addActionListener(this);

        return myButton;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        TilePuzzle game = (TilePuzzle) jplay.getAutoPlay().getGame();

        if (e.getActionCommand().equals("close")) {
            jplay.getFrame().say("exiting JTPControl");
            setVisible(false);

            return;
        }

        if (e.getActionCommand().equals("left")) {
            jplay.getFrame().say("move left");
            game.makeMove(new TilePuzzleMove(TilePuzzleMove.LEFT));
            jplay.frame.repaint();

            return;
        }

        if (e.getActionCommand().equals("right")) {
            jplay.getFrame().say("move right");
            game.makeMove(new TilePuzzleMove(TilePuzzleMove.RIGHT));
            jplay.frame.repaint();

            return;
        }

        if (e.getActionCommand().equals("up")) {
            jplay.getFrame().say("move up");
            game.makeMove(new TilePuzzleMove(TilePuzzleMove.UP));
            jplay.frame.repaint();

            return;
        }

        if (e.getActionCommand().equals("down")) {
            jplay.getFrame().say("move down");
            game.makeMove(new TilePuzzleMove(TilePuzzleMove.DOWN));
            jplay.frame.repaint();

            return;
        }
    }
}
