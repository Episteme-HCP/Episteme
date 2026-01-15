/*
 * TestITS.java
 *
 * Created on 21 July 2003, 16:53
 */
package org.jscience.tests.distributed;

import org.jscience.computing.distributed.InteractiveTask;
import org.jscience.computing.distributed.InteractiveTaskServer;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


/**
 * DOCUMENT ME!
 *
 * @author mmg20
 */
public class TestITS extends UnicastRemoteObject
    implements InteractiveTaskServer {
    /** DOCUMENT ME! */
    String rl = "XIX";

    /** DOCUMENT ME! */
    String sl = "XIX";

    /** DOCUMENT ME! */
    int clients = 0;

/**
     * Creates a new instance of TestITS
     *
     * @throws RemoteException DOCUMENT ME!
     */
    public TestITS() throws RemoteException {
        System.out.println("Loading with Task = " + new Banana(rl, sl));
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            TestITS tits = new TestITS();
            tits.bindServer();
        } catch (RemoteException e) {
            System.out.println("Hohooo - " + e);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void bindServer() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }

        String name = "//localhost/TestITS";

        try {
            //InteractiveTaskServer server = new IslandsEvolutionServer();
            Naming.rebind(name, this);
            System.out.println("Tits bounds as TestITS");
        } catch (Exception e) {
            System.err.println("Tits exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param init DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RemoteException DOCUMENT ME!
     */
    public Object getID(Object init) throws RemoteException {
        System.out.println(init + " regging id " + clients);

        return new Integer(clients++);
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RemoteException DOCUMENT ME!
     */
    public InteractiveTask getTask(Object id) throws RemoteException {
        return new Banana(rl, sl);
    }

    /**
     * Called by clients when they wish to interact.
     *
     * @param ID the client's ID
     * @param clientTaskOutput the outputs of the client obtained by task.get(
     *        null ) on the client task
     *
     * @return whatever should be sent into the client through task.set( )
     *
     * @throws RemoteException DOCUMENT ME!
     */
    public Object interact(Object ID, Object clientTaskOutput)
        throws RemoteException {
        System.out.println("INTERACT " + clientTaskOutput);

        return "INT";
    }
}
