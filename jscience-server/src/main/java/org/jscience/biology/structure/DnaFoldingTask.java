/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

    // Result
    private List<Point3D> foldedStructure;
    private double finalEnergy;

    public DnaFoldingTask(String sequence, int iterations, double temperature) {
        this.sequence = sequence;
        this.iterations = iterations;
        this.temperature = temperature;
    }

    public void run() {
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
}
