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

package org.jscience.net;

//import org.jscience.sound.SampleSounds;
//import org.jscience.sound.SoundException;
//import org.jscience.sound.SoundPlayer;

import org.jscience.swing.JFontChooser;
import org.jscience.swing.JMainFrame;
import org.jscience.swing.Menus;
import org.jscience.swing.SplashScreen;
import org.jscience.util.*;
import org.jscience.util.logging.Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * JMessenger provides a simple GUI messenger for a direct peer-to-peer
 * connection that uses one side as the server.
 * It also supports direct file transfer as a feature; note, that even if
 * the encryption is enabled, the file transfer is not (as it is easy enough
 * to encrypt the file beforehand, while it is not so trivial to encrypt the
 * file in memory if the file is larger than the VM).
 * <p/>
 * JMessenger uses the <dfn>org.jscience.net.chat.port</dfn> property from Settings
 *
 * @author Holger Antelmann
 * @see org.jscience.util.Settings
 */
public class JMessenger extends JMainFrame implements NetConnectionHandler, ConnectionDispatcher, ActionListener {
    static final long serialVersionUID = -4278520307531082834L;

    final static String SIGNATURE = "org.jscience.net.JMessenger";
    final static int SERVER_WAIT_TIME = 6000;
    NetConnectionServer server;
    NetConnection con;
    JTextArea textArea;
    JTextField inputText;
    String callSign;
    JCheckBoxMenuItem encryption;
    /**
     * a very simple encryption
     */
    SynchronousKey key = new SimpleKey();
    Logger logger;
    JFileChooser fileChooser;
    File sendFile;

    /**
     * uses  <dfn>org.jscience.net.chat.port</dfn>
     */
    public JMessenger() throws IOException {
        this(0, new Logger());
    }

    public JMessenger(int port) throws IOException {
        this(port, new Logger());
    }

    public JMessenger(Logger logger) throws IOException {
        this(0, logger);
    }

