/**
 * Java LX200Remote class. Copyright (C) 1999-2001  Mark Hale
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
