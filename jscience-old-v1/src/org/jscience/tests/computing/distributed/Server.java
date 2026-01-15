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
