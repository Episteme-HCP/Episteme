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

package org.jscience.mathematics.analysis;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a scalar field: f: Ã¢â€žÂÃ¢ÂÂ¿ Ã¢â€ â€™ Ã¢â€žÂ
 * <p>
 * A scalar field assigns a scalar value to each point in space.
 * </p>
 * <p>
 * Examples in physics:
 * - Temperature distribution: T(x,y,z)
 * - Potential energy: U(x,y,z)
 * - Pressure: P(x,y,z)
 * - Density: ÃÂ(x,y,z)
 * - Electric potential: Ãâ€ (x,y,z)
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@FunctionalInterface
public interface ScalarField<V> extends Function<V, Real> {

    /**
     * Evaluates the scalar field at a given point.
     * 
     * @param point the point at which to evaluate
     * @return the scalar value at that point
     */
    Real evaluate(V point);

    /**
     * Returns the dimension of the domain.
     * <p>
     * Default implementation returns -1 (unknown).
     * Implementations should override this if dimension is known.
     * </p>
     * 
     * @return the dimension, or -1 if unknown
     */
    default int dimension() {
        return -1;
    }

    /**
     * Creates a scalar field from a lambda expression.
     * 
     * @param <V>       the point type
     * @param evaluator the evaluation function
     * @return the scalar field
     */
    static <V> ScalarField<V> of(java.util.function.Function<V, Real> evaluator) {
        return evaluator::apply;
    }

    /**
     * Creates a scalar field with known dimension.
     * 
     * @param <V>       the point type
     * @param evaluator the evaluation function
     * @param dimension the dimension
     * @return the scalar field
     */
    static <V> ScalarField<V> of(java.util.function.Function<V, Real> evaluator, int dimension) {
        return new ScalarField<V>() {
            @Override
            public Real evaluate(V point) {
                return evaluator.apply(point);
            }

            @Override
            public int dimension() {
                return dimension;
            }
        };
    }
}


