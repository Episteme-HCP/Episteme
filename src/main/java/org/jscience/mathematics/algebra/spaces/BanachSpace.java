package org.jscience.mathematics.algebra.spaces;

import org.jscience.mathematics.structures.spaces.Module;

/**
 * Represents a Banach Space.
 * <p>
 * A Banach space is a complete normed vector space.
 * It is equipped with a norm ||x||.
 * </p>
 * 
 * @param <E> the type of elements (vectors)
 * @param <S> the type of scalars (field elements)
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface BanachSpace<E, S> extends Module<E, S> {

    /**
     * Returns the norm of an element.
     * 
     * @param element the element
     * @return ||element||
     */
    S norm(E element);
}
