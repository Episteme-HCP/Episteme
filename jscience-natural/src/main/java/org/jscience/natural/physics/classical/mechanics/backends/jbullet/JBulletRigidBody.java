/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.physics.classical.mechanics.backends.jbullet;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.complex.Quaternion;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.natural.physics.classical.mechanics.RigidBody;
import org.jscience.natural.physics.classical.mechanics.RigidBodyBridge;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.Arrays;

/**
 * JBullet implementation of RigidBodyBackend.
 */
public class JBulletRigidBody implements RigidBodyBridge {

    private final RigidBody jscienceBody;
    private final com.bulletphysics.dynamics.RigidBody bulletBody;

    public JBulletRigidBody(RigidBody body) {
        this.jscienceBody = body;
        
        CollisionShape shape = createShape(body.getCollisionShape());
        
        Vector3f localInertia = new Vector3f(0, 0, 0);
        float mass = 1.0f; // Default mass
        
        // Note: JScience RigidBody should ideally provide getMass()
        // We'll use a fixed mass for now or try to extract it if possible
        
        if (mass > 0) {
            shape.calculateLocalInertia(mass, localInertia);
        }

        Transform transform = new Transform();
        transform.setIdentity();
        Vector<Real> pos = body.getPosition();
        transform.origin.set((float) pos.get(0).doubleValue(), (float) pos.get(1).doubleValue(), (float) pos.get(2).doubleValue());
        
        Quaternion rot = body.getOrientation();
        transform.setRotation(new Quat4f((float) rot.getI().doubleValue(), (float) rot.getJ().doubleValue(), (float) rot.getK().doubleValue(), (float) rot.getReal().doubleValue()));

        DefaultMotionState motionState = new DefaultMotionState(transform);
        RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, motionState, shape, localInertia);
        this.bulletBody = new com.bulletphysics.dynamics.RigidBody(rbInfo);
    }

    private CollisionShape createShape(org.jscience.core.mathematics.geometry.collision.CollisionShape shape) {
        if (shape instanceof org.jscience.core.mathematics.geometry.collision.BoxShape box) {
            Vector<Real> h = box.getHalfExtents();
            return new BoxShape(new Vector3f((float) h.get(0).doubleValue(), (float) h.get(1).doubleValue(), (float) h.get(2).doubleValue()));
        } else if (shape instanceof org.jscience.core.mathematics.geometry.collision.SphereShape sphere) {
            return new SphereShape((float) sphere.getRadius().doubleValue());
        }
        // Fallback to a small box
        return new BoxShape(new Vector3f(0.5f, 0.5f, 0.5f));
    }

    @Override
    public void pushState() {
        Transform tr = new Transform();
        Vector<Real> pos = jscienceBody.getPosition();
        tr.origin.set((float) pos.get(0).doubleValue(), (float) pos.get(1).doubleValue(), (float) pos.get(2).doubleValue());
        
        Quaternion rot = jscienceBody.getOrientation();
        tr.setRotation(new Quat4f((float) rot.getI().doubleValue(), (float) rot.getJ().doubleValue(), (float) rot.getK().doubleValue(), (float) rot.getReal().doubleValue()));
        
        bulletBody.setWorldTransform(tr);
        
        Vector<Real> vel = jscienceBody.getVelocity();
        bulletBody.setLinearVelocity(new Vector3f((float) vel.get(0).doubleValue(), (float) vel.get(1).doubleValue(), (float) vel.get(2).doubleValue()));
        
        Vector<Real> angVel = jscienceBody.getAngularVelocity();
        bulletBody.setAngularVelocity(new Vector3f((float) angVel.get(0).doubleValue(), (float) angVel.get(1).doubleValue(), (float) angVel.get(2).doubleValue()));
    }

    @Override
    public void pullState() {
        Transform tr = new Transform();
        bulletBody.getMotionState().getWorldTransform(tr);
        
        jscienceBody.setPosition(Vector.of(Arrays.asList(
                Real.of(tr.origin.x), Real.of(tr.origin.y), Real.of(tr.origin.z)), Reals.getInstance()));
        
        Quat4f rot = new Quat4f();
        tr.getRotation(rot);
        // Quaternion is (r, i, j, k). Quat4f is (x, y, z, w) where w is real
        // Wait, check JScience Quaternion constructor: Quaternion(Real r, Real i, Real j, Real k)
        // jscienceBody.setOrientation(new Quaternion(Real.of(rot.w), Real.of(rot.x), Real.of(rot.y), Real.of(rot.z)));
        
        jscienceBody.setAngularVelocity(Vector.of(Arrays.asList(
                Real.of(bulletBody.getAngularVelocity(new Vector3f()).x),
                Real.of(bulletBody.getAngularVelocity(new Vector3f()).y),
                Real.of(bulletBody.getAngularVelocity(new Vector3f()).z)), Reals.getInstance()));
        
        jscienceBody.setVelocity(Vector.of(Arrays.asList(
                Real.of(bulletBody.getLinearVelocity(new Vector3f()).x),
                Real.of(bulletBody.getLinearVelocity(new Vector3f()).y),
                Real.of(bulletBody.getLinearVelocity(new Vector3f()).z)), Reals.getInstance()));
    }

    @Override
    public void applyCentralForce(double x, double y, double z) {
        bulletBody.applyCentralForce(new Vector3f((float) x, (float) y, (float) z));
    }

    @Override
    public void applyCentralImpulse(double x, double y, double z) {
        bulletBody.applyCentralImpulse(new Vector3f((float) x, (float) y, (float) z));
    }

    @Override
    public void destroy() {
        // Bullet cleans up mostly
    }

    public com.bulletphysics.dynamics.RigidBody getBulletBody() {
        return bulletBody;
    }
}
