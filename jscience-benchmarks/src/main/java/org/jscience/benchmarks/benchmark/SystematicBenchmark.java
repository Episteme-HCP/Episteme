/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.benchmarks.benchmark;

import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * Base interface for systematic benchmarks that can be multi-instantiated
 * for different AlgorithmProviders.
 * 
 * @param <P> The type of AlgorithmProvider this benchmark uses.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface SystematicBenchmark<P extends AlgorithmProvider> extends RunnableBenchmark {

    /**
     * @return The class of the provider interface to discover.
     */
    Class<P> getProviderClass();

    /**
     * @return The prefix used for the generated benchmark IDs.
     */
    String getIdPrefix();

    /**
     * @return The base name for the generated benchmark names.
     */
    String getNameBase();

    /**
     * Sets the provider instance for a specific benchmark execution.
     */
    void setProvider(P provider);
}
