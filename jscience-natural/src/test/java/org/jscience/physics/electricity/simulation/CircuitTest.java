/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.physics.electricity.simulation;

import org.jscience.physics.classical.waves.electromagnetism.components.Ground;
import org.jscience.physics.classical.waves.electromagnetism.components.Resistor;
import org.jscience.physics.classical.waves.electromagnetism.components.VoltageSource;
import org.jscience.physics.classical.waves.electromagnetism.circuits.Circuit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Circuit Simulator.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CircuitTest {

    private static final double TOLERANCE = 1e-6;

    /**
     * Tests Ohm's Law: A 10V source with a 100ÃŽÂ© resistor should produce 0.1A
     * current.
     */
    @Test
    public void testOhmsLaw() {
        Circuit circuit = new Circuit();

        VoltageSource source = new VoltageSource(10.0); // 10V
        // Source from (0,0) to (0,1). V(0,1) - V(0,0) = 10V.
        source.setCoordinates(0, 0, 0, 1);

        Resistor resistor = new Resistor(100.0); // 100ÃŽÂ©
        // Resistor from (0,1) to (0,0). Current flows 10V -> 0V.
        resistor.setCoordinates(0, 1, 0, 0);

        Ground ground = new Ground();
        // Ground at (0,0). V(0,0) = 0V.
        ground.setCoordinates(0, 0, 0, 0);

        circuit.add(source);
        circuit.add(resistor);
        circuit.add(ground);

        assertTrue(circuit.analyze(), "Circuit analysis should succeed");
        assertNull(circuit.getStopMessage(), "No error message expected");

        assertTrue(circuit.step(), "Circuit step should succeed");

        // Expected current: I = V / R = 10 / 100 = 0.1A
        double expectedCurrent = 0.1;
        double actualCurrent = Math.abs(resistor.getCurrent());

        assertEquals(expectedCurrent, actualCurrent, TOLERANCE,
                "Resistor current should be " + expectedCurrent + "A (Ohm's Law)");
    }

    /**
     * Tests power dissipation: P = IÃ‚Â² * R = VÃ‚Â² / R
     */
    @Test
    public void testPowerDissipation() {
        Circuit circuit = new Circuit();

        VoltageSource source = new VoltageSource(10.0);
        source.setCoordinates(0, 0, 0, 1);

        Resistor resistor = new Resistor(100.0);
        resistor.setCoordinates(0, 1, 0, 0);

        Ground ground = new Ground();
        ground.setCoordinates(0, 0, 0, 0);

        circuit.add(source);
        circuit.add(resistor);
        circuit.add(ground);

        circuit.analyze();
        circuit.step();

        // Expected power: P = VÃ‚Â² / R = 100 / 100 = 1W
        double expectedPower = 1.0;
        double actualPower = Math.abs(resistor.getPower());

        assertEquals(expectedPower, actualPower, TOLERANCE,
                "Resistor power should be " + expectedPower + "W");
    }

    /**
     * Tests that the circuit resets correctly.
     */
    @Test
    public void testCircuitReset() {
        Circuit circuit = new Circuit();

        VoltageSource source = new VoltageSource(10.0);
        source.setCoordinates(0, 0, 0, 1);

        Resistor resistor = new Resistor(100.0);
        resistor.setCoordinates(0, 1, 0, 0);

        Ground ground = new Ground();
        ground.setCoordinates(0, 0, 0, 0);

        circuit.add(source);
        circuit.add(resistor);
        circuit.add(ground);

        circuit.analyze();
        circuit.step();
        circuit.step();
        circuit.step();

        double timeBeforeReset = circuit.getTime();
        assertTrue(timeBeforeReset > 0, "Time should have advanced");

        circuit.reset();

        assertEquals(0, circuit.getTime(), TOLERANCE, "Time should be reset to 0");
    }

    /**
     * Tests time step configuration.
     */
    @Test
    public void testTimeStep() {
        Circuit circuit = new Circuit();

        double customTimeStep = 1e-5;
        circuit.setTimeStep(customTimeStep);

        assertEquals(customTimeStep, circuit.getTimeStep(), TOLERANCE,
                "Time step should be configurable");
    }

    /**
     * Tests empty circuit handling.
     */
    @Test
    public void testEmptyCircuit() {
        Circuit circuit = new Circuit();

        assertFalse(circuit.analyze(), "Empty circuit analysis should fail gracefully");
        assertFalse(circuit.step(), "Empty circuit step should fail gracefully");
    }

    /**
     * Tests RC Circuit step response.
     * 10V Source, 100kÃŽÂ© Resistor, 1Ã‚ÂµF Capacitor.
     * Tau = R * C = 100e3 * 1e-6 = 0.1s.
     * At t = 0.1s, Voltage should be ~6.32V (10 * (1 - e^-1)).
     */
    @Test
    public void testRCCircuit() {
        Circuit circuit = new Circuit();
        circuit.setTimeStep(1e-3); // 1ms steps

        VoltageSource source = new VoltageSource(10.0);
        source.setCoordinates(0, 0, 0, 1);

        Resistor resistor = new Resistor(100e3); // 100kÃŽÂ©
        resistor.setCoordinates(0, 1, 0, 2);

        org.jscience.physics.classical.waves.electromagnetism.components.Capacitor capacitor = new org.jscience.physics.classical.waves.electromagnetism.components.Capacitor(
                1e-6); // 1Ã‚ÂµF
        capacitor.setCoordinates(0, 2, 0, 0);

        Ground ground = new Ground();
        ground.setCoordinates(0, 0, 0, 0);

        circuit.add(source);
        circuit.add(resistor);
        circuit.add(capacitor);
        circuit.add(ground);

        assertTrue(circuit.analyze(), "Analysis failed");

        // Simulate for 0.1s (1 Tau)
        for (int i = 0; i < 100; i++) {
            if (!circuit.step()) {
                fail("Step failed at iteration " + i + ": " + circuit.getStopMessage());
            }
        }

        double expectedVoltage = 10.0 * (1.0 - Math.exp(-1.0)); // ~6.32V
        double actualVoltage = Math.abs(capacitor.getVoltageDiff());

        // Use larger tolerance due to discretization error (Trapezoidal) at this time
        // step
        assertEquals(expectedVoltage, actualVoltage, 0.1,
                "Capacitor voltage at 1 Tau should be approx " + expectedVoltage + " V");
    }

    /**
     * Tests RL Circuit step response.
     * 10V Source, 100ÃŽÂ© Resistor, 1H Inductor.
     * Tau = L / R = 1 / 100 = 0.01s.
     * At t = 0.01s, Current should be ~0.0632A (0.1 * (1 - e^-1)).
     */
    @Test
    public void testRLCircuit() {
        Circuit circuit = new Circuit();
        circuit.setTimeStep(1e-4); // 0.1ms steps

        VoltageSource source = new VoltageSource(10.0);
        source.setCoordinates(0, 0, 0, 1);

        Resistor resistor = new Resistor(100.0); // 100ÃŽÂ©
        resistor.setCoordinates(0, 1, 0, 2);

        org.jscience.physics.classical.waves.electromagnetism.components.Inductor inductor = new org.jscience.physics.classical.waves.electromagnetism.components.Inductor(
                1.0); // 1H
        inductor.setCoordinates(0, 2, 0, 0);

        Ground ground = new Ground();
        ground.setCoordinates(0, 0, 0, 0);

        circuit.add(source);
        circuit.add(resistor);
        circuit.add(inductor);
        circuit.add(ground);

        assertTrue(circuit.analyze(), "Analysis failed");

        // Simulate for 0.01s (1 Tau) -> 100 steps
        for (int i = 0; i < 100; i++) {
            circuit.step();
        }

        double expectedCurrent = (10.0 / 100.0) * (1.0 - Math.exp(-1.0)); // ~0.0632 A
        double actualCurrent = Math.abs(inductor.getCurrent());

        assertEquals(expectedCurrent, actualCurrent, 0.005,
                "Inductor current at 1 Tau should be approx " + expectedCurrent + " A");
    }
}

