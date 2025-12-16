package org.jscience.biology.ecology;

/**
 * Population dynamics models.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class PopulationDynamics {

    /**
     * Exponential growth model (Malthusian).
     * P(t) = P0 * e^(rt)
     * 
     * @param p0 Initial population
     * @param r  Growth rate
     * @param t  Time
     * @return Population at time t
     */
    public static double exponentialGrowth(double p0, double r, double t) {
        return p0 * Math.exp(r * t);
    }

    /**
     * Logistic growth model.
     * dP/dt = rP(1 - P/K)
     * P(t) = K / (1 + ((K - P0)/P0) * e^(-rt))
     * 
     * @param p0 Initial population
     * @param r  Growth rate
     * @param K  Carrying capacity
     * @param t  Time
     * @return Population at time t
     */
    public static double logisticGrowth(double p0, double r, double K, double t) {
        return K / (1 + ((K - p0) / p0) * Math.exp(-r * t));
    }

    /**
     * Solving Lotka-Volterra predator-prey equations using Euler method.
     * dx/dt = alpha*x - beta*x*y (Prey)
     * dy/dt = delta*x*y - gamma*y (Predator)
     * 
     * @param prey     Initial prey population
     * @param predator Initial predator population
     * @param alpha    Prey growth rate
     * @param beta     Predation rate
     * @param delta    Predator reproduction rate
     * @param gamma    Predator death rate
     * @param timeStep dt
     * @param steps    Number of steps to simulate
     * @return [steps][2] array where [i][0] is prey, [i][1] is predator
     */
    public static double[][] lotkaVolterra(double prey, double predator,
            double alpha, double beta,
            double delta, double gamma,
            double timeStep, int steps) {
        double[][] result = new double[steps][2];
        double x = prey;
        double y = predator;

        for (int i = 0; i < steps; i++) {
            result[i][0] = x;
            result[i][1] = y;

            double dx = (alpha * x - beta * x * y) * timeStep;
            double dy = (delta * x * y - gamma * y) * timeStep;

            x += dx;
            y += dy;

            // Prevent negative populations
            if (x < 0)
                x = 0;
            if (y < 0)
                y = 0;
        }

        return result;
    }
}
