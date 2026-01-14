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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * This class provides the functionality of automatically removing the double
 * DLEs from the GPS-inputstream. The double-DLEs can be found in the
 * size-,data-, and checksum-fields. The only method providing the
 * filtering-functionality is read().
 */
public class GarminInputStream extends FilterInputStream {
    /*
    * Last value read.
    */

    /**
     * DOCUMENT ME!
     */
    private int prev;

    /**
     * Takes the stream to the GPS-unit as an argument.
     *
     * @param i DOCUMENT ME!
     */
    public GarminInputStream(InputStream i) {
        super(i);
        in = i;
        prev = 0;
    }

    /**
     * Reads a packet from the stream. <br/
     * ><b> Note: </b> Method assumes that it's called between packets, ie.
     * when the first byte of a packet is the next in the stream. If this
     * condition is met, the method will leave the stream in the same state.
     *
     * @return DOCUMENT ME!
     * @throws InvalidPacketException DOCUMENT ME!
     * @throws IOException            DOCUMENT ME!
     */
    public int[] readPacket() throws InvalidPacketException, IOException {
        int c;
        int[] packet;
        int id;
        int size;
        c = read();

        if (c != GarminPacket.DLE) {
            throw (new InvalidPacketException(new int[]{GarminPacket.DLE}, 0));
        }

        id = read();
        size = read();
        packet = new int[size + 6];
        packet[0] = GarminPacket.DLE;
        packet[1] = id;
        packet[2] = size;

        for (int i = 0; i < (size + 3); i++)
            packet[3 + i] = read();

        if (packet[packet.length - 2] != GarminPacket.DLE) {
            throw (new InvalidPacketException(packet, packet.length - 2));
        }

        if (packet[packet.length - 1] != GarminPacket.ETX) {
            throw (new InvalidPacketException(packet, packet.length - 1));
        }

        return packet;
    }

    /**
     * Returns the next byte from the stream. Makes sure to remove DLE
     * stuffing.
     *
     * @return DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public int read() throws IOException {
        int c = in.read();

        if ((prev == 16) && (c == 16)) {
            return prev = in.read();
        } else {
            return prev = c;
        }
    }
}
