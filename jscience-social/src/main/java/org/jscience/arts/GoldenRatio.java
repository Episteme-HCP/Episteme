package org.jscience.arts;

import org.jscience.mathematics.numbers.real.Real;
import java.util.ArrayList;
import java.util.List;

/**
 * Golden Ratio, Fibonacci, and compositional proportion analysis.
 */
public final class GoldenRatio {

    private GoldenRatio() {}

    /** The Golden Ratio (φ) = (1 + √5) / 2 ≈ 1.6180339887... */
    public static final Real PHI = Real.of((1 + Math.sqrt(5)) / 2);

    /** The inverse of Golden Ratio (1/φ) ≈ 0.6180339887... */
    public static final Real PHI_INVERSE = Real.of(2 / (1 + Math.sqrt(5)));

    /**
     * Generates Fibonacci sequence up to n terms.
     */
    public static List<Long> fibonacci(int n) {
        List<Long> seq = new ArrayList<>();
        if (n <= 0) return seq;
        seq.add(0L);
        if (n == 1) return seq;
        seq.add(1L);
        for (int i = 2; i < n; i++) {
            seq.add(seq.get(i - 1) + seq.get(i - 2));
        }
        return seq;
    }

    /**
     * Returns the nth Fibonacci number using Binet's formula.
     */
    public static Real fibonacciN(int n) {
        double phi = PHI.doubleValue();
        double psi = -PHI_INVERSE.doubleValue();
        return Real.of(Math.round((Math.pow(phi, n) - Math.pow(psi, n)) / Math.sqrt(5)));
    }

    /**
     * Checks if a ratio approximates the Golden Ratio within tolerance.
     */
    public static boolean isGoldenRatio(Real larger, Real smaller, Real tolerance) {
        if (smaller.compareTo(Real.ZERO) == 0) return false;
        Real ratio = larger.divide(smaller);
        return ratio.subtract(PHI).abs().compareTo(tolerance) < 0;
    }

    /**
     * Divides a length into two parts according to the Golden Ratio.
     * Returns [larger, smaller] segments.
     */
    public static Real[] goldenSection(Real totalLength) {
        Real larger = totalLength.divide(PHI);
        Real smaller = totalLength.subtract(larger);
        return new Real[]{larger, smaller};
    }

    /**
     * Generates a Golden Spiral's radii for n quarter-turns.
     * Each radius is multiplied by φ^(1/4) per 90° turn.
     */
    public static List<Real> goldenSpiralRadii(Real initialRadius, int quarterTurns) {
        List<Real> radii = new ArrayList<>();
        Real growthFactor = Real.of(Math.pow(PHI.doubleValue(), 0.25));
        Real r = initialRadius;
        for (int i = 0; i < quarterTurns; i++) {
            radii.add(r);
            r = r.multiply(growthFactor);
        }
        return radii;
    }

    /**
     * Calculates the Golden Rectangle dimensions from a given width.
     */
    public static Real[] goldenRectangle(Real width) {
        return new Real[]{width, width.divide(PHI)};
    }

    /**
     * Analyzes a set of measurements for Golden Ratio proportions.
     * Returns the proportion of pairs that match the Golden Ratio.
     */
    public static Real analyzeProportions(List<Real> measurements, Real tolerance) {
        if (measurements == null || measurements.size() < 2) return Real.ZERO;
        
        int matches = 0;
        int total = 0;
        
        for (int i = 0; i < measurements.size(); i++) {
            for (int j = i + 1; j < measurements.size(); j++) {
                Real a = measurements.get(i);
                Real b = measurements.get(j);
                Real larger = a.compareTo(b) >= 0 ? a : b;
                Real smaller = a.compareTo(b) >= 0 ? b : a;
                if (isGoldenRatio(larger, smaller, tolerance)) {
                    matches++;
                }
                total++;
            }
        }
        
        return total > 0 ? Real.of((double) matches / total) : Real.ZERO;
    }
}
