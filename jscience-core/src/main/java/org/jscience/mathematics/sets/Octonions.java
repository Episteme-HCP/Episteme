/*
 * JScience Reimagined - Unified Scientific Computing Framework
 * Copyright (c) 2025 Silvere Martin-Michiellot
 */

package org.jscience.mathematics.sets;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.structures.sets.InfiniteSet;
import org.jscience.mathematics.numbers.complex.Octonion;

/**
 * The structure of Octonions (O).
 * <p>
 * This class represents the <strong>structure</strong> of octonions.
 * It implements {@link Field} as requested, though technically O is a
 * non-associative division algebra.
 * </p>
 * <p>
 * Note: Octonion multiplication is <strong>non-commutative</strong> and
 * <strong>non-associative</strong>.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class Octonions implements Field<Octonion>, InfiniteSet<Octonion> {

    /** Singleton instance */
    public static final Octonions INSTANCE = new Octonions();

    /**
     * Returns the singleton instance.
     * 
     * @return the Octonions structure
     */
    public static Octonions getInstance() {
        return INSTANCE;
    }

    /** Private constructor for singleton */
    private Octonions() {
    }

    // --- Field Implementation ---

    @Override
    public Octonion operate(Octonion a, Octonion b) {
        return add(a, b);
    }

    @Override
    public Octonion add(Octonion a, Octonion b) {
        return a.plus(b);
    }

    @Override
    public int characteristic() {
        return 0;
    }

    @Override
    public Octonion zero() {
        return Octonion.ZERO;
    }

    @Override
    public Octonion negate(Octonion element) {
        return element.negate();
    }

    @Override
    public Octonion multiply(Octonion a, Octonion b) {
        return a.times(b);
    }

    @Override
    public Octonion one() {
        return Octonion.ONE;
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return false;
    }

    @Override
    public Octonion inverse(Octonion element) {
        return element.inverse();
    }

    // --- InfiniteSet Implementation ---

    @Override
    public boolean isCountable() {
        return false; // O is uncountable (R^8)
    }

    @Override
    public boolean contains(Octonion element) {
        return element != null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String description() {
        return "Octonions (O)";
    }

    @Override
    public String toString() {
        return "Octonions(O)";
    }
}
