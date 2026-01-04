/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.client.biology;

import org.jscience.client.algorithms.Algorithm;
import java.util.Random;
import java.util.function.Function;

/**
 * Genetic Algorithm Optimizer.
 * Finds the input string that minimizes a cost function (or maximizes fitness).
 * 
 * @javadoc This algorithm has a complexity of O(g * p * l) where g=generations,
 *          p=population, l=length.
 */
public class GeneticOptimizer implements Algorithm<Function<String, Double>, String> {

    private final int populationSize = 100;
    private final int generations = 50;
    private final int genomeLength;
    private final String alphabet = "ACGT";
    private final Random random = new Random();

    public GeneticOptimizer(int genomeLength) {
        this.genomeLength = genomeLength;
    }

    @Override
    public String execute(Function<String, Double> fitnessFunction) {
        String[] population = new String[populationSize];
        for (int i = 0; i < populationSize; i++) {
            population[i] = randomSequence(genomeLength);
        }

        String best = population[0];
        double bestScore = fitnessFunction.apply(best);

        for (int g = 0; g < generations; g++) {
            // Selection and Crossover (Simplified)
            String[] newPop = new String[populationSize];

            // Elitism
            newPop[0] = best;

            for (int i = 1; i < populationSize; i++) {
                String p1 = population[random.nextInt(populationSize)];
                String p2 = population[random.nextInt(populationSize)];
                newPop[i] = mutate(crossover(p1, p2));
            }
            population = newPop;

            // Evaluation
            for (String ind : population) {
                double score = fitnessFunction.apply(ind);
                if (score > bestScore) {
                    bestScore = score;
                    best = ind;
                }
            }
        }
        return best;
    }

    private String randomSequence(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        return sb.toString();
    }

    private String crossover(String p1, String p2) {
        int split = random.nextInt(genomeLength);
        return p1.substring(0, split) + p2.substring(split);
    }

    private String mutate(String ind) {
        if (random.nextDouble() < 0.1) {
            char[] chars = ind.toCharArray();
            chars[random.nextInt(genomeLength)] = alphabet.charAt(random.nextInt(alphabet.length()));
            return new String(chars);
        }
        return ind;
    }

    @Override
    public String getName() {
        return "Genetic Optimizer (Evolutionary)";
    }

    @Override
    public String getComplexity() {
        return "O(generations * population)";
    }

    @Override
    public double getQualityScore() {
        return 0.95;
    }
}
