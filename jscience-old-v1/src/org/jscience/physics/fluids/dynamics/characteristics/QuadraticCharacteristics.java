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

package org.jscience.physics.fluids.dynamics.characteristics;


// import java.applet.*;
//import java.awt.*;
import org.jscience.physics.fluids.dynamics.CompiledData;
import org.jscience.physics.fluids.dynamics.KernelADFC;
import org.jscience.physics.fluids.dynamics.KernelADFCConfiguration;
import org.jscience.physics.fluids.dynamics.mesh.NavierStokesMesh;
import org.jscience.physics.fluids.dynamics.solver.GCCholeskyImpl;
import org.jscience.physics.fluids.dynamics.util.Matrix;

import java.io.FileWriter;
import java.io.PrintWriter;


/**
 * This class calculates the values of the different magnitudes cx, cy, cc
 * y cd at the root of the caracteristics for a 2D meshing with a speed field.
 * Tipically these magnitudes correspond with the two speed components x and y
 * and with the concentration of a contaminant or the turbulent mu.
 */
public class QuadraticCharacteristics {
    /**
     * This value indicates the precision around the borders of the
     * triangular elements for the parameters p and q.
     */
    private final static double DELTA_PQ = 0.000001; // 0.0001;

    /**
     * Convergence Delta for the iterative method for the calculus of
     * the feet of the characteristics
     */
    private final static double DELTA_FOOT = 0.00000001;

    /**
     * In the iterative algorithms (characteristics, slalg...) controls
     * the count before doing a forced interrumption. Serves to avoid
     * blockages.
     */
    private final static int MAX_CONTITER = 96;

    /** DOCUMENT ME! */
    static int numero = 0;

    /** DOCUMENT ME! */
    private KernelADFC kernel;

    /** DOCUMENT ME! */
    private boolean VERBOSE;

    /** DOCUMENT ME! */
    private NavierStokesMesh mesh;

    /** DOCUMENT ME! */
    private double[] vx;

    /** DOCUMENT ME! */
    private double[] vy;

    /** DOCUMENT ME! */
    private double[] vx2;

    /** DOCUMENT ME! */
    private double[] vy2;

    /** DOCUMENT ME! */
    private double[] vx3;

    /** DOCUMENT ME! */
    private double[] vy3;

    /** DOCUMENT ME! */
    private double[] x;

    /** DOCUMENT ME! */
    private double[] y;

    /** DOCUMENT ME! */
    private double[] cx;

    /** DOCUMENT ME! */
    private double[] cy;

    /** DOCUMENT ME! */
    private double[] cc;

    /** DOCUMENT ME! */
    private double[] cd;

    // soluciones for getC?
    /** DOCUMENT ME! */
    private double[] cx2;

    // soluciones for getC?
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double[] cy2;

    // soluciones for getC?
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double[] cc2;

    // soluciones for getC?
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double[] cd2;

    /** DOCUMENT ME! */
    private int[] n1;

    /** DOCUMENT ME! */
    private int[] n2;

    /** DOCUMENT ME! */
    private int[] n3;

    /** DOCUMENT ME! */
    private int[] n4;

    /** DOCUMENT ME! */
    private int[] n5;

    /** DOCUMENT ME! */
    private int[] n6;

    /** DOCUMENT ME! */
    private double[] A;

    /** DOCUMENT ME! */
    private double[] B;

    /** DOCUMENT ME! */
    private double[] C;

    /** DOCUMENT ME! */
    private double[] D;

    /** DOCUMENT ME! */
    private double[] DetCr;

    /** table linealNode-element */
    private int[] CNLE;

    /** DOCUMENT ME! */
    private int[] iCNLE;

    /** reference element for a given node */
    private int[] reference;

    /** DOCUMENT ME! */
    private int totalElements;

    /** DOCUMENT ME! */
    private int totalNodes;

    /** DOCUMENT ME! */
    private double deltaT;

    /** DOCUMENT ME! */
    private boolean debug = false;

    /** DOCUMENT ME! */
    private boolean ANTIDIFUSION = false;

    // Used to give back the values of the function 'values'
    // I do not like to use global variables for this but ...
    /** DOCUMENT ME! */
    private double vxPar;

    // Used to give back the values of the function 'values'
    // I do not like to use global variables for this but ...
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double vyPar;

    // Used to give back the values of the function 'values'
    // I do not like to use global variables for this but ...
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double vx2Par;

    // Used to give back the values of the function 'values'
    // I do not like to use global variables for this but ...
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double vy2Par;

    // Used to give back the values of the function 'values'
    // I do not like to use global variables for this but ...
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double vx3Par;

    // Used to give back the values of the function 'values'
    // I do not like to use global variables for this but ...
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double vy3Par;

    // Used to give back the values of the function 'values'
    // I do not like to use global variables for this but ...
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double cxPar;

    // Used to give back the values of the function 'values'
    // I do not like to use global variables for this but ...
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double cyPar;

    // Used to give back the values of the function 'values'
    // I do not like to use global variables for this but ...
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double ccPar;

    // Used to give back the values of the function 'values'
    // I do not like to use global variables for this but ...
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double cdPar;

    // Used to give back the values of the function 'values'
    // I do not like to use global variables for this but ...
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double pPar;

    // Used to give back the values of the function 'values'
    // I do not like to use global variables for this but ...
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double qPar;

    /** caches of adjacent elements */
    private int[] adjacent12;

    /** caches of adjacent elements */
    private int[] adjacent13;

    /** caches of adjacent elements */
    private int[] adjacent23;

    /** contaminant use */
    private boolean contaminant;

    /** DOCUMENT ME! */
    private boolean[] isDirichlet;

    /** DOCUMENT ME! */
    private int notMoving;

    /** DOCUMENT ME! */
    private int movingInside;

    /** DOCUMENT ME! */
    private int toCleanBorder;

    /** DOCUMENT ME! */
    private int toLeapBorder;

    // base functions for characteristics going forward
    /** DOCUMENT ME! */
    private double phi1;

    // base functions for characteristics going forward
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double phi2;

    // base functions for characteristics going forward
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double phi3;

    // base functions for characteristics going forward
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double phi4;

