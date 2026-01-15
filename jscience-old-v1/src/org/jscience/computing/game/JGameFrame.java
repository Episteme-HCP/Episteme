/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

import org.jscience.swing.CloseButton;
import org.jscience.swing.JMainFrame;
import org.jscience.swing.JWatchLabel;
import org.jscience.swing.Menus;

import org.jscience.util.Stopwatch;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import java.io.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * JGameFrame implements a generic GUI setup for a JGamePlay. It provides
 * all the essentials for a GUI based game (like a legal move list, an undo
 * list, a redo list, standard game functionality, etc.). The gameboard uses
 * functionality of the JGamePlay to display game specific features.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.computing.game.JGamePlay
 */
public class JGameFrame extends JMainFrame implements ActionListener, GameGUI {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 1048104947372782413L;

    /** DOCUMENT ME! */
    public JCheckBoxMenuItem autoAdvance; // used to determine whether to automatically do a counter move

    /** DOCUMENT ME! */
    public JCheckBoxMenuItem showWatch;

    /** DOCUMENT ME! */
    public JCheckBoxMenuItem console;

    /** DOCUMENT ME! */
    JGamePlay jplay;

    /** DOCUMENT ME! */
    JInternalFrame gameFrame;

    /** DOCUMENT ME! */
    JInternalFrame legalMovesFrame;

    /** DOCUMENT ME! */
    JInternalFrame historyFrame;

    /** DOCUMENT ME! */
    JInternalFrame redoFrame;

    /** DOCUMENT ME! */
    JMoveList legalMovesList;

    /** DOCUMENT ME! */
    JMoveList historyList;

    /** DOCUMENT ME! */
    JMoveList redoList;

    /** DOCUMENT ME! */
    JFileChooser fileChooser;

    /** DOCUMENT ME! */
    JFileChooser bookChooser;

    /** DOCUMENT ME! */
    JToolBar tools;

    /** DOCUMENT ME! */
    boolean isBusy;

    /** DOCUMENT ME! */
    JButton autoMoveButton;

    /** DOCUMENT ME! */
    JButton randomMoveButton;

    /** DOCUMENT ME! */
    JButton backButton;

    /** DOCUMENT ME! */
    JButton forwardButton;

    /** DOCUMENT ME! */
    JButton evalButton;

    /** DOCUMENT ME! */
    JWatchLabel jwatch;

/**
     * this constructor uses a standard JDefaultGame object to wrap the given
     * game and then calls JGameFrame(JGamePlay jplay)
     *
     * @see JDefaultGame
     */
    public JGameFrame(GamePlay game) {
        this(new JDefaultGame(game));
    }

/**
     * The GUI application is directly started and made visible by calling the
     * constructor
     *
     * @param jplay DOCUMENT ME!
     */
    public JGameFrame(JGamePlay jplay) {
        super("JGameFrame Application", false, true);
        setTitle(jplay.getTitle());
        this.jplay = jplay;
        setJMenuBar(getMenu());
        fileChooser = new JFileChooser();

        if (jplay.getFileFilter() != null) {
            fileChooser.addChoosableFileFilter(jplay.getFileFilter());
        }

        bookChooser = new JFileChooser();
        isBusy = false;
        drawAllWindows();
        System.out.println("Holger's JGameFrame Application instanciated ..");
    }

