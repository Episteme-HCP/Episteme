package org.jscience.politics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models the influence of lobbying on political decisions.
 */
public final class LobbyingInfluenceModel {

    private LobbyingInfluenceModel() {}

    /**
     * Estimated policy shift based on lobbying spend.
     */
    public static Real policyShift(double spend, double publicOpinion, double bias) {
        return Real.of(Math.log(1 + spend) * (1 - publicOpinion) + bias);
    }
}
