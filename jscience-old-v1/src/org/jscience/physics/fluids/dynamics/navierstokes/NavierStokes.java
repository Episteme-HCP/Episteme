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

package org.jscience.physics.fluids.dynamics.navierstokes;

import org.jscience.physics.fluids.dynamics.CompiledData;
import org.jscience.physics.fluids.dynamics.KernelADFC;
import org.jscience.physics.fluids.dynamics.KernelADFCConfiguration;
import org.jscience.physics.fluids.dynamics.characteristics.QuadraticCharacteristics;
import org.jscience.physics.fluids.dynamics.characteristics.QuadraticCharacteristics3D;
import org.jscience.physics.fluids.dynamics.mesh.NavierStokesMesh;
import org.jscience.physics.fluids.dynamics.solver.GCCholeskyImpl;
import org.jscience.physics.fluids.dynamics.util.Matrix;

import java.io.FileWriter;
import java.io.PrintWriter;


/**
 * This class implements the problem of Navier-Stokes. The pressure is
 * multiplied by 2 before being stored and in the Drag&Lift
 *
 * @author Alejandro "Balrog" Rodriguez Gallego
 * @version 1.1 (06/08/00)
 */
public class NavierStokes {
    /** DOCUMENT ME! */
    private static double DELTA_CONVERGENCE;

    /** DOCUMENT ME! */
    private static int STEPS_SAVE;

    /** DOCUMENT ME! */
    private static int STEPS_BACKUP;

    /** DOCUMENT ME! */
    private static int MAX_ITER;

    /** DOCUMENT ME! */
    private static int TOTAL_STEPS;

    /** DOCUMENT ME! */
    private static double REYNOLDS;

    /** DOCUMENT ME! */
    private static double nu; // 0.00002;

    /** DOCUMENT ME! */
    private static double DELTA_T; // 25

    /** DOCUMENT ME! */
    private static int CHARACTERISTICS_FRACTIONING = 1;

    /** DOCUMENT ME! */
    private static double beta; // nu*DELTA_T/2.0

    /** DOCUMENT ME! */
    private static double F_ELECTRIC = 0.;

    /** DOCUMENT ME! */
    private static double F_SLIP = 0.00;

    /** DOCUMENT ME! */
    private static boolean CALCULATE_DL = true;

    /** DOCUMENT ME! */
    private static boolean SMAGORINSKY = false;

    /** DOCUMENT ME! */
    private static double SMAGORINSKY_CTE = 0.01;

    /** DOCUMENT ME! */
    private static double DELTA_SLIP = 0.005;

    /** DOCUMENT ME! */
    private static double initialValueVx = 0.0;

    /** DOCUMENT ME! */
    private static double initialValueVy = 0.0;

    /** DOCUMENT ME! */
    private static boolean VERBOSE;

    /** DOCUMENT ME! */
    private static boolean OBTAIN_FIELD_TEST = false;

    /** DOCUMENT ME! */
    private KernelADFC kernel;

    /** DOCUMENT ME! */
    private String dataFile;

    /** DOCUMENT ME! */
    private NavierStokesMesh mesh;

    /** DOCUMENT ME! */
    private boolean DEBUG_INK_SPHERE_3D = false;

    // transport ink or hydrogen bubbles
    /** DOCUMENT ME! */
    private boolean TRANSPORT_INK = false;

    /** DOCUMENT ME! */
    private double coordinateBubblesH2;

    /** DOCUMENT ME! */
    private double deltaTBubblesH2;

    /** DOCUMENT ME! */
    private int[] nodesInk;

    /** DOCUMENT ME! */
    private int[] nodesGamma0;

    /** DOCUMENT ME! */
    private double[] ink;

    /** DOCUMENT ME! */
    private double[] pint;

    /** DOCUMENT ME! */
    private double[][] valuesGamma0;

    /** DOCUMENT ME! */
    private double[][] massField;

    /** DOCUMENT ME! */
    private double[][] massesMasico;

    /** DOCUMENT ME! */
    private double[] f;

    /** DOCUMENT ME! */
    private Matrix mdivx;

    /** DOCUMENT ME! */
    private Matrix mdivy;

    /** DOCUMENT ME! */
    private Matrix mdivz;

    /** DOCUMENT ME! */
    private Matrix mdivxCuad;

    /** DOCUMENT ME! */
    private Matrix mdivyCuad;

    /** DOCUMENT ME! */
    private Matrix mdivzCuad;

    /** DOCUMENT ME! */
    private Matrix mrig;

    /** DOCUMENT ME! */
    private Matrix mrigl;

    /** DOCUMENT ME! */
    private Matrix mmas;

    /** DOCUMENT ME! */
    private Matrix mmasl;

    /** DOCUMENT ME! */
    private Matrix d;

    /** DOCUMENT ME! */
    private Matrix e;

    /** DOCUMENT ME! */
    private double[] sumSim;

    /** DOCUMENT ME! */
    private double[] sumaSim;

    /** DOCUMENT ME! */
    private double[] bPoisson;

    /** DOCUMENT ME! */
    private QuadraticCharacteristics characteristics;

    /** DOCUMENT ME! */
    private QuadraticCharacteristics3D characteristics3D;

    /** DOCUMENT ME! */
    private PrintWriter pw;

    /** DOCUMENT ME! */
    private double[][][] derum;

    /** DOCUMENT ME! */
    private double[] divSoltmp;

    /** DOCUMENT ME! */
    private double[] divB;

    /** DOCUMENT ME! */
    private GCCholeskyImpl gcPVel;

    /** DOCUMENT ME! */
    private GCCholeskyImpl gcElip;

    /** DOCUMENT ME! */
    private GCCholeskyImpl gcDiv;

    /** DOCUMENT ME! */
    private GCCholeskyImpl gcPoisson;

    /** DOCUMENT ME! */
    private double[] valuesDirichletHomogeneos;

    // variables for the turbulent mu
    /** DOCUMENT ME! */
    private double[] muT;

    // variables for the turbulent mu
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double[] muTtransp;

    // variables for the turbulent mu
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double[] muTl;

    /** DOCUMENT ME! */
    private double[] hNodo;

    /** DOCUMENT ME! */
    private Observer observer;

    /** DOCUMENT ME! */
    private Feedback feeback;

    // these  "fields" are for debugging
    /** DOCUMENT ME! */
    private double[] fieldTest1;

    // these  "fields" are for debugging
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double[] fieldTest2;

