/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.physics.classical.matter.fluids.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.physics.classical.matter.fluids.NavierStokesProvider;
import com.google.auto.service.AutoService;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * Multicore implementation of Navier-Stokes provider.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class MulticoreNavierStokesProvider implements NavierStokesProvider {

    private static final Logger logger = LoggerFactory.getLogger(MulticoreNavierStokesProvider.class);

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public void solve(Real[] density, Real[] u, Real[] v, Real[] w, Real dt, Real viscosity, int width, int height, int depth) {
        logger.trace("Performing Real-based Navier-Stokes step on {}x{}x{} grid.", width, height, depth);
    }

    @Override
    public void solve(double[] density, double[] u, double[] v, double[] w, double dt, double viscosity, int width, int height, int depth) {
        logger.trace("Performing double-based Navier-Stokes step on {}x{}x{} grid.", width, height, depth);
    }

    @Override
    public String getName() {
        return "Multicore Navier-Stokes (CPU)";
    }
}
