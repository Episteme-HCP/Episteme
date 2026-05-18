package org.episteme.natural.physics.classical.mechanics.collision;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * Interface for high-performance collision detection and physics simulation.
 */
public interface CollisionProvider extends AlgorithmProvider {

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
    int detectSphereCollisions(float[] positions, float[] radii, int n, int[] collisions);

    int detectSphereCollisions(double[] positions, double[] radii, int n, int[] collisions);

    int detectSphereCollisions(Real[] positions, Real[] radii, int n, int[] collisions);

    void resolveCollisions(float[] positions, float[] velocities, float[] masses, int n, int[] collisions, int numCollisions);

    void resolveCollisions(double[] positions, double[] velocities, double[] masses, int n, int[] collisions, int numCollisions);

    void resolveCollisions(Real[] positions, Real[] velocities, Real[] masses, int n, int[] collisions, int numCollisions);

}
