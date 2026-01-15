package org.jscience.mathematics.analysis;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.*;
import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.algebraic.numbers.Float;
import org.jscience.mathematics.algebraic.numbers.Integer;
import org.jscience.mathematics.algebraic.numbers.Long;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;


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
public abstract class ComplexFunction implements ComplexMapping, C1Function,
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

    //be sure to check the domain (intervalsList) or throw an Exception
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Complex map(Complex x);

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Number map(Number x) {
        if (x instanceof Complex) {
            return map((Complex) x);
        } else if (x instanceof Double) {
            return map(new Complex((Double) x));
        } else if (x instanceof Float) {
            return map(new Complex((Float) x));
        } else if (x instanceof Rational) {
            return map(new Complex((Rational) x));
        } else if (x instanceof Long) {
            return map(new Complex((Long) x));
        } else if (x instanceof Integer) {
            return map(new Complex((Integer) x));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the differential of this function.
     *
     * @return DOCUMENT ME!
     */
    public abstract ComplexFunction differentiate();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IntervalsList[] getIntervalsLists() {
        return intervalsLists;
    }

    //should be an array of dimension 2
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
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComplexFunction compose(ComplexFunction f) {
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
        if (f instanceof ComplexFunction) {
            return add((ComplexFunction) f);
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
    public ComplexFunction add(ComplexFunction f) {
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
        if (f instanceof ComplexFunction) {
            return subtract((ComplexFunction) f);
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
    public ComplexFunction subtract(ComplexFunction f) {
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
        if (f instanceof ComplexFunction) {
            return multiply((ComplexFunction) f);
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
    public ComplexFunction multiply(ComplexFunction f) {
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
        if (f instanceof ComplexFunction) {
            return divide((ComplexFunction) f);
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
    public ComplexFunction divide(ComplexFunction f) {
        return new Division(this, f);
    }

    /**
     * Returns this function raised to a power.
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Field.Member power(double d) {
        return new Power(this, d);
    }

    /**
     * Returns the cosine of this function.
     *
     * @return DOCUMENT ME!
     */
    public Field.Member cos() {
        return new Cosine(this);
    }

    /**
     * Returns the sine of this function.
     *
     * @return DOCUMENT ME!
     */
    public Field.Member sin() {
        return new Sine(this);
    }

    /**
     * Returns the log of this function (actually the ln, see
     * java.lang.Math).
     *
     * @return DOCUMENT ME!
     */
    public Field.Member log() {
        return new Logarithm(this);
    }

    /**
     * Returns the exp of this function.
     *
     * @return DOCUMENT ME!
     */
    public Field.Member exp() {
        return new Exponential(this);
    }

    /**
     * Returns the sqrt of this function.
     *
     * @return DOCUMENT ME!
     */
    public Field.Member sqrt() {
        return new Power(this, 1 / 2);
    }

    /**
     * Returns the Taylor expansion of this function about a point.
     *
     * @param a the point at which to expand about.
     * @param n the number of terms to expand to.
     *
     * @return the Taylor series of f(x+a).
     */
    public ComplexPolynomial taylorExpand(Complex a, int n) {
        Complex[] coeff = new Complex[n];
        coeff[0] = map(a);

        ComplexFunction diff = this;
        int factorial = 1;

        for (int i = 1; i < n; i++) {
            diff = diff.differentiate();
            factorial *= i;
            coeff[i] = diff.map(a).divide(factorial);
        }

        return new ComplexPolynomial(coeff);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Composition extends ComplexFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final ComplexFunction f1;

        /** DOCUMENT ME! */
        private final ComplexFunction f2;

/**
         * Creates a new Composition object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Composition(ComplexFunction f1, ComplexFunction f2) {
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
        public Complex map(Complex x) {
            return f1.map(f2.map(x));
        }

        /**
         * Chain rule.
         *
         * @return DOCUMENT ME!
         */
        public ComplexFunction differentiate() {
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
    private static class Negation extends ComplexFunction implements C1Function {
        /** DOCUMENT ME! */
        private final ComplexFunction f;

/**
         * Creates a new Negation object.
         *
         * @param f DOCUMENT ME!
         */
        public Negation(ComplexFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Complex map(Complex x) {
            return (Complex) f.map(x).negate();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ComplexFunction differentiate() {
            return new Negation(f.differentiate());
        }

        //public ComplexFunction integrate() {
        //        return new Negation(f.integrate());
        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Sum extends ComplexFunction implements C1Function {
        /** DOCUMENT ME! */
        private final ComplexFunction f1;

        /** DOCUMENT ME! */
        private final ComplexFunction f2;

/**
         * Creates a new Sum object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Sum(ComplexFunction f1, ComplexFunction f2) {
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
        public Complex map(Complex x) {
            return f1.map(x).add(f2.map(x));
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ComplexFunction differentiate() {
            return new Sum(f1.differentiate(), f2.differentiate());
        }

        //public ComplexFunction integrate() {
        //        return new Sum(f1.integrate(), f2.integrate());
        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Difference extends ComplexFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final ComplexFunction f1;

        /** DOCUMENT ME! */
        private final ComplexFunction f2;

/**
         * Creates a new Difference object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Difference(ComplexFunction f1, ComplexFunction f2) {
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
        public Complex map(Complex x) {
            return f1.map(x).subtract(f2.map(x));
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ComplexFunction differentiate() {
            return new Difference(f1.differentiate(), f2.differentiate());
        }

        //public ComplexFunction integrate() {
        //        return new Difference(f1.integrate(), f2.integrate());
        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Product extends ComplexFunction implements C1Function {
        /** DOCUMENT ME! */
        private final ComplexFunction f1;

        /** DOCUMENT ME! */
        private final ComplexFunction f2;

/**
         * Creates a new Product object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Product(ComplexFunction f1, ComplexFunction f2) {
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
        public Complex map(Complex x) {
            return f1.map(x).multiply(f2.map(x));
        }

        /**
         * Product rule.
         *
         * @return DOCUMENT ME!
         */
        public ComplexFunction differentiate() {
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
    private static class Inversion extends ComplexFunction implements C1Function {
        /** DOCUMENT ME! */
        private final ComplexFunction f;

/**
         * Creates a new Inversion object.
         *
         * @param f DOCUMENT ME!
         */
        public Inversion(ComplexFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Complex map(Complex x) {
            return (Complex) f.map(x).inverse();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ComplexFunction differentiate() {
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
    private static class Division extends ComplexFunction implements C1Function {
        /** DOCUMENT ME! */
        private final ComplexFunction f1;

        /** DOCUMENT ME! */
        private final ComplexFunction f2;

/**
         * Creates a new Division object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Division(ComplexFunction f1, ComplexFunction f2) {
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
        public Complex map(Complex x) {
            return f1.map(x).divide(f2.map(x));
        }

        /**
         * Division rule.
         *
         * @return DOCUMENT ME!
         */
        public ComplexFunction differentiate() {
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
    private static class Power extends ComplexFunction implements C1Function {
        /** DOCUMENT ME! */
        private final ComplexFunction f;

        /** DOCUMENT ME! */
        private final double d;

/**
         * Creates a new Power object.
         *
         * @param f DOCUMENT ME!
         * @param d DOCUMENT ME!
         */
        public Power(ComplexFunction f, double d) {
            this.f = f;
            this.d = d;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Complex map(Complex x) {
            return f.map(x).pow(d);
        }

        /**
         * Power rule.
         *
         * @return DOCUMENT ME!
         */
        public ComplexFunction differentiate() {
            return new Product(f.differentiate(),
                new Product(new ConstantComplexFunction(new Complex(d)),
                    new Power(f, d - 1)));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Cosine extends ComplexFunction implements C1Function {
        /** DOCUMENT ME! */
        private final ComplexFunction f;

/**
         * Creates a new Cosine object.
         *
         * @param f DOCUMENT ME!
         */
        public Cosine(ComplexFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Complex map(Complex x) {
            return Complex.cos(f.map(x));
        }

        /**
         * Cosine rule.
         *
         * @return DOCUMENT ME!
         */
        public ComplexFunction differentiate() {
            return (ComplexFunction) new Product(f.differentiate(), new Sine(f)).negate();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Sine extends ComplexFunction implements C1Function {
        /** DOCUMENT ME! */
        private final ComplexFunction f;

/**
         * Creates a new Sine object.
         *
         * @param f DOCUMENT ME!
         */
        public Sine(ComplexFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Complex map(Complex x) {
            return Complex.sin(f.map(x));
        }

        /**
         * Sine rule.
         *
         * @return DOCUMENT ME!
         */
        public ComplexFunction differentiate() {
            return new Product(f.differentiate(), new Cosine(f));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Logarithm extends ComplexFunction implements C1Function {
        /** DOCUMENT ME! */
        private final ComplexFunction f;

/**
         * Creates a new Logarithm object.
         *
         * @param f DOCUMENT ME!
         */
        public Logarithm(ComplexFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Complex map(Complex x) {
            return Complex.log(f.map(x));
        }

        /**
         * Log rule.
         *
         * @return DOCUMENT ME!
         */
        public ComplexFunction differentiate() {
            return new Product((ComplexFunction) f.inverse(), f.differentiate());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Exponential extends ComplexFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final ComplexFunction f;

/**
         * Creates a new Exponential object.
         *
         * @param f DOCUMENT ME!
         */
        public Exponential(ComplexFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Complex map(Complex x) {
            return Complex.exp(f.map(x));
        }

        /**
         * Exp rule.
         *
         * @return DOCUMENT ME!
         */
        public ComplexFunction differentiate() {
            return new Product(new Exponential(f), f.differentiate());
        }
    }
}
