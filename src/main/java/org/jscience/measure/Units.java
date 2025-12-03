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
package org.jscience.measure;

import org.jscience.measure.quantity.*;

/**
 * Standard units of measurement.
 * <p>
 * Provides SI base units, derived units, and common non-SI units.
 * All units follow UCUM (Unified Code for Units of Measure) conventions
 * where applicable.
 * </p>
 * <p>
 * <b>Example Usage:</b>
 * 
 * <pre>{@code
 * import static org.jscience.measure.Units.*;
 * 
 * Quantity<Length> distance = Quantities.create(100, METER);
 * Quantity<Velocity> speed = Quantities.create(50, KILOMETER_PER_HOUR);
 * }</pre>
 * </p>
 * 
 * @author Silvere Martin-Michiellot (silvere.martin@gmail.com)
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 * 
 * @see Unit
 * @see Quantity
 */
public final class Units {

    private Units() {
        // Utility class
    }

    // ========== SI BASE UNITS ==========

    /**
     * The SI unit of length: meter (m).
     * <p>
     * Defined by the distance light travels in vacuum in 1/299,792,458 second.
     * </p>
     */
    public static final Unit<Length> METER = new StandardUnit<>("m", "meter", Dimension.LENGTH);

    /**
     * The SI unit of mass: kilogram (kg).
     * <p>
     * Defined by the Planck constant h = 6.62607015×10⁻³⁴ kg⋅m²⋅s⁻¹.
     * </p>
     */
    public static final Unit<Mass> KILOGRAM = new StandardUnit<>("kg", "kilogram", Dimension.MASS);

    /**
     * The SI unit of time: second (s).
     * <p>
     * Defined by the cesium-133 hyperfine transition frequency.
     * </p>
     */
    public static final Unit<Time> SECOND = new StandardUnit<>("s", "second", Dimension.TIME);

    /**
     * The SI unit of temperature: kelvin (K).
     */
    public static final Unit<Temperature> KELVIN = new StandardUnit<>("K", "kelvin", Dimension.TEMPERATURE);

    /**
     * The SI unit of electric current: ampere (A).
     */
    public static final Unit<ElectricCurrent> AMPERE = new StandardUnit<>("A", "ampere", Dimension.ELECTRIC_CURRENT);

    /**
     * The SI unit of amount of substance: mole (mol).
     */
    public static final Unit<AmountOfSubstance> MOLE = new StandardUnit<>("mol", "mole", Dimension.AMOUNT_OF_SUBSTANCE);

    // ========== METRIC LENGTH UNITS ==========

    /** Kilometer: 1 km = 1000 m */
    public static final Unit<Length> KILOMETER = METER.multiply(1000);

    /** Centimeter: 1 cm = 0.01 m */
    public static final Unit<Length> CENTIMETER = METER.divide(100);

    /** Millimeter: 1 mm = 0.001 m */
    public static final Unit<Length> MILLIMETER = METER.divide(1000);

    /** Micrometer: 1 μm = 10⁻⁶ m */
    public static final Unit<Length> MICROMETER = METER.divide(1_000_000);

    /** Nanometer: 1 nm = 10⁻⁹ m */
    public static final Unit<Length> NANOMETER = METER.divide(1_000_000_000);

    // ========== METRIC MASS UNITS ==========

    /** Gram: 1 g = 0.001 kg */
    public static final Unit<Mass> GRAM = KILOGRAM.divide(1000);

    /** Milligram: 1 mg = 10⁻⁶ kg */
    public static final Unit<Mass> MILLIGRAM = GRAM.divide(1000);

    /** Tonne (metric ton): 1 t = 1000 kg */
    public static final Unit<Mass> TONNE = KILOGRAM.multiply(1000);

    // ========== TIME UNITS ==========

    /** Minute: 1 min = 60 s */
    public static final Unit<Time> MINUTE = SECOND.multiply(60);

    /** Hour: 1 h = 3600 s */
    public static final Unit<Time> HOUR = SECOND.multiply(3600);

    /** Day: 1 d = 86400 s */
    public static final Unit<Time> DAY = SECOND.multiply(86400);

    /** Millisecond: 1 ms = 0.001 s */
    public static final Unit<Time> MILLISECOND = SECOND.divide(1000);

    /** Microsecond: 1 μs = 10⁻⁶ s */
    public static final Unit<Time> MICROSECOND = SECOND.divide(1_000_000);

    /** Nanosecond: 1 ns = 10⁻⁹ s */
    public static final Unit<Time> NANOSECOND = SECOND.divide(1_000_000_000);

    // ========== DERIVED SI UNITS ==========

    /**
     * Meter per second: m/s (velocity).
     */
    @SuppressWarnings("unchecked")
    public static final Unit<Velocity> METER_PER_SECOND = (Unit<Velocity>) METER.divide(SECOND);

    /**
     * Kilometer per hour: km/h (velocity).
     */
    @SuppressWarnings("unchecked")
    public static final Unit<Velocity> KILOMETER_PER_HOUR = (Unit<Velocity>) KILOMETER.divide(HOUR);

