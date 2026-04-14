/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.algorithm;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Restricted implementation of {@link AlgorithmService} for testing.
 * Only allows a specific set of providers.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class TestingAlgorithmService implements AlgorithmService {

    private final List<AlgorithmProvider> allowedProviders;
    private final AlgorithmService delegate = new StandardAlgorithmService();

    private final boolean fallbackAllowed;

    public TestingAlgorithmService(AlgorithmProvider... providers) {
        this(Arrays.asList(providers), false);
    }

    public TestingAlgorithmService(List<AlgorithmProvider> providers) {
        this(providers, false);
    }

    public TestingAlgorithmService(List<AlgorithmProvider> providers, boolean fallbackAllowed) {
        this.allowedProviders = new ArrayList<>(providers);
        this.fallbackAllowed = fallbackAllowed;
    }

    @Override
    public <P extends AlgorithmProvider> P getProvider(Class<P> providerClass) {
        List<P> providers = getProviders(providerClass);
        if (providers.isEmpty()) {
            throw new NoSuchElementException("No allowed provider found for: " + providerClass.getSimpleName());
        }
        return providers.get(0);
    }

    @Override
    public <P extends AlgorithmProvider> List<P> getProviders(Class<P> providerClass) {
        List<P> result = allowedProviders.stream()
                .filter(providerClass::isInstance)
                .map(providerClass::cast)
                .collect(Collectors.toList());
        
        if (result.isEmpty() && fallbackAllowed) {
            return delegate.getProviders(providerClass);
        }
        return result;
    }

    @Override
    public <P extends AlgorithmProvider, R> R executeWithFallback(Class<P> providerClass, Function<P, R> operation) {
        List<P> providers = getProviders(providerClass);
        if (providers.isEmpty()) {
            throw new NoSuchElementException("No allowed provider found for: " + providerClass.getSimpleName());
        }

        if (!fallbackAllowed) {
            // Only try the first provider
            return operation.apply(providers.get(0));
        }

        UnsupportedOperationException lastError = null;
        for (P provider : providers) {
            try {
                return operation.apply(provider);
            } catch (UnsupportedOperationException e) {
                lastError = e;
            }
        }
        throw lastError != null ? lastError : new UnsupportedOperationException("No provider supports this operation for " + providerClass.getSimpleName());
    }

    @Override
    public boolean isFiltered(String name) {
        return delegate.isFiltered(name);
    }

    @Override
    public void refresh() {
        delegate.refresh();
    }

    @Override
    public void shutdown() {
        // We don't shutdown allowed providers here as they might be shared or managed elsewhere during tests
        delegate.shutdown();
    }
}
