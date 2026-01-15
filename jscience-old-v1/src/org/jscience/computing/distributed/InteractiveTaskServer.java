/*
 * InteractiveTaskServer.java
 *
 * Created on 16 April 2001, 17:00
 */
package org.jscience.computing.distributed;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * <p/>
 * Remote interface for clients to connect to a server managing interactive
 * tasks.  The server and clients work as follows:
 * <p/>
 * <ol>
 * <li>
 * Client gets handle of server through RMI and calls the getID method. From
 * then on this client will always use this ID when communicating to the
 * server.
 * </li>
 * <li>
 * Client will ask for the task and then start running it.
 * </li>
 * <li>
 * Client will regularly call the interact method to exchange I/O with the
 * server.
 * </li>
 * </ol>
 * </p>
 * The reason this is not in the server package is that it is not a server side
 * only object.  Ie the client must know about it.
 *
 * @author Michael Garvie
 */
public interface InteractiveTaskServer extends Remote {
    /**
     * Called by clients the first time they connect.
     *
     * @param initialParameters Initial parameters from client local
     *        configuration. The current client implementation, see
     *        InteractiveTaskClient, provides the ip address of the client and
     *        the command line paramaters used to launch the client.  These
     *        are packaged in a Vector.
     *
     * @return The ID of this client will from now on used for interaction with
     *         the server.
     *
     * @throws RemoteException DOCUMENT ME!
     */
    public Object getID(Object initialParameters) throws RemoteException;

    /**
     * This will provide the client with the InteractiveTask it should
     * run.
     *
     * @param id The ID provided by the getID method by which the client will
     *        always refer to itself.
     *
     * @return The task this client should run.  This could be exactly the same
     *         as the one all other clients are running or tailored to this
     *         particular client.
     *
     * @throws RemoteException DOCUMENT ME!
     */
    public InteractiveTask getTask(Object id) throws RemoteException;

    /**
     * Called by clients when they wish to interact.
     *
     * @param id the client's ID
     * @param clientTaskOutput the outputs of the client obtained by task.get(
     *        null ) on the client task
     *
     * @return whatever should be sent into the client through task.set( )
     *
     * @throws RemoteException DOCUMENT ME!
     */
    public Object interact(Object id, Object clientTaskOutput)
        throws RemoteException;
}
