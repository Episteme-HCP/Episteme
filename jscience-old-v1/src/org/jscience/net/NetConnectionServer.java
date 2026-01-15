/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.net;

import org.jscience.util.Settings;
import org.jscience.util.license.Licensed;
import org.jscience.util.logging.Level;
import org.jscience.util.logging.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;

import javax.net.ServerSocketFactory;


//import javax.net.ssl.*;
/**
 * A NetConnectionServer - once run() is called - listens to incoming
 * requests from NetConnection objects with a signature provided by the
 * ConnectionDispatcher. The server creates and starts a new Thread for each
 * NetConnection with the right signature; the Thread is initialized with a
 * Runnable object provided by the ConnectionDispatcher. The NetConnection
 * objects used by the NetConnectionServer are SocketConnection objects.
 *
 * @author Holger Antelmann
 *
 * @see NetConnectionHandler
 * @see ConnectionDispatcher
 * @see NetConnection
 * @see SocketConnection
 */
public class NetConnectionServer extends Thread implements Licensed {
    /**
     * DOCUMENT ME!
     */
    private boolean enabled = true;

    /**
     * DOCUMENT ME!
     */
    private ArrayList<Thread> threads;

    /**
     * DOCUMENT ME!
     */
    private ArrayList<NetConnection> connections;

    /**
     * DOCUMENT ME!
     */
    private ServerSocket serverSocket;

    /**
     * DOCUMENT ME!
     */
    private int port;

    /**
     * DOCUMENT ME!
     */
    private Logger logger;

    /**
     * DOCUMENT ME!
     */
    private ConnectionDispatcher factory;

/**
     * each connection made will run in a separate thread that
     * delegates every message received to the given handler
     */
    public NetConnectionServer(int port, NetConnectionHandler handler,
        String signature) throws IOException {
        this(port, handler, signature, null);
    }

/**
     * each connection made will run in a separate thread that
     * delegates every message received to the given handler;
     * if logger is not null, it will be used to log both: connection and message handling
     */
    public NetConnectionServer(int port, NetConnectionHandler handler,
        String signature, Logger logger) throws IOException {
        this(port, new DefaultDispatcher(handler, signature, logger));
    }

/**
     * the factory is responsible for providing the runnable objects that
     * handle each connection in a separate thread
     */
    public NetConnectionServer(int port, ConnectionDispatcher factory)
        throws IOException {
        this(port, factory, null);
    }

/**
     * if logger is not null, it will be used to log connection handling
     */
    public NetConnectionServer(int port, ConnectionDispatcher factory,
        Logger logger) throws IOException {
        this.port = port;
        this.factory = factory;
        this.logger = logger;
        serverSocket = ServerSocketFactory.getDefault().createServerSocket(port);
        threads = new ArrayList<Thread>();
        connections = new ArrayList<NetConnection>();
    }

    /**
     * listens for a single connection to be made and returns a
     * NetConnection if the connection made matches the signature
     *
     * @param port DOCUMENT ME!
     * @param signature DOCUMENT ME!
     * @param timeout specified in milliseconds; if 0, the method waits
     *        indefinately until a connection is made or the serverSocket is
     *        closed
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     *
     * @see SocketConnection#createServerConnection(ServerSocket,Object)
     */
    public static NetConnection waitForConnection(int port, Object signature,
        int timeout) throws IOException {
        ServerSocket s = ServerSocketFactory.getDefault()
                                            .createServerSocket(port);
        s.setSoTimeout(timeout);

        return SocketConnection.createServerConnection(s, signature);
    }

