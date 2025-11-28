package org.jscience.mathematics.analysis.rootfinding;

import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.number.Real;

/**
 * Root finding algorithms for solving f(x) = 0.
 * <p>
 * Implements Newton-Raphson, Bisection, and Secant methods.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class RootFinding {

    /**
     * Newton-Raphson method: xₙ₊₁ = xₙ - f(xₙ)/f'(xₙ)
     * <p>
     * Quadratic convergence near root. Requires derivative.
     * </p>
     * 
     * @param f             the function
     * @param df            the derivative of f
     * @param initialGuess  starting point
     * @param tolerance     convergence tolerance
     * @param maxIterations maximum iterations
     * @return approximate root, or null if not found
     */
    public static Real newtonRaphson(Function<Real, Real> f, Function<Real, Real> df,
            Real initialGuess, Real tolerance, int maxIterations) {
        Real x = initialGuess;

        for (int i = 0; i < maxIterations; i++) {
            Real fx = f.evaluate(x);

            if (fx.abs().compareTo(tolerance) < 0) {
                return x;
            }

            Real dfx = df.evaluate(x);
            if (dfx.abs().compareTo(Real.of(1e-15)) < 0) {
                return null; // Derivative too small
            }

            x = x.subtract(fx.divide(dfx));
        }

        return null; // Did not converge
    }

    /**
     * Bisection method (bracketing method).
     * <p>
     * Guaranteed convergence if f(a) and f(b) have opposite signs.
     * Linear convergence but robust.
     * </p>
     * 
     * @param f             the function
     * @param a             left bracket
     * @param b             right bracket
     * @param tolerance     convergence tolerance
     * @param maxIterations maximum iterations
     * @return approximate root, or null if not found
     */
    public static Real bisection(Function<Real, Real> f, Real a, Real b,
            Real tolerance, int maxIterations) {
        Real fa = f.evaluate(a);
        Real fb = f.evaluate(b);

        // Check if root is bracketed
        if (fa.multiply(fb).compareTo(Real.ZERO) > 0) {
            return null; // Same sign, not bracketed
        }

        for (int i = 0; i < maxIterations; i++) {
            Real c = a.add(b).divide(Real.of(2));
            Real fc = f.evaluate(c);

            if (fc.abs().compareTo(tolerance) < 0 || b.subtract(a).abs().compareTo(tolerance) < 0) {
                return c;
            }

            if (fa.multiply(fc).compareTo(Real.ZERO) < 0) {
                b = c;
                fb = fc;
            } else {
                a = c;
                fa = fc;
            }
        }

        return a.add(b).divide(Real.of(2));
    }

    /**
     * Secant method: xₙ₊₁ = xₙ - f(xₙ) * (xₙ - xₙ₋₁) / (f(xₙ) - f(xₙ₋₁))
     * <p>
     * Superlinear convergence. Does not require derivative.
     * </p>
     * 
     * @param f             the function
     * @param x0            first initial guess
     * @param x1            second initial guess
     * @param tolerance     convergence tolerance
     * @param maxIterations maximum iterations
     * @return approximate root, or null if not found
     */
    public static Real secant(Function<Real, Real> f, Real x0, Real x1,
            Real tolerance, int maxIterations) {
        Real fx0 = f.evaluate(x0);
        Real fx1 = f.evaluate(x1);

        for (int i = 0; i < maxIterations; i++) {
            if (fx1.abs().compareTo(tolerance) < 0) {
                return x1;
            }

            Real denominator = fx1.subtract(fx0);
            if (denominator.abs().compareTo(Real.of(1e-15)) < 0) {
                return null; // Division by zero
            }

            Real x2 = x1.subtract(fx1.multiply(x1.subtract(x0)).divide(denominator));

            x0 = x1;
            fx0 = fx1;
            x1 = x2;
            fx1 = f.evaluate(x1);
        }

        return null; // Did not converge
    }

    /**
     * Brent's method - combination of bisection, secant, and inverse quadratic
     * interpolation.
     * <p>
     * Best general-purpose root finder. Guaranteed convergence with superlinear
     * speed.
     * </p>
     * 
     * @param f         the function
     * @param a         left bracket
     * @param b         right bracket
     * @param tolerance convergence tolerance
     * @return approximate root
     */
    public static Real brent(Function<Real, Real> f, Real a, Real b, Real tolerance) {
        Real fa = f.evaluate(a);
        Real fb = f.evaluate(b);

        if (fa.multiply(fb).compareTo(Real.ZERO) > 0) {
            throw new IllegalArgumentException("Function must have opposite signs at a and b");
        }

        if (fa.abs().compareTo(fb.abs()) < 0) {
            Real temp = a;
            a = b;
            b = temp;
            temp = fa;
            fa = fb;
            fb = temp;
        }

        Real c = a;
        Real fc = fa;
        boolean mflag = true;
        Real s = b;
        Real d = c;

        int maxIter = 100;
        for (int iter = 0; iter < maxIter; iter++) {
            if (fb.abs().compareTo(tolerance) < 0) {
                return b;
            }

            if (!fa.equals(fc) && !fb.equals(fc)) {
                // Inverse quadratic interpolation
                Real term1 = a.multiply(fb).multiply(fc).divide(fa.subtract(fb).multiply(fa.subtract(fc)));
                Real term2 = b.multiply(fa).multiply(fc).divide(fb.subtract(fa).multiply(fb.subtract(fc)));
                Real term3 = c.multiply(fa).multiply(fb).divide(fc.subtract(fa).multiply(fc.subtract(fb)));
                s = term1.add(term2).add(term3);
            } else {
                // Secant method
                s = b.subtract(fb.multiply(b.subtract(a)).divide(fb.subtract(fa)));
            }

            // Check conditions for bisection
            Real cond1 = Real.of(3).multiply(a).add(b).divide(Real.of(4));
            boolean condition = s.compareTo(cond1) < 0 || s.compareTo(b) > 0;

            if (condition || (mflag && s.subtract(b).abs().compareTo(b.subtract(c).abs().divide(Real.of(2))) >= 0)) {
                s = a.add(b).divide(Real.of(2));
                mflag = true;
            } else {
                mflag = false;
            }

            Real fs = f.evaluate(s);
            d = c;
            c = b;
            fc = fb;

            if (fa.multiply(fs).compareTo(Real.ZERO) < 0) {
                b = s;
                fb = fs;
            } else {
                a = s;
                fa = fs;
            }

            if (fa.abs().compareTo(fb.abs()) < 0) {
                Real temp = a;
                a = b;
                b = temp;
                temp = fa;
                fa = fb;
                fb = temp;
            }
        }

        return b;
    }
}
