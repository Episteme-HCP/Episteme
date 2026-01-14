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
 * This method is thrown from the constructors of the packet-classes,
 * whenever the int[]-array is not formatted according to the
 * Garmin-packet-specs.
 */
public class InvalidPacketException extends RuntimeException {
    /** DOCUMENT ME! */
    private int[] packet;

    /** DOCUMENT ME! */
    private int index;

/**
     * Creates an InvalidPacketException. pack is a reference to the byte-array
     * that caused the exception. i is the index of the byte that was in
     * error.
     *
     * @param pack DOCUMENT ME!
     * @param i    DOCUMENT ME!
     */
    public InvalidPacketException(int[] pack, int i) {
        packet = pack;
        index = i;
    }

    /**
     * Returns the packet that caused the exception to be thrown.
     * <b>Note:</b> The return-value is a reference to the original array.
     * Changes will likely propagate to other parts of the program.
     *
     * @return DOCUMENT ME!
     */
    public int[] getPacket() {
        return packet;
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    /**
     * Returns the index of the first erroneously configured byte.
     *
     * @return DOCUMENT ME!
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns a formatted string showing which byte is wrong.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer res = new StringBuffer();
        res.append("\nByte\tvalue\n");

        for (int i = 0; i < packet.length; i++) {
            res.append(" " + i + "\t" + packet[i]);

            if (i == index) {
                res.append(" <- Erroneous");
            }

            res.append('\n');
        }

        return res.toString();
    }
}
