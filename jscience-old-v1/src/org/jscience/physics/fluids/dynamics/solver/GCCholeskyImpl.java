/*
 * Program : ADFC 2
 * Class : org.jscience.fluids.solver.GCCholeskyImpl.java
 *         Gradiente Conjugado Algebraico, preconditioningr of Cholesky Incompleto.
 * Author : Alejandro "balrog" Rodriguez Gallego
 * Date  : 27/07/2001
 */
package org.jscience.physics.fluids.dynamics.solver;

import org.jscience.physics.fluids.dynamics.KernelADFC;
import org.jscience.physics.fluids.dynamics.KernelADFCConfiguration;
import org.jscience.physics.fluids.dynamics.util.Matrix;


/**
 * Solver for systems of lineal ecuations through the <I>conjugated
 * gradient</I>. The used preconditioner is a incomplete Cholesky. For more
 * information about <I>conjugated gradient</I> and <I>sparse matrices</I>
 * consult the program documentation.
 * <p/>
 * <p/>
 * The most important changes are the following:<BR> - More clarity in the
 * methods. Style setter/getter.<BR> - Main code is out of the
 * constructor.<BR> - Simetrical data storage.<BR> - Possibility to reuse the
 * coeficient matrix.<BR> - NaN in the convergence criteria resturns a system
 * null (needed for the <CODE>NavierStokes</CODE>)<BR> - Preconditioning
 * improved.<BR> - Revised for <B>ADFC v2.0</B>.
 * </p>
 *
 * @author Alejandro "Balrog" Rodriguez Gallego
 * @version 2.0
 * @see ConjugatedGradient
 */
public class GCCholeskyImpl implements ConjugatedGradient {
    /**
     * Version of the class.
     */
    public static final String VERSION = "2.0.java";

    /**
     * DOCUMENT ME!
     */
    private static final int MAX_ITER = 500;

    /**
     * DOCUMENT ME!
     */
    private KernelADFC kernel;

    /**
     * DOCUMENT ME!
     */
    private double deltaConvergence = 0.0000001;

    /**
     * DOCUMENT ME!
     */
    private boolean VERBOSE;

    /**
     * DOCUMENT ME!
     */
    private int totalRows;

    /**
     * DOCUMENT ME!
     */
    private double[] b;

    /**
     * DOCUMENT ME!
     */
    private double[] yRem;

    /**
     * DOCUMENT ME!
     */
    private int[] nodesDirichlet;

    /**
     * DOCUMENT ME!
     */
    private double[] valuesDirichlet;

    // Data used in resolve(),
    // placed here to reduce the loads of the
    // Garbage Collector. Reserved only
    // once in the execution of the program,
    // or each time the matrix
    // of coeficientes of the system is changed.

    /**
     * DOCUMENT ME!
     */
    private double[] Ap;

    // Data used in resolve(),
    // placed here to reduce the loads of the
    // Garbage Collector. Reserved only
    // once in the execution of the program,
    // or each time the matrix
    // of coeficientes of the system is changed.

    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    private double[] p;

    // Data used in resolve(),
    // placed here to reduce the loads of the
    // Garbage Collector. Reserved only
    // once in the execution of the program,
    // or each time the matrix
    // of coeficientes of the system is changed.

    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    private double[] rk;

    // Data used in resolve(),
    // placed here to reduce the loads of the
    // Garbage Collector. Reserved only
    // once in the execution of the program,
    // or each time the matrix
    // of coeficientes of the system is changed.

    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    private double[] sk;

    // Data used in resolve(),
    // placed here to reduce the loads of the
    // Garbage Collector. Reserved only
    // once in the execution of the program,
    // or each time the matrix
    // of coeficientes of the system is changed.

    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    private double[] bAntiguo;

    // Data used in resolve(),
    // placed here to reduce the loads of the
    // Garbage Collector. Reserved only
    // once in the execution of the program,
    // or each time the matrix
    // of coeficientes of the system is changed.

    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    private double[] uu;

    // Data used in resolve(),
    // placed here to reduce the loads of the
    // Garbage Collector. Reserved only
    // once in the execution of the program,
    // or each time the matrix
    // of coeficientes of the system is changed.

    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    private double[] w;

    /**
     * DOCUMENT ME!
     */
    private Matrix A;

    /**
     * DOCUMENT ME!
     */
    private Matrix L;

