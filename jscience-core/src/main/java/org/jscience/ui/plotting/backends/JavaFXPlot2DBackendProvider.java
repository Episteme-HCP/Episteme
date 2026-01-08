/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.plotting.backends;

import org.jscience.technical.backend.BackendProvider;

/**
 * BackendProvider for JavaFX 2D plotting.
 * Always available as JavaFX is a core dependency.
 */
public class JavaFXPlot2DBackendProvider implements BackendProvider {

    @Override
    public String getType() {
        return "plotting";
    }

    @Override
    public String getId() {
        return "javafx";
    }

    @Override
    public String getName() {
        return "JavaFX Charts";
    }

    @Override
    public String getDescription() {
        return "Native JavaFX charts, always available.";
    }

    @Override
    public boolean isAvailable() {
        return true; // JavaFX is always available
    }

    @Override
    public int getPriority() {
        return 10; // Base priority, fallback option
    }

    @Override
    public Object createBackend() {
        return new JavaFXPlot2D(""); // Title set later
    }
}
