/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.physics.nbody;

import org.jscience.distributed.DistributedTask;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jscience.distributed.PrecisionMode;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.technical.backend.algorithms.NBodyProvider;
import org.jscience.technical.backend.algorithms.MulticoreNBodyProvider;

public class NBodyTask implements DistributedTask<NBodyTask, NBodyTask>, Serializable {

    public static class Body implements Serializable {
        public double x, y, z;
        public double vx, vy, vz;
        public double mass;

        public Body(double x, double y, double z, double vx, double vy, double vz, double mass) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
            this.mass = mass;
        }
    }

    private List<Body> bodies;
    private double dt = 0.01;
    private double softening = 0.1;
    private static final double G = 1.0;
    private PrecisionMode mode = PrecisionMode.PRIMITIVES;

    public NBodyTask(List<Body> bodies) {
        this.bodies = new ArrayList<>(bodies);
    }

    public void setMode(PrecisionMode mode) {
        this.mode = mode;
    }

    public NBodyTask() {
        this.bodies = new ArrayList<>();
    }

    @Override
    public Class<NBodyTask> getInputType() {
        return NBodyTask.class;
    }

    @Override
    public Class<NBodyTask> getOutputType() {
        return NBodyTask.class;
    }

    @Override
    public NBodyTask execute(NBodyTask input) {
        if (input != null && input.bodies != null) {
            input.step();
            return input;
        }
        if (this.bodies != null && !this.bodies.isEmpty()) {
            this.step();
            return this;
        }
        return null;
    }

    @Override
    public String getTaskType() {
        return "N_BODY";
    }

    public void step() {
        if (mode == PrecisionMode.REALS) {
            stepReal();
        } else {
            stepPrimitive();
        }
    }

    private void stepReal() {
        int n = bodies.size();
        Real[] positions = new Real[n * 3];
        Real[] masses = new Real[n];
        Real[] forces = new Real[n * 3];

        for (int i = 0; i < n; i++) {
            Body b = bodies.get(i);
            positions[i * 3] = Real.of(b.x);
            positions[i * 3 + 1] = Real.of(b.y);
            positions[i * 3 + 2] = Real.of(b.z);
            masses[i] = Real.of(b.mass);
        }

        NBodyProvider provider = new MulticoreNBodyProvider();
        provider.computeForces(positions, masses, forces, Real.of(G), Real.of(softening));

        for (int i = 0; i < n; i++) {
            Body b = bodies.get(i);
            double ax = forces[i * 3].doubleValue() / b.mass;
            double ay = forces[i * 3 + 1].doubleValue() / b.mass;
            double az = forces[i * 3 + 2].doubleValue() / b.mass;

            b.vx += ax * dt;
            b.vy += ay * dt;
            b.vz += az * dt;
            b.x += b.vx * dt;
            b.y += b.vy * dt;
            b.z += b.vz * dt;
        }
    }

    private void stepPrimitive() {
        int n = bodies.size();
        double[] positions = new double[n * 3];
        double[] masses = new double[n];
        double[] forces = new double[n * 3];

        for (int i = 0; i < n; i++) {
            Body b = bodies.get(i);
            positions[i * 3] = b.x;
            positions[i * 3 + 1] = b.y;
            positions[i * 3 + 2] = b.z;
            masses[i] = b.mass;
        }

        NBodyProvider provider = new MulticoreNBodyProvider();
        provider.computeForces(positions, masses, forces, G, softening);

        for (int i = 0; i < n; i++) {
            Body b = bodies.get(i);
            double ax = forces[i * 3] / b.mass;
            double ay = forces[i * 3 + 1] / b.mass;
            double az = forces[i * 3 + 2] / b.mass;

            b.vx += ax * dt;
            b.vy += ay * dt;
            b.vz += az * dt;
            b.x += b.vx * dt;
            b.y += b.vy * dt;
            b.z += b.vz * dt;
        }
    }

    public List<Body> getBodies() {
        return bodies;
    }

    public void setDt(double dt) {
        this.dt = dt;
    }

    public void setSoftening(double s) {
        this.softening = s;
    }
}
