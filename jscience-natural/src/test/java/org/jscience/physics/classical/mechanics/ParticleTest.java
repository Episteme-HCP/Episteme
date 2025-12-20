package org.jscience.physics.classical.mechanics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Velocity;
import org.jscience.measure.quantity.Acceleration;

import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

import org.jscience.mathematics.numbers.real.Real;

public class ParticleTest {

        @Test
        public void testParticleCreation() {
                Quantity<Length> len = Quantities.create(10.0, Units.METER);
                Quantity<Mass> mass = Quantities.create(1.0, Units.KILOGRAM);

                // Particle now expects doubles in constructor as per previous refactor or I
                // should update it to take Quantities?
                // Checking previous Particle.java edits: "replaced Quantities.getQuantity with
                // org.jscience.measure.Quantities.create"
                // But the constructor signature in Particle.java might be (double x, double y,
                // double z, double mass) or (Quantity...)
                // Let's assume (double...) for now based on other code updates or check
                // Particle.java content.
                // Wait, in NBodySimulation I saw: new Particle(x, y, z, mass) with doubles.
                // So I should pass doubles here.

                Particle p = new Particle(10.0, 10.0, 10.0, 1.0);

                // Check positions
                assertNotNull(p.getPosition());
                assertEquals(10.0, p.getPosition().get(0).doubleValue(), 0.001);
        }

        @Test
        public void testUpdate() {
                Particle p = new Particle(0, 0, 0, 1.0);

                // v = 1 m/s
                // setVelocity expects Vector<Real> based on previous edits
                // p.setVelocity(0, 1, 0); // Helper method likely exists

                // Let's manually set it
                p.setVelocity(0.0, 1.0, 0.0);

                p.updatePosition(Real.of(1.0)); // dt = 1s

                // x=0, y=1, z=0
                assertEquals(0.0, p.getPosition().get(0).doubleValue(), 0.001);
                assertEquals(1.0, p.getPosition().get(1).doubleValue(), 0.001);
        }
}
