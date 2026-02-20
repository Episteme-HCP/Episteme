/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.BackendDiscovery;

/**
 * Universal manager for algorithm providers.
 * <p>
 * Discovery uses two converging paths:
 * <ol>
 * <li>{@code ServiceLoader<AlgorithmProvider>} — direct SPI registration</li>
 * <li>{@link BackendDiscovery} — via {@link Backend#getAlgorithmProviders()}</li>
 * </ol>
 * Results from both paths are merged, deduplicated, and sorted by priority.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public final class AlgorithmManager {

    private static final Logger LOGGER = Logger.getLogger(AlgorithmManager.class.getName());
    private static final Map<Class<?>, AlgorithmProvider> BEST_PROVIDERS = new ConcurrentHashMap<>();
    private static final ProviderRegistry REGISTRY = new ProviderRegistry();

    private AlgorithmManager() {}

    /**
     * Finds and returns the best available provider for the given interface.
     * 
     * @param <P> the provider type
     * @param providerClass the interface class of the provider
     * @return the best available provider
     * @throws NoSuchElementException if no provider is found
     */
    @SuppressWarnings("unchecked")
    public static <P extends AlgorithmProvider> P getProvider(Class<P> providerClass) {
        return (P) BEST_PROVIDERS.computeIfAbsent(providerClass, k -> findBestProvider((Class<P>) k));
    }

    /**
     * Finds and returns all available providers for the given interface, sorted by priority.
     * <p>
     * Merges providers from both the AlgorithmProvider SPI and the Backend SPI
     * (via {@link Backend#getAlgorithmProviders()}).
     * </p>
     * 
     * @param <P> the provider type
     * @param providerClass the interface class of the provider
     * @return list of available providers sorted by priority (descending)
     */
    public static <P extends AlgorithmProvider> List<P> getProviders(Class<P> providerClass) {
        // Use IdentityHashMap to deduplicate by instance identity
        Set<AlgorithmProvider> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        List<P> available = new ArrayList<>();

        // Path 1: Direct SPI discovery
        ServiceLoader<P> loader = ServiceLoader.load(providerClass);
        for (P provider : loader) {
            if (provider.isAvailable() && seen.add(provider)) {
                available.add(provider);
            }
        }

        // Path 2: Backend-bridged discovery
        try {
            for (Backend backend : BackendDiscovery.getInstance().getProviders()) {
                for (AlgorithmProvider ap : backend.getAlgorithmProviders()) {
                    if (providerClass.isInstance(ap) && ap.isAvailable() && seen.add(ap)) {
                        available.add(providerClass.cast(ap));
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.fine("BackendDiscovery not available for provider bridge: " + e.getMessage());
        }

        available.sort(Comparator.comparingInt(AlgorithmProvider::getPriority).reversed());
        return available;
    }

    private static <P extends AlgorithmProvider> P findBestProvider(Class<P> providerClass) {
        List<P> available = getProviders(providerClass);

        if (available.isEmpty()) {
            throw new NoSuchElementException("No available provider found for: " + providerClass.getSimpleName());
        }
        
        P best = available.get(0);
        LOGGER.info("Selected best provider " + best.getName() + " for " + providerClass.getSimpleName() + " (Priority: " + best.getPriority() + ")");
        return best;
    }

    /**
     * Returns the provider registry for operational selection.
     */
    public static ProviderRegistry getRegistry() {
        return REGISTRY;
    }

    /**
     * Forces a refresh of the discovered providers.
     */
    public static void refresh() {
        BEST_PROVIDERS.clear();
    }
}

