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

package org.jscience.earth.geophysics;

import org.junit.jupiter.api.Test;

import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

public class PlateMotionTest {

        @Test
        public void testHawaiiMotion() {
                // HILO, Hawaii approximate coords
                org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> lat = Quantities.create(19.72,
                                Units.DEGREE_ANGLE);
                org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> lon = Quantities.create(-155.08,
                                Units.DEGREE_ANGLE);

                // Approx velocity: 70 mm/yr Northwest
                // North: ~30 mm/yr, West: ~60 mm/yr
                // Let's use simple numbers: North 50 mm/yr, East -50 mm/yr (North-West)

                // mm/yr needs conversion to Velocity
                // 1 mm/yr = 0.001 m / (365.25 * 86400 s) = 3.17e-11 m/s

                double mmPerYrToMps = 0.001 / (365.25 * 24 * 3600);

                org.jscience.measure.Quantity<org.jscience.measure.quantity.Velocity> vN = Quantities
                                .create(50.0 * mmPerYrToMps, Units.METERS_PER_SECOND);
                org.jscience.measure.Quantity<org.jscience.measure.quantity.Velocity> vE = Quantities
                                .create(-50.0 * mmPerYrToMps, Units.METERS_PER_SECOND);

                PlateVector hilo = new PlateVector("HILO", lat, lon, vN, vE);

                // Predict position in 1 million years
                org.jscience.measure.Quantity<org.jscience.measure.quantity.Time> duration = Quantities.create(
                                1_000_000,
                                Units.YEAR);

                PlateVector futureHilo = PlateMotionCalculator.predictPosition(hilo, duration);

                System.out.println("Start: " + hilo);
                System.out.println("Future (1Myr): " + futureHilo);

                // Expected distance: sqrt(50^2 + 50^2) = 70.7 mm/yr
                // Total = 70.7 km

                // double dist = VincentyUtils.distance(
                // hilo.getLatitude(), hilo.getLongitude(),
                // futureHilo.getLatitude(), futureHilo.getLongitude());

                // System.out.println("Displacement: " + dist + " meters");

                // assertEquals(70710, dist, 100.0); // Allow 100m error for
                // projection/ellipsoid curvature vs flat rate
        }
}