    /**
     * starts a game after selecting the game type through a GUI
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        new JGameFrame(GameUtils.selectJGamePlay()).setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    JMenuBar getMenu() {
        JMenuBar menuBar = new JMenuBar();

        //menuBar.setBackground(Color.blue);
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        menuBar.add(gameMenu);

        JMenuItem item;
        item = new JMenuItem("New Game");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_N);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                InputEvent.CTRL_DOWN_MASK));
        gameMenu.add(item);

        item = new JMenuItem("Load Game ..");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_L);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
                InputEvent.CTRL_DOWN_MASK));
        gameMenu.add(item);

        item = new JMenuItem("Save Game ..");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_S);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                InputEvent.CTRL_DOWN_MASK));
        gameMenu.add(item);

        item = new JMenuItem("Info");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_I);
        gameMenu.add(item);

        item = new JMenuItem("Close");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_C);
        gameMenu.add(item);

        item = new JMenuItem("Exit");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_X);
        gameMenu.add(item);

        JMenu specialMenu = jplay.getMenu(); // game specific menu options

        if (specialMenu != null) {
            menuBar.add(specialMenu);
        }

        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        menuBar.add(viewMenu);
        showWatch = new JCheckBoxMenuItem("show time");
        showWatch.addActionListener(this);
        viewMenu.add(showWatch);

        JMenu optionMenu = new JMenu("Options");
        optionMenu.setMnemonic(KeyEvent.VK_O);
        menuBar.add(optionMenu);
        item = new JMenuItem("Select Game Level ..");
        item.setMnemonic(KeyEvent.VK_L);
        item.addActionListener(this);
        optionMenu.add(item);

        item = new JMenuItem("Set response time ..");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_R);
        optionMenu.add(item);

        item = new JMenuItem("Auto play n moves ..");
        item.setToolTipText("this may take a while depending on the game level");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_A);
        optionMenu.add(item);

        autoAdvance = new JCheckBoxMenuItem("Auto advance until same Player");
        autoAdvance.setToolTipText(
            "with this enabled, when making a move, the Application will continue to perform moves until the same player is to move again");
        autoAdvance.addActionListener(this);
        autoAdvance.setMnemonic(KeyEvent.VK_S);
        optionMenu.add(autoAdvance);

        console = new JCheckBoxMenuItem("verbose console output");
        console.addActionListener(this);
        console.setMnemonic(KeyEvent.VK_V);
        optionMenu.add(console);

        JMenu winMenu = new JMenu("Window");
        winMenu.setMnemonic(KeyEvent.VK_W);
        menuBar.add(winMenu);
        item = new JMenuItem("Redraw frames");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_R);
        winMenu.add(item);
        winMenu.add(Menus.createLookAndFeelMenu(this));

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpMenu.add(Menus.createAboutDialogMenuItem(this));

        item = new JMenuItem("General help");
        item.addActionListener(this);
        helpMenu.add(item);

        item = new JMenuItem(jplay.getTitle() + " help");
        item.setActionCommand("Game specific help");
        item.addActionListener(this);
        helpMenu.add(item);
        menuBar.add(helpMenu);

        return menuBar;
    }

    /**
     * DOCUMENT ME!
     */
    void initTools() {
        tools = new JToolBar();

        //tools.setBackground(Color.blue);
        autoMoveButton = makeButton("AutoMove", KeyEvent.VK_A);
        randomMoveButton = makeButton("RandomMove", KeyEvent.VK_R);
        backButton = makeButton("Back", KeyEvent.VK_B);
        forwardButton = makeButton("Forward", KeyEvent.VK_F);
        evalButton = makeButton("Evaluate Move", KeyEvent.VK_E);
        jwatch = new JWatchLabel();
        tools.add(autoMoveButton);
        tools.add(randomMoveButton);
        tools.add(new JLabel("  ")); // spacer
        tools.add(backButton);
        tools.add(forwardButton);
        tools.add(new JLabel("  ")); // spacer
        tools.add(evalButton);
        tools.add(new JLabel("      ")); // spacer
        tools.add(jwatch);
        jwatch.setVisible(showWatch.isSelected());

        if (showWatch.isSelected()) {
            jwatch.getTimer().start();
        } else {
            jwatch.getTimer().stop();
        }

        tools.setVisible(true);
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
        myButton.setMnemonic(key);
        myButton.addActionListener(this);
        myButton.setMargin(new Insets(0, 0, 0, 0));

        return myButton;
    }

