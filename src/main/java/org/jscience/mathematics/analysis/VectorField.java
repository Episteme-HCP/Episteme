package org.jscience.mathematics.analysis;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Vector;

import org.jscience.mathematics.function.Function;

/**
 * Represents a vector field: F: ℝⁿ → ℝⁿ
 * <p>
 * A vector field assigns a vector to each point in space.
 * </p>
 * <p>
 * Examples in physics:
 * - Velocity field: v(x,y,z) in fluid dynamics
 * - Electric field: E(x,y,z)
 * - Magnetic field: B(x,y,z)
 * - Gravitational field: g(x,y,z)
 * - Force field: F(x,y,z)
 * </p>
 * 
 * @param <V> the type representing points in the domain
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@FunctionalInterface
public interface VectorField<V> extends Function<V, Vector<Real>> {

    /**
     * Evaluates the vector field at a given point.
     * 
     * @param point the point at which to evaluate
     * @return the vector value at that point
     */
    Vector<Real> evaluate(V point);

    /**
     * Returns the dimension of the domain and codomain.
     * <p>
     * Default implementation returns -1 (unknown).
     * </p>
     * 
     * @return the dimension, or -1 if unknown
     */
    default int dimension() {
        return -1;
    }

    /**
     * Creates a vector field from a lambda expression.
     * 
     * @param <V>       the point type
     * @param evaluator the evaluation function
     * @return the vector field
     */
    static <V> VectorField<V> of(java.util.function.Function<V, Vector<Real>> evaluator) {
        return evaluator::apply;
    }

    /**
     * Creates a vector field with known dimension.
     * 
     * @param <V>       the point type
     * @param evaluator the evaluation function
     * @param dimension the dimension
     * @return the vector field
     */
    static <V> VectorField<V> of(java.util.function.Function<V, Vector<Real>> evaluator, int dimension) {
        return new VectorField<V>() {
            @Override
            public Vector<Real> evaluate(V point) {
                return evaluator.apply(point);
            }

            @Override
            public int dimension() {
                return dimension;
            }
        };
    }
}
