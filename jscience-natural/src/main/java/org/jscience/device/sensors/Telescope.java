/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.device.sensors;

import org.jscience.device.Actuator;

import java.io.IOException;

/**
 * Represents a telescope control interface.
 * <p>
 * Allows slewing to coordinates and sync operations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Telescope extends Actuator<String> {

    /**
     * Slew (move) the telescope to specified Equatorial coordinates.
     * 
     * @param ra  Right Ascension in hours (0-24)
     * @param dec Declination in degrees (-90 to +90)
     * @throws IOException if command fails
     */
    void slewTo(double ra, double dec) throws IOException;

    /**
     * Syncs the telescope's internal position to the specified coordinates.
     * 
     * @param ra  Right Ascension in hours
     * @param dec Declination in degrees
     * @throws IOException if command fails
     */
    void syncTo(double ra, double dec) throws IOException;

    /**
     * Returns the current Right Ascension.
     * 
     * @return RA in hours
     * @throws IOException if read fails
     */
    double getRightAscension() throws IOException;

    /**
     * Returns the current Declination.
     * 
     * @return Dec in degrees
     * @throws IOException if read fails
     */
    double getDeclination() throws IOException;

    /**
     * Aborts any current slew operation.
     * 
     * @throws IOException if command fails
     */
    void abort() throws IOException;
}


