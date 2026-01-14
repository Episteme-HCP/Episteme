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
import org.jscience.mathematics.algebraic.numbers.*;
import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.algebraic.numbers.Float;
import org.jscience.mathematics.algebraic.numbers.Integer;
import org.jscience.mathematics.algebraic.numbers.Long;


/**
 * This class describes a function on a 2D space.
 *
 * @author Mark Hale, Silvere Martin-Michiellot
 * @version 1.0
 */

//I am not sure (actually quite the opposite) that I can implement Field.Member and not only Ring.Member (as Mark Hale did in the first place)
//some values won't be defined, here
public abstract class ExactRealFunction2D implements NumberMappingND,
    Field.Member {
    //include support for domains
    /** DOCUMENT ME! */
    private IntervalsList[] intervalsLists = {
            new IntervalsList(new Interval(ExactReal.NEGATIVE_INFINITY,
                    ExactReal.POSITIVE_INFINITY)),
            new IntervalsList(new Interval(ExactReal.NEGATIVE_INFINITY,
                    ExactReal.POSITIVE_INFINITY))
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
    public abstract ExactReal map(ExactReal x, ExactReal y);

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
    public ExactReal[] map(Number[] x) {
        if (x.length == 2) {
            if (x instanceof ExactReal[]) {
                ExactReal[] result;

                result = new ExactReal[1];
                result[0] = map(new ExactReal((ExactRational) x[0]),
                        new ExactReal((ExactRational) x[1]));

                return result;
            } else if (x instanceof ExactRational[]) {
                ExactReal[] result;

                result = new ExactReal[1];
                result[0] = map(new ExactReal((ExactRational) x[0]),
                        new ExactReal((ExactRational) x[1]));

                return result;
            } else if (x instanceof ExactInteger[]) {
                ExactReal[] result;

                result = new ExactReal[1];
                result[0] = map(new ExactReal((ExactInteger) x[0]),
                        new ExactReal((ExactInteger) x[1]));

                return result;
            } else if (x instanceof Double[]) {
                ExactReal[] result;

                result = new ExactReal[1];
                result[0] = map(new ExactReal((Double) x[0]),
                        new ExactReal((Double) x[1]));

                return result;
            } else if (x instanceof Float[]) {
                ExactReal[] result;

                result = new ExactReal[1];
                result[0] = map(new ExactReal((Float) x[0]),
                        new ExactReal((Float) x[1]));

                return result;
            } else if (x instanceof Rational[]) {
                ExactReal[] result;

                result = new ExactReal[1];
                result[0] = map(new ExactReal((Rational) x[0]),
                        new ExactReal((Rational) x[1]));

                return result;
            } else if (x instanceof Long[]) {
                ExactReal[] result;

                result = new ExactReal[1];
                result[0] = map(new ExactReal((Long) x[0]),
                        new ExactReal((Long) x[1]));

                return result;
            } else if (x instanceof Integer[]) {
                ExactReal[] result;

                result = new ExactReal[1];
                result[0] = map(new ExactReal((Integer) x[0]),
                        new ExactReal((Integer) x[1]));

                return result;
            } else {
                throw new IllegalArgumentException(
                    "Member class not recognised by this method.");
            }
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
        if (f instanceof ExactRealFunction2D) {
            return add((ExactRealFunction2D) f);
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
    public ExactRealFunction2D add(ExactRealFunction2D f) {
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
        if (f instanceof ExactRealFunction2D) {
            return subtract((ExactRealFunction2D) f);
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
    public ExactRealFunction2D subtract(ExactRealFunction2D f) {
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
        if (f instanceof ExactRealFunction2D) {
            return multiply((ExactRealFunction2D) f);
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
    public ExactRealFunction2D multiply(ExactRealFunction2D f) {
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
        if (f instanceof ExactRealFunction2D) {
            return divide((ExactRealFunction2D) f);
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
    public ExactRealFunction2D divide(ExactRealFunction2D f) {
        return new Division(this, f);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Negation extends ExactRealFunction2D {
        /** DOCUMENT ME! */
        private final ExactRealFunction2D f;

/**
         * Creates a new Negation object.
         *
         * @param f DOCUMENT ME!
         */
        public Negation(ExactRealFunction2D f) {
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
        public ExactReal map(ExactReal x, ExactReal y) {
            return (ExactReal) f.map(x, y).negate();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Sum extends ExactRealFunction2D {
        /** DOCUMENT ME! */
        private final ExactRealFunction2D f1;

        /** DOCUMENT ME! */
        private final ExactRealFunction2D f2;

/**
         * Creates a new Sum object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Sum(ExactRealFunction2D f1, ExactRealFunction2D f2) {
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
        public ExactReal map(ExactReal x, ExactReal y) {
            return f1.map(x, y).add(f2.map(x, y));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Difference extends ExactRealFunction2D {
        /** DOCUMENT ME! */
        private final ExactRealFunction2D f1;

        /** DOCUMENT ME! */
        private final ExactRealFunction2D f2;

/**
         * Creates a new Difference object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Difference(ExactRealFunction2D f1, ExactRealFunction2D f2) {
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
        public ExactReal map(ExactReal x, ExactReal y) {
            return f1.map(x, y).subtract(f2.map(x, y));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Product extends ExactRealFunction2D {
        /** DOCUMENT ME! */
        private final ExactRealFunction2D f1;

        /** DOCUMENT ME! */
        private final ExactRealFunction2D f2;

/**
         * Creates a new Product object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Product(ExactRealFunction2D f1, ExactRealFunction2D f2) {
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
        public ExactReal map(ExactReal x, ExactReal y) {
            return f1.map(x, y).multiply(f2.map(x, y));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Inversion extends ExactRealFunction2D {
        /** DOCUMENT ME! */
        private final ExactRealFunction2D f;

/**
         * Creates a new Inversion object.
         *
         * @param f DOCUMENT ME!
         */
        public Inversion(ExactRealFunction2D f) {
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
        public ExactReal map(ExactReal x, ExactReal y) {
            return (ExactReal) f.map(x, y).inverse();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Division extends ExactRealFunction2D {
        /** DOCUMENT ME! */
        private final ExactRealFunction2D f1;

        /** DOCUMENT ME! */
        private final ExactRealFunction2D f2;

/**
         * Creates a new Division object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Division(ExactRealFunction2D f1, ExactRealFunction2D f2) {
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
        public ExactReal map(ExactReal x, ExactReal y) {
            return f1.map(x, y).divide(f2.map(x, y));
        }
    }
}