    // base functions for characteristics going forward
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double phi5;

    // base functions for characteristics going forward
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double phi6;

    /** DOCUMENT ME! */
    private int evenElement;

    /** DOCUMENT ME! */
    private boolean borderPar;

    /** DOCUMENT ME! */
    private boolean errorPar;

/**
     * Constructor. Takes as arguments the mesh and the time step duration
     *
     * @param kadfc  kernel parent of the current process
     * @param m      mesh to consider
     * @param deltaT time step
     * @param contam indicates if the contaminant should be transported or not
     */
    public QuadraticCharacteristics(KernelADFC kadfc, NavierStokesMesh m,
        double deltaT, boolean contam) {
        kernel = kadfc;

        KernelADFCConfiguration config = kernel.getConfiguration();
        VERBOSE = CompiledData.VERBOSE;

        if (VERBOSE) {
            kernel.out("Initializing <B>Characteristics2D<B>");
        }

        this.mesh = m;
        this.deltaT = deltaT;
        contaminant = contam;
    }

    /**
     * principal method of the characteristics. Takes the values of the
     * magnitudes to be estudied in each of the nodes (cx, cy, cc, cd) and
     * returns a solution in those given vectors.
     *
     * @param cxa value component X of the magnitude in the nodes
     * @param cya value component Y of the magnitude in the nodes
     * @param cca value auxiliary magnitude 1 in the nodes
     * @param cda value auxiliary magnitude 2 in the nodes
     */
    public void calculate(double[] cxa, double[] cya, double[] cca, double[] cda) {
        int noConvergences;
        int totalIter;
        int contIter;

        double alfax;
        double alfay;
        double alfax2;
        double alfay2;
        double e;
        double dx;
        double dy;

        this.cx = cxa;
        this.cy = cya;
        this.cc = cca;
        this.cd = cda;

        if ((cc != null) && (cc2 == null)) {
            cc2 = new double[totalNodes];
        }

        if ((cd != null) && (cd2 == null)) {
            cd2 = new double[totalNodes];
        }

        long startTime = System.currentTimeMillis();
        notMoving = movingInside = toCleanBorder = toLeapBorder = 0;

        // For all the nodes
        noConvergences = 0;
        totalIter = 0;

        for (int j = 0; j < totalNodes; j++)
            if (isDirichlet[j]) {
                cx2[j] = cx[j];
                cy2[j] = cy[j];

                if (cc != null) {
                    cc2[j] = cc[j];
                }

                if (cd != null) {
                    cd2[j] = cd[j];
                }
            } else {
                double xn = x[j];
                double yn = y[j];

                // in which  element is j?
                int ne = reference[j];

                // System.out.println("Ref: "+ne);
                //////////////////////////////////////////////////////////////
                // Calculate foot of the characteristc, iterative method
                //////////////////////////////////////////////////////////////
                alfax = alfay = 0.;

                contIter = 0;

                do {
                    if (contIter == 0) {
                        // with the values in the nodes: we do not need values(...)
                        //alfax2 = deltaT * (15.0*vx[j] - 10.0*vx2[j]+3.0*vx3[j])/8.0;
                        //alfay2 = deltaT * (15.0*vy[j] - 10.0*vy2[j]+3.0*vy3[j])/8.0;
                        alfax2 = deltaT * ((1.5 * vx[j]) - (0.5 * vx2[j]));
                        alfay2 = deltaT * ((1.5 * vy[j]) - (0.5 * vy2[j]));
                    } else {
                        values(xn - (alfax / 2), yn - (alfay / 2), ne, true); // 0 <- ne

                        //alfax2 = deltaT * (15.0*vxPar - 10.0*vx2Par+3.0*vx3Par)/8.0;
                        //alfay2 = deltaT * (15.0*vyPar - 10.0*vy2Par+3.0*vy3Par)/8.0;
                        alfax2 = deltaT * ((1.5 * vxPar) - (0.5 * vx2Par));
                        alfay2 = deltaT * ((1.5 * vyPar) - (0.5 * vy2Par));
                    }

                    // convergence Criteria
                    dx = alfax2 - alfax;
                    dy = alfay2 - alfay;

                    alfax = alfax2;
                    alfay = alfay2;

                    e = Math.sqrt((dx * dx) + (dy * dy));
                    contIter++;

                    if (contIter > MAX_CONTITER) {
                        // codigo nuevo
                        if (VERBOSE) {
                            kernel.out(
                                "<FONT COLOR=#FF0000><B>MAX_CONTITER:</B> foot " +
                                xn + "," + yn + " * " + alfax + "," + alfay +
                                " / " + e + "</FONT>");
                        }

                        noConvergences++;

                        //alfax = deltaT*vxPar;
                        //alfay = deltaT*vyPar;
                        alfax = deltaT * vx[j];
                        alfay = deltaT * vy[j];

                        break;
                    }
                } while (e > DELTA_FOOT);

                totalIter += contIter;

                // It is done: we have the foot of the characteristic
                double piex = xn - alfax;
                double piey = yn - alfay;

                // New value
                values(piex, piey, ne, false);
                cx2[j] = cxPar;
                cy2[j] = cyPar;

                if (cc != null) {
                    cc2[j] = ccPar;
                }

                if (cd != null) {
                    cd2[j] = cdPar;
                }

                // System.out.println(j+" "+cxPar);
            }

        if (noConvergences > 0) {
            System.out.println(
                "<FONT COLOR=#FF0000><B>CharacteristicsCuad:</B> No convergence in " +
                noConvergences + " case(s).</FONT>");
        }

        if (ANTIDIFUSION) {
            correctAntidifusive(cx2);
            correctAntidifusive(cy2);

            if (cc != null) {
                correctAntidifusive(cc2);
            }

            //sacaCurvaTransformacion(cx,cx2);
            //corrigeMinMax(cx, cx2);
            //corrigeMinMax(cy, cy2);
            //if(cc!=null)corrigeMinMax(cc, cc2);
            //antidifusorPoisson(cx2,0);
            //antidifusorPoisson(cy2,1);
            characteristicForwardCorrection();
        }

        //else System.out.println("No antidifusion!");
        // Copy results
        for (int j = 0; j < totalNodes; j++)
            cxa[j] = cx2[j];

        for (int j = 0; j < totalNodes; j++)
            cya[j] = cy2[j];

        if (cca != null) {
            for (int j = 0; j < totalNodes; j++)
                cca[j] = cc2[j];
        }

        if (cda != null) {
            for (int j = 0; j < totalNodes; j++)
                cda[j] = cd2[j];
        }

        if (VERBOSE) {
            kernel.out("<B>CharacteristcsCuad:</B> done in " +
                ((System.currentTimeMillis() - startTime) / 1000.0) + " segs.");
        }

        if (VERBOSE) {
            kernel.out("NMoving=" + notMoving + " Moving=" + movingInside +
                " BClean=" + toCleanBorder + " BLeap=" + toLeapBorder +
                " MIter=" + ((double) totalIter / totalNodes));
        }
    }

