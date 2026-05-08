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
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import org.episteme.nativ.physics.classical.mechanics.collision.NativeCollisionProvider;
import org.episteme.core.measure.units.SI;
import org.episteme.core.mathematics.numbers.real.Real;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.episteme.natural.physics.classical.mechanics.simulation.SimulationProvider;

/**
 * Implementation of {@link org.episteme.natural.physics.classical.mechanics.CollisionProvider} using Bullet Physics via Project Panama.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@AutoService({MechanicsBackend.class, ComputeBackend.class, Backend.class, SimulationProvider.class})
public class NativeBulletBackend implements NativeCollisionProvider, MechanicsBackend, CPUBackend, NativeBackend, SimulationProvider {

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
    // RigidBody lifecycle handles
    private static final MethodHandle BT_SPHERE_SHAPE_NEW;               // (float radius) -> ptr
    private static final MethodHandle BT_DEFAULT_MOTION_STATE_NEW;        // (float[16] transform) -> ptr
    private static final MethodHandle BT_RIGID_BODY_NEW;                  // (float mass, motionState, shape, float[3] inertia) -> ptr
    private static final MethodHandle BT_DYNAMICS_WORLD_ADD_RIGID_BODY;   // (world, body) -> void
    private static final MethodHandle BT_DYNAMICS_WORLD_REMOVE_RIGID_BODY;// (world, body) -> void
    private static final MethodHandle BT_COLLISION_SHAPE_CALCULATE_INERTIA;// (shape, float mass, float[3] out) -> void
    
    private static final boolean IS_AVAILABLE_FLAG;

    static {
        boolean globalDisabled = Boolean.getBoolean("episteme.backend.native.disabled");
        boolean backendDisabled = Boolean.getBoolean("episteme.backend.bullet.disabled");

        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup = null;
        java.lang.foreign.Arena arena = java.lang.foreign.Arena.global();

        if (!globalDisabled && !backendDisabled) {
            Optional<SymbolLookup> lib = NativeFFMLoader.loadLibrary("bullet_capi", arena);
            if (lib.isEmpty()) {
                lib = NativeFFMLoader.loadLibrary("bulletc", arena);
            }
            lookup = lib.orElse(null);
        }

        if (lookup != null) {
            DETECT_SPHERES = NativeFFMLoader.findSymbol(lookup, "bt_detect_sphere_collisions")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
            RESOLVE_COLLISIONS = NativeFFMLoader.findSymbol(lookup, "bt_resolve_sphere_collisions")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
            
            // Look up dynamics handles
            BT_DEFAULT_COLLISION_CONFIGURATION_NEW = NativeFFMLoader.findSymbol(lookup, "btDefaultCollisionConfiguration_new")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.ADDRESS))).orElse(null);
            BT_COLLISION_DISPATCHER_NEW = NativeFFMLoader.findSymbol(lookup, "btCollisionDispatcher_new")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS))).orElse(null);
            BT_DBVT_BROADPHASE_NEW = NativeFFMLoader.findSymbol(lookup, "btDbvtBroadphase_new")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS))).orElse(null);
            BT_SEQUENTIAL_IMPULSE_CONSTRAINT_SOLVER_NEW = NativeFFMLoader.findSymbol(lookup, "btSequentialImpulseConstraintSolver_new")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.ADDRESS))).orElse(null);
            BT_DISCRETE_DYNAMICS_WORLD_NEW = NativeFFMLoader.findSymbol(lookup, "btDiscreteDynamicsWorld_new")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS))).orElse(null);
            BT_DYNAMICS_WORLD_STEP_SIMULATION = NativeFFMLoader.findSymbol(lookup, "btDynamicsWorld_stepSimulation")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_INT, ValueLayout.JAVA_FLOAT))).orElse(null);
            BT_DYNAMICS_WORLD_SET_GRAVITY = NativeFFMLoader.findSymbol(lookup, "btDynamicsWorld_setGravity")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS))).orElse(null);

            BT_SPHERE_SHAPE_NEW = NativeFFMLoader.findSymbol(lookup, "btSphereShape_new")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_FLOAT))).orElse(null);
            BT_DEFAULT_MOTION_STATE_NEW = NativeFFMLoader.findSymbol(lookup, "btDefaultMotionState_new")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS))).orElse(null);
            BT_RIGID_BODY_NEW = NativeFFMLoader.findSymbol(lookup, "btRigidBody_new")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_FLOAT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS))).orElse(null);
            BT_DYNAMICS_WORLD_ADD_RIGID_BODY = NativeFFMLoader.findSymbol(lookup, "btDynamicsWorld_addRigidBody")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS))).orElse(null);
            BT_DYNAMICS_WORLD_REMOVE_RIGID_BODY = NativeFFMLoader.findSymbol(lookup, "btDynamicsWorld_removeRigidBody")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS))).orElse(null);
            BT_COLLISION_SHAPE_CALCULATE_INERTIA = NativeFFMLoader.findSymbol(lookup, "btCollisionShape_calculateLocalInertia")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_FLOAT, ValueLayout.ADDRESS))).orElse(null);

            IS_AVAILABLE_FLAG = BT_DISCRETE_DYNAMICS_WORLD_NEW != null;
        } else {
            DETECT_SPHERES = RESOLVE_COLLISIONS = null;
            BT_DEFAULT_COLLISION_CONFIGURATION_NEW = BT_COLLISION_DISPATCHER_NEW = BT_DBVT_BROADPHASE_NEW = BT_SEQUENTIAL_IMPULSE_CONSTRAINT_SOLVER_NEW = BT_DISCRETE_DYNAMICS_WORLD_NEW = BT_DYNAMICS_WORLD_STEP_SIMULATION = BT_DYNAMICS_WORLD_SET_GRAVITY = null;
            BT_SPHERE_SHAPE_NEW = BT_DEFAULT_MOTION_STATE_NEW = BT_RIGID_BODY_NEW = BT_DYNAMICS_WORLD_ADD_RIGID_BODY = BT_DYNAMICS_WORLD_REMOVE_RIGID_BODY = BT_COLLISION_SHAPE_CALCULATE_INERTIA = null;
            IS_AVAILABLE_FLAG = false;
        }
    }

    @Override
    public String getStatusMessage() {
        if (IS_AVAILABLE_FLAG) return "Ready (Bullet Native)";
        String cause = NativeFFMLoader.getFailureCause("bullet_capi");
        if (cause.contains("Not found")) cause = NativeFFMLoader.getFailureCause("bulletc");
        return "Native library load failed: " + cause;
    }

    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE_FLAG && !isExplicitlyDisabled();
    }

    @Override
    public void shutdown() {
        // Native Bullet library lifecycle is managed by this wrapper.
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
    public String getType() {
        return "mechanics";
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
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.cpu.CPUExecutionContext();
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
        throw new UnsupportedOperationException("RigidBody bridge creation must be handled via PhysicsWorldBridge.addRigidBody for Bullet.");
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
                resolveCollisions(posSeg, velSeg, massSeg, n, colSeg, numCollisions, ValueLayout.JAVA_DOUBLE);
                MemorySegment.copy(posSeg, ValueLayout.JAVA_DOUBLE, 0, positions, 0, n * 3);
                MemorySegment.copy(velSeg, ValueLayout.JAVA_DOUBLE, 0, velocities, 0, n * 3);
            }
        }
    }

    @Override
    public int detectSphereCollisions(Real[] positions, Real[] radii, int n, int[] collisions) {
        double[] posD = new double[positions.length];
        double[] radD = new double[radii.length];
        for (int i = 0; i < positions.length; i++) posD[i] = positions[i].doubleValue();
        for (int i = 0; i < radii.length; i++) radD[i] = radii[i].doubleValue();
        return detectSphereCollisions(posD, radD, n, collisions);
    }

    @Override
    public void resolveCollisions(Real[] positions, Real[] velocities, Real[] masses, int n, int[] collisions, int numCollisions) {
        double[] posD = new double[positions.length];
        double[] velD = new double[velocities.length];
        double[] massD = new double[masses.length];
        for (int i = 0; i < positions.length; i++) posD[i] = positions[i].doubleValue();
        for (int i = 0; i < velocities.length; i++) velD[i] = velocities[i].doubleValue();
        for (int i = 0; i < masses.length; i++) massD[i] = masses[i].doubleValue();
        
        resolveCollisions(posD, velD, massD, n, collisions, numCollisions);
        
        for (int i = 0; i < positions.length; i++) positions[i] = Real.of(posD[i]);
        for (int i = 0; i < velocities.length; i++) velocities[i] = Real.of(velD[i]);
    }

    @Override
    public int detectSphereCollisions(MemorySegment positions, MemorySegment radii, int n, MemorySegment collisions, ValueLayout layout) {
        if (!IS_AVAILABLE_FLAG || DETECT_SPHERES == null) throw new UnsupportedOperationException("Bullet native batch collision functions not found in DLL");
        try {
            return (int) DETECT_SPHERES.invokeExact(positions, radii, n, collisions);
        } catch (Throwable t) {
            throw new RuntimeException("Bullet collision detection failed", t);
        }
    }

    @Override
    public void resolveCollisions(MemorySegment positions, MemorySegment velocities, MemorySegment masses, int n, MemorySegment collisions, int numCollisions, ValueLayout layout) {
        if (!IS_AVAILABLE_FLAG || RESOLVE_COLLISIONS == null) throw new UnsupportedOperationException("Bullet native batch collision functions not found in DLL");
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
        /** Maps RigidBody identity -> native btRigidBody pointer (global arena). */
        private final java.util.IdentityHashMap<RigidBody, MemorySegment> bodyPtrs = new java.util.IdentityHashMap<>();
        private final java.lang.foreign.Arena bodyArena = java.lang.foreign.Arena.ofAuto();

        NativeBulletWorld(MemorySegment worldPtr) {
            this.worldPtr = worldPtr;
        }

        @Override
        public void addRigidBody(RigidBody body) {
            if (BT_RIGID_BODY_NEW == null || BT_SPHERE_SHAPE_NEW == null) return;
            try {
                // Create a unit sphere shape  
                MemorySegment shape = (MemorySegment) BT_SPHERE_SHAPE_NEW.invokeExact(1.0f);

                // Compute local inertia
                float mass = 1.0f;
                MemorySegment inertia = bodyArena.allocate(ValueLayout.JAVA_FLOAT, 3);
                if (BT_COLLISION_SHAPE_CALCULATE_INERTIA != null)
                    BT_COLLISION_SHAPE_CALCULATE_INERTIA.invokeExact(shape, mass, inertia);

                // Identity transform (column-major 4x4)
                MemorySegment transform = bodyArena.allocate(ValueLayout.JAVA_FLOAT, 16);
                for (int i = 0; i < 16; i++) transform.setAtIndex(ValueLayout.JAVA_FLOAT, i, (i % 5 == 0) ? 1.0f : 0.0f);
                org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> pos = body.getPosition();
                if (pos != null && pos.dimension() >= 3) {
                    transform.setAtIndex(ValueLayout.JAVA_FLOAT, 12, (float) pos.get(0).doubleValue());
                    transform.setAtIndex(ValueLayout.JAVA_FLOAT, 13, (float) pos.get(1).doubleValue());
                    transform.setAtIndex(ValueLayout.JAVA_FLOAT, 14, (float) pos.get(2).doubleValue());
                }

                MemorySegment motionState = (MemorySegment) BT_DEFAULT_MOTION_STATE_NEW.invokeExact(transform);
                MemorySegment rbPtr = (MemorySegment) BT_RIGID_BODY_NEW.invokeExact(mass, motionState, shape, inertia);
                bodyPtrs.put(body, rbPtr);
                if (BT_DYNAMICS_WORLD_ADD_RIGID_BODY != null)
                    BT_DYNAMICS_WORLD_ADD_RIGID_BODY.invokeExact(worldPtr, rbPtr);
            } catch (Throwable t) {
                java.util.logging.Logger.getLogger(NativeBulletBackend.class.getName())
                    .warning("[NativeBulletWorld] addRigidBody failed: " + t.getMessage());
            }
        }

        @Override
        public void removeRigidBody(RigidBody body) {
            MemorySegment rbPtr = bodyPtrs.remove(body);
            if (rbPtr == null || BT_DYNAMICS_WORLD_REMOVE_RIGID_BODY == null) return;
            try {
                BT_DYNAMICS_WORLD_REMOVE_RIGID_BODY.invokeExact(worldPtr, rbPtr);
            } catch (Throwable t) {
                java.util.logging.Logger.getLogger(NativeBulletBackend.class.getName())
                    .warning("[NativeBulletWorld] removeRigidBody failed: " + t.getMessage());
            }
        }

        @Override
        public void stepSimulation(org.episteme.core.measure.Quantity<org.episteme.core.measure.quantity.Time> timeStep) {
            stepSimulation(timeStep, 10, org.episteme.core.measure.Quantities.create(1.0/60.0, org.episteme.core.measure.units.SI.SECOND));
        }

        @Override
        public void stepSimulation(org.episteme.core.measure.Quantity<org.episteme.core.measure.quantity.Time> timeStep, int maxSubSteps, org.episteme.core.measure.Quantity<org.episteme.core.measure.quantity.Time> fixedTimeStep) {
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





