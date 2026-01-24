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

package org.jscience.arts;

import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Provides mathematical tools for analyzing compositional proportions, specifically 
 * focusing on the Golden Ratio (phi) and the Fibonacci sequence.
 * These metrics are frequently used in art history and architectural analysis 
 * to evaluate the aesthetic structure of works.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class GoldenRatio {

    private GoldenRatio() {}

    /** The Golden Ratio (phi) = (1 + sqrt(5)) / 2 approx 1.6180339887... */
    public static final Real PHI = Real.of((1 + Math.sqrt(5)) / 2);

    /** The inverse of the Golden Ratio (1/phi) approx 0.6180339887... */
    public static final Real PHI_INVERSE = Real.of(2 / (1 + Math.sqrt(5)));

    /**
     * Generates a Fibonacci sequence up to specified number of terms.
     * 
     * @param n the number of terms to generate
     * @return the Fibonacci sequence
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
     * Calculates the n-th Fibonacci number using Binet's formula for 
     * direct analytical computation.
     * 
     * @param n the position in the sequence
     * @return the n-th Fibonacci number as a Real
     */
    public static Real fibonacciN(int n) {
        double phi = PHI.doubleValue();
        double psi = -PHI_INVERSE.doubleValue();
        return Real.of(Math.round((Math.pow(phi, n) - Math.pow(psi, n)) / Math.sqrt(5)));
    }

    /**
     * Checks if the ratio of two measurements approximates the Golden Ratio 
     * within a specified tolerance.
     * 
     * @param larger the larger measurement
     * @param smaller the smaller measurement
     * @param tolerance the maximum allowed deviation from phi
     * @return true if the ratio matches the Golden Ratio
     */
    public static boolean isGoldenRatio(Real larger, Real smaller, Real tolerance) {
        if (smaller.isZero()) return false;
        Real ratio = larger.divide(smaller);
        return ratio.subtract(PHI).abs().compareTo(tolerance) < 0;
    }

    /**
     * Divides a total length into two segments according to the Golden Ratio.
     * 
     * @param totalLength the total length to divide
     * @return an array containing [largerSegment, smallerSegment]
     */
    public static Real[] goldenSection(Real totalLength) {
        Real larger = totalLength.divide(PHI);
        Real smaller = totalLength.subtract(larger);
        return new Real[]{larger, smaller};
    }

    /**
     * Generates the radii of a Golden Spiral for a given number of quarter-turns.
     * 
     * @param initialRadius the starting radius
     * @param quarterTurns number of 90-degree turns to compute
     * @return list of radii for each turn
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
     * Calculates the dimensions of a Golden Rectangle derived from a base width.
     * 
     * @param width the base width
     * @return array containing [width, height] where width/height = phi
     */
    public static Real[] goldenRectangle(Real width) {
        return new Real[]{width, width.divide(PHI)};
    }

    /**
     * Scans a set of measurements and calculates what fraction of measurement 
     * pairs conform to the Golden Ratio.
     * 
     * @param measurements list of measured dimensions in an artwork
     * @param tolerance maximum allowed error
     * @return a percentage (0 to 1) of pairs matching the golden proportion
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
