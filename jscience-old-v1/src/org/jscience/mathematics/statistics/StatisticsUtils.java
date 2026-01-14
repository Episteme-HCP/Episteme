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

package org.jscience.mathematics.statistics;

/**
 * This class defines a collection of static functions that are useful in
 * probability.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public final class StatisticsUtils {
    //Constants
    /** DOCUMENT ME! */
    public final static int WITHOUT_REPLACEMENT = 0;

    //Constants
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    public final static int WITH_REPLACEMENT = 1;

    /**
     * This method takes an array of numbers and returns a probability
     * array (nonnegatie numbers that sum to 1). Negative numbers in the
     * original array are converted to zeros, and the remaining numbers scaled
     * by the sum of the numbers, assuming that not all of the original
     * numbers are 0. In the last case, the method returns the uniform
     * probabiltiy array.
     *
     * @param a the array of numbers
     *
     * @return the probability array
     */
    public static double[] getProbabilities(double[] a) {
        int n = a.length;
        double[] p = new double[n];
        double sum = 0;

        for (int i = 0; i < n; i++) {
            if (a[i] < 0) {
                a[i] = 0;
            } else {
                sum = sum + a[i];
            }
        }

        if (sum == 0) {
            for (int i = 0; i < n; i++)
                p[i] = 1.0 / n;
        } else {
            for (int i = 0; i < n; i++)
                p[i] = a[i] / sum;
        }

        return p;
    }

    /**
     * This method takes a real number and converts it to a probability
     * (a number between 0 and 1).
     *
     * @param p the given number
     *
     * @return the probability.
     */
    public static double getProbability(double p) {
        if (p < 0) {
            return 0;
        } else if (p > 1) {
            return 1;
        } else {
            return p;
        }
    }

    /**
     * This method returns an index between 0 and n - 1, for use in
     * error correcting in arrays.
     *
     * @param i the given index
     * @param n the specified size of the array
     *
     * @return the verified index between 0 and n - 1
     */
    public static int getIndex(int i, int n) {
        if (i < 0) {
            return 0;
        } else if (i > (n - 1)) {
            return n - 1;
        } else {
            return i;
        }
    }

    /**
     * This method computes the number of permutations of a specified
     * number of objects chosen from a population of a specified number of
     * objects.
     *
     * @param n the population size
     * @param k the sample size
     *
     * @return the number of ordered samples
     */
    public static double permutations(double n, int k) {
        double prod;

        if ((k > n) | (k < 0)) {
            return 0;
        } else {
            prod = 1;

            for (int i = 1; i <= k; i++)
                prod = prod * (n - i + 1);

            return prod;
        }
    }

    /**
     * This method computes the factorial function, the number of
     * permutations of a specified number of objects.
     *
     * @param k the number of objects
     *
     * @return the number of permutations of the objects
     */
    public static double factorial(int k) {
        return permutations(k, k);
    }

    /**
     * This method computes the number of combinations of a specified
     * number of objects chosen from a population of a specified size.
     *
     * @param n the population size
     * @param k the sample size
     *
     * @return the number of unordered samples
     */
    public static double combinations(double n, int k) {
        return permutations(n, k) / factorial(k);
    }

    /**
     * This method computes the log of the gamma function.
     *
     * @param x a positive number
     *
     * @return the log of the gamma function at x
     */
    public static double logGamma(double x) {
        double[] coef = {
                76.18009173, -86.50532033, 24.01409822, -1.231739516,
                0.00120858003, -0.00000536382
            };
        double step = 2.50662827465;
        double fpf = 5.5;
        double t;
        double tmp;
        double ser;
        double logGamma;
        t = x - 1;
        tmp = t + fpf;
        tmp = ((t + 0.5) * Math.log(tmp)) - tmp;
        ser = 1;

        for (int i = 1; i <= 6; i++) {
            t = t + 1;
            ser = ser + (coef[i - 1] / t);
        }

        return tmp + Math.log(step * ser);
    }

    /**
     * This method computes the gamma function.
     *
     * @param x a positive number
     *
     * @return the gamma function at x
     */
    public static double gamma(double x) {
        return Math.exp(logGamma(x));
    }

    /**
     * This method computes the cumulative distribution function of the
     * gamma distribution with a specified shape parameter and scale parameter
     * 1.
     *
     * @param x a positive number
     * @param a the shape parameter
     *
     * @return the cumulative probability at x
     */
    public static double gammaCDF(double x, double a) {
        if (x <= 0) {
            return 0;
        } else if (x < (a + 1)) {
            return gammaSeries(x, a);
        } else {
            return 1 - gammaCF(x, a);
        }
    }

    /**
     * This method computes a gamma series that is used in the gamma
     * cumulative distribution function.
     *
     * @param x a postive number
     * @param a the shape parameter
     *
     * @return the gamma series at x
     */
    private static double gammaSeries(double x, double a) {
        //Constants
        int maxit = 100;
        double eps = 0.0000003;

        //Variables
        double sum = 1.0 / a;

        //Variables
        double ap = a;

        //Variables
        double gln = logGamma(a);

        //Variables
        double del = sum;

        for (int n = 1; n <= maxit; n++) {
            ap++;
            del = (del * x) / ap;
            sum = sum + del;

            if (Math.abs(del) < (Math.abs(sum) * eps)) {
                break;
            }
        }

        return sum * Math.exp((-x + (a * Math.log(x))) - gln);
    }

    /**
     * This method computes a gamma continued fraction function
     * function that is used in the gamma cumulative distribution function.
     *
     * @param x a positive number
     * @param a the shape parameter
     *
     * @return the gamma continued fraction function at x
     */
    private static double gammaCF(double x, double a) {
        //Constants
        int maxit = 100;
        double eps = 0.0000003;

        //Variables
        double gln = logGamma(a);

        //Variables
        double g = 0;

        //Variables
        double gOld = 0;

        //Variables
        double a0 = 1;

        //Variables
        double a1 = x;

        //Variables
        double b0 = 0;

        //Variables
        double b1 = 1;

        //Variables
        double fac = 1;
        double an;
        double ana;
        double anf;

        for (int n = 1; n <= maxit; n++) {
            an = 1.0 * n;
            ana = an - a;
            a0 = (a1 + (a0 * ana)) * fac;
            b0 = (b1 + (b0 * ana)) * fac;
            anf = an * fac;
            a1 = (x * a0) + (anf * a1);
            b1 = (x * b0) + (anf * b1);

            if (a1 != 0) {
                fac = 1.0 / a1;
                g = b1 * fac;

                if (Math.abs((g - gOld) / g) < eps) {
                    break;
                }

                gOld = g;
            }
        }

        return Math.exp((-x + (a * Math.log(x))) - gln) * g;
    }

    /**
     * The method computes the beta cumulative distribution function.
     *
     * @param x a number between 0 and 1
     * @param a the left paramter
     * @param b the right parameter
     *
     * @return the beta cumulative probability at x
     */
    public static double betaCDF(double x, double a, double b) {
        double bt;

        if ((x == 0) | (x == 1)) {
            bt = 0;
        } else {
            bt = Math.exp(logGamma(a + b) - logGamma(a) - logGamma(b) +
                    (a * Math.log(x)) + (b * Math.log(1 - x)));
        }

        if (x < ((a + 1) / (a + b + 2))) {
            return (bt * betaCF(x, a, b)) / a;
        } else {
            return 1 - ((bt * betaCF(1 - x, b, a)) / b);
        }
    }

    /**
     * This method computes a beta continued fractions function that is
     * used in the beta cumulative distribution function.
     *
     * @param x a number between 0 and 1
     * @param a the left parameter
     * @param b the right parameter
     *
     * @return the beta continued fraction function at x
     */
    private static double betaCF(double x, double a, double b) {
        int maxit = 100;
        double eps = 0.0000003;
        double am = 1;
        double bm = 1;
        double az = 1;
        double qab = a + b;
        double qap = a + 1;
        double qam = a - 1;
        double bz = 1 - ((qab * x) / qap);
        double tem;
        double em;
        double d;
        double bpp;
        double bp;
        double app;
        double aOld;
        double ap;

        for (int m = 1; m <= maxit; m++) {
            em = m;
            tem = em + em;
            d = (em * (b - m) * x) / ((qam + tem) * (a + tem));
            ap = az + (d * am);
            bp = bz + (d * bm);
            d = (-(a + em) * (qab + em) * x) / ((a + tem) * (qap + tem));
            app = ap + (d * az);
            bpp = bp + (d * bz);
            aOld = az;
            am = ap / bpp;
            bm = bp / bpp;
            az = app / bpp;
            bz = 1;

            if (Math.abs(az - aOld) < (eps * Math.abs(az))) {
                break;
            }
        }

        return az;
    }

    /**
     * This method returns an approximation to the beta function.
     *
     * @param a the left value
     * @param b the right value
     *
     * @return the beta function at (a, b)
     */
    public static double beta(double a, double b) {
        return (gamma(a) * gamma(b)) / gamma(a + b);
    }

    /**
     * This method computes a sample of a specified size from a
     * specified population and of a specified type (with or without
     * replacement).
     *
     * @param p the population
     * @param n the sample size
     * @param t the type (0 without replacement, 1 with replacemen);
     *
     * @return DOCUMENT ME!
     */
    public static int[] getSample(int[] p, int n, int t) {
        int m = p.length;
        int temp;
        int k;
        int u;

        if (n < 1) {
            n = 1;
        } else if (n > m) {
            n = m;
        }

        //Define the sample
        int[] s = new int[n];

        if (t == WITH_REPLACEMENT) {
            for (int i = 0; i < n; i++) {
                u = (int) (m * Math.random());
                s[i] = p[u];
            }
        } else {
            for (int i = 0; i < n; i++) {
                //Select a random index from 0 to m - i - 1;
                k = m - i;
                u = (int) (k * Math.random());

                //Define the sample element
                s[i] = p[u];

                //Interchange the sampled element p[u] with p[k - 1], at the end of the
                // population so that it will not be sampled again.
                temp = p[k - 1];
                p[k - 1] = p[u];
                p[u] = temp;
            }
        }

        return s;
    }

    /**
     * This method computes a sample of a specified size from a
     * population of the form 1, 2, ..., m
     *
     * @param m the population size
     * @param n the sample size
     * @param t the type (0 without replacement, 1 with replacement)
     *
     * @return DOCUMENT ME!
     */
    public static int[] getSample(int m, int n, int t) {
        if (m < 1) {
            m = 1;
        }

        if (n < 1) {
            n = 1;
        } else if (n > m) {
            n = m;
        }

        int[] p = new int[m];

        //Define the population
        for (int i = 0; i < m; i++)
            p[i] = i + 1;

        return getSample(p, n, t);
    }

    /**
     * This method sorts an array of doubles.  Beware as this method is
     * probably suboptimal.
     *
     * @param a the array of doubles
     *
     * @return the sorted arry
     */

    // Beware as this method is probably suboptimal.
    public static double[] sort(double[] a) {
        boolean smallest;
        int n = a.length;
        double[] b = new double[n];

        for (int i = 0; i < n; i++) {
            smallest = true;

            for (int j = i - 1; j >= 0; j--) {
                if (b[j] <= a[i]) {
                    b[j + 1] = a[i];
                    smallest = false;

                    break;
                }

                b[j + 1] = b[j];
            }

            if (smallest) {
                b[0] = a[i];
            }
        }

        return b;
    }

    /**
     * This method sorts an array of integers. Beware as this method is
     * probably suboptimal.
     *
     * @param a the array of integers
     *
     * @return the sorted arry
     */

    // Beware as this method is probably suboptimal.
    public static int[] sort(int[] a) {
        boolean smallest;
        int n = a.length;
        int[] b = new int[n];

        for (int i = 0; i < n; i++) {
            smallest = true;

            for (int j = i - 1; j >= 0; j--) {
                if (b[j] <= a[i]) {
                    b[j + 1] = a[i];
                    smallest = false;

                    break;
                }

                b[j + 1] = b[j];
            }

            if (smallest) {
                b[0] = a[i];
            }
        }

        return b;
    }

    /**
     * This class method tests to see if a specified number is real.
     *
     * @param x the number
     *
     * @return true if the number is real and finite
     */
    public static boolean isReal(double x) {
        if (Double.isInfinite(x) | Double.isNaN(x)) {
            return false;
        } else {
            return true;
        }
    }
}
