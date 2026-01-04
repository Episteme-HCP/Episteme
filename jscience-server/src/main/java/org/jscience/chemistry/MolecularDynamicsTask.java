/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.chemistry;

import org.jscience.distributed.DistributedTask;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Molecular Dynamics Simulation Task.
 */
public class MolecularDynamicsTask implements DistributedTask<MolecularDynamicsTask, MolecularDynamicsTask>, Serializable {

    private final int numAtoms;
    private final double timeStep;
    private final int steps;
    private final double boxSize;

    // State
    private List<AtomState> atoms;
    private double totalEnergy;

    public MolecularDynamicsTask(int numAtoms, double timeStep, int steps, double boxSize) {
        this.numAtoms = numAtoms;
        this.timeStep = timeStep;
        this.steps = steps;
        this.boxSize = boxSize;
        this.atoms = new ArrayList<>(numAtoms);
        initialize();
    }
    
    // No-arg constructor for ServiceLoader/Reflective instantiation
    public MolecularDynamicsTask() {
        this(0, 0, 0, 0);
    }

    public MolecularDynamicsTask(List<? extends Object> particles, double boxSize) {
        this.numAtoms = particles.size();
        this.timeStep = 0.001;
        this.steps = 100;
        this.boxSize = boxSize;
        this.atoms = new ArrayList<>(numAtoms);
        initialize();
    }

    private void initialize() {
        if (numAtoms == 0) return;
        for (int i = 0; i < numAtoms; i++) {
            atoms.add(new AtomState(
                    Math.random() * boxSize, Math.random() * boxSize, Math.random() * boxSize,
                    (Math.random() - 0.5), (Math.random() - 0.5), (Math.random() - 0.5),
                    1.0
            ));
        }
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
        if (this.numAtoms > 0) {
            this.run();
            return this;
        }
        return null;
    }

    @Override
    public String getTaskType() {
        return "MOLECULAR_DYNAMICS";
    }

    public void run() {
        for (int s = 0; s < steps; s++) {
            verletStep();
        }
        calculateTotalEnergy();
    }

    private void verletStep() {
        double[][] forces = calculateForces();
        for (int i = 0; i < numAtoms; i++) {
            AtomState a = atoms.get(i);
            double ax = forces[i][0] / a.mass;
            double ay = forces[i][1] / a.mass;
            double az = forces[i][2] / a.mass;
            a.vx += ax * timeStep;
            a.vy += ay * timeStep;
            a.vz += az * timeStep;
            a.x += a.vx * timeStep;
            a.y += a.vy * timeStep;
            a.z += a.vz * timeStep;
            a.x += a.vx * timeStep;
            a.y += a.vy * timeStep;
            a.z += a.vz * timeStep;
            if (a.x < 0 || a.x > boxSize) a.vx *= -1;
            if (a.y < 0 || a.y > boxSize) a.vy *= -1;
            if (a.z < 0 || a.z > boxSize) a.vz *= -1;
            a.x = Math.max(0, Math.min(boxSize, a.x));
            a.y = Math.max(0, Math.min(boxSize, a.y));
            a.z = Math.max(0, Math.min(boxSize, a.z));
        }
    }

    private double[][] calculateForces() {
        double[][] forces = new double[numAtoms][3];
        double epsilon = 1.0;
        double sigma = 1.0;
        double cutoff = 2.5 * sigma;
        double cutoffSq = cutoff * cutoff;
        for (int i = 0; i < numAtoms; i++) {
            for (int j = i + 1; j < numAtoms; j++) {
                AtomState a1 = atoms.get(i);
                AtomState a2 = atoms.get(j);
                double dx = a2.x - a1.x;
                double dy = a2.y - a1.y;
                double dz = a2.z - a1.z;
                double distSq = dx * dx + dy * dy + dz * dz;
                if (distSq < cutoffSq && distSq > 0.0001) {
                    double invDist2 = 1.0 / distSq;
                    double invDist6 = invDist2 * invDist2 * invDist2;
                    double invDist12 = invDist6 * invDist6;
                    double forceMag = 24 * epsilon * invDist2 * (2 * invDist12 - invDist6);
                    double fx = forceMag * dx;
                    double fy = forceMag * dy;
                    double fz = forceMag * dz;
                    forces[i][0] -= fx;
                    forces[i][1] -= fy;
                    forces[i][2] -= fz;
                    forces[j][0] += fx;
                    forces[j][1] += fy;
                    forces[j][2] += fz;
                }
            }
        }
        return forces;
    }

    private void calculateTotalEnergy() {
        totalEnergy = 0;
        for (AtomState a : atoms) {
            totalEnergy += 0.5 * a.mass * (a.vx * a.vx + a.vy * a.vy + a.vz * a.vz);
        }
    }

    public List<AtomState> getAtoms() { return atoms; }
    public double getTotalEnergy() { return totalEnergy; }
    public int getNumAtoms() { return numAtoms; }
    public double getTimeStep() { return timeStep; }
    public int getSteps() { return steps; }
    public double getBoxSize() { return boxSize; }
    public void updateState(List<AtomState> newAtoms, double newEnergy) {
        this.atoms = newAtoms;
        this.totalEnergy = newEnergy;
    }

    public static class AtomState implements Serializable {
        public double x, y, z;
        public double vx, vy, vz;
        public double mass;

        public AtomState(double x, double y, double z, double vx, double vy, double vz, double mass) {
            this.x = x; this.y = y; this.z = z;
            this.vx = vx; this.vy = vy; this.vz = vz;
            this.mass = mass;
        }
    }
}
