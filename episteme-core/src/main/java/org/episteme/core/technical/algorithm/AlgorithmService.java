/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.algorithm;

import java.util.List;
import java.util.function.Function;

/**
 * Service interface for algorithm provider discovery and execution.
 * <p>
 * This interface abstracts the logic previously held in {@link AlgorithmManager}
 * to allow for better test isolation and dynamic backend steering.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface AlgorithmService {

    /**
     * Gets the best available provider for the given class.
     */
    <P extends AlgorithmProvider> P getProvider(Class<P> providerClass);

    /**
     * Gets all available providers for the given class, sorted by priority.
     */
    <P extends AlgorithmProvider> List<P> getProviders(Class<P> providerClass);

    /**
     * Executes an operation with automatic fallback through the provider chain.
     */
    <P extends AlgorithmProvider, R> R executeWithFallback(Class<P> providerClass, Function<P, R> operation);

    /**
     * Checks if a provider with the given name is filtered out by global configuration.
     *
     * @param name the provider name
     * @return true if filtered out
     */
    boolean isFiltered(String name);

    /**
     * Refreshes the provider cache.
     */
    void refresh();

    /**
     * Shuts down the service and all managed providers.
     */
    void shutdown();
}
