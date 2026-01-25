package org.jscience.mathematics.util;
import org.jscience.mathematics.MathConstants;

/**
 * The extra math library.
 * Provides extra functions not in java.lang.Math class.
 * This class cannot be subclassed or instantiated because all methods are static.
 *
 * @author Mark Hale
 * @version 1.2
 */
public final class MathUtils {
    private MathUtils() {
    }

    /**
     * Rounds a number to so many significant figures.
     *
     * @param x           a number to be rounded.
     * @param significant number of significant figures to round to.
     * @return the rounded number
     */
    public static double round(double x, final int significant) {
        if (x == 0.0)
            return x;
        final double exp = Math.ceil(log10(x) - significant);
        final double factor = Math.pow(10.0, exp);
        return Math.round(x / factor) * factor;
    }

    /**
     * Returns sqrt(x<sup>2</sup>+y<sup>2</sup>).
     * @param x a double
     * @param y a double
     * @return the hypotenuse
     * @see Math#hypot(double, double)
     */
    public static double hypot(final double x, final double y) {
        return Math.hypot(x, y);
    }

    /**
     * Returns a<sup>b</sup>.
     *
     * @param a an integer.
     * @param b a positive integer.
     * @return a^b
     */
    public static int pow(int a, int b) {
        if (b < 0) {
            throw new IllegalArgumentException(b + " must be a positive integer.");
        } else if (b == 0) {
            return 1;
        } else {
            if (a == 0) {
                return 0;
            } else if (a == 1) {
                return 1;
              } else if (a == 2) {
                return 1 << b;
            } else {
                int result = 1;
                for (int i = 0; i < b; i++)
                    result *= a;
                return result;
            }
        }
    }

    /**
     * Returns 2<sup>a</sup>.
     *
     * @param a a positive integer.
     * @return 2^a
     */
    public static int pow2(int a) {
        return 1 << a;
    }

    /**
     * Returns the factorial.
     * (Wrapper for the gamma function).
     *
     * @param x a double.
     * @return x!
     * @see SpecialMathUtils#gamma
     */
    public static double factorial(double x) {
        return SpecialMathUtils.gamma(x + 1.0);
    }

    /**
     * Returns the natural logarithm of the factorial.
     * (Wrapper for the log gamma function).
     *
     * @param x a double.
     * @return ln(x!)
     * @see SpecialMathUtils#logGamma
     */
    public static double logFactorial(double x) {
        return SpecialMathUtils.logGamma(x + 1.0);
    }

    /**
     * Returns the binomial coefficient (n k).
     * Uses Pascal's recursion formula.
     *
     * @param n an integer.
     * @param k an integer.
     * @return (n k)
     */
    public static int binomial(int n, int k) {
        if (k == n || k == 0)
            return 1;
        else if (n == 0)
            return 1;
        else
            return binomial(n - 1, k - 1) + binomial(n - 1, k);
    }

    /**
     * Returns the binomial coefficient (n k).
     * Uses gamma functions.
     *
     * @param n a double.
     * @param k a double between 0 and n.
     * @return (n k)
     */
    public static double binomial(double n, double k) {
        return Math.exp(SpecialMathUtils.logGamma(n + 1.0) - SpecialMathUtils.logGamma(k + 1.0) - SpecialMathUtils.logGamma(n - k + 1.0));
    }

    /**
     * Returns the multinomial coefficient (n k[]).
     * Uses gamma functions.
     *
     * @param n   a double.
     * @param k   a double array such that k1 + k2 + k3 + ... + km = n (unchecked).
     * @return the multinomial coefficient
     */
    public static double polynomialExpansion(double n, double k[]) {
        double result = 0;
        for (int i = 0; i < k.length; i++) {
            result += SpecialMathUtils.logGamma(k[i] + 1.0);
        }
        return Math.exp(SpecialMathUtils.logGamma(n + 1.0) - result);
    }

    /**
     * Returns the Greatest Common Divisor (GCD).
     *
     * @param a the first integer.
     * @param b the second integer.
     * @return the GCD.
     */
    public static int GCD(int a, int b) {
        int c;
        while (b != 0) {
            c = a % b;
            a = b;
            b = c;
        }
        return a;
    }

    /**
     * Returns the Least Common Multiple (LCM).
     *
     * @param a the first integer.
     * @param b the second integer.
     * @return the LCM.
     */
    public static int LCM(int a, int b) {
        int c;
        if ((c = GCD(a, b)) == 0)
            return 0;
        if (a > b) {
            return (a / c) * b;
        } else {
            return (b / c) * a;
        }
    }

    /**
     * Returns the base 10 logarithm of a double.
     *
     * @param x a double.
     * @return log10(x)
     */
    public static double log10(double x) {
        return Math.log(x) / MathConstants.LOG10.doubleValue();
    }

    /**
     * Returns the logarithm of a double in a given base.
     * @param d the value
     * @param d1 the base
     * @return log_d1(d)
     */
    public static double logB(double d, double d1) {
        return Math.log(d) / Math.log(d1);
    }

    /**
     * Returns the base 2 logarithm of a double.
     * @param d the value
     * @return log2(d)
     */
    public static double log2(double d) {
        return logB(d, 2.0);
    }

