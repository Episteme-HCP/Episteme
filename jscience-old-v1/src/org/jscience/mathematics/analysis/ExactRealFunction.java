package org.jscience.mathematics.analysis;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.*;
import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.algebraic.numbers.Float;
import org.jscience.mathematics.algebraic.numbers.Integer;
import org.jscience.mathematics.algebraic.numbers.Long;
import org.jscience.mathematics.analysis.polynomials.ExactRealPolynomial;


/**
 * This class describes a function on the real numbers.
 *
 * @author Mark Hale, Silvere Martin-Michiellot
 * @version 1.0
 */

//I am not sure (actually quite the opposite) that I can implement Field.Member and not only Ring.Member (as Mark Hale did in the first place)
//some values won't be defined, here...
//please note that we don't support composition of domains although this would be an interesting feature
//this can currently lead to some strange results for getIntervalsList() unless you manually change the Intervals bounds for example when building Compositions of Functions.
public abstract class ExactRealFunction implements NumberMapping, C1Function,
    Field.Member {
    //include support for domains
    /** DOCUMENT ME! */
    private IntervalsList intervalsList = new IntervalsList(new Interval(
                ExactReal.NEGATIVE_INFINITY, ExactReal.POSITIVE_INFINITY));

    //check that Number is an ExactReal
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Number map(Number x) {
        if (x instanceof ExactReal) {
            return map((ExactReal) x);
        } else if (x instanceof ExactRational) {
            return map(new ExactReal((ExactRational) x));
        } else if (x instanceof ExactInteger) {
            return map(new ExactReal((ExactInteger) x));
        } else if (x instanceof Double) {
            return map(new ExactReal((Double) x));
        } else if (x instanceof Float) {
            return map(new ExactReal((Float) x));
        } else if (x instanceof Rational) {
            return map(new ExactReal((Rational) x));
        } else if (x instanceof Long) {
            return map(new ExactReal((Long) x));
        } else if (x instanceof Integer) {
            return map(new ExactReal((Integer) x));
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
    public abstract ExactReal map(ExactReal x);

    /**
     * Returns the differential of this function.
     *
     * @return DOCUMENT ME!
     */
    public abstract ExactRealFunction differentiate();

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
    public ExactRealFunction compose(ExactRealFunction f) {
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
        if (f instanceof ExactRealFunction) {
            return add((ExactRealFunction) f);
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
    public ExactRealFunction add(ExactRealFunction f) {
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
        if (f instanceof ExactRealFunction) {
            return subtract((ExactRealFunction) f);
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
    public ExactRealFunction subtract(ExactRealFunction f) {
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
        if (f instanceof ExactRealFunction) {
            return multiply((ExactRealFunction) f);
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
    public ExactRealFunction multiply(ExactRealFunction f) {
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
        if (f instanceof ExactRealFunction) {
            return divide((ExactRealFunction) f);
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
    public ExactRealFunction divide(ExactRealFunction f) {
        return new Division(this, f);
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ExactRealFunction2D tensor(ExactRealFunction f) {
        return new TensorProduct(this, f);
    }

    /**
     * Returns the Taylor expansion of this function about a point.
     *
     * @param a the point at which to expand about.
     * @param n the number of terms to expand to.
     *
     * @return the Taylor series of f(x+a).
     */
    public ExactRealPolynomial taylorExpand(ExactReal a, int n) {
        ExactReal[] coeff = new ExactReal[n];
        coeff[0] = map(a);

        ExactRealFunction diff = this;
        int factorial = 1;

        for (int i = 1; i < n; i++) {
            diff = diff.differentiate();
            factorial *= i;
            coeff[i] = diff.map(a).divide(new ExactReal(factorial));
        }

        return new ExactRealPolynomial(coeff);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Composition extends ExactRealFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final ExactRealFunction f1;

        /** DOCUMENT ME! */
        private final ExactRealFunction f2;

/**
         * Creates a new Composition object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Composition(ExactRealFunction f1, ExactRealFunction f2) {
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
        public ExactReal map(ExactReal x) {
            return f1.map(f2.map(x));
        }

        /**
         * Chain rule.
         *
         * @return DOCUMENT ME!
         */
        public ExactRealFunction differentiate() {
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
    private static class Negation extends ExactRealFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final ExactRealFunction f;

/**
         * Creates a new Negation object.
         *
         * @param f DOCUMENT ME!
         */
        public Negation(ExactRealFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactReal map(ExactReal x) {
            return (ExactReal) f.map(x).negate();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactRealFunction differentiate() {
            return new Negation(f.differentiate());
        }

        //public ExactRealFunction integrate() {
        //        return new Negation(f.integrate());
        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Sum extends ExactRealFunction implements C1Function {
        /** DOCUMENT ME! */
        private final ExactRealFunction f1;

        /** DOCUMENT ME! */
        private final ExactRealFunction f2;

/**
         * Creates a new Sum object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Sum(ExactRealFunction f1, ExactRealFunction f2) {
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
        public ExactReal map(ExactReal x) {
            return f1.map(x).add(f2.map(x));
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactRealFunction differentiate() {
            return new Sum(f1.differentiate(), f2.differentiate());
        }

        //public ExactRealFunction integrate() {
        //        return new Sum(f1.integrate(), f2.integrate());
        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Difference extends ExactRealFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final ExactRealFunction f1;

        /** DOCUMENT ME! */
        private final ExactRealFunction f2;

/**
         * Creates a new Difference object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Difference(ExactRealFunction f1, ExactRealFunction f2) {
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
        public ExactReal map(ExactReal x) {
            return f1.map(x).subtract(f2.map(x));
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactRealFunction differentiate() {
            return new Difference(f1.differentiate(), f2.differentiate());
        }

        //public ExactRealFunction integrate() {
        //        return new Difference(f1.integrate(), f2.integrate());
        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Product extends ExactRealFunction implements C1Function {
        /** DOCUMENT ME! */
        private final ExactRealFunction f1;

        /** DOCUMENT ME! */
        private final ExactRealFunction f2;

/**
         * Creates a new Product object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Product(ExactRealFunction f1, ExactRealFunction f2) {
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
        public ExactReal map(ExactReal x) {
            return f1.map(x).multiply(f2.map(x));
        }

        /**
         * Product rule.
         *
         * @return DOCUMENT ME!
         */
        public ExactRealFunction differentiate() {
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
    private static class Inversion extends ExactRealFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final ExactRealFunction f;

/**
         * Creates a new Inversion object.
         *
         * @param f DOCUMENT ME!
         */
        public Inversion(ExactRealFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactReal map(ExactReal x) {
            return (ExactReal) f.map(x).inverse();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ExactRealFunction differentiate() {
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
    private static class Division extends ExactRealFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final ExactRealFunction f1;

        /** DOCUMENT ME! */
        private final ExactRealFunction f2;

/**
         * Creates a new Division object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Division(ExactRealFunction f1, ExactRealFunction f2) {
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
        public ExactReal map(ExactReal x) {
            return f1.map(x).divide(f2.map(x));
        }

        /**
         * Division rule.
         *
         * @return DOCUMENT ME!
         */
        public ExactRealFunction differentiate() {
            return new Division(new Difference(
                    new Product(f1.differentiate(), f2),
                    new Product(f1, f2.differentiate())), new Product(f2, f2));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class TensorProduct extends ExactRealFunction2D {
        /** DOCUMENT ME! */
        private final ExactRealFunction f1;

        /** DOCUMENT ME! */
        private final ExactRealFunction f2;

/**
         * Creates a new TensorProduct object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public TensorProduct(ExactRealFunction f1, ExactRealFunction f2) {
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
            return f1.map(x).multiply(f2.map(y));
        }
    }
}