    /**
     * Default Constructor of the class.
     *
     * @param kadfc DOCUMENT ME!
     */
    public GCCholeskyImpl(KernelADFC kadfc) {
        super();

        // Get kernel configuration
        kernel = kadfc;

        KernelADFCConfiguration c = kernel.getConfiguration();
        deltaConvergence = c.deltaConjugatedGradientConvergence;

        //    VERBOSE = true;
        L = null;
        Ap = p = rk = sk = bAntiguo = uu = w = null;

        if (VERBOSE) {
            System.out.println("GCCholeskyImpl " + VERSION);
        }
    }

    /**
     * Preconditioning. In principle, this is a IncompleteCholesky
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     */
    private void multiplyByInverseMatrix(double[] a, double[] b) {
        double sum;

        // We have to solve: a = InvM * b
        // b = M * a
        // M = L*Lt
        // First
        // b = L*y   (obtain y)
        for (int j = 0; j < totalRows; j++)
            yRem[j] = 0.0;

        for (int i = 0; i < totalRows; i++) {
            sum = b[i];

            //for(int j= 0; j<i;  j++)
            //  sum -= yRem[j]*L.leeElement(i,j);
            for (int nk = L.ipos[i] + 1; nk < L.ipos[i + 1]; nk++)

                // if(L.jpos[nk] < i)
                sum -= (yRem[L.jpos[nk]] * L.elem[nk]);

            yRem[i] = sum / L.elem[L.ipos[i]];
        }

        // Secondly, experimental method
        for (int i = totalRows - 1; i >= 0; i--) {
            int n1 = L.ipos[i];
            int n2 = L.ipos[i + 1];

            yRem[i] = yRem[i] / L.elem[n1];

            for (int j = n1 + 1; j < n2; j++) {
                int p = L.jpos[j];
                yRem[p] = yRem[p] - (yRem[i] * L.elem[j]);
            }
        }

        for (int j = 0; j < a.length; j++)
            a[j] = yRem[j];
    }

