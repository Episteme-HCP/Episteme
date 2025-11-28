package org.jscience.mathematics.analysis.special;

import org.jscience.mathematics.number.Real;

/**
 * Special mathematical functions.
 * <p>
 * Gamma, Beta, Bessel, Error functions - essential for physics, statistics,
 * engineering.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class SpecialFunctions {

    private static final Real SQRT_PI = Real.of(Math.sqrt(Math.PI));
    private static final Real SQRT_2 = Real.of(Math.sqrt(2));

    /**
     * Gamma function Γ(x) - generalization of factorial to real numbers.
     * <p>
     * Γ(n) = (n-1)! for positive integers
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
            // Reflection formula: Γ(1-z) * Γ(z) = π/sin(πz)
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
     * Natural logarithm of Gamma function: ln(Γ(x))
     * <p>
     * More numerically stable than Γ(x) for large x.
     * </p>
     */
    public static Real logGamma(Real x) {
        return gamma(x).log();
    }

    /**
     * Beta function: B(x, y) = Γ(x)Γ(y) / Γ(x+y)
     * <p>
     * Used in probability distributions (Beta, Dirichlet).
     * </p>
     */
    public static Real beta(Real x, Real y) {
        return gamma(x).multiply(gamma(y)).divide(gamma(x.add(y)));
    }

    /**
     * Error function: erf(x) = (2/√π) ∫₀ˣ e^(-t²) dt
     * <p>
     * Probability: P(X ≤ x) for normal distribution = ½[1 + erf(x/√2)]
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
     * Bessel function of the first kind J₀(x).
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
     * Bessel function of the second kind Y₀(x).
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
     * For non-integers, uses Γ(n+1).
     * </p>
     */
    public static Real factorial(Real n) {
        return gamma(n.add(Real.ONE));
    }
}
