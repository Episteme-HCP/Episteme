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

package org.jscience.devices.telescopes.LX200;

import java.io.IOException;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public final class LX200Server extends UnicastRemoteObject
    implements LX200Remote {
    /** DOCUMENT ME! */
    private final LX200 lx200;

/**
     * Constructs a telescope server.
     *
     * @param telescope the telescope to serve.
     * @throws RemoteException DOCUMENT ME!
     */
    public LX200Server(LX200 telescope) throws RemoteException {
        lx200 = telescope;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        if (arg.length != 2) {
            System.out.println("Usage: LX200Server <com port> <remote name>");

            return;
        }

        System.setSecurityManager(new RMISecurityManager());

        try {
            Registry localRegistry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            localRegistry.rebind(arg[1], new LX200Server(new LX200(arg[0])));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param rate DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void setFocusRate(int rate) throws IOException, RemoteException {
        lx200.setFocusRate(rate);
    }

    /**
     * DOCUMENT ME!
     *
     * @param direction DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void startFocus(int direction) throws IOException, RemoteException {
        lx200.startFocus(direction);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void stopFocus() throws IOException, RemoteException {
        lx200.stopFocus();
    }

    /**
     * DOCUMENT ME!
     *
     * @param rate DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void setSlewRate(int rate) throws IOException, RemoteException {
        lx200.setSlewRate(rate);
    }

    /**
     * DOCUMENT ME!
     *
     * @param direction DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void startSlew(int direction) throws IOException, RemoteException {
        lx200.startSlew(direction);
    }

    /**
     * DOCUMENT ME!
     *
     * @param direction DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void stopSlew(int direction) throws IOException, RemoteException {
        lx200.stopSlew(direction);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public String getRA() throws IOException, RemoteException {
        return lx200.getRA();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public String getDec() throws IOException, RemoteException {
        return lx200.getDec();
    }

    /**
     * DOCUMENT ME!
     *
     * @param ra DOCUMENT ME!
     * @param dec DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void setObjectCoords(String ra, String dec)
        throws IOException, RemoteException {
        lx200.setObjectCoords(ra, dec);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public int slewToObject() throws IOException, RemoteException {
        return lx200.slewToObject();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void syncCoords() throws IOException, RemoteException {
        lx200.syncCoords();
    }
}
