/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.technical.backend;

/**
 * BackendProvider for Colt.
 */
public class ColtBackendProvider implements BackendProvider {
    @Override
    public String getType() {
        return BackendDiscovery.TYPE_MATH;
    }

    @Override
    public String getId() {
        return "colt";
    }

    @Override
    public String getName() {
        return "Colt";
    }

    @Override
    public String getDescription() {
        return "Open Source Libraries for High Performance Scientific and Technical Computing.";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("cern.colt.Version");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 60;
    }

    @Override
    public Object createBackend() {
        return null;
    }
}
