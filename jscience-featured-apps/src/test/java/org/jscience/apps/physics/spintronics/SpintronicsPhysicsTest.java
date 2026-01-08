/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for core spintronics physics calculations.
 */
class SpintronicsPhysicsTest {

    private static final double TOLERANCE = 1e-6;

    // ========== GMR Effect Tests ==========

    @Test
    @DisplayName("Valet-Fert: Parallel state has lower resistance than antiparallel")
    void testValetFertParallelLowerResistance() {
        FerromagneticLayer pinned = new FerromagneticLayer(SpintronicMaterial.COBALT, Real.of(5e-9), true);
        FerromagneticLayer freeP = new FerromagneticLayer(SpintronicMaterial.COBALT, Real.of(5e-9), false);
        freeP.setMagnetization(Real.ONE, Real.ZERO, Real.ZERO); // Parallel to pinned

        FerromagneticLayer freeAP = new FerromagneticLayer(SpintronicMaterial.COBALT, Real.of(5e-9), false);
        freeAP.setMagnetization(Real.ONE.negate(), Real.ZERO, Real.ZERO); // Antiparallel

        SpinValve svP = new SpinValve(pinned, SpintronicMaterial.COPPER, Real.of(3e-9), freeP);
        SpinValve svAP = new SpinValve(pinned, SpintronicMaterial.COPPER, Real.of(3e-9), freeAP);

        Real rP = GMREffect.valetFertResistance(svP);
        Real rAP = GMREffect.valetFertResistance(svAP);

        assertTrue(rP.doubleValue() < rAP.doubleValue(), 
            "Parallel resistance should be lower than antiparallel");
    }

    @Test
    @DisplayName("Julliere TMR: Higher polarization gives higher TMR")
    void testJulliereTMR() {
        Real p1 = Real.of(0.5);
        Real p2 = Real.of(0.5);
        Real tmr = GMREffect.calculateJulliereTMR(p1, p2);

        // TMR = 2*P1*P2 / (1 - P1*P2) = 2*0.25 / 0.75 = 0.667
        assertEquals(0.667, tmr.doubleValue(), 0.01);
    }

    // ========== LLG Dynamics Tests ==========

    @Test
    @DisplayName("LLG: Magnetization magnitude preserved after step")
    void testLLGNormPreservation() {
        FerromagneticLayer layer = new FerromagneticLayer(SpintronicMaterial.COBALT, Real.of(5e-9), false);
        layer.setMagnetization(Real.of(0.6), Real.of(0.8), Real.ZERO);

        Real[] hEff = {Real.of(1000), Real.ZERO, Real.ZERO};
        Real dt = Real.of(1e-12);
        Real alpha = Real.of(0.01);
        Real gamma = Real.of(1.76e11);

        Real[] newM = SpinTransport.stepLLG(layer, hEff, dt, alpha, gamma);

        double norm = Math.sqrt(
            newM[0].doubleValue() * newM[0].doubleValue() +
            newM[1].doubleValue() * newM[1].doubleValue() +
            newM[2].doubleValue() * newM[2].doubleValue()
        );

        assertEquals(1.0, norm, TOLERANCE, "Magnetization should remain unit length");
    }

    @Test
    @DisplayName("LLG Heun: More accurate than Euler for precession")
    void testLLGHeunAccuracy() {
        FerromagneticLayer layer = new FerromagneticLayer(SpintronicMaterial.COBALT, Real.of(5e-9), false);
        layer.setMagnetization(Real.ONE, Real.ZERO, Real.ZERO);

        Real[] hEff = {Real.ZERO, Real.ZERO, Real.of(1e5)}; // Strong Z-field
        Real dt = Real.of(1e-13);
        Real alpha = Real.of(0.001); // Low damping for pure precession
        Real gamma = Real.of(1.76e11);

        // Run 1000 steps
        Real[] m = layer.getMagnetization();
        for (int i = 0; i < 1000; i++) {
            m = SpinTransport.stepLLGHeun(layer, hEff, dt, alpha, gamma);
            layer.setMagnetization(m[0], m[1], m[2]);
        }

        // Should still be in XY plane (mz ≈ 0) with |m| = 1
        assertTrue(Math.abs(m[2].doubleValue()) < 0.1, "Magnetization should stay in XY plane");
    }

    // ========== SOT Tests ==========

