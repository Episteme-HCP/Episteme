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

package org.jscience.architecture.traffic.infrastructure;

import org.jscience.architecture.traffic.TrafficException;
import org.jscience.architecture.traffic.simulation.SimModel;
import org.jscience.architecture.traffic.simulation.SimulationRunningException;
import org.jscience.architecture.traffic.util.ListEnumeration;
import org.jscience.architecture.traffic.xml.*;

import java.awt.*;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Dictionary;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * NetTunnel, our K-r4D 31337 eAsTr egq0r! ph34R uS !!!!!
 *
 * @author Group Datastructures
 * @version 666
 */
public class NetTunnel extends SpecialNode {
    /** The type of this node */
    protected static final int type = Node.NET_TUNNEL;

    /** The port on which this this Tunnel should be listening */
    protected int localPort;

    /** The hostname and port to which this Tunnel should send its Roadusers */
    protected String remoteHostname;

    /** DOCUMENT ME! */
    protected int remotePort;

    /** Indicates if our network gear is ready for rock 'n roll */
    protected boolean netInitialized = false;

    /**
     * A LinkedList with Roadusers which are waiting to be send to the
     * remote machine
     */
    protected LinkedList sendQueue;

    /** These variables indicate the state of the infra */
    protected boolean paused = false;

    /** These variables indicate the state of the infra */
    protected boolean stopped = false;

    /** These variables indicate the state of the infra */
    protected boolean sigStop = false;

    /** Network stuff */
    XMLSaver sender;

