/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.physics.quantum.scf.providers;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.physics.quantum.scf.SCFProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import java.util.stream.IntStream;

/**
 * Multicore implementation of SCF algorithms.
 * Calculates Form Matrix components in parallel.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class MulticoreSCFProvider implements SCFProvider {

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public void computeFockMatrix(double[] P, double[] H, double[] ERIs, double[] F, int n) {
        System.arraycopy(H, 0, F, 0, n * n);
        
        IntStream.range(0, n).parallel().forEach(u -> {
           for (int v = 0; v < n; v++) {
               double g_uv = 0.0;
               for (int l = 0; l < n; l++) {
                   for (int s = 0; s < n; s++) {
                       double p_ls = P[l * n + s];
                       int idx_uvls = u*n*n*n + v*n*n + l*n + s;
                       int idx_ulvs = u*n*n*n + l*n*n + v*n + s;
                       double eri_coulomb = ERIs[idx_uvls];
                       double eri_exchange = ERIs[idx_ulvs];
                       g_uv += p_ls * (eri_coulomb - 0.5 * eri_exchange);
                   }
               }
               F[u * n + v] += g_uv;
           } 
        });
    }

    @Override
    public void computeFockMatrixReal(Real[] P, Real[] H, Real[] ERIs, Real[] F, int n) {
        System.arraycopy(H, 0, F, 0, n * n);

        IntStream.range(0, n).parallel().forEach(u -> {
           for (int v = 0; v < n; v++) {
               Real g_uv = Real.ZERO;
               for (int l = 0; l < n; l++) {
                   for (int s = 0; s < n; s++) {
                       Real p_ls = P[l * n + s];
                       int idx_uvls = u*n*n*n + v*n*n + l*n + s;
                       int idx_ulvs = u*n*n*n + l*n*n + v*n + s;
                       Real eri_coulomb = ERIs[idx_uvls];
                       Real eri_exchange = ERIs[idx_ulvs];
                       
                       // g_uv += p_ls * (eri_coulomb - 0.5 * eri_exchange)
                       Real contribution = eri_coulomb.subtract(eri_exchange.multiply(Real.of(0.5))).multiply(p_ls);
                       g_uv = g_uv.add(contribution);
                   }
               }
               F[u * n + v] = F[u * n + v].add(g_uv);
           } 
        });
    }

    @Override
    public String getName() {
        return "Multicore SCF (CPU)";
    }
}
