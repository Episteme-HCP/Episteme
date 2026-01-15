/*
 * Program : A�DFC 2.0
 * Class : org.jscience.fluids.poisson.Poisson
 *         Resuelve ec. Laplace/Poisson.
 * Author : Alejandro "balrog" Rodriguez Gallego
 * Date  : 2/07/2001
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
