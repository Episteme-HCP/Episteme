/*
 * InteractiveTaskClient.java
 *
 * Created on 16 April 2001, 17:33
 */
package org.jscience.computing.distributed.client;

import org.jscience.computing.distributed.InteractiveTask;
import org.jscience.computing.distributed.InteractiveTaskServer;

import java.io.IOException;
import java.rmi.*;
import java.util.Vector;

/**
 * This is a generic client that will connect to an InteractiveTaskServer, download its task,
 * run it and perform interaction with the server at regular intervals. <p>
 * The client is pull based in that it is the sole initiator of all communication. <p>
 * There are two main modes the client can be in: <p>
 * 1. Connecting. <p>
 * 2. Interacting. <p>
 * <p/>
 * The client starts in the first and enters the second once it manages to
 * contact the server.  It will fallback onto the first if communication is broken. <p>
 * <p/>
 * Once connection is re-established with the server the client will check that
 * the task the server is sending out is equal to the one it's been running.  If
 * it is, then the client resumes interaction mode.  If it isn't then the previous
 * task is aborted, the new one started before the client resumes interaction mode. <p>
 *
 * @author Michael Garvie
 */
public class InteractiveTaskClient extends Object {
    final static int CONNECT_MODE = 0;
    final static int INTERACT_MODE = 1;
    final static int MINUTES = 1000 * 60;
    private Object id;
    private InteractiveTaskServer server;
    private InteractiveTask task;
    private Thread taskThread;
    private String taskSnap;
    private Vector data4Server;
    private String serverClassName;
    private int mode = CONNECT_MODE;

    //final static int MINUTES = 1000 * 10; // D
    int[] periods = {5 * MINUTES, 1 * MINUTES};

    /**
     * Creates new InteractiveTaskClient
     *
     * @param hostName    The name of the host where the interactive task
     *                    server we wish to connect to is residing.
     * @param bindingName The name under which the server is registered in
     *                    the RMI registry.
     * @param data4Server The main method will make this parameter a Vector
     *                    containing various strings.  The first being the
     *                    ip address of the client and the rest being the
     *                    remainding command line arguments after the
     *                    hostName and bindingName.
     */
    public InteractiveTaskClient(String hostName, String bindingName,
                                 Vector data4Server) {
        this.data4Server = data4Server;
        serverClassName = "//" + hostName + "/" + bindingName;
    }

    /**
     * This method loops forever trying to connect to the server
     * if it isn't and interacting with it if it is connected.
     */
    public void connectLoop() throws InterruptedException {
        boolean serverRestarted = false;

        while (true) {
            try {
                switch (mode) {
                    case CONNECT_MODE: {
                        println("Trying to get task from server " +
                                serverClassName);
                        server = (InteractiveTaskServer) Naming.lookup(serverClassName);
                        id = server.getID(data4Server);

                        InteractiveTask newTask = server.getTask(id);
                        println("Got it, task is " + newTask);

                        if (!newTask.toString().equals(taskSnap)) {
                            if (serverRestarted) {
                                restartClient();
                            }

                            // really is new task
                            if (taskThread != null) {
                                taskThread.interrupt();
                            }

                            task = newTask;
                            taskSnap = task.toString();
                            taskThread = spawnThread();
                            taskThread.start();
                        }

                        mode = INTERACT_MODE;

                        break;
                    }

                    case INTERACT_MODE: {
                        task.set(server.interact(id, task.get(null)));

                        break;
                    }
                }
            } catch (ConnectException ce) {
                System.out.println(ce);

                if (ce.getMessage().startsWith("Connection refused to host")) {
                    serverRestarted = true;
                    println("No worries, server is probably being restarted.");
                }

                mode = CONNECT_MODE;
            } catch (UnmarshalException ue) {
                println(ue);
                restartClient();
            } catch (IOException ioe) {
                println(ioe);
                mode = CONNECT_MODE;
            } catch (NotBoundException nbe) {
                println(nbe);
                mode = CONNECT_MODE;
            } catch (Exception e) {
                println("InteractiveTaskClient exception: " + e.getMessage());
                e.printStackTrace();
            } finally {
                Thread.currentThread().sleep(periods[mode]);
                System.gc();
            }
        }
    }

    private void restartClient() {
        String narrator = "RestartInteractiveTaskClientEvent: The server might have ";
        narrator += "been restarted with new versions of classes, ";
        narrator += "client should be restarted just in case.";
        println(narrator);

        try {
            Thread.currentThread().sleep(5000); // Wait a little so that RestartInteractiveTaskClientEvent: string at output can be handled if necesary.
        } catch (InterruptedException e) {
        }

        System.exit(0);
    }

    private Thread spawnThread() {
        Thread t = new Thread() {
            String taskSnap = task.toString();

            public void run() {
                try {
                    println(task.run(null));
                } catch (InterruptedException e) {
                    println("ITC - " + taskSnap + " interrupted ");
                }
            }
        };

        return t;
    }

    /**
     * Method executed when the client is started.
     *
     * @param args[0] Server host address can be any internet address.
     * @param args[1] Server binding name is RMI registry registration name.
     * @param args[2] If preceded with -i interact loop time in minutes.
     * @param args[3] If preceded with -c connect loop time in minutes.
     */
    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }

        try {
            Vector data4Server = new Vector();
            data4Server.add(args[2]); // the name

            try {
                data4Server.add(java.net.InetAddress.getLocalHost()); // IP add
            } catch (java.net.UnknownHostException e) {
                data4Server.add(null);
            }

            InteractiveTaskClient c = new InteractiveTaskClient(args[0],
                    args[1], data4Server);

            int currArg = 3;

            while (currArg < args.length) {
                char option;

                if (args[currArg].charAt(0) == '-') {
                    option = args[currArg].charAt(1);

                    switch (option) {
                        case'd': {
                            //DebugLib.trcLogger.isLogging = true;
                            currArg++;

                            //println("Running in debug mode.");
                            break;
                        }

                        case'i': {
                            int itime = new Integer(args[++currArg]).intValue();
                            c.periods[INTERACT_MODE] = MINUTES * itime;
                            currArg++;
                            println("Interact period is " + itime + " minutes.");

                            break;
                        }

                        case'c': {
                            int ctime = new Integer(args[++currArg]).intValue();
                            c.periods[CONNECT_MODE] = MINUTES * ctime;
                            currArg++;
                            println("Connect period is " + ctime + " minutes.");

                            break;
                        }
                    }
                } else {
                    data4Server.add(args[currArg]);
                    currArg++;
                }
            }

            c.connectLoop();
        } catch (InterruptedException e) {
            println("Someone stopped the client, fair enough..");
        } catch (ArrayIndexOutOfBoundsException e) {
            String narrator = "Usage: InteractiveTaskClient <server address> <server name> <client name> [options]";
            println(narrator);
        }
    }

    private static void println(Object o) {
        //DebugLib.println( o );
        System.out.println(o.toString());
    }
}
