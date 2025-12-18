package org.jscience.biology.ecology;

/**
 * Population dynamics models for ecology.
 * <p>
 * Provides:
 * <ul>
 * <li>Exponential growth</li>
 * <li>Logistic growth (Verhulst equation)</li>
 * <li>Lotka-Volterra predator-prey model</li>
 * <li>Competition models</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 2.0
 */
public class PopulationDynamics {

    private PopulationDynamics() {
    }

    // === Exponential Growth ===

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
     * Exponential growth rate calculation.
     * r = ln(Nt/N0) / t
     */
    public static double calculateGrowthRate(double n0, double nt, double time) {
        return Math.log(nt / n0) / time;
    }

    /**
     * Doubling time for exponential growth.
     * td = ln(2) / r
     */
    public static double doublingTime(double growthRate) {
        return Math.log(2) / growthRate;
    }

    // === Logistic Growth ===

    /**
     * Logistic growth model.
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
     * Logistic growth rate at population N.
     * dN/dt = rN(1 - N/K)
     */
    public static double logisticGrowthRate(double n, double k, double r) {
        return r * n * (1 - n / k);
    }

    /**
     * Inflection point of logistic curve (maximum growth rate).
     * Occurs at N = K/2
     */
    public static double inflectionPoint(double k) {
        return k / 2;
    }

    // === Lotka-Volterra Predator-Prey ===

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

    /**
     * Lotka-Volterra equilibrium - prey population.
     * N* = gamma/delta
     */
    public static double preyEquilibrium(double gamma, double delta) {
        return gamma / delta;
    }

    /**
     * Lotka-Volterra equilibrium - predator population.
     * P* = alpha/beta
     */
    public static double predatorEquilibrium(double alpha, double beta) {
        return alpha / beta;
    }

    // === Competition ===

    /**
     * Competitive exclusion - Lotka-Volterra competition.
     * dN1/dt = r1*N1 * (K1 - N1 - alpha12*N2) / K1
     * 
     * @param n1      species 1 population
     * @param n2      species 2 population
     * @param r1      growth rate of species 1
     * @param k1      carrying capacity of species 1
     * @param alpha12 competition coefficient (effect of N2 on N1)
     * @return growth rate of species 1
     */
    public static double competitionGrowthRate(double n1, double n2, double r1, double k1, double alpha12) {
        return r1 * n1 * (k1 - n1 - alpha12 * n2) / k1;
    }

    /**
     * Allee effect - reduced growth at low density.
     * dN/dt = rN(N/A - 1)(1 - N/K)
     * 
     * @param n population
     * @param r growth rate
     * @param a Allee threshold
     * @param k carrying capacity
     */
    public static double alleeEffectGrowthRate(double n, double r, double a, double k) {
        return r * n * (n / a - 1) * (1 - n / k);
    }
}
