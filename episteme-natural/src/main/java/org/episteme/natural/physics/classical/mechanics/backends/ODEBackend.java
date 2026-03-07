/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.episteme.natural.physics.classical.mechanics.backends;

import org.episteme.core.technical.backend.HardwareAccelerator;
import com.google.auto.service.AutoService;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.Operation;
import org.episteme.natural.physics.classical.mechanics.MechanicsBackend;
import org.episteme.natural.physics.classical.mechanics.PhysicsWorldBridge;
import org.episteme.natural.physics.classical.mechanics.RigidBodyBridge;
import org.episteme.natural.physics.classical.mechanics.CollisionProvider;
import java.lang.foreign.MemorySegment;
import org.episteme.natural.physics.classical.mechanics.RigidBody;

/**
 * ODE (Open Dynamics Engine) physics backend provider.
 * Uses ode4j (Java port of ODE) for simulation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({MechanicsBackend.class, CollisionProvider.class})
public class ODEBackend implements MechanicsBackend, CPUBackend, CollisionProvider {

    @Override
    public String getType() {
        return "mechanics";
    }

    @Override
    public String getAlgorithmType() {
        return "PhysicsEngine";
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
        return "ode4j physics engine backend (Java Port of ODE).";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.ode4j.ode.OdeHelper");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public void shutdown() {
        // No explicit resources to release for ODE (Java port) backend.
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    @Override
    public PhysicsWorldBridge createWorld() {
        return new org.episteme.natural.physics.classical.mechanics.backends.ode.ODEWorld();
    }

    @Override
    public RigidBodyBridge createRigidBody(RigidBody body) {
        return null; // Created by world.addRigidBody
    }

    @Override
    public int detectSphereCollisions(MemorySegment positions, MemorySegment radii, int n, MemorySegment collisions) {
        // Placeholder for ODE-specific optimized collision detection
        return 0;
    }

    @Override
    public void resolveCollisions(MemorySegment positions, MemorySegment velocities, MemorySegment masses, int n, MemorySegment collisions, int numCollisions) {
        // Placeholder for ODE-specific collision resolution
    }

    @Override
    public ExecutionContext createContext() {
        return new ExecutionContext() {
            @Override
            public <T> T execute(Operation<T> operation) {
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
