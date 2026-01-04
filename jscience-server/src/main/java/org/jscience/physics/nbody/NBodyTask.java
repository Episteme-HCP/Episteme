/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.physics.nbody;

import org.jscience.distributed.DistributedTask;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NBodyTask implements DistributedTask<NBodyTask, NBodyTask>, Serializable {

    public static class Body implements Serializable {
        public double x, y, z;
        public double vx, vy, vz;
        public double mass;

        public Body(double x, double y, double z, double vx, double vy, double vz, double mass) {
            this.x = x; this.y = y; this.z = z;
            this.vx = vx; this.vy = vy; this.vz = vz;
            this.mass = mass;
        }
    }

    private List<Body> bodies;
    private double dt = 0.01;
    private double softening = 0.1;
    private static final double G = 1.0;

    public NBodyTask(List<Body> bodies) {
        this.bodies = new ArrayList<>(bodies);
    }
    
    public NBodyTask() {
        this.bodies = new ArrayList<>();
    }

    @Override
    public Class<NBodyTask> getInputType() { return NBodyTask.class; }
    @Override
    public Class<NBodyTask> getOutputType() { return NBodyTask.class; }

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
    public String getTaskType() { return "N_BODY"; }

    public void step() {
        int n = bodies.size();
        double[][] acc = new double[n][3];

        for (int i = 0; i < n; i++) {
            Body bi = bodies.get(i);
            for (int j = i + 1; j < n; j++) {
                Body bj = bodies.get(j);
                double dx = bj.x - bi.x;
                double dy = bj.y - bi.y;
                double dz = bj.z - bi.z;
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz + softening * softening);
                double f = G / (dist * dist * dist);

                acc[i][0] += f * dx * bj.mass;
                acc[i][1] += f * dy * bj.mass;
                acc[i][2] += f * dz * bj.mass;

                acc[j][0] -= f * dx * bi.mass;
                acc[j][1] -= f * dy * bi.mass;
                acc[j][2] -= f * dz * bi.mass;
            }
        }

        for (int i = 0; i < n; i++) {
            Body b = bodies.get(i);
            b.vx += acc[i][0] * dt;
            b.vy += acc[i][1] * dt;
            b.vz += acc[i][2] * dt;
            b.x += b.vx * dt;
            b.y += b.vy * dt;
            b.z += b.vz * dt;
        }
    }

    public List<Body> getBodies() { return bodies; }
    public void setDt(double dt) { this.dt = dt; }
    public void setSoftening(double s) { this.softening = s; }
}