    /** DOCUMENT ME! */
    private DragLift2D dragLift2D;

/**
     * Creates a new NavierStokes object.
     *
     * @param kadfc DOCUMENT ME!
     */
    public NavierStokes(KernelADFC kadfc) {
        kernel = kadfc;

        // Reading configuration parameters from the kernel.
        KernelADFCConfiguration c = kernel.getConfiguration();

        SMAGORINSKY = c.smagorinsky;
        SMAGORINSKY_CTE = c.cSmagorinsky;
        DELTA_SLIP = c.deltaCCSlip;
        DELTA_CONVERGENCE = c.deltaConvergenceNavierStokes;
        DELTA_T = c.timeStepDuration;
        REYNOLDS = c.reynolds;
        initialValueVx = c.initialSpeedX;
        initialValueVy = c.initialSpeedY;
        MAX_ITER = c.maxIterationsNavierStokes;
        STEPS_BACKUP = STEPS_SAVE = c.stepsSave;
        TOTAL_STEPS = c.stepsTime;
        dataFile = c.meshName;
        nodesInk = c.nodesInk;
        coordinateBubblesH2 = c.coordinateBubblesH2;
        deltaTBubblesH2 = c.deltaTBubblesH2;

        if ((nodesInk != null) || (deltaTBubblesH2 != 0.0)) {
            TRANSPORT_INK = true;
        }

        VERBOSE = CompiledData.VERBOSE;
        nu = 1.0 / REYNOLDS;
        beta = (nu * DELTA_T) / 2.0;

        kernel.out("<B>NavierStokes:</B> Reynolds = " + REYNOLDS +
            " DeltaT = " + DELTA_T);

        if ((c.coordinates.length == 3) && SMAGORINSKY) {
            kernel.newWarningDialog(
                "The solver3D does not support SMAGORINSKY.\n" +
                "Specifies a turbulence model that is \n" +
                "not yet ported to 3D. It will not be used.");

            kernel.out(
                "<FONT COLOR=#FF0000><B>Smagorinsky:</B> ignored in solver3D.</FONT>");
            SMAGORINSKY = false;
        }

        // FORCE INK
        if (DEBUG_INK_SPHERE_3D) {
            TRANSPORT_INK = true;
            kernel.out(
                "<FONT COLOR=#0000FF><B>NavierStokes:</B> Forcing ink in constructor!</FONT>");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param um DOCUMENT ME!
     */
    private void calculatePartialDerivativeSpeed(double[][] um) {
        if (mesh.is3D()) {
            kernel.out("<B> calculatePartialDerivativeSpeed()? mesh 3D!!");

            return;
        }

        if (gcPVel == null) {
            // Initializing the system of ecuations
            gcPVel = new GCCholeskyImpl(kernel);
            gcPVel.setCoeficientMatrix(mmasl);

            derum = new double[um.length][um.length][mmasl.getRows()];

            // gcPVel.precondiciona();
        }

        /* calculate all classes of partial derivatives */
        gcPVel.setB(mdivx.multiply(um[0], mmasl.ipos.length - 1));
        gcPVel.resolve(derum[0][0]);

        gcPVel.setB(mdivy.multiply(um[1], mmasl.ipos.length - 1));
        gcPVel.resolve(derum[1][1]);

        gcPVel.setB(mdivx.multiply(um[1], mmasl.ipos.length - 1));
        gcPVel.resolve(derum[1][0]);

        gcPVel.setB(mdivy.multiply(um[0], mmasl.ipos.length - 1));
        gcPVel.resolve(derum[0][1]);
    }

    /**
     * DOCUMENT ME!
     */
    private void initializeMuTurbulent() {
        double[] tams = mesh.getElementAverageSizes();
        int[] ref = mesh.elementReferencesNode();
        int nLinealNodes = mesh.getCoordinates(0).length;
        int nNodesCuad = mesh.getCoordinatesQuad(0).length;
        int[] CNLE = mesh.getCacheNodeLinealElement();
        int[] iCNLE = mesh.getStartCacheNodeLinealElement();
        hNodo = new double[nLinealNodes];
        muTl = new double[nLinealNodes];
        muT = new double[nNodesCuad];
        muTtransp = new double[nNodesCuad];

        for (int n = 0; n < hNodo.length; n++) {
            //    hNodo[n] = tams[ref[n]];
            double tam = 0.0;
            int den = 0;

            for (int k = iCNLE[n]; k < iCNLE[n + 1]; k++)
                if (CNLE[k] != -1) {
                    int ijj = CNLE[k];
                    tam += tams[ijj];
                    den++;
                }

            tam /= den;
            hNodo[n] = tam;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param um DOCUMENT ME!
     * @param muT DOCUMENT ME!
     */
    private void calculateMuTurbulent(double[][] um, double[] muT) {
        if (mesh.is3D()) {
            return;
        }

        if (VERBOSE) {
            kernel.out("Caltulating Mu Turbulenta...");
        }

        //calcularDerivadasParcialesVelocidad(um);
        if (VERBOSE) {
            kernel.out("   * Inicializando...");
        }

        for (int j = 0; j < muTl.length; j++)
            muTl[j] = 0.0;

        // Calculate for this case
        for (int n = 0; n < muTl.length; n++) {
            // tensorial calculation
            double EijEij2 = (2 * derum[0][0][n] * derum[0][0][n]) +
                (2 * derum[1][1][n] * derum[1][1][n]) +
                (derum[0][1][n] * derum[0][1][n]) +
                (derum[1][0][n] * derum[1][0][n]) +
                (2 * derum[1][0][n] * derum[0][1][n]);

            // A node is in several elements... promediate.
            double tam = 0.0;

            /*
            int den = 0;
            for(int k = 0; k < cnce[n].length; k++)
            if(cnce[n][k] != -1)
            {
            int ijj=cnce[n][k];
            tam += hElem[ijj];
            den ++;
            }
            
            tam /= den;
             */
            tam = hNodo[n];
            muTl[n] = SMAGORINSKY_CTE * tam * tam * Math.sqrt(EijEij2);
        }

        // Estadistics
        double min;

        // Estadistics
        double max;

        // Estadistics
        double med;

        min = max = muTl[0];
        med = 0.0;

        for (int e = 0; e < muTl.length; e++) {
            if (muTl[e] < min) {
                min = muTl[e];
            }

            if (muTl[e] > max) {
                max = muTl[e];
            }

            med += muTl[e];
        }

        med /= muTl.length;

        System.out.println("MU_T : (min/max/media)     (" + min + "/" + max +
            "/" + med + ")");

        mesh.calculateInterpolatedField(muT, muTl);
    }

    /**
     * sum element by element the content of other matrix, <b>which is
     * supposed with identical Ipos and Jpos</b>. The factor is incremented in
     * the aritmethic average of turbulent mu's i j for the element j. (this
     * code was extracted directly from org.jscience.fluids.util.Matrix)
     *
     * @param smag matrix to be sumed to<code>this</code>
     * @param masses DOCUMENT ME!
     * @param rigidity DOCUMENT ME!
     * @param factor factor to apply to the sum term. For exemple, for having a
     *        difference, pass -1.
     * @param mu DOCUMENT ME!
     */
    private void sumSmagorinskyMatrix(Matrix smag, Matrix masses,
        Matrix rigidity, double factor, double[] mu) {
        double sign = 1.0;

        if (factor < 0.0) {
            sign = -1.0;
        }

        for (int i = 0; i < (smag.ipos.length - 1); i++)
            for (int j = smag.ipos[i]; j < smag.ipos[i + 1]; j++) {
                // multiply here by (DELTA_T*0.5)
                // factor is already multiplied.
                double smagor = ((DELTA_T * 0.5) * sign * (mu[i] +
                    mu[smag.jpos[j]])) / 2.0;
                smag.elem[j] = masses.elem[j] +
                    (rigidity.elem[j] * (factor + smagor));
            }
    }

    /**
     * DOCUMENT ME!
     */
    private void calculateElectricNodes() {
        if (mesh.is3D()) {
            return;
        }

        double tamElem = 0.1;
        double dist1 = (0.5 + 0.001);
        dist1 = dist1 * dist1;

        double dist2 = (0.5 + tamElem + 0.001);
        dist2 = dist2 * dist2;

        double[] x = mesh.getCoordinatesQuad(0);
        double[] y = mesh.getCoordinatesQuad(1);

        kernel.out("Calculation Electric Nodes -> Masic Field");

        double totalX = 0;
        double totalY = 0;

        for (int j = 0; j < x.length; j++) {
            double dist = (x[j] * x[j]) + (y[j] * y[j]);

            if ((dist > dist1) && (dist < dist2)) {
                massField[0][j] = F_ELECTRIC * Math.abs(y[j] / Math.sqrt(dist));
                massField[1][j] = (F_ELECTRIC * x[j]) / Math.sqrt(dist);

                if (y[j] > 0) {
                    massField[1][j] = -massField[1][j];
                }

                totalX += massField[0][j];
                totalY += massField[1][j];
            }
        }

        kernel.out("TOTALX = " + totalX + "\tTOTALY = " + totalY + "");
    }

    // the good
    /**
     * DOCUMENT ME!
     */
    private void initializeDivergence() {
        gcDiv = new GCCholeskyImpl(kernel);
        gcDiv.setCoeficientMatrix(mmasl);

        divSoltmp = new double[mmasl.getRows()];

        divB = new double[divSoltmp.length];
    }

    /**
     * DOCUMENT ME!
     *
     * @param campo DOCUMENT ME!
     * @param solucion DOCUMENT ME!
     */
    private void calculateDivergence(double[][] campo, double[] solucion) {
        // divB = divSol_X + divSol_Y + divSol_Z
        mdivx.multiply(divB, campo[0], mmasl.ipos.length - 1);

        mdivy.multiply(divSoltmp, campo[1], mmasl.ipos.length - 1);

        for (int j = 0; j < divB.length; j++)
            divB[j] += divSoltmp[j];

        if (mesh.is3D()) {
            mdivz.multiply(divSoltmp, campo[2], mmasl.ipos.length - 1);

            for (int j = 0; j < divB.length; j++)
                divB[j] += divSoltmp[j];
        }

        gcDiv.setB(divB);
        gcDiv.resolve(solucion);

        double min;
        double max;
        min = max = solucion[0];

        for (int j = 1; j < solucion.length; j++) {
            if (solucion[j] < min) {
                min = solucion[j];
            }

            if (solucion[j] > max) {
                max = solucion[j];
            }
        }

        System.out.println("Solucion of Divergence entre " + min + "  " + max +
            "");
    }

    /**
     * DOCUMENT ME!
     */
    public void execute() {
        mesh = new NavierStokesMesh(kernel);

        // initializacion mu turbulenta
        muT = null;
        gcPVel = null;

        // is the list of nodes in which we
        // evaluate the dirichlet contour condition
        // in function of (x,y,t). For that reason, we do not load
        // the values, but only the indixes.
        nodesGamma0 = mesh.getNodesDirichletQuad();
        valuesGamma0 = mesh.getValuesDirichletQuad();

        // campoMasico = new double[u0.length][Q]; calcularNodosElectricos();
        massField = null;

        // Preparing the output
        try {
            pw = new PrintWriter(new FileWriter(dataFile + ".flavia.res"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /////////////////////////////////////////////////////////////
        // initial conditions / Matrices / Characteristics
        if (mesh.is3D()) {
            characteristics3D = new QuadraticCharacteristics3D(kernel, mesh,
                    DELTA_T, TRANSPORT_INK);
            characteristics3D.initializeCaches();
        } else {
            characteristics = new QuadraticCharacteristics(kernel, mesh,
                    DELTA_T / CHARACTERISTICS_FRACTIONING, TRANSPORT_INK);
            characteristics.initializeCaches();
        }

        double[][] u0 = new double[mesh.getCoordinatesQuad().length][mesh.getCoordinatesQuad(0).length];
        double[][] u00 = new double[u0.length][u0[0].length];

        //double[][] u000 = new double[u0.length][u0[0].length];
        kernel.out("initial condition of the field (" + initialValueVx + ", " +
            initialValueVy + ")");

        for (int i = 0; i < u0[0].length; i++) {
            //u000[0][i]=
            u00[0][i] = u0[0][i] = initialValueVx;

            //u000[1][i]=
            u00[1][i] = u0[1][i] = initialValueVy;

            if (mesh.is3D()) { //u000[2][i]=
                u00[2][i] = u0[2][i] = 0.0;
            }
        }

        kernel.getInputProfile().createInitialField(u0[0], u0[1], mesh);

        double[] p0 = new double[mesh.getCoordinates(0).length];

        // reuse the poisson  matrices without contour conditions
        mrig = mesh.getRigidityMatrixQuad();
        mmas = mesh.getMassMatrixQuad();
        mdivx = mesh.getMatrixDivergenceX();
        mdivy = mesh.getMatrixDivergenceY();

        mdivxCuad = mesh.getMatrixDivergenceXQuad();
        mdivyCuad = mesh.getMatrixDivergenceYQuad();

        if (mesh.is3D()) {
            mdivzCuad = mesh.getMatrixDivergenceZQuad();
            mdivz = mesh.getMatrixDivergenceZ();
        }

        mrigl = mesh.getRigidityMatrix();
        mmasl = mesh.getMassMatrix();

        // Create d and e for the first time
        // Bij + beta*Aij
        d = new Matrix(mmas, true);
        d.sumMatrix(mrig, +beta);

        // Bij - beta*Aij
        e = new Matrix(mmas, true);
        e.sumMatrix(mrig, -beta);

        // DEPURAR_MATRICES();
        // initializations of the eliptical problems
        // Used for precondicioning GC's etcetera.
        int L = p0.length;
        int Q = u0[0].length;

        // f = new double[u0.length][Q];
        double[][] ubarram = new double[u0.length][Q];
        double[][] um = new double[u0.length][Q];
        double[] rm = new double[L];
        double[] rm1 = new double[L];
        double[] rbarram = new double[L];
        double[] pm = new double[L];
        double[] phim = new double[L];
        double[] phibarram = new double[L];
        double[] gm = new double[L];
        double[] gm1 = new double[L];
        double[] wm = new double[L];

        if (TRANSPORT_INK) {
            ink = new double[Q];
        }

        initializeSolversForElipticalProblems();
        initializeDivergence();

        if (SMAGORINSKY) {
            initializeMuTurbulent();
        }

        if (CALCULATE_DL) {
            dragLift2D = new DragLift2D(kernel, mesh);
        }

        // initialize feeback
        feeback = new Feedback(kernel);

        feeback.initializeCaches(mesh);

        // Observer
        observer = new SpeedObserver(kernel, mesh, dataFile);

        // observer = new ObservadorPresion(kernel, mesh, dataFile);
        //////////////////////////////////////////////////////////////
        // main loop
        long startTimeNavier = System.currentTimeMillis();
        kernel.setStartupTime(getCadenaDate(startTimeNavier));

        // Deserialization if suitable
        LoadBackupCopy ccr = new LoadBackupCopy(kernel);

        ccr.loadBackup();

        if (ccr.isOk()) {
            u0 = ccr.getU0();
            u00 = ccr.getU00();

            // u000 = ccr.getU000();
            p0 = ccr.getP();
            ink = ccr.getInk();

            // loads a backup with a diferent configuration
            // avoid an exception
            if (TRANSPORT_INK && (ink == null)) {
                ink = new double[Q];
            }
        }

        // initial condition: umx for save!
        for (int i = 0; i < um.length; i++)
            for (int j = 0; j < um[0].length; j++)
                um[i][j] = u0[i][j];

        storeResult(um, pm, 0);

        for (int step = 0; step < TOTAL_STEPS; step++) {
            long startTime = System.currentTimeMillis();

            // Aplicamos feeback
            feeback.applyFeedback(mesh, um[0], um[1]);
            valuesGamma0 = mesh.getValuesDirichletQuad();

            // Transporte ink o burbujas of hidrogeno
            // aqui introducimos in the mesh.
            int saltoBubbles = (int) (deltaTBubblesH2 / DELTA_T);

            if (saltoBubbles > 0) {
                if ((step % saltoBubbles) == 0) {
                    kernel.out("Nuevo frente burbujas: " + saltoBubbles +
                        " steps in " + coordinateBubblesH2);

                    double[][] cq = mesh.getCoordinatesQuad();

                    for (int q = 0; q < cq[0].length; q++) {
                        if ((cq[0][q] < (coordinateBubblesH2 + 0.1)) &&
                                (cq[0][q] > (coordinateBubblesH2 - 0.1))) {
                            ink[q] = 1.0;
                        }
                    }
                }
            }

            if (nodesInk != null) {
                for (int j = 0; j < nodesInk.length; j++)
                    ink[nodesInk[j]] = 1.0;
            }

            System.out.println("Metiendo ink");
            _produceInkForSphere(ink, mesh);

            for (int i = 0; i < u0.length; i++)
                for (int j = 0; j < u0[0].length; j++)
                    um[i][j] = u0[i][j];

            characteristicsProblem(u00, u0, um);

            elipticalProblem1(um, p0);

            // (2.4.74)
            calculateDivergence(um, rm);

            // 2.4.75)
            elipticalProblem3(rm, phim);

            // 2.4.76 y 2.4.77
            for (int j = 0; j < gm.length; j++)
                wm[j] = gm[j] = (0.5 * nu * rm[j]) + (phim[j] / DELTA_T);

            double integral_r0g0 = mesh.evaluateIntegralLinealProduct(rm, gm);

            for (int j = 0; j < pm.length; j++)
                pm[j] = p0[j];

            boolean convergence = false;
            int iter;

            for (iter = 0; !convergence; iter++) {
                // (2.4.78)
                elipticalProblem2(ubarram, wm);

                // (2.4.79)
                calculateDivergence(ubarram, rbarram);

                // (2.4.80)
                double rhom = mesh.evaluateIntegralLinealProduct(rm, gm) / mesh.evaluateIntegralLinealProduct(rbarram,
                        wm);

                // Analisis

                /*
                 System.out.println("RHO="+rhom);
                double min=1000000, max=-1000000;
                for(int j=0; j<ubarram[0].length; j++)
                    if(ubarram[0][j] < min) min = ubarram[0][j];
                    else if(ubarram[0][j] > max) max = ubarram[0][j];
                    System.out.println("UBmax="+max+"\tUBmin="+min);
                
                min=1000000; max=-1000000;
                for(int j=0; j<wm.length; j++)
                    if(wm[j] < min) min = wm[j];
                    else if(wm[j] > max) max = wm[j];
                    System.out.println("Wmax="+max+"\tWmin="+min);
                 */

                // (2.4.81) (2.4.82)
                for (int j = 0; j < pm.length; j++)
                    pm[j] = pm[j] - (rhom * wm[j]);

                for (int i = 0; i < um.length; i++)
                    for (int j = 0; j < um[0].length; j++)
                        um[i][j] = um[i][j] - (rhom * ubarram[i][j]);

                // (2.4.83)
                for (int j = 0; j < rm.length; j++)
                    rm1[j] = rm[j] - (rhom * rbarram[j]);

                // (2.4.84)
                elipticalProblem3(rbarram, phibarram);

                // (2.4.85)
                for (int l = 0; l < gm.length; l++)
                    // gm1[l] = gm[l] - rhom * (beta*rbarram[l]/DELTA_T + phibarram[l]/DELTA_T);
                    //gm1[l] = gm[l] - rhom*(0.5*nu*rbarram[l] + phibarram[l]/DELTA_T);
                    gm1[l] = gm[l] -
                        (rhom * ((0.5 * nu * rbarram[l]) +
                        (phibarram[l] / DELTA_T)));

                double numerador = mesh.evaluateIntegralLinealProduct(rm1, gm1);
                double valorConv = numerador / integral_r0g0;

                System.out.println("step: " + step + " iter: " + iter +
                    " epsilon = " + valorConv);
                kernel.setActualStep(step, iter);

                // VALOR ABSOLUTO: Si o NO ???
                if (Math.abs(valorConv) > DELTA_CONVERGENCE) {
                    double gamma = numerador / mesh.evaluateIntegralLinealProduct(rm,
                            gm);

                    for (int l = 0; l < wm.length; l++)
                        wm[l] = gm1[l] + (gamma * wm[l]);
                } else {
                    convergence = true;
                }

                for (int l = 0; l < rm.length; l++) {
                    rm[l] = rm1[l];
                    gm[l] = gm1[l];
                    ;
                }

                if (iter > MAX_ITER) {
                    convergence = true;
                }
            }

            // STORE RESULTS
            ///////////////////////////////////////
            storeResult(um, pm, step + 1);

            if (SMAGORINSKY || CALCULATE_DL) {
                calculatePartialDerivativeSpeed(um);
            }

            if (CALCULATE_DL) {
                dragLift2D.calculateDragLift(um, derum, pm, step);
            }

            if (observer != null) {
                if ((step % STEPS_SAVE) == 0) {
                    observer.evaluate(step * DELTA_T, um, pm);
                }
            }

            if (SMAGORINSKY) {
                System.out.println("SMAGORINSKY = " + SMAGORINSKY_CTE);
                calculateMuTurbulent(um, muT);

                //Bij + beta*Aij
                sumSmagorinskyMatrix(d, mmas, mrig, +beta, muTtransp);

                //Bij - beta*Aij
                sumSmagorinskyMatrix(e, mmas, mrig, -beta, muT);

                /*
                double[][] coord = mesh.getCoordinatesQuad();
                
                
                for(int j=0; j<um[0].length; j++)
                    for(int i=0; i<um.length; i++)
                        if(Math.sqrt(coord[0][j]*coord[0][j]+coord[1][j]*coord[1][j])<0.6) {
                            // normal  = (Sum Ui - n/2)/sqrt(n/12)
                            double normal=0.0;
                
                            for(int k=0; k<12; k++)
                                normal += Math.random();
                            normal -= 6.0;
                            um[i][j] += muT[j]*normal;
                        }
                 */
            }

            // Estimation
            if ((step % STEPS_SAVE) == 0) {
                long pasado = System.currentTimeMillis() - startTimeNavier;
                long estimado = (pasado * TOTAL_STEPS) / (step + 1);
                kernel.setEndingTime(getCadenaDate(startTimeNavier + estimado));
            }

            kernel.setCalculationProgress((100 * step) / TOTAL_STEPS);

            // Update pressures
            for (int j = 0; j < p0.length; j++)
                p0[j] = pm[j];

            // Update values of speeds
            for (int i = 0; i < u0.length; i++)
                for (int j = 0; j < u0[0].length; j++) {
                    // u000[i][j] = u00[i][j];
                    u00[i][j] = u0[i][j];
                    u0[i][j] = um[i][j];
                }

            // backup copy
            if ((step % STEPS_BACKUP) == 0) {
                SaveBackupCopy scr = new SaveBackupCopy(kernel);
                scr.salvaBackup(u0, u00, null, p0, ink);
                scr = null;
            }

            // Slip experimental dirichlet SLIP_BALROG
            NavierStokesMesh m = mesh; // alias

            if ((m.lengthSlip != null) && !m.is3D()) {
                double[] xco = m.getCoordinatesQuad(0);
                double[] yco = m.getCoordinatesQuad(1);

                for (int s = 0; s < m.lengthSlip.length; s++) {
                    double nxs = m.nxSlip[s];
                    double nys = m.nySlip[s];

                    int nodeG = -1;
                    int node = m.l1Slip[s];
                    double x;
                    double y;
                    double k;
                    double vxc;
                    double vyc;
                    double Vbx;
                    double Vby;

                    for (int j = 0; j < nodesGamma0.length; j++)
                        if (nodesGamma0[j] == node) {
                            nodeG = j;

                            break;
                        }

                    if (nodeG < 0) {
                        System.out.print("*");
                    } else {
                        x = xco[node];
                        y = yco[node];

                        Vbx = m.valueFieldQuad(x - (DELTA_SLIP * m.nxSlip[s]),
                                y - (DELTA_SLIP * m.nySlip[s]), u0[0]);
                        Vby = m.valueFieldQuad(x - (DELTA_SLIP * m.nxSlip[s]),
                                y - (DELTA_SLIP * m.nySlip[s]), u0[1]);

                        //valuesGamma0[0][nodeG] = 2*m.valorCampoQuad(x-DELTA_SLIP*m.nxSlip[s], y-DELTA_SLIP*m.nySlip[s], u0[0]);
                        //valuesGamma0[1][nodeG] = 2*m.valorCampoQuad(x-DELTA_SLIP*m.nxSlip[s], y-DELTA_SLIP*m.nySlip[s], u0[1]);
                        //valuesGamma0[0][nodeG] -= m.valorCampoQuad(x-DELTA_SLIP*m.nxSlip[s], y-DELTA_SLIP*m.nySlip[s], u00[0]);
                        //valuesGamma0[1][nodeG] -= m.valorCampoQuad(x-DELTA_SLIP*m.nxSlip[s], y-DELTA_SLIP*m.nySlip[s], u00[1]);
                        k = (Vbx * nxs) + (Vby * nys);
                        vxc = Vbx - (k * nxs);
                        vyc = Vby - (k * nys);

                        if (((Vbx * vxc) + (Vby * vyc)) < 0) {
                            vxc = -vxc;
                            vyc = -vyc;
                        }

                        valuesGamma0[0][nodeG] = vxc -
                            (F_SLIP * valuesGamma0[0][nodeG] * valuesGamma0[0][nodeG]);
                        valuesGamma0[1][nodeG] = vyc -
                            (F_SLIP * valuesGamma0[1][nodeG] * valuesGamma0[1][nodeG]);
                    }

                    nodeG = -1;
                    node = m.l2Slip[s];

                    for (int j = 0; j < nodesGamma0.length; j++)
                        if (nodesGamma0[j] == node) {
                            nodeG = j;

                            break;
                        }

                    if (nodeG < 0) {
                        System.out.print("*");
                    } else {
                        x = xco[node];
                        y = yco[node];

                        Vbx = m.valueFieldQuad(x - (DELTA_SLIP * m.nxSlip[s]),
                                y - (DELTA_SLIP * m.nySlip[s]), u0[0]);
                        Vby = m.valueFieldQuad(x - (DELTA_SLIP * m.nxSlip[s]),
                                y - (DELTA_SLIP * m.nySlip[s]), u0[1]);

                        //valuesGamma0[0][nodeG] = 2*m.valorCampoQuad(x-DELTA_SLIP*m.nxSlip[s], y-DELTA_SLIP*m.nySlip[s], u0[0]);
                        //valuesGamma0[1][nodeG] = 2*m.valorCampoQuad(x-DELTA_SLIP*m.nxSlip[s], y-DELTA_SLIP*m.nySlip[s], u0[1]);
                        //valuesGamma0[0][nodeG] -= m.valorCampoQuad(x-DELTA_SLIP*m.nxSlip[s], y-DELTA_SLIP*m.nySlip[s], u00[0]);
                        //valuesGamma0[1][nodeG] -= m.valorCampoQuad(x-DELTA_SLIP*m.nxSlip[s], y-DELTA_SLIP*m.nySlip[s], u00[1]);
                        k = (Vbx * nxs) + (Vby * nys);
                        vxc = Vbx - (k * nxs);
                        vyc = Vby - (k * nys);

                        if (((Vbx * vxc) + (Vby * vyc)) < 0) {
                            vxc = -vxc;
                            vyc = -vyc;
                        }

                        valuesGamma0[0][nodeG] = vxc -
                            (F_SLIP * valuesGamma0[0][nodeG] * valuesGamma0[0][nodeG]);
                        valuesGamma0[1][nodeG] = vyc -
                            (F_SLIP * valuesGamma0[1][nodeG] * valuesGamma0[1][nodeG]);
                    }

                    nodeG = -1;
                    node = m.qSlip[s];

                    for (int j = 0; j < nodesGamma0.length; j++)
                        if (nodesGamma0[j] == node) {
                            nodeG = j;

                            break;
                        }

                    if (nodeG < 0) {
                        System.out.print("*");
                    } else {
                        x = xco[node];
                        y = yco[node];

                        Vbx = m.valueFieldQuad(x - (DELTA_SLIP * m.nxSlip[s]),
                                y - (DELTA_SLIP * m.nySlip[s]), u0[0]);
                        Vby = m.valueFieldQuad(x - (DELTA_SLIP * m.nxSlip[s]),
                                y - (DELTA_SLIP * m.nySlip[s]), u0[1]);

                        //valuesGamma0[0][nodeG] = 2*m.valorCampoQuad(x-DELTA_SLIP*m.nxSlip[s], y-DELTA_SLIP*m.nySlip[s], u0[0]);
                        //valuesGamma0[1][nodeG] = 2*m.valorCampoQuad(x-DELTA_SLIP*m.nxSlip[s], y-DELTA_SLIP*m.nySlip[s], u0[1]);
                        //valuesGamma0[0][nodeG] -= m.valorCampoQuad(x-DELTA_SLIP*m.nxSlip[s], y-DELTA_SLIP*m.nySlip[s], u00[0]);
                        //valuesGamma0[1][nodeG] -= m.valorCampoQuad(x-DELTA_SLIP*m.nxSlip[s], y-DELTA_SLIP*m.nySlip[s], u00[1]);
                        k = (Vbx * nxs) + (Vby * nys);
                        vxc = Vbx - (k * nxs);
                        vyc = Vby - (k * nys);

                        if (((Vbx * vxc) + (Vby * vyc)) < 0) {
                            vxc = -vxc;
                            vyc = -vyc;
                        }

                        valuesGamma0[0][nodeG] = vxc -
                            (F_SLIP * valuesGamma0[0][nodeG] * valuesGamma0[0][nodeG]);
                        valuesGamma0[1][nodeG] = vyc -
                            (F_SLIP * valuesGamma0[1][nodeG] * valuesGamma0[1][nodeG]);
                    }
                }
            }
        }

        try {
            pw.close();
            kernel.out("<FONT SIZE=4>Saved and closed</FONT><HR>");
            kernel.setCalculationProgress(100);

            if (kernel.getConfiguration().plugin) {
                System.exit(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (observer != null) {
            observer.close();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param millis DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String getCadenaDate(long millis) {
        String cadena = "" + new java.util.Date(millis);

        return cadena;
    }

    /**
     * DOCUMENT ME!
     */
    private void initializeSolversForElipticalProblems() {
        // Apply contour conditions
        System.out.print("Inicializing pElip1, pElip2, pElip3.. ");
        gcElip = new GCCholeskyImpl(kernel);
        gcElip.setCoeficientMatrix(d);
        gcElip.setNodesDirichlet(mesh.getNodesDirichletQuad());
        valuesDirichletHomogeneos = new double[mesh.getValuesDirichletQuad()[0].length];

        //pElip3 = new Poisson(kernel, mrigl, mmasl, mesh.nNeumannl, new double[mesh.vNeumannl.length]);
        gcPoisson = new GCCholeskyImpl(kernel);
        gcPoisson.setCoeficientMatrix(mrigl);
        gcPoisson.setDirichlet(mesh.nNeumannl, new double[mesh.vNeumannl.length]);
        bPoisson = new double[mesh.getCoordinates(0).length];

        sumSim = new double[d.getRows()];
        sumaSim = new double[sumSim.length];
        f = new double[sumSim.length];

        //f[1] = new double[sumsim.length];
        //if(mesh.is3D()) f[2] = new double[sumsim.length];
        // if(CONTAMINANTE) fc = new double[sumsim.length];
        if (massField != null) {
            massesMasico = new double[massField.length][];
        }

        // r0neg = new double[mesh.getCoordinates(0).length];
    }

    /**
     * DOCUMENT ME!
     *
     * @param u00 DOCUMENT ME!
     * @param u0 DOCUMENT ME!
     * @param um DOCUMENT ME!
     */
    private void characteristicsProblem(double[][] u00, double[][] u0,
        double[][] um) {
        // Apply characteristics
        /////////////////////////////////////////////
        for (int i = 0; i < um.length; i++)
            for (int q = 0; q < nodesGamma0.length; q++)
                um[i][nodesGamma0[q]] = valuesGamma0[i][q];

        if (mesh.is3D()) {
            characteristics3D.setSpeedFields(u0[0], u0[1], u0[2], u00[0],
                u00[1], u00[2]);

            if (TRANSPORT_INK) {
                characteristics3D.calculate(um[0], um[1], um[2], ink);
            } else {
                characteristics3D.calculate(um[0], um[1], um[2], null);
            }
        } else {
            if (CHARACTERISTICS_FRACTIONING == 1) {
                System.out.println("Characteristics not fractionated");
                characteristics.setSpeedFields(u0[0], u0[1], u00[0], u00[1]);

                //if(TRANSPORT_INK)
                if (muT != null) {
                    for (int j = 0; j < muT.length; j++)
                        muTtransp[j] = muT[j];
                }

                characteristics.calculate(um[0], um[1], ink, muTtransp);

                //else caracteristicas.calcular(um[0],um[1], null);
            } else {
                // Paso fraccionado
                double[][] ucf0 = new double[u0.length][u0[0].length];
                double[][] ucf00 = new double[u00.length][u00[0].length];

                double[][] ucfm = new double[u0.length][u0[0].length];

                //
                for (int i = 0; i < u0.length; i++)
                    for (int j = 0; j < u0[0].length; j++) {
                        ucf0[i][j] = u0[i][j];
                        ucf00[i][j] = (((CHARACTERISTICS_FRACTIONING - 1) * u0[i][j]) +
                            u00[i][j]) / CHARACTERISTICS_FRACTIONING;

                        ucfm[i][j] = um[i][j];
                    }

                if (muT != null) {
                    for (int j = 0; j < muT.length; j++)
                        muTtransp[j] = muT[j];
                }

                System.out.println("" + CHARACTERISTICS_FRACTIONING + " steps");

                for (int stepfrac = 0; stepfrac < CHARACTERISTICS_FRACTIONING;
                        stepfrac++) {
                    // ***** caracteristicas.setCampoVelocidades(ucf0[0], ucf0[1], ucf00[0], ucf00[1]);
                    // **** caracteristicas.calcular(ucfm[0],ucfm[1], ink, muTtransp);
                    for (int i = 0; i < u0.length; i++)
                        for (int j = 0; j < u0[0].length; j++) {
                            ucf00[i][j] = ucf0[i][j];
                            ucf0[i][j] = ucfm[i][j];
                        }

                    //System.out.print("*");
                }

                for (int i = 0; i < u0.length; i++)
                    for (int j = 0; j < u0[0].length; j++) {
                        um[i][j] = ucfm[i][j];
                    }
            }
        }
    }

    // the good
    /**
     * eliptical problem  1. takes arguments u0x, u0y returns in u0x,
     * u0y
     *
     * @param um DOCUMENT ME!
     * @param p0 DOCUMENT ME!
     */
    private void elipticalProblem1(double[][] um, double[] p0) {
        // previous calculations  DIFUSSION COMPONENT X
        //////////////////////////////////////////
        // e<ij> * cci<n> = f<j>
        e.multiply(sumSim, um[0]);
        mdivxCuad.multiply(sumaSim, p0, um[0].length);

        for (int j = 0; j < sumSim.length; j++)
            f[j] = sumSim[j] + (DELTA_T * sumaSim[j]);

        if (massField != null) {
            mmas.multiply(massesMasico[0], massField[0]);

            for (int j = 0; j < sumSim.length; j++)
                f[j] += (DELTA_T * massesMasico[0][j]);
        }

        gcElip.setB(f);
        gcElip.setValuesDirichlet(valuesGamma0[0]);
        gcElip.resolve(um[0]);

        // previous calculations  DIFUSSION COMPONENT Y
        //////////////////////////////////////////
        // e<ij> * cci<n> = f<j>
        e.multiply(sumSim, um[1]);
        mdivyCuad.multiply(sumaSim, p0, um[1].length);

        for (int j = 0; j < sumSim.length; j++)
            f[j] = sumSim[j] + (DELTA_T * sumaSim[j]);

        if (massField != null) {
            mmas.multiply(massesMasico[1], massField[1]);

            for (int j = 0; j < sumSim.length; j++)
                f[j] += (DELTA_T * massesMasico[1][j]);
        }

        gcElip.setB(f);
        gcElip.setValuesDirichlet(valuesGamma0[1]);
        gcElip.resolve(um[1]);

        if (mesh.is3D()) {
            // previous calculations  DIFUSSION COMPONENT Z
            //////////////////////////////////////////
            // e<ij> * cci<n> = f<j>
            e.multiply(sumSim, um[2]);
            mdivzCuad.multiply(sumaSim, p0, um[2].length);

            for (int j = 0; j < sumSim.length; j++)
                f[j] = sumSim[j] + (DELTA_T * sumaSim[j]);

            if (massField != null) {
                mmas.multiply(massesMasico[2], massField[2]);

                for (int j = 0; j < sumSim.length; j++)
                    f[j] += (DELTA_T * massesMasico[2][j]);
            }

            gcElip.setB(f);
            gcElip.setValuesDirichlet(valuesGamma0[2]);
            gcElip.resolve(um[2]);
        }
    }

    // the good
    /**
     * eliptical problem  2. takes arguments ubarramx, ubarramy returns
     * in ubarramx, ubarramy
     *
     * @param ubarram DOCUMENT ME!
     * @param wm DOCUMENT ME!
     */
    private void elipticalProblem2(double[][] ubarram, double[] wm) {
        double[] sumasim;

        // DIFUSSION COMPONENT X
        ////////////////////////////////////
        // (e<ij>  +  divCuad<ij>)* cci<n> = f<j>
        sumasim = mdivxCuad.multiply(wm, ubarram[0].length);

        //f[0] = new double[sumasim.length];
        for (int j = 0; j < sumasim.length; j++)
            f[j] = DELTA_T * sumasim[j];

        // Resolver d<ij> * ci<n+1> = f<j>
        // Apply contour conditions
        gcElip.setB(f);
        gcElip.setValuesDirichlet(valuesDirichletHomogeneos);
        gcElip.resolve(ubarram[0]);

        // DIFUSSION COMPONENT Y
        ////////////////////////////////////
        // e<ij> * cci<n> = f<j>
        sumasim = mdivyCuad.multiply(wm, ubarram[1].length);

        //f[1] = new double[sumasim.length];
        for (int j = 0; j < sumasim.length; j++)
            f[j] = DELTA_T * sumasim[j];

        // Resolver d<ij> * ci<n+1> = f<j>
        // Apply contour conditions
        gcElip.setB(f);
        gcElip.setValuesDirichlet(valuesDirichletHomogeneos);
        gcElip.resolve(ubarram[1]);

        if (mesh.is3D()) {
            // DIFUSSION COMPONENT Z
            ////////////////////////////////////
            // e<ij> * cci<n> = f<j>
            sumasim = mdivzCuad.multiply(wm, ubarram[2].length);

            // f[2] = new double[sumasim.length];
            for (int j = 0; j < sumasim.length; j++)
                f[j] = DELTA_T * sumasim[j];

            // Resolver d<ij> * ci<n+1> = f<j>
            // Apply contour conditions
            gcElip.setB(f);
            gcElip.setValuesDirichlet(valuesDirichletHomogeneos);
            gcElip.resolve(ubarram[2]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param rma DOCUMENT ME!
     * @param sol DOCUMENT ME!
     */
    private void elipticalProblem3(double[] rma, double[] sol) {
        mmasl.multiply(bPoisson, rma);
        gcPoisson.setB(bPoisson);
        gcPoisson.resolve(sol);
    }

    /**
     * DOCUMENT ME!
     *
     * @param um DOCUMENT ME!
     * @param pm DOCUMENT ME!
     * @param step DOCUMENT ME!
     */
    private void storeResult(double[][] um, double[] pm, int step) {
        if ((step % STEPS_SAVE) == 0) {
            if (pint == null) {
                pint = new double[um[0].length];
            }

            KernelADFCConfiguration c = kernel.getConfiguration();

            if (um.length != 3) {
                c.saveVz = false;
            }

            kernel.out("<FONT COLOR=#000080><B>Grabando step " + step +
                "<B></FONT>");

            // double[] rint = new double[um[0].length];
            try {
                // Pressures
                if (c.saveP) {
                    pint = mesh.calculateInterpolatedField(pint, pm);
                    pw.println("PRESSURE              1   " + (step * DELTA_T) +
                        "    1    1    1");

                    for (int i = 0; i < um[0].length; i++)
                        // pw.println((i+1)+"       "+pint[i]);
                        pw.println((i + 1) + "\t" + (2 * pint[i]));
                }

                // Divergence
                if (c.saveDiv) {
                    double[] div = new double[pm.length];
                    calculateDivergence(um, div);
                    pint = mesh.calculateInterpolatedField(pint, div);
                    pw.println("DIVERGENCE              1   " +
                        (step * DELTA_T) + "    1    1    1");

                    for (int i = 0; i < pint.length; i++)
                        pw.println((i + 1) + "\t" + (2 * pint[i]));
                }

                // MuTurbulent
                if (c.saveMuT) {
                    if (!SMAGORINSKY) {
                        muT = new double[um[0].length];

                        calculateMuTurbulent(um, muT);
                    }

                    pw.println("MU_T              1   " + (step * DELTA_T) +
                        "    1    1    1");

                    for (int i = 0; i < muT.length; i++)
                        pw.println((i + 1) + "\t" + (2 * muT[i]));
                }

                if (TRANSPORT_INK) {
                    pw.println("INK              1   " + (step * DELTA_T) +
                        "    1    1    1");

                    for (int i = 0; i < ink.length; i++)
                        // pw.println((i+1)+"       "+pint[i]);
                        pw.println((i + 1) + "\t" + ink[i]);
                }

                // speeds
                int components = 0;

                if (c.saveVx) {
                    components++;
                }

                if (c.saveVy) {
                    components++;
                }

                if (c.saveVz) {
                    components++;
                }

                if (components > 1) {
                    components = 2;
                }

                pw.println("SPEED              1   " + (step * DELTA_T) +
                    "    " + components + "    1    1");

                if (components > 1) {
                    if (c.saveVx) {
                        pw.println("SPEED_X");
                    }

                    if (c.saveVy) {
                        pw.println("SPEED_Y");
                    }

                    if (c.saveVz) {
                        pw.println("SPEED_Z");
                    }
                }

                for (int i = 0; i < um[0].length; i++) {
                    pw.print("" + (i + 1));

                    if (c.saveVx) {
                        pw.print("\t" + um[0][i]);
                    }

                    if (c.saveVy) {
                        pw.print("\t" + um[1][i]);
                    }

                    if (c.saveVz) {
                        pw.print("\t" + um[2][i]);
                    }

                    pw.println();
                }

                if (OBTAIN_FIELD_TEST) {
                    // CampoTest
                    pw.println("TEST              1   " + (step * DELTA_T) +
                        "    2    1    1");
                    pw.println("TEST_X");
                    pw.println("TEST_Y");

                    double[] tq1 = new double[um.length];
                    double[] tq2 = new double[um.length];

                    tq1 = mesh.calculateInterpolatedField(tq1, fieldTest1);
                    tq2 = mesh.calculateInterpolatedField(tq2, fieldTest2);

                    for (int i = 0; i < tq1.length; i++) {
                        double valx = tq1[i];
                        double valy = tq2[i];

                        // pw.println((i+1)+"       "+valx +"      " +valy);
                        pw.println((i + 1) + "\t" + valx + "\t" + valy);
                    }
                }

                pw.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void DEBUG_MATRICES() {
        kernel.out("<FONT COLOR=#FF0000>Saving matrices to filename...</FONT>");

        mmasl.saveToFile(dataFile + ".masses", "MASS MATRIX LINEAL");
        mmas.saveToFile(dataFile + ".massesCuad", "MASS MATRIX QUADRATIC");
        mrigl.saveToFile(dataFile + ".rigidity", "RIGIDITY MATRIX LINEAL");
        mrig.saveToFile(dataFile + ".rigidityCuad", "RIGIDITY MATRIX QUADRATIC");
        mdivx.saveToFile(dataFile + ".divx", "DIVERGENCE MATRIX X");
        mdivy.saveToFile(dataFile + ".divy", "DIVERGENCE MATRIX Y");
        mdivxCuad.saveToFile(dataFile + ".divxCuad",
            "DIVERGENCE MATRIX X TRANSPOSED");
        mdivyCuad.saveToFile(dataFile + ".divyCuad",
            "DIVERGENCE MATRIX Y TRANSPOSED");
        d.saveToFile(dataFile + ".matrixD", "MASS MATRIX + beta*RIGIDITY MATRIX");
        e.saveToFile(dataFile + ".matrixE", "MASS MATRIX - beta*RIGIDITY MATRIX");
    }

    /**
     * DOCUMENT ME!
     *
     * @param ink DOCUMENT ME!
     * @param m DOCUMENT ME!
     */
    public void _produceInkForSphere(double[] ink, NavierStokesMesh m) {
        if (DEBUG_INK_SPHERE_3D) {
            double[][] coord = m.getCoordinatesQuad();

            System.out.println("Introducing forced ink for 3D");

            for (int j = 0; j < ink.length; j++) {
                double x = coord[0][j];
                double y = coord[1][j];
                double z = coord[2][j];

                if ((x > -1.3) && (x < -0.6)) {
                    if (((y * y) + (z * z)) < (0.5 * 0.5)) {
                        ink[j] = 1.0;
                    }
                }
            }
        }
    }
}
