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

import org.jscience.util.Settings;
import org.jscience.util.logging.ConsoleLog;
import org.jscience.util.logging.Level;
import org.jscience.util.logging.Logger;

import java.io.IOException;

import java.util.List;
import java.util.Vector;


/**
 * ChatServer implements a server that handles chat messages that are
 * broadcasted among the active connections to the server.
 *
 * @author Holger Antelmann
 *
 * @see JChat
 */
public class ChatServer extends Thread implements ConnectionDispatcher,
    NetConnectionHandler {
    /**
     * DOCUMENT ME!
     */
    public final static String SIGNATURE = "org.jscience.net.ChatServer connection signature";

    /**
     * DOCUMENT ME!
     */
    NetConnectionServer server;

    /**
     * DOCUMENT ME!
     */
    int serverPort;

    /**
     * DOCUMENT ME!
     */
    List<NetConnection> connections;

    /**
     * DOCUMENT ME!
     */
    Logger logger;

    /**
     * Creates a new ChatServer object.
     *
     * @param serverPort DOCUMENT ME!
     * @param logger DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public ChatServer(int serverPort, Logger logger) throws IOException {
        super("ChatServer");
        this.logger = logger;
        this.serverPort = serverPort;
        connections = new Vector<NetConnection>();
        server = new NetConnectionServer(serverPort, this, logger);
    }

    /**
     * DOCUMENT ME!
     *
     * @param con DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized Thread createHandlerThread(final NetConnection con) {
        if (!SIGNATURE.equals(con.getSignature())) {
            return null;
        }

        MessageDelegator listener = new MessageDelegator(con, this);
        connections.add(con);
        broadcast("[Server] new connection from " +
            con.getSocket().getInetAddress().getHostName());
        broadcast("[Server] there are " + connections.size() +
            " active connections");

        try {
            con.sendMessage("[Server] welcome; there are " +
                connections.size() + " active connections");
        } catch (IOException e) {
            // do nothing in this case
        }

        return listener;
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    synchronized void broadcast(Object message) {
        for (int i = 0; i < connections.size(); i++) {
            try {
                ((NetConnection) connections.get(i)).sendMessage(message);
            } catch (IOException e) {
                connectionLost((NetConnection) connections.get(i));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param con DOCUMENT ME!
     */
    public synchronized void connectionLost(NetConnection con) {
        if (logger != null) {
            logger.log(this, Level.WARNING, "connection closed",
                new Object[] { con });
        }

        connections.remove(con);
        broadcast("[Server] lost connection to  " +
            con.getSocket().getInetAddress().getHostName());
        broadcast("[Server] there are " + connections.size() +
            " active connections");
        con.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getConnectionSignature() {
        return SIGNATURE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     * @param con DOCUMENT ME!
     */
    public synchronized void handleMessage(Object message, NetConnection con) {
        if (logger != null) {
            logger.log(this, Level.FINER, "handling message",
                new Object[] { message, con });
        }

        broadcast(message);
    }

    /**
     * DOCUMENT ME!
     */
    public void shutdown() {
        broadcast("ChatServer is shutting down; bye bye ..");

        for (int i = 0; i < connections.size(); i++) {
            ((NetConnection) connections.get(i)).close();
        }

        server.shutdown();
    }

    /**
     * starts the underlying server
     */
    public void run() {
        server.start();
    }

    /**
     * returns the port the server is listening on
     *
     * @return DOCUMENT ME!
     */
    public int getLocalPort() {
        return server.getLocalPort();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NetConnection[] getConnections() {
        return (NetConnection[]) connections.toArray(new NetConnection[connections.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * uses the <dfn>org.jscience.net.chat.port</dfn> property from
     * Settings
     *
     * @see org.jscience.util.Settings
     */
    public static void startServerWithGUI(Logger logger)
        throws IOException {
        //if (logger == null) logger = new Logger();
        int serverPort = 0;

        try {
            serverPort = Integer.parseInt(Settings.getProperty(
                        "org.jscience.net.chat.port"));
        } catch (NumberFormatException ex) {
        }

        String s = javax.swing.JOptionPane.showInputDialog("enter server port:",
                new Integer(serverPort));

        try {
            serverPort = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return;
        }

        ChatServer server = new ChatServer(serverPort, logger);
        server.start();

        org.jscience.swing.JMainFrame frame = new org.jscience.swing.JMainFrame(new javax.swing.JLabel(
                    "listening at port " + serverPort), true, true);
        frame.pack();
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        System.out.println("Stop server by closing the window");
        System.out.println("ChatServer started");

        while (true) {
            frame.updateStatusText("active connections: " +
                server.connections.size());
            org.jscience.util.MiscellaneousUtils.pause(100);
        }
    }

    /**
     * starts a ChatServer after making some GUI selections
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            Logger logger = new Logger(new ConsoleLog());

            if (args.length > 0) {
                logger = new Logger(new org.jscience.util.logging.LogFile(
                            new java.io.File(args[0])));
            }

            startServerWithGUI(logger);
            System.out.println(
                "select options in the pop-up window  to continue..");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
