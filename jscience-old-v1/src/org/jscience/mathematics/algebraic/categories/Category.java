package org.jscience.mathematics.algebraic.categories;

/**
 * This interface defines a category.
 *
 * @author Mark Hale
 * @version 1.0
 * @planetmath Category
 */
public interface Category {
    /**
     * Returns the identity morphism for an object.
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Morphism identity(Object a);

    /**
     * Returns the cardinality of an object. In general, this may not
     * be an Integer.
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Object cardinality(Object a);

    /**
     * Returns a hom-set.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    HomSet hom(Object a, Object b);

/**
     * This interface defines a morphism in a category.
     */
    interface Morphism {
        /**
         * Returns the domain.
         *
         * @return DOCUMENT ME!
         */
        Object domain();

        /**
         * Returns the codomain.
         *
         * @return DOCUMENT ME!
         */
        Object codomain();

        /**
         * Maps an object from the domain to the codomain.
         *
         * @param o DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        Object map(Object o);

        /**
         * Returns the composition of this morphism with another.
         *
         * @param m DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws UndefinedCompositionException DOCUMENT ME!
         */
        Morphism compose(Morphism m) throws UndefinedCompositionException;
    }

/**
     * This interface defines a hom-set.
     */
    interface HomSet {
    }
}