    /**
     * Solves the lineal system of ecuations by a preconditioning conjugated
     * gradient.
     *
     * @param xk DOCUMENT ME!
     * @since org.jscience.fluids.solver v0.3
     */
    public void resolve(double[] xk) {
        double a;
        double bb;
        double c;
        double delta = 0;
        double rkrk = 0;
        double rksk = 0;
        totalRows = b.length;

        // Reserving RAM: only done once
        if (Ap == null) {
            if (VERBOSE) {
                System.out.println("GCCholeskyImpl: reserving RAM");
            }

            Ap = new double[totalRows];
            p = new double[totalRows];
            rk = new double[totalRows];
            sk = new double[totalRows];

            // xk = new double[totalFilas];
            bAntiguo = new double[totalRows];
            uu = new double[totalRows];
            w = new double[totalRows];
            yRem = new double[totalRows];
        }

        //System.out.println("GradienteConjugado: iniciando");
        long startTime = System.currentTimeMillis();

        if (L == null) {
            preconditioning();
        }

        // Convert in a homogeneus system,
        // store copy of the independient terms,
        // because we are going to modify b.
        for (int j = 0; j < b.length; j++)
            bAntiguo[j] = b[j];

        // creation of vector uu: contains the value of
        // dirichlet contours no homogeneus and, for the rest,
        // the value is zero.
        for (int j = 0; j < uu.length; j++)
            uu[j] = 0.0;

        if (nodesDirichlet != null) {
            for (int j = 0; j < nodesDirichlet.length; j++)
                uu[nodesDirichlet[j]] = valuesDirichlet[j];
        }

        // multiply by the matrix of the system
        A.multiply(w, uu);

        // Rest to the right side.
        for (int j = 0; j < b.length; j++)
            b[j] = b[j] - w[j];

        /**
         * NORMAL CODE
         */

        // Start with the initial estimation
        for (int i = 0; i < totalRows; i++)
            xk[i] = 0.0;

        // conjugated gradient algorithm
        // r0 = b - A*x0
        //multiplyByA(Ax0, xk);
        for (int j = 0; j < b.length; j++)
            rk[j] = b[j]; //  - Ax0[j];

        // apply contour conditions of Dirichlet (1rst part)
        if (nodesDirichlet != null) {
            for (int cd = 0; cd < nodesDirichlet.length; cd++) {
                xk[nodesDirichlet[cd]] = 0;
                rk[nodesDirichlet[cd]] = 0;
            }
        }

        // bb = b*b   (new criteria of convergence)
        bb = scalarProduct(b, b);

        // s0 = Inv(M) * r0
        // p = s0
        multiplyByInverseMatrix(sk, rk);

        for (int i = 0; i < sk.length; i++)
            p[i] = sk[i];

        // main iteration
        int iter;
        boolean convergence;

        for (iter = 0, convergence = false; !convergence; iter++) {
            // Ap = A*p
            A.multiply(Ap, p);

            // apply conditions of contour Dirichlet (2nd part)
            if (nodesDirichlet != null) {
                for (int cd = 0; cd < nodesDirichlet.length; cd++) {
                    int ndir = nodesDirichlet[cd];
                    Ap[ndir] = 0;
                    xk[ndir] = 0;
                    p[ndir] = 0;
                }
            }

            // a = (rk.sk) / (p . A*p)
            rksk = scalarProduct(rk, sk);

            double pAp = scalarProduct(p, Ap);

            if (pAp == 0) {
                System.out.println("pAp == 0.0 !!!!!!!!!!!!!!");
            }

            a = rksk / pAp;

            // xk1 = xk + a*p
            for (int j = 0; j < totalRows; j++)
                xk[j] += (a * p[j]);

            // Criteria of convergence
            //rkrk = 0.0;
            //for(int j=0; j<totalFilas; j++)
            //rkrk += rk[j]*rk[j];
            rkrk = scalarProduct(rk, rk);

            delta = Math.sqrt(Math.abs(rkrk / bb));

            if (VERBOSE) {
                System.out.println("iter : " + iter + "\tdelta : " + delta +
                        "\ta : " + a);
            }

            if (delta < deltaConvergence) {
                convergence = true;
            }

            if (iter > MAX_ITER) {
                System.out.println("Convergence forzada!");
                convergence = true;
            }

            // Diciembre 2001

            /*
            if(convergence)
            {
                double mmax=-1000, mmin=1000;

                for(int i=0; i<p.length; i++)
                {
                    if(mmin > a*p[i]) mmin = a*p[i];
                    if(mmax < a*p[i]) mmax = a*p[i];
                }

                System.out.println("---> ("+mmin+" /// "+mmax+")");
            }
             */
            if (Double.isNaN(delta)) {
                System.out.println(
                        "NaN in convergence criteria -> null solution #################################");

                for (int j = 0; j < xk.length; j++)
                    xk[j] = 0.0;

                convergence = true;
            }

            // rk1 = rk - a*Ap (reuse rk<-rk1)
            for (int j = 0; j < totalRows; j++)
                rk[j] = rk[j] - (a * Ap[j]);

            // sk1 = Inv(M)*rk1
            multiplyByInverseMatrix(sk, rk);

            // c = (rk1.sk1) / (rk.sk)
            c = scalarProduct(rk, sk) / rksk;

            if (Double.isNaN(c)) {
                System.out.println("c NaN " + rksk);

                System.out.println(
                        "NaN in c -> null solution #################################");

                for (int j = 0; j < xk.length; j++)
                    xk[j] = 0.0;

                convergence = true;
            }

            // p = sk1 + c*p
            for (int i = 0; i < totalRows; i++)
                p[i] = sk[i] + (c * p[i]);
        }

        /**
         * NORMAL CODE
         */

        // Resolve previous modifications
        if (nodesDirichlet != null) {
            for (int j = 0; j < nodesDirichlet.length; j++)
                xk[nodesDirichlet[j]] = valuesDirichlet[j];
        }

        // restore b
        for (int j = 0; j < b.length; j++)
            b[j] = bAntiguo[j];

        // total time
        // if(VERBOSE)
        System.out.println("GCCholesky: done in " +
                ((System.currentTimeMillis() - startTime) / 1000.0) + " sec. " +
                iter + " iter. " + delta + " delta");
    }

    // scalar product: return a*b
    private double scalarProduct(double[] a, double[] b) {
        double pe = 0.0;

        for (int i = 0; i < a.length; i++)
            pe += (a[i] * b[i]);

        return pe;
    }

    // Commands for reading private variables

    /**
     * Returns the current column of independient terms.
     *
     * @return vector of independient terms.
     * @since org.jscience.fluids.solver v0.3
     */
    public double[] getB() {
        return b;
    }

