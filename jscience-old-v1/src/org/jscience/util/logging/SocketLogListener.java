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
