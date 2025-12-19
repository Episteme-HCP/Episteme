package org.jscience.physics.classical.mechanics;

import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParticleTest {

        @Test
        public void testQuantityConstructor() {
                Particle p = new Particle(
                                Quantities.create(1.0, Units.METER),
                                Quantities.create(2.0, Units.METER),
                                Quantities.create(3.0, Units.METER),
                                Quantities.create(10.0, Units.KILOGRAM));

                assertEquals(1.0, p.getPosition().get(0).doubleValue(), 1e-9);
                assertEquals(2.0, p.getPosition().get(1).doubleValue(), 1e-9);
                assertEquals(3.0, p.getPosition().get(2).doubleValue(), 1e-9);
                assertEquals(10.0, p.getMass().to(Units.KILOGRAM).getValue().doubleValue(), 1e-9);
        }

        @Test
        public void testQuantitySetters() {
                Particle p = new Particle(0, 0, 0, 5.0);

                p.setPosition(
                                Quantities.create(100, Units.METER),
                                Quantities.create(0, Units.METER),
                                Quantities.create(0, Units.METER));

                assertEquals(100.0, p.getX().doubleValue(), 1e-9);

                p.setVelocity(
                                Quantities.create(10, Units.METERS_PER_SECOND),
                                Quantities.create(0, Units.METERS_PER_SECOND),
                                Quantities.create(0, Units.METERS_PER_SECOND));

                assertEquals(10.0, p.getVelocity().get(0).doubleValue(), 1e-9);

                p.setAcceleration(
                                Quantities.create(0, Units.METERS_PER_SECOND_SQUARED),
                                Quantities.create(-9.81, Units.METERS_PER_SECOND_SQUARED),
                                Quantities.create(0, Units.METERS_PER_SECOND_SQUARED));

                assertEquals(-9.81, p.getAcceleration().get(1).doubleValue(), 1e-9);
        }
}
