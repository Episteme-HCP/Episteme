package org.jscience.mathematics.sets;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.structures.sets.InfiniteSet;
import org.jscience.mathematics.numbers.rationals.Rational;

/**
 * The structure of rational numbers (ℚ).
 * <p>
 * This class represents the <strong>structure</strong> of rational numbers,
 * implementing {@link Field} because ℚ forms a field under addition and
 * multiplication.
 * </p>
 * <p>
 * Rational numbers are an infinite, countable set.
 * </p>
 * 
 * @see Rational
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Rationals implements Field<Rational>, InfiniteSet<Rational> {

    /** Singleton instance */
    private static final Rationals INSTANCE = new Rationals();

    /**
     * Returns the singleton instance.
     * 
     * @return the Rationals structure
     */
    public static Rationals getInstance() {
        return INSTANCE;
    }

    /** Private constructor for singleton */
    private Rationals() {
    }

    // --- Field Implementation ---

    @Override
    public Rational operate(Rational a, Rational b) {
        return add(a, b);
    }

    @Override
    public Rational add(Rational a, Rational b) {
        return a.add(b);
    }

    @Override
    public Rational zero() {
        return Rational.ZERO;
    }

    @Override
    public Rational negate(Rational element) {
        return element.negate();
    }

    @Override
    public Rational multiply(Rational a, Rational b) {
        return a.multiply(b);
    }

    @Override
    public Rational one() {
        return Rational.ONE;
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return true;
    }

    @Override
    public Rational inverse(Rational element) {
        return element.inverse();
    }

    @Override
    public Rational divide(Rational a, Rational b) {
        return a.divide(b);
    }

    @Override
    public int characteristic() {
        return 0;
    }

    // --- InfiniteSet Implementation ---

    @Override
    public boolean isCountable() {
        return true; // ℚ is countable
    }

    @Override
    public boolean contains(Rational element) {
        return element != null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String description() {
        return "ℚ (Rational Numbers)";
    }

    @Override
    public String toString() {
        return "Rationals(ℚ)";
    }
}
