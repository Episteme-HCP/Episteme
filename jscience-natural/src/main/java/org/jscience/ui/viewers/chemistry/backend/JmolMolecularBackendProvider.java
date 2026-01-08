/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.chemistry.backend;

import org.jscience.technical.backend.BackendProvider;

/**
 * BackendProvider for Jmol molecular renderer.
 * Available only when Jmol library is on the classpath.
 */
public class JmolMolecularBackendProvider implements BackendProvider {

    @Override
    public String getType() {
        return "molecular";
    }

    @Override
    public String getId() {
        return "jmol";
    }

    @Override
    public String getName() {
        return "Jmol";
    }

    @Override
    public String getDescription() {
        return "Open-source Java viewer for chemical structures with scripting support.";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.jmol.viewer.Viewer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 50; // Higher priority than JavaFX when available
    }

    @Override
    public Object createBackend() {
        return new JmolMolecularRenderer();
    }
}
