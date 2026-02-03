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
package org.jscience.core.mathematics.optimization.evolutionary;

/**
 * Represents a solution in a multi-objective optimization problem.
 */
public class MultiobjectiveSolution {
    private final double[] variables;
    private final double[] objectives;
    private int rank;
    private double crowdingDistance;

    public MultiobjectiveSolution(double[] variables, int numObjectives) {
        this.variables = variables;
        this.objectives = new double[numObjectives];
    }

    public double[] getVariables() { return variables; }
    public double[] getObjectives() { return objectives; }
    
    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
    
    public double getCrowdingDistance() { return crowdingDistance; }
    public void setCrowdingDistance(double crowdingDistance) { this.crowdingDistance = crowdingDistance; }

    /**
     * Pareto dominance check: true if this dominates other.
     */
    public boolean dominates(MultiobjectiveSolution other) {
        boolean betterInAtLeastOne = false;
        for (int i = 0; i < objectives.length; i++) {
            if (this.objectives[i] > other.objectives[i]) return false; // Assuming minimization
            if (this.objectives[i] < other.objectives[i]) betterInAtLeastOne = true;
        }
        return betterInAtLeastOne;
    }
}
