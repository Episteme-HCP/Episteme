package org.jscience.mathematics.algebraic.numbers;

import org.jscience.mathematics.algebraic.fields.ExactIntegerRing;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * The ExactInteger class encapsulates integer numbers with infinite precision but slower speed than Integer or Long.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 * @see org.jscience.mathematics.algebraic.fields.IntegerRing
 */
//candidates for implementation
//and, andNot, or, not, xor
//mod, gcd, remainder
//altough ExactInteger is a Ring.Member, its private field x is built using BigInteger which (not so) oddly supports
//divide(BigInteger b). We could therefore provide division (for speed reason) although this would break the model a bit.

//we could define also ExactNatural as the set of positive integers but will anyone ever need this.
public final class ExactInteger extends ComparableNumber<ExactInteger>
        implements Cloneable, Serializable, Ring.Member {

    /**
     * The exact integer representing the additive identity.
     */
    public static final ExactInteger ZERO = ExactIntegerRing.ZERO;

    /**
     * The exact integer representing the multiplicative identity.
     */
    public static final ExactInteger ONE = ExactIntegerRing.ONE;

    public static final ExactInteger NEGATIVE_INFINITY
            = new ExactInteger(true, false);

    public static final ExactInteger POSITIVE_INFINITY
            = new ExactInteger(false, true);

    public static final ExactInteger NaN = new ExactInteger(true, true);

    //The Class instance representing the type.
    public static final Class TYPE = ZERO.getClass();

    private static final long serialVersionUID = 8616680319093653108L;

    private final BigInteger x;

    private final boolean neg;

    private final boolean pos;

    /**
     * Constructs a ExactInteger number.
     */
    private ExactInteger(final boolean neg, final boolean pos) {
        x = BigInteger.valueOf(0);
        this.neg = neg;
        this.pos = pos;
    }

    /**
     * Constructs a ExactInteger number.
     */
    public ExactInteger(final ExactInteger num) {
        x = num.x;
        neg = num.neg;
        pos = num.pos;
    }

    /**
     * Constructs a ExactInteger number.
     */
    public ExactInteger(final BigInteger num) {
        x = num;
        neg = false;
        pos = false;
    }

    /**
     * Constructs a ExactInteger number.
     */
    public ExactInteger(final Long num) {
        if (num == null) {
            x = BigInteger.valueOf(0);
            neg = true;
            pos = true;
        } else if (num.equals(Long.NEGATIVE_INFINITY)) {
            x = BigInteger.valueOf(0);
            neg = true;
            pos = false;
        } else if (num.equals(Long.POSITIVE_INFINITY)) {
            x = BigInteger.valueOf(0);
            neg = false;
            pos = true;
        } else {
            x = new BigInteger(num.toString());
            neg = false;
            pos = false;
        }
    }

    /**
     * Constructs a ExactInteger number.
     */
    public ExactInteger(final Integer num) {
        {
            if (num == null) {
                x = BigInteger.valueOf(0);
                neg = true;
                pos = true;
            } else if (num.equals(Long.NEGATIVE_INFINITY)) {
                x = BigInteger.valueOf(0);
                neg = true;
                pos = false;
            } else if (num.equals(Long.POSITIVE_INFINITY)) {
                x = BigInteger.valueOf(0);
                neg = false;
                pos = true;
            } else {
                x = new BigInteger(num.toString());
                neg = false;
                pos = false;
            }

        }
    }

    /**
     * Constructs a ExactInteger number.
     */
    public ExactInteger(final long num) {
        if (num == Long.NEGATIVE_INFINITY) {
            x = BigInteger.valueOf(0);
            neg = true;
            pos = false;
        } else {
            if (num == Long.POSITIVE_INFINITY) {
                x = BigInteger.valueOf(0);
                neg = false;
                pos = true;
            } else {
                //if (num==Long.NaN) {
                //    x = BigInteger.valueOf(0);
                //    neg = true;
                //    pos = true;
                //} else {
                x = BigInteger.valueOf(num);
                neg = false;
                pos = false;
                //}
            }
        }
    }

    /**
     * Constructs a ExactInteger number.
     */
    public ExactInteger(final int num) {
        if (num == Integer.NEGATIVE_INFINITY) {
            x = BigInteger.valueOf(0);
            neg = true;
            pos = false;
        } else {
            if (num == Integer.POSITIVE_INFINITY) {
                x = BigInteger.valueOf(0);
                neg = false;
                pos = true;
            } else {
                //if (num==Integer.NaN) {
                //    x = BigInteger.valueOf(0);
                //    neg = true;
                //    pos = true;
                //} else {
                x = BigInteger.valueOf(num);
                neg = false;
                pos = false;
                //}
            }
        }
    }

    /**
     * Constructs the ExactInteger number represented by a string.
     *
     * @param s a string representing a ExactInteger number.
     * @throws NumberFormatException if the string does not contain a parsable number.
     */
    public ExactInteger(final String s) throws NumberFormatException {
        if (s.equals("NaN")) {
            x = BigInteger.valueOf(0);
            neg = true;
            pos = true;
        } else {
            if (s.equals("-Infinity")) {
                x = BigInteger.valueOf(0);
                neg = true;
                pos = false;
            } else {
                if (s.equals("Infinity")) {
                    x = BigInteger.valueOf(0);
                    neg = false;
                    pos = true;
                } else {
                    x = new BigInteger(s);
                    neg = false;
                    pos = false;
                }
            }
        }
    }

    /**
     * Compares two ExactInteger numbers for equality.
     *
     * @param obj a ExactInteger number.
     */
    public boolean equals(Object obj) {
        if (obj instanceof ExactInteger) {
            return x.equals(((ExactInteger) obj).x) && (neg == ((ExactInteger) obj).neg) && (pos == ((ExactInteger) obj).pos);
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
        if (obj instanceof ExactInteger) {
            return compareTo((ExactInteger) obj);
        } else if (obj instanceof Long) {
            return compareTo(new ExactInteger((Long) obj));
        } else if (obj instanceof Integer) {
            return compareTo(new ExactInteger((Integer) obj));
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Compares two ExactInteger numbers.
     *
     * @param value a ExactInteger number.
     * @return a negative value if <code>this&lt;obj</code>,
     *         zero if <code>this==obj</code>,
     *         and a positive value if <code>this&gt;obj</code>.
     */
    public int compareTo(ExactInteger value) {
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
                        return x.compareTo(((ExactInteger) value).x);
                    }
                }
            }
        }
    }

    /**
     * Returns a string representing the value of this ExactInteger number.
     */
    public String toString() {
        if (isInfinite()) {
            return "Infinity";
        } else {
            return x.toString();
        }
    }

    public BigInteger value() {
        return x;
    }

    //returns an exception if this equals to ExactInteger.NaN, ExactInteger.NEGATIVE_INFINITY or ExactInteger.POSITIVE_INFINITY
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

    //returns an exception if this equals to ExactInteger.NaN, ExactInteger.NEGATIVE_INFINITY or ExactInteger.POSITIVE_INFINITY
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
    public ExactInteger abs() {
        if (neg) {
            if (pos) {
                return ExactInteger.NaN;
            } else {
                return ExactInteger.POSITIVE_INFINITY;
            }
        } else {
            if (pos) {
                return ExactInteger.POSITIVE_INFINITY;
            } else {
                return new ExactInteger(x.abs());
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
    public ExactInteger min(ExactInteger val) {
        if (compareTo(val) == -1) {
            return this;
        } else {
            return val;
        }
    }

    /**
     * Returns the max of this number and another, according to compareTo rules.
     */
    public ExactInteger max(ExactInteger val) {
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
        return neg & pos;
    }

    /**
     * Returns true if this number is infinite.
     */
    public boolean isInfinite() {
        return neg ^ pos;
    }

    public boolean isNegativeInfinity() {
        return neg & !pos;
    }

    public boolean isPositiveInfinity() {
        return pos & !neg;
    }

    public ExactInteger getNaN() {
        return ExactInteger.NaN;
    }

    public ExactInteger getNegativeInfinity() {
        return ExactInteger.NEGATIVE_INFINITY;
    }

    public ExactInteger getPositiveInfinity() {
        return ExactInteger.POSITIVE_INFINITY;
    }

    public ExactInteger getDistance(ComparableNumber n) {
        if (n instanceof ExactInteger) {
            return new ExactInteger((ExactInteger) n).subtract(this);
        } else if (n instanceof Long) {
            return new ExactInteger((Long) n).subtract(this);
        } else if (n instanceof Integer) {
            return new ExactInteger((Integer) n).subtract(this);
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
                return ExactInteger.NaN;
            } else {
                return ExactInteger.POSITIVE_INFINITY;
            }
        } else {
            if (pos) {
                return ExactInteger.NEGATIVE_INFINITY;
            } else {
                return new ExactInteger(x.negate());
            }
        }
    }

    /**
     * Returns the addition of this number and another.
     */
    public AbelianGroup.Member add(final AbelianGroup.Member n) {
        if (n instanceof ExactInteger) {
            return add((ExactInteger) n);
        } else if (n instanceof Integer) {
            return add(new ExactInteger(((Integer) n)));
        } else if (n instanceof Long) {
            return add(new ExactInteger(((Long) n)));
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this ExactInteger number and another.
     */
    public ExactInteger add(final ExactInteger n) {
        if (neg) {
            if (pos) {//NaN
                return ExactInteger.NaN;
            } else {//NEGATIVE_INFINITY
                if (n.neg) {
                    if (n.pos) {
                        return ExactInteger.NaN;
                    } else {
                        return ExactInteger.NEGATIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactInteger.NaN;
                    } else {
                        return ExactInteger.NEGATIVE_INFINITY;
                    }
                }
            }
        } else {
            if (pos) {//POSITIVE_INFINITY
                if (n.neg) {
                    return ExactInteger.NaN;
                } else {
                    return ExactInteger.POSITIVE_INFINITY;
                }
            } else {//Number
                if (n.neg) {
                    if (n.pos) {
                        return ExactInteger.NaN;
                    } else {
                        return ExactInteger.NEGATIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactInteger.POSITIVE_INFINITY;
                    } else {
                        return new ExactInteger(x.add(n.x));
                    }
                }
            }
        }
    }

    /**
     * Returns the subtraction of this number and another.
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member n) {
        if (n instanceof ExactInteger) {
            return subtract((ExactInteger) n);
        } else if (n instanceof Integer) {
            return subtract(new ExactInteger(((Integer) n)));
        } else if (n instanceof Long) {
            return subtract(new ExactInteger(((Long) n)));
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this ExactInteger number and another.
     */
    public ExactInteger subtract(final ExactInteger n) {
        if (neg) {
            if (pos) {//NaN
                return ExactInteger.NaN;
            } else {//NEGATIVE_INFINITY
                if (n.neg) {
                    return ExactInteger.NaN;
                } else {
                    return ExactInteger.NEGATIVE_INFINITY;
                }
            }
        } else {
            if (pos) {//POSITIVE_INFINITY
                if (n.neg) {
                    if (n.pos) {
                        return ExactInteger.NaN;
                    } else {
                        return ExactInteger.POSITIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactInteger.NaN;
                    } else {
                        return ExactInteger.POSITIVE_INFINITY;
                    }
                }
            } else {//Number
                if (n.neg) {
                    if (n.pos) {
                        return ExactInteger.NaN;
                    } else {
                        return ExactInteger.POSITIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactInteger.NEGATIVE_INFINITY;
                    } else {
                        return new ExactInteger(x.subtract(n.x));
                    }
                }
            }
        }
    }

    /**
     * Returns the multiplication of this number and another.
     */
    public Ring.Member multiply(final Ring.Member n) {
        if (n instanceof ExactInteger) {
            return multiply((ExactInteger) n);
        } else if (n instanceof Integer) {
            return multiply(new ExactInteger(((Integer) n)));
        } else if (n instanceof Long) {
            return multiply(new ExactInteger(((Long) n)));
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the multiplication of this ExactInteger number and another.
     */
    public ExactInteger multiply(final ExactInteger n) {
        if (neg) {
            if (pos) {//NaN
                return ExactInteger.NaN;
            } else {//NEGATIVE_INFINITY
                if (n.neg) {
                    if (n.pos) {
                        return ExactInteger.NaN;
                    } else {
                        return ExactInteger.POSITIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactInteger.NEGATIVE_INFINITY;
                    } else {
                        if (n.signum() == 1) {
                            return ExactInteger.NEGATIVE_INFINITY;
                        } else {
                            if (n.signum() == -1) {
                                return ExactInteger.POSITIVE_INFINITY;
                            } else {
                                return ExactInteger.NaN;
                            }
                        }
                    }
                }
            }
        } else {
            if (pos) {//POSITIVE_INFINITY
                if (n.neg) {
                    if (n.pos) {
                        return ExactInteger.NaN;
                    } else {
                        return ExactInteger.NEGATIVE_INFINITY;
                    }
                } else {
                    if (n.pos) {
                        return ExactInteger.POSITIVE_INFINITY;
                    } else {
                        if (n.signum() == 1) {
                            return ExactInteger.POSITIVE_INFINITY;
                        } else {
                            if (n.signum() == -1) {
                                return ExactInteger.NEGATIVE_INFINITY;
                            } else {
                                return ExactInteger.NaN;
                            }
                        }
                    }
                }
            } else {//Number
                if (n.neg) {
                    if (n.pos) {
                        return ExactInteger.NaN;
                    } else {
                        if (signum() == 1) {
                            return ExactInteger.NEGATIVE_INFINITY;
                        } else {
                            if (signum() == -1) {
                                return ExactInteger.POSITIVE_INFINITY;
                            } else {
                                return ExactInteger.NaN;
                            }
                        }
                    }
                } else {
                    if (n.pos) {
                        if (signum() == 1) {
                            return ExactInteger.POSITIVE_INFINITY;
                        } else {
                            if (signum() == -1) {
                                return ExactInteger.NEGATIVE_INFINITY;
                            } else {
                                return ExactInteger.NaN;
                            }
                        }
                    } else {
                        return new ExactInteger(x.multiply(n.x));
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
    public ExactInteger pow(final int i) {

        if (neg) {
            if (pos) {
                return ExactInteger.NaN;
            } else {
                if (i == 0) {
                    return ExactInteger.ONE;
                } else {
                    if (i > 0) {
                        if (i % 2 == 0) {
                            return ExactInteger.POSITIVE_INFINITY;
                        } else {
                            return ExactInteger.NEGATIVE_INFINITY;
                        }
                    } else {
                        return ExactInteger.ZERO;
                    }
                }
            }
        } else {
            if (pos) {
                if (i == 0) {
                    return ExactInteger.ONE;
                } else {
                    if (i > 0) {
                        return ExactInteger.POSITIVE_INFINITY;
                    } else {
                        return ExactInteger.ZERO;
                    }
                }
            } else {
                return new ExactInteger(x.pow(i));
            }
        }

    }

    /**
     * // LOG
     * <p/>
     * /**
     * Returns the natural logarithm (base e) of a number.
     */
    //public static ExactInteger log(final ExactInteger x) {
    //}

// SIN

    /**
     * Returns the trigonometric sine of an angle.
     *
     * @param x an angle that is measured in radians
     */
    //public static ExactInteger sin(final ExactInteger x) {
    //}

// COS

    /**
     * Returns the trigonometric cosine of an angle.
     *
     * @param x an angle that is measured in radians
     */
    //public static ExactInteger cos(final ExactInteger x) {
    //}

// TAN

    /**
     * Returns the trigonometric tangent of an angle.
     *
     * @param x an angle that is measured in radians
     */
    //public static ExactInteger tan(final ExactInteger x) {
    //}

// SINH

    /**
     * Returns the hyperbolic sine of a number.
     */
    //public static ExactInteger sinh(final ExactInteger x) {
    //}

// COSH

    /**
     * Returns the hyperbolic cosine of a number.
     */
    //public static ExactInteger cosh(final ExactInteger x) {
    //}

// TANH

    /**
     * Returns the hyperbolic tangent of a number.
     */
    //public static ExactInteger tanh(final ExactInteger x) {
    //}

// INVERSE SIN

    /**
     * Returns the arc sine of a number.
     */
    //public static ExactInteger asin(final ExactInteger x) {
    //}

// INVERSE COS

    /**
     * Returns the arc cosine of a number.
     */
    //public static ExactInteger acos(final ExactInteger x) {
    //}

// INVERSE TAN

    /**
     * Returns the arc tangent of a number.
     */
    //public static ExactInteger atan(final ExactInteger x) {
    //}

// INVERSE SINH

    /**
     * Returns the arc hyperbolic sine of a number.
     */
    //public static ExactInteger asinh(final ExactInteger x) {
    //}

// INVERSE COSH

    /**
     * Returns the arc hyperbolic cosine of a number.
     */
    //public static ExactInteger acosh(final ExactInteger x) {
    //}

// INVERSE TANH

    /**
     * Returns the arc hyperbolic tangent of a number.
     */
    //public static ExactInteger atanh(final ExactInteger x) {
    //}
    public Object clone() {
        return new ExactInteger(this);
    }

}

