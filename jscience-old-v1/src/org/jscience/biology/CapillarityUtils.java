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

package org.jscience.biology;

/**
 * The CapillarityUtils class provides useful vascular biology related
 * methods.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//also see org.jscience.physics.fluids.Electrophoresis 
public final class CapillarityUtils extends Object {
    //http://en.wikipedia.org/wiki/Capillary_wave
    /**
     * DOCUMENT ME!
     *
     * @param gamma DOCUMENT ME!
     * @param rho DOCUMENT ME!
     * @param rhoprime DOCUMENT ME!
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDispertionFrequency(double gamma, double rho,
        double rhoprime, double k) {
        return Math.sqrt((k * k * k * gamma) / (rho + rhoprime));
    }

    //The waves with large wavelengths are generally also affected by gravity and are then called gravity-capillary waves. Their dispersion relation reads, for infinite depth of the two fluids,
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param gamma DOCUMENT ME!
     * @param rho DOCUMENT ME!
     * @param rhoprime DOCUMENT ME!
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getLargeWavesDispertionFrequency(double g, double gamma,
        double rho, double rhoprime, double k) {
        return Math.sqrt(((g * (rho - rhoprime) * k) / rho) +
            ((k * k * k * gamma) / (rho + rhoprime)));
    }

    //http://en.wikipedia.org/wiki/Washburn%27s_equation
    /**
     * DOCUMENT ME!
     *
     * @param eta DOCUMENT ME!
     * @param gamma DOCUMENT ME!
     * @param diameter DOCUMENT ME!
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getWashburnPenetrationDistance(double eta, double gamma,
        double diameter, double t) {
        return Math.sqrt((gamma * diameter * t) / (4 * eta));
    }

    //http://en.wikipedia.org/wiki/Capillary_action
    /**
     * DOCUMENT ME!
     *
     * @param tension DOCUMENT ME!
     * @param theta DOCUMENT ME!
     * @param rho DOCUMENT ME!
     * @param g DOCUMENT ME!
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMotionHeight(double tension, double theta, double rho,
        double g, double r) {
        return Math.sqrt((2 * tension * Math.cos(theta)) / (rho * g * r));
    }
}
