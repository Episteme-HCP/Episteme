package org.jscience.mathematics.dynamical;

/**
 * Represents a dynamical system map f: T -> T.
 *
 * @param <T> the type of the state space (e.g., Real, Complex, double[])
 */
public interface DynamicalMap<T> {
    /**
     * Applies the map to a state.
     *
     * @param state the current state
     * @return the next state
     */
    T map(T state);

    /**
     * Iterates the map n times.
     *
     * @param n       number of iterations
     * @param initial initial state
     * @return the state after n iterations
     */
    default T iterate(int n, T initial) {
        T current = initial;
        for (int i = 0; i < n; i++) {
            current = map(current);
        }
        return current;
    }
}
