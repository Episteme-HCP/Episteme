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
package org.jscience.physics.classical.waves.optics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Snell's Law and Fresnel equations for ray optics.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SnellLaw {

    /**
     * Snell's Law: $n_1 \sin\theta_1 = n_2 \sin\theta_2$
     * 
     * @param theta1 Incident angle (radians)
     * @param n1     Refractive index of incident medium
     * @param n2     Refractive index of transmitted medium
     * @return Refracted angle (radians), or null if total internal reflection
     */
    public static Real refract(Real theta1, Real n1, Real n2) {
        Real sinTheta2 = n1.multiply(theta1.sin()).divide(n2);

        // Check for total internal reflection
        if (sinTheta2.abs().compareTo(Real.ONE) > 0) {
            return null;
        }

        return sinTheta2.asin();
    }

    /**
     * Fresnel reflectance for s-polarized light (TE mode).
     */
    public static Real fresnelRs(Real theta1, Real n1, Real n2) {
        Real theta2 = refract(theta1, n1, n2);
        if (theta2 == null)
            return Real.ONE; // Total internal reflection

        Real cosTheta1 = theta1.cos();
        Real cosTheta2 = theta2.cos();

        Real num = n1.multiply(cosTheta1).subtract(n2.multiply(cosTheta2));
        Real den = n1.multiply(cosTheta1).add(n2.multiply(cosTheta2));

        return num.divide(den).pow(2);
    }

    /**
     * Fresnel reflectance for p-polarized light (TM mode).
     */
    public static Real fresnelRp(Real theta1, Real n1, Real n2) {
        Real theta2 = refract(theta1, n1, n2);
        if (theta2 == null)
            return Real.ONE;

        Real cosTheta1 = theta1.cos();
        Real cosTheta2 = theta2.cos();

        Real num = n2.multiply(cosTheta1).subtract(n1.multiply(cosTheta2));
        Real den = n2.multiply(cosTheta1).add(n1.multiply(cosTheta2));

        return num.divide(den).pow(2);
    }

    /**
     * Brewster's angle: $\theta_B = \arctan(n_2 / n_1)$
     */
    public static Real brewsterAngle(Real n1, Real n2) {
        return n2.divide(n1).atan();
    }
}
