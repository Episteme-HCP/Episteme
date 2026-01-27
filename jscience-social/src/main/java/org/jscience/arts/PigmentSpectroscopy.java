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

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Simulates reflectance spectra of historical pigments for authentication.
 */
public final class PigmentSpectroscopy {

    private PigmentSpectroscopy() {}

    public record Pigment(
        String name,
        String chemicalFormula,
        int introductionYear,
        int obsolescenceYear,
        double[] reflectanceSpectrum // 400-700nm in 10nm steps (31 values)
    ) {}

    // Simplified reflectance spectra (normalized 0-1)
    private static final Map<String, Pigment> PIGMENT_DATABASE = Map.ofEntries(
        Map.entry("vermilion", new Pigment("Vermilion", "HgS", -3000, 2100,
            new double[]{0.05, 0.05, 0.06, 0.07, 0.08, 0.10, 0.12, 0.15, 0.20, 0.30, 
                        0.45, 0.65, 0.80, 0.88, 0.92, 0.94, 0.95, 0.95, 0.95, 0.95,
                        0.95, 0.95, 0.95, 0.95, 0.95, 0.95, 0.95, 0.95, 0.95, 0.95, 0.95})),
        Map.entry("lapis_lazuli", new Pigment("Lapis Lazuli (Ultramarine)", "Na₈Al₆Si₆O₂₄S₂", -3000, 1828,
            new double[]{0.60, 0.65, 0.70, 0.72, 0.70, 0.65, 0.55, 0.40, 0.25, 0.15,
                        0.10, 0.08, 0.07, 0.06, 0.06, 0.06, 0.06, 0.06, 0.06, 0.06,
                        0.07, 0.08, 0.10, 0.12, 0.15, 0.18, 0.20, 0.22, 0.25, 0.28, 0.30})),
        Map.entry("synthetic_ultramarine", new Pigment("Synthetic Ultramarine", "Na₈Al₆Si₆O₂₄S₂", 1828, 2100,
            new double[]{0.62, 0.67, 0.72, 0.74, 0.72, 0.67, 0.57, 0.42, 0.27, 0.17,
                        0.12, 0.10, 0.09, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08,
                        0.09, 0.10, 0.12, 0.14, 0.17, 0.20, 0.22, 0.24, 0.27, 0.30, 0.32})),
        Map.entry("ochre", new Pigment("Yellow Ochre", "Fe₂O₃·H₂O", -40000, 2100,
            new double[]{0.05, 0.06, 0.07, 0.08, 0.10, 0.15, 0.25, 0.40, 0.55, 0.65,
                        0.72, 0.78, 0.82, 0.85, 0.87, 0.88, 0.88, 0.88, 0.87, 0.86,
                        0.85, 0.84, 0.83, 0.82, 0.81, 0.80, 0.79, 0.78, 0.77, 0.76, 0.75})),
        Map.entry("cadmium_yellow", new Pigment("Cadmium Yellow", "CdS", 1840, 2100,
            new double[]{0.03, 0.03, 0.04, 0.05, 0.10, 0.25, 0.50, 0.75, 0.88, 0.93,
                        0.95, 0.96, 0.96, 0.96, 0.95, 0.94, 0.93, 0.92, 0.91, 0.90,
                        0.89, 0.88, 0.87, 0.86, 0.85, 0.84, 0.83, 0.82, 0.81, 0.80, 0.79})),
        Map.entry("lead_white", new Pigment("Lead White", "2PbCO₃·Pb(OH)₂", -400, 1970,
            new double[]{0.90, 0.91, 0.92, 0.93, 0.94, 0.95, 0.95, 0.95, 0.95, 0.95,
                        0.95, 0.95, 0.95, 0.95, 0.95, 0.95, 0.95, 0.95, 0.95, 0.95,
                        0.95, 0.95, 0.95, 0.95, 0.95, 0.95, 0.95, 0.95, 0.95, 0.95, 0.95})),
        Map.entry("titanium_white", new Pigment("Titanium White", "TiO₂", 1921, 2100,
            new double[]{0.92, 0.93, 0.94, 0.95, 0.96, 0.97, 0.97, 0.97, 0.97, 0.97,
                        0.97, 0.97, 0.97, 0.97, 0.97, 0.97, 0.97, 0.97, 0.97, 0.97,
                        0.97, 0.97, 0.97, 0.97, 0.97, 0.97, 0.97, 0.97, 0.97, 0.97, 0.97}))
    );

    /**
     * Gets available pigments for a specific year.
     */
    public static List<Pigment> getAvailablePigments(int year) {
        return PIGMENT_DATABASE.values().stream()
            .filter(p -> year >= p.introductionYear() && year <= p.obsolescenceYear())
            .toList();
    }

    /**
     * Compares a measured spectrum against known pigments.
     * Returns best matching pigments with correlation scores.
     */
    public static List<Map.Entry<Pigment, Real>> matchSpectrum(double[] measured) {
        List<Map.Entry<Pigment, Real>> matches = new ArrayList<>();
        
        for (Pigment pigment : PIGMENT_DATABASE.values()) {
            double correlation = pearsonCorrelation(measured, pigment.reflectanceSpectrum());
            matches.add(Map.entry(pigment, Real.of(correlation)));
        }
        
        matches.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        return matches;
    }

    /**
     * Detects anachronistic pigments for a claimed date.
     */
    public static List<String> detectAnachronisms(List<String> identifiedPigments, int claimedYear) {
        List<String> issues = new ArrayList<>();
        
        for (String pigmentName : identifiedPigments) {
            Pigment p = PIGMENT_DATABASE.get(pigmentName.toLowerCase().replace(" ", "_"));
            if (p != null) {
                if (claimedYear < p.introductionYear()) {
                    issues.add(p.name() + " not available until " + p.introductionYear());
                }
                if (claimedYear > p.obsolescenceYear()) {
                    issues.add(p.name() + " rarely used after " + p.obsolescenceYear());
                }
            }
        }
        
        return issues;
    }

    private static double pearsonCorrelation(double[] x, double[] y) {
        int n = Math.min(x.length, y.length);
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0, sumY2 = 0;
        
        for (int i = 0; i < n; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];
            sumX2 += x[i] * x[i];
            sumY2 += y[i] * y[i];
        }
        
        double numerator = n * sumXY - sumX * sumY;
        double denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));
        
        return denominator == 0 ? 0 : numerator / denominator;
    }
}
