/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.server.server.tasks.chemistry;

import org.episteme.core.distributed.DistributedTask;
import org.episteme.core.distributed.TaskRegistry;
import org.episteme.core.distributed.TaskState;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.random.RandomGenerator;
import org.episteme.natural.physics.classical.matter.molecular.MolecularDynamicsProvider;
import org.episteme.natural.physics.classical.matter.molecular.providers.MulticoreMolecularDynamicsProvider;
import java.io.Serializable;
import com.google.auto.service.AutoService;
import java.util.ArrayList;
import java.util.List;
import org.episteme.natural.physics.classical.mechanics.Particle;

/**
 * Molecular Dynamics Simulation Task.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService(DistributedTask.class)
public class MolecularDynamicsTask
        implements DistributedTask<MolecularDynamicsTask, MolecularDynamicsTask> {

    private final int numAtoms;
    private final double timeStep;
    private final int steps;
    private final double boxSize;
    
    private TaskState<AtomState[]> state;
    private double totalEnergy;
    private TaskRegistry.PrecisionMode mode = TaskRegistry.PrecisionMode.DOUBLE;

    public MolecularDynamicsTask(int numAtoms, double timeStep, int steps, double boxSize) {
        this.numAtoms = numAtoms;
        this.timeStep = timeStep;
        this.steps = steps;
        this.boxSize = boxSize;
        
        AtomState[] initialAtoms = new AtomState[numAtoms];
        RandomGenerator rng = new RandomGenerator(System.nanoTime());
        for (int i = 0; i < numAtoms; i++) {
            initialAtoms[i] = new AtomState(
                    rng.nextReal().doubleValue() * boxSize, rng.nextReal().doubleValue() * boxSize, rng.nextReal().doubleValue() * boxSize,
                    (rng.nextReal().doubleValue() - 0.5), (rng.nextReal().doubleValue() - 0.5), (rng.nextReal().doubleValue() - 0.5),
                    1.0);
        }
        
        initTaskState(initialAtoms);
    }

    public MolecularDynamicsTask(List<Particle> particles, double timeStep) {
        this.numAtoms = particles.size();
        this.timeStep = timeStep;
        this.steps = 100; 
        this.boxSize = 10.0;
        
        AtomState[] initialAtoms = new AtomState[numAtoms];
        for (int i = 0; i < numAtoms; i++) {
            Particle p = particles.get(i);
            initialAtoms[i] = new AtomState(p.getX(), p.getY(), p.getZ(), 
                p.getVelocity().get(0).doubleValue(), 
                p.getVelocity().get(1).doubleValue(), 
                p.getVelocity().get(2).doubleValue(), 
                p.getMass().to(org.episteme.core.measure.Units.KILOGRAM).getValue().doubleValue());
        }
        initTaskState(initialAtoms);
    }

    private void initTaskState(AtomState[] initialAtoms) {
        this.state = new TaskState<>(initialAtoms,
            arr -> flattenDouble(arr), d -> unflattenDouble(d),
            arr -> flattenFloat(arr), f -> unflattenFloat(f)
        );
    }

    private double[] flattenDouble(AtomState[] arr) {
        double[] flat = new double[arr.length * 7];
        for (int i = 0; i < arr.length; i++) {
            AtomState a = arr[i];
            flat[i*7] = a.x; flat[i*7+1] = a.y; flat[i*7+2] = a.z;
            flat[i*7+3] = a.vx; flat[i*7+4] = a.vy; flat[i*7+5] = a.vz;
            flat[i*7+6] = a.mass;
        }
        return flat;
    }

    private AtomState[] unflattenDouble(double[] arr) {
        AtomState[] res = new AtomState[arr.length / 7];
        for (int i = 0; i < res.length; i++) {
            res[i] = new AtomState(arr[i*7], arr[i*7+1], arr[i*7+2], arr[i*7+3], arr[i*7+4], arr[i*7+5], arr[i*7+6]);
        }
        return res;
    }

    private float[] flattenFloat(AtomState[] arr) {
        float[] flat = new float[arr.length * 7];
        for (int i = 0; i < arr.length; i++) {
            AtomState a = arr[i];
            flat[i*7] = (float)a.x; flat[i*7+1] = (float)a.y; flat[i*7+2] = (float)a.z;
            flat[i*7+3] = (float)a.vx; flat[i*7+4] = (float)a.vy; flat[i*7+5] = (float)a.vz;
            flat[i*7+6] = (float)a.mass;
        }
        return flat;
    }

    private AtomState[] unflattenFloat(float[] arr) {
        AtomState[] res = new AtomState[arr.length / 7];
        for (int i = 0; i < res.length; i++) {
            res[i] = new AtomState(arr[i*7], arr[i*7+1], arr[i*7+2], arr[i*7+3], arr[i*7+4], arr[i*7+5], arr[i*7+6]);
        }
        return res;
    }

    public MolecularDynamicsTask() {
        this(0, 0, 0, 0);
    }

    public void setMode(TaskRegistry.PrecisionMode mode) {
        this.mode = mode;
        this.state.syncTo(mode);
    }

    @Override
    public Class<MolecularDynamicsTask> getInputType() { return MolecularDynamicsTask.class; }
    @Override
    public Class<MolecularDynamicsTask> getOutputType() { return MolecularDynamicsTask.class; }

    @Override
    public MolecularDynamicsTask execute(MolecularDynamicsTask input) {
        if (input != null && input.numAtoms > 0) {
            input.run();
            return input;
        }
        return null;
    }

    @Override
    public String getTaskType() { return "MOLECULAR_DYNAMICS"; }

    public void run() {
        MolecularDynamicsProvider provider = new MulticoreMolecularDynamicsProvider();
        for (int s = 0; s < steps; s++) {
            switch (mode) {
                case REAL -> runRealStep(provider);
                case FLOAT -> runFloatStep(provider);
                default -> runPrimitiveStep(provider);
            }
        }
        calculateTotalEnergy();
    }

    private void runFloatStep(MolecularDynamicsProvider provider) {
        float[] flat = state.getFloat();
        int n = numAtoms;
        float[] pos = new float[n*3], vel = new float[n*3], forces = new float[n*3], masses = new float[n];
        for(int i=0; i<n; i++) {
            pos[i*3]=flat[i*7]; pos[i*3+1]=flat[i*7+1]; pos[i*3+2]=flat[i*7+2];
            vel[i*3]=flat[i*7+3]; vel[i*3+1]=flat[i*7+4]; vel[i*3+2]=flat[i*7+5];
            masses[i]=flat[i*7+6];
        }
        provider.calculateNonBondedForces(pos, forces, 1.0f, 1.0f, 2.5f);
        provider.integrate(pos, vel, forces, masses, (float)timeStep, 1.0f);
        for(int i=0; i<n; i++) {
            for(int j=0; j<3; j++) {
                if(pos[i*3+j] < 0 || pos[i*3+j] > boxSize) { vel[i*3+j]*=-1; pos[i*3+j] = (float)Math.max(0, Math.min(boxSize, pos[i*3+j])); }
            }
            flat[i*7]=pos[i*3]; flat[i*7+1]=pos[i*3+1]; flat[i*7+2]=pos[i*3+2];
            flat[i*7+3]=vel[i*3]; flat[i*7+4]=vel[i*3+1]; flat[i*7+5]=vel[i*3+2];
        }
    }

    private void runPrimitiveStep(MolecularDynamicsProvider provider) {
        double[] flat = state.getDouble();
        int n = numAtoms;
        double[] pos = new double[n*3], vel = new double[n*3], forces = new double[n*3], masses = new double[n];
        for(int i=0; i<n; i++) {
            pos[i*3]=flat[i*7]; pos[i*3+1]=flat[i*7+1]; pos[i*3+2]=flat[i*7+2];
            vel[i*3]=flat[i*7+3]; vel[i*3+1]=flat[i*7+4]; vel[i*3+2]=flat[i*7+5];
            masses[i]=flat[i*7+6];
        }
        provider.calculateNonBondedForces(pos, forces, 1.0, 1.0, 2.5);
        provider.integrate(pos, vel, forces, masses, timeStep, 1.0);
        for(int i=0; i<n; i++) {
            for(int j=0; j<3; j++) {
                if(pos[i*3+j] < 0 || pos[i*3+j] > boxSize) { vel[i*3+j]*=-1; pos[i*3+j] = Math.max(0, Math.min(boxSize, pos[i*3+j])); }
            }
            flat[i*7]=pos[i*3]; flat[i*7+1]=pos[i*3+1]; flat[i*7+2]=pos[i*3+2];
            flat[i*7+3]=vel[i*3]; flat[i*7+4]=vel[i*3+1]; flat[i*7+5]=vel[i*3+2];
        }
    }

    private void runRealStep(MolecularDynamicsProvider provider) {
        AtomState[] atoms = state.getReal();
        int n = numAtoms;
        Real[] pos = new Real[n*3], vel = new Real[n*3], forces = new Real[n*3], masses = new Real[n];
        for(int i=0; i<n; i++) {
            AtomState a = atoms[i];
            pos[i*3]=Real.of(a.x); pos[i*3+1]=Real.of(a.y); pos[i*3+2]=Real.of(a.z);
            vel[i*3]=Real.of(a.vx); vel[i*3+1]=Real.of(a.vy); vel[i*3+2]=Real.of(a.vz);
            masses[i]=Real.of(a.mass);
            forces[i*3]=Real.ZERO; forces[i*3+1]=Real.ZERO; forces[i*3+2]=Real.ZERO;
        }
        provider.calculateNonBondedForces(pos, forces, Real.ONE, Real.ONE, Real.of(2.5));
        provider.integrate(pos, vel, forces, masses, Real.of(timeStep), Real.ONE);
        for(int i=0; i<n; i++) {
            double x=pos[i*3].doubleValue(), y=pos[i*3+1].doubleValue(), z=pos[i*3+2].doubleValue();
            double vx=vel[i*3].doubleValue(), vy=vel[i*3+1].doubleValue(), vz=vel[i*3+2].doubleValue();
            if(x<0||x>boxSize) { vx*=-1; x=Math.max(0, Math.min(boxSize, x)); }
            if(y<0||y>boxSize) { vy*=-1; y=Math.max(0, Math.min(boxSize, y)); }
            if(z<0||z>boxSize) { vz*=-1; z=Math.max(0, Math.min(boxSize, z)); }
            atoms[i] = new AtomState(x,y,z, vx,vy,vz, atoms[i].mass);
        }
    }

    private void calculateTotalEnergy() {
        totalEnergy = 0;
        state.syncTo(TaskRegistry.PrecisionMode.REAL);
        for (AtomState a : state.getReal()) {
            totalEnergy += 0.5 * a.mass * (a.vx * a.vx + a.vy * a.vy + a.vz * a.vz);
        }
    }

    public List<AtomState> getAtoms() { 
        state.syncTo(TaskRegistry.PrecisionMode.REAL); 
        return java.util.Arrays.asList(state.getReal()); 
    }
    public double getTotalEnergy() { return totalEnergy; }
    
    public int getNumAtoms() { return numAtoms; }
    public double getTimeStep() { return timeStep; }
    public int getSteps() { return steps; }
    public double getBoxSize() { return boxSize; }

    public void updateState(List<AtomState> atoms, double energy) {
        this.totalEnergy = energy;
        state.syncTo(TaskRegistry.PrecisionMode.REAL);
        AtomState[] real = state.getReal();
        for(int i=0; i<Math.min(real.length, atoms.size()); i++) real[i] = atoms.get(i);
    }

    public static class AtomState implements Serializable {
        public double x, y, z, vx, vy, vz, mass;
        public AtomState(double x, double y, double z, double vx, double vy, double vz, double mass) {
            this.x = x; this.y = y; this.z = z; this.vx = vx; this.vy = vy; this.vz = vz; this.mass = mass;
        }
    }
}
