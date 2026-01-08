/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Predefined spintronic device configurations.
 * Provides ready-to-use structures for common applications.
 */
public class DevicePresets {

    /**
     * Standard GMR Read Head (Co/Cu/NiFe).
     */
    public static SpinValve createGMRReadHead() {
        FerromagneticLayer pinned = new FerromagneticLayer(SpintronicMaterial.COBALT, Real.of(3e-9), true);
        FerromagneticLayer free = new FerromagneticLayer(SpintronicMaterial.PERMALLOY, Real.of(4e-9), false);
        return new SpinValve(pinned, SpintronicMaterial.COPPER, Real.of(2.5e-9), free);
    }

    /**
     * STT-MRAM Cell (CoFeB/MgO/CoFeB - simplified as CoFe/Cu/CoFe).
     */
    public static SpinValve createSTTMRAM() {
        FerromagneticLayer pinned = new FerromagneticLayer(SpintronicMaterial.COBALT, Real.of(2e-9), true);
        FerromagneticLayer free = new FerromagneticLayer(SpintronicMaterial.COBALT, Real.of(1.5e-9), false);
        // In reality MgO barrier, using Cu as placeholder
        return new SpinValve(pinned, SpintronicMaterial.COPPER, Real.of(1e-9), free);
    }

    /**
     * Spin-Torque Oscillator (STO) optimized for RF output.
     */
    public static SpinValve createSTO() {
        FerromagneticLayer pinned = new FerromagneticLayer(SpintronicMaterial.COBALT, Real.of(10e-9), true);
        FerromagneticLayer free = new FerromagneticLayer(SpintronicMaterial.PERMALLOY, Real.of(5e-9), false);
        return new SpinValve(pinned, SpintronicMaterial.COPPER, Real.of(4e-9), free);
    }

    /**
     * SOT-MRAM structure with heavy metal underlayer.
     */
    public static class SOTMRAMDevice {
        public final SpinOrbitTorque.HeavyMetal heavyMetal;
        public final Real hmThickness;
        public final FerromagneticLayer freeLayer;
        public final boolean perpendicularAnisotropy;

        private SOTMRAMDevice(SpinOrbitTorque.HeavyMetal hm, Real hmT, FerromagneticLayer fl, boolean pma) {
            this.heavyMetal = hm;
            this.hmThickness = hmT;
            this.freeLayer = fl;
            this.perpendicularAnisotropy = pma;
        }

        public SpinOrbitTorque createSOT() {
            return new SpinOrbitTorque(heavyMetal, hmThickness);
        }
    }

    /**
     * Pt/Co SOT-MRAM with perpendicular anisotropy.
     */
    public static SOTMRAMDevice createPtCoSOTMRAM() {
        FerromagneticLayer co = new FerromagneticLayer(SpintronicMaterial.COBALT, Real.of(0.8e-9), false);
        return new SOTMRAMDevice(SpinOrbitTorque.HeavyMetal.PLATINUM, Real.of(5e-9), co, true);
    }

    /**
     * W/CoFeB SOT-MRAM with perpendicular anisotropy (high efficiency).
     */
    public static SOTMRAMDevice createWCoFeBSOTMRAM() {
        FerromagneticLayer cofeb = new FerromagneticLayer(SpintronicMaterial.COBALT, Real.of(1e-9), false); // CoFeB approximated
        return new SOTMRAMDevice(SpinOrbitTorque.HeavyMetal.TUNGSTEN_BETA, Real.of(4e-9), cofeb, true);
    }

    /**
     * Ta/NiFe SOT device (in-plane anisotropy, for domain wall motion).
     */
    public static SOTMRAMDevice createTaNiFeSOT() {
        FerromagneticLayer nife = new FerromagneticLayer(SpintronicMaterial.PERMALLOY, Real.of(3e-9), false);
        return new SOTMRAMDevice(SpinOrbitTorque.HeavyMetal.TANTALUM_BETA, Real.of(6e-9), nife, false);
    }

    /**
     * Domain Wall Racetrack Memory nanowire.
     */
    public static Micromagnetics1D createRacetrackMemory() {
        // 100 cells, 5nm each = 500nm wire
        return new Micromagnetics1D(
            100, 
            Real.of(5e-9), 
            SpintronicMaterial.PERMALLOY,
            Real.of(1.3e-11),  // Exchange stiffness A
            Real.of(5e3)       // Uniaxial anisotropy Ku
        );
    }

    /**
     * Skyrmion track for neuromorphic computing.
     * Note: Requires 2D micromagnetics for full simulation.
     */
    public static Micromagnetics1D createSkyrmionTrack() {
        return new Micromagnetics1D(
            200,
            Real.of(2e-9),
            SpintronicMaterial.COBALT,
            Real.of(1.5e-11),
            Real.of(8e5)  // Strong PMA
        );
    }
}
