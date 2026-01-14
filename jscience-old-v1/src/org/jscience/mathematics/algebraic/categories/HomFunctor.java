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
 * The HomFunctor class encapsulates the hom-bifunctor.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class HomFunctor extends Object implements Bifunctor {
    /** DOCUMENT ME! */
    private Category cat;

/**
     * Constructs the hom bifunctor for a category.
     *
     * @param c DOCUMENT ME!
     */
    public HomFunctor(Category c) {
        cat = c;
    }

    /**
     * Maps two objects to another.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object map(Object a, Object b) {
        return cat.hom(a, b);
    }

    /**
     * Maps two morphisms to another.
     *
     * @param m DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Category.Morphism map(Category.Morphism m, Category.Morphism n) {
        return new HomMorphism(m, n);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class HomMorphism implements Category.Morphism {
        /** DOCUMENT ME! */
        private Category.Morphism in;

        /** DOCUMENT ME! */
        private Category.Morphism out;

/**
         * Creates a new HomMorphism object.
         *
         * @param m DOCUMENT ME!
         * @param n DOCUMENT ME!
         */
        public HomMorphism(Category.Morphism m, Category.Morphism n) {
            in = m;
            out = n;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object domain() {
            return cat.hom(in.codomain(), out.domain());
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object codomain() {
            return cat.hom(in.domain(), out.codomain());
        }

        /**
         * DOCUMENT ME!
         *
         * @param o DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object map(Object o) {
            return in.compose((Category.Morphism) o).compose(out);
        }

        /**
         * DOCUMENT ME!
         *
         * @param m DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Category.Morphism compose(Category.Morphism m) {
            HomMorphism hm = (HomMorphism) m;

            return new HomMorphism(hm.in.compose(in), out.compose(hm.out));
        }
    }
}
