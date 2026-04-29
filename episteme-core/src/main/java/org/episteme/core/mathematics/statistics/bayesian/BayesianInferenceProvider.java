/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.statistics.bayesian;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import java.util.Map;
import java.util.List;

/**
 * Service provider interface for Bayesian Inference.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface BayesianInferenceProvider extends AlgorithmProvider {
    
    interface BayesNodeData {
        String getName();
        List<String> getStates();
        @Deprecated
        Map<Map<String, String>, Map<String, Double>> getCPT();
        
        default Map<Map<String, String>, Map<String, Float>> getCPTFloat() {
             Map<Map<String, String>, Map<String, Double>> cpt = getCPT();
             Map<Map<String, String>, Map<String, Float>> res = new java.util.HashMap<>();
             for (var entry : cpt.entrySet()) {
                 Map<String, Float> inner = new java.util.HashMap<>();
                 for (var state : entry.getValue().entrySet()) {
                     inner.put(state.getKey(), state.getValue().floatValue());
                 }
                 res.put(entry.getKey(), inner);
             }
             return res;
        }
        
        default Map<Map<String, String>, Map<String, Real>> getCPTReal() {
             Map<Map<String, String>, Map<String, Double>> cpt = getCPT();
             Map<Map<String, String>, Map<String, Real>> res = new java.util.HashMap<>();
             for (var entry : cpt.entrySet()) {
                 Map<String, Real> inner = new java.util.HashMap<>();
                 for (var state : entry.getValue().entrySet()) {
                     inner.put(state.getKey(), Real.of(state.getValue()));
                 }
                 res.put(entry.getKey(), inner);
             }
             return res;
        }
    }

    /**
     * Performs inference on a Bayesian network using Real precision.
     */
    Real query(String target, String targetState, Map<String, String> evidence, List<BayesNodeData> nodes);

    /**
     * Performs inference on a Bayesian network using float precision.
     */
    default float queryFloat(String target, String targetState, Map<String, String> evidence, List<BayesNodeData> nodes) {
        return query(target, targetState, evidence, nodes).floatValue();
    }

    /**
     * Performs inference on a Bayesian network using double precision.
     */
    default double queryDouble(String target, String targetState, Map<String, String> evidence, List<BayesNodeData> nodes) {
        return query(target, targetState, evidence, nodes).doubleValue();
    }

    @Override
    default String getName() {
        return "Bayesian Inference Provider";
    }

    @Override
    default String getAlgorithmType() {
        return "Bayesian Inference";
    }
}
