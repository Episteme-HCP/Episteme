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

package org.jscience.devices.gps.garmin;

import org.jscience.devices.gps.IPosition;
import org.jscience.devices.gps.ITime;
import org.jscience.devices.gps.PositionRadians;


/**
 * This class encapsulates the PVT (Position, velocity and time) packet.
 * After receiving a Cmnd_Start_Pvt-packet, the GPS will continually transmit
 * packets of the PVT-type.
 */
public class PVTDataPacket extends GarminPacket implements IPosition, ITime {
    /** DOCUMENT ME! */
    protected float alt; // Altitude

    /** DOCUMENT ME! */
    protected float epe; // Estimated position error

    /** DOCUMENT ME! */
    protected float eph; // Horizontal epe

    /** DOCUMENT ME! */
    protected float epv; // Vertical epe

    /** DOCUMENT ME! */
    protected int fix; // Position fix

    /** DOCUMENT ME! */
    protected double tow; // Time of week (seconds).

    /** DOCUMENT ME! */
    protected PositionRadians lat; // Latitude

    /** DOCUMENT ME! */
    protected PositionRadians lon; // Longitude

    /** DOCUMENT ME! */
    protected float veast; // Velocity east.

    /** DOCUMENT ME! */
    protected float vnorth; // Velocity north.

    /** DOCUMENT ME! */
    protected float vup; // Velocity up.

    /** DOCUMENT ME! */
    protected float msl_hght; //

    /** DOCUMENT ME! */
    protected int leap_scnds; // Time difference between GPS and GMT (UTC)

    /** DOCUMENT ME! */
    protected long wn_days; // Week number days.

/**
     * Treats the packet p as a packet containing PVT-data. Throws
     * PacketNotRecognizedException if p is not a PVT-packet. Throws
     * InvalidPacketException if the packet contains too little data.
     *
     * @param p DOCUMENT ME!
     */
    public PVTDataPacket(int[] p) {
        super(p);

        if (getID() != Pid_Pvt_Data) {
            throw (new PacketNotRecognizedException(Pid_Pvt_Data, getID()));
        }

        if (getDataLength() != 64) {
            throw (new InvalidPacketException(packet, 2));
        }

        alt = readFloat(3);
        epe = readFloat(7);
        eph = readFloat(11);
        epv = readFloat(15);
        fix = readWord(19);
        tow = readDouble(21);
        lat = new PositionRadians(readDouble(29));
        lon = new PositionRadians(readDouble(37));
        veast = readFloat(45);
        vnorth = readFloat(49);
        vup = readFloat(53);
        msl_hght = readFloat(57);
        leap_scnds = readWord(61);
        wn_days = readLong(63);
    }

/**
     * This method is a copy-constructor allowing to "upgrade" a GarminPacket
     * to a PVTDataPacket. Throws PacketNotRecognizedException if p is not a
     * PVT-data-packet.
     *
     * @param p DOCUMENT ME!
     */
    public PVTDataPacket(GarminPacket p) {
        this(p.packet);
    }

    /**
     * Returns the hour of the day.
     *
     * @return DOCUMENT ME!
     */
    public int getHours() {
        int hour = (int) tow;
        hour = hour % (24 * 60 * 60); // Remove all preceding days.
        hour = hour / 3600;

        return hour;
    }

    /**
     * Returns the minute of the hour.
     *
     * @return DOCUMENT ME!
     */
    public short getMinutes() {
        int minute = (int) tow;
        minute = minute % (24 * 60 * 60); // Remove all preceding days.
        minute = minute / 60;
        minute = minute % 60;

        return (short) minute;
    }

    /**
     * Returns the second of the minute.
     *
     * @return DOCUMENT ME!
     */
    public short getSeconds() {
        return (short) (tow % 60);
    }

    /**
     * This method returns the latitude of the position.
     *
     * @return DOCUMENT ME!
     */
    public PositionRadians getLatitude() {
        return lat;
    }

    /**
     * This method returns the longitude of the position.
     *
     * @return DOCUMENT ME!
     */
    public PositionRadians getLongitude() {
        return lon;
    }
}
