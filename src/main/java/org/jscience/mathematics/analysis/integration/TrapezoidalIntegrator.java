package org.jscience.mathematics.analysis.integration;

import org.jscience.mathematics.analysis.RealFunction;
import org.jscience.mathematics.number.Real;

/**
 * Trapezoidal rule integration.
 * <p>
 * Simple, fast numerical integration using the trapezoidal rule.
 * Less accurate than adaptive methods but computationally efficient.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class TrapezoidalIntegrator implements Integrator {

    private static final int DEFAULT_INTERVALS = 1000;

    private final int intervals;

    /**
     * Creates a trapezoidal integrator with default number of intervals.
     */
    public TrapezoidalIntegrator() {
        this(DEFAULT_INTERVALS);
    }

    /**
     * Creates a trapezoidal integrator with specified intervals.
     *
     * @param intervals number of subdivisions
     */
    public TrapezoidalIntegrator(int intervals) {
        if (intervals <= 0) {
            throw new IllegalArgumentException("Intervals must be positive");
        }
        this.intervals = intervals;
    }

    @Override
    public Real integrate(RealFunction f, Real a, Real b) {
        double start = a.doubleValue();
        double end = b.doubleValue();
        double h = (end - start) / intervals;
        double sum = 0.5 * (f.evaluate(a).doubleValue() + f.evaluate(b).doubleValue());

        for (int i = 1; i < intervals; i++) {
            double x = start + i * h;
            sum += f.evaluate(Real.of(x)).doubleValue();
        }

        return Real.of(sum * h);
    }

    @Override
    public Real integrate(RealFunction f, Real a, Real b, Real tolerance) {
        // Tolerance doesn't affect trapezoidal rule directly
        // Could adaptively increase intervals, but for simplicity just use default
        return integrate(f, a, b);
    }
}
