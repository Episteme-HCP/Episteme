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
 * Server.java
 *
 * Created on 21 April 2004, 18:50
 */
package org.jscience.tests.distributed;

import java.io.IOException;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;


/**
 * DOCUMENT ME!
 *
 * @author mmg20
 */
public class Server extends UnicastRemoteObject implements Getter {
    static {
        try {
            RMISocketFactory.setSocketFactory(new org.jscience.computing.distributed.server.FixedPortRMISocketFactory());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/**
     * Creates a new instance of Server
     *
     * @throws RemoteException DOCUMENT ME!
     */
    public Server() throws RemoteException {
    }

    /**
     * DOCUMENT ME!
     */
    protected void bind() {
        String serverObjectName = "Bob";

        try {
            //UnicastRemoteObject.exportObject( this );
            Naming.rebind(serverObjectName, this);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (java.net.MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args the command line arguments
     *
     * @throws RemoteException DOCUMENT ME!
     */
    public static void main(String[] args) throws RemoteException {
        Server s = new Server();
        s.bind();
    }
}
