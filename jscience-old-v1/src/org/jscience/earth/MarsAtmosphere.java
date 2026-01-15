/*
*
**/
package org.jscience.earth;

/**
 * A really basic model of Mars Atmosphere.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//http://www.grc.nasa.gov/WWW/K-12/airplane/atmosmrm.html until a better model comes
public class MarsAtmosphere extends AbstractAtmosphere {
    //-----------------------------------------------------------------------------------
/**
     * Default constructor that assumes an altitude of 0 meters,
     */
    public MarsAtmosphere() {
        super();
    }

/**
     * Constructor that takes a value of altitude in meters. If the specified
     * altitude is outside the range 0 to Float.Max_VALUE meters, an
     * IllegalArgumentException exception is thrown.
     *
     * @param altitude Altitude at which standard atmosphere is to be
     *                 calculated; value given in meters.
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public MarsAtmosphere(float altitude) throws IllegalArgumentException {
        //	Set the altitude (if out of range, throw exception).
        if (altitude < 0F) {
            throw new IllegalArgumentException(
                "Exception: altitude can not be less than 0 km.");
        } else {
            if (altitude > 86000F) {
                throw new IllegalArgumentException(
                    "Exception: altitude can not be greater than Float.Max_VALUE m.");
            } else {
                //	Reset the altitude only if it has changed.
                if (altitude != alt) {
                    alt = altitude;

                    //	Go off and calculate the standard atmosphere parameters.
                    calculate();
                }
            }
        }
    }

    //-----------------------------------------------------------------------------------
    /**
     * Returns the standard sea level temperature for this atmosphere
     * model. Value returned in degrees Kelvin.
     *
     * @return Returns the standard sea level temperature in �K.
     */
    public float T0() {
        return 273.1f - 31f;
    }

    /**
     * Returns the standard sea level pressure for this atmosphere
     * model. Value returned in Newtons/m^2.
     *
     * @return Returns the standard sea level pressure in N/m^2.
     */
    public float P0() {
        return 699f;
    }

    /**
     * Returns the standard sea level density for this atmosphere
     * model.  Value returned in kg/L (g/cm^3).
     *
     * @return Returns the standard sea level density in kg/L (g/cm^3).
     */
    public float RHO0() {
        return P0() / (0.1921f * T0());
    }

    /**
     * Returns the standard sea level speed of sound for this
     * atmosphere model. Value returned in meters/sec.
     *
     * @return Returns the standard sea level density in m/s.
     */

    //actually I don't know how to compute that
    public float a0() {
        return 0;
    }

    /**
     * Returns the minimum altitude supported by this atmosphere model.
     * Sub-classes should return the minimum altitude supported the the
     * sub-class' atmosphere model.
     *
     * @return Returns the minimum altitude supported by this atmosphere model.
     */
    public float minAltitude() {
        return 0;
    }

    /**
     * Returns the maximum altitude supported by this atmosphere model.
     * Sub-classes should return the maximum altitude supported the the
     * sub-class' atmosphere model.
     *
     * @return Returns the maximum altitude supported by this atmosphere model.
     */
    public float maxAltitude() {
        return Float.MAX_VALUE;
    }

    /**
     * Sets the geometric altitude where the standard atmosphere is to
     * be calculated.
     *
     * @param altitude Geometric altitude at which standard atmosphere is to be
     *        calculated; value given in meters.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setAltitude(float altitude) throws IllegalArgumentException {
        //	Set the altitude (if out of range, throw exception).
        if (altitude < 0F) {
            throw new IllegalArgumentException(
                "Exception: altitude can not be less than 0 km.");
        } else {
            if (altitude > 86000F) {
                throw new IllegalArgumentException(
                    "Exception: altitude can not be greater than Float.MAX_VALUE m.");
            } else {
                //	Reset the altitude only if it has changed.
                if (altitude != alt) {
                    alt = altitude;

                    //	Go off and calculate the standard atmosphere parameters.
                    calculate();
                }
            }
        }
    }

    /**
     * Sub-classes must provide a method here that calculates the
     * properties of a particular atmosphere model.  The following poperties
     * must be calculated (sub-classes may add others, but these are
     * required): Density Ratio (sigma) as fn(alt) Pressure Ratio (delta) as
     * fn(alt) Temperature Ratio (theta) as fn(alt)
     */
    protected void calculate() {
        if (alt < 7000) {
            theta = -31 - (0.000998f * alt);
        } else {
            theta = -23.4f - (0.00222f * alt);
        }

        delta = 699f * (float) Math.exp(-0.00009f * alt);
        sigma = delta / (0.1921f * (theta + 273.1f));
    }
}
