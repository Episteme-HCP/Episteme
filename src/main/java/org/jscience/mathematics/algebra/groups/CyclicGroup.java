package org.jscience.mathematics.algebra.groups;

import org.jscience.mathematics.structures.groups.Group;
import org.jscience.mathematics.numbers.integers.Integer;

/**
 * Represents a Cyclic Group of order n (Z_n).
 * <p>
 * Elements are represented by {@link Integer}s in the range [0, n-1].
 * The group operation is addition modulo n.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CyclicGroup implements Group<Integer> {

    private final Integer order;

    /**
     * Creates a cyclic group of order n.
     * 
     * @param n the order of the group (must be > 0)
     */
    public CyclicGroup(long n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Order must be positive");
        }
        this.order = Integer.of(n);
    }

    /**
     * Returns the order of this group.
     * 
     * @return n
     */
    public Integer getOrder() {
        return order;
    }

    @Override
    public Integer operate(Integer a, Integer b) {
        // (a + b) % n
        return a.add(b).remainder(order);
    }

    @Override
    public Integer identity() {
        return Integer.ZERO;
    }

    @Override
    public Integer inverse(Integer element) {
        // -a % n = (n - a) % n
        if (element.equals(Integer.ZERO)) {
            return Integer.ZERO;
        }
        return order.subtract(element).remainder(order);
    }

    @Override
    public boolean isCommutative() {
        return true;
    }

    @Override
    public String description() {
        return "Cyclic Group of order " + order;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Integer element) {
        return element.compareTo(Integer.ZERO) >= 0 && element.compareTo(order) < 0;
    }

    @Override
    public String toString() {
        return "Z_" + order;
    }
}
