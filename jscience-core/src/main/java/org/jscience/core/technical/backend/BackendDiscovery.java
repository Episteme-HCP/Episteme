/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility for discovering and accessing backends of different types.
 * <p>
 * This class provides <strong>type-based</strong> backend lookup using string type constants
 * (e.g., {@link #TYPE_PLOTTING}, {@link #TYPE_TENSOR}). It is the primary entry point for
 * UI code that needs to enumerate, filter, and select backends by category. It also integrates
 * with {@link org.jscience.core.io.UserPreferences} for persistent user preferences.
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

    private static final BackendDiscovery INSTANCE = new BackendDiscovery();

    private BackendDiscovery() {}

    public static BackendDiscovery getInstance() {
        return INSTANCE;
    }

    private List<Backend> cachedProviders;

    public synchronized List<Backend> getProviders() {
        if (cachedProviders == null) {
            cachedProviders = new ArrayList<>();
            ServiceLoader<Backend> loader = ServiceLoader.load(Backend.class);
            for (Backend provider : loader) cachedProviders.add(provider);
        }
        return cachedProviders;
    }

    public List<Backend> getProvidersByType(String type) {
        return getProviders().stream()
                .filter(p -> p.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public List<Backend> getAvailableProvidersByType(String type) {
        return getProvidersByType(type).stream()
                .filter(Backend::isAvailable)
                .sorted(Comparator.comparingInt(Backend::getPriority).reversed())
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
        String preferredId = org.jscience.core.io.UserPreferences.getInstance().getPreferredBackend(type);
        if (preferredId != null && !preferredId.isEmpty()) {
            Optional<Backend> preferred = getProvider(type, preferredId);
            if (preferred.isPresent() && preferred.get().isAvailable()) return preferred;
        }
        return getBestProvider(type);
    }

    public void setPreferredProvider(String type, String providerId) {
        org.jscience.core.io.UserPreferences.getInstance().setPreferredBackend(type, providerId);
    }
}
