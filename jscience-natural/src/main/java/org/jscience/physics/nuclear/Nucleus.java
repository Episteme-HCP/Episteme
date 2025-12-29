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

package org.jscience.physics.nuclear;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Energy;

/**
 * Represents an atomic nucleus with proton/neutron counts.
 * <p>
 * Provides nuclear physics calculations including binding energy,
 * nuclear radius, and stability assessment using proper Real arithmetic.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Nucleus {

    private final int protons; // Z (atomic number)
    private final int neutrons; // N
    private final String symbol;

    // Physical constants as Real (MeasuredQuantity approach)
    /** Proton mass in kg */
    public static final Real PROTON_MASS = Real.of(1.67262192369e-27);
    /** Neutron mass in kg */
    public static final Real NEUTRON_MASS = Real.of(1.67492749804e-27);
    /** Atomic mass unit in kg */
    public static final Real AMU = Real.of(1.66053906660e-27);
    /** Speed of light squared in m²/s² */
    public static final Real C_SQUARED = Real.of(299792458).pow(2);
    /** Nuclear radius constant r₀ in meters */
    public static final Real R0_METERS = Real.of(1.2e-15);

    // Weizsäcker formula coefficients in MeV
    private static final Real A_VOLUME = Real.of(15.8);
    private static final Real A_SURFACE = Real.of(18.3);
    private static final Real A_COULOMB = Real.of(0.72);
    private static final Real A_ASYMMETRY = Real.of(23.2);
    private static final Real A_PAIRING = Real.of(12.0);

    // Exponents as Real
    private static final Real TWO_THIRDS = Real.TWO.divide(Real.of(3));
    private static final Real ONE_THIRD = Real.ONE.divide(Real.of(3));

    public Nucleus(int protons, int neutrons, String symbol) {
        this.protons = protons;
        this.neutrons = neutrons;
        this.symbol = symbol;
    }

    /**
     * Mass number: A = Z + N
     */
    public int getMassNumber() {
        return protons + neutrons;
    }

    /**
     * Returns mass number as Real for calculations.
     */
    public Real getMassNumberReal() {
        return Real.of(getMassNumber());
    }

    /**
     * Returns proton count as Real for calculations.
     */
    public Real getProtonsReal() {
        return Real.of(protons);
    }

    /**
     * Returns neutron count as Real for calculations.
     */
    public Real getNeutronsReal() {
        return Real.of(neutrons);
    }

    /**
     * Approximate nuclear radius: R = r₀ A^(1/3) where r₀ ≈ 1.2 fm.
     *
     * @return nuclear radius as Length quantity
     */
    public Quantity<Length> getNuclearRadius() {
        Real radius = R0_METERS.multiply(getMassNumberReal().cbrt());
        return Quantities.create(radius, Units.METER);
    }

    /**
     * Nuclear radius as Real (meters).
     */
    public Real getNuclearRadiusReal() {
        return R0_METERS.multiply(getMassNumberReal().cbrt());
    }

    /**
     * Semi-empirical binding energy (Weizsäcker formula).
     * <p>
     * B = aᵥA - aₛA^(2/3) - aᴄZ(Z-1)/A^(1/3) - aₐ(N-Z)²/A + δ
     * </p>
     *
     * @return binding energy in MeV as Real
     */
    public Real getBindingEnergy() {
        Real A = getMassNumberReal();
        Real Z = getProtonsReal();
        Real N = getNeutronsReal();

        // Volume term
        Real volumeTerm = A_VOLUME.multiply(A);

        // Surface term
        Real surfaceTerm = A_SURFACE.multiply(A.pow(TWO_THIRDS));

        // Coulomb term
        Real coulombTerm = A_COULOMB.multiply(Z).multiply(Z.subtract(Real.ONE))
                .divide(A.pow(ONE_THIRD));

        // Asymmetry term
        Real asymmetryTerm = A_ASYMMETRY.multiply(N.subtract(Z).pow(2)).divide(A);

        // Pairing term
        Real delta;
        if (protons % 2 == 0 && neutrons % 2 == 0) {
            // Even-even: more stable
            delta = A_PAIRING.divide(A.sqrt());
        } else if (protons % 2 == 1 && neutrons % 2 == 1) {
            // Odd-odd: less stable
            delta = A_PAIRING.divide(A.sqrt()).negate();
        } else {
            // Even-odd or odd-even
            delta = Real.ZERO;
        }

        return volumeTerm.subtract(surfaceTerm).subtract(coulombTerm)
                .subtract(asymmetryTerm).add(delta);
    }

    /**
     * Binding energy as Energy quantity.
     */
    public Quantity<Energy> getBindingEnergyQuantity() {
        // 1 MeV = 1.60218e-13 J
        Real joules = getBindingEnergy().multiply(Real.of(1.60218e-13));
        return Quantities.create(joules, Units.JOULE);
    }

    /**
     * Binding energy per nucleon: B/A in MeV.
     */
    public Real getBindingEnergyPerNucleon() {
        return getBindingEnergy().divide(getMassNumberReal());
    }

    /**
     * Checks if this nucleus is stable based on neutron-proton ratio.
     */
    public boolean isLikelyStable() {
        Real npRatio = getNeutronsReal().divide(getProtonsReal());
        // Stable nuclei: N/Z ≈ 1 for light, increases for heavy
        Real idealRatio = Real.ONE.add(Real.of(0.015).multiply(getMassNumberReal()));
        Real deviation = npRatio.subtract(idealRatio).abs();
        return deviation.compareTo(Real.of(0.3)) < 0;
    }

    // --- Common isotopes ---
    public static final Nucleus HYDROGEN_1 = new Nucleus(1, 0, "H");
    public static final Nucleus DEUTERIUM = new Nucleus(1, 1, "D");
    public static final Nucleus TRITIUM = new Nucleus(1, 2, "T");
    public static final Nucleus HELIUM_4 = new Nucleus(2, 2, "He");
    public static final Nucleus CARBON_12 = new Nucleus(6, 6, "C");
    public static final Nucleus IRON_56 = new Nucleus(26, 30, "Fe");
    public static final Nucleus URANIUM_235 = new Nucleus(92, 143, "U");
    public static final Nucleus URANIUM_238 = new Nucleus(92, 146, "U");

    // --- Accessors ---
    public int getProtons() {
        return protons;
    }

    public int getNeutrons() {
        return neutrons;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol + "-" + getMassNumber();
    }
}
