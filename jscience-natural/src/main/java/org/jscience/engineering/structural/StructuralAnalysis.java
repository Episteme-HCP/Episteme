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

package org.jscience.engineering.structural;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Force;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Pressure;

/**
 * Structural analysis calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StructuralAnalysis {

    /**
     * Simply supported beam: maximum deflection at center.
     * δ_max = (5 * w * L⁴) / (384 * E * I)
     */
    public static Real simpleBeamDeflectionUniform(Real w, Real L, Real E, Real I) {
        return Real.of(5).multiply(w).multiply(L.pow(4)).divide(Real.of(384).multiply(E).multiply(I));
    }

    /**
     * Simply supported beam: deflection under point load at center.
     * δ_max = (P * L³) / (48 * E * I)
     */
    public static Real simpleBeamDeflectionPoint(Real P, Real L, Real E, Real I) {
        return P.multiply(L.pow(3)).divide(Real.of(48).multiply(E).multiply(I));
    }

    /**
     * Cantilever beam: deflection at free end under uniform load.
     * δ_max = (w * L⁴) / (8 * E * I)
     */
    public static Real cantileverDeflectionUniform(Real w, Real L, Real E, Real I) {
        return w.multiply(L.pow(4)).divide(Real.of(8).multiply(E).multiply(I));
    }

    /**
     * Cantilever beam: deflection at free end under point load.
     * δ_max = (P * L³) / (3 * E * I)
     */
    public static Real cantileverDeflectionPoint(Real P, Real L, Real E, Real I) {
        return P.multiply(L.pow(3)).divide(Real.of(3).multiply(E).multiply(I));
    }

    /**
     * Maximum bending moment for simply supported beam with uniform load.
     * M_max = w * L² / 8
     */
    public static Real simpleBeamMomentUniform(Real w, Real L) {
        return w.multiply(L.pow(2)).divide(Real.of(8));
    }

    /**
     * Maximum bending moment for cantilever with uniform load.
     * M_max = w * L² / 2
     */
    public static Real cantileverMomentUniform(Real w, Real L) {
        return w.multiply(L.pow(2)).divide(Real.TWO);
    }

    /**
     * Bending stress.
     * σ = M * y / I
     */
    public static Real bendingStress(Real moment, Real distanceFromNA, Real momentOfInertia) {
        return moment.multiply(distanceFromNA).divide(momentOfInertia);
    }

    /**
     * Shear stress in beam.
     * τ = V * Q / (I * b)
     */
    public static Real shearStress(Real shearForce, Real firstMoment, Real momentOfInertia, Real width) {
        return shearForce.multiply(firstMoment).divide(momentOfInertia.multiply(width));
    }

    /**
     * Second moment of area for rectangle.
     * I = b * h³ / 12
     */
    public static Real rectangleMomentOfInertia(Real width, Real height) {
        return width.multiply(height.pow(3)).divide(Real.of(12));
    }

    /**
     * Second moment of area for circle.
     * I = π * r⁴ / 4
     */
    public static Real circleMomentOfInertia(Real radius) {
        return Real.PI.multiply(radius.pow(4)).divide(Real.of(4));
    }

    /**
     * Section modulus.
     * S = I / y_max
     */
    public static Real sectionModulus(Real momentOfInertia, Real distanceToExtreme) {
        return momentOfInertia.divide(distanceToExtreme);
    }

    /**
     * Column buckling (Euler formula).
     * P_cr = π² * E * I / (K * L)²
     */
    public static Real eulerBucklingLoad(Real E, Real I, Real L, Real K) {
        return Real.PI.pow(2).multiply(E).multiply(I).divide(K.multiply(L).pow(2));
    }

    /**
     * Slenderness ratio.
     * λ = K * L / r
     */
    public static Real slendernessRatio(Real K, Real L, Real radiusOfGyration) {
        return K.multiply(L).divide(radiusOfGyration);
    }

    /**
     * Radius of gyration.
     * r = √(I / A)
     */
    public static Real radiusOfGyration(Real momentOfInertia, Real area) {
        return momentOfInertia.divide(area).sqrt();
    }

    /**
     * Truss member force (method of joints - simplified).
     */
    public static Real trussMemberForce(Real angleDegrees, Real jointForce) {
        return jointForce.divide(angleDegrees.toRadians().cos());
    }

    /**
     * Torsional shear stress in circular shaft.
     * τ = T * r / J where J = πr⁴/2
     */
    public static Real torsionalShearStress(Real torque, Real radius) {
        Real J = Real.PI.multiply(radius.pow(4)).divide(Real.TWO);
        return torque.multiply(radius).divide(J);
    }

    /**
     * Angle of twist.
     * θ = T * L / (G * J)
     */
    public static Real angleOfTwist(Real torque, Real length, Real shearModulus, Real radius) {
        Real J = Real.PI.multiply(radius.pow(4)).divide(Real.TWO);
        return torque.multiply(length).divide(shearModulus.multiply(J));
    }

    // === Quantity Overloads ===

    /**
     * Bending stress using Quantity types.
     */
    public static Quantity<Pressure> bendingStressQ(Quantity<?> moment, Quantity<Length> distanceFromNA,
            Real momentOfInertia) {
        double m = moment.getValue().doubleValue();
        double y = distanceFromNA.to(Units.METER).getValue().doubleValue();
        double stress = m * y / momentOfInertia.doubleValue();
        return Quantities.create(stress, Units.PASCAL);
    }

    /**
     * Euler buckling load using Quantity types.
     */
    public static Quantity<Force> eulerBucklingLoadQ(Quantity<Pressure> E, Real I, Quantity<Length> L, Real K) {
        double ePa = E.to(Units.PASCAL).getValue().doubleValue();
        double lM = L.to(Units.METER).getValue().doubleValue();
        Real pCr = eulerBucklingLoad(Real.of(ePa), I, Real.of(lM), K);
        return Quantities.create(pCr.doubleValue(), Units.NEWTON);
    }
}
