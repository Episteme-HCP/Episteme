package org.jscience.mathematics;

/**
 * The extra math library.
 * Provides extra functions not in java.lang.Math class.
 * This class cannot be subclassed or instantiated because all methods are static.
 *
 * @author Mark Hale
 * @version 1.2
 */
//also look into org.jscience.mathematics.statistics.StatisticsUtils
public final class MathUtils extends Object {
    private MathUtils() {
    }

    /**
     * Rounds a number to so many significant figures.
     *
     * @param x           a number to be rounded.
     * @param significant number of significant figures to round to.
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
     */
    public static double hypot(final double x, final double y) {
        final double xAbs = Math.abs(x);
        final double yAbs = Math.abs(y);
        if (xAbs == 0.0 && yAbs == 0.0)
            return 0.0;
        else if (xAbs < yAbs)
            return yAbs * Math.sqrt(1.0 + (x / y) * (x / y));
        else
            return xAbs * Math.sqrt(1.0 + (y / x) * (y / x));
    }

    /**
     * Returns a<sup>b</sup>.
     *
     * @param a an integer.
     * @param b a positive integer.
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
                for (int i = 1; i < b; i++)
                    a *= a;
                return a;
            }
        }
    }

    /**
     * Returns 2<sup>a</sup>.
     *
     * @param a a positive integer.
     */
    public static int pow2(int a) {
        return 1 << a;
    }

    /**
     * Returns the factorial.
     * (Wrapper for the gamma function).
     *
     * @param x a double.
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
     * @planetmath PascalsRule
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
     * @param k a double btween 0 and n.
     * @planetmath BinomialCoefficient
     */
    public static double binomial(double n, double k) {
        return Math.exp(SpecialMathUtils.logGamma(n + 1.0) - SpecialMathUtils.logGamma(k + 1.0) - SpecialMathUtils.logGamma(n - k + 1.0));
    }

    //polynomial expansion, see http://en.wikipedia.org/wiki/Multinomial_theorem
    //also see GeometryUtils.pascalTriangle() for case number of variables equals 2
    //or MathUtils.binomial() for a specific coefficient of these
    //or ArrayMathUtils.getPowerN()
    /**
     * Returns the multinomial coefficient (n k[]).
     * Uses gamma functions.
     *
     * @param n   a double.
     * @param k[] a double array such that k1 + k2 + k3 + ... + km = n (unchecked).
     * @planetmath BinomialCoefficient
     */
    public static double polynomialExpansion(double n, double k[]) {
        double result;
        result = 0;
        for (int i = 0; i < k.length; i++) {
            result += SpecialMathUtils.logGamma(k[i] + 1.0);
        }
        return Math.exp(SpecialMathUtils.logGamma(n + 1.0) - result);
    }

    /*
    * ��̔񕉂�?�?���?ő���?� (Greatest Common Divider) ��Ԃ�?B
    * <p>
    * a �� 0 ��?�?��ɂ� b ��Ԃ�?B
    * </p>
    * <p>
    * b �� 0 ��?�?��ɂ� a ��Ԃ�?B
    * </p>
    *
    * @param a	?�?�1 (���ł��BĂ͂Ȃ�Ȃ�)
    * @param b	?�?�2 (���ł��BĂ͂Ȃ�Ȃ�)
    * @return	?ő���?�
    * @see	#LCM(int, int)
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

    /*
    * ��̔񕉂�?�?���?�?���{?� (Least Common Multiple) ��Ԃ�?B
    * <p>
    * a, b �̂����ꂩ?A���邢�͗��� 0 ��?�?��ɂ� 0 ��Ԃ�?B
    * </p>
    *
    * @param a	?�?�1 (���ł��BĂ͂Ȃ�Ȃ�)
    * @param b	?�?�2 (���ł��BĂ͂Ȃ�Ȃ�)
    * @return	?ő���?�
    * @see	#GCD(int, int)
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
     */
    public static double log10(double x) {
        return Math.log(x) / MathConstants.LOG10;
    }

    public static double logB(double d, double d1) {
        return Math.log(d) / Math.log(d1);
    }

