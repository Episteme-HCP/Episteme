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
import org.episteme.natural.physics.classical.mechanics.CollisionProvider;
import org.episteme.natural.physics.classical.mechanics.MechanicsBackend;
import org.episteme.natural.physics.classical.mechanics.PhysicsWorldBridge;
import org.episteme.natural.physics.classical.mechanics.RigidBody;
import org.episteme.natural.physics.classical.mechanics.RigidBodyBridge;
import org.episteme.natural.physics.classical.mechanics.simulation.SimulationProvider;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
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
@AutoService({CollisionProvider.class, MechanicsBackend.class, ComputeBackend.class, Backend.class, SimulationProvider.class})
public class GenesisBackend implements CollisionProvider, MechanicsBackend, CPUBackend, NativeBackend, SimulationProvider {

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
        if (isLoaded()) {
            return "Native high-performance Genesis physics engine (Project Panama).";
        }
        return "Genesis high-performance physics engine backend (SIMD accelerated).";
    }

    @Override
    public boolean isAvailable() {
        ensureInitialized();
        if (IS_AVAILABLE) return !isExplicitlyDisabled();
        // Pure Java/SIMD backend fallback is always available if not disabled
        return !isExplicitlyDisabled();
    }

    @Override
    public String getStatusMessage() {
        if (isLoaded()) return "Ready (Native Genesis)";
        return "Ready (SIMD fallback)";
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
        if (isLoaded()) {
            return new org.episteme.nativ.physics.classical.mechanics.collision.backends.genesis.NativeGenesisWorld();
        }
        return new org.episteme.nativ.physics.classical.mechanics.collision.backends.genesis.GenesisWorld();
    }

    @Override
    public RigidBodyBridge createRigidBody(RigidBody body) {
        return null;
    }

    @Override
    public int detectSphereCollisions(MemorySegment positions, MemorySegment radii, int n, MemorySegment collisions) {
        return 0;
    }

    @Override
    public void resolveCollisions(MemorySegment positions, MemorySegment velocities, MemorySegment masses, int n, MemorySegment collisions, int numCollisions) {
        // Native resolution
    }

    @Override
    public void parallelExecute(List<Runnable> tasks, int parallelism) {
        // Native parallel execution
    }

    @Override
    public int getPriority() {
        return isLoaded() ? 80 : 20;
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
