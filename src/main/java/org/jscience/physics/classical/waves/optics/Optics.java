/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
 * @since 2.0
 */
public class Optics {

    /**
     * Snell's law: n₁ sin(θ₁) = n₂ sin(θ₂)
     * Returns refraction angle θ₂
     */
    public static Real snellsLaw(Real n1, Real theta1, Real n2) {
        double sinTheta2 = (n1.doubleValue() * Math.sin(theta1.doubleValue())) / n2.doubleValue();
        return Real.of(Math.asin(sinTheta2));
    }

    /**
     * Thin lens equation: 1/f = 1/do + 1/di
     * Returns image distance di given object distance do and focal length f
     */
    public static Real thinLensImageDistance(Real objectDistance, Real focalLength) {
        Real term = Real.ONE.divide(focalLength).subtract(Real.ONE.divide(objectDistance));
        return Real.ONE.divide(term);
    }

    /**
     * Magnification: M = -di/do = hi/ho
     */
    public static Real magnification(Real imageDistance, Real objectDistance) {
        return imageDistance.divide(objectDistance).negate();
    }

    /**
     * Energy of photon: E = hf = hc/λ
     */
    public static Quantity<Energy> photonEnergy(Quantity<Length> wavelength) {
        return PhysicalConstants.PLANCK_CONSTANT
                .multiply(PhysicalConstants.SPEED_OF_LIGHT)
                .divide(wavelength)
                .asType(Energy.class);
    }

    /**
     * Diffraction grating: d sin(θ) = mλ
     * Returns diffraction angle for order m
     */
    public static Real diffractionAngle(Real gratingSpacing, int order, Real wavelength) {
        double sinTheta = (order * wavelength.doubleValue()) / gratingSpacing.doubleValue();
        return Real.of(Math.asin(sinTheta));
    }

    /**
     * Rayleigh criterion for resolution: θ_min = 1.22 λ/D
     */
    public static Real rayleighCriterion(Real wavelength, Real apertureDiameter) {
        return Real.of(1.22).multiply(wavelength).divide(apertureDiameter);
    }

    /**
     * Mirror equation: 1/f = 1/do + 1/di
     * Same form as thin lens equation.
     * Returns image distance di given object distance do and focal length f.
     */
    public static Real mirrorEquation(Real objectDistance, Real focalLength) {
        return thinLensImageDistance(objectDistance, focalLength);
    }

    /**
     * Lens Maker's Equation: 1/f = (n - 1) * (1/R1 - 1/R2)
     * For a thin lens in air.
     * 
     * @param n  Refractive index of lens material
     * @param r1 Radius of curvature of first surface (positive if convex towards
     *           light)
     * @param r2 Radius of curvature of second surface (positive if convex towards
     *           light)
     * @return Focal length f
     */
    public static Real lensMakerEquation(Real n, Real r1, Real r2) {
        Real term1 = n.subtract(Real.ONE);
        Real term2 = Real.ONE.divide(r1).subtract(Real.ONE.divide(r2));
        return Real.ONE.divide(term1.multiply(term2));
    }
}
