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

import org.jscience.measure.quantity.Angle;

/**
 * Unit conversion utilities.
 * <p>
 * Moved from physics package. Now uses Quantity for automatic conversion.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class UnitConversion {

    private UnitConversion() {
    }

    // --- Length ---

    public static double metersToFeet(double m) {
        return Quantities.create(m, Units.METER).to(Units.FOOT).getValue().doubleValue();
    }

    public static double feetToMeters(double ft) {
        return Quantities.create(ft, Units.FOOT).to(Units.METER).getValue().doubleValue();
    }

    public static double metersToMiles(double m) {
        return Quantities.create(m, Units.METER).to(Units.MILE).getValue().doubleValue();
    }

    public static double milesToMeters(double mi) {
        return Quantities.create(mi, Units.MILE).to(Units.METER).getValue().doubleValue();
    }

    public static double metersToInches(double m) {
        return Quantities.create(m, Units.METER).to(Units.INCH).getValue().doubleValue();
    }

    public static double inchesToMeters(double in) {
        return Quantities.create(in, Units.INCH).to(Units.METER).getValue().doubleValue();
    }

    public static double kmToMiles(double km) {
        return Quantities.create(km, Units.KILOMETER).to(Units.MILE).getValue().doubleValue();
    }

    public static double milesToKm(double mi) {
        return Quantities.create(mi, Units.MILE).to(Units.KILOMETER).getValue().doubleValue();
    }

    // --- Temperature ---

    public static double celsiusToFahrenheit(double c) {
        return Quantities.create(c, Units.CELSIUS).to(Units.FAHRENHEIT).getValue().doubleValue();
    }

    public static double fahrenheitToCelsius(double f) {
        return Quantities.create(f, Units.FAHRENHEIT).to(Units.CELSIUS).getValue().doubleValue();
    }

    public static double celsiusToKelvin(double c) {
        return Quantities.create(c, Units.CELSIUS).to(Units.KELVIN).getValue().doubleValue();
    }

    public static double kelvinToCelsius(double k) {
        return Quantities.create(k, Units.KELVIN).to(Units.CELSIUS).getValue().doubleValue();
    }

    public static double fahrenheitToKelvin(double f) {
        return Quantities.create(f, Units.FAHRENHEIT).to(Units.KELVIN).getValue().doubleValue();
    }

    public static double kelvinToFahrenheit(double k) {
        return Quantities.create(k, Units.KELVIN).to(Units.FAHRENHEIT).getValue().doubleValue();
    }

    // --- Mass ---

    public static double kgToPounds(double kg) {
        return Quantities.create(kg, Units.KILOGRAM).to(Units.POUND).getValue().doubleValue();
    }

    public static double poundsToKg(double lb) {
        return Quantities.create(lb, Units.POUND).to(Units.KILOGRAM).getValue().doubleValue();
    }

    public static double kgToOunces(double kg) {
        return Quantities.create(kg, Units.KILOGRAM).to(Units.OUNCE).getValue().doubleValue();
    }

    public static double ouncesToKg(double oz) {
        return Quantities.create(oz, Units.OUNCE).to(Units.KILOGRAM).getValue().doubleValue();
    }

    public static double gramsToKg(double g) {
        return Quantities.create(g, Units.GRAM).to(Units.KILOGRAM).getValue().doubleValue();
    }

    public static double kgToGrams(double kg) {
        return Quantities.create(kg, Units.KILOGRAM).to(Units.GRAM).getValue().doubleValue();
    }

    // --- Volume ---

    public static double litersToGallons(double L) {
        return Quantities.create(L, Units.LITRE).to(Units.GALLON_LIQUID_US).getValue().doubleValue();
    }

    public static double gallonsToLiters(double gal) {
        return Quantities.create(gal, Units.GALLON_LIQUID_US).to(Units.LITRE).getValue().doubleValue();
    }

    public static double litersToCubicMeters(double L) {
        return Quantities.create(L, Units.LITRE).to(Units.CUBIC_METER).getValue().doubleValue();
    }

    public static double cubicMetersToLiters(double m3) {
        return Quantities.create(m3, Units.CUBIC_METER).to(Units.LITRE).getValue().doubleValue();
    }

    // --- Pressure ---

    public static double pascalToAtm(double Pa) {
        return Quantities.create(Pa, Units.PASCAL).to(Units.ATMOSPHERE).getValue().doubleValue();
    }

    public static double atmToPascal(double atm) {
        return Quantities.create(atm, Units.ATMOSPHERE).to(Units.PASCAL).getValue().doubleValue();
    }

    public static double pascalToBar(double Pa) {
        return Quantities.create(Pa, Units.PASCAL).to(Units.BAR).getValue().doubleValue();
    }

    public static double barToPascal(double bar) {
        return Quantities.create(bar, Units.BAR).to(Units.PASCAL).getValue().doubleValue();
    }

    public static double pascalToPsi(double Pa) {
        // Assuming PSI is available in Units or we use derived
        // If Units.PSI is missing, we might need a fallback or define it.
        // But for now assuming standard units are present in JScience generic
        // implementation
        return Quantities.create(Pa, Units.PASCAL).to(Units.POUND_PER_SQUARE_INCH).getValue().doubleValue();
    }

    public static double psiToPascal(double psi) {
        return Quantities.create(psi, Units.POUND_PER_SQUARE_INCH).to(Units.PASCAL).getValue().doubleValue();
    }

    public static double pascalToMmHg(double Pa) {
        return Quantities.create(Pa, Units.PASCAL).to(Units.MILLIMETRE_OF_MERCURY).getValue().doubleValue();
    }

    public static double mmHgToPascal(double mmHg) {
        return Quantities.create(mmHg, Units.MILLIMETRE_OF_MERCURY).to(Units.PASCAL).getValue().doubleValue();
    }

    // --- Energy ---

    public static double joulesToCalories(double J) {
        return Quantities.create(J, Units.JOULE).to(Units.CALORIE).getValue().doubleValue();
    }

    public static double caloriesToJoules(double cal) {
        return Quantities.create(cal, Units.CALORIE).to(Units.JOULE).getValue().doubleValue();
    }

    public static double joulesToEv(double J) {
        return Quantities.create(J, Units.JOULE).to(Units.ELECTRON_VOLT).getValue().doubleValue();
    }

    public static double evToJoules(double eV) {
        return Quantities.create(eV, Units.ELECTRON_VOLT).to(Units.JOULE).getValue().doubleValue();
    }

    public static double joulesToKwh(double J) {
        // kWh might not be a standard Unit constant, but JOULE is.
        // 1 kWh = 3.6e6 J. J/3.6e6
        return J / 3.6e6;
    }

    public static double kwhToJoules(double kWh) {
        return kWh * 3.6e6;
    }

    // --- Angles ---

    public static double degreesToRadians(double deg) {
        return Quantities.create(deg, Units.DEGREE_ANGLE).asType(org.jscience.measure.quantity.Dimensionless.class)
                .to(Units.RADIAN).getValue().doubleValue();
    }

    public static double radiansToDegrees(double rad) {
        return Quantities.create(rad, Units.RADIAN).asType(Angle.class).to(Units.DEGREE_ANGLE).getValue().doubleValue();
    }

    public static double degreesToArcminutes(double deg) {
        return Quantities.create(deg, Units.DEGREE_ANGLE).to(Units.MINUTE_ANGLE).getValue().doubleValue();
    }

    public static double arcminutesToDegrees(double arcmin) {
        return Quantities.create(arcmin, Units.MINUTE_ANGLE).to(Units.DEGREE_ANGLE).getValue().doubleValue();
    }

    public static double degreesToArcseconds(double deg) {
        return Quantities.create(deg, Units.DEGREE_ANGLE).to(Units.SECOND_ANGLE).getValue().doubleValue();
    }

    public static double arcsecondsToDegrees(double arcsec) {
        return Quantities.create(arcsec, Units.SECOND_ANGLE).to(Units.DEGREE_ANGLE).getValue().doubleValue();
    }

    // --- Time ---

    public static double secondsToMinutes(double s) {
        return Quantities.create(s, Units.SECOND).to(Units.MINUTE).getValue().doubleValue();
    }

    public static double minutesToSeconds(double min) {
        return Quantities.create(min, Units.MINUTE).to(Units.SECOND).getValue().doubleValue();
    }

    public static double secondsToHours(double s) {
        return Quantities.create(s, Units.SECOND).to(Units.HOUR).getValue().doubleValue();
    }

    public static double hoursToSeconds(double h) {
        return Quantities.create(h, Units.HOUR).to(Units.SECOND).getValue().doubleValue();
    }

    public static double daysToSeconds(double d) {
        return Quantities.create(d, Units.DAY).to(Units.SECOND).getValue().doubleValue();
    }

    public static double secondsToDays(double s) {
        return Quantities.create(s, Units.SECOND).to(Units.DAY).getValue().doubleValue();
    }

    // Years are tricky due to definitions (Julian vs Tropical), sticking to logic
    // if Units.YEAR is not specific
    public static double yearsToSeconds(double y) {
        return Quantities.create(y, Units.YEAR).to(Units.SECOND).getValue().doubleValue();
    }

    public static double secondsToYears(double s) {
        return Quantities.create(s, Units.SECOND).to(Units.YEAR).getValue().doubleValue();
    }

    // --- Speed ---

    public static double mpsToKmph(double mps) {
        return Quantities.create(mps, Units.METERS_PER_SECOND).to(Units.KILOMETRES_PER_HOUR).getValue().doubleValue();
    }

    public static double kmphToMps(double kmph) {
        return Quantities.create(kmph, Units.KILOMETRES_PER_HOUR).to(Units.METERS_PER_SECOND).getValue().doubleValue();
    }

    public static double mpsToMph(double mps) {
        return Quantities.create(mps, Units.METERS_PER_SECOND).to(Units.MILES_PER_HOUR).getValue().doubleValue();
    }

    public static double mphToMps(double mph) {
        return Quantities.create(mph, Units.MILES_PER_HOUR).to(Units.METERS_PER_SECOND).getValue().doubleValue();
    }

    public static double knotsToMps(double knots) {
        return Quantities.create(knots, Units.KNOT).to(Units.METERS_PER_SECOND).getValue().doubleValue();
    }

    public static double mpsToKnots(double mps) {
        return Quantities.create(mps, Units.METERS_PER_SECOND).to(Units.KNOT).getValue().doubleValue();
    }

    // --- Frequency ---

    public static double hzToRpm(double hz) {
        // RPM is not always standard, but let's assume it is or derive it
        // 1 RPM = 1/60 Hz
        return hz * 60; // Helper fallback
    }

    public static double rpmToHz(double rpm) {
        return rpm / 60;
    }

    public static double hzToAngularVelocity(double hz) {
        return Quantities.create(hz, Units.HERTZ).asType(org.jscience.measure.quantity.Frequency.class)
                .to(Units.RADIAN.divide(Units.SECOND).asType(org.jscience.measure.quantity.Frequency.class)).getValue()
                .doubleValue();
    }

    public static double angularVelocityToHz(double omega) {
        return Quantities
                .create(omega, Units.RADIAN.divide(Units.SECOND).asType(org.jscience.measure.quantity.Frequency.class))
                .to(Units.HERTZ).getValue().doubleValue();
    }
}
