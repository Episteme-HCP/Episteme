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

package org.jscience.biology.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.distributed.PrecisionMode;

/**
 * DNA Folding Simulation Task.
 * 
 * Simulates the folding of a DNA/RNA sequence using a simplified energy model.
 * 
 * <p>
 * References:
 * <ul>
 * <li>Zuker, M., & Stiegler, P. (1981). Optimal computer folding of large RNA
 * sequences using thermodynamics and auxiliary information. Nucleic Acids
 * Research, 9(1), 133-148.</li>
 * <li>Tinoco Jr, I., Uhlenbeck, O. C., & Levine, M. D. (1971). Estimation of
 * secondary structure in ribonucleic acids. Nature, 230(5293), 362-367.</li>
 * </ul>
 * </p>
 */
public class DnaFoldingTask implements Serializable {

    private final String sequence; // ACGT...
    private final int iterations;
    private final double temperature;
    private PrecisionMode mode = PrecisionMode.PRIMITIVES;

    // Result
    private List<Point3D> foldedStructure;
    private List<Point3DReal> foldedStructureReal;
    private double finalEnergy;

    public DnaFoldingTask(String sequence, int iterations, double temperature) {
        this.sequence = sequence;
        this.iterations = iterations;
        this.temperature = temperature;
    }

    public void setMode(PrecisionMode mode) {
        this.mode = mode;
    }

    public void run() {
        if (mode == PrecisionMode.REALS) {
            runReal();
        } else {
            runPrimitive();
        }
    }

    private void runReal() {
        List<Point3DReal> structure = new ArrayList<>();
        Random rand = new Random();

        if (foldedStructureReal == null) {
            for (int i = 0; i < sequence.length(); i++) {
                structure.add(new Point3DReal(Real.of(i * 3.4), Real.of(0), Real.of(0)));
            }
        } else {
            structure.addAll(foldedStructureReal);
        }

        Real currentEnergy = calculateEnergyReal(structure);

        for (int i = 0; i < iterations; i++) {
            int idx = rand.nextInt(sequence.length());
            Point3DReal originalPos = structure.get(idx);

            Real dx = Real.of((rand.nextDouble() - 0.5) * 2.0);
            Real dy = Real.of((rand.nextDouble() - 0.5) * 2.0);
            Real dz = Real.of((rand.nextDouble() - 0.5) * 2.0);
            Point3DReal newPos = new Point3DReal(originalPos.x.add(dx), originalPos.y.add(dy), originalPos.z.add(dz));

            structure.set(idx, newPos);
            Real newEnergy = calculateEnergyReal(structure);

            if (newEnergy.doubleValue() < currentEnergy.doubleValue()
                    || Math.exp(-(newEnergy.subtract(currentEnergy).doubleValue()) / temperature) > rand.nextDouble()) {
                currentEnergy = newEnergy;
            } else {
                structure.set(idx, originalPos);
            }
        }

        this.foldedStructureReal = structure;
        this.finalEnergy = currentEnergy.doubleValue();
    }

    private void runPrimitive() {
        List<Point3D> structure = new ArrayList<>();
        Random rand = new Random();

        // Initial linear structure if none exists
        if (foldedStructure == null) {
            for (int i = 0; i < sequence.length(); i++) {
                structure.add(new Point3D(i * 3.4, 0, 0));
            }
        } else {
            structure.addAll(foldedStructure);
        }

        double currentEnergy = calculateEnergy(structure);

        for (int i = 0; i < iterations; i++) {
            int idx = rand.nextInt(sequence.length());
            Point3D originalPos = structure.get(idx);

            double dx = (rand.nextDouble() - 0.5) * 2.0;
            double dy = (rand.nextDouble() - 0.5) * 2.0;
            double dz = (rand.nextDouble() - 0.5) * 2.0;
            Point3D newPos = new Point3D(originalPos.x + dx, originalPos.y + dy, originalPos.z + dz);

            structure.set(idx, newPos);
            double newEnergy = calculateEnergy(structure);

            if (newEnergy < currentEnergy || Math.exp(-(newEnergy - currentEnergy) / temperature) > rand.nextDouble()) {
                currentEnergy = newEnergy;
            } else {
                structure.set(idx, originalPos);
            }
        }

        this.foldedStructure = structure;
        this.finalEnergy = currentEnergy;
    }

    private Real calculateEnergyReal(List<Point3DReal> points) {
        Real energy = Real.of(0);
        Real idealDist = Real.of(3.4);
        Real k = Real.of(10.0);

        for (int i = 0; i < points.size() - 1; i++) {
            Real dist = points.get(i).distance(points.get(i + 1));
            energy = energy.add(k.multiply(dist.subtract(idealDist).pow(2)));
        }

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 2; j < points.size(); j++) {
                Real dist = points.get(i).distance(points.get(j));
                if (dist.doubleValue() < 1.0)
                    energy = energy.add(Real.of(1000));
                else if (dist.doubleValue() < 6.0 && isPair(sequence.charAt(i), sequence.charAt(j)))
                    energy = energy.subtract(Real.of(10.0).divide(dist));
            }
        }
        return energy;
    }

    private double calculateEnergy(List<Point3D> points) {
        double energy = 0;
        double idealDist = 3.4;
        double k = 10.0;

        for (int i = 0; i < points.size() - 1; i++) {
            double dist = points.get(i).distance(points.get(i + 1));
            energy += k * Math.pow(dist - idealDist, 2);
        }

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 2; j < points.size(); j++) {
                double dist = points.get(i).distance(points.get(j));
                if (dist < 1.0)
                    energy += 1000;
                else if (dist < 6.0 && isPair(sequence.charAt(i), sequence.charAt(j)))
                    energy -= 10.0 / dist;
            }
        }
        return energy;
    }

    private boolean isPair(char b1, char b2) {
        return (b1 == 'A' && b2 == 'T') || (b1 == 'T' && b2 == 'A') || (b1 == 'C' && b2 == 'G')
                || (b1 == 'G' && b2 == 'C');
    }

    public List<Point3D> getFoldedStructure() {
        return foldedStructure;
    }

    public double getFinalEnergy() {
        return finalEnergy;
    }

    public String getSequence() {
        return sequence;
    }

    public void updateState(List<Point3D> newStructure, double newEnergy) {
        this.foldedStructure = newStructure;
        this.finalEnergy = newEnergy;
    }

    public record Point3D(double x, double y, double z) implements Serializable {
        public double distance(Point3D other) {
            double dx = x - other.x;
            double dy = y - other.y;
            double dz = z - other.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    public record Point3DReal(Real x, Real y, Real z) implements Serializable {
        public Real distance(Point3DReal other) {
            Real dx = x.subtract(other.x);
            Real dy = y.subtract(other.y);
            Real dz = z.subtract(other.z);
            return dx.pow(2).add(dy.pow(2)).add(dz.pow(2)).sqrt();
        }
    }
}
