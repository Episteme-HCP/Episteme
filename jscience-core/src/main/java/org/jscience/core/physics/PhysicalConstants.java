/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.physics;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.*;

/**
 * A registry of physical constants with high precision values (CODATA 2018).
 * <p>
 * Constants are provided both as {@link Real} numbers for direct computation
 * and as {@link Quantity} objects for unit-aware calculations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class PhysicalConstants {

    private PhysicalConstants() {}

    /**
     * Speed of light in vacuum (c): exactly 299,792,458 m/s.
     */
    public static final Real SPEED_OF_LIGHT = Real.of(299792458);
    public static final Quantity<Velocity> C = Quantities.create(SPEED_OF_LIGHT, Units.METER_PER_SECOND);

    /**
     * Planck constant (h): 6.62607015 x 10^-34 J s.
     */
    public static final Real PLANCK_CONSTANT = Real.of("6.62607015E-34");
    // Action / Angular Momentum unit = J*s = kg*m^2/s
    public static final Quantity<?> H = Quantities.create(PLANCK_CONSTANT, Units.JOULE.multiply(Units.SECOND));

    /**
     * Newtonian constant of gravitation (G): 6.67430(15) x 10^-11 m^3 kg^-1 s^-2.
     */
    public static final Real GRAVITATIONAL_CONSTANT = Real.of("6.67430E-11");
    public static final Quantity<?> G = Quantities.create(GRAVITATIONAL_CONSTANT, 
        Units.CUBIC_METER.divide(Units.KILOGRAM).divide(Units.SECOND.multiply(Units.SECOND)));

    /**
     * Avogadro constant (N_A): 6.02214076 x 10^23 mol^-1.
     */
    public static final Real AVOGADRO_CONSTANT = Real.of("6.02214076E23");
    public static final Quantity<?> N_A = Quantities.create(AVOGADRO_CONSTANT, Units.ONE.divide(Units.MOLE));

    /**
     * Boltzmann constant (k): 1.380649 x 10^-23 J/K.
     */
    public static final Real BOLTZMANN_CONSTANT = Real.of("1.380649E-23");
    public static final Quantity<?> K = Quantities.create(BOLTZMANN_CONSTANT, Units.JOULE.divide(Units.KELVIN));

    /**
     * Elementary charge (e): 1.602176634 x 10^-19 C.
     */
    public static final Real ELEMENTARY_CHARGE = Real.of("1.602176634E-19");
    public static final Quantity<ElectricCharge> E = Quantities.create(ELEMENTARY_CHARGE, Units.COULOMB);

    /**
     * Standard acceleration of gravity (g_n): 9.80665 m/s^2.
     */
    public static final Real STANDARD_GRAVITY = Real.of("9.80665");
    public static final Quantity<Acceleration> G_N = Quantities.create(STANDARD_GRAVITY, Units.METERS_PER_SECOND_SQUARED);
}
