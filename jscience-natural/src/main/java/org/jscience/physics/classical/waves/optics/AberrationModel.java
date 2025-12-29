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
 * Models for optical aberrations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AberrationModel {

    private AberrationModel() {
    }

    /**
     * Approximates longitudinal spherical aberration for a simple lens.
     * LSA ≈ k * h² / f
     * This is a simplified model.
     * 
     * @param rayHeight   Height of ray from optical axis (h)
     * @param focalLength Paraxial focal length (f)
     * @param index       Refractive index (n)
     * @return Longitudinal Spherical Aberration (LSA) estimate
     */
    public static Real sphericalAberration(Real rayHeight, Real focalLength, Real index) {
        // Simple third-order approximation factor for a plano-convex lens
        // Factor derived from Seidel aberration theory simplified
        Real k = index.pow(2).multiply(Real.of(0.5));
        return k.multiply(rayHeight.pow(2)).divide(focalLength);
    }

    /**
     * Axial chromatic aberration.
     * ACA = f_red - f_blue
     * 
     * @param fBlue Focal length for blue light
     * @param fRed  Focal length for red light
     * @return Axial Chromatic Aberration
     */
    public static Real chromaticAberration(Real fBlue, Real fRed) {
        return fRed.subtract(fBlue);
    }

    /**
     * Coma aberration estimate.
     * Coma produces comet-shaped blur for off-axis points.
     * Sagittal coma ≈ 3 * k * h² * θ / f²
     * 
     * @param rayHeight   Height of ray from axis (h)
     * @param fieldAngle  Off-axis field angle (θ in radians)
     * @param focalLength Focal length (f)
     * @param shapeCoeff  Shape factor coefficient (depends on lens configuration)
     * @return Coma aberration estimate
     */
    public static Real comaAberration(Real rayHeight, Real fieldAngle, Real focalLength, Real shapeCoeff) {
        return shapeCoeff.multiply(Real.of(3.0))
                .multiply(rayHeight.pow(2))
                .multiply(fieldAngle)
                .divide(focalLength.pow(2));
    }

    /**
     * Astigmatism aberration.
     * Causes different focal lengths for tangential vs sagittal rays.
     * ΔS ≈ k * θ² * f
     * 
     * @param fieldAngle  Off-axis field angle (θ in radians)
     * @param focalLength Focal length
     * @param astCoeff    Astigmatism coefficient
     * @return Astigmatic focus difference
     */
    public static Real astigmatism(Real fieldAngle, Real focalLength, Real astCoeff) {
        return astCoeff.multiply(fieldAngle.pow(2)).multiply(focalLength);
    }

    /**
     * Field curvature (Petzval curvature).
     * Image surface is curved rather than flat.
     * Radius of curvature R_p = f / Σ(1/n_i)
     * 
     * @param focalLength System focal length
     * @param sumInverseN Sum of 1/n for each lens element
     * @return Petzval radius of curvature
     */
    public static Real petzvalCurvature(Real focalLength, Real sumInverseN) {
        return focalLength.divide(sumInverseN);
    }

    /**
     * Barrel/pincushion distortion.
     * r' = r * (1 + k₁*r² + k₂*r⁴ + ...)
     * 
     * @param radialDistance Distance from optical center (r)
     * @param k1             Third-order distortion coefficient
     * @param k2             Fifth-order distortion coefficient (optional)
     * @return Distorted radial position
     */
    public static Real distortion(Real radialDistance, Real k1, Real k2) {
        Real r2 = radialDistance.pow(2);
        Real r4 = r2.pow(2);
        Real factor = Real.ONE.add(k1.multiply(r2)).add(k2.multiply(r4));
        return radialDistance.multiply(factor);
    }

    /**
     * Calculates the Abbe number (dispersion) for a material.
     * V = (n_d - 1) / (n_F - n_C)
     * 
     * @param nD Index at 587.6nm (yellow, Fraunhofer D line)
     * @param nF Index at 486.1nm (blue, Fraunhofer F line)
     * @param nC Index at 656.3nm (red, Fraunhofer C line)
     * @return Abbe number
     */
    public static Real abbeNumber(Real nD, Real nF, Real nC) {
        return nD.subtract(Real.ONE).divide(nF.subtract(nC));
    }
}
