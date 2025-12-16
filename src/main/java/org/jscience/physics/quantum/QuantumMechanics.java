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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.physics.quantum;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.PhysicalConstants;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Energy;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Velocity;

/**
 * Quantum mechanics equations and principles.
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class QuantumMechanics {

    /**
     * Heisenberg uncertainty principle: Δx × Δp ≥ ℏ/2
     */
    /**
     * Heisenberg uncertainty principle: Δx × Δp ≥ ℏ/2
     */
    public static Real heisenbergUncertainty() {
        return PhysicalConstants.REDUCED_PLANCK.getValue().divide(Real.TWO);
    }

    /**
     * De Broglie wavelength: λ = h/p
     */
    public static Real deBroglieWavelength(Real momentum) {
        return PhysicalConstants.PLANCK_CONSTANT.getValue().divide(momentum);
    }

    /**
     * Energy levels of hydrogen atom: E_n = -13.6 eV / n²
     */
    public static Real hydrogenEnergyLevel(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be positive");
        return Real.of(-13.6 / (n * n));
    }

    /**
     * Rydberg formula for hydrogen spectral lines: 1/λ = R(1/n₁² - 1/n₂²)
     */
    public static Real rydbergWavelength(int n1, int n2) {
        if (n1 >= n2)
            throw new IllegalArgumentException("n1 must be < n2");
        Real R = PhysicalConstants.RYDBERG_CONSTANT.getValue();
        Real term1 = Real.ONE.divide(Real.of(n1 * n1));
        Real term2 = Real.ONE.divide(Real.of(n2 * n2));
        return Real.ONE.divide(R.multiply(term1.subtract(term2)));
    }

    /**
     * Compton wavelength shift: Δλ = (h/mc)(1 - cos θ)
     */
    public static Real comptonShift(Real scatteringAngle) {
        Real h = PhysicalConstants.PLANCK_CONSTANT.getValue();
        Real me = PhysicalConstants.ELECTRON_MASS.getValue();
        Real c = PhysicalConstants.SPEED_OF_LIGHT.getValue();

        Real comptonWavelength = h.divide(me.multiply(c));
        Real factor = Real.ONE.subtract(Real.of(Math.cos(scatteringAngle.doubleValue())));

        return comptonWavelength.multiply(factor);
    }

    /**
     * Fine structure constant: α = e²/(4πε₀ℏc) ≈ 1/137
     */
    public static Real fineStructureConstant() {
        return PhysicalConstants.FINE_STRUCTURE.getValue();
    }

    /**
     * Bohr radius: a₀ = 4πε₀ℏ²/(mₑe²) ≈ 0.529 Å
     */
    public static Real bohrRadius() {
        Real epsilon0 = PhysicalConstants.ELECTRIC_CONSTANT.getValue();
        Real hbar = PhysicalConstants.REDUCED_PLANCK.getValue();
        Real me = PhysicalConstants.ELECTRON_MASS.getValue();
        Real e = PhysicalConstants.ELEMENTARY_CHARGE.getValue();

        Real numerator = Real.of(4 * Math.PI).multiply(epsilon0).multiply(hbar).multiply(hbar);
        Real denominator = me.multiply(e).multiply(e);

        return numerator.divide(denominator);
    }

    /**
     * Calculates the energy of a photon given its frequency.
     * E = h * f
     * 
     * @param f frequency
     * @return Energy
     */
    public static Quantity<Energy> photonEnergy(Quantity<Frequency> f) {
        double h = PhysicalConstants.PLANCK_CONSTANT.getValue().doubleValue(); // Joule seconds
        double fVal = f.to(Units.HERTZ).getValue().doubleValue();

        return Quantities.create(h * fVal, Units.JOULE);
    }

    /**
     * Calculates de Broglie wavelength using Quantities.
     * lambda = h / p = h / (m * v)
     * 
     * @param m mass
     * @param v velocity
     * @return Wavelength (Length)
     */
    public static Quantity<Length> deBroglieWavelength(Quantity<Mass> m, Quantity<Velocity> v) {
        double h = PhysicalConstants.PLANCK_CONSTANT.getValue().doubleValue();
        double mVal = m.to(Units.KILOGRAM).getValue().doubleValue();
        double vVal = v.to(Units.METER_PER_SECOND).getValue().doubleValue();

        // p = m * v
        double p = mVal * vVal;

        if (p == 0)
            throw new ArithmeticException("Momentum cannot be zero (wavelength undefined)");

        return Quantities.create(h / p, Units.METER);
    }
}
