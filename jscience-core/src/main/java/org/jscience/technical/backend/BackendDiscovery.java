/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
    public static final String TYPE_QUANTUM = "quantum";
    public static final String TYPE_MAP = "map";
    public static final String TYPE_NETWORK = "network";

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
        Iterator<BackendProvider> iterator = loader.iterator();
        while (iterator.hasNext()) {
            try {
                all.add(iterator.next());
            } catch (ServiceConfigurationError e) {
                // Log and skip providers that cannot be loaded
                System.err.println("Warning: Could not load backend provider: " + e.getMessage());
            }
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

    /**
     * Returns the preferred provider for a type, considering user preferences.
     * Falls back to best available if no preference is set.
     */
    public Optional<BackendProvider> getPreferredProvider(String type) {
        String preferredId = org.jscience.io.UserPreferences.getInstance().getPreferredBackend(type);
        if (preferredId != null && !preferredId.isEmpty()) {
            Optional<BackendProvider> preferred = getProvider(type, preferredId);
            if (preferred.isPresent() && preferred.get().isAvailable()) {
                return preferred;
            }
        }
        return getBestProvider(type);
    }

    /**
     * Sets the preferred provider for a type (persists to user preferences).
     */
    public void setPreferredProvider(String type, String providerId) {
        org.jscience.io.UserPreferences.getInstance().setPreferredBackend(type, providerId);
    }
}
