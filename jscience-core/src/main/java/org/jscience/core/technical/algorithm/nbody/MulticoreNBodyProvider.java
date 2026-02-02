/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.nbody;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.NBodyProvider;
import java.util.stream.IntStream;

/**
 * Multicore N-body simulation provider using parallel streams.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class MulticoreNBodyProvider implements NBodyProvider {

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public void computeForces(Real[] positions, Real[] masses, Real[] forces, Real G, Real softening) {
        double[] pos = new double[positions.length];
        double[] mass = new double[masses.length];
        double[] force = new double[forces.length];
        
        for (int i = 0; i < positions.length; i++) pos[i] = positions[i].doubleValue();
        for (int i = 0; i < masses.length; i++) mass[i] = masses[i].doubleValue();
        
        computeForces(pos, mass, force, G.doubleValue(), softening.doubleValue());
        
        for (int i = 0; i < forces.length; i++) forces[i] = Real.of(force[i]);
    }

    @Override
    public void computeForces(double[] positions, double[] masses, double[] forces, double G, double softening) {
        int n = masses.length;
        
        // Zero forces
        for (int i = 0; i < forces.length; i++) {
            forces[i] = 0.0;
        }
        
        // Parallel O(N²) force computation
        IntStream.range(0, n).parallel().forEach(i -> {
            int idx_i = i * 3;
            double xi = positions[idx_i];
            double yi = positions[idx_i + 1];
            double zi = positions[idx_i + 2];
            double mi = masses[i];
            
            double fx = 0, fy = 0, fz = 0;
            
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                
                int idx_j = j * 3;
                double xj = positions[idx_j];
                double yj = positions[idx_j + 1];
                double zj = positions[idx_j + 2];
                double mj = masses[j];
                
                double dx = xj - xi;
                double dy = yj - yi;
                double dz = zj - zi;
                double r2 = dx * dx + dy * dy + dz * dz + softening * softening;
                double r3 = r2 * Math.sqrt(r2);
                
                double f = G * mj / r3;
                fx += f * dx;
                fy += f * dy;
                fz += f * dz;
            }
            
            forces[idx_i] = fx * mi;
            forces[idx_i + 1] = fy * mi;
            forces[idx_i + 2] = fz * mi;
        });
    }

    @Override
    public String getName() {
        return "Multicore N-Body (CPU)";
    }
}
