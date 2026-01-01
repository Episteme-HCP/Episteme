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

package org.jscience.physics.relativity;

import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.linearalgebra.tensors.DenseTensor;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.geometry.Vector4D;

/**
 * The Kerr metric describing spacetime around a rotating uncharged
 * axially-symmetric black hole.
 * <p>
 * Uses Boyer-Lindquist coordinates: $(ct, r, \theta, \phi)$.
 * Metric signature: $(-, +, +, +)$.
 *
 * Main parameters:
 * <ul>
 * <li>$r_s$: Schwarzschild radius ($2GM/c^2$)</li>
 * <li>$a$: Spin parameter ($J/Mc$)</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class KerrMetric implements SpacetimeMetric {

    private final Real rs; // Schwarzschild radius
    private final Real a; // Spin parameter

    /**
     * @param schwarzschildRadius ($r_s = 2GM/c^2$)
     * @param spinParameter       ($a = J/Mc$)
     */
    public KerrMetric(Real schwarzschildRadius, Real spinParameter) {
        this.rs = schwarzschildRadius;
        this.a = spinParameter;
    }

    @Override
    public Tensor<Real> getMetricTensor(Vector4D point) {
        // point: (ct, r, theta, phi)
        // Indices: 0=t, 1=r, 2=theta, 3=phi

        Real r = point.x(); // x^1 -> r
        Real theta = point.y(); // x^2 -> theta

        // Precompute frequent terms
        Real r2 = r.pow(2);
        Real a2 = a.pow(2);
        Real cosTheta = theta.cos();
        Real sinTheta = theta.sin();
        Real sin2Theta = sinTheta.pow(2);
        Real cos2Theta = cosTheta.pow(2);

        // rho^2 = r^2 + a^2 * cos^2(theta)
        Real rho2 = r2.add(a2.multiply(cos2Theta));

        // Delta = r^2 - rs * r + a^2
        Real delta = r2.subtract(rs.multiply(r)).add(a2);

        // Common term: rs * r / rho^2
        Real common = rs.multiply(r).divide(rho2);

        // g_00 = -(1 - rs * r / rho^2)
        // Note: we use c=1 units effectively for structure, vector4D has ct.
        Real g00 = Real.ONE.subtract(common).negate();

        // g_11 = rho^2 / Delta
        Real g11 = rho2.divide(delta);

        // g_22 = rho^2
        Real g22 = rho2;

        // g_33 = (r^2 + a^2 + (rs * r * a^2 / rho^2) * sin^2(theta)) * sin^2(theta)
        // = (r^2 + a^2 + common * a^2 * sin^2(theta)) * sin^2(theta)
        // Actually often written as: sigma^2 / rho^2 * sin^2(theta) where sigma^2 =
        // (r^2+a^2)^2 - a^2*delta*sin^2(theta)
        // Or expanded:
        Real termPhi = r2.add(a2).add(common.multiply(a2).multiply(sin2Theta));
        Real g33 = termPhi.multiply(sin2Theta);

        // Off-diagonal terms g_03 = g_30
        // g_03 = - (2 * rs * r * a * sin^2(theta)) / rho^2
        // = - common * 2 * a * sin^2(theta) * (correction factor for c?)
        // Standard form: - 2*M*r*a*sin^2 / rho^2. Here rs = 2M. So - rs*r*a*sin^2 /
        // rho^2.
        Real g03 = common.multiply(a).multiply(sin2Theta).negate();

        // Construct 4x4 matrix
        Real zero = Real.ZERO;

        Real[] data = new Real[] {
                g00, zero, zero, g03,
                zero, g11, zero, zero,
                zero, zero, g22, zero,
                g03, zero, zero, g33
        };

        return new DenseTensor<>(data, 4, 4);
    }

    // --- TopologicalSpace / Set Defaults ---

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsPoint(Vector4D p) {
        return true;
    }

    // Set interface compatibility
    @Override
    public boolean contains(Vector4D item) {
        return containsPoint(item);
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public boolean isClosed() {
        return true;
    }

    @Override
    public String description() {
        return "Kerr Spacetime (Rotating Black Hole)";
    }
}


