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

package org.jscience.mathematics.analysis;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.Double;


/**
 * This class describes a function on a 2D space.
 *
 * @author Mark Hale, Silvere Martin-Michiellot
 * @version 1.0
 */

//I am not sure (actually quite the opposite) that I can implement Field.Member and not only Ring.Member (as Mark Hale did in the first place)
//some values won't be defined, here
public abstract class DoubleFunction2D implements PrimitiveMappingND,
    Field.Member {
    //include support for domains
    /** DOCUMENT ME! */
    private IntervalsList[] intervalsLists = {
            new IntervalsList(new Interval(
                    new Double(Double.NEGATIVE_INFINITY),
                    new Double(Double.POSITIVE_INFINITY))),
            new IntervalsList(new Interval(
                    new Double(Double.NEGATIVE_INFINITY),
                    new Double(Double.POSITIVE_INFINITY)))
        };

    //you have to take care that x[] is of dimension 2
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract double map(double x, double y);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int numInputDimensions() {
        return 2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int numOutputDimensions() {
        return 1;
    }

    //we do check that x[] is of dimension 2.
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] map(double[] x) {
        if (x.length == 2) {
            double[] result;

            result = new double[1];
            result[0] = map(x[0], x[1]);

            return result;
        } else {
            throw new IllegalArgumentException(
                "This class supports only 2D mappings.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double[] map(float[] x) {
        if (x.length == 2) {
            double[] result;

            result = new double[1];
            result[0] = map((float) x[0], (float) x[1]);

            return result;
        } else {
            throw new IllegalArgumentException(
                "This class supports only 2D mappings.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double[] map(long[] x) {
        if (x.length == 2) {
            double[] result;

            result = new double[1];
            result[0] = map((long) x[0], (long) x[1]);

            return result;
        } else {
            throw new IllegalArgumentException(
                "This class supports only 2D mappings.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double[] map(int[] x) {
        if (x.length == 2) {
            double[] result;

            result = new double[1];
            result[0] = map((int) x[0], (int) x[1]);

            return result;
        } else {
            throw new IllegalArgumentException(
                "This class supports only 2D mappings.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IntervalsList[] getIntervalsLists() {
        return intervalsLists;
    }

    //this should be an array of dimension 2
    /**
     * DOCUMENT ME!
     *
     * @param lists DOCUMENT ME!
     */
    public void setIntervalsLists(IntervalsList[] lists) {
        if (lists.length == 2) {
            intervalsLists = lists;
        } else {
            throw new IllegalArgumentException(
                "This class supports only 2D mappings.");
        }
    }

    /**
     * Returns the negative of this function.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member negate() {
        return new Negation(this);
    }

    /**
     * Returns the addition of this function and another.
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member add(final AbelianGroup.Member f) {
        if (f instanceof DoubleFunction2D) {
            return add((DoubleFunction2D) f);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleFunction2D add(DoubleFunction2D f) {
        return new Sum(this, f);
    }

    /**
     * Returns the subtraction of this function and another.
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member f) {
        if (f instanceof DoubleFunction2D) {
            return subtract((DoubleFunction2D) f);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleFunction2D subtract(DoubleFunction2D f) {
        return new Difference(this, f);
    }

    /**
     * Returns the multiplication of this function and another.
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Ring.Member multiply(Ring.Member f) {
        if (f instanceof DoubleFunction2D) {
            return multiply((DoubleFunction2D) f);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleFunction2D multiply(DoubleFunction2D f) {
        return new Product(this, f);
    }

    /**
     * Returns this function inverse.
     *
     * @return DOCUMENT ME!
     */
    public Field.Member inverse() {
        return new Inversion(this);
    }

    /**
     * Returns the quotient of this function and another.
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Field.Member divide(Field.Member f) {
        if (f instanceof DoubleFunction2D) {
            return divide((DoubleFunction2D) f);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleFunction2D divide(DoubleFunction2D f) {
        return new Division(this, f);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Negation extends DoubleFunction2D {
        /** DOCUMENT ME! */
        private final DoubleFunction2D f;

/**
         * Creates a new Negation object.
         *
         * @param f DOCUMENT ME!
         */
        public Negation(DoubleFunction2D f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double map(double x, double y) {
            return -f.map(x, y);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Sum extends DoubleFunction2D {
        /** DOCUMENT ME! */
        private final DoubleFunction2D f1;

        /** DOCUMENT ME! */
        private final DoubleFunction2D f2;

/**
         * Creates a new Sum object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Sum(DoubleFunction2D f1, DoubleFunction2D f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double map(double x, double y) {
            return f1.map(x, y) + f2.map(x, y);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Difference extends DoubleFunction2D {
        /** DOCUMENT ME! */
        private final DoubleFunction2D f1;

        /** DOCUMENT ME! */
        private final DoubleFunction2D f2;

/**
         * Creates a new Difference object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Difference(DoubleFunction2D f1, DoubleFunction2D f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double map(double x, double y) {
            return f1.map(x, y) - f2.map(x, y);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Product extends DoubleFunction2D {
        /** DOCUMENT ME! */
        private final DoubleFunction2D f1;

        /** DOCUMENT ME! */
        private final DoubleFunction2D f2;

/**
         * Creates a new Product object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Product(DoubleFunction2D f1, DoubleFunction2D f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double map(double x, double y) {
            return f1.map(x, y) * f2.map(x, y);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Inversion extends DoubleFunction2D {
        /** DOCUMENT ME! */
        private final DoubleFunction2D f;

/**
         * Creates a new Inversion object.
         *
         * @param f DOCUMENT ME!
         */
        public Inversion(DoubleFunction2D f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double map(double x, double y) {
            return 1 / f.map(x, y);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Division extends DoubleFunction2D {
        /** DOCUMENT ME! */
        private final DoubleFunction2D f1;

        /** DOCUMENT ME! */
        private final DoubleFunction2D f2;

/**
         * Creates a new Division object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Division(DoubleFunction2D f1, DoubleFunction2D f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double map(double x, double y) {
            return f1.map(x, y) / f2.map(x, y);
        }
    }
}
