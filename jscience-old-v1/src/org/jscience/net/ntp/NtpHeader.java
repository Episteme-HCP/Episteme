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

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * This class encapsulates the header of a NtpDatagram. See rfc2030 for
 * more details.
 */
public class NtpHeader {
    /** The default header data for a client datagram. Version=3, Mode=client. */
    public static final byte[] defaultHeaderData = {
            (byte) 0x1B, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };

    /**
     * The default header for a client datagram. This is a wrapper
     * around 'defaultHeaderData'
     */
    public static final NtpHeader defaultHeader = new NtpHeader(defaultHeaderData);

    /** Reference identifier is InetAddress. */
    private static final byte RI_IP_ADDRESS = 0;

    /** Reference identifier is String. */
    private static final byte RI_CODE = 1;

    /** Reference identifier is 4 byte array. */
    private static final byte RI_OTHER = 2;

    /** DOCUMENT ME! */
    private byte[] data;

/**
     * Construct a NtpHeader from a 16 byte array.
     *
     * @param data DOCUMENT ME!
     */
    public NtpHeader(byte[] data) {
        this.data = data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int mp(byte b) {
        int bb = b;

        return (bb < 0) ? (256 + bb) : bb;
    }

    /**
     * Gets the 16 byte array constituting the header.
     *
     * @return DOCUMENT ME!
     */
    public byte[] getData() {
        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLeapYearIndicator() {
        return (int) ((data[0] & 0xc0) >>> 6);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVersionNumber() {
        return (int) ((data[0] & 0x38) >>> 3);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMode() {
        return (int) (data[0] & 0x07);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getStratum() {
        return (int) data[1];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPollInterval() {
        return (int) Math.round(Math.pow(2, data[2]));
    }

    /**
     * Get precision in milliseconds.
     *
     * @return DOCUMENT ME!
     */
    public double getPrecision() {
        return 1000 * Math.pow(2, data[3]);
    }

    /**
     * Get root delay in milliseconds.
     *
     * @return DOCUMENT ME!
     */
    public double getRootDelay() {
        int temp = 0;
        temp = (256 * ((256 * ((256 * data[4]) + data[5])) + data[6])) +
            data[7];

        return 1000 * (((double) temp) / 0x10000);
    }

    /**
     * Get root dispersion in milliseconds.
     *
     * @return DOCUMENT ME!
     */
    public double getRootDispersion() {
        long temp = 0;
        temp = (256 * ((256 * ((256 * data[8]) + data[9])) + data[10])) +
            data[11];

        return 1000 * (((double) temp) / 0x10000);
    }

    /**
     * Gets the type of the reference identifier.
     *
     * @return DOCUMENT ME!
     */
    private int getReferenceIdentifierType() {
        if (getMode() == NtpInfo.MODE_CLIENT) {
            return RI_OTHER;
        } else if (getStratum() < 2) {
            return RI_CODE;
        } else if (getVersionNumber() <= 3) {
            return RI_IP_ADDRESS;
        } else {
            return RI_OTHER;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     * @throws UnknownHostException DOCUMENT ME!
     */
    private InetAddress getReferenceAddress()
        throws IllegalArgumentException, UnknownHostException {
        if (getReferenceIdentifierType() != RI_IP_ADDRESS) {
            throw new IllegalArgumentException();
        }

        String temp = "" + mp(data[12]) + "." + mp(data[13]) + "." +
            mp(data[14]) + "." + mp(data[15]);

        return InetAddress.getByName(temp);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    private String getReferenceCode() throws IllegalArgumentException {
        if (getReferenceIdentifierType() != RI_CODE) {
            throw new IllegalArgumentException();
        }

        int codeLength = 0;
        int index = 12;
        boolean zeroFound = false;

        while ((!zeroFound) && (index <= 15)) {
            if (data[index] == 0) {
                zeroFound = true;
            } else {
                index++;
                codeLength++;
            }
        }

        return new String(data, 12, codeLength);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private byte[] getReferenceData() {
        byte[] temp = new byte[4];
        temp[0] = data[12];
        temp[1] = data[13];
        temp[2] = data[14];
        temp[3] = data[15];

        return temp;
    }

    /**
     * Gets the  reference identifier as an object. It can be either a
     * String, a InetAddress or a 4 byte array. Use 'instanceof' to find out
     * what the true class is.
     *
     * @return DOCUMENT ME!
     */
    public Object getReferenceIdentifier() {
        if (getReferenceIdentifierType() == RI_IP_ADDRESS) {
            try {
                return getReferenceAddress();
            } catch (Exception e) {
                return getReferenceData();
            }
        } else if (getReferenceIdentifierType() == RI_CODE) {
            return getReferenceCode();
        } else {
            return getReferenceData();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "Leap year indicator : " + getLeapYearIndicator() + "\n" +
            "Version number : " + getVersionNumber() + "\n" + "Mode : " +
            getMode() + "\n" + "Stratum : " + getStratum() + "\n" +
            "Poll interval : " + getPollInterval() + " s\n" + "Precision : " +
            getPrecision() + " ms\n" + "Root delay : " + getRootDelay() +
            " ms\n" + "Root dispersion : " + getRootDispersion() + " ms\n";
        Object o = getReferenceIdentifier();

        if (o instanceof InetAddress) {
            s = s + "Reference address : " + (InetAddress) o;
        } else if (o instanceof String) {
            s = s + "Reference code : " + (String) o;
        } else {
            byte[] temp = (byte[]) o;
            s = s + "Reference data : " + temp[0] + " " + temp[1] + " " +
                temp[2] + " " + temp[3];
        }

        return s;
    }
}
