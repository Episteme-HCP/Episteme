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

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public interface LX200Remote extends Remote {
    /**
     * DOCUMENT ME!
     *
     * @param rate DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    void setFocusRate(int rate) throws IOException, RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @param direction DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    void startFocus(int direction) throws IOException, RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    void stopFocus() throws IOException, RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @param rate DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    void setSlewRate(int rate) throws IOException, RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @param direction DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    void startSlew(int direction) throws IOException, RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @param direction DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    void stopSlew(int direction) throws IOException, RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    String getRA() throws IOException, RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    String getDec() throws IOException, RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @param ra DOCUMENT ME!
     * @param dec DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    void setObjectCoords(String ra, String dec)
        throws IOException, RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    int slewToObject() throws IOException, RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    void syncCoords() throws IOException, RemoteException;
}
