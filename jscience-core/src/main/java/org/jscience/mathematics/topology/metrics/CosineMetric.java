/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.topology.metrics;

import org.jscience.mathematics.topology.Metric;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;

/**
 * Cosine distance metric.
 * <p>
 * d(x,y) = 1 - cos(ÃŽÂ¸) = 1 - (xÃ‚Â·y)/(||x|| ||y||)
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
 * @since 1.0
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


