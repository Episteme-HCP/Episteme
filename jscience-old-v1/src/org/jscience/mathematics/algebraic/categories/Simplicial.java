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

import org.jscience.mathematics.MathUtils;


/**
 * The Simplicial class encapsulates the simplicial category.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class Simplicial extends Object implements Category {
    /** DOCUMENT ME! */
    public final Bifunctor ADDITION = new Addition();

/**
     * Constructs a simplicial category.
     */
    public Simplicial() {
    }

    /**
     * Returns the identity morphism for an object.
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Category.Morphism identity(Object a) {
        Preorder p = (Preorder) a;
        Integer[] id = new Integer[p.ordinal()];

        for (int i = 0; i < id.length; i++)
            id[i] = new Integer(i);

        return new IncreasingMap(p, id);
    }

    /**
     * Returns the cardinality of an object.
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object cardinality(Object a) {
        return new Integer(((Preorder) a).ordinal());
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
        return new FunctionSet((Preorder) a, (Preorder) b);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object terminal() {
        return new Preorder(1);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    public class FunctionSet implements Category.HomSet {
        /** DOCUMENT ME! */
        private final Preorder from;

        /** DOCUMENT ME! */
        private final Preorder to;

/**
         * Creates a new FunctionSet object.
         *
         * @param a DOCUMENT ME!
         * @param b DOCUMENT ME!
         */
        public FunctionSet(Preorder a, Preorder b) {
            from = a;
            to = b;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int cardinality() {
            return (int) MathUtils.binomial((from.ordinal() + to.ordinal()) -
                1, from.ordinal());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    public class IncreasingMap implements Functor {
        /** DOCUMENT ME! */
        protected final Preorder from;

        /** DOCUMENT ME! */
        protected final Preorder to;

        /** DOCUMENT ME! */
        protected final Integer[] out;

/**
         * Creates a new IncreasingMap object.
         *
         * @param toObj DOCUMENT ME!
         * @param toImg DOCUMENT ME!
         */
        public IncreasingMap(Preorder toObj, Integer[] toImg) {
            from = new Preorder(toImg.length);
            to = toObj;
            out = toImg;
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
            return out[((Integer) o).intValue()];
        }

        /**
         * DOCUMENT ME!
         *
         * @param m DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Category.Morphism map(Category.Morphism m) {
            return ((Preorder.RelationSet) to.hom(map(m.domain()),
                map(m.codomain()))).morphism;
        }

        /**
         * DOCUMENT ME!
         *
         * @param m DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Category.Morphism compose(Category.Morphism m) {
            return compose((Functor) m);
        }

        /**
         * DOCUMENT ME!
         *
         * @param f DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws UndefinedCompositionException DOCUMENT ME!
         * @throws IllegalArgumentException DOCUMENT ME!
         */
        public Functor compose(Functor f) {
            if (f instanceof IncreasingMap) {
                IncreasingMap im = (IncreasingMap) f;

                if (to.equals(im.from)) {
                    Integer[] outImg = new Integer[out.length];

                    for (int i = 0; i < outImg.length; i++)
                        outImg[i] = (Integer) im.map(out[i]);

                    return new IncreasingMap(im.to, outImg);
                } else {
                    throw new UndefinedCompositionException();
                }
            } else {
                throw new IllegalArgumentException(
                    "Morphism is not an IncreasingMap.");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    public final class FaceMap extends IncreasingMap {
        /** DOCUMENT ME! */
        private final int skip;

/**
         * Creates a new FaceMap object.
         *
         * @param toObj DOCUMENT ME!
         * @param i     DOCUMENT ME!
         */
        public FaceMap(Preorder toObj, int i) {
            super(toObj, new Integer[toObj.ordinal() - 1]);
            skip = i;

            for (i = 0; i < skip; i++)
                out[i] = new Integer(i);

            for (; i < out.length; i++)
                out[i] = new Integer(i + 1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    public final class DegeneracyMap extends IncreasingMap {
        /** DOCUMENT ME! */
        private final int repeat;

/**
         * Creates a new DegeneracyMap object.
         *
         * @param toObj DOCUMENT ME!
         * @param i     DOCUMENT ME!
         */
        public DegeneracyMap(Preorder toObj, int i) {
            super(toObj, new Integer[toObj.ordinal() + 1]);
            repeat = i;

            for (i = 0; i <= repeat; i++)
                out[i] = new Integer(i);

            for (; i < out.length; i++)
                out[i] = new Integer(i - 1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    public final class Addition implements Bifunctor {
/**
         * Creates a new Addition object.
         */
        public Addition() {
        }

        /**
         * DOCUMENT ME!
         *
         * @param a DOCUMENT ME!
         * @param b DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object map(Object a, Object b) {
            return new Preorder(((Preorder) a).ordinal() +
                ((Preorder) b).ordinal());
        }

        /**
         * DOCUMENT ME!
         *
         * @param m DOCUMENT ME!
         * @param n DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Category.Morphism map(Category.Morphism m, Category.Morphism n) {
            IncreasingMap im = (IncreasingMap) m;
            IncreasingMap in = (IncreasingMap) n;
            Preorder to = new Preorder(im.to.ordinal() + in.to.ordinal());
            Integer[] toObj = new Integer[im.out.length + in.out.length];
            int i;

            for (i = 0; i < im.out.length; i++)
                toObj[i] = im.out[i];

            for (; i < toObj.length; i++)
                toObj[i] = in.out[i];

            return new IncreasingMap(to, toObj);
        }
    }
}
