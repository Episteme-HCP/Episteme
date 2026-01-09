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

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.ArrayList;
import java.util.List;

/**
 * Magnon-Phonon Coupling simulation for hybrid spin-lattice dynamics.
 * <p>
 * Magnons (spin waves) and phonons (lattice vibrations) interact through
 * magnetoelastic coupling, enabling:
 * <ul>
 *   <li>Spin-lattice relaxation</li>
 *   <li>Acoustic spin pumping</li>
 *   <li>Magnon-polaron formation</li>
 *   <li>Surface acoustic wave (SAW) driven magnetization dynamics</li>
 * </ul>
 * </p>
 * 
 * <h3>Coupled Equations</h3>
 * <p>
 * The magnon-phonon Hamiltonian includes:
 * $$ H_{mp} = \sum_k \hbar\omega_k^m a_k^\dagger a_k + \sum_q \hbar\omega_q^p b_q^\dagger b_q 
 *           + \sum_{k,q} g_{kq}(a_k^\dagger b_q + a_k b_q^\dagger) $$
 * </p>
 * 
 * <h3>References</h3>
 * <ul>
 *   <li><b>Kittel, C.</b> (1958). "Interaction of spin waves and ultrasonic waves".
 *       <i>Physical Review</i>, 110(3), 836.</li>
 *   <li><b>An, K. et al.</b> (2020). "Coherent long-range transfer of angular momentum".
 *       <i>Physical Review B</i>, 101(6), 060407.</li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MagnonPhononCoupling {

    // Material parameters
    private final Real magnetoelasticB1;    // First magnetoelastic constant (J/m³)
    private final Real magnetoelasticB2;    // Second magnetoelastic constant (J/m³)
    private final Real elasticC11;          // Elastic stiffness (Pa)
    private final Real elasticC44;          // Shear modulus (Pa)
    private final Real massDensity;         // Mass density (kg/m³)
    private final Real saturationMag;       // Saturation magnetization (A/m)
    private final Real exchangeStiffness;   // Exchange stiffness (J/m)
    
    // Simulation state
    private final int numModes;             // Number of momentum modes
    private final double[] magnonAmplitudes;
    private final double[] phononAmplitudes;
    private final double[] magnonFrequencies;
    private final double[] phononFrequencies;
    private final double[][] couplingMatrix;
    
    // Physical constants
    private static final double HBAR = 1.054571817e-34;
    private static final double GAMMA = 1.76e11; // Gyromagnetic ratio

    /**
     * Creates a magnon-phonon coupling simulator.
     * 
     * @param b1 First magnetoelastic constant B1 (J/m³)
     * @param b2 Second magnetoelastic constant B2 (J/m³)
     * @param c11 Longitudinal elastic constant (Pa)
     * @param c44 Shear elastic constant (Pa)
     * @param density Mass density (kg/m³)
     * @param ms Saturation magnetization (A/m)
     * @param aEx Exchange stiffness (J/m)
     * @param numModes Number of k-space modes to simulate
     */
    public MagnonPhononCoupling(Real b1, Real b2, Real c11, Real c44, 
                                 Real density, Real ms, Real aEx, int numModes) {
        this.magnetoelasticB1 = b1;
        this.magnetoelasticB2 = b2;
        this.elasticC11 = c11;
        this.elasticC44 = c44;
        this.massDensity = density;
        this.saturationMag = ms;
        this.exchangeStiffness = aEx;
        this.numModes = numModes;
        
        this.magnonAmplitudes = new double[numModes];
        this.phononAmplitudes = new double[numModes];
        this.magnonFrequencies = new double[numModes];
        this.phononFrequencies = new double[numModes];
        this.couplingMatrix = new double[numModes][numModes];
        
        initializeDispersions();
        initializeCoupling();
    }

    /**
     * Initializes magnon and phonon dispersion relations.
     */
    private void initializeDispersions() {
        double kMax = 1e9; // Maximum wavevector (1/m)
        double dk = kMax / numModes;
        
        double msVal = saturationMag.doubleValue();
        double aExVal = exchangeStiffness.doubleValue();
        double c11Val = elasticC11.doubleValue();
        double rhoVal = massDensity.doubleValue();
        
        for (int i = 0; i < numModes; i++) {
            double k = (i + 1) * dk;
            
            // Magnon dispersion: ω_m = γ(H_eff + D*k²/Ms)
            // Simplified: ω_m ≈ γ * 2A_ex * k² / (μ0 * Ms)
            double omegaM = GAMMA * 2 * aExVal * k * k / (4 * Math.PI * 1e-7 * msVal);
            magnonFrequencies[i] = omegaM;
            
            // Phonon dispersion: ω_p = v_s * k (acoustic)
            double vs = Math.sqrt(c11Val / rhoVal);
            phononFrequencies[i] = vs * k;
        }
    }

    /**
     * Initializes the magnon-phonon coupling matrix.
     */
    private void initializeCoupling() {
        double b1Val = magnetoelasticB1.doubleValue();
        double msVal = saturationMag.doubleValue();
        
        for (int i = 0; i < numModes; i++) {
            for (int j = 0; j < numModes; j++) {
                // Coupling strength ~ B1 * k / Ms
                double kAvg = (magnonFrequencies[i] + phononFrequencies[j]) / 2e9;
                couplingMatrix[i][j] = Math.abs(b1Val * kAvg / msVal) * 1e-12;
            }
        }
    }

    /**
     * Excites magnon modes with a delta function pulse.
     * 
     * @param centerMode Mode index to excite
     * @param amplitude Excitation amplitude
     * @param width Gaussian width
     */
    public void exciteMagnons(int centerMode, double amplitude, double width) {
        for (int i = 0; i < numModes; i++) {
            double dist = (i - centerMode) / width;
            magnonAmplitudes[i] += amplitude * Math.exp(-dist * dist / 2);
        }
    }

    /**
     * Excites phonon modes (e.g., via SAW transducer).
     * 
     * @param frequency SAW frequency (Hz)
     * @param amplitude Displacement amplitude
     */
    public void excitePhonons(double frequency, double amplitude) {
        for (int i = 0; i < numModes; i++) {
            double deltaf = Math.abs(phononFrequencies[i] - frequency);
            double bandwidth = frequency * 0.1; // 10% bandwidth
            if (deltaf < bandwidth) {
                phononAmplitudes[i] += amplitude * (1 - deltaf / bandwidth);
            }
        }
    }

    /**
     * Evolves the coupled magnon-phonon system for one time step.
     * Uses coupled oscillator equations with damping.
     * 
     * @param dt Time step (s)
     * @param magnonDamping Magnon Gilbert damping parameter
     * @param phononDamping Phonon damping rate (1/s)
     */
    public void evolve(Real dt, Real magnonDamping, Real phononDamping) {
        double deltaT = dt.doubleValue();
        double alphaM = magnonDamping.doubleValue();
        double gammaP = phononDamping.doubleValue();
        
        double[] newMagnon = new double[numModes];
        double[] newPhonon = new double[numModes];
        
        for (int i = 0; i < numModes; i++) {
            // Magnon evolution with coupling
            double dMdt = -alphaM * magnonFrequencies[i] * magnonAmplitudes[i];
            for (int j = 0; j < numModes; j++) {
                dMdt += couplingMatrix[i][j] * phononAmplitudes[j] * Math.sin(phononFrequencies[j] * deltaT);
            }
            newMagnon[i] = magnonAmplitudes[i] + dMdt * deltaT;
            
            // Phonon evolution with coupling
            double dPdt = -gammaP * phononAmplitudes[i];
            for (int j = 0; j < numModes; j++) {
                dPdt += couplingMatrix[j][i] * magnonAmplitudes[j] * Math.sin(magnonFrequencies[j] * deltaT);
            }
            newPhonon[i] = phononAmplitudes[i] + dPdt * deltaT;
        }
        
        System.arraycopy(newMagnon, 0, magnonAmplitudes, 0, numModes);
        System.arraycopy(newPhonon, 0, phononAmplitudes, 0, numModes);
    }

    /**
     * Calculates total magnon energy in the system.
     * 
     * @return Magnon energy (J)
     */
    public Real calculateMagnonEnergy() {
        double energy = 0;
        for (int i = 0; i < numModes; i++) {
            energy += HBAR * magnonFrequencies[i] * magnonAmplitudes[i] * magnonAmplitudes[i];
        }
        return Real.of(energy);
    }

    /**
     * Calculates total phonon energy in the system.
     * 
     * @return Phonon energy (J)
     */
    public Real calculatePhononEnergy() {
        double energy = 0;
        for (int i = 0; i < numModes; i++) {
            energy += HBAR * phononFrequencies[i] * phononAmplitudes[i] * phononAmplitudes[i];
        }
        return Real.of(energy);
    }

    /**
     * Finds anticrossing (level repulsion) between magnon and phonon modes.
     * 
     * @return List of (mode index, coupling gap) pairs
     */
    public List<double[]> findAnticrossings() {
        List<double[]> crossings = new ArrayList<>();
        
        for (int i = 0; i < numModes; i++) {
            double deltaOmega = Math.abs(magnonFrequencies[i] - phononFrequencies[i]);
            double coupling = couplingMatrix[i][i];
            
            // Strong coupling: g > (γ_m + γ_p)/2
            if (coupling > deltaOmega * 0.1) {
                double gap = 2 * coupling; // Anticrossing gap
                crossings.add(new double[]{i, gap});
            }
        }
        
        return crossings;
    }

    /**
     * Calculates the magnon-polaron hybridization at a specific mode.
     * 
     * @param modeIndex Mode index
     * @return Hybridization coefficient (0 = pure magnon, 1 = pure phonon, 0.5 = maximum hybridization)
     */
    public Real calculateHybridization(int modeIndex) {
        if (modeIndex < 0 || modeIndex >= numModes) return Real.ZERO;
        
        double omegaM = magnonFrequencies[modeIndex];
        double omegaP = phononFrequencies[modeIndex];
        double g = couplingMatrix[modeIndex][modeIndex];
        
        double delta = omegaM - omegaP;
        double mixing = g / Math.sqrt(delta * delta + g * g);
        
        return Real.of(0.5 * (1 - delta / Math.sqrt(delta * delta + g * g)));
    }

    /**
     * Simulates SAW-driven ferromagnetic resonance.
     * 
     * @param sawFrequency SAW driving frequency (Hz)
     * @param sawAmplitude SAW strain amplitude
     * @param duration Simulation duration (s)
     * @param dt Time step (s)
     * @return Time series of absorbed power
     */
    public List<Real> simulateSAWFMR(double sawFrequency, double sawAmplitude, 
                                      Real duration, Real dt) {
        List<Real> powerAbsorbed = new ArrayList<>();
        double time = 0;
        double durVal = duration.doubleValue();
        double deltaT = dt.doubleValue();
        
        // Reset state
        java.util.Arrays.fill(magnonAmplitudes, 0);
        java.util.Arrays.fill(phononAmplitudes, 0);
        
        while (time < durVal) {
            // Drive phonons at SAW frequency
            excitePhonons(sawFrequency, sawAmplitude * Math.sin(2 * Math.PI * sawFrequency * time));
            
            // Evolve system
            evolve(dt, Real.of(0.01), Real.of(1e8));
            
            // Calculate power absorbed by magnons
            Real power = calculateMagnonEnergy().divide(dt);
            powerAbsorbed.add(power);
            
            time += deltaT;
        }
        
        return powerAbsorbed;
    }

    // Getters
    public double[] getMagnonAmplitudes() { return magnonAmplitudes.clone(); }
    public double[] getPhononAmplitudes() { return phononAmplitudes.clone(); }
    public double[] getMagnonFrequencies() { return magnonFrequencies.clone(); }
    public double[] getPhononFrequencies() { return phononFrequencies.clone(); }

    // Factory methods
    
    /**
     * Creates a YIG (Yttrium Iron Garnet) magnon-phonon system.
     * YIG has exceptionally low magnon damping.
     */
    public static MagnonPhononCoupling createYIG(int numModes) {
        return new MagnonPhononCoupling(
            Real.of(6.96e6),    // B1 for YIG (J/m³)
            Real.of(6.96e6),    // B2 ≈ B1
            Real.of(2.69e11),   // C11 (Pa)
            Real.of(0.764e11),  // C44 (Pa)
            Real.of(5170),      // Density (kg/m³)
            Real.of(1.4e5),     // Ms (A/m)
            Real.of(3.65e-12),  // Exchange stiffness (J/m)
            numModes
        );
    }

    /**
     * Creates a Permalloy (Ni80Fe20) magnon-phonon system.
     */
    public static MagnonPhononCoupling createPermalloy(int numModes) {
        return new MagnonPhononCoupling(
            Real.of(-2.1e7),    // B1 for Permalloy
            Real.of(-2.1e7),    // B2
            Real.of(2.5e11),    // C11
            Real.of(0.8e11),    // C44
            Real.of(8600),      // Density
            Real.of(8e5),       // Ms
            Real.of(1.05e-11),  // Exchange stiffness
            numModes
        );
    }

    /**
     * Creates a Cobalt magnon-phonon system.
     */
    public static MagnonPhononCoupling createCobalt(int numModes) {
        return new MagnonPhononCoupling(
            Real.of(-8.1e7),    // B1 for Co
            Real.of(-8.1e7),    // B2
            Real.of(3.07e11),   // C11
            Real.of(0.76e11),   // C44
            Real.of(8900),      // Density
            Real.of(1.4e6),     // Ms
            Real.of(3e-11),     // Exchange stiffness
            numModes
        );
    }
}
