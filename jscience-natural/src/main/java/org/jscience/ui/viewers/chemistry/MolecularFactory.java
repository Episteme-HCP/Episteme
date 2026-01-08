/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.chemistry;

import org.jscience.technical.backend.BackendDiscovery;
import org.jscience.technical.backend.BackendProvider;

import org.jscience.ui.viewers.chemistry.backend.MolecularRenderer;
import org.jscience.ui.viewers.chemistry.backend.JavaFXMolecularRenderer;

import java.util.List;
import java.util.Optional;

/**
 * Factory for creating molecular renderers using SPI-based backend discovery.
 * Similar to {@link org.jscience.ui.plotting.PlotFactory}.
 */
public class MolecularFactory {

    // private static String selectedBackendId = null; // Delegated to JScience.java

    /**
     * Sets the preferred backend by ID.
     * 
     * @param backendId Backend ID (e.g., "javafx", "jmol") or null for AUTO
     */
    /**
     * Sets the preferred backend by ID.
     * 
     * @param backendId Backend ID (e.g., "javafx", "jmol") or null for AUTO
     */
    public static void setBackend(String backendId) {
        org.jscience.JScience.setMolecularBackendId(backendId);
    }

    /**
     * Gets the currently selected backend ID.
     */
    public static String getSelectedBackendId() {
        return org.jscience.JScience.getMolecularBackendId();
    }

    /**
     * Creates a molecular renderer using the selected (or best available) backend.
     */
    public static MolecularRenderer createRenderer() {
        BackendProvider provider;
        String selectedBackendId = getSelectedBackendId();

        if (selectedBackendId != null) {
            // Use specifically selected backend
            Optional<BackendProvider> specific = BackendDiscovery.getInstance()
                    .getProvider(BackendDiscovery.TYPE_MOLECULAR, selectedBackendId);
            if (specific.isPresent() && specific.get().isAvailable()) {
                provider = specific.get();
            } else {
                System.out.println("Selected backend '" + selectedBackendId + "' not available, using best available.");
                provider = getBestProvider();
            }
        } else {
            // Auto-select best available
            provider = getBestProvider();
        }

        if (provider != null) {
            Object backend = provider.createBackend();
            if (backend instanceof MolecularRenderer) {
                return (MolecularRenderer) backend;
            }
        }

        // Ultimate fallback
        System.out.println("No backends discovered, using fallback JavaFX renderer.");
        return new JavaFXMolecularRenderer();
    }

    /**
     * Gets the best available molecular backend provider.
     */
    private static BackendProvider getBestProvider() {
        Optional<BackendProvider> best = BackendDiscovery.getInstance()
                .getBestProvider(BackendDiscovery.TYPE_MOLECULAR);
        return best.orElse(null);
    }

    /**
     * Returns all discovered molecular backend providers.
     */
    public static List<BackendProvider> getAvailableBackends() {
        return BackendDiscovery.getInstance()
                .getProvidersByType(BackendDiscovery.TYPE_MOLECULAR);
    }

    /**
     * Returns only the available (loaded) molecular backends.
     */
    public static List<BackendProvider> getLoadedBackends() {
        return BackendDiscovery.getInstance()
                .getAvailableProvidersByType(BackendDiscovery.TYPE_MOLECULAR);
    }

    /**
     * Checks if a specific backend is available.
     */
    public static boolean isBackendAvailable(String backendId) {
        Optional<BackendProvider> provider = BackendDiscovery.getInstance()
                .getProvider(BackendDiscovery.TYPE_MOLECULAR, backendId);
        return provider.isPresent() && provider.get().isAvailable();
    }

    // Legacy compatibility methods

    public static boolean isJmolAvailable() {
        return isBackendAvailable("jmol");
    }

    public static boolean isPyMOLAvailable() {
        return isBackendAvailable("pymol");
    }

    public static boolean isVMDAvailable() {
        return isBackendAvailable("vmd");
    }
}
