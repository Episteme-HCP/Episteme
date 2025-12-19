package org.jscience.physics.astrophysics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Angle;
import org.jscience.measure.quantity.Mass;

public class OrbitSolverTest {

    @Test
    public void testEarthOrbit() {
        // Approximate Earth parameters
        double AU = 149597870700.0;

        KeplerParams earth = new KeplerParams(
                Quantities.create(AU, Units.METER),
                0.0167, // e
                Quantities.create(0.0, Units.DEGREE_ANGLE), // i
                Quantities.create(0.0, Units.DEGREE_ANGLE), // Omega
                Quantities.create(102.0, Units.DEGREE_ANGLE) // omega (perihelion)
        );

        Quantity<Mass> sunMass = Quantities.create(1.989e30, Units.KILOGRAM);
        Quantity<Angle> trueAnomaly = Quantities.create(0.0, Units.DEGREE_ANGLE); // At perihelion

        OrbitalState state = OrbitSolver.solve(earth, trueAnomaly, sunMass);

        System.out.println("Earth State at Perihelion: " + state);

        double vx = state.getVx().to(Units.METERS_PER_SECOND).getValue().doubleValue();
        double vy = state.getVy().to(Units.METERS_PER_SECOND).getValue().doubleValue();
        double vz = state.getVz().to(Units.METERS_PER_SECOND).getValue().doubleValue();
        double vMag = Math.sqrt(vx * vx + vy * vy + vz * vz);

        System.out.println("Velocity Magnitude: " + vMag + " m/s");

        // Perihelion velocity: v = sqrt(GM/a * (1+e)/(1-e))
        // GM = 6.6743e-11 * 1.989e30 = 1.3275e20
        // v = sqrt(1.3275e20 / 1.5e11 * 1.0167 / 0.9833) = sqrt(8.85e8 * 1.03) =
        // sqrt(9.11e8) = 30190 m/s
        // Usually quoted as ~30.29 km/s at perihelion, 29.29 at aphelion.
        // Mean is 29.78.

        assertTrue(vMag > 29000 && vMag < 31000, "Earth velocity should be around 30 km/s");
    }

    @Test
    public void testInclination() {
        double dist = 1000.0;
        KeplerParams polar = new KeplerParams(
                Quantities.create(dist, Units.METER),
                0.0, // circular
                Quantities.create(90.0, Units.DEGREE_ANGLE), // Polar orbit
                Quantities.create(0.0, Units.DEGREE_ANGLE),
                Quantities.create(0.0, Units.DEGREE_ANGLE));

        Quantity<Mass> center = Quantities.create(1.0e10, Units.KILOGRAM); // Small mass
        Quantity<Angle> nu = Quantities.create(0.0, Units.DEGREE_ANGLE);

        OrbitalState state = OrbitSolver.solve(polar, nu, center);

        // At nu=0 (periapsis/node), if omega=0, we are at ascending node?
        // r = r * cos(nu) * P + ...
        // With i=90, Omega=0, omega=0:
        // Px = 1*1 - 0 = 1
        // Py = 0
        // Pz = 0
        // Qx = ...
        // Qz = 1
        // So P is x-axis, Q is z-axis (rotated 90 deg around x).
        // Velocity should be along Q (z-axis) if circular?

        System.out.println("Polar State: " + state);
        assertEquals(dist, state.getX().to(Units.METER).getValue().doubleValue(), 1e-6);
        assertEquals(0.0, state.getY().to(Units.METER).getValue().doubleValue(), 1e-6);
        assertEquals(0.0, state.getZ().to(Units.METER).getValue().doubleValue(), 1e-6);

        // Velocity should be purely in Z direction (Py=0, Qy=0?? Wait Qy = -sin(0) +
        // cos(0)*cos(90) = 0.
        // Qz = cos(0)*sin(90) = 1.
        // So Vy should be 0, Vz should be non-zero.

        assertTrue(Math.abs(state.getVz().to(Units.METERS_PER_SECOND).getValue().doubleValue()) > 0);
        assertEquals(0.0, state.getVy().to(Units.METERS_PER_SECOND).getValue().doubleValue(), 1e-9);
    }
}
