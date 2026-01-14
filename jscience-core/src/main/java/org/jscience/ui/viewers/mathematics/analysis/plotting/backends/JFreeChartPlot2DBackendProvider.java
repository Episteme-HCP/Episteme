/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.mathematics.analysis.plotting.backends;

import org.jscience.technical.backend.BackendProvider;

/**
 * BackendProvider for JFreeChart 2D plotting.
 * Available when JFreeChart library is on classpath.
 */
public class JFreeChartPlot2DBackendProvider implements BackendProvider {

    @Override
    public String getType() {
        return "plotting";
    }

    @Override
    public String getId() {
        return "jfreechart";
    }

    @Override
    public String getName() {
        return "JFreeChart";
    }

    @Override
    public String getDescription() {
        return "Professional charting and plot library.";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.jfree.chart.JFreeChart");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 50; // Medium-high priority for 2D
    }

    @Override
    public Object createBackend() {
        return new JFreeChartPlot2D("");
    }
}