    /**
     * @param port   if 0, <dfn>org.jscience.net.chat.port</dfn> property is used
     * @param logger must not be null; use another constructor if you don't need a logger
     */
    public JMessenger(int port, Logger logger) throws IOException {
        super("JMessenger", false, true);
        if (logger == null) throw new NullPointerException();
        this.logger = logger;
        fileChooser = new JFileChooser();
        setJMenuBar(getMenu());
        textArea = new JTextArea(6, 15);
        textArea.setEditable(false);
        inputText = new JTextField(10);
        inputText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                send(e.getActionCommand());
            }
        });
        JButton button = new JButton("send file");
        button.addActionListener(this);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(button, BorderLayout.NORTH);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        panel.add(inputText, BorderLayout.SOUTH);
        getContentPane().add(panel, BorderLayout.CENTER);
        callSign = System.getProperty("user.name", "unknown");
        pack();
        if (port == 0) {
            port = Integer.parseInt(Settings.getProperty("org.jscience.net.chat.port", "0"));
        }
        server = new NetConnectionServer(port, this, logger);
        server.start();
        updateStatusText("waiting for connection");
    }

    JMenuBar getMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenuItem item;
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
        {
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

            item = new JMenuItem("close");
            item.addActionListener(this);
            item.setMnemonic(KeyEvent.VK_C);
            fileMenu.add(item);

            item = new JMenuItem("exit");
            item.addActionListener(this);
            item.setMnemonic(KeyEvent.VK_X);
            fileMenu.add(item);
        }
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        menuBar.add(editMenu);
        {
            item = new JMenuItem("set user name");
            item.addActionListener(this);
            item.setMnemonic(KeyEvent.VK_C);
            editMenu.add(item);

            encryption = new JCheckBoxMenuItem("use encryption");
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
        }
        JMenu winMenu = new JMenu("Window");
        winMenu.setMnemonic(KeyEvent.VK_W);
        menuBar.add(winMenu);
        {
            winMenu.add(Menus.createLookAndFeelMenu(this));
            item = new JMenuItem("font");
            item.setMnemonic(KeyEvent.VK_F);
            item.addActionListener(this);
            winMenu.add(item);
        }
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        {
            item = new JMenuItem("whoami");
            item.addActionListener(this);
            item.setMnemonic(KeyEvent.VK_C);
            helpMenu.add(item);
            helpMenu.add(Menus.createAboutDialogMenuItem(this));
        }
        menuBar.add(helpMenu);
        return menuBar;
    }

    /**
     * sets the encryption key to be used if encryption is enabled
     *
     * @throws IllegalStateException if the client is connected
     */
    public void setEncryptionKey(SynchronousKey key) throws IllegalStateException {
        if (con != null) {
            throw new IllegalStateException("disconnect first");
        }
        if (key == null) throw new NullPointerException();
        this.key = key;
    }

    /**
     * sets the port to listen for incoming connections (if other than default)
     */
    public void setPort(int port) throws IOException {
        server.shutdown();
        server = new NetConnectionServer(port, this, logger);
        server.start();
        updateStatusText("waiting for connection");
    }

    /**
     * overwritten to shut down the server
     */
    public void dispose() {
        server.shutdown();
        super.dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("connect")) {
            if (con != null) {
                JOptionPane.showMessageDialog(this, "disconnect first");
                return;
            }
            String hostname;
            String selected = (String) JOptionPane.showInputDialog(this,
                    "Enter hostname of other party");
            if (selected != null) hostname = selected;
            else return;
            int port = 0;
            try {
                port = Integer.parseInt(Settings.getProperty("org.jscience.net.chat.port"));
            } catch (NumberFormatException ex) {
            }
            selected = (String) JOptionPane.showInputDialog(this,
                    "Enter ChatServer port", "connect ..", JOptionPane.PLAIN_MESSAGE,
                    null, null, new Integer(port));
            try {
                port = Integer.parseInt(selected);
            } catch (NumberFormatException ex) {
                return;
            }
            try {
                if (InetAddress.getByName(hostname).equals(InetAddress.getLocalHost()) &&
                        (port == server.getLocalPort())) {
                    JOptionPane.showMessageDialog(this, "You tried to connect to yourself",
                            "Connection failure", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                con = SocketConnection.createConnection(hostname, port, SIGNATURE);
                if (encryption.isSelected()) {
                    // enable simple encryption
                    con = new SecureConnection(con, key);
                }
                MessageDelegator conListener = new MessageDelegator(con, this);
                new Thread(conListener).start();
                //JOptionPane.showMessageDialog(this, "Connection successful",
                //    "connected", JOptionPane.INFORMATION_MESSAGE);
                textArea.append("[connected to "
                        + con.getSocket().getInetAddress().getHostName() + "]\n");
                try {
                    textArea.scrollRectToVisible(textArea.modelToView(textArea.getDocument().getLength()));
                } catch (BadLocationException ex) {
                }
                updateStatusText("connected");
                inputText.requestFocus();
                return;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Connection didn't work out",
                        "Connection failure", JOptionPane.ERROR_MESSAGE);
                if (con != null) con.close();
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
            updateStatusText("waiting for connection");
            return;
        }
        if (e.getActionCommand().equals("save")) {
            fileChooser.setSelectedFile(null);
            int chosen = fileChooser.showSaveDialog(this);
            if (chosen == fileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (file.exists()) {
                    int a = JOptionPane.showConfirmDialog(JMessenger.this, "overwrite " + file.getName() + "?");
                    if (a != JOptionPane.YES_OPTION) return;
                }
                try {
                    FileWriter fw = new FileWriter(file);
                    fw.write(textArea.getText());
                    fw.flush();
                    fw.close();
                    JOptionPane.showMessageDialog(this, "chat text saved");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error encountered", "save failed", JOptionPane.ERROR_MESSAGE);
                }
            }
            return;
        }
        if (e.getActionCommand().equals("whoami")) {
            String h = null;
            try {
                h = InetAddress.getLocalHost().toString();
            } catch (IOException ex) {
            }
            JOptionPane.showMessageDialog(this, h + ", port "
                    + server.getServerSocket().getLocalPort(), "whoami",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getActionCommand().equals("exit")) {
            System.exit(0);
        }
        if (e.getActionCommand().equals("close")) {
            dispose();
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
            String s = JOptionPane.showInputDialog(this, "enter your name: ", callSign);
            if (s != null) callSign = s;
            return;
        }
        if (e.getActionCommand().equals("font")) {
            Font f = JFontChooser.showDialog(textArea, null);
            if (f != null) setWindowFont(f);
        }
        if (e.getActionCommand().equals("send file")) {
            if (con == null) {
                JOptionPane.showMessageDialog(this, "no connection active");
                return;
            }
            fileChooser.setSelectedFile(null);
            int chosen = fileChooser.showDialog(this, "transmit file");
            if (chosen == fileChooser.APPROVE_OPTION) {
                sendFile = fileChooser.getSelectedFile();
                try {
                    con.sendMessage(new FileSendRequest(sendFile.getName(), sendFile.length()));
                } catch (IOException ex) {
                }
            }
        }
    }

    public void setWindowFont(Font font) {
        textArea.setFont(font);
        inputText.setFont(font);
    }

    void send(String text) {
        if (con == null) return;
        try {
            String s = "[" + callSign + "] " + inputText.getText();
            if (encryption.isSelected()) {
                con.sendMessage(new Encoded(s, key));
            } else {
                con.sendMessage(s);
            }
            textArea.append(s + "\n");
            try {
                textArea.scrollRectToVisible(textArea.modelToView(textArea.getDocument().getLength()));
            } catch (BadLocationException e) {
            }
            inputText.setText(null);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
            connectionLost(con);
        }
    }

    /**
     * called internally to handle messages
     */
    public void handleMessage(Object message, NetConnection con) {
        if (message instanceof Encoded) {
            try {
                message = ((Encoded) message).decode(key);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "error decoding message",
                        "encryption error", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "unknown encoded message",
                        "encryption error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        // handling file sending feature
        if (message instanceof FileSendRequest) {
            FileSendRequest r = (FileSendRequest) message;
            int a = JOptionPane.showConfirmDialog(this, "receive file \""
                    + r.fileName + "\" (" + r.length + " bytes) ?", "File send request",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (a == JOptionPane.YES_OPTION) {
                fileChooser.setSelectedFile(new File(fileChooser.getCurrentDirectory(), r.fileName));
                int op = fileChooser.showSaveDialog(this);
                if ((op == fileChooser.APPROVE_OPTION) && (fileChooser.getSelectedFile() != null)) {
                    File file = fileChooser.getSelectedFile();
                    if (file.exists()) {
                        a = JOptionPane.showConfirmDialog(JMessenger.this, "overwrite " + file.getName() + "?");
                        if (a != JOptionPane.YES_OPTION) {
                            try {
                                con.sendMessage(new FileSendResponse(false, 0));
                            } catch (IOException e) {
                                connectionLost(con);
                            }
                            return;
                        }
                    }
                    try {
                        ServerSocket serverSocket = new ServerSocket(0);
                        con.sendMessage(new FileSendResponse(true,
                                serverSocket.getLocalPort()));
                        serverSocket.setSoTimeout(SERVER_WAIT_TIME);
                        Socket socket = serverSocket.accept();
                        new JFileTransmitter(this, con, socket,
                                file, r.length).start();
                        return;
                    } catch (IOException e) {
                        //connectionLost(con);
                        return;
                    }
                }
            }
            try {
                con.sendMessage(new FileSendResponse(false, 0));
            } catch (IOException e) {
                connectionLost(con);
            }
            return;
        }
        if (message instanceof FileSendResponse) {
            FileSendResponse r = (FileSendResponse) message;
            if (r.yesno) {
                new JFileTransmitter(this, sendFile,
                        con.getSocket().getInetAddress(), r.port).start();
            } else {
                JOptionPane.showMessageDialog(this, "user declined file",
                        "file transfer", JOptionPane.WARNING_MESSAGE);
            }
            return;
        }
        if (message instanceof FileSendAbort) {
            JOptionPane.showMessageDialog(this, "user aborted transfer",
                    "file transfer", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // handling sound features
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
        // sound features end
        textArea.append(message + "\n");
        try {
            textArea.scrollRectToVisible(textArea.modelToView(textArea.getDocument().getLength()));
        } catch (BadLocationException e) {
        }
    }

    /**
     * called internally to establish a connection
     */
    public Thread createHandlerThread(NetConnection connection) {
        if (con != null) {
            try {
                connection.sendMessage("[destination busy] ");
            } catch (IOException e) {
            }
            connection.close();
            return null;
        }
        String s = "do you want to accept a connection from ";
        s += connection.getSocket().getInetAddress().getHostName() + "?";
        int c = JOptionPane.showConfirmDialog(this, s, "connection request",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (c == JOptionPane.OK_OPTION) {
            if (encryption.isSelected()) {
                con = new SecureConnection(connection, key);
            } else {
                con = connection;
            }
            textArea.append("[connected to "
                    + connection.getSocket().getInetAddress().getHostName() + "]\n");
            try {
                textArea.scrollRectToVisible(textArea.modelToView(textArea.getDocument().getLength()));
            } catch (BadLocationException e) {
            }
            updateStatusText("connected");
            return new MessageDelegator(con, this, logger);
        } else {
            try {
                connection.sendMessage("[connection refused] ");
            } catch (IOException e) {
            }
            connection.close();
            return null;
        }
    }

    public Object getConnectionSignature() {
        return SIGNATURE;
    }

    /**
     * called internally to handle lost connections
     */
    public void connectionLost(NetConnection connection) {
        sendFile = null;
        if (con == null) return;
        con.close();
        con = null;
        JOptionPane.showMessageDialog(this, "Connection lost",
                "connection error", JOptionPane.WARNING_MESSAGE);
        updateStatusText("waiting for connection at port "
                + server.getServerSocket().getLocalPort());
        textArea.append("[connection lost]\n");
        try {
            textArea.scrollRectToVisible(textArea.modelToView(textArea.getDocument().getLength()));
        } catch (BadLocationException e) {
        }
    }

    public Logger getLogger() {
        return logger;
    }

    static class FileSendRequest implements Serializable {
        static final long serialVersionUID = 2858312781464944021L;
        String fileName;
        long length;

        FileSendRequest(String fileName, long length) {
            this.fileName = fileName;
            this.length = length;
        }
    }

    static class FileSendResponse implements Serializable {
        static final long serialVersionUID = -472170968792134340L;
        boolean yesno;
        int port;

        FileSendResponse(boolean yesno, int port) {
            this.yesno = yesno;
            this.port = port;
        }
    }

    static class FileSendAbort implements Serializable {
        static final long serialVersionUID = 6171202066528308823L;
    }

    /**
     * starts a JMessenger instance and makes it visible
     */
    public static void main(String[] args) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(Debug.dialogExceptionHandler);
        SplashScreen.splash().disposeAfter(3000);
        JMessenger jm;
        if (args.length < 1) {
            jm = new JMessenger();
        } else {
            int port = Integer.parseInt(args[0]);
            jm = new JMessenger(port);
        }
        jm.setDefaultCloseOperation(jm.EXIT_ON_CLOSE);
        jm.setVisible(true);
    }
}
