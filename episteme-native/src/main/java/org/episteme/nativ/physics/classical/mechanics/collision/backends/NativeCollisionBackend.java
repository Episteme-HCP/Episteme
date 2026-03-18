/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.physics.classical.mechanics.collision.backends;

import com.google.auto.service.AutoService;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.natural.physics.classical.mechanics.collision.MechanicsBackend;
import org.episteme.natural.physics.classical.mechanics.collision.PhysicsWorldBridge;
import org.episteme.natural.physics.classical.mechanics.collision.RigidBody;
import org.episteme.natural.physics.classical.mechanics.collision.RigidBodyBridge;
import org.episteme.natural.physics.classical.mechanics.simulation.SimulationProvider;
import org.episteme.nativ.physics.classical.mechanics.collision.NativeCollisionProvider;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
/**
 * Native implementation of {@link MechanicsBackend} for general collision processing.
 * Acts as a bridge to native optimized collision kernels.
 */
@AutoService({MechanicsBackend.class, ComputeBackend.class, Backend.class, SimulationProvider.class})
public class NativeCollisionBackend implements NativeCollisionProvider, MechanicsBackend, CPUBackend, NativeBackend, SimulationProvider {

    @Override
    public String getId() {
        return "native-collision";
    }

    @Override
    public String getName() {
        return "Native Collision Backend";
    }

    @Override
    public String getDescription() {
        return "Native Optimized Collision Detection and Resolution.";
    }

    @Override
    public boolean isLoaded() {
        return IS_AVAILABLE;
    }

    @Override
    public String getNativeLibraryName() {
        return "episteme-native";
    }

    private static final java.lang.invoke.MethodHandle DETECT_SPHERES;
    private static final java.lang.invoke.MethodHandle RESOLVE_COLLISIONS;
    private static final boolean IS_AVAILABLE;

    static {
        java.lang.foreign.Linker linker = java.lang.foreign.Linker.nativeLinker();
        java.lang.foreign.Arena arena = java.lang.foreign.Arena.global();
        java.util.Optional<java.lang.foreign.SymbolLookup> lib = org.episteme.nativ.technical.backend.nativ.NativeFFMLoader.loadLibrary("episteme-native", arena);
        
        if (lib.isPresent()) {
            java.lang.foreign.SymbolLookup lookup = lib.get();
            DETECT_SPHERES = org.episteme.nativ.technical.backend.nativ.NativeFFMLoader.findSymbol(lookup, "detect_sphere_collisions")
                .map(s -> linker.downcallHandle(s, java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_INT, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.JAVA_INT, java.lang.foreign.ValueLayout.ADDRESS))).orElse(null);
            RESOLVE_COLLISIONS = org.episteme.nativ.technical.backend.nativ.NativeFFMLoader.findSymbol(lookup, "resolve_sphere_collisions")
                .map(s -> linker.downcallHandle(s, java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.JAVA_INT, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.JAVA_INT))).orElse(null);
            IS_AVAILABLE = DETECT_SPHERES != null;
        } else {
            DETECT_SPHERES = null;
            RESOLVE_COLLISIONS = null;
            IS_AVAILABLE = false;
        }
    }

    @Override
    public String getType() {
        return "mechanics";
    }

    @Override
    public String getAlgorithmType() {
        return "mechanics";
    }

    @Override
    public int getPriority() {
        return 70;
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    /**
     * This backend is specialized for high-performance batch collision detection 
     * and resolution using primitive arrays and direct FFM segments.
     * It does not support higher-level world or rigid body abstractions.
     */
    @Override
    public PhysicsWorldBridge createWorld() {
        throw new UnsupportedOperationException("Physics world creation not supported by the specialized batch native backend.");
    }

    @Override
    public RigidBodyBridge createRigidBody(RigidBody body) {
        throw new UnsupportedOperationException("Rigid body creation not supported by the specialized batch native backend.");
    }

    @Override
    public int detectSphereCollisions(double[] positions, double[] radii, int n, int[] collisions) {
        if (!IS_AVAILABLE) return 0;
        try (java.lang.foreign.Arena arena = java.lang.foreign.Arena.ofConfined()) {
            java.lang.foreign.MemorySegment posSeg = arena.allocateFrom(java.lang.foreign.ValueLayout.JAVA_DOUBLE, positions);
            java.lang.foreign.MemorySegment radSeg = arena.allocateFrom(java.lang.foreign.ValueLayout.JAVA_DOUBLE, radii);
            java.lang.foreign.MemorySegment colSeg = arena.allocate(java.lang.foreign.ValueLayout.JAVA_INT, (long) n * n * 2);
            int count = detectSphereCollisions(posSeg, radSeg, n, colSeg);
            java.lang.foreign.MemorySegment.copy(colSeg, java.lang.foreign.ValueLayout.JAVA_INT, 0, collisions, 0, count * 2);
            return count;
        }
    }

    @Override
    public void resolveCollisions(double[] positions, double[] velocities, double[] masses, int n, int[] collisions, int numCollisions) {
        if (!IS_AVAILABLE) return;
        try (java.lang.foreign.Arena arena = java.lang.foreign.Arena.ofConfined()) {
            java.lang.foreign.MemorySegment posSeg = arena.allocateFrom(java.lang.foreign.ValueLayout.JAVA_DOUBLE, positions);
            java.lang.foreign.MemorySegment velSeg = arena.allocateFrom(java.lang.foreign.ValueLayout.JAVA_DOUBLE, velocities);
            java.lang.foreign.MemorySegment massSeg = arena.allocateFrom(java.lang.foreign.ValueLayout.JAVA_DOUBLE, masses);
            java.lang.foreign.MemorySegment colSeg = arena.allocateFrom(java.lang.foreign.ValueLayout.JAVA_INT, collisions);
            resolveCollisions(posSeg, velSeg, massSeg, n, colSeg, numCollisions);
            java.lang.foreign.MemorySegment.copy(posSeg, java.lang.foreign.ValueLayout.JAVA_DOUBLE, 0, positions, 0, n * 3);
            java.lang.foreign.MemorySegment.copy(velSeg, java.lang.foreign.ValueLayout.JAVA_DOUBLE, 0, velocities, 0, n * 3);
        }
    }

    @Override
    public int detectSphereCollisions(java.lang.foreign.MemorySegment positions, java.lang.foreign.MemorySegment radii, int n, java.lang.foreign.MemorySegment collisions) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("Native library 'episteme-native' not loaded");
        try {
            return (int) DETECT_SPHERES.invokeExact(positions, radii, n, collisions);
        } catch (Throwable t) {
            throw new RuntimeException("Native collision detection failed", t);
        }
    }

    @Override
    public void resolveCollisions(java.lang.foreign.MemorySegment positions, java.lang.foreign.MemorySegment velocities, java.lang.foreign.MemorySegment masses, int n, java.lang.foreign.MemorySegment collisions, int numCollisions) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("Native library 'episteme-native' not loaded");
        try {
            RESOLVE_COLLISIONS.invokeExact(positions, velocities, masses, n, collisions, numCollisions);
        } catch (Throwable t) {
            throw new RuntimeException("Native collision resolution failed", t);
        }
    }

    @Override
    public void parallelExecute(java.util.List<Runnable> tasks, int parallelism) {
        tasks.parallelStream().forEach(Runnable::run);
    }

    @Override
    public void shutdown() {
    }

    @Override
    public Object createBackend() {
        return this;
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.cpu.CPUExecutionContext();
    }
}
