/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics.viewer;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Abstract interface for 3D spintronic structure visualization.
 * Supports multiple backends (JavaFX 3D, Jzy3d).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public interface Spintronic3DViewer {

    /**
     * Adds a ferromagnetic layer to the scene.
     * @param y Y position (vertical stack)
     * @param thickness Layer thickness (for display scaling)
     * @param color Color hex string (e.g., "#34495e")
     * @param label Layer name
     */
    void addLayer(double y, double thickness, String color, String label);

    /**
     * Adds a magnetization arrow at given position.
     * @param x X position
     * @param y Y position
     * @param z Z position
     * @param mx Magnetization X component
     * @param my Magnetization Y component
     * @param mz Magnetization Z component
     * @param id Unique ID for updates
     */
    void addMagnetizationArrow(double x, double y, double z, Real mx, Real my, Real mz, String id);

    /**
     * Updates an existing magnetization arrow.
     */
    void updateMagnetizationArrow(String id, Real mx, Real my, Real mz);

    /**
     * Adds a spin current flow visualization (arrow field).
     */
    void addSpinCurrentFlow(double fromY, double toY, int density);

    /**
     * Clears the scene.
     */
    void clear();

    /**
     * Gets the renderable component (JavaFX Node, AWT Component, etc.)
     */
    Object getComponent();

    /**
     * Sets the view angle.
     */
    void setViewAngle(double azimuth, double elevation);
}
