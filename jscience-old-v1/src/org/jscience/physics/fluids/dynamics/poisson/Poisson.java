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

package org.jscience.physics.fluids.dynamics.poisson;

import org.jscience.physics.fluids.dynamics.CompiledData;
import org.jscience.physics.fluids.dynamics.KernelADFC;
import org.jscience.physics.fluids.dynamics.solver.GCCholeskyImpl;
import org.jscience.physics.fluids.dynamics.util.Matrix;


/**
 * Solver of the Poisson ecuation by finite elements of first or second
 * order. Resolves the Poisson ecuation without the minus sign!.
 *
 * @author Alejandro "balrog" Rodriguez Gallego
 * @version 2.0 (2/07/01)
 */
public class Poisson {
    /** DOCUMENT ME! */
    private static double DELTA_CONVERGENCE = 0.0000001;

    /** DOCUMENT ME! */
    private KernelADFC kernel;

    /** DOCUMENT ME! */
    private boolean VERBOSE;

    /** indixes of the contour nodes */
    private int[] nodesDirichlet;

    /** DOCUMENT ME! */
    private double[] valuesDirichlet;

    /** column vector of independent terms */
    private double[] b;

    /** column vector of independent terms */
    private double[] f;

    /** DOCUMENT ME! */
    private GCCholeskyImpl gc;

    /** Meshing */
    private Matrix rigidity;

    /** Meshing */
    private Matrix masses;

/**
     * Creates a new Poisson object.
     *
     * @param kadfc    DOCUMENT ME!
     * @param rig      DOCUMENT ME!
     * @param mas      DOCUMENT ME!
     * @param nodesDir DOCUMENT ME!
     * @param valDir   DOCUMENT ME!
     */
    public Poisson(KernelADFC kadfc, Matrix rig, Matrix mas, int[] nodesDir,
        double[] valDir) {
        kernel = kadfc;
        VERBOSE = CompiledData.VERBOSE;
        DELTA_CONVERGENCE = kadfc.getConfiguration().deltaConvergencePoisson;

        nodesDirichlet = nodesDir;
        valuesDirichlet = valDir;
        rigidity = rig;
        masses = mas;
        f = null;
        b = new double[rigidity.getRows()];
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void calculate(double[] r) {
        // Cronometer
        long startTime = 0;

        if (VERBOSE) {
            startTime = System.currentTimeMillis();
        }

        if (f != null) {
            b = masses.multiply(b, f);
        }

        for (int j = 0; j < b.length; j++)
            b[j] = -b[j];

        if (gc == null) {
            gc = new GCCholeskyImpl(kernel);

            gc.setB(b);
            gc.setDirichlet(nodesDirichlet, valuesDirichlet);
            gc.setCoeficientMatrix(rigidity);
            gc.setDeltaConvergence(DELTA_CONVERGENCE);
        }

        gc.setB(b);
        gc.resolve(r);

        if (VERBOSE) {
            System.out.println("Poisson: done in " +
                ((System.currentTimeMillis() - startTime) / 1000.0) +
                " seconds.");
        }
    }

    // Functions set*
    /**
     * DOCUMENT ME!
     *
     * @param farg DOCUMENT ME!
     */
    public void setF(double[] farg) {
        f = farg;
    }
}
