/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

/**
 * Context-aware provider selector.
 * <p>
 * Unlike {@link AlgorithmManager#getProvider(Class)} which uses static priority,
 * {@code ProviderSelector} uses {@link AlgorithmProvider#score(OperationContext)}
 * to pick the best provider for a specific operation.
 * </p>
 *
 * <pre>
 * OperationContext ctx = new OperationContext.Builder()
 *     .dataSize(1_000_000)
 *     .addHint(OperationContext.Hint.SPARSE)
 *     .build();
 *
 * LinearAlgebraProvider best = ProviderSelector.select(
 *     LinearAlgebraProvider.class, ctx);
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public final class ProviderSelector {

    private static final Logger LOGGER = Logger.getLogger(ProviderSelector.class.getName());

    private ProviderSelector() {}

    /**
     * Selects the best provider for the given operation context.
     * <p>
     * Scores all available providers via {@link AlgorithmProvider#score(OperationContext)}
     * and returns the one with the highest score.
     * </p>
     *
     * @param <P> the provider type
     * @param providerClass the interface class of the provider
     * @param context the operation context describing data characteristics
     * @return the best-scoring available provider
     * @throws NoSuchElementException if no provider is available
     */
    public static <P extends AlgorithmProvider> P select(Class<P> providerClass, OperationContext context) {
        List<P> providers = AlgorithmManager.getProviders(providerClass);

        if (providers.isEmpty()) {
            throw new NoSuchElementException("No available provider for: " + providerClass.getSimpleName());
        }

        P best = providers.stream()
                .max(Comparator.comparingDouble(p -> ((AlgorithmProvider) p).score(context)))
                .orElseThrow();

        LOGGER.fine("Selected " + best.getName() + " (score=" + best.score(context) + ") for " + providerClass.getSimpleName());
        return best;
    }

    /**
     * Selects using static priority (delegates to {@link AlgorithmManager}).
     * <p>
     * Convenience method for when no operation context is available.
     * </p>
     */
    public static <P extends AlgorithmProvider> P select(Class<P> providerClass) {
        return AlgorithmManager.getProvider(providerClass);
    }
}
