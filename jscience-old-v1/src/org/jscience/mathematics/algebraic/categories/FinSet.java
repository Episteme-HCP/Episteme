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
import org.jscience.mathematics.Set;

import java.util.Iterator;


/**
 * The FinSet class encapsulates the category <b>FinSet</b>.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class FinSet extends Object implements Category {
/**
     * Constructs a <b>FinSet</b> category.
     */
    public FinSet() {
    }

    /**
     * Returns the identity morphism for an object.
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Category.Morphism identity(Object a) {
        return new IdentityFunction((Set) a);
    }

    /**
     * Returns the cardinality of an object.
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object cardinality(Object a) {
        return new Integer(((Set) a).cardinality());
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
        return new FunctionSet((Set) a, (Set) b);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    public class FunctionSet implements Set, Category.HomSet {
        /** DOCUMENT ME! */
        private final Set from;

        /** DOCUMENT ME! */
        private final Set to;

        /** DOCUMENT ME! */
        private final int size;

/**
         * Creates a new FunctionSet object.
         *
         * @param a DOCUMENT ME!
         * @param b DOCUMENT ME!
         */
        public FunctionSet(Set a, Set b) {
            from = a;
            to = b;
            size = MathUtils.pow(b.cardinality(), a.cardinality());
        }

        /**
         * Returns an element of this hom-set.
         *
         * @param in DOCUMENT ME!
         * @param out DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Function getElement(Object[] in, Object[] out) {
            return new Function(from, to, in, out);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int cardinality() {
            return size;
        }

        /**
         * DOCUMENT ME!
         *
         * @param set DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Set union(Set set) {
            return set.union(this);
        }

        /**
         * DOCUMENT ME!
         *
         * @param set DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Set intersection(Set set) {
            return set.intersection(this);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean isEmpty() {
            return size == 0;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws UnsupportedOperationException DOCUMENT ME!
         */
        public Iterator iterator() {
            //to many elements
            throw new UnsupportedOperationException(
                "Iterator is not a supported method for FunctionSet.");
        }

        /**
         * DOCUMENT ME!
         *
         * @param o DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws UnsupportedOperationException DOCUMENT ME!
         */
        public boolean remove(Object o) {
            throw new UnsupportedOperationException(
                "Remove is not a supported method for FunctionSet.");
        }

        /**
         * DOCUMENT ME!
         *
         * @param o DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws UnsupportedOperationException DOCUMENT ME!
         */
        public boolean contains(Object o) {
            //we could probably provide an useful result for this method although I don't know what
            //as I (S.M.M.) didn't program the rest, neither do I understand what this is about
            throw new UnsupportedOperationException(
                "Contains is not a supported method for FunctionSet.");
        }

        /**
         * DOCUMENT ME!
         *
         * @param o DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean equals(Object o) {
            if (o instanceof FunctionSet) {
                return from.equals(((FunctionSet) o).from) &&
                to.equals(((FunctionSet) o).to);
            } else {
                return false;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int hashCode() {
            return from.hashCode() + to.hashCode();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws UnsupportedOperationException DOCUMENT ME!
         */
        public Object[] toArray() {
            //to many elements
            throw new UnsupportedOperationException(
                "toArray is not a supported method for FunctionSet.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    public class Function implements Category.Morphism {
        /** DOCUMENT ME! */
        private final Set from;

        /** DOCUMENT ME! */
        private final Set to;

        /** DOCUMENT ME! */
        private final Object[] in;

        /** DOCUMENT ME! */
        private final Object[] out;

/**
         * Creates a new Function object.
         *
         * @param a       DOCUMENT ME!
         * @param b       DOCUMENT ME!
         * @param inObjs  DOCUMENT ME!
         * @param outObjs DOCUMENT ME!
         */
        public Function(Set a, Set b, Object[] inObjs, Object[] outObjs) {
            from = a;
            to = b;
            in = inObjs;
            out = outObjs;
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
            for (int i = 0; i < in.length; i++) {
                if (o.equals(in[i])) {
                    return out[i];
                }
            }

            return null;
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
            if (m instanceof Function) {
                Function f = (Function) m;

                if (to.equals(f.from)) {
                    Object[] outObjs = new Object[in.length];

                    for (int i = 0; i < outObjs.length; i++)
                        outObjs[i] = f.map(out[i]);

                    return new Function(from, f.to, in, outObjs);
                } else {
                    throw new UndefinedCompositionException();
                }
            } else {
                throw new IllegalArgumentException(
                    "Morphism is not a Function.");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class IdentityFunction extends Function {
/**
         * Creates a new IdentityFunction object.
         *
         * @param s DOCUMENT ME!
         */
        public IdentityFunction(Set s) {
            super(s, s, null, null);
        }

        /**
         * DOCUMENT ME!
         *
         * @param o DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object map(Object o) {
            return o;
        }

        /**
         * DOCUMENT ME!
         *
         * @param m DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws IllegalArgumentException DOCUMENT ME!
         */
        public Category.Morphism compose(Category.Morphism m) {
            if (m instanceof Function) {
                return m;
            } else {
                throw new IllegalArgumentException(
                    "Morphism is not a Function.");
            }
        }
    }
}
