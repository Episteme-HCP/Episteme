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

import org.jscience.net.*;

import org.jscience.util.logging.ConsoleLog;
import org.jscience.util.logging.Level;
import org.jscience.util.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;


/**
 * A GameServer hosts a GamePlay object that clients can play remotely by
 * connecting to it and following this server's protocol. JPlayerClient is
 * known to work with this server. Every GamePlay role is represented by a
 * different NetConnection running in a different thread. The GameServer hosts
 * a single game and then closes all connections once the game is over (i.e.
 * when there are no more legal moves available); if you want to play another
 * game, you need a new GameServer. The GameServer listens to new connections
 * only if there are still game roles left that are not taken by a connection.
 * The GameServer feature reconnects; i.e. if a client disconnects, the server
 * is restarted to allow a new client to take that role - until all game roles
 * are connected again.
 *
 * @author Holger Antelmann
 *
 * @see GamePlay
 * @see JPlayerClient
 */
public class GameServer extends Thread implements ConnectionDispatcher,
    NetConnectionHandler {
    /** this signature must be used when creating connections to this server */
    public final static String SIGNATURE = "org.jscience.computing.game.GameServer connection";

    /** DOCUMENT ME! */
    NetConnectionServer server;

    /** DOCUMENT ME! */
    int timeout;

    /** DOCUMENT ME! */
    int serverPort;

    /** DOCUMENT ME! */
    GamePlay game;

    /** DOCUMENT ME! */
    NetConnection[] playerCon;

    /** DOCUMENT ME! */
    Logger logger;

/**
     * Creates a new GameServer object.
     *
     * @param serverPort DOCUMENT ME!
     * @param game       DOCUMENT ME!
     * @param timeout    DOCUMENT ME!
     * @param logger     DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public GameServer(int serverPort, GamePlay game, int timeout, Logger logger)
        throws IOException {
        super("GameServer");
        this.logger = logger;
        this.game = game;
        this.serverPort = serverPort;
        this.timeout = timeout;
        playerCon = new NetConnection[game.numberOfPlayers()];
        server = new NetConnectionServer(serverPort, this, logger);
        server.getServerSocket().setSoTimeout(timeout);
    }

    /**
     * starts a GameServer after some GUI selections
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            Logger logger = new Logger();

            if (args.length > 0) {
                logger.addWriter(new org.jscience.util.logging.LogFile(
                        new File(args[0])));
            } else {
                logger.addWriter(new ConsoleLog());
            }

            startServerWithGUI(logger);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * a conveninent way to start a GameServer with a little GUI;
     *
     * @param logger DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void startServerWithGUI(Logger logger)
        throws IOException {
        //if (logger == null) logger = new Logger(new ConsoleLog());
        System.out.println("select options in the pop-up window  to continue..");

        int serverPort = 12345;
        String s = javax.swing.JOptionPane.showInputDialog("enter server port:",
                "12345");

        try {
            serverPort = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return;
        }

        GamePlay game = GameUtils.selectJGamePlay().getAutoPlay().getGame();
        new GameServer(serverPort, game, 0, logger).start();

        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new javax.swing.BoxLayout(panel,
                javax.swing.BoxLayout.Y_AXIS));
        panel.add(new javax.swing.JLabel((game.getGameName() + " server on " +
                java.net.InetAddress.getLocalHost().getHostName() +
                " at port " + serverPort)));
        panel.add(new javax.swing.JLabel("close this window to stop server"));

        org.jscience.swing.JMainFrame frame = new org.jscience.swing.JMainFrame(panel,
                true, true);
        frame.setVisible(true);
        System.out.println(
            "note: server plays one game only; start a new one for another game");
        System.out.println("server started");

        while (true) {
            org.jscience.util.MiscellaneousUtils.pause(500);
            frame.updateStatusText("number of moves: " +
                game.getMoveHistory().length);

            if (game.getLegalMoves().length == 0) {
                frame.updateStatusText("game over; you can close this window");

                break;
            }
        }
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

        for (int i = 0; (i < playerCon.length); i++) {
            if (playerCon[i] == null) {
                playerCon[i] = con;

                try {
                    con.sendMessage(new Message(Message.GAME, game));
                    con.sendMessage(new Message(Message.ROLE, new Integer(i)));
                    con.sendMessage(new Message(Message.INFO,
                            "welcome to the game"));

                    if (logger != null) {
                        logger.log(this,
                            "new connection assigned to role " + i,
                            new Object[] { con });
                    }
                } catch (IOException e) {
                    freeConnection(i);

                    return null;
                }

                break;
            }
        }

        broadcast(Message.INFO,
            "player connected from " +
            con.getSocket().getInetAddress().getHostName() + " playing role " +
            getRole(con));

        if (activePlayers() < playerCon.length) {
            broadcast(Message.INFO,
                "waiting for " + (playerCon.length - activePlayers()) +
                " more player(s)");
        } else {
            broadcast(Message.INFO, "all players connected");
            server.shutdown();
        }

        MessageDelegator listener = new MessageDelegator(con, this);

        return listener;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int activePlayers() {
        int count = 0;

        for (int i = 0; i < playerCon.length; i++) {
            if (playerCon[i] != null) {
                count++;
            }
        }

        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @param con DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int getRole(NetConnection con) {
        for (int i = 0; i < playerCon.length; i++)
            if (con.equals(playerCon[i])) {
                return i;
            }

        // connection already closed; return -1
        // - hoping that it will be handled appropriately
        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     * @param message DOCUMENT ME!
     */
    synchronized void broadcast(int type, Object message) {
        for (int i = 0; i < playerCon.length; i++) {
            try {
                if (playerCon[i] != null) {
                    playerCon[i].sendMessage(new Message(type, message));
                }
            } catch (IOException e) {
                freeConnection(i);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param con DOCUMENT ME!
     */
    public void connectionLost(NetConnection con) {
        freeConnection(con);
    }

    /**
     * DOCUMENT ME!
     *
     * @param con DOCUMENT ME!
     */
    synchronized void freeConnection(NetConnection con) {
        freeConnection(getRole(con));
    }

    /**
     * called when a player connection threw an IOException
     *
     * @param i DOCUMENT ME!
     */
    synchronized void freeConnection(int i) {
        // make sure this hasn't been done before
        if (i < 0) {
            return;
        }

        if (logger != null) {
            logger.log(this, Level.WARNING, "connection closed for role " + i,
                new Object[] { playerCon[i] });
        }

        // just to be sure
        if (playerCon[i] == null) {
            return;
        }

        playerCon[i].close();
        playerCon[i] = null;
        broadcast(Message.INFO, "lost connection to player role " + i);
        broadcast(Message.INFO,
            "waiting for " + (playerCon.length - activePlayers()) +
            " more player(s)");

        if (!server.isAlive()) {
            try {
                server = new NetConnectionServer(serverPort, this, logger);
                server.getServerSocket().setSoTimeout(timeout);
                server.start();
            } catch (IOException e) {
                broadcast(Message.INFO,
                    "sorry, server cannot be restarted for reconnect");
                shutdown();

                return;
            }

            broadcast(Message.INFO,
                "server restarted for player clients to (re-)connect");
        }
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

        Message msg = null;

        try {
            msg = (Message) message;
        } catch (ClassCastException e) {
            return;
        }

        switch (msg.type) {
        case Message.MOVE:

            // make sure that the move is performed by the right connection
            if (((GameMove) msg.content).getPlayer() != getRole(con)) {
                if (logger != null) {
                    logger.log(this, Level.FINE, "wrong player");
                }

                try {
                    con.sendMessage(new Message(Message.ILLEGALMOVE, null));
                } catch (IOException e) {
                    freeConnection(con);
                }
            }

            // perform the move and broadcast it to all connections
            // if it was legal
            if (logger != null) {
                logger.log(this, Level.FINE, "processing move");
            }

            if (game.makeMove((GameMove) msg.content)) {
                if (logger != null) {
                    logger.log(this, Level.FINE, "legal move performed");
                }

                broadcast(Message.MOVE, msg.content);

                if (game.getLegalMoves().length == 0) {
                    broadcast(Message.INFO, "Game over; thank you for playing!");
                    shutdown();
                }
            } else {
                // give feedback to the connection that the move wasn't legal;
                // the connection may then ask to have the game resent for consistency
                if (logger != null) {
                    logger.log(this, Level.FINE, "illegal move received");
                }

                try {
                    con.sendMessage(new Message(Message.ILLEGALMOVE, null));
                } catch (IOException e) {
                    freeConnection(con);
                }
            }

            break;

        case Message.RESEND_GAME:

            try {
                if (logger != null) {
                    logger.log(this, Level.FINE, "resending game");
                }

                con.sendMessage(new Message(Message.GAME, game));
                con.sendMessage(new Message(Message.ROLE,
                        new Integer(getRole(con))));
            } catch (IOException e) {
                freeConnection(con);
            }

            break;

        /*
        case Message.GET_CONNECTIONS:
            // a connection asked to find out about the other connected parties
            try {
                for (int i = 0; i < playerCon.length; i++) {
                    con.sendMessage(new Message(Message.GET_CONNECTIONS, playerCon[i]));
                }
            } catch (IOException e) { freeConnection(con); }
            break;
        */
        default: // unrecognized message

        }
    }

    /**
     * DOCUMENT ME!
     */
    public void shutdown() {
        broadcast(Message.INFO, "GameServer is shutting down; bye bye ..");

        for (int i = 0; i < playerCon.length; i++) {
            if (playerCon[i] != null) {
                playerCon[i].close();
            }
        }

        server.shutdown();
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        server.start();
    }

    /**
     * Message is the only object allowed to be sent over a connection
     * between a GameServer and its client
     *
     * @see GameServer
     */
    static class Message implements Serializable {
        /** DOCUMENT ME! */
        static final long serialVersionUID = 149162084552843533L;

        /** DOCUMENT ME! */
        public static final int INFO = 1;

        /** DOCUMENT ME! */
        public static final int GAME = 2;

        /** DOCUMENT ME! */
        public static final int ROLE = 3;

        /** DOCUMENT ME! */
        public static final int MOVE = 4;

        /** DOCUMENT ME! */
        public static final int ILLEGALMOVE = 5;

        /** DOCUMENT ME! */
        public static final int RESEND_GAME = 6;

        /** DOCUMENT ME! */
        public int type;

        /** DOCUMENT ME! */
        public Object content;

        //public static final int GET_CONNECTIONS = 7;
        /**
         * Creates a new Message object.
         *
         * @param type DOCUMENT ME!
         * @param content DOCUMENT ME!
         */
        Message(int type, Object content) {
            this.type = type;
            this.content = content;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            return super.toString() +
            ((content == null) ? ""
                               : (", type: " + type + ", message: " +
            content.toString()));
        }
    }
}
