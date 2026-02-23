/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.physics.classical.mechanics.backends.genesis;

import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.quantity.Time;
import org.jscience.core.measure.units.SI;
import org.jscience.natural.physics.classical.mechanics.PhysicsWorldBridge;
import org.jscience.natural.physics.classical.mechanics.RigidBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Genesis implementation of PhysicsWorldBackend.
 * Specialized for many-body systems and optimized for massive parallelization potential.
 */
public class GenesisWorld implements PhysicsWorldBridge {

    private final List<GenesisRigidBody> bodies = new ArrayList<>();
    private double gx, gy, gz;

    public GenesisWorld() {
        this.gx = 0;
        this.gy = -9.81;
        this.gz = 0;
    }

    @Override
    public void addRigidBody(RigidBody body) {
        bodies.add(new GenesisRigidBody(body));
    }

    @Override
    public void removeRigidBody(RigidBody body) {
        bodies.removeIf(b -> b.getJScienceBody() == body);
    }

    @Override
    public void stepSimulation(Quantity<Time> timeStep) {
        double dt = timeStep.to(org.jscience.core.measure.units.SI.SECOND).getValue().doubleValue();
        
        // High-performance Euler-Cromer integration for many-body stability
        for (GenesisRigidBody b : bodies) {
            b.applyGravity(gx, gy, gz);
            b.integrate(dt);
        }
    }

    @Override
    public void stepSimulation(Quantity<Time> timeStep, int maxSubSteps, Quantity<Time> fixedTimeStep) {
        float dt = (float) timeStep.getValue(SI.SECOND).doubleValue();
        float fdt = (float) fixedTimeStep.getValue(SI.SECOND).doubleValue();

        int steps = Math.min(maxSubSteps, (int) Math.ceil(dt / fdt));
        for (int i = 0; i < steps; i++) {
            for (GenesisRigidBody b : bodies) {
                b.applyGravity(gx, gy, gz);
                b.integrate(fdt);
            }
        }
    }

    @Override
    public void setGravity(double gravityX, double gravityY, double gravityZ) {
        this.gx = gravityX;
        this.gy = gravityY;
        this.gz = gravityZ;
    }
}
