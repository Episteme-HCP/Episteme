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

package org.jscience.mathematics.analysis;

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.algebraic.numbers.Complex;

/**
 * The numerical math library.
 * This class cannot be subclassed or instantiated because all methods are static.
 *
 * @author Mark Hale, Silvere Martin-Michiellot
 * @version 0.9
 */
public final class NumericalMathUtils extends Object {
    private NumericalMathUtils() {
    }

    /**
     * Calculates the roots of the linear equation, a must be different from 0.
     * ax+b=0.
     *
     * @return an array containing the root.
     */
    public static double[] solveLinear(final double a, final double b) {
        final double roots[] = new double[1];
        roots[0] = -b / a;
        return roots;
    }

    /**
     * Calculates the roots of the quadratic equation, a must be different from 0 (or use linear equation solver).
     * Furthermore, if b*b-4.0*a*c < 0 then the solution is not defined on R.
     * ax<sup>2</sup>+bx+c=0.
     *
     * @return an array containing the two roots.
     */
    public static double[] solveQuadratic(final double a, final double b, final double c) {
        final double roots[] = new double[2];
        final double q = Math.sqrt(b * b - 4.0 * a * c);
        roots[0] = (-b - q) / (2 * a);
        roots[1] = (-b + q) / (2 * a);
        return roots;
    }

    /**
     * Calculates the roots of the cubic equation, a must be different from 0 (or use quadratic equation solver).
     * Furthermore, there may be only one real solution.
     * ax<sup>3</sup>+bx<sup>2</sup>+cx+d=0.
     *
     * @return an array containing the three roots.
     */
    //as found at http://www.josechu.com/ecuaciones_polinomicas/
    public static double[] solveCubic(final double a, final double b, final double c, final double d) {
        final double roots[] = new double[3];
        final double twoonethird = Math.pow(2, (1 / 3));
        final double delta = Math.pow((-2 * b * b * b + 9 * a * b * c - 27 * a * a * d + Math.sqrt(4 * Math.pow((-b * b + 3 * a * c), 3) + Math.pow((-2 * b * b * b + 9 * a * b * c - 27 * a * a * d), 2))), (1 / 3));
//we can further improve this computation
        roots[0] = -b / (3 * a) - (twoonethird * (-b * b + 3 * a * c)) / (3 * a * delta) + delta / (3 * twoonethird * a);
        //roots[1]= -b/(3*a) + ((1 + i*Math.sqrt(3))*(-b*b + 3*a*c))/(3*Math.pow(2,(2/3))*a*delta) - (1 - i*Math.sqrt(3))*delta/(6*twoonethird *a);
        //roots[2]= -b/(3*a) + ((1 - i*Math.sqrt(3))*(-b*b + 3*a*c))/(3*Math.pow(2,(2/3))*a*delta) - (1 + i*Math.sqrt(3))*delta/(6*twoonethird *a);
//we return the norm of the complex roots although there may be a better solution like thworing an error when imaginary part is not null
        roots[1] = new Complex(1, Math.sqrt(3)).multiply(-b * b + 3 * a * c).divide(3 * Math.pow(2, (2 / 3)) * a * delta).subtract(new Complex(1, -Math.sqrt(3)).multiply(delta / (6 * twoonethird * a))).subtractReal(b / (3 * a)).norm();
        roots[2] = new Complex(1, -Math.sqrt(3)).multiply(-b * b + 3 * a * c).divide(3 * Math.pow(2, (2 / 3)) * a * delta).subtract(new Complex(1, Math.sqrt(3)).multiply(delta / (6 * twoonethird * a))).subtractReal(b / (3 * a)).norm();
        return roots;
    }

