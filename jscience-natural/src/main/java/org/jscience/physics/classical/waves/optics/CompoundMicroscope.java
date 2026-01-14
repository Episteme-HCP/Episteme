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

package org.jscience.physics.classical.waves.optics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a compound microscope optical system.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CompoundMicroscope {

    private final Real objectiveFocalLength;
    private final Real eyepieceFocalLength;
    private final Real tubeLength;
    private final Real objectiveRadius;

    public CompoundMicroscope(Real objectiveFocalLength, Real eyepieceFocalLength,
            Real tubeLength, Real objectiveRadius) {
        this.objectiveFocalLength = objectiveFocalLength;
        this.eyepieceFocalLength = eyepieceFocalLength;
        this.tubeLength = tubeLength;
        this.objectiveRadius = objectiveRadius;
    }

    /**
     * Returns the objective magnification.
     * M_obj = L / f_obj
     */
    public Real getObjectiveMagnification() {
        return tubeLength.divide(objectiveFocalLength);
    }

    /**
     * Returns the eyepiece magnification.
     * M_eye = 0.25m / f_eye (using 25cm near point)
     */
    public Real getEyepieceMagnification() {
        return Real.of(0.25).divide(eyepieceFocalLength);
    }

    /**
     * Returns total magnification.
     */
    public Real getTotalMagnification() {
        return getObjectiveMagnification().multiply(getEyepieceMagnification());
    }

    /**
     * Calculates the image position for a given object distance.
     * Uses thin lens equation: 1/f = 1/do + 1/di
     * 
     * @param objectDistance Distance from object to objective lens
     * @return Image distance from objective lens
     */
    public Real calculateImageDistance(Real objectDistance) {
        // 1/di = 1/f - 1/do
        Real invDi = Real.ONE.divide(objectiveFocalLength).subtract(Real.ONE.divide(objectDistance));
        return Real.ONE.divide(invDi);
    }

    /**
     * Returns the working distance for a given object position.
     * Working distance is typically just outside the focal length.
     */
    public Real getWorkingDistance() {
        return objectiveFocalLength.multiply(Real.of(1.1)); // ~10% beyond focal point
    }

    // Getters
    public Real getObjectiveFocalLength() {
        return objectiveFocalLength;
    }

    public Real getEyepieceFocalLength() {
        return eyepieceFocalLength;
    }

    public Real getTubeLength() {
        return tubeLength;
    }

    public Real getObjectiveRadius() {
        return objectiveRadius;
    }
}