    /**
     * Returns the current delta of convergence of the solver.
     *
     * @return delta of convergence.
     * @since org.jscience.fluids.solver v0.3
     */
    public double getDeltaConvergence() {
        return deltaConvergence;
    }

    // Functions

    /**
     * It carries out all previous tasks needed to solve the current system of
     * ecuations. The preconditioning by <I>Incomplete Cholesky</I> has to do
     * a series of tediose calculus before resolving the system. These
     * calculus are done only once, they can be reused.
     *
     * @since org.jscience.fluids.solver v0.3
     */
    private void preconditioning() {
        double sum;
        double v;
        long startTime = System.currentTimeMillis();

        System.out.println("Conjugated Gradient v2 (LDLt)");

        totalRows = A.ipos.length - 1;
        L = new Matrix(null, A.ipos, A.jpos, false);

        // Case Dirichlet: desingularize the matrix L
        // where we copied A.
        // Copy the matrix to rlt
        for (int i = 0; i < A.elem.length; i++)
            L.elem[i] = A.elem[i];

        if (nodesDirichlet != null) {
            System.out.println("   *  Dirichlet Desingularize ...");

            /*
             for(int j=0; j<nodesDirichlet.length; j++)
            {
                int nd = nodesDirichlet[j];

                // row
                for(int i=L.ipos[nd]; i<L.ipos[nd+1]; i++)
                    L.elem[i] = 0.0;

                // column
                int col;
                for(int f=nd+1; f<totalFilas; f++)
                    for(int i=L.ipos[f]; i<L.ipos[f+1]; i++)
                    {
                        col = L.jpos[i];
                        if(col == nd)
                        {
                            L.elem[i] = 0.0;
                            break; // En is row ya esta. Siguiente.
                        }
                        // rows jpos ordenadas of mayor a menor
                        // con seguridad podemos saltar aqui.
                        else if(col < nd) break;
                    }

                // diagonal
                L.elem[L.ipos[nd]] = 1.0;
            }
             */

            // NEW ALGORITHM
            // create bitmap
            boolean[] isDirichlet = new boolean[L.ipos[totalRows]];

            for (int j = 0; j < nodesDirichlet.length; j++)
                isDirichlet[nodesDirichlet[j]] = true;

            for (int row = 0; row < totalRows; row++) {
                // If we are in a row belonging to a Dirichlet node
                // We have to zero all but the diagonal
                if (isDirichlet[row]) {
                    // zeroing
                    for (int e = L.ipos[row]; e < L.ipos[row + 1]; e++)
                        L.elem[e] = 0.0;

                    // diagonal to 1
                    L.elem[L.ipos[row]] = 1.0;
                }
                // if not, traverse searching columns of Dirichlet nodes
                else {
                    for (int e = L.ipos[row]; e < L.ipos[row + 1]; e++)
                        if (isDirichlet[L.jpos[e]]) {
                            L.elem[e] = 0.0;
                        }
                }
            }

            isDirichlet = null;
        }

        // Desingularization if neumann
        if (nodesDirichlet == null) {
            System.out.println("   * Desingularization Neumann (Not done)...");

            //for(int i=L.ipos[totalFilas-1]; i<L.ipos[totalFilas]; i++)
            //    if(L.jpos[i] == totalFilas-1)
            //    { L.elem[i] = 1.0; }
            //    else { L.elem[i] = 0.0; }
        }

        // GENERATION L y Lt ///////////////////////////////////////////
        // bucle for traversing columns
        System.out.println("   * Generating L y Lt...");

        for (int row = 0; row < totalRows; row++) {
            // Calculate elements in the row, from left to right.
            for (int pos = L.ipos[row + 1] - 1; pos > L.ipos[row]; pos--) {
                int column = L.jpos[pos];

                // we keep going ... (to calculate)
                double sum_ = 0.0;

                // slow version:
                //for(int k=0; k<colum; k++)
                //       suma += L.leeElement(row,k)*L.leeElemento(colum,k);
                //L.assignsElement(row, colum, (L.leeElemento(row,colum) - suma)/L.leeElemento(colum,colum));
                // Optimized version:
                int last = L.ipos[column + 1] - 1;

                for (int n = L.ipos[row + 1] - 1; n > pos; n--) // summing loop

                    for (int m = last; L.jpos[m] < L.jpos[pos]; m--) // search loop

                        if (L.jpos[n] == L.jpos[m]) {
                            sum_ += (L.elem[n] * L.elem[m]);
                            last = m;

                            break; // out from the search loop
                        }

                L.elem[pos] = (L.elem[pos] - sum_) / L.elem[L.ipos[column]];
            }

            // Calculate diagonal L(j,j)
            double suma = 0.0;

            // slow version:
            //double valor;
            //for(int k=0; k<colum; k++)
            //{
            //        value = L.leeElement(colum,k);
            //        suma += valor*valor;
            //}
            //L.assignsElement(colum, colum, Math.sqrt(L.leeElemento(colum, colum) - suma));
            // optimized version:
            for (int n = L.ipos[row] + 1; n < L.ipos[row + 1]; n++)
                suma += (L.elem[n] * L.elem[n]);

            L.elem[L.ipos[row]] = Math.sqrt(L.elem[L.ipos[row]] - suma);
        }

        // total time
        if (VERBOSE) {
            System.out.println("Generation LD: done in " +
                    ((System.currentTimeMillis() - startTime) / 1000.0) + " segs.");
        }
    }

