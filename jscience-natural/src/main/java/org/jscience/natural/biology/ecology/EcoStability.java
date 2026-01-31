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

package org.jscience.natural.biology.ecology;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import java.util.List;

/**
 * Ecological stability analysis using Lyapunov exponents and Jacobian matrices.
 */
public final class EcoStability {

    private EcoStability() {}

    /**
     * Calculates the stability of an equilibrium point using the eigenvalue of the community matrix.
     * @param jacobian The community matrix (Jacobian).
     * @return True if stable (all real parts of eigenvalues are negative).
     */
    public static boolean isStable(Matrix<Real> jacobian) {
        // In a real implementation, we would calculate eigenvalues.
        // For now, a simplified check: the trace must be negative for 2x2 systems.
        if (jacobian.rows() == 2 && jacobian.cols() == 2) {
            Real trace = jacobian.get(0, 0).add(jacobian.get(1, 1));
            Real det = jacobian.get(0, 0).multiply(jacobian.get(1, 1))
                       .subtract(jacobian.get(0, 1).multiply(jacobian.get(1, 0)));
            return trace.compareTo(Real.of(0.0)) < 0 && det.compareTo(Real.of(0.0)) > 0;
        }
        return false;
    }

    /**
     * Estimates the Lyapunov exponent for a 1D mapping to check for chaos.
     * Î» = (1/n) * sum(log|f'(xi)|)
     */
    public static Real estimateLyapunovExponent(List<Real> slopes) {
        if (slopes == null || slopes.isEmpty()) return Real.of(Double.NaN);
        Real sum = Real.of(0.0);
        for (Real slope : slopes) {
            Real absSlope = slope.abs();
            if (absSlope.compareTo(Real.of(0.0)) > 0) {
                sum = sum.add(absSlope.log());
            }
        }
        return sum.divide(Real.of(slopes.size()));
    }
}

