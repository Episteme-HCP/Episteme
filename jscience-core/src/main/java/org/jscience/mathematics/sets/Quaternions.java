/*
 * JScience Reimagined - Unified Scientific Computing Framework
 * Copyright (c) 2025 Silvere Martin-Michiellot
 */

package org.jscience.mathematics.sets;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.structures.sets.InfiniteSet;
import org.jscience.mathematics.numbers.complex.Quaternion;

/**
 * The structure of Quaternions (H).
 * <p>
 * This class represents the <strong>structure</strong> of quaternions.
 * It implements {@link Field} because H forms a skew field (division ring)
 * under addition and multiplication.
 * </p>
 * <p>
 * Note: Quaternion multiplication is <strong>non-commutative</strong>.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class Quaternions implements Field<Quaternion>, InfiniteSet<Quaternion> {

    /** Singleton instance */
    public static final Quaternions INSTANCE = new Quaternions();

    /**
     * Returns the singleton instance.
     * 
     * @return the Quaternions structure
     */
    public static Quaternions getInstance() {
        return INSTANCE;
    }

    /** Private constructor for singleton */
    private Quaternions() {
    }

    // --- Field Implementation ---

    @Override
    public Quaternion operate(Quaternion a, Quaternion b) {
        return add(a, b);
    }

    @Override
    public Quaternion add(Quaternion a, Quaternion b) {
        return a.add(b);
    }

    @Override
    public int characteristic() {
        return 0;
    }

    @Override
    public Quaternion zero() {
        return Quaternion.ZERO;
    }

    @Override
    public Quaternion negate(Quaternion element) {
        return element.negate();
    }

    @Override
    public Quaternion multiply(Quaternion a, Quaternion b) {
        return a.multiply(b);
    }

    @Override
    public Quaternion one() {
        return Quaternion.ONE;
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return false;
    }

    @Override
    public Quaternion inverse(Quaternion element) {
        return element.inverse();
    }

    // --- InfiniteSet Implementation ---

    @Override
    public boolean isCountable() {
        return false; // H is uncountable (R^4)
    }

    @Override
    public boolean contains(Quaternion element) {
        return element != null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String description() {
        return "Quaternions (H)";
    }

    @Override
    public String toString() {
        return "Quaternions(H)";
    }
}
