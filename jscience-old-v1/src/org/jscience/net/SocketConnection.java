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

import org.jscience.util.logging.Level;
import org.jscience.util.logging.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * A SocketConnection provides simplified access to a remote process
 * through a Socket connection. To create a SocketConnection, use
 * createNetConnection()
 *
 * @author Holger Antelmann
 *
 * @see NetConnectionServer
 * @see NetConnectionHandler
 * @see ConnectionDispatcher
 * @see NetConnection
 */
public class SocketConnection implements NetConnection {
    /** DOCUMENT ME! */
    ObjectInputStream in;

    /** DOCUMENT ME! */
    ObjectOutputStream out;

    /** DOCUMENT ME! */
    Socket socket;

    /** DOCUMENT ME! */
    InetAddress host;

    /** DOCUMENT ME! */
    int port;

    /** DOCUMENT ME! */
    Object signature;

    /** DOCUMENT ME! */
    Logger logger = new Logger();

/**
     * called by createConnection(), createServerConnection(), or a subclass
     *
     * @param hostname  DOCUMENT ME!
     * @param port      DOCUMENT ME!
     * @param signature DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    protected SocketConnection(String hostname, int port, Object signature)
        throws IOException {
        this.host = InetAddress.getByName(hostname);
        this.port = port;
        this.signature = signature;
    }

    // used by NetConnectionServer.run()
/**
     * called by objects that create the connection themselves - including the
     * handshake
     *
     * @param socket    DOCUMENT ME!
     * @param in        DOCUMENT ME!
     * @param out       DOCUMENT ME!
     * @param signature DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    protected SocketConnection(Socket socket, ObjectInputStream in,
        ObjectOutputStream out, Object signature) throws IOException {
        this.socket = socket;
        this.host = socket.getInetAddress();
        this.port = socket.getPort();
        this.in = in;
        this.out = out;
        this.signature = signature;
    }

    /**
     * DOCUMENT ME!
     *
     * @param serverSocket DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws HandshakeException DOCUMENT ME!
     */
    public static SocketConnection createServerConnection(
        ServerSocket serverSocket) throws IOException, HandshakeException {
        return createServerConnection(serverSocket, null);
    }

    /**
     * waits and listens for a single connection to be made and returns
     * a SocketConnection if the connection made matches the signature
     *
     * @param serverSocket DOCUMENT ME!
     * @param signature DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws HandshakeException if the signatures did not match
     */
    public static SocketConnection createServerConnection(
        ServerSocket serverSocket, Object signature)
        throws IOException, HandshakeException {
        try {
            Socket socket = serverSocket.accept();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Object remoteSignature = in.readObject();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(signature);
            out.flush();

            SocketConnection con = new SocketConnection(socket, in, out,
                    signature);

            if (signature == null) {
                if (remoteSignature == null) {
                    con.logger.log(con, Level.CONFIG,
                        "handshake from host: " +
                        socket.getInetAddress().getHostName() + ", port: " +
                        socket.getPort());

                    return con;
                }
            } else if (signature.equals(remoteSignature)) {
                con.logger.log(con, Level.CONFIG,
                    "handshake from host: " +
                    socket.getInetAddress().getHostName() + ", port: " +
                    socket.getPort());

                return con;
            }
        } catch (ClassNotFoundException e) {
        }

        throw new HandshakeException();
    }

    /**
     * DOCUMENT ME!
     *
     * @param hostname DOCUMENT ME!
     * @param port DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws HandshakeException DOCUMENT ME!
     */
    public static SocketConnection createConnection(String hostname, int port)
        throws IOException, HandshakeException {
        return createConnection(hostname, port, null);
    }

    /**
     * returns a SocketConnection provided there is a Server listening
     * at the specified location with the secified signature; note that this
     * method is blocking.
     *
     * @param hostname DOCUMENT ME!
     * @param port DOCUMENT ME!
     * @param signature - an object that identifies the desired type of
     *        connection
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws HandshakeException if the signatures did not match
     *
     * @see NetConnectionServer
     */
    public static SocketConnection createConnection(String hostname, int port,
        Object signature) throws IOException, HandshakeException {
        SocketConnection con = new SocketConnection(hostname, port, signature);

        if (con.handshake()) {
            return con;
        }

        con.close();
        throw new HandshakeException();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    boolean handshake() throws IOException {
        try {
            logger.log(this, Level.FINE,
                "handshake with host: " + host + ", port: " + port);
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(signature);
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            Object remoteSignature = in.readObject();

            if (signature == null) {
                if (remoteSignature == null) {
                    logger.log(this, Level.CONFIG,
                        "handshake successull (host: " + host + ", port: " +
                        port);

                    return true;
                }
            } else if (signature.equals(remoteSignature)) {
                logger.log(this, Level.CONFIG,
                    "handshake successull (host: " + host + ", port: " + port);

                return true;
            }

            logger.log(this, Level.CONFIG,
                "handshake failed (no matching signatures)",
                new Object[] { signature, remoteSignature });
        } catch (ClassNotFoundException e) {
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isActive() {
        return (socket.isConnected() && !socket.isClosed());
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws HandshakeException DOCUMENT ME!
     */
    public void reconnect() throws IOException, HandshakeException {
        close();
        handshake();
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public synchronized void sendMessage(Object message)
        throws IOException {
        if (out == null) {
            throw new IOException("connection is closed");
        }

        out.writeObject(message);
        out.flush();
        logger.log(this, Level.FINER, "message sent", message);
    }

    // must not be synchronized to avoid deadlocks with sendMessage()
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    public Object readMessage() throws IOException, ClassNotFoundException {
        if (in == null) {
            throw new IOException("connection is closed");
        }

        Object msg = in.readObject();
        logger.log(this, Level.FINER, "message read", msg);

        return msg;
    }

    /**
     * propagates to the embedded socket
     *
     * @param timeout DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void setSoTimeout(int timeout) throws IOException {
        if (socket == null) {
            throw new IOException("connection is closed");
        }

        socket.setSoTimeout(timeout);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getSignature() {
        return signature;
    }

    /**
     * closes the SocketConnection
     */
    public synchronized void close() {
        try {
            if (in != null) {
                in.close();
            }

            if (out != null) {
                out.close();
            }

            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
        } finally {
            out = null;
            in = null;
            socket = null;
            logger.log(this, Level.CONFIG, "connection closed");
        }
    }

    //protected void finalize() { close(); }
    /**
     * loggs standard events - no exceptions are logged here
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
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public void setLogger(Logger logger) {
        if (logger == null) {
            throw new NullPointerException();
        }

        this.logger = logger;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public InetAddress getRemoteHost() {
        return host;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRemotePort() {
        return port;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLocalPort() {
        return socket.getLocalPort();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = super.toString() + " connected to host " +
            host.getHostName();
        s += (" at port " + port);
        s += (" (signature: " + signature + ")");

        return s;
    }
}
