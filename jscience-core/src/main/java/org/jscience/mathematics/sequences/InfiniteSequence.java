package org.jscience.mathematics.sequences;

/**
 * Represents an infinite mathematical sequence.
 * Infinite sequences may or may not converge to a limit.
 *
 * @param <T> the type of elements in the sequence
 */
public interface InfiniteSequence<T> extends Sequence<T> {
    /**
     * Checks if the sequence converges to a limit.
     *
     * @return true if the sequence converges, false otherwise
     */
    boolean isConvergent();

    /**
     * Returns the limit of the sequence if it converges.
     *
     * @return the limit of the sequence
     * @throws ArithmeticException if the sequence does not converge
     */
    T limit();
}