    // Comands for writing in local variables

    /**
     * Assigns a new column of independient terms. The rest of the data is
     * kept.
     *
     * @param v column of independient terms.
     * @since org.jscience.fluids.solver v0.3
     */
    public void setB(double[] v) {
        boolean nans = false;
        b = v;

        for (int j = 0; j < b.length; j++)
            if (Double.isNaN(b[j])) {
                nans = true;
            }

        if (nans) {
            System.out.println("NaNs in SetB() !!!!!!!!!");
        }
    }

    /**
     * Assigns the delta of convergence of the current solver. This value is
     * used to decide if the solution is suficiently precise for our needs.
     * The value by default is <CODE>deltaConvergence</CODE>, good for almost
     * all purposes.
     * <p/>
     * <p/>
     * Lower values of delta increase precision, but increase the calculation
     * time.
     * </p>
     *
     * @param delta nuevo delta of convergence.
     * @since org.jscience.fluids.solver v0.3
     */
    public void setDeltaConvergence(double delta) {
        deltaConvergence = delta;
    }

    /**
     * We use the current solver to resolve the defined system of ecuations by
     * the  indicated <CODE>Matrix</CODE>.
     *
     * @param m matrix of coeficientes of the system.
     * @since org.jscience.fluids.solver v0.3
     */
    public void setCoeficientMatrix(Matrix m) {
        // force again preconditioning
        L = null;

        // variables of resolve()
        Ap = p = rk = sk = bAntiguo = uu = w = null;

        // copy the matrix of coeficientes
        A = m;

        if (!A.simetric) {
            System.out.println(
                    "error: GCCholeskyImpl only acepts simetric matrices.");
            System.exit(0);
        }
    }

    /**
     * Indicates the indices of the new Dirichlet nodes. Must be used in
     * conjunction with <CODE>setValuesDirichlet()</CODE>. To change the
     * Dirichlet  nodes implies generate a new Cholesky matrix associated with
     * the system.
     *
     * @param nodes vector of indexes.
     * @since org.jscience.fluids.solver v0.3
     */
    public void setNodesDirichlet(int[] nodes) {
        L = null;
        nodesDirichlet = nodes;
    }

    /**
     * Adjust the values of the Dirichlet nodes specified by
     * <CODE>setNodesDirichlet()</CODE>.
     *
     * @param values values of the Dirichlet nodes.
     * @since org.jscience.fluids.solver v0.3
     */
    public void setValuesDirichlet(double[] values) {
        valuesDirichlet = values;
    }

    /**
     * Split in two calls: a <CODE>setNodesDirichlet()</CODE> and
     * <CODE>setValuesDirichlet()</CODE>.
     *
     * @param nodes  indexes of Dirichlet nodes.
     * @param values values of Dirichlet nodes.
     * @since org.jscience.fluids.solver v0.3
     */
    public void setDirichlet(int[] nodes, double[] values) {
        setNodesDirichlet(nodes);
        setValuesDirichlet(values);
    }

    /**
     * Activates flag <CODE>VERBOSE</CODE>.
     *
     * @param v DOCUMENT ME!
     */
    public void setVerbose(boolean v) {
        VERBOSE = v;
    }
}
