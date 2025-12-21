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
package org.jscience.physics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.Quantity;
import org.jscience.measure.Unit;
import org.jscience.measure.quantity.*;

/**
 * Physical constants from CODATA 2022 recommendations.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class PhysicalConstants {

        private PhysicalConstants() {
        }

        // =========================================================================
        // Fundamental Constants
        // =========================================================================

        public static final PhysicalConstant<Velocity> C_REF = new PhysicalConstant<>(
                        "Speed of light in vacuum",
                        299792458.0,
                        Units.METER_PER_SECOND,
                        CODATA.V2022,
                        0.0 // exact
        );
        public static final Quantity<Velocity> SPEED_OF_LIGHT = C_REF.toQuantity();
        public static final Real c = C_REF.getValue();

        public static final PhysicalConstant<Action> PLANCK_REF = new PhysicalConstant<>(
                        "Planck constant",
                        6.62607015e-34,
                        Units.JOULE.multiply(Units.SECOND).asType(Action.class),
                        CODATA.V2022,
                        0.0 // exact
        );
        public static final Quantity<Action> PLANCK_CONSTANT = PLANCK_REF.toQuantity();
        public static final Real h = PLANCK_REF.getValue();

        public static final Quantity<Action> HBAR = PLANCK_CONSTANT.divide(2.0 * Math.PI);
        public static final Real h_bar = h.divide(Real.of(2.0 * Math.PI));

        public static final PhysicalConstant<ElectricCharge> ELEMENTARY_CHARGE_REF = new PhysicalConstant<>(
                        "Elementary charge",
                        1.602176634e-19,
                        Units.COULOMB,
                        CODATA.V2022,
                        0.0 // exact
        );
        public static final Quantity<ElectricCharge> ELEMENTARY_CHARGE = ELEMENTARY_CHARGE_REF.toQuantity();
        public static final Real e = ELEMENTARY_CHARGE_REF.getValue();

        public static final PhysicalConstant<Entropy> BOLTZMANN_REF = new PhysicalConstant<>(
                        "Boltzmann constant",
                        1.380649e-23,
                        Units.JOULE.divide(Units.KELVIN).asType(Entropy.class),
                        CODATA.V2022,
                        0.0 // exact
        );
        public static final Quantity<Entropy> BOLTZMANN_CONSTANT = BOLTZMANN_REF.toQuantity();
        public static final Real k_B = BOLTZMANN_REF.getValue();

        public static final PhysicalConstant<?> AVOGADRO_REF = new PhysicalConstant<>(
                        "Avogadro constant",
                        6.02214076e23,
                        Units.MOLE.inverse(),
                        CODATA.V2022,
                        0.0 // exact
        );
        public static final Quantity<?> AVOGADRO_CONSTANT = AVOGADRO_REF.toQuantity();
        public static final Real N_A = AVOGADRO_REF.getValue();

        // =========================================================================
        // Other Common Constants (Partial Implementation of Full CODATA Set)
        // =========================================================================

        // Gravitational constant
        // 6.674 30(15) x 10^-11
        public static final PhysicalConstant<?> GRAVITATIONAL_REF = new PhysicalConstant<>(
                        "Newtonian constant of gravitation",
                        6.67430e-11,
                        (Unit<?>) Units.CUBIC_METER.divide(Units.KILOGRAM).divide(Units.SECOND.pow(2)),
                        CODATA.V2022,
                        1.5e-15 // uncertainty
        );
        public static final Quantity<?> GRAVITATIONAL_CONSTANT = GRAVITATIONAL_REF.toQuantity();
        public static final Real G = GRAVITATIONAL_REF.getValue();

        // Standard acceleration of gravity (Standard)
        public static final PhysicalConstant<Acceleration> STANDARD_GRAVITY_REF = new PhysicalConstant<>(
                        "Standard acceleration of gravity",
                        9.80665,
                        Units.METERS_PER_SECOND_SQUARED,
                        null, // Defined standard, not CODATA measured
                        0.0);
        public static final Quantity<Acceleration> STANDARD_GRAVITY = STANDARD_GRAVITY_REF.toQuantity();
        public static final Real g_n = STANDARD_GRAVITY_REF.getValue();

        // Vacuum permittivity
        public static final PhysicalConstant<ElectricPermittivity> EPSILON_0_REF = new PhysicalConstant<>(
                        "Vacuum electric permittivity",
                        8.8541878128e-12,
                        Units.FARAD.divide(Units.METER).asType(ElectricPermittivity.class),
                        CODATA.V2022,
                        1.3e-21 // uncertainty approx
        );
        public static final Quantity<ElectricPermittivity> EPSILON_0 = EPSILON_0_REF.toQuantity();
        public static final Real epsilon_0 = EPSILON_0_REF.getValue();

        // Electron mass
        public static final PhysicalConstant<Mass> ELECTRON_MASS_REF = new PhysicalConstant<>(
                        "Electron mass",
                        9.1093837015e-31,
                        Units.KILOGRAM,
                        CODATA.V2022,
                        2.8e-40);
        public static final Quantity<Mass> ELECTRON_MASS = ELECTRON_MASS_REF.toQuantity();
        public static final Real m_e = ELECTRON_MASS_REF.getValue();

        // Proton mass
        public static final PhysicalConstant<Mass> PROTON_MASS_REF = new PhysicalConstant<>(
                        "Proton mass",
                        1.67262192369e-27,
                        Units.KILOGRAM,
                        CODATA.V2022,
                        5.1e-37);
        public static final Quantity<Mass> PROTON_MASS = PROTON_MASS_REF.toQuantity();
        public static final Real m_p = PROTON_MASS_REF.getValue();

        // Neutron mass
        public static final PhysicalConstant<Mass> NEUTRON_MASS_REF = new PhysicalConstant<>(
                        "Neutron mass",
                        1.67492749804e-27,
                        Units.KILOGRAM,
                        CODATA.V2022,
                        9.5e-37);
        public static final Quantity<Mass> NEUTRON_MASS = NEUTRON_MASS_REF.toQuantity();
        public static final Real m_n = NEUTRON_MASS_REF.getValue();

        // Gas constant
        public static final Quantity<?> GAS_CONSTANT = Quantities.create(8.314462618,
                        (Unit<?>) Units.JOULE.divide(Units.MOLE.multiply(Units.KELVIN)));
        public static final Real R = Real.of(8.314462618);

        // Aliases
        public static final Quantity<ElectricPermittivity> ELECTRIC_CONSTANT = EPSILON_0;

        // Remaining legacy/derived constants can be added as needed or computed
        public static final Real mu_0 = Real.of(1.25663706212e-6); // Approx derived

        // Astronomical (Legacy)
        public static final Real AU = Real.of(1.495978707e11);
        public static final Real ly = Real.of(9.4607304725808e15);
        public static final Real pc = Real.of(3.0856775814913673e16);
        public static final Real M_sun = Real.of(1.98892e30);
        public static final Real M_earth = Real.of(5.9722e24);
        // Quantum Constants
        public static final Quantity<Action> REDUCED_PLANCK = HBAR;
        public static final PhysicalConstant<WaveNumber> RYDBERG_CONSTANT_REF = new PhysicalConstant<>(
                        "Rydberg constant",
                        10973731.568160,
                        Units.METER.inverse().asType(WaveNumber.class),
                        CODATA.V2022,
                        2.1e-4 // uncertainty
        );
        public static final Quantity<WaveNumber> RYDBERG_CONSTANT = RYDBERG_CONSTANT_REF.toQuantity();
        public static final PhysicalConstant<Dimensionless> FINE_STRUCTURE_REF = new PhysicalConstant<>(
                        "Fine-structure constant",
                        7.2973525693e-3,
                        Units.ONE,
                        CODATA.V2022,
                        1.1e-12);
        public static final Quantity<Dimensionless> FINE_STRUCTURE = FINE_STRUCTURE_REF.toQuantity();
        public static final Real alpha = FINE_STRUCTURE_REF.getValue();

        // Electron volt
        public static final Real eV = e; // Value in Joules match elementary charge numeric value if J/C=V=1?
        // No, eV is unit of energy. 1 eV = 1.602...e-19 J.
        // So PhysicalConstants.eV should likely be the conversion factor or Real value
        // in Joules.
        // Since 'e' is Real(1.602e-19), 'eV' as a Real representing Energy in Joules is
        // just 'e'.
        // Wait, e is Charge. eV is Energy.
        // If ParticleType expects eV to be Energy in Joules (L21: "Electron volt in
        // Joules"), then it is numerically equal to 'e' value in derived units.

        public static final Real PI = Real.PI;
}
