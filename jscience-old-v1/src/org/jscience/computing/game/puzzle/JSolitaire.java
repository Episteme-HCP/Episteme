/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.puzzle;

import org.jscience.computing.game.*;

import org.jscience.io.ExtensionFileFilter;

import org.jscience.util.Monitor;
import org.jscience.util.Stopwatch;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * enables Solitaire to be played with JGameFrame
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.computing.game.JGameFrame
 */
public class JSolitaire extends JDefaultGame implements ActionListener {
    /** DOCUMENT ME! */
    JSolitairePanel panel;

    /** DOCUMENT ME! */
    JSolitairePanelListener mouseListener;

    /** DOCUMENT ME! */
    String helpText = "<html>Solitaire instructions<br><br>" +
        "To understand the positions displayed by<br>" +
        "the moves available, you may want to check out<br>" +
        "this little graphic showing the position numbers:<br>" + "<br><pre>" +
        " 11 21 31 41 51 61 71\n" + " 12 22 32 42 52 62 72\n" +
        " 13 23 33 43 53 63 73\n" + " 14 24 34 44 54 64 74\n" +
        " 15 25 35 45 55 65 75\n" + " 16 26 36 46 56 66 76\n" +
        " 17 27 37 47 57 67 77\n" + " \n" + "</pre>\n" +
        "consequently, the following positions are off the board:<br>" +
        " 11 12 21 22 16 17 26 27 61 62 71 72 66 67 76 77<br>";

/**
     * Creates a new JSolitaire object.
     */
    public JSolitaire() {
        this(new Solitaire(), new RandomPlayer(), 2);
        ((RandomPlayer) play.getPlayer(0)).setCheckForWin(true);
    }

/**
     * Creates a new JSolitaire object.
     *
     * @param game    DOCUMENT ME!
     * @param player1 DOCUMENT ME!
     * @param level   DOCUMENT ME!
     */
    public JSolitaire(Solitaire game, Player player1, int level) {
        super(game, new Player[] { player1 }, level,
            new ExtensionFileFilter("sol", "Solitaire Files (*.sol)"));
        panel = new JSolitairePanel(this);
        mouseListener = new JSolitairePanelListener(this);
        panel.addMouseListener(mouseListener);
        panel.addMouseMotionListener(mouseListener);
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
    public JMenu getMenu() {
        JMenu specialMenu = new JMenu("Solitaire");
        JMenuItem item;
        item = new JMenuItem("solve");
        item.addActionListener(this);
        item.setToolTipText(
            "try to solve this puzzle within a given number of moves");
        specialMenu.add(item);

        return specialMenu;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JGamePlay getNewGame() {
        Solitaire game = null;
        String choice = (String) JOptionPane.showInputDialog(frame.getFrame(),
                "select a game type", "new game dialog",
                JOptionPane.PLAIN_MESSAGE, null,
                new String[] {
                    "standard Solitaire", "Solitaire cross", "Solitaire plus",
                    "Solitaire fireplace", "Solitaire up arrow",
                    "Solitaire pyramid", "Solitaire diamond",
                    "random Solitaire field"
                }, "standard Solitaire");

        if (choice == null) {
            frame.say("no game selected; old game is maintained");

            return this;
        }

        if (choice.equals("standard Solitaire")) {
            game = new Solitaire("standard Solitaire",
                    SolitaireSamples.getSolitaire());
        }

        if (choice.equals("Solitaire cross")) {
            game = new Solitaire("Solitaire cross", SolitaireSamples.getCross());
        }

        if (choice.equals("Solitaire plus")) {
            game = new Solitaire("Solitaire plus", SolitaireSamples.getPlus());
        }

        if (choice.equals("Solitaire fireplace")) {
            game = new Solitaire("Solitaire fireplace",
                    SolitaireSamples.getFireplace());
        }

        if (choice.equals("Solitaire up arrow")) {
            game = new Solitaire("Solitaire up arrow",
                    SolitaireSamples.getArrow());
        }

        if (choice.equals("Solitaire pyramid")) {
            game = new Solitaire("Solitaire pyramid",
                    SolitaireSamples.getPyramid());
        }

        if (choice.equals("Solitaire diamond")) {
            game = new Solitaire("Solitaire diamond",
                    SolitaireSamples.getDiamond());
        }

        if (choice.equals("random Solitaire field")) {
            String selected = JOptionPane.showInputDialog(frame.getFrame(),
                    "Enter desired number of peggs (between 2 and 32):",
                    "specify random field", JOptionPane.QUESTION_MESSAGE);
            int number = 0;

            try {
                number = Integer.parseInt(selected);
            } catch (NumberFormatException ex) {
            }

            if ((number < 2) || (number > 32)) {
                frame.say("input invalid; old game is maintained");

                return this;
            }

            game = new Solitaire("random Solitaire field",
                    SolitaireSamples.getRandomField(number,
                        System.currentTimeMillis()));
        }

        play = new GameDriver(game, new Player[] { play.getPlayer(0) },
                play.getLevel());

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        return "Pegged Solitaire";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStatusMessage() {
        Solitaire game = (Solitaire) play.getGame();
        String s = "# of moves done: " + game.getMoveHistory().length;
        s += (", pegs left: " + game.pegsLeft());

        if (game.isSolved()) {
            s += " - ** Game Won! **";
        } else {
            if (game.getLegalMoves().length == 0) {
                s += " - Game Over!";
            } else {
                s += (", available moves: " + game.getLegalMoves().length);
            }
        }

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
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        Solitaire game = (Solitaire) play.getGame();

        if (e.getActionCommand().equals("solve")) {
            SolitaireSolver ss = new SolitaireSolver(this, game.pegsLeft() - 1);
            frame.say("trying to solve within " + (game.pegsLeft() - 1) +
                " moves ..");
            ss.start();

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
}


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class SolitaireSolver extends Thread {
    /** DOCUMENT ME! */
    JSolitaire jplay;

    /** DOCUMENT ME! */
    int number;

/**
     * Creates a new SolitaireSolver object.
     *
     * @param jplay  DOCUMENT ME!
     * @param number DOCUMENT ME!
     */
    SolitaireSolver(JSolitaire jplay, int number) {
        this.jplay = jplay;
        this.number = number;
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        ProgressMonitor jmon = new ProgressMonitor(jplay.getFrame().getFrame(),
                "Trying to solve puzzle in " + number + " moves",
                "running analysis ..", 0, 10);
        Stopwatch t = new Stopwatch();
        Monitor monitor = new Monitor();
        SolveThread task = new SolveThread(jplay.getAutoPlay().getGame(),
                number, monitor);
        task.start();

        int count = 0;

        while (!monitor.test && !jmon.isCanceled()) {
            jmon.setProgress(count++);

            if (count == 10) {
                count = 1;
            }

            jmon.setNote("positions searched: " + monitor.getNumber() + " ..");

            try {
                Thread.currentThread().sleep(500);
            } catch (InterruptedException ex) {
            }
        }

        if (jmon.isCanceled()) {
            monitor.disable();
        }

        try {
            task.join();
        } catch (InterruptedException e) {
            while (!monitor.test) {
            }
        }

        jmon.close();
        java.awt.Toolkit.getDefaultToolkit().beep();

        Solitaire solved = null;

        try {
            solved = (Solitaire) monitor.getObject();
        } catch (ClassCastException e) {
            String s = "no solution possible; positions searched: " +
                monitor.getNumber();
            s += ("\ntime taken: " + t.elapsedAsString());
            JOptionPane.showMessageDialog(jplay.getFrame().getFrame(), s,
                "solve attempt impossible", JOptionPane.INFORMATION_MESSAGE);
            jplay.getFrame().say("done solving");

            return;
        }

        if (solved == null) {
            String s = "no solution found; positions searched: " +
                monitor.getNumber();
            s += ("\ntime taken: " + t.elapsedAsString());
            JOptionPane.showMessageDialog(jplay.getFrame().getFrame(), s,
                "solve attempt failed", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String s = "puzzle solved (searching " + monitor.getNumber() +
                " positions)";
            s += ("\ntime taken: " + t.elapsedAsString());
            s += "\nthe game will be set to the solved position; you can backtrack through the move list";
            JOptionPane.showMessageDialog(jplay.getFrame().getFrame(), s,
                "solve attempt successful", JOptionPane.INFORMATION_MESSAGE);

            AutoPlay play = new GameDriver(solved,
                    new Player[] { jplay.getAutoPlay().getPlayer(0) },
                    jplay.getAutoPlay().getLevel());
            jplay.setGame(play);
        }

        jplay.getFrame().say("done solving");
        jplay.getFrame().repaint();
    }
}
