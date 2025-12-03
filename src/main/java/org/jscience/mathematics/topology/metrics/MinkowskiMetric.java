package org.jscience.mathematics.topology.metrics;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Vector;

/**
 * Minkowski metric (Lp norm).
 * <p>
 * d(x,y) = (Σ|xᵢ - yᵢ|ᵖ)^(1/p)
 * </p>
 * <p>
 * Special cases:
 * - p = 1: Manhattan metric
 * - p = 2: Euclidean metric
 * - p = ∞: Chebyshev metric
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class MinkowskiMetric implements Metric<Vector<Real>> {

    private final Real p;

    /**
     * Creates a Minkowski metric with the given p value.
     * 
     * @param p the power (must be >= 1)
     */
    public MinkowskiMetric(Real p) {
        if (p.compareTo(Real.ONE) < 0) {
            throw new IllegalArgumentException("p must be >= 1");
        }
        this.p = p;
    }

    /**
     * Creates a Minkowski metric with the given p value.
     * 
     * @param p the power (must be >= 1)
     */
    public MinkowskiMetric(double p) {
        this(Real.of(p));
    }

    @Override
    public Real distance(Vector<Real> a, Vector<Real> b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }

        Real sum = Real.ZERO;
        for (int i = 0; i < a.dimension(); i++) {
            Real diff = a.get(i).subtract(b.get(i)).abs();
            sum = sum.add(diff.pow(p));
        }

        return sum.pow(Real.ONE.divide(p));
    }

    /**
     * Returns the p value.
     * 
     * @return the power
     */
    public Real getP() {
        return p;
    }
}
