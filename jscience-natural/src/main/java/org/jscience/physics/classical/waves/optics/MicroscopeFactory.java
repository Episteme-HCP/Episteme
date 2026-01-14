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
 * Factory methods for common microscope configurations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MicroscopeFactory {

    private MicroscopeFactory() {
    }

    /**
     * Creates a simple compound microscope (two lenses).
     * 
     * @param objectiveFocalLength Focal length of objective lens (short, e.g. 4mm)
     * @param eyepieceFocalLength  Focal length of eyepiece lens (e.g. 25mm)
     * @param tubeLength           Distance between lenses (e.g. 160mm standard)
     * @param objectiveRadius      Radius/aperture of objective lens
     * @return Compound microscope optical system
     */
    public static CompoundMicroscope compound(Real objectiveFocalLength, Real eyepieceFocalLength,
            Real tubeLength, Real objectiveRadius) {
        return new CompoundMicroscope(objectiveFocalLength, eyepieceFocalLength, tubeLength, objectiveRadius);
    }

    /**
     * Calculates total magnification of a compound microscope.
     * M = (L / f_obj) * (25cm / f_eye)
     * where L is tube length and 25cm is near point distance.
     * 
     * @param tubeLength           Optical tube length
     * @param objectiveFocalLength Objective focal length
     * @param eyepieceFocalLength  Eyepiece focal length
     * @return Total magnification
     */
    public static Real totalMagnification(Real tubeLength, Real objectiveFocalLength,
            Real eyepieceFocalLength) {
        Real nearPoint = Real.of(0.25); // 25cm near point in meters
        Real objectiveMag = tubeLength.divide(objectiveFocalLength);
        Real eyepieceMag = nearPoint.divide(eyepieceFocalLength);
        return objectiveMag.multiply(eyepieceMag);
    }

    /**
     * Calculates numerical aperture (NA).
     * NA = n * sin(ÃŽÂ¸)
     * 
     * @param refractiveIndex Medium refractive index (1.0 for air, ~1.5 for oil)
     * @param halfAngle       Half-angle of maximum cone of light (radians)
     * @return Numerical aperture
     */
    public static Real numericalAperture(Real refractiveIndex, Real halfAngle) {
        return refractiveIndex.multiply(halfAngle.sin());
    }

    /**
     * Calculates resolution limit (Rayleigh criterion).
     * d = 0.61 * ÃŽÂ» / NA
     * 
     * @param wavelength        Wavelength of light
     * @param numericalAperture Numerical aperture of objective
     * @return Minimum resolvable distance
     */
    public static Real resolutionLimit(Real wavelength, Real numericalAperture) {
        return Real.of(0.61).multiply(wavelength).divide(numericalAperture);
    }

    /**
     * Calculates depth of field.
     * DOF Ã¢â€°Ë† ÃŽÂ» * n / NAÃ‚Â²
     * 
     * @param wavelength        Wavelength of light
     * @param refractiveIndex   Medium refractive index
     * @param numericalAperture Numerical aperture
     * @return Depth of field
     */
    public static Real depthOfField(Real wavelength, Real refractiveIndex, Real numericalAperture) {
        return wavelength.multiply(refractiveIndex).divide(numericalAperture.pow(2));
    }
}


