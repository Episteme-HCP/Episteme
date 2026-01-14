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

package org.jscience.physics.waves.optics;

/**
 * The class defines several methods to describe geometric optics.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class GeometricOptics extends Object {
/**
     * Creates a new GeometricOptics object.
     */
    public GeometricOptics() {
    }

    //reflection law of Snell-Descartes
    /**
     * DOCUMENT ME!
     *
     * @param angle DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getReflectionAngle(double angle) {
        return angle;
    }

    //refraction law of Snell-Descartes
    /**
     * DOCUMENT ME!
     *
     * @param angle DOCUMENT ME!
     * @param index1 DOCUMENT ME!
     * @param index2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getRefractionAngle(double angle, double index1, double index2) {
        return Math.asin((index1 * Math.sin(angle)) / index2);
    }

    //we could also introduce several methods on prisms and lens should I know what could be useful
    /**
     * DOCUMENT ME!
     *
     * @param sourceIntensity DOCUMENT ME!
     * @param length DOCUMENT ME!
     * @param concentration DOCUMENT ME!
     * @param absorption DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getResultingIntensity(double sourceIntensity, double length,
        double concentration, double absorption) {
        return sourceIntensity * Math.exp(length * concentration * absorption);
    }

    //Beer Law
    /**
     * DOCUMENT ME!
     *
     * @param transmittance DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAbsorbance(double transmittance) {
        //return 2 - Math.log10(100 * transmittance);
        return 2 - (Math.log(100 * transmittance) / Math.log(10));
    }

    //Fresnel Law
    /**
     * DOCUMENT ME!
     *
     * @param indicesRatio DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getReflectionLoss(double indicesRatio) {
        return Math.pow((indicesRatio - 1), 2) / Math.pow((indicesRatio + 1), 2);
    }

    //Rayleigh scattering
    /**
     * DOCUMENT ME!
     *
     * @param nScatters DOCUMENT ME!
     * @param diameter DOCUMENT ME!
     * @param refractionIndex DOCUMENT ME!
     * @param wavelenght DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getRayleighScattering(int nScatters, float diameter,
        double refractionIndex, double wavelenght) {
        return ((2 * Math.pow(Math.PI, 5)) / 3 * nScatters * Math.pow(((refractionIndex * refractionIndex) -
            1) / ((refractionIndex * refractionIndex) + 2), 2) * Math.pow(diameter,
            6)) / Math.pow(wavelenght, 4);
    }
}
