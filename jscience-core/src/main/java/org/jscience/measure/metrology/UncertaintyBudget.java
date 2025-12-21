/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.measure.metrology;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;

/**
 * An uncertainty budget following ISO/IEC Guide 98-3 (GUM) methodology.
 * <p>
 * Combines multiple uncertainty sources (Type A and Type B) to compute
 * the combined standard uncertainty and expanded uncertainty.
 * </p>
 * <p>
 * <b>Example Usage:</b>
 * 
 * <pre>{@code
 * UncertaintyBudget<Mass> budget = new UncertaintyBudget<>();
 * 
 * // Type A (statistical)
 * budget.addSource("Repeatability",
 *         Quantities.create(0.05, GRAM),
 *         UncertaintyType.TYPE_A);
 * 
 * // Type B (systematic)
 * budget.addSource("Calibration",
 *         Quantities.create(0.1, GRAM),
 *         UncertaintyType.TYPE_B);
 * 
 * budget.addSource("Temperature",
 *         Quantities.create(0.02, GRAM),
 *         UncertaintyType.TYPE_B);
 * 
 * // Combined: √(0.05² + 0.1² + 0.02²) = 0.112 g
 * Quantity<Mass> combined = budget.getCombinedUncertainty();
 * 
 * // Expanded (k=2): 2 × 0.112 = 0.224 g
 * Quantity<Mass> expanded = budget.getExpandedUncertainty(2.0);
 * }</pre>
 * </p>
 * 
 * <h2>Uncertainty Types</h2>
 * <ul>
 * <li><b>Type A</b>: Evaluated by statistical methods (repeated
 * measurements)</li>
 * <li><b>Type B</b>: Evaluated by other means (specifications, calibration,
 * experience)</li>
 * </ul>
 * 
 * <h2>References</h2>
 * <ul>
 * <li>ISO/IEC Guide 98-3:2008 - Uncertainty of measurement (GUM)</li>
 * <li>JCGM 100:2008 - Evaluation of measurement data</li>
 * </ul>
 * 
 * @param <Q> the quantity type
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class UncertaintyBudget<Q extends Quantity<Q>> {

    private final Map<String, UncertaintySource<Q>> sources = new LinkedHashMap<>();

    /**
     * Adds an uncertainty source to the budget.
     * 
     * @param name        the source name (e.g., "Repeatability", "Calibration")
     * @param uncertainty the standard uncertainty contribution
     * @param type        the uncertainty type (A or B)
     */
    public void addSource(String name, Quantity<Q> uncertainty, UncertaintyType type) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Source name cannot be null or empty");
        }
        if (uncertainty == null) {
            throw new IllegalArgumentException("Uncertainty cannot be null");
        }
        sources.put(name, new UncertaintySource<>(name, uncertainty, type, Real.ONE));
    }

    /**
     * Adds an uncertainty source with sensitivity coefficient.
     * <p>
     * The sensitivity coefficient c_i represents ∂f/∂x_i, the partial
     * derivative of the measurement function with respect to the input.
     * </p>
     * 
     * @param name                   the source name
     * @param uncertainty            the standard uncertainty
     * @param type                   the uncertainty type
     * @param sensitivityCoefficient the sensitivity coefficient c_i
     */
    public void addSource(String name, Quantity<Q> uncertainty,
            UncertaintyType type, Real sensitivityCoefficient) {
        sources.put(name, new UncertaintySource<>(name, uncertainty, type, sensitivityCoefficient));
    }

    /**
     * Returns the combined standard uncertainty.
     * <p>
     * Combined uncertainty u_c = √(Σ u_i²), where u_i are individual
     * standard uncertainties (possibly multiplied by sensitivity coefficients).
     * </p>
     * 
     * @return the combined standard uncertainty
     */
    public Quantity<Q> getCombinedUncertainty() {
        if (sources.isEmpty()) {
            throw new IllegalStateException("No uncertainty sources defined");
        }

        Quantity<Q> firstUncertainty = sources.values().iterator().next().uncertainty;
        Real sumSquares = Real.ZERO;

        for (UncertaintySource<Q> source : sources.values()) {

            // u_i effective = c_i × u_i
            Real u = source.uncertainty.getValue();
            Real c = source.sensitivityCoefficient;
            Real effectiveU = u.multiply(c);

            sumSquares = sumSquares.add(effectiveU.multiply(effectiveU));
        }

        Real combined = sumSquares.sqrt();

        // Return with same unit as first source
        return firstUncertainty.multiply(combined.divide(firstUncertainty.getValue()));
    }

    /**
     * Returns the expanded uncertainty.
     * <p>
     * Expanded uncertainty U = k × u_c, where k is the coverage factor.
     * Common values:
     * </p>
     * <ul>
     * <li>k = 2 for ~95% confidence</li>
     * <li>k = 3 for ~99.7% confidence</li>
     * </ul>
     * 
     * @param coverageFactor the coverage factor k
     * @return the expanded uncertainty
     */
    public Quantity<Q> getExpandedUncertainty(double coverageFactor) {
        Quantity<Q> combined = getCombinedUncertainty();
        return combined.multiply(coverageFactor);
    }

    /**
     * Returns the sensitivity coefficients for all sources.
     * <p>
     * The sensitivity coefficient quantifies how much the output changes
     * with respect to each input variable.
     * </p>
     * 
     * @return a map of source names to sensitivity coefficients
     */
    public Map<String, Real> getSensitivityCoefficients() {
        Map<String, Real> coefficients = new LinkedHashMap<>();
        for (UncertaintySource<Q> source : sources.values()) {
            coefficients.put(source.name, source.sensitivityCoefficient);
        }
        return coefficients;
    }

    /**
     * Returns the contribution of each source to the combined uncertainty.
     * <p>
     * Contribution percentage = (c_i × u_i)² / u_c² × 100%
     * </p>
     * 
     * @return a map of source names to percentage contributions
     */
    public Map<String, Double> getContributions() {
        Map<String, Double> contributions = new LinkedHashMap<>();

        Quantity<Q> combined = getCombinedUncertainty();
        Real combinedSquared = combined.getValue().multiply(combined.getValue());

        for (UncertaintySource<Q> source : sources.values()) {
            Real u = source.uncertainty.getValue();
            Real c = source.sensitivityCoefficient;
            Real effectiveU = u.multiply(c);
            Real contributionSquared = effectiveU.multiply(effectiveU);
            Real percentage = contributionSquared.divide(combinedSquared).multiply(Real.of(100));

            contributions.put(source.name, percentage.doubleValue());
        }

        return contributions;
    }

    /**
     * Generates a formatted uncertainty budget report.
     * 
     * @return the formatted report
     */
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Uncertainty Budget\n");
        sb.append("==================\n\n");

        Quantity<Q> combined = getCombinedUncertainty();
        Map<String, Double> contributions = getContributions();

        sb.append("Source                    Type    Uncertainty    c_i      Contribution\n");
        sb.append("------------------------------------------------------------------------\n");

        for (UncertaintySource<Q> source : sources.values()) {
            sb.append(String.format("%-25s %-7s %12s  %6.2f   %5.1f%%\n",
                    source.name,
                    source.type,
                    source.uncertainty.getValue(),
                    source.sensitivityCoefficient.doubleValue(),
                    contributions.get(source.name)));
        }

        sb.append("------------------------------------------------------------------------\n");
        sb.append(String.format("Combined Standard Uncertainty (u_c): %s\n", combined));
        sb.append(String.format("Expanded Uncertainty (k=2, ~95%%):  %s\n", getExpandedUncertainty(2.0)));

        return sb.toString();
    }

    /**
     * Uncertainty classification per GUM.
     */
    public enum UncertaintyType {
        /** Type A: Statistical evaluation (repeated measurements) */
        TYPE_A,
        /** Type B: Other evaluation (specs, calibration, etc.) */
        TYPE_B
    }

    /**
     * Internal representation of an uncertainty source.
     */
    private static class UncertaintySource<Q extends Quantity<Q>> {
        final String name;
        final Quantity<Q> uncertainty;
        final UncertaintyType type;
        final Real sensitivityCoefficient;

        UncertaintySource(String name, Quantity<Q> uncertainty,
                UncertaintyType type, Real sensitivityCoefficient) {
            this.name = name;
            this.uncertainty = uncertainty;
            this.type = type;
            this.sensitivityCoefficient = sensitivityCoefficient;
        }
    }
}