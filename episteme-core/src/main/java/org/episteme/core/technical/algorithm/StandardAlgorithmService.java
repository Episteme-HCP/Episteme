/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.algorithm;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;
import org.episteme.core.io.UserPreferences;

/**
 * Standard implementation of {@link AlgorithmService}.
 * delegates to the traditional SPI and Backend discovery mechanisms.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class StandardAlgorithmService implements AlgorithmService {

    private static final Logger logger = LoggerFactory.getLogger(StandardAlgorithmService.class);
    
    private final Map<Class<?>, AlgorithmProvider> bestProviders = new ConcurrentHashMap<>();
    private final Map<Class<?>, List<? extends AlgorithmProvider>> providerCache = new ConcurrentHashMap<>();
    
    private static final Set<String> GLOBAL_EXCLUDES;
    private static final Set<String> GLOBAL_INCLUDES;

    static {
        String ex = System.getProperty("org.episteme.exclude.provider", "");
        String in = System.getProperty("org.episteme.include.provider", "");
        GLOBAL_EXCLUDES = ex.isEmpty() ? Set.of() : Arrays.stream(ex.split(",")).map(String::trim).map(String::toLowerCase).collect(Collectors.toSet());
        GLOBAL_INCLUDES = in.isEmpty() ? Set.of() : Arrays.stream(in.split(",")).map(String::trim).map(String::toLowerCase).collect(Collectors.toSet());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P extends AlgorithmProvider> P getProvider(Class<P> providerClass) {
        return (P) bestProviders.computeIfAbsent(providerClass, k -> findBestProvider((Class<P>) k));
    }

    private <P extends AlgorithmProvider> P findBestProvider(Class<P> providerClass) {
        List<P> available = getProviders(providerClass);
        if (available.isEmpty()) {
            logger.error("No available provider found for: {}", providerClass.getSimpleName());
            throw new NoSuchElementException("No available provider found for: " + providerClass.getSimpleName());
        }
        P best = available.get(0);
        logger.info("Selected best provider {} for {} (Priority: {})", best.getName(), providerClass.getSimpleName(), best.getPriority());
        return best;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P extends AlgorithmProvider> List<P> getProviders(Class<P> providerClass) {
        return (List<P>) providerCache.computeIfAbsent(providerClass, k -> discoverProviders((Class<P>) k));
    }

    private <P extends AlgorithmProvider> List<P> discoverProviders(Class<P> providerClass) {
        Set<String> seenKeys = new HashSet<>();
        List<P> available = new ArrayList<>();

        // Path 1: SPI
        ServiceLoader<P> loader = ServiceLoader.load(providerClass);
        Iterator<P> iterator = loader.iterator();
        while (true) {
            try {
                if (!iterator.hasNext()) break;
                P provider = iterator.next();
                if (isFiltered(provider.getName())) continue;
                String key = provider.getClass().getName() + ":" + provider.getName();
                if (provider.isAvailable() && seenKeys.add(key)) {
                    available.add(provider);
                }
            } catch (ServiceConfigurationError | Exception e) {
                logger.warn("Skipping bad provider entry: {}", e.getMessage());
            } catch (Throwable t) {
                logger.error("Critical error during provider discovery: {}", t.getMessage());
                break;
            }
        }

        // Path 2: Backends
        try {
            for (Backend backend : BackendDiscovery.getInstance().getProviders()) {
                for (AlgorithmProvider ap : backend.getAlgorithmProviders()) {
                    if (providerClass.isInstance(ap)) {
                        P provider = providerClass.cast(ap);
                        if (isFiltered(provider.getName()) || UserPreferences.getInstance().isBackendDeactivated(backend.getId())) {
                            continue;
                        }
                        String key = provider.getClass().getName() + ":" + provider.getName();
                        if (provider.isAvailable() && seenKeys.add(key)) {
                            available.add(provider);
                        }
                    }
                }
            }
        } catch (Exception e) {}

        available.sort(Comparator.comparingInt(AlgorithmProvider::getPriority).reversed());
        return available;
    }

    @Override
    public boolean isFiltered(String name) {
        if (name == null) return false;
        String lowerName = name.trim().toLowerCase();
        if (!GLOBAL_INCLUDES.isEmpty()) {
            boolean matchesInclude = false;
            for (String inc : GLOBAL_INCLUDES) {
                if (lowerName.contains(inc)) {
                    matchesInclude = true;
                    break;
                }
            }
            if (!matchesInclude) return true;
        }
        for (String exc : GLOBAL_EXCLUDES) {
            if (!exc.isEmpty() && lowerName.contains(exc)) return true;
        }
        return false;
    }

    @Override
    public <P extends AlgorithmProvider, R> R executeWithFallback(Class<P> providerClass, Function<P, R> operation) {
        if (Boolean.getBoolean("episteme.fallback.disabled")) {
            return operation.apply(getProvider(providerClass));
        }
        
        List<P> providers = getProviders(providerClass);
        UnsupportedOperationException lastError = null;
        for (P provider : providers) {
            try {
                return operation.apply(provider);
            } catch (UnsupportedOperationException e) {
                logger.info("Operation not supported by {}, falling back (Cause: {})", provider.getName(), e.getMessage());
                lastError = e;
            }
        }
        throw lastError != null ? lastError : new UnsupportedOperationException("No provider supports this operation for " + providerClass.getSimpleName());
    }

    @Override
    public void refresh() {
        bestProviders.clear();
        providerCache.clear();
    }

    @Override
    public void shutdown() {
        Set<AlgorithmProvider> all = new HashSet<>();
        for (List<? extends AlgorithmProvider> l : providerCache.values()) all.addAll(l);
        all.addAll(bestProviders.values());
        for (AlgorithmProvider p : all) {
            try { p.shutdown(); } catch (Exception e) { logger.warn("Error shutting down provider {}: {}", p.getName(), e.getMessage()); }
        }
        refresh();
    }
}
