/*
*   StdAtmos1976 -- Calculates the properties of the ICAO 1976 Standard Atmosphere
*
*   Copyright (C) 1999-2002 by Joseph A. Huwaldt <jhuwaldt@knology.net>.
*   All rights reserved.
*
*   This library is free software; you can redistribute it and/or
*   modify it under the terms of the GNU Library General Public
*   License as published by the Free Software Foundation; either
*   version 2 of the License, or (at your option) any later version.
*
*   This library is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*   Library General Public License for more details.
**/
package org.jscience.earth;

/**
 * A collection of routines and data used in calculating
 * the properties of the ICAO 1976 Standard Atmosphere to
 * an altitude of 86,000 meters (53.4 miles)
 * <p/>
 * <p>  Modified by:  Joseph A. Huwaldt  </p>
 *
 * @author Joseph A. Huwaldt  Date:        September 27, 1998
 * @version May 21, 2002
 */
public class EarthStandardAtmosphere1976 extends AbstractAtmosphere {
    //	Gas constant in m/�K.
    private static final float R = 29.27F;

    //	Standard gravitational acceleration m/s^2.
    private static final float g0 = 9.80665F;

    //	Ratio of specific heats of air.
    private static final float GAMMA = 1.4F;

    //	Standard sea level pressure in Newtons/m^2.
    private static final float kP0 = 101327.52F;

    //	Standard sea level temperature in �Kelvin.
    private static final float kT0 = 288.16F;

    //	Standard sea level density in kg/L (g/cm^3).
    private static final float kRHO0 = 0.001225054F;

    //	Standard sea level speed of sound (m/s).
    private static final float ka0 = 340.29396F;

    //	Radius of the Earth (meters).
    private static final float REARTH = 6369000.0F;

    //	Gas constant.
    private static final float GMR = 0.034163195F;

    //	1976 Standard Atmosphere tables.
    private static final float[] htab = {
            0.0F, 11000.0F, 20000.0F, 32000.0F, 47000.0F, 51000.0F, 71000.0F,
            84852.F
    };
    private static final float[] ttab = {
            288.15F, 216.65F, 216.65F, 228.65F, 270.65F, 270.65F, 214.65F,
            186.946F
    };
    private static final float[] ptab = {
            1.0F, 2.233611E-1F, 5.403295E-2F, 8.5666784E-3F, 1.0945601E-3F,
            6.6063531E-4F, 3.9046834E-5F, 3.68501E-6F
    };
    private static final float[] gtab = {
            -0.0065F, 0.0F, 0.0010F, 0.0028F, 0.0F, -0.0028F, -0.0020F, 0.0F
    };

    //-----------------------------------------------------------------------------------

    /**
     * Default constructor that assumes an altitude of 0 meters,
     */
    public EarthStandardAtmosphere1976() {
        super();
    }

    /**
     * Constructor that takes a value of altitude in meters.
     * If the specified altitude is outside the range 0 to 86,000
     * meters, an IllegalArgumentException exception is thrown.
     *
     * @param altitude Altitude at which standard atmosphere is to be
     *                 calculated; value given in meters.
     */
    public EarthStandardAtmosphere1976(float altitude)
            throws IllegalArgumentException {
        //	Set the altitude (if out of range, throw exception).
        if (altitude < 0F) {
            throw new IllegalArgumentException("Exception: altitude can not be less than 0 km.");
        } else {
            if (altitude > 86000F) {
                throw new IllegalArgumentException("Exception: altitude can not be greater than 86 km.");
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
     * Returns the standard sea level temperature for this
     * atmosphere model.  Value returned in degrees Kelvin.
     *
     * @return Returns the standard sea level temperature in �K.
     */
    public final float T0() {
        return kT0;
    }

    /**
     * Returns the standard sea level pressure for this
     * atmosphere model.  Value returned in Newtons/m^2.
     *
     * @return Returns the standard sea level pressure in N/m^2.
     */
    public final float P0() {
        return kP0;
    }

    /**
     * Returns the standard sea level density for this
     * atmosphere model.  Value returned in kg/L (g/cm^3).
     *
     * @return Returns the standard sea level density in kg/L (g/cm^3).
     */
    public final float RHO0() {
        return kRHO0;
    }

    /**
     * Returns the standard sea level speed of sound for this
     * atmosphere model.  Value returned in meters/sec.
     *
     * @return Returns the standard sea level density in m/s.
     */
    public final float a0() {
        return ka0;
    }

    /**
     * Returns the minimum altitude supported by this atmosphere model.
     *
     * @return Returns the minimum altitude supported by this atmosphere model.
     */
    public float minAltitude() {
        return 0F;
    }

    /**
     * Returns the maximum altitude supported by this atmosphere model.
     *
     * @return Returns the maximum altitude supported by this atmosphere model.
     */
    public float maxAltitude() {
        return 86000F;
    }

    /**
     * Sets the geometric altitude where the standard atmosphere
     * is to be calculated.
     *
     * @param altitude Geometric altitude at which standard atmosphere is to be
     *                 calculated;  value given in meters.
     */
    public void setAltitude(float altitude) throws IllegalArgumentException {
        //	Set the altitude (if out of range, throw exception).
        if (altitude < 0F) {
            throw new IllegalArgumentException("Exception: altitude can not be less than 0 km.");
        } else {
            if (altitude > 86000F) {
                throw new IllegalArgumentException("Exception: altitude can not be greater than 86 km.");
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
     * Calculates the properties of the 1976 IACO Standard Atmosphere.
     */
    protected void calculate() {
        //	Loop counters.
        int i;

        //	Loop counters.
        int j;

        //	Loop counters.
        int k;

        //	Geopotential altitude (km).
        float h;

        //	Temperature gradient and base temp of this layer.
        float tgrad;

        //	Temperature gradient and base temp of this layer.
        float tbase;

        //	Local temperature.
        float tlocal;

        //	Height above base of this layer.
        float deltah;

        //	Convert geometric to geopotential altitude.
        h = (alt * REARTH) / (alt + REARTH);

        i = 0;
        j = htab.length - 1;

        //	Setting up for binary search
        do {
            k = (i + j) / 2;

            //	Find atmosphere layer containing specified altitude.
            if (h < htab[k]) {
                j = k;
            } else {
                i = k;
            }
        } while (j > (i + 1));

        //	i will be in range 0...htab.length-2
        tgrad = gtab[i];
        tbase = ttab[i];
        deltah = h - htab[i];
        tlocal = tbase + (tgrad * deltah);

        //	temperature ratio
        theta = tlocal / ttab[0];

        if (tgrad == 0.0F) {
            //	pressure ratio
            delta = ptab[i] * (float) Math.exp((-GMR * deltah) / tbase);
        } else {
            delta = ptab[i] * (float) Math.pow(tbase / tlocal, GMR / tgrad);
        }

        // Density ratio.
        sigma = delta / theta;
    }
}
