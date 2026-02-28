/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.physics.classical.mechanics.backends.jbullet;

import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.quantity.Time;
import org.episteme.core.measure.units.SI;
import org.episteme.natural.physics.classical.mechanics.PhysicsWorldBridge;
import org.episteme.natural.physics.classical.mechanics.RigidBody;

import javax.vecmath.Vector3f;
import java.util.HashMap;
import java.util.Map;

/**
 * JBullet implementation of PhysicsWorldBackend.
 */
public class JBulletWorld implements PhysicsWorldBridge {

    private final DiscreteDynamicsWorld dynamicsWorld;
    private final Map<RigidBody, JBulletRigidBody> bodies = new HashMap<>();

    public JBulletWorld() {
        DefaultCollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
        DbvtBroadphase broadphase = new DbvtBroadphase();
        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
        dynamicsWorld.setGravity(new Vector3f(0, -9.81f, 0));
    }

    @Override
    public void addRigidBody(RigidBody body) {
        JBulletRigidBody backendBody = new JBulletRigidBody(body);
        dynamicsWorld.addRigidBody(backendBody.getBulletBody());
        bodies.put(body, backendBody);
    }

    @Override
    public void removeRigidBody(RigidBody body) {
        JBulletRigidBody backendBody = bodies.remove(body);
        if (backendBody != null) {
            dynamicsWorld.removeRigidBody(backendBody.getBulletBody());
            backendBody.destroy();
        }
    }

    @Override
    public void stepSimulation(Quantity<Time> timeStep) {
        float dt = (float) timeStep.getValue(SI.SECOND).doubleValue();
        
        // Push states to Bullet
        for (JBulletRigidBody b : bodies.values()) {
            b.pushState();
        }

        dynamicsWorld.stepSimulation(dt, 10);

        // Pull states from Bullet
        for (JBulletRigidBody b : bodies.values()) {
            b.pullState();
        }
    }

    @Override
    public void stepSimulation(Quantity<Time> timeStep, int maxSubSteps, Quantity<Time> fixedTimeStep) {
        float dt = (float) timeStep.getValue(SI.SECOND).doubleValue();
        float fdt = (float) fixedTimeStep.getValue(SI.SECOND).doubleValue();

        // Push states
        for (JBulletRigidBody b : bodies.values()) {
            b.pushState();
        }

        dynamicsWorld.stepSimulation(dt, maxSubSteps, fdt);

        // Pull states
        for (JBulletRigidBody b : bodies.values()) {
            b.pullState();
        }
    }

    @Override
    public void setGravity(double gravityX, double gravityY, double gravityZ) {
        dynamicsWorld.setGravity(new Vector3f((float) gravityX, (float) gravityY, (float) gravityZ));
    }

    public DiscreteDynamicsWorld getDynamicsWorld() {
        return dynamicsWorld;
    }
}
