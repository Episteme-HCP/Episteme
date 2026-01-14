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

package org.jscience.computing.game;

import org.jscience.computing.game.GameServer.Message;

import org.jscience.net.MessageDelegator;
import org.jscience.net.NetConnection;
import org.jscience.net.NetConnectionHandler;
import org.jscience.net.SocketConnection;

import org.jscience.swing.CloseButton;
import org.jscience.swing.JMainFrame;
import org.jscience.swing.JWatchLabel;
import org.jscience.swing.Menus;

import org.jscience.util.Stopwatch;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.*;


/**
 * a GUI application acting as a client Player in a game played over the
 * network
 *
 * @author Holger Antelmann
 *
 * @see GameServer
 */
public class JPlayerClient extends JMainFrame implements ActionListener,
    GameGUI, NetConnectionHandler {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 860741347100802936L;

    /** DOCUMENT ME! */
    JGamePlay jplay;

    /** DOCUMENT ME! */
    JInternalFrame gameFrame;

    /** DOCUMENT ME! */
    JInternalFrame legalMovesFrame;

    /** DOCUMENT ME! */
    JInternalFrame historyFrame;

    /** DOCUMENT ME! */
    JInternalFrame serverMessageFrame;

    /** DOCUMENT ME! */
    DefaultListModel serverMessages;

    /** DOCUMENT ME! */
    JMoveList legalMovesList;

    /** DOCUMENT ME! */
    JMoveList historyList;

    /** DOCUMENT ME! */
    JFileChooser fileChooser;

    /** DOCUMENT ME! */
    JWatchLabel jwatch;

    /** DOCUMENT ME! */
    JLabel netStatus;

    /** DOCUMENT ME! */
    boolean requestPending;

    /** DOCUMENT ME! */
    int[] myRoles;

    /** DOCUMENT ME! */
    NetConnection con;

    /** DOCUMENT ME! */
    MessageDelegator conListener;

/**
     * this constructor uses a standard JDefaultGame object to wrap the given
     * game and then calls JGameFrame(JGamePlay jplay)
     *
     * @see JDefaultGame
     */
    public JPlayerClient(GamePlay game) {
        this(new JDefaultGame(game));
    }

/**
     * The GUI application is directly started and made visible by calling the
     * constructor
     *
     * @param jplay DOCUMENT ME!
     */
    public JPlayerClient(JGamePlay jplay) {
        super("JPlayerClient Application", false, true);
        setTitle(jplay.getTitle());
        System.out.println("Starting Holger's JPlayerGUI Application ..");
        this.jplay = jplay;
        setJMenuBar(getMenu());
        jwatch = new JWatchLabel(new Stopwatch(false));
        netStatus = new JLabel("(not connected)");
        serverMessages = new DefaultListModel();
        fileChooser = new JFileChooser();

        if (jplay.getFileFilter() != null) {
            fileChooser.addChoosableFileFilter(jplay.getFileFilter());
        }

        drawAllWindows();
    }

    /**
     * starts a JPlayerClient after selecting a game type through a GUI
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        System.out.println("select game type in pop-up window to continue ..");
        new JPlayerClient(GameUtils.selectJGamePlay()).setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    JMenuBar getMenu() {
        JMenuBar menuBar = new JMenuBar();

        //menuBar.setBackground(Color.blue);
        JMenuItem item;
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        menuBar.add(gameMenu);
        item = new JMenuItem("Connect ..");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_C);
        gameMenu.add(item);

        item = new JMenuItem("Disconnect");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_D);
        gameMenu.add(item);

        item = new JMenuItem("Save Game ..");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_S);
        gameMenu.add(item);

        item = new JMenuItem("start a server");
        item.addActionListener(this);
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

        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic(KeyEvent.VK_W);
        menuBar.add(toolsMenu);
        item = new JMenuItem("Start Messenger");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_W);
        toolsMenu.add(item);

        JMenu winMenu = new JMenu("Window");
        winMenu.setMnemonic(KeyEvent.VK_W);
        menuBar.add(winMenu);
        item = new JMenuItem("Redraw Windows");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_W);
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
     *
     * @return DOCUMENT ME!
     */
    JToolBar getToolBar() {
        JToolBar tools = new JToolBar();

        //tools.setBackground(Color.blue);
        tools.add(new JLabel("  current move time:  "));
        tools.add(jwatch);
        tools.add(new JLabel("      ")); // spacer
        tools.add(netStatus);

        return tools;
    }

    /**
     * redraws all windows with their appropriate initial size
     */
    public void drawAllWindows() {
        getContentPane().removeAll();
        getContentPane().add(statusLabel, BorderLayout.SOUTH);
        getContentPane().add(getToolBar(), BorderLayout.NORTH);
        updateStatusText(jplay.getStatusMessage());

        JDesktopPane desktop = new JDesktopPane();
        getContentPane().add(desktop, BorderLayout.CENTER);
        desktop.setBackground(Color.lightGray);
        setSize(jplay.windowWidth, jplay.windowHeight);
        setLocation(new Point(jplay.windowLocationX, jplay.windowLocationY));

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

        // creating history frame
        historyList = new JMoveList(this, JMoveList.HISTORY);
        historyFrame = new JInternalFrame("MoveHistory", true, true, true, true);
        historyFrame.setContentPane(historyList);
        historyFrame.setLocation(jplay.historyFrameX, jplay.historyFrameY);
        historyFrame.pack();
        historyFrame.setVisible(true);
        desktop.add(historyFrame);

        // creating server message frame
        serverMessageFrame = new JInternalFrame("ServerMessages", true, true,
                true, true);
        serverMessageFrame.setContentPane(new JScrollPane(
                new JList(serverMessages)));
        serverMessageFrame.setLocation(jplay.redoFrameX, jplay.redoFrameY);
        serverMessageFrame.pack();
        serverMessageFrame.setVisible(true);
        desktop.add(serverMessageFrame);

        // creating the game board frame
        gameFrame = new JInternalFrame("Game Representation", true, true, true,
                true);
        gameFrame.setContentPane(jplay.getContainer(this));
        gameFrame.pack();
        gameFrame.setLocation(jplay.gameFrameX, jplay.gameFrameY);
        gameFrame.setVisible(true);
        desktop.add(gameFrame);
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
        legalMovesList.repaint();
        historyList.repaint();
        gameFrame.repaint();
        updateStatusText(jplay.getStatusMessage());
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
     * actionPerformed listens to all the user events happening within
     * the GUI
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit")) {
            say("Exit Menu selected");
            say("bye bye");
            System.exit(0);
        }

        if (e.getActionCommand().equals("Close")) {
            say("Close Menu selected");
            say("bye bye");
            dispose();
        }

        if (e.getActionCommand().equals("Connect ..")) {
            if (con != null) {
                JOptionPane.showMessageDialog(this, "disconnect first");

                return;
            }

            String hostname;
            int port;
            String selected = JOptionPane.showInputDialog(this,
                    "Enter GameServer hostname", "connect ..",
                    JOptionPane.PLAIN_MESSAGE);

            if (selected != null) {
                hostname = selected;
            } else {
                return;
            }

            selected = JOptionPane.showInputDialog(this,
                    "Enter GameServer port", "connect ..",
                    JOptionPane.PLAIN_MESSAGE);

            try {
                port = Integer.parseInt(selected);
            } catch (NumberFormatException ex) {
                return;
            }

            try {
                con = SocketConnection.createConnection(hostname, port,
                        GameServer.SIGNATURE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Connection didn't work out", "Connection failure",
                    JOptionPane.ERROR_MESSAGE);

                return;
            }

            try {
                GamePlay netgame = (GamePlay) ((Message) con.readMessage()).content;

                if (!jplay.getAutoPlay().getGame().getClass()
                              .equals(netgame.getClass())) {
                    con.close();
                    con = null;
                    JOptionPane.showMessageDialog(this,
                        "GameServer serves the wrong game type",
                        "wrong GameServer", JOptionPane.ERROR_MESSAGE);

                    return;
                }

                int r = ((Integer) ((Message) con.readMessage()).content).intValue();
                myRoles = new int[] { r };
                conListener = new MessageDelegator(con, this);
                new Thread(conListener).start();
                JOptionPane.showMessageDialog(this,
                    "Connection successful; you play role " + myRoles[0],
                    "connected", JOptionPane.INFORMATION_MESSAGE);
                setGameFromNet(netgame);

                return;
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "game type not recognized",
                    "wrong GameServer", JOptionPane.ERROR_MESSAGE);
                con.close();
                con = null;

                return;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "No GameServer present at the given location",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
                con.close();
                con = null;

                return;
            }
        }

        if (e.getActionCommand().equals("Disconnect")) {
            if (con == null) {
                JOptionPane.showMessageDialog(this, "no connection active");

                return;
            }

            conListener.stopListening(); // reduntant; the listener will stop when the connection is closed
            conListener = null;
            con.close();
            con = null;
            requestPending = false;
            netStatus.setText("not connected");
            jwatch.getStopwatch().stop();

            return;
        }

        if (e.getActionCommand().equals("Save Game ..")) {
            //if (requestPending) {
            //    JOptionPane.showMessageDialog(this, "answer to request first");
            //    return;
            //}
            say("Save Game Menu selected");

            // GamePlay will be saved with RandomPlayer objects for serializabibity
            int chosen = fileChooser.showSaveDialog(this);

            if (chosen == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();

                if ((f.getName().indexOf('.') == -1) &&
                        (jplay.getDefaultFileExtension() != null)) {
                    f = new File(f.getAbsolutePath() + "." +
                            jplay.getDefaultFileExtension());
                }

                saveToFile(f);
            } else {
                say("file save cancelled by user");
            }

            return;
        }

        if (e.getActionCommand().equals("start a server")) {
            Thread t = new Thread() {
                    public void run() {
                        try {
                            GameServer.startServerWithGUI(new org.jscience.util.logging.Logger());
                        } catch (IOException ex) {
                        }
                    }
                };

            t.start();

            return;
        }

        if (e.getActionCommand().equals("Start Messenger")) {
            say("starting messenger");

            try {
                new org.jscience.net.JMessenger().setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Could not start Messenger", JOptionPane.ERROR_MESSAGE);
            }

            return;
        }

        if (e.getActionCommand().equals("Redraw Windows")) {
            say("Redraw Windows Menu selected");
            drawAllWindows();

            return;
        }

        if (e.getActionCommand().equals("General help")) {
            say("Menu 'General help' selected; displayin help box");

            final JDialog dialog = new JDialog(this, "General help", false);
            dialog.getContentPane()
                  .add(new JNetGameHelpText(), BorderLayout.CENTER);
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
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     * @param con DOCUMENT ME!
     */
    public void handleMessage(Object message, NetConnection con) {
        Message msg = null;

        try {
            msg = (Message) message;
        } catch (ClassCastException e) {
            return;
        }

        switch (msg.type) {
        case Message.INFO:
            serverMessages.add(0, msg.content);

            break;

        case Message.MOVE:

            GameMove move = (GameMove) msg.content;
            say("move received from network");

            // if the received move originated here, it needs
            // to be undone before reapplied
            if (GameUtils.isInArray(myRoles, move.getPlayer())) {
                jplay.getAutoPlay().getGame().undoLastMove();
            }

            if (jplay.getAutoPlay().getGame().makeMove(move)) {
                updateGameStatus();
            } else {
                try {
                    say("move from network was illegal; requesting resend");
                    con.sendMessage(new Message(Message.RESEND_GAME, null));
                } catch (IOException e) {
                    connectionClosedByServer();
                }
            }

            break;

        case Message.ILLEGALMOVE:
            JOptionPane.showMessageDialog(this,
                "you made an illegal move; game will have to be reloaded",
                "illegal move", JOptionPane.WARNING_MESSAGE);

            try {
                con.sendMessage(new Message(Message.RESEND_GAME, null));
            } catch (IOException e) {
                connectionClosedByServer();
            }

            break;

        case Message.GAME:
            say("received new game status from server");

            GamePlay netgame = (GamePlay) msg.content;
            setGameFromNet(netgame);

            break;

        default: // unrecognized message

        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param con DOCUMENT ME!
     */
    public void connectionLost(NetConnection con) {
        connectionClosedByServer();
    }

    /**
     * DOCUMENT ME!
     */
    void connectionClosedByServer() {
        netStatus.setText("connection lost");
        jwatch.getStopwatch().stop();
        con.close();
        con = null;
        conListener = null;
        JOptionPane.showMessageDialog(this, "Connection lost",
            "connection error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     */
    void setGameFromNet(GamePlay game) {
        jplay.setGame(new GameDriver(game));
        updateGameStatus();
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
     * @param text DOCUMENT ME!
     */
    public void say(String text) {
        System.out.println("[JPlayerClient Message:] " + text);
    }

    /**
     * returns the roles that this GUI client is playing
     *
     * @return DOCUMENT ME!
     */
    public int[] getGameRoles() {
        return myRoles;
    }

    /**
     * DOCUMENT ME!
     */
    private void updateGameStatus() {
        if (GameUtils.isInArray(myRoles,
                    jplay.getAutoPlay().getGame().nextPlayer())) {
            requestPending = true;
            netStatus.setText("(your move)");
        } else {
            netStatus.setText("(opponent's move)");
            requestPending = false;
        }

        jwatch.getStopwatch().restart();
        repaint();

        //org.jscience.sound.SoundPlayer.beep();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean requestGUIUndoMove() {
        // request ignored
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean requestGUIRedoMove() {
        // request ignored
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
        say("GUIMove requested");

        if (requestPending) {
            if (jplay.getAutoPlay().getGame().makeMove(move)) {
                requestPending = false;

                // send last move to server
                GamePlay game = jplay.getAutoPlay().getGame();
                GameMove last = game.getMoveHistory()[game.getMoveHistory().length -
                    1];

                try {
                    con.sendMessage(new Message(Message.MOVE, last));
                } catch (IOException e) {
                    connectionClosedByServer();
                }

                say("Legal Move performed and sent to server");
                repaint();

                return true;
            } else {
                complain("Legal Move could not be executed");

                return false;
            }
        } else {
            complain("It's not your turn to move");

            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     */
    void saveToFile(File f) {
        try {
            if (f == null) {
                return;
            }

            FileOutputStream saveFile = new FileOutputStream(f);
            ObjectOutputStream s = new ObjectOutputStream(saveFile);
            Player[] tmpPlayer = new Player[jplay.getAutoPlay().getGame()
                                                 .numberOfPlayers()];

            for (int i = 0; i < tmpPlayer.length; i++) {
                tmpPlayer[i] = jplay.getAutoPlay()
                                    .changePlayer(i,
                        new RandomPlayer(jplay.getAutoPlay().getPlayer(i)
                                              .getPlayerName()));
            }

            s.writeObject(jplay.getAutoPlay());
            s.flush();

            for (int i = 0; i < tmpPlayer.length; i++) {
                jplay.getAutoPlay().changePlayer(i, tmpPlayer[i]);
            }

            say("File saved");
        } catch (Exception ex) {
            complain("Sorry, saving the Game didn't work out; Details follow:");
            complain(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.5 $
     */
    static class JNetGameHelpText extends JScrollPane {
        /** DOCUMENT ME! */
        static final long serialVersionUID = -2953342324943121185L;

/**
         * Creates a new JNetGameHelpText object.
         */
        JNetGameHelpText() {
            super(getText());
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        static JTextArea getText() {
            String s = "Holger's JNetGame instructions\n\n";
            s += "\n";
            s += "one of these days ..\n";
            s += "\n";
            s += "For more information, select the the 'About' menu option\n";
            s += "in the help menu or choose game specific help.";

            JTextArea txt = new JTextArea(s);
            txt.setRows(20);
            txt.setColumns(40);
            txt.setEditable(false);
            txt.setCaretPosition(0);

            return txt;
        }
    }
}
