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

package org.jscience.mathematics.algebraic.numbers;

import org.jscience.mathematics.algebraic.fields.ExactRationalField;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The ExactRational class encapsulates rational numbers with infinite precision. Theyt are inbetween ExactInteger and ExactReal.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 * @see org.jscience.mathematics.algebraic.fields.RationalField
 */
//candidates for implementation
//mod
//When building the Javadoc for this package you should hide the BigRational class
//may be this class should not be defined using BigRational but rather using hand made code as the quotient of two ExactIntegers, and we would benefit additional support for the infinite bounds
public final class ExactRational extends ComparableNumber<ExactRational> implements Cloneable, Serializable,
        Field.Member {

    /**
     * The exact rational representing the additive identity.
     */
    public static final ExactRational ZERO = ExactRationalField.ZERO;

    /**
     * The exact rational representing the multiplicative identity.
     */
    public static final ExactRational ONE = ExactRationalField.ONE;

    public static final ExactRational NEGATIVE_INFINITY = new ExactRational(true, false);

    public static final ExactRational POSITIVE_INFINITY = new ExactRational(false, true);

    public static final ExactRational NaN = new ExactRational(true, true);

    /**
     * The Class instance representing the type.
     */
    public static final Class TYPE = ZERO.getClass();

    private static final long serialVersionUID = 861668031909L;

    private final BigRational x;

    private final boolean neg;

    private final boolean pos;

    private final static int DEFAULT_PRECISION = 128;

    /**
     * Constructs a ExactRational number.
     */
    private ExactRational(final boolean neg, final boolean pos) {
        x = new BigRational(0);
        this.neg = neg;
        this.pos = pos;
    }

    /**
     * Constructs a ExactRational number.
     */
//WARNING: The BigRational class is not expected to be part of the public API, so the protected modifier
    protected ExactRational(final BigRational num) {
        x = num;
        this.neg = false;
        this.pos = false;
    }

    /**
     * Constructs a ExactRational number.
     */
    public ExactRational(final ExactRational num) {
        x = num.x;
        neg = num.neg;
        pos = num.pos;
    }

    /**
     * Constructs a ExactRational number.
     */
    public ExactRational(final ExactInteger num, final ExactInteger den) {
        if (num.equals(ExactInteger.NaN) || den.equals(ExactInteger.NaN)) {
            x = new BigRational(0);
            neg = true;
            pos = true;
        } else {
            if (num.equals(ExactInteger.NEGATIVE_INFINITY)) {
                if (den.equals(ExactInteger.NEGATIVE_INFINITY)) {
                    x = new BigRational(0);
                    neg = true;
                    pos = true;
                } else {
                    if (den.equals(ExactInteger.POSITIVE_INFINITY)) {
                        x = new BigRational(0);
                        neg = true;
                        pos = true;
                    } else {
                        if (den.equals(0L)) {
                            x = new BigRational(0);
                            neg = true;
                            pos = true;
                        } else {
                            x = new BigRational(0);
                            neg = true;
                            pos = false;
                        }
                    }
                }
            } else {
                if (num.equals(ExactInteger.POSITIVE_INFINITY)) {
                    if (den.equals(ExactInteger.NEGATIVE_INFINITY)) {
                        x = new BigRational(0);
                        neg = true;
                        pos = true;
                    } else {
                        if (den.equals(ExactInteger.POSITIVE_INFINITY)) {
                            x = new BigRational(0);
                            neg = true;
                            pos = true;
                        } else {
                            if (den.equals(ExactInteger.ZERO)) {
                                x = new BigRational(0);
                                neg = true;
                                pos = true;
                            } else {
                                x = new BigRational(0);
                                neg = false;
                                pos = true;
                            }
                        }
                    }
                } else {
                    if (num.equals(ExactInteger.ZERO)) {
                        if (den.equals(ExactInteger.NEGATIVE_INFINITY)) {
                            x = new BigRational(0);
                            neg = false;
                            pos = false;
                        } else {
                            if (den.equals(ExactInteger.POSITIVE_INFINITY)) {
                                x = new BigRational(0);
                                neg = false;
                                pos = false;
                            } else {
                                if (den.equals(ExactInteger.ZERO)) {
                                    x = new BigRational(0);
                                    neg = true;
                                    pos = true;
                                } else {
                                    x = new BigRational(0);
                                    neg = false;
                                    pos = false;
                                }
                            }
                        }
                    } else {
                        if (den.equals(ExactInteger.NEGATIVE_INFINITY)) {
                            x = new BigRational(0);
                            neg = false;
                            pos = false;
                        } else {
                            if (den.equals(ExactInteger.POSITIVE_INFINITY)) {
                                x = new BigRational(0);
                                neg = false;
                                pos = false;
                            } else {
                                if (den.equals(ExactInteger.ZERO)) {
                                    x = new BigRational(0);
                                    neg = true;
                                    pos = true;
                                } else {
                                    x = new BigRational(num.value(), den.value());
                                    neg = false;
                                    pos = false;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Constructs a ExactRational number.
     */
    public ExactRational(final ExactInteger num) {
        if (num.equals(ExactInteger.NEGATIVE_INFINITY)) {
            x = new BigRational(0);
            neg = true;
            pos = false;
        } else if (num.equals(ExactInteger.POSITIVE_INFINITY)) {
            x = new BigRational(0);
            neg = false;
            pos = true;
        } else if (num.equals(ExactInteger.NaN)) {
            x = new BigRational(0);
            neg = true;
            pos = true;
        } else {
            x = new BigRational(num.value());
            neg = false;
            pos = false;
        }
    }

    /**
     * Constructs a ExactRational number.
     */
    public ExactRational(final Long num, final Long den) {
        if (num.equals(Long.NEGATIVE_INFINITY)) {
            if (den.equals(Long.NEGATIVE_INFINITY)) {
                x = new BigRational(0);
                neg = true;
                pos = true;
            } else {
                if (den.equals(Long.POSITIVE_INFINITY)) {
                    x = new BigRational(0);
                    neg = true;
                    pos = true;
                } else {
                    if (den.equals(0L)) {
                        x = new BigRational(0);
                        neg = true;
                        pos = true;
                    } else {
                        x = new BigRational(0);
                        neg = true;
                        pos = false;
                    }
                }
            }
        } else {
            if (num.equals(Long.POSITIVE_INFINITY)) {
                if (den.equals(Long.NEGATIVE_INFINITY)) {
                    x = new BigRational(0);
                    neg = true;
                    pos = true;
                } else {
                    if (den.equals(Long.POSITIVE_INFINITY)) {
                        x = new BigRational(0);
                        neg = true;
                        pos = true;
                    } else {
                        if (den.equals(0L)) {
                            x = new BigRational(0);
                            neg = true;
                            pos = true;
                        } else {
                            x = new BigRational(0);
                            neg = false;
                            pos = true;
                        }
                    }
                }
            } else {
                if (num.equals(0L)) {
                    if (den.equals(Long.NEGATIVE_INFINITY)) {
                        x = new BigRational(0);
                        neg = false;
                        pos = false;
                    } else {
                        if (den.equals(Long.POSITIVE_INFINITY)) {
                            x = new BigRational(0);
                            neg = false;
                            pos = false;
                        } else {
                            if (den.equals(0L)) {
                                x = new BigRational(0);
                                neg = true;
                                pos = true;
                            } else {
                                x = new BigRational(0);
                                neg = false;
                                pos = false;
                            }
                        }
                    }
                } else {
                    if (den.equals(Long.NEGATIVE_INFINITY)) {
                        x = new BigRational(0);
                        neg = false;
                        pos = false;
                    } else {
                        if (den.equals(Long.POSITIVE_INFINITY)) {
                            x = new BigRational(0);
                            neg = false;
                            pos = false;
                        } else {
                            if (den.equals(0L)) {
                                x = new BigRational(0);
                                neg = true;
                                pos = true;
                            } else {
                                x = new BigRational(num.longValue(), den.longValue());
                                neg = false;
                                pos = false;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Constructs a ExactRational number.
     */
    public ExactRational(final Long num) {
        if (num == null) {
            x = new BigRational(0);
            neg = true;
            pos = true;
        } else if (num.equals(Long.NEGATIVE_INFINITY)) {
            x = new BigRational(0);
            neg = true;
            pos = false;
        } else if (num.equals(Long.POSITIVE_INFINITY)) {
            x = new BigRational(0);
            neg = false;
            pos = true;
        } else {
            x = new BigRational(num.value());
            neg = false;
            pos = false;
        }
    }

    /**
     * Constructs a ExactRational number.
     */
    public ExactRational(final Rational num) {
        if (num == null) {
            x = new BigRational(0);
            neg = true;
            pos = true;
        } else if (num.equals(Rational.NEGATIVE_INFINITY)) {
            x = new BigRational(0);
            neg = true;
            pos = false;
        } else if (num.equals(Rational.POSITIVE_INFINITY)) {
            x = new BigRational(0);
            neg = false;
            pos = true;
        } else {
            x = new BigRational(num.getNumerator(), num.getDenominator());
            neg = false;
            pos = false;
        }
    }

    /**
     * Constructs a ExactRational number.
     */
    public ExactRational(final Integer num) {
        if (num == null) {
            x = new BigRational(0);
            neg = true;
            pos = true;
        } else if (num.equals(Integer.NEGATIVE_INFINITY)) {
            x = new BigRational(0);
            neg = true;
            pos = false;
        } else if (num.equals(Integer.POSITIVE_INFINITY)) {
            x = new BigRational(0);
            neg = false;
            pos = true;
        } else {
            x = new BigRational(num.value());
            neg = false;
            pos = false;
        }
    }

    /**
     * Constructs a ExactRational number.
     */
    public ExactRational(final long num, final long den) {
        if (num == Long.NEGATIVE_INFINITY) {
            if (den == Long.NEGATIVE_INFINITY) {
                x = new BigRational(0);
                neg = true;
                pos = true;
            } else {
                if (den == Long.POSITIVE_INFINITY) {
                    x = new BigRational(0);
                    neg = true;
                    pos = true;
                } else {
                    if (den == 0L) {
                        x = new BigRational(0);
                        neg = true;
                        pos = true;
                    } else {
                        x = new BigRational(0);
                        neg = true;
                        pos = false;
                    }
                }
            }
        } else {
            if (num == Long.POSITIVE_INFINITY) {
                if (den == Long.NEGATIVE_INFINITY) {
                    x = new BigRational(0);
                    neg = true;
                    pos = true;
                } else {
                    if (den == Long.POSITIVE_INFINITY) {
                        x = new BigRational(0);
                        neg = true;
                        pos = true;
                    } else {
                        if (den == 0L) {
                            x = new BigRational(0);
                            neg = true;
                            pos = true;
                        } else {
                            x = new BigRational(0);
                            neg = false;
                            pos = true;
                        }
                    }
                }
            } else {
                if (num == 0L) {
                    if (den == Long.NEGATIVE_INFINITY) {
                        x = new BigRational(0);
                        neg = false;
                        pos = false;
                    } else {
                        if (den == Long.POSITIVE_INFINITY) {
                            x = new BigRational(0);
                            neg = false;
                            pos = false;
                        } else {
                            if (den == 0L) {
                                x = new BigRational(0);
                                neg = true;
                                pos = true;
                            } else {
                                x = new BigRational(0);
                                neg = false;
                                pos = false;
                            }
                        }
                    }
                } else {
                    if (den == Long.NEGATIVE_INFINITY) {
                        x = new BigRational(0);
                        neg = false;
                        pos = false;
                    } else {
                        if (den == Long.POSITIVE_INFINITY) {
                            x = new BigRational(0);
                            neg = false;
                            pos = false;
                        } else {
                            if (den == 0L) {
                                x = new BigRational(0);
                                neg = true;
                                pos = true;
                            } else {
                                x = new BigRational(num, den);
                                neg = false;
                                pos = false;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Constructs a ExactRational number.
     */
    public ExactRational(final long num) {
        if (num == Long.NEGATIVE_INFINITY) {
            x = new BigRational(0);
            neg = true;
            pos = false;
        } else {
            if (num == Long.POSITIVE_INFINITY) {
                x = new BigRational(0);
                neg = false;
                pos = true;
            } else {
                //if (num==Long.NaN) {
                //    x = new BigRational(0);
                //    neg = true;
                //    pos = true;
                //} else {
                x = new BigRational(num);
                neg = false;
                pos = false;
                //}
            }
        }
    }

    /**
     * Constructs a ExactRational number.
     */
    public ExactRational(final int num) {
        if (num == Integer.NEGATIVE_INFINITY) {
            x = new BigRational(0);
            neg = true;
            pos = false;
        } else {
            if (num == Integer.POSITIVE_INFINITY) {
                x = new BigRational(0);
                neg = false;
                pos = true;
            } else {
                //if (num==Integer.NaN) {
                //    x = new BigRational(0);
                //    neg = true;
                //    pos = true;
                //} else {
                x = new BigRational(num);
                neg = false;
                pos = false;
                //}
            }
        }
    }

    /**
     * Constructs the ExactRational number represented by a string.
     *
     * @param s a string representing a ExactRational number.
     * @throws NumberFormatException if the string does not contain a parsable number.
     */
    public ExactRational(final String s) throws NumberFormatException {
        if (s.equals("NaN")) {
            x = new BigRational(0);
            neg = true;
            pos = true;
        } else {
            if (s.equals("-Infinity")) {
                x = new BigRational(0);
                neg = true;
                pos = false;
            } else {
                if (s.equals("Infinity")) {
                    x = new BigRational(0);
                    neg = false;
                    pos = true;
                } else {
                    x = new BigRational(s);
                    neg = false;
                    pos = false;
                }
            }
        }
    }

    /**
     * Retrieves the integer part as a ExactInteger.
     */
    public ExactInteger integerPart() {
        return new ExactInteger(x.integerPart().bigIntegerValue());
    }

    /**
     * Retrieves the integer part as a ExactInteger.
     */
    public ExactInteger fractionalPart() {
        return new ExactInteger(x.fractionalPart().bigIntegerValue());
    }

    /**
     * Retrieves the numerator as a ExactInteger.
     */
    public ExactInteger getNumerator() {
        return new ExactInteger(x.getNumerator());
    }

    /**
     * Retrieves the denominator as a ExactReal.
     */
    public ExactInteger getDenominator() {
        return new ExactInteger(x.getDenominator());
    }

    /**
     * Compares two ExactRational numbers for equality.
     *
     * @param obj a ExactRational number.
     */
    public boolean equals(Object obj) {
        if (obj instanceof ExactRational) {
            return x.equals(((ExactRational) obj).x) && (neg == ((ExactRational) obj).neg) && (pos == ((ExactRational) obj).pos);
        } else {
            return false;
        }
    }

    /**
     * Compares two numbers.
     *
     * @param obj a comparable number.
     * @return a negative value if <code>this&lt;obj</code>,
     *         zero if <code>this==obj</code>,
     *         and a positive value if <code>this&gt;obj</code>.
     */
    public int compareTo(ComparableNumber obj) throws IllegalArgumentException {
        if (obj instanceof ExactRational) {
            return compareTo((ExactRational) obj);
        } else if (obj instanceof ExactInteger) {
            return compareTo(new ExactRational((ExactInteger) obj));
        } else if (obj instanceof Rational) {
            return compareTo(new ExactRational((Rational) obj));
        } else if (obj instanceof Long) {
            return compareTo(new ExactRational((Long) obj));
        } else if (obj instanceof Integer) {
            return compareTo(new ExactRational((Integer) obj));
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Compares two ExactRational numbers.
     *
     * @param value a ExactRational number.
     * @return a negative value if <code>this&lt;obj</code>,
     *         zero if <code>this==obj</code>,
     *         and a positive value if <code>this&gt;obj</code>.
     */
    public int compareTo(ExactRational value) {
        if (value.neg) {
            if (value.pos) {//value.NaN
                if (neg) {
                    if (pos) {
                        return 0;
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }
            } else {//value.NEGATIVE_INFINITY
                if (neg) {
                    if (pos) {
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    return 1;
                }
            }
        } else {
            if (value.pos) {//value.POSITIVE_INFINITY
                if (neg) {
                    if (pos) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    if (pos) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            } else {//value.Number
                if (neg) {
                    if (pos) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    if (pos) {
                        return 1;
                    } else {
                        return x.compareTo(((ExactRational) value).x);
                    }
                }
            }
        }
    }

    /**
     * Returns a string representing the value of this ExactRational number.
     */
    public String toString() {
        if (isInfinite()) {
            return "Infinity";
        } else {
            return x.toString();
        }
    }

    public BigDecimal value() {
        return x.bigDecimalValue(DEFAULT_PRECISION);
    }

    //returns an exception if this equals to ExactRational.NaN, ExactRational.NEGATIVE_INFINITY or ExactRational.POSITIVE_INFINITY
    //Alas, we don't have any way to give a meaningful result but throwing an arithmetic exception
    public int intValue() {
        if (neg) {
            if (pos) {
                throw new ArithmeticException("Can't cast a non numeric value.");
            } else {
                throw new ArithmeticException("Can't cast a non numeric value.");
            }
        } else {
            if (pos) {
                throw new ArithmeticException("Can't cast a non numeric value.");
            } else {
                return x.intValue();
            }
        }
    }

    //returns an exception if this equals to ExactRational.NaN, ExactRational.NEGATIVE_INFINITY or ExactRational.POSITIVE_INFINITY
    //Alas, we don't have any way to give a meaningful result but throwing an arithmetic exception
    public long longValue() {
        if (neg) {
            if (pos) {
                throw new ArithmeticException("Can't cast a non numeric value.");
            } else {
                throw new ArithmeticException("Can't cast a non numeric value.");
            }
        } else {
            if (pos) {
                throw new ArithmeticException("Can't cast a non numeric value.");
            } else {
                return x.longValue();
            }
        }
    }

    public float floatValue() {
        if (neg) {
            if (pos) {
                return Float.NaN;
            } else {
                return Float.NEGATIVE_INFINITY;
            }
        } else {
            if (pos) {
                return Float.POSITIVE_INFINITY;
            } else {
                return x.floatValue();
            }
        }
    }

    public Rational rationalValue() {
        return new Rational(getNumerator().longValue(), getDenominator().longValue());
    }

    public double doubleValue() {
        if (neg) {
            if (pos) {
                return java.lang.Double.NaN;
            } else {
                return java.lang.Double.NEGATIVE_INFINITY;
            }
        } else {
            if (pos) {
                return java.lang.Double.POSITIVE_INFINITY;
            } else {
                return x.doubleValue();
            }
        }
    }

    /**
     * Returns the abs of this number.
     */
    public ExactRational abs() {
        if (neg) {
            if (pos) {
                return ExactRational.NaN;
            } else {
                return ExactRational.POSITIVE_INFINITY;
            }
        } else {
            if (pos) {
                return ExactRational.POSITIVE_INFINITY;
            } else {
                return new ExactRational(x.abs());
            }
        }
    }

    /**
     * Returns the sign of this number.
     */
    public int signum() {
        if (neg) {
            if (pos) {
                throw new ArithmeticException("Can't get the sign of a NaN value.");
            } else {
                return -1;
            }
        } else {
            if (pos) {
                return 1;
            } else {
                return x.signum();
            }
        }
    }

    /**
     * Returns the min of this number and another, according to compareTo rules.
     */
    public ExactRational min(ExactRational val) {
        if (compareTo(val) == -1) {
            return this;
        } else {
            return val;
        }
    }

    /**
     * Returns the max of this number and another, according to compareTo rules.
     */
    public ExactRational max(ExactRational val) {
        if (compareTo(val) == -1) {
            return val;
        } else {
            return this;
        }
    }

    /**
     * Returns the hashcode of this number.
     */
    public int hashCode() {
        return x.hashCode();
    }

    /**
     * Returns true if this number is NaN.
     */
    public boolean isNaN() {
        return pos & neg;
    }

    public boolean isDefined() {
        return !isInfinite();
    }

    /**
     * Returns true if this number is infinite.
     */
    public boolean isInfinite() {
        return pos ^ neg;
    }

    public boolean isNegativeInfinity() {
        return neg & !pos;
    }

    public boolean isPositiveInfinity() {
        return pos & !neg;
    }

    public ExactRational getNaN() {
        return ExactRational.NaN;
    }

    public ExactRational getNegativeInfinity() {
        return ExactRational.NEGATIVE_INFINITY;
    }

    public ExactRational getPositiveInfinity() {
        return ExactRational.POSITIVE_INFINITY;
    }

    //also look at Rational.isInfinite(), Rational.isQuasiZero(), and Rational.isQuasiNaN

    public ExactRational getDistance(ComparableNumber n) {
        if (n instanceof ExactRational) {
            return new ExactRational((ExactRational) n).subtract(this);
        } else if (n instanceof ExactInteger) {
            return new ExactRational((ExactInteger) n).subtract(this);
        } else if (n instanceof Rational) {
            return new ExactRational((Rational) n).subtract(this);
        } else if (n instanceof Long) {
            return new ExactRational((Long) n).subtract(this);
        } else if (n instanceof Integer) {
            return new ExactRational((Integer) n).subtract(this);
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the negative of this number.
     */
    public AbelianGroup.Member negate() {
        if (neg) {
            if (pos) {
                return ExactRational.NaN;
            } else {
                return ExactRational.POSITIVE_INFINITY;
            }
        } else {
            if (pos) {
                return ExactRational.NEGATIVE_INFINITY;
            } else {
                return new ExactRational(x.negate());
            }
        }
    }

    /**
     * Returns the inverse of this number.
     */
    public Field.Member inverse() {
        if (neg) {
            if (pos) {
                return ExactRational.NaN;
            } else {
                return ExactRational.ZERO;
            }
        } else {
            if (pos) {
                return ExactRational.ZERO;
            } else {
                return new ExactRational(x.pow(-1));
            }
        }
    }

    /**
     * Returns the addition of this number and another.
     */
    public AbelianGroup.Member add(final AbelianGroup.Member n) {
        if (n instanceof ExactRational) {
            return add((ExactRational) n);
        } else if (n instanceof Integer) {
            return add(new ExactRational(((Integer) n)));
        } else if (n instanceof Long) {
            return add(new ExactRational(((Long) n)));
        } else if (n instanceof Rational) {
            return add(new ExactRational(((Rational) n)));
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this ExactRational number and another.
     */
    public ExactRational add(final ExactRational n) {
        if (neg) {
            if (pos) {//NaN
                return ExactRational.NaN;
            } else {//NEGATIVE_INFINITY
                if (n.neg) {
                    if (n.pos) {
                        return ExactRational.NaN;
                    } else {
                        return ExactRational.NEGATIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactRational.NaN;
                    } else {
                        return ExactRational.NEGATIVE_INFINITY;
                    }
                }
            }
        } else {
            if (pos) {//POSITIVE_INFINITY
                if (n.neg) {
                    return ExactRational.NaN;
                } else {
                    return ExactRational.POSITIVE_INFINITY;
                }
            } else {//Number
                if (n.neg) {
                    if (n.pos) {
                        return ExactRational.NaN;
                    } else {
                        return ExactRational.NEGATIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactRational.POSITIVE_INFINITY;
                    } else {
                        return new ExactRational(x.add(n.x));
                    }
                }
            }
        }
    }

    /**
     * Returns the subtraction of this number and another.
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member n) {
        if (n instanceof ExactRational) {
            return subtract((ExactRational) n);
        } else if (n instanceof Integer) {
            return subtract(new ExactRational(((Integer) n)));
        } else if (n instanceof Long) {
            return subtract(new ExactRational(((Long) n)));
        } else if (n instanceof Rational) {
            return subtract(new ExactRational(((Rational) n)));
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this ExactRational number and another.
     */
    public ExactRational subtract(final ExactRational n) {
        if (neg) {
            if (pos) {//NaN
                return ExactRational.NaN;
            } else {//NEGATIVE_INFINITY
                if (n.neg) {
                    return ExactRational.NaN;
                } else {
                    return ExactRational.NEGATIVE_INFINITY;
                }
            }
        } else {
            if (pos) {//POSITIVE_INFINITY
                if (n.neg) {
                    if (n.pos) {
                        return ExactRational.NaN;
                    } else {
                        return ExactRational.POSITIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactRational.NaN;
                    } else {
                        return ExactRational.POSITIVE_INFINITY;
                    }
                }
            } else {//Number
                if (n.neg) {
                    if (n.pos) {
                        return ExactRational.NaN;
                    } else {
                        return ExactRational.POSITIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactRational.NEGATIVE_INFINITY;
                    } else {
                        return new ExactRational(x.subtract(n.x));
                    }
                }
            }
        }
    }

    /**
     * Returns the multiplication of this number and another.
     */
    public Ring.Member multiply(final Ring.Member n) {
        if (n instanceof ExactRational) {
            return multiply((ExactRational) n);
        } else if (n instanceof Integer) {
            return multiply(new ExactRational(((Integer) n)));
        } else if (n instanceof Long) {
            return multiply(new ExactRational(((Long) n)));
        } else if (n instanceof Rational) {
            return multiply(new ExactRational(((Rational) n)));
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the multiplication of this ExactRational number and another.
     */
    public ExactRational multiply(final ExactRational n) {
        if (neg) {
            if (pos) {//NaN
                return ExactRational.NaN;
            } else {//NEGATIVE_INFINITY
                if (n.neg) {
                    if (n.pos) {
                        return ExactRational.NaN;
                    } else {
                        return ExactRational.POSITIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactRational.NEGATIVE_INFINITY;
                    } else {
                        if (n.signum() == 1) {
                            return ExactRational.NEGATIVE_INFINITY;
                        } else {
                            if (n.signum() == -1) {
                                return ExactRational.POSITIVE_INFINITY;
                            } else {
                                return ExactRational.NaN;
                            }
                        }
                    }
                }
            }
        } else {
            if (pos) {//POSITIVE_INFINITY
                if (n.neg) {
                    if (n.pos) {
                        return ExactRational.NaN;
                    } else {
                        return ExactRational.NEGATIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactRational.POSITIVE_INFINITY;
                    } else {
                        if (n.signum() == 1) {
                            return ExactRational.POSITIVE_INFINITY;
                        } else {
                            if (n.signum() == -1) {
                                return ExactRational.NEGATIVE_INFINITY;
                            } else {
                                return ExactRational.NaN;
                            }
                        }
                    }
                }
            } else {//Number
                if (n.neg) {
                    if (n.pos) {
                        return ExactRational.NaN;
                    } else {
                        if (signum() == 1) {
                            return ExactRational.NEGATIVE_INFINITY;
                        } else {
                            if (signum() == -1) {
                                return ExactRational.POSITIVE_INFINITY;
                            } else {
                                return ExactRational.NaN;
                            }
                        }
                    }
                } else {
                    if (n.pos) {
                        if (signum() == 1) {
                            return ExactRational.POSITIVE_INFINITY;
                        } else {
                            if (signum() == -1) {
                                return ExactRational.NEGATIVE_INFINITY;
                            } else {
                                return ExactRational.NaN;
                            }
                        }
                    } else {
                        return new ExactRational(x.multiply(n.x));
                    }
                }
            }
        }
    }

    /**
     * Returns the division of this number and another.
     */
    public Field.Member divide(final Field.Member n) {
        if (n instanceof ExactRational) {
            return divide((ExactRational) n);
        }
        //Wouldn't it be nice if we could divide by Ring.Member instead ?
        //else if (n instanceof ExactInteger)
        //    return divide(new ExactRational((ExactInteger)n));
        //else if (n instanceof Integer)
        //    return divide(new ExactRational(((Integer) n)));
        //else if (n instanceof Long)
        //    return divide(new ExactRational(((Long) n)));
        else if (n instanceof Rational) {
            return divide(new ExactRational(((Rational) n)));
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the division of this ExactRational number and another.
     */
    public ExactRational divide(final ExactRational n) {
        if (neg) {
            if (pos) {//NaN
                return ExactRational.NaN;
            } else {//NEGATIVE_INFINITY
                if (n.neg) {
                    return ExactRational.NaN;
                } else {
                    if (n.pos) {
                        return ExactRational.NaN;
                    } else {
                        if (n.signum() == 1) {
                            return ExactRational.NEGATIVE_INFINITY;
                        } else {
                            if (n.signum() == -1) {
                                return ExactRational.POSITIVE_INFINITY;
                            } else {
                                throw new ArithmeticException("Can't divide by zero.");
                            }
                        }
                    }
                }
            }
        } else {
            if (pos) {//POSITIVE_INFINITY
                if (n.neg) {
                    return ExactRational.NaN;
                } else {
                    if (n.pos) {
                        return ExactRational.NaN;
                    } else {
                        if (n.signum() == 1) {
                            return ExactRational.POSITIVE_INFINITY;
                        } else {
                            if (n.signum() == -1) {
                                return ExactRational.NEGATIVE_INFINITY;
                            } else {
                                throw new ArithmeticException("Can't divide by zero.");
                            }
                        }
                    }
                }
            } else {//Number
                if (n.neg) {
                    if (n.pos) {
                        return ExactRational.NaN;
                    } else {
                        if (signum() == 1) {
                            return ExactRational.NEGATIVE_INFINITY;
                        } else {
                            if (signum() == -1) {
                                return ExactRational.POSITIVE_INFINITY;
                            } else {
                                return ExactRational.ZERO;
                            }
                        }
                    }
                } else {
                    if (n.pos) {
                        if (signum() == 1) {
                            return ExactRational.POSITIVE_INFINITY;
                        } else {
                            if (signum() == -1) {
                                return ExactRational.NEGATIVE_INFINITY;
                            } else {
                                return ExactRational.ZERO;
                            }
                        }
                    } else {
                        return new ExactRational(x.divide(n.x));
                    }
                }
            }
        }
    }

//===========
// FUNCTIONS
//===========

    /**
     * Calculate the remainder of two ExactRationals and return a new ExactRational.
     * [Name: see class BigInteger.]
     * The remainder result may be negative.
     * The remainder is based on round down (towards zero) / truncate.
     * 5/3 == 1 + 2/3 (remainder 2), 5/-3 == -1 + 2/-3 (remainder 2),
     * -5/3 == -1 + -2/3 (remainder -2), -5/-3 == 1 + -2/-3 (remainder -2).
     */
    public ExactRational remainder(ExactRational that) {
        if (neg || pos || that.neg || that.pos) {
            throw new ArithmeticException("Can't compute remainder of a NaN or infinite value.");
        } else {
            if (that.signum() == 0) {
                return ExactRational.NaN;
            } else {
                return new ExactRational(x.remainder(that.x));
            }
        }
    }

    /**
     * Calculate the modulus of two ExactRationals and return a new ExactRational.
     * The modulus result may be negative.
     * Modulus is based on round floor (towards negative).
     * 5/3 == 1 + 2/3 (modulus 2), 5/-3 == -2 + -1/-3 (modulus -1),
     * -5/3 == -2 + 1/3 (modulus 1), -5/-3 == 1 + -2/-3 (modulus -2).
     */
    public ExactRational mod(ExactRational that) {
        if (neg || pos || that.neg || that.pos) {
            throw new ArithmeticException("Can't compute mod of a NaN or infinite value.");
        } else {
            if (that.signum() == 0) {
                return ExactRational.NaN;
            } else {
                return new ExactRational(x.mod(that.x));
            }
        }
    }

    /**
     * Rounding mode to round away from zero.
     */
    public final static int ROUND_UP = 0;

    /**
     * Rounding mode to round towards zero.
     */
    public final static int ROUND_DOWN = 1;

    /**
     * Rounding mode to round towards positive infinity.
     */
    public final static int ROUND_CEILING = 2;

    /**
     * Rounding mode to round towards negative infinity.
     */
    public final static int ROUND_FLOOR = 3;

    /**
     * Rounding mode to round towards nearest neighbor unless both
     * neighbors are equidistant, in which case to round up.
     */
    public final static int ROUND_HALF_UP = 4;

    /**
     * Rounding mode to round towards nearest neighbor unless both
     * neighbors are equidistant, in which case to round down.
     */
    public final static int ROUND_HALF_DOWN = 5;

    /**
     * Rounding mode to round towards the nearest neighbor unless both
     * neighbors are equidistant, in which case to round towards the even neighbor.
     */
    public final static int ROUND_HALF_EVEN = 6;

    /**
     * Rounding mode to assert that the requested operation has an exact
     * result, hence no rounding is necessary.
     * If this rounding mode is specified on an operation that yields an inexact result,
     * an ArithmeticException is thrown.
     */
    public final static int ROUND_UNNECESSARY = 7;

    /**
     * Rounding mode to round towards nearest neighbor unless both
     * neighbors are equidistant, in which case to round ceiling.
     */
    public final static int ROUND_HALF_CEILING = 8;

    /**
     * Rounding mode to round towards nearest neighbor unless both
     * neighbors are equidistant, in which case to round floor.
     */
    public final static int ROUND_HALF_FLOOR = 9;

    /**
     * Rounding mode to round towards the nearest neighbor unless both
     * neighbors are equidistant, in which case to round towards the odd neighbor.
     */
    public final static int ROUND_HALF_ODD = 10;

    /**
     * Default round mode, ROUND_HALF_UP.
     */
    public final static int DEFAULT_ROUND_MODE = ROUND_HALF_UP;

    /**
     * Round.
     */
    public ExactRational round(int roundMode) {
        if (neg || pos) {
            throw new ArithmeticException("Can't round a NaN or infinite value.");
        } else {
            return new ExactRational(x.round(roundMode));
        }
    }

    /**
     * Round by default mode.
     */
    public ExactRational round() {
        if (neg || pos) {
            throw new ArithmeticException("Can't round a NaN or infinite value.");
        } else {
            return new ExactRational(x.round());
        }
    }

    /**
     * Floor, round towards negative infinity.
     */
    public ExactRational floor() {
        if (neg || pos) {
            throw new ArithmeticException("Can't floor a NaN or infinite value.");
        } else {
            return new ExactRational(x.floor());
        }
    }

    /**
     * Ceiling, round towards positive infinity.
     */
    public ExactRational ceil() {
        if (neg || pos) {
            throw new ArithmeticException("Can't ceil a NaN or infinite value.");
        } else {
            return new ExactRational(x.ceil());
        }
    }

    /**
     * Truncate, round towards zero.
     */
    public ExactRational truncate() {
        if (neg || pos) {
            throw new ArithmeticException("Can't truncate a NaN or infinite value.");
        } else {
            return new ExactRational(x.truncate());
        }
    }

// EXP

    /**
     * Returns this number raised to the power of a number.
     */
    public ExactRational pow(final int i) {

        if (neg) {
            if (pos) {
                return ExactRational.NaN;
            } else {
                if (i == 0) {
                    return ExactRational.ONE;
                } else {
                    if (i > 0) {
                        if (i % 2 == 0) {
                            return ExactRational.POSITIVE_INFINITY;
                        } else {
                            return ExactRational.NEGATIVE_INFINITY;
                        }
                    } else {
                        return ExactRational.ZERO;
                    }
                }
            }
        } else {
            if (pos) {
                if (i == 0) {
                    return ExactRational.ONE;
                } else {
                    if (i > 0) {
                        return ExactRational.POSITIVE_INFINITY;
                    } else {
                        return ExactRational.ZERO;
                    }
                }
            } else {
                return new ExactRational(x.pow(i));
            }
        }

    }

    /**
     * // LOG
     * <p/>
     * /**
     * Returns the natural logarithm (base e) of a number.
     */
    //public static ExactRational log(final ExactRational x) {
    //}

// SIN

    /**
     * Returns the trigonometric sine of an angle.
     *
     * @param x an angle that is measured in radians
     */
    //public static ExactRational sin(final ExactRational x) {
    //}

// COS

    /**
     * Returns the trigonometric cosine of an angle.
     *
     * @param x an angle that is measured in radians
     */
    //public static ExactRational cos(final ExactRational x) {
    //}

// TAN

    /**
     * Returns the trigonometric tangent of an angle.
     *
     * @param x an angle that is measured in radians
     */
    //public static ExactRational tan(final ExactRational x) {
    //}

// SINH

    /**
     * Returns the hyperbolic sine of a number.
     */
    //public static ExactRational sinh(final ExactRational x) {
    //}

// COSH

    /**
     * Returns the hyperbolic cosine of a number.
     */
    //public static ExactRational cosh(final ExactRational x) {
    //}

// TANH

    /**
     * Returns the hyperbolic tangent of a number.
     */
    //public static ExactRational tanh(final ExactRational x) {
    //}

// INVERSE SIN

    /**
     * Returns the arc sine of a number.
     */
    //public static ExactRational asin(final ExactRational x) {
    //}

// INVERSE COS

    /**
     * Returns the arc cosine of a number.
     */
    //public static ExactRational acos(final ExactRational x) {
    //}

// INVERSE TAN

    /**
     * Returns the arc tangent of a number.
     */
    //public static ExactRational atan(final ExactRational x) {
    //}

// INVERSE SINH

    /**
     * Returns the arc hyperbolic sine of a number.
     */
    //public static ExactRational asinh(final ExactRational x) {
    //}

// INVERSE COSH

    /**
     * Returns the arc hyperbolic cosine of a number.
     */
    //public static ExactRational acosh(final ExactRational x) {
    //}

// INVERSE TANH

    /**
     * Returns the arc hyperbolic tangent of a number.
     */
    //public static ExactRational atanh(final ExactRational x) {
    //}
    public Object clone() {
        return new ExactRational(this);
    }

}

