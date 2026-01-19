package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Extracts metrics from tracking data (xG, assists, etc.).
 */
public final class VideoAnalysisMetrics {

    private VideoAnalysisMetrics() {}

    public record Shot(double x, double y, double distance, boolean isHeader) {}

    /**
     * Expected Goals (xG) simplified model.
     */
    public static Real calculateXG(Shot shot) {
        double baseLine = 0.1;
        double distFactor = Math.exp(-shot.distance() / 20.0);
        double angleFactor = Math.abs(shot.x() / 50.0); // Simplified
        return Real.of(baseLine * distFactor * (1 - angleFactor));
    }
}
