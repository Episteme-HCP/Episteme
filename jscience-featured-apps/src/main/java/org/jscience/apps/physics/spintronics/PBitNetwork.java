/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Probabilistic Boltzmann Machine using coupled p-bits (stochastic MTJs).
 * <p>
 * Implements a network of interacting p-bits for combinatorial optimization,
 * sampling from probability distributions, and neural network inference.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class PBitNetwork {

    private final List<StochasticMTJ> pbits;
    private final double[][] weights; // Symmetric weight matrix J_ij
    private final double[] biases;    // External biases h_i
    private final Real beta;          // Inverse temperature
    private final Random random;

    public PBitNetwork(int size, Real beta) {
        this.pbits = new ArrayList<>();
        this.weights = new double[size][size];
        this.biases = new double[size];
        this.beta = beta;
        this.random = new Random();

        for (int i = 0; i < size; i++) {
            pbits.add(StochasticMTJ.createLowBarrier());
        }
    }

    /**
     * Sets the weight between p-bits i and j (symmetric).
     */
    public void setWeight(int i, int j, double weight) {
        weights[i][j] = weight;
        weights[j][i] = weight;
    }

    /**
     * Sets the external bias for p-bit i.
     */
    public void setBias(int i, double bias) {
        biases[i] = bias;
    }

    /**
     * Performs one asynchronous Gibbs sampling step.
     * Updates a random p-bit based on inputs from all others.
     */
    public void asyncUpdate() {
        int i = random.nextInt(pbits.size());
        updatePbit(i);
    }

    /**
     * Performs one synchronous update of all p-bits.
     */
    public void syncUpdate() {
        int[] newStates = new int[pbits.size()];
        
        for (int i = 0; i < pbits.size(); i++) {
            double input = calculateInput(i);
            double prob = 1.0 / (1.0 + Math.exp(-beta.doubleValue() * input));
            newStates[i] = random.nextDouble() < prob ? 1 : 0;
        }
        
        for (int i = 0; i < pbits.size(); i++) {
            pbits.get(i).setState(newStates[i]);
        }
    }

    private void updatePbit(int i) {
        double input = calculateInput(i);
        pbits.get(i).update(Real.of(input), beta);
    }

    private double calculateInput(int i) {
        // I_i = h_i + Σ_j J_ij * m_j
        double sum = biases[i];
        for (int j = 0; j < pbits.size(); j++) {
            if (i != j) {
                sum += weights[i][j] * pbits.get(j).getIsingState();
            }
        }
        return sum;
    }

    /**
     * Calculates the Ising energy of current configuration.
     * E = -Σ_i h_i * m_i - (1/2) Σ_ij J_ij * m_i * m_j
     */
    public double getEnergy() {
        double energy = 0;
        
        // Bias terms
        for (int i = 0; i < pbits.size(); i++) {
            energy -= biases[i] * pbits.get(i).getIsingState();
        }
        
        // Coupling terms
        for (int i = 0; i < pbits.size(); i++) {
            for (int j = i + 1; j < pbits.size(); j++) {
                energy -= weights[i][j] * pbits.get(i).getIsingState() * pbits.get(j).getIsingState();
            }
        }
        
        return energy;
    }

    /**
     * Gets current state as array of ±1.
     */
    public int[] getState() {
        int[] state = new int[pbits.size()];
        for (int i = 0; i < pbits.size(); i++) {
            state[i] = pbits.get(i).getIsingState();
        }
        return state;
    }

    /**
     * Runs simulated annealing to find ground state.
     */
    public int[] anneal(int steps, double betaStart, double betaEnd) {
        int[] bestState = getState();
        double bestEnergy = getEnergy();

        for (int step = 0; step < steps; step++) {
            // Linear annealing schedule
            double currentBeta = betaStart + (betaEnd - betaStart) * step / steps;
            
            for (int i = 0; i < pbits.size(); i++) {
                double input = calculateInput(i);
                double prob = 1.0 / (1.0 + Math.exp(-currentBeta * input));
                pbits.get(i).setState(random.nextDouble() < prob ? 1 : 0);
            }

            double energy = getEnergy();
            if (energy < bestEnergy) {
                bestEnergy = energy;
                bestState = getState().clone();
            }
        }

        return bestState;
    }

    public int size() { return pbits.size(); }
    public StochasticMTJ getPbit(int i) { return pbits.get(i); }
}