    public static double log2(double d) {
        return logB(d, 2D);
    }

    /**
     * Returns the hyperbolic sine of a double.
     *
     * @param x a double.
     */
    public static double sinh(double x) {
        return (Math.exp(x) - Math.exp(-x)) / 2.0;
    }

    /**
     * Returns the hyperbolic cosine of a double.
     *
     * @param x a double.
     */
    public static double cosh(double x) {
        return (Math.exp(x) + Math.exp(-x)) / 2.0;
    }

    /**
     * Returns the hyperbolic tangent of a double.
     *
     * @param x a double.
     */
    public static double tanh(double x) {
        return sinh(x) / cosh(x);
    }

    /**
     * Returns the arc hyperbolic sine of a double,
     * in the range of -<img border=0 alt="infinity" src="doc-files/infinity.gif"> through <img border=0 alt="infinity" src="doc-files/infinity.gif">.
     *
     * @param x a double.
     */
    public static double asinh(double x) {
        return Math.log(x + Math.sqrt(x * x + 1.0));
    }

    /**
     * Returns the arc hyperbolic cosine of a double,
     * in the range of 0.0 through <img border=0 alt="infinity" src="doc-files/infinity.gif">.
     *
     * @param x a double.
     */
    public static double acosh(double x) {
        return Math.log(x + Math.sqrt(x * x - 1.0));
    }

    /**
     * Returns the arc hyperbolic tangent of a double,
     * in the range of -<img border=0 alt="infinity" src="doc-files/infinity.gif"> through <img border=0 alt="infinity" src="doc-files/infinity.gif">.
     *
     * @param x a double.
     */
    public static double atanh(double x) {
        return Math.log((1.0 + x) / (1.0 - x)) / 2.0;
    }

