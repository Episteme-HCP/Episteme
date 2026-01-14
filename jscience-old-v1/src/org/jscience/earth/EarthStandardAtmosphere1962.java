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
 * This class provides methods for calculating the atmospheric
 * properties of the U.S. Standard Atmosphere, 1962, assuming
 * an inverse square gravitational field.  This assumption
 * yields data that agrees with the COESA document within 1%
 * at all altitudes up to 700,000 meters (434.96 miles).
 * <p/>
 * <p>  Modified by:  Joseph A. Huwaldt  </p>
 *
 * @author Joseph A. Huwaldt  Date:        July 10, 1999
 * @version August 16, 2002
 */
public class EarthStandardAtmosphere1962 extends AbstractAtmosphere {
    //	Standard gravitational acceleration ft/s^2.
    private static final float g0 = 32.1740484F;

    //	Ratio of specific heats of air.
    private static final float GAMMA = 1.4F;

    // Molecular weight.
    private static final float WM0 = 28.9644F;

    //	Radius of the Earth (ft).
    private static final float REARTH = 20890855.F;

    //	Gas constant (deg R/ft).
    private static final float GMR = 0.018743418F;

    //	Standard sea level pressure in Newtons/m^2 (101325).
    private static final float kP0 = 2116.2165F / 0.09290304F * 4.448222F;

    //	Standard sea level temperature in deg. Kelvin (288.15).
    private static final float kT0 = (518.67F * 5.F) / 9;

    //	Standard sea level density in kg/L (g/cm^3 -- 0.00122501).
    private static final float kRHO0 = (0.00237691F / 0.3048F * 9.80665F) / 28.316846592F * 0.45359237F;

    //	Standard sea level speed of sound in m/s (340.292036).
    private static final float ka0 = 661.4748F / 3600 * 1852;

    //	1962 Standard Atmosphere tables (altitude in ft, temperature in deg. R, pressure in lb/ft^2).
    private static final float[] htab = {
            -16404, 0, 36089, 65617, 104987, 154199, 170604, 200131, 250186,
            291160
    };
    private static final float[] ZM = {
            295276, 328084, 360892, 393701, 492126, 524934, 557743, 623360,
            754593, 984252, 1312336, 1640420, 1968504, 2296588
    };
    private static final float[] WM = {
            28.9644F, 28.88F, 28.56F, 28.07F, 26.92F, 26.66F, 26.4F, 25.85F,
            24.7F, 22.66F, 19.94F, 17.94F, 16.84F, 16.17F
    };
    private static final float[] TM = {
            577.17F, 518.67F, 389.97F, 389.97F, 411.57F, 487.17F, 487.17F,
            454.77F, 325.17F, 325.17F, 379.17F, 469.17F, 649.17F, 1729.17F,
            1999.17F, 2179.17F, 2431.17F, 2791.17F, 3295.17F, 3889.17F, 4357.17F,
            4663.17F, 4861.17F
    };
    private static final float[] PM = {
            3711.0839F, 2116.2165F, 472.67563F, 114.34314F, 18.128355F,
            2.3162178F, 1.2321972F, 3.8030279E-01F, 2.1671352E-02F,
            3.4313478E-03F, 6.2773411E-04F, 1.5349091E-04F, 5.2624212E-05F,
            1.0561806E-05F, 7.7083076E-06F, 5.8267151E-06F, 3.5159854E-06F,
            1.4520255E-06F, 3.9290563E-07F, 8.4030242E-08F, 2.2835256E-08F,
            7.1875452E-09F
    };

    //-----------------------------------------------------------------------------------

    /**
     * Constructor that assumes an altitude of 0 meters,
     */
    public EarthStandardAtmosphere1962() {
        super();
    }

