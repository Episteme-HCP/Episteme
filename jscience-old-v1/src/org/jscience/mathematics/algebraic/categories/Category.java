/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
