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
 * MultiServerDaemon.java
 *
 * Created on 15 March 2004, 16:57
 */
package org.jscience.computing.distributed.server;

import org.jscience.computing.distributed.InteractiveTask;
import org.jscience.computing.distributed.InteractiveTaskServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.*;


/**
 * <p/>
 * MultiServerDaemon allocates multiple clients between multiple servers in
 * real-time. An XML configuration file is regularly checked for changes to
 * the allocation scheme.  All clients connect to the same RMI server (same
 * host, port and binding name) which is this MultiServerDaemon.
 * MultiServerDaemon then decides which server to allocate each client to.
 * Control over which client gets allocated to what server is defined in an
 * XML file.  This file also defines the classes for the internal servers to
 * load and with what parameters. The current implementation allows the
 * following allocation policies to be defined in the configuration file:
 * </p>
 * <p/>
 * <ol>
 * <li>
 * Specific:  A server can be defined so that clients with a specified name
 * will always connect to it.  The name of the client is taken from the first
 * String in the Vector sent as a parameter to the getID() method.
 * </li>
 * <li>
 * Public:  Clients are allocated to public servers in a round-robin fashion.
 * </li>
 * </ol>
 * <p/>
 * <p/>
 * When the XML configuration file is changed during operation the server will
 * force clients to reconnect to fit the new allocation policy.  Following is
 * a sample XML configuration file:
 * </p>
 * <!-- Text only version:
 * <?xml version='1.0' encoding='utf-8'?>
 * <MSCFG>
 * <server>
 * <class>package.name.ClassName</class>
 * <args>arg0 arg1 passed ClassName constructor in ArrayList</args>
 * <name>MyFirstServer</name>
 * <client>Cruncher</client>
 * </server>
 * <server>
 * <class>other.package.name.OtherClassName</class>
 * <args>more args</args>
 * <name>MySecondServer</name>
 * </server>
 * <server>
 * <class>jaga.pj.circuits.control.EvoRepair</class>
 * <args>/home/username/log/ server0.txt C17 C All TP 5 0 1</args>
 * <name>evoRep</name>
 * <client>BANK</client>
 * </server>
 * <server>
 * <class>jaga.pj.circuits.control.EvoBIST</class>
 * z<args>/home/mmg20/log/test/ evob.txt nologging</args>
 * <name>evoBIST</name>
 * </server>
 * </MSCFG>
 * -->
 * <code> &lt;?xml version='1.0' encoding='utf-8'?&gt;<br>
 * &lt;MSCFG&gt;<br>
 * &lt;server&gt;<br>
 * &nbsp;&nbsp;&lt;class&gt;package.name.ClassName&lt;/class&gt;<br>
 * &nbsp;&nbsp;&lt;args&gt;arg0 arg1 passed ClassName constructor in ArrayList&lt;/args&gt;<br>
 * &nbsp;&nbsp;&lt;name&gt;MyFirstServer&lt;/name&gt;<br>
 * &nbsp;&nbsp;&lt;client&gt;Cruncher&lt;/client&gt;<br>
 * &lt;/server&gt;<br>
 * &lt;server&gt;<br>
 * &nbsp;&nbsp;&lt;class&gt;other.package.name.OtherClassName&lt;/class&gt;<br>
 * &nbsp;&nbsp;&lt;args&gt;more args&lt;/args&gt;<br>
 * &nbsp;&nbsp;&lt;name&gt;MySecondServer&lt;/name&gt;<br>
 * &lt;/server&gt;<br>
 * &lt;server&gt;<br>
 * &nbsp;&nbsp;&lt;class&gt;jaga.pj.circuits.control.EvoRepair&lt;/class&gt;<br>
 * &nbsp;&nbsp;&lt;args&gt;/home/username/log/ server0.txt C17 C All TP 5 0 1&lt;/args&gt;<br>
 * &nbsp;&nbsp;&lt;name&gt;evoRep&lt;/name&gt;<br>
 * &nbsp;&nbsp;&lt;client&gt;BANK&lt;/client&gt;<br>
 * &lt;/server&gt;<br>
 * &lt;server&gt;<br>
 * &nbsp;&nbsp;&lt;class&gt;jaga.pj.circuits.control.EvoBIST&lt;/class&gt;<br>
 * &nbsp;&nbsp;&lt;args&gt;/home/mmg20/log/test/ evob.txt nologging&lt;/args&gt;<br>
 * &nbsp;&nbsp;&lt;name&gt;evoBIST&lt;/name&gt;<br>
 * &lt;/server&gt;<br>
 * &lt;/MSCFG&gt;<br></code>
 * <p/>
 * <p/>
 * This XML file defines two servers with specific clients and two public
 * servers.  All clients with name Cruncher and BANK will be allocated to
 * servers MyFirstServer and evoRep respectively.  All other clients will be
 * shared between MySecondServer and evoBIST.
 * </p>
 *
 * @author mmg20
 */
