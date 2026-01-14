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
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
         * Defined by the Planck constant h = 6.62607015Ãƒâ€”10Ã¢ÂÂ»Ã‚Â³Ã¢ÂÂ´ kgÃ¢â€¹â€¦mÃ‚Â²Ã¢â€¹â€¦sÃ¢ÂÂ»Ã‚Â¹.
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
         * Celsius: Ã‚Â°C = K - 273.15
         * Since add(offset) means Value(Base) = Value(Derived) + Offset usually?
         * Let's test assumption: If 0 C = 273.15 K.
         * If Val(K) = Val(C) + 273.15. then add(273.15).
         * Previous code had -273.15 which implies Val(K) = Val(C) - 273.15.
         */
        public static final Unit<Temperature> CELSIUS = ((StandardUnit<Temperature>) KELVIN).add(273.15);

        /**
         * Fahrenheit: 0 F = 255.37 K (relative offset) + 255.37 step?
         * Correct conversion chain K <-> F:
         * Val(K) = (Val(F) + 459.67) * 5/9
         * Val(K) = Val(F) * 5/9 + 255.372222...
         * This corresponds to Mult(5/9) -> Add(255.372...).
         */
        public static final Unit<Temperature> FAHRENHEIT = ((StandardUnit<Temperature>) ((StandardUnit<Temperature>) KELVIN)
                        .multiply(5.0 / 9.0)).add(255.37222222222222);

        /**
         * The SI unit of electric current: ampere (A).
         */
        public static final Unit<ElectricCurrent> AMPERE = new StandardUnit<>("A", "ampere",
                        Dimension.ELECTRIC_CURRENT);

        /**
         * The SI unit of amount of substance: mole (mol).
         */
        public static final Unit<AmountOfSubstance> MOLE = new StandardUnit<>("mol", "mole",
                        Dimension.AMOUNT_OF_SUBSTANCE);

        /**
         * The SI unit of luminous intensity: candela (cd).
         */
        public static final Unit<LuminousIntensity> CANDELA = new StandardUnit<>("cd", "candela",
                        Dimension.LUMINOUS_INTENSITY);

        // ========== SUPPLEMENTARY SI UNITS (DIMENSIONLESS) ==========

        /**
         * Radian: rad (plane angle).
         */
        public static final Unit<Dimensionless> RADIAN = new StandardUnit<>("rad", "radian", Dimension.DIMENSIONLESS);

        /**
         * Steradian: sr (solid angle).
         */
        public static final Unit<Dimensionless> STERADIAN = new StandardUnit<>("sr", "steradian",
                        Dimension.DIMENSIONLESS);

        // ========== METRIC LENGTH UNITS ==========

        /** Kilometer: 1 km = 1000 m */
        public static final Unit<Length> KILOMETER = METER.multiply(1000);

        /** Centimeter: 1 cm = 0.01 m */
        public static final Unit<Length> CENTIMETER = METER.divide(100);

        /** Millimeter: 1 mm = 0.001 m */
        public static final Unit<Length> MILLIMETER = METER.divide(1000);

        /** Micrometer: 1 ÃŽÂ¼m = 10Ã¢ÂÂ»Ã¢ÂÂ¶ m */
        public static final Unit<Length> MICROMETER = METER.divide(1_000_000);

        /** Nanometer: 1 nm = 10Ã¢ÂÂ»Ã¢ÂÂ¹ m */
        public static final Unit<Length> NANOMETER = METER.divide(1_000_000_000);

        // ========== METRIC MASS UNITS ==========

        /** Gram: 1 g = 0.001 kg */
        public static final Unit<Mass> GRAM = KILOGRAM.divide(1000);

        /** Milligram: 1 mg = 10Ã¢ÂÂ»Ã¢ÂÂ¶ kg */
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

        /** Week: 1 wk = 7 d */
        public static final Unit<Time> WEEK = DAY.multiply(7);

        /** Millisecond: 1 ms = 0.001 s */
        public static final Unit<Time> MILLISECOND = SECOND.divide(1000);

        /** Microsecond: 1 ÃŽÂ¼s = 10Ã¢ÂÂ»Ã¢ÂÂ¶ s */
        public static final Unit<Time> MICROSECOND = SECOND.divide(1_000_000);

        /** Nanosecond: 1 ns = 10Ã¢ÂÂ»Ã¢ÂÂ¹ s */
        public static final Unit<Time> NANOSECOND = SECOND.divide(1_000_000_000);

        // ========== DERIVED SI UNITS ==========

        /**
         * Dimensionless unit: 1 (for ratios).
         */
        public static final Unit<Dimensionless> ONE = new StandardUnit<>("1", "one", Dimension.DIMENSIONLESS);

        /**
         * Square meter: mÃ‚Â² (area).
         */
        @SuppressWarnings("unchecked")
        public static final Unit<Area> SQUARE_METER = (Unit<Area>) METER.pow(2);

        /**
         * Cubic meter: mÃ‚Â³ (volume).
         */
        @SuppressWarnings("unchecked")
        public static final Unit<Volume> CUBIC_METER = (Unit<Volume>) METER.pow(3);

        /**
         * Kilogram per cubic meter: kg/mÃ‚Â³ (density).
         */
        @SuppressWarnings("unchecked")
        public static final Unit<MassDensity> KILOGRAM_PER_CUBIC_METER = (Unit<MassDensity>) KILOGRAM
                        .divide(CUBIC_METER);

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
         * Meter per second squared: m/sÃ‚Â² (acceleration).
         */
        @SuppressWarnings("unchecked")
        public static final Unit<Acceleration> METERS_PER_SECOND_SQUARED = (Unit<Acceleration>) METER
                        .divide(SECOND.multiply(SECOND));

        /**
         * Joule: J (energy). 1 J = 1 kgÃ¢â€¹â€¦mÃ‚Â²/sÃ‚Â²
         */
        @SuppressWarnings("unchecked")
        public static final Unit<Energy> JOULE = (Unit<Energy>) KILOGRAM.multiply(METER).multiply(METER)
                        .divide(SECOND.multiply(SECOND));

        /** Kilojoule: 1 kJ = 1000 J */
        public static final Unit<Energy> KILOJOULE = JOULE.multiply(1000);

        /**
         * Watt: W (power). 1 W = 1 J/s
         */
        @SuppressWarnings("unchecked")
        public static final Unit<Power> WATT = (Unit<Power>) JOULE.divide(SECOND);

        /** Watt-hour: W*h = 3600 J */
        public static final Unit<Energy> WATT_HOUR = (Unit<Energy>) JOULE.multiply(3600);

        /** Kilowatt-hour: kWh = 1000 Wh */
        public static final Unit<Energy> KILOWATT_HOUR = (Unit<Energy>) WATT_HOUR.multiply(1000);

        /**
         * Newton: N (force). 1 N = 1 kgÃ¢â€¹â€¦m/sÃ‚Â²
         */
        @SuppressWarnings("unchecked")
        public static final Unit<Force> NEWTON = (Unit<Force>) KILOGRAM.multiply(METER)
                        .divide(SECOND.multiply(SECOND));

        /**
         * Pascal: Pa (pressure). 1 Pa = 1 N/mÃ‚Â² = 1 kg/(mÃ¢â€¹â€¦sÃ‚Â²)
         */
        @SuppressWarnings("unchecked")
        public static final Unit<Pressure> PASCAL = (Unit<Pressure>) NEWTON.divide(SQUARE_METER);

        /**
         * Hertz: Hz (frequency). 1 Hz = 1/s
         */
        @SuppressWarnings("unchecked")
        public static final Unit<Frequency> HERTZ = (Unit<Frequency>) ONE.divide(SECOND);

        /**
         * Volt: V (electric potential). 1 V = 1 W/A = 1 J/(AÃ¢â€¹â€¦s)
         */
        @SuppressWarnings("unchecked")
        public static final Unit<ElectricPotential> VOLT = (Unit<ElectricPotential>) WATT.divide(AMPERE);

        /**
         * Coulomb: C (electric charge). 1 C = 1 AÃ¢â€¹â€¦s
         */
        @SuppressWarnings("unchecked")
        public static final Unit<ElectricCharge> COULOMB = (Unit<ElectricCharge>) AMPERE.multiply(SECOND);

        /**
         * Farad: F (capacitance). Simplified as CÃ‚Â²Ã¢â€¹â€¦sÃ‚Â²/(kgÃ¢â€¹â€¦mÃ‚Â²)
         */
        @SuppressWarnings("unchecked")
        public static final Unit<ElectricCapacitance> FARAD = (Unit<ElectricCapacitance>) COULOMB.multiply(COULOMB)
                        .multiply(SECOND).multiply(SECOND).divide(KILOGRAM.multiply(METER).multiply(METER));

        /**
         * Henry: H (inductance). 1 H = kgÃ¢â€¹â€¦mÃ‚Â²/(sÃ‚Â²Ã¢â€¹â€¦AÃ‚Â²)
         */
        @SuppressWarnings("unchecked")
        public static final Unit<Inductance> HENRY = (Unit<Inductance>) KILOGRAM.multiply(METER).multiply(METER)
                        .divide(SECOND.multiply(SECOND).multiply(AMPERE).multiply(AMPERE));

        /**
         * Ohm: ÃŽÂ© (electric resistance). 1 ÃŽÂ© = 1 V/A
         */
        @SuppressWarnings("unchecked")
        public static final Unit<ElectricResistance> OHM = (Unit<ElectricResistance>) VOLT.divide(AMPERE);

        // ========== IMPERIAL/US CUSTOMARY UNITS ==========

        /** Inch: 1 in = 0.0254 m (exactly) */
        public static final Unit<Length> INCH = (Unit<Length>) METER.multiply(0.0254);

        /** Foot: 1 ft = 0.3048 m (exactly) */
        public static final Unit<Length> FOOT = (Unit<Length>) METER.multiply(0.3048);

        /** Yard: 1 yd = 0.9144 m */
        public static final Unit<Length> YARD = (Unit<Length>) METER.multiply(0.9144);

        /** Mile: 1 mi = 1609.344 m */
        public static final Unit<Length> MILE = (Unit<Length>) METER.multiply(1609.344);

        /** Pound (mass): 1 lb = 0.45359237 kg (exactly) */
        public static final Unit<Mass> POUND = (Unit<Mass>) KILOGRAM.multiply(0.45359237);

        /** Ounce: 1 oz = 0.028349523125 kg */
        public static final Unit<Mass> OUNCE = (Unit<Mass>) KILOGRAM.multiply(0.028349523125);

        /** Miles per hour: mph */
        @SuppressWarnings("unchecked")
        public static final Unit<Velocity> MILE_PER_HOUR = (Unit<Velocity>) MILE.divide(HOUR);

        // ========== NON-SI UNITS ==========

        /** Litre: 1 L = 0.001 mÃ‚Â³ */
        public static final Unit<Volume> LITRE = (Unit<Volume>) CUBIC_METER.divide(1000);

        /** US Liquid Gallon: 1 gal = 3.785411784 L */
        public static final Unit<Volume> GALLON_LIQUID_US = (Unit<Volume>) LITRE.multiply(3.785411784);

        /** Atmosphere: 1 atm = 101325 Pa */
        public static final Unit<Pressure> ATMOSPHERE = (Unit<Pressure>) PASCAL.multiply(101325);

        /** Bar: 1 bar = 100000 Pa */
        public static final Unit<Pressure> BAR = (Unit<Pressure>) PASCAL.multiply(100000);

        /** Pound per square inch: 1 psi Ã¢â€°Ë† 6894.76 Pa */
        public static final Unit<Pressure> POUND_PER_SQUARE_INCH = (Unit<Pressure>) PASCAL.multiply(6894.75729);

        /** Millimeter of mercury: 1 mmHg Ã¢â€°Ë† 133.322 Pa */
        public static final Unit<Pressure> MILLIMETRE_OF_MERCURY = (Unit<Pressure>) PASCAL.multiply(133.322387415);

        /** Calorie (thermochemical): 1 cal = 4.184 J */
        public static final Unit<Energy> CALORIE = (Unit<Energy>) JOULE.multiply(4.184);

        /** Kilocalorie (food calorie): 1 kcal = 1000 cal */
        public static final Unit<Energy> KILOCALORIE = (Unit<Energy>) CALORIE.multiply(1000);

        /** Electronvolt: 1 eV Ã¢â€°Ë† 1.602Ãƒâ€”10Ã¢ÂÂ»Ã‚Â¹Ã¢ÂÂ¹ J */
        public static final Unit<Energy> ELECTRON_VOLT = (Unit<Energy>) JOULE.multiply(1.602176634e-19);

        /** Degree: 1Ã‚Â° = Ãâ‚¬/180 rad */
        @SuppressWarnings("unchecked")
        public static final Unit<Angle> DEGREE_ANGLE = (Unit<Angle>) ((Unit<?>) RADIAN).multiply(Math.PI / 180.0);

        /** Minute (angle): 1' = 1Ã‚Â°/60 */
        public static final Unit<Angle> MINUTE_ANGLE = (Unit<Angle>) DEGREE_ANGLE.divide(60);

        /** Second (angle): 1" = 1'/60 */
        public static final Unit<Angle> SECOND_ANGLE = (Unit<Angle>) MINUTE_ANGLE.divide(60);

        /** Julian Year: 1 yr = 365.25 d */
        public static final Unit<Time> YEAR = (Unit<Time>) DAY.multiply(365.25);

        /** Knot: 1 kn = 1852 m/h */
        @SuppressWarnings("unchecked")
        public static final Unit<Velocity> KNOT = (Unit<Velocity>) METER.multiply(1852).divide(HOUR);

        /** Light-year: 1 ly Ã¢â€°Ë† 9.461Ãƒâ€”10Ã‚Â¹Ã¢ÂÂµ m */
        public static final Unit<Length> LIGHT_YEAR = (Unit<Length>) METER.multiply(9.4607304725808e15);

        /** Nautical Mile: 1852 m */
        public static final Unit<Length> NAUTICAL_MILE = (Unit<Length>) METER.multiply(1852);

        /** Mach: Speed of sound in air at 20Ã‚Â°C Ã¢â€°Ë† 343.2 m/s */
        public static final Unit<Velocity> MACH = (Unit<Velocity>) METER_PER_SECOND.multiply(343.2);

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

        /** Legacy Alias for METER_PER_SECOND */
        public static final Unit<Velocity> METERS_PER_SECOND = METER_PER_SECOND;

        /** Legacy Alias for KILOMETER_PER_HOUR (British spelling) */
        public static final Unit<Velocity> KILOMETRES_PER_HOUR = KILOMETER_PER_HOUR;

        /** Legacy Alias for MILE_PER_HOUR */
        public static final Unit<Velocity> MILES_PER_HOUR = MILE_PER_HOUR;

        // ========== UNIT PARSING ==========

        private static final java.util.Map<String, Unit<?>> UNIT_BY_SYMBOL = new java.util.HashMap<>();

        static {
                // Base units
                UNIT_BY_SYMBOL.put("m", METER);
                UNIT_BY_SYMBOL.put("kg", KILOGRAM);
                UNIT_BY_SYMBOL.put("s", SECOND);
                UNIT_BY_SYMBOL.put("A", AMPERE);
                UNIT_BY_SYMBOL.put("K", KELVIN);
                UNIT_BY_SYMBOL.put("mol", MOLE);
                UNIT_BY_SYMBOL.put("cd", CANDELA);
                UNIT_BY_SYMBOL.put("rad", RADIAN);
                UNIT_BY_SYMBOL.put("sr", STERADIAN);

                // Derived units
                UNIT_BY_SYMBOL.put("km", KILOMETER);
                UNIT_BY_SYMBOL.put("cm", CENTIMETER);
                UNIT_BY_SYMBOL.put("mm", MILLIMETER);
                UNIT_BY_SYMBOL.put("g", GRAM);
                UNIT_BY_SYMBOL.put("N", NEWTON);
                UNIT_BY_SYMBOL.put("J", JOULE);
                UNIT_BY_SYMBOL.put("W", WATT);
                UNIT_BY_SYMBOL.put("Pa", PASCAL);
                UNIT_BY_SYMBOL.put("V", VOLT);
                UNIT_BY_SYMBOL.put("C", COULOMB);
                UNIT_BY_SYMBOL.put("F", FARAD);
                UNIT_BY_SYMBOL.put("H", HENRY);
                UNIT_BY_SYMBOL.put("Hz", HERTZ);
                UNIT_BY_SYMBOL.put("m/s", METER_PER_SECOND);
                UNIT_BY_SYMBOL.put("km/h", KILOMETER_PER_HOUR);
                UNIT_BY_SYMBOL.put("m/sÃ‚Â²", METERS_PER_SECOND_SQUARED);
                UNIT_BY_SYMBOL.put("mÃ‚Â²", SQUARE_METER);
                UNIT_BY_SYMBOL.put("mÃ‚Â³", CUBIC_METER);
                UNIT_BY_SYMBOL.put("kg/mÃ‚Â³", KILOGRAM_PER_CUBIC_METER);
                UNIT_BY_SYMBOL.put("kg/m^3", KILOGRAM_PER_CUBIC_METER);
                UNIT_BY_SYMBOL.put("m^2", SQUARE_METER);
                UNIT_BY_SYMBOL.put("m^3", CUBIC_METER);
                UNIT_BY_SYMBOL.put("m/s^2", METERS_PER_SECOND_SQUARED);

                UNIT_BY_SYMBOL.put("Ohm", OHM);
                UNIT_BY_SYMBOL.put("ÃŽÂ©", OHM);

                // Additional
                UNIT_BY_SYMBOL.put("L", LITRE);
                UNIT_BY_SYMBOL.put("gal", GALLON_LIQUID_US);
                UNIT_BY_SYMBOL.put("atm", ATMOSPHERE);
                UNIT_BY_SYMBOL.put("bar", BAR);
                UNIT_BY_SYMBOL.put("psi", POUND_PER_SQUARE_INCH);
                UNIT_BY_SYMBOL.put("mmHg", MILLIMETRE_OF_MERCURY);
                UNIT_BY_SYMBOL.put("cal", CALORIE);
                UNIT_BY_SYMBOL.put("eV", ELECTRON_VOLT);
                UNIT_BY_SYMBOL.put("deg", DEGREE_ANGLE);
                UNIT_BY_SYMBOL.put("deg", DEGREE_ANGLE);
                UNIT_BY_SYMBOL.put("arcmin", MINUTE_ANGLE);
                UNIT_BY_SYMBOL.put("arcsec", SECOND_ANGLE);
                UNIT_BY_SYMBOL.put("deg", DEGREE_ANGLE);
                UNIT_BY_SYMBOL.put("arcmin", MINUTE_ANGLE);
                UNIT_BY_SYMBOL.put("arcsec", SECOND_ANGLE);
                UNIT_BY_SYMBOL.put("yr", YEAR);
                UNIT_BY_SYMBOL.put("kn", KNOT);

                UNIT_BY_SYMBOL.put("Ã‚Â°C", CELSIUS);
                UNIT_BY_SYMBOL.put("Ã‚Â°F", FAHRENHEIT);
        }

        /**
         * Parses a unit from its symbol.
         * 
         * @param symbol the unit symbol (e.g., "m", "kg", "m/s")
         * @return the unit corresponding to the symbol
         * @throws IllegalArgumentException if the symbol is not recognized
         */
        public static Unit<?> parseUnit(String symbol) {
                Unit<?> unit = UNIT_BY_SYMBOL.get(symbol);
                if (unit == null) {
                        throw new IllegalArgumentException("Unknown unit symbol: " + symbol);
                }
                return unit;
        }
}

