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

import java.net.DatagramPacket;
import java.net.InetAddress;


/**
 * This class encapsulates a ntp-datagram as described in rfc2030. Such a
 * datagram consists of a header and four timestamps. The four timestamps are
 * respectively:
 * <p/>
 * <UL>
 * <li>
 * The reference timestamp. This indicates when the local clock was last set.
 * Can be set to zero for datagrams originating on the client.
 * </li>
 * <li>
 * The originate timestamp. Indicates when the datagram originated on the
 * client. Copied by the server from the transmit timestamp. Can be set to
 * zero for datagrams originating on the client.
 * </li>
 * <li>
 * The receive timestamp. Indicates when the reply left the server. Can be set
 * to zero for datagrams originating on the client.
 * </li>
 * <li>
 * The transmit timestamp. Indicates when the datagram departed.
 * </li>
 * </ul>
 * <p/>
 * We have added a fifth timestamp. Namely a 'reception timestamp' which is
 * normally set by NtpConnection.receive(NtpDatagramPacket). When transmitted
 * a ntp-datagram is wrapped in a UDP datagram.
 *
 * @author Michel Van den Bergh
 * @version 1.0
 * @see java.net.Datagram
 * @see TimeStamp
 * @see NtpHeader
 * @see NtpConnection
 */
public class NtpDatagramPacket {
    /**
     * DOCUMENT ME!
     */
    private static final int headerOffset = 0;

    /**
     * DOCUMENT ME!
     */
    private static final int referenceTimeStampOffset = 16;

    /**
     * DOCUMENT ME!
     */
    private static final int originateTimeStampOffset = 24;

    /**
     * DOCUMENT ME!
     */
    private static final int receiveTimeStampOffset = 32;

    /**
     * DOCUMENT ME!
     */
    private static final int transmitTimeStampOffset = 40;

    /**
     * DOCUMENT ME!
     */
    private static final int ntpDatagramLength = 48;

    /**
     * DOCUMENT ME!
     */
    private DatagramPacket dp;

    /**
     * DOCUMENT ME!
     */
    private TimeStamp receptionTimeStamp;

    /**
     * Construct a NtpDatagram from a header, four timestamps, an Inetaddress
     * and a portnumber.
     *
     * @see InetAddress
     */
    public NtpDatagramPacket(NtpHeader header, TimeStamp referenceTimeStamp,
                             TimeStamp originateTimeStamp, TimeStamp receiveTimeStamp,
                             TimeStamp transmitTimeStamp, InetAddress iaddr, int iport) {
        byte[] temp;
        byte[] buffer = new byte[ntpDatagramLength];

        for (int i = headerOffset; i < referenceTimeStampOffset; i++) {
            buffer[i] = (header.getData())[i - headerOffset];
        }

        for (int i = referenceTimeStampOffset; i < originateTimeStampOffset;
             i++) {
            temp = referenceTimeStamp.getData();
            buffer[i] = temp[i - referenceTimeStampOffset];
        }

        for (int i = originateTimeStampOffset; i < receiveTimeStampOffset;
             i++) {
            temp = originateTimeStamp.getData();
            buffer[i] = temp[i - originateTimeStampOffset];
        }

        for (int i = receiveTimeStampOffset; i < transmitTimeStampOffset;
             i++) {
            temp = receiveTimeStamp.getData();
            buffer[i] = temp[i - receiveTimeStampOffset];
        }

        for (int i = transmitTimeStampOffset; i < ntpDatagramLength; i++) {
            temp = transmitTimeStamp.getData();
            buffer[i] = temp[i - transmitTimeStampOffset];
        }

        dp = new DatagramPacket(buffer, ntpDatagramLength, iaddr, iport);
    }

    /**
     * Construct a NtpDatagram with only the transmit timestamp filled in (set
     * to the current time). The header is set to a NtpHeader.defaultHeader.
     *
     * @see NtpHeader
     */
    public NtpDatagramPacket(InetAddress iaddr, int iport) {
        this(NtpHeader.defaultHeader, TimeStamp.zero, TimeStamp.zero,
                TimeStamp.zero, new TimeStamp(), iaddr, iport);
    }

    /**
     * Constructs an uninitialized NtpDatagram.
     */
    public NtpDatagramPacket() {
        byte[] buffer = new byte[ntpDatagramLength];
        dp = new DatagramPacket(buffer, ntpDatagramLength);
    }

    /**
     * Constructs an uninitialized NtpDatagram from a UDP datagram.
     *
     * @param dp DOCUMENT ME!
     */
    public NtpDatagramPacket(DatagramPacket dp) {
        this.dp = dp;
    }