    /**
     * Returns the hyperbolic sine of a double.
     * @param x a double.
     * @return sinh(x)
     * @see Math#sinh(double)
     */
    public static double sinh(double x) {
        return Math.sinh(x);
    }

    /**
     * Returns the hyperbolic cosine of a double.
     * @param x a double.
     * @return cosh(x)
     * @see Math#cosh(double)
     */
    public static double cosh(double x) {
        return Math.cosh(x);
    }

    /**
     * Returns the hyperbolic tangent of a double.
     * @param x a double.
     * @return tanh(x)
     * @see Math#tanh(double)
     */
    public static double tanh(double x) {
        return Math.tanh(x);
    }

    /**
     * Returns the inverse hyperbolic sine of a double.
     * @param x a double.
     * @return asinh(x)
     */
    public static double asinh(double x) {
        return Math.log(x + Math.sqrt(x * x + 1.0));
    }

    /**
     * Returns the inverse hyperbolic cosine of a double.
     * @param x a double.
     * @return acosh(x)
     */
    public static double acosh(double x) {
        return Math.log(x + Math.sqrt(x * x - 1.0));
    }

    /**
     * Returns the inverse hyperbolic tangent of a double.
     * @param x a double.
     * @return atanh(x)
     */
    public static double atanh(double x) {
        return Math.log((1.0 + x) / (1.0 - x)) / 2.0;
    }

    /**
     * Returns the signum of a double.
     * @param x the double
     * @return the signum
     */
    public static int sign(double x) {
        return (int) Math.signum(x);
    }

    /**
     * Returns the maximum of three double values.
     * @param a first value
     * @param b second value
     * @param c third value
     * @return max(a, b, c)
     */
    public static double maxOf3(double a, double b, double c) {
        return Math.max(Math.max(a, b), c);
    }

    /**
     * Returns the middle of three double values.
     * @param a first value
     * @param b second value
     * @param c third value
     * @return the middle value
     */
    public static double midOf3(double a, double b, double c) {
        if (b > a) {
            if (c > b) return b;
            if (a > c) return a;
            return c;
        } else {
            if (c > a) return a;
            if (b > c) return b;
            return c;
        }
    }

    /**
     * Returns the minimum of three double values.
     * @param a first value
     * @param b second value
     * @param c third value
     * @return min(a, b, c)
     */
    public static double minOf3(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }

    /**
     * Produces a double between 0 and 1 using the sigmoid function.
     * @param x the value
     * @return sigmoid(x)
     */
    public static double sigmoid(double x) {
        return (1.0 / (1.0 + Math.exp(-x)));
    }

    /**
     * Produces a double between 0 and 1 using a flattened sigmoid function.
     * @param x the value
     * @param flatteningFactor the factor
     * @return flattened sigmoid
     */
    public static double sigmoid(double x, double flatteningFactor) {
        return (1.0 / (1.0 + Math.exp(-x / flatteningFactor)));
    }

    /**
     * Returns the sum of all individual digits of a number.
     * @param number the number
     * @return the sum of digits
     */
    public static int sumOfDigits(long number) {
        int sum = 0;
        long tmp = Math.abs(number);
        while (tmp > 0) {
            sum = sum + (int) (tmp % 10);
            tmp = tmp / 10;
        }
        return sum;
    }

    private static final double[] pascal01 = {1.};
    private static final double[] pascal02 = {1., 1.};
    private static final double[] pascal03 = {1., 2., 1.};
    private static final double[] pascal04 = {1., 3., 3., 1.};
    private static final double[] pascal05 = {1., 4., 6., 4., 1.};
    private static final double[] pascal06 = {1., 5., 10., 10., 5., 1.};
    private static final double[] pascal07 = {1., 6., 15., 20., 15., 6., 1.};
    private static final double[] pascal08 = {1., 7., 21., 35., 35., 21., 7., 1.};
    private static final double[] pascal09 = {1., 8., 28., 56., 70., 56., 28., 8., 1.};
    private static final double[] pascal10 = {1., 9., 36., 84., 126., 126., 84., 36., 9., 1.};
    private static final double[] pascal11 = {1., 10., 45., 120., 210., 252., 210., 120., 45., 10., 1.};

    private static final double[][] predefPascal = {
        null, pascal01, pascal02, pascal03, pascal04, pascal05,
        pascal06, pascal07, pascal08, pascal09, pascal10, pascal11
    };

    /**
     * Returns the n-th row of Pascal's triangle.
     *
     * @param n the row index (number of elements in the row)
     * @return the row as an array of doubles.
     */
    public static double[] pascalTriangle(int n) {
        if (n <= 0) {
            return predefPascal[0];
        } else if (n < predefPascal.length) {
            return predefPascal[n];
        } else {
            double[] pascal = new double[n];
            double[] lastPredefPascal = predefPascal[predefPascal.length - 1];
            for (int i = 0; i < lastPredefPascal.length; i++)
                pascal[i] = lastPredefPascal[i];

            for (int i = lastPredefPascal.length; i < n; i++) {
                pascal[i] = 1.0;
                for (int j = i - 1; j > 0; j--)
                    pascal[j] = pascal[j - 1] + pascal[j];
            }
            return pascal;
        }
    }
}
