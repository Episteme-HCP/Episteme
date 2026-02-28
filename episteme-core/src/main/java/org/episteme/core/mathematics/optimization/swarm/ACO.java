/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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
package org.episteme.core.mathematics.optimization.swarm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Ant Colony Optimization (ACO) Engine.
 * Implements simplified MAX-MIN Ant System for TSP.
 */
public class ACO {
    private final int numNodes;
    private final int numAnts;
    private final double[][] distances;
    private final double[][] pheromones;
    
    private final List<Ant> ants;
    private List<Integer> bestTour;
    private double bestTourLength = Double.POSITIVE_INFINITY;
    
    private final double alpha = 1.0;
    private final double beta = 2.0;
    private final double rho = 0.5;   // Evaporation rate
    private final double Q = 100.0;     // Pheromone constant

    public ACO(int numNodes, double[][] distances, int numAnts) {
        this.numNodes = numNodes;
        this.distances = distances;
        this.numAnts = numAnts;
        this.pheromones = new double[numNodes][numNodes];
        this.ants = new ArrayList<>(numAnts);
        
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                pheromones[i][j] = 1.0; // Initial pheromone
            }
        }
        
        for (int i = 0; i < numAnts; i++) {
            ants.add(new Ant(numNodes));
        }
    }

    public void step() {
        for (Ant ant : ants) {
            ant.clear();
            int current = new Random().nextInt(numNodes);
            ant.visitNode(current, 0);
            
            for (int i = 0; i < numNodes - 1; i++) {
                int next = ant.selectNextNode(current, pheromones, distances, alpha, beta);
                if (next != -1) {
                    ant.visitNode(next, distances[current][next]);
                    current = next;
                }
            }
            // Return to start
            ant.visitNode(ant.getTour().get(0), distances[current][ant.getTour().get(0)]);
            
            if (ant.getTourLength() < bestTourLength) {
                bestTourLength = ant.getTourLength();
                bestTour = new ArrayList<>(ant.getTour());
            }
        }
        
        updatePheromones();
    }

    private void updatePheromones() {
        // Evaporation
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                pheromones[i][j] *= (1.0 - rho);
            }
        }
        
        // Deposit
        for (Ant ant : ants) {
            double deposit = Q / ant.getTourLength();
            List<Integer> tour = ant.getTour();
            for (int i = 0; i < tour.size() - 1; i++) {
                int from = tour.get(i);
                int to = tour.get(i + 1);
                pheromones[from][to] += deposit;
                pheromones[to][from] += deposit;
            }
        }
    }

    public List<Integer> getBestTour() { return bestTour; }
    public int getNumAnts() { return numAnts; }
    public double getBestTourLength() { return bestTourLength; }
}
