/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.physics.classical.mechanics.backends.ode;

import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.quantity.Time;
import org.jscience.core.measure.units.SI;
import org.jscience.natural.physics.classical.mechanics.PhysicsWorldBridge;
import org.jscience.natural.physics.classical.mechanics.RigidBody;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.OdeHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * ODE implementation of PhysicsWorldBackend.
 */
public class ODEWorld implements PhysicsWorldBridge {

    private final DWorld world;
    private final Map<RigidBody, ODERigidBody> bodies = new HashMap<>();

    public ODEWorld() {
        OdeHelper.initODE();
        world = OdeHelper.createWorld();
        world.setGravity(0, -9.81, 0);
    }

    @Override
    public void addRigidBody(RigidBody body) {
        ODERigidBody backendBody = new ODERigidBody(world, body);
        bodies.put(body, backendBody);
    }

    @Override
    public void removeRigidBody(RigidBody body) {
        ODERigidBody backendBody = bodies.remove(body);
        if (backendBody != null) {
            backendBody.destroy();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void stepSimulation(Quantity<Time> timeStep) {
        double dt = timeStep.to(org.jscience.core.measure.units.SI.SECOND).getValue().doubleValue();
        
        for (ODERigidBody b : bodies.values()) {
            b.pushState();
        }

        world.step(dt);

        for (ODERigidBody b : bodies.values()) {
            b.pullState();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void stepSimulation(Quantity<Time> timeStep, int maxSubSteps, Quantity<Time> fixedTimeStep) {
        double dt = (double) timeStep.getValue(SI.SECOND).doubleValue();
        // ODE usually doesn't have an internal sub-stepping auto-manager like Bullet, 
        // we'd have to loop manually. For simplicity:
        world.step(dt);

        for (ODERigidBody b : bodies.values()) {
            b.pullState();
        }
    }

    @Override
    public void setGravity(double gravityX, double gravityY, double gravityZ) {
        world.setGravity(gravityX, gravityY, gravityZ);
    }

    public DWorld getOdeWorld() {
        return world;
    }
}
