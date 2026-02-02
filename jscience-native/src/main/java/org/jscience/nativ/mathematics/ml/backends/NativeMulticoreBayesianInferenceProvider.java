/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.ml.backends;

import org.jscience.core.technical.algorithm.BayesianInferenceProvider;
import org.jscience.core.technical.algorithm.inference.VariableEliminationProvider;
import org.jscience.core.mathematics.numbers.real.Real;
import java.util.Map;
import java.util.List;

/**
 * Native multicore implementation of BayesianInferenceProvider.
 * Currently delegates to Variable Elimination as a placeholder for
 * a future native-optimized inference engine.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class NativeMulticoreBayesianInferenceProvider implements BayesianInferenceProvider {

    private final VariableEliminationProvider fallback = new VariableEliminationProvider();

    @Override
    public String getName() {
        return "Native Multicore Bayesian Inference (C++ Core)";
    }

    @Override
    public Real query(String target, String targetState, Map<String, String> evidence, List<BayesianInferenceProvider.BayesNodeData> nodes) {
        return fallback.query(target, targetState, evidence, nodes);
    }
}
