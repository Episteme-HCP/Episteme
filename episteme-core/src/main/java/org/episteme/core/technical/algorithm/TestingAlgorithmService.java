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

    public TestingAlgorithmService(AlgorithmProvider... providers) {
        this.allowedProviders = Arrays.asList(providers);
    }

    public TestingAlgorithmService(List<AlgorithmProvider> providers) {
        this.allowedProviders = new ArrayList<>(providers);
    }

    @Override
    public <P extends AlgorithmProvider> P getProvider(Class<P> providerClass) {
        return getProviders(providerClass).stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No allowed provider found for: " + providerClass.getSimpleName()));
    }

    @Override
    public <P extends AlgorithmProvider> List<P> getProviders(Class<P> providerClass) {
        return allowedProviders.stream()
                .filter(providerClass::isInstance)
                .map(providerClass::cast)
                .sorted(Comparator.comparingInt(AlgorithmProvider::getPriority).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public <P extends AlgorithmProvider, R> R executeWithFallback(Class<P> providerClass, Function<P, R> operation) {
        List<P> providers = getProviders(providerClass);
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
