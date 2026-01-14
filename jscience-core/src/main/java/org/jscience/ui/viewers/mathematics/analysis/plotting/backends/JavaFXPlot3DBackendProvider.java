/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.mathematics.analysis.plotting.backends;

import org.jscience.technical.backend.BackendProvider;

/**
 * BackendProvider for JavaFX 3D plotting.
 * Always available as JavaFX is a core dependency.
 */
public class JavaFXPlot3DBackendProvider implements BackendProvider {

    @Override
    public String getType() {
        return "plotting";
    }

    @Override
    public String getId() {
        return "javafx3d";
    }

    @Override
    public String getName() {
        return "JavaFX 3D";
    }

    @Override
    public String getDescription() {
        return "Native JavaFX 3D plotting, always available.";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getPriority() {
        return 10; // Fallback for 3D
    }

    @Override
    public Object createBackend() {
        return new JavaFXPlot3D("");
    }
}
