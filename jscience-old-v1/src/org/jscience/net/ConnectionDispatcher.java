/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.net;

/**
 * used by a NetConnectionServer object to create a Thread that handles
 * incoming connections
 *
 * @author Holger Antelmann
 * @see NetConnectionServer
 * @see NetConnectionHandler
 * @see NetConnection
 */
public interface ConnectionDispatcher {
    /**
     * returns an object which will be started in a new Thread to
     * handle the given NetConnection. Typically, the returned object could be
     * a MessageDelegator.
     *
     * @see MessageDelegator
     */
    Thread createHandlerThread(NetConnection con);

    /**
     * returns a signature object used to identify the desired
     * connection type handled by this ConnectionDispatcher object
     *
     * @return DOCUMENT ME!
     */
    Object getConnectionSignature();
}
