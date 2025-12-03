package org.jscience.mathematics.analysis.integration;

import org.jscience.mathematics.analysis.RealFunction;
import org.jscience.mathematics.number.Real;

/**
 * Adaptive Simpson's rule integration.
 * <p>
 * Uses Simpson's 1/3 rule with adaptive subdivision for accurate results.
 * This is generally more accurate than trapezoidal rule for smooth functions.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class AdaptiveSimpsonIntegrator implements Integrator {

    private static final double DEFAULT_EPSILON = 1e-9;
    private static final int DEFAULT_MAX_DEPTH = 20;

    @Override
    public Real integrate(RealFunction f, Real a, Real b) {
        return integrate(f, a, b, Real.of(DEFAULT_EPSILON));
    }

    @Override
    public Real integrate(RealFunction f, Real a, Real b, Real tolerance) {
        double start = a.doubleValue();
        double end = b.doubleValue();
        double eps = tolerance.doubleValue();

        return Real.of(adaptiveSimpson(f, start, end, eps, DEFAULT_MAX_DEPTH));
    }

    private double adaptiveSimpson(RealFunction f, double a, double b, double eps, int maxDepth) {
        double c = (a + b) / 2.0;
        double h = b - a;
        double fa = f.evaluate(Real.of(a)).doubleValue();
        double fb = f.evaluate(Real.of(b)).doubleValue();
        double fc = f.evaluate(Real.of(c)).doubleValue();

        double S = (h / 6.0) * (fa + 4 * fc + fb);

        return adaptiveSimpsonRec(f, a, b, eps, S, fa, fb, fc, maxDepth);
    }

    private double adaptiveSimpsonRec(RealFunction f, double a, double b, double eps,
            double S, double fa, double fb, double fc, int depth) {
        double c = (a + b) / 2.0;
        double h = b - a;
        double d = (a + c) / 2.0;
        double e = (c + b) / 2.0;

        double fd = f.evaluate(Real.of(d)).doubleValue();
        double fe = f.evaluate(Real.of(e)).doubleValue();

        double Sleft = (h / 12.0) * (fa + 4 * fd + fc);
        double Sright = (h / 12.0) * (fc + 4 * fe + fb);
        double S2 = Sleft + Sright;

        if (depth <= 0 || Math.abs(S2 - S) <= 15 * eps) {
            return S2 + (S2 - S) / 15.0; // Richardson extrapolation
        }

        return adaptiveSimpsonRec(f, a, c, eps / 2.0, Sleft, fa, fc, fd, depth - 1) +
                adaptiveSimpsonRec(f, c, b, eps / 2.0, Sright, fc, fb, fe, depth - 1);
    }
}
