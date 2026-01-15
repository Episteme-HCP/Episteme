/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

import org.jscience.util.Monitor;
import org.jscience.util.logging.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.ServerSocket;
import java.net.Socket;


/**
 * SocketPlayerServer provides the capability of taking network requests
 * from a SocketPlayer. SocketPlayerServer runs in a thread which when
 * instanciated binds itself to a Socket. When the start() method is invoked
 * (or run() is explicitly called), the SocketPlayer Server will listen for
 * incoming requests as long as the boolean value in the given monitor (see
 * constructor) is true. If no monitor is given, no monitoring or external
 * stopping of the SocketPlayerServer is possible; after starting, the Server
 * will be in an infinitive loop. Use of the monitor: <br>
 * SocketPlayerServer needs a monitor with (monitor.getSize() >= 3); <br>
 * monitor.timer[0] has the non-idle time of this server, <br>
 * monitor.getNumber(0) has the number of requests made to the server <br>
 * and monitor.getObject(0) has a String with the current status message. <br>
 * monitor.object[1] keeps a String of the last connection made.<br>
 * monitor.object[2] holds the last object sent through
 * SocketPlayer.sendMessage(Object message).<br>
 * In addition, for each connection made, SocketPlayerServer updates a status
 * message in the monitor 3 times (when a connection is made, when the tasks
 * finishes and when the SocketPlayerServer is idle again). This status
 * message will be written as a String into monitor.setObject(0) each time. To
 * give a monitoring process the ability to timely react on each event, the
 * monitor needs to have been initialized with a custom task, since for each
 * status message update, SocketPlayerServer calls monitor.runTask().
 * Limitation: the protocol is still fairly primitive; any exceptions that may
 * be thrown by the embedded actual Player object are not propagated but cause
 * the connection to reset.
 *
 * @author Holger Antelmann
 *
 * @see SocketPlayer
 * @see org.jscience.util.Monitor
 */
public class SocketPlayerServer extends Thread {
    /** DOCUMENT ME! */
    public static int SO_TIMEOUT = 5 * 60 * 1000;

    /** DOCUMENT ME! */
    private ObjectInputStream in;

    /** DOCUMENT ME! */
    private ObjectOutputStream out;

    /** DOCUMENT ME! */
    private Player player;

    /** DOCUMENT ME! */
    private ServerSocket playerSocket;

    /** DOCUMENT ME! */
    private Socket gameSocket;

    /** DOCUMENT ME! */
    private int port;

    /** DOCUMENT ME! */
    private GamePlay game;

    /** DOCUMENT ME! */
    private GameMove move;

    /** DOCUMENT ME! */
    private int[] role;

    /** DOCUMENT ME! */
    private int level;

    /** DOCUMENT ME! */
    private long milliseconds;

    /** DOCUMENT ME! */
    private String requestType;

    /** DOCUMENT ME! */
    private Monitor monitor;

    /** DOCUMENT ME! */
    private Logger logger;

/**
     * The constructor binds itself to the given port and keeps a reference to
     * the actual Player object. The monitor should be enabled if the thread
     * is supposed to successully listen on the port; also monitor.getSize()
     * must be greater or equal 2 (see class description for how the monitor
     * is used). see SocketPlayer org.jscience.util.Monitor
     *
     * @param port    DOCUMENT ME!
     * @param player  DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     * @param logger  DOCUMENT ME!
     */
    public SocketPlayerServer(int port, Player player, Monitor monitor,
        Logger logger) {
        super(player.getPlayerName());

        if (player == null) {
            throw (new IllegalArgumentException("Player was null"));
        }

        if ((monitor != null) && (monitor.getSize() < 3)) {
            throw (new IllegalArgumentException(
                "need a monitor with at least 3 slot please"));
        }

        this.port = port;
        this.player = player;
        this.monitor = monitor;

        if (monitor != null) {
            monitor.getTimer(0).stop();
        }

        this.logger = logger;

        if (logger != null) {
            logger.log(this, "SocketPlayerServer logging started");
        }
    }

