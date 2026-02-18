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

package org.jscience.core.technical.backend;

import java.util.*;

/**
 * Global registry and manager for all discoverable backends.
 * <p>
 * Provides static access for backward compatibility and a general-purpose manager.
 * This class manages instances of the base {@link Backend} type, while domain-specific
 * managers (e.g., {@code PlottingBackendManager}, {@code AudioBackendManager}) extend
 * {@link AbstractBackendManager} with their specific backend interfaces.
 * </p>
 * <p>
 * <strong>Relationship to {@link BackendDiscovery}:</strong>
 * {@code BackendDiscovery} provides type-string based queries and user preference integration,
 * primarily for UI backend selection panels. This class provides name-based lookup and
 * explicit registration. The {@link #staticGetProvidersByType(String)} method delegates to
 * {@code BackendDiscovery} to avoid duplicating type-based filtering logic.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see BackendDiscovery
 * @see AbstractBackendManager
 */
public class BackendManager extends AbstractBackendManager<Backend> {

    private static final BackendManager INSTANCE = new BackendManager();

    private BackendManager() {
        super(Backend.class);
    }

    /**
     * Returns the global singleton instance.
     */
    public static BackendManager getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the default backend.
     */
    public static Backend staticGetDefault() {
        return INSTANCE.managerDefault();
    }

    /**
     * Selects a backend by name.
     */
    public static Backend staticSelect(String name) {
        return INSTANCE.managerSelect(name);
    }

    /**
     * Sets the default backend.
     */
    public static void staticSetDefault(String name) {
        INSTANCE.managerSetDefault(name);
        // Backends are now persisted immediately on setXXX(), so mostly no need to save here
        // except for legacy plotting/linear algebra fields kept in this class
        // org.jscience.core.technical.backend.Backend b2d = (org.jscience.core.technical.backend.Backend) getPlottingBackend2D();
        // org.jscience.core.technical.backend.Backend b3d = (org.jscience.core.technical.backend.Backend) getPlottingBackend3D();
        // prefs.set("plotting.backend.2d", b2d != null ? b2d.getId() : "auto");
        // prefs.set("plotting.backend.3d", b3d != null ? b3d.getId() : "auto");
        // if (getLinearAlgebraProviderId() != null) prefs.set("linear.algebra.backend", getLinearAlgebraProviderId());
    }

    /**
     * Registers a backend.
     */
    public static void staticRegister(Backend backend) {
        INSTANCE.managerRegister(backend);
    }

    /**
     * Returns names of all registered backends.
     */
    public static Collection<String> staticAvailableNames() {
        return INSTANCE.managerNames();
    }

    /**
     * Returns all registered backends.
     */
    public static Collection<Backend> staticAllBackends() {
        return INSTANCE.managerAll();
    }

    /**
     * Returns providers by type using Discovery.
     */
    public static List<Backend> staticGetProvidersByType(String type) {
        return BackendDiscovery.getInstance().getProvidersByType(type);
    }
}
