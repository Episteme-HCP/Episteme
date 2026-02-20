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

package org.jscience.core.mathematics.ml;

import org.jscience.core.mathematics.discrete.DirectedWeightedGraph;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.statistics.bayesian.BayesianInferenceProvider;
import org.jscience.core.mathematics.statistics.bayesian.providers.VariableEliminationProvider;
import java.util.*;

/**
 * Represents a Bayesian Belief Network (BBN).
 * A probabilistic graphical model that represents a set of variables and their conditional dependencies via a directed acyclic graph (DAG).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BayesianBeliefNetwork extends DirectedWeightedGraph<String, Real> {

    private final Map<String, BayesianNode> nodes = new LinkedHashMap<>();

    /**
     * Adds a random variable to the network.
     * @param name Name of the variable.
     * @param states Possible states of the variable.
     */
    public void addVariable(String name, String... states) {
        if (nodes.containsKey(name)) {
            throw new IllegalArgumentException("Variable already exists: " + name);
        }
        BayesianNode node = new BayesianNode(name, Arrays.asList(states));
        nodes.put(name, node);
        addVertex(name);
    }

    /**
     * Adds a dependency between two variables.
     * @param parent The parent variable.
     * @param child The child variable.
     */
    public void addDependency(String parent, String child) {
        if (!nodes.containsKey(parent) || !nodes.containsKey(child)) {
            throw new IllegalArgumentException("One or both variables do not exist.");
        }
        addEdge(parent, child, Real.ONE);
    }

    /**
     * Sets the Conditional Probability Table (CPT) for a variable.
     * @param name The variable name.
     * @param probabilities The probabilities in a specific order corresponding to the parent states combinations.
     */
    public void setCPT(String name, double... probabilities) {
        BayesianNode node = nodes.get(name);
        if (node == null) throw new IllegalArgumentException("Variable not found: " + name);
        
        List<String> parents = getParents(name);
        int expectedSize = node.states.size();
        for (String parent : parents) {
            expectedSize *= nodes.get(parent).states.size();
        }
        
        if (probabilities.length != expectedSize) {
            throw new IllegalArgumentException("Invalid CPT size. Expected: " + expectedSize + ", got: " + probabilities.length);
        }
        
        node.setProbabilities(probabilities, parents, nodes);
    }

    private List<String> getParents(String name) {
        List<String> parents = new ArrayList<>();
        for (String v : vertices()) {
            if (neighbors(v).contains(name)) {
                parents.add(v);
            }
        }
        return parents;
    }

    /**
     * Performs exact inference using the Variable Elimination algorithm.
     * @param target The variable to query.
     * @param targetState The state of interest.
     * @param evidence A map of observed variables and their states.
     * @return The probability P(target=targetState | evidence).
     */
    public Real query(String target, String targetState, Map<String, String> evidence) {
        BayesianInferenceProvider provider = findProvider();
        List<BayesianInferenceProvider.BayesNodeData> nodeData = new ArrayList<>(nodes.values());
        return provider.query(target, targetState, evidence, nodeData);
    }

    private BayesianInferenceProvider findProvider() {
        ServiceLoader<BayesianInferenceProvider> loader = ServiceLoader.load(BayesianInferenceProvider.class);
        for (BayesianInferenceProvider p : loader) {
            return p;
        }
        return new VariableEliminationProvider();
    }


    private static class BayesianNode implements BayesianInferenceProvider.BayesNodeData {
        private final List<String> states;
        private final Map<Map<String, String>, Map<String, Double>> cpt = new HashMap<>();
        private final String name;

        @Override
        public String getName() { return name; }

        @Override
        public List<String> getStates() { return states; }

        @Override
        public Map<Map<String, String>, Map<String, Double>> getCPT() { return cpt; }

        BayesianNode(String name, List<String> states) {
            this.name = name;
            this.states = states;
        }

        // Factor logic moved to provider

        void setProbabilities(double[] probs, List<String> parents, Map<String, BayesianNode> allNodes) {
            List<List<String>> parentStateCombos = generateCombos(parents, allNodes);
            int idx = 0;
            for (List<String> combo : parentStateCombos) {
                Map<String, String> parentValues = new HashMap<>();
                for (int i = 0; i < parents.size(); i++) {
                    parentValues.put(parents.get(i), combo.get(i));
                }
                
                Map<String, Double> stateProbs = new HashMap<>();
                for (String state : states) {
                    stateProbs.put(state, probs[idx++]);
                }
                cpt.put(parentValues, stateProbs);
            }
        }

        private List<List<String>> generateCombos(List<String> parents, Map<String, BayesianNode> allNodes) {
            List<List<String>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            for (String p : parents) {
                List<List<String>> next = new ArrayList<>();
                for (String state : allNodes.get(p).states) {
                    for (List<String> combo : result) {
                        List<String> newCombo = new ArrayList<>(combo);
                        newCombo.add(state);
                        next.add(newCombo);
                    }
                }
                result = next;
            }
            return result;
        }
    }
}
