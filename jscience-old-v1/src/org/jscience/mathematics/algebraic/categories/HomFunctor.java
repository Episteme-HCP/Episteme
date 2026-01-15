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
