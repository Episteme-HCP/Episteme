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

/**
 * This packet is transmitted between devices before a large transfer of
 * data-units, ie. a transfer of waypoints.
 */
public class RecordsPacket extends GarminPacket {
    /** The number of records to come, that this packet announces. */
    protected int number;

/**
     * Creates a new RecordsPacket object.
     *
     * @param p DOCUMENT ME!
     */
    public RecordsPacket(int[] p) {
        super(p);

        if (getID() != Pid_Records) {
            throw (new PacketNotRecognizedException(Pid_Records, getID()));
        }

        if (getDataLength() != 2) {
            throw (new InvalidPacketException(packet, 2));
        }

        number = readWord(3);
    }

/**
     * Creates a new RecordsPacket object.
     *
     * @param p DOCUMENT ME!
     */
    public RecordsPacket(GarminPacket p) {
        this(p.packet);
    }

    /**
     * Returns the number of records that this packet announces.
     *
     * @return DOCUMENT ME!
     */
    public int getNumber() {
        return number;
    }
}
