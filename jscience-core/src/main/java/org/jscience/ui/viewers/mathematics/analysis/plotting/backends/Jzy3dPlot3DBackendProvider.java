/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.mathematics.analysis.plotting.backends;

import org.jscience.technical.backend.BackendProvider;

/**
 * BackendProvider for Jzy3D 3D plotting.
 * Available when Jzy3D library is on classpath.
 */
public class Jzy3dPlot3DBackendProvider implements BackendProvider {

    @Override
    public String getType() {
        return "plotting";
    }

    @Override
    public String getId() {
        return "jzy3d";
    }

    @Override
    public String getName() {
        return "Jzy3D";
    }

    @Override
    public String getDescription() {
        return "Java library for 3D charts and scientific plotting.";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.jzy3d.chart.Chart");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 70; // High priority for 3D (OpenGL-based)
    }

    @Override
    public Object createBackend() {
        return new Jzy3dPlot3D("");
    }
}
