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
 * ClientRecord.java
 *
 * Created on 17 March 2004, 11:47
 */
package org.jscience.computing.distributed.server;

/**
 * Object representing a client connected to an Interactive Task Server.
 * Used in Multiple Server setup to represent the allocation between servers
 * and clients.
 *
 * @author mmg20
 */
public class ClientRecord {
    /** DOCUMENT ME! */
    protected Object uid;

    /** DOCUMENT ME! */
    protected String name;

    /** DOCUMENT ME! */
    protected Object realId;

    /** DOCUMENT ME! */
    protected ServerRecord allocatedServer;

/**
     * Creates a new instance of ClientRecord
     *
     * @param uid  DOCUMENT ME!
     * @param name A unique name for this client
     */
    public ClientRecord(Object uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    /**
     * Get method for this client's unique name
     *
     * @return This client's unique name
     */
    public String getName() {
        return name;
    }

    /**
     * Get method for the ID given to this client by its currently
     * allocated server.  This is the ID by which the server refers to this
     * client and may be equal to the ID another client is refered to by
     * another server.
     *
     * @return ID given to this client by its currently allocated server
     */
    public Object getRealId() {
        return realId;
    }

    /**
     * Set method for the ID given to this client by its currently
     * allocated server.  This is the ID by which the server refers to this
     * client and may be equal to the ID another client is refered to by
     * another server.
     *
     * @param realId ID by which the currently allocated server refers to this
     *        client
     */
    public void setRealId(Object realId) {
        this.realId = realId;
    }

    /**
     * Get method for the currently allocated server, ie. the server
     * which this client is servicing.  This is the server whose
     * InteractiveTask returned by the getTask( ) method this client is
     * running.
     *
     * @return ServerRecord object representing the server currently allocated
     *         to this client
     */
    public ServerRecord getAllocatedServer() {
        return allocatedServer;
    }

    /**
     * Set method for the currently allocated server, ie. the server
     * which this client is servicing.  This is the server whose
     * InteractiveTask returned by the getTask( ) method this client is
     * running.
     *
     * @param allocatedServer The ServerRecord object of the server this client
     *        is now allocated to
     */
    public void setAllocatedServer(ServerRecord allocatedServer) {
        this.allocatedServer = allocatedServer;
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "[ " + uid + ", " + name + " ]";
    }
}
