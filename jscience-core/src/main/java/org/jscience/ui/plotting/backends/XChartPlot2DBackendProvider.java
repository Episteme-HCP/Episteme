/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.plotting.backends;

import org.jscience.technical.backend.BackendProvider;

/**
 * BackendProvider for XChart 2D plotting.
 * Available when XChart library is on classpath.
 */
public class XChartPlot2DBackendProvider implements BackendProvider {

    @Override
    public String getType() {
        return "plotting";
    }

    @Override
    public String getId() {
        return "xchart";
    }

    @Override
    public String getName() {
        return "XChart";
    }

    @Override
    public String getDescription() {
        return "Lightweight library for plotting data.";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.knowm.xchart.XYChart");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 60; // Highest priority for 2D (modern, lightweight)
    }

    @Override
    public Object createBackend() {
        return new XChartPlot2D("");
    }
}
