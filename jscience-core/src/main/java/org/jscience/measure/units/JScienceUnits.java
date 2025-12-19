package org.jscience.measure.units;

import org.jscience.measure.Unit;
import org.jscience.measure.quantity.*;
import org.jscience.measure.Units;

/**
 * JScience-specific units for scientific domains.
 * <p>
 * Extends JSR-385 with domain-specific units for astronomy, atomic physics,
 * and other scientific fields.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class JScienceUnits {

    private JScienceUnits() {
        // Utility class
    }

    // ========== Astronomy Units ==========

    /**
     * Light year - distance light travels in one Julian year.
     * Approximately 9.461 × 10^15 meters.
     */
    public static final Unit<Length> LIGHT_YEAR = Units.METER.multiply(9.4607304725808e15);

    /**
     * Parsec - parallax of one arcsecond.
     * Approximately 3.086 × 10^16 meters or 3.26 light-years.
     */
    public static final Unit<Length> PARSEC = Units.METER.multiply(3.0856775814913673e16);

    /**
     * Astronomical Unit - mean Earth-Sun distance.
     * Exactly 149,597,870,700 meters (IAU 2012 definition).
     */
    public static final Unit<Length> ASTRONOMICAL_UNIT = Units.METER.multiply(149597870700.0);

    // ========== Atomic Physics Units ==========

    /**
     * Bohr radius - characteristic radius of hydrogen atom ground state.
     * Approximately 5.292 × 10^-11 meters.
     */
    public static final Unit<Length> BOHR = Units.METER.multiply(5.29177210903e-11);

    /**
     * Angstrom - 10^-10 meters, commonly used for atomic-scale measurements.
     */
    public static final Unit<Length> ANGSTROM = Units.METER.multiply(1e-10);

    /**
     * Electron volt - energy gained by electron through 1V potential.
     * Approximately 1.602 × 10^-19 joules.
     */
    public static final Unit<Energy> ELECTRON_VOLT = Units.JOULE.multiply(1.602176634e-19);

    // ========== Time Units ==========

    /**
     * Julian year - exactly 365.25 days, used in astronomy.
     */
    public static final Unit<Time> JULIAN_YEAR = Units.SECOND.multiply(31557600.0);

    // ========== Mass Units ==========

    /**
     * Solar mass - mass of the Sun.
     * Approximately 1.989 × 10^30 kilograms.
     */
    public static final Unit<Mass> SOLAR_MASS = Units.KILOGRAM.multiply(1.98892e30);

    /**
     * Atomic mass unit (Dalton) - 1/12 mass of carbon-12 atom.
     * Approximately 1.661 × 10^-27 kilograms.
     */
    public static final Unit<Mass> ATOMIC_MASS_UNIT = Units.KILOGRAM.multiply(1.66053906660e-27);

    // ========== Derived Units ==========

    /**
     * Gauss - CGS unit of magnetic flux density.
     * 1 Gauss = 10^-4 Tesla.
     */
    // public static final Unit<MagneticFluxDensity> GAUSS =
    // Units.TESLA.multiply(1e-4);
}
