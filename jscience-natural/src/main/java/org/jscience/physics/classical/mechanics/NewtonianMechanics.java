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

package org.jscience.physics.classical.mechanics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.*;

/**
 * Newtonian mechanics equations and models.
 *
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * Newton, I. (1687). <i>Philosophiæ Naturalis Principia Mathematica</i>. Londini.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NewtonianMechanics {

    /**
     * Newton's second law: F = ma
     */
    public static Quantity<Force> force(Quantity<Mass> mass, Quantity<Acceleration> acceleration) {
        // F = ma
        return mass.multiply(acceleration).asType(Force.class);
    }

    /**
     * Kinetic energy: KE = (1/2)mvÃ‚Â²
     */
    public static Quantity<Energy> kineticEnergy(Quantity<Mass> mass, Quantity<Velocity> velocity) {
        // KE = 0.5 * m * vÃ‚Â²
        Real half = Real.of(0.5);
        return mass.multiply(velocity).multiply(velocity).multiply(half).asType(Energy.class);
    }

    /**
     * Momentum: p = mv
     */
    public static Quantity<?> momentum(Quantity<Mass> mass, Quantity<Velocity> velocity) {
        return mass.multiply(velocity);
    }

    /**
     * Gravitational potential energy: PE = mgh
     */
    public static Quantity<Energy> gravitationalPotentialEnergy(
            Quantity<Mass> mass,
            Quantity<Acceleration> g,
            Quantity<Length> height) {
        return mass.multiply(g).multiply(height).asType(Energy.class);
    }
}


