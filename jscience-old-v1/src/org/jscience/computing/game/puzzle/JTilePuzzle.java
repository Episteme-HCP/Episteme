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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;


/**
 * implements the GUI wrapper for the TilePuzzle
 *
 * @author Holger Antelmann
 */
public class JTilePuzzle extends AbstractTableModel implements JGamePlay,
    ActionListener {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -9197827184866672147L;

    /** DOCUMENT ME! */
    GameGUI frame;

    /** DOCUMENT ME! */
    AutoPlay play;

    /** DOCUMENT ME! */
    TilePuzzle game;

    /** DOCUMENT ME! */
    ExtensionFileFilter filter;

    /** DOCUMENT ME! */
    Container jgame;

    /** DOCUMENT ME! */
    JCheckBoxMenuItem disableReverseMove;

    /** DOCUMENT ME! */
    JCheckBoxMenuItem endWhenSolved;

    /** DOCUMENT ME! */
    JFileChooser imageChooser;

/**
     * Creates a new JTilePuzzle object.
     */
    public JTilePuzzle() {
        this(new TilePuzzle(), new TilePuzzlePlayer(), 3);
    }

/**
     * Creates a new JTilePuzzle object.
     *
     * @param game   DOCUMENT ME!
     * @param player DOCUMENT ME!
     * @param level  DOCUMENT ME!
     */
    public JTilePuzzle(TilePuzzle game, Player player, int level) {
        this.game = game;
        play = new GameDriver(game, new Player[] { player }, level);
        filter = new ExtensionFileFilter("tpz", "TilePuzzle Files (*.tpz)");
        imageChooser = new JFileChooser();

        ExtensionFileFilter jpegs = new ExtensionFileFilter("jpg",
                "JPEG files (*.jpg)");
        ExtensionFileFilter gifs = new ExtensionFileFilter("gif",
                "GIF files (*.gif)");
        imageChooser.addChoosableFileFilter(gifs);
        imageChooser.addChoosableFileFilter(jpegs);
        initContainer();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        return "TilePuzzle";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStatusMessage() {
        String s = "Number of moves: ";
        s += game.numberOfMoves();

        try {
            if (game.isSolved()) {
                s += " - Puzzle solved!";
            } else {
                s += ("  (out of place: " + TilePuzzlePlayer.outOfPlace(game));
                s += (", manhattan distance: " +
                TilePuzzlePlayer.manhattanDistance(game));
                s += ")";
            }
        } catch (GameException e) {
            s += " - Puzzle is impossible to solve!";
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

        return jgame;
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
     * @return DOCUMENT ME!
     */
    public String getDefaultFileExtension() {
        return filter.getDefaultType();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JMenu getMenu() {
        JMenu specialMenu = new JMenu("TilePuzzle");
        JMenuItem item;
        item = new JMenuItem("reset");
        item.addActionListener(this);
        item.setToolTipText("show the solution");
        specialMenu.add(item);

        item = new JMenuItem("shuffle");
        item.addActionListener(this);
        item.setToolTipText(
            "change the status by shuffling; goal may not be reachable");
        specialMenu.add(item);

        item = new JMenuItem("randomize ..");
        item.addActionListener(this);
        item.setToolTipText(
            "change the status through a given number of random moves");
        specialMenu.add(item);

        item = new JMenuItem("solve ..");
        item.addActionListener(this);
        item.setToolTipText(
            "try to solve this puzzle within a given number of moves");
        specialMenu.add(item);

        item = new JMenuItem("start puzzle controller");
        item.addActionListener(this);
        item.setToolTipText(
            "try to solve this puzzle within a given number of moves");
        specialMenu.add(item);

        disableReverseMove = new JCheckBoxMenuItem("Disable reverse move");
        disableReverseMove.setToolTipText(
            "with this enabled, the move that reverts to the last position won't be legal");
        disableReverseMove.addActionListener(this);
        specialMenu.add(disableReverseMove);
        disableReverseMove.setState(game.isReverseMoveDisabled());

        endWhenSolved = new JCheckBoxMenuItem("Game ends when solved");
        endWhenSolved.setToolTipText(
            "with this enabled, no move is available anymore if the puzzle is solved");
        endWhenSolved.addActionListener(this);
        specialMenu.add(endWhenSolved);
        endWhenSolved.setState(game.isEndWhenSolved());

        return specialMenu;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JGamePlay getNewGame() {
        String[] items = new String[4];
        items[0] = "US flag Puzzle";
        items[1] = "SF skyline Puzzle";
        items[2] = "Custom number Puzzle ..";
        items[3] = "Custom image Puzzle ..";

        String selected = (String) JOptionPane.showInputDialog(frame.getFrame(),
                "select a game type", "new game dialog",
                JOptionPane.PLAIN_MESSAGE, null, items, null);

        if (selected == null) {
            frame.say("no game selected; old game is maintained");

            return this;
        }

        if (selected.equals("US flag Puzzle")) {
            game = new TilePuzzle("US Flag Puzzle",
                    TilePuzzleSamples.getUSFlagPuzzle(3));
            game.shuffle();
        }

        if (selected.equals("SF skyline Puzzle")) {
            game = new TilePuzzle("SF skyline Puzzle",
                    TilePuzzleSamples.getSFPuzzle(4));
            game.shuffle();
        }

        if (selected.equals("Custom number Puzzle ..")) {
            int size = getPuzzleSizeFromUser();

            if (size == 0) {
                frame.say("no game selected; old game is maintained");

                return this;
            }

            game = new TilePuzzle("Custom number Puzzle",
                    TilePuzzleSamples.getNumberPuzzle(size));
        }

        if (selected.equals("Custom image Puzzle ..")) {
            ImageIcon icon = null;
            int chosen = imageChooser.showOpenDialog(frame.getFrame());

            if (chosen == JFileChooser.APPROVE_OPTION) {
                try {
                    icon = new ImageIcon(imageChooser.getSelectedFile().toURL());
                } catch (Exception e) {
                    frame.say(
                        "error while trying to create ImageIcon; old game will be maintained");

                    return this;
                }
            } else {
                frame.say("no game selected; old game is maintained");

                return this;
            }

            int size = getPuzzleSizeFromUser();

            if (size < 1) {
                frame.say("no game selected; old game is maintained");

                return this;
            }

            int scaleOption = JOptionPane.showConfirmDialog(frame.getFrame(),
                    "Do you want to scale the image? (orig. size: " +
                    icon.getIconWidth() + "x" + icon.getIconHeight() + ")",
                    "customize image", JOptionPane.YES_NO_CANCEL_OPTION);

            switch (scaleOption) {
            case JOptionPane.CANCEL_OPTION:
                frame.say("no game selected; old game is maintained");

                return this;

            case JOptionPane.NO_OPTION:
                game = new TilePuzzle("Custom image Puzzle",
                        TilePuzzleSamples.getImagePuzzle(icon, size));

                break;

            case JOptionPane.YES_OPTION:

                String swidth = JOptionPane.showInputDialog(frame.getFrame(),
                        "enter new width: (was: " + icon.getIconWidth() +
                        "; cancel aborts)");
                int width = 0;

                try {
                    width = Integer.parseInt(swidth);
                } catch (NumberFormatException e) {
                }

                if (width < size) {
                    frame.say(
                        "invalid scaling selected; old game is maintained");

                    return this;
                }

                int height = (int) (((float) (icon.getIconHeight() / (float) icon.getIconWidth())) * (float) width);
                String sheight = JOptionPane.showInputDialog(frame.getFrame(),
                        "enter new height: (press cancel to proportinally scale to " +
                        height + ")");

                try {
                    height = Integer.parseInt(sheight);
                } catch (NumberFormatException e) {
                }

                if (height < size) {
                    frame.say(
                        "invalid scaling selected; old game is maintained");

                    return this;
                }

                game = new TilePuzzle("Custom image Puzzle",
                        TilePuzzleSamples.getImagePuzzle(icon, width, height,
                            size));

                break;
            }
        }

        if (selected == null) {
            frame.say("no game selected; old game is maintained");

            return this;
        }

        play = new GameDriver(game, new Player[] { play.getPlayer(0) },
                play.getLevel());
        initContainer();
        game.setReverseMoveDisabled(disableReverseMove.getState());
        game.setEndWhenSolved(endWhenSolved.getState());

        if (frame.getFrame() instanceof JGameFrame) {
            ((JGameFrame) frame.getFrame()).drawAllWindows();
        }

        frame.repaint();

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int getPuzzleSizeFromUser() {
        Integer[] iarray = new Integer[7];

        for (int i = 0; i < iarray.length; i++)
            iarray[i] = new Integer(i + 2);

        Integer selected = (Integer) JOptionPane.showInputDialog(frame.getFrame(),
                "select puzzle size", "specify new game",
                JOptionPane.QUESTION_MESSAGE, null, iarray, null);

        if (selected == null) {
            return 0;
        }

        return selected.intValue();
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
        if (play.getGame().getClass() != TilePuzzle.class) {
            String text = "JTilePuzzle cannot play the given game of class ";
            text = text + this.play.getGame().getClass().getName();
            throw (new GameRuntimeException(play.getGame(), text));
        }

        this.play = play;
        disableReverseMove.setState(game.isReverseMoveDisabled());
        endWhenSolved.setState(game.isEndWhenSolved());
        initContainer();

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Container getHelp() {
        return null;
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
     */
    void initContainer() {
        JTable table = new JTable(this) {
                static final long serialVersionUID = 8374738901469401758L;

                public Class getColumnClass(int c) {
                    Object o = getValueAt(0, c);

                    if (o == null) {
                        o = getValueAt(1, c);
                    }

                    return o.getClass();
                }
            };

        Object tile = getValueAt(0, 0);

        if (tile == null) {
            tile = getValueAt(0, 1);
        }

        if (tile.getClass() == ImageIcon.class) {
            table.setRowHeight(((ImageIcon) tile).getIconHeight());

            for (int i = 0; i < getColumnCount(); i++) {
                table.getColumnModel().getColumn(i)
                     .setPreferredWidth(((ImageIcon) tile).getIconWidth());
            }
        }

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(false);
        table.addMouseListener(new JTilePuzzleMouseListener(this));
        jgame = table;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("reset")) {
            frame.say("(Menu option 'reset' selected)");
            game.reset();
            frame.repaint();

            return;
        }

        if (e.getActionCommand().equals("shuffle")) {
            frame.say("(Menu option 'shuffle' selected)");
            game.shuffle();
            frame.repaint();

            return;
        }

        if (e.getActionCommand().equals("solve ..")) {
            frame.say("(Menu option 'solve' selected)");

            String selected = JOptionPane.showInputDialog(frame.getFrame(),
                    "Please select number of moves to search:",
                    "solve puzzle try", JOptionPane.QUESTION_MESSAGE);
            int number = 0;

            try {
                number = Integer.parseInt(selected);
            } catch (NumberFormatException ex) {
            }

            if (number == 0) {
                frame.say("no valid number selected - no solve attempted");

                return;
            }

            TPSolverThread ps = new TPSolverThread(this, number);
            frame.say("trying to solve within " + number + " moves ..");
            ps.start();

            return;
        }

        if (e.getActionCommand().equals("randomize ..")) {
            frame.say("(Menu option 'randomize' selected)");

            String selected = JOptionPane.showInputDialog(frame.getFrame(),
                    "Please select number of moves for ranomization:",
                    "randomize", JOptionPane.QUESTION_MESSAGE);
            int number = 0;

            try {
                number = Integer.parseInt(selected);
            } catch (NumberFormatException ex) {
            }

            if (number == 0) {
                frame.say("no valid number selected - no randomization");

                return;
            }

            frame.say("randomizing ...");
            game.randomize(number);
            frame.say("done randomizing with " + number + " moves");
            frame.repaint();

            return;
        }

        if (e.getActionCommand().equals("start puzzle controller")) {
            new JTPControl(this);
            frame.say("game control started");
        }

        if (e.getActionCommand().equals("Disable reverse move")) {
            frame.say("(Menu option 'Disable reverse move' selected)");
            game.setReverseMoveDisabled(disableReverseMove.getState());
        }

        if (e.getActionCommand().equals("Game ends when solved")) {
            frame.say("(Menu option 'Disable reverse move' selected)");
            game.setEndWhenSolved(endWhenSolved.getState());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        return game.getSolutionMatrix().length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return game.getSolutionMatrix()[0].length;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValueAt(int row, int column) {
        return game.getPuzzleMatrix()[row][column];
    }
}
