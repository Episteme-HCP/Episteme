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

package org.jscience.social.geography;

import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Length;
import org.jscience.core.measure.quantity.Mass;
import org.jscience.core.measure.quantity.Acceleration;


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
     * Reference: 5.972 Ã— 10^24 kg.
     */
    public static final Quantity<Mass> EARTH_MASS = Quantities.create(5.972235E24, Units.KILOGRAM);

    /** 
     * Nominal mean radius of the Earth as defined by the IUGG in meters (m).
     */
    public static final Quantity<Length> EARTH_MEAN_RADIUS = Quantities.create(6371000.79, Units.METER);

    /** 
     * Equatorial radius (semi-major axis) of the Earth (WGS84) in meters (m).
     */
    public static final Quantity<Length> EARTH_EQUATORIAL_RADIUS = Quantities.create(6378137.0, Units.METER);

    /** 
     * Polar radius (semi-minor axis) of the Earth (WGS84) in meters (m).
     */
    public static final Quantity<Length> EARTH_POLAR_RADIUS = Quantities.create(6356752.3142, Units.METER);

    /** 
     * Average acceleration due to gravity on the Earth's surface (standard gravity) 
     * in meters per second squared (m/sÂ²).
     */
    public static final Quantity<Acceleration> STANDARD_GRAVITY = Quantities.create(9.80665, Units.METERS_PER_SECOND_SQUARED);
}


