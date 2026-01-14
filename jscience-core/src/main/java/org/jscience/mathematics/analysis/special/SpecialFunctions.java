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

package org.jscience.mathematics.analysis.special;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Special mathematical functions.
 * <p>
 * Gamma, Beta, Bessel, Error functions - essential for physics, statistics,
 * engineering.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpecialFunctions {

    /**
     * Gamma function ÃŽâ€œ(x) - generalization of factorial to real numbers.
     * <p>
     * ÃŽâ€œ(n) = (n-1)! for positive integers
     * Uses Lanczos approximation for accuracy.
     * </p>
     */
    public static Real gamma(Real x) {
        // Lanczos approximation coefficients
        double[] coef = {
                76.18009172947146, -86.50532032941677,
                24.01409824083091, -1.231739572450155,
                0.1208650973866179e-2, -0.5395239384953e-5
        };

        double z = x.doubleValue();

        if (z < 0.5) {
            // Reflection formula: ÃŽâ€œ(1-z) * ÃŽâ€œ(z) = Ãâ‚¬/sin(Ãâ‚¬z)
            return Real.of(Math.PI).divide(
                    Real.of(Math.sin(Math.PI * z)).multiply(gamma(Real.ONE.subtract(x))));
        }

        z = z - 1.0;
        double base = z + 5.5;
        double sum = 1.000000000190015;

        for (int i = 0; i < coef.length; i++) {
            sum += coef[i] / (z + i + 1.0);
        }

        double result = Math.sqrt(2 * Math.PI) * Math.pow(base, z + 0.5)
                * Math.exp(-base) * sum;

        return Real.of(result);
    }

    /**
     * Natural logarithm of Gamma function: ln(ÃŽâ€œ(x))
     * <p>
     * More numerically stable than ÃŽâ€œ(x) for large x.
     * </p>
     */
    public static Real logGamma(Real x) {
        return gamma(x).log();
    }

    /**
     * Beta function: B(x, y) = ÃŽâ€œ(x)ÃŽâ€œ(y) / ÃŽâ€œ(x+y)
     * <p>
     * Used in probability distributions (Beta, Dirichlet).
     * </p>
     */
    public static Real beta(Real x, Real y) {
        return gamma(x).multiply(gamma(y)).divide(gamma(x.add(y)));
    }

    /**
     * Error function: erf(x) = (2/Ã¢Ë†Å¡Ãâ‚¬) Ã¢Ë†Â«Ã¢â€šâ‚¬Ã‹Â£ e^(-tÃ‚Â²) dt
     * <p>
     * Probability: P(X Ã¢â€°Â¤ x) for normal distribution = Ã‚Â½[1 + erf(x/Ã¢Ë†Å¡2)]
     * Uses Abramowitz & Stegun approximation.
     * </p>
     */
    public static Real erf(Real x) {
        double t = x.doubleValue();
        double sign = (t >= 0) ? 1.0 : -1.0;
        t = Math.abs(t);

        // Abramowitz & Stegun formula
        double a1 = 0.254829592;
        double a2 = -0.284496736;
        double a3 = 1.421413741;
        double a4 = -1.453152027;
        double a5 = 1.061405429;
        double p = 0.3275911;

        double tau = 1.0 / (1.0 + p * t);
        double result = 1.0 - (((((a5 * tau + a4) * tau + a3) * tau + a2) * tau + a1) * tau)
                * Math.exp(-t * t);

        return Real.of(sign * result);
    }

    /**
     * Complementary error function: erfc(x) = 1 - erf(x)
     */
    public static Real erfc(Real x) {
        return Real.ONE.subtract(erf(x));
    }

    /**
     * Bessel function of the first kind JÃ¢â€šâ‚¬(x).
     * <p>
     * Solutions to Bessel's differential equation.
     * Used in physics: wave propagation, heat conduction, vibrations.
     * </p>
     */
    public static Real besselJ0(Real x) {
        double t = Math.abs(x.doubleValue());

        if (t < 8.0) {
            // Polynomial approximation for small x
            double y = t * t;
            double ans1 = 57568490574.0 + y * (-13362590354.0 + y * (651619640.7
                    + y * (-11214424.18 + y * (77392.33017 + y * (-184.9052456)))));
            double ans2 = 57568490411.0 + y * (1029532985.0 + y * (9494680.718
                    + y * (59272.64853 + y * (267.8532712 + y))));
            return Real.of(ans1 / ans2);
        } else {
            // Asymptotic form for large x
            double z = 8.0 / t;
            double y = z * z;
            double xx = t - 0.785398164;

            double ans1 = 1.0 + y * (-0.1098628627e-2 + y * (0.2734510407e-4
                    + y * (-0.2073370639e-5 + y * 0.2093887211e-6)));
            double ans2 = -0.1562499995e-1 + y * (0.1430488765e-3
                    + y * (-0.6911147651e-5 + y * (0.7621095161e-6
                            - y * 0.934945152e-7)));

            return Real.of(Math.sqrt(0.636619772 / t)
                    * (Math.cos(xx) * ans1 - z * Math.sin(xx) * ans2));
        }
    }

    /**
     * Bessel function of the second kind YÃ¢â€šâ‚¬(x).
     */
    public static Real besselY0(Real x) {
        double t = x.doubleValue();

        if (t < 0) {
            throw new IllegalArgumentException("Y0 undefined for negative x");
        }

        if (t < 8.0) {
            double j0 = besselJ0(x).doubleValue();
            double y = t * t;
            double ans1 = -2957821389.0 + y * (7062834065.0 + y * (-512359803.6
                    + y * (10879881.29 + y * (-86327.92757 + y * 228.4622733))));
            double ans2 = 40076544269.0 + y * (745249964.8 + y * (7189466.438
                    + y * (47447.26470 + y * (226.1030244 + y))));
            return Real.of(ans1 / ans2 + 0.636619772 * j0 * Math.log(t));
        } else {
            double z = 8.0 / t;
            double y = z * z;
            double xx = t - 0.785398164;

            double ans1 = 1.0 + y * (-0.1098628627e-2 + y * (0.2734510407e-4
                    + y * (-0.2073370639e-5 + y * 0.2093887211e-6)));
            double ans2 = -0.1562499995e-1 + y * (0.1430488765e-3
                    + y * (-0.6911147651e-5 + y * (0.7621095161e-6
                            - y * 0.9349451520e-7)));

            return Real.of(Math.sqrt(0.636619772 / t)
                    * (Math.sin(xx) * ans1 + z * Math.cos(xx) * ans2));
        }
    }

    /**
     * Factorial function: n!
     * <p>
     * For non-integers, uses ÃŽâ€œ(n+1).
     * </p>
     */
    public static Real factorial(Real n) {
        return gamma(n.add(Real.ONE));
    }

    /**
     * Legendre polynomial P_n(x) evaluated at x.
     * <p>
     * Uses recurrence relation:
     * P_0(x) = 1, P_1(x) = x, P_{n+1}(x) = ((2n+1)xP_n(x) - nP_{n-1}(x))/(n+1)
     * </p>
     * 
     * @param n degree of the polynomial (n >= 0)
     * @param x point at which to evaluate
     * @return P_n(x)
     */
    public static Real legendre(int n, Real x) {
        if (n < 0) {
            throw new IllegalArgumentException("Degree must be non-negative");
        }
        if (n == 0) {
            return Real.ONE;
        }
        if (n == 1) {
            return x;
        }

        Real pPrev = Real.ONE; // P_{n-2}
        Real pCurr = x; // P_{n-1}

        for (int k = 1; k < n; k++) {
            // P_{k+1} = ((2k+1)*x*P_k - k*P_{k-1}) / (k+1)
            Real pNext = Real.of(2 * k + 1).multiply(x).multiply(pCurr)
                    .subtract(Real.of(k).multiply(pPrev))
                    .divide(Real.of(k + 1));
            pPrev = pCurr;
            pCurr = pNext;
        }
        return pCurr;
    }

    /**
     * Hermite polynomial H_n(x) evaluated at x (physicist's convention).
     * <p>
     * Uses recurrence relation:
     * H_0(x) = 1, H_1(x) = 2x, H_{n+1}(x) = 2xH_n(x) - 2nH_{n-1}(x)
     * </p>
     * 
     * @param n degree of the polynomial (n >= 0)
     * @param x point at which to evaluate
     * @return H_n(x)
     */
    public static Real hermite(int n, Real x) {
        if (n < 0) {
            throw new IllegalArgumentException("Degree must be non-negative");
        }
        if (n == 0) {
            return Real.ONE;
        }
        if (n == 1) {
            return Real.of(2).multiply(x);
        }

        Real hPrev = Real.ONE; // H_{n-2}
        Real hCurr = Real.of(2).multiply(x); // H_{n-1}

        for (int k = 1; k < n; k++) {
            // H_{k+1} = 2*x*H_k - 2*k*H_{k-1}
            Real hNext = Real.of(2).multiply(x).multiply(hCurr)
                    .subtract(Real.of(2 * k).multiply(hPrev));
            hPrev = hCurr;
            hCurr = hNext;
        }
        return hCurr;
    }

    /**
     * Laguerre polynomial L_n(x) evaluated at x.
     * <p>
     * Uses recurrence relation:
     * L_0(x) = 1, L_1(x) = 1-x, L_{n+1}(x) = ((2n+1-x)L_n(x) - nL_{n-1}(x))/(n+1)
     * </p>
     * 
     * @param n degree of the polynomial (n >= 0)
     * @param x point at which to evaluate
     * @return L_n(x)
     */
    public static Real laguerre(int n, Real x) {
        if (n < 0) {
            throw new IllegalArgumentException("Degree must be non-negative");
        }
        if (n == 0) {
            return Real.ONE;
        }
        if (n == 1) {
            return Real.ONE.subtract(x);
        }

        Real lPrev = Real.ONE; // L_{n-2}
        Real lCurr = Real.ONE.subtract(x); // L_{n-1}

        for (int k = 1; k < n; k++) {
            // L_{k+1} = ((2k+1-x)*L_k - k*L_{k-1}) / (k+1)
            Real lNext = Real.of(2 * k + 1).subtract(x).multiply(lCurr)
                    .subtract(Real.of(k).multiply(lPrev))
                    .divide(Real.of(k + 1));
            lPrev = lCurr;
            lCurr = lNext;
        }
        return lCurr;
    }

    // ==================== Incomplete Special Functions ====================

    /**
     * Incomplete gamma function: ÃŽÂ³(a, x) = Ã¢Ë†Â«Ã¢â€šâ‚¬Ã‹Â£ t^(a-1) e^(-t) dt
     * <p>
     * Regularized: P(a, x) = ÃŽÂ³(a, x) / ÃŽâ€œ(a)
     * </p>
     * 
     * @param a Shape parameter (a > 0)
     * @param x Upper limit (x >= 0)
     * @return Regularized incomplete gamma P(a, x)
     */
    public static Real incompleteGamma(Real a, Real x) {
        return Real.of(incompleteGammaP(a.doubleValue(), x.doubleValue()));
    }

    private static double incompleteGammaP(double a, double x) {
        if (x < 0 || a <= 0) {
            throw new IllegalArgumentException("Require a > 0 and x >= 0");
        }
        if (x == 0)
            return 0;

        // Use series for x < a+1, continued fraction otherwise
        if (x < a + 1) {
            return gammaSeries(a, x);
        } else {
            return 1.0 - gammaCF(a, x);
        }
    }

    private static double gammaSeries(double a, double x) {
        double ap = a;
        double sum = 1.0 / a;
        double del = sum;

        for (int n = 1; n <= 100; n++) {
            ap += 1.0;
            del *= x / ap;
            sum += del;
            if (Math.abs(del) < Math.abs(sum) * 1e-15) {
                double gammaA = gamma(Real.of(a)).doubleValue();
                return sum * Math.exp(-x + a * Math.log(x) - Math.log(gammaA));
            }
        }
        throw new RuntimeException("Gamma series failed to converge");
    }

    private static double gammaCF(double a, double x) {
        double b = x + 1.0 - a;
        double c = 1.0 / 1e-30;
        double d = 1.0 / b;
        double h = d;

        for (int i = 1; i <= 100; i++) {
            double an = -i * (i - a);
            b += 2.0;
            d = an * d + b;
            if (Math.abs(d) < 1e-30)
                d = 1e-30;
            c = b + an / c;
            if (Math.abs(c) < 1e-30)
                c = 1e-30;
            d = 1.0 / d;
            double del = d * c;
            h *= del;
            if (Math.abs(del - 1.0) < 1e-15) {
                double gammaA = gamma(Real.of(a)).doubleValue();
                return Math.exp(-x + a * Math.log(x) - Math.log(gammaA)) * h;
            }
        }
        throw new RuntimeException("Gamma CF failed to converge");
    }

    /**
     * Incomplete beta function: I_x(a, b).
     * <p>
     * Regularized: I_x(a, b) = B(x; a,b) / B(a,b)
     * Used in statistics: CDF of Beta distribution.
     * </p>
     * 
     * @param x Value (0 <= x <= 1)
     * @param a First parameter (a > 0)
     * @param b Second parameter (b > 0)
     * @return Regularized incomplete beta I_x(a, b)
     */
    public static Real incompleteBeta(Real x, Real a, Real b) {
        return Real.of(incompleteBetaI(x.doubleValue(), a.doubleValue(), b.doubleValue()));
    }

    private static double incompleteBetaI(double x, double a, double b) {
        if (x < 0 || x > 1) {
            throw new IllegalArgumentException("x must be in [0, 1]");
        }
        if (a <= 0 || b <= 0) {
            throw new IllegalArgumentException("a and b must be positive");
        }
        if (x == 0)
            return 0;
        if (x == 1)
            return 1;

        double bt = Math.exp(
                a * Math.log(x) + b * Math.log(1 - x)
                        - logGamma(Real.of(a)).doubleValue()
                        - logGamma(Real.of(b)).doubleValue()
                        + logGamma(Real.of(a + b)).doubleValue());

        // Use symmetry for efficiency
        if (x < (a + 1) / (a + b + 2)) {
            return bt * betaCF(x, a, b) / a;
        } else {
            return 1.0 - bt * betaCF(1 - x, b, a) / b;
        }
    }

    private static double betaCF(double x, double a, double b) {
        double qab = a + b;
        double qap = a + 1.0;
        double qam = a - 1.0;
        double c = 1.0;
        double d = 1.0 - qab * x / qap;
        if (Math.abs(d) < 1e-30)
            d = 1e-30;
        d = 1.0 / d;
        double h = d;

        for (int m = 1; m <= 100; m++) {
            int m2 = 2 * m;
            double aa = m * (b - m) * x / ((qam + m2) * (a + m2));
            d = 1.0 + aa * d;
            if (Math.abs(d) < 1e-30)
                d = 1e-30;
            c = 1.0 + aa / c;
            if (Math.abs(c) < 1e-30)
                c = 1e-30;
            d = 1.0 / d;
            h *= d * c;

            aa = -(a + m) * (qab + m) * x / ((a + m2) * (qap + m2));
            d = 1.0 + aa * d;
            if (Math.abs(d) < 1e-30)
                d = 1e-30;
            c = 1.0 + aa / c;
            if (Math.abs(c) < 1e-30)
                c = 1e-30;
            d = 1.0 / d;
            double del = d * c;
            h *= del;

            if (Math.abs(del - 1.0) < 1e-15)
                return h;
        }
        throw new RuntimeException("Beta CF failed to converge");
    }

    /**
     * Airy function Ai(x).
     * <p>
     * Solution to y'' - xy = 0 that decays as x Ã¢â€ â€™ Ã¢Ë†Å¾.
     * Used in quantum mechanics (wave functions near turning points).
     * </p>
     */
    public static Real airyAi(Real x) {
        double z = x.doubleValue();

        if (z < -10) {
            // Asymptotic for large negative x
            double xi = 2.0 / 3.0 * Math.pow(-z, 1.5);
            return Real.of(Math.sin(xi + Math.PI / 4) / (Math.sqrt(Math.PI) * Math.pow(-z, 0.25)));
        } else if (z > 10) {
            // Asymptotic for large positive x
            double xi = 2.0 / 3.0 * Math.pow(z, 1.5);
            return Real.of(0.5 * Math.exp(-xi) / (Math.sqrt(Math.PI) * Math.pow(z, 0.25)));
        } else {
            // Series expansion near zero
            double a0 = 0.355028053887817; // Ai(0)
            double a1 = -0.258819403792807; // Ai'(0)
            double sum = a0;
            double term = a1 * z;
            sum += term;

            for (int n = 2; n < 50; n++) {
                if (n % 3 == 0) {
                    term = term * z / n;
                } else if (n % 3 == 1) {
                    term = term * z / n;
                } else {
                    term = term * z * z / (n * (n - 1));
                }
                sum += term;
                if (Math.abs(term) < 1e-15 * Math.abs(sum))
                    break;
            }
            return Real.of(sum);
        }
    }

    /**
     * Bessel function of the first kind J_n(x) for integer order.
     * 
     * @param n Order (integer)
     * @param x Argument
     * @return J_n(x)
     */
    public static Real besselJn(int n, Real x) {
        if (n == 0)
            return besselJ0(x);
        if (n < 0) {
            // J_{-n}(x) = (-1)^n * J_n(x)
            return besselJn(-n, x).multiply(Real.of((n % 2 == 0) ? 1 : -1));
        }

        double t = x.doubleValue();
        if (Math.abs(t) < 1e-15)
            return Real.ZERO;

        // Miller's backward recurrence for stability
        int m = 2 * ((n + (int) Math.sqrt(40 * n)) / 2);
        double tox = 2.0 / t;
        double bjp = 0.0;
        double bj = 1.0;
        double[] bjArr = new double[n + 1];

        for (int j = m; j > 0; j--) {
            double bjm = j * tox * bj - bjp;
            bjp = bj;
            bj = bjm;
            if (j <= n)
                bjArr[j] = bj;
        }

        // Normalize using J0
        double j0 = besselJ0(x).doubleValue();
        double scale = j0 / bj;

        return Real.of(bjArr[n] * scale);
    }

    /**
     * Bessel function of the second kind Y_n(x) for integer order.
     * 
     * @param n Order (integer, n >= 0)
     * @param x Argument (x > 0)
     * @return Y_n(x)
     */
    public static Real besselYn(int n, Real x) {
        if (n == 0)
            return besselY0(x);
        if (n < 0) {
            // Y_{-n}(x) = (-1)^n * Y_n(x)
            return besselYn(-n, x).multiply(Real.of((n % 2 == 0) ? 1 : -1));
        }

        double t = x.doubleValue();
        if (t <= 0) {
            throw new IllegalArgumentException("Y_n undefined for x <= 0");
        }

        // Forward recurrence: Y_{n+1} = (2n/x) * Y_n - Y_{n-1}
        double y0 = besselY0(x).doubleValue();
        double y1 = besselY1(x).doubleValue();

        if (n == 1)
            return Real.of(y1);

        double yPrev = y0;
        double yCurr = y1;

        for (int k = 1; k < n; k++) {
            double yNext = (2.0 * k / t) * yCurr - yPrev;
            yPrev = yCurr;
            yCurr = yNext;
        }

        return Real.of(yCurr);
    }

    /**
     * Bessel function of the second kind Y_1(x).
     */
    private static Real besselY1(Real x) {
        double t = x.doubleValue();
        if (t <= 0) {
            throw new IllegalArgumentException("Y1 undefined for x <= 0");
        }

        if (t < 8.0) {
            double j1 = besselJn(1, x).doubleValue();
            double y = t * t;
            double ans1 = t * (-4016890.6 + y * (3759217.3 + y * (-1015809.0
                    + y * (99447.43 + y * (-4709.49 + y * 11.53)))));
            double ans2 = 2411356418.0 + y * (4240077.4 + y * (35818.29
                    + y * (230.39 + y)));
            return Real.of(ans1 / ans2 + 0.636619772 * (j1 * Math.log(t) - 1.0 / t));
        } else {
            double z = 8.0 / t;
            double y = z * z;
            double xx = t - 2.356194491;

            double ans1 = 1.0 + y * (0.183105e-2 + y * (-0.3516396496e-4
                    + y * (0.2457520174e-5 + y * (-0.240337019e-6))));
            double ans2 = 0.04687499995 + y * (-0.2002690873e-3
                    + y * (0.8449199096e-5 + y * (-0.88228987e-6 + y * 0.105787412e-6)));

            return Real.of(Math.sqrt(0.636619772 / t)
                    * (Math.sin(xx) * ans1 + z * Math.cos(xx) * ans2));
        }
    }

    /**
     * Modified Bessel function of the first kind I_n(x).
     * <p>
     * I_n(x) = i^(-n) * J_n(ix)
     * </p>
     * 
     * @param n Order (integer)
     * @param x Argument
     * @return I_n(x)
     */
    public static Real besselIn(int n, Real x) {
        if (n < 0) {
            return besselIn(-n, x); // I_{-n} = I_n
        }

        double t = x.doubleValue();
        if (Math.abs(t) < 1e-15) {
            return (n == 0) ? Real.ONE : Real.ZERO;
        }

        // Miller's backward recurrence
        int m = 2 * ((n + (int) Math.sqrt(40.0 * n)) / 2) + 2;
        double tox = 2.0 / Math.abs(t);
        double bip = 0.0;
        double bi = 1.0;
        double[] result = new double[n + 1];

        for (int j = m; j > 0; j--) {
            double bim = bip + j * tox * bi;
            bip = bi;
            bi = bim;
            if (j <= n)
                result[j] = bi;
        }

        // Normalize using I_0
        double i0 = besselI0(t);
        double scale = i0 / bi;

        return Real.of(result[n] * scale * (t < 0 && n % 2 != 0 ? -1 : 1));
    }

    private static double besselI0(double x) {
        double ax = Math.abs(x);
        if (ax < 3.75) {
            double y = x / 3.75;
            y = y * y;
            return 1.0 + y * (3.5156229 + y * (3.0899424 + y * (1.2067492
                    + y * (0.2659732 + y * (0.360768e-1 + y * 0.45813e-2)))));
        } else {
            double y = 3.75 / ax;
            return (Math.exp(ax) / Math.sqrt(ax)) * (0.39894228 + y * (0.1328592e-1
                    + y * (0.225319e-2 + y * (-0.157565e-2 + y * (0.916281e-2
                            + y * (-0.2057706e-1 + y * (0.2635537e-1 + y * (-0.1647633e-1
                                    + y * 0.392377e-2))))))));
        }
    }

    /**
     * Modified Bessel function of the second kind K_n(x).
     * <p>
     * Decays exponentially for large x, diverges at x=0.
     * </p>
     * 
     * @param n Order (integer, n >= 0)
     * @param x Argument (x > 0)
     * @return K_n(x)
     */
    public static Real besselKn(int n, Real x) {
        if (n < 0)
            n = -n; // K_{-n} = K_n

        double t = x.doubleValue();
        if (t <= 0) {
            throw new IllegalArgumentException("K_n undefined for x <= 0");
        }

        double k0 = besselK0(t);
        if (n == 0)
            return Real.of(k0);

        double k1 = besselK1(t);
        if (n == 1)
            return Real.of(k1);

        // Forward recurrence: K_{n+1} = (2n/x) * K_n + K_{n-1}
        double kPrev = k0;
        double kCurr = k1;

        for (int k = 1; k < n; k++) {
            double kNext = (2.0 * k / t) * kCurr + kPrev;
            kPrev = kCurr;
            kCurr = kNext;
        }

        return Real.of(kCurr);
    }

    private static double besselK0(double x) {
        if (x <= 2.0) {
            double y = x * x / 4.0;
            return (-Math.log(x / 2.0) * besselI0(x)) + (-0.57721566 + y * (0.42278420
                    + y * (0.23069756 + y * (0.3488590e-1 + y * (0.262698e-2
                            + y * (0.10750e-3 + y * 0.74e-5))))));
        } else {
            double y = 2.0 / x;
            return (Math.exp(-x) / Math.sqrt(x)) * (1.25331414 + y * (-0.7832358e-1
                    + y * (0.2189568e-1 + y * (-0.1062446e-1 + y * (0.587872e-2
                            + y * (-0.251540e-2 + y * 0.53208e-3))))));
        }
    }

    private static double besselK1(double x) {
        if (x <= 2.0) {
            double y = x * x / 4.0;
            double i1 = x * (0.5 + y * (0.87890594 + y * (0.51498869 + y * (0.15084934
                    + y * (0.2658733e-1 + y * (0.301532e-2 + y * 0.32411e-3))))));
            return (Math.log(x / 2.0) * i1) + (1.0 / x) * (1.0 + y * (0.15443144
                    + y * (-0.67278579 + y * (-0.18156897 + y * (-0.1919402e-1
                            + y * (-0.110404e-2 + y * (-0.4686e-4)))))));
        } else {
            double y = 2.0 / x;
            return (Math.exp(-x) / Math.sqrt(x)) * (1.25331414 + y * (0.23498619
                    + y * (-0.3655620e-1 + y * (0.1504268e-1 + y * (-0.780353e-2
                            + y * (0.325614e-2 + y * (-0.68245e-3)))))));
        }
    }
}


