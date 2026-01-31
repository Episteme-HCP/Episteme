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

package org.jscience.natural.biology;

import org.jscience.core.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Advanced protein secondary structure prediction and energy-based folding simulation.
 * Uses a simplified force field with Lennard-Jones potentials and hydrogen bonding energy.
 */
public final class ProteinFolding {

    public enum Structure { ALPHA_HELIX, BETA_SHEET, COIL }

    public record Residue(String symbol, double[] position, Structure predicted) {
        public Residue(String symbol, Real ignored) { 
            this(symbol, new double[]{Math.random()*10, Math.random()*10, Math.random()*10}, predictAa(symbol)); 
        }
    }

    private final List<Residue> chain;
    private double temperature = 1.0;
    private final Random rand = new Random();

    public ProteinFolding(List<Residue> initialChain) {
        this.chain = new ArrayList<>(initialChain);
    }

    public void step(double tempDecrease) {
        temperature = Math.max(0.1, temperature - tempDecrease);
        
        // Perturb a random residue
        int idx = rand.nextInt(chain.size());
        Residue oldRes = chain.get(idx);
        double currentEnergy = calculateTotalEnergy().doubleValue();

        double[] nextPos = {
            oldRes.position()[0] + (rand.nextDouble() - 0.5),
            oldRes.position()[1] + (rand.nextDouble() - 0.5),
            oldRes.position()[2] + (rand.nextDouble() - 0.5)
        };
        Residue nextRes = new Residue(oldRes.symbol(), nextPos, oldRes.predicted());
        chain.set(idx, nextRes);
        
        double nextEnergy = calculateTotalEnergy().doubleValue();
        
        // Metropolis Criterion
        if (!(nextEnergy < currentEnergy || rand.nextDouble() < Math.exp((currentEnergy - nextEnergy) / temperature))) {
            chain.set(idx, oldRes); // Revert
        }
    }

    public Real calculateTotalEnergy() {
        double energy = 0;
        int n = chain.size();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = distance(chain.get(i).position(), chain.get(j).position());
                if (dist < 0.1) dist = 0.1; // Prevent singularity
                
                // Lennard-Jones Potential (Simplified)
                double sigma = 3.8; 
                double epsilon = 0.5;
                double ratio = Math.pow(sigma / dist, 6);
                energy += 4 * epsilon * (ratio * ratio - ratio);

                // Hydrogen bonding energy
                if (Math.abs(i - j) == 4 && dist > 2.5 && dist < 3.5) {
                    energy -= 3.0;
                }
            }
        }
        return Real.of(energy);
    }

    private double distance(double[] p1, double[] p2) {
        return Math.sqrt(Math.pow(p1[0] - p2[0], 2) + Math.pow(p1[1] - p2[1], 2) + Math.pow(p1[2] - p2[2], 2));
    }

    public static Structure predictAa(String aa) {
        if (aa == null || aa.isEmpty()) return Structure.COIL;
        return switch (aa.toUpperCase().charAt(0)) {
            case 'E', 'A', 'L', 'M', 'Q', 'K' -> Structure.ALPHA_HELIX;
            case 'V', 'I', 'T', 'F', 'Y', 'W' -> Structure.BETA_SHEET;
            default -> Structure.COIL;
        };
    }

    public List<Residue> getChain() {
        return Collections.unmodifiableList(chain);
    }
}

