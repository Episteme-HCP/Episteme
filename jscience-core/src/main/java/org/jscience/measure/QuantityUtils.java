/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.quantity.*;
import org.jscience.measure.units.SI;

/**
 * Utility methods for working with Quantities.
 * <p>
 * Provides convenient factory methods for common physical quantities.
 * All methods return strongly-typed Quantity objects using Real precision.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class QuantityUtils {

    private QuantityUtils() {
    }

    // ========== Length ==========
    public static Quantity<Length> meters(double value) {
        return Quantities.create(Real.of(value), SI.METRE);
    }

    public static Quantity<Length> kilometers(double value) {
        return Quantities.create(Real.of(value * 1000), SI.METRE);
    }

    public static Quantity<Length> centimeters(double value) {
        return Quantities.create(Real.of(value / 100), SI.METRE);
    }

    public static Quantity<Length> millimeters(double value) {
        return Quantities.create(Real.of(value / 1000), SI.METRE);
    }

    // ========== Mass ==========
    public static Quantity<Mass> kilograms(double value) {
        return Quantities.create(Real.of(value), SI.KILOGRAM);
    }

    public static Quantity<Mass> grams(double value) {
        return Quantities.create(Real.of(value / 1000), SI.KILOGRAM);
    }

    public static Quantity<Mass> milligrams(double value) {
        return Quantities.create(Real.of(value / 1_000_000), SI.KILOGRAM);
    }

    // ========== Time ==========
    public static Quantity<Time> seconds(double value) {
        return Quantities.create(Real.of(value), SI.SECOND);
    }

    public static Quantity<Time> minutes(double value) {
        return Quantities.create(Real.of(value * 60), SI.SECOND);
    }

    public static Quantity<Time> hours(double value) {
        return Quantities.create(Real.of(value * 3600), SI.SECOND);
    }

    // ========== Temperature ==========
    public static Quantity<Temperature> kelvin(double value) {
        return Quantities.create(Real.of(value), SI.KELVIN);
    }

    public static Quantity<Temperature> celsius(double value) {
        return Quantities.create(Real.of(value + 273.15), SI.KELVIN);
    }

    public static Quantity<Temperature> fahrenheit(double value) {
        return Quantities.create(Real.of((value - 32) * 5 / 9 + 273.15), SI.KELVIN);
    }

    // ========== Energy ==========
    public static Quantity<Energy> joules(double value) {
        return Quantities.create(Real.of(value), SI.JOULE);
    }

    public static Quantity<Energy> kilojoules(double value) {
        return Quantities.create(Real.of(value * 1000), SI.JOULE);
    }

    public static Quantity<Energy> electronVolts(double value) {
        return Quantities.create(Real.of(value * 1.602176634e-19), SI.JOULE);
    }

    // ========== Force ==========
    public static Quantity<Force> newtons(double value) {
        return Quantities.create(Real.of(value), SI.NEWTON);
    }

    // ========== Pressure ==========
    public static Quantity<Pressure> pascals(double value) {
        return Quantities.create(Real.of(value), SI.PASCAL);
    }

    public static Quantity<Pressure> atmospheres(double value) {
        return Quantities.create(Real.of(value * 101325), SI.PASCAL);
    }

    public static Quantity<Pressure> bars(double value) {
        return Quantities.create(Real.of(value * 100000), SI.PASCAL);
    }

    // ========== Electric ==========
    public static Quantity<ElectricCurrent> amperes(double value) {
        return Quantities.create(Real.of(value), SI.AMPERE);
    }

    public static Quantity<ElectricPotential> volts(double value) {
        return Quantities.create(Real.of(value), SI.VOLT);
    }

    public static Quantity<ElectricCharge> coulombs(double value) {
        return Quantities.create(Real.of(value), SI.COULOMB);
    }

    // ========== Frequency ==========
    public static Quantity<Frequency> hertz(double value) {
        return Quantities.create(Real.of(value), SI.HERTZ);
    }

    public static Quantity<Frequency> kilohertz(double value) {
        return Quantities.create(Real.of(value * 1000), SI.HERTZ);
    }

    public static Quantity<Frequency> megahertz(double value) {
        return Quantities.create(Real.of(value * 1_000_000), SI.HERTZ);
    }

    // ========== Power ==========
    public static Quantity<Power> watts(double value) {
        return Quantities.create(Real.of(value), SI.WATT);
    }

    public static Quantity<Power> kilowatts(double value) {
        return Quantities.create(Real.of(value * 1000), SI.WATT);
    }
}