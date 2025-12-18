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
package org.jscience.physics.classical.matter.fluids;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Fluid mechanics - Navier-Stokes, Bernoulli, Reynolds number, viscosity.
 * <p>
 * Implements classical and modern computational fluid dynamics equations.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class FluidMechanics {

    /**
     * Bernoulli's equation (incompressible flow):
     * P + ½ρv² + ρgh = constant
     * 
     * Returns pressure at point 2 given conditions at point 1
     */
    public static Real bernoulliPressure(Real P1, Real rho, Real v1, Real v2, Real h1, Real h2, Real g) {
        // P1 + ½ρv1² + ρgh1 = P2 + ½ρv2² + ρgh2
        Real term1 = P1;
        Real term2 = Real.of(0.5).multiply(rho).multiply(v1.multiply(v1).subtract(v2.multiply(v2)));
        Real term3 = rho.multiply(g).multiply(h1.subtract(h2));
        return term1.add(term2).add(term3);
    }

    /**
     * Reynolds number: Re = ρvL/μ = vL/ν
     * Determines laminar (Re < 2300) vs turbulent (Re > 4000) flow
     */
    public static Real reynoldsNumber(Real density, Real velocity, Real charLength, Real dynamicViscosity) {
        return density.multiply(velocity).multiply(charLength).divide(dynamicViscosity);
    }

    /**
     * Reynolds number (kinematic viscosity version): Re = vL/ν
     */
    public static Real reynoldsNumberKinematic(Real velocity, Real charLength, Real kinematicViscosity) {
        return velocity.multiply(charLength).divide(kinematicViscosity);
    }

    /**
     * Continuity equation (mass conservation): A₁v₁ = A₂v₂
     * Returns velocity at point 2
     */
    public static Real continuityVelocity(Real area1, Real vel1, Real area2) {
        return area1.multiply(vel1).divide(area2);
    }

    /**
     * Hagen-Poiseuille equation (laminar pipe flow):
     * Q = πΔPr⁴/(8μL)
     * 
     * Returns volumetric flow rate
     */
    public static Real poiseuilleFlow(Real pressureDrop, Real radius, Real viscosity, Real length) {
        Real r4 = radius.multiply(radius).multiply(radius).multiply(radius);
        Real numerator = Real.of(Math.PI).multiply(pressureDrop).multiply(r4);
        Real denominator = Real.of(8).multiply(viscosity).multiply(length);
        return numerator.divide(denominator);
    }

    /**
     * Stokes' law (drag on sphere): F_drag = 6πμrv
     */
    public static Real stokesDrag(Real viscosity, Real radius, Real velocity) {
        return Real.of(6 * Math.PI).multiply(viscosity).multiply(radius).multiply(velocity);
    }

    /**
     * Terminal velocity (sphere falling): v_t = (2r²g(ρ_s - ρ_f))/(9μ)
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
     * Drag coefficient: C_d = 2F_d/(ρv²A)
     */
    public static Real dragCoefficient(Real dragForce, Real density, Real velocity, Real area) {
        return Real.TWO.multiply(dragForce).divide(
                density.multiply(velocity).multiply(velocity).multiply(area));
    }

    /**
     * Lift coefficient: C_l = 2F_l/(ρv²A)
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
     * Froude number: Fr = v/√(gL)
     * Ratio of inertial to gravitational forces
     */
    public static Real froudeNumber(Real velocity, Real g, Real charLength) {
        return velocity.divide(g.multiply(charLength).sqrt());
    }

    /**
     * Weber number: We = ρv²L/σ
     * Ratio of inertial to surface tension forces
     */
    public static Real weberNumber(Real density, Real velocity, Real charLength, Real surfaceTension) {
        return density.multiply(velocity).multiply(velocity).multiply(charLength).divide(surfaceTension);
    }
}
