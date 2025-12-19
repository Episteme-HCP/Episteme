package org.jscience.physics.classical.mechanics;

import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Angle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KinematicsTest {

    @Test
    public void testPositionConstantAccel() {
        // x = x0 + v0*t + 0.5*a*t^2
        // x0 = 0, v0 = 0, a = 2 m/s^2, t = 3 s -> x = 0 + 0 + 0.5*2*9 = 9 m

        var x0 = Quantities.create(0, Units.METER);
        var v0 = Quantities.create(0, Units.METERS_PER_SECOND);
        var a = Quantities.create(2, Units.METERS_PER_SECOND_SQUARED);
        var t = Quantities.create(3, Units.SECOND);

        var pos = Kinematics.position(x0, v0, a, t);

        assertEquals(9.0, pos.to(Units.METER).getValue().doubleValue(), 1e-9);
    }

    @Test
    public void testVelocityConstantAccel() {
        // v = v0 + a*t
        // v0 = 10, a = -5, t = 2 -> v = 10 - 10 = 0

        var v0 = Quantities.create(10, Units.METERS_PER_SECOND);
        var a = Quantities.create(-5, Units.METERS_PER_SECOND_SQUARED);
        var t = Quantities.create(2, Units.SECOND);

        var v = Kinematics.velocity(v0, a, t);

        assertEquals(0.0, v.to(Units.METERS_PER_SECOND).getValue().doubleValue(), 1e-9);
    }

    @Test
    public void testProjectileRange() {
        // R = v^2 * sin(2theta) / g
        // v = 10, theta = 45 deg, g = 9.81
        // sin(90) = 1. R = 100 / 9.81 = 10.1936...

        var v = Quantities.create(10, Units.METERS_PER_SECOND);
        var theta = Quantities.create(45, Units.DEGREE_ANGLE); // Need to check if DEGREE unit exists or convert
        // If JScience Units doesn't have DEGREE, stick to RADIAN. 45 deg = PI/4

        // Let's assume Units.RADIAN for safety first, or check Units later.
        // Assuming Units.DEGREE_ANGLE or similar isn't standard in basic set, using
        // Radian.
        theta = Quantities.create(Math.PI / 4, Units.RADIAN).asType(Angle.class);

        var g = Quantities.create(9.81, Units.METERS_PER_SECOND_SQUARED);

        var range = Kinematics.projectileRange(v, theta, g);
        assertEquals(100.0 / 9.81, range.to(Units.METER).getValue().doubleValue(), 1e-4);
    }

    @Test
    public void testKineticEnergy() {
        // KE = 0.5 * m * v^2
        // m = 10 kg, v = 2 m/s -> KE = 0.5 * 10 * 4 = 20 J

        var m = Quantities.create(10, Units.KILOGRAM);
        var v = Quantities.create(2, Units.METERS_PER_SECOND);

        var ke = Kinematics.kineticEnergy(m, v);
        assertEquals(20.0, ke.to(Units.JOULE).getValue().doubleValue(), 1e-9);
    }
}
