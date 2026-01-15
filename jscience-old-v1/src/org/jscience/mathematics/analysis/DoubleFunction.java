package org.jscience.mathematics.analysis;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;


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
public abstract class DoubleFunction implements PrimitiveMapping, C1Function,
    Field.Member {
    //include support for domains
    /** DOCUMENT ME! */
    private IntervalsList intervalsList = new IntervalsList(new Interval(
                new Double(Double.NEGATIVE_INFINITY),
                new Double(Double.POSITIVE_INFINITY)));

    //be sure to check the domain (intervalsList) or throw an Exception
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract double map(double x);

    /**
     * Returns the differential of this function.
     *
     * @return DOCUMENT ME!
     */
    public abstract DoubleFunction differentiate();

    /**
     * Evaluates this function.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(float x) {
        return map((double) x);
    }

    /**
     * Evaluates this function.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(long x) {
        return map((double) x);
    }

    /**
     * Evaluates this function.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(int x) {
        return map((double) x);
    }

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
    public DoubleFunction compose(DoubleFunction f) {
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
        if (f instanceof DoubleFunction) {
            return add((DoubleFunction) f);
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
    public DoubleFunction add(DoubleFunction f) {
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
        if (f instanceof DoubleFunction) {
            return subtract((DoubleFunction) f);
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
    public DoubleFunction subtract(DoubleFunction f) {
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
        if (f instanceof DoubleFunction) {
            return multiply((DoubleFunction) f);
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
    public DoubleFunction multiply(DoubleFunction f) {
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
        if (f instanceof DoubleFunction) {
            return divide((DoubleFunction) f);
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
    public DoubleFunction divide(DoubleFunction f) {
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
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleFunction2D tensor(DoubleFunction f) {
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
    public DoublePolynomial taylorExpand(double a, int n) {
        double[] coeff = new double[n];
        coeff[0] = map(a);

        DoubleFunction diff = this;
        int factorial = 1;

        for (int i = 1; i < n; i++) {
            diff = diff.differentiate();
            factorial *= i;
            coeff[i] = diff.map(a) / factorial;
        }

        return new DoublePolynomial(coeff);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Composition extends DoubleFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final DoubleFunction f1;

        /** DOCUMENT ME! */
        private final DoubleFunction f2;

/**
         * Creates a new Composition object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Composition(DoubleFunction f1, DoubleFunction f2) {
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
        public double map(double x) {
            return f1.map(f2.map(x));
        }

        /**
         * Chain rule.
         *
         * @return DOCUMENT ME!
         */
        public DoubleFunction differentiate() {
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
    private static class Negation extends DoubleFunction implements C1Function {
        /** DOCUMENT ME! */
        private final DoubleFunction f;

/**
         * Creates a new Negation object.
         *
         * @param f DOCUMENT ME!
         */
        public Negation(DoubleFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double map(double x) {
            return -f.map(x);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public DoubleFunction differentiate() {
            return new Negation(f.differentiate());
        }

        //public DoubleFunction integrate() {
        //        return new Negation(f.integrate());
        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Sum extends DoubleFunction implements C1Function {
        /** DOCUMENT ME! */
        private final DoubleFunction f1;

        /** DOCUMENT ME! */
        private final DoubleFunction f2;

/**
         * Creates a new Sum object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Sum(DoubleFunction f1, DoubleFunction f2) {
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
        public double map(double x) {
            return f1.map(x) + f2.map(x);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public DoubleFunction differentiate() {
            return new Sum(f1.differentiate(), f2.differentiate());
        }

        //public DoubleFunction integrate() {
        //        return new Sum(f1.integrate(), f2.integrate());
        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Difference extends DoubleFunction implements C1Function {
        /** DOCUMENT ME! */
        private final DoubleFunction f1;

        /** DOCUMENT ME! */
        private final DoubleFunction f2;

/**
         * Creates a new Difference object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Difference(DoubleFunction f1, DoubleFunction f2) {
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
        public double map(double x) {
            return f1.map(x) - f2.map(x);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public DoubleFunction differentiate() {
            return new Difference(f1.differentiate(), f2.differentiate());
        }

        //public DoubleFunction integrate() {
        //        return new Difference(f1.integrate(), f2.integrate());
        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Product extends DoubleFunction implements C1Function {
        /** DOCUMENT ME! */
        private final DoubleFunction f1;

        /** DOCUMENT ME! */
        private final DoubleFunction f2;

/**
         * Creates a new Product object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Product(DoubleFunction f1, DoubleFunction f2) {
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
        public double map(double x) {
            return f1.map(x) * f2.map(x);
        }

        /**
         * Product rule.
         *
         * @return DOCUMENT ME!
         */
        public DoubleFunction differentiate() {
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
    private static class Inversion extends DoubleFunction implements C1Function {
        /** DOCUMENT ME! */
        private final DoubleFunction f;

/**
         * Creates a new Inversion object.
         *
         * @param f DOCUMENT ME!
         */
        public Inversion(DoubleFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double map(double x) {
            return 1 / f.map(x);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public DoubleFunction differentiate() {
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
    private static class Division extends DoubleFunction implements C1Function {
        /** DOCUMENT ME! */
        private final DoubleFunction f1;

        /** DOCUMENT ME! */
        private final DoubleFunction f2;

/**
         * Creates a new Division object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public Division(DoubleFunction f1, DoubleFunction f2) {
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
        public double map(double x) {
            return f1.map(x) / f2.map(x);
        }

        /**
         * Division rule.
         *
         * @return DOCUMENT ME!
         */
        public DoubleFunction differentiate() {
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
    private static class Power extends DoubleFunction implements C1Function {
        /** DOCUMENT ME! */
        private final DoubleFunction f;

        /** DOCUMENT ME! */
        private final double d;

/**
         * Creates a new Power object.
         *
         * @param f DOCUMENT ME!
         * @param d DOCUMENT ME!
         */
        public Power(DoubleFunction f, double d) {
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
        public double map(double x) {
            return Math.pow(f.map(x), d);
        }

        /**
         * Power rule.
         *
         * @return DOCUMENT ME!
         */
        public DoubleFunction differentiate() {
            return new Product(f.differentiate(),
                new Product(new ConstantDoubleFunction(d), new Power(f, d - 1)));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Cosine extends DoubleFunction implements C1Function {
        /** DOCUMENT ME! */
        private final DoubleFunction f;

/**
         * Creates a new Cosine object.
         *
         * @param f DOCUMENT ME!
         */
        public Cosine(DoubleFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double map(double x) {
            return Math.cos(f.map(x));
        }

        /**
         * Cosine rule.
         *
         * @return DOCUMENT ME!
         */
        public DoubleFunction differentiate() {
            return (DoubleFunction) new Product(f.differentiate(), new Sine(f)).negate();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Sine extends DoubleFunction implements C1Function {
        /** DOCUMENT ME! */
        private final DoubleFunction f;

/**
         * Creates a new Sine object.
         *
         * @param f DOCUMENT ME!
         */
        public Sine(DoubleFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double map(double x) {
            return Math.sin(f.map(x));
        }

        /**
         * Sine rule.
         *
         * @return DOCUMENT ME!
         */
        public DoubleFunction differentiate() {
            return new Product(f.differentiate(), new Cosine(f));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Logarithm extends DoubleFunction implements C1Function {
        /** DOCUMENT ME! */
        private final DoubleFunction f;

/**
         * Creates a new Logarithm object.
         *
         * @param f DOCUMENT ME!
         */
        public Logarithm(DoubleFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double map(double x) {
            return Math.log(f.map(x));
        }

        /**
         * Log rule.
         *
         * @return DOCUMENT ME!
         */
        public DoubleFunction differentiate() {
            return new Product((DoubleFunction) f.inverse(), f.differentiate());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class Exponential extends DoubleFunction
        implements C1Function {
        /** DOCUMENT ME! */
        private final DoubleFunction f;

/**
         * Creates a new Exponential object.
         *
         * @param f DOCUMENT ME!
         */
        public Exponential(DoubleFunction f) {
            this.f = f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double map(double x) {
            return Math.exp(f.map(x));
        }

        /**
         * Exp rule.
         *
         * @return DOCUMENT ME!
         */
        public DoubleFunction differentiate() {
            return new Product(new Exponential(f), f.differentiate());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class TensorProduct extends DoubleFunction2D {
        /** DOCUMENT ME! */
        private final DoubleFunction f1;

        /** DOCUMENT ME! */
        private final DoubleFunction f2;

/**
         * Creates a new TensorProduct object.
         *
         * @param f1 DOCUMENT ME!
         * @param f2 DOCUMENT ME!
         */
        public TensorProduct(DoubleFunction f1, DoubleFunction f2) {
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
            return f1.map(x) * f2.map(y);
        }
    }
}
