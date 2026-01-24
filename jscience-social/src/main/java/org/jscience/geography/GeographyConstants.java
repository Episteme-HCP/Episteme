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

package org.jscience.geography;

/**
 * Standard physical and geometric constants for Earth-based geographical 
 * calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class GeographyConstants {

    private GeographyConstants() {}

    /** 
     * Approximate mass of the Earth in kilograms (kg).
     * Reference: 5.972 × 10^24 kg.
     */
    public static final double EARTH_MASS = 5.972235E24;

    /** 
     * Nominal mean radius of the Earth as defined by the IUGG in meters (m).
     */
    public static final double EARTH_MEAN_RADIUS = 6371000.79;

    /** 
     * Equatorial radius (semi-major axis) of the Earth (WGS84) in meters (m).
     */
    public static final double EARTH_EQUATORIAL_RADIUS = 6378137.0;

    /** 
     * Polar radius (semi-minor axis) of the Earth (WGS84) in meters (m).
     */
    public static final double EARTH_POLAR_RADIUS = 6356752.3142;

    /** 
     * Average acceleration due to gravity on the Earth's surface (standard gravity) 
     * in meters per second squared (m/s²).
     */
    public static final double STANDARD_GRAVITY = 9.80665;
}
