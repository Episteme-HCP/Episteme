/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.natural.physics.classical.mechanics.backends;

import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.backend.cpu.CPUBackend;
import org.jscience.natural.physics.classical.mechanics.MechanicsBackend;
import org.jscience.natural.physics.classical.mechanics.PhysicsWorldBridge;
import org.jscience.natural.physics.classical.mechanics.RigidBodyBridge;
import org.jscience.natural.physics.classical.mechanics.CollisionProvider;
import java.lang.foreign.MemorySegment;
import org.jscience.natural.physics.classical.mechanics.RigidBody;

/**
 * Genesis physics backend provider.
 * High-performance backend specialized for robotics and many-body systems.
 */
@AutoService({AlgorithmProvider.class, MechanicsBackend.class, CollisionProvider.class})
public class GenesisBackend implements MechanicsBackend, CPUBackend, CollisionProvider {

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
        return "Genesis high-performance physics engine backend.";
    }

    @Override
    public boolean isAvailable() {
        // Genesis wraps Bullet via libbulletc
        String libName = "libbulletc.dll"; 
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("linux")) libName = "libbulletc.so";
        else if (os.contains("mac")) libName = "libbulletc.dylib";

        return java.nio.file.Files.exists(java.nio.file.Paths.get("libs", "Bullet3DLL", libName)) ||
               java.nio.file.Files.exists(java.nio.file.Paths.get("libs", libName)) ||
               java.nio.file.Files.exists(java.nio.file.Paths.get("native", libName)) ||
               java.nio.file.Files.exists(java.nio.file.Paths.get(libName));
    }

    @Override
    public String getAlgorithmType() {
        return "mechanics";
    }

    @Override
    public int getPriority() {
        return 20; // High priority for specialized engine
    }

    @Override
    public org.jscience.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.jscience.core.technical.backend.HardwareAccelerator.CPU;
    }

    @Override
    public PhysicsWorldBridge createWorld() {
        return new org.jscience.natural.physics.classical.mechanics.backends.genesis.GenesisWorld();
    }

    @Override
    public RigidBodyBridge createRigidBody(RigidBody body) {
        return null; // Created by world.addRigidBody
    }

    @Override
    public int detectSphereCollisions(MemorySegment positions, MemorySegment radii, int n, MemorySegment collisions) {
        // Placeholder for Genesis-specific optimized collision detection
        return 0;
    }

    @Override
    public void resolveCollisions(MemorySegment positions, MemorySegment velocities, MemorySegment masses, int n, MemorySegment collisions, int numCollisions) {
        // Placeholder for Genesis-specific collision resolution
    }

    @Override
    public org.jscience.core.technical.backend.ExecutionContext createContext() {
        return new org.jscience.core.technical.backend.ExecutionContext() {
            @Override
            public <T> T execute(org.jscience.core.technical.backend.Operation<T> operation) {
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