    /**
     * Constructor taking a value of geometric altitude in meters.
     * If the specified altitude is outside the range -5000 to 90,000
     * meters, an IllegalArgumentException exception is thrown.  </p>
     *
     * @param altitude Altitude at which standard atmosphere is to be
     *                 calculated; value given in meters.
     */
    public EarthStandardAtmosphere1962(float altitude)
            throws IllegalArgumentException {
        //	Set the altitude (if out of range, throw exception).
        if (altitude < -5000F) {
            throw new IllegalArgumentException("Altitude can not be less than -5 km.");
        } else {
            if (altitude > 700000F) {
                throw new IllegalArgumentException("Altitude can not be greater than 700 km.");
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
     * atmosphere model.  Value returned in Kelvins.
     *
     * @return Returns the standard sea level temperature in K.
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
     * @return Returns the standard sea level density in kg/L
     *         (g/cm^3).
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
     * Returns the minimum altitude supported by this
     * atmosphere model.
     *
     * @return Returns the minimum altitude supported by this
     *         atmosphere model in meters.
     */
    public float minAltitude() {
        return -5000; //  = -16404.F*0.3048F;
    }

    /**
     * Returns the maximum altitude supported by this
     * atmosphere model.
     *
     * @return Returns the maximum altitude supported by this
     *         atmosphere model in meters.
     */
    public float maxAltitude() {
        return 700000F;
    }

    /**
     * Sets the geometric altitude where the standard atmosphere
     * is to be calculated.
     *
     * @param altitude Geometric altitude at which standard atmosphere is to be
     *                 calculated (in meters).
     */
    public void setAltitude(float altitude) throws IllegalArgumentException {
        //	Set the altitude (if out of range, throw exception).
        if (altitude < -5000F) {
            throw new IllegalArgumentException("Altitude can not be less than -5 km.");
        } else {
            if (altitude > 700000F) {
                throw new IllegalArgumentException("Altitude can not be greater than 700 km.");
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
     * Calculates the properties of the US 1962 Standard Atmosphere.
     */
    protected void calculate() {
        //	Loop counters.
        int j = 0;

        //	Geometric and Geopotential altitude (ft).
        float Z;

        //	Geometric and Geopotential altitude (ft).
        float G;

        float TMS;
        float EM;
        float P;
        float Rho;
        float T;

        // First convert the altitude from meters to feet.
        Z = alt / 0.3048F;

        G = REARTH / (REARTH + Z);
        G *= (G * g0);

        if (Z <= 295276) {
            // TMS linear with geopotential, calculate H and search.
            float H = (REARTH * Z) / (REARTH + Z);

            // Find region in geopotential table for this altitude.
            for (int i = 1; i < htab.length; ++i) {
                j = i - 1;

                if (htab[i] > H) {
                    break;
                }
            }

            // Calculate TMS slope, TMS, and set mol wt stuff.
            float ELH = (TM[j + 1] - TM[j]) / (htab[j + 1] - htab[j]);
            TMS = TM[j] + (ELH * (H - htab[j]));

            //			ELZ = ELH*G/g0;
            //			DMDZ = 0;
            EM = WM0;

            // Check TMS slope and calculate pressure.
            if (ELH == 0) {
                // Zero slope pressure equation (lbs/ft^2).
                P = PM[j] * (float) Math.exp((GMR * (htab[j] - H)) / TMS);
            } else {
                // Non-zero slope pressure equation (lbs/ft^2).
                P = PM[j] * (float) Math.pow(TM[j] / TMS, GMR / ELH);
            }
        } else {
            // TMS linear with Z. Search matrix.
            int k = 0;

            for (int i = 1; i < ZM.length; ++i) {
                j = i + 8;
                k = i - 1;

                if (ZM[i] > Z) {
                    break;
                }
            }

            // Calculate TMS, slope, and stuff.
            float ELZ = (TM[j + 1] - TM[j]) / (ZM[k + 1] - ZM[k]);
            TMS = TM[j] + (ELZ * (Z - ZM[k]));

            float DMDZ = (WM[k + 1] - WM[k]) / (ZM[k + 1] - ZM[k]);
            EM = WM[k] + (DMDZ * (Z - ZM[k]));

            float ZLZ = Z - (TMS / ELZ);

            // Pressure equation for TMS linear with Z (lbs/ft^2).
            float temp = (float) Math.exp(GMR / ELZ * (REARTH / (REARTH + ZLZ)));
            P = PM[j] * temp * temp;
            temp = (float) Math.log((TMS * (REARTH + ZM[k])) / TM[j] / (REARTH +
                    Z));
            P *= ((((Z - ZM[k]) * (REARTH + ZLZ)) / (REARTH + Z) / (REARTH +
                    ZM[k])) - temp);
        }

        // Calculate pressure ratio.
        delta = P / 2116.2165F;

        // Calculate density (slug/ft^3).
        Rho = (GMR * P) / g0 / TMS;
        sigma = Rho / 0.00237691F;

        // Calculate temperature (deg Rankine).
        T = (EM * TMS) / WM0;
        theta = T / 518.67F;
    }

    /**
     * A simple method to test the 1962 standard atmosphere.
     */
    public static void main(String[] args) {
        System.out.println("\nTesting StandardAtmosphere1962 class:");

        float h = 0;
        EarthStandardAtmosphere1962 atmos = new EarthStandardAtmosphere1962(h);
        System.out.println("    minAltitude = " + atmos.minAltitude() +
                ", maxAltitude = " + atmos.maxAltitude() + " m.");
        System.out.println("    h = " + h + " m, delta = " +
                atmos.getPressureRatio() + ", sigma = " + atmos.getDensityRatio() +
                ", theta = " + atmos.getTemperatureRatio() + ".");
        h = 20000;
        atmos = new EarthStandardAtmosphere1962(h);
        System.out.println("    h = " + h + " m, delta = " +
                atmos.getPressureRatio() + ", sigma = " + atmos.getDensityRatio() +
                ", theta = " + atmos.getTemperatureRatio() + ".");
        System.out.println("    h = " + h + " m, P = " + atmos.getPressure() +
                " N/m^2, density = " + atmos.getDensity() + " g/cm^3, T = " +
                atmos.getTemperature() + " K.");
        System.out.println("    h = " + h + " m, speed of sound = " +
                atmos.getSpeedOfSound() + " m/s.");

        System.out.println("Done!");
    }
}
