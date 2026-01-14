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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * This class take care of adding DLE-stuffing to all packets sent to the
 * GPS. <b> NOTE: </b> Only the method write(GarminPacket) performs addition
 * of DLE-stuffing. The remaining methods write directly to the GPS without
 * format-control.
 */
public class GarminOutputStream extends FilterOutputStream {
/**
     * Creates a new GarminOutputStream object.
     *
     * @param o DOCUMENT ME!
     */
    public GarminOutputStream(OutputStream o) {
        super(o);
    }

    /**
     * DOCUMENT ME!
     *
     * @param packet DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws InvalidPacketException DOCUMENT ME!
     */
    public synchronized void write(GarminPacket packet)
        throws IOException, InvalidPacketException {
        if (packet.isLegal() != -1) {
            throw (new InvalidPacketException(packet.getPacket(),
                packet.isLegal()));
        }

        write(packet.getByte(0));
        write(packet.getByte(1));

        int c;

        // Iterate through size-field, data-field and checksum-field. Add stuffing where necessary.
        for (int i = 0; i < (packet.getByte(2) + 2); i++) {
            c = packet.getByte(i + 2);
            write(c);

            if (c == GarminPacket.DLE) {
                write(c);
            }
        }

        write(GarminPacket.DLE);
        write(GarminPacket.ETX);
        flush();
    }
}