    /**
     * starts listening and creates a new Thread per every
     * NetConnection made - handled by a NetConnectionHandler
     */
    public void run() {
        if (serverSocket == null) {
            return;
        }

        while (enabled) {
            Socket socket;

            try {
                Settings.checkLicense(NetConnectionServer.this);
                socket = serverSocket.accept();

                if (logger != null) {
                    logger.log(this, Level.FINE, "connection attempt",
                        new Object[] { socket });
                }
            } catch (IOException e) {
                //e.printStackTrace();
                if (logger != null) {
                    logger.log(this, Level.WARNING,
                        "server shut down due to exception during connection accept",
                        e);
                }

                shutdown();

                break;
            }

            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                Object remoteSignature = in.readObject();
                Object signature = factory.getConnectionSignature();
                out.writeObject(signature);
                out.flush();

                if (signature == null) {
                    if (remoteSignature != null) {
                        continue;
                    }
                } else if (!signature.equals(remoteSignature)) {
                    continue;
                }

                NetConnection con = new SocketConnection(socket, in, out,
                        signature);
                connections.add(con);

                Thread task = factory.createHandlerThread(con);

                if (task == null) {
                    continue;
                }

                threads.add(task);
                task.start();

                if (logger != null) {
                    logger.log(this, Level.NORMAL,
                        "connection established and listener started",
                        new Object[] { con, task });
                }
            } catch (ClassNotFoundException e) {
                if (logger != null) {
                    logger.log(this, Level.WARNING,
                        "received signature class not recognized", e);
                }
            } catch (IOException e) {
                if (logger != null) {
                    logger.log(this, Level.WARNING,
                        "server shutting down after IOException while listening for connections",
                        e);
                }

                shutdown();
            }
        }
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
     * DOCUMENT ME!
     *
     * @param logger DOCUMENT ME!
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * returns the list that contains all Threads that were
     * instanciated by this server to handle incoming connections
     *
     * @return DOCUMENT ME!
     */
    public List<Thread> getThreads() {
        return threads;
    }

    /**
     * returns all NetConnections that have been created with this
     * server
     *
     * @return DOCUMENT ME!
     */
    public List<NetConnection> getNetConnections() {
        return connections;
    }

    /**
     * shutdown() does not close the connections that have already been
     * established
     */
    public void shutdown() {
        enabled = false;

        try {
            if (serverSocket != null) {
                if (logger != null) {
                    logger.log(this, Level.CONFIG, "server shutting down");
                }

                serverSocket.close();
            }
        } catch (IOException e) {
            if (logger != null) {
                logger.log(this, Level.WARNING, "exception during shutdown", e);
            }
        }

        serverSocket = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLocalPort() {
        return port;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * DOCUMENT ME!
     */
    protected void finalize() {
        shutdown();
    }

    /**
     * The DefaultDispatcher provides a default ConnectionDispatcher
     * that is used when the NetConnectionServer constructor in the enclosing
     * class is called with a NetConnectionHandler
     */
    public static class DefaultDispatcher implements ConnectionDispatcher {
        /**
         * DOCUMENT ME!
         */
        NetConnectionHandler handler;

        /**
         * DOCUMENT ME!
         */
        Object signature;

        /**
         * DOCUMENT ME!
         */
        Logger logger;

        /**
         * Creates a new DefaultDispatcher object.
         *
         * @param handler DOCUMENT ME!
         * @param signature DOCUMENT ME!
         */
        public DefaultDispatcher(NetConnectionHandler handler, Object signature) {
            this(handler, signature, null);
        }

/**
         * if logger is not null, it will be used to log established and closed connections
         */
        public DefaultDispatcher(NetConnectionHandler handler,
            Object signature, Logger logger) {
            this.handler = handler;
            this.signature = signature;
            this.logger = logger;
        }

        /**
         * uses a MessageDelegator instance as Runnable; the
         * DefaultDispatcher's logger is passed to the MessageDelegator.
         *
         * @see MessageDelegator
         */
        public Thread createHandlerThread(final NetConnection con) {
            if (logger != null) {
                String s = "connection established: ";
                s += con.getSocket().getInetAddress().getHostName();
                s += (" port " + con.getSocket().getPort());
                logger.log(this, Level.CONFIG, s);
            }

            return new MessageDelegator(con, handler, logger);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object getConnectionSignature() {
            return signature;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Logger getLogger() {
            return logger;
        }
    }
}
