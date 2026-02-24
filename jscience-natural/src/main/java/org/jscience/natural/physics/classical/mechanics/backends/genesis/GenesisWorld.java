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

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;

import java.util.ArrayList;
import java.util.List;

/**
 * Genesis implementation of PhysicsWorldBackend.
 * Specialized for many-body systems. Uses the JDK Vector API (SIMD) to
 * accelerate the Euler-Cromer integration loop over flat double arrays.
 */
public class GenesisWorld implements PhysicsWorldBridge {

    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

    /** Wrapped JScience bodies for state sync. */
    private final List<GenesisRigidBody> bodies = new ArrayList<>();

    // Flat double arrays: indexed [i] for body i
    private double[] px, py, pz;
    private double[] vx, vy, vz;
    private double[] fx, fy, fz;
    private double[] mass;
    private int n = 0;

    private double gx, gy, gz;

    public GenesisWorld() {
        this.gx = 0;
        this.gy = -9.81;
        this.gz = 0;
        allocate(16);
    }

    private void allocate(int capacity) {
        px = new double[capacity]; py = new double[capacity]; pz = new double[capacity];
        vx = new double[capacity]; vy = new double[capacity]; vz = new double[capacity];
        fx = new double[capacity]; fy = new double[capacity]; fz = new double[capacity];
        mass = new double[capacity];
    }

    private void ensureCapacity() {
        if (n >= px.length) {
            int newCap = px.length * 2;
            px = java.util.Arrays.copyOf(px, newCap); py = java.util.Arrays.copyOf(py, newCap); pz = java.util.Arrays.copyOf(pz, newCap);
            vx = java.util.Arrays.copyOf(vx, newCap); vy = java.util.Arrays.copyOf(vy, newCap); vz = java.util.Arrays.copyOf(vz, newCap);
            fx = java.util.Arrays.copyOf(fx, newCap); fy = java.util.Arrays.copyOf(fy, newCap); fz = java.util.Arrays.copyOf(fz, newCap);
            mass = java.util.Arrays.copyOf(mass, newCap);
        }
    }

    @Override
    public void addRigidBody(RigidBody body) {
        GenesisRigidBody gb = new GenesisRigidBody(body);
        ensureCapacity();
        int i = n++;
        bodies.add(gb);
        // Pull initial state into flat arrays
        org.jscience.core.mathematics.linearalgebra.Vector<org.jscience.core.mathematics.numbers.real.Real> pos = body.getPosition();
        org.jscience.core.mathematics.linearalgebra.Vector<org.jscience.core.mathematics.numbers.real.Real> vel = body.getVelocity();
        px[i] = pos.get(0).doubleValue(); py[i] = pos.get(1).doubleValue(); pz[i] = pos.get(2).doubleValue();
        vx[i] = vel.get(0).doubleValue(); vy[i] = vel.get(1).doubleValue(); vz[i] = vel.get(2).doubleValue();
        fx[i] = 0; fy[i] = 0; fz[i] = 0;
        mass[i] = 1.0; // default mass
    }

    @Override
    public void removeRigidBody(RigidBody body) {
        int idx = -1;
        for (int i = 0; i < n; i++) {
            if (bodies.get(i).getJScienceBody() == body) { idx = i; break; }
        }
        if (idx < 0) return;
        bodies.remove(idx);
        // Compact flat arrays
        int tail = n - idx - 1;
        if (tail > 0) {
            System.arraycopy(px, idx+1, px, idx, tail); System.arraycopy(py, idx+1, py, idx, tail); System.arraycopy(pz, idx+1, pz, idx, tail);
            System.arraycopy(vx, idx+1, vx, idx, tail); System.arraycopy(vy, idx+1, vy, idx, tail); System.arraycopy(vz, idx+1, vz, idx, tail);
            System.arraycopy(fx, idx+1, fx, idx, tail); System.arraycopy(fy, idx+1, fy, idx, tail); System.arraycopy(fz, idx+1, fz, idx, tail);
            System.arraycopy(mass, idx+1, mass, idx, tail);
        }
        n--;
    }

    @Override
    public void stepSimulation(Quantity<Time> timeStep) {
        double dt = timeStep.to(SI.SECOND).getValue().doubleValue();
        integrateVectorized(dt);
        pushStates();
    }

    @Override
    public void stepSimulation(Quantity<Time> timeStep, int maxSubSteps, Quantity<Time> fixedTimeStep) {
        double dt = timeStep.getValue(SI.SECOND).doubleValue();
        double fdt = fixedTimeStep.getValue(SI.SECOND).doubleValue();
        int steps = Math.min(maxSubSteps, (int) Math.ceil(dt / fdt));
        for (int s = 0; s < steps; s++) {
            integrateVectorized(fdt);
        }
        pushStates();
    }

