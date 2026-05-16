/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.physics.classical.mechanics.collision.backends.jbullet;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.complex.Quaternion;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.physics.classical.mechanics.collision.RigidBody;
import org.episteme.natural.physics.classical.mechanics.collision.RigidBodyBridge;

/**
 * JBullet implementation of RigidBodyBackend.
 */
public class JBulletRigidBody implements RigidBodyBridge {

    private final RigidBody owner;
    private final com.bulletphysics.dynamics.RigidBody bulletBody;

    public JBulletRigidBody(RigidBody owner) {
        this.owner = owner;

        // Create shape
        CollisionShape shape;
        double radius = owner.getBoundingRadius();
        if (radius > 0) {
            shape = new SphereShape((float) radius);
        } else {
            shape = new BoxShape(new Vector3f(0.5f, 0.5f, 0.5f));
        }

        // Mass and inertia
        float mass = (float) owner.getMass().doubleValue();
        Vector3f inertia = new Vector3f(0, 0, 0);
        if (mass > 0) {
            shape.calculateLocalInertia(mass, inertia);
        }

        // Transform
        Transform transform = new Transform();
        transform.setIdentity();
        Vector<Real> pos = owner.getPosition();
        if (pos != null) {
            transform.origin.set((float) pos.get(0).doubleValue(), (float) pos.get(1).doubleValue(), (float) pos.get(2).doubleValue());
        }

        DefaultMotionState motionState = new DefaultMotionState(transform);
        RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, motionState, shape, inertia);
        bulletBody = new com.bulletphysics.dynamics.RigidBody(rbInfo);
    }

    public void pushState() {
        Transform trans = new Transform();
        trans.setIdentity();
        Vector<Real> pos = owner.getPosition();
        if (pos != null) {
            trans.origin.set((float) pos.get(0).doubleValue(), (float) pos.get(1).doubleValue(), (float) pos.get(2).doubleValue());
        }
        
        Quaternion rot = owner.getRotation();
        if (rot != null) {
            Quat4f bRot = new Quat4f(
                (float) rot.getI().doubleValue(),
                (float) rot.getJ().doubleValue(),
                (float) rot.getK().doubleValue(),
                (float) rot.getReal().doubleValue()
            );
            trans.setRotation(bRot);
        }
        
        bulletBody.setWorldTransform(trans);
        
        Vector<Real> vel = owner.getVelocity();
        if (vel != null) {
            bulletBody.setLinearVelocity(new Vector3f((float) vel.get(0).doubleValue(), (float) vel.get(1).doubleValue(), (float) vel.get(2).doubleValue()));
        }
    }

    public void pullState() {
        Transform trans = new Transform();
        bulletBody.getWorldTransform(trans);
        
        owner.setPosition(Vector.of(Real.of(trans.origin.x), Real.of(trans.origin.y), Real.of(trans.origin.z)));
        
        Vector3f vel = new Vector3f();
        bulletBody.getLinearVelocity(vel);
        owner.setVelocity(Vector.of(Real.of(vel.x), Real.of(vel.y), Real.of(vel.z)));
    }

    public void applyCentralForce(double x, double y, double z) {
        bulletBody.applyCentralForce(new Vector3f((float) x, (float) y, (float) z));
    }

    public void applyCentralImpulse(double x, double y, double z) {
        bulletBody.applyCentralImpulse(new Vector3f((float) x, (float) y, (float) z));
    }

    public com.bulletphysics.dynamics.RigidBody getBulletBody() {
        return bulletBody;
    }

    public void destroy() {
        // No-op for JBullet
    }

    @Override
    public RigidBody getOwner() {
        return owner;
    }
}


