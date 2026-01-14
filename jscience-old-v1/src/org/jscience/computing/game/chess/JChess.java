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

package org.jscience.computing.game.chess;

import org.jscience.computing.game.GameGUI;
import org.jscience.computing.game.JDefaultGame;
import org.jscience.computing.game.Player;

import org.jscience.io.ExtensionFileFilter;

import java.awt.*;
import java.awt.event.*;

import java.io.File;

import javax.swing.*;


/**
 * Implements the GUI wrapper around ChessGame to use with JGameFrame. This
 * class features easy customization of the appearance of the chess figures
 * with a call to the method <code>setGraphics (ChessGraphics icons)</code>.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.computing.game.JGameFrame
 */
public class JChess extends JDefaultGame implements ActionListener {
    /** DOCUMENT ME! */
    ChessGame setupGame;

    /** DOCUMENT ME! */
    JDialog setupPanel;

    /** DOCUMENT ME! */
    JPieceSelector pieceSelector;

    /** DOCUMENT ME! */
    JChessBoard jboard;

    /** DOCUMENT ME! */
    JChessListener listener;

    /** DOCUMENT ME! */
    JCheckBoxMenuItem flipped;

    /** DOCUMENT ME! */
    JCheckBoxMenuItem showCoordinates;

    /** DOCUMENT ME! */
    JRadioButtonMenuItem gset1;

    /** DOCUMENT ME! */
    JRadioButtonMenuItem gset2;

    /** DOCUMENT ME! */
    JFileChooser fileChooser;

    /** DOCUMENT ME! */
    String helpText = "<html>Chess Help<br><br>" +
        "Player role 0 is white and player role 1 is black.<br>" +
        "In order to perform an 'en passant' move,<br>" +
        "drag your pawn and release it above the<br>" +
        "pawn to be captured.<br>" +
        "If you want to promote a pawn to anything but<br>" +
        "a queen, you will have to select the appropriate<br>" +
        "move from the LegalMoves list; performing the move<br>" +
        "using drag&drop will always promote to a queen." + "<br>";

/**
     * Creates a new JChess object.
     */
    public JChess() {
        this(new ChessGame(), new ChessPlayer("white"),
            new ChessPlayer("black"), 1);
        ((ChessPlayer) play.getPlayers()[0]).setTracking(true);
        ((ChessPlayer) play.getPlayers()[1]).setTracking(true);
    }

/**
     * Creates a new JChess object.
     *
     * @param game    DOCUMENT ME!
     * @param player1 DOCUMENT ME!
     * @param player2 DOCUMENT ME!
     * @param level   DOCUMENT ME!
     */
    public JChess(ChessGame game, Player player1, Player player2, int level) {
        super(game, new Player[] { player1, player2 }, level,
            new ExtensionFileFilter("chess", "Chess game (*.chess)"));
        jboard = new JChessBoard(this);
        listener = new JChessListener(this);
        jboard.addMouseListener(listener);
        jboard.addMouseMotionListener(listener);
        flipped = new JCheckBoxMenuItem("flip board");
        flipped.addActionListener(this);
        flipped.setMnemonic(KeyEvent.VK_F);
        showCoordinates = new JCheckBoxMenuItem("show coordinates");
        showCoordinates.addActionListener(this);
        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new ExtensionFileFilter("txt",
                "text files (*.txt)"));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JMenu getMenu() {
        JMenu specialMenu = new JMenu("Chess");
        specialMenu.setMnemonic(KeyEvent.VK_C);

        JMenuItem item;

        {
            specialMenu.add(flipped);
            specialMenu.add(showCoordinates);

            item = new JMenuItem("setup board");
            item.addActionListener(this);
            specialMenu.add(item);

            item = new JMenuItem("resize board to default");
            item.addActionListener(this);
            specialMenu.add(item);

            item = new JMenuItem("export game in PGN");
            item.addActionListener(this);
            specialMenu.add(item);

            JMenu gselect = new JMenu("select graphics");
            specialMenu.add(gselect);
            gset1 = new JRadioButtonMenuItem("set 1", true);
            gset1.addActionListener(this);
            gselect.add(gset1);
            gset2 = new JRadioButtonMenuItem("set 2", false);
            gset2.addActionListener(this);
            gselect.add(gset2);
        }

        return specialMenu;
    }

