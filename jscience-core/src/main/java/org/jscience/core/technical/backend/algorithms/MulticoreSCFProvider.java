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

package org.jscience.core.technical.backend.algorithms;

import java.util.stream.IntStream;

/**
 * Multicore implementation of SCF algorithms.
 * Calculates Form Matrix components in parallel.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MulticoreSCFProvider implements SCFProvider {

    @Override
    public String getName() {
        return "Multicore SCF (CPU)";
    }

    /**
     * Compute F = H + G
     * G_uv = sum_ls P_ls * ( (uv|ls) - 0.5 * (ul|vs) )
     */
    @Override
    public void computeFockMatrix(double[] P, double[] H, double[] ERIs, double[] F, int n) {
        // Initialize F with H (Core Hamiltonian)
        // Since F is output array, we overwrite it.
        // H is read-only.
        System.arraycopy(H, 0, F, 0, n * n);
        
        // Shared array for F updates.
        // Since we parallelize over 'u', and write to F[u*n + v], 
        // each thread writes to a distinct slice of F (rows).
        // No synchronization needed for F write.
        
        IntStream.range(0, n).parallel().forEach(u -> {
           for (int v = 0; v < n; v++) {
               double g_uv = 0.0;
               
               for (int l = 0; l < n; l++) {
                   for (int s = 0; s < n; s++) {
                       double p_ls = P[l * n + s];
                       
                       // Indices for ERIs (chemists notation)
                       // (uv|ls) -> index(u, v, l, s)
                       // (ul|vs) -> index(u, l, v, s)
                       
                       // Naive indexing u*n^3 + v*n^2 + l*n + s
                       int idx_uvls = u*n*n*n + v*n*n + l*n + s;
                       int idx_ulvs = u*n*n*n + l*n*n + v*n + s;
                       
                       double eri_coulomb = ERIs[idx_uvls];
                       double eri_exchange = ERIs[idx_ulvs];
                       
                       g_uv += p_ls * (eri_coulomb - 0.5 * eri_exchange);
                   }
               }
               
               // Atomic F update not needed as we own F[u*n + v]
               F[u * n + v] += g_uv;
           } 
        });
    }
}
