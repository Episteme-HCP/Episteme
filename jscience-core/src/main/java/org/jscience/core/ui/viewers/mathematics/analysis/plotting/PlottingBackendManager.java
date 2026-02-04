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

import org.jscience.core.technical.backend.AbstractBackendManager;
import java.util.Comparator;

/**
 * Manager for plotting backends, orchestrating 2D and 3D visualization.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class PlottingBackendManager extends AbstractBackendManager<PlottingBackend> {

    private static final PlottingBackendManager INSTANCE = new PlottingBackendManager();

    public static PlottingBackendManager getInstance() {
        return INSTANCE;
    }

    private String preferred2DId = "auto";
    private String preferred3DId = "auto";

    private PlottingBackendManager() {
        super(PlottingBackend.class);
    }

    /**
     * Gets the current 2D plotting backend.
     */
    public PlottingBackend get2D() {
        if ("auto".equalsIgnoreCase(preferred2DId)) {
            return selectBest2D();
        }
        PlottingBackend b = select(preferred2DId);
        return (b != null && b.isSupported2D()) ? b : selectBest2D();
    }

    /**
     * Sets the preferred 2D plotting backend by ID.
     */
    public void set2D(String id) {
        this.preferred2DId = id;
    }

    /**
     * Gets the current 3D plotting backend.
     */
    public PlottingBackend get3D() {
        if ("auto".equalsIgnoreCase(preferred3DId)) {
            return selectBest3D();
        }
        PlottingBackend b = select(preferred3DId);
        return (b != null && b.isSupported3D()) ? b : selectBest3D();
    }

    /**
     * Sets the preferred 3D plotting backend by ID.
     */
    public void set3D(String id) {
        this.preferred3DId = id;
    }

    private PlottingBackend selectBest2D() {
        return backends.values().stream()
                .filter(PlottingBackend::isAvailable)
                .filter(PlottingBackend::isSupported2D)
                .max(Comparator.comparingInt(PlottingBackend::getPriority))
                .orElse(null);
    }

    private PlottingBackend selectBest3D() {
        return backends.values().stream()
                .filter(PlottingBackend::isAvailable)
                .filter(PlottingBackend::isSupported3D)
                .max(Comparator.comparingInt(PlottingBackend::getPriority))
                .orElse(null);
    }
}