    /**
     * experimental Method
     */
    public void characteristicForwardCorrection() {
        double F = 0.667;
        int ITERATIONS = 10;
        double[] fi1 = new double[reference.length];
        double[] fi2 = new double[reference.length];
        double[] fi3 = new double[reference.length];
        double[] fi4 = new double[reference.length];
        double[] fi5 = new double[reference.length];
        double[] fi6 = new double[reference.length];
        int[] el = new int[reference.length];
        boolean[] borde = new boolean[reference.length];
        int bordes = 0;

        System.out.println("Correction Characteristics phi� (F=" + F +
            ",iter=" + ITERATIONS + ")");

        for (int n = 0; n < cx.length; n++) {
            double dx = ((1.5 * vx[n]) - (0.5 * vx2[n])) * deltaT;
            double dy = ((1.5 * vy[n]) - (0.5 * vy2[n])) * deltaT;

            values(x[n] + dx, y[n] + dy, reference[n], false);
            fi1[n] = phi1;
            fi2[n] = phi2;
            fi3[n] = phi3;
            fi4[n] = phi4;
            fi5[n] = phi5;
            fi6[n] = phi6;
            el[n] = evenElement;
            borde[n] = borderPar || errorPar || (x[n] > 2.0) || (y[n] > 2.0) ||
                (y[n] < -2.0) || (x[n] < -2.0); // || (x[n]*x[n]+y[n]*y[n] > 1.0);

            if (borde[n]) {
                bordes++;
            }
        }

        System.out.println("bordes=" + bordes);

        double conv = 0;

        for (int iter = 0; iter < ITERATIONS; iter++) {
            conv = 0;

            for (int n = 0; n < cx.length; n++) {
                if (!borde[n]) {
                    double vAhorax = (cx2[n1[el[n]]] * fi1[n]) +
                        (cx2[n2[el[n]]] * fi2[n]) + (cx2[n3[el[n]]] * fi3[n]) +
                        (cx2[n4[el[n]]] * fi4[n]) + (cx2[n5[el[n]]] * fi5[n]) +
                        (cx2[n6[el[n]]] * fi6[n]);

                    double deltax = (cx[n] - vAhorax); ///(phi1+phi2+phi3+phi4+phi5+phi6);

                    //conv += Math.abs(deltax);
                    //System.out.println("delta="+delta);

                    /*
                    cx2[n1[el[n]]] += F*fi1[n]*fi1[n]*deltax;
                    cx2[n2[el[n]]] += F*fi2[n]*fi2[n]*deltax;
                    cx2[n3[el[n]]] += F*fi3[n]*fi3[n]*deltax;
                    cx2[n4[el[n]]] += F*fi4[n]*fi4[n]*deltax;
                    cx2[n5[el[n]]] += F*fi5[n]*fi5[n]*deltax;
                    cx2[n6[el[n]]] += F*fi6[n]*fi6[n]*deltax;
                     */
                    cx2[n1[el[n]]] += (F * fi1[n] * deltax);
                    cx2[n2[el[n]]] += (F * fi2[n] * deltax);
                    cx2[n3[el[n]]] += (F * fi3[n] * deltax);
                    cx2[n4[el[n]]] += (F * fi4[n] * deltax);
                    cx2[n5[el[n]]] += (F * fi5[n] * deltax);
                    cx2[n6[el[n]]] += (F * fi6[n] * deltax);

                    double vAhoray = (cy2[n1[el[n]]] * fi1[n]) +
                        (cy2[n2[el[n]]] * fi2[n]) + (cy2[n3[el[n]]] * fi3[n]) +
                        (cy2[n4[el[n]]] * fi4[n]) + (cy2[n5[el[n]]] * fi5[n]) +
                        (cy2[n6[el[n]]] * fi6[n]);

                    double deltay = (cy[n] - vAhoray); ///(phi1+phi2+phi3+phi4+phi5+phi6);

                    //conv += Math.abs(deltay);
                    //System.out.println("delta="+delta);

                    /*
                    cy2[n1[el[n]]] += F*fi1[n]*fi1[n]*deltay;
                    cy2[n2[el[n]]] += F*fi2[n]*fi2[n]*deltay;
                    cy2[n3[el[n]]] += F*fi3[n]*fi3[n]*deltay;
                    cy2[n4[el[n]]] += F*fi4[n]*fi4[n]*deltay;
                    cy2[n5[el[n]]] += F*fi5[n]*fi5[n]*deltay;
                    cy2[n6[el[n]]] += F*fi6[n]*fi6[n]*deltay;
                     */
                    cy2[n1[el[n]]] += (F * fi1[n] * deltay);
                    cy2[n2[el[n]]] += (F * fi2[n] * deltay);
                    cy2[n3[el[n]]] += (F * fi3[n] * deltay);
                    cy2[n4[el[n]]] += (F * fi4[n] * deltay);
                    cy2[n5[el[n]]] += (F * fi5[n] * deltay);
                    cy2[n6[el[n]]] += (F * fi6[n] * deltay);
                }
            }
        }

        //System.out.println("DELTA="+conv);
    }

    /* incorporates modifications' matrix */
    private double max(double a, double b) {
        if (Math.abs(a) > Math.abs(b)) {
            return a;
        } else {
            return b;
        }
    }

