package org.jscience.mathematics.algebraic.categories;

/**
 * This interface defines a functor.
 *
 * @author Mark Hale
 * @version 1.0
 * @planetmath Functor
 */
public interface Functor extends Category.Morphism {
    /**
     * Maps an object from one category to another.
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Object map(Object o);

    /**
     * Maps a morphism from one category to another.
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Category.Morphism map(Category.Morphism m);

    /**
     * Returns the composition of this functor with another.
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Functor compose(Functor f);
}
