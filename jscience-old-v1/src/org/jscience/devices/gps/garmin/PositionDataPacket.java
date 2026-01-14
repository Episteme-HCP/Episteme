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
import org.jscience.devices.gps.PositionRadians;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class PositionDataPacket extends GarminPacket implements IPosition {
    /** DOCUMENT ME! */
    private PositionRadians lat;

    /** DOCUMENT ME! */
    private PositionRadians lon;

/**
     * Treats the packet p as a packet containing position-data. Throws
     * PacketNotRecognizedException if p is not a position-data-packet. Throws
     * InvalidPacketException if the packet contains too little data.
     *
     * @param p DOCUMENT ME!
     */
    public PositionDataPacket(int[] p) {
        super(p);

        if (getID() != Pid_Position_Data) {
            throw (new PacketNotRecognizedException(Pid_Position_Data, getID()));
        }

        if (getDataLength() != 16) {
            throw (new InvalidPacketException(packet, 2));
        }

        lat = new PositionRadians(readDouble(3));
        lon = new PositionRadians(readDouble(11));
    }

/**
     * This method is a copy-constructor allowing to "upgrade" a GarminPacket
     * to a PositionPacket. Throws PacketNotRecognizedException if p is not a
     * position-data-packet.
     *
     * @param p DOCUMENT ME!
     */
    public PositionDataPacket(GarminPacket p) {
        this(p.packet);
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

    /**
     * Returns a String containing the position in a human-readable
     * format.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer res = new StringBuffer();
        res.append("\nPosition:");
        res.append("\nLatitude: " + lat.toString());
        res.append("\nLongitude: " + lon.toString());

        return res.toString();
    }
}
