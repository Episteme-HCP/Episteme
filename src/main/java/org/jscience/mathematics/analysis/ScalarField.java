package org.jscience.mathematics.analysis;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a scalar field: f: ℝⁿ → ℝ
 * <p>
 * A scalar field assigns a scalar value to each point in space.
 * </p>
 * <p>
 * Examples in physics:
 * - Temperature distribution: T(x,y,z)
 * - Potential energy: U(x,y,z)
 * - Pressure: P(x,y,z)
 * - Density: ρ(x,y,z)
 * - Electric potential: φ(x,y,z)
 * </p>
 * 
 * @param <V> the type representing points in the domain
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
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
