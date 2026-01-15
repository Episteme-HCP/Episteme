/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.net;

/**
 * A NetConnectionHandler is used to handle incoming messages from a
 * NetConnection. On the server side, there is typically one object for each
 * connected client.
 *
 * @author Holger Antelmann
 * @see NetConnection
 */
public interface NetConnectionHandler {
    /**
     * called when a message is received by the given NetConnection
     * object
     *
     * @param message DOCUMENT ME!
     * @param connection DOCUMENT ME!
     */
    void handleMessage(Object message, NetConnection connection);

    /**
     * called when a connection caused an IOException during reading or
     * writing; the connection may not have been closed at this point. <br>
     * Usually, it is a good idea to close the connection here explicitly.
     *
     * @param connection DOCUMENT ME!
     */
    void connectionLost(NetConnection connection);
}
