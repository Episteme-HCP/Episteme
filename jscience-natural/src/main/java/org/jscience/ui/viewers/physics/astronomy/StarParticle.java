/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.physics.astronomy;

import javafx.scene.paint.Color;

/**
 * Represents a star particle in the galaxy simulation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StarParticle {
    public double x, y;
    public double vx, vy;
    public Color color;

    public StarParticle(double x, double y, double vx, double vy, Color c) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.color = c;
    }
}