    /**
     * experimental Method
     */
    public void correctionForwardCharacteristics2() {
        double[] fi1 = new double[reference.length];
        double[] fi2 = new double[reference.length];
        double[] fi3 = new double[reference.length];
        double[] fi4 = new double[reference.length];
        double[] fi5 = new double[reference.length];
        double[] fi6 = new double[reference.length];
        int[] el = new int[reference.length];
        boolean[] isIgnored = new boolean[reference.length];
        int ignored = 0;
        double[] incx = new double[reference.length];
        double[] incy = new double[reference.length];
        int[] nInc = new int[reference.length];

        for (int n = 0; n < cx.length; n++) {
            double dx = ((1.5 * vx[n]) - (0.5 * vx2[n])) * deltaT;
            double dy = ((1.5 * vy[n]) - (0.5 * vy2[n])) * deltaT;

            values(x[n] + dx, y[n] + dy, reference[n], false);
            fi1[n] = phi1;
            fi2[n] = phi2;
            fi3[n] = phi3;
            fi4[n] = phi4;
            fi5[n] = phi5;
            fi6[n] = phi6;
            el[n] = evenElement;
            isIgnored[n] = borderPar || errorPar || (x[n] > 5.0) ||
                (y[n] > 5.0) || (y[n] < -5.0) || (x[n] < -5.0);

            if (isIgnored[n]) {
                ignored++;
            }

            nInc[n1[el[n]]]++;
            nInc[n2[el[n]]]++;
            nInc[n3[el[n]]]++;
            nInc[n4[el[n]]]++;
            nInc[n5[el[n]]]++;
            nInc[n6[el[n]]]++;
        }

        System.out.println("ignored=" + ignored);

        double conv = 0;

        for (int iter = 0; iter < 1; iter++) {
            conv = 0;

            for (int i = 0; i < cx.length; i++)
                incx[i] = incy[i] = 0.0;

            for (int n = 0; n < cx.length; n++) {
                if (!isIgnored[n]) {
                    double vAhorax = (cx2[n1[el[n]]] * fi1[n]) +
                        (cx2[n2[el[n]]] * fi2[n]) + (cx2[n3[el[n]]] * fi3[n]) +
                        (cx2[n4[el[n]]] * fi4[n]) + (cx2[n5[el[n]]] * fi5[n]) +
                        (cx2[n6[el[n]]] * fi6[n]);

                    double deltax = (cx[n] - vAhorax); ///(phi1+phi2+phi3+phi4+phi5+phi6);

                    //conv += Math.abs(deltax);
                    //System.out.println("delta="+delta);
                    double F = 1;

                    /*
                    incx[n1[el[n]]] += F*fi1[n]*fi1[n]*deltax;
                    incx[n2[el[n]]] += F*fi2[n]*fi2[n]*deltax;
                    incx[n3[el[n]]] += F*fi3[n]*fi3[n]*deltax;
                    incx[n4[el[n]]] += F*fi4[n]*fi4[n]*deltax;
                    incx[n5[el[n]]] += F*fi5[n]*fi5[n]*deltax;
                    incx[n6[el[n]]] += F*fi6[n]*fi6[n]*deltax;
                     */
                    incx[n1[el[n]]] = max(incx[n1[el[n]]],
                            F * fi1[n] * fi1[n] * deltax);
                    incx[n2[el[n]]] = max(incx[n2[el[n]]],
                            F * fi2[n] * fi2[n] * deltax);
                    incx[n3[el[n]]] = max(incx[n3[el[n]]],
                            F * fi3[n] * fi3[n] * deltax);
                    incx[n4[el[n]]] = max(incx[n4[el[n]]],
                            F * fi4[n] * fi4[n] * deltax);
                    incx[n5[el[n]]] = max(incx[n5[el[n]]],
                            F * fi5[n] * fi5[n] * deltax);
                    incx[n6[el[n]]] = max(incx[n6[el[n]]],
                            F * fi6[n] * fi6[n] * deltax);

                    double vAhoray = (cy2[n1[el[n]]] * fi1[n]) +
                        (cy2[n2[el[n]]] * fi2[n]) + (cy2[n3[el[n]]] * fi3[n]) +
                        (cy2[n4[el[n]]] * fi4[n]) + (cy2[n5[el[n]]] * fi5[n]) +
                        (cy2[n6[el[n]]] * fi6[n]);

                    double deltay = (cy[n] - vAhoray); ///(phi1+phi2+phi3+phi4+phi5+phi6);

                    //conv += Math.abs(deltay);
                    //System.out.println("delta="+delta);

                    /*
                    incy[n1[el[n]]] += F*fi1[n]*fi1[n]*deltay;
                    incy[n2[el[n]]] += F*fi2[n]*fi2[n]*deltay;
                    incy[n3[el[n]]] += F*fi3[n]*fi3[n]*deltay;
                    incy[n4[el[n]]] += F*fi4[n]*fi4[n]*deltay;
                    incy[n5[el[n]]] += F*fi5[n]*fi5[n]*deltay;
                    incy[n6[el[n]]] += F*fi6[n]*fi6[n]*deltay;
                     */
                    incy[n1[el[n]]] = max(incy[n1[el[n]]],
                            F * fi1[n] * fi1[n] * deltay);
                    incy[n2[el[n]]] = max(incy[n2[el[n]]],
                            F * fi2[n] * fi2[n] * deltay);
                    incy[n3[el[n]]] = max(incy[n3[el[n]]],
                            F * fi3[n] * fi3[n] * deltay);
                    incy[n4[el[n]]] = max(incy[n4[el[n]]],
                            F * fi4[n] * fi4[n] * deltay);
                    incy[n5[el[n]]] = max(incy[n5[el[n]]],
                            F * fi5[n] * fi5[n] * deltay);
                    incy[n6[el[n]]] = max(incy[n6[el[n]]],
                            F * fi6[n] * fi6[n] * deltay);
                }
            }

            for (int i = 0; i < cx2.length; i++) {
                if (nInc[i] > 0) {
                    cx2[i] += incx[i]; // /nInc[i];
                    cy2[i] += incy[i]; // /nInc[i];
                }
            }
        }

        System.out.println("DELTA=" + conv);
    }

