/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.technical.backend;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility for discovering and accessing backends of different types.
 * Uses ServiceLoader to find implementations of BackendProvider.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class BackendDiscovery {

    public static final String TYPE_PLOTTING = "plotting";
    public static final String TYPE_MOLECULAR = "molecular";
    public static final String TYPE_MATH = "math";
    public static final String TYPE_TENSOR = "tensor";
    public static final String TYPE_LINEAR_ALGEBRA = "linear-algebra";

    private static final BackendDiscovery INSTANCE = new BackendDiscovery();

    private BackendDiscovery() {
    }

    public static BackendDiscovery getInstance() {
        return INSTANCE;
    }

    /**
     * Returns all discovered backend providers.
     */
    public List<BackendProvider> getProviders() {
        List<BackendProvider> all = new ArrayList<>();
        ServiceLoader<BackendProvider> loader = ServiceLoader.load(BackendProvider.class);
        for (BackendProvider provider : loader) {
            all.add(provider);
        }
        return all;
    }

    /**
     * Returns providers of a specific type.
     */
    public List<BackendProvider> getProvidersByType(String type) {
        return getProviders().stream()
                .filter(p -> p.getType().equals(type))
                .collect(Collectors.toList());
    }

    /**
     * Returns available (currently working) providers of a specific type.
     */
    public List<BackendProvider> getAvailableProvidersByType(String type) {
        return getProvidersByType(type).stream()
                .filter(BackendProvider::isAvailable)
                .sorted(Comparator.comparingInt(BackendProvider::getPriority).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Finds a provider by type and ID.
     */
    public Optional<BackendProvider> getProvider(String type, String id) {
        return getProvidersByType(type).stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    /**
     * Returns the best available provider for a type (highest priority).
     */
    public Optional<BackendProvider> getBestProvider(String type) {
        return getAvailableProvidersByType(type).stream().findFirst();
    }
}
