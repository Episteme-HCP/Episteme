/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.algorithm;

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
        return select(providerClass, context, null);
    }

    /**
     * Selects the best provider for the given operation context, with an optional filter.
     */
    public static <P extends AlgorithmProvider> P select(Class<P> providerClass, OperationContext context, java.util.function.Predicate<P> filter) {
        List<P> providers = AlgorithmManager.getProviders(providerClass);

        if (providers.isEmpty()) {
            throw new NoSuchElementException("No available provider for: " + providerClass.getSimpleName());
        }

        P best = providers.stream()
                .filter(p -> filter == null || filter.test(p))
                .max(Comparator.comparingDouble(p -> p.score(context)))
                .orElseThrow(() -> new NoSuchElementException("No provider satisfying filter for: " + providerClass.getSimpleName()));

        LOGGER.fine("Selected " + best.getName() + " (score=" + best.score(context) + ") for " + providerClass.getSimpleName());
        return best;
    }

    /**
     * Executes an operation using the best available provider, with automatic fallback.
     * <p>
     * Iterates through all available providers (sorted by score) and attempts the operation.
     * If a provider fails, it logs the warning and tries the next best provider.
     * </p>
     *
     * @param <P> the provider type
     * @param <R> the return type
     * @param providerClass the interface class of the provider
     * @param context the operation context
     * @param operation the operation to execute
     * @return the result of the operation
     * @throws RuntimeException if all providers fail
     */
    public static <P extends AlgorithmProvider, R> R execute(Class<P> providerClass, OperationContext context, java.util.function.Function<P, R> operation) {
        List<P> providers = AlgorithmManager.getProviders(providerClass);
        providers.sort(Comparator.comparingDouble((P p) -> p.score(context)).reversed());

        Throwable lastError = null;
        for (P provider : providers) {
            try {
                return operation.apply(provider);
            } catch (Throwable t) {
                LOGGER.warning("Provider " + provider.getName() + " failed: " + t.getMessage() + ". Attempting fallback...");
                lastError = t;
            }
        }

        throw new RuntimeException("All " + providers.size() + " providers for " + providerClass.getSimpleName() + " failed.", lastError);
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
