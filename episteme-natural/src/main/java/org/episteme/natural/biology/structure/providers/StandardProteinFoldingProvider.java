/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.biology.structure.providers;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.biology.structure.ProteinFoldingProvider;
import java.util.Random;

/**
 * Standard implementation of HP model folding.
 */
public class StandardProteinFoldingProvider implements ProteinFoldingProvider {

    @Override
    public Real calculateEnergy(int[][] positions, boolean[] isH) {
        return Real.of(calculateEnergyDouble(positions, isH));
    }

    @Override
    public double calculateEnergyDouble(int[][] positions, boolean[] isH) {
        double e = 0;
        int n = isH.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 2; j < n; j++) {
                if (isH[i] && isH[j]) {
                    int dist = Math.abs(positions[i][0] - positions[j][0]) +
                               Math.abs(positions[i][1] - positions[j][1]) +
                               Math.abs(positions[i][2] - positions[j][2]);
                    if (dist == 1) e -= 1.0;
                }
            }
        }
        return e;
    }

    @Override
    public float calculateEnergyFloat(int[][] positions, boolean[] isH) {
        return (float) calculateEnergyDouble(positions, isH);
    }

    @Override
    public void simulate(int[][] pos, boolean[] isH, int iterations, double temp, long seed) {
        Random rand = new Random(seed);
        int n = isH.length;
        double energy = calculateEnergyDouble(pos, isH);

        for (int i = 0; i < iterations; i++) {
            int idx = rand.nextInt(n);
            int ox = pos[idx][0], oy = pos[idx][1], oz = pos[idx][2];

            pos[idx][0] += rand.nextInt(3) - 1;
            pos[idx][1] += rand.nextInt(3) - 1;
            pos[idx][2] += rand.nextInt(3) - 1;

            if (isValid(pos, idx)) {
                double nextE = calculateEnergyDouble(pos, isH);
                if (nextE < energy || Math.exp(-(nextE - energy) / temp) > rand.nextDouble()) {
                    energy = nextE;
                } else {
                    pos[idx][0] = ox; pos[idx][1] = oy; pos[idx][2] = oz;
                }
            } else {
                pos[idx][0] = ox; pos[idx][1] = oy; pos[idx][2] = oz;
            }
        }
    }

    private boolean isValid(int[][] pos, int idx) {
        int n = pos.length;
        // Self-avoidance
        for (int i = 0; i < n; i++) {
            if (i != idx && pos[i][0] == pos[idx][0] && pos[i][1] == pos[idx][1] && pos[i][2] == pos[idx][2])
                return false;
        }
        // Connectivity
        if (idx > 0) {
            int dist = Math.abs(pos[idx-1][0] - pos[idx][0]) + Math.abs(pos[idx-1][1] - pos[idx][1]) + Math.abs(pos[idx-1][2] - pos[idx][2]);
            if (dist != 1) return false;
        }
        if (idx < n - 1) {
            int dist = Math.abs(pos[idx+1][0] - pos[idx][0]) + Math.abs(pos[idx+1][1] - pos[idx][1]) + Math.abs(pos[idx+1][2] - pos[idx][2]);
            if (dist != 1) return false;
        }
        return true;
    }

    @Override
    public String getName() { return "Standard HP Folding"; }
    @Override
    public String getAlgorithmType() { return "Biology"; }
}
