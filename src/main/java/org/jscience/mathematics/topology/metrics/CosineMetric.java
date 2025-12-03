package org.jscience.mathematics.topology;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Vector;

/**
 * Cosine distance metric.
 * <p>
 * d(x,y) = 1 - cos(θ) = 1 - (x·y)/(||x|| ||y||)
 * </p>
 * <p>
 * Measures the angle between vectors, commonly used in:
 * - Text similarity (TF-IDF vectors)
 * - Recommendation systems
 * - Machine learning
 * </p>
 * <p>
 * Note: This is actually a distance (1 - similarity), not the cosine similarity
 * itself.
 * Range: [0, 2] where 0 = identical direction, 2 = opposite direction
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class CosineMetric implements Metric<Vector<Real>> {

    @Override
    public Real distance(Vector<Real> a, Vector<Real> b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }

        Real dotProduct = a.dot(b);
        Real normA = a.norm();
        Real normB = b.norm();

        if (normA.equals(Real.ZERO) || normB.equals(Real.ZERO)) {
            throw new ArithmeticException("Cannot compute cosine distance for zero vectors");
        }

        Real cosineSimilarity = dotProduct.divide(normA.multiply(normB));
        return Real.ONE.subtract(cosineSimilarity);
    }

    /**
     * Returns the cosine similarity (not distance).
     * 
     * @param a first vector
     * @param b second vector
     * @return cosine similarity in [-1, 1]
     */
    public Real similarity(Vector<Real> a, Vector<Real> b) {
        return Real.ONE.subtract(distance(a, b));
    }
}
