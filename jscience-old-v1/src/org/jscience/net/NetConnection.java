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

import java.io.IOException;

import java.net.Socket;


/**
 * A NetConnection provides a convenient way to communicate with a remote
 * process. All objects used must be serializable.
 *
 * @author Holger Antelmann
 * @see NetConnectionServer
 * @see NetConnectionHandler
 * @see ConnectionDispatcher
 */
public interface NetConnection {
    /**
     * returns the identification type object for this connection; this
     * signature is the same on both sides of the connection. Note that this
     * object requires the equals(Object obj) method to work after
     * serialization/de-serialization; Strings work just fine.
     *
     * @return DOCUMENT ME!
     */
    Object getSignature();

    /**
     * sends an object over the connection
     *
     * @param message DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    void sendMessage(Object message) throws IOException;

    /**
     * reads an object from the connection (operation blocks until
     * object is read)
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    Object readMessage() throws IOException, ClassNotFoundException;

    /**
     * returns the underlying Socket of this connection
     *
     * @return DOCUMENT ME!
     */
    Socket getSocket();

    /**
     * returns whether this NetConnection is currently active
     *
     * @return DOCUMENT ME!
     */
    boolean isActive();

    /**
     * closes the NetConnection
     */
    void close();
}
