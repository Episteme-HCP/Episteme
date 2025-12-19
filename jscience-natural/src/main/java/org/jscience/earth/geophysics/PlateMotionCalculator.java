package org.jscience.earth.geophysics;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Angle;
import org.jscience.measure.quantity.Time;

/**
 * Calculates tectonic plate motions.
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
        public static PlateVector predictPosition(PlateVector start, Quantity<Time> duration) {

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
                double latRad = start.getLatitude()
                                .to((org.jscience.measure.Unit<Angle>) (org.jscience.measure.Unit<?>) Units.RADIAN)
                                .getValue()
                                .doubleValue();
                double lonRad = start.getLongitude()
                                .to((org.jscience.measure.Unit<Angle>) (org.jscience.measure.Unit<?>) Units.RADIAN)
                                .getValue()
                                .doubleValue();

                double[] newPosRad = VincentyUtils.destination(latRad, lonRad, bearing, totalDist);

                Quantity<Angle> newLat = Quantities
                                .create(newPosRad[0],
                                                (org.jscience.measure.Unit<Angle>) (org.jscience.measure.Unit<?>) Units.RADIAN)
                                .to(Units.DEGREE_ANGLE);
                Quantity<Angle> newLon = Quantities
                                .create(newPosRad[1],
                                                (org.jscience.measure.Unit<Angle>) (org.jscience.measure.Unit<?>) Units.RADIAN)
                                .to(Units.DEGREE_ANGLE);

                return new PlateVector(start.getSiteId(), newLat, newLon, start.getNorthVelocity(),
                                start.getEastVelocity());
        }
}
