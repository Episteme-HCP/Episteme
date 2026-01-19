package org.jscience.politics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models the economic impact of international sanctions.
 */
public final class SanctionsImpactModel {

    private SanctionsImpactModel() {}

    /**
     * Estimated GDP drop from trade sanctions.
     */
    public static Real estimateGDPImpact(double tradeVolume, double relianceFactor) {
        return Real.of(-tradeVolume * relianceFactor * 0.5);
    }
}
