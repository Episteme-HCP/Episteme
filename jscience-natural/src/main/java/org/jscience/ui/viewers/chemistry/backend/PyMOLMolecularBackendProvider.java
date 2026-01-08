/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.chemistry.backend;

import org.jscience.technical.backend.BackendProvider;

/**
 * BackendProvider for PyMOL molecular renderer.
 * PyMOL is an external application that requires a bridge.
 */
public class PyMOLMolecularBackendProvider implements BackendProvider {

    @Override
    public String getType() {
        return "molecular";
    }

    @Override
    public String getId() {
        return "pymol";
    }

    @Override
    public String getName() {
        return "PyMOL";
    }

    @Override
    public String getDescription() {
        return "Molecular visualization system with advanced rendering (external process).";
    }

    @Override
    public boolean isAvailable() {
        // Check if pymol executable exists in PATH
        // For now, return false as not yet implemented
        return false;
    }

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public Object createBackend() {
        // Would create a PyMOL bridge renderer
        throw new UnsupportedOperationException("PyMOL backend not yet implemented");
    }
}
