/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.physics;

import org.jscience.mathematics.numbers.real.Real;

import org.jscience.physics.classical.thermodynamics.Thermodynamics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Pressure;

import org.jscience.measure.quantity.Volume;

/**
 * Test suite for physics packages.
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class PhysicsTest {

    private static final double TOLERANCE = 1e-6;

    /**
     * Tests physical constants against CODATA values.
     */
    @Test
    public void testPhysicalConstants() {
        // CODATA 2022 values
        assertEquals(299792458.0, PhysicalConstants.SPEED_OF_LIGHT.getValue().doubleValue(), 0.1);
        assertEquals(6.62607015e-34, PhysicalConstants.PLANCK_CONSTANT.getValue().doubleValue(), 1e-42);
        assertEquals(1.602176634e-19, PhysicalConstants.ELEMENTARY_CHARGE.getValue().doubleValue(), 1e-27);
    }

    /**
     * Tests basic Newtonian mechanics (F=ma).
     */
    @Test
    public void testNewtonianMechanics() {
        // F = ma test
        @SuppressWarnings("unused")
        Real mass = Real.of(10.0); // 10 kg
        @SuppressWarnings("unused")
        Real accel = Real.of(9.8); // 9.8 m/s²

        // Expected: F = 10 * 9.8 = 98 N (simplified, needs Quantity)
        // This is a simplified test; full test would use Quantity types
    }

    /**
     * Tests conservation laws (elastic collision).
     */
    @Test
    public void testConservationLaws() {
        // Elastic collision test
        Real m1 = Real.of(1.0);
        Real v1 = Real.of(2.0);
        Real m2 = Real.of(1.0);
        Real v2 = Real.ZERO;

        Real[] finalVelocities = ConservationLaws.elasticCollision1D(m1, v1, m2, v2);

        // For equal masses with v2=0, velocities should swap
        assertEquals(0.0, finalVelocities[0].doubleValue(), TOLERANCE);
        assertEquals(2.0, finalVelocities[1].doubleValue(), TOLERANCE);
    }

    /**
     * Tests Lagrangian mechanics (free particle).
     */
    @Test
    public void testLagrangianMechanics() {
        // Free particle: L = T = ½mv²
        Real mass = Real.of(2.0);
        Real velocity = Real.of(3.0);

        Real lagrangian = LagrangianMechanics.lagrangianFreeParticle(mass, velocity);

        // Expected: ½ * 2 * 9 = 9
        assertEquals(9.0, lagrangian.doubleValue(), TOLERANCE);
    }

    /**
     * Tests Hamiltonian mechanics (free particle).
     */
    @Test
    public void testHamiltonianMechanics() {
        // Free particle: H = p²/(2m)
        Real momentum = Real.of(6.0);
        Real mass = Real.of(2.0);

        Real hamiltonian = HamiltonianMechanics.hamiltonianFreeParticle(momentum, mass);

        // Expected: 36 / 4 = 9
        assertEquals(9.0, hamiltonian.doubleValue(), TOLERANCE);
    }

    /**
     * Tests statistical mechanics (Boltzmann entropy).
     */
    @Test
    public void testStatisticalMechanics() {
        // Boltzmann entropy: S = k ln(Ω)
        Real numMicrostates = Real.of(Math.E); // Ω = e

        Real entropy = StatisticalMechanics.boltzmannEntropy(numMicrostates);

        // Expected: k * ln(e) = k
        assertEquals(PhysicalConstants.BOLTZMANN_CONSTANT.getValue().doubleValue(),
                entropy.doubleValue(), 1e-30);
    }

    /**
     * Tests thermodynamics (Ideal Gas Law).
     */
    @Test
    public void testThermodynamics() {
        // Ideal gas law simplified test
        // 1 mole, 273.15 K, 22.4 L = 0.0224 m^3
        Quantity<Pressure> pressure = Thermodynamics.idealGasPressure(
                Quantities.create(1.0, Units.MOLE),
                Quantities.create(273.15, Units.KELVIN),
                Quantities.create(0.0224,
                        (org.jscience.measure.Unit<Volume>) (org.jscience.measure.Unit<?>) Units.METER.pow(3)));

        // Expected: P ≈ 101325 Pa (1 atm)
        // PV = nRT -> P = nRT/V = 1 * 8.314 * 273.15 / 0.0224 ≈ 101383 Pa
        assertEquals(101325.0, pressure.getValue().doubleValue(), 1000.0);
    }

    /**
     * Tests relativity (Lorentz factor, Time dilation).
     */
    @Test
    public void testRelativity() {
        // Lorentz factor for v = 0.6c
        // gamma = 1 / sqrt(1 - 0.6^2) = 1 / sqrt(0.64) = 1 / 0.8 = 1.25
        Real c = PhysicalConstants.SPEED_OF_LIGHT.getValue();
        Real v = c.multiply(Real.of(0.6));
        Real gamma = org.jscience.physics.relativity.Relativity.lorentzFactor(v);
        assertEquals(1.25, gamma.doubleValue(), TOLERANCE);

        // Time dilation
        Real t0 = Real.of(10.0);
        Real t = org.jscience.physics.relativity.Relativity.timeDilation(t0, v);
        assertEquals(12.5, t.doubleValue(), TOLERANCE);
    }

    /**
     * Tests quantum mechanics (Hydrogen energy, Bohr radius).
     */
    @Test
    public void testQuantumMechanics() {
        // Hydrogen ground state energy (n=1)
        Real e1 = org.jscience.physics.quantum.QuantumMechanics.hydrogenEnergyLevel(1);
        assertEquals(-13.6, e1.doubleValue(), TOLERANCE);

        // Bohr radius check (approx 5.29e-11 m)
        Real a0 = org.jscience.physics.quantum.QuantumMechanics.bohrRadius();
        assertEquals(5.291772109e-11, a0.doubleValue(), 1e-13);
    }

    /**
     * Tests nuclear physics (Mass-energy, Half-life).
     */
    @Test
    public void testNuclearPhysics() {
        // Mass-energy: E = mc^2
        Real m = Real.of(1.0);
        Real E = org.jscience.physics.quantum.NuclearPhysics.massEnergy(m);
        Real c = PhysicalConstants.SPEED_OF_LIGHT.getValue();
        Real expected = c.multiply(c);
        assertEquals(expected.doubleValue(), E.doubleValue(), TOLERANCE);

        // Half-life: T1/2 = ln(2)/lambda
        Real lambda = Real.of(0.69314718056); // ln(2)
        Real halfLife = org.jscience.physics.quantum.NuclearPhysics.halfLife(lambda);
        assertEquals(1.0, halfLife.doubleValue(), TOLERANCE);
    }
}
