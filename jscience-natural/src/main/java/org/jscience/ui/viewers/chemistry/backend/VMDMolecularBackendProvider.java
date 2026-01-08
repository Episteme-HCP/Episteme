/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.chemistry.backend;

import org.jscience.technical.backend.BackendProvider;

/**
 * BackendProvider for VMD (Visual Molecular Dynamics).
 * VMD is an external application for biomolecular simulations.
 */
public class VMDMolecularBackendProvider implements BackendProvider {

    @Override
    public String getType() {
        return "molecular";
    }

    @Override
    public String getId() {
        return "vmd";
    }

    @Override
    public String getName() {
        return "VMD";
    }

    @Override
    public String getDescription() {
        return "Visual Molecular Dynamics for biomolecular simulation (external process).";
    }

    @Override
    public boolean isAvailable() {
        // Check if vmd executable exists in PATH
        // For now, return false as not yet implemented
        return false;
    }

    @Override
    public int getPriority() {
        return 30;
    }

    @Override
    public Object createBackend() {
        // Would create a VMD bridge renderer
        throw new UnsupportedOperationException("VMD backend not yet implemented");
    }
}