    /** DOCUMENT ME! */
    SocketServer server;

/**
     * Creates a new NetTunnel object.
     */
    public NetTunnel() {
        super();
        sendQueue = new LinkedList();
    }

/**
     * Creates a new NetTunnel object.
     *
     * @param _coord DOCUMENT ME!
     */
    public NetTunnel(Point _coord) {
        super(_coord);
        sendQueue = new LinkedList();
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
        try {
            sendQueue = new LinkedList();
            server = new SocketServer();
            server.start();
            netInitialized = true;
        } catch (Exception e) {
            System.out.println("Cannot start NetTunnel " + nodeId + " : " + e);
            e.printStackTrace(System.out);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void stop() {
        sigStop = true;
        sender.close();
        stopped = true;
        notifyAll();
        reset();
    }

    /**
     * DOCUMENT ME!
     *
     * @param ru DOCUMENT ME!
     */
    public void enter(Roaduser ru) {
        sendQueue.add(ru);
        super.enter(ru);
    }

    /*============================================*/
    /* LOAD and SAVE                              */
    /*============================================*/
    public void load(XMLElement myElement, XMLLoader loader)
        throws XMLTreeException, IOException, XMLInvalidInputException {
        super.load(myElement, loader);
        remoteHostname = myElement.getAttribute("remote-host").getValue();
        remotePort = myElement.getAttribute("remote-port").getIntValue();
        localPort = myElement.getAttribute("local-port").getIntValue();
        sendQueue = (LinkedList) (XMLArray.loadArray(this, loader));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public XMLElement saveSelf() throws XMLCannotSaveException {
        XMLElement result = super.saveSelf();
        result.setName("node-tunnel");
        result.addAttribute(new XMLAttribute("remote-host", remoteHostname));
        result.addAttribute(new XMLAttribute("remote-port", remotePort));
        result.addAttribute(new XMLAttribute("local-port", localPort));

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param saver DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public void saveChilds(XMLSaver saver)
        throws XMLTreeException, IOException, XMLCannotSaveException {
        super.saveChilds(saver);
        XMLUtils.setParentName(new ListEnumeration(sendQueue), getXMLName());
        XMLArray.saveArray(sendQueue, this, saver, "send-queue");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return parentName + ".node-tunnel";
    }

    /**
     * DOCUMENT ME!
     *
     * @param dictionaries DOCUMENT ME!
     *
     * @throws XMLInvalidInputException DOCUMENT ME!
     * @throws XMLTreeException DOCUMENT ME!
     */
    public void loadSecondStage(Dictionary dictionaries)
        throws XMLInvalidInputException, XMLTreeException {
        super.loadSecondStage(dictionaries);
        XMLUtils.loadSecondStage(new ListEnumeration(sendQueue), dictionaries);
    }

    /*============================================*/
    /* Basic GET and SET methods                  */
    /*============================================*/
    /**
     * Returns the type of this node
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * Returns the name of this nettunnel.
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "NetTunnel " + nodeId;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getRemoteHostname() {
        return remoteHostname;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLocalPort() {
        return localPort;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRemotePort() {
        return remotePort;
    }

    /**
     * DOCUMENT ME!
     *
     * @param remoteHostname DOCUMENT ME!
     *
     * @throws SimulationRunningException DOCUMENT ME!
     */
    public void setRemoteHostname(String remoteHostname)
        throws SimulationRunningException {
        if (netInitialized) {
            throw new SimulationRunningException(
                "Cannot change network settings of NetTunnel when simulation is running.");
        } else {
            this.remoteHostname = remoteHostname;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param localPort DOCUMENT ME!
     *
     * @throws SimulationRunningException DOCUMENT ME!
     */
    public void setLocalPort(int localPort) throws SimulationRunningException {
        if (netInitialized) {
            throw new SimulationRunningException(
                "Cannot change network settings of NetTunnel when simulation is running.");
        } else {
            this.localPort = localPort;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param remotePort DOCUMENT ME!
     *
     * @throws SimulationRunningException DOCUMENT ME!
     */
    public void setRemotePort(int remotePort) throws SimulationRunningException {
        if (netInitialized) {
            throw new SimulationRunningException(
                "Cannot change network settings of NetTunnel when simulation is running.");
        } else {
            this.remotePort = remotePort;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        super.reset();
        sendQueue = new LinkedList();
        waitingQueue = new LinkedList();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSendQueueLength() {
        return sendQueue.size();
    }

    /*============================================*/
    /* Graphics stuff                             */
    /*============================================*/
    public void paint(Graphics g) throws TrafficException {
        paint(g, 0, 0, 1.0f, 0.0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param zf DOCUMENT ME!
     *
     * @throws TrafficException DOCUMENT ME!
     */
    public void paint(Graphics g, int x, int y, float zf)
        throws TrafficException {
        paint(g, x, y, zf, 0.0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param zf DOCUMENT ME!
     * @param bogus DOCUMENT ME!
     *
     * @throws TrafficException DOCUMENT ME!
     */
    public void paint(Graphics g, int x, int y, float zf, double bogus)
        throws TrafficException {
        int width = getWidth();
        g.setColor(Color.green);
        g.drawRect((int) (((coord.x + x) - (5 * width)) * zf),
            (int) (((coord.y + y) - (5 * width)) * zf),
            (int) (10 * width * zf), (int) (10 * width * zf));

        if (nodeId != -1) {
            g.drawString("" + nodeId,
                (int) (((coord.x + x) - (5 * width)) * zf) - 10,
                (int) (((coord.y + y) - (5 * width)) * zf) - 3);
        }
    }

    /*============================================*/
    /* Receive stuff                                     */
    /*============================================*/
    public void receive(XMLElement element)
        throws XMLInvalidInputException, InfraException {
        waitingQueue.add(RoaduserFactory.genRoaduser(element.getAttribute(
                    "type").getIntValue(), this, this, 0));
    }

    /**
     * DOCUMENT ME!
     *
     * @param model DOCUMENT ME!
     */
    public void doStep(SimModel model) {
        super.doStep(model);

        try {
            processSend();
            processReceive(model);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param model DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public void processReceive(SimModel model) throws InfraException {
        Iterator i = waitingQueue.iterator();
        Roaduser ru;

        while (i.hasNext()) {
            ru = (Roaduser) (i.next());
            ru.setDestNode(model.getRandomDestination(this));

            try {
                placeRoaduser(ru);
                i.remove();
            } catch (InfraException e) { // Lane was full. Wait till next turn
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws XMLCannotSaveException DOCUMENT ME!
     * @throws XMLTreeException DOCUMENT ME!
     */
    public void processSend()
        throws IOException, XMLCannotSaveException, XMLTreeException {
        if ((sender == null) || !sender.hasStream()) {
            try {
                sender = new XMLSaver(remoteHostname, remotePort);
            } catch (Exception e) {
                System.out.println("NetTunnel " + nodeId +
                    " cannot connect to its peer on " + remoteHostname + ":" +
                    remotePort + ". Postponing send. I will retry later.");
            }
        } else {
            Iterator i = sendQueue.iterator();
            Roaduser ru;

            while (i.hasNext()) {
                sender.saveAtomaryElement(null,
                    ((Roaduser) (i.next())).saveSelf());
                i.remove();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    class TwoStageLoaderData {
        /** DOCUMENT ME! */
        int roadId;
    }

    /*============================================*/
    /* Server thread                              */
    /*============================================*/
    class SocketServer extends Thread {
        /** DOCUMENT ME! */
        private ServerSocket socket;

/**
         * Creates a new SocketServer object.
         */
        public SocketServer() {
            try {
                socket = new ServerSocket(localPort);
            } catch (Exception e) {
                System.out.println(
                    "Unable to make socket server for NetTunnel." +
                    "Cannot receive roadusers :" + e);
            }
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            Socket client;

            while (!sigStop) {
                System.out.println("Server socket of tunnel " + nodeId +
                    " runs on  " + localPort);

                try {
                    client = socket.accept();
                    (new ReceivingSocket(client)).start();
                    System.out.println("NetTunnel " + nodeId +
                        " received new connection.");
                } catch (Exception e) {
                    System.out.println("NetTunnel couldn't accept connection :" +
                        e);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    class ReceivingSocket extends Thread {
        /** DOCUMENT ME! */
        private XMLLoader loader;

/**
         * Creates a new ReceivingSocket object.
         *
         * @param socket DOCUMENT ME!
         */
        public ReceivingSocket(Socket socket) {
            try {
                loader = new XMLLoader(socket);
            } catch (Exception e) {
                System.out.println(
                    "Problem with initializing listening socket for NetTunnel");
            }
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            while (!sigStop) {
                try {
                    receive(loader.getNextElement(null));
                } catch (InfraException e) {
                    System.out.println(
                        "Cannot generate received RU in NetTunnel " + nodeId);
                } catch (Exception e) {
                    System.out.println("A receiver in NetTunnel " + nodeId +
                        " broke of its connection.");

                    //e.printStackTrace(System.out);
                    return;
                }
            }

            if (sigStop) {
                loader.close();
            }
        }
    }
}
