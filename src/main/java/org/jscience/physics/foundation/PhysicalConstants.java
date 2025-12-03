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
package org.jscience.physics.foundation;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;

import org.jscience.measure.quantity.*;
import org.jscience.mathematics.number.Real;

/**
 * Physical constants from CODATA 2022 recommendations.
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public final class PhysicalConstants {

        private PhysicalConstants() {
        }

        // Speed of light in vacuum (exact)
        public static final Quantity<Velocity> SPEED_OF_LIGHT = Quantities.create(299792458.0, Units.METER_PER_SECOND);

        // Planck constant (exact)
        public static final Quantity<Action> PLANCK_CONSTANT = Quantities.create(6.62607015e-34,
                        Units.JOULE.multiply(Units.SECOND).asType(Action.class));

        // Reduced Planck constant
        public static final Quantity<Action> HBAR = PLANCK_CONSTANT.divide(2.0 * Math.PI);

        // Elementary charge (exact)
        public static final Quantity<ElectricCharge> ELEMENTARY_CHARGE = Quantities.create(1.602176634e-19,
                        Units.COULOMB);

        // Boltzmann constant (exact)
        public static final Quantity<Entropy> BOLTZMANN_CONSTANT = Quantities.create(1.380649e-23,
                        Units.JOULE.divide(Units.KELVIN).asType(Entropy.class));

        // Avogadro constant (exact)
        public static final Quantity<?> AVOGADRO_CONSTANT = Quantities.create(6.02214076e23, Units.MOLE.inverse());

        // Gravitational constant
        public static final Quantity<?> GRAVITATIONAL_CONSTANT = Quantities.create(6.67430e-11,
                        (Unit<?>) Units.CUBIC_METER.divide(Units.KILOGRAM).divide(Units.SECOND.pow(2)));

        // Standard acceleration of gravity
        public static final Quantity<Acceleration> STANDARD_GRAVITY = Quantities.create(9.80665,
                        Units.METERS_PER_SECOND_SQUARED);

        // Vacuum permittivity
        public static final Quantity<ElectricPermittivity> EPSILON_0 = Quantities.create(8.8541878128e-12,
                        Units.FARAD.divide(Units.METER).asType(ElectricPermittivity.class));

        // Vacuum permeability
        public static final Quantity<MagneticPermeability> MU_0 = Quantities.create(1.25663706212e-6,
                        Units.HENRY.divide(Units.METER).asType(MagneticPermeability.class));

        // Electron mass
        public static final Quantity<Mass> ELECTRON_MASS = Quantities.create(9.1093837015e-31, Units.KILOGRAM);

        // Proton mass
        public static final Quantity<Mass> PROTON_MASS = Quantities.create(1.67262192369e-27, Units.KILOGRAM);

        // Neutron mass
        public static final Quantity<Mass> NEUTRON_MASS = Quantities.create(1.67492749804e-27, Units.KILOGRAM);

        // Fine structure constant (dimensionless)
        public static final Quantity<Dimensionless> FINE_STRUCTURE_CONSTANT = Quantities.create(7.2973525693e-3,
                        Units.ONE);

        // Rydberg constant
        public static final Quantity<WaveNumber> RYDBERG_CONSTANT = Quantities.create(10973731.568160,
                        Units.METER.inverse().asType(WaveNumber.class));

        // Gas constant
        public static final Quantity<?> GAS_CONSTANT = Quantities.create(8.314462618,
                        (Unit<?>) Units.JOULE.divide(Units.MOLE.multiply(Units.KELVIN)));

        // Faraday constant
        public static final Quantity<?> FARADAY_CONSTANT = Quantities.create(96485.33212,
                        (Unit<?>) Units.COULOMB.divide(Units.MOLE));

        // Stefan-Boltzmann constant
        public static final Quantity<?> STEFAN_BOLTZMANN = Quantities.create(5.670374419e-8,
                        (Unit<?>) Units.WATT.divide(Units.SQUARE_METER.multiply(Units.KELVIN.pow(4))));

        // Aliases for compatibility
        public static final Quantity<Action> REDUCED_PLANCK = HBAR;
        public static final Quantity<ElectricPermittivity> ELECTRIC_CONSTANT = EPSILON_0;
        public static final Quantity<Dimensionless> FINE_STRUCTURE = FINE_STRUCTURE_CONSTANT;
}