public class MultiServerDaemon implements InteractiveTaskServer {
    /**
     * Number of seconds to wait between re-reading configuration file.
     */
    public static final int REFRESH_SECONDS = 10;

    // Working

    /**
     * DOCUMENT ME!
     */
    protected HashSet serverPool = new HashSet();

    /**
     * DOCUMENT ME!
     */
    protected Iterator serverPoolI = serverPool.iterator();

    /**
     * DOCUMENT ME!
     */
    protected Hashtable nameServer = new Hashtable();

    /**
     * DOCUMENT ME!
     */
    protected ArrayList clientRecords = new ArrayList();

    /**
     * DOCUMENT ME!
     */
    protected HashSet clientsToDisconnect = new HashSet();

    /**
     * DOCUMENT ME!
     */
    protected Thread configRefreshDaemon;

    /**
     * DOCUMENT ME!
     */
    protected String configFileName;

    /**
     * DOCUMENT ME!
     */
    protected int currGeneratedID = -1;

    /**
     * Creates a new instance of MultiServerStage2
     *
     * @param args Element 0 of this arraylist is a String holding the file
     *             name of the configuration file. Element 1 is a String holding
     *             the file name of the log file.
     * @throws Exception DOCUMENT ME!
     */
    public MultiServerDaemon(ArrayList args) throws Exception {
        configFileName = (String) args.get(0);

        if (args.size() > 1) {
            //DebugLib.register( this, ( String ) args.get( 1 ) );
        }

        startReadConfigDaemon();
    }

    /**
     * DOCUMENT ME!
     */
    protected void startReadConfigDaemon() {
        configRefreshDaemon = new Thread() {
            public void run() {
                while (true) {
                    try {
                        //System.out.println("Reading Config."); // Debug this
                        readXMLConfig(configFileName);
                        configRefreshDaemon.sleep(REFRESH_SECONDS * 1000);
                    } catch (Exception e) {
                        System.out.println("Exception while running " +
                                this.getClass().getName());
                        System.out.println(
                                "Printing stack trace and resuming.");
                        e.printStackTrace();
                    }
                }
            }
        };

        configRefreshDaemon.start();
    }

