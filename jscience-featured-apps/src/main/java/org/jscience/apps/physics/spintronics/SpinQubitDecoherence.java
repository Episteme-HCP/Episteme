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
 * Spin Qubit Decoherence Simulation for quantum spintronics.
 * <p>
 * Models the loss of quantum coherence in spin qubits due to environmental
 * interactions. Key decoherence mechanisms include:
 * <ul>
 *   <li>T1 relaxation (spin-lattice, energy decay)</li>
 *   <li>T2 dephasing (spin-spin, phase randomization)</li>
 *   <li>T2* inhomogeneous broadening</li>
 * </ul>
 * </p>
 * 
 * <h3>Bloch-Redfield Theory</h3>
 * <p>
 * The density matrix evolution follows:
 * $$ \frac{d\rho}{dt} = -\frac{i}{\hbar}[H, \rho] + \mathcal{L}[\rho] $$
 * where $\mathcal{L}$ is the Lindblad superoperator.
 * </p>
 * 
 * <h3>References</h3>
 * <ul>
 *   <li><b>Loss, D. & DiVincenzo, D. P.</b> (1998). "Quantum computation with 
 *       quantum dots". <i>Physical Review A</i>, 57(1), 120.
 *       <a href="https://doi.org/10.1103/PhysRevA.57.120">DOI: 10.1103/PhysRevA.57.120</a></li>
 *   <li><b>Hanson, R. et al.</b> (2007). "Spins in few-electron quantum dots".
 *       <i>Reviews of Modern Physics</i>, 79(4), 1217.
 *       <a href="https://doi.org/10.1103/RevModPhys.79.1217">DOI: 10.1103/RevModPhys.79.1217</a></li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpinQubitDecoherence {

    private final Real t1;           // T1 relaxation time (s)
    private final Real t2;           // T2 dephasing time (s)
    private final Real t2Star;       // T2* inhomogeneous dephasing (s)
    private final Real temperature;  // Temperature (K)
    private final Real magneticField;// External B-field (T)
    
    // Qubit state: Bloch sphere representation
    private Real sx, sy, sz;         // Expectation values <σx>, <σy>, <σz>
    
    private final Random random = new Random();
    
    // Physical constants
    private static final double HBAR = 1.054571817e-34;  // J·s
    private static final double KB = 1.380649e-23;       // J/K
    private static final double GE = 2.002319;           // Electron g-factor
    private static final double MU_B = 9.274009994e-24;  // Bohr magneton (J/T)

    /**
     * Creates a spin qubit decoherence simulator.
     * 
     * @param t1 T1 relaxation time (s)
     * @param t2 T2 dephasing time (s)
     * @param temperature Environment temperature (K)
     * @param magneticField External magnetic field (T)
     */
    public SpinQubitDecoherence(Real t1, Real t2, Real temperature, Real magneticField) {
        this.t1 = t1;
        this.t2 = t2;
        this.t2Star = t2.multiply(Real.of(0.5)); // Typical T2* ~ T2/2
        this.temperature = temperature;
        this.magneticField = magneticField;
        
        // Initialize in |↑⟩ state
        this.sx = Real.ZERO;
        this.sy = Real.ZERO;
        this.sz = Real.ONE;
    }

    /**
     * Initializes qubit in a superposition state.
     * |ψ⟩ = cos(θ/2)|↑⟩ + e^(iφ)sin(θ/2)|↓⟩
     * 
     * @param theta Polar angle (radians)
     * @param phi Azimuthal angle (radians)
     */
    public void initializeState(double theta, double phi) {
        this.sx = Real.of(Math.sin(theta) * Math.cos(phi));
        this.sy = Real.of(Math.sin(theta) * Math.sin(phi));
        this.sz = Real.of(Math.cos(theta));
    }

    /**
     * Applies a single-qubit gate (rotation on Bloch sphere).
     * 
     * @param axis Rotation axis ('X', 'Y', 'Z')
     * @param angle Rotation angle (radians)
     */
    public void applyGate(char axis, double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        double x = sx.doubleValue();
        double y = sy.doubleValue();
        double z = sz.doubleValue();
        
        switch (axis) {
            case 'X':
                sy = Real.of(c * y - s * z);
                sz = Real.of(s * y + c * z);
                break;
            case 'Y':
                sx = Real.of(c * x + s * z);
                sz = Real.of(-s * x + c * z);
                break;
            case 'Z':
                sx = Real.of(c * x - s * y);
                sy = Real.of(s * x + c * y);
                break;
        }
    }

    /**
     * Evolves the qubit state under decoherence for a time step.
     * Uses Lindblad master equation approximation.
     * 
     * @param dt Time step (s)
     */
    public void evolve(Real dt) {
        double deltaT = dt.doubleValue();
        double t1Val = t1.doubleValue();
        double t2Val = t2.doubleValue();
        
        // Larmor precession around z-axis
        double omega = GE * MU_B * magneticField.doubleValue() / HBAR;
        double phase = omega * deltaT;
        applyGate('Z', phase);
        
        // T1 relaxation: σz → equilibrium
        double szEq = calculateEquilibriumPolarization();
        double gammaT1 = deltaT / t1Val;
        sz = Real.of(sz.doubleValue() * Math.exp(-gammaT1) + szEq * (1 - Math.exp(-gammaT1)));
        
        // T2 dephasing: σx, σy → 0
        double gammaT2 = deltaT / t2Val;
        sx = sx.multiply(Real.of(Math.exp(-gammaT2)));
        sy = sy.multiply(Real.of(Math.exp(-gammaT2)));
        
        // Add stochastic noise for T2*
        addDephaseNoise(deltaT);
    }

    /**
     * Calculates thermal equilibrium polarization.
     * P_eq = tanh(ℏω / 2kT)
     */
    private double calculateEquilibriumPolarization() {
        double energy = GE * MU_B * magneticField.doubleValue();
        double thermalEnergy = 2 * KB * temperature.doubleValue();
        
        if (thermalEnergy < 1e-30) return 1.0; // Zero temperature limit
        
        return Math.tanh(energy / thermalEnergy);
    }

    /**
     * Adds stochastic dephasing noise (T2* effects).
     */
    private void addDephaseNoise(double dt) {
        double t2StarVal = t2Star.doubleValue();
        if (t2StarVal > 0) {
            double noiseAmplitude = Math.sqrt(dt / t2StarVal);
            double phaseNoise = random.nextGaussian() * noiseAmplitude;
            applyGate('Z', phaseNoise);
        }
    }

    /**
     * Performs a projective measurement in the Z basis.
     * 
     * @return 0 for |↓⟩, 1 for |↑⟩
     */
    public int measureZ() {
        double pUp = (1 + sz.doubleValue()) / 2;
        int result = random.nextDouble() < pUp ? 1 : 0;
        
        // Collapse state
        sx = Real.ZERO;
        sy = Real.ZERO;
        sz = result == 1 ? Real.ONE : Real.ONE.negate();
        
        return result;
    }

    /**
     * Calculates the fidelity with respect to a target state.
     * 
     * @param targetSx Target ⟨σx⟩
     * @param targetSy Target ⟨σy⟩
     * @param targetSz Target ⟨σz⟩
     * @return Fidelity (0 to 1)
     */
    public Real calculateFidelity(Real targetSx, Real targetSy, Real targetSz) {
        // F = (1 + s·t) / 2 for pure states on Bloch sphere
        double dot = sx.doubleValue() * targetSx.doubleValue()
                   + sy.doubleValue() * targetSy.doubleValue()
                   + sz.doubleValue() * targetSz.doubleValue();
        return Real.of((1 + dot) / 2);
    }

    /**
     * Calculates the purity of the quantum state.
     * γ = Tr(ρ²) = (1 + |s|²) / 2
     * 
     * @return Purity (0.5 for maximally mixed, 1 for pure)
     */
    public Real calculatePurity() {
        double sMag2 = sx.doubleValue() * sx.doubleValue()
                     + sy.doubleValue() * sy.doubleValue()
                     + sz.doubleValue() * sz.doubleValue();
        return Real.of((1 + sMag2) / 2);
    }

    /**
     * Simulates a Ramsey experiment to measure T2*.
     * 
     * @param waitTime Free evolution time (s)
     * @param numShots Number of measurements
     * @return Average ⟨σx⟩ after Ramsey sequence
     */
    public Real ramseyExperiment(Real waitTime, int numShots) {
        double totalSx = 0;
        
        for (int shot = 0; shot < numShots; shot++) {
            // Reset to |↑⟩
            initializeState(0, 0);
            
            // π/2 pulse (X rotation)
            applyGate('X', Math.PI / 2);
            
            // Free evolution with decoherence
            Real dt = Real.of(waitTime.doubleValue() / 100);
            for (int i = 0; i < 100; i++) {
                evolve(dt);
            }
            
            // π/2 pulse (X rotation)
            applyGate('X', Math.PI / 2);
            
            totalSx += sx.doubleValue();
        }
        
        return Real.of(totalSx / numShots);
    }

    /**
     * Simulates a Hahn echo experiment to measure T2.
     * 
     * @param waitTime Total free evolution time (s)
     * @param numShots Number of measurements
     * @return Average ⟨σx⟩ after echo sequence
     */
    public Real hahnEchoExperiment(Real waitTime, int numShots) {
        double totalSx = 0;
        Real halfTime = waitTime.divide(Real.TWO);
        
        for (int shot = 0; shot < numShots; shot++) {
            initializeState(0, 0);
            
            // π/2 pulse
            applyGate('X', Math.PI / 2);
            
            // First half evolution
            Real dt = Real.of(halfTime.doubleValue() / 50);
            for (int i = 0; i < 50; i++) {
                evolve(dt);
            }
            
            // π pulse (refocusing)
            applyGate('X', Math.PI);
            
            // Second half evolution
            for (int i = 0; i < 50; i++) {
                evolve(dt);
            }
            
            // π/2 pulse
            applyGate('X', Math.PI / 2);
            
            totalSx += sx.doubleValue();
        }
        
        return Real.of(totalSx / numShots);
    }

    // Getters
    public Real getSx() { return sx; }
    public Real getSy() { return sy; }
    public Real getSz() { return sz; }
    public Real getT1() { return t1; }
    public Real getT2() { return t2; }
    public Real getT2Star() { return t2Star; }

    // Factory methods for common qubit systems
    
    /**
     * Creates a silicon quantum dot spin qubit.
     * Si/SiGe has excellent coherence times.
     */
    public static SpinQubitDecoherence createSiliconQuantumDot() {
        return new SpinQubitDecoherence(
            Real.of(1e-3),   // T1 ~ 1 ms
            Real.of(100e-6), // T2 ~ 100 μs
            Real.of(0.1),    // 100 mK
            Real.of(0.5)     // 0.5 T
        );
    }

    /**
     * Creates a GaAs quantum dot spin qubit.
     * Limited by hyperfine coupling to nuclear spins.
     */
    public static SpinQubitDecoherence createGaAsQuantumDot() {
        return new SpinQubitDecoherence(
            Real.of(100e-6), // T1 ~ 100 μs
            Real.of(10e-9),  // T2 ~ 10 ns (limited by nuclei)
            Real.of(0.05),   // 50 mK
            Real.of(1.0)     // 1 T
        );
    }

    /**
     * Creates a nitrogen-vacancy (NV) center in diamond.
     * Can operate at room temperature.
     */
    public static SpinQubitDecoherence createNVCenter() {
        return new SpinQubitDecoherence(
            Real.of(6e-3),   // T1 ~ 6 ms at room temp
            Real.of(2e-3),   // T2 ~ 2 ms with dynamical decoupling
            Real.of(300),    // Room temperature
            Real.of(0.01)    // Low field
        );
    }
}
