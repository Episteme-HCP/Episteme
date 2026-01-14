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

package org.jscience.measure;

import java.util.Objects;

/**
 * Standard implementation of {@link Dimension} using seven SI base dimensions.
 * <p>
 * This class is immutable and thread-safe.
 * </p>
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
final class StandardDimension implements Dimension {

    private final int length;
    private final int mass;
    private final int time;
    private final int electricCurrent;
    private final int temperature;
    private final int amountOfSubstance;
    private final int luminousIntensity;

    /**
     * Constructs a dimension with the specified exponents.
     */
    StandardDimension(int length, int mass, int time, int electricCurrent,
            int temperature, int amountOfSubstance, int luminousIntensity) {
        this.length = length;
        this.mass = mass;
        this.time = time;
        this.electricCurrent = electricCurrent;
        this.temperature = temperature;
        this.amountOfSubstance = amountOfSubstance;
        this.luminousIntensity = luminousIntensity;
    }

    @Override
    public int getLengthExponent() {
        return length;
    }

    @Override
    public int getMassExponent() {
        return mass;
    }

    @Override
    public int getTimeExponent() {
        return time;
    }

    @Override
    public int getElectricCurrentExponent() {
        return electricCurrent;
    }

    @Override
    public int getTemperatureExponent() {
        return temperature;
    }

    @Override
    public int getAmountOfSubstanceExponent() {
        return amountOfSubstance;
    }

    @Override
    public int getLuminousIntensityExponent() {
        return luminousIntensity;
    }

    @Override
    public Dimension multiply(Dimension other) {
        return new StandardDimension(
                length + other.getLengthExponent(),
                mass + other.getMassExponent(),
                time + other.getTimeExponent(),
                electricCurrent + other.getElectricCurrentExponent(),
                temperature + other.getTemperatureExponent(),
                amountOfSubstance + other.getAmountOfSubstanceExponent(),
                luminousIntensity + other.getLuminousIntensityExponent());
    }

    @Override
    public Dimension divide(Dimension other) {
        return new StandardDimension(
                length - other.getLengthExponent(),
                mass - other.getMassExponent(),
                time - other.getTimeExponent(),
                electricCurrent - other.getElectricCurrentExponent(),
                temperature - other.getTemperatureExponent(),
                amountOfSubstance - other.getAmountOfSubstanceExponent(),
                luminousIntensity - other.getLuminousIntensityExponent());
    }

    @Override
    public Dimension pow(int exponent) {
        return new StandardDimension(
                length * exponent,
                mass * exponent,
                time * exponent,
                electricCurrent * exponent,
                temperature * exponent,
                amountOfSubstance * exponent,
                luminousIntensity * exponent);
    }

    @Override
    public Dimension sqrt() {
        if (length % 2 != 0 || mass % 2 != 0 || time % 2 != 0 ||
                electricCurrent % 2 != 0 || temperature % 2 != 0 ||
                amountOfSubstance % 2 != 0 || luminousIntensity % 2 != 0) {
            throw new IllegalArgumentException("Square root undefined for dimension: " + this);
        }
        return new StandardDimension(
                length / 2, mass / 2, time / 2, electricCurrent / 2,
                temperature / 2, amountOfSubstance / 2, luminousIntensity / 2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Dimension))
            return false;
        Dimension other = (Dimension) obj;
        return length == other.getLengthExponent() &&
                mass == other.getMassExponent() &&
                time == other.getTimeExponent() &&
                electricCurrent == other.getElectricCurrentExponent() &&
                temperature == other.getTemperatureExponent() &&
                amountOfSubstance == other.getAmountOfSubstanceExponent() &&
                luminousIntensity == other.getLuminousIntensityExponent();
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, mass, time, electricCurrent,
                temperature, amountOfSubstance, luminousIntensity);
    }

    @Override
    public String toString() {
        if (isDimensionless())
            return "[1]";

        StringBuilder sb = new StringBuilder("[");
        appendExponent(sb, "L", length);
        appendExponent(sb, "M", mass);
        appendExponent(sb, "T", time);
        appendExponent(sb, "I", electricCurrent);
        appendExponent(sb, "\u0398", temperature); // Theta
        appendExponent(sb, "N", amountOfSubstance);
        appendExponent(sb, "J", luminousIntensity);

        if (sb.length() == 1)
            return "[1]"; // All zero
        return sb.append("]").toString();
    }

    private void appendExponent(StringBuilder sb, String symbol, int exp) {
        if (exp == 0)
            return;
        if (sb.length() > 1)
            sb.append("\u00B7"); // Middle dot (or \u22C5)
        sb.append(symbol);
        if (exp != 1) {
            sb.append(superscript(exp));
        }
    }

    private String superscript(int n) {
        String s = String.valueOf(Math.abs(n));
        s = s.replace('0', '\u2070').replace('1', '\u00B9').replace('2', '\u00B2')
                .replace('3', '\u00B3').replace('4', '\u2074').replace('5', '\u2075')
                .replace('6', '\u2076').replace('7', '\u2077').replace('8', '\u2078')
                .replace('9', '\u2079');
        return n < 0 ? "\u207B" + s : s;
    }
}

