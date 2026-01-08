/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Circuit coupling for spintronic devices.
 * <p>
 * Models a spin valve connected in series with an RLC circuit,
 * enabling simulation of:
 * <ul>
 *   <li>Resonant oscillator circuits</li>
 *   <li>Neuromorphic computing elements</li>
 *   <li>Chaotic dynamics near bifurcations</li>
 * </ul>
 * </p>
 * 
 * <h3>Circuit Equation</h3>
 * <p>
 * $$ L \frac{dI}{dt} + R_{total} I + \frac{Q}{C} = V_{DC} $$
 * where $R_{total} = R_{load} + R_{spinvalve}(\theta)$
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class SpintronicCircuit {

    private final Real inductance;   // L (H)
    private final Real capacitance;  // C (F)
    private final Real loadResistance; // R_load (Ω)
    private final Real dcVoltage;    // V_DC (V)

    private Real current;    // I (A)
    private Real charge;     // Q (C)

    private final SpinValve spinValve;

    public SpintronicCircuit(Real L, Real C, Real Rload, Real Vdc, SpinValve valve) {
        this.inductance = L;
        this.capacitance = C;
        this.loadResistance = Rload;
        this.dcVoltage = Vdc;
        this.spinValve = valve;

        this.current = Real.ZERO;
        this.charge = Real.ZERO;
    }

    /**
     * Gets the total circuit resistance including the angle-dependent spin valve.
     */
    public Real getTotalResistance() {
        Real rSV = GMREffect.valetFertResistance(spinValve);
        // Scale from AR product to actual resistance (assume 100nm x 100nm pillar)
        Real area = Real.of(100e-9 * 100e-9);
        Real rActual = rSV.divide(area);
        return loadResistance.add(rActual);
    }

    /**
     * Gets current density through spin valve (for STT calculation).
     */
    public Real getCurrentDensity() {
        Real area = Real.of(100e-9 * 100e-9);
        return current.divide(area);
    }

    /**
     * Performs one time step of circuit dynamics.
     * Uses simple Euler integration.
     */
    public void step(Real dt) {
        Real R = getTotalResistance();

        // dI/dt = (V_DC - R*I - Q/C) / L
        Real dIdT = dcVoltage.subtract(R.multiply(current)).subtract(charge.divide(capacitance)).divide(inductance);

        // dQ/dt = I
        Real dQdT = current;

        // Euler update
        current = current.add(dIdT.multiply(dt));
        charge = charge.add(dQdT.multiply(dt));
    }

    /**
     * Coupled step: updates both circuit and magnetization dynamics.
     */
    public void coupledStep(Real dt, Real alpha, Real gamma, Real hExtX, Real hExtY, Real hExtZ) {
        // 1. Update circuit
        step(dt);

        // 2. Calculate STT from current
        Real j = getCurrentDensity();
        FerromagneticLayer free = spinValve.getFreeLayer();
        Real[] stt = SpinTransport.calculateSTT(j, free, spinValve.getPinnedLayer());

        // 3. Build effective field (external + STT-equivalent)
        // For simplicity, we add STT effect as a pseudo-field (approximation)
        Real ms = free.getMaterial().getSaturationMagnetization();
        Real hSttX = stt[0].divide(ms);
        Real hSttY = stt[1].divide(ms);
        Real hSttZ = stt[2].divide(ms);

        Real[] hEff = {
            hExtX.add(hSttX),
            hExtY.add(hSttY),
            hExtZ.add(hSttZ)
        };

        // 4. Update magnetization
        Real[] newM = SpinTransport.stepLLG(free, hEff, dt, alpha, gamma);
        free.setMagnetization(newM[0], newM[1], newM[2]);
    }

    // Getters for plotting
    public Real getCurrent() { return current; }
    public Real getCharge() { return charge; }
    public Real getVoltageCapacitor() { return charge.divide(capacitance); }
    public Real getVoltageSpinValve() { 
        Real area = Real.of(100e-9 * 100e-9);
        return GMREffect.valetFertResistance(spinValve).divide(area).multiply(current);
    }

    public void reset() {
        current = Real.ZERO;
        charge = Real.ZERO;
    }
}
