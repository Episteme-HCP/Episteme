package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Analyzes fan engagement across social and physical channels.
 */
public final class FanEngagementAnalyzer {

    private FanEngagementAnalyzer() {}

    /**
     * Calculates sentiment index from social media mentions.
     */
    public static Real engagementScore(int attendance, int socialMentions, double sentiment) {
        return Real.of(attendance * 0.4 + socialMentions * 0.3 * sentiment);
    }
}