    /**
     * experimental Method
     *
     * @param a DOCUMENT ME!
     * @param coord DOCUMENT ME!
     */
    public void antidifusorPoisson(double[] a, int coord) {
        double beta = 0.1 / 2000;
        Matrix mmas = mesh.getMassMatrixQuad();
        Matrix mrig = mesh.getRigidityMatrixQuad();
        Matrix d;
        Matrix e;
        d = new Matrix(mmas, true);
        e = new Matrix(mmas, true);
        d.sumMatrix(mrig, -beta);
        e.sumMatrix(mrig, +beta);

        GCCholeskyImpl gc = new GCCholeskyImpl(kernel);
        gc.setCoeficientMatrix(d);
        gc.setDirichlet(mesh.getNodesDirichletQuad(),
            new double[mesh.getValuesDirichletQuad()[coord].length]);

        double[] f = new double[a.length];
        f = e.multiply(f, a);

        gc.setB(f);

        gc.resolve(a);
    }

    /**
     * experimental Method
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void obtainTransformationCurve(double[] x, double[] y) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(kernel.getConfiguration().meshName +
                        ".CT" + (numero++)));

            for (int i = 0; i < x.length; i++)
                pw.println("" + x[i] + "\t" + y[i]);

            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * experimental Method
     *
     * @param c DOCUMENT ME!
     */
    public void correctAntidifusive(double[] c) {
        double[] c2 = new double[c.length];
        int[] nvec = new int[c.length];
        double F = 0.001;
        double DIST = 0.02;
        double atenuacion;

        int[] ipos = mesh.getIposQuad();
        int[] jpos = mesh.getJposQuad();
        double[][] xy = mesh.getCoordinatesQuad();

        for (int i = 0; i < (ipos.length - 1); i++)
            for (int n = ipos[i]; n < ipos[i + 1]; n++) {
                int j = jpos[n];

                if (i != j) {
                    double dx;
                    double dy;
                    double dist;
                    dx = xy[0][i] - xy[0][j];
                    dy = xy[1][i] - xy[1][j];
                    dist = Math.sqrt((dx * dx) + (dy * dy));

                    atenuacion = DIST / dist;
                    atenuacion *= atenuacion;

                    c2[i] += (c[j] * atenuacion);
                    nvec[i]++;
                    c2[j] += (c[i] * atenuacion);
                    nvec[j]++;
                }
            }

        for (int j = 0; j < c.length; j++)
            c[j] = ((1.0 + F) * c[j]) - ((F * c2[j]) / nvec[j]);
    }

    /**
     * experimental Method
     *
     * @param orig DOCUMENT ME!
     * @param trans DOCUMENT ME!
     */
    public void correctMinMax(double[] orig, double[] trans) {
        double minOrig = 100000.0;
        double maxOrig = -100000.0;
        double mediaOrig = 0.0;
        double minTrans = 100000.0;
        double maxTrans = -100000.0;
        double mediaTrans = 0.0;

        System.out.println("Correction characteristics MinMax");

        for (int i = 0; i < orig.length; i++) {
            if (orig[i] < minOrig) {
                minOrig = orig[i];
            }

            if (orig[i] > maxOrig) {
                maxOrig = orig[i];
            }

            mediaOrig += (orig[i] / orig.length);

            if (trans[i] < minTrans) {
                minTrans = trans[i];
            }

            if (trans[i] > maxTrans) {
                maxTrans = trans[i];
            }

            mediaTrans += (trans[i] / trans.length);
        }

        //System.out.println("mo="+minOrig+" Mo="+maxOrig+" mT="+minTrans+" MT="+maxTrans);
        int cUp = 0;

        //System.out.println("mo="+minOrig+" Mo="+maxOrig+" mT="+minTrans+" MT="+maxTrans);
        int cDown = 0;
        double div = 10;

        for (int i = 0; i < orig.length; i++) {
            if (trans[i] < ((((div - 1) * minTrans) + maxTrans) / div)) {
                trans[i] = minOrig +
                    (((trans[i] - minTrans) * ((((div - 1) * minTrans) +
                    maxTrans) - (div * minOrig))) / (maxTrans - minTrans));

                cDown++;
            } else if (trans[i] > ((((div - 1) * maxTrans) + minTrans) / div)) {
                trans[i] = maxOrig +
                    (((trans[i] - maxTrans) * ((((div - 1) * maxTrans) +
                    minTrans) - (div * maxOrig))) / (minTrans - maxTrans));
                cUp++;
            }
        }

        System.out.println("Cup=" + cUp + "\tCdown=" + cDown + "\tdiv=" + div);
    }

