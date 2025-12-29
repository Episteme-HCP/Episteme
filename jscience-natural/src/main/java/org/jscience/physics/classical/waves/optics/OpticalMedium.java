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
 * Optical material properties for refraction/reflection calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OpticalMedium {

    private final String name;
    private final Real refractiveIndex; // n (at specific wavelength)

    public static final OpticalMedium VACUUM = new OpticalMedium("Vacuum", Real.ONE);
    public static final OpticalMedium AIR = new OpticalMedium("Air", Real.of(1.000293));
    public static final OpticalMedium WATER = new OpticalMedium("Water", Real.of(1.333));
    public static final OpticalMedium GLASS = new OpticalMedium("Glass (Crown)", Real.of(1.52));
    public static final OpticalMedium DIAMOND = new OpticalMedium("Diamond", Real.of(2.417));

    public OpticalMedium(String name, Real refractiveIndex) {
        this.name = name;
        this.refractiveIndex = refractiveIndex;
    }

    /**
     * Speed of light in this medium: $v = c/n$
     */
    public Real getPhaseVelocity() {
        Real c = Real.of(299792458.0);
        return c.divide(refractiveIndex);
    }

    /**
     * Critical angle for total internal reflection (this -> other medium).
     * $\theta_c = \arcsin(n_2 / n_1)$ (only if $n_1 > n_2$)
     */
    public Real criticalAngle(OpticalMedium other) {
        if (refractiveIndex.compareTo(other.refractiveIndex) <= 0) {
            return null; // No total internal reflection
        }
        Real ratio = other.refractiveIndex.divide(refractiveIndex);
        return ratio.asin(); // radians
    }

    // --- Accessors ---
    public String getName() {
        return name;
    }

    public Real getRefractiveIndex() {
        return refractiveIndex;
    }
}
