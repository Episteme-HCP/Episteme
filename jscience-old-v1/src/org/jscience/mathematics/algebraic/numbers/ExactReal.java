package org.jscience.mathematics.algebraic.numbers;

import org.jscience.mathematics.algebraic.fields.ExactRealField;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The ExactReal class encapsulates real numbers with infinite precision but slower speed than Float or Double.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 * @see org.jscience.mathematics.algebraic.fields.ExactRealField
 */
//candidates for implementation
//remainder
public final class ExactReal extends ComparableNumber<ExactReal> implements Cloneable, Serializable,
        Field.Member {

    /**
     * The exact integer representing the additive identity.
     */
    public static final ExactReal ZERO = ExactRealField.ZERO;

    /**
     * The exact integer representing the multiplicative identity.
     */
    public static final ExactReal ONE = ExactRealField.ONE;

    public static final ExactReal NEGATIVE_INFINITY = new ExactReal(true, false);

    public static final ExactReal POSITIVE_INFINITY = new ExactReal(false, true);

    public static final ExactReal NaN = new ExactReal(true, true);

    //The Class instance representing the type.
    public static final Class TYPE = ZERO.getClass();

    private static final long serialVersionUID = 861668L;

    private final BigDecimal x;

    private final boolean neg;

    private final boolean pos;

    /**
     * Constructs a ExactReal number.
     */
    private ExactReal(final boolean neg, final boolean pos) {
        x = new BigDecimal(0);
        this.neg = neg;
        this.pos = pos;
    }

    /**
     * Constructs a ExactReal number.
     */
    public ExactReal(final ExactReal num) {
        x = num.x;
        neg = num.neg;
        pos = num.pos;
    }

    /**
     * Constructs a ExactReal number.
     */
    public ExactReal(final ExactRational num) {
        if (num.equals(ExactRational.NEGATIVE_INFINITY)) {
            x = new BigDecimal(0);
            neg = true;
            pos = false;
        } else {
            if (num.equals(ExactRational.POSITIVE_INFINITY)) {
                x = new BigDecimal(0);
                neg = false;
                pos = true;
            } else {
                if (num.equals(ExactRational.NaN)) {
                    x = new BigDecimal(0);
                    neg = true;
                    pos = true;
                } else {
                    x = num.value();
                    neg = false;
                    pos = false;
                }
            }
        }
    }

    /**
     * Constructs a ExactReal number.
     */
    public ExactReal(final BigDecimal num) {
        x = num;
        neg = false;
        pos = false;
    }

    /**
     * Constructs a ExactReal number.
     */
    public ExactReal(final ExactInteger num) {
        if (num.equals(ExactInteger.NEGATIVE_INFINITY)) {
            x = new BigDecimal(0);
            neg = true;
            pos = false;
        } else {
            if (num.equals(ExactInteger.POSITIVE_INFINITY)) {
                x = new BigDecimal(0);
                neg = false;
                pos = true;
            } else {
                if (num.equals(ExactInteger.NaN)) {
                    x = new BigDecimal(0);
                    neg = true;
                    pos = true;
                } else {
                    x = new BigDecimal(num.value());
                    neg = false;
                    pos = false;
                }
            }
        }
    }

    /**
     * Constructs a ExactReal number.
     */
    public ExactReal(final Double num) {
        if (num.equals(Double.NEGATIVE_INFINITY)) {
            x = new BigDecimal(0);
            neg = true;
            pos = false;
        } else {
            if (num.equals(Double.POSITIVE_INFINITY)) {
                x = new BigDecimal(0);
                neg = false;
                pos = true;
            } else {
                if (num.equals(Double.NaN)) {
                    x = new BigDecimal(0);
                    neg = true;
                    pos = true;
                } else {
                    x = new BigDecimal(num.value());
                    neg = false;
                    pos = false;
                }
            }
        }
    }

    /**
     * Constructs a ExactReal number.
     */
    public ExactReal(final Float num) {
        if (num.equals(Float.NEGATIVE_INFINITY)) {
            x = new BigDecimal(0);
            neg = true;
            pos = false;
        } else if (num.equals(Float.POSITIVE_INFINITY)) {
            x = new BigDecimal(0);
            neg = false;
            pos = true;
        } else if (num.equals(Float.NaN)) {
            x = new BigDecimal(0);
            neg = true;
            pos = true;
        } else {
            x = new BigDecimal(num.value());
            neg = false;
            pos = false;
        }
    }

    /**
     * Constructs a ExactReal number.
     */
    public ExactReal(final Long num) {
        if (num == null) {
            x = new BigDecimal(0);
            neg = true;
            pos = true;
        } else if (num.equals(Long.NEGATIVE_INFINITY)) {
            x = new BigDecimal(0);
            neg = true;
            pos = false;
        } else if (num.equals(Long.POSITIVE_INFINITY)) {
            x = new BigDecimal(0);
            neg = false;
            pos = true;
        } else {
            x = new BigDecimal(num.value());
            neg = false;
            pos = false;
        }
    }

    /**
     * Constructs a ExactReal number.
     */
    public ExactReal(final Rational num) {
        if (num == null) {
            x = new BigDecimal(0);
            neg = true;
            pos = true;
        } else if (num.equals(Rational.NEGATIVE_INFINITY)) {
            x = new BigDecimal(0);
            neg = true;
            pos = false;
        } else {
            if (num.equals(Rational.POSITIVE_INFINITY)) {
                x = new BigDecimal(0);
                neg = false;
                pos = true;
            } else {
                x = new BigDecimal(num.value());
                neg = false;
                pos = false;
            }

        }
    }

    /**
     * Constructs a ExactReal number.
     */
    public ExactReal(final Integer num) {
        if (num == null) {
            x = new BigDecimal(0);
            neg = true;
            pos = true;
        } else if (num.equals(Integer.NEGATIVE_INFINITY)) {
            x = new BigDecimal(0);
            neg = true;
            pos = false;
        } else if (num.equals(Integer.POSITIVE_INFINITY)) {
            x = new BigDecimal(0);
            neg = false;
            pos = true;
        } else {
            x = new BigDecimal(num.value());
            neg = false;
            pos = false;
        }

    }

    /**
     * Constructs a ExactReal number.
     */
    public ExactReal(final double num) {
        if (num == Double.NEGATIVE_INFINITY) {
            x = new BigDecimal(0);
            neg = true;
            pos = false;
        } else {
            if (num == Double.POSITIVE_INFINITY) {
                x = new BigDecimal(0);
                neg = false;
                pos = true;
            } else {
                if (num == Double.NaN) {
                    x = new BigDecimal(0);
                    neg = true;
                    pos = true;
                } else {
                    x = new BigDecimal(num);
                    neg = false;
                    pos = false;
                }
            }
        }
    }

    /**
     * Constructs a ExactReal number.
     */
    public ExactReal(final float num) {
        if (num == Float.NEGATIVE_INFINITY) {
            x = new BigDecimal(0);
            neg = true;
            pos = false;
        } else {
            if (num == Float.POSITIVE_INFINITY) {
                x = new BigDecimal(0);
                neg = false;
                pos = true;
            } else {
                if (num == Float.NaN) {
                    x = new BigDecimal(0);
                    neg = true;
                    pos = true;
                } else {
                    x = new BigDecimal(num);
                    neg = false;
                    pos = false;
                }
            }
        }
    }

    /**
     * Constructs a ExactReal number.
     */
    public ExactReal(final long num) {
        if (num == Long.NEGATIVE_INFINITY) {
            x = new BigDecimal(0);
            neg = true;
            pos = false;
        } else {
            if (num == Long.POSITIVE_INFINITY) {
                x = new BigDecimal(0);
                neg = false;
                pos = true;
            } else {
                //if (num==Long.NaN) {
                //    x = new BigDecimal(0);
                //    neg = true;
                //    pos = true;
                //} else {
                x = new BigDecimal(num);
                neg = false;
                pos = false;
                //}
            }
        }
    }

    /**
     * Constructs a ExactReal number.
     */
    public ExactReal(final int num) {
        if (num == Integer.NEGATIVE_INFINITY) {
            x = new BigDecimal(0);
            neg = true;
            pos = false;
        } else {
            if (num == Integer.POSITIVE_INFINITY) {
                x = new BigDecimal(0);
                neg = false;
                pos = true;
            } else {
                //if (num==Integer.NaN) {
                //    x = new BigDecimal(0);
                //    neg = true;
                //    pos = true;
                //} else {
                x = new BigDecimal(num);
                neg = false;
                pos = false;
                //}
            }
        }
    }

    /**
     * Constructs the ExactReal number represented by a string.
     *
     * @param s a string representing a ExactReal number.
     * @throws NumberFormatException if the string does not contain a parsable number.
     */
    public ExactReal(final String s) throws NumberFormatException {
        if (s.equals("NaN")) {
            x = new BigDecimal(0);
            neg = true;
            pos = true;
        } else {
            if (s.equals("-Infinity")) {
                x = new BigDecimal(0);
                neg = true;
                pos = false;
            } else {
                if (s.equals("Infinity")) {
                    x = new BigDecimal(0);
                    neg = false;
                    pos = true;
                } else {
                    x = new BigDecimal(s);
                    neg = false;
                    pos = false;
                }
            }
        }
    }

    /**
     * Compares two ExactReal numbers for equality.
     *
     * @param obj a ExactReal number.
     */
    public boolean equals(Object obj) {
        if (obj instanceof ExactReal) {
            return x.equals(((ExactReal) obj).x) && (neg == ((ExactReal) obj).neg) && (pos == ((ExactReal) obj).pos);
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
        if (obj instanceof ExactReal) {
            return compareTo((ExactReal) obj);
        } else if (obj instanceof ExactRational) {
            return compareTo(new ExactReal((ExactRational) obj));
        } else if (obj instanceof ExactInteger) {
            return compareTo(new ExactReal((ExactInteger) obj));
        } else if (obj instanceof Double) {
            return compareTo(new ExactReal((Double) obj));
        } else if (obj instanceof Float) {
            return compareTo(new ExactReal((Float) obj));
        } else if (obj instanceof Rational) {
            return compareTo(new ExactReal((Rational) obj));
        } else if (obj instanceof Long) {
            return compareTo(new ExactReal((Long) obj));
        } else if (obj instanceof Integer) {
            return compareTo(new ExactReal((Integer) obj));
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Compares two ExactReal numbers.
     *
     * @param value a ExactReal number.
     * @return a negative value if <code>this&lt;obj</code>,
     *         zero if <code>this==obj</code>,
     *         and a positive value if <code>this&gt;obj</code>.
     */
    public int compareTo(ExactReal value) {
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
                        return x.compareTo(((ExactReal) value).x);
                    }
                }
            }
        }
    }

    /**
     * Returns a string representing the value of this ExactReal number.
     */
    public String toString() {
        if (isInfinite()) {
            return "Infinity";
        } else {
            return x.toString();
        }
    }

    public BigDecimal value() {
        return x;
    }

    //returns an exception if this equals to ExactReal.NaN, ExactReal.NEGATIVE_INFINITY or ExactReal.POSITIVE_INFINITY
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

    //returns an exception if this equals to ExactReal.NaN, ExactReal.NEGATIVE_INFINITY or ExactReal.POSITIVE_INFINITY
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
    public ExactReal abs() {
        if (neg) {
            if (pos) {
                return ExactReal.NaN;
            } else {
                return ExactReal.POSITIVE_INFINITY;
            }
        } else {
            if (pos) {
                return ExactReal.POSITIVE_INFINITY;
            } else {
                return new ExactReal(x.abs());
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
    public ExactReal min(ExactReal val) {
        if (compareTo(val) == -1) {
            return this;
        } else {
            return val;
        }
    }

    /**
     * Returns the max of this number and another, according to compareTo rules.
     */
    public ExactReal max(ExactReal val) {
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

    public ExactReal getNaN() {
        return ExactReal.NaN;
    }

    public ExactReal getNegativeInfinity() {
        return ExactReal.NEGATIVE_INFINITY;
    }

    public ExactReal getPositiveInfinity() {
        return ExactReal.POSITIVE_INFINITY;
    }

    public ExactReal getDistance(ComparableNumber n) {
        if (n instanceof ExactReal) {
            return ((ExactReal) n).subtract(this);
        } else if (n instanceof ExactRational) {
            return new ExactReal((ExactRational) n).subtract(this);
        } else if (n instanceof ExactInteger) {
            return new ExactReal((ExactInteger) n).subtract(this);
        } else if (n instanceof Double) {
            return new ExactReal((Double) n).subtract(this);
        } else if (n instanceof Float) {
            return new ExactReal((Float) n).subtract(this);
        } else if (n instanceof Rational) {
            return new ExactReal((Rational) n).subtract(this);
        } else if (n instanceof Long) {
            return new ExactReal((Long) n).subtract(this);
        } else if (n instanceof Integer) {
            return new ExactReal((Integer) n).subtract(this);
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
                return ExactReal.NaN;
            } else {
                return ExactReal.POSITIVE_INFINITY;
            }
        } else {
            if (pos) {
                return ExactReal.NEGATIVE_INFINITY;
            } else {
                return new ExactReal(x.negate());
            }
        }
    }

    /**
     * Returns the inverse of this number.
     */
    public Field.Member inverse() {
        if (neg) {
            if (pos) {
                return ExactReal.NaN;
            } else {
                return ExactReal.ZERO;
            }
        } else {
            if (pos) {
                return ExactReal.ZERO;
            } else {
                return new ExactReal(x.pow(-1));
            }
        }
    }

    /**
     * Returns the addition of this number and another.
     */
    public AbelianGroup.Member add(final AbelianGroup.Member n) {
        if (n instanceof ExactReal) {
            return add((ExactReal) n);
        } else if (n instanceof ExactInteger) {
            return add(new ExactReal((ExactInteger) n));
        } else if (n instanceof Integer) {
            return add(new ExactReal(((Integer) n)));
        } else if (n instanceof Long) {
            return add(new ExactReal(((Long) n)));
        } else if (n instanceof Rational) {
            return add(new ExactReal(((Rational) n)));
        } else if (n instanceof Float) {
            return add(new ExactReal(((Float) n)));
        } else if (n instanceof Double) {
            return add(new ExactReal(((Double) n)));
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this ExactReal number and another. Adding positive and negative infinity will return NaN.
     */
    public ExactReal add(final ExactReal n) {
        if (neg) {
            if (pos) {//NaN
                return ExactReal.NaN;
            } else {//NEGATIVE_INFINITY
                if (n.neg) {
                    if (n.pos) {
                        return ExactReal.NaN;
                    } else {
                        return ExactReal.NEGATIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactReal.NaN;
                    } else {
                        return ExactReal.NEGATIVE_INFINITY;
                    }
                }
            }
        } else {
            if (pos) {//POSITIVE_INFINITY
                if (n.neg) {
                    return ExactReal.NaN;
                } else {
                    return ExactReal.POSITIVE_INFINITY;
                }
            } else {//Number
                if (n.neg) {
                    if (n.pos) {
                        return ExactReal.NaN;
                    } else {
                        return ExactReal.NEGATIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactReal.POSITIVE_INFINITY;
                    } else {
                        return new ExactReal(x.add(n.x));
                    }
                }
            }
        }
    }

    /**
     * Returns the subtraction of this number and another.
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member n) {
        if (n instanceof ExactReal) {
            return subtract((ExactReal) n);
        } else if (n instanceof ExactInteger) {
            return subtract(new ExactReal((ExactInteger) n));
        } else if (n instanceof Integer) {
            return subtract(new ExactReal(((Integer) n)));
        } else if (n instanceof Long) {
            return subtract(new ExactReal(((Long) n)));
        } else if (n instanceof Rational) {
            return subtract(new ExactReal(((Rational) n)));
        } else if (n instanceof Float) {
            return subtract(new ExactReal(((Float) n)));
        } else if (n instanceof Double) {
            return subtract(new ExactReal(((Double) n)));
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this ExactReal number and another. Subtracting positive and positive infinity will return NaN. Subtracting negative and negative infinity will return NaN.
     */
    public ExactReal subtract(final ExactReal n) {
        if (neg) {
            if (pos) {//NaN
                return ExactReal.NaN;
            } else {//NEGATIVE_INFINITY
                if (n.neg) {
                    return ExactReal.NaN;
                } else {
                    return ExactReal.NEGATIVE_INFINITY;
                }
            }
        } else {
            if (pos) {//POSITIVE_INFINITY
                if (n.neg) {
                    if (n.pos) {
                        return ExactReal.NaN;
                    } else {
                        return ExactReal.POSITIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactReal.NaN;
                    } else {
                        return ExactReal.POSITIVE_INFINITY;
                    }
                }
            } else {//Number
                if (n.neg) {
                    if (n.pos) {
                        return ExactReal.NaN;
                    } else {
                        return ExactReal.POSITIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactReal.NEGATIVE_INFINITY;
                    } else {
                        return new ExactReal(x.subtract(n.x));
                    }
                }
            }
        }
    }

    /**
     * Returns the multiplication of this number and another.
     */
    public Ring.Member multiply(final Ring.Member n) {
        if (n instanceof ExactReal) {
            return multiply((ExactReal) n);
        } else if (n instanceof ExactInteger) {
            return multiply(new ExactReal((ExactInteger) n));
        } else if (n instanceof Integer) {
            return multiply(new ExactReal(((Integer) n)));
        } else if (n instanceof Long) {
            return multiply(new ExactReal(((Long) n)));
        } else if (n instanceof Rational) {
            return multiply(new ExactReal(((Rational) n)));
        } else if (n instanceof Float) {
            return multiply(new ExactReal(((Float) n)));
        } else if (n instanceof Double) {
            return multiply(new ExactReal(((Double) n)));
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the multiplication of this ExactReal number and another. Multiplying infinity with 0 will return NaN.
     */
    public ExactReal multiply(final ExactReal n) {
        if (neg) {
            if (pos) {//NaN
                return ExactReal.NaN;
            } else {//NEGATIVE_INFINITY
                if (n.neg) {
                    if (n.pos) {
                        return ExactReal.NaN;
                    } else {
                        return ExactReal.POSITIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactReal.NEGATIVE_INFINITY;
                    } else {
                        if (n.signum() == 1) {
                            return ExactReal.NEGATIVE_INFINITY;
                        } else {
                            if (n.signum() == -1) {
                                return ExactReal.POSITIVE_INFINITY;
                            } else {
                                return ExactReal.NaN;
                            }
                        }
                    }
                }
            }
        } else {
            if (pos) {//POSITIVE_INFINITY
                if (n.neg) {
                    if (n.pos) {
                        return ExactReal.NaN;
                    } else {
                        return ExactReal.NEGATIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactReal.POSITIVE_INFINITY;
                    } else {
                        if (n.signum() == 1) {
                            return ExactReal.POSITIVE_INFINITY;
                        } else {
                            if (n.signum() == -1) {
                                return ExactReal.NEGATIVE_INFINITY;
                            } else {
                                return ExactReal.NaN;
                            }
                        }
                    }
                }
            } else {//Number
                if (n.neg) {
                    if (n.pos) {
                        return ExactReal.NaN;
                    } else {
                        if (signum() == 1) {
                            return ExactReal.NEGATIVE_INFINITY;
                        } else {
                            if (signum() == -1) {
                                return ExactReal.POSITIVE_INFINITY;
                            } else {
                                return ExactReal.NaN;
                            }
                        }
                    }
                } else {
                    if (n.pos) {
                        if (signum() == 1) {
                            return ExactReal.POSITIVE_INFINITY;
                        } else {
                            if (signum() == -1) {
                                return ExactReal.NEGATIVE_INFINITY;
                            } else {
                                return ExactReal.NaN;
                            }
                        }
                    } else {
                        return new ExactReal(x.multiply(n.x));
                    }
                }
            }
        }
    }

    /**
     * Returns the division of this number and another.
     */
    public Field.Member divide(final Field.Member n) {
        if (n instanceof ExactReal) {
            return divide((ExactReal) n);
        }
        //Wouldn't it be nice if we could divide by Ring.Member instead ?
        //else if (n instanceof ExactInteger)
        //    return divide(new ExactReal((ExactInteger)n));
        //else if (n instanceof Integer)
        //    return divide(new ExactReal(((Integer) n)));
        //else if (n instanceof Long)
        //    return divide(new ExactReal(((Long) n)));
        else if (n instanceof Rational) {
            return divide(new ExactReal(((Rational) n)));
        } else if (n instanceof Float) {
            return divide(new ExactReal(((Float) n)));
        } else if (n instanceof Double) {
            return divide(new ExactReal(((Double) n)));
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the division of this ExactReal number and another. Dividing infinity with infinity will return NaN.
     */
    public ExactReal divide(final ExactReal n) {
        if (neg) {
            if (pos) {//NaN
                return ExactReal.NaN;
            } else {//NEGATIVE_INFINITY
                if (n.neg) {
                    return ExactReal.NaN;
                } else {
                    if (n.pos) {
                        return ExactReal.NaN;
                    } else {
                        if (n.signum() == 1) {
                            return ExactReal.NEGATIVE_INFINITY;
                        } else {
                            if (n.signum() == -1) {
                                return ExactReal.POSITIVE_INFINITY;
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
                    return ExactReal.NaN;
                } else {
                    if (n.pos) {
                        return ExactReal.NaN;
                    } else {
                        if (n.signum() == 1) {
                            return ExactReal.POSITIVE_INFINITY;
                        } else {
                            if (n.signum() == -1) {
                                return ExactReal.NEGATIVE_INFINITY;
                            } else {
                                throw new ArithmeticException("Can't divide by zero.");
                            }
                        }
                    }
                }
            } else {//Number
                if (n.neg) {
                    if (n.pos) {
                        return ExactReal.NaN;
                    } else {
                        if (signum() == 1) {
                            return ExactReal.NEGATIVE_INFINITY;
                        } else {
                            if (signum() == -1) {
                                return ExactReal.POSITIVE_INFINITY;
                            } else {
                                return ExactReal.ZERO;
                            }
                        }
                    }
                } else {
                    if (n.pos) {
                        if (signum() == 1) {
                            return ExactReal.POSITIVE_INFINITY;
                        } else {
                            if (signum() == -1) {
                                return ExactReal.NEGATIVE_INFINITY;
                            } else {
                                return ExactReal.ZERO;
                            }
                        }
                    } else {
                        return new ExactReal(x.divide(n.x));
                    }
                }
            }
        }
    }

//===========
// FUNCTIONS
//===========

// EXP

    /**
     * Returns this number raised to the power of a number.
     */
    public ExactReal pow(final int i) {
        if (neg) {
            if (pos) {
                return ExactReal.NaN;
            } else {
                if (i == 0) {
                    return ExactReal.ONE;
                } else {
                    if (i > 0) {
                        if (i % 2 == 0) {
                            return ExactReal.POSITIVE_INFINITY;
                        } else {
                            return ExactReal.NEGATIVE_INFINITY;
                        }
                    } else {
                        return ExactReal.ZERO;
                    }
                }
            }
        } else {
            if (pos) {
                if (i == 0) {
                    return ExactReal.ONE;
                } else {
                    if (i > 0) {
                        return ExactReal.POSITIVE_INFINITY;
                    } else {
                        return ExactReal.ZERO;
                    }
                }
            } else {
                return new ExactReal(x.pow(i));
            }
        }
    }

    /**
     * // LOG
     * <p/>
     * /**
     * Returns the natural logarithm (base e) of a number.
     */
    //public static ExactReal log(final ExactReal x) {
    //}

// SIN

    /**
     * Returns the trigonometric sine of an angle.
     *
     * @param x an angle that is measured in radians
     */
    //public static ExactReal sin(final ExactReal x) {
    //}

// COS

    /**
     * Returns the trigonometric cosine of an angle.
     *
     * @param x an angle that is measured in radians
     */
    //public static ExactReal cos(final ExactReal x) {
    //}

// TAN

    /**
     * Returns the trigonometric tangent of an angle.
     *
     * @param x an angle that is measured in radians
     */
    //public static ExactReal tan(final ExactReal x) {
    //}

// SINH

    /**
     * Returns the hyperbolic sine of a number.
     */
    //public static ExactReal sinh(final ExactReal x) {
    //}

// COSH

    /**
     * Returns the hyperbolic cosine of a number.
     */
    //public static ExactReal cosh(final ExactReal x) {
    //}

// TANH

    /**
     * Returns the hyperbolic tangent of a number.
     */
    //public static ExactReal tanh(final ExactReal x) {
    //}

// INVERSE SIN

    /**
     * Returns the arc sine of a number.
     */
    //public static ExactReal asin(final ExactReal x) {
    //}

// INVERSE COS

    /**
     * Returns the arc cosine of a number.
     */
    //public static ExactReal acos(final ExactReal x) {
    //}

// INVERSE TAN

    /**
     * Returns the arc tangent of a number.
     */
    //public static ExactReal atan(final ExactReal x) {
    //}

// INVERSE SINH

    /**
     * Returns the arc hyperbolic sine of a number.
     */
    //public static ExactReal asinh(final ExactReal x) {
    //}

// INVERSE COSH

    /**
     * Returns the arc hyperbolic cosine of a number.
     */
    //public static ExactReal acosh(final ExactReal x) {
    //}

// INVERSE TANH

    /**
     * Returns the arc hyperbolic tangent of a number.
     */
    //public static ExactReal atanh(final ExactReal x) {
    //}
    public Object clone() {
        return new ExactReal(this);
    }

}

