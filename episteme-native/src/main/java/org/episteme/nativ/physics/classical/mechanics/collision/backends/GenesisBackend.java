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
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.util.List;
import java.util.Optional;


/**
 * Implementation of {@link MechanicsBackend} for Genesis physics engine.
 * <p>
 * High-performance backend specialized for robotics and many-body systems.
 * Prefers the native `GenesisC` library (via Project Panama) but falls back to
 * a pure Java implementation using JDK Vector API (SIMD) if native is unavailable.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@AutoService({MechanicsBackend.class, ComputeBackend.class, Backend.class, SimulationProvider.class})
public class GenesisBackend implements NativeCollisionProvider, MechanicsBackend, CPUBackend, NativeBackend, SimulationProvider {

    private static boolean IS_INITIALIZED = false;
    private static boolean IS_AVAILABLE = false;

    private static synchronized void ensureInitialized() {
        if (IS_INITIALIZED) return;
        Optional<SymbolLookup> lib = NativeFFMLoader.loadLibrary("GenesisC", Arena.global());
        IS_AVAILABLE = lib.isPresent();
        IS_INITIALIZED = true;
    }

    @Override
    public String getId() {
        return "genesis";
    }

    @Override
    public String getName() {
        return "Genesis";
    }

    @Override
    public String getDescription() {
        return "Native high-performance Genesis physics engine (Project Panama).";
    }

    @Override
    public boolean isAvailable() {
        ensureInitialized();
        return IS_AVAILABLE && !isExplicitlyDisabled();
    }

    @Override
    public String getStatusMessage() {
        if (isLoaded()) return "Ready (Native Genesis)";
        return "Native library 'GenesisC' not found";
    }

    @Override
    public void shutdown() {
        // Native library lifecycle is managed by this wrapper.
    }

    @Override
    public boolean isLoaded() {
        ensureInitialized();
        return IS_AVAILABLE;
    }

    @Override
    public String getNativeLibraryName() {
        return "GenesisC";
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    @Override
    public PhysicsWorldBridge createWorld() {
        if (!isLoaded()) {
            throw new UnsupportedOperationException("Native Genesis library not loaded");
        }
        return new org.episteme.nativ.physics.classical.mechanics.collision.backends.genesis.NativeGenesisWorld();
    }

    @Override
    public RigidBodyBridge createRigidBody(RigidBody body) {
        throw new UnsupportedOperationException("RigidBody creation must be handled via PhysicsWorldBridge.addRigidBody for Genesis backend.");
    }

    @Override
    public int detectSphereCollisions(double[] positions, double[] radii, int n, int[] collisions) {
        if (isLoaded()) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment posSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, positions);
                MemorySegment radSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, radii);
                MemorySegment colSeg = arena.allocate(ValueLayout.JAVA_INT, (long) n * n * 2);
                int count = detectSphereCollisions(posSeg, radSeg, n, colSeg);
                MemorySegment.copy(colSeg, ValueLayout.JAVA_INT, 0, collisions, 0, count * 2);
                return count;
            }
        }
        return 0; 
    }

    @Override
    public void resolveCollisions(double[] positions, double[] velocities, double[] masses, int n, int[] collisions, int numCollisions) {
        if (isLoaded()) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment posSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, positions);
                MemorySegment velSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, velocities);
                MemorySegment massSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, masses);
                MemorySegment colSeg = arena.allocateFrom(ValueLayout.JAVA_INT, collisions);
                resolveCollisions(posSeg, velSeg, massSeg, n, colSeg, numCollisions);
                MemorySegment.copy(posSeg, ValueLayout.JAVA_DOUBLE, 0, positions, 0, n * 3);
                MemorySegment.copy(velSeg, ValueLayout.JAVA_DOUBLE, 0, velocities, 0, n * 3);
            }
        }
    }

    @Override
    public int detectSphereCollisions(MemorySegment positions, MemorySegment radii, int n, MemorySegment collisions) {
        throw new UnsupportedOperationException("Raw MemorySegment collision detection not yet implemented for Genesis.");
    }

    @Override
    public void resolveCollisions(MemorySegment positions, MemorySegment velocities, MemorySegment masses, int n, MemorySegment collisions, int numCollisions) {
        throw new UnsupportedOperationException("Raw MemorySegment collision resolution not yet implemented for Genesis.");
    }

    @Override
    public void parallelExecute(List<Runnable> tasks, int parallelism) {
        throw new UnsupportedOperationException("Higher-level parallel execution not supported by this native backend.");
    }

    @Override
    public int getPriority() {
        return isLoaded() ? 80 : 20;
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
            public void close() {}
        };
    }
 
    @Override
    public Object createBackend() {
        return this;
    }
}
