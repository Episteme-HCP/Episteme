/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.physics.classical.mechanics.backends.genesis;

import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.quantity.Time;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.physics.classical.mechanics.PhysicsWorldBridge;
import org.episteme.natural.physics.classical.mechanics.RigidBody;
import org.episteme.core.technical.backend.nativ.NativeLibraryLoader;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Native-backed implementation of {@link PhysicsWorldBridge} for the Genesis physics engine.
 * <p>
 * Uses Project Panama (FFM API) to call into the {@code GenesisC} native library.
 * Falls back to no-ops gracefully if the library is not available on the current system.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class NativeGenesisWorld implements PhysicsWorldBridge {

    private static final Logger logger = LoggerFactory.getLogger(NativeGenesisWorld.class);

    /** Opaque pointer to the native Genesis world object. {@code null} when unavailable. */
    private final MemorySegment nativeWorld;

    private static MethodHandle GENESIS_CREATE_WORLD;
    private static MethodHandle GENESIS_ADD_BODY;
    private static MethodHandle GENESIS_STEP;
    private static MethodHandle GENESIS_SET_GRAVITY;

    private static final boolean NATIVE_AVAILABLE;

    static {
        boolean avail = false;
        Optional<SymbolLookup> libOpt = NativeLibraryLoader.loadLibrary("GenesisC", Arena.global());
        if (libOpt.isPresent()) {
            SymbolLookup lib = libOpt.get();
            Linker linker = Linker.nativeLinker();
            try {
                // genesis_world_t* genesis_create_world();
                GENESIS_CREATE_WORLD = linker.downcallHandle(
                    lib.find("genesis_create_world").orElseThrow(),
                    FunctionDescriptor.of(ValueLayout.ADDRESS)
                );
                // void genesis_add_body(genesis_world_t* world, double mass, double x, double y, double z);
                GENESIS_ADD_BODY = linker.downcallHandle(
                    lib.find("genesis_add_body").orElseThrow(),
                    FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
                        ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
                        ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE)
                );
                // void genesis_step(genesis_world_t* world, double dt);
                GENESIS_STEP = linker.downcallHandle(
                    lib.find("genesis_step").orElseThrow(),
                    FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE)
                );
                // void genesis_set_gravity(genesis_world_t* world, double gx, double gy, double gz);
                GENESIS_SET_GRAVITY = linker.downcallHandle(
                    lib.find("genesis_set_gravity").orElseThrow(),
                    FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
                        ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE)
                );
                avail = true;
            } catch (Throwable t) {
                logger.debug("[NativeGenesisWorld] Failed to bind native symbols: {}", t.getMessage());
            }
        }
        NATIVE_AVAILABLE = avail;
    }

    public NativeGenesisWorld() {
        MemorySegment world = null;
        if (NATIVE_AVAILABLE && GENESIS_CREATE_WORLD != null) {
            try {
                world = (MemorySegment) GENESIS_CREATE_WORLD.invoke();
            } catch (Throwable t) {
                logger.warn("[NativeGenesisWorld] genesis_create_world() failed: {}", t.getMessage());
            }
        }
        if (world == null) {
            logger.info("[NativeGenesisWorld] GenesisC library not available; running in no-op mode.");
        }
        this.nativeWorld = world;
    }

    @Override
    public void addRigidBody(RigidBody body) {
        if (nativeWorld == null || GENESIS_ADD_BODY == null) return;
        try {
            Vector<Real> pos = body.getPosition();
            double x = (pos != null && pos.dimension() > 0) ? pos.get(0).doubleValue() : 0.0;
            double y = (pos != null && pos.dimension() > 1) ? pos.get(1).doubleValue() : 0.0;
            double z = (pos != null && pos.dimension() > 2) ? pos.get(2).doubleValue() : 0.0;
            // Mass is not directly accessible from RigidBody at this level; default to 1.0
            GENESIS_ADD_BODY.invoke(nativeWorld, 1.0, x, y, z);
        } catch (Throwable t) {
            logger.warn("[NativeGenesisWorld] addRigidBody failed: {}", t.getMessage());
        }
    }

    @Override
    public void removeRigidBody(RigidBody body) {
        // Genesis does not directly expose a per-body removal; silently no-op.
    }

    @Override
    public void stepSimulation(Quantity<Time> timeStep) {
        stepSimulation(timeStep, 1, timeStep);
    }

    @Override
    public void stepSimulation(Quantity<Time> timeStep, int maxSubSteps, Quantity<Time> fixedTimeStep) {
        if (nativeWorld == null || GENESIS_STEP == null) return;
        double dt = timeStep.getValue().doubleValue();
        double subDt = fixedTimeStep.getValue().doubleValue();
        int steps = Math.min(maxSubSteps, Math.max(1, (int) Math.round(dt / subDt)));
        for (int i = 0; i < steps; i++) {
            try {
                GENESIS_STEP.invoke(nativeWorld, subDt);
            } catch (Throwable t) {
                logger.warn("[NativeGenesisWorld] stepSimulation failed: {}", t.getMessage());
                break;
            }
        }
    }

    @Override
    public void setGravity(double gravityX, double gravityY, double gravityZ) {
        if (nativeWorld == null || GENESIS_SET_GRAVITY == null) return;
        try {
            GENESIS_SET_GRAVITY.invoke(nativeWorld, gravityX, gravityY, gravityZ);
        } catch (Throwable t) {
            logger.warn("[NativeGenesisWorld] setGravity failed: {}", t.getMessage());
        }
    }
}
