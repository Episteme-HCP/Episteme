package org.jscience.mathematics.topology.metrics;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Vector;

/**
 * Chebyshev metric (L∞ norm, maximum metric).
 * <p>
 * d(x,y) = max|xᵢ - yᵢ|
 * </p>
 * <p>
 * Also known as:
 * - L∞ metric
 * - Maximum metric
 * - Supremum metric
 * - Chessboard distance (in 2D)
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class ChebyshevMetric implements Metric<Vector<Real>> {

    @Override
    public Real distance(Vector<Real> a, Vector<Real> b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }

        Real maxDiff = Real.ZERO;
        for (int i = 0; i < a.dimension(); i++) {
            Real diff = a.get(i).subtract(b.get(i)).abs();
            if (diff.compareTo(maxDiff) > 0) {
                maxDiff = diff;
            }
        }

        return maxDiff;
    }
}
