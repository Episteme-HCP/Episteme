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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.engineering.fluids;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.physics.PhysicalConstants;
import org.jscience.measure.quantity.Pressure;
import org.jscience.measure.quantity.Velocity;

/**
 * Fluid flow calculations.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FluidFlow {

    private FluidFlow() {
    }

    /** Reynolds number: Re = v * L / ν */
    public static Real reynoldsNumber(Real velocity, Real characteristicLength, Real kinematicViscosity) {
        return velocity.multiply(characteristicLength).divide(kinematicViscosity);
    }

    /** Flow regime from Reynolds number */
    public static String flowRegime(Real reynoldsNumber) {
        double re = reynoldsNumber.doubleValue();
        if (re < 2300)
            return "Laminar";
        if (re < 4000)
            return "Transitional";
        return "Turbulent";
    }

    /** Bernoulli's equation */
    public static Quantity<Pressure> bernoulliPressure(
            Quantity<Pressure> p1, Quantity<Velocity> v1, Real h1,
            Quantity<Velocity> v2, Real h2, Real density) {

        Real P1 = p1.to(Units.PASCAL).getValue();
        Real V1 = v1.to(Units.METERS_PER_SECOND).getValue();
        Real V2 = v2.to(Units.METERS_PER_SECOND).getValue();
        Real g = PhysicalConstants.g_n;

        Real P2 = P1.add(Real.of(0.5).multiply(density).multiply(V1.pow(2).subtract(V2.pow(2))))
                .add(density.multiply(g).multiply(h1.subtract(h2)));
        return Quantities.create(P2, Units.PASCAL);
    }

    /** Hagen-Poiseuille: Q = π * ΔP * r⁴ / (8 * μ * L) */
    public static Real laminarPipeFlow(Real pressureDrop, Real radius, Real dynamicViscosity, Real length) {
        return Real.PI.multiply(pressureDrop).multiply(radius.pow(4))
                .divide(Real.of(8).multiply(dynamicViscosity).multiply(length));
    }

    /** Laminar friction factor: f = 64 / Re */
    public static Real laminarFrictionFactor(Real reynoldsNumber) {
        return Real.of(64).divide(reynoldsNumber);
    }
}
