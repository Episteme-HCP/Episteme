package org.episteme.core.mathematics.logic.fuzzy;

/**
 * <p/>
 * Abstraction for fuzzy membership functions.
 * </p>
 *
 * @author Levent Bayindir
 * @version 0.0.1
 */
public interface MembershipFunction<T> extends java.util.function.Function<T, Double> {
    /** Triangular shape type */
    public static int TYPE_TRIANGULAR = 0;

    /**
     * Returns the membership value (0.0 to 1.0) for the given input.
     *
     * @param input the element to test.
     * @return the membership degree.
     */
    public double fuzzify(T input);

    @Override
    default Double apply(T input) {
        return fuzzify(input);
    }

    /**
     * Returns the name of this membership function.
     * @return name
     */
    default String getName() { return "Unknown"; }

    /**
     * Returns the type constant.
     * @return type id
     */
    default int getType() { return -1; }

    /**
     * Returns the typical value (center) of the set.
     */
    default double getTypicalValue() { return 0.0; }

    // Legacy support methods can be deprecated or kept as default helpers
    default void setDeFuzzificationInputValue(double inputValue) {}
    default double getDeFuzzificationInputValue() { return 0.0; }
}
