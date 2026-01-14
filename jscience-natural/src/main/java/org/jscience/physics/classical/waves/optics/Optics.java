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
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.*;
import org.jscience.physics.PhysicalConstants;

/**
 * Optics equations and models (geometric and wave optics).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Optics {

    /** Snell's law: nÃ¢â€šÂsin(ÃŽÂ¸Ã¢â€šÂ) = nÃ¢â€šâ€šsin(ÃŽÂ¸Ã¢â€šâ€š), returns ÃŽÂ¸Ã¢â€šâ€š */
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

    /** Photon energy: E = hc/ÃŽÂ» */
    public static Quantity<Energy> photonEnergy(Quantity<Length> wavelength) {
        return PhysicalConstants.PLANCK_CONSTANT
                .multiply(PhysicalConstants.SPEED_OF_LIGHT)
                .divide(wavelength)
                .asType(Energy.class);
    }

    /** Diffraction grating: d sin(ÃŽÂ¸) = mÃŽÂ», returns ÃŽÂ¸ */
    public static Real diffractionAngle(Real gratingSpacing, int order, Real wavelength) {
        Real sinTheta = Real.of(order).multiply(wavelength).divide(gratingSpacing);
        return sinTheta.asin();
    }

    /** Rayleigh criterion: ÃŽÂ¸_min = 1.22ÃŽÂ»/D */
    public static Real rayleighCriterion(Real wavelength, Real apertureDiameter) {
        return Real.of(1.22).multiply(wavelength).divide(apertureDiameter);
    }

    /** Mirror equation (same as thin lens) */
    public static Real mirrorEquation(Real objectDistance, Real focalLength) {
        return thinLensImageDistance(objectDistance, focalLength);
    }

    /** Lens Maker's: 1/f = (n-1)(1/RÃ¢â€šÂ - 1/RÃ¢â€šâ€š) */
    public static Real lensMakerEquation(Real n, Real r1, Real r2) {
        Real term = n.subtract(Real.ONE).multiply(Real.ONE.divide(r1).subtract(Real.ONE.divide(r2)));
        return Real.ONE.divide(term);
    }
}


