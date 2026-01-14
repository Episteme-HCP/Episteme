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

package org.jscience.tests.mathematics.analysis.roots;

import org.jscience.tests.mathematics.analysis.functions.FunctionException;
import org.jscience.tests.mathematics.analysis.functions.scalar.ComputableFunction;

import java.util.ArrayList;


/**
 * This class implement a reference problem for junit tests.
 */
public abstract class TestProblem implements ComputableFunction {
    /** DOCUMENT ME! */
    private double a;

    /** DOCUMENT ME! */
    private double b;

    /** DOCUMENT ME! */
    private double expectedRoot;

/**
     * Creates a new TestProblem object.
     *
     * @param a            DOCUMENT ME!
     * @param b            DOCUMENT ME!
     * @param expectedRoot DOCUMENT ME!
     */
    protected TestProblem(double a, double b, double expectedRoot) {
        this.a = a;
        this.b = b;
        this.expectedRoot = expectedRoot;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getA() {
        return a;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getB() {
        return b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getExpectedRoot() {
        return expectedRoot;
    }

    /**
     * DOCUMENT ME!
     *
     * @param foundRoot DOCUMENT ME!
     * @param tol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean checkResult(double foundRoot, double tol) {
        return Math.abs(foundRoot - expectedRoot) <= tol;
    }

    /**
     * Get the reference problems from G. E. Alefeld, F. A. Potra and
     * Y. Shi.
     *
     * @return DOCUMENT ME!
     */
    public static TestProblem[] getAPSProblems() {
        ArrayList problems = new ArrayList();

        // problem 1
        problems.add(new APSProblem1(Math.PI / 2, Math.PI, 1.8954942670340));

        // problems 2 to 11
        double[] roots2To11 = {
                3.0229153472731, 6.6837535608081, 11.238701655002,
                19.676000080623, 29.828227326505, 41.906116195289,
                55.953595800143, 71.985665586588, 90.008868539167,
                110.02653274833
            };

        for (int k = 0, n = 1; n <= 10; ++n) {
            problems.add(new APSProblems2To11(1.0e-9 + (n * n),
                    ((n + 1) * (n + 1)) - 1.0e-9, roots2To11[k++]));
        }

        // problems 12 to 14
        problems.add(new APSProblems12To14(-40, -9.0, 31.0, 0.0));
        problems.add(new APSProblems12To14(-100, -9.0, 31.0, 0.0));
        problems.add(new APSProblems12To14(-200, -9.0, 31.0, 0.0));

        // problems 15 to 17
        int[] n15 = { 4, 6, 8, 10, 12 };
        double[] roots15 = {
                0.66874030497642, 0.76472449133173, 0.81776543395794,
                0.85133992252078, 0.87448527222117
            };

        for (int k = 0; k < n15.length; ++k) {
            problems.add(new APSProblems15To17(n15[k], 0.2, 0.0, 5.0, roots15[k]));
        }

        int[] n16 = { 4, 6, 8, 10, 12 };

        for (int k = 0; k < n16.length; ++k) {
            problems.add(new APSProblems15To17(n16[k], 1.0, 0.0, 5.0, 1.0));
        }

        int[] n17 = { 8, 10, 12, 14 };

        for (int k = 0; k < n17.length; ++k) {
            problems.add(new APSProblems15To17(n17[k], 1.0, -0.95, 4.05, 1.0));
        }

        // problem 18
        problems.add(new APSProblem18(0.0, 1.5, 0.52359877559830));

        // problem 19
        int[] n19 = { 1, 2, 3, 4, 5, 20, 40, 60, 80, 100 };
        double[] roots19 = {
                0.42247770964124, 0.30669941048320, 0.22370545765466,
                0.17171914751951, 0.13825715505682, 3.4657359020854e-2,
                1.7328679513999e-2, 1.1552453009332e-2, 8.6643397569993e-3,
                6.9314718055995e-3
            };

        for (int k = 0; k < n19.length; ++k) {
            problems.add(new APSProblem19(n19[k], 0.0, 1.0, roots19[k]));
        }

        // problem 20
        int[] n20 = { 5, 10, 20 };
        double[] roots20 = {
                3.8402551840622e-2, 9.9000099980005e-3, 2.4937500390620e-3
            };

        for (int k = 0; k < n20.length; ++k) {
            problems.add(new APSProblem20(n20[k], 0.0, 1.0, roots20[k]));
        }

        // problem 21
        int[] n21 = { 2, 5, 10, 15, 20 };
        double[] roots21 = {
                0.5, 0.34595481584824, 0.24512233375331, 0.19554762353657,
                0.16492095727644
            };

        for (int k = 0; k < n21.length; ++k) {
            problems.add(new APSProblem21(n21[k], 0.0, 1.0, roots21[k]));
        }

        // problem 22
        int[] n22 = { 1, 2, 4, 5, 8, 15, 20 };
        double[] roots22 = {
                0.27550804099948, 0.13775402049974, 1.0305283778156e-2,
                3.6171081789041e-3, 4.1087291849640e-4, 2.5989575892908e-5,
                7.6685951221853e-6
            };

        for (int k = 0; k < n22.length; ++k) {
            problems.add(new APSProblem22(n22[k], 0.0, 1.0, roots22[k]));
        }

        // problem 23
        int[] n23 = { 1, 5, 10, 15, 20 };
        double[] roots23 = {
                0.40105813754155, 0.51615351875793, 0.53952222690842,
                0.54818229434066, 0.55270466667849
            };

        for (int k = 0; k < n23.length; ++k) {
            problems.add(new APSProblem23(n23[k], 0.0, 1.0, roots23[k]));
        }

        // problem 24
        int[] n24 = { 2, 5, 15, 20 };

        for (int k = 0; k < n24.length; ++k) {
            problems.add(new APSProblem24(n24[k], 0.01, 1, 1.0 / n24[k]));
        }

        // problem 25
        int[] n25 = {
                2, 3, 4, 5, 6, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31,
                33
            };

        for (int k = 0; k < n25.length; ++k) {
            problems.add(new APSProblem25(n25[k], 1.0, 100.0, n25[k]));
        }

        // problem 26
        problems.add(new APSProblem26(-1.0, 4.0, 0.0));

        // problem 27
        int[] n27 = {
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
                19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34,
                35, 36, 37, 38, 39, 40
            };

        for (int k = 0; k < n27.length; ++k) {
            problems.add(new APSProblem27(n27[k], -10000.0, Math.PI / 2,
                    0.62380651896161));
        }

        // problem 28
        int[] n28 = {
                20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35,
                36, 37, 38, 39, 40, 100, 200, 300, 400, 500, 600, 700, 800, 900,
                1000
            };
        double[] roots28 = {
                5.9051305594220e-5, 5.6367155339937e-5, 5.3916409455592e-5,
                5.1669892394942e-5, 4.9603096699145e-5, 4.7695285287639e-5,
                4.5928793239949e-5, 4.4288479195665e-5, 4.2761290257883e-5,
                4.1335913915954e-5, 4.0002497338020e-5, 3.8752419296207e-5,
                3.7578103559958e-5, 3.6472865219959e-5, 3.5430783356532e-5,
                3.4446594929961e-5, 3.3515605877800e-5, 3.2633616249437e-5,
                3.1796856858426e-5, 3.1001935436965e-5, 3.0245790670210e-5,
                1.2277994232462e-5, 6.1695393904409e-6, 4.1198585298293e-6,
                3.0924623877272e-6, 2.4752044261050e-6, 2.0633567678513e-6,
                1.7690120078154e-6, 1.5481615698859e-6, 1.3763345366022e-6,
                1.2388385788997e-6
            };

        for (int k = 0; k < n28.length; ++k) {
            problems.add(new APSProblem28(n28[k], -10000.0, 10000.0, roots28[k]));
        }

        return (TestProblem[]) problems.toArray(new TestProblem[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class APSProblem1 extends TestProblem {
/**
         * Creates a new APSProblem1 object.
         *
         * @param a            DOCUMENT ME!
         * @param b            DOCUMENT ME!
         * @param expectedRoot DOCUMENT ME!
         */
        public APSProblem1(double a, double b, double expectedRoot) {
            super(a, b, expectedRoot);
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            return Math.sin(x) - (x / 2);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class APSProblems2To11 extends TestProblem {
/**
         * Creates a new APSProblems2To11 object.
         *
         * @param a            DOCUMENT ME!
         * @param b            DOCUMENT ME!
         * @param expectedRoot DOCUMENT ME!
         */
        public APSProblems2To11(double a, double b, double expectedRoot) {
            super(a, b, expectedRoot);
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            double f = 0;

            for (int i = 1; i <= 20; ++i) {
                double n = (2.0 * i) - 5.0;
                double d = x - (i * i);
                f += ((n * n) / (d * d * d));
            }

            return -2 * f;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class APSProblems12To14 extends TestProblem {
        /** DOCUMENT ME! */
        private int n;

/**
         * Creates a new APSProblems12To14 object.
         *
         * @param n            DOCUMENT ME!
         * @param a            DOCUMENT ME!
         * @param b            DOCUMENT ME!
         * @param expectedRoot DOCUMENT ME!
         */
        public APSProblems12To14(int n, double a, double b, double expectedRoot) {
            super(a, b, expectedRoot);
            this.n = n;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            return n * x * Math.exp(-x);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class APSProblems15To17 extends TestProblem {
        /** DOCUMENT ME! */
        private int n;

        /** DOCUMENT ME! */
        private double u;

/**
         * Creates a new APSProblems15To17 object.
         *
         * @param n            DOCUMENT ME!
         * @param u            DOCUMENT ME!
         * @param a            DOCUMENT ME!
         * @param b            DOCUMENT ME!
         * @param expectedRoot DOCUMENT ME!
         */
        public APSProblems15To17(int n, double u, double a, double b,
            double expectedRoot) {
            super(a, b, expectedRoot);
            this.n = n;
            this.u = u;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            return Math.pow(x, n) - u;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class APSProblem18 extends TestProblem {
/**
         * Creates a new APSProblem18 object.
         *
         * @param a            DOCUMENT ME!
         * @param b            DOCUMENT ME!
         * @param expectedRoot DOCUMENT ME!
         */
        public APSProblem18(double a, double b, double expectedRoot) {
            super(a, b, expectedRoot);
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            return Math.sin(x) - 0.5;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class APSProblem19 extends TestProblem {
        /** DOCUMENT ME! */
        private int n;

/**
         * Creates a new APSProblem19 object.
         *
         * @param n            DOCUMENT ME!
         * @param a            DOCUMENT ME!
         * @param b            DOCUMENT ME!
         * @param expectedRoot DOCUMENT ME!
         */
        public APSProblem19(int n, double a, double b, double expectedRoot) {
            super(a, b, expectedRoot);
            this.n = n;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            return (2.0 * x * Math.exp(-n)) - (2.0 * Math.exp(-n * x)) + 1.0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class APSProblem20 extends TestProblem {
        /** DOCUMENT ME! */
        private int n;

        /** DOCUMENT ME! */
        private int oPoMn2;

/**
         * Creates a new APSProblem20 object.
         *
         * @param n            DOCUMENT ME!
         * @param a            DOCUMENT ME!
         * @param b            DOCUMENT ME!
         * @param expectedRoot DOCUMENT ME!
         */
        public APSProblem20(int n, double a, double b, double expectedRoot) {
            super(a, b, expectedRoot);
            this.n = n;

            int oMn = 1 - n;
            oPoMn2 = 1 + (oMn * oMn);
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            double v = 1.0 - (n * x);

            return (oPoMn2 * x) - (v * v);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class APSProblem21 extends TestProblem {
        /** DOCUMENT ME! */
        private int n;

/**
         * Creates a new APSProblem21 object.
         *
         * @param n            DOCUMENT ME!
         * @param a            DOCUMENT ME!
         * @param b            DOCUMENT ME!
         * @param expectedRoot DOCUMENT ME!
         */
        public APSProblem21(int n, double a, double b, double expectedRoot) {
            super(a, b, expectedRoot);
            this.n = n;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            return (x * x) - Math.pow(1 - x, n);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class APSProblem22 extends TestProblem {
        /** DOCUMENT ME! */
        private int n;

        /** DOCUMENT ME! */
        private int oPoMn4;

/**
         * Creates a new APSProblem22 object.
         *
         * @param n            DOCUMENT ME!
         * @param a            DOCUMENT ME!
         * @param b            DOCUMENT ME!
         * @param expectedRoot DOCUMENT ME!
         */
        public APSProblem22(int n, double a, double b, double expectedRoot) {
            super(a, b, expectedRoot);
            this.n = n;

            int oMn = 1 - n;
            int oMn2 = oMn * oMn;
            oPoMn4 = 1 + (oMn2 * oMn2);
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            double oMnx = 1 - (n * x);
            double oMnx2 = oMnx * oMnx;

            return (oPoMn4 * x) - (oMnx2 * oMnx2);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class APSProblem23 extends TestProblem {
        /** DOCUMENT ME! */
        private int n;

/**
         * Creates a new APSProblem23 object.
         *
         * @param n            DOCUMENT ME!
         * @param a            DOCUMENT ME!
         * @param b            DOCUMENT ME!
         * @param expectedRoot DOCUMENT ME!
         */
        public APSProblem23(int n, double a, double b, double expectedRoot) {
            super(a, b, expectedRoot);
            this.n = n;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            return ((x - 1.0) * Math.exp(-n * x)) + Math.pow(x, n);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class APSProblem24 extends TestProblem {
        /** DOCUMENT ME! */
        private int n;

/**
         * Creates a new APSProblem24 object.
         *
         * @param n            DOCUMENT ME!
         * @param a            DOCUMENT ME!
         * @param b            DOCUMENT ME!
         * @param expectedRoot DOCUMENT ME!
         */
        public APSProblem24(int n, double a, double b, double expectedRoot) {
            super(a, b, expectedRoot);
            this.n = n;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            return ((n * x) - 1.0) / ((n - 1) * x);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class APSProblem25 extends TestProblem {
        /** DOCUMENT ME! */
        private double u;

        /** DOCUMENT ME! */
        private double v;

/**
         * Creates a new APSProblem25 object.
         *
         * @param n            DOCUMENT ME!
         * @param a            DOCUMENT ME!
         * @param b            DOCUMENT ME!
         * @param expectedRoot DOCUMENT ME!
         */
        public APSProblem25(int n, double a, double b, double expectedRoot) {
            super(a, b, expectedRoot);
            u = 1.0 / n;
            v = Math.pow(n, u);
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            return Math.pow(x, u) - v;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class APSProblem26 extends TestProblem {
/**
         * Creates a new APSProblem26 object.
         *
         * @param a            DOCUMENT ME!
         * @param b            DOCUMENT ME!
         * @param expectedRoot DOCUMENT ME!
         */
        public APSProblem26(double a, double b, double expectedRoot) {
            super(a, b, expectedRoot);
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            if (x == 0.0) {
                return 0;
            } else {
                return x / Math.exp(1 / (x * x));
            }
        }

        // this is a very special case since there is a wide range around
        /**
         * DOCUMENT ME!
         *
         * @param foundRoot DOCUMENT ME!
         * @param tol DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean checkResult(double foundRoot, double tol) {
            return Math.abs(foundRoot) <= 0.03768;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class APSProblem27 extends TestProblem {
        /** DOCUMENT ME! */
        private double u;

/**
         * Creates a new APSProblem27 object.
         *
         * @param n            DOCUMENT ME!
         * @param a            DOCUMENT ME!
         * @param b            DOCUMENT ME!
         * @param expectedRoot DOCUMENT ME!
         */
        public APSProblem27(int n, double a, double b, double expectedRoot) {
            super(a, b, expectedRoot);
            u = n / 20.0;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            if (x >= 0.0) {
                return (((x / 1.5) + Math.sin(x)) - 1.0) * u;
            } else {
                return -u;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private static class APSProblem28 extends TestProblem {
        /** DOCUMENT ME! */
        private double threshold;

        /** DOCUMENT ME! */
        private double yHigh;

        /** DOCUMENT ME! */
        private int u;

/**
         * Creates a new APSProblem28 object.
         *
         * @param n            DOCUMENT ME!
         * @param a            DOCUMENT ME!
         * @param b            DOCUMENT ME!
         * @param expectedRoot DOCUMENT ME!
         */
        public APSProblem28(int n, double a, double b, double expectedRoot) {
            super(a, b, expectedRoot);
            threshold = 0.002 / (1 + n);
            yHigh = Math.exp(1.0) - 1.859;
            u = (n + 1) * 500;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            if (x >= threshold) {
                return yHigh;
            } else if (x >= 0) {
                return Math.exp(u * x) - 1.859;
            } else {
                return -0.859;
            }
        }
    }
}