    /**
     * DOCUMENT ME!
     *
     * @param configFileName DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    protected void readXMLConfig(String configFileName)
            throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(configFileName));
        String currLine;
        HashSet serversFound = new HashSet();

        while ((currLine = br.readLine()) != null) {
            if (currLine.indexOf("<server>") >= 0) {
                String name = "";
                String className = "";
                String args = "";
                String clientName = "";

                while ((currLine = br.readLine()).indexOf("</server>") < 0) {
                    if (currLine.indexOf("<name>") >= 0) {
                        name = currLine.substring(currLine.indexOf("<name>") +
                                6, currLine.indexOf("</name>"));
                    }

                    if (currLine.indexOf("<class>") >= 0) {
                        className = currLine.substring(currLine.indexOf(
                                "<class>") + 7, currLine.indexOf("</class>"));
                    }

                    if (currLine.indexOf("<args>") >= 0) {
                        args = currLine.substring(currLine.indexOf("<args>") +
                                6, currLine.indexOf("</args>"));
                    }

                    if (currLine.indexOf("<client>") >= 0) {
                        clientName = currLine.substring(currLine.indexOf(
                                "<client>") + 8,
                                currLine.indexOf("</client>"));
                    }
                }

                InteractiveTaskServer inITS;
                ServerRecord sr;

                if (!nameServer.containsKey(name)) // If server has already been loaded, don't reload
                {
                    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
                    Class inITSClass = classLoader.loadClass(className);
                    ArrayList extraArgs = new ArrayList();
                    StringTokenizer st = new StringTokenizer(args);

                    while (st.hasMoreElements()) {
                        extraArgs.add(st.nextElement());
                    }

                    if (extraArgs.size() == 0) {
                        inITS = (InteractiveTaskServer) inITSClass.newInstance();
                    } else {
                        Class[] parameterTypes = {extraArgs.getClass()};
                        Constructor inITSConstructor = inITSClass.getConstructor(parameterTypes);
                        Object[] parameters = {extraArgs};
                        inITS = (InteractiveTaskServer) inITSConstructor.newInstance(parameters);
                    }

                    println("Succesfully loaded InteractiveTaskServer " +
                            inITS);

                    sr = new ServerRecord(name, inITS, clientName);
                    nameServer.put(name, sr);
                    serverPool.add(sr);
                    serverPoolI = serverPool.iterator();

                    if (!clientName.equals("")) {
                        // Server is new and specific, must find client and disconnect it
                        addToDisconnectList(clientName);
                    }
                } else // Server was already loaded
                {
                    sr = (ServerRecord) nameServer.get(name);
                    inITS = sr.getServer();

                    String oldClientName = sr.getSpecificClientName();

                    if (!oldClientName.equals("")) // Server was specific
                    {
                        if (!clientName.equals("")) // Is specific now too
                        {
                            if (oldClientName.equals(clientName)) {
                                //System.out.println(name + " already loaded as specific with same name." );
                            } else {
                                println(name +
                                        " was specific with different name. ");
                                addToDisconnectList(oldClientName); // First disconnect previous specific because not tied anymore
                                addToDisconnectList(clientName); // Add new client to disconnect list
                            }
                        } else {
                            println(name + " was specific, now open.");

                            // Server not specific anymore ** MINIMUM implementation here
                        }
                    } else {
                        // Was in Pool
                        if (!clientName.equals("")) // Is specific now
                        {
                            println(name + " becomes specific.");

                            // Disconnect all clients connected to this server
                            HashSet allocatedClients = sr.getAllocatedClients();
                            HashSet clientRecordsForSpecificName = clientRecordsForName(clientName);

                            HashSet specClientsNotConnectedToServer = (HashSet) clientRecordsForSpecificName.clone();
                            specClientsNotConnectedToServer.removeAll(allocatedClients);
                            clientsToDisconnect.addAll(specClientsNotConnectedToServer);

                            HashSet nonSpecClientsConnectedToServer = (HashSet) allocatedClients.clone();
                            nonSpecClientsConnectedToServer.removeAll(clientRecordsForSpecificName);
                            clientsToDisconnect.addAll(nonSpecClientsConnectedToServer);

                            // Switch from pool to specific
                        } else {
                            //System.out.println(name + " is still open");
                            // Still in pool, do nothing
                        }
                    } // end if old spec

                    sr.setSpecificClientName(clientName);
                } // end if loaded

                serversFound.add(sr);
            }
        }

        // Remove servers from pool which are not in config any more and disconnect their clients
        Iterator serversI = serverPool.iterator();

        while (serversI.hasNext()) {
            ServerRecord csr = (ServerRecord) serversI.next();

            if (!serversFound.contains(csr)) {
                HashSet allocatedClients = csr.getAllocatedClients();
                clientsToDisconnect.addAll(allocatedClients);
                serversI.remove();
                println(csr.getName() + " removed.");
                serverPoolI = serverPool.iterator();
            }
        }

        //serverPool.retainAll( serversFound );
    }

    /**
     * DOCUMENT ME!
     *
     * @param clientName DOCUMENT ME!
     */
    protected void addToDisconnectList(String clientName) {
        clientsToDisconnect.addAll(clientRecordsForName(clientName));
    }

