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
 * @author Gemini AI
 * @since 2.0
 */
public class RadioactiveDecay {

    /** Avogadro's number */
    public static final Real AVOGADRO = Real.of("6.02214076e23");

    public enum DecayType {
        ALPHA, // Emits He-4 nucleus
        BETA_MINUS, // n -> p + e- + ν̄e
        BETA_PLUS, // p -> n + e+ + νe
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
     * Decay constant: λ = ln(2) / t½
     */
    public Real getDecayConstant() {
        return Real.of(2.0).log().divide(halfLife);
    }

    /**
     * Mean lifetime: τ = 1/λ
     */
    public Real getMeanLifetime() {
        return Real.ONE.divide(getDecayConstant());
    }

    /**
     * Remaining nuclei: N(t) = N₀ e^(-λt)
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
     * Activity: A = λN (decays per second)
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
        return Quantities.create(seconds.doubleValue(), Units.SECOND);
    }

    // --- Dating Methods ---

    /**
     * Carbon-14 dating: estimate age from remaining C-14 ratio.
     * 
     * @param ratioToModern N/N₀ (ratio of current to modern C-14)
     * @return age in years
     */
    public static Real carbonDate(Real ratioToModern) {
        // Half-life 5730 years
        Real halfLifeSeconds = Real.of(5730 * 365.25 * 24 * 3600);
        Real lambda = Real.of(2.0).log().divide(halfLifeSeconds);
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
        Real lambda = Real.of(2.0).log().divide(halfLifeSeconds);
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
        return Quantities.create(halfLife.doubleValue(), Units.SECOND);
    }

    @Override
    public String toString() {
        double years = halfLife.doubleValue() / (365.25 * 24 * 3600);
        if (years > 1) {
            return String.format("RadioactiveDecay{%s, t½=%.3g years}", type, years);
        } else {
            double days = halfLife.doubleValue() / (24 * 3600);
            return String.format("RadioactiveDecay{%s, t½=%.3g days}", type, days);
        }
    }
}
