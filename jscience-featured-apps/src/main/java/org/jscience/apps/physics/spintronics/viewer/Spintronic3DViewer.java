/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
