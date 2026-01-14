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
