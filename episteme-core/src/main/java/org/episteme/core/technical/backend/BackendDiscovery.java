/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.backend;

import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for discovering and accessing backends of different types.
 * <p>
 * This class provides <strong>type-based</strong> backend lookup using string type constants
 * (e.g., {@link #TYPE_PLOTTING}, {@link #TYPE_TENSOR}). It is the primary entry point for
 * UI code that needs to enumerate, filter, and select backends by category. It also integrates
 * with {@link org.episteme.core.io.UserPreferences} for persistent user preferences.
 * </p>
 * <p>
 * <strong>Relationship to {@link BackendManager} / {@link AbstractBackendManager}:</strong>
 * Domain-specific managers (e.g., {@code PlottingBackendManager}, {@code AudioBackendManager})
 * extend {@link AbstractBackendManager} for typed management with registration and default
 * selection. {@code BackendDiscovery} complements this by providing cross-cutting, type-string
 * based queries used in the settings UI. Both use {@link java.util.ServiceLoader} independently,
 * which is intentional — domain managers load their specific subtypes (e.g., {@code PlottingBackend}),
 * while this class loads the generic {@link Backend} service.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 * @see BackendManager
 * @see AbstractBackendManager
 */
public class BackendDiscovery {

    public static final String TYPE_MATH = "math";
    public static final String TYPE_TENSOR = "tensor";
    public static final String TYPE_LINEAR_ALGEBRA = "linear-algebra";
    public static final String TYPE_MOLECULAR = "molecular";
    public static final String TYPE_PLOTTING = "plotting";
    public static final String TYPE_QUANTUM = "quantum";
    public static final String TYPE_MAP = "map";
    public static final String TYPE_GRAPH = "graph";
    public static final String TYPE_AUDIO = "audio";
    public static final String TYPE_DISTRIBUTED = "distributed";
    public static final String TYPE_NETWORK = "network";
    public static final String TYPE_IO = "io";
    public static final String TYPE_ML = "ml";
    public static final String TYPE_VISION = "vision";
    public static final String TYPE_VIDEO = "video";

    private static final Logger logger = LoggerFactory.getLogger(BackendDiscovery.class);
    private static final BackendDiscovery INSTANCE = new BackendDiscovery();

    private BackendDiscovery() {}

    public static BackendDiscovery getInstance() {
        return INSTANCE;
    }

    private List<Backend> cachedProviders;

    public synchronized void refresh() {
        cachedProviders = null;
    }

    public synchronized List<Backend> getProviders() {
        if (cachedProviders == null) {
            cachedProviders = new ArrayList<>();
            ServiceLoader<Backend> loader = ServiceLoader.load(Backend.class);
            String excludeFilter = System.getProperty("org.episteme.audit.exclude", "");
            String[] excludes = excludeFilter.isEmpty() ? new String[0] : excludeFilter.split(",");

            loader.stream().forEach(provider -> {
                String className = provider.type().getName();
                boolean excluded = false;
                for (String ex : excludes) {
                    if (className.toLowerCase().contains(ex.trim().toLowerCase())) {
                        excluded = true;
                        break;
                    }
                }
                
                if (excluded) {
                    logger.debug("BackendDiscovery: Skipping excluded class: {}", className);
                    return;
                }

                try {
                    logger.info("BackendDiscovery: Initializing provider: {}", className);
                    Backend backend = provider.get();
                    logger.info("BackendDiscovery: Successfully initialized: {}", className);
                    
                    // Also check name for exclusion
                    boolean nameExcluded = false;
                    for (String ex : excludes) {
                        if (backend.getName().toLowerCase().contains(ex.trim().toLowerCase())) {
                            nameExcluded = true;
                            break;
                        }
                    }

                    if (nameExcluded) {
                        logger.debug("BackendDiscovery: Skipping excluded backend (by name): {}", backend.getName());
                    } else {
                        cachedProviders.add(backend);
                        logger.debug("Discovered Backend: {} (Priority: {})", backend.getName(), backend.getPriority());
                    }
                } catch (Throwable t) {
                    logger.warn("Skipping bad Backend provider [{}]: {}", className, t.getMessage());
                }
            });
            logger.info("Backend Discovery complete. {} backends loaded.", cachedProviders.size());
        }
        return cachedProviders;
    }

    public List<Backend> getProvidersByType(String type) {
        return getProviders().stream()
                .filter(p -> p.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public List<Backend> getAvailableProvidersByType(String type) {
        org.episteme.core.io.UserPreferences prefs = org.episteme.core.io.UserPreferences.getInstance();
        return getProvidersByType(type).stream()
                .filter(Backend::isAvailable)
                .filter(p -> !prefs.isBackendDeactivated(p.getId()))
                .sorted((p1, p2) -> {
                    // Use autotuning scores if available, otherwise fallback to static priority.
                    // We use a default size of 1024 for generic comparison.
                    double s1 = org.episteme.core.technical.algorithm.AutoTuningManager.getDynamicScore(p1.getName(), 1024, p1.getPriority());
                    double s2 = org.episteme.core.technical.algorithm.AutoTuningManager.getDynamicScore(p2.getName(), 1024, p2.getPriority());
                    return Double.compare(s2, s1);
                })
                .collect(Collectors.toList());
    }

    public Optional<Backend> getProvider(String type, String id) {
        return getProvidersByType(type).stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    public Optional<Backend> getBestProvider(String type) {
        return getAvailableProvidersByType(type).stream().findFirst();
    }

    public Optional<Backend> getPreferredProvider(String type) {
        String preferredId = org.episteme.core.io.UserPreferences.getInstance().getPreferredBackend(type);
        if (preferredId != null && !preferredId.isEmpty()) {
            Optional<Backend> preferred = getProvider(type, preferredId);
            if (preferred.isPresent() && preferred.get().isAvailable()) return preferred;
        }
        return getBestProvider(type);
    }

    public void setPreferredProvider(String type, String providerId) {
        org.episteme.core.io.UserPreferences.getInstance().setPreferredBackend(type, providerId);
    }
}
