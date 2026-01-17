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
 * <p>
 * <b>Reference:</b><br>
 * Dirac, P. A. M. (1930). <i>The Principles of Quantum Mechanics</i>. Oxford University Press.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class QuantumMechanics {

    /** Heisenberg uncertainty: ÃŽâ€x Ãƒâ€” ÃŽâ€p Ã¢â€°Â¥ Ã¢â€žÂ/2 */
    public static Real heisenbergUncertainty() {
        return PhysicalConstants.h_bar.divide(Real.TWO);
    }

    /** De Broglie wavelength: ÃŽÂ» = h/p */
    public static Real deBroglieWavelength(Real momentum) {
        return PhysicalConstants.h.divide(momentum);
    }

    /** Hydrogen atom energy levels: E_n = -13.6 eV / nÃ‚Â² */
    public static Real hydrogenEnergyLevel(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be positive");
        return Real.of(-13.6).divide(Real.of(n * n));
    }

    /** Rydberg formula: 1/ÃŽÂ» = R(1/nÃ¢â€šÂÃ‚Â² - 1/nÃ¢â€šâ€šÃ‚Â²) */
    public static Real rydbergWavelength(int n1, int n2) {
        if (n1 >= n2)
            throw new IllegalArgumentException("n1 must be < n2");
        Real R = PhysicalConstants.RYDBERG_CONSTANT.getValue();
        Real term1 = Real.ONE.divide(Real.of(n1 * n1));
        Real term2 = Real.ONE.divide(Real.of(n2 * n2));
        return Real.ONE.divide(R.multiply(term1.subtract(term2)));
    }

    /** Compton shift: ÃŽâ€ÃŽÂ» = (h/mc)(1 - cos ÃŽÂ¸) */
    public static Real comptonShift(Real scatteringAngle) {
        Real comptonWavelength = PhysicalConstants.h.divide(PhysicalConstants.m_e.multiply(PhysicalConstants.c));
        return comptonWavelength.multiply(Real.ONE.subtract(scatteringAngle.cos()));
    }

    /** Fine structure constant: ÃŽÂ± Ã¢â€°Ë† 1/137 */
    public static Real fineStructureConstant() {
        return PhysicalConstants.alpha;
    }

    /** Bohr radius: aÃ¢â€šâ‚¬ Ã¢â€°Ë† 0.529 Ãƒâ€¦ */
    public static Real bohrRadius() {
        Real numerator = Real.of(4).multiply(Real.PI).multiply(PhysicalConstants.epsilon_0)
                .multiply(PhysicalConstants.h_bar.pow(2));
        Real denominator = PhysicalConstants.m_e.multiply(PhysicalConstants.e.pow(2));
        return numerator.divide(denominator);
    }

    /** Photon energy: E = h * f */
    public static Quantity<Energy> photonEnergy(Quantity<Frequency> f) {
        Real h = PhysicalConstants.h;
        Real fVal = f.to(Units.HERTZ).getValue();
        return Quantities.create(h.multiply(fVal), Units.JOULE);
    }

    /** De Broglie wavelength: ÃŽÂ» = h / (m * v) */
    public static Quantity<Length> deBroglieWavelength(Quantity<Mass> m, Quantity<Velocity> v) {
        Real h = PhysicalConstants.h;
        Real mVal = m.to(Units.KILOGRAM).getValue();
        Real vVal = v.to(Units.METER_PER_SECOND).getValue();
        Real p = mVal.multiply(vVal);
        if (p.compareTo(Real.ZERO) == 0) {
            throw new ArithmeticException("Momentum cannot be zero");
        }
        return Quantities.create(h.divide(p), Units.METER);
    }
}


