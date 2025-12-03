package org.jscience.mathematics.algebra.spaces;

/**
 * Represents a Hilbert Space.
 * <p>
 * A Hilbert space is a complete inner product space.
 * It is a Banach space where the norm is induced by the inner product.
 * </p>
 * 
 * @param <E> the type of elements (vectors)
 * @param <S> the type of scalars (field elements)
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface HilbertSpace<E, S> extends BanachSpace<E, S> {

    /**
     * Returns the inner product of two elements.
     * 
     * @param a the first element
     * @param b the second element
     * @return <a, b>
     */
    S innerProduct(E a, E b);

    // Note: norm(x) is typically sqrt(<x, x>)
}