    // Returns an element by the side of 'elem' which has
    /**
     * DOCUMENT ME!
     *
     * @param r1 DOCUMENT ME!
     * @param r2 DOCUMENT ME!
     * @param elem DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int nearbyElement(int r1, int r2, int elem) {
        for (int p1 = iCNLE[r1]; p1 < iCNLE[r1 + 1]; p1++)
            for (int p2 = iCNLE[r2]; p2 < iCNLE[r2 + 1]; p2++)
                if (CNLE[p1] == CNLE[p2]) {
                    if (CNLE[p1] != elem) {
                        // if(CNLE[p1] != -1)
                        return CNLE[p1];
                    }
                }

        if (VERBOSE) {
            kernel.out("<FONT COLOR=#FF0000><B>NearbyElement:</B> fail in " +
                r1 + " " + r2 + " " + elem + "</FONT>");
        }

        return -1;
    }

    /**
     * Call this method before calculate(). The invocation is done by
     * default if needed.
     */
    public void initializeCaches() {
        kernel.out("Initializing caches of <B>Characteristics2D</B>");

        totalNodes = mesh.getCoordinatesQuad(0).length;
        cx2 = new double[totalNodes];
        cy2 = new double[totalNodes];

        System.out.println("   * Loading cache node-element");
        totalElements = mesh.getNodesQuad(0).length;

        x = mesh.getCoordinatesQuad(0);
        y = mesh.getCoordinatesQuad(1);

        // change respect Caracteristicas.java
        n1 = mesh.getNodesQuad(0);
        n2 = mesh.getNodesQuad(1);
        n3 = mesh.getNodesQuad(2);
        n4 = mesh.getNodesQuad(3);
        n5 = mesh.getNodesQuad(4);
        n6 = mesh.getNodesQuad(5);

        int[] li1;
        int[] li2;
        int[] li3;
        li1 = mesh.getNodes(0);
        li2 = mesh.getNodes(1);
        li3 = mesh.getNodes(2);

        // cached data for calculations
        A = new double[totalElements];
        B = new double[totalElements];
        C = new double[totalElements];
        D = new double[totalElements];
        DetCr = new double[totalElements];

        System.out.println("   * generating caches a1c,a2c,a3c,b1c,b2c,b3c");

        for (int n = 0; n < totalElements; n++) {
            A[n] = x[n2[n]] - x[n1[n]];
            B[n] = x[n3[n]] - x[n1[n]];
            C[n] = y[n2[n]] - y[n1[n]];
            D[n] = y[n3[n]] - y[n1[n]];

            DetCr[n] = (A[n] * D[n]) - (B[n] * C[n]);
        }

        System.out.println(
            "   * generanting caches adjacent12, adjacent13, adjacent23");

        CNLE = mesh.getCacheNodeLinealElement();
        iCNLE = mesh.getStartCacheNodeLinealElement();

        adjacent12 = new int[totalElements];
        adjacent23 = new int[totalElements];
        adjacent13 = new int[totalElements];
        reference = mesh.elementReferencesNodeQuad();

        int successes = 0;

        for (int j = 0; j < totalElements; j++) {
            if ((adjacent12[j] = nearbyElement(li1[j], li2[j], j)) != -1) {
                successes++;
            }

            if ((adjacent23[j] = nearbyElement(li2[j], li3[j], j)) != -1) {
                successes++;
            }

            if ((adjacent13[j] = nearbyElement(li1[j], li3[j], j)) != -1) {
                successes++;
            }
        }

        kernel.out("<B>Exitos</B> in " + successes + "<B>/</B>" +
            (totalElements * 3) + " aristas");

        kernel.out("bitmap dirichlet");

        int[] nd = mesh.getNodesDirichletQuad();
        isDirichlet = new boolean[x.length];

        for (int j = 0; j < nd.length; j++)
            isDirichlet[nd[j]] = true;
    }

    /**
     * Indicatestes the values of the two speed components in each node
     * at the instants n and (n-1)
     *
     * @param vxa component x of the speed at instant n
     * @param vya component y of the speed at instant n
     * @param vxb component x of the speed at instant n-1
     * @param vyb component y of the speed at instant n-1
     */
    public void setSpeedFields(double[] vxa, double[] vya, double[] vxb,
        double[] vyb) //, double[] vxc, double[] vyc) {
     {
        // System.out.println("   * New definition of the field.");
        this.vx = vxa;
        this.vy = vya;
        this.vx2 = vxb;
        this.vy2 = vyb;

        // this.vx3 = vxc;
        // this.vy3 = vyc;
    }

    // returns the index of the element that contains (x,y)
    /**
     * DOCUMENT ME!
     *
     * @param xp DOCUMENT ME!
     * @param yp DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int slalg(double xp, double yp, int n) {
        double p;
        double q;
        int old;
        int direction = 4;

        int contIter = 0;
        borderPar = errorPar = false;

        do {
            double x1;
            double y1;
            x1 = x[n1[n]];
            y1 = y[n1[n]];
            p = (((xp - x1) * D[n]) - (B[n] * (yp - y1))) / DetCr[n];
            q = ((A[n] * (yp - y1)) - ((xp - x1) * C[n])) / DetCr[n];

            // We search new element to continue the search...
            // The ELSE-IF are compulsory.
            old = n;

            // If it is outside...
            if ((p <= -DELTA_PQ) || (q <= -DELTA_PQ) ||
                    ((p + q) >= (1 + DELTA_PQ))) {
                /*
                // METODO DE PERTENECIA A BISECTRICES
                /
                /
                double xi1 = -0.3826834326*(p-0.2928932188)-0.9238795325*(q-0.2928932188);
                double xi2 = +0.9238795325*(p-0.2928932188)+0.3826834326*(q-0.2928932188);
                double xi3 = +1.0000000000*(p-0.2928932188)-1.0000000000*(q-0.2928932188);
                
                // left
                if(xi2 < 0 && xi3 < 0) n = adjacent13[n];
                // abajo
                else if(xi1 > 0 && xi3 > 0) n = adjacent12[n];
                // diagonal
                else if(xi1 < 0 && xi2 > 0) n = adjacent23[n];
                 */
                double dDiag = Math.abs((p + q) - 1) / Math.sqrt(2);

                if (p < 0.0) {
                    if (q > 0.0) {
                        if (q < 1.0) {
                            direction = 1; // n = adjacent13[n];
                        } else if (dDiag < -p) {
                            direction = 1; // n = adjacent13[n];
                        } else {
                            direction = 2; // n = adjacent23[n];
                        }
                    } else if (-q < -p) {
                        direction = 1; // n = adjacent13[n];
                    } else {
                        direction = 0; // n = adjacent12[n];
                    }
                } else if (q < 0.0) {
                    if (p > 1.0) {
                        if (-q < dDiag) {
                            direction = 2; //n = adjacent23[n];
                        } else {
                            direction = 0; // n = adjacent12[n];
                        }
                    } else {
                        direction = 0; // n=adjacent12[n];
                    }
                } else {
                    direction = 2; //n=adjacent23[n];
                }

