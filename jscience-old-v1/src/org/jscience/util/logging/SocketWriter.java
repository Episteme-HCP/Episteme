/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import org.jscience.net.SocketConnection;

import java.io.IOException;


/**
 * simply used to write serialized LogEntry objects via network
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.net.SocketConnection
 */
public class SocketWriter extends AbstractLogWriter {
    /** DOCUMENT ME! */
    public static final String signature = SocketWriter.class.getName();

    /** DOCUMENT ME! */
    SocketConnection con;

/**
     * Creates a new SocketWriter object.
     *
     * @param host DOCUMENT ME!
     * @param port DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public SocketWriter(String host, int port) throws IOException {
        con = SocketConnection.createConnection(host, port, signature);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SocketConnection getSocketConnection() {
        return con;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pattern DOCUMENT ME!
     *
     * @throws LogException DOCUMENT ME!
     */
    public synchronized void writeLogEntry(Object pattern)
        throws LogException {
        try {
            con.sendMessage(pattern);
        } catch (IOException ex) {
            throw new LogException(ex);
        }
    }
}
