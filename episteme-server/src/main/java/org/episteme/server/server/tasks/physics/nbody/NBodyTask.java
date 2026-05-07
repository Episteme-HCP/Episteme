/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.server.server.tasks.physics.nbody;

import org.episteme.core.distributed.DistributedTask;
import org.episteme.core.distributed.TaskRegistry;
import org.episteme.core.distributed.TaskState;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.physics.classical.mechanics.nbody.NBodyProvider;
import org.episteme.natural.physics.classical.mechanics.nbody.providers.MulticoreNBodyProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.auto.service.AutoService;

/**
 * N-Body Simulation Task.
 */
@AutoService(DistributedTask.class)
public class NBodyTask implements DistributedTask<NBodyTask, NBodyTask> {

    public static class Body implements Serializable {
        public double x, y, z;
        public double vx, vy, vz;
        public double mass;
        public Body(double x, double y, double z, double vx, double vy, double vz, double mass) {
            this.x = x; this.y = y; this.z = z; this.vx = vx; this.vy = vy; this.vz = vz; this.mass = mass;
        }
    }

    private final int numBodies;
    private double dt = 0.01;
    private double softening = 0.1;
    private double G = 1.0;
    
    // State: [x,y,z, vx,vy,vz, mass] * numBodies
    private TaskState<Real[]> state;
    private TaskRegistry.PrecisionMode mode = TaskRegistry.PrecisionMode.DOUBLE;

    public NBodyTask(List<Body> bodies) {
        this.numBodies = bodies.size();
        Real[] initial = new Real[numBodies * 7];
        for (int i = 0; i < numBodies; i++) {
            Body b = bodies.get(i);
            initial[i*7] = Real.of(b.x);
            initial[i*7+1] = Real.of(b.y);
            initial[i*7+2] = Real.of(b.z);
            initial[i*7+3] = Real.of(b.vx);
            initial[i*7+4] = Real.of(b.vy);
            initial[i*7+5] = Real.of(b.vz);
            initial[i*7+6] = Real.of(b.mass);
        }
        this.state = new TaskState<>(initial,
            r -> flattenD(r), d -> unflattenR(d),
            r -> flattenF(r), f -> unflattenR(f)
        );
    }

    private double[] flattenD(Real[] r) {
        double[] d = new double[r.length];
        for(int i=0; i<r.length; i++) d[i] = r[i].doubleValue();
        return d;
    }
    private float[] flattenF(Real[] r) {
        float[] f = new float[r.length];
        for(int i=0; i<r.length; i++) f[i] = r[i].floatValue();
        return f;
    }
    private Real[] unflattenR(double[] d) {
        Real[] r = new Real[d.length];
        for(int i=0; i<d.length; i++) r[i] = Real.of(d[i]);
        return r;
    }
    private Real[] unflattenR(float[] f) {
        Real[] r = new Real[f.length];
        for(int i=0; i<f.length; i++) r[i] = Real.of(f[i]);
        return r;
    }

    public NBodyTask() { this(new ArrayList<>()); }

    public void setMode(TaskRegistry.PrecisionMode mode) {
        this.mode = mode;
        if(state != null) state.syncTo(mode);
    }

    @Override
    public Class<NBodyTask> getInputType() { return NBodyTask.class; }
    @Override
    public Class<NBodyTask> getOutputType() { return NBodyTask.class; }

    @Override
    public NBodyTask execute(NBodyTask input) {
        if (input != null && input.numBodies > 0) {
            input.step();
            return input;
        }
        return null;
    }

    @Override
    public String getTaskType() { return "N_BODY"; }

