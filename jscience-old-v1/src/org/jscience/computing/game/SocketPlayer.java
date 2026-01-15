/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.net.Socket;


/**
 * SocketPlayer is a wrapper around a standard Player object. It handles
 * all requests to the Player by routing it through the network to a
 * SocketPlayerServer, who embedds the actual object that returns the results
 * for the AutoPlay. Limitation: any exceptions that may be thrown by the
 * SocketPlayerServer are not properly propagated, but show up as simple
 * IOExceptions, which are not handled with much sophistication.
 *
 * @author Holger Antelmann
 *
 * @see SocketPlayerServer
 * @see org.jscience.computing.game.Player
 * @see org.jscience.computing.game.AutoPlay
 */
public class SocketPlayer implements Player, Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -3143408026074082860L;

    /** DOCUMENT ME! */
    public static int SO_TIMEOUT = 5 * 60 * 1000;

    /** DOCUMENT ME! */
    transient ObjectInputStream in;

    /** DOCUMENT ME! */
    transient ObjectOutputStream out;

    /** DOCUMENT ME! */
    transient Socket socket;

    /** DOCUMENT ME! */
    String hostName;

    /** DOCUMENT ME! */
    int port;

    /** DOCUMENT ME! */
    boolean keepAlive;

/**
     * sets the host and port number where the SocketPlayerServer is listening
     *
     * @see SocketPlayerServer
     */
    public SocketPlayer(String hostName, int port) {
        this(hostName, port, false);
    }

