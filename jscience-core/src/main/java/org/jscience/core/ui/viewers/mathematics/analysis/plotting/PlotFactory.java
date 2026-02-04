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

package org.jscience.core.ui.viewers.mathematics.analysis.plotting;

import org.jscience.core.technical.backend.BackendDiscovery;
import org.jscience.core.technical.backend.Backend;

import java.util.List;
import java.util.Optional;

/**
 * Factory for creating plots using SPI-based backend discovery.
 * Auto-detects best available backend if not specified.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PlotFactory {

    private static String selectedBackendId = null; // null = AUTO

    /**
     * Sets the preferred backend by ID.
     * 
     * @param backendId Backend ID (e.g., "javafx", "xchart", "jfreechart") or null
     *                  for AUTO
     */
    public static void setBackend(String backendId) {
        selectedBackendId = backendId;
    }

    /**
     * Gets the currently selected backend ID.
     */
    public static String getSelectedBackendId() {
        return selectedBackendId;
    }

    /**
     * Creates 2D plot with default/AUTO backend.
     */
    public static Plot2DRenderer create2D(String title) {
        Backend provider = getBackendProvider();
        if (provider != null) {
            Object backend = provider.createBackend();
            if (backend instanceof Plot2DRenderer) {
                Plot2DRenderer plot = (Plot2DRenderer) backend;
                plot.setTitle(title);
                return plot;
            }
        }

        // Ultimate fallback
        return new org.jscience.core.ui.viewers.mathematics.analysis.plotting.backends.JavaFXPlot2D(title);
    }

    /**
     * Creates 2D plot with specified backend enum (compatibility).
     */
    public static Plot2DRenderer create2D(String title, PlottingBackend backend) {
        if (backend == null || backend == PlottingBackend.AUTO) {
            return create2D(title);
        }
        String id = backend.name().toLowerCase();
        Optional<Backend> provider = BackendDiscovery.getInstance()
                .getProvider(BackendDiscovery.TYPE_PLOTTING, id);

        if (provider.isPresent() && provider.get().isAvailable()) {
            Plot2DRenderer plot = (Plot2DRenderer) provider.get().createBackend();
            plot.setTitle(title);
            return plot;
        }
        return create2D(title);
    }

    /**
     * Creates 3D plot with default/AUTO backend.
     */
    public static Plot3DRenderer create3D(String title) {
        // For 3D, prefer jzy3d specifically if available
        Optional<Backend> jzy3d = BackendDiscovery.getInstance()
                .getProvider(BackendDiscovery.TYPE_PLOTTING, "jzy3d");

        if (jzy3d.isPresent() && jzy3d.get().isAvailable()) {
            Object backend = jzy3d.get().createBackend();
            if (backend instanceof Plot3DRenderer) {
                Plot3DRenderer plot = (Plot3DRenderer) backend;
                plot.setTitle(title);
                return plot;
            }
        }

        // Fallback to JavaFX 3D
        Optional<Backend> javafx3d = BackendDiscovery.getInstance()
                .getProvider(BackendDiscovery.TYPE_PLOTTING, "javafx3d");

        if (javafx3d.isPresent()) {
            Object backend = javafx3d.get().createBackend();
            if (backend instanceof Plot3DRenderer) {
                Plot3DRenderer plot = (Plot3DRenderer) backend;
                plot.setTitle(title);
                return plot;
            }
        }

        return new org.jscience.core.ui.viewers.mathematics.analysis.plotting.backends.JavaFXPlot3D(title);
    }

    /**
     * Creates 3D plot with specified backend enum (compatibility).
     */
    public static Plot3DRenderer create3D(String title, PlottingBackend backend) {
        if (backend == null || backend == PlottingBackend.AUTO) {
            return create3D(title);
        }
        String id = backend.name().toLowerCase();
        if (backend == PlottingBackend.JAVAFX) {
            id = "javafx3d";
        }

        Optional<Backend> provider = BackendDiscovery.getInstance()
                .getProvider(BackendDiscovery.TYPE_PLOTTING, id);

        if (provider.isPresent() && provider.get().isAvailable()) {
            Object backendObj = provider.get().createBackend();
            if (backendObj instanceof Plot3DRenderer) {
                Plot3DRenderer plot = (Plot3DRenderer) backendObj;
                plot.setTitle(title);
                return plot;
            }
        }
        return create3D(title);
    }

    /**
     * Gets the best available plotting backend provider for 2D.
     */
    private static Backend getBackendProvider() {
        if (selectedBackendId != null) {
            Optional<Backend> specific = BackendDiscovery.getInstance()
                    .getProvider(BackendDiscovery.TYPE_PLOTTING, selectedBackendId);
            if (specific.isPresent() && specific.get().isAvailable()) {
                return specific.get();
            }
        }

        // Auto-select best available (sorted by priority)
        Optional<Backend> best = BackendDiscovery.getInstance()
                .getBestProvider(BackendDiscovery.TYPE_PLOTTING);
        return best.orElse(null);
    }

    /**
     * Returns all discovered plotting backend providers.
     */
    public static List<Backend> getAvailableBackends() {
        return BackendDiscovery.getInstance()
                .getProvidersByType(BackendDiscovery.TYPE_PLOTTING);
    }

    /**
     * Returns only the available (loaded) plotting backends.
     */
    public static List<Backend> getLoadedBackends() {
        return BackendDiscovery.getInstance()
                .getAvailableProvidersByType(BackendDiscovery.TYPE_PLOTTING);
    }

    /**
     * Checks if a specific backend is available.
     */
    public static boolean isBackendAvailable(String backendId) {
        Optional<Backend> provider = BackendDiscovery.getInstance()
                .getProvider(BackendDiscovery.TYPE_PLOTTING, backendId);
        return provider.isPresent() && provider.get().isAvailable();
    }
}


