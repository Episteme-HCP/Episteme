package org.jscience.arts;

import org.jscience.mathematics.numbers.real.Real;

import java.util.*;

/**
 * Statistical analysis for art forgery detection.
 */
public final class ForgeryDetector {

    private ForgeryDetector() {}

    /**
     * Represents a brushstroke signature profile.
     */
    public record BrushstrokeProfile(
        double avgLength,
        double avgWidth,
        double avgPressure,
        double directionVariance,
        double curveFrequency,
        int strokeCount
    ) {}

    /**
     * Represents an artist's known statistical fingerprint.
     */
    public record ArtistFingerprint(
        String artistName,
        BrushstrokeProfile meanProfile,
        double[] stdDeviations, // For each profile parameter
        int sampleSize
    ) {}

    /**
     * Calculates a brushstroke profile from stroke measurements.
     */
    public static BrushstrokeProfile calculateProfile(List<double[]> strokes) {
        // strokes: each double[] = {length, width, pressure, direction, curvature}
        if (strokes.isEmpty()) {
            return new BrushstrokeProfile(0, 0, 0, 0, 0, 0);
        }

        double[] lengths = strokes.stream().mapToDouble(s -> s[0]).toArray();
        double[] widths = strokes.stream().mapToDouble(s -> s[1]).toArray();
        double[] pressures = strokes.stream().mapToDouble(s -> s[2]).toArray();
        double[] directions = strokes.stream().mapToDouble(s -> s[3]).toArray();
        double[] curvatures = strokes.stream().mapToDouble(s -> s[4]).toArray();

        return new BrushstrokeProfile(
            average(lengths),
            average(widths),
            average(pressures),
            variance(directions),
            average(curvatures),
            strokes.size()
        );
    }

    /**
     * Compares a sample artwork against an artist's fingerprint.
     * Returns a confidence score (0-1) that the artwork is authentic.
     */
    public static Real authenticityScore(BrushstrokeProfile sample, ArtistFingerprint fingerprint) {
        BrushstrokeProfile ref = fingerprint.meanProfile();
        double[] stds = fingerprint.stdDeviations();
        
        // Calculate z-scores for each parameter
        double zLength = Math.abs(sample.avgLength() - ref.avgLength()) / Math.max(stds[0], 0.001);
        double zWidth = Math.abs(sample.avgWidth() - ref.avgWidth()) / Math.max(stds[1], 0.001);
        double zPressure = Math.abs(sample.avgPressure() - ref.avgPressure()) / Math.max(stds[2], 0.001);
        double zDirection = Math.abs(sample.directionVariance() - ref.directionVariance()) / Math.max(stds[3], 0.001);
        double zCurve = Math.abs(sample.curveFrequency() - ref.curveFrequency()) / Math.max(stds[4], 0.001);
        
        // Combined z-score
        double avgZ = (zLength + zWidth + zPressure + zDirection + zCurve) / 5.0;
        
        // Convert to probability (using simplified normal CDF approximation)
        double confidence = Math.exp(-avgZ * avgZ / 2.0);
        
        return Real.of(confidence);
    }

    /**
     * Flags potential forgery indicators.
     */
    public static List<String> detectAnomalies(BrushstrokeProfile sample, ArtistFingerprint fingerprint) {
        List<String> anomalies = new ArrayList<>();
        BrushstrokeProfile ref = fingerprint.meanProfile();
        double[] stds = fingerprint.stdDeviations();
        double threshold = 3.0; // 3 standard deviations
        
        if (Math.abs(sample.avgLength() - ref.avgLength()) > threshold * stds[0]) {
            anomalies.add("Stroke length significantly different from artist's known work");
        }
        if (Math.abs(sample.avgPressure() - ref.avgPressure()) > threshold * stds[2]) {
            anomalies.add("Pressure profile inconsistent with artist's technique");
        }
        if (Math.abs(sample.directionVariance() - ref.directionVariance()) > threshold * stds[3]) {
            anomalies.add("Direction variation unusual - possible hesitation marks");
        }
        if (sample.curveFrequency() < ref.curveFrequency() * 0.5) {
            anomalies.add("Suspicious lack of natural curve variation");
        }
        
        return anomalies;
    }

    private static double average(double[] values) {
        return Arrays.stream(values).average().orElse(0);
    }

    private static double variance(double[] values) {
        double mean = average(values);
        return Arrays.stream(values).map(v -> (v - mean) * (v - mean)).average().orElse(0);
    }
}
