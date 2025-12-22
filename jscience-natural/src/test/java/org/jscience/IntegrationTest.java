package org.jscience;

import org.junit.jupiter.api.Test;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.astronomy.StarSystem;
import org.jscience.biology.ecology.Population;
import org.jscience.biology.taxonomy.Species;
import org.jscience.biology.Individual;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Tests for JScience.
 * Verifies cross-module functionality.
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
        Population pop = new Population("City", human, "Earth");
        Individual adam = new Individual("1", human, Individual.Sex.MALE);
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
