package org.jscience.history;


/**
 * Historical population dynamics (epidemics, famines, migrations).
 * Extends sociology Demography for historical use cases.
 */
public final class PopulationDynamics {

    private PopulationDynamics() {}

    /**
     * Simulates impact of an epidemic on a population.
     */
    public static double[] simulateEpidemic(double[] population, double mortalityRate) {
        double[] next = new double[population.length];
        for (int i = 0; i < population.length; i++) {
            next[i] = population[i] * (1.0 - mortalityRate);
        }
        return next;
    }
}
