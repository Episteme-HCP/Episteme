package org.jscience.mathematics.sequences;

/**
 * Represents a mathematical sequence of elements.
 * A sequence is an ordered list of elements (a_0, a_1, a_2, ...).
 *
 * @param <T> the type of elements in the sequence
 */
@FunctionalInterface
public interface Sequence<T> {
    /**
     * Returns the element at the specified index (0-based).
     *
     * @param index the index of the element to retrieve
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is negative or out of bounds
     *                                   for finite sequences
     */
    T get(int index);
}
