/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural;

import org.junit.jupiter.api.Test;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.physics.astronomy.StarSystem;
import org.episteme.natural.biology.ecology.Population;
import org.episteme.natural.biology.taxonomy.Species;
import org.episteme.natural.biology.BiologicalSex;
import org.episteme.natural.biology.Individual;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Tests for Episteme.
 * Verifies cross-module functionality.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class IntegrationTest {

    @Test
    public void testStarSystemPhysics() {
        StarSystem sys = new StarSystem("Test System");
        assertNotNull(sys);
        assertEquals("Test System", sys.getName());
    }

    @Test
    public void testPopulationDynamics() {
        Species human = new Species("Homo sapiens", "Human");
        Population<Individual> pop = new Population<>("City", human, null);
        Individual adam = new Individual(new org.episteme.core.util.identity.SimpleIdentification("1"), human, BiologicalSex.MALE);
        pop.addMember(adam);

        assertEquals(1, pop.size());
        assertEquals(1, pop.countAlive());
    }

    @Test
    public void testRealMath() {
        Real a = Real.of(10.0);
        Real b = Real.of(20.0);
        Real c = a.add(b);
        assertEquals(30.0, c.doubleValue(), 0.001);
    }
}





