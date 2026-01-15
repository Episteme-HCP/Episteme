package org.jscience.physics;


import org.jscience.mathematics.MathConstants;


/**
 * A collection of fundamental physics constants. All values expressed in
 * SI units. (Source: CODATA Bulletin No. 63 Nov 1986)
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//for example use it to convert Joules to and from electronVolts
//WARNING: the values for constants from this class as well as AstronomyConstants or GeographicalConstants, or any other Constants class in general may be sligthly different in futures versions
//Warning, there is overlapping between this class and javax.quantities.QuantifiedConstants
//see http://physics.nist.gov/cuu/Constants/index.html
public final class PhysicsConstants extends Object {
    /** Planck's constant. */
    public final static double PLANCK = 6.6260755E-34;

    /** Planck's constant divided by 2Pi (defined). */
    public final static double H_BAR = PLANCK / MathConstants.TWO_PI;

    /** Speed of light in vacuo (exact). */
    public final static double SPEED_OF_LIGHT = 299792458.0;

    /** Permeability constant (exact). */
    public final static double PERMEABILITY = Math.PI * 4E-7;

    /** Permittivity constant (defined). */
    public final static double PERMITTIVITY = 1.0 / (PERMEABILITY * SPEED_OF_LIGHT * SPEED_OF_LIGHT);

    /** Gravitational constant. */
    public final static double GRAVITATION = 6.67259E-11;

    /** Elementary charge. */
    public final static double CHARGE = 1.60217733E-19;

    /** Electron rest mass. */
    public final static double ELECTRON_MASS = 9.1093897E-31;

    /** Proton rest mass. */
    public final static double PROTON_MASS = 1.6726231E-27;

    /** Neutron rest mass. */
    public final static double NEUTRON_MASS = 1.6749286E-27;

    /** Rydberg constant (defined). */
    public final static double RYDBERG = ((SPEED_OF_LIGHT * PERMEABILITY) * (SPEED_OF_LIGHT * PERMEABILITY) * SPEED_OF_LIGHT * ELECTRON_MASS * CHARGE * (CHARGE / PLANCK) * (CHARGE / PLANCK) * (CHARGE / PLANCK)) / 8.0;

    /** Fine structure constant (defined). */
    public final static double FINE_STRUCTURE = ((PERMEABILITY * SPEED_OF_LIGHT * CHARGE) / PLANCK * CHARGE) / 2.0;

    /** Faraday constant. */
    public final static double FARADAY = 96485.309;

    /** Magnetic flux quantum (defined). */
    public final static double FLUX_QUANTUM = PLANCK / (2.0 * CHARGE);

    /** Bohr magneton (defined). */
    public final static double BOHR_MAGNETON = (CHARGE / ELECTRON_MASS * H_BAR) / 2.0;

    /** Magnetic moment of electron. */
    public final static double ELECTRON_MOMENT = 9.2847701E-24;

    /** Magnetic moment of proton. */
    public final static double PROTON_MOMENT = 1.41060761E-26;

    /** DOCUMENT ME! */
      private final static double EARTH_MASS = 5.9742E24;
      /** DOCUMENT ME! */
      private final static double EARTH_RADIUS = 6378137; //beware because Earth is not a round sphere, see org.jscience.Astronomy
    /** g gravity at 0 meters on earth. */
      public final static double G = (GRAVITATION * EARTH_MASS) / (EARTH_RADIUS * EARTH_RADIUS);
   //should be but introduces dependency: public final static double G = (GRAVITATION * GeographyConstants.EARTH_MASS) / (GeographyConstants.EARTH_RADIUS * GeographyConstants.EARTH_RADIUS);


}
