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

package org.jscience.mathematics.analysis.special;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Elliptic integrals and Jacobi elliptic functions.
 * <p>
 * Essential for pendulum motion, electromagnetic theory, and orbital mechanics.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EllipticFunctions {

    private static final double EPSILON = 1e-15;
    private static final int MAX_ITERATIONS = 100;

    // ==================== Complete Elliptic Integrals ====================

    /**
     * Complete elliptic integral of the first kind: K(m).
     * <p>
     * $K(m) = \int_0^{\pi/2} \frac{d\theta}{\sqrt{1 - m\sin^2\theta}}$
     * </p>
     * 
     * @param m Parameter (0 Ã¢â€°Â¤ m < 1)
     * @return K(m)
     */
    public static Real completeK(Real m) {
        return Real.of(completeK(m.doubleValue()));
    }

    public static double completeK(double m) {
        if (m < 0 || m >= 1) {
            if (m == 1)
                return Double.POSITIVE_INFINITY;
            throw new IllegalArgumentException("m must be in [0, 1)");
        }

        // Arithmetic-Geometric Mean (AGM) algorithm
        double a = 1.0;
        double b = Math.sqrt(1.0 - m);
        double c = Math.sqrt(m);

        while (Math.abs(c) > EPSILON) {
            double aNew = (a + b) / 2.0;
            double bNew = Math.sqrt(a * b);
            c = (a - b) / 2.0;
            a = aNew;
            b = bNew;
        }

        return Math.PI / (2.0 * a);
    }

    /**
     * Complete elliptic integral of the second kind: E(m).
     * <p>
     * $E(m) = \int_0^{\pi/2} \sqrt{1 - m\sin^2\theta} d\theta$
     * </p>
     * 
     * @param m Parameter (0 Ã¢â€°Â¤ m Ã¢â€°Â¤ 1)
     * @return E(m)
     */
    public static Real completeE(Real m) {
        return Real.of(completeE(m.doubleValue()));
    }

    public static double completeE(double m) {
        if (m < 0 || m > 1) {
            throw new IllegalArgumentException("m must be in [0, 1]");
        }
        if (m == 0)
            return Math.PI / 2.0;
        if (m == 1)
            return 1.0;

        // AGM algorithm with coefficient tracking
        double a = 1.0;
        double b = Math.sqrt(1.0 - m);
        double c = Math.sqrt(m);
        double sum = c * c;
        double power = 1.0;

        while (Math.abs(c) > EPSILON) {
            double aNew = (a + b) / 2.0;
            double bNew = Math.sqrt(a * b);
            c = (a - b) / 2.0;
            power *= 2.0;
            sum += power * c * c;
            a = aNew;
            b = bNew;
        }

        return (Math.PI / (4.0 * a)) * (2.0 - sum);
    }

    // ==================== Incomplete Elliptic Integrals ====================

    /**
     * Incomplete elliptic integral of the first kind: F(Ãâ€ , m).
     * <p>
     * $F(\phi, m) = \int_0^{\phi} \frac{d\theta}{\sqrt{1 - m\sin^2\theta}}$
     * </p>
     * 
     * @param phi Amplitude angle (radians)
     * @param m   Parameter (0 Ã¢â€°Â¤ m < 1)
     * @return F(Ãâ€ , m)
     */
    public static double incompleteF(double phi, double m) {
        if (m < 0 || m >= 1) {
            throw new IllegalArgumentException("m must be in [0, 1)");
        }
        if (phi == 0)
            return 0;

        // Reduce phi to [0, Ãâ‚¬/2]
        int sign = (phi < 0) ? -1 : 1;
        phi = Math.abs(phi);
        int periods = (int) (phi / Math.PI);
        phi = phi - periods * Math.PI;

        boolean reflected = false;
        if (phi > Math.PI / 2) {
            phi = Math.PI - phi;
            reflected = true;
        }

        // Carlson R_F form
        double sinPhi = Math.sin(phi);
        double cosPhi = Math.cos(phi);
        double result = sinPhi * carlsonRF(cosPhi * cosPhi, 1 - m * sinPhi * sinPhi, 1);

        if (reflected) {
            result = 2 * completeK(m) - result;
        }
        result += 2 * periods * completeK(m);

        return sign * result;
    }

    /**
     * Carlson's symmetric form R_F(x, y, z).
     */
    private static double carlsonRF(double x, double y, double z) {
        double A = (x + y + z) / 3.0;
        double Q = Math.max(Math.abs(A - x), Math.max(Math.abs(A - y), Math.abs(A - z)));
        double pow4 = 1.0;

        while (pow4 * Q > EPSILON * Math.abs(A)) {
            double sqrtX = Math.sqrt(x);
            double sqrtY = Math.sqrt(y);
            double sqrtZ = Math.sqrt(z);
            double lambda = sqrtX * sqrtY + sqrtY * sqrtZ + sqrtZ * sqrtX;

            x = (x + lambda) / 4.0;
            y = (y + lambda) / 4.0;
            z = (z + lambda) / 4.0;
            A = (A + lambda) / 4.0;
            pow4 /= 4.0;
        }

        return 1.0 / Math.sqrt(A);
    }

    // ==================== Jacobi Elliptic Functions ====================

    /**
     * Jacobi elliptic functions sn, cn, dn.
     * <p>
     * Returns [sn(u,m), cn(u,m), dn(u,m)].
     * </p>
     * 
     * @param u Argument
     * @param m Parameter (0 Ã¢â€°Â¤ m Ã¢â€°Â¤ 1)
     * @return Array [sn, cn, dn]
     */
    public static double[] jacobi(double u, double m) {
        if (m < 0 || m > 1) {
            throw new IllegalArgumentException("m must be in [0, 1]");
        }

        // Edge cases
        if (m == 0) {
            return new double[] { Math.sin(u), Math.cos(u), 1.0 };
        }
        if (m == 1) {
            double sech = 1.0 / Math.cosh(u);
            return new double[] { Math.tanh(u), sech, sech };
        }

        // Landen transformation (descending)
        double[] a = new double[MAX_ITERATIONS];
        double[] c = new double[MAX_ITERATIONS];
        a[0] = 1.0;
        c[0] = Math.sqrt(m);
        double b = Math.sqrt(1 - m);

        int n = 0;
        while (Math.abs(c[n]) > EPSILON && n < MAX_ITERATIONS - 1) {
            n++;
            a[n] = (a[n - 1] + b) / 2.0;
            c[n] = (a[n - 1] - b) / 2.0;
            b = Math.sqrt(a[n - 1] * b);
        }

        double phi = Math.pow(2, n) * a[n] * u;

        // Ascending recurrence
        for (int i = n; i > 0; i--) {
            double sinPhi = Math.sin(phi);
            phi = (phi + Math.asin(c[i] * sinPhi / a[i])) / 2.0;
        }

        double sn = Math.sin(phi);
        double cn = Math.cos(phi);
        double dn = cn / Math.cos(phi - Math.asin(c[0] * sn));

        return new double[] { sn, cn, dn };
    }

    public static Real sn(Real u, Real m) {
        return Real.of(jacobi(u.doubleValue(), m.doubleValue())[0]);
    }

    public static Real cn(Real u, Real m) {
        return Real.of(jacobi(u.doubleValue(), m.doubleValue())[1]);
    }

    public static Real dn(Real u, Real m) {
        return Real.of(jacobi(u.doubleValue(), m.doubleValue())[2]);
    }
}


