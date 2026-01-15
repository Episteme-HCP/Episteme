/**
 * Java LX200DebugServer class. Copyright (C) 1999-2001  Mark Hale
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
public final class LX200DebugServer extends UnicastRemoteObject
    implements LX200Remote {
    /** DOCUMENT ME! */
    private String ra = "00:00:00";

    /** DOCUMENT ME! */
    private String dec = "+00*00'00";

/**
     * Constructs a telescope debug server.
     *
     * @throws RemoteException DOCUMENT ME!
     */
    public LX200DebugServer() throws RemoteException {
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        if (arg.length != 1) {
            System.out.println("Usage: LX200DebugServer <remote name>");

            return;
        }

        System.setSecurityManager(new RMISecurityManager());

        try {
            Registry localRegistry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            localRegistry.rebind(arg[0], new LX200DebugServer());
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
        System.err.println("setFocusRate: " + rate);
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
        System.err.println("startFocus: " + direction);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void stopFocus() throws IOException, RemoteException {
        System.err.println("stopFocus");
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
        System.err.println("setSlewRate: " + rate);
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
        System.err.println("startSlew: " + direction);
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
        System.err.println("stopSlew: " + direction);
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
        System.err.println("getRA");

        return ra;
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
        System.err.println("getDec");

        return dec;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newRa DOCUMENT ME!
     * @param newDec DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void setObjectCoords(String newRa, String newDec)
        throws IOException, RemoteException {
        System.err.println("setObjectCoords: " + newRa + ", " + newDec);
        ra = newRa;
        dec = newDec;
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
        System.err.println("slewToObject");

        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void syncCoords() throws IOException, RemoteException {
        System.err.println("syncCoords");
    }
}
