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
        return "javafx_mol";
    }

    @Override
    public String getName() {
        return "JavaFX Molecular";
    }

    @Override
    public String getDescription() {
        return "Integrated JavaFX 3D molecular visualization engine.";
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
