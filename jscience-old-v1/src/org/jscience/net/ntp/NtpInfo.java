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


/**
 * This class represents a datastructure describing  the useful information
 * that can be extracted from a NtpDatagram returning from the server. We
 * refer to rfc2030 for more details.
 */
public class NtpInfo {
    /** No leap second warning. */
    public static final byte LI_NO_WARNING = 0;

    /** Last minute has 61 seconds. */
    public static final byte LI_POSITIVE_LEAP_SECOND = 1;

    /** Last minute has 59 seconds. */
    public static final byte LI_NEGATIVE_LEAP_SECOND = 2;

    /** Alarm condition (clock not synchrinized) */
    public static final byte LI_ALARM_CONDITION = 3;

    /** Reserved mode. */
    public static final byte MODE_RESERVED = 0;

    /** Symmetric active mode. */
    public static final byte MODE_SYMMETRIC_ACTIVE = 1;

    /** Symmetric passive mode. */
    public static final byte MODE_RESERVED_PASSIVE = 2;

    /** Client mode. */
    public static final byte MODE_CLIENT = 3;

    /** Server mode. */
    public static final byte MODE_SERVER = 4;

    /** Broadcast mode. */
    public static final byte MODE_BROADCAST = 5;

    /** Reserved for NTP control message. */
    public static final byte MODE_RESERVED_FOR_NTP_CONTROL = 6;

    /** Reserved for private use. */
    public static final byte MODE_RESERVED_FOR_PRIVATE_USE = 7;

    /** Unspecified or unavailable stratum. */
    public static final byte STRATUM_UNSPECIFIED = 0;

    /** Primary reference. */
    public static final byte STRATUM_PRIMARY_REFERENCE = 1;

    /** InetAddress of the server. */
    public InetAddress serverAddress;

    /** Leap year indicator. */
    public int leapYearIndicator;

    /**
     * Version number of the packet. In this application we always send
     * version 3 packet to the server. The servers always seem to reply with
     * version 3 packets (and not version 4).
     */
    public int versionNumber;

    /**
     * Mode of the communication with the server. In our application
     * this is MODE_CLIENT for the client and MODE_SERVER for the server.
     */
    public int mode;

    /**
     * The stratum. This number indicates the distance (in hops) from
     * the server to the primary server (which is stratum 1).
     */
    public int stratum;

    /** Poll Interval in seconds. See rfc2030 */
    public int pollInterval;

    /** Precision of the server clock (in milliseconds). */
    public double precision;

    /**
     * Total roundtrip delay from the server to the primary server (in
     * milliseconds).
     */
    public double rootDelay;

    /**
     * Nominal error error relative to the primary reference source (in
     * milliseconds).
     */
    public double rootDispersion;

    /** Reference Identifier. */
    public Object referenceIdentifier;

    /** Reference timestamp. Indicates when the server clock was last set. */
    public TimeStamp referenceTimeStamp;

    /** Roundtrip delay (in milliseconds). Calculated according to rfc2030. */
    public long roundTripDelay;

    /**
     * Offset of the local clock versus the server clock, taking into
     * account the roundtrip delay (in milliseconds). Calculated according to
     * rfc2030.
     */
    public long offset;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "Server address : " + serverAddress + "\n" +
            "Leap year indicator : " + leapYearIndicator + "\n" +
            "Version number : " + versionNumber + "\n" + "Mode : " + mode +
            "\n" + "Stratum : " + stratum + "\n" + "Poll interval : " +
            pollInterval + " s\n" + "Precision : " + precision + " ms\n" +
            "Root delay : " + rootDelay + " ms\n" + "Root dispersion : " +
            rootDispersion + " ms\n";

        if (referenceIdentifier instanceof InetAddress) {
            s = s + "Reference address : " + (InetAddress) referenceIdentifier +
                "\n";
        } else if (referenceIdentifier instanceof String) {
            s = s + "Reference code : " + (String) referenceIdentifier + "\n";
        } else {
            byte[] temp = (byte[]) referenceIdentifier;
            s = s + "Reference data : " + temp[0] + " " + temp[1] + " " +
                temp[2] + " " + temp[3] + "\n";
        }

        s = s + "Reference timestamp : " + referenceTimeStamp + "\n" +
            "Round trip delay : " + roundTripDelay + " ms\n" + "Offset : " +
            offset + " ms";

        return s;
    }
}
