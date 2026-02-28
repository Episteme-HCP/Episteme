/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.physics.classical.mechanics.backends.genesis;

import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.natural.physics.classical.mechanics.RigidBody;
import org.episteme.natural.physics.classical.mechanics.RigidBodyBridge;

import java.util.Arrays;

/**
 * Genesis implementation of RigidBodyBackend.
 * Optimized for frequent state updates and parallel access.
 */
public class GenesisRigidBody implements RigidBodyBridge {

    private final RigidBody body;
    private double px, py, pz;
    private double vx, vy, vz;
    private double fx, fy, fz;
    private double mass = 1.0;

    public GenesisRigidBody(RigidBody body) {
        this.body = body;
        pullState();
    }

    public RigidBody getEpistemeBody() {
        return body;
    }

    @Override
    public void pushState() {
        body.setPosition(Vector.of(Arrays.asList(Real.of(px), Real.of(py), Real.of(pz)), Reals.getInstance()));
        body.setVelocity(Vector.of(Arrays.asList(Real.of(vx), Real.of(vy), Real.of(vz)), Reals.getInstance()));
    }

    @Override
    public void pullState() {
        Vector<Real> pos = body.getPosition();
        this.px = pos.get(0).doubleValue();
        this.py = pos.get(1).doubleValue();
        this.pz = pos.get(2).doubleValue();

        Vector<Real> vel = body.getVelocity();
        this.vx = vel.get(0).doubleValue();
        this.vy = vel.get(1).doubleValue();
        this.vz = vel.get(2).doubleValue();
    }

    void applyGravity(double gx, double gy, double gz) {
        fx += gx * mass;
        fy += gy * mass;
        fz += gz * mass;
    }

    void integrate(double dt) {
        // Semi-implicit Euler
        vx += (fx / mass) * dt;
        vy += (fy / mass) * dt;
        vz += (fz / mass) * dt;

        px += vx * dt;
        py += vy * dt;
        pz += vz * dt;

        // Clear forces
        fx = 0; fy = 0; fz = 0;
        
        pushState();
    }

    @Override
    public void applyCentralForce(double x, double y, double z) {
        fx += x;
        fy += y;
        fz += z;
    }

    @Override
    public void applyCentralImpulse(double x, double y, double z) {
        vx += x / mass;
        vy += y / mass;
        vz += z / mass;
        pushState();
    }

    @Override
    public void destroy() {
        // No-op
    }
}
