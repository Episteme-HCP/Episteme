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
        Map<Map<String, String>, Map<String, Double>> getCPT();
    }

    /**
     * Performs inference on a Bayesian network using Real precision.
     */
    Real query(String target, String targetState, Map<String, String> evidence, List<BayesNodeData> nodes);

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
