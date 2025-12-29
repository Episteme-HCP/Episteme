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

package org.jscience.physics.classical.waves.optics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Factory methods for common telescope configurations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TelescopeFactory {

    private TelescopeFactory() {
    }

    /**
     * Creates a Newtonian reflecting telescope.
     * 
     * @param primaryMirrorRadius   Radius of curvature of primary mirror (meters)
     * @param primaryMirrorAperture Aperture diameter of primary mirror (meters)
     * @param eyepieceFocalLength   Focal length of eyepiece (meters)
     * @return An OpticalSystem representing the Newtonian telescope
     */
    public static OpticalSystem newtonian(Real primaryMirrorRadius, Real primaryMirrorAperture,
            Real eyepieceFocalLength) {
        OpticalSystem system = new OpticalSystem();

        // Primary concave mirror at origin, facing +Z
        SphericalMirror primaryMirror = new SphericalMirror(
                primaryMirrorRadius,
                primaryMirrorAperture,
                new Real[] { Real.ZERO, Real.ZERO, Real.ZERO },
                new Real[] { Real.ZERO, Real.ZERO, Real.of(-1.0) } // Light comes from +Z
        );

        system.addElement(primaryMirror);

        // Note: Flat secondary mirror (diagonal) would redirect light to eyepiece
        // For simplicity, we model the system with just the primary for magnification
        // calculations

        return system;
    }

    /**
     * Creates a Cassegrain reflecting telescope.
     * Uses primary concave + secondary convex mirrors.
     * 
     * @param primaryRadius   Radius of curvature of primary mirror
     * @param secondaryRadius Radius of curvature of secondary mirror (negative for
     *                        convex)
     * @param aperture        Aperture diameter
     * @param separation      Distance between mirrors
     * @return An OpticalSystem representing the Cassegrain telescope
     */
    public static OpticalSystem cassegrain(Real primaryRadius, Real secondaryRadius,
            Real aperture, Real separation) {
        OpticalSystem system = new OpticalSystem();

        // Primary concave mirror
        SphericalMirror primary = new SphericalMirror(
                primaryRadius,
                aperture,
                new Real[] { Real.ZERO, Real.ZERO, Real.ZERO },
                new Real[] { Real.ZERO, Real.ZERO, Real.of(-1.0) });

        // Secondary convex mirror (placed in front of primary)
        SphericalMirror secondary = new SphericalMirror(
                secondaryRadius.negate(), // Convex
                aperture.divide(Real.of(4.0)), // Smaller secondary
                new Real[] { Real.ZERO, Real.ZERO, separation.negate() },
                new Real[] { Real.ZERO, Real.ZERO, Real.ONE } // Facing primary
        );

        system.addElement(primary);
        system.addElement(secondary);

        return system;
    }

    /**
     * Calculates angular magnification for a telescope.
     * M = f_objective / f_eyepiece
     * 
     * @param objectiveFocalLength Focal length of objective (mirror or lens)
     * @param eyepieceFocalLength  Focal length of eyepiece
     * @return Angular magnification
     */
    public static Real angularMagnification(Real objectiveFocalLength, Real eyepieceFocalLength) {
        return objectiveFocalLength.divide(eyepieceFocalLength);
    }

    /**
     * Calculates the light-gathering power relative to the human eye.
     * LGP = (D_aperture / D_pupil)²
     * 
     * @param apertureMeters Telescope aperture diameter in meters
     * @param pupilMeters    Human pupil diameter (typically 0.007m = 7mm)
     * @return Light gathering power ratio
     */
    public static Real lightGatheringPower(Real apertureMeters, Real pupilMeters) {
        return apertureMeters.divide(pupilMeters).pow(2);
    }
}
