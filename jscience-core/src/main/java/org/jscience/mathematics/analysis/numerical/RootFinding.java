/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.mathematics.analysis.numerical;

import org.jscience.mathematics.analysis.RealFunction;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Root finding algorithms for single-variable functions.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RootFinding {

    private static final int MAX_ITERATIONS = 100;
    private static final double DEFAULT_TOLERANCE = 1e-10;

    /**
     * Finds a root using the bisection method.
     */
    public static Real bisection(RealFunction f, Real a, Real b) {
        return bisection(f, a, b, DEFAULT_TOLERANCE);
    }

    public static Real bisection(RealFunction f, Real a, Real b, double tolerance) {
        Real fa = f.evaluate(a);
        Real fb = f.evaluate(b);

        if (Math.abs(fa.doubleValue()) < tolerance)
            return a;
        if (Math.abs(fb.doubleValue()) < tolerance)
            return b;

        if (fa.doubleValue() * fb.doubleValue() > 0) {
            throw new IllegalArgumentException("Function must have opposite signs at endpoints");
        }

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Real c = a.add(b).divide(Real.of(2.0));
            Real fc = f.evaluate(c);

            double bMinusA = b.subtract(a).doubleValue();
            if (Math.abs(fc.doubleValue()) < tolerance || bMinusA / 2.0 < tolerance) {
                return c;
            }

            if (fa.doubleValue() * fc.doubleValue() < 0) {
                b = c;
                fb = fc;
            } else {
                a = c;
                fa = fc;
            }
        }

        return a.add(b).divide(Real.of(2.0));
    }

    /**
     * Finds a root using Newton-Raphson method.
     */
    public static Real newtonRaphson(RealFunction f, RealFunction df, Real x0) {
        return newtonRaphson(f, df, x0, DEFAULT_TOLERANCE);
    }

    public static Real newtonRaphson(RealFunction f, RealFunction df, Real x0, double tolerance) {
        Real x = x0;

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Real fx = f.evaluate(x);
            if (Math.abs(fx.doubleValue()) < tolerance) {
                return x;
            }

            Real dfx = df.evaluate(x);
            if (Math.abs(dfx.doubleValue()) < 1e-15) {
                throw new ArithmeticException("Derivative too small, Newton-Raphson failed");
            }

            Real xNew = x.subtract(fx.divide(dfx));

            if (Math.abs(xNew.subtract(x).doubleValue()) < tolerance) {
                return xNew;
            }

            x = xNew;
        }

        return x;
    }

    /**
     * Finds a root using the secant method.
     */
    public static Real secant(RealFunction f, Real x0, Real x1) {
        return secant(f, x0, x1, DEFAULT_TOLERANCE);
    }

    public static Real secant(RealFunction f, Real x0, Real x1, double tolerance) {
        Real f0 = f.evaluate(x0);
        Real f1 = f.evaluate(x1);

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            if (Math.abs(f1.doubleValue()) < tolerance) {
                return x1;
            }

            Real diff = f1.subtract(f0);
            if (Math.abs(diff.doubleValue()) < 1e-15) {
                throw new ArithmeticException("Denominator too small, secant method failed");
            }

            Real x2 = x1.subtract(f1.multiply(x1.subtract(x0)).divide(diff));

            if (Math.abs(x2.subtract(x1).doubleValue()) < tolerance) {
                return x2;
            }

            x0 = x1;
            f0 = f1;
            x1 = x2;
            f1 = f.evaluate(x1);
        }

        return x1;
    }

    /**
     * Finds a root using Brent's method.
     */
    public static Real brent(RealFunction f, Real a, Real b) {
        return brent(f, a, b, DEFAULT_TOLERANCE);
    }

    public static Real brent(RealFunction f, Real a, Real b, double tolerance) {
        Real fa = f.evaluate(a);
        Real fb = f.evaluate(b);

        if (Math.abs(fa.doubleValue()) < tolerance)
            return a;
        if (Math.abs(fb.doubleValue()) < tolerance)
            return b;
        if (fa.doubleValue() * fb.doubleValue() > 0) {
            throw new IllegalArgumentException("Function must have opposite signs at endpoints");
        }

        if (Math.abs(fa.doubleValue()) < Math.abs(fb.doubleValue())) {
            Real temp = a;
            a = b;
            b = temp;
            temp = fa;
            fa = fb;
            fb = temp;
        }

        Real c = a;
        Real fc = fa;
        boolean mflag = true;
        Real s = Real.ZERO;
        Real d = Real.ZERO;

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            if (Math.abs(fb.doubleValue()) < tolerance || Math.abs(b.subtract(a).doubleValue()) < tolerance) {
                return b;
            }

            double faVal = fa.doubleValue();
            double fbVal = fb.doubleValue();
            double fcVal = fc.doubleValue();
            double aVal = a.doubleValue();
            double bVal = b.doubleValue();
            double cVal = c.doubleValue();

            double sVal;
            if (Math.abs(faVal - fcVal) > tolerance && Math.abs(fbVal - fcVal) > tolerance) {
                // Inverse quadratic interpolation
                sVal = aVal * fbVal * fcVal / ((faVal - fbVal) * (faVal - fcVal))
                        + bVal * faVal * fcVal / ((fbVal - faVal) * (fbVal - fcVal))
                        + cVal * faVal * fbVal / ((fcVal - faVal) * (fcVal - fbVal));
            } else {
                // Secant method
                sVal = bVal - fbVal * (bVal - aVal) / (fbVal - faVal);
            }

            double tmp2 = (3 * aVal + bVal) / 4;
            double dVal = d.doubleValue();

            if (!(((sVal > tmp2) && (sVal < bVal)) || ((sVal < tmp2) && (sVal > bVal)))
                    || (mflag && (Math.abs(sVal - bVal) >= Math.abs(bVal - cVal) / 2))
                    || (!mflag && (Math.abs(sVal - bVal) >= Math.abs(cVal - dVal) / 2))
                    || (mflag && (Math.abs(bVal - cVal) < tolerance))
                    || (!mflag && (Math.abs(cVal - dVal) < tolerance))) {
                sVal = (aVal + bVal) / 2;
                mflag = true;
            } else {
                mflag = false;
            }

            s = Real.of(sVal);
            Real fs = f.evaluate(s);
            d = c;
            c = b;
            fc = fb;

            if (fa.doubleValue() * fs.doubleValue() < 0) {
                b = s;
                fb = fs;
            } else {
                a = s;
                fa = fs;
            }

            if (Math.abs(fa.doubleValue()) < Math.abs(fb.doubleValue())) {
                Real temp = a;
                a = b;
                b = temp;
                temp = fa;
                fa = fb;
                fb = temp;
            }
        }

        return b;
    }
}
