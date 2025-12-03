package org.jscience.mathematics.topology.metrics;

import org.jscience.mathematics.topology.Metric;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Vector;

/**
 * Manhattan metric (L1 norm, taxicab metric).
 * <p>
 * d(x,y) = Σ|xi - yi|
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class ManhattanMetric implements Metric<Vector<Real>> {

    @Override
    public Real distance(Vector<Real> a, Vector<Real> b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }

        Real sum = Real.ZERO;
        for (int i = 0; i < a.dimension(); i++) {
            Real diff = a.get(i).subtract(b.get(i));
            sum = sum.add(diff.abs());
        }

        return sum;
    }
}


