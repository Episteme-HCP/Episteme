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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Represents an Ant in Ant Colony Optimization (ACO).
 * Typically used for finding paths in a graph.
 */
public class Ant {
    private final int numNodes;
    private final List<Integer> tour = new ArrayList<>();
    private final Set<Integer> visited = new HashSet<>();
    private double tourLength = 0;
    
    private static final Random random = new Random();

    public Ant(int numNodes) {
        this.numNodes = numNodes;
    }

    public void visitNode(int node, double distance) {
        tour.add(node);
        visited.add(node);
        tourLength += distance;
    }

    public boolean hasVisited(int node) {
        return visited.contains(node);
    }

    public List<Integer> getTour() { return tour; }
    public double getTourLength() { return tourLength; }
    
    public void clear() {
        tour.clear();
        visited.clear();
        tourLength = 0;
    }

    /**
     * Probabilistically select the next node based on pheromones and distance.
     */
    public int selectNextNode(int current, double[][] pheromones, double[][] distances, double alpha, double beta) {
        double[] selectionProbabilities = new double[numNodes];
        double totalSelectionValue = 0;

        for (int i = 0; i < numNodes; i++) {
            if (!visited.contains(i)) {
                double pheromone = Math.pow(pheromones[current][i], alpha);
                double heuristic = Math.pow(1.0 / (distances[current][i] + 1e-10), beta);
                selectionProbabilities[i] = pheromone * heuristic;
                totalSelectionValue += selectionProbabilities[i];
            } else {
                selectionProbabilities[i] = 0;
            }
        }

        if (totalSelectionValue == 0) return -1; // No nodes left to visit

        double threshold = random.nextDouble() * totalSelectionValue;
        double cumulative = 0;
        for (int i = 0; i < numNodes; i++) {
            cumulative += selectionProbabilities[i];
            if (cumulative >= threshold) {
                return i;
            }
        }
        return -1;
    }
}
