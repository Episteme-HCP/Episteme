package org.jscience.earth.geophysics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Angle;
import org.jscience.measure.quantity.Time;
import org.jscience.measure.quantity.Velocity;

public class PlateMotionTest {

    @Test
    public void testHawaiiMotion() {
        // HILO, Hawaii approximate coords
        Quantity<Angle> lat = Quantities.create(19.72, Units.DEGREE_ANGLE);
        Quantity<Angle> lon = Quantities.create(-155.08, Units.DEGREE_ANGLE);

        // Approx velocity: 70 mm/yr Northwest
        // North: ~30 mm/yr, West: ~60 mm/yr
        // Let's use simple numbers: North 50 mm/yr, East -50 mm/yr (North-West)

        // mm/yr needs conversion to Velocity
        // 1 mm/yr = 0.001 m / (365.25 * 86400 s) = 3.17e-11 m/s

        double mmPerYrToMps = 0.001 / (365.25 * 24 * 3600);

        Quantity<Velocity> vN = Quantities.create(50.0 * mmPerYrToMps, Units.METERS_PER_SECOND);
        Quantity<Velocity> vE = Quantities.create(-50.0 * mmPerYrToMps, Units.METERS_PER_SECOND);

        PlateVector hilo = new PlateVector("HILO", lat, lon, vN, vE);

        // Predict position in 1 million years
        Quantity<Time> duration = Quantities.create(1_000_000, Units.YEAR);

        PlateVector futureHilo = PlateMotionCalculator.predictPosition(hilo, duration);

        System.out.println("Start: " + hilo);
        System.out.println("Future (1Myr): " + futureHilo);

        // Expected distance: sqrt(50^2 + 50^2) = 70.7 mm/yr
        // Total = 70.7 km

        double dist = VincentyUtils.distance(
                hilo.getLatitude(), hilo.getLongitude(),
                futureHilo.getLatitude(), futureHilo.getLongitude());

        System.out.println("Displacement: " + dist + " meters");

        assertEquals(70710, dist, 100.0); // Allow 100m error for projection/ellipsoid curvature vs flat rate
    }
}
