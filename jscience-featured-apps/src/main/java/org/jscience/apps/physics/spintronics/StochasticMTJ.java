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

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.Random;

/**
 * Stochastic Magnetic Tunnel Junction (p-bit) for probabilistic computing.
 * <p>
 * A p-bit is a classical analog of a qubit that fluctuates between 0 and 1
 * states with controllable probability. Used in probabilistic computers
 * for optimization, sampling, and machine learning.
 * </p>
 * 
 * <h3>Physics</h3>
 * <p>
 * The output probability is controlled by the input voltage/current:
 * $$ P(m_i = +1) = \frac{1}{1 + e^{-\beta \cdot I_i}} $$
 * where β is the inverse "temperature" (gain) and I_i is the weighted input.
 * </p>
 * 
 * <h3>References</h3>
 * <ul>
 * <li><b>Camsari, K.Y. et al.</b> (2017). "Stochastic p-bits for Invertible Logic". 
 *     <i>Phys. Rev. X</i>, 7, 031014. 
 *     <a href="https://doi.org/10.1103/PhysRevX.7.031014">DOI: 10.1103/PhysRevX.7.031014</a></li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class StochasticMTJ {

    private final String id;
    private final Real thermalStability; // Δ = E_B / k_B T
    private final Real criticalCurrent;  // I_c (A)
    private final Real resistance0;      // Resistance in state 0 (Parallel)
    private final Real resistance1;      // Resistance in state 1 (Antiparallel)
    private final Real fluctuationRate;  // Attempt frequency (Hz)
    
    private int currentState; // 0 or 1
    private final Random random;

    public StochasticMTJ(String id, Real delta, Real ic, Real r0, Real r1, Real f0) {
        this.id = id;
        this.thermalStability = delta;
        this.criticalCurrent = ic;
        this.resistance0 = r0;
        this.resistance1 = r1;
        this.fluctuationRate = f0;
        this.currentState = 0;
        this.random = new Random();
    }

    /**
     * Updates the p-bit state based on input current.
     * Uses sigmoid activation with Boltzmann-like stochasticity.
     * 
     * @param inputCurrent Weighted sum of inputs (A)
     * @param beta Inverse temperature / gain parameter
     */
    public void update(Real inputCurrent, Real beta) {
        // Sigmoid probability: P(1) = 1 / (1 + exp(-β * I / I_c))
        double x = beta.doubleValue() * inputCurrent.doubleValue() / criticalCurrent.doubleValue();
        double prob = 1.0 / (1.0 + Math.exp(-x));
        
        // Stochastic update
        currentState = random.nextDouble() < prob ? 1 : 0;
    }

    /**
     * Updates using thermal fluctuations only (zero input).
     * Uses Néel-Arrhenius model.
     * 
     * @param dt Time step (s)
     */
    public void thermalFluctuation(Real dt) {
        // Switching rate: Γ = f_0 * exp(-Δ)
        double rate = fluctuationRate.doubleValue() * Math.exp(-thermalStability.doubleValue());
        double switchProb = rate * dt.doubleValue();
        
        if (random.nextDouble() < switchProb) {
            currentState = 1 - currentState; // Flip
        }
    }

    /**
     * Gets current resistance based on state.
     */
    public Real getResistance() {
        return currentState == 0 ? resistance0 : resistance1;
    }

    /**
     * Gets output voltage for given read current.
     */
    public Real getOutputVoltage(Real readCurrent) {
        return getResistance().multiply(readCurrent);
    }

    /**
     * Gets current state as ±1 (Ising spin representation).
     */
    public int getIsingState() {
        return currentState == 0 ? -1 : 1;
    }

    public int getState() { return currentState; }
    public void setState(int state) { this.currentState = state; }
    public String getId() { return id; }
    public Real getTMR() { return resistance1.subtract(resistance0).divide(resistance0); }

    // Factory for low-barrier MTJ
    public static StochasticMTJ createLowBarrier() {
        // Δ ~ 1-5 for fast fluctuations at room temperature
        return new StochasticMTJ("pbit",
            Real.of(3.0),      // Low thermal stability
            Real.of(50e-6),    // 50 μA critical current
            Real.of(5000),     // 5 kΩ parallel
            Real.of(10000),    // 10 kΩ antiparallel (100% TMR)
            Real.of(1e9)       // 1 GHz attempt frequency
        );
    }
}
