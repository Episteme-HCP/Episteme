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

package org.jscience.devices.gps;

/**
 * This interface is used to receive notification each time the GPS transmits
 * one of the common data, ie. position, time and date.
 * <p/>
 * <ul>
 * <li>
 * The GPS does not necessarily transmit these things periodially by itself!
 * Some GPS-units needs a request before transmitting anything. Use the method
 * GPS.setAutoTransmission(true) if you want the GPS to periodically send this
 * data.
 * </li>
 * <li>
 * Don't perform any long calculations or big operations in these methods.
 * They're called by a dispatching thread, and putting it to too much work
 * will slow performance on the communication with the GPS.
 * </li>
 * </ul>
 */
public interface IGPSlistener {
    /**
     * Invoked when the GPS transmits time-data.
     *
     * @param t DOCUMENT ME!
     */
    public void timeReceived(ITime t);

    /**
     * Invoked when the GPS transmits date-data.
     *
     * @param d DOCUMENT ME!
     */
    public void dateReceived(IDate d);

    /**
     * Invoked when the GPS transmits position-data.
     *
     * @param pos DOCUMENT ME!
     */
    public void positionReceived(IPosition pos);
}
