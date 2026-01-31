/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.physics;

import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.ExecutionContext;
import java.nio.DoubleBuffer;

/**
 * Interface for high-performance collision detection and physics simulation.
 * Optimized for N-body collisions using spatial partitioning and native solvers.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public interface CollisionBackend extends ComputeBackend {

    /**
     * Performs collision detection between spheres.
     *
     * @param positions Flattened array [x0, y0, z0, x1, y1, z1, ...]
     * @param radii     Array of radii [r0, r1, ...]
     * @param n         Number of spheres
     * @param collisions Output buffer for collision pairs [idA, idB, ...]
     * @return Number of collisions found
     */
    int detectSphereCollisions(DoubleBuffer positions, DoubleBuffer radii, int n, java.nio.IntBuffer collisions);

    /**
     * Resolves collisions by updating velocities.
     */
    void resolveCollisions(DoubleBuffer positions, DoubleBuffer velocities, DoubleBuffer masses, int n, java.nio.IntBuffer collisions, int numCollisions);

    @Override
    default ExecutionContext createContext() {
        return null;
    }
}

