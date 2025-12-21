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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Unit;

/**
 * Simple implementation of {@link MeasuredQuantity}.
 * <p>
 * This class is immutable and thread-safe.
 * </p>
 * 
 * @param <Q> the quantity type
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
final class SimpleMeasuredQuantity<Q extends Quantity<Q>> implements MeasuredQuantity<Q> {

    private final Quantity<Q> value;
    private final Quantity<Q> uncertainty;
    private final double confidenceLevel;

    SimpleMeasuredQuantity(Quantity<Q> value, Quantity<Q> uncertainty, double confidenceLevel) {
        if (value == null || uncertainty == null) {
            throw new IllegalArgumentException("Value and uncertainty cannot be null");
        }
        if (confidenceLevel <= 0 || confidenceLevel > 1) {
            throw new IllegalArgumentException("Confidence level must be in (0, 1]");
        }
        this.value = value;
        this.uncertainty = uncertainty.abs(); // Always positive
        this.confidenceLevel = confidenceLevel;
    }

    @Override
    public Quantity<Q> getValue() {
        return value;
    }

    @Override
    public Quantity<Q> getUncertainty() {
        return uncertainty;
    }

    @Override
    public Real getRelativeUncertainty() {
        Real v = value.getValue();
        Real u = uncertainty.getValue();
        if (v.abs().compareTo(Real.of(1e-10)) < 0) {
            return Real.of(Double.POSITIVE_INFINITY); // Undefined
        }
        return u.divide(v.abs());
    }

    @Override
    public double getConfidenceLevel() {
        return confidenceLevel;
    }

    @Override
    public MeasuredQuantity<Q> add(MeasuredQuantity<Q> other) {
        // z = x + y
        // σ_z = √(σ_x² + σ_y²)
        Quantity<Q> newValue = value.add(other.getValue());
        Quantity<Q> sigma1 = uncertainty;
        Quantity<Q> sigma2 = other.getUncertainty();

        Real s1 = sigma1.getValue();
        Real s2 = sigma2.getValue();
        Real combinedSigma = s1.multiply(s1).add(s2.multiply(s2)).sqrt();

        Quantity<Q> newUncertainty = uncertainty.multiply(combinedSigma.divide(s1));
        return new SimpleMeasuredQuantity<>(newValue, newUncertainty, confidenceLevel);
    }

    @Override
    public MeasuredQuantity<Q> subtract(MeasuredQuantity<Q> other) {
        // Same propagation as addition
        Quantity<Q> newValue = value.subtract(other.getValue());
        Quantity<Q> sigma1 = uncertainty;
        Quantity<Q> sigma2 = other.getUncertainty();

        Real s1 = sigma1.getValue();
        Real s2 = sigma2.getValue();
        Real combinedSigma = s1.multiply(s1).add(s2.multiply(s2)).sqrt();

        Quantity<Q> newUncertainty = uncertainty.multiply(combinedSigma.divide(s1));
        return new SimpleMeasuredQuantity<>(newValue, newUncertainty, confidenceLevel);
    }

    @Override
    public MeasuredQuantity<Q> multiply(Real scalar) {
        // z = k × x
        // σ_z = |k| × σ_x
        Quantity<Q> newValue = value.multiply(scalar);
        Quantity<Q> newUncertainty = uncertainty.multiply(scalar.abs());
        return new SimpleMeasuredQuantity<>(newValue, newUncertainty, confidenceLevel);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <R extends Quantity<R>> MeasuredQuantity<?> multiply(MeasuredQuantity<R> other) {
        // z = x × y
        // σ_z/z = √((σ_x/x)² + (σ_y/y)²)
        Quantity<?> newValue = value.multiply(other.getValue());

        Real relUncert1 = getRelativeUncertainty();
        Real relUncert2 = other.getRelativeUncertainty();
        Real combinedRelUncert = relUncert1.multiply(relUncert1)
                .add(relUncert2.multiply(relUncert2))
                .sqrt();

        Quantity<?> newUncertainty = newValue.multiply(combinedRelUncert);
        return new SimpleMeasuredQuantity(newValue, newUncertainty, confidenceLevel);
    }

    @Override
    public MeasuredQuantity<Q> divide(Real scalar) {
        // z = x / k
        // σ_z = σ_x / |k|
        Quantity<Q> newValue = value.divide(scalar);
        Quantity<Q> newUncertainty = uncertainty.divide(scalar.abs());
        return new SimpleMeasuredQuantity<>(newValue, newUncertainty, confidenceLevel);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <R extends Quantity<R>> MeasuredQuantity<?> divide(MeasuredQuantity<R> other) {
        // Same relative uncertainty propagation as multiplication
        Quantity<?> newValue = value.divide(other.getValue());

        Real relUncert1 = getRelativeUncertainty();
        Real relUncert2 = other.getRelativeUncertainty();
        Real combinedRelUncert = relUncert1.multiply(relUncert1)
                .add(relUncert2.multiply(relUncert2))
                .sqrt();

        Quantity<?> newUncertainty = newValue.multiply(combinedRelUncert);
        return new SimpleMeasuredQuantity(newValue, newUncertainty, confidenceLevel);
    }

    @Override
    public MeasuredQuantity<Q> to(Unit<Q> targetUnit) {
        Quantity<Q> newValue = value.to(targetUnit);
        Quantity<Q> newUncertainty = uncertainty.to(targetUnit);
        return new SimpleMeasuredQuantity<>(newValue, newUncertainty, confidenceLevel);
    }

    @Override
    public String toScientificNotation() {
        return String.format("%s ± %s (%d%%)",
                value, uncertainty, (int) (confidenceLevel * 100));
    }

    @Override
    public String toPercentageNotation() {
        double percentUncertainty = getRelativeUncertainty().doubleValue() * 100;
        return String.format("%s ± %.1f%%", value, percentUncertainty);
    }

    @Override
    public String toIntervalNotation() {
        Quantity<Q> lower = value.subtract(uncertainty);
        Quantity<Q> upper = value.add(uncertainty);
        return String.format("%s [%s, %s]",
                value.getValue(), lower.getValue(), upper.getValue());
    }

    @Override
    public boolean overlaps(MeasuredQuantity<Q> other) {
        // Check if intervals overlap
        Quantity<Q> thisLower = value.subtract(uncertainty);
        Quantity<Q> thisUpper = value.add(uncertainty);
        Quantity<Q> otherLower = other.getValue().subtract(other.getUncertainty());
        Quantity<Q> otherUpper = other.getValue().add(other.getUncertainty());

        // Overlaps if: thisLower <= otherUpper AND otherLower <= thisUpper
        return thisLower.compareTo(otherUpper) <= 0 &&
                otherLower.compareTo(thisUpper) <= 0;
    }

    @Override
    public boolean isConsistentWith(MeasuredQuantity<Q> other) {
        // Normalized difference should be < k (coverage factor)
        Real diff = getStandardizedDifference(other);
        return diff.compareTo(Real.of(getCoverageFactor())) < 0;
    }

    @Override
    public Real getStandardizedDifference(MeasuredQuantity<Q> other) {
        // z = |x - y| / √(σ_x² + σ_y²)
        Quantity<Q> diff = value.subtract(other.getValue()).abs();

        Real s1 = uncertainty.getValue();
        Real s2 = other.getUncertainty().getValue();
        Real combinedSigma = s1.multiply(s1).add(s2.multiply(s2)).sqrt();

        return diff.getValue().divide(combinedSigma);
    }

    @Override
    public String toString() {
        return toScientificNotation();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof MeasuredQuantity))
            return false;

        @SuppressWarnings("unchecked")
        MeasuredQuantity<Q> other = (MeasuredQuantity<Q>) obj;

        return value.equals(other.getValue()) &&
                uncertainty.equals(other.getUncertainty()) &&
                Math.abs(confidenceLevel - other.getConfidenceLevel()) < 1e-6;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + uncertainty.hashCode();
        result = 31 * result + Double.hashCode(confidenceLevel);
        return result;
    }
}