/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.physics.classical.mechanics.backends;

import com.google.auto.service.AutoService;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.HardwareAccelerator;
import org.jscience.core.technical.backend.cpu.CPUBackend;
import org.jscience.natural.physics.classical.mechanics.CollisionProvider;
import org.jscience.natural.physics.classical.mechanics.MechanicsBackend;
import org.jscience.natural.physics.classical.mechanics.PhysicsWorldBridge;
import org.jscience.natural.physics.classical.mechanics.RigidBody;
import org.jscience.natural.physics.classical.mechanics.RigidBodyBridge;
import org.jscience.nativ.technical.backend.nativ.NativeBackend;
import org.jscience.core.measure.units.SI;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.jscience.natural.physics.classical.mechanics.simulation.SimulationProvider;

/**
 * Implementation of {@link org.jscience.natural.physics.classical.mechanics.CollisionProvider} using Bullet Physics via Project Panama.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@AutoService({CollisionProvider.class, MechanicsBackend.class, ComputeBackend.class, Backend.class, SimulationProvider.class})
public class NativeBulletBackend implements CollisionProvider, MechanicsBackend, CPUBackend, NativeBackend, SimulationProvider {

    private static final MethodHandle DETECT_SPHERES;
    private static final MethodHandle RESOLVE_COLLISIONS;
    
    // Mechanics handles
    private static final MethodHandle BT_DEFAULT_COLLISION_CONFIGURATION_NEW;
    private static final MethodHandle BT_COLLISION_DISPATCHER_NEW;
    private static final MethodHandle BT_DBVT_BROADPHASE_NEW;
    private static final MethodHandle BT_SEQUENTIAL_IMPULSE_CONSTRAINT_SOLVER_NEW;
    private static final MethodHandle BT_DISCRETE_DYNAMICS_WORLD_NEW;
    private static final MethodHandle BT_DYNAMICS_WORLD_STEP_SIMULATION;
    private static final MethodHandle BT_DYNAMICS_WORLD_SET_GRAVITY;
    
    private static final boolean IS_AVAILABLE_FLAG;

    static {
        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup = null;
        java.lang.foreign.Arena arena = java.lang.foreign.Arena.global();

        // 1. Try standard library path
        try {
            lookup = SymbolLookup.libraryLookup("bullet_capi", arena);
        } catch (Throwable t) {
            // 2. Try explicit path: libs/Bullet3DLL/libbulletc.dll
            java.nio.file.Path libPath = java.nio.file.Path.of("libs", "Bullet3DLL", "libbulletc.dll");
            if (java.nio.file.Files.exists(libPath)) {
                try {
                    lookup = SymbolLookup.libraryLookup(libPath, arena);
                } catch (Throwable t2) {
                     // ignore
                }
            } else {
                 // 3. Try root fallback (just in case)
                 java.nio.file.Path rootPath = java.nio.file.Path.of("libbulletc.dll");
                 if (java.nio.file.Files.exists(rootPath)) {
                     try {
                         lookup = SymbolLookup.libraryLookup(rootPath, arena);
                     } catch (Throwable t3) {}
                 }
            }
        }

        if (lookup != null && lookup.find("bt_detect_sphere_collisions").isPresent()) {
            DETECT_SPHERES = linker.downcallHandle(lookup.find("bt_detect_sphere_collisions").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            RESOLVE_COLLISIONS = linker.downcallHandle(lookup.find("bt_resolve_sphere_collisions").get(),
                FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
            
            // Look up dynamics handles
            BT_DEFAULT_COLLISION_CONFIGURATION_NEW = lookup.find("btDefaultCollisionConfiguration_new")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.ADDRESS))).orElse(null);
            BT_COLLISION_DISPATCHER_NEW = lookup.find("btCollisionDispatcher_new")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS))).orElse(null);
            BT_DBVT_BROADPHASE_NEW = lookup.find("btDbvtBroadphase_new")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS))).orElse(null);
            BT_SEQUENTIAL_IMPULSE_CONSTRAINT_SOLVER_NEW = lookup.find("btSequentialImpulseConstraintSolver_new")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.ADDRESS))).orElse(null);
            BT_DISCRETE_DYNAMICS_WORLD_NEW = lookup.find("btDiscreteDynamicsWorld_new")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS))).orElse(null);
            BT_DYNAMICS_WORLD_STEP_SIMULATION = lookup.find("btDynamicsWorld_stepSimulation")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_INT, ValueLayout.JAVA_FLOAT))).orElse(null);
            BT_DYNAMICS_WORLD_SET_GRAVITY = lookup.find("btDynamicsWorld_setGravity")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS))).orElse(null);

            IS_AVAILABLE_FLAG = BT_DISCRETE_DYNAMICS_WORLD_NEW != null;
        } else {
            DETECT_SPHERES = RESOLVE_COLLISIONS = null;
            BT_DEFAULT_COLLISION_CONFIGURATION_NEW = BT_COLLISION_DISPATCHER_NEW = BT_DBVT_BROADPHASE_NEW = BT_SEQUENTIAL_IMPULSE_CONSTRAINT_SOLVER_NEW = BT_DISCRETE_DYNAMICS_WORLD_NEW = BT_DYNAMICS_WORLD_STEP_SIMULATION = BT_DYNAMICS_WORLD_SET_GRAVITY = null;
            IS_AVAILABLE_FLAG = false;
        }
    }

    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE_FLAG;
    }

    @Override
    public boolean isLoaded() {
        return IS_AVAILABLE_FLAG;
    }

    @Override
    public String getNativeLibraryName() {
        return "bullet_capi";
    }

    @Override
    public String getName() {
        return "Native Bullet Physics (FFM)";
    }

    @Override
    public String getAlgorithmType() {
        return "mechanics";
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU; // Bullet is CPU-based
    }

    @Override
    public org.jscience.core.technical.backend.ExecutionContext createContext() {
        return null;
    }

    @Override
    public PhysicsWorldBridge createWorld() {
        if (!IS_AVAILABLE_FLAG) throw new UnsupportedOperationException("Bullet native library not found");
        try {
            MemorySegment config = (MemorySegment) BT_DEFAULT_COLLISION_CONFIGURATION_NEW.invokeExact();
            MemorySegment dispatcher = (MemorySegment) BT_COLLISION_DISPATCHER_NEW.invokeExact(config);
            MemorySegment broadphase = (MemorySegment) BT_DBVT_BROADPHASE_NEW.invokeExact(MemorySegment.NULL);
            MemorySegment solver = (MemorySegment) BT_SEQUENTIAL_IMPULSE_CONSTRAINT_SOLVER_NEW.invokeExact();
            MemorySegment worldPtr = (MemorySegment) BT_DISCRETE_DYNAMICS_WORLD_NEW.invokeExact(dispatcher, broadphase, solver, config);
            return new NativeBulletWorld(worldPtr);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to create Bullet world", t);
        }
    }

    @Override
    public RigidBodyBridge createRigidBody(RigidBody body) {
        return null; // Created via world.addRigidBody
    }

    @Override
    public int detectSphereCollisions(MemorySegment positions, MemorySegment radii, int n, MemorySegment collisions) {
        if (!IS_AVAILABLE_FLAG) throw new UnsupportedOperationException("Bullet native library not found");
        try {
            return (int) DETECT_SPHERES.invokeExact(positions, radii, n, collisions);
        } catch (Throwable t) {
            throw new RuntimeException("Bullet collision detection failed", t);
        }
    }

    @Override
    public void resolveCollisions(MemorySegment positions, MemorySegment velocities, MemorySegment masses, int n, MemorySegment collisions, int numCollisions) {
        if (!IS_AVAILABLE_FLAG) throw new UnsupportedOperationException("Bullet native library not found");
        try {
            RESOLVE_COLLISIONS.invokeExact(positions, velocities, masses, n, collisions, numCollisions);
        } catch (Throwable t) {
            throw new RuntimeException("Bullet collision resolution failed", t);
        }
    }

    @Override
    public void parallelExecute(List<Runnable> tasks, int parallelism) {
        if (!IS_AVAILABLE_FLAG) return;
        ExecutorService executor = Executors.newFixedThreadPool(parallelism);
        try {
            for (Runnable task : tasks) executor.execute(task);
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (!executor.isTerminated()) {
                executor.shutdownNow();
            }
        }
    }

    private static class NativeBulletWorld implements PhysicsWorldBridge {
        private final MemorySegment worldPtr;

        NativeBulletWorld(MemorySegment worldPtr) {
            this.worldPtr = worldPtr;
        }

        @Override
        public void addRigidBody(RigidBody body) {
            // Implementation for adding rigid body to native world
        }

        @Override
        public void removeRigidBody(RigidBody body) {
            // Implementation for removing rigid body
        }

        @Override
        public void stepSimulation(org.jscience.core.measure.Quantity<org.jscience.core.measure.quantity.Time> timeStep) {
            stepSimulation(timeStep, 10, org.jscience.core.measure.Quantities.create(1.0/60.0, org.jscience.core.measure.units.SI.SECOND));
        }

        @Override
        public void stepSimulation(org.jscience.core.measure.Quantity<org.jscience.core.measure.quantity.Time> timeStep, int maxSubSteps, org.jscience.core.measure.Quantity<org.jscience.core.measure.quantity.Time> fixedTimeStep) {
            try {
                BT_DYNAMICS_WORLD_STEP_SIMULATION.invokeExact(worldPtr, 
                    (float) timeStep.getValue(SI.SECOND).doubleValue(), 
                    maxSubSteps, 
                    (float) fixedTimeStep.getValue(SI.SECOND).doubleValue());
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }

        @Override
        public void setGravity(double gravityX, double gravityY, double gravityZ) {
            // Need a 3-float buffer for gravity
            try (java.lang.foreign.Arena stack = java.lang.foreign.Arena.ofConfined()) {
                MemorySegment grav = stack.allocate(ValueLayout.JAVA_FLOAT, 3);
                grav.set(ValueLayout.JAVA_FLOAT, 0, (float) gravityX);
                grav.set(ValueLayout.JAVA_FLOAT, 4, (float) gravityY);
                grav.set(ValueLayout.JAVA_FLOAT, 8, (float) gravityZ);
                BT_DYNAMICS_WORLD_SET_GRAVITY.invokeExact(worldPtr, grav);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }
}





