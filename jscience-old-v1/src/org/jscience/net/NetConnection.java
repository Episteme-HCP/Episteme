/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