    /**
     * Calculates the roots of the quartics equation, a must be different from 0 (or use cubic equation solver).
     * ax<sup>4</sup>+bx<sup>3</sup>+cx<sup>2</sup>+dx+e=0.
     * Furthermore, there may be no roots in R.
     *
     * @return an array containing the four roots.
     */
    //as found at http://www.josechu.com/ecuaciones_polinomicas/
    public static double[] solveQuartic(final double a, final double b, final double c, final double d, final double e) {
        final double roots[] = new double[4];
        final double twoonethird = Math.pow(2, (1 / 3));
        final double delta0 = c * c - 3 * b * d + 12 * a * e;
        final double delta1 = Math.pow(2 * c * c * c - 9 * b * c * d + 27 * a * d * d + 27 * b * b * e - 72 * a * c * e + Math.sqrt(-4 * Math.pow(delta0, 3) + Math.pow(2 * c * c * c - 9 * b * c * d + 27 * a * d * d + 27 * b * b * e - 72 * a * c * e, 2)), 1 / 3);
        final double delta2 = twoonethird * delta0 / (3 * a * delta1);
        final double delta3 = delta1 / (3 * a * twoonethird);
        final double delta4 = 0.5 * Math.sqrt(b * b / (4 * a * a) - 2 * c / (3 * a) + delta2 + delta3);
        final double delta5 = 0.5 * Math.sqrt(b * b / (2 * a * a) - 4 * c / (3 * a) - delta2 - delta3);
        final double delta6 = (-b * b * b / (a * a * a) + 4 * b * c / (a * a) - 8 * d / a) / (4 * Math.sqrt(b * b / (4 * a * a) - 2 * c / (3 * a) + delta2 + delta3));
        roots[0] = -b / (4 * a) - delta4 - delta5 - delta6;
        roots[0] = -b / (4 * a) - delta4 + delta5 - delta6;
        roots[0] = -b / (4 * a) + delta4 - delta5 + delta6;
        roots[0] = -b / (4 * a) + delta4 + delta5 + delta6;
        return roots;
    }

    /**
     * Uses the Euler method to solve an ODE.
     *
     * @param y    an array to be filled with y values, set y[0] to initial condition.
     * @param func dy/dt as a function of y.
     * @param dt   step size.
     * @return y.
     */
    public static double[] euler(final double y[], final PrimitiveMapping func, final double dt) {
        for (int i = 0; i < y.length - 1; i++)
            y[i + 1] = y[i] + dt * func.map(y[i]);
        return y;
    }

    /**
     * Uses the Leap-Frog method to solve an ODE.
     *
     * @param y    an array to be filled with y values, set y[0], y[1] to initial conditions.
     * @param func dy/dt as a function of y.
     * @param dt   step size.
     * @return y.
     */
    public static double[] leapFrog(final double y[], final PrimitiveMapping func, final double dt) {
        final double two_dt = 2.0 * dt;
        for (int i = 1; i < y.length - 1; i++)
            y[i + 1] = y[i - 1] + two_dt * func.map(y[i]);
        return y;
    }

    /**
     * Uses the 2nd order Runge-Kutta method to solve an ODE.
     *
     * @param y    an array to be filled with y values, set y[0] to initial condition.
     * @param func dy/dt as a function of y.
     * @param dt   step size.
     * @return y.
     */
    public static double[] rungeKutta2(final double y[], final PrimitiveMapping func, final double dt) {
        final double dt2 = dt / 2.0;
        for (int i = 0; i < y.length - 1; i++)
            y[i + 1] = y[i] + dt * func.map(y[i] + dt2 * func.map(y[i]));
        return y;
    }

    /**
     * Uses the 2nd order Runge-Kutta method to solve an ODE.
     *
     * @param y    an array to be filled with y values, set y[0] to initial condition.
     * @param func dy/dt as a function of y and t.
     * @param t0   initial time.
     * @param dt   step size.
     * @return y.
     */
    public static double[] rungeKutta2(final double y[], final DoubleFunction2D func, final double t0, final double dt) {
        final double dt2 = dt / 2.0;
        double t = t0;
        for (int i = 0; i < y.length - 1; i++) {
            y[i + 1] = y[i] + dt * func.map(y[i] + dt2 * func.map(y[i], t), t + dt2);
            t += dt;
        }
        return y;
    }

    /**
     * Uses the 4th order Runge-Kutta method to solve an ODE.
     *
     * @param y    an array to be filled with y values, set y[0] to initial condition.
     * @param func dy/dt as a function of y.
     * @param dt   step size.
     * @return y.
     */
    public static double[] rungeKutta4(final double y[], final PrimitiveMapping func, final double dt) {
        double k1, k2, k3, k4;
        for (int i = 0; i < y.length - 1; i++) {
            k1 = dt * func.map(y[i]);
            k2 = dt * func.map(y[i] + k1 / 2.0);
            k3 = dt * func.map(y[i] + k2 / 2.0);
            k4 = dt * func.map(y[i] + k3);
            y[i + 1] = y[i] + (k1 + k4) / 6.0 + (k2 + k3) / 3.0;
        }
        return y;
    }

