package org.jscience.mathematics.analysis.roots;

import java.util.function.DoubleUnaryOperator;

/**
 * Root finding algorithms.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class RootFinder {

    private final double tolerance;
    private final int maxIterations;

    public RootFinder(double tolerance, int maxIterations) {
        this.tolerance = tolerance;
        this.maxIterations = maxIterations;
    }

    public RootFinder() {
        this(1e-12, 100);
    }

    /**
     * Bisection method (guaranteed convergence if root exists in interval).
     */
    public double bisection(DoubleUnaryOperator f, double a, double b) {
        double fa = f.applyAsDouble(a);
        double fb = f.applyAsDouble(b);

        if (fa * fb > 0) {
            throw new IllegalArgumentException("Function must have opposite signs at endpoints");
        }

        for (int i = 0; i < maxIterations; i++) {
            double c = (a + b) / 2;
            double fc = f.applyAsDouble(c);

            if (Math.abs(fc) < tolerance || (b - a) / 2 < tolerance) {
                return c;
            }

            if (fc * fa < 0) {
                b = c;
                fb = fc;
            } else {
                a = c;
                fa = fc;
            }
        }

        return (a + b) / 2;
    }

    /**
     * Newton-Raphson method (fast quadratic convergence near root).
     */
    public double newton(DoubleUnaryOperator f, DoubleUnaryOperator df, double x0) {
        double x = x0;

        for (int i = 0; i < maxIterations; i++) {
            double fx = f.applyAsDouble(x);
            if (Math.abs(fx) < tolerance) {
                return x;
            }

            double dfx = df.applyAsDouble(x);
            if (Math.abs(dfx) < 1e-15) {
                throw new ArithmeticException("Derivative too small");
            }

            double xNew = x - fx / dfx;
            if (Math.abs(xNew - x) < tolerance) {
                return xNew;
            }
            x = xNew;
        }

        return x;
    }

    /**
     * Newton-Raphson with numerical derivative.
     */
    public double newtonNumerical(DoubleUnaryOperator f, double x0) {
        double h = 1e-8;
        DoubleUnaryOperator df = x -> (f.applyAsDouble(x + h) - f.applyAsDouble(x - h)) / (2 * h);
        return newton(f, df, x0);
    }

    /**
     * Secant method (superlinear convergence, no derivative needed).
     */
    public double secant(DoubleUnaryOperator f, double x0, double x1) {
        double xPrev = x0;
        double fPrev = f.applyAsDouble(x0);
        double x = x1;

        for (int i = 0; i < maxIterations; i++) {
            double fx = f.applyAsDouble(x);
            if (Math.abs(fx) < tolerance) {
                return x;
            }

            double df = (fx - fPrev) / (x - xPrev);
            if (Math.abs(df) < 1e-15) {
                throw new ArithmeticException("Division by zero in secant");
            }

            double xNew = x - fx / df;
            xPrev = x;
            fPrev = fx;
            x = xNew;

            if (Math.abs(x - xPrev) < tolerance) {
                return x;
            }
        }

        return x;
    }

    /**
     * Brent's method (combines bisection, secant, inverse quadratic).
     */
    public double brent(DoubleUnaryOperator f, double a, double b) {
        double fa = f.applyAsDouble(a);
        double fb = f.applyAsDouble(b);

        if (fa * fb > 0) {
            throw new IllegalArgumentException("Function must have opposite signs at endpoints");
        }

        if (Math.abs(fa) < Math.abs(fb)) {
            double temp = a;
            a = b;
            b = temp;
            temp = fa;
            fa = fb;
            fb = temp;
        }

        double c = a, fc = fa;
        boolean mflag = true;
        double s = 0, d = 0;

        while (Math.abs(fb) > tolerance && Math.abs(b - a) > tolerance) {
            if (fa != fc && fb != fc) {
                // Inverse quadratic interpolation
                s = a * fb * fc / ((fa - fb) * (fa - fc))
                        + b * fa * fc / ((fb - fa) * (fb - fc))
                        + c * fa * fb / ((fc - fa) * (fc - fb));
            } else {
                // Secant
                s = b - fb * (b - a) / (fb - fa);
            }

            // Conditions for bisection
            boolean cond1 = (s < (3 * a + b) / 4 || s > b);
            boolean cond2 = mflag && Math.abs(s - b) >= Math.abs(b - c) / 2;
            boolean cond3 = !mflag && Math.abs(s - b) >= Math.abs(c - d) / 2;
            boolean cond4 = mflag && Math.abs(b - c) < tolerance;
            boolean cond5 = !mflag && Math.abs(c - d) < tolerance;

            if (cond1 || cond2 || cond3 || cond4 || cond5) {
                s = (a + b) / 2;
                mflag = true;
            } else {
                mflag = false;
            }

            double fs = f.applyAsDouble(s);
            d = c;
            c = b;
            fc = fb;

            if (fa * fs < 0) {
                b = s;
                fb = fs;
            } else {
                a = s;
                fa = fs;
            }

            if (Math.abs(fa) < Math.abs(fb)) {
                double temp = a;
                a = b;
                b = temp;
                temp = fa;
                fa = fb;
                fb = temp;
            }
        }

        return Math.abs(fb) < Math.abs(fa) ? b : a;
    }

    /**
     * Find all roots in an interval using subdivision.
     */
    public double[] findAllRoots(DoubleUnaryOperator f, double a, double b, int subdivisions) {
        java.util.List<Double> roots = new java.util.ArrayList<>();
        double h = (b - a) / subdivisions;

        for (int i = 0; i < subdivisions; i++) {
            double left = a + i * h;
            double right = left + h;
            double fLeft = f.applyAsDouble(left);
            double fRight = f.applyAsDouble(right);

            if (fLeft * fRight < 0) {
                try {
                    double root = brent(f, left, right);
                    // Avoid duplicates
                    boolean isDuplicate = false;
                    for (double r : roots) {
                        if (Math.abs(r - root) < tolerance * 100) {
                            isDuplicate = true;
                            break;
                        }
                    }
                    if (!isDuplicate) {
                        roots.add(root);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        return roots.stream().mapToDouble(Double::doubleValue).toArray();
    }
}
