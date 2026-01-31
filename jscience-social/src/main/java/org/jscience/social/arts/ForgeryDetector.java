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

package org.jscience.social.arts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Provides statistical analysis tools for art forgery detection.
 * It analyzes brushstroke patterns and compares them against known 
 * artist statistical fingerprints using z-score analysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class ForgeryDetector {

    private ForgeryDetector() {}

    /**
     * Represents a mathematical signature of brushstroke characteristics.
     */
    public record BrushstrokeProfile(
        double avgLength,
        double avgWidth,
        double avgPressure,
        double directionVariance,
        double curveFrequency,
        int strokeCount
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Represents the unique statistical fingerprint of a specific artist's 
     * brushstroke technique.
     */
    public record ArtistFingerprint(
        String artistName,
        BrushstrokeProfile meanProfile,
        double[] stdDeviations, // For each profile parameter
        int sampleSize
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Calculates a summary brushstroke profile from raw stroke measurements.
     * 
     * @param strokes raw measurements: {length, width, pressure, direction, curvature}
     * @return a consolidated BrushstrokeProfile
     */
    public static BrushstrokeProfile calculateProfile(List<double[]> strokes) {
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
     * Computes an authenticity confidence score (0 to 1) for a sample work 
     * against a known artist fingerprint.
     * 
     * @param sample the profile of the work being examined
     * @param fingerprint the verified fingerprint of the artist
     * @return confidence score as a Real number
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
     * Identifies specific metrics that deviate significantly from an artist's 
     * typical technique (e.g., "hesitation marks").
     * 
     * @param sample the profile of the work being examined
     * @param fingerprint the verified fingerprint of the artist
     * @return list of detected anomalies
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

