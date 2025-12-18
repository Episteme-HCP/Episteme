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
package org.jscience.measure.units;

import org.jscience.measure.*;
import org.jscience.measure.quantity.*;

/**
 * International System of Units (SI) - Complete Implementation.
 */
@SuppressWarnings("unchecked")
public final class SI {
        private SI() {
        }

        // ========== BASE UNITS ==========
        // ========== BASE UNITS ==========
        public static final Unit<Length> METRE = new StandardUnit<Length>("m", "meter",
                        org.jscience.measure.Dimension.LENGTH);
        public static final Unit<Mass> KILOGRAM = new StandardUnit<Mass>("kg", "kilogram", Dimension.MASS);
        public static final Unit<Time> SECOND = new StandardUnit<Time>("s", "second", Dimension.TIME);
        public static final Unit<ElectricCurrent> AMPERE = new StandardUnit<ElectricCurrent>("A", "ampere",
                        Dimension.ELECTRIC_CURRENT);
        public static final Unit<Temperature> KELVIN = new StandardUnit<Temperature>("K", "kelvin",
                        Dimension.TEMPERATURE);
        public static final Unit<AmountOfSubstance> MOLE = new StandardUnit<AmountOfSubstance>("mol", "mole",
                        Dimension.AMOUNT_OF_SUBSTANCE);
        public static final Unit<LuminousIntensity> CANDELA = new StandardUnit<LuminousIntensity>("cd", "candela",
                        Dimension.LUMINOUS_INTENSITY);

        // ========== DERIVED UNITS ==========
        public static final Unit<Frequency> HERTZ = (Unit<Frequency>) SECOND.pow(-1);
        public static final Unit<Force> NEWTON = (Unit<Force>) KILOGRAM.multiply(METRE).divide(SECOND.pow(2));
        public static final Unit<Pressure> PASCAL = (Unit<Pressure>) NEWTON.divide(METRE.pow(2));
        public static final Unit<Energy> JOULE = (Unit<Energy>) NEWTON.multiply(METRE);
        public static final Unit<Power> WATT = (Unit<Power>) JOULE.divide(SECOND);
        public static final Unit<ElectricCharge> COULOMB = (Unit<ElectricCharge>) AMPERE.multiply(SECOND);
        public static final Unit<ElectricPotential> VOLT = (Unit<ElectricPotential>) WATT.divide(AMPERE);
        public static final Unit<ElectricCapacitance> FARAD = (Unit<ElectricCapacitance>) COULOMB.divide(VOLT);
        public static final Unit<ElectricResistance> OHM = (Unit<ElectricResistance>) VOLT.divide(AMPERE);
        public static final Unit<MagneticFlux> WEBER = (Unit<MagneticFlux>) VOLT.multiply(SECOND);
        public static final Unit<MagneticFluxDensity> TESLA = (Unit<MagneticFluxDensity>) WEBER.divide(METRE.pow(2));
        public static final Unit<Inductance> HENRY = (Unit<Inductance>) WEBER.divide(AMPERE);
}
