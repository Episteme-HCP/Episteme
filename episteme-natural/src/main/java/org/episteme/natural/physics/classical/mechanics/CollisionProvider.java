/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.physics.classical.mechanics;

import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.ExecutionContext;
import java.lang.foreign.MemorySegment;

/**
 * Interface for high-performance collision detection and physics simulation.
 * Optimized for N-body collisions using spatial partitioning and native solvers.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public interface CollisionProvider extends ComputeBackend, AlgorithmProvider {

    @Override
    default void shutdown() {
        // Default no-op
    }

    @Override
    default int getPriority() {
        return 0;
    }

    @Override
    default boolean isAvailable() {
        return true;
    }

    @Override
    default String getAlgorithmType() {
        return "collision";
    }

    /**
     * Performs collision detection between spheres.
     *
     * @param positions Flattened array [x0, y0, z0, x1, y1, z1, ...] as MemorySegment
     * @param radii     Array of radii [r0, r1, ...] as MemorySegment
     * @param n         Number of spheres
     * @param collisions Output segment for collision pairs [idA, idB, ...]
     * @return Number of collisions found
     */
    int detectSphereCollisions(MemorySegment positions, MemorySegment radii, int n, MemorySegment collisions);

    /**
     * Resolves collisions by updating velocities.
     *
     * @param positions  Positions segment
     * @param velocities Velocities segment
     * @param masses     Masses segment
     * @param n          Number of spheres
     * @param collisions Collisions segment
     * @param numCollisions Number of collision pairs
     */
    void resolveCollisions(MemorySegment positions, MemorySegment velocities, MemorySegment masses, int n, MemorySegment collisions, int numCollisions);

    @Override
    default ExecutionContext createContext() {
        return null;
    }
}
