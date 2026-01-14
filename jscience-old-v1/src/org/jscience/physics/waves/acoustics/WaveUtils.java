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

package org.jscience.physics.waves.acoustics;

/**
 * The Wave class provides a basic representation of an accoustic wave.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class WaveUtils extends Object {
    //http://en.wikipedia.org/wiki/Wave
    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     * @param rho DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDispertionFrequency(double t, double rho) {
        return Math.sqrt(t / rho);
    }

    //http://en.wikipedia.org/wiki/Sound_energy_flux
    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     * @param A DOCUMENT ME!
     * @param rho DOCUMENT ME!
     * @param v DOCUMENT ME!
     * @param theta DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSoundEnergyFlux(double p, double A, double rho, double v,
        double theta) {
        return (p * p * A * Math.cos(theta)) / (rho * v);
    }

    //http://en.wikipedia.org/wiki/Speed_of_sound
    /**
     * DOCUMENT ME!
     *
     * @param theta DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSoundSpeedInAir(double theta) {
        return 331.5 + (0.6 * theta);
    }

    //The period (T) is the time for one complete cycle for an oscillation of a wave. The frequency (F) is how many periods per unit time (for example one second) and is measured in hertz. These are related by:
    /**
     * DOCUMENT ME!
     *
     * @param frequency DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAngularFrequency(double frequency) {
        return 2 * Math.PI * frequency;
    }

    //Suppose sound is emitted as a sine wave travelling outward spherically from a point source. The pressure (above ambient, see gauge pressure) of the sound wave can be written as
    /**
     * DOCUMENT ME!
     *
     * @param radius DOCUMENT ME!
     * @param t DOCUMENT ME!
     * @param p0 DOCUMENT ME!
     * @param frequency DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSinWavePressure(double radius, double t, double p0,
        double frequency, double c) {
        return p0 * Math.sin(2 * Math.PI * frequency * (t - (radius / c)));
    }
}
