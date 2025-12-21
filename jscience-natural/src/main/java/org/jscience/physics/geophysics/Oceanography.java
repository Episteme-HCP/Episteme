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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.physics.geophysics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.physics.PhysicalConstants;

/**
 * Oceanography calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Oceanography {

    /** Standard density of seawater (kg/m³) */
    public static final Real RHO_SEAWATER = Real.of(1025);

    /** Use standard gravitational acceleration from PhysicalConstants */
    private static final Real G = PhysicalConstants.g_n;

    /**
     * Deep water wave speed.
     * c = √(gL / 2π) = gT / 2π
     */
    public static Real deepWaterWaveSpeed(Real wavelength) {
        return G.multiply(wavelength).divide(Real.TWO_PI).sqrt();
    }

    /**
     * Deep water wave speed from period.
     */
    public static Real deepWaterWaveSpeedFromPeriod(Real period) {
        return G.multiply(period).divide(Real.TWO_PI);
    }

    /**
     * Shallow water wave speed.
     * c = √(gh)
     */
    public static Real shallowWaterWaveSpeed(Real depth) {
        return G.multiply(depth).sqrt();
    }

    /**
     * General dispersion relation (solved iteratively).
     * ω² = gk * tanh(kh)
     */
    public static Real waveNumber(Real period, Real depth) {
        Real omega = Real.TWO_PI.divide(period);
        Real k = omega.pow(2).divide(G); // Initial guess (deep water)

        for (int i = 0; i < 20; i++) {
            Real kh = k.multiply(depth);
            Real tanhKh = kh.tanh();
            Real coshKh = kh.cosh();

            Real f = omega.pow(2).subtract(G.multiply(k).multiply(tanhKh));
            // df/dk = -G * (tanh(kh) + kh * sech^2(kh))
            // sech^2(x) = 1/cosh^2(x)
            Real sechSq = Real.ONE.divide(coshKh.pow(2));
            Real term = tanhKh.add(kh.multiply(sechSq));
            Real df = G.negate().multiply(term);

            k = k.subtract(f.divide(df));
        }

        return k;
    }

    /**
     * Significant wave height from wind (simplified).
     * H_s ≈ 0.0246 * U² (Fully developed sea)
     */
    public static Real significantWaveHeight(Real windSpeed) {
        return Real.of(0.0246).multiply(windSpeed.pow(2));
    }

    /**
     * Tidal constituent prediction.
     * η(t) = Σ A_i * cos(ω_i * t - φ_i)
     */
    public static Real tidalHeight(Real time, Vector<Real> amplitudes,
            Vector<Real> frequencies, Vector<Real> phases) {
        Real height = Real.ZERO;
        for (int i = 0; i < amplitudes.dimension(); i++) {
            Real phaseArg = frequencies.get(i).multiply(time).subtract(phases.get(i));
            height = height.add(amplitudes.get(i).multiply(phaseArg.cos()));
        }
        return height;
    }

    /**
     * M2 tidal constituent frequency (rad/s).
     * Period ≈ 12.42 hours
     */
    public static final Real OMEGA_M2 = Real.TWO_PI.divide(Real.of(12.42 * 3600));

    /**
     * S2 tidal constituent frequency.
     * Period = 12.00 hours
     */
    public static final Real OMEGA_S2 = Real.TWO_PI.divide(Real.of(12.00 * 3600));

    /**
     * Density of seawater from temperature and salinity (UNESCO 1983).
     */
    public static Real seawaterDensity(Real temperature, Real salinity) {
        Real rho0 = Real.of(1028.1);
        Real alpha = Real.of(0.00015); // Thermal expansion (1/°C)
        Real beta = Real.of(0.00078); // Haline contraction (1/PSU)

        // rho0 * (1 - alpha * (temperature - 10) + beta * (salinity - 35))
        return rho0.multiply(Real.ONE.subtract(alpha.multiply(temperature.subtract(Real.of(10))))
                .add(beta.multiply(salinity.subtract(Real.of(35)))));
    }

    /**
     * Speed of sound in seawater (Mackenzie equation).
     */
    public static Real soundSpeedSeawater(Real temperature, Real salinity, Real depth) {
        Real T = temperature;
        Real S = salinity;
        Real D = depth;

        return Real.of(1448.96)
                .add(Real.of(4.591).multiply(T))
                .subtract(Real.of(0.05304).multiply(T.pow(2)))
                .add(Real.of(0.0002374).multiply(T.pow(3)))
                .add(Real.of(1.340).multiply(S.subtract(Real.of(35))))
                .add(Real.of(0.0163).multiply(D))
                .add(Real.of(1.675e-7).multiply(D.pow(2)))
                .subtract(Real.of(0.01025).multiply(T).multiply(S.subtract(Real.of(35))))
                .subtract(Real.of(7.139e-13).multiply(T).multiply(D.pow(3)));
    }

    /**
     * Mixed layer depth estimate from wind stress.
     * h_ml ≈ (2 * τ * t / ρ)^(1/2) (simplified Kraus-Turner)
     */
    public static Real mixedLayerDepth(Real windStress, Real time, Real density) {
        return Real.TWO.multiply(windStress).multiply(time).divide(density).sqrt();
    }

    /**
     * Ekman transport.
     * M_E = τ / (ρ * f) where f = 2Ω sin(φ)
     */
    public static Real ekmanTransport(Real windStress, Real latitude) {
        Real omega = Real.of(7.292e-5); // Earth's rotation rate
        Real f = Real.TWO.multiply(omega).multiply(latitude.toRadians().sin());
        return windStress.divide(RHO_SEAWATER.multiply(f.abs()));
    }

    /**
     * Brunt-Väisälä (buoyancy) frequency.
     * N² = -(g/ρ) * dρ/dz
     */
    public static Real bruntVaisalaFrequency(Real densityGradient, Real meanDensity) {
        Real N2 = G.negate().divide(meanDensity).multiply(densityGradient);
        return (N2.compareTo(Real.ZERO) > 0) ? N2.sqrt() : Real.ZERO;
    }
}
