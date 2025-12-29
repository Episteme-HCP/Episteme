/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui;

import java.util.List;

/**
 * Interface for reusable scientific viewer panels.
 */
public interface ScientificViewer {
    /**
     * Resets the viewer to its initial state.
     */
    void reset();

    /**
     * Returns true if the viewer is currently active/running.
     */
    boolean isRunning();

    /**
     * Returns a list of parameters exposed by this viewer.
     */
    List<Parameter<?>> getParameters();
}
