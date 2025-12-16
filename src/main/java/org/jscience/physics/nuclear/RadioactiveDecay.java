package org.jscience.physics.nuclear;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models radioactive decay processes.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class RadioactiveDecay {

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
     * Decay constant: $\lambda = \ln(2) / t_{1/2}$
     */
    public Real getDecayConstant() {
        return Real.of(Math.log(2)).divide(halfLife);
    }

    /**
     * Mean lifetime: $\tau = 1/\lambda$
     */
    public Real getMeanLifetime() {
        return Real.ONE.divide(getDecayConstant());
    }

    /**
     * Remaining nuclei: $N(t) = N_0 e^{-\lambda t}$
     */
    public Real remainingNuclei(Real initialCount, Real time) {
        Real exponent = getDecayConstant().negate().multiply(time);
        return initialCount.multiply(exponent.exp());
    }

    /**
     * Activity: $A = \lambda N$
     */
    public Real activity(Real nucleiCount) {
        return getDecayConstant().multiply(nucleiCount);
    }

    // --- Common decays ---

    /** Carbon-14 beta decay: half-life 5730 years */
    public static final RadioactiveDecay CARBON_14 = new RadioactiveDecay(
            Real.of(5730 * 365.25 * 24 * 3600), DecayType.BETA_MINUS, Real.of(0.156));

    /** Uranium-238 alpha decay: half-life 4.468 billion years */
    public static final RadioactiveDecay URANIUM_238 = new RadioactiveDecay(
            Real.of(4.468e9 * 365.25 * 24 * 3600), DecayType.ALPHA, Real.of(4.27));

    /** Cobalt-60 beta decay: half-life 5.27 years */
    public static final RadioactiveDecay COBALT_60 = new RadioactiveDecay(
            Real.of(5.27 * 365.25 * 24 * 3600), DecayType.BETA_MINUS, Real.of(2.82));

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
}
