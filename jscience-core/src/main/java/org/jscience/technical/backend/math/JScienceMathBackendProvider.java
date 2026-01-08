/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.technical.backend.math;

import org.jscience.technical.backend.BackendProvider;
import org.jscience.technical.backend.BackendDiscovery;

/**
 * BackendProvider for JScience Core Math engine.
 */
public class JScienceMathBackendProvider implements BackendProvider {
    @Override
    public String getType() {
        return BackendDiscovery.TYPE_MATH;
    }

    @Override
    public String getId() {
        return "jsci_core";
    }

    @Override
    public String getName() {
        return "JScience Core";
    }

    @Override
    public String getDescription() {
        return "Native JScience mathematical framework.";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public Object createBackend() {
        return null;
    }
}

