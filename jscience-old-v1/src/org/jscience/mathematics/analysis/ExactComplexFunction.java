package org.jscience.mathematics.analysis;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.*;
import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.algebraic.numbers.Float;
import org.jscience.mathematics.algebraic.numbers.Integer;
import org.jscience.mathematics.algebraic.numbers.Long;
import org.jscience.mathematics.analysis.polynomials.ExactComplexPolynomial;


/**
 * This class describes a function on the complex numbers.
 *
 * @author Mark Hale, Silvere Martin-Michiellot
 * @version 1.0
 */

//I am not sure (actually quite the opposite) that I can implement Field.Member and not only Ring.Member (as Mark Hale did in the first place)
//some values won't be defined, here...
//please note that we don't support composition of domains although this would be an interesting feature
//this can currently lead to some strange results for getIntervalsList() unless you manually change the Intervals bounds for example when building Compositions of Functions.
public abstract class ExactComplexFunction implements NumberMapping, C1Function,
    Field.Member {
    //include support for domains
    /** DOCUMENT ME! */
    private IntervalsList intervalsList = new IntervalsList(new Interval(
                ExactReal.NEGATIVE_INFINITY, ExactReal.POSITIVE_INFINITY));

    //check that Number is an ExactComplex
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Number map(Number x) {
        if (x instanceof ExactComplex) {
            return map((ExactComplex) x);
        } else if (x instanceof ExactReal) {
            return map(new ExactComplex((ExactReal) x, ExactReal.ZERO));
        } else if (x instanceof ExactRational) {
            return map(new ExactComplex(new ExactReal((ExactRational) x),
                    ExactReal.ZERO));
        } else if (x instanceof ExactInteger) {
            return map(new ExactComplex(new ExactReal((ExactInteger) x),
                    ExactReal.ZERO));
        } else if (x instanceof Double) {
            return map(new ExactComplex(new ExactReal((Double) x),
                    ExactReal.ZERO));
        } else if (x instanceof Float) {
            return map(new ExactComplex(new ExactReal((Float) x), ExactReal.ZERO));
        } else if (x instanceof Rational) {
            return map(new ExactComplex(new ExactReal((Rational) x),
                    ExactReal.ZERO));
        } else if (x instanceof Long) {
            return map(new ExactComplex(new ExactReal((Long) x), ExactReal.ZERO));
        } else if (x instanceof Integer) {
            return map(new ExactComplex(new ExactReal((Integer) x),
                    ExactReal.ZERO));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    //be sure to check the domain (intervalsList) or throw an Exception
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract ExactComplex map(ExactComplex x);

    /**
     * Returns the differential of this function.
     *
     * @return DOCUMENT ME!
     */
    public abstract ExactComplexFunction differentiate();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IntervalsList getIntervalsList() {
        return intervalsList;
    }

    /**
     * DOCUMENT ME!
     *
     * @param list DOCUMENT ME!
     */
    public void setIntervalsList(IntervalsList list) {
        intervalsList = list;
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ExactComplexFunction compose(ExactComplexFunction f) {
        return new Composition(this, f);
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
        if (f instanceof ExactComplexFunction) {
            return add((ExactComplexFunction) f);
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
    public ExactComplexFunction add(ExactComplexFunction f) {
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
        if (f instanceof ExactComplexFunction) {
            return subtract((ExactComplexFunction) f);
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
    public ExactComplexFunction subtract(ExactComplexFunction f) {
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
        if (f instanceof ExactComplexFunction) {
            return multiply((ExactComplexFunction) f);
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
    public ExactComplexFunction multiply(ExactComplexFunction f) {
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
        if (f instanceof ExactComplexFunction) {
            return divide((ExactComplexFunction) f);
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
    public ExactComplexFunction divide(ExactComplexFunction f) {
        return new Division(this, f);
    }

    /**
     * Returns the Taylor expansion of this function about a point.
     *
     * @param a the point at which to expand about.
     * @param n the number of terms to expand to.
     *
     * @return the Taylor series of f(x+a).
     */
    public ExactComplexPolynomial taylorExpand(ExactComplex a, int n) {
        ExactComplex[] coeff = new ExactComplex[n];
        coeff[0] = map(a);

        ExactComplexFunction diff = this;
        int factorial = 1;

        for (int i = 1; i < n; i++) {
            diff = diff.differentiate();
            factorial *= i;
            coeff[i] = diff.map(a).divide(new ExactReal(factorial));
        }

        return new ExactComplexPolynomial(coeff);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Composition extends ExactComplexFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final ExactComplexFunction f1;

        /** DOCUMENT ME! */
        private final ExactComplexFunction f2;

/**
         * Creates a new Composition object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Composition(ExactComplexFunction f1, ExactComplexFunction f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactComplex map(ExactComplex x) {
            return f1.map(f2.map(x));
        }

        /**
         * Chain rule.
         *
         * @return DOCUMENT ME!
         */
        public ExactComplexFunction differentiate() {
            return new Product(new Composition(f1.differentiate(), f2),
                f2.differentiate());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Negation extends ExactComplexFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final ExactComplexFunction f;

/**
         * Creates a new Negation object.
         *
         * @param f DOCUMENT ME!
         */
        public Negation(ExactComplexFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactComplex map(ExactComplex x) {
            return (ExactComplex) f.map(x).negate();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactComplexFunction differentiate() {
            return new Negation(f.differentiate());
        }

        //public ExactComplexFunction integrate() {
        //        return new Negation(f.integrate());
        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Sum extends ExactComplexFunction implements C1Function {
        /** DOCUMENT ME! */
        private final ExactComplexFunction f1;

        /** DOCUMENT ME! */
        private final ExactComplexFunction f2;

/**
         * Creates a new Sum object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Sum(ExactComplexFunction f1, ExactComplexFunction f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactComplex map(ExactComplex x) {
            return f1.map(x).add(f2.map(x));
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactComplexFunction differentiate() {
            return new Sum(f1.differentiate(), f2.differentiate());
        }

        //public ExactComplexFunction integrate() {
        //        return new Sum(f1.integrate(), f2.integrate());
        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Difference extends ExactComplexFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final ExactComplexFunction f1;

        /** DOCUMENT ME! */
        private final ExactComplexFunction f2;

/**
         * Creates a new Difference object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Difference(ExactComplexFunction f1, ExactComplexFunction f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactComplex map(ExactComplex x) {
            return f1.map(x).subtract(f2.map(x));
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactComplexFunction differentiate() {
            return new Difference(f1.differentiate(), f2.differentiate());
        }

        //public ExactComplexFunction integrate() {
        //        return new Difference(f1.integrate(), f2.integrate());
        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Product extends ExactComplexFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final ExactComplexFunction f1;

        /** DOCUMENT ME! */
        private final ExactComplexFunction f2;

/**
         * Creates a new Product object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Product(ExactComplexFunction f1, ExactComplexFunction f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactComplex map(ExactComplex x) {
            return f1.map(x).multiply(f2.map(x));
        }

        /**
         * Product rule.
         *
         * @return DOCUMENT ME!
         */
        public ExactComplexFunction differentiate() {
            return new Sum(new Product(f1.differentiate(), f2),
                new Product(f1, f2.differentiate()));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Inversion extends ExactComplexFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final ExactComplexFunction f;

/**
         * Creates a new Inversion object.
         *
         * @param f DOCUMENT ME!
         */
        public Inversion(ExactComplexFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactComplex map(ExactComplex x) {
            return (ExactComplex) f.map(x).inverse();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactComplexFunction differentiate() {
            return new Division(new Negation(f.differentiate()),
                new Product(f, f));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Division extends ExactComplexFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final ExactComplexFunction f1;

        /** DOCUMENT ME! */
        private final ExactComplexFunction f2;

/**
         * Creates a new Division object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Division(ExactComplexFunction f1, ExactComplexFunction f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactComplex map(ExactComplex x) {
            return f1.map(x).divide(f2.map(x));
        }

        /**
         * Division rule.
         *
         * @return DOCUMENT ME!
         */
        public ExactComplexFunction differentiate() {
            return new Division(new Difference(
                    new Product(f1.differentiate(), f2),
                    new Product(f1, f2.differentiate())), new Product(f2, f2));
        }
    }
}
