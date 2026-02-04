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

package org.jscience.social.ui.viewers.geography;

import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.BackendDiscovery;
import org.jscience.social.ui.viewers.geography.backends.JavaFXMapBackendProvider;

import java.util.List;
import java.util.Optional;

/**
 * Factory for creating maps using SPI-based backend discovery.
 * Auto-detects best available backend if not specified.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MapFactory {

    private static String selectedBackendId = null; // null = AUTO

    /**
     * Sets the preferred backend by ID.
     * 
     * @param backendId Backend ID (e.g., "javafx_map", "openmap", "geotools") or null
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
     * Creates a map with default/AUTO backend.
     */
    public static Object createMap(String title) {
        Backend provider = getBackendProvider();
        if (provider != null) {
            Object backend = provider.createBackend();
            // Assuming map backends return something usable directly or we need a wrapper.
            // PlotFactory returns Plot2D/Plot3D which are interfaces or classes implemented by backend object.
            // Map backends seem to return Object in createBackend().
            // For now, returning Object as the "Map".
            return backend;
        }

        // Ultimate fallback
        return new JavaFXMapBackendProvider().createBackend();
    }
    
    /**
     * Creates a map with specified backend (compatibility).
     */
    public static Object createMap(String title, MapBackend backend) {
         if (backend == null) {
            return createMap(title);
        }
        // If backend is passed as instance, we can just use it.
        // But PlotFactory logic uses ID from enum. Mapping logic might be needed if MapBackend was still an enum.
        // Since MapBackend is now an interface, passing an instance is explicit.
        // However, looking at PlotFactory: create2D(String title, PlottingBackend backend) where PlottingBackend IS AN ENUM (Wait, PlottingBackend is an Interface in step 1108? But PlotFactory checked for backend == PlottingBackend.AUTO? Maybe it was an enum before or I am confused.
        // In Step 1131, MapBackend WAS an enum. PlotFactory step 1099 uses `PlottingBackend backend` argument. Step 1108 shows PlottingBackend as Interface.
        // Step 1146 "grep enum PlottingBackend" found no results.
        // Wait, step 1099 PlotFactory lines 82-83: `create2D(String title, PlottingBackend backend) { if (backend == null || backend == PlottingBackend.AUTO) ...`
        // This implies PlottingBackend has a static field AUTO or IS an enum.
        // Step 1108 PlottingBackend definition: `public interface PlottingBackend extends Backend`. It does NOT have AUTO field shown.
        // Maybe it has constants?
        // Or maybe I missed something.
        // Anyway, for MapFactory, I will allow passing ID string or just rely on setBackend.
        
        return backend.createBackend();
    }

    /**
     * Gets the best available map backend provider.
     */
    private static Backend getBackendProvider() {
        if (selectedBackendId != null) {
             Optional<Backend> specific = MapBackendManager.getInstance().select(selectedBackendId) != null ? 
                 Optional.of(MapBackendManager.getInstance().select(selectedBackendId)) : Optional.empty();
                 
             if (specific.isPresent() && specific.get().isAvailable()) {
                return specific.get();
             }
        }

        // Auto-select best available (sorted by priority)
        return MapBackendManager.getInstance().getActiveBackend();
    }

    /**
     * Returns all discovered map backend providers.
     */
    public static List<MapBackend> getAvailableBackends() {
        return (List<MapBackend>) MapBackendManager.getInstance().getAllBackends();
    }

    /**
     * Checks if a specific backend is available.
     */
    public static boolean isBackendAvailable(String backendId) {
        MapBackend b = MapBackendManager.getInstance().select(backendId);
        return b != null && b.isAvailable();
    }
}
