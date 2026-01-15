/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.net;


//import org.jscience.sound.SampleSounds;
//import org.jscience.sound.SoundException;
//import org.jscience.sound.SoundPlayer;
import org.jscience.swing.JMainFrame;
import org.jscience.swing.Menus;
import org.jscience.swing.SplashScreen;

import org.jscience.util.*;
import org.jscience.util.logging.ConsoleLog;
import org.jscience.util.logging.Logger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.security.GeneralSecurityException;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.*;
import javax.swing.text.BadLocationException;


/**
 * JChat provides a simple GUI chat client that works with ChatServer. Uses
 * the <dfn>org.jscience.net.chat.port</dfn> and
 * <dfn>org.jscience.net.chat.host</dfn> property from Settings
 *
 * @author Holger Antelmann
 *
 * @see ChatServer
 * @see org.jscience.util.Settings
 */
public class JChat extends JMainFrame implements NetConnectionHandler,
    ActionListener {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -1486628635911760025L;

    /** proprietory password */
    protected static char[] passPhrase = new char[] {
            'x', '3', 'A', '&', '|', 'q', 'b', 'W', '7', '*', '*', 'h', 'a'
        };

    /** DOCUMENT ME! */
    static SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");

    /** DOCUMENT ME! */
    NetConnection con;

    /** DOCUMENT ME! */
    ChatServer server;

    /** DOCUMENT ME! */
    JMenuItem serverMenu;

    /** DOCUMENT ME! */
    JTextArea textArea;

    /** DOCUMENT ME! */
    JTextField inputText;

    /** DOCUMENT ME! */
    String callSign;

    /** DOCUMENT ME! */
    JCheckBoxMenuItem encryption;

    /** DOCUMENT ME! */
    SynchronousKey key;

/**
     * Creates a new JChat object.
     */
    public JChat() {
        super("JChat", false, true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        try {
            key = new CypherKey(passPhrase);
        } catch (GeneralSecurityException ex) {
            throw new SecurityException(ex);
        }

        setJMenuBar(createMenu());
        textArea = new JTextArea(6, 15);
        textArea.setEditable(false);
        inputText = new JTextField(10);
        inputText.addActionListener(this);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        panel.add(inputText, BorderLayout.SOUTH);
        getContentPane().add(panel, BorderLayout.CENTER);
        updateStatusText("not connected");
        callSign = System.getProperty("user.name", "unknown");
        pack();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenuItem item;
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
        item = new JMenuItem("connect");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_C);
        fileMenu.add(item);

        item = new JMenuItem("disconnect");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_D);
        fileMenu.add(item);

        item = new JMenuItem("save");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_S);
        fileMenu.add(item);

        serverMenu = new JMenuItem("start a server");
        serverMenu.addActionListener(this);
        fileMenu.add(serverMenu);

        item = new JMenuItem("close");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_C);
        fileMenu.add(item);

        item = new JMenuItem("exit");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_X);
        fileMenu.add(item);

        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        menuBar.add(editMenu);
        item = new JMenuItem("set user name");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_C);
        editMenu.add(item);

        encryption = new JCheckBoxMenuItem("use encryption", true);
        encryption.setMnemonic(KeyEvent.VK_Y);
        encryption.addActionListener(this);
        editMenu.add(encryption);

        item = new JMenuItem("select all");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_C);
        editMenu.add(item);

        item = new JMenuItem("copy");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_C);
        editMenu.add(item);

        item = new JMenuItem("clear");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_C);
        editMenu.add(item);

        JMenu winMenu = new JMenu("Window");
        winMenu.setMnemonic(KeyEvent.VK_W);
        menuBar.add(winMenu);
        winMenu.add(Menus.createLookAndFeelMenu(this));

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpMenu.add(Menus.createAboutDialogMenuItem(this));
        menuBar.add(helpMenu);

        return menuBar;
    }

    /**
     * sets the encryption key to be used if encryption is enabled
     *
     * @param key DOCUMENT ME!
     *
     * @throws IllegalStateException if the client is connected
     * @throws NullPointerException DOCUMENT ME!
     */
    public void setEncryptionKey(SynchronousKey key)
        throws IllegalStateException {
        if (con != null) {
            throw new IllegalStateException("disconnect first");
        }

        if (key == null) {
            throw new NullPointerException();
        }

        this.key = key;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("exit")) {
            System.exit(0);
        }

        if (e.getActionCommand().equals("close")) {
            if (server != null) {
                server.shutdown();
            }

            dispose();
        }

        if (e.getActionCommand().equals("save")) {
            JFileChooser fileChooser = new JFileChooser();
            int chosen = fileChooser.showSaveDialog(this);

            if (chosen == fileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                if (file.exists()) {
                    int a = JOptionPane.showConfirmDialog(JChat.this,
                            "overwrite " + file.getName() + "?");

                    if (a != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                try {
                    FileWriter fw = new FileWriter(file);
                    fw.write(textArea.getText());
                    fw.flush();
                    fw.close();
                    JOptionPane.showMessageDialog(this, "chat text saved");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error encountered",
                        "save failed", JOptionPane.ERROR_MESSAGE);
                }
            }

            return;
        }

        if (e.getActionCommand().equals("connect")) {
            if (con != null) {
                JOptionPane.showMessageDialog(this, "disconnect first");

                return;
            }

            String hostname;
            String selected = (String) JOptionPane.showInputDialog(this,
                    "Enter ChatServer hostname", "connect ..",
                    JOptionPane.PLAIN_MESSAGE, null, null,
                    Settings.getProperty("org.jscience.net.chat.host"));

            if (selected != null) {
                hostname = selected;
            } else {
                return;
            }

            int port = 0;

            try {
                port = Integer.parseInt(Settings.getProperty(
                            "org.jscience.net.chat.port"));
            } catch (NumberFormatException ex) {
            }

            selected = (String) JOptionPane.showInputDialog(this,
                    "Enter ChatServer port", "connect ..",
                    JOptionPane.PLAIN_MESSAGE, null, null, new Integer(port));

            try {
                port = Integer.parseInt(selected);
            } catch (NumberFormatException ex) {
                return;
            }

            try {
                con = SocketConnection.createConnection(hostname, port,
                        ChatServer.SIGNATURE);

                if (encryption.isSelected()) {
                    // enable simple encryption
                    con = new SecureConnection(con, key);
                }

                MessageDelegator conListener = new MessageDelegator(con, this);
                conListener.start();

                //JOptionPane.showMessageDialog(this, "Connection successful",
                //    "connected", JOptionPane.INFORMATION_MESSAGE);
                updateStatusText("connected");
                inputText.requestFocus();

                return;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Connection didn't work out", "Connection failure",
                    JOptionPane.ERROR_MESSAGE);

                if (con != null) {
                    con.close();
                }

                con = null;

                return;
            }
        }

        if (e.getActionCommand().equals("disconnect")) {
            if (con == null) {
                JOptionPane.showMessageDialog(this, "no connection active");

                return;
            }

            con.close();
            con = null;
            updateStatusText("connection lost");

            return;
        }

        if (e.getActionCommand().equals("start a server")) {
            if (server != null) {
                server.shutdown();
            }

            String s = JOptionPane.showInputDialog(this,
                    "enter chat server port: ",
                    Settings.getProperty("org.jscience.net.chat.port"));
            int p = 0;

            try {
                p = Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                return;
            }

            try {
                server = new ChatServer(p, new Logger(new ConsoleLog()));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "server could not start", JOptionPane.ERROR_MESSAGE);

                return;
            }

            server.start();
            serverMenu.setText("shutdown server");
            serverMenu.setActionCommand("shutdown server");
            JOptionPane.showMessageDialog(this, "listening at port " + p,
                "server started", JOptionPane.INFORMATION_MESSAGE);

            return;
        }

        if (e.getActionCommand().equals("shutdown server")) {
            if (server != null) {
                server.shutdown();
            }

            server = null;
            serverMenu.setText("start a server");
            serverMenu.setActionCommand("start a server");
            JOptionPane.showMessageDialog(this, "server stopped",
                "server shutdown", JOptionPane.INFORMATION_MESSAGE);

            return;
        }

        if (e.getActionCommand().equals("select all")) {
            textArea.selectAll();

            return;
        }

        if (e.getActionCommand().equals("copy")) {
            textArea.copy();

            return;
        }

        if (e.getActionCommand().equals("clear")) {
            textArea.setText(null);

            return;
        }

        if (e.getActionCommand().equals("use encryption")) {
            if (con != null) {
                JOptionPane.showMessageDialog(this,
                    "reconnect to enable new setting for received messages");
            }

            return;
        }

        if (e.getActionCommand().equals("set user name")) {
            String s = JOptionPane.showInputDialog(this, "enter your name: ",
                    callSign);

            if (s != null) {
                callSign = s;
            }

            return;
        }

        // no other action recognized; must be a new text entered in the text field
        // don't to anything if not connected
        if (con == null) {
            return;
        }

        // send the message entered
        try {
            con.sendMessage("[" + callSign + "] " + inputText.getText());
            inputText.setText(null);
        } catch (IOException ex) {
            connectionClosedByServer();
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
        updateStatusText("connection lost");
        con.close();
        con = null;
        JOptionPane.showMessageDialog(this, "Connection lost",
            "connection error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     * @param con DOCUMENT ME!
     */
    public void handleMessage(Object message, NetConnection con) {
        textArea.append(message + "\n");

        // handling special features
        try {
            //if ("beep".equals(message.toString().substring(
            //    message.toString().indexOf("]") + 2))) SoundPlayer.beep();
            //if ("deranged".equals(message.toString().substring(
            //    message.toString().indexOf("]") + 2))) new SoundPlayer(SampleSounds.DERANGED).play();
            //if ("disappointed".equals(message.toString().substring(
            //    message.toString().indexOf("]") + 2))) new SoundPlayer(SampleSounds.DISAPPOINTED).play();
            //if ("doh".equals(message.toString().substring(
            //    message.toString().indexOf("]") + 2))) new SoundPlayer(SampleSounds.DOH).play();
            //if ("message4u".equals(message.toString().substring(
            //    message.toString().indexOf("]") + 2))) new SoundPlayer(SampleSounds.MESSAGE4U).play();
            //if ("ni".equals(message.toString().substring(
            //    message.toString().indexOf("]") + 2))) new SoundPlayer(SampleSounds.NI).play();
            //if ("ring".equals(message.toString().substring(
            //    message.toString().indexOf("]") + 2))) new SoundPlayer(SampleSounds.RING).play();
            //} catch (SoundException e) {
        } catch (ResourceNotFoundException e) {
        }

        // special features end
        try {
            textArea.scrollRectToVisible(textArea.modelToView(
                    textArea.getDocument().getLength()));
        } catch (BadLocationException e) {
        }

        updateStatusText("connected (last msg at: " +
            timeFormat.format(new Date()) + "h)");
    }

    /**
     * starts a JChat instance and makes it visible
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(Debug.dialogExceptionHandler);
        SplashScreen.splash().disposeAfter(3000);

        final JChat chat1 = new JChat();

        //chat1.setDefaultCloseOperation(chat1.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    chat1.setVisible(true);
                }
            });
    }
}
