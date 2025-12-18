package org.jscience.mathematics.algebra.categories;

import org.jscience.mathematics.structures.categories.Category;

/**
 * Represents a Preorder as a Category.
 * <p>
 * Objects are elements of the preorder.
 * There is at most one morphism between any two objects (a -> b iff a <= b).
 * </p>
 * 
 * @param <E> the type of elements
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Preorder<E> extends Category<E, Boolean> {

    /**
     * Checks if a <= b.
     */
    boolean isLessThanOrEqualTo(E a, E b);

    default Boolean compose(Boolean f, Boolean g) {
        // Transitivity: if a<=b (g) and b<=c (f), then a<=c
        return f && g;
    }

    default Boolean identity(E object) {
        // Reflexivity: a<=a
        return true;
    }
}
