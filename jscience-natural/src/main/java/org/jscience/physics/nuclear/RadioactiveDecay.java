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

package org.jscience.physics.nuclear;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Time;

/**
 * Models radioactive decay processes.
 * <p>
 * Provides:
 * <ul>
 * <li>Exponential decay law</li>
 * <li>Half-life and activity calculations</li>
 * <li>Carbon-14 and Uranium-Lead dating</li>
 * <li>Common isotope presets</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RadioactiveDecay {

    /** Avogadro's number */
    public static final Real AVOGADRO = Real.of("6.02214076e23");

    public enum DecayType {
        ALPHA, // Emits He-4 nucleus
        BETA_MINUS, // n -> p + e- + ÃŽÂ½ÃŒâ€že
        BETA_PLUS, // p -> n + e+ + ÃŽÂ½e
        GAMMA, // Photon emission
        ELECTRON_CAPTURE
    }

    private final Real halfLife; // seconds
    private final DecayType type;
    private final Real qValue; // MeV (energy released)

    public RadioactiveDecay(Real halfLife, DecayType type, Real qValue) {
        this.halfLife = halfLife;
        this.type = type;
        this.qValue = qValue;
    }

    /**
     * Decay constant: ÃŽÂ» = ln(2) / tÃ‚Â½
     */
    public Real getDecayConstant() {
        return Real.LN2.divide(halfLife);
    }

    /**
     * Mean lifetime: Ãâ€ž = 1/ÃŽÂ»
     */
    public Real getMeanLifetime() {
        return Real.ONE.divide(getDecayConstant());
    }

    /**
     * Remaining nuclei: N(t) = NÃ¢â€šâ‚¬ e^(-ÃŽÂ»t)
     */
    public Real remainingNuclei(Real initialCount, Real time) {
        Real exponent = getDecayConstant().negate().multiply(time);
        return initialCount.multiply(exponent.exp());
    }

    /**
     * Fraction remaining after time t.
     */
    public Real fractionRemaining(Real timeSeconds) {
        Real lambda = getDecayConstant();
        return lambda.negate().multiply(timeSeconds).exp();
    }

    /**
     * Activity: A = ÃŽÂ»N (decays per second)
     */
    public Real activity(Real nucleiCount) {
        return getDecayConstant().multiply(nucleiCount);
    }

    /**
     * Activity from mass of pure isotope.
     * 
     * @param massGrams  mass in grams
     * @param atomicMass atomic mass (g/mol)
     * @return activity in Bq
     */
    public Real activityFromMass(Real massGrams, Real atomicMass) {
        Real moles = massGrams.divide(atomicMass);
        Real atoms = moles.multiply(AVOGADRO);
        return getDecayConstant().multiply(atoms);
    }

    /**
     * Time for specific fraction to remain.
     */
    public Quantity<Time> timeForFraction(Real fraction) {
        Real lambda = getDecayConstant();
        // t = -ln(f) / lambda
        Real seconds = fraction.log().negate().divide(lambda);
        return Quantities.create(seconds, Units.SECOND);
    }

    // --- Dating Methods ---

    /**
     * Carbon-14 dating: estimate age from remaining C-14 ratio.
     * 
     * @param ratioToModern N/NÃ¢â€šâ‚¬ (ratio of current to modern C-14)
     * @return age in years
     */
    public static Real carbonDate(Real ratioToModern) {
        // Half-life 5730 years
        Real halfLifeSeconds = Real.of(5730 * 365.25 * 24 * 3600);
        Real lambda = Real.LN2.divide(halfLifeSeconds);
        Real ageSeconds = ratioToModern.log().negate().divide(lambda);
        return ageSeconds.divide(Real.of(365.25 * 24 * 3600)); // Convert to years
    }

    /**
     * Uranium-Lead dating using U-238 to Pb-206 ratio.
     */
    public static Real uraniumLeadDate(Real uraniumAtoms, Real leadAtoms) {
        Real n0 = uraniumAtoms.add(leadAtoms);
        Real ratio = uraniumAtoms.divide(n0);
        // Half-life 4.468 billion years
        Real halfLifeSeconds = Real.of(4.468e9 * 365.25 * 24 * 3600);
        Real lambda = Real.LN2.divide(halfLifeSeconds);
        Real ageSeconds = ratio.log().negate().divide(lambda);
        return ageSeconds.divide(Real.of(365.25 * 24 * 3600));
    }

    // --- Common Isotopes ---

    /** Carbon-14 beta decay: half-life 5730 years */
    public static final RadioactiveDecay CARBON_14 = new RadioactiveDecay(
            Real.of(5730 * 365.25 * 24 * 3600), DecayType.BETA_MINUS, Real.of(0.156));

    /** Uranium-238 alpha decay: half-life 4.468 billion years */
    public static final RadioactiveDecay URANIUM_238 = new RadioactiveDecay(
            Real.of(4.468e9 * 365.25 * 24 * 3600), DecayType.ALPHA, Real.of(4.27));

    /** Cobalt-60 beta decay: half-life 5.27 years */
    public static final RadioactiveDecay COBALT_60 = new RadioactiveDecay(
            Real.of(5.27 * 365.25 * 24 * 3600), DecayType.BETA_MINUS, Real.of(2.82));

    /** Potassium-40: half-life 1.248 billion years */
    public static final RadioactiveDecay POTASSIUM_40 = new RadioactiveDecay(
            Real.of(1.248e9 * 365.25 * 24 * 3600), DecayType.BETA_MINUS, Real.of(1.31));

    /** Iodine-131: half-life 8.02 days */
    public static final RadioactiveDecay IODINE_131 = new RadioactiveDecay(
            Real.of(8.02 * 24 * 3600), DecayType.BETA_MINUS, Real.of(0.97));

    /** Radon-222: half-life 3.82 days */
    public static final RadioactiveDecay RADON_222 = new RadioactiveDecay(
            Real.of(3.82 * 24 * 3600), DecayType.ALPHA, Real.of(5.59));

    // --- Accessors ---

    public Real getHalfLife() {
        return halfLife;
    }

    public DecayType getType() {
        return type;
    }

    public Real getQValue() {
        return qValue;
    }

    public Quantity<Time> getHalfLifeQuantity() {
        return Quantities.create(halfLife, Units.SECOND);
    }

    @Override
    public String toString() {
        double years = halfLife.doubleValue() / (365.25 * 24 * 3600);
        if (years > 1) {
            return String.format("RadioactiveDecay{%s, tÃ‚Â½=%.3g years}", type, years);
        } else {
            double days = halfLife.doubleValue() / (24 * 3600);
            return String.format("RadioactiveDecay{%s, tÃ‚Â½=%.3g days}", type, days);
        }
    }
}


