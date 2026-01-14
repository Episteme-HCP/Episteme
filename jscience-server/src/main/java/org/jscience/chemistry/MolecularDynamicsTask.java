/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.chemistry;

import org.jscience.distributed.DistributedTask;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Molecular Dynamics Simulation Task.
 */
public class MolecularDynamicsTask
        implements DistributedTask<MolecularDynamicsTask, MolecularDynamicsTask> {

    private final int numAtoms;
    private final double timeStep;
    private final int steps;
    private final double boxSize;
    private List<AtomState> atoms;
    private double totalEnergy;

    public enum PrecisionMode {
        REALS,
        PRIMITIVES
    }

    private PrecisionMode mode = PrecisionMode.PRIMITIVES;
    private List<org.jscience.chemistry.Atom> jscienceAtoms;

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
        if (numAtoms == 0)
            return;
        for (int i = 0; i < numAtoms; i++) {
            atoms.add(new AtomState(
                    Math.random() * boxSize, Math.random() * boxSize, Math.random() * boxSize,
                    (Math.random() - 0.5), (Math.random() - 0.5), (Math.random() - 0.5),
                    1.0));
        }
    }

    public void setMode(PrecisionMode mode) {
        this.mode = mode;
        if (mode == PrecisionMode.REALS && jscienceAtoms == null) {
            syncToJScience();
        }
    }

    private void syncToJScience() {
        jscienceAtoms = new ArrayList<>(numAtoms);
        org.jscience.chemistry.Element hydrogen = new org.jscience.chemistry.Element("Hydrogen", "H");
        for (AtomState a : atoms) {
            org.jscience.mathematics.linearalgebra.Vector<org.jscience.mathematics.numbers.real.Real> pos = createVector(
                    a.x, a.y, a.z);
            org.jscience.chemistry.Atom atom = new org.jscience.chemistry.Atom(hydrogen, pos);
            atom.setVelocity(createVector(a.vx, a.vy, a.vz));
            jscienceAtoms.add(atom);
        }
    }

    private void syncFromJScience() {
        for (int i = 0; i < numAtoms; i++) {
            org.jscience.chemistry.Atom atom = jscienceAtoms.get(i);
            AtomState a = atoms.get(i);
            a.x = atom.getPosition().get(0).doubleValue();
            a.y = atom.getPosition().get(1).doubleValue();
            a.z = atom.getPosition().get(2).doubleValue();
            a.vx = atom.getVelocity().get(0).doubleValue();
            a.vy = atom.getVelocity().get(1).doubleValue();
            a.vz = atom.getVelocity().get(2).doubleValue();
        }
    }

    private org.jscience.mathematics.linearalgebra.Vector<org.jscience.mathematics.numbers.real.Real> createVector(
            double x, double y, double z) {
        List<org.jscience.mathematics.numbers.real.Real> list = new ArrayList<>();
        list.add(org.jscience.mathematics.numbers.real.Real.of(x));
        list.add(org.jscience.mathematics.numbers.real.Real.of(y));
        list.add(org.jscience.mathematics.numbers.real.Real.of(z));
        return org.jscience.mathematics.linearalgebra.vectors.DenseVector.of(list,
                org.jscience.mathematics.sets.Reals.getInstance());
    }

    @Override
    public Class<MolecularDynamicsTask> getInputType() {
        return MolecularDynamicsTask.class;
    }

    @Override
    public Class<MolecularDynamicsTask> getOutputType() {
        return MolecularDynamicsTask.class;
    }

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
            if (mode == PrecisionMode.REALS) {
                jscienceStep();
            } else {
                primitiveStep();
            }
        }
        calculateTotalEnergy();
    }

    private void jscienceStep() {
        org.jscience.technical.backend.algorithms.MolecularDynamicsProvider provider = new org.jscience.technical.backend.algorithms.MulticoreMolecularDynamicsProvider();

        int n = numAtoms;
        org.jscience.mathematics.numbers.real.Real[] positions = new org.jscience.mathematics.numbers.real.Real[n * 3];
        org.jscience.mathematics.numbers.real.Real[] velocities = new org.jscience.mathematics.numbers.real.Real[n * 3];
        org.jscience.mathematics.numbers.real.Real[] forces = new org.jscience.mathematics.numbers.real.Real[n * 3];
        org.jscience.mathematics.numbers.real.Real[] masses = new org.jscience.mathematics.numbers.real.Real[n];

        for (int i = 0; i < n; i++) {
            org.jscience.chemistry.Atom a = jscienceAtoms.get(i);
            positions[i * 3] = a.getPosition().get(0);
            positions[i * 3 + 1] = a.getPosition().get(1);
            positions[i * 3 + 2] = a.getPosition().get(2);
            velocities[i * 3] = a.getVelocity().get(0);
            velocities[i * 3 + 1] = a.getVelocity().get(1);
            velocities[i * 3 + 2] = a.getVelocity().get(2);
            forces[i * 3] = org.jscience.mathematics.numbers.real.Real.ZERO;
            forces[i * 3 + 1] = org.jscience.mathematics.numbers.real.Real.ZERO;
            forces[i * 3 + 2] = org.jscience.mathematics.numbers.real.Real.ZERO;
            masses[i] = org.jscience.mathematics.numbers.real.Real.of(a.getMass().getValue().doubleValue());
        }

        org.jscience.mathematics.numbers.real.Real epsilon = org.jscience.mathematics.numbers.real.Real.ONE;
        org.jscience.mathematics.numbers.real.Real sigma = org.jscience.mathematics.numbers.real.Real.ONE;
        org.jscience.mathematics.numbers.real.Real cutoff = org.jscience.mathematics.numbers.real.Real.of(2.5);

        provider.calculateNonBondedForces(positions, forces, epsilon, sigma, cutoff);
        provider.integrate(positions, velocities, forces, masses,
                org.jscience.mathematics.numbers.real.Real.of(timeStep),
                org.jscience.mathematics.numbers.real.Real.ONE);

        for (int i = 0; i < n; i++) {
            org.jscience.chemistry.Atom a = jscienceAtoms.get(i);
            a.setPosition(positions[i * 3], positions[i * 3 + 1], positions[i * 3 + 2]);
            a.setVelocity(velocities[i * 3], velocities[i * 3 + 1], velocities[i * 3 + 2]);

            double x = a.getX();
            double y = a.getY();
            double z = a.getZ();
            boolean hit = false;
            if (x < 0 || x > boxSize) {
                hit = true;
                x = Math.max(0, Math.min(boxSize, x));
            }
            if (y < 0 || y > boxSize) {
                hit = true;
                y = Math.max(0, Math.min(boxSize, y));
            }
            if (z < 0 || z > boxSize) {
                hit = true;
                z = Math.max(0, Math.min(boxSize, z));
            }

            if (hit) {
                a.setPosition(org.jscience.mathematics.numbers.real.Real.of(x),
                        org.jscience.mathematics.numbers.real.Real.of(y),
                        org.jscience.mathematics.numbers.real.Real.of(z));
                a.setVelocity(a.getVelocity().multiply(org.jscience.mathematics.numbers.real.Real.of(-1)));
            }
        }
        syncFromJScience();
    }

    private void primitiveStep() {
        MolecularDynamicsPrimitiveSupport support = new MolecularDynamicsPrimitiveSupport();
        int n = numAtoms;
        double[] positions = new double[n * 3];
        double[] velocities = new double[n * 3];
        double[] forces = new double[n * 3];
        double[] masses = new double[n];

        for (int i = 0; i < n; i++) {
            AtomState a = atoms.get(i);
            positions[i * 3] = a.x;
            positions[i * 3 + 1] = a.y;
            positions[i * 3 + 2] = a.z;
            velocities[i * 3] = a.vx;
            velocities[i * 3 + 1] = a.vy;
            velocities[i * 3 + 2] = a.vz;
            masses[i] = a.mass;
        }

        support.calculateNonBondedForces(positions, forces, 1.0, 1.0, 2.5, n);
        support.integrate(positions, velocities, forces, masses, timeStep, 1.0, n);

        for (int i = 0; i < n; i++) {
            AtomState a = atoms.get(i);
            a.x = positions[i * 3];
            a.y = positions[i * 3 + 1];
            a.z = positions[i * 3 + 2];
            a.vx = velocities[i * 3];
            a.vy = velocities[i * 3 + 1];
            a.vz = velocities[i * 3 + 2];

            if (a.x < 0 || a.x > boxSize) {
                a.vx *= -1;
                a.x = Math.max(0, Math.min(boxSize, a.x));
            }
            if (a.y < 0 || a.y > boxSize) {
                a.vy *= -1;
                a.y = Math.max(0, Math.min(boxSize, a.y));
            }
            if (a.z < 0 || a.z > boxSize) {
                a.vz *= -1;
                a.z = Math.max(0, Math.min(boxSize, a.z));
            }
        }
    }

    private void calculateTotalEnergy() {
        totalEnergy = 0;
        for (AtomState a : atoms) {
            totalEnergy += 0.5 * a.mass * (a.vx * a.vx + a.vy * a.vy + a.vz * a.vz);
        }
    }

    public List<AtomState> getAtoms() {
        return atoms;
    }

    public double getTotalEnergy() {
        return totalEnergy;
    }

    public int getNumAtoms() {
        return numAtoms;
    }

    public double getTimeStep() {
        return timeStep;
    }

    public int getSteps() {
        return steps;
    }

    public double getBoxSize() {
        return boxSize;
    }

    public void updateState(List<AtomState> newAtoms, double newEnergy) {
        this.atoms = newAtoms;
        this.totalEnergy = newEnergy;
    }

    public static class AtomState implements Serializable {
        public double x, y, z;
        public double vx, vy, vz;
        public double mass;

        public AtomState(double x, double y, double z, double vx, double vy, double vz, double mass) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
            this.mass = mass;
        }
    }
}
