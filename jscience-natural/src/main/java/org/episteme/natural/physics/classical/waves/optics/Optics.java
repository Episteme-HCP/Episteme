/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.physics.classical.waves.optics;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.quantity.*;
import org.episteme.natural.physics.PhysicalConstants;

/**
 * Optics equations and models (geometric and wave optics).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Optics {

    /** Snell's law: nÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Âsin(ÃƒÆ’Ã…Â½Ãƒâ€šÃ‚Â¸ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â) = nÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡sin(ÃƒÆ’Ã…Â½Ãƒâ€šÃ‚Â¸ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡), returns ÃƒÆ’Ã…Â½Ãƒâ€šÃ‚Â¸ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ */
    public static Real snellsLaw(Real n1, Real theta1, Real n2) {
        Real sinTheta2 = n1.multiply(theta1.sin()).divide(n2);
        return sinTheta2.asin();
    }

    /** Thin lens: 1/f = 1/do + 1/di, returns di */
    public static Real thinLensImageDistance(Real objectDistance, Real focalLength) {
        return Real.ONE.divide(Real.ONE.divide(focalLength).subtract(Real.ONE.divide(objectDistance)));
    }

    /** Magnification: M = -di/do */
    public static Real magnification(Real imageDistance, Real objectDistance) {
        return imageDistance.divide(objectDistance).negate();
    }

    /** Photon energy: E = hc/ÃƒÆ’Ã…Â½Ãƒâ€šÃ‚Â» */
    public static Quantity<Energy> photonEnergy(Quantity<Length> wavelength) {
        return PhysicalConstants.PLANCK_CONSTANT
                .multiply(PhysicalConstants.SPEED_OF_LIGHT)
                .divide(wavelength)
                .asType(Energy.class);
    }

    /** Diffraction grating: d sin(ÃƒÆ’Ã…Â½Ãƒâ€šÃ‚Â¸) = mÃƒÆ’Ã…Â½Ãƒâ€šÃ‚Â», returns ÃƒÆ’Ã…Â½Ãƒâ€šÃ‚Â¸ */
    public static Real diffractionAngle(Real gratingSpacing, int order, Real wavelength) {
        Real sinTheta = Real.of(order).multiply(wavelength).divide(gratingSpacing);
        return sinTheta.asin();
    }

    /** Rayleigh criterion: ÃƒÆ’Ã…Â½Ãƒâ€šÃ‚Â¸_min = 1.22ÃƒÆ’Ã…Â½Ãƒâ€šÃ‚Â»/D */
    public static Real rayleighCriterion(Real wavelength, Real apertureDiameter) {
        return Real.of(1.22).multiply(wavelength).divide(apertureDiameter);
    }

    /** Mirror equation (same as thin lens) */
    public static Real mirrorEquation(Real objectDistance, Real focalLength) {
        return thinLensImageDistance(objectDistance, focalLength);
    }

    /** Lens Maker's: 1/f = (n-1)(1/RÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â - 1/RÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡) */
    public static Real lensMakerEquation(Real n, Real r1, Real r2) {
        Real term = n.subtract(Real.ONE).multiply(Real.ONE.divide(r1).subtract(Real.ONE.divide(r2)));
        return Real.ONE.divide(term);
    }
}




