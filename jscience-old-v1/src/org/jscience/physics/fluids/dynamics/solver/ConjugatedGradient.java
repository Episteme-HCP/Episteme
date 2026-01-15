/*
 * Program : ADFC 2
 * Class : org.jscience.fluids.solver.GradienteConjugado
 *         Common interface for all solvers of the GC family.
 * Author : Alejandro "balrog" Rodriguez Gallego
 * Date  : 26/07/2001
 */
package org.jscience.physics.fluids.dynamics.solver;


// import java.rmi.*;
import org.jscience.physics.fluids.dynamics.util.Matrix;


/**
 * This interface is responsible of the paralelization process and
 * both remote and local invocations of the solver. <p>
 * <p/>
 * For a detailed description of ech of the methods,
 * check <code>GradienteConjugadoImpl</code>. <p>
 *
 * @author Alejandro Rodriguez Gallego
 * @version 0.3
 * @see GradienteConjugadoImpl
 */
public interface ConjugatedGradient {
    /**
     * DOCUMENT ME!
     *
     * @param xk DOCUMENT ME!
     */
    public void resolve(double[] xk); //throws RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getB(); //throws RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDeltaConvergence(); //throws RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     */
    public void setB(double[] v); //throws RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @param delta DOCUMENT ME!
     */
    public void setDeltaConvergence(double delta); //throws RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @param nodes DOCUMENT ME!
     * @param values DOCUMENT ME!
     */
    public void setDirichlet(int[] nodes, double[] values); //throws RemoteException;

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void setCoeficientMatrix(Matrix m); //throws RemoteException;
}
