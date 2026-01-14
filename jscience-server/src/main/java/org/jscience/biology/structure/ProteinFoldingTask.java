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
 * Protein Folding Simulation Task using the HP (Hydrophobic-Polar) Model.
 * 
 * Simulates protein folding on a 3D lattice using Metropolis Monte Carlo.
 * Hydrophobic monomers (H) attract each other, representing the hydrophobic
 * collapse.
 * 
 * <p>
 * References:
 * <ul>
 * <li>Dill, K. A. (1985). Theory for the folding and stability of globular
 * proteins. Biochemistry, 24(6), 1501-1509. (HP Model)</li>
 * <li>Lau, K. F., & Dill, K. A. (1989). A lattice statistical mechanics model
 * of the conformational and sequence spaces of proteins. Macromolecules,
 * 22(10), 3986-3997.</li>
 * </ul>
 * </p>
 */
public class ProteinFoldingTask implements Serializable {

    public enum ResidueType {
        HYDROPHOBIC('H'), POLAR('P');

        final char code;

        ResidueType(char c) {
            this.code = c;
        }
    }

    public record Monomer(int x, int y, int z, ResidueType type) implements Serializable {
    }

    private final List<ResidueType> sequence;
    private final int iterations;
    private final double temperature;
    private PrecisionMode mode = PrecisionMode.PRIMITIVES;

    private List<Monomer> currentFold;
    private double energy;
    private Real energyReal;

    public ProteinFoldingTask(String hpSequence, int iterations, double temperature) {
        this.sequence = new ArrayList<>();
        for (char c : hpSequence.toUpperCase().toCharArray()) {
            sequence.add(c == 'P' ? ResidueType.POLAR : ResidueType.HYDROPHOBIC);
        }
        this.iterations = iterations;
        this.temperature = temperature;
    }

    public void setMode(PrecisionMode mode) {
        this.mode = mode;
    }

    public void run() {
        if (currentFold == null)
            initializeLinear();

        Random rand = new Random();
        for (int i = 0; i < iterations; i++) {
            attemptMove(rand);
        }

        if (mode == PrecisionMode.REALS) {
            this.energyReal = calculateEnergyReal(currentFold);
            this.energy = energyReal.doubleValue();
        } else {
            this.energy = calculateEnergy(currentFold);
        }
    }

    private void initializeLinear() {
        currentFold = new ArrayList<>();
        for (int i = 0; i < sequence.size(); i++) {
            currentFold.add(new Monomer(i, 0, 0, sequence.get(i)));
        }
        energy = calculateEnergy(currentFold);
    }

    private void attemptMove(Random rand) {
        int idx = rand.nextInt(sequence.size());
        Monomer oldM = currentFold.get(idx);

        // Pivot move or end-flip
        int dx = rand.nextInt(3) - 1;
        int dy = rand.nextInt(3) - 1;
        int dz = rand.nextInt(3) - 1;

        Monomer newM = new Monomer(oldM.x + dx, oldM.y + dy, oldM.z + dz, oldM.type);

        if (isValid(newM, idx)) {
            List<Monomer> nextFold = new ArrayList<>(currentFold);
            nextFold.set(idx, newM);
            double nextEnergy = calculateEnergy(nextFold);

            if (nextEnergy < energy || Math.exp(-(nextEnergy - energy) / temperature) > rand.nextDouble()) {
                currentFold = nextFold;
                energy = nextEnergy;
            }
        }
    }

    private boolean isValid(Monomer m, int idx) {
        // Self-avoidance
        for (int i = 0; i < currentFold.size(); i++) {
            if (i != idx) {
                Monomer other = currentFold.get(i);
                if (other.x == m.x && other.y == m.y && other.z == m.z)
                    return false;
            }
        }
        // Connectivity check
        if (idx > 0) {
            Monomer prev = currentFold.get(idx - 1);
            if (Math.abs(prev.x - m.x) + Math.abs(prev.y - m.y) + Math.abs(prev.z - m.z) != 1)
                return false;
        }
        if (idx < currentFold.size() - 1) {
            Monomer next = currentFold.get(idx + 1);
            if (Math.abs(next.x - m.x) + Math.abs(next.y - m.y) + Math.abs(next.z - m.z) != 1)
                return false;
        }
        return true;
    }

    private Real calculateEnergyReal(List<Monomer> fold) {
        Real e = Real.of(0);
        // HP Model: -1 for each topological H-H contact (non-sequential neighbors)
        for (int i = 0; i < fold.size(); i++) {
            for (int j = i + 2; j < fold.size(); j++) {
                Monomer m1 = fold.get(i);
                Monomer m2 = fold.get(j);
                if (m1.type == ResidueType.HYDROPHOBIC && m2.type == ResidueType.HYDROPHOBIC) {
                    int dist = Math.abs(m1.x - m2.x) + Math.abs(m1.y - m2.y) + Math.abs(m1.z - m2.z);
                    if (dist == 1)
                        e = e.subtract(Real.of(1.0));
                }
            }
        }
        return e;
    }

    private double calculateEnergy(List<Monomer> fold) {
        double e = 0;
        // HP Model: -1 for each topological H-H contact (non-sequential neighbors)
        for (int i = 0; i < fold.size(); i++) {
            for (int j = i + 2; j < fold.size(); j++) {
                Monomer m1 = fold.get(i);
                Monomer m2 = fold.get(j);
                if (m1.type == ResidueType.HYDROPHOBIC && m2.type == ResidueType.HYDROPHOBIC) {
                    int dist = Math.abs(m1.x - m2.x) + Math.abs(m1.y - m2.y) + Math.abs(m1.z - m2.z);
                    if (dist == 1)
                        e -= 1.0;
                }
            }
        }
        return e;
    }

    public List<Monomer> getResult() {
        return currentFold;
    }
}
