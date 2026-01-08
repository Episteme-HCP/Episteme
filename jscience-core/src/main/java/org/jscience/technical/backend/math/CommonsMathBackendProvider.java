/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.technical.backend.math;

import org.jscience.technical.backend.BackendProvider;
import org.jscience.technical.backend.BackendDiscovery;

/**
 * BackendProvider for Apache Commons Math.
 */
public class CommonsMathBackendProvider implements BackendProvider {
    @Override
    public String getType() {
        return BackendDiscovery.TYPE_MATH;
    }

    @Override
    public String getId() {
        return "commonsmath";
    }

    @Override
    public String getName() {
        return "Apache Commons Math";
    }

    @Override
    public String getDescription() {
        return "General purpose mathematics and statistics library.";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.apache.commons.math3.util.Precision");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public Object createBackend() {
        return null;
    }
}

