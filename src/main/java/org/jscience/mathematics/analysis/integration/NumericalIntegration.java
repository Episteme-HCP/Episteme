package org.jscience.mathematics.analysis.integration;

import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.number.Real;

/**
 * Numerical integration methods for computing definite integrals.
 * <p>
 * Implements Trapezoidal Rule and Simpson's Rule for approximating ∫f(x)dx.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class NumericalIntegration {

    /**
     * Trapezoidal rule: ∫[a,b] f(x)dx ≈ h/2 * (f(a) + 2Σf(xi) + f(b))
     * <p>
     * Error: O(h²) where h = (b-a)/n
     * </p>
     * 
     * @param f the function to integrate
     * @param a lower bound
     * @param b upper bound
     * @param n number of subintervals (must be > 0)
     * @return approximate integral value
     */
    public static Real trapezoidal(Function<Real, Real> f, Real a, Real b, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        if (a.compareTo(b) >= 0) {
            throw new IllegalArgumentException("a must be < b");
        }

        Real h = b.subtract(a).divide(Real.of(n));
        Real sum = f.evaluate(a).add(f.evaluate(b)).divide(Real.of(2));

        for (int i = 1; i < n; i++) {
            Real xi = a.add(h.multiply(Real.of(i)));
            sum = sum.add(f.evaluate(xi));
        }

        return h.multiply(sum);
    }

    /**
     * Simpson's rule: ∫[a,b] f(x)dx ≈ h/3 * (f(a) + 4Σf(x_odd) + 2Σf(x_even) +
     * f(b))
     * <p>
     * Error: O(h⁴) - more accurate than trapezoidal
     * Requires n to be even.
     * </p>
     * 
     * @param f the function to integrate
     * @param a lower bound
     * @param b upper bound
     * @param n number of subintervals (must be even and > 0)
     * @return approximate integral value
     */
    public static Real simpsons(Function<Real, Real> f, Real a, Real b, int n) {
        if (n <= 0 || n % 2 != 0) {
            throw new IllegalArgumentException("n must be positive and even");
        }
        if (a.compareTo(b) >= 0) {
            throw new IllegalArgumentException("a must be < b");
        }

        Real h = b.subtract(a).divide(Real.of(n));
        Real sum = f.evaluate(a).add(f.evaluate(b));

        // Odd indices: coefficient 4
        for (int i = 1; i < n; i += 2) {
            Real xi = a.add(h.multiply(Real.of(i)));
            sum = sum.add(f.evaluate(xi).multiply(Real.of(4)));
        }

        // Even indices: coefficient 2
        for (int i = 2; i < n; i += 2) {
            Real xi = a.add(h.multiply(Real.of(i)));
            sum = sum.add(f.evaluate(xi).multiply(Real.of(2)));
        }

        return h.divide(Real.of(3)).multiply(sum);
    }

    /**
     * Adaptive Simpson's rule with error tolerance.
     * <p>
     * Recursively subdivides intervals until error estimate < tolerance.
     * </p>
     * 
     * @param f         the function to integrate
     * @param a         lower bound
     * @param b         upper bound
     * @param tolerance error tolerance
     * @return approximate integral value
     */
    public static Real adaptiveSimpsons(Function<Real, Real> f, Real a, Real b, Real tolerance) {
        Real c = a.add(b).divide(Real.of(2));
        Real h = b.subtract(a);
        Real fa = f.evaluate(a);
        Real fb = f.evaluate(b);
        Real fc = f.evaluate(c);

        Real S = h.divide(Real.of(6)).multiply(fa.add(fc.multiply(Real.of(4))).add(fb));

        return adaptiveSimpsonsRecursive(f, a, b, tolerance, S, fa, fb, fc, 0);
    }

    private static Real adaptiveSimpsonsRecursive(Function<Real, Real> f, Real a, Real b,
            Real tolerance, Real S, Real fa, Real fb, Real fc, int depth) {
        if (depth > 20) {
            return S; // Max recursion depth
        }

        Real c = a.add(b).divide(Real.of(2));
        Real h = b.subtract(a);
        Real d = a.add(c).divide(Real.of(2));
        Real e = c.add(b).divide(Real.of(2));
        Real fd = f.evaluate(d);
        Real fe = f.evaluate(e);

        Real Sleft = h.divide(Real.of(12)).multiply(fa.add(fd.multiply(Real.of(4))).add(fc));
        Real Sright = h.divide(Real.of(12)).multiply(fc.add(fe.multiply(Real.of(4))).add(fb));
        Real S2 = Sleft.add(Sright);

        Real error = S2.subtract(S).abs().divide(Real.of(15));

        if (error.compareTo(tolerance) < 0) {
            return S2.add(error);
        } else {
            Real halfTolerance = tolerance.divide(Real.of(2));
            Real left = adaptiveSimpsonsRecursive(f, a, c, halfTolerance, Sleft, fa, fc, fd, depth + 1);
            Real right = adaptiveSimpsonsRecursive(f, c, b, halfTolerance, Sright, fc, fb, fe, depth + 1);
            return left.add(right);
        }
    }

    /**
     * Midpoint rule: ∫[a,b] f(x)dx ≈ h * Σf(midpoints)
     * <p>
     * Simple but effective for smooth functions.
     * </p>
     */
    public static Real midpoint(Function<Real, Real> f, Real a, Real b, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }

        Real h = b.subtract(a).divide(Real.of(n));
        Real sum = Real.ZERO;

        for (int i = 0; i < n; i++) {
            Real midpoint = a.add(h.multiply(Real.of(i + 0.5)));
            sum = sum.add(f.evaluate(midpoint));
        }

        return h.multiply(sum);
    }
}
