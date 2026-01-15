/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.puzzle;

import org.jscience.computing.game.AutoPlay;
import org.jscience.computing.game.GameDriver;
import org.jscience.computing.game.Player;

import org.jscience.util.Monitor;
import org.jscience.util.Stopwatch;

import javax.swing.*;


/**
 * uses a SolveThread to find a puzzle solution while providing visual
 * feedback through a ProgressMonitor
 *
 * @author Holger Antelmann
 *
 * @see SolveThread
 */
class TPSolverThread extends Thread {
    /** DOCUMENT ME! */
    JTilePuzzle jplay;

    /** DOCUMENT ME! */
    int number;

/**
     * Creates a new TPSolverThread object.
     *
     * @param jplay  DOCUMENT ME!
     * @param number DOCUMENT ME!
     */
    TPSolverThread(JTilePuzzle jplay, int number) {
        this.jplay = jplay;
        this.number = number;
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        ProgressMonitor jmon = new ProgressMonitor(jplay.frame.getFrame(),
                "Trying to solve puzzle in " + number + " moves",
                "running analysis ..", 0, 10);
        Stopwatch t = new Stopwatch();
        Monitor monitor = new Monitor();
        SolveThread task = new SolveThread(jplay.game, number, monitor);
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

        TilePuzzle solved = null;

        try {
            solved = (TilePuzzle) monitor.getObject();
        } catch (ClassCastException e) {
            String s = "** no solution possible; positions searched: " +
                monitor.getNumber();
            s += ("\ntime taken: " + t.elapsedAsString());
            JOptionPane.showMessageDialog(jplay.frame.getFrame(), s,
                "solve attempt impossible", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("done solving");

            return;
        }

        java.awt.Toolkit.getDefaultToolkit().beep();

        if (solved == null) {
            String s = "no solution found; positions searched: " +
                monitor.getNumber();
            s += ("\ntime taken: " + t.elapsedAsString());
            JOptionPane.showMessageDialog(jplay.frame.getFrame(), s,
                "solve attempt failed", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String s = "puzzle solved (searching " + monitor.getNumber() +
                " positions)";
            s += ("\ntime taken: " + t.elapsedAsString());
            s += "\nthe game will be set to the solved position; you can backtrack through the move list";
            JOptionPane.showMessageDialog(jplay.frame.getFrame(), s,
                "solve attempt successful", JOptionPane.INFORMATION_MESSAGE);

            AutoPlay play = new GameDriver(solved,
                    new Player[] { jplay.play.getPlayers()[0] },
                    jplay.play.getLevel());
            jplay.setGame(play);
        }

        jplay.frame.say("done solving");
        jplay.frame.repaint();
    }
}
