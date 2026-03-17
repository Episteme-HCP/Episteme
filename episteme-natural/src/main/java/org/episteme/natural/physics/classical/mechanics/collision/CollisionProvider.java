package org.episteme.natural.physics.classical.mechanics.collision;

import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.backend.Backend;

/**
 * Interface for high-performance collision detection and physics simulation.
 */
public interface CollisionProvider extends AlgorithmProvider, Backend {
    @Override
    default int getPriority() {
        return 0;
    }

    @Override
    default boolean isAvailable() {
        return true;
    }

    @Override
    default String getType() {
        return "collision";
    }

    @Override
    default Object createBackend() {
        return this;
    }

    @Override
    default void shutdown() {
        AlgorithmProvider.super.shutdown();
    }

    @Override
    default String getAlgorithmType() {
        return "collision";
    }

    /**
     * Performs collision detection between spheres.
     *
     * @param positions Flattened array [x0, y0, z0, x1, y1, z1, ...]
     * @param radii     Array of radii [r0, r1, ...]
     * @param n         Number of spheres
     * @param collisions Output array for collision pairs [idA, idB, ...]
     * @return Number of collisions found
     */
    int detectSphereCollisions(double[] positions, double[] radii, int n, int[] collisions);

    /**
     * Resolves collisions by updating velocities.
     *
     * @param positions  Positions array
     * @param velocities Velocities array
     * @param masses     Masses array
     * @param n          Number of spheres
     * @param collisions Collisions array
     * @param numCollisions Number of collision pairs
     */
    void resolveCollisions(double[] positions, double[] velocities, double[] masses, int n, int[] collisions, int numCollisions);

}
