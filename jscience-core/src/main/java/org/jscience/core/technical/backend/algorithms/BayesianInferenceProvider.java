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

package org.jscience.core.technical.backend.algorithms;

import org.jscience.core.technical.backend.AlgorithmProvider;
import org.jscience.core.mathematics.numbers.real.Real;
import java.util.Map;
import java.util.List;

/**
 * Service provider interface for Bayesian Inference.
 * Enables backend-agnostic exact and approximate inference (e.g., Variable Elimination, MCMC).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface BayesianInferenceProvider extends AlgorithmProvider {
    
    /**
     * Map representing a Conditional Probability Table (CPT) for a node.
     * Key is a map of parent variable names and their states.
     * Value is a map of the node's variable name and its state, pointing to its probability.
     */
    interface BayesNodeData {
        String getName();
        List<String> getStates();
        Map<Map<String, String>, Map<String, Double>> getCPT();
    }

    /**
     * Performs inference on a Bayesian network.
     * 
     * @param target The variable to query.
     * @param targetState The state of interest.
     * @param evidence A map of observed variables and their states.
     * @param nodes Information about all nodes in the network.
     * @return The probability P(target=targetState | evidence).
     */
    Real query(String target, String targetState, Map<String, String> evidence, List<BayesNodeData> nodes);
}
