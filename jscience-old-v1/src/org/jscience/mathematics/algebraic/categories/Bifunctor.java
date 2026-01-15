package org.jscience.mathematics.algebraic.categories;

/**
 * This interface defines a bifunctor.
 *
 * @author Mark Hale
 * @version 1.0
 */
public interface Bifunctor {
    /**
     * Maps a pair of objects from one category to another.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Object map(Object a, Object b);

    /**
     * Maps a pair of morphisms from one category to another.
     *
     * @param m DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Category.Morphism map(Category.Morphism m, Category.Morphism n);
}
