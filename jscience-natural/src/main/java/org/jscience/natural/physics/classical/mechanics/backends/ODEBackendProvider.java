/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.natural.physics.classical.mechanics.backends;

import org.jscience.core.technical.backend.HardwareAccelerator;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.Operation;
import org.jscience.natural.physics.classical.mechanics.MechanicsBackend;
import org.jscience.natural.physics.classical.mechanics.NativePhysicsWorld;
import org.jscience.natural.physics.classical.mechanics.NativeRigidBody;
import org.jscience.natural.physics.classical.mechanics.RigidBody;

/**
 * ODE (Open Dynamics Engine) physics backend provider.
 * Uses ode4j (Java port of ODE) for simulation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ODEBackendProvider implements MechanicsBackend {

    @Override
    public String getType() {
        return "mechanics";
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
    public int getPriority() {
        return 20;
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    @Override
    public NativePhysicsWorld createWorld() {
        // Placeholder for ODE world creation
        return null;
    }

    @Override
    public NativeRigidBody createRigidBody(RigidBody body) {
        // Placeholder for ODE body creation
        return null;
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