    public void step() {
        NBodyProvider provider = new MulticoreNBodyProvider();
        switch (mode) {
            case REAL -> {
                Real[] flat = state.getReal();
                Real[] pos = new Real[numBodies * 3];
                Real[] masses = new Real[numBodies];
                for(int i=0; i<numBodies; i++) {
                    pos[i*3] = flat[i*7]; pos[i*3+1] = flat[i*7+1]; pos[i*3+2] = flat[i*7+2];
                    masses[i] = flat[i*7+6];
                }
                Real[] forces = new Real[numBodies * 3];
                provider.computeForces(pos, masses, forces, Real.of(G), Real.of(softening));
                updateBodies(flat, forces);
            }
            case FLOAT -> {
                float[] flat = state.getFloat();
                float[] pos = new float[numBodies * 3];
                float[] masses = new float[numBodies];
                for(int i=0; i<numBodies; i++) {
                    pos[i*3] = flat[i*7]; pos[i*3+1] = flat[i*7+1]; pos[i*3+2] = flat[i*7+2];
                    masses[i] = flat[i*7+6];
                }
                float[] forces = new float[numBodies * 3];
                provider.computeForces(pos, masses, forces, (float)G, (float)softening);
                updateBodiesFloat(flat, forces);
            }
            default -> {
                double[] flat = state.getDouble();
                double[] pos = new double[numBodies * 3];
                double[] masses = new double[numBodies];
                for(int i=0; i<numBodies; i++) {
                    pos[i*3] = flat[i*7]; pos[i*3+1] = flat[i*7+1]; pos[i*3+2] = flat[i*7+2];
                    masses[i] = flat[i*7+6];
                }
                double[] forces = new double[numBodies * 3];
                provider.computeForces(pos, masses, forces, G, softening);
                updateBodiesDouble(flat, forces);
            }
        }
    }

    private void updateBodies(Real[] flat, Real[] forces) {
        for(int i=0; i<numBodies; i++) {
            double mass = flat[i*7+6].doubleValue();
            double ax = forces[i*3].doubleValue() / mass;
            double ay = forces[i*3+1].doubleValue() / mass;
            double az = forces[i*3+2].doubleValue() / mass;
            flat[i*7+3] = Real.of(flat[i*7+3].doubleValue() + ax * dt);
            flat[i*7+4] = Real.of(flat[i*7+4].doubleValue() + ay * dt);
            flat[i*7+5] = Real.of(flat[i*7+5].doubleValue() + az * dt);
            flat[i*7] = Real.of(flat[i*7].doubleValue() + flat[i*7+3].doubleValue() * dt);
            flat[i*7+1] = Real.of(flat[i*7+1].doubleValue() + flat[i*7+4].doubleValue() * dt);
            flat[i*7+2] = Real.of(flat[i*7+2].doubleValue() + flat[i*7+5].doubleValue() * dt);
        }
    }

    private void updateBodiesDouble(double[] flat, double[] forces) {
        for(int i=0; i<numBodies; i++) {
            double mass = flat[i*7+6];
            double ax = forces[i*3] / mass;
            double ay = forces[i*3+1] / mass;
            double az = forces[i*3+2] / mass;
            flat[i*7+3] += ax * dt; flat[i*7+4] += ay * dt; flat[i*7+5] += az * dt;
            flat[i*7] += flat[i*7+3] * dt; flat[i*7+1] += flat[i*7+4] * dt; flat[i*7+2] += flat[i*7+5] * dt;
        }
    }

    private void updateBodiesFloat(float[] flat, float[] forces) {
        for(int i=0; i<numBodies; i++) {
            float mass = flat[i*7+6];
            float ax = forces[i*3] / mass;
            float ay = forces[i*3+1] / mass;
            float az = forces[i*3+2] / mass;
            flat[i*7+3] += ax * (float)dt; flat[i*7+4] += ay * (float)dt; flat[i*7+5] += az * (float)dt;
            flat[i*7] += flat[i*7+3] * (float)dt; flat[i*7+1] += flat[i*7+4] * (float)dt; flat[i*7+2] += flat[i*7+5] * (float)dt;
        }
    }

    public List<Body> getBodies() {
        state.syncTo(TaskRegistry.PrecisionMode.DOUBLE);
        double[] flat = state.getDouble();
        List<Body> res = new ArrayList<>();
        for(int i=0; i<numBodies; i++) {
            res.add(new Body(flat[i*7], flat[i*7+1], flat[i*7+2], flat[i*7+3], flat[i*7+4], flat[i*7+5], flat[i*7+6]));
        }
        return res;
    }
}
