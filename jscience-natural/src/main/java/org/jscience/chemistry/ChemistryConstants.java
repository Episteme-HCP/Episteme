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

package org.jscience.chemistry;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Volume;
import org.jscience.measure.quantity.AmountOfSubstance;
import org.jscience.physics.PhysicalConstants;

/**
 * Constants and enumerations for chemistry.
 * <p>
 * Provides chemical constants (Avogadro, Gas constant, etc.) and
 * classifications for states of matter and phase transitions.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class ChemistryConstants {

    private ChemistryConstants() {}

    /**
     * States of matter.
     */
    public enum MatterState {
        SOLID, LIQUID, GAS, PLASMA, 
        BOSE_EINSTEIN_CONDENSATE, FERMIONIC_CONDENSATE, 
        STRANGE_MATTER, LIQUID_CRYSTAL, SUPERFLUID, SUPERSOLID, 
        PARAMAGNETIC, FERROMAGNETIC, DEGENERATE_MATTER, 
        NEUTRON_MATTER, STRONGLY_SYMMETRICAL_MATTER, 
        WEAKLY_SYMMETRICAL_MATTER, QUARK_GLUON_PLASMA, UNKNOWN
    }

    /**
     * Phase transitions.
     */
    public enum PhaseTransition {
        VAPORIZATION, // Liquid to Gas
        DEPOSITION,   // Gas to Solid
        MELTING,      // Solid to Liquid
        FREEZING,      // Liquid to Solid
        SUBLIMATION,  // Solid to Gas
        CONDENSATION, // Gas to Liquid
        IONIZATION,   // Gas to Plasma
        RECOMBINATION // Plasma to Gas
    }

    /** Avogadro's constant. */
    public static final Quantity<?> AVOGADRO = PhysicalConstants.AVOGADRO_CONSTANT;

    /** Molar gas constant (R). */
    public static final Quantity<?> MOLAR_GAS_CONSTANT = PhysicalConstants.GAS_CONSTANT;

    /** Boltzmann's constant (k_B). */
    public static final Quantity<?> BOLTZMANN_CONSTANT = PhysicalConstants.BOLTZMANN_CONSTANT;

    /** Stefan-Boltzmann constant (sigma). */
    public static final Quantity<?> STEFAN_BOLTZMANN_CONSTANT = Quantities.create(
            Real.of("5.670374419e-8"), 
            Units.WATT.divide(Units.SQUARE_METER).divide(Units.KELVIN.pow(4)));

    /** Molar volume of an ideal gas at STP (0°C, 1 atm). */
    public static final Quantity<Volume> MOLAR_VOLUME_STP = Quantities.create(
            Real.of("0.02241396954"), 
            Units.CUBIC_METER.divide(Units.MOLE).asType(Volume.class));

    /** Molar volume of an ideal gas at SATP (25°C, 100 kPa). */
    public static final Quantity<Volume> MOLAR_VOLUME_SATP = Quantities.create(
            Real.of("0.02478957029"), 
            Units.CUBIC_METER.divide(Units.MOLE).asType(Volume.class));
}
