package org.jscience.sociology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models voting behavior based on partisanship and economy.
 */
public final class VotingBehaviorModel {

    private VotingBehaviorModel() {}

    /**
     * Probability of voting for incumbent based on GDP growth.
     */
    public static Real incumbentVoteProb(double gdpGrowth, double partyId) {
        double prob = 0.5 + 0.05 * gdpGrowth + 0.3 * partyId;
        return Real.of(Math.max(0, Math.min(1, prob)));
    }
}
