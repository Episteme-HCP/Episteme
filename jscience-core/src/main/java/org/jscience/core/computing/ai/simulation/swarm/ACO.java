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
package org.jscience.core.computing.ai.simulation.swarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Ant Colony Optimization (ACO) implementation for TSP-like problems (Graph traversal).
 * <p>
 * Uses Pheromone trails to find shortest paths.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class ACO {
    private int numAnts;
    private int numNodes;
    private double[][] distanceMatrix;
    private double[][] pheromoneMatrix;
    
    private double alpha = 1.0; // Pheromone importance
    private double beta = 5.0;  // Distance importance
    private double evaporationRate = 0.5;
    private double Q = 100.0;   // Pheromone constant
    
    private int[] bestTour;
    private double bestTourLength = Double.MAX_VALUE;
    
    private Random random = new Random();
    
    public ACO(int numAnts, double[][] distanceMatrix) {
        this.numAnts = numAnts;
        this.distanceMatrix = distanceMatrix;
        this.numNodes = distanceMatrix.length;
        this.pheromoneMatrix = new double[numNodes][numNodes];
        
        // Init pheromones
        for (double[] row : pheromoneMatrix) {
            Arrays.fill(row, 1.0);
        }
    }
    
    public void solve(int maxIterations) {
        for (int i = 0; i < maxIterations; i++) {
             List<int[]> tours = new ArrayList<>();
             List<Double> lengths = new ArrayList<>();
             
             // Construct solutions
             for (int ant = 0; ant < numAnts; ant++) {
                 int[] tour = buildTour();
                 double len = calculateTourLength(tour);
                 tours.add(tour);
                 lengths.add(len);
                 
                 if (len < bestTourLength) {
                     bestTourLength = len;
                     bestTour = tour.clone();
                 }
             }
             
             // Update pheromones
             evaporate();
             deposit(tours, lengths);
        }
    }
    
    private int[] buildTour() {
        int[] tour = new int[numNodes];
        boolean[] visited = new boolean[numNodes];
        
        int start = random.nextInt(numNodes);
        tour[0] = start;
        visited[start] = true;
        
        for (int i = 1; i < numNodes; i++) {
            int current = tour[i-1];
            int next = selectNextNode(current, visited);
            tour[i] = next;
            visited[next] = true;
        }
        return tour;
    }
    
    private int selectNextNode(int current, boolean[] visited) {
        double[] probabilities = new double[numNodes];
        double sum = 0.0;
        
        for (int next = 0; next < numNodes; next++) {
            if (!visited[next]) {
                double pheromone = Math.pow(pheromoneMatrix[current][next], alpha);
                double heuristic = Math.pow(1.0 / distanceMatrix[current][next], beta); // 1/distance
                probabilities[next] = pheromone * heuristic;
                sum += probabilities[next];
            }
        }
        
        // Roulette wheel selection
        double r = random.nextDouble() * sum;
        double partial = 0.0;
        for (int next = 0; next < numNodes; next++) {
            if (!visited[next]) {
                partial += probabilities[next];
                if (partial >= r) {
                    return next;
                }
            }
        }
        
        // Fallback (should not happen usually)
        for (int i = 0; i < numNodes; i++) if (!visited[i]) return i;
        return -1;
    }

    private double calculateTourLength(int[] tour) {
        double len = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            len += distanceMatrix[tour[i]][tour[i+1]];
        }
        len += distanceMatrix[tour[tour.length-1]][tour[0]]; // Return to start
        return len;
    }
    
    private void evaporate() {
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                pheromoneMatrix[i][j] *= (1.0 - evaporationRate);
            }
        }
    }
    
    private void deposit(List<int[]> tours, List<Double> lengths) {
        for (int i = 0; i < tours.size(); i++) {
            int[] tour = tours.get(i);
            double len = lengths.get(i);
            double pheromoneToAdd = Q / len;
            
            for (int j = 0; j < tour.length - 1; j++) {
                int from = tour[j];
                int to = tour[j+1];
                pheromoneMatrix[from][to] += pheromoneToAdd;
                pheromoneMatrix[to][from] += pheromoneToAdd;
            }
            // Return edge
            int from = tour[tour.length-1];
            int to = tour[0];
            pheromoneMatrix[from][to] += pheromoneToAdd;
            pheromoneMatrix[to][from] += pheromoneToAdd;
        }
    }
    
    public int[] getBestTour() {
        return bestTour;
    }
    
    public double getBestTourLength() {
        return bestTourLength;
    }
}
