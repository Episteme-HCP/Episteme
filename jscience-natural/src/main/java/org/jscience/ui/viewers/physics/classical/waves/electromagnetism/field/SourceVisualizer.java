/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.physics.classical.waves.electromagnetism.field;

import javafx.scene.Node;
import org.jscience.technical.backend.algorithms.MaxwellSource;

/**
 * Interface for visualizing field sources in the MagneticFieldViewer.
 */
public interface SourceVisualizer {
    /**
     * Checks if this visualizer supports the given source.
     */
    boolean supports(MaxwellSource source);

    /**
     * Returns a visual representation of the source.
     */
    Node getVisualRepresentation(MaxwellSource source);
}
