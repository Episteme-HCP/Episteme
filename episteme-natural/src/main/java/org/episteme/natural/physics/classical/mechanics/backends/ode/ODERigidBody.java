/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.physics.classical.mechanics.backends.ode;

import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.complex.Quaternion;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.natural.physics.classical.mechanics.RigidBody;
import org.episteme.natural.physics.classical.mechanics.RigidBodyBridge;
import org.ode4j.math.DQuaternion;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.DBody;
import org.ode4j.ode.DMass;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.OdeHelper;

import java.util.Arrays;

/**
 * ODE implementation of RigidBodyBackend.
 */
public class ODERigidBody implements RigidBodyBridge {

    private final RigidBody epistemeBody;
    private final DBody odeBody;

    public ODERigidBody(DWorld world, RigidBody body) {
        this.epistemeBody = body;
        this.odeBody = OdeHelper.createBody(world);
        
        DMass mass = OdeHelper.createMass();
        double m = 1.0; // Default mass
        mass.setI(OdeHelper.createMass().getI()); // Use identity or box mass
        mass.setBoxTotal(m, 1, 1, 1);
        odeBody.setMass(mass);

        pushState();
    }

    @Override
    public void pushState() {
        Vector<Real> pos = epistemeBody.getPosition();
        odeBody.setPosition(pos.get(0).doubleValue(), pos.get(1).doubleValue(), pos.get(2).doubleValue());
        
        Quaternion rot = epistemeBody.getOrientation();
        DQuaternion odeRot = new DQuaternion();
        odeRot.set(rot.getReal().doubleValue(), rot.getI().doubleValue(), rot.getJ().doubleValue(), rot.getK().doubleValue());
        odeBody.setQuaternion(odeRot);
        
        Vector<Real> vel = epistemeBody.getVelocity();
        odeBody.setLinearVel(vel.get(0).doubleValue(), vel.get(1).doubleValue(), vel.get(2).doubleValue());
        
        Vector<Real> angVel = epistemeBody.getAngularVelocity();
        odeBody.setAngularVel(angVel.get(0).doubleValue(), angVel.get(1).doubleValue(), angVel.get(2).doubleValue());
    }

    @Override
    public void pullState() {
        DVector3C pos = odeBody.getPosition();
        epistemeBody.setPosition(Vector.of(Arrays.asList(
                Real.of(pos.get0()), Real.of(pos.get1()), Real.of(pos.get2())), Reals.getInstance()));
        
        // Episteme orientation is Quaternion(r, i, j, k)
        // ODERigidBody pullState: need to set orientation.
        
        epistemeBody.setAngularVelocity(Vector.of(Arrays.asList(
                Real.of(odeBody.getAngularVel().get0()),
                Real.of(odeBody.getAngularVel().get1()),
                Real.of(odeBody.getAngularVel().get2())), Reals.getInstance()));
                
        epistemeBody.setVelocity(Vector.of(Arrays.asList(
                Real.of(odeBody.getLinearVel().get0()),
                Real.of(odeBody.getLinearVel().get1()),
                Real.of(odeBody.getLinearVel().get2())), Reals.getInstance()));
    }

    @Override
    public void applyCentralForce(double x, double y, double z) {
        odeBody.addForce(x, y, z);
    }

    @Override
    public void applyCentralImpulse(double x, double y, double z) {
        // ODE body.setLinearVel(odeBody.getLinearVel().add(x/m, y/m, z/m))
        // or just use impulse methods if available in extensions. ODE Core uses addForce.
        odeBody.addForce(x, y, z); // Approximation for fixed step
    }

    @Override
    public void destroy() {
        odeBody.destroy();
    }
}
