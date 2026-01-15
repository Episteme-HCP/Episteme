package org.jscience.mathematics.algebraic.categories;

/**
 * This interface defines a natural transformation.
 *
 * @author Mark Hale
 * @version 1.0
 */
public interface NaturalTransformation {
    /**
     * Maps one functor to another.
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Functor map(Functor f);

    /**
     * Returns the vertical composition of this transformation with
     * another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    NaturalTransformation composeVert(NaturalTransformation n);

    /**
     * Returns the horizontal composition of this transformation with
     * another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    NaturalTransformation composeHorz(NaturalTransformation n);
}
