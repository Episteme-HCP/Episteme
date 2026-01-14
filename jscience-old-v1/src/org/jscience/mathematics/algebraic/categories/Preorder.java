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
 * The Preorder class encapsulates preorders as categories.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class Preorder extends Object implements Category {
    /** DOCUMENT ME! */
    private final int N;

/**
     * Constructs a preorder category.
     *
     * @param n DOCUMENT ME!
     */
    public Preorder(int n) {
        N = n;
    }

    /**
     * Returns the identity morphism for an object.
     *
     * @param a an Integer.
     *
     * @return DOCUMENT ME!
     */
    public Category.Morphism identity(Object a) {
        return new Relation((Integer) a, (Integer) a);
    }

    /**
     * Returns the cardinality of an object.
     *
     * @param a an Integer.
     *
     * @return an Integer.
     */
    public Object cardinality(Object a) {
        return a;
    }

    /**
     * Returns a hom-set.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Category.HomSet hom(Object a, Object b) {
        final Integer i = (Integer) a;
        final Integer j = (Integer) b;

        if (i.compareTo(j) <= 0) {
            return new RelationSet(i, j);
        } else {
            return new RelationSet();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object initial() {
        return new Integer(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object terminal() {
        return new Integer(N - 1);
    }

    /**
     * Returns the ordinal that this category represents.
     *
     * @return DOCUMENT ME!
     */
    public int ordinal() {
        return N;
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    public class RelationSet implements Category.HomSet {
        /** DOCUMENT ME! */
        public final Relation morphism;

/**
         * Creates a new RelationSet object.
         */
        public RelationSet() {
            morphism = null;
        }

/**
         * Creates a new RelationSet object.
         *
         * @param a DOCUMENT ME!
         * @param b DOCUMENT ME!
         */
        public RelationSet(Integer a, Integer b) {
            morphism = new Relation(a, b);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    public class Relation implements Category.Morphism {
        /** DOCUMENT ME! */
        private final Integer from;

        /** DOCUMENT ME! */
        private final Integer to;

/**
         * Creates a new Relation object.
         *
         * @param a DOCUMENT ME!
         * @param b DOCUMENT ME!
         */
        public Relation(Integer a, Integer b) {
            from = a;
            to = b;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object domain() {
            return from;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object codomain() {
            return to;
        }

        /**
         * DOCUMENT ME!
         *
         * @param o DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object map(Object o) {
            return to;
        }

        /**
         * DOCUMENT ME!
         *
         * @param m DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws UndefinedCompositionException DOCUMENT ME!
         * @throws IllegalArgumentException DOCUMENT ME!
         */
        public Category.Morphism compose(Category.Morphism m) {
            if (m instanceof Relation) {
                Relation r = (Relation) m;

                if (to.equals(r.from)) {
                    return new Relation(from, r.to);
                } else {
                    throw new UndefinedCompositionException();
                }
            } else {
                throw new IllegalArgumentException(
                    "Morphism is not a Relation.");
            }
        }
    }
}
