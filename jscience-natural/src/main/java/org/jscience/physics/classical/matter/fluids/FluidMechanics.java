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

package org.jscience.physics.classical.matter.fluids;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Fluid mechanics - Navier-Stokes, Bernoulli, Reynolds number, viscosity.
 * <p>
 * Implements classical and modern computational fluid dynamics equations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FluidMechanics {

    /**
     * Bernoulli's equation (incompressible flow):
     * P + Ã‚Â½ÃÂvÃ‚Â² + ÃÂgh = constant
     * 
     * Returns pressure at point 2 given conditions at point 1
     */
    public static Real bernoulliPressure(Real P1, Real rho, Real v1, Real v2, Real h1, Real h2, Real g) {
        // P1 + Ã‚Â½ÃÂv1Ã‚Â² + ÃÂgh1 = P2 + Ã‚Â½ÃÂv2Ã‚Â² + ÃÂgh2
        Real term1 = P1;
        Real term2 = Real.of(0.5).multiply(rho).multiply(v1.multiply(v1).subtract(v2.multiply(v2)));
        Real term3 = rho.multiply(g).multiply(h1.subtract(h2));
        return term1.add(term2).add(term3);
    }

    /**
     * Reynolds number: Re = ÃÂvL/ÃŽÂ¼ = vL/ÃŽÂ½
     * Determines laminar (Re < 2300) vs turbulent (Re > 4000) flow
     */
    public static Real reynoldsNumber(Real density, Real velocity, Real charLength, Real dynamicViscosity) {
        return density.multiply(velocity).multiply(charLength).divide(dynamicViscosity);
    }

    /**
     * Reynolds number (kinematic viscosity version): Re = vL/ÃŽÂ½
     */
    public static Real reynoldsNumberKinematic(Real velocity, Real charLength, Real kinematicViscosity) {
        return velocity.multiply(charLength).divide(kinematicViscosity);
    }

    /**
     * Continuity equation (mass conservation): AÃ¢â€šÂvÃ¢â€šÂ = AÃ¢â€šâ€švÃ¢â€šâ€š
     * Returns velocity at point 2
     */
    public static Real continuityVelocity(Real area1, Real vel1, Real area2) {
        return area1.multiply(vel1).divide(area2);
    }

    /**
     * Hagen-Poiseuille equation (laminar pipe flow):
     * Q = Ãâ‚¬ÃŽâ€PrÃ¢ÂÂ´/(8ÃŽÂ¼L)
     * 
     * Returns volumetric flow rate
     */
    public static Real poiseuilleFlow(Real pressureDrop, Real radius, Real viscosity, Real length) {
        Real r4 = radius.multiply(radius).multiply(radius).multiply(radius);
        Real numerator = Real.PI.multiply(pressureDrop).multiply(r4);
        Real denominator = Real.of(8).multiply(viscosity).multiply(length);
        return numerator.divide(denominator);
    }

    /**
     * Stokes' law (drag on sphere): F_drag = 6Ãâ‚¬ÃŽÂ¼rv
     */
    public static Real stokesDrag(Real viscosity, Real radius, Real velocity) {
        return Real.of(6 * Math.PI).multiply(viscosity).multiply(radius).multiply(velocity);
    }

    /**
     * Terminal velocity (sphere falling): v_t = (2rÃ‚Â²g(ÃÂ_s - ÃÂ_f))/(9ÃŽÂ¼)
     */
    public static Real terminalVelocity(Real radius, Real g, Real sphereDensity,
            Real fluidDensity, Real viscosity) {
        Real twoR2G = Real.TWO.multiply(radius).multiply(radius).multiply(g);
        Real densityDiff = sphereDensity.subtract(fluidDensity);
        Real numerator = twoR2G.multiply(densityDiff);
        Real denominator = Real.of(9).multiply(viscosity);
        return numerator.divide(denominator);
    }

    /**
     * Drag coefficient: C_d = 2F_d/(ÃÂvÃ‚Â²A)
     */
    public static Real dragCoefficient(Real dragForce, Real density, Real velocity, Real area) {
        return Real.TWO.multiply(dragForce).divide(
                density.multiply(velocity).multiply(velocity).multiply(area));
    }

    /**
     * Lift coefficient: C_l = 2F_l/(ÃÂvÃ‚Â²A)
     */
    public static Real liftCoefficient(Real liftForce, Real density, Real velocity, Real area) {
        return Real.TWO.multiply(liftForce).divide(
                density.multiply(velocity).multiply(velocity).multiply(area));
    }

    /**
     * Mach number: M = v/c (velocity / speed of sound)
     */
    public static Real machNumber(Real velocity, Real soundSpeed) {
        return velocity.divide(soundSpeed);
    }

    /**
     * Froude number: Fr = v/Ã¢Ë†Å¡(gL)
     * Ratio of inertial to gravitational forces
     */
    public static Real froudeNumber(Real velocity, Real g, Real charLength) {
        return velocity.divide(g.multiply(charLength).sqrt());
    }

    /**
     * Weber number: We = ÃÂvÃ‚Â²L/ÃÆ’
     * Ratio of inertial to surface tension forces
     */
    public static Real weberNumber(Real density, Real velocity, Real charLength, Real surfaceTension) {
        return density.multiply(velocity).multiply(velocity).multiply(charLength).divide(surfaceTension);
    }
}


