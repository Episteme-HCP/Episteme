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
 * In a real scenario, this would distribute Monte Carlo trials across the grid.
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
        // Simplified Hash-based protein folding simulation (HP model variant for DNA)
        // Hydrophobic (H) and Polar (P) model is typical for proteins,
        // for DNA we'll use a simplified elastic model for demo purposes.

        List<Point3D> structure = new ArrayList<>();
        Random rand = new Random();

        // Initial linear structure
        for (int i = 0; i < sequence.length(); i++) {
            structure.add(new Point3D(i * 3.4, 0, 0)); // 3.4nm spacing
        }

        double currentEnergy = calculateEnergy(structure);

        for (int i = 0; i < iterations; i++) {
            // Pick random residue to move
            int idx = rand.nextInt(sequence.length());
            Point3D originalPos = structure.get(idx);

            // Random small move
            double dx = (rand.nextDouble() - 0.5) * 2.0;
            double dy = (rand.nextDouble() - 0.5) * 2.0;
            double dz = (rand.nextDouble() - 0.5) * 2.0;
            Point3D newPos = new Point3D(originalPos.x + dx, originalPos.y + dy, originalPos.z + dz);

            structure.set(idx, newPos);

            double newEnergy = calculateEnergy(structure);

            // Metropolis criterion
            if (newEnergy < currentEnergy ||
                    Math.exp(-(newEnergy - currentEnergy) / temperature) > rand.nextDouble()) {
                currentEnergy = newEnergy; // Accept
            } else {
                structure.set(idx, originalPos); // Reject
            }
        }

        this.foldedStructure = structure;
        this.finalEnergy = currentEnergy;
    }

    private double calculateEnergy(List<Point3D> points) {
        double energy = 0;

        // Bond length constraints (harmonic potential)
        double idealDist = 3.4;
        double k = 10.0;

        for (int i = 0; i < points.size() - 1; i++) {
            double dist = points.get(i).distance(points.get(i + 1));
            energy += k * Math.pow(dist - idealDist, 2);
        }

        // Non-bonded interactions (Lennard-Jones like)
        // A-T and C-G attraction
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 2; j < points.size(); j++) {
                double dist = points.get(i).distance(points.get(j));
                if (dist < 1.0) {
                    energy += 1000; // Clash penalty
                } else if (dist < 6.0) {
                    char b1 = sequence.charAt(i);
                    char b2 = sequence.charAt(j);
                    if (isPair(b1, b2)) {
                        energy -= 10.0 / dist; // Attraction
                    }
                }
            }
        }

        return energy;
    }

    private boolean isPair(char b1, char b2) {
        return (b1 == 'A' && b2 == 'T') || (b1 == 'T' && b2 == 'A') ||
                (b1 == 'C' && b2 == 'G') || (b1 == 'G' && b2 == 'C');
    }

    public List<Point3D> getFoldedStructure() {
        return foldedStructure;
    }

    public double getFinalEnergy() {
        return finalEnergy;
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


