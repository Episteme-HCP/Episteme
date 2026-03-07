/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.algorithm;

/**
 * Base interface for high-level scientific algorithm providers.
 * Discovery is based on ServiceLoader and priority.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface AlgorithmProvider {

    /**
     * Returns the human-readable name of the provider.
     */
    String getName();

    /**
     * Returns the execution priority (higher is better).
     */
    default int getPriority() {
        return 0;
    }

    /**
     * Checks if the provider is available in the current environment.
     */
    default boolean isAvailable() {
        return true;
    }

    /**
     * Returns the unique category of the algorithm.
     */
    default String getAlgorithmType() {
        return "generic";
    }

    /**
     * Scores this provider for a specific operation context.
     * <p>
     * Higher scores indicate better suitability. Used by
     * {@link ProviderSelector} for context-aware selection.
     * </p>
     * <p>
     * Default implementation returns {@link #getPriority()}, so existing
     * providers behave identically without changes.
     * </p>
     *
     * @param context the operation context (data size, hints, etc.)
     * @return suitability score (higher = better)
     */
    default double score(OperationContext context) {
        return getPriority();
    }

    /**
     * Called when the provider is no longer needed (e.g., application shutdown).
     * Use this to release local resources or close native segments.
     */
    default void shutdown() {
        // Default implementation does nothing
    }

    /**
     * Returns metadata about the provider's performance and capabilities.
     * This can be used for advanced matching in the ProviderSelector.
     * 
     * @return a map of metadata keys and values
     */
    default java.util.Map<String, String> getMetadata() {
        return java.util.Collections.emptyMap();
    }
}
