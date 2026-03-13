/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.algorithm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;
import org.episteme.core.io.UserPreferences;

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
    private static final ProviderRegistry REGISTRY = new ProviderRegistry();
    private static AlgorithmService service = new StandardAlgorithmService();

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
                        AutoTuningRunner.runAll();
                        AutoTuningManager.loadResults();
                    } catch (Exception e) {
                        logger.warn("Auto-benchmark (tuning) failed: {}", e.getMessage());
                    }
                }, "Episteme-AutoTuning").start();
            }
            Runtime.getRuntime().addShutdownHook(new Thread(AlgorithmManager::shutdown, "Episteme-Shutdown"));
        } catch (Throwable t) {
            System.err.println("[CRITICAL] AlgorithmManager static init failed: " + t.getMessage());
            t.printStackTrace();
        }
    }

    private AlgorithmManager() {}

    /**
     * Sets the AlgorithmService to use.
     */
    public static void setService(AlgorithmService newService) {
        service = newService;
    }

    /**
     * Gets the current AlgorithmService.
     */
    public static AlgorithmService getService() {
        return service;
    }

    /**
     * Finds and returns the best available provider for the given interface.
     */
    public static <P extends AlgorithmProvider> P getProvider(Class<P> providerClass) {
        return service.getProvider(providerClass);
    }

    /**
     * Finds and returns all available providers for the given interface, sorted by priority.
     */
    public static <P extends AlgorithmProvider> List<P> getProviders(Class<P> providerClass) {
        return service.getProviders(providerClass);
    }

    /**
     * Finds and returns the reference (baseline) provider for the given interface.
     */
    public static <P extends AlgorithmProvider> P getReferenceProvider(Class<P> providerClass) {
        List<P> available = getProviders(providerClass);
        if (available.isEmpty()) {
            throw new NoSuchElementException("No available provider found for: " + providerClass.getSimpleName());
        }
        return available.get(available.size() - 1);
    }

    /**
     * Finds and returns the next-best available provider after the given one.
     */
    public static <P extends AlgorithmProvider> P getNextProvider(Class<P> providerClass, AlgorithmProvider current) {
        List<P> available = getProviders(providerClass);
        if (available.isEmpty()) {
            throw new NoSuchElementException("No provider available for " + providerClass.getSimpleName());
        }

        String currentName = current.getName().trim();
        Class<?> currentClass = current.getClass();

        int index = -1;
        for (int i = 0; i < available.size(); i++) {
            P p = available.get(i);
            if (p == current || (p.getClass().equals(currentClass) && p.getName().trim().equals(currentName))) {
                index = i;
                break;
            }
        }

        for (int i = index + 1; i < available.size(); i++) {
            P next = available.get(i);
            if (!next.getClass().equals(currentClass) && !next.getName().trim().equals(currentName)) {
                return next;
            }
        }

        for (P alternative : available) {
            if (!alternative.getClass().equals(currentClass) && !alternative.getName().trim().equals(currentName)) {
                return alternative;
            }
        }

        throw new NoSuchElementException("No alternative provider available for " + providerClass.getSimpleName());
    }

    /**
     * Executes an operation using the best available provider, automatically falling back
     * to the next provider if the current one throws {@link UnsupportedOperationException}.
     */
    public static <P extends AlgorithmProvider, R> R executeWithFallback(
            Class<P> providerClass, java.util.function.Function<P, R> operation) {
        return service.executeWithFallback(providerClass, operation);
    }

    /**
     * Returns the provider registry for operational selection.
     */
    public static ProviderRegistry getRegistry() {
        return REGISTRY;
    }

    /**
     * Checks if a provider with the given name is filtered out by global configuration.
     */
    public static boolean isFiltered(String name) {
        return service.isFiltered(name);
    }

    /**
     * Forces a refresh of the discovered providers.
     */
    public static void refresh() {
        service.refresh();
    }

    /**
     * Shuts down all discovered backends and providers.
     */
    public static void shutdown() {
        logger.info("Universal AlgorithmManager shutting down...");
        service.shutdown();
        
        // Also shutdown all discovered Backends (Backend SPI)
        try {
            for (Backend backend : BackendDiscovery.getInstance().getProviders()) {
                try {
                    backend.shutdown();
                } catch (Exception e) {
                    logger.warn("Error shutting down backend {}: {}", backend.getName(), e.getMessage());
                }
            }
        } catch (Exception e) {}
    }
}

