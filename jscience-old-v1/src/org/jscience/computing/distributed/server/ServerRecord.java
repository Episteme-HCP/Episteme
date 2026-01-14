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
 * ServerRecord.java
 *
 * Created on 17 March 2004, 11:43
 */
package org.jscience.computing.distributed.server;

import org.jscience.computing.distributed.InteractiveTaskServer;

import java.util.HashSet;


/**
 * Record representing an InteractiveTaskServer.  Used for managing the
 * allocation of multiple clients to multiple servers in a multi server
 * environment.
 *
 * @author mmg20
 */
public class ServerRecord {
    /** DOCUMENT ME! */
    protected String name;

    /** DOCUMENT ME! */
    protected InteractiveTaskServer server;

    /** DOCUMENT ME! */
    protected String specificClientName;

    /** DOCUMENT ME! */
    protected HashSet allocatedClients = new HashSet();

/**
     * Creates a new instance of ServerRecord
     *
     * @param name               A unique name for this server
     * @param server             Actual InteractiveTaskServer object
     * @param specificClientName Name of client specifically allocated to this
     *                           server.  A "" or null value define a server with no specific
     *                           client
     */
    public ServerRecord(String name, InteractiveTaskServer server,
        String specificClientName) {
        this.name = name;
        this.server = server;
        this.specificClientName = specificClientName;
    }

    /**
     * Get method for this server's unique name
     *
     * @return This server's unique name
     */
    public String getName() {
        return name;
    }

    /**
     * Get method for the actual server object within this record
     *
     * @return The actual server object within this record
     */
    public InteractiveTaskServer getServer() {
        return server;
    }

    /**
     * Get method for the name of the client specifically allocated to
     * this server
     *
     * @return The name of the client specifically allocated to this server
     */
    public String getSpecificClientName() {
        return specificClientName;
    }

    /**
     * Set method for the name of the client specifically allocated to
     * this server
     *
     * @param specificClientName The name of the client specifically allocated
     *        to this server
     */
    public void setSpecificClientName(String specificClientName) {
        this.specificClientName = specificClientName;
    }

    /**
     * Adds a client to the set allocated to this server
     *
     * @param cr Record of client to be added to the allocation set
     */
    public void allocateClient(ClientRecord cr) {
        allocatedClients.add(cr);
    }

    /**
     * Removes a client to the set allocated to this server
     *
     * @param cr Record of client to be removed to the allocation set
     */
    public void deallocateClient(ClientRecord cr) {
        allocatedClients.remove(cr);
    }

    /**
     * Get method for the set of clients currently allocated to this
     * server
     *
     * @return The set of clients currently allocated to this server
     */
    public HashSet getAllocatedClients() {
        return allocatedClients;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String rv = name;

        return rv;
    }
}
