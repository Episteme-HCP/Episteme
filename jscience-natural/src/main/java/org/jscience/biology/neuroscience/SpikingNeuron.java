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

package org.jscience.biology.neuroscience;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Spiking Neuron model.
 * Leaky Integrate-and-Fire (LIF) implementation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpikingNeuron {

    // Parameters
    private final Real membraneResistance; // Rm (Ohm)
    private final Real thresholdPotential; // Vth (Volts)
    private final Real restingPotential; // Vrest (Volts)
    private final Real resetPotential; // Vreset (Volts)
    private final Real refractoryPeriod; // seconds

    // State
    private Real membranePotential; // Vm
    private Real lastSpikeTime;
    private Real currentInput; // I(t)

    // Derived
    private final Real tau; // Time constant = Rm * Cm

    public SpikingNeuron(Real rm, Real cm, Real vTh, Real vRest, Real vReset, Real refract) {
        this.membraneResistance = rm;
        this.thresholdPotential = vTh;
        this.restingPotential = vRest;
        this.resetPotential = vReset;
        this.refractoryPeriod = refract;
        this.tau = rm.multiply(cm);

        reset();
    }

    public void reset() {
        this.membranePotential = restingPotential;
        this.lastSpikeTime = Real.NEGATIVE_INFINITY;
        this.currentInput = Real.ZERO;
    }

    /**
     * Adds input current for this time step.
     */
    public void addInputCurrent(Real current) {
        this.currentInput = this.currentInput.add(current);
    }

    /**
     * Updates neuron state.
     * dV/dt = (-(V - Vrest) + R*I) / tau
     * 
     * @param dt          Time step (seconds)
     * @param currentTime Current simulation time
     * @return true if neuron spiked
     */
    public boolean update(Real dt, Real currentTime) {
        // Refractory period check
        if (currentTime.subtract(lastSpikeTime).compareTo(refractoryPeriod) < 0) {
            membranePotential = resetPotential;
            currentInput = Real.ZERO;
            return false;
        }

        // Euler integration
        Real leak = membranePotential.subtract(restingPotential).negate();
        Real inputTerm = membraneResistance.multiply(currentInput);
        Real dV = leak.add(inputTerm).divide(tau).multiply(dt);
        membranePotential = membranePotential.add(dV);

        currentInput = Real.ZERO; // Reset input for next step

        // Spike check
        if (membranePotential.compareTo(thresholdPotential) >= 0) {
            membranePotential = resetPotential;
            lastSpikeTime = currentTime;
            return true;
        }

        return false;
    }

    public Real getMembranePotential() {
        return membranePotential;
    }
}
