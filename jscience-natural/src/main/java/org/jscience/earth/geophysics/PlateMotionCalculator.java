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
package org.jscience.earth.geophysics;

import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

/**
 * Calculates tectonic plate motions. * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
 */
public class PlateMotionCalculator {

        private PlateMotionCalculator() {
        }

        /**
         * Projects the position of a site into the future based on its velocity vector.
         * 
         * @param start    Initial state
         * @param duration Time duration
         * @return New PlateVector with updated position (velocity assumed constant)
         */
        @SuppressWarnings("unchecked")
        public static PlateVector predictPosition(PlateVector start,
                        org.jscience.measure.Quantity<org.jscience.measure.quantity.Time> duration) {

                // 1. Calculate displacement in meters
                // 1. Calculate displacement in meters (dtYears was unused)

                // Velocity in m/year?
                // Vn and Ve are Quantities (Velocity, likely m/s or mm/yr)
                // Convert to m/s then * seconds? Or use internal units.

                double vn_ms = start.getNorthVelocity().to(Units.METERS_PER_SECOND).getValue().doubleValue();
                double ve_ms = start.getEastVelocity().to(Units.METERS_PER_SECOND).getValue().doubleValue();

                double dtSeconds = duration.to(Units.SECOND).getValue().doubleValue();

                double northDistMeters = vn_ms * dtSeconds;
                double eastDistMeters = ve_ms * dtSeconds;

                double totalDist = Math.sqrt(northDistMeters * northDistMeters + eastDistMeters * eastDistMeters);

                if (totalDist < 1e-9) {
                        return start;
                }

                double bearing = Math.atan2(eastDistMeters, northDistMeters); // 0 is North, PI/2 is East

                // 2. Use Vincenty 'destination' to find new Lat/Lon
                // Cast to raw Unit to bypass generics hell if explicit FQN doesn't match
                // Unit<Angle> vs Unit<?>
                double latRad = start.getLatitude()
                                .to((org.jscience.measure.Unit<org.jscience.measure.quantity.Angle>) (org.jscience.measure.Unit<?>) Units.RADIAN)
                                .getValue()
                                .doubleValue();
                double lonRad = start.getLongitude()
                                .to((org.jscience.measure.Unit<org.jscience.measure.quantity.Angle>) (org.jscience.measure.Unit<?>) Units.RADIAN)
                                .getValue()
                                .doubleValue();

                double[] newPosRad = VincentyUtils.destination(latRad, lonRad, bearing, totalDist);

                org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> newLat = Quantities
                                .create(newPosRad[0],
                                                (org.jscience.measure.Unit<org.jscience.measure.quantity.Angle>) (org.jscience.measure.Unit<?>) Units.RADIAN)
                                .to(Units.DEGREE_ANGLE);
                org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> newLon = Quantities
                                .create(newPosRad[1],
                                                (org.jscience.measure.Unit<org.jscience.measure.quantity.Angle>) (org.jscience.measure.Unit<?>) Units.RADIAN)
                                .to(Units.DEGREE_ANGLE);

                return new PlateVector(start.getSiteId(), newLat, newLon, start.getNorthVelocity(),
                                start.getEastVelocity());
        }
}
