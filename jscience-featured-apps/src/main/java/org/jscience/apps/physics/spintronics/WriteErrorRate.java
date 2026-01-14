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
 * Write Error Rate (WER) Monte Carlo simulation for spintronic memory devices.
 * <p>
 * Calculates the probability of write failures in MRAM, SOT-MRAM, and STT-MRAM
 * devices using statistical physics and stochastic simulations.
 * </p>
 * 
 * <h3>Error Sources</h3>
 * <ul>
 *   <li>Thermal fluctuations (Néel-Brown model)</li>
 *   <li>Current pulse variations</li>
 *   <li>Device-to-device variability</li>
 *   <li>Read disturb</li>
 *   <li>Retention failures</li>
 * </ul>
 * 
 * <h3>Key Metrics</h3>
 * <p>
 * $$ WER = \exp\left(-\frac{E_b}{k_B T}\right) \cdot f(I, t_p) $$
 * where $E_b$ is the energy barrier and $f(I, t_p)$ captures write dynamics.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class WriteErrorRate {

    // private final Real energyBarrier;        // Eb = Ku * V (J)
    private final Real thermalStability;     // Δ = Eb / kT
    private final Real criticalCurrent;      // Ic0 (A)
    private final Real attemptFrequency;     // f0 ~ 1 GHz
    private final Real temperature;          // T (K)
    private final Real deviceVariability;    // σ/μ for device parameters
    
    private final Random random = new Random();
    
    // Physical constants
    // private static final double KB = 1.380649e-23; // Boltzmann constant (thermal stability Δ used directly)
    // private static final double HBAR = 1.054571817e-34;

    /**
     * Creates a WER Monte Carlo simulator.
     * 
     * @param thermalStability Δ = Eb/kT (dimensionless, typically 40-80)
     * @param criticalCurrent Critical switching current (A)
     * @param temperature Operating temperature (K)
     * @param deviceVariability Device parameter variability (σ/μ)
     */
    public WriteErrorRate(Real thermalStability, Real criticalCurrent, 
                          Real temperature, Real deviceVariability) {
        this.thermalStability = thermalStability;
        this.temperature = temperature;
        this.criticalCurrent = criticalCurrent;
        this.deviceVariability = deviceVariability;
        // this.energyBarrier = thermalStability.multiply(Real.of(KB)).multiply(temperature);
        this.attemptFrequency = Real.of(1e9); // 1 GHz
    }

    /**
     * Calculates WER for STT-MRAM write operation.
     * Uses Sun model for switching probability.
     * 
     * @param writeCurrent Applied write current (A)
     * @param pulseDuration Pulse duration (s)
     * @param numTrials Number of Monte Carlo trials
     * @return Write error rate (probability)
     */
    public Real calculateSTTWriteErrorRate(Real writeCurrent, Real pulseDuration, int numTrials) {
        int failures = 0;
        
        double ic0 = criticalCurrent.doubleValue();
        double delta = thermalStability.doubleValue();
        double tp = pulseDuration.doubleValue();
        double f0 = attemptFrequency.doubleValue();
        double sigma = deviceVariability.doubleValue();
        
        for (int trial = 0; trial < numTrials; trial++) {
            // Add device variability
            double icVaried = ic0 * (1 + sigma * random.nextGaussian());
            double deltaVaried = delta * (1 + sigma * 0.1 * random.nextGaussian());
            double iWrite = writeCurrent.doubleValue() * (1 + sigma * 0.05 * random.nextGaussian());
            
            // Sun model: τ = τ0 * exp(Δ(1 - I/Ic)^ξ)
            double overdrive = iWrite / icVaried;
            double xi = 1.5; // Exponent (1 for high barrier, 2 for low overdrive)
            
            if (overdrive <= 1) {
                failures++; // Not enough current to switch
            } else {
                double effectiveDelta = deltaVaried * Math.pow(1 - 1 / overdrive, xi);
                double switchingTime = (1 / f0) * Math.exp(effectiveDelta);
                
                // Thermal activation probability
                double pSwitch = 1 - Math.exp(-tp / switchingTime);
                
                // Add thermal noise to switching
                if (random.nextDouble() > pSwitch) {
                    failures++;
                }
            }
        }
        
        return Real.of((double) failures / numTrials);
    }

    /**
     * Calculates WER for SOT-MRAM write operation.
     * Includes field-free switching via exchange bias.
     * 
     * @param chargeCurrent In-plane charge current (A)
     * @param pulseDuration Pulse duration (s)
     * @param externalField External z-field for deterministic switching (T)
     * @param numTrials Number of Monte Carlo trials
     * @return Write error rate
     */
    public Real calculateSOTWriteErrorRate(Real chargeCurrent, Real pulseDuration,
                                            Real externalField, int numTrials) {
        int failures = 0;
        
        double ic0 = criticalCurrent.doubleValue();
        double delta = thermalStability.doubleValue();
        double tp = pulseDuration.doubleValue();
        double hExt = externalField.doubleValue();
        double sigma = deviceVariability.doubleValue();
        
        for (int trial = 0; trial < numTrials; trial++) {
            double iCharge = chargeCurrent.doubleValue() * (1 + sigma * 0.05 * random.nextGaussian());
            double deltaVaried = delta * (1 + sigma * 0.1 * random.nextGaussian());
            
            // SOT switching requires breaking symmetry with Hz
            // Or using exchange bias / tilted anisotropy
            double symmetryBreaking = Math.abs(hExt) > 0.001 ? 1.0 : 0.0;
            
            if (symmetryBreaking < 0.5) {
                // 50% chance of switching to wrong state without symmetry breaking
                if (random.nextDouble() < 0.5) {
                    failures++;
                    continue;
                }
            }
            
            // SOT critical current model
            double overdrive = iCharge / ic0;
            if (overdrive < 1) {
                failures++;
            } else {
                // Faster switching than STT due to damping-like torque efficiency
                double tauSwitch = tp / (overdrive - 1) / 10;
                double pSwitch = 1 - Math.exp(-tp / tauSwitch);
                
                // Stochastic thermal assistance/hindrance
                pSwitch *= Math.exp(-deltaVaried * (1 - overdrive) * 0.1);
                
                if (random.nextDouble() > pSwitch) {
                    failures++;
                }
            }
        }
        
        return Real.of((double) failures / numTrials);
    }

    /**
     * Calculates retention error rate.
     * Probability of spontaneous switching during idle time.
     * 
     * @param retentionTime Required retention time (s)
     * @param numTrials Monte Carlo trials
     * @return Retention error probability
     */
    public Real calculateRetentionError(Real retentionTime, int numTrials) {
        int failures = 0;
        
        double delta = thermalStability.doubleValue();
        double f0 = attemptFrequency.doubleValue();
        double tRet = retentionTime.doubleValue();
        double sigma = deviceVariability.doubleValue();
        
        for (int trial = 0; trial < numTrials; trial++) {
            double deltaVaried = delta * (1 + sigma * 0.1 * random.nextGaussian());
            
            // Néel-Brown thermal activation
            double tau = (1 / f0) * Math.exp(deltaVaried);
            double pRetain = Math.exp(-tRet / tau);
            
            if (random.nextDouble() > pRetain) {
                failures++;
            }
        }
        
        return Real.of((double) failures / numTrials);
    }

    /**
     * Calculates read disturb probability.
     * Lower current but longer duration may cause partial switching.
     * 
     * @param readCurrent Read current (A)
     * @param readDuration Read pulse duration (s)
     * @param numTrials Monte Carlo trials
     * @return Read disturb probability
     */
    public Real calculateReadDisturb(Real readCurrent, Real readDuration, int numTrials) {
        int disturbs = 0;
        
        double ic0 = criticalCurrent.doubleValue();
        double delta = thermalStability.doubleValue();
        double iRead = readCurrent.doubleValue();
        double tRead = readDuration.doubleValue();
        double f0 = attemptFrequency.doubleValue();
        
        // Read current typically 0.1-0.3 * Ic
        double overdrive = iRead / ic0;
        
        for (int trial = 0; trial < numTrials; trial++) {
            // Reduced barrier due to partial STT
            double effectiveDelta = delta * (1 - 0.5 * overdrive * overdrive);
            double tau = (1 / f0) * Math.exp(effectiveDelta);
            
            double pDisturb = 1 - Math.exp(-tRead / tau);
            
            if (random.nextDouble() < pDisturb) {
                disturbs++;
            }
        }
        
        return Real.of((double) disturbs / numTrials);
    }

    /**
     * Runs a comprehensive WER sweep over current and pulse width.
     * 
     * @param currentMin Minimum current (A)
     * @param currentMax Maximum current (A)
     * @param currentSteps Number of current steps
     * @param pulseMin Minimum pulse duration (s)
     * @param pulseMax Maximum pulse duration (s)
     * @param pulseSteps Number of pulse steps
     * @param trialsPerPoint Monte Carlo trials per point
     * @return 2D array of WER values [current][pulse]
     */
    public double[][] sweepWER(double currentMin, double currentMax, int currentSteps,
                                double pulseMin, double pulseMax, int pulseSteps,
                                int trialsPerPoint) {
        double[][] werMatrix = new double[currentSteps][pulseSteps];
        
        double dI = (currentMax - currentMin) / (currentSteps - 1);
        double dT = (pulseMax - pulseMin) / (pulseSteps - 1);
        
        for (int i = 0; i < currentSteps; i++) {
            Real current = Real.of(currentMin + i * dI);
            for (int j = 0; j < pulseSteps; j++) {
                Real pulse = Real.of(pulseMin + j * dT);
                werMatrix[i][j] = calculateSTTWriteErrorRate(current, pulse, trialsPerPoint).doubleValue();
            }
        }
        
        return werMatrix;
    }

    /**
     * Calculates required write current for target WER at given pulse duration.
     * 
     * @param targetWER Target write error rate (e.g., 1e-6)
     * @param pulseDuration Pulse duration (s)
     * @param searchTrials Trials for each search iteration
     * @return Required write current (A)
     */
    public Real findRequiredCurrent(Real targetWER, Real pulseDuration, int searchTrials) {
        double icMin = criticalCurrent.doubleValue() * 0.5;
        double icMax = criticalCurrent.doubleValue() * 3.0;
        double target = targetWER.doubleValue();
        
        // Binary search
        for (int iter = 0; iter < 20; iter++) {
            double icMid = (icMin + icMax) / 2;
            Real wer = calculateSTTWriteErrorRate(Real.of(icMid), pulseDuration, searchTrials);
            
            if (wer.doubleValue() > target) {
                icMin = icMid; // Need more current
            } else {
                icMax = icMid; // Can reduce current
            }
        }
        
        return Real.of((icMin + icMax) / 2);
    }

    // Getters
    public Real getThermalStability() { return thermalStability; }
    public Real getCriticalCurrent() { return criticalCurrent; }
    public Real getTemperature() { return temperature; }

    // Factory methods
    
    /**
     * Creates a typical STT-MRAM device at room temperature.
     */
    public static WriteErrorRate createSTTMRAM() {
        return new WriteErrorRate(
            Real.of(60),        // Δ = 60 (10 year retention)
            Real.of(100e-6),    // Ic = 100 μA
            Real.of(358),       // 85°C operating
            Real.of(0.05)       // 5% device variability
        );
    }

    /**
     * Creates a typical SOT-MRAM device.
     */
    public static WriteErrorRate createSOTMRAM() {
        return new WriteErrorRate(
            Real.of(50),        // Slightly lower Δ acceptable (2-terminal write)
            Real.of(200e-6),    // Higher Ic but faster
            Real.of(358),       
            Real.of(0.04)       
        );
    }

    /**
     * Creates a voltage-controlled MRAM (VCMA) device.
     */
    public static WriteErrorRate createVCMAMRAM() {
        return new WriteErrorRate(
            Real.of(45),        // Lower Δ due to precessional switching
            Real.of(20e-6),     // Very low current (field effect)
            Real.of(300),       // Room temp
            Real.of(0.06)       // Higher variability
        );
    }
}
