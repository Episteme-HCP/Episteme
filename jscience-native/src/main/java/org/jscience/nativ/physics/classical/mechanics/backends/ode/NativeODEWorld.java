/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.physics.classical.mechanics.backends.ode;

import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.quantity.Time;
import org.jscience.natural.physics.classical.mechanics.PhysicsWorldBridge;
import org.jscience.natural.physics.classical.mechanics.RigidBody;
import org.jscience.nativ.technical.backend.nativ.NativeLibraryLoader;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Native-backed implementation of {@link PhysicsWorldBridge} for the ODE (Open Dynamics Engine).
 * <p>
 * Uses Project Panama (FFM API) to call into the native {@code ode} library.
 * Falls back to no-ops gracefully if the library is not found on this system.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class NativeODEWorld implements PhysicsWorldBridge {

    private static final Logger LOG = Logger.getLogger(NativeODEWorld.class.getName());

    /** Opaque pointer to the native dWorldID. {@code null} when unavailable. */
    private final MemorySegment worldId;

    private static MethodHandle ODE_INIT;
    private static MethodHandle ODE_CREATE_WORLD;
    private static MethodHandle ODE_STEP;
    private static MethodHandle ODE_SET_GRAVITY;

    private static final boolean NATIVE_AVAILABLE;

    static {
        boolean avail = false;
        Optional<SymbolLookup> libOpt = NativeLibraryLoader.loadLibrary("ode", Arena.global());
        if (libOpt.isPresent()) {
            SymbolLookup lib = libOpt.get();
            Linker linker = Linker.nativeLinker();
            try {
                // void dInitODE2(unsigned int uiInitFlags);
                ODE_INIT = linker.downcallHandle(
                    lib.find("dInitODE2").orElseThrow(),
                    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT)
                );
                // dWorldID dWorldCreate();
                ODE_CREATE_WORLD = linker.downcallHandle(
                    lib.find("dWorldCreate").orElseThrow(),
                    FunctionDescriptor.of(ValueLayout.ADDRESS)
                );
                // void dWorldStep(dWorldID w, dReal stepsize);
                ODE_STEP = linker.downcallHandle(
                    lib.find("dWorldStep").orElseThrow(),
                    FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE)
                );
                // void dWorldSetGravity(dWorldID w, dReal x, dReal y, dReal z);
                ODE_SET_GRAVITY = linker.downcallHandle(
                    lib.find("dWorldSetGravity").orElseThrow(),
                    FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
                        ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE)
                );
                // Initialise ODE runtime
                ODE_INIT.invoke(0);
                avail = true;
            } catch (Throwable t) {
                LOG.fine("[NativeODEWorld] Failed to bind native ODE symbols: " + t.getMessage());
            }
        }
        NATIVE_AVAILABLE = avail;
    }

    public NativeODEWorld() {
        MemorySegment world = null;
        if (NATIVE_AVAILABLE && ODE_CREATE_WORLD != null) {
            try {
                world = (MemorySegment) ODE_CREATE_WORLD.invoke();
            } catch (Throwable t) {
                LOG.warning("[NativeODEWorld] dWorldCreate() failed: " + t.getMessage());
            }
        }
        if (world == null) {
            LOG.info("[NativeODEWorld] Native ODE library not available; running in no-op mode.");
        }
        this.worldId = world;
    }

    @Override
    public void addRigidBody(RigidBody body) {
        if (body == null) return;
        // Body creation via dBodyCreate(worldId) + position requires per-body tracking.
        // Stubbed until full dBody lifecycle management is implemented.
    }

    @Override
    public void removeRigidBody(RigidBody body) {
        // Corresponding dBodyDestroy(bodyId) would go here when body lifecycle is tracked.
    }

    @Override
    public void stepSimulation(Quantity<Time> timeStep) {
        stepSimulation(timeStep, 1, timeStep);
    }

    @Override
    public void stepSimulation(Quantity<Time> timeStep, int maxSubSteps, Quantity<Time> fixedTimeStep) {
        if (worldId == null || ODE_STEP == null) return;
        double dt = timeStep.getValue().doubleValue();
        double subDt = fixedTimeStep.getValue().doubleValue();
        int steps = Math.min(maxSubSteps, Math.max(1, (int) Math.round(dt / subDt)));
        for (int i = 0; i < steps; i++) {
            try {
                ODE_STEP.invoke(worldId, subDt);
            } catch (Throwable t) {
                LOG.warning("[NativeODEWorld] dWorldStep failed: " + t.getMessage());
                break;
            }
        }
    }

    @Override
    public void setGravity(double gravityX, double gravityY, double gravityZ) {
        if (worldId == null || ODE_SET_GRAVITY == null) return;
        try {
            ODE_SET_GRAVITY.invoke(worldId, gravityX, gravityY, gravityZ);
        } catch (Throwable t) {
            LOG.warning("[NativeODEWorld] dWorldSetGravity failed: " + t.getMessage());
        }
    }
}
