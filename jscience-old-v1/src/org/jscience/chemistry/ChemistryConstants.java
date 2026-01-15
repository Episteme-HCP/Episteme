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