    /**
     * run() listens at the port specified in the constructor for a
     * connecton from a SocketPlayer and routes all requests through to the
     * embedded Player object as long as the Monitor object given to the
     * constructor is disabled.
     *
     * @see SocketPlayer
     * @see org.jscience.util.Monitor
     */
    public void run() {
        try {
            playerSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(
                "Error: SocketPlayerServer cannot listen at port " + port);
            e.printStackTrace();

            return;
        }

        while ((monitor == null) || monitor.enabled()) {
            try {
                boolean test = false;
                String task = handshake(); // that's where the thread waits for the socket sonnection

                if (monitor != null) {
                    monitor.getTimer(0).resume();
                    monitor.increment(0);
                }

                if (task == null) {
                    continue;
                }

                if (task.equals("canPlayGame")) {
                    test = canPlayGame();
                }

                if (task.equals("evaluate")) {
                    test = evaluate();
                }

                if (task.equals("getPlayerName")) {
                    test = getPlayerName();
                }

                if (task.equals("heuristic")) {
                    test = heuristic();
                }

                if (task.equals("selectMove")) {
                    test = selectMove();
                }

                if (task.equals("sendMessage")) {
                    test = sendMessage();
                }

                if (task.equals("pruneMove")) {
                    test = pruneMove();
                }

                if (monitor != null) {
                    monitor.getTimer(0).pause();
                }

                if (test) {
                    updateMonitor("task: " + task + " completed successfully");
                } else {
                    updateMonitor(
                        "something went wrong while trying to perform: " +
                        task);
                }
            } catch (Exception e) {
                if (monitor != null) {
                    monitor.getTimer(0).pause();
                }

                if (monitor != null) {
                    monitor.setObject(0, "Exception caught during processing");
                }

                e.printStackTrace();
            }

            cleanup();

            if (monitor != null) {
                monitor.runTask();
            }
        }

        String s = "Server exited as the monitor has been disabled";

        if (monitor != null) {
            s += ("; total number of requests: " + monitor.getNumber(0));
            s += ("; total time taken for processing requests: " +
            monitor.getTimer(0).elapsedAsString());
        }

        updateMonitor(s);

        try {
            playerSocket.close();
        } catch (IOException e) {
            System.err.println("Error: I/O Exception in SocketPlayerServer");
            e.printStackTrace();
            cleanup();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    String handshake() {
        String requestType = null;

        try {
            updateMonitor("waiting for a connection ..");
            gameSocket = playerSocket.accept(); // waiting for someone to knock
            gameSocket.setSoTimeout(SO_TIMEOUT);
            in = new ObjectInputStream(gameSocket.getInputStream());

            // Connection established; updating monitor
            String s = "processing request from " +
                gameSocket.getInetAddress().getHostName();
            s += (", port: " + gameSocket.getPort());
            updateMonitor(s);

            if (monitor != null) {
                monitor.setObject(1, s);
            }

            String code = (String) in.readObject();

            if (code.equals(
                        "I am your org.jscience.computing.game.SocketPlayer")) {
                // it's a SocketPlayer connection
            } else {
                // it's not a known connection
                cleanup();

                return null;
            }

            //Sending   signature ..
            out = new ObjectOutputStream(gameSocket.getOutputStream());
            out.writeObject(
                "I am your org.jscience.computing.game.SocketPlayerServer");
            out.flush();

            //Signature sent; attempting to receive request type ..
            requestType = (String) in.readObject();
        } catch (ClassCastException e) {
            System.err.println(
                "Error: Object read was not of the expected type");
            e.printStackTrace();

            return null;
        } catch (ClassNotFoundException e) {
            System.err.println(
                "Error: SocketPlayerServer couldn't find the given class");
            e.printStackTrace();

            return null;
        } catch (IOException e) {
            System.err.println("Error: I/O Exception in SocketPlayerServer");
            e.printStackTrace();

            return null;
        }

        return requestType;
    }

    /**
     * DOCUMENT ME!
     */
    void cleanup() {
        try {
            out.close();
            in.close();
            gameSocket.close();
        } catch (IOException e) {
            System.err.println("Error: cleanup failed");
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    protected void finalize() throws IOException {
        cleanup();
        playerSocket.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean sendMessage() {
        Object message;

        try {
            message = in.readObject();
        } catch (ClassNotFoundException e) {
            System.err.println(
                "Error: SocketPlayerServer couldn't find the given class");
            e.printStackTrace();

            return false;
        } catch (IOException e) {
            System.err.println(
                "Error: SocketPlayerServer couldn't write to OutputStream");
            e.printStackTrace();

            return false;
        }

        if (monitor != null) {
            monitor.setObject(2, message);
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean getPlayerName() {
        try {
            out.writeObject(player.getPlayerName());
            out.flush();
        } catch (IOException e) {
            System.err.println(
                "Error: SocketPlayerServer couldn't write to OutputStream");
            e.printStackTrace();

            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean canPlayGame() {
        try {
            game = (GamePlay) in.readObject();
            out.writeBoolean(player.canPlayGame(game));
            out.flush();
        } catch (ClassCastException e) {
            System.err.println(
                "Error: Object read was not of the expected type");
            e.printStackTrace();

            return false;
        } catch (ClassNotFoundException e) {
            System.err.println(
                "Error: SocketPlayerServer couldn't find the given class");
            e.printStackTrace();

            return false;
        } catch (IOException e) {
            System.err.println(
                "Error: SocketPlayerServer couldn't write to OutputStream");
            e.printStackTrace();

            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean heuristic() {
        try {
            game = (GamePlay) in.readObject();
            move = (GameMove) in.readObject();
            role = (int[]) in.readObject();

            double h = player.heuristic(game, move, role);
            out.writeDouble(h);
            out.flush();
        } catch (ClassCastException e) {
            System.err.println(
                "Error: Object read was not of the expected type");
            e.printStackTrace();

            return false;
        } catch (ClassNotFoundException e) {
            System.err.println(
                "Error: SocketPlayerServer couldn't find the given class");
            e.printStackTrace();

            return false;
        } catch (IOException e) {
            System.err.println("Error: I/O Exception in SocketPlayerServer");
            e.printStackTrace();

            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean pruneMove() {
        try {
            game = (GamePlay) in.readObject();
            move = (GameMove) in.readObject();
            role = (int[]) in.readObject();

            boolean p = player.pruneMove(game, move, role);
            out.writeBoolean(p);
            out.flush();
        } catch (ClassCastException e) {
            System.err.println(
                "Error: Object read was not of the expected type");
            e.printStackTrace();

            return false;
        } catch (ClassNotFoundException e) {
            System.err.println(
                "Error: SocketPlayerServer couldn't find the given class");
            e.printStackTrace();

            return false;
        } catch (IOException e) {
            System.err.println("Error: I/O Exception in SocketPlayerServer");
            e.printStackTrace();

            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean evaluate() {
        try {
            game = (GamePlay) in.readObject();
            move = (GameMove) in.readObject();
            role = (int[]) in.readObject();
            level = in.readInt();
            milliseconds = in.readLong();

            double eval = player.evaluate(game, move, role, level, milliseconds);
            out.writeDouble(eval);
            out.flush();
        } catch (ClassCastException e) {
            System.err.println(
                "Error: Object read was not of the expected type");
            e.printStackTrace();

            return false;
        } catch (ClassNotFoundException e) {
            System.err.println(
                "Error: SocketPlayerServer couldn't find the given class");
            e.printStackTrace();

            return false;
        } catch (IOException e) {
            System.err.println("Error: I/O Exception in SocketPlayerServer");
            e.printStackTrace();

            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean selectMove() {
        try {
            game = (GamePlay) in.readObject();
            role = (int[]) in.readObject();
            level = in.readInt();
            milliseconds = in.readLong();

            GameMove m = player.selectMove(game, role, level, milliseconds);
            out.writeObject(m);
            out.flush();
        } catch (ClassCastException e) {
            System.err.println(
                "Error: Object read was not of the expected type");
            e.printStackTrace();

            return false;
        } catch (ClassNotFoundException e) {
            System.err.println(
                "Error: SocketPlayerServer couldn't find the given class");
            e.printStackTrace();

            return false;
        } catch (IOException e) {
            System.err.println("Error: I/O Exception in SocketPlayerServer");
            e.printStackTrace();

            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    void updateMonitor(String message) {
        if (monitor != null) {
            monitor.setObject(0, message);
        }

        if (logger != null) {
            logger.log(this, message);

            if ((monitor != null) && (message.indexOf("sendMessage") > -1)) {
                logger.log(this, "message sent: " + monitor.getObject(2));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "SocketPlayerServer at port " + port;
        s += ("; embedded Player object: " + player.toString());

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Player getPlayer() {
        return player;
    }
}
