/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.earth.seismology;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Energy;
import org.jscience.measure.quantity.Length;

/**
 * Seismology calculations for earthquake analysis.
 * <p>
 * Provides:
 * <ul>
 * <li>Magnitude scale conversions (Richter, moment magnitude)</li>
 * <li>Energy release calculations</li>
 * <li>Seismic wave travel times</li>
 * <li>Intensity estimations</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Seismology {

    /** P-wave velocity in Earth's crust (km/s) */
    public static final Real VP_CRUST = Real.of(6.0);

    /** S-wave velocity in Earth's crust (km/s) */
    public static final Real VS_CRUST = Real.of(3.5);

    /** Earth's radius (km) */
    public static final Real EARTH_RADIUS_KM = Real.of(6371.0);

    private Seismology() {
    }

    // === Magnitude Conversions ===

    /**
     * Convert local magnitude (Richter) to seismic moment.
     * log10(M0) = 1.5 * ML + 16.1 (CGS units, dyne-cm)
     * 
     * @param localMagnitude ML (Richter scale)
     * @return seismic moment in N·m
     */
    public static Real magnitudeToMoment(Real localMagnitude) {
        // CGS to SI: 1 dyne-cm = 1e-7 N·m
        Real log10M0_cgs = localMagnitude.multiply(Real.of(1.5)).add(Real.of(16.1));
        Real m0_cgs = Real.of(10).pow(log10M0_cgs);
        return m0_cgs.multiply(Real.of(1e-7));
    }

    /**
     * Convert seismic moment to moment magnitude (Mw).
     * Mw = (2/3) * log10(M0) - 10.7 (SI units, N·m)
     */
    public static Real momentToMagnitude(Real seismicMoment) {
        return seismicMoment.log10().multiply(Real.of(2.0).divide(Real.of(3.0)))
                .subtract(Real.of(10.7));
    }

    /**
     * Estimate energy released by earthquake.
     * log10(E) = 1.5 * M + 4.8 (Joules)
     * 
     * @param magnitude earthquake magnitude
     * @return energy as Quantity
     */
    public static Quantity<Energy> energyReleased(Real magnitude) {
        Real logE = magnitude.multiply(Real.of(1.5)).add(Real.of(4.8));
        Real joules = Real.of(10).pow(logE);
        return Quantities.create(joules.doubleValue(), Units.JOULE);
    }

    /**
     * Compare energy of two magnitudes.
     * Each magnitude step represents ~31.6x energy increase.
     */
    public static Real energyRatio(Real mag1, Real mag2) {
        return Real.of(10).pow(mag1.subtract(mag2).multiply(Real.of(1.5)));
    }

    // === Wave Travel Times ===

    /**
     * P-wave travel time for crustal path.
     * 
     * @param distanceKm epicentral distance in km
     * @return travel time in seconds
     */
    public static Real pWaveTravelTime(Real distanceKm) {
        return distanceKm.divide(VP_CRUST);
    }

    /**
     * S-wave travel time for crustal path.
     */
    public static Real sWaveTravelTime(Real distanceKm) {
        return distanceKm.divide(VS_CRUST);
    }

    /**
     * S-P time difference (used for locating earthquakes).
     */
    public static Real spTimeDifference(Real distanceKm) {
        return sWaveTravelTime(distanceKm).subtract(pWaveTravelTime(distanceKm));
    }

    /**
     * Estimate distance from S-P time difference.
     */
    public static Quantity<Length> distanceFromSpTime(Real spTimeSeconds) {
        // d = sp_time * Vp * Vs / (Vp - Vs)
        Real distanceKm = spTimeSeconds.multiply(VP_CRUST).multiply(VS_CRUST)
                .divide(VP_CRUST.subtract(VS_CRUST));
        return Quantities.create(distanceKm.multiply(Real.of(1000)).doubleValue(), Units.METER);
    }

    // === Intensity ===

    /**
     * Estimate Modified Mercalli Intensity from magnitude and distance.
     * Approximate empirical relationship.
     * 
     * @param magnitude  earthquake magnitude
     * @param distanceKm epicentral distance
     * @return estimated MMI (I-XII scale)
     */
    public static int estimateIntensity(Real magnitude, Real distanceKm) {
        // Simplified attenuation model
        // mmi = 1.5 * mag - 3.5 * log10(max(dist, 1)) + 3
        Real dist = distanceKm.max(Real.ONE);
        Real mmi = magnitude.multiply(Real.of(1.5))
                .subtract(dist.log10().multiply(Real.of(3.5)))
                .add(Real.of(3));

        return (int) Math.round(Math.max(1, Math.min(12, mmi.doubleValue())));
    }

    /**
     * Get intensity description (Modified Mercalli).
     */
    public static String intensityDescription(int mmi) {
        switch (mmi) {
            case 1:
                return "Not felt";
            case 2:
                return "Weak - Felt by few";
            case 3:
                return "Weak - Felt by several";
            case 4:
                return "Light - Felt by many";
            case 5:
                return "Moderate - Felt by all";
            case 6:
                return "Strong - Slight damage";
            case 7:
                return "Very Strong - Moderate damage";
            case 8:
                return "Severe - Considerable damage";
            case 9:
                return "Violent - Heavy damage";
            case 10:
                return "Extreme - Some buildings destroyed";
            case 11:
                return "Extreme - Many buildings destroyed";
            case 12:
                return "Extreme - Total destruction";
            default:
                return "Unknown";
        }
    }

    // === Magnitude Classification ===

    /**
     * Classify earthquake by magnitude.
     */
    public static String classifyMagnitude(Real magnitude) {
        double mag = magnitude.doubleValue();
        if (mag < 2.0)
            return "Micro";
        if (mag < 4.0)
            return "Minor";
        if (mag < 5.0)
            return "Light";
        if (mag < 6.0)
            return "Moderate";
        if (mag < 7.0)
            return "Strong";
        if (mag < 8.0)
            return "Major";
        return "Great";
    }

    /**
     * Estimate annual frequency of earthquakes at magnitude level.
     * Based on Gutenberg-Richter law: log10(N) = a - bM
     * Using global average: a ≈ 8, b ≈ 1
     */
    public static Real annualFrequency(Real magnitude) {
        return Real.of(10).pow(Real.of(8).subtract(magnitude));
    }

    // === Fault Parameters ===

    /**
     * Estimate fault rupture length from seismic moment.
     * Empirical relationship: log10(L) = 0.5 * M - 1.88 (km)
     */
    public static Quantity<Length> ruptureLength(Real magnitude) {
        Real logL = magnitude.multiply(Real.of(0.5)).subtract(Real.of(1.88));
        Real lengthKm = Real.of(10).pow(logL);
        return Quantities.create(lengthKm.multiply(Real.of(1000)).doubleValue(), Units.METER);
    }

    /**
     * Estimate average slip from magnitude.
     * log10(D) = 0.5 * M - 3.58 (meters)
     */
    public static Quantity<Length> averageSlip(Real magnitude) {
        Real logD = magnitude.multiply(Real.of(0.5)).subtract(Real.of(3.58));
        Real slipM = Real.of(10).pow(logD);
        return Quantities.create(slipM.doubleValue(), Units.METER);
    }
}