/**
     * when keepAlive is set to true, the SocketPlayer will reuse the current
     * socket connection for further requests
     *
     * @param hostName  DOCUMENT ME!
     * @param port      DOCUMENT ME!
     * @param keepAlive DOCUMENT ME!
     */
    public SocketPlayer(String hostName, int port, boolean keepAlive) {
        this.hostName = hostName;
        this.port = port;
        this.keepAlive = keepAlive;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean handshake() {
        try {
            if (!keepAlive || (socket == null)) {
                socket = new Socket(hostName, port);
                socket.setSoTimeout(SO_TIMEOUT);
            }

            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(
                "I am your org.jscience.computing.game.SocketPlayer");
            in = new ObjectInputStream(socket.getInputStream());

            String s = (String) in.readObject();

            if (s.equals(
                        "I am your org.jscience.computing.game.SocketPlayerServer")) {
                return true;
            } else {
                return false;
            }
        } catch (ClassCastException e) {
            System.err.println("Error: cast unsuccessfull");
            e.printStackTrace();
            throw (new RuntimeException("Connection error in SocketPlayer"));
        } catch (IOException e) {
            System.err.println("Error: SocketPlayer couldn't get InputStream");
            e.printStackTrace();
            throw (new RuntimeException("Connection error in SocketPlayer"));
        } catch (ClassNotFoundException e) {
            System.err.println(
                "Error: SocketPlayerServer couldn't find the given class");
            e.printStackTrace();
            throw (new RuntimeException("Connection error in SocketPlayer"));
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected void cleanup() {
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            // who cares
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected void finalize() {
        cleanup();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPlayerName() {
        if (handshake()) {
            String s = null;

            try {
                out.writeObject("getPlayerName");
                out.flush();
                s = (String) in.readObject();
            } catch (ClassCastException e) {
                System.err.println("Error: cast unsuccessfull");
                e.printStackTrace();
                throw (new RuntimeException("Connection error in SocketPlayer"));
            } catch (ClassNotFoundException e) {
                System.err.println(
                    "Error: SocketPlayerServer couldn't find the given class");
                e.printStackTrace();
                throw (new RuntimeException("Connection error in SocketPlayer"));
            } catch (IOException e) {
                System.err.println(
                    "Error: SocketPlayer couldn't read from OutputStream");
                e.printStackTrace();
                throw (new RuntimeException("Connection error in SocketPlayer"));
            }

            if (!keepAlive) {
                cleanup();
            }

            return s;
        }

        throw (new RuntimeException("Sorry, the desired Player is unavailable"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void sendMessage(Object message) {
        if (handshake()) {
            boolean b = false;

            try {
                out.writeObject("sendMessage");
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                System.err.println(
                    "Error: SocketPlayer couldn't read from OutputStream");
                e.printStackTrace();
                throw (new RuntimeException("Connection error in SocketPlayer"));
            }

            if (!keepAlive) {
                cleanup();
            }

            return;
        }

        throw (new RuntimeException("Sorry, the desired Player is unavailable"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canPlayGame(GamePlay game) {
        if (handshake()) {
            boolean b = false;

            try {
                out.writeObject("canPlayGame");
                out.writeObject(game);
                out.flush();
                b = in.readBoolean();
            } catch (IOException e) {
                System.err.println(
                    "Error: SocketPlayer couldn't read from OutputStream");
                e.printStackTrace();
                throw (new RuntimeException("Connection error in SocketPlayer"));
            }

            if (!keepAlive) {
                cleanup();
            }

            return b;
        }

        throw (new GameRuntimeException(game,
            "Sorry, the desired Player is unavailable"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean pruneMove(GamePlay game, GameMove move, int[] role) {
        if (handshake()) {
            boolean p = false;

            try {
                out.writeObject("pruneMove");
                out.writeObject(game);
                out.writeObject(move);
                out.writeObject(role);
                out.flush();
                p = in.readBoolean();
            } catch (IOException e) {
                System.err.println(
                    "Error: SocketPlayer couldn't read from OutputStream");
                e.printStackTrace();
                throw (new RuntimeException("Connection error in SocketPlayer"));
            }

            if (!keepAlive) {
                cleanup();
            }

            return p;
        }

        throw (new GameRuntimeException(game,
            "Sorry, the desired Player is unavailable"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CannotPlayGameException DOCUMENT ME!
     */
    public double heuristic(GamePlay game, GameMove move, int[] role)
        throws CannotPlayGameException {
        if (handshake()) {
            double h = 0;

            try {
                out.writeObject("heuristic");
                out.writeObject(game);
                out.writeObject(move);
                out.writeObject(role);
                out.flush();
                h = in.readDouble();
            } catch (IOException e) {
                System.err.println(
                    "Error: SocketPlayer couldn't read from OutputStream");
                e.printStackTrace();
                throw (new RuntimeException("Connection error in SocketPlayer"));
            }

            if (!keepAlive) {
                cleanup();
            }

            return h;
        }

        throw (new GameRuntimeException(game,
            "Sorry, the desired Player is unavailable"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     * @param level DOCUMENT ME!
     * @param milliseconds DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CannotPlayGameException DOCUMENT ME!
     */
    public double evaluate(GamePlay game, GameMove move, int[] role, int level,
        long milliseconds) throws CannotPlayGameException {
        if (handshake()) {
            double h = 0;

            try {
                out.writeObject("evaluate");
                out.writeObject(game);
                out.writeObject(move);
                out.writeObject(role);
                out.writeInt(level);
                out.writeLong(milliseconds);
                out.flush();
                h = in.readDouble();
            } catch (IOException e) {
                System.err.println(
                    "Error: SocketPlayer couldn't read from OutputStream");
                e.printStackTrace();
                throw (new RuntimeException("Connection error in SocketPlayer"));
            }

            if (!keepAlive) {
                cleanup();
            }

            return h;
        }

        throw (new GameRuntimeException(game,
            "Sorry, the desired Player is unavailable"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     * @param role DOCUMENT ME!
     * @param level DOCUMENT ME!
     * @param milliseconds DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CannotPlayGameException DOCUMENT ME!
     */
    public GameMove selectMove(GamePlay game, int[] role, int level,
        long milliseconds) throws CannotPlayGameException {
        if (handshake()) {
            GameMove m = null;

            try {
                out.writeObject("selectMove");
                out.writeObject(game);
                out.writeObject(role);
                out.writeInt(level);
                out.writeLong(milliseconds);
                out.flush();
                m = (GameMove) in.readObject();
            } catch (ClassNotFoundException e) {
                System.err.println(
                    "Error: SocketPlayerServer couldn't find the given class");
                e.printStackTrace();
                throw (new RuntimeException("Connection error in SocketPlayer"));
            } catch (IOException e) {
                System.err.println(
                    "Error: SocketPlayer couldn't read from OutputStream");
                e.printStackTrace();
                throw (new RuntimeException("Connection error in SocketPlayer"));
            }

            if (!keepAlive) {
                cleanup();
            }

            return m;
        }

        throw (new GameRuntimeException(game,
            "Sorry, the desired Player is unavailable"));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getHostname() {
        return hostName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPort() {
        return port;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "SocketPlayer listening to host" + hostName;
        s += (" at port " + port);

        return s;
    }
}
