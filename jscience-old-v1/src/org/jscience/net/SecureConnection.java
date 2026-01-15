/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.net;

import org.jscience.util.Encoded;
import org.jscience.util.SynchronousKey;

import java.io.IOException;

import java.net.Socket;


/**
 * SecureConnection is a small wrapper for NetConnection objects, which
 * provides automatic encoding/decoding of messages. objects as Encoded
 * objects.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.util.Encoded
 * @see org.jscience.net.NetConnection
 */
public class SecureConnection implements NetConnection {
    /** DOCUMENT ME! */
    NetConnection con;

    /** DOCUMENT ME! */
    SynchronousKey key;

/**
     * Creates a new SecureConnection object.
     *
     * @param con DOCUMENT ME!
     * @param key DOCUMENT ME!
     */
    public SecureConnection(NetConnection con, SynchronousKey key) {
        if (con == null) {
            throw new NullPointerException();
        }

        if (key == null) {
            throw new NullPointerException();
        }

        this.con = con;
        this.key = key;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SynchronousKey getKey() {
        return key;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public void setKey(SynchronousKey key) {
        if (key == null) {
            throw new NullPointerException();
        }

        this.key = key;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NetConnection getEmbeddedConnection() {
        return con;
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        con.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getSignature() {
        return con.getSignature();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Socket getSocket() {
        return con.getSocket();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isActive() {
        Socket socket = con.getSocket();

        return (socket.isConnected() && !socket.isClosed());
    }

    /**
     * extracts the encoded object from the Encoded object received
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException also if the key is incorrect when an Encoded message
     *         was received
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    public Object readMessage() throws IOException, ClassNotFoundException {
        Object msg = con.readMessage();

        if (msg instanceof Encoded) {
            msg = ((Encoded) msg).decode(key);
        }

        return msg;
    }

    /**
     * every message will be encoded before it is sent unless it is
     * already an Encoded object
     *
     * @param message DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void sendMessage(Object message) throws IOException {
        if (message instanceof Encoded) {
            con.sendMessage(message);
        } else {
            con.sendMessage(new Encoded(message, key));
        }
    }
}
