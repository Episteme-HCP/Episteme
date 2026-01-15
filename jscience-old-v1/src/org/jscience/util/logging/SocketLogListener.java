/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import org.jscience.net.NetConnection;
import org.jscience.net.NetConnectionHandler;
import org.jscience.net.NetConnectionServer;

import java.io.IOException;


/**
 * used to listen to LogEntry objects send through a NetConnection. The
 * NetConnectionServer must be started before use.
 *
 * @author Holger Antelmann
 */
public class SocketLogListener implements NetConnectionHandler {
    /** DOCUMENT ME! */
    Logger logger;

    /** DOCUMENT ME! */
    NetConnectionServer server;

/**
     * Creates a new SocketLogListener object.
     *
     * @param port   DOCUMENT ME!
     * @param logger DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public SocketLogListener(int port, Logger logger) throws IOException {
        server = new NetConnectionServer(port, this, SocketWriter.signature,
                logger);
        this.logger = logger;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NetConnectionServer getNetConnectionServer() {
        return server;
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
        server.start();
    }

    /**
     * DOCUMENT ME!
     */
    public void stop() {
        server.shutdown();
    }

    /**
     * DOCUMENT ME!
     *
     * @param con DOCUMENT ME!
     */
    public void connectionLost(NetConnection con) {
        logger.log(this, Level.DEFAULT, "SocketLogListener connection lost");
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     * @param con DOCUMENT ME!
     */
    public void handleMessage(Object message, NetConnection con) {
        if (message instanceof LogEntry) {
            logger.log((LogEntry) message);
        } else {
            logger.log(this, Level.WARNING,
                "unexpected object received: " + message);
        }
    }
}
