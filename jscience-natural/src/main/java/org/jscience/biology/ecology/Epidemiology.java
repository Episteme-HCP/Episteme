package org.jscience.biology.ecology;

/**
 * Epidemiology models.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Epidemiology {

    /**
     * SIR (Susceptible-Infectious-Recovered) model simulation.
     * dS/dt = -beta * S * I / N
     * dI/dt = beta * S * I / N - gamma * I
     * dR/dt = gamma * I
     * 
     * @param S0    Initial susceptible
     * @param I0    Initial infectious
     * @param R0    Initial recovered
     * @param beta  Transmission rate
     * @param gamma Recovery rate
     * @param dt    Time step
     * @param steps Number of steps
     * @return [steps][3] array
     */
    public static double[][] sirModel(double S0, double I0, double R0,
            double beta, double gamma,
            double dt, int steps) {
        double[][] result = new double[steps][3];
        double S = S0;
        double I = I0;
        double R = R0;
        double N = S0 + I0 + R0;

        for (int i = 0; i < steps; i++) {
            result[i][0] = S;
            result[i][1] = I;
            result[i][2] = R;

            double dS = (-beta * S * I / N) * dt;
            double dI = (beta * S * I / N - gamma * I) * dt;
            double dR = (gamma * I) * dt;

            S += dS;
            I += dI;
            R += dR;
        }

        return result;
    }

    /**
     * Basic reproduction number (R0).
     * R0 = beta / gamma
     */
    public static double basicReproductionNumber(double beta, double gamma) {
        return beta / gamma;
    }

    /**
     * Herd immunity threshold.
     * HIT = 1 - 1/R0
     */
    public static double herdImmunityThreshold(double r0) {
        if (r0 <= 1)
            return 0;
        return 1.0 - 1.0 / r0;
    }
}