    /**
     * DOCUMENT ME!
     *
     * @param clientName DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    protected HashSet clientRecordsForName(String clientName) {
        HashSet rv = new HashSet();

        for (int clp = 0; clp < clientRecords.size(); clp++) {
            ClientRecord currClientRecord = (ClientRecord) clientRecords.get(clp);

            if (currClientRecord.getName().equals(clientName)) {
                rv.add(currClientRecord);
            }
        }

        return rv;
    }

    /**
     * DOCUMENT ME!
     *
     * @param clientName DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    protected ServerRecord findSpecificServer(String clientName) {
        Iterator servers = serverPool.iterator();

        while (servers.hasNext()) {
            ServerRecord csr = (ServerRecord) servers.next();

            if (csr.getSpecificClientName().equals(clientName)) {
                return csr;
            }
        }

        return null;
    }

    /**
     * Called by clients the first time they connect.
     *
     * @param initialParameters Initial parameters from client local
     *                          configuration. The current client implementation, see
     *                          InteractiveTaskClient, provides the ip address of the client and
     *                          the command line paramaters used to launch the client.  These
     *                          are packaged in a Vector.
     * @return The ID of this client will from now on used for interaction with
     *         the server.
     * @throws java.rmi.RemoteException DOCUMENT ME!
     */
    public synchronized Object getID(Object initialParameters)
            throws java.rmi.RemoteException {
        Object rv = new Integer(++currGeneratedID);
        println("Entering getID with params " + initialParameters +
                ". New MSD ID = " + currGeneratedID); // D

        String clientName = (String) ((Vector) initialParameters).get(0);
        ClientRecord cr = new ClientRecord(rv, clientName);
        clientRecords.add(cr);

        // Now find a server for it
        ServerRecord server = findSpecificServer(clientName);

        if (server != null) // Allocate specific server
        {
            println("Found specific server for " + clientName + " is " +
                    server);
        } else {
            int iterations = 0;

            do {
                if (!serverPoolI.hasNext()) {
                    serverPoolI = serverPool.iterator();
                    iterations++;
                }

                server = (ServerRecord) serverPoolI.next();
            } while (!server.getSpecificClientName().equals("") &&
                    (iterations < 2));

            // WARNING: If no unspecific server then other clients will be assigned to specific servers
            println("Allocated from pool for " + clientName + " server " +
                    server);
        }

        cr.setAllocatedServer(server);
        server.allocateClient(cr);

        InteractiveTaskServer its = server.getServer();
        Object rid = its.getID(initialParameters);
        cr.setRealId(rid);

        return rv;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws java.rmi.RemoteException  DOCUMENT ME!
     * @throws java.rmi.ConnectException DOCUMENT ME!
     */
    protected void disconnectClient() throws java.rmi.RemoteException {
        throw new java.rmi.ConnectException(
                "Client has been reallocated to a different server, must reconnect.");
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     * @throws java.rmi.RemoteException DOCUMENT ME!
     */
    protected void checkAndDisconnectClient(Object id)
            throws java.rmi.RemoteException {
        ClientRecord cr = (ClientRecord) clientRecords.get(((Integer) id).intValue());

        if (clientsToDisconnect.contains(cr)) {
            println("Disconnecting " + cr);

            ServerRecord sr = cr.getAllocatedServer();
            sr.deallocateClient(cr);
            cr.setAllocatedServer(null);
            clientsToDisconnect.remove(cr);
            disconnectClient();
        }
    }

    /**
     * This will provide the client with the InteractiveTask it should run.
     *
     * @param id The ID provided by the getID method by which the client will
     *           always refer to itself.
     * @return The task this client should run.  This could be exactly the same
     *         as the one all other clients are running or tailored to this
     *         particular client.
     * @throws java.rmi.RemoteException DOCUMENT ME!
     */
    public InteractiveTask getTask(Object id) throws java.rmi.RemoteException {
        checkAndDisconnectClient(id);

        ClientRecord cr = (ClientRecord) clientRecords.get(((Integer) id).intValue());
        ServerRecord sr = cr.getAllocatedServer();
        InteractiveTaskServer its = sr.getServer();

        return its.getTask(cr.getRealId());
    }

    /**
     * Called by clients when they wish to interact.
     *
     * @param id               the client's ID
     * @param clientTaskOutput the outputs of the client obtained by task.get(
     *                         null ) on the client task
     * @return whatever should be sent into the client through task.set( )
     * @throws java.rmi.RemoteException DOCUMENT ME!
     */
    public Object interact(Object id, Object clientTaskOutput)
            throws java.rmi.RemoteException {
        checkAndDisconnectClient(id);

        ClientRecord cr = (ClientRecord) clientRecords.get(((Integer) id).intValue());
        InteractiveTaskServer its = cr.getAllocatedServer().getServer();

        return its.interact(cr.getRealId(), clientTaskOutput);
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    protected void println(String text) {
        //DebugLib.println( this, text );
    }
}
