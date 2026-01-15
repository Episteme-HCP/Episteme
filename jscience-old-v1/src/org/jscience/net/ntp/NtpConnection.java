//////////////////////license & copyright header///////////////////////
//                                                                   //
//                Copyright (c) 1999 by Michel Van den Bergh         //
//                                                                   //
// This library is free software; you can redistribute it and/or     //
// modify it under the terms of the GNU Lesser General Public        //
// License as published by the Free Software Foundation; either      //
// version 2 of the License, or (at your option) any later version.  //
//                                                                   //
// This library is distributed in the hope that it will be useful,   //
// but WITHOUT ANY WARRANTY; without even the implied warranty of    //
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU //
// Lesser General Public License for more details.                   //
//                                                                   //
// You should have received a copy of the GNU Lesser General Public  //
// License along with this library; if not, write to the             //
// Free Software Foundation, Inc., 59 Temple Place, Suite 330,       //
// Boston, MA  02111-1307  USA, or contact the author:               //
//                                                                   //
//                  Michel Van den Bergh  <vdbergh@luc.ac.be>        //
//                                                                   //
////////////////////end license & copyright header/////////////////////
package org.jscience.net.ntp;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import java.util.Date;
import java.util.Vector;


/**
 * This class encapsulates the exchange of a NtpDatagram between a client
 * and a server.
 */
public class NtpConnection {
    /** Default port for the NTP protocol. */
    public static final int defaultNtpPort = 123;

    /** DOCUMENT ME! */
    private InetAddress ntpServer;

    /** DOCUMENT ME! */
    private int ntpPort;

    /** DOCUMENT ME! */
    private DatagramSocket datagramSocket;

    /** DOCUMENT ME! */
    private int maxHops = 15;

    /** DOCUMENT ME! */
    private int timeout = 10000;

/**
     * Creates a UDP connection for the exchange of NtpDatagrams.
     *
     * @param iaddr DOCUMENT ME!
     * @param iport DOCUMENT ME!
     * @throws SocketException DOCUMENT ME!
     */
    public NtpConnection(InetAddress iaddr, int iport)
        throws SocketException {
        ntpServer = iaddr;
        ntpPort = iport;
        datagramSocket = new DatagramSocket();
        datagramSocket.setSoTimeout(timeout);
    }

/**
     * Creates a UDP connection for the exchange of NtpDatagrams.
     *
     * @param iaddr DOCUMENT ME!
     * @throws SocketException DOCUMENT ME!
     */
    public NtpConnection(InetAddress iaddr) throws SocketException {
        ntpServer = iaddr;
        ntpPort = defaultNtpPort;
        datagramSocket = new DatagramSocket();
        datagramSocket.setSoTimeout(timeout);
    }

    /**
     * Get the timeout associated with the connection. The default
     * timeout is 10s.
     *
     * @return DOCUMENT ME!
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Set the timeout associated with the connection.
     *
     * @param timeout DOCUMENT ME!
     *
     * @throws SocketException DOCUMENT ME!
     */
    public void setTimeout(int timeout) throws SocketException {
        this.timeout = timeout;
        datagramSocket.setSoTimeout(timeout);
    }

    /**
     * Send a NtpDatagram to the server.
     *
     * @param ntpDatagramPacket DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void send(NtpDatagramPacket ntpDatagramPacket)
        throws IOException {
        datagramSocket.send(ntpDatagramPacket.getDatagramPacket());
    }

    /**
     * Wait for a reply from the server.
     *
     * @param ntpDatagramPacket DOCUMENT ME!
     *
     * @throws IOException A IOException is thrown in case of a timeout.
     */
    public void receive(NtpDatagramPacket ntpDatagramPacket)
        throws IOException {
        datagramSocket.receive(ntpDatagramPacket.getDatagramPacket());
        ntpDatagramPacket.setReceptionTimeStamp(new TimeStamp(new Date()));
    }

    /**
     * Obtain info from the server.
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException A IOException is thrown in case of a timeout.
     *
     * @see NtpInfo
     */
    public NtpInfo getInfo() throws IOException {
        NtpDatagramPacket dpSend = new NtpDatagramPacket(ntpServer, ntpPort);
        NtpDatagramPacket dpReceive = new NtpDatagramPacket();
        send(dpSend);
        receive(dpReceive);

        return dpReceive.getInfo();
    }

    /**
     * Traces a server to the primary server.
     *
     * @return Vector containing the NtpInfo objects associated with the
     *         servers on the path to the primary server. Sometimes only a
     *         partial list will be generated due to timeouts or other
     *         problems.
     */
    public Vector getTrace() {
        Vector traceList = new Vector();
        int hops = 0;
        boolean finished = false;
        NtpConnection currentNtpConnection = this;

        while ((!finished) && (hops < maxHops)) {
            try {
                NtpInfo info = currentNtpConnection.getInfo();

                if (currentNtpConnection != this) {
                    currentNtpConnection.close();
                }

                traceList.addElement(info);

                if (info.referenceIdentifier instanceof InetAddress) {
                    currentNtpConnection = new NtpConnection((InetAddress) info.referenceIdentifier);
                    hops++;
                } else {
                    finished = true;
                }
            } catch (Exception e) {
                finished = true;
            }
        }

        return traceList;
    }

    /**
     * Get the time from the server.
     *
     * @return A Date object containing the server time, adjusted for roundtrip
     *         delay. Note that it is better to use getInfo() and then to use
     *         the offset field of the returned NtpInfo object.
     */
    public Date getTime() {
        try {
            long offset = getInfo().offset;

            return new Date(System.currentTimeMillis() + offset);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Close the connection.
     */
    public void close() {
        datagramSocket.close();
    }

    /**
     * Could be used to do some cleaning up. The default implementation
     * just invokes close().
     */
    public void finalize() {
        close();
    }
}
