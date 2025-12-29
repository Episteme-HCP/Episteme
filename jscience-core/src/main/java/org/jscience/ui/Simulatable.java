/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui;

/**
 * Interface for viewers or engines that support simulation/animation control.
 */
public interface Simulatable {
    /** Starts or resumes the simulation. */
    void play();

    /** Pauses the simulation. */
    void pause();

    /** Stops and resets the simulation. */
    void stop();

    /** Advances the simulation by a single step. */
    void step();

    /** Sets the simulation speed (e.g., 1.0 for normal, 2.0 for double). */
    void setSpeed(double multiplier);

    /** Returns true if the simulation is currently playing. */
    boolean isPlaying();
}
