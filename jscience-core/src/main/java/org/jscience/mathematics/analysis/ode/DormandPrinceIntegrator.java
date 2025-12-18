package org.jscience.mathematics.analysis.ode;

/**
 * Dormand-Prince 5(4) adaptive step-size ODE integrator.
 * <p>
 * Embedded Runge-Kutta method with error estimation for automatic step size
 * control.
 * Industry standard for general-purpose ODE solving.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class DormandPrinceIntegrator {

    // Dormand-Prince 5(4) coefficients
    private static final double[] C = { 0, 1.0 / 5, 3.0 / 10, 4.0 / 5, 8.0 / 9, 1, 1 };

    private static final double[][] A = {
            {},
            { 1.0 / 5 },
            { 3.0 / 40, 9.0 / 40 },
            { 44.0 / 45, -56.0 / 15, 32.0 / 9 },
            { 19372.0 / 6561, -25360.0 / 2187, 64448.0 / 6561, -212.0 / 729 },
            { 9017.0 / 3168, -355.0 / 33, 46732.0 / 5247, 49.0 / 176, -5103.0 / 18656 },
            { 35.0 / 384, 0, 500.0 / 1113, 125.0 / 192, -2187.0 / 6784, 11.0 / 84 }
    };

    // 5th order coefficients
    private static final double[] B5 = { 35.0 / 384, 0, 500.0 / 1113, 125.0 / 192, -2187.0 / 6784, 11.0 / 84, 0 };

    // 4th order coefficients (for error estimation)
    private static final double[] B4 = { 5179.0 / 57600, 0, 7571.0 / 16695, 393.0 / 640, -92097.0 / 339200,
            187.0 / 2100, 1.0 / 40 };

    private final double relTol;
    private final double absTol;
    private final double minStep;
    private final double maxStep;

    public DormandPrinceIntegrator(double relTol, double absTol, double minStep, double maxStep) {
        this.relTol = relTol;
        this.absTol = absTol;
        this.minStep = minStep;
        this.maxStep = maxStep;
    }

    public DormandPrinceIntegrator() {
        this(1e-6, 1e-9, 1e-10, 1.0);
    }

    /**
     * Integrates a system of ODEs: dy/dt = f(t, y).
     * 
     * @param f    Derivative function (t, y[]) -> dy/dt[]
     * @param t0   Initial time
     * @param y0   Initial state
     * @param tEnd Final time
     * @return Final state y(tEnd)
     */
    public double[] integrate(
            java.util.function.BiFunction<Double, double[], double[]> f,
            double t0, double[] y0, double tEnd) {

        int n = y0.length;
        double t = t0;
        double[] y = y0.clone();
        double h = (tEnd - t0) / 100.0; // Initial step guess
        h = Math.min(Math.max(h, minStep), maxStep);

        double[][] k = new double[7][n];

        while (t < tEnd) {
            // Adjust last step
            if (t + h > tEnd) {
                h = tEnd - t;
            }

            // Compute k values
            k[0] = f.apply(t, y);

            for (int stage = 1; stage < 7; stage++) {
                double[] yTemp = new double[n];
                for (int i = 0; i < n; i++) {
                    yTemp[i] = y[i];
                    for (int j = 0; j < stage; j++) {
                        yTemp[i] += h * A[stage][j] * k[j][i];
                    }
                }
                k[stage] = f.apply(t + C[stage] * h, yTemp);
            }

            // Compute 5th and 4th order solutions
            double[] y5 = new double[n];
            double[] y4 = new double[n];
            for (int i = 0; i < n; i++) {
                y5[i] = y[i];
                y4[i] = y[i];
                for (int j = 0; j < 7; j++) {
                    y5[i] += h * B5[j] * k[j][i];
                    y4[i] += h * B4[j] * k[j][i];
                }
            }

            // Error estimation
            double error = 0;
            for (int i = 0; i < n; i++) {
                double sci = absTol + relTol * Math.max(Math.abs(y[i]), Math.abs(y5[i]));
                double err = Math.abs(y5[i] - y4[i]) / sci;
                error = Math.max(error, err);
            }

            // Step size control
            if (error <= 1.0) {
                // Accept step
                t += h;
                y = y5;
            }

            // Compute new step size
            double factor = 0.9 * Math.pow(1.0 / Math.max(error, 1e-10), 0.2);
            factor = Math.max(0.1, Math.min(5.0, factor));
            h = Math.min(Math.max(h * factor, minStep), maxStep);
        }

        return y;
    }

    /**
     * Integrate with trajectory recording.
     */
    public java.util.List<double[]> integrateWithHistory(
            java.util.function.BiFunction<Double, double[], double[]> f,
            double t0, double[] y0, double tEnd) {

        java.util.List<double[]> history = new java.util.ArrayList<>();

        int n = y0.length;
        double t = t0;
        double[] y = y0.clone();
        double h = (tEnd - t0) / 100.0;
        h = Math.min(Math.max(h, minStep), maxStep);

        // Record initial state
        double[] record = new double[n + 1];
        record[0] = t;
        System.arraycopy(y, 0, record, 1, n);
        history.add(record);

        double[][] k = new double[7][n];

        while (t < tEnd) {
            if (t + h > tEnd) {
                h = tEnd - t;
            }

            k[0] = f.apply(t, y);

            for (int stage = 1; stage < 7; stage++) {
                double[] yTemp = new double[n];
                for (int i = 0; i < n; i++) {
                    yTemp[i] = y[i];
                    for (int j = 0; j < stage; j++) {
                        yTemp[i] += h * A[stage][j] * k[j][i];
                    }
                }
                k[stage] = f.apply(t + C[stage] * h, yTemp);
            }

            double[] y5 = new double[n];
            double[] y4 = new double[n];
            for (int i = 0; i < n; i++) {
                y5[i] = y[i];
                y4[i] = y[i];
                for (int j = 0; j < 7; j++) {
                    y5[i] += h * B5[j] * k[j][i];
                    y4[i] += h * B4[j] * k[j][i];
                }
            }

            double error = 0;
            for (int i = 0; i < n; i++) {
                double sci = absTol + relTol * Math.max(Math.abs(y[i]), Math.abs(y5[i]));
                double err = Math.abs(y5[i] - y4[i]) / sci;
                error = Math.max(error, err);
            }

            if (error <= 1.0) {
                t += h;
                y = y5;

                record = new double[n + 1];
                record[0] = t;
                System.arraycopy(y, 0, record, 1, n);
                history.add(record);
            }

            double factor = 0.9 * Math.pow(1.0 / Math.max(error, 1e-10), 0.2);
            factor = Math.max(0.1, Math.min(5.0, factor));
            h = Math.min(Math.max(h * factor, minStep), maxStep);
        }

        return history;
    }
}
