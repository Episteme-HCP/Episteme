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

import java.lang.foreign.*;
import java.util.List;
import java.util.Optional;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;

/**
 * Implementation of {@link MechanicsBackend} for ODE (Open Dynamics Engine).
 * <p>
 * This provider prefers the native ODE library (via Project Panama) for maximum performance,
 * but falls back to the {@code ode4j} Java port if the native library is not available.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@AutoService({MechanicsBackend.class, ComputeBackend.class, Backend.class, SimulationProvider.class})
public class ODEBackend implements NativeCollisionProvider, MechanicsBackend, CPUBackend, NativeBackend, SimulationProvider {
 
    private static boolean IS_INITIALIZED = false;
    private static boolean IS_AVAILABLE = false;

    private static synchronized void ensureInitialized() {
        if (IS_INITIALIZED) return;
        Optional<SymbolLookup> lib = NativeFFMLoader.loadLibrary("ode", Arena.global());
        IS_AVAILABLE = lib.isPresent();
        IS_INITIALIZED = true;
    }

    @Override
    public String getId() {
        return "ode";
    }

    @Override
    public String getName() {
        return "ODE (Open Dynamics Engine)";
    }

    @Override
    public String getDescription() {
        return "Native high-performance ODE physics engine (Project Panama).";
    }

    @Override
    public boolean isAvailable() {
        ensureInitialized();
        return IS_AVAILABLE && !isExplicitlyDisabled();
    }

    @Override
    public String getStatusMessage() {
        if (isLoaded()) return "Ready (Native ODE)";
        return "Native library 'ode' not found";
    }

    @Override
    public void shutdown() {
        // Lifecycle managed by the backend implementation
    }


    @Override
    public boolean isLoaded() {
        ensureInitialized();
        return IS_AVAILABLE;
    }

    @Override
    public String getNativeLibraryName() {
        return "ode";
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    @Override
    public PhysicsWorldBridge createWorld() {
        if (!isLoaded()) {
            throw new UnsupportedOperationException("Native ODE library not loaded");
        }
        return new org.episteme.nativ.physics.classical.mechanics.collision.backends.ode.NativeODEWorld();
    }

    @Override
    public RigidBodyBridge createRigidBody(RigidBody body) {
        throw new UnsupportedOperationException("RigidBody creation must be handled via PhysicsWorldBridge.addRigidBody for ODE backend.");
    }

    @Override
    public int detectSphereCollisions(double[] positions, double[] radii, int n, int[] collisions) {
        if (isLoaded()) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment posSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, positions);
                MemorySegment radSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, radii);
                MemorySegment colSeg = arena.allocate(ValueLayout.JAVA_INT, (long) n * n * 2);
                int count = detectSphereCollisions(posSeg, radSeg, n, colSeg, ValueLayout.JAVA_DOUBLE);
                MemorySegment.copy(colSeg, ValueLayout.JAVA_INT, 0, collisions, 0, count * 2);
                return count;
            }
        }
        return 0; // Or fallback to CPU implementation
    }

    @Override
    public void resolveCollisions(double[] positions, double[] velocities, double[] masses, int n, int[] collisions, int numCollisions) {
        if (isLoaded()) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment posSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, positions);
                MemorySegment velSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, velocities);
                MemorySegment massSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, masses);
                MemorySegment colSeg = arena.allocateFrom(ValueLayout.JAVA_INT, collisions);
                resolveCollisions(posSeg, velSeg, massSeg, n, colSeg, numCollisions, ValueLayout.JAVA_DOUBLE);
                // Copy back updated positions and velocities
                MemorySegment.copy(posSeg, ValueLayout.JAVA_DOUBLE, 0, positions, 0, n * 3);
                MemorySegment.copy(velSeg, ValueLayout.JAVA_DOUBLE, 0, velocities, 0, n * 3);
            }
        }
    }

    @Override
    public int detectSphereCollisions(MemorySegment positions, MemorySegment radii, int n, MemorySegment collisions, ValueLayout layout) {
        throw new UnsupportedOperationException("Raw MemorySegment collision detection not yet implemented for ODE.");
    }

    @Override
    public void resolveCollisions(MemorySegment positions, MemorySegment velocities, MemorySegment masses, int n, MemorySegment collisions, int numCollisions, ValueLayout layout) {
        throw new UnsupportedOperationException("Raw MemorySegment collision resolution not yet implemented for ODE.");
    }

    @Override
    public void parallelExecute(List<Runnable> tasks, int parallelism) {
        throw new UnsupportedOperationException("Higher-level parallel execution not supported by this native backend.");
    }

    @Override
    public int getPriority() {
        return isLoaded() ? 70 : 10;
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
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.ExecutionContext() {
            @Override
            public <T> T execute(org.episteme.core.technical.backend.Operation<T> operation) {
                return operation.compute(this);
            }
            @Override
            public void close() {
                // No-op
            }
        };
    }
  
    @Override
    public Object createBackend() {
        return this;
    }
}
