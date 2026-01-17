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

package org.jscience.physics.astronomy;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Time;
import org.jscience.measure.quantity.Angle;

/**
 * Astronomical constants and parameters.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public final class AstronomyConstants {
    
    private AstronomyConstants() {}
    
    // =========================================================================
    // Standard Gravitational Parameters (mu = GM)
    // Units: m^3 s^-2
    // =========================================================================
    
    public static final Quantity<?> SOLAR_GM = Quantities.create(Real.of(1.32712440018e20), 
            Units.CUBIC_METER.divide(Units.SECOND.pow(2)));
    public static final Real MU_SUN = (Real) SOLAR_GM.getValue();

    public static final Quantity<?> EARTH_GM = Quantities.create(Real.of(3.986004418e14), 
            Units.CUBIC_METER.divide(Units.SECOND.pow(2)));
    public static final Real MU_EARTH = (Real) EARTH_GM.getValue();
    
    // =========================================================================
    // Planetary Data
    // =========================================================================
    
    public static final Quantity<Mass> JUPITER_MASS_QTY = Quantities.create(Real.of(1.898e27), Units.KILOGRAM);
    public static final Real JUPITER_MASS = (Real) JUPITER_MASS_QTY.getValue();
    
    public static final Quantity<Length> JUPITER_RADIUS_QTY = Quantities.create(Real.of(6.9911e7), Units.METER);
    public static final Real JUPITER_RADIUS = (Real) JUPITER_RADIUS_QTY.getValue();
    
    // =========================================================================
    // Distances
    // =========================================================================
    
    public static final Quantity<Length> PARSEC_QTY = Quantities.create(Real.of(3.0857e16), Units.METER);
    public static final Real PARSEC = (Real) PARSEC_QTY.getValue();

    public static final Quantity<Length> AU_QTY = Quantities.create(Real.of(1.496e11), Units.METER);
    public static final Real AU = (Real) AU_QTY.getValue();
    
    // =========================================================================
    // Galactic Coordinates (J2000)
    // =========================================================================
    
    // Convention: Angles in Degrees
    public static final Quantity<Angle> NGP_RA_QTY = Quantities.create(Real.of(192.85948), Units.DEGREE_ANGLE);
    public static final Real NGP_RA = (Real) NGP_RA_QTY.getValue();

    public static final Quantity<Angle> NGP_DEC_QTY = Quantities.create(Real.of(27.12825), Units.DEGREE_ANGLE);
    public static final Real NGP_DEC = (Real) NGP_DEC_QTY.getValue();

    public static final Quantity<Angle> GALACTIC_LON_NCP_QTY = Quantities.create(Real.of(122.93192), Units.DEGREE_ANGLE);
    public static final Real GALACTIC_LON_NCP = (Real) GALACTIC_LON_NCP_QTY.getValue();

    // =========================================================================
    // Time
    // =========================================================================
    
    public static final Quantity<Time> DAY_DURATION = Quantities.create(Real.of(86400.0), Units.SECOND);
    public static final Real DAY_SECONDS = (Real) DAY_DURATION.getValue();

    // Julian epochs are dimensionless days or dates? Usually just distinct formatting. 
    // Keeping Real for Julian Dates as they are often treated as scalar timeline points.
    public static final Real DAYS_PER_CENTURY = Real.of(36525.0);
    public static final Real JULIAN_EPOCH_J2000 = Real.of(2451545.0);
    public static final Real JULIAN_EPOCH_B1950 = Real.of(2433282.4235);
}
