/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.devices;

import java.io.IOException;

/**
 * Represents a physical or virtual device.
 * <p>
 * This interface provides the basic contract for device interaction, including
 * connection management and identification.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Device extends AutoCloseable {

    /**
     * Connects to the device.
     * 
     * @throws IOException if the connection fails
     */
    void connect() throws IOException;

    /**
     * Disconnects from the device.
     * 
     * @throws IOException if the disconnection fails
     */
    void disconnect() throws IOException;

    /**
     * Checks if the device is currently connected.
     * 
     * @return true if connected, false otherwise
     */
    boolean isConnected();

    /**
     * Returns the device name or identifier.
     * 
     * @return the device name
     */
    String getName();

    /**
     * Returns the physical instrument metadata associated with this device.
     * 
     * @return the instrument metadata, or null if not applicable/simulated.
     */
    default org.jscience.measure.Instrument getInstrumentMetadata() {
        return null;
    }
}