    /**
     * Uses the 4th order Runge-Kutta method to solve an ODE.
     *
     * @param y    an array to be filled with y values, set y[0] to initial condition.
     * @param func dy/dt as a function of y and t.
     * @param dt   step size.
     * @return y.
     */
    public static double[] rungeKutta4(final double y[], final DoubleFunction2D func, final double t0, final double dt) {
        final double dt2 = dt / 2.0;
        double t = t0;
        double k1, k2, k3, k4;
        for (int i = 0; i < y.length - 1; i++) {
            k1 = dt * func.map(y[i], t);
            k2 = dt * func.map(y[i] + k1 / 2.0, t + dt2);
            k3 = dt * func.map(y[i] + k2 / 2.0, t + dt2);
            k4 = dt * func.map(y[i] + k3, t + dt);
            y[i + 1] = y[i] + (k1 + k4) / 6.0 + (k2 + k3) / 3.0;
            t += dt;
        }
        return y;
    }

    /**
     * Numerical integration using the trapezium rule.
     *
     * @param N    the number of strips to use.
     * @param func a function.
     * @param a    the first ordinate.
     * @param b    the last ordinate.
     */
    public static double trapezium(final int N, final PrimitiveMapping func, final double a, final double b) {
        double A = 0.0, x = a, h = (b - a) / N;
        for (int i = 0; i < N; i++) {
            A += func.map(x) + func.map(x + h);
            x += h;
        }
        return A * h / 2.0;
    }

    /**
     * Numerical integration using Simpson's rule.
     *
     * @param N    the number of strip pairs to use.
     * @param func a function.
     * @param a    the first ordinate.
     * @param b    the last ordinate.
     */
    public static double simpson(final int N, final PrimitiveMapping func, final double a, final double b) {
        double Ao = 0.0, Ae = 0.0, x = a;
        final double h = (b - a) / (2 * N);
        for (int i = 0; i < N - 1; i++) {
            Ao += func.map(x + h);
            Ae += func.map(x + 2 * h);
            x += 2.0 * h;
        }
        Ao += func.map(x + h);
        return h / 3.0 * (func.map(a) + 4.0 * Ao + 2.0 * Ae + func.map(b));
    }

    /**
     * Numerical integration using the Richardson extrapolation.
     *
     * @param N    the number of strip pairs to use (lower value).
     * @param func a function.
     * @param a    the first ordinate.
     * @param b    the last ordinate.
     */
    public static double richardson(final int N, final PrimitiveMapping func, final double a, final double b) {
        double Aa, Aao = 0.0, Aae = 0.0, Ab, Abo = 0.0, Abe = 0.0, x = a;
        final double ha = (b - a) / (2 * N);
        final double hb = ha / 2.0;
        for (int i = 0; i < N - 1; i++) {
            Aao += func.map(x + ha);
            Aae += func.map(x + 2.0 * ha);
            Abo += func.map(x + hb);
            Abe += func.map(x + 2 * hb);
            Abo += func.map(x + 3 * hb);
            Abe += func.map(x + 4 * hb);
            x += 2.0 * ha;
        }
        Aao += func.map(x + ha);
        Abo += func.map(x + hb);
        Abe += func.map(x + 2.0 * hb);
        Abo += func.map(x + 3.0 * hb);
        Aa = ha / 3.0 * (func.map(a) + 4.0 * Aao + 2.0 * Aae + func.map(b));
        Ab = hb / 3.0 * (func.map(a) + 4.0 * Abo + 2.0 * Abe + func.map(b));
        return (16.0 * Ab - Aa) / 15.0;
    }

    /**
     * Numerical integration using the Gaussian integration formula (4 points).
     *
     * @param N    the number of strips to use.
     * @param func a function.
     * @param a    the first ordinate.
     * @param b    the last ordinate.
     */
    public static double gaussian4(final int N, final PrimitiveMapping func, double a, final double b) {
        int n, i;
        double A = 0.0;
        final double h = (b - a) / N;
        final double h2 = h / 2.0;
        final double zeros[] = new double[4];
        final double coeffs[] = new double[4];
        zeros[2] = 0.339981043584856264802665759103;
        zeros[3] = 0.861136311594052575223946488893;
        zeros[0] = -zeros[3];
        zeros[1] = -zeros[2];
        coeffs[0] = coeffs[3] = 0.347854845137453857373063949222;
        coeffs[1] = coeffs[2] = 0.652145154862546142626936050778;
        for (n = 0; n < N; n++) {
            for (i = 0; i < zeros.length; i++)
                A += coeffs[i] * func.map(a + (zeros[i] + 1) * h2);
            a += h;
        }
        return A * h2;
    }

