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

package org.episteme.social.sociology;

import org.episteme.core.mathematics.discrete.Graph;
import java.util.*;

/**
 * Implementation of Axelrod's Model of Cultural Dissemination.
 * Agents interact based on similarity and adopt traits from each other, 
 * leading to the emergence of cultural regions or total homogenization.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AxelrodModel {

    private final Graph<Person> network;
    private final Map<Person, int[]> culturalVectors = new HashMap<>();
    private final int numFeatures;
    private final int numTraits;
    private final Random random = new Random();

    /**
     * Creates a new Axelrod model simulation.
     * 
     * @param network the social network graph
     * @param numFeatures number of cultural features (dimensions)
     * @param numTraits number of possible traits per feature
     */
    public AxelrodModel(Graph<Person> network, int numFeatures, int numTraits) {
        this.network = Objects.requireNonNull(network);
        this.numFeatures = numFeatures;
        this.numTraits = numTraits;
        initialize();
    }

    private void initialize() {
        for (Person p : network.vertices()) {
            int[] vector = new int[numFeatures];
            for (int i = 0; i < numFeatures; i++) {
                vector[i] = random.nextInt(numTraits);
            }
            culturalVectors.put(p, vector);
        }
    }

    /**
     * Performs one simulation step.
     * A random agent is picked, one of its neighbors is picked, and they interact with probability 
     * equal to their cultural similarity.
     */
    public void step() {
        List<Person> agents = new ArrayList<>(network.vertices());
        if (agents.isEmpty()) return;
        
        Person agentA = agents.get(random.nextInt(agents.size()));
        Set<Person> neighbors = network.neighbors(agentA);
        
        if (neighbors.isEmpty()) return;
        
        List<Person> neighborList = new ArrayList<>(neighbors);
        Person agentB = neighborList.get(random.nextInt(neighborList.size()));
        
        double similarity = calculateSimilarity(agentA, agentB);
        
        if (similarity > 0 && similarity < 1.0) {
            if (random.nextDouble() < similarity) {
                adoptTrait(agentA, agentB);
            }
        }
    }

    private double calculateSimilarity(Person a, Person b) {
        int[] vA = culturalVectors.get(a);
        int[] vB = culturalVectors.get(b);
        int matching = 0;
        for (int i = 0; i < numFeatures; i++) {
            if (vA[i] == vB[i]) {
                matching++;
            }
        }
        return (double) matching / numFeatures;
    }

    private void adoptTrait(Person receiver, Person sender) {
        int[] vR = culturalVectors.get(receiver);
        int[] vS = culturalVectors.get(sender);
        
        List<Integer> differingFeatures = new ArrayList<>();
        for (int i = 0; i < numFeatures; i++) {
            if (vR[i] != vS[i]) {
                differingFeatures.add(i);
            }
        }
        
        if (!differingFeatures.isEmpty()) {
            int featureToAdopt = differingFeatures.get(random.nextInt(differingFeatures.size()));
            vR[featureToAdopt] = vS[featureToAdopt];
        }
    }

    public int[] getCulture(Person p) {
        return culturalVectors.get(p);
    }
}