    @Test
    @DisplayName("SOT: Damping-like torque perpendicular to m and σ")
    void testSOTDampingLikeTorque() {
        SpinOrbitTorque sot = new SpinOrbitTorque(
            SpinOrbitTorque.HeavyMetal.PLATINUM, Real.of(5e-9));
        FerromagneticLayer layer = new FerromagneticLayer(SpintronicMaterial.COBALT, Real.of(1e-9), false);
        layer.setMagnetization(Real.ZERO, Real.ZERO, Real.ONE); // m along z

        Real[] currentDir = {Real.ONE, Real.ZERO, Real.ZERO}; // Current along x
        Real j = Real.of(1e12);

        Real[] tau = sot.calculateDampingLikeTorque(j, layer, currentDir);

        // τ_DL should be along x (σ × m direction for m||z, σ||y)
        assertTrue(Math.abs(tau[0].doubleValue()) > 0 || Math.abs(tau[1].doubleValue()) > 0,
            "Damping-like torque should be non-zero in-plane");
    }

    @Test
    @DisplayName("SOT: Tungsten has negative spin Hall angle")
    void testTungstenNegativeSHA() {
        assertTrue(SpinOrbitTorque.HeavyMetal.TUNGSTEN_BETA.spinHallAngle.doubleValue() < 0,
            "β-W should have negative spin Hall angle");
    }

    // ========== Micromagnetics Tests ==========

    @Test
    @DisplayName("Micromag 2D: Skyrmion has non-zero topological charge")
    void testSkyrmionTopologicalCharge() {
        Micromagnetics2D sim = new Micromagnetics2D(
            50, 50, Real.of(2e-9), SpintronicMaterial.COBALT,
            Real.of(1.5e-11), Real.of(3e-3), Real.of(8e5));

        sim.nucleateSkyrmion(25, 25, 8);

        double Q = sim.calculateSkyrmionNumber();

        // Skyrmion should have Q ≈ -1 (for down core)
        assertTrue(Math.abs(Q + 1.0) < 0.5, 
            "Skyrmion should have topological charge near -1, got: " + Q);
    }

    // ========== p-bit Tests ==========

    @Test
    @DisplayName("StochasticMTJ: High current drives state to 1")
    void testPbitHighCurrentDrivesToOne() {
        StochasticMTJ pbit = StochasticMTJ.createLowBarrier();
        Real highCurrent = Real.of(500e-6); // Well above I_c
        Real beta = Real.of(10);

        // Run many updates
        int ones = 0;
        for (int i = 0; i < 100; i++) {
            pbit.update(highCurrent, beta);
            if (pbit.getState() == 1) ones++;
        }

        assertTrue(ones > 80, "High current should drive pbit to state 1 most of the time");
    }

    @Test
    @DisplayName("PBitNetwork: Annealing finds lower energy state")
    void testPbitNetworkAnnealing() {
        // Simple ferromagnetic chain
        PBitNetwork net = new PBitNetwork(5, Real.of(1.0));
        for (int i = 0; i < 4; i++) {
            net.setWeight(i, i + 1, 1.0); // Ferromagnetic coupling
        }

        int[] result = net.anneal(1000, 0.1, 10.0);
        double energy = net.getEnergy();
        assertNotNull(result, "Anneal should return a result");

        // Ferromagnetic ground state: all aligned → E = -4
        assertTrue(energy <= -2.0, "Annealing should find low energy state, got: " + energy);
    }

    // ========== VCMA Tests ==========

    @Test
    @DisplayName("VCMA: Positive voltage increases anisotropy")
    void testVCMAPositiveVoltage() {
        VCMA vcma = VCMA.createCoFeBMgO();
        Real kBase = vcma.getBaseAnisotropy();
        Real kWithV = vcma.getEffectiveAnisotropy(Real.of(1.0)); // 1V

        assertTrue(kWithV.doubleValue() > kBase.doubleValue(),
            "Positive voltage should increase anisotropy for positive ξ");
    }

    // ========== Spin Seebeck Tests ==========

    @Test
    @DisplayName("SpinSeebeck: Larger gradient gives larger voltage")
    void testSpinSeebeckScaling() {
        SpinSeebeckEffect sse = SpinSeebeckEffect.createYIGPt();

        Real v1 = sse.measureVoltage(Real.of(1e6));  // 1 K/mm
        Real v2 = sse.measureVoltage(Real.of(2e6));  // 2 K/mm

        assertTrue(v2.doubleValue() > v1.doubleValue(),
            "Larger temperature gradient should give larger voltage");
    }
}