    public static int sign(double x) {
        if (x > 0) {
            return 1;
        } else {
            if (x < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * �O�̎�?��̓��?ő�l��Ԃ�?B
     *
     * @param a ��?�
     * @param b ��?�
     * @param c ��?�
     * @return �O�̎�?��̓��?ő�l
     * @see #midOf3(double,double,double)
     * @see #minOf3(double,double,double)
     */
    public static double maxOf3(double a, double b, double c) {
        return Math.max(Math.max(a, b), c);
    }

    /**
     * �O�̎�?��̓�̂܂񒆂̒l��Ԃ�?B
     *
     * @param a ��?�
     * @param b ��?�
     * @param c ��?�
     * @return �O�̎�?��̓�̂܂񒆂̒l
     * @see #maxOf3(double,double,double)
     * @see #minOf3(double,double,double)
     */
    public static double midOf3(double a, double b, double c) {
        if (b > a) {
            if (c > b) return b;    /* c > b > a */
            if (a > c) return a;    /* b > a > c */
            return c;            /* b >= c >= a */
        } else {    /* a >= b */
            if (c > a) return a;    /* c > a >= b */
            if (b > c) return b;    /* a >= b > c */
            return c;            /* a >= c >= b */
        }
    }

    /**
     * �O�̎�?��̓��?�?��l��Ԃ�?B
     *
     * @param a ��?�
     * @param b ��?�
     * @param c ��?�
     * @return �O�̎�?��̓��?�?��l
     * @see #maxOf3(double,double,double)
     * @see #midOf3(double,double,double)
     */
    public static double minOf3(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }

    /**
     * produces a double between 0 and 1 with <code>(sigmoid(0.5) == 0)</code>
     * being true
     */
    public static double sigmoid(double x) {
        return (1d / (1d + Math.exp(-x)));
    }

    /**
     * <code>sigmoid(double x)</code> is the same as <code>sigmoid(double x, 1)</code>,
     * large flatteningFactor flattens the curve
     */
    public static double sigmoid(double x, double flatteningFactor) {
        return (1d / (1d + Math.exp(-x / flatteningFactor)));
    }

    /**
     * returns the sum of all individual digits.
     * Example: <code>sumOfDigits(123045)</code> returns 15
     * (1+2+3+4+0+5).
     */
    public static int sumOfDigits(long number) {
        int sum = 0;
        long tmp = number;
        while (tmp > 0) {
            sum = sum + (int) (tmp % 10);
            tmp = tmp / 10;
        }
        return sum;
    }

    /*
    public static int sumOfDigits (long number) {
        int sum = 0;
        String s = new Long(number).toString();
        for (int i = 0; i < s.length(); i++) {
            sum = sum + s.charAt(i) - '0';
        }
        return sum;
    }
    */

    /**
     * �p�X�J���̎O�p�`�̊�v�Z�l (n = 1)
     *
     * @see #pascalTriangle(int)
     */
    private static double[] pascal01 = {1.};

    /**
     * �p�X�J���̎O�p�`�̊�v�Z�l (n = 2)
     *
     * @see #pascalTriangle(int)
     */
    private static double[] pascal02 = {1., 1.};

    /**
     * �p�X�J���̎O�p�`�̊�v�Z�l (n = 3)
     *
     * @see #pascalTriangle(int)
     */
    private static double[] pascal03 = {1., 2., 1.};

    /**
     * �p�X�J���̎O�p�`�̊�v�Z�l (n = 4)
     *
     * @see #pascalTriangle(int)
     */
    private static double[] pascal04 = {1., 3., 3., 1.};

    /**
     * �p�X�J���̎O�p�`�̊�v�Z�l (n = 5)
     *
     * @see #pascalTriangle(int)
     */
    private static double[] pascal05 = {1., 4., 6., 4., 1.};

    /**
     * �p�X�J���̎O�p�`�̊�v�Z�l (n = 6)
     *
     * @see #pascalTriangle(int)
     */
    private static double[] pascal06 = {1., 5., 10., 10., 5., 1.};

    /**
     * �p�X�J���̎O�p�`�̊�v�Z�l (n = 7)
     *
     * @see #pascalTriangle(int)
     */
    private static double[] pascal07 = {1., 6., 15., 20., 15., 6., 1.};

    /**
     * �p�X�J���̎O�p�`�̊�v�Z�l (n = 8)
     *
     * @see #pascalTriangle(int)
     */
    private static double[] pascal08 = {1., 7., 21., 35., 35., 21., 7., 1.};

    /**
     * �p�X�J���̎O�p�`�̊�v�Z�l (n = 9)
     *
     * @see #pascalTriangle(int)
     */
    private static double[] pascal09 = {1., 8., 28., 56., 70., 56., 28., 8., 1.};

    /**
     * �p�X�J���̎O�p�`�̊�v�Z�l (n = 10)
     *
     * @see #pascalTriangle(int)
     */
    private static double[] pascal10 = {1., 9., 36., 84., 126., 126., 84., 36., 9., 1.};

    /**
     * �p�X�J���̎O�p�`�̊�v�Z�l (n = 11)
     *
     * @see #pascalTriangle(int)
     */
    private static double[] pascal11 = {1., 10., 45., 120., 210., 252., 210., 120., 45., 10., 1.};

    /**
     * �p�X�J���̎O�p�`�̊�v�Z�l�̔z�� (n = 11 �܂�)
     *
     * @see #pascalTriangle(int)
     */
    private static double[][] predefPascal
            = {null,
            pascal01, pascal02, pascal03, pascal04, pascal05,
            pascal06, pascal07, pascal08, pascal09, pascal10,
            pascal11};

    /**
     * �p�X�J���̎O�p�`�̊�v�Z�l�̒��� n ��?ő�̂��
     *
     * @see #pascalTriangle(int)
     */
    private static double[] lastPredefPascal = pascal11;

    /**
     * ���R?� N �ɑ΂���p�X�J���̎O�p�`�̌W?���Ԃ�?B
     *
     * @param n ���R?� N
     * @return n �ɑ΂���p�X�J���̎O�p�`�̌W?��̔z��
     */
    public static double[] pascalTriangle(int n) {
        if (n <= 0) {
            return predefPascal[0];
        } else if (n < predefPascal.length) {
            return predefPascal[n];
        } else {
            double[] pascal = new double[n];

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

