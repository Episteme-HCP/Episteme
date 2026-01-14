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
