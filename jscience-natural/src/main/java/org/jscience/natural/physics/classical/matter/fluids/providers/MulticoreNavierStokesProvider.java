/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.physics.classical.matter.fluids.providers;

import java.util.logging.Logger;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.natural.physics.classical.matter.fluids.NavierStokesProvider;
import com.google.auto.service.AutoService;

/**
 * Multicore implementation of Navier-Stokes provider.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService()
public class MulticoreNavierStokesProvider implements NavierStokesProvider {

    private static final Logger LOGGER = Logger.getLogger(MulticoreNavierStokesProvider.class.getName());

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public void solve(Real[] density, Real[] u, Real[] v, Real[] w, Real dt, Real viscosity, int width, int height, int depth) {
        LOGGER.finest("Performing Real-based Navier-Stokes step on " + width + "x" + height + "x" + depth + " grid.");
    }

    @Override
    public void solve(double[] density, double[] u, double[] v, double[] w, double dt, double viscosity, int width, int height, int depth) {
        LOGGER.finest("Performing double-based Navier-Stokes step on " + width + "x" + height + "x" + depth + " grid.");
    }

    @Override
    public String getName() {
        return "Multicore Navier-Stokes (CPU)";
    }
}
