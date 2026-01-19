package org.jscience.earth.atmosphere;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Temperature;
import org.jscience.measure.Units;
import org.jscience.measure.Quantities;

/**
 * Calculators for humid air properties (Psychrometrics).
 */
public final class Psychrometrics {

    private Psychrometrics() {}

    /**
     * Calculates Saturation Vapor Pressure (hPa) using Magnus-Tetens formula.
     */
    public static Real saturationVaporPressure(Quantity<Temperature> temperature) {
        double t = temperature.to(Units.CELSIUS).getValue().doubleValue();
        double es = 6.112 * Math.exp((17.67 * t) / (t + 243.5));
        return Real.of(es);
    }

    /**
     * Calculates Dew Point temperature.
     */
    public static Quantity<Temperature> dewPoint(Quantity<Temperature> temperature, Real relativeHumidity) {
        double t = temperature.to(Units.CELSIUS).getValue().doubleValue();
        double rh = relativeHumidity.doubleValue();
        double gamma = (17.67 * t) / (t + 243.5) + Math.log(rh / 100.0);
        double dp = (243.5 * gamma) / (17.67 - gamma);
        return Quantities.create(dp, Units.CELSIUS);
    }

    /**
     * Calculates Relative Humidity.
     */
    public static Real relativeHumidity(Quantity<Temperature> temperature, Quantity<Temperature> dewPoint) {
        Real es = saturationVaporPressure(temperature);
        Real e = saturationVaporPressure(dewPoint);
        return e.divide(es).multiply(Real.of(100.0));
    }

    /**
     * Calculates Enthalpy of moist air (kJ/kg).
     * h = cpa*t + w*(hwe + cpw*t)
     */
    public static Real enthalpy(Quantity<Temperature> temperature, Real humidityRatio) {
        double t = temperature.to(Units.CELSIUS).getValue().doubleValue();
        double w = humidityRatio.doubleValue();
        double h = 1.006 * t + w * (2501 + 1.86 * t);
        return Real.of(h);
    }
}
