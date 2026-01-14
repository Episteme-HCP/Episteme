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

/*
 * FixedPortSocketFactory.java
 *
 * Created on 21 April 2004, 18:53
 */
package org.jscience.computing.distributed.server;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.rmi.server.RMISocketFactory;


/*
* Created: Tim Goffings
* Date: Oct 3, 2002 - 3:51:34 PM
 * Modified: mmg20
 * Date modified: 21/04/2004
*/
public class FixedPortRMISocketFactory extends RMISocketFactory {
    /** DOCUMENT ME! */
    int fixedPort;

/**
     * Creates a FixedPortRMISocketFactory.
     *
     * @param fixedPort Port at which anonymous (requested as port 0) server
     *                  sockets should be created.  If this is equal to 0 then the Java
     *                  default policy of selecting a random port will be followed.
     */
    public FixedPortRMISocketFactory(int fixedPort) {
        this.fixedPort = fixedPort;
    }

    /**
     * Creates a client socket connected to the specified host and port
     * and writes out debugging info
     *
     * @param host the host name
     * @param port the port number
     *
     * @return a socket connected to the specified host and port.
     *
     * @throws IOException if an I/O error occurs during socket creation
     */
    public Socket createSocket(String host, int port) throws IOException {
        System.out.println("creating socket to host : " + host + " on port " +
            port);

        return new Socket(host, port);
    }

    /**
     * Create a server socket on the specified port (port 0 indicates
     * an anonymous port) and writes out some debugging info
     *
     * @param port the port number
     *
     * @return the server socket on the specified port
     *
     * @throws IOException if an I/O error occurs during server socket creation
     */
    public ServerSocket createServerSocket(int port) throws IOException {
        port = ((port == 0) ? fixedPort : port);
        System.out.println("creating ServerSocket on port " + port);

        return new ServerSocket(port);
    }
}
