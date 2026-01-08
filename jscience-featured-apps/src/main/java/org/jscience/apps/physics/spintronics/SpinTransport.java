/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.PhysicalConstants;

/**
 * Advanced Spin Transport and Spin Transfer Torque (STT) calculations.
 * <p>
 * This class implements the Landau-Lifshitz-Gilbert (LLG) equation augmented
 * with
 * Spin Transfer Torque (STT) and stochastic thermal noise terms.
 * </p>
 * 
 * <h3>The LLG-STT Equation</h3>
 * <p>
 * $$ \frac{d\mathbf{m}}{dt} = -\gamma \mathbf{m} \times \mathbf{H}_{eff} +
 * \alpha \mathbf{m} \times \frac{d\mathbf{m}}{dt} + \mathbf{\tau}_{STT} $$
 * </p>
 * 
 * <h3>References</h3>
 * <ul>
 * <li><b>Slonczewski, J. C.</b> (1996). "Current-driven excitation of magnetic
 * multilayers".
 * <i>Journal of Magnetism and Magnetic Materials</i>, 159(1-2), L1-L7.
 * <a href="https://doi.org/10.1016/0304-8853(96)00062-5">DOI:
 * 10.1016/0304-8853(96)00062-5</a></li>
 * <li><b>Berger, L.</b> (1996). "Emission of spin waves by a magnetic
 * multilayer traversed by a current".
 * <i>Physical Review B</i>, 54(13), 9353.
 * <a href="https://doi.org/10.1103/PhysRevB.54.9353">DOI:
 * 10.1103/PhysRevB.54.9353</a></li>
 * <li><b>Brown, W. F.</b> (1963). "Thermal Fluctuations of a Single-Domain
 * Particle".
 * <i>Physical Review</i>, 130(5), 1677.
 * <a href="https://doi.org/10.1103/PhysRev.130.1677">DOI:
 * 10.1103/PhysRev.130.1677</a></li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class SpinTransport {

    private static final java.util.Random random = new java.util.Random();

    /**
     * Calculates the adiabatic Spin Transfer Torque (STT) vector.
     * $$ \tau_{STT} = \frac{\hbar}{2e} \eta J (\mathbf{m}_{free} \times
     * (\mathbf{m}_{fixed} \times \mathbf{m}_{free})) $$
     * 
     * @param currentDensity Current density J (A/m²)
     * @param layerFree      Free layer
     * @param layerPinned    Pinned layer
     * @return Torque vector components (N/m³ or equivalent units depending on
     *         normalization)
     */
    public static Real[] calculateSTT(Real currentDensity, FerromagneticLayer layerFree,
            FerromagneticLayer layerPinned) {
        Real hbar = PhysicalConstants.h_bar;
        Real e = PhysicalConstants.e;
        Real eta = layerPinned.getMaterial().getSpinPolarization(); // Efficiency factor loosely based on polarization

        // Factor = (hbar / 2e) * eta * J
        Real factor = hbar.divide(Real.TWO.multiply(e)).multiply(eta).multiply(currentDensity);

        Real[] mF = layerFree.getMagnetization();
        Real[] mP = layerPinned.getMagnetization();

        // m_pinned x m_free
        Real[] cross1 = crossProduct(mP, mF);
        // m_free x (mP x mF)
        Real[] cross2 = crossProduct(mF, cross1);

        return new Real[] {
                cross2[0].multiply(factor),
                cross2[1].multiply(factor),
                cross2[2].multiply(factor)
        };
    }

    /**
     * Spin accumulation at the interface.
     * $$ \Delta\mu = 2 P \rho \lambda_{sf} J $$
     */
    public static Real spinAccumulation(Real currentDensity, SpintronicMaterial material) {
        return Real.TWO.multiply(material.getSpinPolarization())
                .multiply(material.getResistivity())
                .multiply(material.getSpinDiffusionLength())
                .multiply(currentDensity);
    }

    /**
     * Calculates a single time step of magnetodynamics using the Stochastic
     * Landau-Lifshitz-Gilbert equation.
     * Includes thermal fluctuations (Brownian motion of the magnetization vector).
     * 
     * @param layer       The ferromagnetic layer to update
     * @param hEff        The deterministic effective field (Anisotropy + Zeeman +
     *                    Demag)
     * @param dt          Time step (s)
     * @param alpha       Gilbert damping parameter (dimensionless)
     * @param gamma       Gyromagnetic ratio (rad/s/T)
     * @param temperature Temperature (K) for noise calculation
     * @param volume      Volume of the magnetic element (m³)
     * @param ms          Saturation magnetization (A/m)
     * @return The new normalized magnetization vector M(t+dt)
     */
    public static Real[] stepLLGWithThermalNoise(FerromagneticLayer layer, Real[] hEff, Real dt, Real alpha, Real gamma,
            Real temperature, Real volume, Real ms) {
        Real[] m = layer.getMagnetization();

        // Thermal Field H_th calculation (White Gaussian Noise)
        Real[] hTh = { Real.ZERO, Real.ZERO, Real.ZERO };

        if (temperature.compareTo(Real.ZERO) > 0) {
            // Variance of the field distribution:
            // sigma = sqrt( (2 * alpha * kB * T) / (gamma * Ms * V * dt) )
            double kB = 1.380649e-23; // Boltzmann constant
            double num = 2.0 * alpha.doubleValue() * kB * temperature.doubleValue();
            double den = gamma.doubleValue() * ms.doubleValue() * volume.doubleValue() * dt.doubleValue();

            // Safety check for density
            if (den > 1e-30) {
                double sigma = Math.sqrt(num / den);
                hTh[0] = Real.of(random.nextGaussian() * sigma);
                hTh[1] = Real.of(random.nextGaussian() * sigma);
                hTh[2] = Real.of(random.nextGaussian() * sigma);
            }
        }

        // Total effective field H_tot = H_eff + H_th
        Real[] hTotal = {
                hEff[0].add(hTh[0]),
                hEff[1].add(hTh[1]),
                hEff[2].add(hTh[2])
        };

        // LLG Integration (Simplified Euler Heun or similar could be better, using
        // simple Euler here for MVP)
        // dM/dt = -gamma/(1+alpha^2) * ( M x H_tot + alpha * M x (M x H_tot) )

        // 1. Precession term: M x H_tot
        Real[] precession = crossProduct(m, hTotal);

        // 2. Damping term: M x (M x H_tot)
        Real[] damping = crossProduct(m, precession);

        // Pre-factor: -gamma / (1 + alpha^2)
        // Note: gamma is usually given positive (1.76e11), but electron charge is
        // negative,
        // leading to precession direction. We use standard convention with negative
        // sign.
        Real preFactor = gamma.divide(Real.ONE.add(alpha.pow(2))).negate();

        Real[] dm = new Real[3];
        for (int i = 0; i < 3; i++) {
            Real change = precession[i].add(alpha.multiply(damping[i]));
            dm[i] = preFactor.multiply(change).multiply(dt);
        }

        // Calculate new M and Normalize
        Real mx = m[0].add(dm[0]);
        Real my = m[1].add(dm[1]);
        Real mz = m[2].add(dm[2]);
        Real norm = mx.pow(2).add(my.pow(2)).add(mz.pow(2)).sqrt();

        if (norm.compareTo(Real.ZERO) == 0)
            return m; // Should not happen

        return new Real[] {
                mx.divide(norm),
                my.divide(norm),
                mz.divide(norm)
        };
    }

    /** Backward compatibility wrapper */
    public static Real[] stepLLG(FerromagneticLayer layer, Real[] hEff, Real dt, Real alpha, Real gamma) {
        return stepLLGWithThermalNoise(layer, hEff, dt, alpha, gamma, Real.ZERO, Real.ONE, Real.ONE);
    }
    
    /**
     * Heun (RK2) integration for improved stability.
     * Predictor-Corrector scheme: more accurate than Euler, especially for oscillatory dynamics.
     */
    public static Real[] stepLLGHeun(FerromagneticLayer layer, Real[] hEff, Real dt, Real alpha, Real gamma) {
        Real[] m0 = layer.getMagnetization();
        
        // Predictor (Euler step)
        Real[] mPred = stepLLGEulerCore(m0, hEff, dt, alpha, gamma);
        
        // Corrector: evaluate derivative at predicted point
        FerromagneticLayer predLayer = new FerromagneticLayer(layer.getMaterial(), layer.getThickness(), layer.isPinned());
        predLayer.setMagnetization(mPred[0], mPred[1], mPred[2]);
        Real[] mCorr = stepLLGEulerCore(mPred, hEff, dt, alpha, gamma);
        
        // Average: m_new = m0 + dt/2 * (f(m0) + f(mPred))
        Real[] dm0 = dmdt(m0, hEff, alpha, gamma);
        Real[] dmPred = dmdt(mPred, hEff, alpha, gamma);
        
        Real mx = m0[0].add(dt.divide(Real.TWO).multiply(dm0[0].add(dmPred[0])));
        Real my = m0[1].add(dt.divide(Real.TWO).multiply(dm0[1].add(dmPred[1])));
        Real mz = m0[2].add(dt.divide(Real.TWO).multiply(dm0[2].add(dmPred[2])));
        
        // Normalize
        Real norm = mx.pow(2).add(my.pow(2)).add(mz.pow(2)).sqrt();
        return new Real[]{mx.divide(norm), my.divide(norm), mz.divide(norm)};
    }
    
    private static Real[] stepLLGEulerCore(Real[] m, Real[] hEff, Real dt, Real alpha, Real gamma) {
        Real[] dm = dmdt(m, hEff, alpha, gamma);
        Real mx = m[0].add(dm[0].multiply(dt));
        Real my = m[1].add(dm[1].multiply(dt));
        Real mz = m[2].add(dm[2].multiply(dt));
        Real norm = mx.pow(2).add(my.pow(2)).add(mz.pow(2)).sqrt();
        return new Real[]{mx.divide(norm), my.divide(norm), mz.divide(norm)};
    }
    
    private static Real[] dmdt(Real[] m, Real[] h, Real alpha, Real gamma) {
        Real[] prec = crossProduct(m, h);
        Real[] damp = crossProduct(m, prec);
        Real factor = gamma.divide(Real.ONE.add(alpha.pow(2))).negate();
        return new Real[]{
            factor.multiply(prec[0].add(alpha.multiply(damp[0]))),
            factor.multiply(prec[1].add(alpha.multiply(damp[1]))),
            factor.multiply(prec[2].add(alpha.multiply(damp[2])))
        };
    }

    private static Real[] crossProduct(Real[] a, Real[] b) {
        return new Real[] {
                a[1].multiply(b[2]).subtract(a[2].multiply(b[1])),
                a[2].multiply(b[0]).subtract(a[0].multiply(b[2])),
                a[0].multiply(b[1]).subtract(a[1].multiply(b[0]))
        };
    }
}
