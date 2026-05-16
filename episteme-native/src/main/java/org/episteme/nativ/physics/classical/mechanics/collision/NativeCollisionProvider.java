package org.episteme.nativ.physics.classical.mechanics.collision;

import org.episteme.natural.physics.classical.mechanics.collision.CollisionProvider;
import java.lang.foreign.MemorySegment;

/**
 * Native implementation of CollisionProvider using FFM (Project Panama).
 * Optimized for hardware-accelerated collision detection.
 */
public interface NativeCollisionProvider extends CollisionProvider {

    /**
     * Performs collision detection between spheres using MemorySegments.
     */
    int detectSphereCollisions(MemorySegment positions, MemorySegment radii, int n, MemorySegment collisions, java.lang.foreign.ValueLayout layout);

    /**
     * Resolves collisions using MemorySegments.
     */
    void resolveCollisions(MemorySegment positions, MemorySegment velocities, MemorySegment masses, int n, MemorySegment collisions, int numCollisions, java.lang.foreign.ValueLayout layout);

    @Override
    default int detectSphereCollisions(float[] positions, float[] radii, int n, int[] collisions) {
        return 0;
    }

    @Override
    default int detectSphereCollisions(double[] positions, double[] radii, int n, int[] collisions) {
        // Default implementation that can be overridden or use a fallback
        return 0; 
    }

    @Override
    default void resolveCollisions(float[] positions, float[] velocities, float[] masses, int n, int[] collisions, int numCollisions) {
    }

    @Override
    default void resolveCollisions(double[] positions, double[] velocities, double[] masses, int n, int[] collisions, int numCollisions) {
        // Default implementation
    }
    /**
     * Returns true if the native library is loaded and operational.
     */
    boolean isLoaded();
}


