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

import java.util.*;
import java.util.function.BiConsumer;

/**
 * NSGA-II (Non-dominated Sorting Genetic Algorithm II) Implementation.
 */
public class NSGA2 {
    private final int populationSize;
    private final int numObjectives;
    private final int numVariables;
    private final BiConsumer<double[], double[]> evaluate;

    private List<MultiobjectiveSolution> population;
    private final Random random = new Random();

    public NSGA2(int populationSize, int numVariables, int numObjectives, BiConsumer<double[], double[]> evaluate) {
        this.populationSize = populationSize;
        this.numVariables = numVariables;
        this.numObjectives = numObjectives;
        this.evaluate = evaluate;
        this.population = new ArrayList<>(populationSize);
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < populationSize; i++) {
            double[] vars = new double[numVariables];
            for (int j = 0; j < numVariables; j++) vars[j] = random.nextDouble();
            MultiobjectiveSolution sol = new MultiobjectiveSolution(vars, numObjectives);
            evaluate.accept(sol.getVariables(), sol.getObjectives());
            population.add(sol);
        }
    }

    public void evolve() {
        List<MultiobjectiveSolution> children = generateChildren();
        List<MultiobjectiveSolution> combined = new ArrayList<>(population);
        combined.addAll(children);

        List<List<MultiobjectiveSolution>> fronts = fastNonDominatedSort(combined);
        List<MultiobjectiveSolution> nextGen = new ArrayList<>();

        for (List<MultiobjectiveSolution> front : fronts) {
            calculateCrowdingDistance(front);
            if (nextGen.size() + front.size() <= populationSize) {
                nextGen.addAll(front);
            } else {
                front.sort((s1, s2) -> Double.compare(s2.getCrowdingDistance(), s1.getCrowdingDistance()));
                nextGen.addAll(front.subList(0, populationSize - nextGen.size()));
                break;
            }
        }
        population = nextGen;
    }

    private List<MultiobjectiveSolution> generateChildren() {
        List<MultiobjectiveSolution> children = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            MultiobjectiveSolution p1 = binaryTournament();
            MultiobjectiveSolution p2 = binaryTournament();
            double[] childVars = crossover(p1.getVariables(), p2.getVariables());
            mutate(childVars);
            MultiobjectiveSolution child = new MultiobjectiveSolution(childVars, numObjectives);
            evaluate.accept(child.getVariables(), child.getObjectives());
            children.add(child);
        }
        return children;
    }

    private MultiobjectiveSolution binaryTournament() {
        MultiobjectiveSolution s1 = population.get(random.nextInt(populationSize));
        MultiobjectiveSolution s2 = population.get(random.nextInt(populationSize));
        if (s1.getRank() < s2.getRank()) return s1;
        if (s2.getRank() < s1.getRank()) return s2;
        return (s1.getCrowdingDistance() > s2.getCrowdingDistance()) ? s1 : s2;
    }

    private double[] crossover(double[] v1, double[] v2) {
        double[] child = new double[numVariables];
        int cp = random.nextInt(numVariables);
        for (int i = 0; i < numVariables; i++) child[i] = (i < cp) ? v1[i] : v2[i];
        return child;
    }

    private void mutate(double[] v) {
        if (random.nextDouble() < 0.1) {
            v[random.nextInt(numVariables)] = random.nextDouble();
        }
    }

    private List<List<MultiobjectiveSolution>> fastNonDominatedSort(List<MultiobjectiveSolution> solutions) {
        List<List<MultiobjectiveSolution>> fronts = new ArrayList<>();
        Map<MultiobjectiveSolution, List<MultiobjectiveSolution>> dominatedBy = new HashMap<>();
        Map<MultiobjectiveSolution, Integer> dominantCount = new HashMap<>();
        List<MultiobjectiveSolution> currentFront = new ArrayList<>();

        for (MultiobjectiveSolution p : solutions) {
            dominatedBy.put(p, new ArrayList<>());
            dominantCount.put(p, 0);
            for (MultiobjectiveSolution q : solutions) {
                if (p.dominates(q)) {
                    dominatedBy.get(p).add(q);
                } else if (q.dominates(p)) {
                    dominantCount.put(p, dominantCount.get(p) + 1);
                }
            }
            if (dominantCount.get(p) == 0) {
                p.setRank(1);
                currentFront.add(p);
            }
        }

        while (!currentFront.isEmpty()) {
            fronts.add(currentFront);
            List<MultiobjectiveSolution> nextFront = new ArrayList<>();
            for (MultiobjectiveSolution p : currentFront) {
                for (MultiobjectiveSolution q : dominatedBy.get(p)) {
                    dominantCount.put(q, dominantCount.get(q) - 1);
                    if (dominantCount.get(q) == 0) {
                        q.setRank(fronts.size() + 1);
                        nextFront.add(q);
                    }
                }
            }
            currentFront = nextFront;
        }
        return fronts;
    }

    private void calculateCrowdingDistance(List<MultiobjectiveSolution> front) {
        int n = front.size();
        if (n == 0) return;
        if (n <= 2) {
            for (MultiobjectiveSolution s : front) s.setCrowdingDistance(Double.POSITIVE_INFINITY);
            return;
        }

        for (MultiobjectiveSolution s : front) s.setCrowdingDistance(0);

        for (int m = 0; m < numObjectives; m++) {
            final int objIdx = m;
            front.sort(Comparator.comparingDouble(s -> s.getObjectives()[objIdx]));
            front.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
            front.get(n - 1).setCrowdingDistance(Double.POSITIVE_INFINITY);

            double min = front.get(0).getObjectives()[m];
            double max = front.get(n - 1).getObjectives()[m];
            double range = max - min;
            if (range == 0) continue;

            for (int i = 1; i < n - 1; i++) {
                double distance = front.get(i).getCrowdingDistance();
                distance += (front.get(i + 1).getObjectives()[m] - front.get(i - 1).getObjectives()[m]) / range;
                front.get(i).setCrowdingDistance(distance);
            }
        }
    }

    public List<MultiobjectiveSolution> getPopulation() { return population; }
}
