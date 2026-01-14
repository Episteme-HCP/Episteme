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

package org.jscience.physics.waves;

import org.jscience.physics.PhysicsConstants;


/**
 * The class defines doppler laws.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public final class Doppler extends Object {
/**
     * Creates a new Doppler object.
     */
    private Doppler() {
    }

    //Doppler
    /**
     * DOCUMENT ME!
     *
     * @param sourceFrequency DOCUMENT ME!
     * @param sourceVelocity DOCUMENT ME!
     * @param waveVelocity DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getDopplerFrequency(double sourceFrequency,
        double sourceVelocity, double waveVelocity) {
        return sourceFrequency * (waveVelocity / (waveVelocity -
        sourceVelocity));
    }

    //relativistic Doppler effect is change in the observed frequency of light due to the relative motion of source and observer when taking into account the Special Theory of Relativity.
    /**
     * DOCUMENT ME!
     *
     * @param sourceFrequency DOCUMENT ME!
     * @param relativeVelocity DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getRelativisticDopplerFrequency(
        double sourceFrequency, double relativeVelocity) {
        return sourceFrequency * Math.sqrt((1 -
            (relativeVelocity / PhysicsConstants.SPEED_OF_LIGHT)) / (1 +
            (relativeVelocity / PhysicsConstants.SPEED_OF_LIGHT)));
    }
}
