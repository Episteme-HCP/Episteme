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

package org.jscience.physics.classical.mechanics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.mathematics.numbers.real.Real;

public class ParticleTest {

        @Test
        public void testParticleCreation() {

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


