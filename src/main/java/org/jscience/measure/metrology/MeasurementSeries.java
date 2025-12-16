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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.measure.metrology;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;

/**
 * Statistical analysis of repeated measurements.
 * <p>
 * Collects multiple measurements of the same quantity and provides
 * statistical analysis including mean, standard deviation, confidence
 * intervals, and outlier detection.
 * </p>
 * <p>
 * <b>Example Usage:</b>
 * 
 * <pre>{@code
 * MeasurementSeries<Mass> series = new MeasurementSeries<>();
 * 
 * // Add measurements
 * series.addMeasurement(Quantities.create(100.2, GRAM));
 * series.addMeasurement(Quantities.create(100.1, GRAM));
 * series.addMeasurement(Quantities.create(100.3, GRAM));
 * // ... more measurements
 * 
 * // Statistical analysis
 * Quantity<Mass> mean = series.getMean();
 * Quantity<Mass> stdDev = series.getStandardDeviation();
 * MeasuredQuantity<Mass> result = series.getConfidenceInterval(0.95);
 * }</pre>
 * </p>
 * 
 * @param <Q> the quantity type
 * 
 * @author Silvere Martin-Michiellot (silvere.martin@gmail.com)
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 * 
 * @see MeasuredQuantity
 */
public class MeasurementSeries<Q extends Quantity<Q>> {

    private final List<Quantity<Q>> measurements = new ArrayList<>();

    /**
     * Adds a measurement to the series.
     * 
     * @param value the measured value
     */
    public void addMeasurement(Quantity<Q> value) {
        if (value == null) {
            throw new IllegalArgumentException("Measurement cannot be null");
        }
        measurements.add(value);
    }

    /**
     * Returns the number of measurements.
     * 
     * @return the count
     */
    public int getCount() {
        return measurements.size();
    }

    /**
     * Returns the arithmetic mean of all measurements.
     * 
     * @return the mean value
     * @throws IllegalStateException if no measurements
     */
    public Quantity<Q> getMean() {
        if (measurements.isEmpty()) {
            throw new IllegalStateException("No measurements available");
        }

        Quantity<Q> sum = measurements.get(0).multiply(0.0); // Zero with correct unit
        for (Quantity<Q> m : measurements) {
            sum = sum.add(m);
        }

        return sum.divide(measurements.size());
    }

    /**
     * Returns the sample standard deviation.
     * <p>
     * Uses Bessel's correction (n-1 denominator).
     * </p>
     * 
     * @return the standard deviation
     * @throws IllegalStateException if fewer than 2 measurements
     */
    public Quantity<Q> getStandardDeviation() {
        if (measurements.size() < 2) {
            throw new IllegalStateException("Need at least 2 measurements for std dev");
        }

        Quantity<Q> mean = getMean();
        Real sumSquaredDiff = Real.ZERO;

        for (Quantity<Q> m : measurements) {
            Real diff = m.getValue().subtract(mean.getValue());
            sumSquaredDiff = sumSquaredDiff.add(diff.multiply(diff));
        }

        Real variance = sumSquaredDiff.divide(Real.of(measurements.size() - 1));
        Real stdDev = variance.sqrt();

        return mean.multiply(stdDev.divide(mean.getValue())); // Preserve units
    }

    /**
     * Returns the standard error of the mean (SEM).
     * <p>
     * SEM = σ / √n
     * </p>
     * 
     * @return the standard error
     */
    public Quantity<Q> getStandardError() {
        Quantity<Q> stdDev = getStandardDeviation();
        Real sqrtN = Real.of(Math.sqrt(measurements.size()));
        return stdDev.divide(sqrtN);
    }

    /**
     * Returns a measurement with confidence interval.
     * <p>
     * For normal distributions:
     * </p>
     * <ul>
     * <li>68.3% → mean ± 1×SEM</li>
     * <li>95.4% → mean ± 2×SEM</li>
     * <li>99.7% → mean ± 3×SEM</li>
     * </ul>
     * 
     * @param confidenceLevel the confidence level (e.g., 0.95)
     * @return the mean with uncertainty
     */
    public MeasuredQuantity<Q> getConfidenceInterval(double confidenceLevel) {
        Quantity<Q> mean = getMean();
        Quantity<Q> sem = getStandardError();

        // Approximate coverage factor from confidence level
        double k;
        if (confidenceLevel >= 0.997)
            k = 3.0;
        else if (confidenceLevel >= 0.954)
            k = 2.0;
        else
            k = 1.0;

        Quantity<Q> uncertainty = sem.multiply(k);

        return MeasuredQuantities.create(mean, uncertainty, confidenceLevel);
    }

    /**
     * Detects outliers using the specified method.
     * 
     * @param method the outlier detection method
     * @return the set of detected outliers
     */
    public Set<Quantity<Q>> detectOutliers(OutlierMethod method) {
        Set<Quantity<Q>> outliers = new HashSet<>();

        if (measurements.size() < 3) {
            return outliers; // Need at least 3 for outlier detection
        }

        Quantity<Q> mean = getMean();
        Quantity<Q> stdDev = getStandardDeviation();

        switch (method) {
            case ZSCORE:
                // Z-score > 3 considered outlier
                for (Quantity<Q> m : measurements) {
                    Real diff = m.getValue().subtract(mean.getValue()).abs();
                    Real z = diff.divide(stdDev.getValue());
                    if (z.compareTo(Real.of(3.0)) > 0) {
                        outliers.add(m);
                    }
                }
                break;

            case IQR:
                // Interquartile range method
                List<Quantity<Q>> sorted = new ArrayList<>(measurements);
                sorted.sort((q1, q2) -> q1.getValue().compareTo(q2.getValue()));

                int n = sorted.size();
                double q1Val = sorted.get(n / 4).getValue().doubleValue();
                double q3Val = sorted.get((n * 3) / 4).getValue().doubleValue();
                double iqr = q3Val - q1Val;

                double lowerBound = q1Val - 1.5 * iqr;
                double upperBound = q3Val + 1.5 * iqr;

                for (Quantity<Q> m : measurements) {
                    double val = m.getValue().doubleValue();
                    if (val < lowerBound || val > upperBound) {
                        outliers.add(m);
                    }
                }
                break;
        }

        return outliers;
    }

    /**
     * Returns the mean excluding detected outliers.
     * 
     * @return the trimmed mean with confidence interval
     */
    public MeasuredQuantity<Q> getMeanExcludingOutliers() {
        Set<Quantity<Q>> outliers = detectOutliers(OutlierMethod.ZSCORE);

        if (outliers.isEmpty()) {
            return getConfidenceInterval(0.954); // 95%
        }

        // Create temporary series without outliers
        MeasurementSeries<Q> trimmed = new MeasurementSeries<>();
        for (Quantity<Q> m : measurements) {
            if (!outliers.contains(m)) {
                trimmed.addMeasurement(m);
            }
        }

        return trimmed.getConfidenceInterval(0.954);
    }

    /**
     * Outlier detection methods.
     */
    public enum OutlierMethod {
        /** Z-score method (>3σ from mean) */
        ZSCORE,
        /** Interquartile range method */
        IQR
    }
}
