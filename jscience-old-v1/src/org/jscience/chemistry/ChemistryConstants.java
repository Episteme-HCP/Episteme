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

import org.jscience.physics.PhysicsConstants;


/**
 * A class representing common constants used in chemistry.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class ChemistryConstants extends Object {
    /** Volume of 1 mole of ideal gas, at 1 atmosphere, 0<sup>0</sup>C. */
    public final static double MOLAR_VOLUME_0 = 22413.6e-7;

    /** Volume of 1 mole of ideal gas, at 1 atmosphere, 25<sup>0</sup>C. */
    public final static double MOLAR_VOLUME_25 = 24465.1e-7;

    /** Avogadro's constant. */
    public final static double AVOGADRO = 6.0221367E+23;

    /** Molar gas constant. */
    public final static double MOLAR_GAS = 8.314472;

    /** Boltzmann's constant (defined). */
    public final static double BOLTZMANN = MOLAR_GAS / AVOGADRO;

    /** Stefan-Boltzmann constant. */
    public final static double STEFAN_BOLTZMANN = (Math.PI * Math.PI * Math.pow(BOLTZMANN,
            4)) / (60 * Math.pow(PhysicsConstants.H_BAR, 3) * PhysicsConstants.SPEED_OF_LIGHT * PhysicsConstants.SPEED_OF_LIGHT);

    //5.670 400(40) � 10-8 J s-1 m-2 K-4.
    //http://en.wikipedia.org/wiki/List_of_states_of_matter
    //phases
    //mutually exclusive
    /** DOCUMENT ME! */
    public final static int SOLID = 1;

    /** DOCUMENT ME! */
    public final static int LIQUID = 2;

    /** DOCUMENT ME! */
    public final static int GAS = 3;

    /** DOCUMENT ME! */
    public final static int PLASMA = 4;

    /** DOCUMENT ME! */
    public final static int BOSE_EINSTEIN_CONDENSATE = 5;

    /** DOCUMENT ME! */
    public final static int FERMIONIC_CONDENSATE = 6;

    /** DOCUMENT ME! */
    public final static int STRANGE_MATTER = 7;

    /** DOCUMENT ME! */
    public final static int LIQUID_CRYSTAL = 8;

    /** DOCUMENT ME! */
    public final static int SUPERFLUID = 9;

    /** DOCUMENT ME! */
    public final static int SUPERSOLID = 10;

    /** DOCUMENT ME! */
    public final static int PARAMAGNETIC = 11;

    /** DOCUMENT ME! */
    public final static int FERROMAGNETIC = 12;

    /** DOCUMENT ME! */
    public final static int DEGENERATE_MATTER = 13;

    /** DOCUMENT ME! */
    public final static int NEUTRON_MATTER = 14;

    /** DOCUMENT ME! */
    public final static int STRONGLY_SYMMETRICAL_MATTER = 15;

    /** DOCUMENT ME! */
    public final static int WEAKLY_SYMMETRICAL_MATTER = 16;

    /** DOCUMENT ME! */
    public final static int QUARK_GLUON_PLASMA = 17;

    //transistion between solid liquid and gas states
    /** DOCUMENT ME! */
    public final static int VAPORIZATION = 1; //liquid to gas

    /** DOCUMENT ME! */
    public final static int DEPOSITION = 2; //gas to solid

    /** DOCUMENT ME! */
    public final static int MELTING = 3; //solid to liquid

    /** DOCUMENT ME! */
    public final static int FREEZING = 4; //liquid to solid

    /** DOCUMENT ME! */
    public final static int SUBLIMATION = 5; //solid to gas

    /** DOCUMENT ME! */
    public final static int CONDENSATION = 6; //gas to liquid
}