    /**
     * Numerical integration using the Gaussian integration formula (8 points).
     *
     * @param N    the number of strips to use.
     * @param func a function.
     * @param a    the first ordinate.
     * @param b    the last ordinate.
     */
    public static double gaussian8(final int N, final PrimitiveMapping func, double a, final double b) {
        int n, i;
        double A = 0.0;
        final double h = (b - a) / N;
        final double h2 = h / 2.0;
        final double zeros[] = new double[8];
        final double coeffs[] = new double[8];
        zeros[4] = 0.183434642495649804939476142360;
        zeros[5] = 0.525532409916328985817739049189;
        zeros[6] = 0.796666477413626739591553936476;
        zeros[7] = 0.960289856497536231683560868569;
        zeros[0] = -zeros[7];
        zeros[1] = -zeros[6];
        zeros[2] = -zeros[5];
        zeros[3] = -zeros[4];
        coeffs[0] = coeffs[7] = 0.101228536290376259152531354310;
        coeffs[1] = coeffs[6] = 0.222381034453374470544355994426;
        coeffs[2] = coeffs[5] = 0.313706645877887287337962201987;
        coeffs[3] = coeffs[4] = 0.362683783378361982965150449277;
        for (n = 0; n < N; n++) {
            for (i = 0; i < zeros.length; i++)
                A += coeffs[i] * func.map(a + (zeros[i] + 1) * h2);
            a += h;
        }
        return A * h2;
    }

    /**
     * Numerical differentiation.
     *
     * @param N    the number of points to use.
     * @param func a function.
     * @param a    the first ordinate.
     * @param b    the last ordinate.
     */
    public static double[] differentiate(final int N, final PrimitiveMapping func, final double a, final double b) {
        final double diff[] = new double[N];
        double x = a;
        final double dx = (b - a) / N;
        final double dx2 = dx / 2.0;
        for (int i = 0; i < N; i++) {
            diff[i] = (func.map(x + dx2) - func.map(x - dx2)) / dx;
            x += dx;
        }
        return diff;
    }

    /**
     * Numerical differentiation in multiple dimensions.
     *
     * @param func a function.
     * @param x    coordinates at which to differentiate about.
     * @param dx   step size.
     * @return an array M<sub>ij</sub>=df<sup>i</sup>/dx<sub>j</sub>.
     */
    public static double[][] differentiate(final PrimitiveMappingND func, final double x[], final double dx[]) {
        final double xplus[] = new double[x.length];
        final double xminus[] = new double[x.length];
        System.arraycopy(x, 0, xplus, 0, x.length);
        System.arraycopy(x, 0, xminus, 0, x.length);
        xplus[0] += dx[0];
        xminus[0] -= dx[0];
        double funcdiff[] = ArrayMathUtils.scalarMultiply(0.5 / dx[0], ArrayMathUtils.subtract(func.map(xplus), func.map(xminus)));
        final double diff[][] = new double[funcdiff.length][x.length];
        for (int i = 0; i < funcdiff.length; i++)
            diff[i][0] = funcdiff[i];
        for (int i, j = 1; j < x.length; j++) {
            System.arraycopy(x, 0, xplus, 0, x.length);
            System.arraycopy(x, 0, xminus, 0, x.length);
            xplus[j] += dx[j];
            xminus[j] -= dx[j];
            funcdiff = ArrayMathUtils.scalarMultiply(0.5 / dx[j], ArrayMathUtils.subtract(func.map(xplus), func.map(xminus)));
            for (i = 0; i < funcdiff.length; i++)
                diff[i][j] = funcdiff[i];
        }
        return diff;
    }

    /**
     * The Metropolis algorithm.
     *
     * @param list an array to be filled with values distributed according to func, set list[0] to initial value.
     * @param func distribution function.
     * @param dx   step size.
     * @return list.
     */
    public static double[] metropolis(final double list[], final PrimitiveMapping func, final double dx) {
        for (int i = 0; i < list.length - 1; i++) {
            list[i + 1] = list[i] + dx * (2.0 * Math.random() - 1.0);
            if (func.map(list[i + 1]) / func.map(list[i]) < Math.random())
                list[i + 1] = list[i];
        }
        return list;
    }
}
