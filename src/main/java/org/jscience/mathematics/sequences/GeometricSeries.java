package org.jscience.mathematics.sequences;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a Geometric series: a + ar + ar^2 + ar^3 + ...
 * The n-th term of the sequence is the n-th partial sum.
 */
public class GeometricSeries implements InfiniteSequence<Real> {

    private final Real a; // First term
    private final Real r; // Common ratio

    public GeometricSeries(Real a, Real r) {
        this.a = a;
        this.r = r;
    }

    @Override
    public Real get(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index must be non-negative");
        }
        // Partial sum S_n = a(1 - r^(n+1)) / (1 - r)
        // index is n (0-based, so sum up to r^n)

        if (r.equals(Real.ONE)) {
            return a.multiply(Real.of(index + 1));
        }

        Real one = Real.ONE;
        Real rPow = r.pow(index + 1);
        return a.multiply(one.subtract(rPow)).divide(one.subtract(r));
    }

    @Override
    public boolean isConvergent() {
        return r.abs().compareTo(Real.ONE) < 0;
    }

    @Override
    public Real limit() {
        if (!isConvergent()) {
            throw new ArithmeticException("Geometric series diverges for |r| >= 1");
        }
        // S = a / (1 - r)
        return a.divide(Real.ONE.subtract(r));
    }
}
