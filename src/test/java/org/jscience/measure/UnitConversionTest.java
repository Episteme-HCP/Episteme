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
    @SuppressWarnings("unchecked")
    public void testAffineUnitBehavior() {
        // Test manual affine unit creation
        Unit<Temperature> kelvin = Units.KELVIN;
        @SuppressWarnings("unchecked")
        Unit<Temperature> celsius = (Unit<Temperature>) ((StandardUnit<Temperature>) kelvin).add(-273.15);

        Real kVal = Real.of(273.15);
        // 273.15 K -> 0 C
        Real cVal = kelvin.getConverterTo(celsius).convert(kVal);
        Assertions.assertEquals(0.0, cVal.doubleValue(), DELTA);

        // 0 C -> 273.15 K
        Real kVal2 = celsius.getConverterTo(kelvin).convert(Real.ZERO);
        Assertions.assertEquals(273.15, kVal2.doubleValue(), DELTA);
    }
}
