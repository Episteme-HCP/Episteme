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

package org.jscience.earth;

/**
 * An abstract class that provides standard functionality
 * for all atmosphere models that sub-class off of this
 * one.  Sub-classes must provide a method for calculating
 * the atmosphere properties of density ratio, temperature
 * ratio and pressure ratio.  Also methods must be provided
 * that simply return the values of temperature, pressure,
 * density and speed of sound at sea level conditions.
 * <p/>
 * <p>  Modified by:  Joseph A. Huwaldt  </p>
 *
 * @author Joseph A. Huwaldt  Date:        September 27, 1998
 * @version May 21, 2002
 */
public abstract class AbstractAtmosphere implements java.io.Serializable {
    //	The geometric altitude in meters.
    protected float alt = 0F;

    //	The density at altitude / sea-level standard density.
    protected float sigma = 1F;

    //	The pressure at altitude / sea-level standard pressure.
    protected float delta = 1F;

    //	The temp. at altitude / sea-level standard temperature.
    protected float theta = 1F;

    //-----------------------------------------------------------------------------------

    /**
     * Get geometric altitude currently being used for standard
     * atmosphere calculations.
     *
     * @return Returns altitude where the standard atmosphere is
     *         being calculated in meters.
     */
    public final float getAltitude() {
        return alt;
    }

    /**
     * Get the density at altitude divided by the sea-level
     * standard density.
     *
     * @return Returns the density ratio at altitude.
     */
    public final float getDensityRatio() {
        return sigma;
    }

    /**
     * Get the pressure at altitude divided by the sea-level
     * standard pressure.
     *
     * @return Returns the pressure ratio at altitude.
     */
    public final float getPressureRatio() {
        return delta;
    }

    /**
     * Get the temperature at altitude divided by the sea-level
     * standard temperature.
     *
     * @return Returns the temperature ratio at altitude.
     */
    public final float getTemperatureRatio() {
        return theta;
    }

    /**
     * Get the static air temperature at altitude.
     *
     * @return Returns the temperature at altitude in units of �K.
     */
    public float getTemperature() {
        return T0() * theta;
    }

    /**
     * Get the static air pressure at altitude.
     *
     * @return Returns the pressure at altitude in units of N/m^2.
     */
    public float getPressure() {
        return P0() * delta;
    }

    /**
     * Get the static air density at altitude.
     *
     * @return Returns the density at altitude in units of g/cm^3.
     */
    public float getDensity() {
        return RHO0() * sigma;
    }

    /**
     * Get the speed of sound at altitude.
     *
     * @return Returns the speed of sound at altitude in units of m/s.
     */
    public float getSpeedOfSound() {
        return a0() * (float) Math.sqrt(theta);
    }

    /**
     * Creates a String representation of this object.
     *
     * @return The String representation of this AbstractAtmosphere object.
     */
    public String toString() {
        String s;
        s = "h = " + alt + " meters DensityR = " + sigma + " PressureR = " +
                delta;
        s += (" TemperatureR = " + theta);

        return s;
    }

    //-----------------------------------------------------------------------------------

    /**
     * Returns the standard sea level temperature for this
     * atmosphere model.  Value returned in degrees Kelvin.
     *
     * @return Returns the standard sea level temperature in �K.
     */
    public abstract float T0();

    /**
     * Returns the standard sea level pressure for this
     * atmosphere model.  Value returned in Newtons/m^2.
     *
     * @return Returns the standard sea level pressure in N/m^2.
     */
    public abstract float P0();

    /**
     * Returns the standard sea level density for this
     * atmosphere model.  Value returned in kg/L (g/cm^3).
     *
     * @return Returns the standard sea level density in kg/L (g/cm^3).
     */
    public abstract float RHO0();

    /**
     * Returns the standard sea level speed of sound for this
     * atmosphere model.  Value returned in meters/sec.
     *
     * @return Returns the standard sea level density in m/s.
     */
    public abstract float a0();

    /**
     * Returns the minimum altitude supported by this
     * atmosphere model.  Sub-classes should return the
     * minimum altitude supported the the sub-class'
     * atmosphere model.
     *
     * @return Returns the minimum altitude supported by this atmosphere model.
     */
    public abstract float minAltitude();

    /**
     * Returns the maximum altitude supported by this
     * atmosphere model.  Sub-classes should return the
     * maximum altitude supported the the sub-class'
     * atmosphere model.
     *
     * @return Returns the maximum altitude supported by this atmosphere model.
     */
    public abstract float maxAltitude();

    /**
     * Sets the geometric altitude where the standard
     * atmosphere is to be calculated.
     *
     * @param altitude Geometric altitude at which standard atmosphere is
     *                 to be calculated; value given in meters.
     */
    public abstract void setAltitude(float altitude)
            throws IllegalArgumentException;

    /**
     * Sub-classes must provide a method here that calculates
     * the properties of a particular atmosphere model.  The
     * following poperties must be calculated
     * (sub-classes may add others, but these are required):
     * Density Ratio (sigma) as fn(alt)
     * Pressure Ratio (delta) as fn(alt)
     * Temperature Ratio (theta) as fn(alt)
     */
    protected abstract void calculate();
}
