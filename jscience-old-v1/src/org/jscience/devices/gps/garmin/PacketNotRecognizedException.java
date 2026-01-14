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
 * This exception is thrown whenever a method expects one type of packet,
 * but receives another. An example is if a PositionDataPacket is initialized
 * with the byte-array containing time-data. This exception is a runtime
 * exception because it's assumed that there will in most cases be
 * type-checking of packets.
 */
public class PacketNotRecognizedException extends RuntimeException {
/**
     * expected is the type of packet that the method was expecting. actual is
     * the type of the packet that it received.
     *
     * @param expected DOCUMENT ME!
     * @param actual   DOCUMENT ME!
     */
    public PacketNotRecognizedException(int expected, int actual) {
        super("\nPacket initialization error:\n" + "Expected: " +
            GarminPacket.idToString(expected) + '\n' + "received: " +
            GarminPacket.idToString(actual) + '\n');
    }
}