    /**
     * Returns the UDP datagram associated to an NtpDatagram.
     *
     * @return DOCUMENT ME!
     */
    DatagramPacket getDatagramPacket() {
        return dp;
    }

    /**
     * Returns the header associated to a NtpDatagram.
     *
     * @see NtpHeader
     */
    public NtpHeader getHeader() {
        byte[] buffer = dp.getData();
        byte[] temp = new byte[16];

        for (int i = headerOffset; i < referenceTimeStampOffset; i++) {
            temp[i - headerOffset] = buffer[i];
        }

        return new NtpHeader(temp);
    }

    /**
     * Returns the reference timestamp.
     *
     * @return DOCUMENT ME!
     */
    public TimeStamp getReferenceTimeStamp() {
        byte[] buffer = dp.getData();
        byte[] temp = new byte[8];

        for (int i = referenceTimeStampOffset; i < originateTimeStampOffset;
             i++) {
            temp[i - referenceTimeStampOffset] = buffer[i];
        }

        return new TimeStamp(temp);
    }

    /**
     * Returns the originate timestamp
     *
     * @return DOCUMENT ME!
     */
    public TimeStamp getOriginateTimeStamp() {
        byte[] buffer = dp.getData();
        byte[] temp = new byte[8];

        for (int i = originateTimeStampOffset; i < receiveTimeStampOffset;
             i++) {
            temp[i - originateTimeStampOffset] = buffer[i];
        }

        return new TimeStamp(temp);
    }

    /**
     * Returns the receive timestamp
     *
     * @return DOCUMENT ME!
     */
    public TimeStamp getReceiveTimeStamp() {
        byte[] buffer = dp.getData();
        byte[] temp = new byte[8];

        for (int i = receiveTimeStampOffset; i < transmitTimeStampOffset;
             i++) {
            temp[i - receiveTimeStampOffset] = buffer[i];
        }

        return new TimeStamp(temp);
    }

    /**
     * Returns the transmit timestamp
     *
     * @return DOCUMENT ME!
     */
    public TimeStamp getTransmitTimeStamp() {
        byte[] buffer = dp.getData();
        byte[] temp = new byte[8];

        for (int i = transmitTimeStampOffset; i < ntpDatagramLength; i++) {
            temp[i - transmitTimeStampOffset] = buffer[i];
        }

        return new TimeStamp(temp);
    }

    /**
     * Returns the reception timestamp
     *
     * @return DOCUMENT ME!
     */
    public TimeStamp getReceptionTimeStamp() {
        return receptionTimeStamp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param receptionTimeStamp DOCUMENT ME!
     */
    void setReceptionTimeStamp(TimeStamp receptionTimeStamp) {
        this.receptionTimeStamp = receptionTimeStamp;
    }

    /**
     * A convenience method which returns the useful information contained in a
     * NtpDatagram.
     *
     * @see NtpInfo
     */
    public NtpInfo getInfo() {
        NtpInfo info = new NtpInfo();
        NtpHeader h = getHeader();
        info.serverAddress = dp.getAddress();
        info.leapYearIndicator = h.getLeapYearIndicator();
        info.versionNumber = h.getVersionNumber();
        info.stratum = h.getStratum();
        info.mode = h.getMode();
        info.pollInterval = h.getPollInterval();
        info.precision = h.getPrecision();
        info.rootDelay = h.getRootDelay();
        info.rootDispersion = h.getRootDispersion();
        info.referenceIdentifier = h.getReferenceIdentifier();
        info.referenceTimeStamp = getReferenceTimeStamp();

        long originate = getOriginateTimeStamp().getTime().getTime();
        long receive = getReceiveTimeStamp().getTime().getTime();
        long transmit = getTransmitTimeStamp().getTime().getTime();
        long reception = getReceptionTimeStamp().getTime().getTime();
        info.roundTripDelay = (receive - originate + reception) - transmit;
        info.offset = (receive - originate - reception + transmit) / 2;

        return info;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s;
        s = "Header : ";
        s = s + getHeader();
        s = s + "\n";
        s = s + "ReferenceTimeStamp : ";
        s = s + getReferenceTimeStamp();
        s = s + "\n";
        s = s + "OriginateTimeStamp : ";
        s = s + getOriginateTimeStamp();
        s = s + "\n";
        s = s + "ReceiveTimeStamp : ";
        s = s + getReceiveTimeStamp();
        s = s + "\n";
        s = s + "TransmitTimeStamp : ";
        s = s + getTransmitTimeStamp();

        return s;
    }
}