    /**
     * 
     * @see #setGraphics(ChessGraphics)
     */
    public void setStandardGraphics() {
        jboard.setStandardGraphics();
    }

    /**
     * This method allows to customize the images used for displaying
     * the chess board.
     *
     * @param icons DOCUMENT ME!
     *
     * @see #setStandardGraphics()
     * @see ChessGraphics
     */
    public void setGraphics(ChessGraphics icons) {
        // propagating the request to the class that may not be public
        jboard.setGraphics(icons);
    }

    /**
     * DOCUMENT ME!
     *
     * @param setNumber DOCUMENT ME!
     */
    void selectGraphicsSet(int setNumber) {
        jboard.setGraphics(new SampleChessGraphics());
        jboard.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public org.jscience.computing.game.JGamePlay getNewGame() {
        return super.getNewGame();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("flip board")) {
            frame.say("Menu Flip Board selected");
            jboard.repaint();

            return;
        }

        if (e.getActionCommand().equals("show coordinates")) {
            jboard.setShowCoorinates(showCoordinates.isSelected());

            if (showCoordinates.isSelected()) {
                frame.say("displaying coordinates");
            } else {
                frame.say("removing coordinates from display");
            }

            return;
        }

        if (e.getActionCommand().equals("setup board")) {
            if (setupPanel != null) {
                return;
            }

            frame.say("entering setup mode");
            pieceSelector = new JPieceSelector(this);
            setupPanel = new JDialog(frame.getFrame(), "setup board", false);
            setupPanel.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent we) {
                        pieceSelector.cancel();
                        setupPanel.dispose();
                    }
                });
            setupPanel.getContentPane().add(pieceSelector);
            setupPanel.pack();
            setupPanel.setVisible(true);
            setupPanel.requestFocus();

            try {
                setupGame = (ChessGame) getAutoPlay().getGame().clone();
            } catch (CloneNotSupportedException ex) {
                throw new Error();
            }

            return;
        }

        if (e.getActionCommand().equals("resize board to default")) {
            frame.say("resizing board to default size");
            jboard.setTileSize(jboard.defaultTileSize);
            jboard.repaint();

            return;
        }

        if (e.getActionCommand().equals("export game in PGN")) {
            frame.say("Menu Option export game in PGN selected");

            int chosen = fileChooser.showDialog(frame.getFrame(), "Export");

            if (chosen == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();

                if (f.getName().indexOf('.') == -1) {
                    f = new File(f.getAbsolutePath() + ".txt");
                }

                try {
                    PGN.export((ChessGame) play.getGame(), f.getAbsolutePath());
                    frame.say("game exported in PGN to file: " +
                        f.getAbsolutePath());
                } catch (Exception ex) {
                    frame.say(
                        "Sorry, exporting the Game didn't work out; Details follow:");
                    frame.say(ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                frame.say("PGN file export canceled by user");
            }

            return;
        }

        if (e.getActionCommand().equals("set 1")) {
            if (gset1.isSelected()) {
                selectGraphicsSet(1);
            }

            gset2.setSelected(!gset1.isSelected());
        }

        if (e.getActionCommand().equals("set 2")) {
            if (gset1.isSelected()) {
                selectGraphicsSet(2);
            }

            gset1.setSelected(!gset2.isSelected());
        }
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
    public String getStatusMessage() {
        String s = "'s turn;  ";
        s = ((play.getGame().nextPlayer() == ChessBoard.WHITE) ? "white" : "black") +
            s;
        s += super.getStatusMessage();

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
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        return "Chess";
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
        return flipped.isSelected();
    }
}
