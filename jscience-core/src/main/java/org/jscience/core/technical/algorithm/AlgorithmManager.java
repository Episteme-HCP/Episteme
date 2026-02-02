/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Universal manager for algorithm providers.
 * Discovery is based on ServiceProviderInterface (SPI) and priority.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public final class AlgorithmManager {

    private static final Logger LOGGER = Logger.getLogger(AlgorithmManager.class.getName());
    private static final Map<Class<?>, AlgorithmProvider> BEST_PROVIDERS = new ConcurrentHashMap<>();

    private AlgorithmManager() {}

    /**
     * Finds and returns the best available provider for the given interface.
     * 
     * @param <P> the provider type
     * @param providerClass the interface class of the provider
     * @return the best available provider
     * @throws RuntimeException if no provider is found (including fallbacks)
     */
    @SuppressWarnings("unchecked")
    public static <P extends AlgorithmProvider> P getProvider(Class<P> providerClass) {
        return (P) BEST_PROVIDERS.computeIfAbsent(providerClass, k -> findBestProvider((Class<P>) k));
    }

    private static <P extends AlgorithmProvider> P findBestProvider(Class<P> providerClass) {
        ServiceLoader<P> loader = ServiceLoader.load(providerClass);
        List<P> available = new ArrayList<>();
        
        for (P provider : loader) {
            if (provider.isAvailable()) {
                available.add(provider);
            }
        }

        if (available.isEmpty()) {
            throw new NoSuchElementException("No available provider found for: " + providerClass.getName());
        }

        // Sort by priority descending
        available.sort(Comparator.comparingInt(AlgorithmProvider::getPriority).reversed());
        
        P best = available.get(0);
        LOGGER.info("Selected best provider " + best.getName() + " for " + providerClass.getSimpleName() + " (Priority: " + best.getPriority() + ")");
        return best;
    }

    /**
     * Forces a refresh of the discovered providers.
     */
    public static void refresh() {
        BEST_PROVIDERS.clear();
    }
}
