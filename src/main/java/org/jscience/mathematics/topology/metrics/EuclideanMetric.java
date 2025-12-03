package org.jscience.mathematics.topology;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Vector;

/**
 * Euclidean metric (L2 norm).
 * <p>
 * d(x,y) = √(Σ(xi - yi)²)
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class EuclideanMetric implements Metric<Vector<Real>> {

    @Override
    public Real distance(Vector<Real> a, Vector<Real> b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }

        Real sumSquares = Real.ZERO;
        for (int i = 0; i < a.dimension(); i++) {
            Real diff = a.get(i).subtract(b.get(i));
            sumSquares = sumSquares.add(diff.multiply(diff));
        }

        return sumSquares.sqrt();
    }
}
