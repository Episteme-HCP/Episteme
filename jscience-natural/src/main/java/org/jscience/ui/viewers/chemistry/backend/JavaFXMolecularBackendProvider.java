/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.chemistry.backend;

import org.jscience.technical.backend.BackendProvider;

/**
 * BackendProvider for JavaFX molecular renderer.
 * Always available as JavaFX is a core dependency.
 */
public class JavaFXMolecularBackendProvider implements BackendProvider {

    @Override
    public String getType() {
        return "molecular";
    }

    @Override
    public String getId() {
        return "javafx";
    }

    @Override
    public String getName() {
        return "JavaFX 3D";
    }

    @Override
    public String getDescription() {
        return "Native JavaFX 3D molecular rendering, always available.";
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
        return new JavaFXMolecularRenderer();
    }
}