    /**
     * Meter per second squared: m/s² (acceleration).
     */
    @SuppressWarnings("unchecked")
    public static final Unit<Acceleration> METERS_PER_SECOND_SQUARED = (Unit<Acceleration>) METER
            .divide(SECOND.multiply(SECOND));

    /**
     * Joule: J (energy). 1 J = 1 kg⋅m²/s²
     */
    @SuppressWarnings("unchecked")
    public static final Unit<Energy> JOULE = (Unit<Energy>) KILOGRAM.multiply(METER).multiply(METER)
            .divide(SECOND.multiply(SECOND));

    /**
     * Watt: W (power). 1 W = 1 J/s
     */
    @SuppressWarnings("unchecked")
    public static final Unit<Power> WATT = (Unit<Power>) JOULE.divide(SECOND);

    /**
     * Coulomb: C (electric charge). 1 C = 1 A⋅s
     */
    @SuppressWarnings("unchecked")
    public static final Unit<ElectricCharge> COULOMB = (Unit<ElectricCharge>) AMPERE.multiply(SECOND);

    /**
     * Farad: F (capacitance). Simplified as C²⋅s²/(kg⋅m²)
     */
    @SuppressWarnings("unchecked")
    public static final Unit<ElectricCapacitance> FARAD = (Unit<ElectricCapacitance>) COULOMB.multiply(COULOMB)
            .multiply(SECOND).multiply(SECOND).divide(KILOGRAM.multiply(METER).multiply(METER));

    /**
     * Henry: H (inductance). 1 H = kg⋅m²/(s²⋅A²)
     */
    @SuppressWarnings("unchecked")
    public static final Unit<Inductance> HENRY = (Unit<Inductance>) KILOGRAM.multiply(METER).multiply(METER)
            .divide(SECOND.multiply(SECOND).multiply(AMPERE).multiply(AMPERE));

    /**
     * Dimensionless unit: 1 (for ratios).
     */
    public static final Unit<Dimensionless> ONE = new StandardUnit<>("1", "one", Dimension.DIMENSIONLESS);

    /**
     * Cubic meter: m³ (volume).
     */
    @SuppressWarnings("unchecked")
    public static final Unit<Volume> CUBIC_METER = (Unit<Volume>) METER.pow(3);

    /**
     * Square meter: m² (area).
     */
    @SuppressWarnings("unchecked")
    public static final Unit<Area> SQUARE_METER = (Unit<Area>) METER.pow(2);

    // ========== IMPERIAL/US CUSTOMARY UNITS ==========

    /** Inch: 1 in = 0.0254 m (exactly) */
    @SuppressWarnings("unchecked")
    public static final Unit<Length> INCH = (Unit<Length>) METER.multiply(0.0254);

    /** Foot: 1 ft = 0.3048 m (exactly) */
    @SuppressWarnings("unchecked")
    public static final Unit<Length> FOOT = (Unit<Length>) METER.multiply(0.3048);

    /** Yard: 1 yd = 0.9144 m */
    @SuppressWarnings("unchecked")
    public static final Unit<Length> YARD = (Unit<Length>) METER.multiply(0.9144);

    /** Mile: 1 mi = 1609.344 m */
    @SuppressWarnings("unchecked")
    public static final Unit<Length> MILE = (Unit<Length>) METER.multiply(1609.344);

    /** Pound (mass): 1 lb = 0.45359237 kg (exactly) */
    @SuppressWarnings("unchecked")
    public static final Unit<Mass> POUND = (Unit<Mass>) KILOGRAM.multiply(0.45359237);

    /** Ounce: 1 oz = 0.028349523125 kg */
    @SuppressWarnings("unchecked")
    public static final Unit<Mass> OUNCE = (Unit<Mass>) KILOGRAM.multiply(0.028349523125);

    /** Miles per hour: mph */
    @SuppressWarnings("unchecked")
    public static final Unit<Velocity> MILE_PER_HOUR = (Unit<Velocity>) MILE.divide(HOUR);

    // ========== COMMON ALIASES ==========

    /** Alias for METER */
    public static final Unit<Length> M = METER;

    /** Alias for KILOMETER */
    public static final Unit<Length> KM = KILOMETER;

    /** Alias for KILOGRAM */
    public static final Unit<Mass> KG = KILOGRAM;

    /** Alias for GRAM */
    public static final Unit<Mass> G = GRAM;

    /** Alias for SECOND */
    public static final Unit<Time> S = SECOND;

    /** Alias for METER_PER_SECOND */
    public static final Unit<Velocity> MPS = METER_PER_SECOND;

    /** Alias for KILOMETER_PER_HOUR */
    public static final Unit<Velocity> KPH = KILOMETER_PER_HOUR;

    /** Alias for MILE_PER_HOUR */
    public static final Unit<Velocity> MPH = MILE_PER_HOUR;
}