    /**
     * SIMD-accelerated Euler-Cromer integration using JDK Vector API.
     * Processes bodies in SPECIES.length() lanes simultaneously.
     */
    private void integrateVectorized(double dt) {
        int len = SPECIES.length();

        // Broadcast scalars into vectors
        DoubleVector vGx = DoubleVector.broadcast(SPECIES, gx);
        DoubleVector vGy = DoubleVector.broadcast(SPECIES, gy);
        DoubleVector vGz = DoubleVector.broadcast(SPECIES, gz);
        DoubleVector vDt = DoubleVector.broadcast(SPECIES, dt);

        int i = 0;
        // Vectorized loop (full lanes)
        for (; i <= n - len; i += len) {
            // Load velocity
            DoubleVector vVx = DoubleVector.fromArray(SPECIES, vx, i);
            DoubleVector vVy = DoubleVector.fromArray(SPECIES, vy, i);
            DoubleVector vVz = DoubleVector.fromArray(SPECIES, vz, i);

            // Load position
            DoubleVector vPx = DoubleVector.fromArray(SPECIES, px, i);
            DoubleVector vPy = DoubleVector.fromArray(SPECIES, py, i);
            DoubleVector vPz = DoubleVector.fromArray(SPECIES, pz, i);

            // Load forces and mass
            DoubleVector vFx = DoubleVector.fromArray(SPECIES, fx, i);
            DoubleVector vFy = DoubleVector.fromArray(SPECIES, fy, i);
            DoubleVector vFz = DoubleVector.fromArray(SPECIES, fz, i);
            DoubleVector vM  = DoubleVector.fromArray(SPECIES, mass, i);

            // Apply gravity (F += g * m)
            vFx = vFx.add(vGx.mul(vM));
            vFy = vFy.add(vGy.mul(vM));
            vFz = vFz.add(vGz.mul(vM));

            // Semi-implicit Euler: v += (F/m) * dt
            DoubleVector invM = DoubleVector.broadcast(SPECIES, 1.0).div(vM);
            vVx = vVx.add(vFx.mul(invM).mul(vDt));
            vVy = vVy.add(vFy.mul(invM).mul(vDt));
            vVz = vVz.add(vFz.mul(invM).mul(vDt));

            // p += v * dt
            vPx = vPx.add(vVx.mul(vDt));
            vPy = vPy.add(vVy.mul(vDt));
            vPz = vPz.add(vVz.mul(vDt));

            // Store results
            vVx.intoArray(vx, i); vVy.intoArray(vy, i); vVz.intoArray(vz, i);
            vPx.intoArray(px, i); vPy.intoArray(py, i); vPz.intoArray(pz, i);

            // Clear forces
            DoubleVector zero = DoubleVector.zero(SPECIES);
            zero.intoArray(fx, i); zero.intoArray(fy, i); zero.intoArray(fz, i);
        }

        // Scalar tail for remaining bodies
        for (; i < n; i++) {
            fx[i] += gx * mass[i]; fy[i] += gy * mass[i]; fz[i] += gz * mass[i];
            vx[i] += (fx[i] / mass[i]) * dt; vy[i] += (fy[i] / mass[i]) * dt; vz[i] += (fz[i] / mass[i]) * dt;
            px[i] += vx[i] * dt;             py[i] += vy[i] * dt;             pz[i] += vz[i] * dt;
            fx[i] = 0; fy[i] = 0; fz[i] = 0;
        }
    }

    /** Sync flat arrays back to JScience RigidBody objects. */
    private void pushStates() {
        for (int i = 0; i < n; i++) {
            GenesisRigidBody gb = bodies.get(i);
            RigidBody body = gb.getJScienceBody();
            body.setPosition(org.jscience.core.mathematics.linearalgebra.Vector.of(
                java.util.Arrays.asList(
                    org.jscience.core.mathematics.numbers.real.Real.of(px[i]),
                    org.jscience.core.mathematics.numbers.real.Real.of(py[i]),
                    org.jscience.core.mathematics.numbers.real.Real.of(pz[i])),
                org.jscience.core.mathematics.sets.Reals.getInstance()));
            body.setVelocity(org.jscience.core.mathematics.linearalgebra.Vector.of(
                java.util.Arrays.asList(
                    org.jscience.core.mathematics.numbers.real.Real.of(vx[i]),
                    org.jscience.core.mathematics.numbers.real.Real.of(vy[i]),
                    org.jscience.core.mathematics.numbers.real.Real.of(vz[i])),
                org.jscience.core.mathematics.sets.Reals.getInstance()));
        }
    }

    @Override
    public void setGravity(double gravityX, double gravityY, double gravityZ) {
        this.gx = gravityX;
        this.gy = gravityY;
        this.gz = gravityZ;
    }
}