    /**
     * redraws all windows with their appropriate initial size
     */
    public void drawAllWindows() {
        getContentPane().removeAll();
        getContentPane().add(statusLabel, BorderLayout.SOUTH);

        JDesktopPane desktop = new JDesktopPane();
        initTools();
        getContentPane().add(tools, BorderLayout.NORTH);
        getContentPane().add(desktop, BorderLayout.CENTER);
        desktop.setBackground(Color.gray);
        setSize(jplay.windowWidth, jplay.windowHeight);
        setLocation(new Point(jplay.windowLocationX, jplay.windowLocationY));

        if (verbose) {
            console.setSelected(true);
        } else {
            console.setSelected(false);
        }

        // creating legal moves frame
        legalMovesList = new JMoveList(this, JMoveList.LEGAL);
        legalMovesFrame = new JInternalFrame("LegalMoves", true, true, true,
                true);
        legalMovesFrame.setContentPane(legalMovesList);
        legalMovesFrame.setLocation(jplay.legalMovesFrameX,
            jplay.legalMovesFrameY);
        legalMovesFrame.pack();
        legalMovesFrame.setVisible(true);
        desktop.add(legalMovesFrame);
        legalMovesList.getJList().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    evalButton.setEnabled(true);
                }
            });

        // creating history frame
        historyList = new JMoveList(this, JMoveList.HISTORY);
        historyFrame = new JInternalFrame("MoveHistory", true, true, true, true);
        historyFrame.setContentPane(historyList);
        historyFrame.setLocation(jplay.historyFrameX, jplay.historyFrameY);
        historyFrame.pack();
        historyFrame.setVisible(true);
        desktop.add(historyFrame);

        // creating redo frame
        redoList = new JMoveList(this, JMoveList.REDO);
        redoFrame = new JInternalFrame("RedoList", true, true, true, true);
        redoFrame.setContentPane(redoList);
        redoFrame.setLocation(jplay.redoFrameX, jplay.redoFrameY);
        redoFrame.pack();
        redoFrame.setVisible(true);
        desktop.add(redoFrame);

        // creating the game board frame
        gameFrame = new JInternalFrame("Game Representation", true, true, true,
                true);
        gameFrame.setContentPane(jplay.getContainer(this));
        gameFrame.pack();
        gameFrame.setLocation(jplay.gameFrameX, jplay.gameFrameY);
        gameFrame.setVisible(true);
        desktop.add(gameFrame);
        redraw();
    }

    /**
     * overwritten to update all component windows with updated game
     * info
     */
    public void repaint() {
        redraw();
        super.repaint();
    }

    /**
     * DOCUMENT ME!
     */
    void redraw() {
        updateStatusText(jplay.getStatusMessage());

        //if (isBusy) updateStatusText("busy.. please wait ..");
        legalMovesList.repaint();
        historyList.repaint();
        redoList.repaint();
        evalButton.setEnabled(false);

        if (legalMovesList.getJList().getModel().getSize() == 0) {
            autoMoveButton.setEnabled(false);
            randomMoveButton.setEnabled(false);
        } else {
            autoMoveButton.setEnabled(true);
            randomMoveButton.setEnabled(true);
        }

        if (historyList.getJList().getModel().getSize() == 0) {
            backButton.setEnabled(false);
        } else {
            backButton.setEnabled(true);
        }

        if (redoList.getJList().getModel().getSize() == 0) {
            forwardButton.setEnabled(false);
        } else {
            forwardButton.setEnabled(true);
        }
    }

    /**
     * actionPerformed listens to all the user events happening within
     * the GUI
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit")) {
            say("Exit Menu selected");
            say("stopping VM ..");
            System.exit(0);
        }

        if (e.getActionCommand().equals("Close")) {
            say("Close Menu selected");
            say("bye bye");
            dispose();
        }

        if (e.getActionCommand().equals("New Game")) {
            if (isBusy) {
                return;
            }

            say("New Game Menu selected");
            jplay = jplay.getNewGame();
            repaint();

            return;
        }

        if (e.getActionCommand().equals("Save Game ..")) {
            //if (isBusy) return;
            say("Save Game Menu selected");

            int chosen = fileChooser.showSaveDialog(this);

            if (chosen == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();

                if ((f.getName().indexOf('.') == -1) &&
                        (jplay.getDefaultFileExtension() != null)) {
                    f = new File(f.getAbsolutePath() + "." +
                            jplay.getDefaultFileExtension());
                }

                try {
                    FileOutputStream saveFile = new FileOutputStream(f);
                    ObjectOutputStream s = new ObjectOutputStream(saveFile);
                    s.writeObject(jplay.getAutoPlay());
                    s.flush();
                    say("File saved");
                    updateStatusText("file saved");
                } catch (Exception ex) {
                    complain(
                        "Sorry, saving the Game didn't work out; Details follow:");
                    complain(ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                say("file save cancelled by user");
            }

            return;
        }

        if (e.getActionCommand().equals("Info")) {
            say("Game Info selected");

            final JDialog dialog = new JDialog(this, "Game Info", true);
            String s = "Game information:";
            s += "\n-----------------------------------------\n";
            s += jplay.getAutoPlay().toString();
            s += "\n-----------------------------------------\n";
            s += "Player information:\n";

            for (int i = 0;
                    i < jplay.getAutoPlay().getGame().numberOfPlayers(); i++) {
                s += ("role " + i + ": " + jplay.getAutoPlay().getPlayer(i) +
                "\n");
            }

            s += "\n-----------------------------------------\n";
            s += "Player statistics - if instance of TemplatePlayer:\n";

            for (int i = 0;
                    i < jplay.getAutoPlay().getGame().numberOfPlayers(); i++) {
                s += ("role " + i + ": ");

                try {
                    TemplatePlayer tp = (TemplatePlayer) jplay.getAutoPlay()
                                                              .getPlayer(i);
                    s += tp.statsAsString();
                } catch (ClassCastException ex) {
                    s += "not a TemplatePlayer object";
                }

                s += "\n";
            }

            s += "\n-----------------------------------------\n";

            int[] win = jplay.getAutoPlay().getGame().getWinner();

            if (win != null) {
                s += "Winners: ";

                if (win.length == 0) {
                    s += "none ";
                }

                for (int i = 0; i < win.length; i++) {
                    if (i != 0) {
                        s += ", ";
                    }

                    s += jplay.getAutoPlay().getPlayer(win[i]).getPlayerName();
                }
            } else {
                s += "no winner info";
            }

            s += "\n-----------------------------------------\n";
            s += ("moves performed: " +
            jplay.getAutoPlay().getGame().getMoveHistory().length);
            s += ("\navailable legal moves: " +
            jplay.getAutoPlay().getGame().getLegalMoves().length);
            s += ("\navailable redo moves: " +
            jplay.getAutoPlay().getGame().getRedoList().length);
            s += "\n-----------------------------------------\n";
            s += "Move list:\n";

            for (int i = 0;
                    i < jplay.getAutoPlay().getGame().getMoveHistory().length;
                    i++) {
                s += (jplay.getAutoPlay().getGame().getMoveHistory()[i] + "\n");
            }

            JTextArea txt = new JTextArea(s);
            txt.setCaretPosition(0);
            txt.setEditable(false);
            txt.setRows(15);
            txt.setColumns(50);
            dialog.getContentPane()
                  .add(new JScrollPane(txt), BorderLayout.CENTER);
            dialog.getContentPane()
                  .add(new CloseButton(dialog), BorderLayout.SOUTH);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);

            return;
        }

        if (e.getActionCommand().equals("Load Game ..")) {
            if (isBusy) {
                return;
            }

            say("Load Game Menu selected");

            //fileChooser = new JFileChooser();
            //fileChooser.addChoosableFileFilter(new ChessFileFilter());
            int chosen = fileChooser.showOpenDialog(this);

            if (chosen == JFileChooser.APPROVE_OPTION) {
                try {
                    FileInputStream saveFile = new FileInputStream(fileChooser.getSelectedFile());
                    ObjectInputStream s = new ObjectInputStream(saveFile);
                    jplay = jplay.setGame((AutoPlay) s.readObject());
                    say("File loaded");
                    drawAllWindows();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                        "file could not be opened", "file error",
                        JOptionPane.ERROR_MESSAGE);
                    complain(
                        "Sorry, loading the Game didn't work; Details follow:");
                    complain(ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                say("load game cancelled by user");
            }

            repaint();

            return;
        }

        if (e.getActionCommand().equals("show time")) {
            jwatch.setVisible(showWatch.isSelected());

            if (showWatch.isSelected()) {
                jwatch.getTimer().start();
            } else {
                jwatch.getTimer().stop();
            }

            repaint();

            return;
        }

        if (e.getActionCommand().equals("Set response time ..")) {
            say("Menu: set response time selected; current time in sec: " +
                (jplay.getAutoPlay().getResponseTime() / 1000));

            String selected = JOptionPane.showInputDialog(this,
                    "Enter desired response time in seconds (0 for no limit):",
                    "current response time: " +
                    (jplay.getAutoPlay().getResponseTime() / 1000),
                    JOptionPane.QUESTION_MESSAGE);
            int number = 0;

            try {
                number = Integer.parseInt(selected);
            } catch (NumberFormatException ex) {
                complain("wrong input; selection ignored - try again");

                return;
            }

            jplay.getAutoPlay().setResponseTime(number * 1000);
            say("new response time (sec.): " + number);

            return;
        }

        if (e.getActionCommand().equals("verbose console output")) {
            if (console.isSelected()) {
                verbose = true;
            } else {
                verbose = false;
            }

            return;
        }

        if (e.getActionCommand().equals("Select Game Level ..")) {
            say("Menu Select Game Level chosen (current level: " +
                jplay.getAutoPlay().getLevel() + ")");

            //JOptionPane.showMessageDialog(this,"selecting a new game level");
            final JDialog dialog = new JDialog(this, "Selecting Game Level",
                    true);
            SpinnerNumberModel model = new SpinnerNumberModel(jplay.getAutoPlay()
                                                                   .getLevel(),
                    0, 100, 1);
            JSpinner spinner = new JSpinner(model);
            spinner.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        JSpinner source = (JSpinner) e.getSource();
                        jplay.getAutoPlay()
                             .setLevel(((Integer) source.getValue()).intValue());
                        say("Game Level set to: " +
                            jplay.getAutoPlay().getLevel());
                    }
                });

            JButton button = new JButton("ok");
            button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        dialog.dispose();
                    }
                });
            dialog.getContentPane().add(button, BorderLayout.EAST);
            dialog.getContentPane().add(spinner, BorderLayout.CENTER);
            dialog.setLocationRelativeTo(this);
            dialog.pack();
            dialog.setVisible(true);

            return;
        }

        if (e.getActionCommand().equals("Auto advance until same Player")) {
            if (isBusy) {
                return;
            }

            if (autoAdvance.isSelected()) {
                say("Menu Option autoAdvance enabled");
            } else {
                say("Menu Option autoAdvance disabled");
            }

            return;
        }

        if (e.getActionCommand().equals("Evaluate Move")) {
            if (isBusy) {
                return;
            }

            isBusy = true;
            say("Button 'Evaluate Move' pressed");

            GameMove m = (GameMove) legalMovesList.getJList().getSelectedValue();

            if (m != null) {
                Stopwatch t = new Stopwatch();
                double h = jplay.getAutoPlay().evaluateMove(m);
                say("analysis done");

                String s = "<html>heuristic for move: " + m + "<br>" + h +
                    "<br>";
                s += ("(game level: " + jplay.getAutoPlay().getLevel() +
                ")<br>");
                s += ("time taken: " + t.elapsedAsString() + "<br>");
                s += ("(provided by Player: " +
                jplay.getAutoPlay().getPlayer(m.getPlayer()).getPlayerName() +
                ")");
                JOptionPane.showMessageDialog(this, s, "Move Statistic",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "You need to select a move from the legal moves list to perform this option",
                    "No Move Selected", JOptionPane.WARNING_MESSAGE);
            }

            isBusy = false;

            return;
        }

        if (e.getActionCommand().equals("AutoMove")) {
            if (isBusy) {
                return;
            }

            isBusy = true;
            say("AutoMove Button pressed");

            if (jplay.getAutoPlay().getGame().getLegalMoves().length == 0) {
                say("There are no legal moves left.");
                isBusy = false;
            } else {
                MoveThread t = new MoveThread();
                t.start();
                updateStatusText("performing autoMove (please wait ..)");
            }

            //repaint();
            return;
        }

        if (e.getActionCommand().equals("Auto play n moves ..")) {
            if (isBusy) {
                return;
            }

            say("'Auto play n moves' selected; current game level: " +
                jplay.getAutoPlay().getLevel());

            String selected = JOptionPane.showInputDialog(this,
                    "Please select how many moves you want to advance:",
                    "AutoPlay at level " + jplay.getAutoPlay().getLevel(),
                    JOptionPane.QUESTION_MESSAGE);
            int number = 0;

            try {
                number = Integer.parseInt(selected);
            } catch (NumberFormatException ex) {
            }

            if (number == 0) {
                say("user selected 0 for auto play - nothing to do");

                return;
            }

            say("performing " + number + " auto moves as user selected");

            Stopwatch t = new Stopwatch();
            isBusy = true;

            AutoplayThread at = new AutoplayThread(number, t);
            at.start();
            updateStatusText("performing " + number +
                " auto moves; stand by..");

            return;
        }

        if (e.getActionCommand().equals("Back")) {
            if (isBusy) {
                return;
            }

            say("Back Button pressed");
            say("move taken back: " +
                jplay.getAutoPlay().getGame().undoLastMove());
            repaint();

            return;
        }

        if (e.getActionCommand().equals("Forward")) {
            if (isBusy) {
                return;
            }

            say("Forward Button pressed");
            say("move re-done: " + jplay.getAutoPlay().getGame().redoMove());
            repaint();

            return;
        }

        if (e.getActionCommand().equals("RandomMove")) {
            if (isBusy) {
                return;
            }

            say("RandomMove Button pressed");

            GameMove m = jplay.getAutoPlay().getRandomLegalMove();

            if (m != null) {
                jplay.getAutoPlay().getGame().makeMove(m);
            }

            say("random move performed: " + m);
            repaint();

            return;
        }

        if (e.getActionCommand().equals("Redraw frames")) {
            say("Redraw Windows Menu selected");
            drawAllWindows();

            return;
        }

        if (e.getActionCommand().equals("General help")) {
            say("Menu 'General help' selected; displayin help box");

            final JDialog dialog = new JDialog(this, "General help", false);
            dialog.getContentPane().add(new HelpText(), BorderLayout.CENTER);
            dialog.getContentPane()
                  .add(new CloseButton(dialog), BorderLayout.SOUTH);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);

            return;
        }

        if (e.getActionCommand().equals("Game specific help")) {
            say("Menu 'Game specific help' selected; displayin help box");

            final JDialog dialog = new JDialog(this,
                    jplay.getTitle() + " help", false);
            Component help = jplay.getHelp();

            if (help == null) {
                help = new JLabel("Sorry, no game specific help available.");
            }

            dialog.getContentPane().add(help, BorderLayout.CENTER);
            dialog.getContentPane()
                  .add(new CloseButton(dialog), BorderLayout.SOUTH);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);

            return;
        }
    }

    /**
     * sets the 'Auto advance' option that can also be set through the
     * menu
     *
     * @param on DOCUMENT ME!
     */
    public void setAutoAdvance(boolean on) {
        autoAdvance.setSelected(on);
        repaint();
    }

    /**
     * enables to load an AutoPlay object after instanciation without
     * going through the GUI; note that this is not a synchronized operation
     *
     * @param play DOCUMENT ME!
     */
    public void load(AutoPlay play) {
        jplay = jplay.setGame(play);
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.awt.Frame getFrame() {
        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JGamePlay getJGamePlay() {
        return jplay;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean requestGUIUndoMove() {
        if (isBusy) {
            return false;
        }

        isBusy = true;
        say("GUIUndoMove requested");

        if (jplay.getAutoPlay().getGame().undoLastMove()) {
            repaint();
            isBusy = false;

            return true;
        }

        isBusy = false;

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean requestGUIRedoMove() {
        if (isBusy) {
            return false;
        }

        isBusy = true;
        say("GUIRedoMove requested");

        if (jplay.getAutoPlay().getGame().redoMove()) {
            repaint();
            isBusy = false;

            return true;
        }

        isBusy = false;

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean requestGUIMove(GameMove move) {
        if (isBusy) {
            return false;
        }

        isBusy = true;
        say("GUIMove requested");

        if (jplay.getAutoPlay().getGame().makeMove(move)) {
            say("Legal Move performed");
            repaint();

            if (autoAdvance.isSelected()) {
                new AutoAdvanceThread(move.getPlayer()).start();
                updateStatusText("autoAdvance .. please wait ..");
            }

            isBusy = false;

            return true;
        } else {
            complain("Legal Move could not be executed");
            isBusy = false;

            return false;
        }
    }

    /**
     * if this function returns true, the JGameFrame is currently
     * running some analysis, which suggests that other event listening
     * components should not alter the game status at this time
     *
     * @return DOCUMENT ME!
     */
    public boolean isBusy() {
        return isBusy;
    }

    /**
     * presets the directory for the file selection
     *
     * @param directoryLocation DOCUMENT ME!
     */
    public void setInitialDirectory(String directoryLocation) {
        fileChooser.setCurrentDirectory(new File(directoryLocation));
        bookChooser.setCurrentDirectory(new File(directoryLocation));
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.5 $
     */
    class AutoAdvanceThread extends Thread {
        /** DOCUMENT ME! */
        int player;

/**
         * Creates a new AutoAdvanceThread object.
         *
         * @param player DOCUMENT ME!
         */
        AutoAdvanceThread(int player) {
            this.player = player;
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            say("auto advancing ..");
            isBusy = true;

            GameMove m;
            Stopwatch t = new Stopwatch();

            while (JGameFrame.this.jplay.getAutoPlay().getGame().nextPlayer() != player) {
                t.restart();
                m = jplay.getAutoPlay().autoMove();
                say("move performed: " + m + "; time taken: " +
                    t.elapsedAsString());
                repaint();
                updateStatusText("autoAdvance .. please wait ..");

                if (m == null) {
                    break;
                }
            }

            java.awt.Toolkit.getDefaultToolkit().beep();
            isBusy = false;
            repaint();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.5 $
     */
    class MoveThread extends Thread {
/**
         * Creates a new MoveThread object.
         */
        MoveThread() {
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            isBusy = true;

            String tmp = "Delegating operation to Player: ";
            tmp = tmp +
                jplay.getAutoPlay()
                     .getPlayer(jplay.getAutoPlay().getGame().nextPlayer())
                     .getPlayerName();
            say(tmp);

            Stopwatch t = new Stopwatch();
            GameMove m = jplay.getAutoPlay().autoMove();
            say("moving: " + m + "; time taken: " + t.elapsedAsString());
            java.awt.Toolkit.getDefaultToolkit().beep();
            repaint();
            isBusy = false;
            repaint();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.5 $
     */
    class AutoplayThread extends Thread {
        /** DOCUMENT ME! */
        int number;

        /** DOCUMENT ME! */
        Stopwatch t;

/**
         * Creates a new AutoplayThread object.
         *
         * @param number DOCUMENT ME!
         * @param t      DOCUMENT ME!
         */
        AutoplayThread(int number, Stopwatch t) {
            this.number = number;
            this.t = t;
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            isBusy = true;

            Stopwatch tmp = new Stopwatch(false);
            AutoPlay play = jplay.getAutoPlay();
            ProgressMonitor jmon = new ProgressMonitor(JGameFrame.this,
                    "Performing " + number + " moves (level " +
                    play.getLevel() + ", resp.time: " +
                    (play.getResponseTime() / 1000) + ")",
                    "waiting for Player.autoMove()..", 0, number);

            for (int i = 0; (i < number) && (!jmon.isCanceled()); i++) {
                tmp.start();

                GameMove m = play.autoMove();
                String s = "move: " + m + "; time: " + tmp.elapsedAsString();
                jmon.setProgress(i);
                jmon.setNote(s);
                s = "move: " + m + "; total time: " + t.elapsedAsString();
                say(s);

                if (m == null) {
                    break;
                }

                repaint();
            }

            say("'Auto play n moves' is done");
            jmon.close();
            java.awt.Toolkit.getDefaultToolkit().beep();
            isBusy = false;
            repaint();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.5 $
     */
    static class HelpText extends JScrollPane {
        /** DOCUMENT ME! */
        static final long serialVersionUID = -5310289311842406050L;

/**
         * Creates a new HelpText object.
         */
        HelpText() {
            super(getText());
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        static JTextArea getText() {
            String s = "Holger's JGameFrame instructions\n\n";
            s += "1. Menu\n\n";
            s += "Most of the menu items should be self-explanatory,\n";
            s += "there may be a game specific menu option between\n";
            s += "the 'Game' and the 'Options' menu; for help on that,\n";
            s += "please see the game specific help under the 'Help' menu.\n\n";
            s += "2. Toolbar\n\n";
            s += "The toolbar lets you perform standard operations on the\n";
            s += "game such as performing an AutoMove (which will delegate)\n";
            s += "the selection of a move to the Player object associated\n";
            s += "with the game role of the next player in the game)\n";
            s += "or performing a random move.\n";
            s += "You can also move back&forth within the list of available\n";
            s += "moves provided that the MoveHistory and/or RedoList are not\n";
            s += "empty\n\n";
            s += "3. Application desktop\n\n";
            s += "There are 4 windows displayed on the application desktop:\n";
            s += "a) the game representation (displaying the current game status)\n";
            s += "b) the list of legal moves currently available\n";
            s += "c) a list of moves that have been performed on the game\n";
            s += "d) a list of moves that have been taken back and can be reapplied\n\n";
            s += "The functionality of the Game Representation window depends on\n";
            s += "the implementation of the game at hand; see the game specific\n";
            s += "help for more information\n";
            s += "You can advance the game and perform a move by simply double-clicking\n";
            s += "on the desired legal move\n";
            s += "The same double-click ability is available for taking back several\n";
            s += "moves through the MoveHistory list or reapplying moves in the\n";
            s += "RedoList\n";
            s += "\n";
            s += "\n";
            s += "For more information, select the the 'About' menu option\n";
            s += "in the help menu or choose game specific help.";

            JTextArea txt = new JTextArea(s);
            txt.setRows(20);
            txt.setColumns(40);
            txt.setCaretPosition(0);
            txt.setEditable(false);

            return txt;
        }
    }
}