                // searching
                switch (direction) {
                case 0: // down

                    if (adjacent12[n] == -1) {
                        if ((p < 1) && (p > 0)) // facil
                         {
                            toCleanBorder++;
                            q = 0;
                            n = old;
                            borderPar = true;
                        } else if (p < 0.5) {
                            toLeapBorder++;

                            if (adjacent13[n] == -1) {
                                n = adjacent23[n];
                            } else {
                                n = adjacent13[n];
                            }
                        } else {
                            toLeapBorder++;

                            if (adjacent23[n] == -1) {
                                n = adjacent13[n];
                            } else {
                                n = adjacent23[n];
                            }
                        }
                    } else {
                        n = adjacent12[n];
                    }

                    break;

                case 1: // by left

                    if (adjacent13[n] == -1) {
                        if ((q < 1) && (q > 0)) {
                            toCleanBorder++;
                            p = 0;
                            n = old;
                            borderPar = true;
                        } else if (q < 0.5) {
                            toLeapBorder++;

                            if (adjacent12[n] == -1) {
                                n = adjacent23[n];
                            } else {
                                n = adjacent12[n];
                            }
                        } else {
                            toLeapBorder++;

                            if (adjacent23[n] == -1) {
                                n = adjacent12[n];
                            } else {
                                n = adjacent23[n];
                            }
                        }
                    } else {
                        n = adjacent13[n];
                    }

                    break;

                case 2: // by diagonal: CORRECTED!!

                    if (adjacent23[n] == -1) {
                        if (((p - q) > -1) && ((p - q) < 1)) {
                            toCleanBorder++;

                            double a = ((1 + p) - q) / 2;
                            double b = q - p + a;
                            p = a;
                            q = b;
                            n = old;
                            borderPar = true;
                        } else if ((p - q) > 0) {
                            toLeapBorder++;

                            if (adjacent12[n] == -1) {
                                n = adjacent13[n];
                            } else {
                                n = adjacent12[n];
                            }
                        } else {
                            toLeapBorder++;

                            if (adjacent13[n] == -1) {
                                n = adjacent12[n];
                            } else {
                                n = adjacent13[n];
                            }
                        }
                    } else {
                        n = adjacent23[n];
                    }

                    break;

                default:
                    System.out.println("ErRor InTeRnO! " + direction);
                }
            } else {
                if (contIter == 0) {
                    notMoving++;
                } else {
                    movingInside++;
                }
            }

            // Whenever there is a change of element, start again
            contIter++;

            if (contIter > MAX_CONTITER) {
                System.err.println(
                    "</FONT COLOR=#FF0000><B>MAX_CONTITER</B>: SLALG " + xp +
                    "," + yp + " * " + p + "," + q + "</FONT>");

                /*
                 kernel.nuevoDialogoErrorFatal(
                "The characteristics (SLALG) have failed.\n"
                +"It is impossible to continue with the processing, because \n"
                +"previsibly global convergence will be \n"
                +"degraded and the solver will return chaotic results\n");
                 */
                pPar = p;
                qPar = q;
                errorPar = true;

                // Txao. -> Goodbye
                return n;

                //break;
            }
        } while (old != n);

        pPar = p;
        qPar = q;

        // Txao.
        return n;
    }

    /**
     * Method that returns the values of vx, vy and (cx,cy) at the
     * point (x,y), and receives elem_ref to help. completo serves to indicate
     * the fields that are being calculated. If false,  cxPar y cyPar are
     * calculated. If true,   vxPar, vyPar, vx2Par and vy2Par are  calculated.
     * True is for the iterative bucle of the calculus of the foot of the +
     * characteristics. false when the foot is already known.
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double correct(double a) {
        double DELTA = 0.01;
        double b = 0.;

        if (a < DELTA) {
            b = 0.0;
        } else if (a > (1.0 - DELTA)) {
            b = 1.0;
        }

        // else b = (a-DELTA)/(1.0-2.0*DELTA);
        return b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param xp DOCUMENT ME!
     * @param yp DOCUMENT ME!
     * @param elem_ref DOCUMENT ME!
     * @param iter DOCUMENT ME!
     */
    private void values(double xp, double yp, int elem_ref, boolean iter) {
        double p;
        double q;
        double x1;
        double y1;
        double l1;
        double l2;
        double l3;
        double max;
        double min;

        evenElement = slalg(xp, yp, elem_ref);

        int n = evenElement;

        p = pPar;
        q = qPar;

        // Functions quadratic base
        l1 = 1.0 - p - q;
        l2 = p;
        l3 = q;

        //l1=corrige(l1);
        //l2=corrige(l2);
        //l3=corrige(l3);
        phi1 = l1 * ((2.0 * l1) - 1.0);
        phi2 = l2 * ((2.0 * l2) - 1.0);
        phi3 = l3 * ((2.0 * l3) - 1.0);
        phi4 = 4.0 * l1 * l2;
        phi5 = 4.0 * l2 * l3;
        phi6 = 4.0 * l1 * l3;

        if (iter) {
            // Interpolamos los parameters
            // Vx t=0
            vxPar = (vx[n1[n]] * phi1) + (vx[n2[n]] * phi2) +
                (vx[n3[n]] * phi3) + (vx[n4[n]] * phi4) + (vx[n5[n]] * phi5) +
                (vx[n6[n]] * phi6);

            min = max = vx[n1[n]];

            if (max < vx[n2[n]]) {
                max = vx[n2[n]];
            }

            if (min > vx[n2[n]]) {
                min = vx[n2[n]];
            }

            if (max < vx[n3[n]]) {
                max = vx[n3[n]];
            }

            if (min > vx[n3[n]]) {
                min = vx[n3[n]];
            }

            if (vxPar < min) {
                vxPar = min;
            }

            if (vxPar > max) {
                vxPar = max;
            }

            // Vy y=0
            vyPar = (vy[n1[n]] * phi1) + (vy[n2[n]] * phi2) +
                (vy[n3[n]] * phi3) + (vy[n4[n]] * phi4) + (vy[n5[n]] * phi5) +
                (vy[n6[n]] * phi6);

            min = max = vy[n1[n]];

            if (max < vy[n2[n]]) {
                max = vy[n2[n]];
            }

            if (min > vy[n2[n]]) {
                min = vy[n2[n]];
            }

            if (max < vy[n3[n]]) {
                max = vy[n3[n]];
            }

            if (min > vy[n3[n]]) {
                min = vy[n3[n]];
            }

            if (vyPar < min) {
                vyPar = min;
            }

            if (vyPar > max) {
                vyPar = max;
            }

            // Vx t-1
            vx2Par = (vx2[n1[n]] * phi1) + (vx2[n2[n]] * phi2) +
                (vx2[n3[n]] * phi3) + (vx2[n4[n]] * phi4) +
                (vx2[n5[n]] * phi5) + (vx2[n6[n]] * phi6);

            min = max = vx2[n1[n]];

            if (max < vx2[n2[n]]) {
                max = vx2[n2[n]];
            }

            if (min > vx2[n2[n]]) {
                min = vx2[n2[n]];
            }

            if (max < vx2[n3[n]]) {
                max = vx2[n3[n]];
            }

            if (min > vx2[n3[n]]) {
                min = vx2[n3[n]];
            }

            if (vx2Par < min) {
                vx2Par = min;
            }

            if (vx2Par > max) {
                vx2Par = max;
            }

            // Vy t-1
            vy2Par = (vy2[n1[n]] * phi1) + (vy2[n2[n]] * phi2) +
                (vy2[n3[n]] * phi3) + (vy2[n4[n]] * phi4) +
                (vy2[n5[n]] * phi5) + (vy2[n6[n]] * phi6);

            min = max = vy2[n1[n]];

            if (max < vy2[n2[n]]) {
                max = vy2[n2[n]];
            }

            if (min > vy2[n2[n]]) {
                min = vy2[n2[n]];
            }

            if (max < vy2[n3[n]]) {
                max = vy2[n3[n]];
            }

            if (min > vy2[n3[n]]) {
                min = vy2[n3[n]];
            }

            if (vy2Par < min) {
                vy2Par = min;
            }

            if (vy2Par > max) {
                vy2Par = max;
            }

            // Vx t-2

            /*
            vx3Par = vx3[n1[n]]*phi1+vx3[n2[n]]*phi2
            +vx3[n3[n]]*phi3+vx3[n4[n]]*phi4
            +vx3[n5[n]]*phi5+vx3[n6[n]]*phi6;
            
            min = max = vx3[n1[n]];
            if(max < vx3[n2[n]]) max = vx3[n2[n]];
            if(min > vx3[n2[n]]) min = vx3[n2[n]];
            if(max < vx3[n3[n]]) max = vx3[n3[n]];
            if(min > vx3[n3[n]]) min = vx3[n3[n]];
            
            if(vx3Par < min) vx3Par = min;
            if(vx3Par > max) vx3Par = max;
            
            // Vy t-2
            vy3Par = vy3[n1[n]]*phi1+vy3[n2[n]]*phi2
            +vy3[n3[n]]*phi3+vy3[n4[n]]*phi4
            +vy3[n5[n]]*phi5+vy3[n6[n]]*phi6;
            
            min = max = vy3[n1[n]];
            if(max < vy3[n2[n]]) max = vy3[n2[n]];
            if(min > vy3[n2[n]]) min = vy3[n2[n]];
            if(max < vy3[n3[n]]) max = vy3[n3[n]];
            if(min > vy3[n3[n]]) min = vy3[n3[n]];
            
            if(vy3Par < min) vy3Par = min;
            if(vy3Par > max) vy3Par = max;
             */
        } else {
            cxPar = (cx[n1[n]] * phi1) + (cx[n2[n]] * phi2) +
                (cx[n3[n]] * phi3) + (cx[n4[n]] * phi4) + (cx[n5[n]] * phi5) +
                (cx[n6[n]] * phi6);

            min = max = cx[n1[n]];

            if (max < cx[n2[n]]) {
                max = cx[n2[n]];
            }

            if (min > cx[n2[n]]) {
                min = cx[n2[n]];
            }

            if (max < cx[n3[n]]) {
                max = cx[n3[n]];
            }

            if (min > cx[n3[n]]) {
                min = cx[n3[n]];
            }

            if (cxPar < min) {
                cxPar = min;
            }

            if (cxPar > max) {
                cxPar = max;
            }

            cyPar = (cy[n1[n]] * phi1) + (cy[n2[n]] * phi2) +
                (cy[n3[n]] * phi3) + (cy[n4[n]] * phi4) + (cy[n5[n]] * phi5) +
                (cy[n6[n]] * phi6);

            min = max = cy[n1[n]];

            if (max < cy[n2[n]]) {
                max = cy[n2[n]];
            }

            if (min > cy[n2[n]]) {
                min = cy[n2[n]];
            }

            if (max < cy[n3[n]]) {
                max = cy[n3[n]];
            }

            if (min > cy[n3[n]]) {
                min = cy[n3[n]];
            }

            if (cyPar < min) {
                cyPar = min;
            }

            if (cyPar > max) {
                cyPar = max;
            }

            if (cc != null) {
                ccPar = (cc[n1[n]] * phi1) + (cc[n2[n]] * phi2) +
                    (cc[n3[n]] * phi3) + (cc[n4[n]] * phi4) +
                    (cc[n5[n]] * phi5) + (cc[n6[n]] * phi6);

                min = max = cc[n1[n]];

                if (max < cc[n2[n]]) {
                    max = cc[n2[n]];
                }

                if (min > cc[n2[n]]) {
                    min = cc[n2[n]];
                }

                if (max < cc[n3[n]]) {
                    max = cc[n3[n]];
                }

                if (min > cc[n3[n]]) {
                    min = cc[n3[n]];
                }

                if (ccPar < min) {
                    ccPar = min;
                }

                if (ccPar > max) {
                    ccPar = max;
                }
            }

            if (cd != null) {
                cdPar = (cd[n1[n]] * phi1) + (cd[n2[n]] * phi2) +
                    (cd[n3[n]] * phi3) + (cd[n4[n]] * phi4) +
                    (cd[n5[n]] * phi5) + (cd[n6[n]] * phi6);

                min = max = cd[n1[n]];

                if (max < cd[n2[n]]) {
                    max = cd[n2[n]];
                }

                if (min > cd[n2[n]]) {
                    min = cd[n2[n]];
                }

                if (max < cd[n3[n]]) {
                    max = cd[n3[n]];
                }

                if (min > cd[n3[n]]) {
                    min = cd[n3[n]];
                }

                if (cdPar < min) {
                    cdPar = min;
                }

                if (cdPar > max) {
                    cdPar = max;
                }
            }
        }

        /*        if(xp*xp + yp*yp < 0.5*0.5)
                {
                        kernel.out("BOUNDS : "+vxPar+" "+vyPar+" "+vx2Par+" "+vy2Par);
                        vxPar=vyPar=vx2Par=vy2Par=ccPar=0.0;
        
                }
         */
    }
}
