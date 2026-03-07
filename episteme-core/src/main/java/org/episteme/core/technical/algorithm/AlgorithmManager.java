/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.algorithm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;

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

    private static final Logger logger = LoggerFactory.getLogger(AlgorithmManager.class);
    private static final Map<Class<?>, AlgorithmProvider> BEST_PROVIDERS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, List<? extends AlgorithmProvider>> PROVIDER_CACHE = new ConcurrentHashMap<>();
    private static final ProviderRegistry REGISTRY = new ProviderRegistry();

    static {
        try {
            AutoTuningManager.loadResults();
            // Detect if running in a test environment
            boolean isTest = false;
            try {
                Class.forName("org.junit.jupiter.api.Test");
                isTest = true;
            } catch (ClassNotFoundException e) {
                // Not a test environment
            }

            // Trigger benchmark if no results found
            Path path = Paths.get(System.getProperty("user.home"), ".episteme", "benchmarks.json");
            if (!Files.exists(path) && !Boolean.getBoolean("episteme.benchmark.skip") && !isTest) {
                new Thread(() -> {
                    try {
                        Thread.sleep(5000); // Wait for system to stabilize
                        BenchmarkRunner.runAll();
                        AutoTuningManager.loadResults();
                    } catch (Exception e) {
                        logger.warn("Auto-benchmark failed: {}", e.getMessage());
                    }
                }, "Episteme-AutoBenchmark").start();
            }
            Runtime.getRuntime().addShutdownHook(new Thread(AlgorithmManager::shutdown, "Episteme-Shutdown"));
        } catch (Throwable t) {
            System.err.println("[CRITICAL] AlgorithmManager static init failed: " + t.getMessage());
            t.printStackTrace();
        }
    }

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
    @SuppressWarnings("unchecked")
    public static <P extends AlgorithmProvider> List<P> getProviders(Class<P> providerClass) {
        return (List<P>) PROVIDER_CACHE.computeIfAbsent(providerClass, k -> discoverProviders((Class<P>) k));
    }

    private static <P extends AlgorithmProvider> List<P> discoverProviders(Class<P> providerClass) {
        // Key for deduplication: Class + Name
        Set<String> seenKeys = new HashSet<>();
        List<P> available = new ArrayList<>();

        // Path 1: Direct SPI discovery
        ServiceLoader<P> loader = ServiceLoader.load(providerClass);
        Iterator<P> iterator = loader.iterator();
        while (iterator.hasNext()) {
            try {
                P provider = iterator.next();
                String key = provider.getClass().getName() + ":" + provider.getName();
                if (provider.isAvailable() && seenKeys.add(key)) {
                    available.add(provider);
                }
            } catch (ServiceConfigurationError | RuntimeException e) {
                logger.warn("Skipping bad provider for {}: {}", providerClass.getSimpleName(), e.getMessage());
            }
        }

        try {
            for (Backend backend : BackendDiscovery.getInstance().getProviders()) {
                for (AlgorithmProvider ap : backend.getAlgorithmProviders()) {
                    if (providerClass.isInstance(ap)) {
                        P provider = providerClass.cast(ap);
                        String key = provider.getClass().getName() + ":" + provider.getName();
                        if (provider.isAvailable() && seenKeys.add(key)) {
                            available.add(provider);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.trace("BackendDiscovery not available for provider bridge: {}", e.getMessage());
        }

        available.sort(Comparator.comparingInt(AlgorithmProvider::getPriority).reversed());
        return available;
    }

    private static <P extends AlgorithmProvider> P findBestProvider(Class<P> providerClass) {
        List<P> available = getProviders(providerClass);

        if (available.isEmpty()) {
            logger.error("No available provider found for: {}", providerClass.getSimpleName());
            throw new NoSuchElementException("No available provider found for: " + providerClass.getSimpleName());
        }
        
        P best = available.get(0);
        logger.info("Selected best provider {} for {} (Priority: {})", best.getName(), providerClass.getSimpleName(), best.getPriority());
        return best;
    }

    /**
     * Finds and returns the reference (baseline) provider for the given interface.
     * <p>
     * The reference provider is defined as the available provider with the lowest priority.
     * </p>
     * 
     * @param <P> the provider type
     * @param providerClass the interface class of the provider
     * @return the reference provider
     * @throws NoSuchElementException if no provider is available
     */
    public static <P extends AlgorithmProvider> P getReferenceProvider(Class<P> providerClass) {
        List<P> available = getProviders(providerClass);

        if (available.isEmpty()) {
            throw new NoSuchElementException("No available provider found for: " + providerClass.getSimpleName());
        }

        // Return the one with lowest priority (likely the standard JVM implementation)
        return available.get(available.size() - 1);
    }

    /**
     * Finds and returns the next-best available provider after the given one.
     * <p>
     * Useful for chained fallbacks where a provider is available but an operation 
     * is not supported or fails.
     * </p>
     * 
     * @param <P> the provider type
     * @param providerClass the interface class of the provider
     * @param current the current provider attempting to fall back
     * @return the next provider in priority order, or the reference provider if no better option exists
     */
    public static <P extends AlgorithmProvider> P getNextProvider(Class<P> providerClass, AlgorithmProvider current) {
        List<P> available = getProviders(providerClass);
        if (available.isEmpty()) {
            throw new NoSuchElementException("No provider available for " + providerClass.getSimpleName());
        }

        String currentName = current.getName().trim();
        Class<?> currentClass = current.getClass();

        // Find the position of 'current' in the priority list
        int index = -1;
        for (int i = 0; i < available.size(); i++) {
            P p = available.get(i);
            if (p == current || (p.getClass().equals(currentClass) && p.getName().trim().equals(currentName))) {
                index = i;
                break;
            }
        }

        // Try to find a successor that is NOT basically the same as 'current'
        // We look forward in the priority list first.
        for (int i = index + 1; i < available.size(); i++) {
            P next = available.get(i);
            if (!next.getClass().equals(currentClass) && !next.getName().trim().equals(currentName)) {
                logger.debug("Falling back from {} to {} for {}", currentName, next.getName(), providerClass.getSimpleName());
                return next;
            }
        }

        // Emergency: if we can't find a forward successor, try any alternative that is actually DIFFERENT.
        // This handles cases where 'current' might not have been in the priority list somehow.
        for (P alternative : available) {
            if (!alternative.getClass().equals(currentClass) && !alternative.getName().trim().equals(currentName)) {
                logger.debug("Non-sequential fallback from {} to {} for {}", currentName, alternative.getName(), providerClass.getSimpleName());
                return alternative;
            }
        }

        logger.error("No alternative provider available for {} after {}. Total available: {}. Loop detected if we return {}.", 
            providerClass.getSimpleName(), currentName, available.size(), currentName);
        throw new NoSuchElementException("No alternative provider available for " + providerClass.getSimpleName());
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
        PROVIDER_CACHE.clear();
    }

    /**
     * Shuts down all discovered backends and providers.
     */
    public static void shutdown() {
        logger.info("Universal AlgorithmManager shutting down...");

        // Shutdown all discovered AlgorithmProviders currently in cache
        Set<AlgorithmProvider> allProviders = new HashSet<>();
        for (List<? extends AlgorithmProvider> providers : PROVIDER_CACHE.values()) {
            allProviders.addAll(providers);
        }
        allProviders.addAll(BEST_PROVIDERS.values());

        for (AlgorithmProvider provider : allProviders) {
            try {
                provider.shutdown();
            } catch (Exception e) {
                logger.warn("Error shutting down provider {}: {}", provider.getName(), e.getMessage());
            }
        }

        // Shutdown all discovered Backends
        try {
            for (Backend backend : BackendDiscovery.getInstance().getProviders()) {
                try {
                    backend.shutdown();
                } catch (Exception e) {
                    logger.warn("Error shutting down backend {}: {}", backend.getName(), e.getMessage());
                }
            }
        } catch (Exception e) {
            // BackendDiscovery might not be initialized or accessible
        }

        refresh();
    }
}

