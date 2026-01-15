package org.jscience.computing.ai.evolutionary.geneticprogramming;

/**
 * Represents the fitness function used in the genetic program to evaluate
 * individuals
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public interface FitnessFunction {
    /**
     * Computes fitness value of a given individual
     *
     * @param individual individual to compute fitness value
     * @param individualNo index of the individual in the current population
     */
    public void evaluate(Individual individual, int individualNo);
}
