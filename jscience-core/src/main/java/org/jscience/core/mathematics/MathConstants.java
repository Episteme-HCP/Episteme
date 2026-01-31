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

package org.jscience.core.mathematics;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Dimensionless;


/**
 * A collection of useful mathematical constants stored as high-precision {@link Real} numbers.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @author Mark Hale
 * @since 1.0
 */
public final class MathConstants {

    private MathConstants() {}

    /**
     * The number π (pi), the ratio of a circle's circumference to its diameter.
     */
    public static final Real PI = Real.PI;
    public static final Quantity<Dimensionless> PI_QTY = Quantities.create(PI, Units.ONE);

    /**
     * Two times π (τ - tau).
     */
    public static final Real TWO_PI = Real.of("6.2831853071795864769252867665590057683943387987502");
    public static final Quantity<Dimensionless> TWO_PI_QTY = Quantities.create(TWO_PI, Units.ONE);

    /**
     * Half times π.
     */
    public static final Real HALF_PI = PI.divide(Real.of(2));
    public static final Quantity<Dimensionless> HALF_PI_QTY = Quantities.create(HALF_PI, Units.ONE);

    /**
     * The base of natural logarithms (e).
     */
    public static final Real E = Real.E;
    public static final Quantity<Dimensionless> E_QTY = Quantities.create(E, Units.ONE);

    /**
     * Square root of 2.
     */
    public static final Real SQRT2 = Real.of("1.4142135623730950488016887242096980785696718753769");
    public static final Quantity<Dimensionless> SQRT2_QTY = Quantities.create(SQRT2, Units.ONE);

    /**
     * Square root of 2π.
     */
    public static final Real SQRT2PI = Real.of("2.5066282746310005024157652848110452530069867406099");
    public static final Quantity<Dimensionless> SQRT2PI_QTY = Quantities.create(SQRT2PI, Units.ONE);

    /**
     * Euler's gamma constant (γ).
     */
    public static final Real GAMMA = Real.of("0.57721566490153286060651209008240243104215933593992");
    public static final Quantity<Dimensionless> GAMMA_QTY = Quantities.create(GAMMA, Units.ONE);

    /**
     * The golden ratio (φ).
     */
    public static final Real GOLDEN_RATIO = Real.of("1.6180339887498948482045868343656381177203091798058");
    public static final Quantity<Dimensionless> GOLDEN_RATIO_QTY = Quantities.create(GOLDEN_RATIO, Units.ONE);

    /**
     * Natural logarithm of 10.
     */
    public static final Real LOG10 = Real.of("2.30258509299404568401799145468436420760110148862877");
    public static final Quantity<Dimensionless> LOG10_QTY = Quantities.create(LOG10, Units.ONE);
    
    /**
     * Natural logarithm of 2.
     */
    public static final Real LOG2 = Real.of("0.69314718055994530941723212145817656807550013436025");
    public static final Quantity<Dimensionless> LOG2_QTY = Quantities.create(LOG2, Units.ONE);

}

