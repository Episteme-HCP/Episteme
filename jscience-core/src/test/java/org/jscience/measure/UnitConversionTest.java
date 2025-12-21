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
package org.jscience.measure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.quantity.Temperature;

/**
 * Tests for Units and UnitConversion.
 */
public class UnitConversionTest {

    private static final double DELTA = 1e-9;

    @Test
    public void testTemperatureConversions() {
        // Celsius to Fahrenheit
        // 0°C = 32°F
        Assertions.assertEquals(32.0, UnitConversion.celsiusToFahrenheit(0), DELTA);
        // 100°C = 212°F
        Assertions.assertEquals(212.0, UnitConversion.celsiusToFahrenheit(100), DELTA);
        // -40°C = -40°F
        Assertions.assertEquals(-40.0, UnitConversion.celsiusToFahrenheit(-40), DELTA);

        // Fahrenheit to Celsius
        Assertions.assertEquals(0.0, UnitConversion.fahrenheitToCelsius(32), DELTA);
        Assertions.assertEquals(100.0, UnitConversion.fahrenheitToCelsius(212), DELTA);

        // Kelvin conversions
        // 0°C = 273.15K
        Assertions.assertEquals(273.15, UnitConversion.celsiusToKelvin(0), DELTA);
        Assertions.assertEquals(0.0, UnitConversion.kelvinToCelsius(273.15), DELTA);
    }

    @Test
    public void testLengthConversions() {
        // 1 inch = 0.0254 m
        Assertions.assertEquals(0.0254, UnitConversion.inchesToMeters(1), DELTA);
        Assertions.assertEquals(1.0, UnitConversion.metersToInches(0.0254), DELTA);

        // 1 foot = 0.3048 m
        Assertions.assertEquals(0.3048, UnitConversion.feetToMeters(1), DELTA);
    }

    @Test

    public void testAffineUnitBehavior() {
        // Test manual affine unit creation
        Unit<Temperature> kelvin = Units.KELVIN;

        Unit<Temperature> celsius = (Unit<Temperature>) ((StandardUnit<Temperature>) kelvin).add(273.15);

        Real kVal = Real.of(273.15);
        // 273.15 K -> 0 C
        Real cVal = kelvin.getConverterTo(celsius).convert(kVal);
        Assertions.assertEquals(0.0, cVal.doubleValue(), DELTA);

        // 0 C -> 273.15 K
        Real kVal2 = celsius.getConverterTo(kelvin).convert(Real.ZERO);
        Assertions.assertEquals(273.15, kVal2.doubleValue(), DELTA);
    }
}
