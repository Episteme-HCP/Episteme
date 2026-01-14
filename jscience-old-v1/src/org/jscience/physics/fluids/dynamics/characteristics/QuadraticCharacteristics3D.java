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
import org.jscience.physics.fluids.dynamics.mesh.NavierStokesMesh;


/**
 * This class calculates the values of the different magnitudes cx, cy, cc
 * y cd at the root of the caracteristics for a 3D meshing with a speed field.
 * Tipically these magnitudes correspond with the three speed components x, y
 * and z and with the concentration of a contaminant.
 */
public class QuadraticCharacteristics3D {
    /**
     * This value indicates the precision around the borders of the
     * triangular elements for the parameters p and q.
     */
    private final static double DELTA_PQ = 0.000001;

    /**
     * Convergence Delta for the iterative method for the calculus of
     * the feet of the characteristics
     */
    private final static double DELTA_FOOT = 0.000001;

    /**
     * In the iterative algorithms (characteristics, slalg...) controls
     * the count before doing a forced interrumption. Serves to avoid
     * blockages.
     */
    private final static int MAX_CONTITER = 96;

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
    private double[] vz;

    /** DOCUMENT ME! */
    private double[] vx2;

    /** DOCUMENT ME! */
    private double[] vy2;

    /** DOCUMENT ME! */
    private double[] vz2;

    /** DOCUMENT ME! */
    private double[] x;

    /** DOCUMENT ME! */
    private double[] y;

    /** DOCUMENT ME! */
    private double[] z;

    /** DOCUMENT ME! */
    private double[] cx;

    /** DOCUMENT ME! */
    private double[] cy;

    /** DOCUMENT ME! */
    private double[] cz;

    /** DOCUMENT ME! */
    private double[] cc;

    // solutions for getC?
    /** DOCUMENT ME! */
    private double[] cx2;

    // solutions for getC?
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double[] cy2;

    // solutions for getC?
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double[] cz2;

    // solutions for getC?
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double[] cc2;

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
    private int[] n7;

    /** DOCUMENT ME! */
    private int[] n8;

    /** DOCUMENT ME! */
    private int[] n9;

    /** DOCUMENT ME! */
    private int[] na;

    /** DOCUMENT ME! */
    private double[] invJ11;

    /** DOCUMENT ME! */
    private double[] invJ12;

    /** DOCUMENT ME! */
    private double[] invJ13;

    /** DOCUMENT ME! */
    private double[] invJ21;

    /** DOCUMENT ME! */
    private double[] invJ22;

    /** DOCUMENT ME! */
    private double[] invJ23;

    /** DOCUMENT ME! */
    private double[] invJ31;

    /** DOCUMENT ME! */
    private double[] invJ32;

    /** DOCUMENT ME! */
    private double[] invJ33;

    /** DOCUMENT ME! */
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
    private double vzPar;

    // Used to give back the values of the function 'values'
    // I do not like to use global variables for this but ...
    /** DOCUMENT ME! */
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
    /** DOCUMENT ME! */
    private double vy2Par;

    // Used to give back the values of the function 'values'
    // I do not like to use global variables for this but ...
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double vz2Par;

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
    private double czPar;

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
    /** DOCUMENT ME! */
    private double rPar;

    /** caches of adjacent elements */
    private int[] adjacent123;

    /** caches of adjacent elements */
    private int[] adjacent124;

    /** caches of adjacent elements */
    private int[] adjacent134;

    /** caches of adjacent elements */
    private int[] adjacent234;

    /** use of contaminant */
    private boolean contaminant;

/**
     * Constructor. Takes as arguments the mesh and the time step duration
     *
     * @param kadfc  kernel parent of the current process
     * @param m      mesh to consider
     * @param deltaT time step
     * @param contam indicates if the contaminant should be transported or not
     */
    public QuadraticCharacteristics3D(KernelADFC kadfc, NavierStokesMesh m,
        double deltaT, boolean contam) {
        kernel = kadfc;
        VERBOSE = CompiledData.VERBOSE;

        if (VERBOSE) {
            kernel.out("Inicializando <B>Caracteristicas3D<B>");
        }

        this.mesh = m;
        this.deltaT = deltaT;
        contaminant = contam;
    }

    /**
     * Calculates the values transported by the vectors which are pased
     * as arguments. The new values are placed in those same vectors.
     *
     * @param cxa component x of the speed
     * @param cya component y of the speed
     * @param cza component z of the speed
     * @param cca contaminant concentration
     */
    public void calculate(double[] cxa, double[] cya, double[] cza, double[] cca) {
        int noConvergences;

        double alfax;
        double alfay;
        double alfaz;
        double alfax2;
        double alfay2;
        double alfaz2;
        double e;
        double dx;
        double dy;
        double dz;

        this.cx = cxa;
        this.cy = cya;
        this.cz = cza;
        this.cc = cca;

        long startTime = System.currentTimeMillis();

        // For all nodes
        noConvergences = 0;

        for (int j = 0; j < totalNodes; j++) {
            double xn = x[j];
            double yn = y[j];
            double zn = z[j];

            // in which element is j?
            int ne = reference[j];

            // System.out.println("Ref: "+ne);
            //////////////////////////////////////////////////////////////
            // Calculate foot of the characteristc, iterative method
            //////////////////////////////////////////////////////////////
            alfax = alfay = alfaz = 0.;

            int contIter = 0;

            do {
                values(xn - (alfax / 2), yn - (alfay / 2), zn - (alfaz / 2),
                    ne, true); // 0 <- ne
                alfax2 = deltaT * ((1.5 * vxPar) - (0.5 * vx2Par));
                alfay2 = deltaT * ((1.5 * vyPar) - (0.5 * vy2Par));
                alfaz2 = deltaT * ((1.5 * vzPar) - (0.5 * vz2Par));

                // convergence criteria
                dx = alfax2 - alfax;
                dy = alfay2 - alfay;
                dz = alfaz2 - alfaz;

                alfax = alfax2;
                alfay = alfay2;
                alfaz = alfaz2;

                e = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
                contIter++;

                if (contIter > MAX_CONTITER) {
                    if (VERBOSE) {
                        kernel.out(
                            "<FONT COLOR=#FF0000><B>MAX_CONTITER:</B> pie " +
                            xn + "," + yn + "," + zn + " * " + alfax + "," +
                            alfay + "," + alfaz + " / " + e + "</FONT>");
                    }

                    noConvergences++;

                    // If no convergence...
                    // old code:
                    /*
                    alfax = deltaT*(1.5*vxPar - 0.5*vx2Par);
                    alfay = deltaT*(1.5*vyPar - 0.5*vy2Par);
                    alfaz = deltaT*(1.5*vzPar - 0.5*vz2Par);
                     **/

                    // new code: we stay with the first aproximation.
                    // possibly the rest are degenerated.
                    values(xn, yn, zn, ne, true);
                    alfax = deltaT * vxPar;
                    alfay = deltaT * vyPar;
                    alfaz = deltaT * vzPar;

                    break;
                }
            } while (e > DELTA_FOOT);

            // It is done: we have the foot of the characteristic
            double piex = xn - alfax;
            double piey = yn - alfay;
            double piez = zn - alfaz;

            // New value
            values(piex, piey, piez, ne, false);
            cx2[j] = cxPar;
            cy2[j] = cyPar;
            cz2[j] = czPar;

            if (cc != null) {
                cc2[j] = ccPar;
            }

            // System.out.println(j+" "+cxPar);
        }

        if (noConvergences > 0) {
            // kernel.out
            System.out.println(
                "<FONT COLOR=#FF0000><B>CharacteristicsCuad:</B> No convergence in " +
                noConvergences + " case(s).</FONT>");
        }

        // Copy the results
        for (int j = 0; j < totalNodes; j++)
            cxa[j] = cx2[j];

        for (int j = 0; j < totalNodes; j++)
            cya[j] = cy2[j];

        for (int j = 0; j < totalNodes; j++)
            cza[j] = cz2[j];

        if (cca != null) {
            for (int j = 0; j < totalNodes; j++)
                cca[j] = cc2[j];
        }

        if (VERBOSE) {
            kernel.out("<B>CharacteristicsCuad:</B> done in " +
                ((System.currentTimeMillis() - startTime) / 1000.0) + " segs.");
        }
    }

    /**
     * Returns an element by the side of 'elem' which has in common
     * with it nodes r1, r2 y r3. The checking is as follows: 1.- the two must
     * be equals 2.- they should not refer to the element origen (argument)
     * 3.- not be -1. THE FACT OF CACHING THESE VALUES - PRODUCE A PENALTY OF
     * 12 bytes  nElem - JUST A SAVING OF 3% IN THE PROCESSING TIME
     *
     * @param r1 DOCUMENT ME!
     * @param r2 DOCUMENT ME!
     * @param r3 DOCUMENT ME!
     * @param elem DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int nearbyElement(int r1, int r2, int r3, int elem) {
        for (int p1 = iCNLE[r1]; p1 < iCNLE[r1 + 1]; p1++)
            for (int p2 = iCNLE[r2]; p2 < iCNLE[r2 + 1]; p2++)
                if (CNLE[p1] == CNLE[p2]) {
                    //if(elemNod[r1][p1] != -1)
                    if (CNLE[p1] != elem) {
                        for (int p3 = iCNLE[r3]; p3 < iCNLE[r3 + 1]; p3++)
                            if (CNLE[p1] == CNLE[p3]) {
                                return CNLE[p1];
                            }
                    }
                }

        if (VERBOSE) {
            System.out.println(
                "<FONT COLOR=#FF0000><B>NearbyElement:</B> failure in " + r1 +
                " " + r2 + " " + r3 + " " + elem + "</FONT>");
        }

        return -1;
    }

    /**
     * Call this method before calculate(). The invocation is done by
     * default if needed.
     */
    public void initializeCaches() {
        double x1;
        double y1;
        double z1;
        double x2;
        double y2;
        double z2;
        double x3;
        double y3;
        double z3;
        double x4;
        double y4;
        double z4;
        double X21;
        double X31;
        double X41;
        double Y21;
        double Y31;
        double Y41;
        double Z21;
        double Z31;
        double Z41;
        double jacobiano;

        kernel.out("Initializing caches of <B>Characteristics3D</B>");

        totalNodes = mesh.getCoordinatesQuad(0).length;
        cx2 = new double[totalNodes];
        cy2 = new double[totalNodes];
        cz2 = new double[totalNodes];

        if (contaminant) {
            cc2 = new double[totalNodes];
        }

        System.out.println("   * Loading cache node-element");
        totalElements = mesh.getNodesQuad(0).length;

        x = mesh.getCoordinatesQuad(0);
        y = mesh.getCoordinatesQuad(1);
        z = mesh.getCoordinatesQuad(2);

        // change with respect Caracteristicas.java
        n1 = mesh.getNodesQuad(0);
        n2 = mesh.getNodesQuad(1);
        n3 = mesh.getNodesQuad(2);
        n4 = mesh.getNodesQuad(3);
        n5 = mesh.getNodesQuad(4);
        n6 = mesh.getNodesQuad(5);
        n7 = mesh.getNodesQuad(6);
        n8 = mesh.getNodesQuad(7);
        n9 = mesh.getNodesQuad(8);
        na = mesh.getNodesQuad(9);

        int[] li1;
        int[] li2;
        int[] li3;
        int[] li4;
        li1 = mesh.getNodes(0);
        li2 = mesh.getNodes(1);
        li3 = mesh.getNodes(2);
        li4 = mesh.getNodes(3);

        // cached data for the calculus
        invJ11 = new double[totalElements];
        invJ12 = new double[totalElements];
        invJ13 = new double[totalElements];
        invJ21 = new double[totalElements];
        invJ22 = new double[totalElements];
        invJ23 = new double[totalElements];
        invJ31 = new double[totalElements];
        invJ32 = new double[totalElements];
        invJ33 = new double[totalElements];

        System.out.println("   * generating caches");

        for (int n = 0; n < totalElements; n++) {
            x1 = x[n1[n]];
            y1 = y[n1[n]];
            z1 = z[n1[n]];
            x2 = x[n2[n]];
            y2 = y[n2[n]];
            z2 = z[n2[n]];
            x3 = x[n3[n]];
            y3 = y[n3[n]];
            z3 = z[n3[n]];
            x4 = x[n4[n]];
            y4 = y[n4[n]];
            z4 = z[n4[n]];

            // differences (jacobian matrix)
            X21 = x2 - x1;
            X31 = x3 - x1;
            X41 = x4 - x1;
            Y21 = y2 - y1;
            Y31 = y3 - y1;
            Y41 = y4 - y1;
            Z21 = z2 - z1;
            Z31 = z3 - z1;
            Z41 = z4 - z1;

            // determinant of the jacobian matrix
            jacobiano = ((X31 * Y41 * Z21) + (X41 * Y21 * Z31) +
                (X21 * Y31 * Z41)) - (X41 * Y31 * Z21) - (X21 * Y41 * Z31) -
                (X31 * Y21 * Z41);

            // Jacobian inverse matrix
            invJ11[n] = ((Y31 * Z41) - (Y41 * Z31)) / jacobiano;
            invJ12[n] = ((X41 * Z31) - (X31 * Z41)) / jacobiano;
            invJ13[n] = ((X31 * Y41) - (X41 * Y31)) / jacobiano;

            invJ21[n] = ((Y41 * Z21) - (Y21 * Z41)) / jacobiano;
            invJ22[n] = ((X21 * Z41) - (X41 * Z21)) / jacobiano;
            invJ23[n] = ((X41 * Y21) - (X21 * Y41)) / jacobiano;

            invJ31[n] = ((Y21 * Z31) - (Y31 * Z21)) / jacobiano;
            invJ32[n] = ((X31 * Z21) - (X21 * Z31)) / jacobiano;
            invJ33[n] = ((X21 * Y31) - (X31 * Y21)) / jacobiano;
        }

        System.out.println(
            "   * generating caches adjacent12, adjacent13, adjacent23");
        CNLE = mesh.getCacheNodeLinealElement();
        iCNLE = mesh.getStartCacheNodeLinealElement();
        adjacent123 = new int[totalElements];
        adjacent124 = new int[totalElements];
        adjacent234 = new int[totalElements];
        adjacent134 = new int[totalElements];
        reference = mesh.elementReferencesNodeQuad();

        int successes = 0;

        for (int j = 0; j < totalElements; j++) {
            if ((adjacent123[j] = nearbyElement(li1[j], li2[j], li3[j], j)) != -1) {
                successes++;
            }

            if ((adjacent124[j] = nearbyElement(li1[j], li2[j], li4[j], j)) != -1) {
                successes++;
            }

            if ((adjacent234[j] = nearbyElement(li2[j], li3[j], li4[j], j)) != -1) {
                successes++;
            }

            if ((adjacent134[j] = nearbyElement(li1[j], li3[j], li4[j], j)) != -1) {
                successes++;
            }
        }

        kernel.out("<B>Success</B> in " + successes + "<B>/</B>" +
            (totalElements * 4) + " faces");
    }

    /**
     * Indicatestes the values of the two speed components in each
     * node.
     *
     * @param vxa component x of the speed at instant n
     * @param vya component y of the speed at instant n
     * @param vza component z of the speed at instant n
     * @param vxb component x of the speed at instant n-1
     * @param vyb component y of the speed at instant n-1
     * @param vzb component z of the speed at instant n-1
     */
    public void setSpeedFields(double[] vxa, double[] vya, double[] vza,
        double[] vxb, double[] vyb, double[] vzb) {
        // System.out.println("   * Nueva definicion of campo.");
        this.vx = vxa;
        this.vy = vya;
        this.vz = vza;
        this.vx2 = vxb;
        this.vy2 = vyb;
        this.vz2 = vzb;
    }

    // returns the index of the element that contains (x,y)
    /**
     * DOCUMENT ME!
     *
     * @param xp DOCUMENT ME!
     * @param yp DOCUMENT ME!
     * @param zp DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int slalg(double xp, double yp, double zp, int n) {
        double p;
        double q;
        double r;
        int old;

        int contIter = 0;

        do {
            double x1;
            double y1;
            double z1;
            x1 = x[n1[n]];
            y1 = y[n1[n]];
            z1 = z[n1[n]];

            p = ((xp - x1) * invJ11[n]) + ((yp - y1) * invJ12[n]) +
                ((zp - z1) * invJ13[n]);
            q = ((xp - x1) * invJ21[n]) + ((yp - y1) * invJ22[n]) +
                ((zp - z1) * invJ23[n]);
            r = ((xp - x1) * invJ31[n]) + ((yp - y1) * invJ32[n]) +
                ((zp - z1) * invJ33[n]);

            // Search the new element to continue the search ...
            // the ELSE-IF are imprescindible.
            old = n;

            // by left
            if (p <= -DELTA_PQ) {
                n = adjacent134[n]; // n = elementColindante(n1[n], n3[n], n);
            }
            // down
            else if (q <= -DELTA_PQ) {
                n = adjacent124[n]; // n = elementColindante(n1[n],n2[n],n);
            }
            // by left
            else if (r <= -DELTA_PQ) {
                n = adjacent123[n]; // n = elementColindante(n1[n], n3[n], n);
            }
            // by the diagonal
            else if ((p + q + r) >= (1 + DELTA_PQ)) {
                n = adjacent234[n]; // n=elementColindante(n3[n],n2[n], n);
            }

            // If we get into trouble...
            if ((n < 0) || (n >= totalElements)) {
                double p2 = p;
                double q2 = q;
                double r2 = r;

                // no ELSE-IF should be here
                if ((p + q + r) >= (1.0 - DELTA_PQ)) {
                    // much nicer than the 2D
                    double corr = ((p + q + r) - (1.0 - DELTA_PQ)) / 3.0;

                    p = p - corr;
                    q = q - corr;
                    r = r - corr;
                }

                // left
                if (p <= DELTA_PQ) {
                    p = DELTA_PQ;
                }

                // down
                if (q <= DELTA_PQ) {
                    q = DELTA_PQ;
                }

                // down
                if (r <= DELTA_PQ) {
                    r = DELTA_PQ;
                }

                // by the diagonal
                if (VERBOSE) {
                    System.out.println("<B>slalg:</B> No surrounding  (" + p2 +
                        "," + q2 + "," + r2 + ")  " + n + "\n" +
                        "Using actual, with (p,q,r)" + "corrected (" + p + "," +
                        q + "," + r + ")" + " " + xp + "," + yp + "," + zp +
                        "");
                }

                // the anterior es Ok.
                n = old;
            }

            // When there is an element change, start again
            contIter++;

            if (contIter > MAX_CONTITER) {
                System.err.println(
                    "</FONT COLOR=#FF0000><B>MAX_CONTITER</B>: SLALG " + xp +
                    "," + yp + "," + zp + " * " + p + "," + q + "," + r +
                    "</FONT>");

                kernel.newErrorFatalDialog(
                    "The characteristics (SLALG) have failed.\n" +
                    "It is impossible to continue with the processing, because\n" +
                    "previsibly the global convergence \n" +
                    "will degrade and the solver will return\n" +
                    "chaotic results.");

                System.exit(0);

                break;
            }
        } while (old != n);

        pPar = p;
        qPar = q;
        rPar = r;

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
     * @param xp DOCUMENT ME!
     * @param yp DOCUMENT ME!
     * @param zp DOCUMENT ME!
     * @param elem_ref DOCUMENT ME!
     * @param iter DOCUMENT ME!
     */
    private void values(double xp, double yp, double zp, int elem_ref,
        boolean iter) {
        double p;
        double q;
        double r;
        double x1;
        double y1;
        double z1;
        double phi1;
        double phi2;
        double phi3;
        double phi4;
        double phi5;
        double phi6;
        double phi7;
        double phi8;
        double phi9;
        double phia;
        double l1;
        double l2;
        double l3;
        double l4;
        double min;
        double max;

        int n = slalg(xp, yp, zp, elem_ref);

        p = pPar;
        q = qPar;
        r = rPar;

        // forced estability!
        if ((p + q + r) >= (1 - DELTA_PQ)) {
            // I like it more than the 2D one
            double corr = ((p + q + r) - 1.) / 3.0;
            p = p - corr;
            q = q - corr;
            r = r - corr;
        }

        if (p > (1.0 - DELTA_PQ)) { // System.out.println("RET p1 "+p+" / "+q);
            p = 1.0 - DELTA_PQ;
        } else if (p < DELTA_PQ) { // System.out.println("RET p0 "+p+" / "+q);
            p = DELTA_PQ;
        }

        if (q > (1.0 - DELTA_PQ)) { // System.out.println("RET q1 "+q+" / "+p);
            q = 1.0 - DELTA_PQ;
        } else if (q < DELTA_PQ) { // System.out.println("RET q0 "+q+" / "+p);
            q = DELTA_PQ;
        }

        if (r > (1.0 - DELTA_PQ)) { // System.out.println("RET q1 "+q+" / "+p);
            r = 1.0 - DELTA_PQ;
        } else if (r < DELTA_PQ) { // System.out.println("RET q0 "+q+" / "+p);
            r = DELTA_PQ;
        }

        // Functions quadratic base
        l1 = 1.0 - p - q - r;
        l2 = p;
        l3 = q;
        l4 = r;
        phi1 = l1 * ((2.0 * l1) - 1.0);
        phi2 = l2 * ((2.0 * l2) - 1.0);
        phi3 = l3 * ((2.0 * l3) - 1.0);
        phi4 = l4 * ((2.0 * l4) - 1.0);
        phi5 = 4.0 * l1 * l2;
        phi6 = 4.0 * l2 * l3;
        phi7 = 4.0 * l1 * l3;
        phi8 = 4.0 * l1 * l4;
        phi9 = 4.0 * l2 * l4;
        phia = 4.0 * l3 * l4;

        if (iter) {
            // Interpolate the parameters
            vxPar = (vx[n1[n]] * phi1) + (vx[n2[n]] * phi2) +
                (vx[n3[n]] * phi3) + (vx[n4[n]] * phi4) + (vx[n5[n]] * phi5) +
                (vx[n6[n]] * phi6) + (vx[n7[n]] * phi7) + (vx[n8[n]] * phi8) +
                (vx[n9[n]] * phi9) + (vx[na[n]] * phia);

            vyPar = (vy[n1[n]] * phi1) + (vy[n2[n]] * phi2) +
                (vy[n3[n]] * phi3) + (vy[n4[n]] * phi4) + (vy[n5[n]] * phi5) +
                (vy[n6[n]] * phi6) + (vy[n7[n]] * phi7) + (vy[n8[n]] * phi8) +
                (vy[n9[n]] * phi9) + (vy[na[n]] * phia);

            vzPar = (vz[n1[n]] * phi1) + (vz[n2[n]] * phi2) +
                (vz[n3[n]] * phi3) + (vz[n4[n]] * phi4) + (vz[n5[n]] * phi5) +
                (vz[n6[n]] * phi6) + (vz[n7[n]] * phi7) + (vz[n8[n]] * phi8) +
                (vz[n9[n]] * phi9) + (vz[na[n]] * phia);

            vx2Par = (vx2[n1[n]] * phi1) + (vx2[n2[n]] * phi2) +
                (vx2[n3[n]] * phi3) + (vx2[n4[n]] * phi4) +
                (vx2[n5[n]] * phi5) + (vx2[n6[n]] * phi6) +
                (vx2[n7[n]] * phi7) + (vx2[n8[n]] * phi8) +
                (vx2[n9[n]] * phi9) + (vx2[na[n]] * phia);

            vy2Par = (vy2[n1[n]] * phi1) + (vy2[n2[n]] * phi2) +
                (vy2[n3[n]] * phi3) + (vy2[n4[n]] * phi4) +
                (vy2[n5[n]] * phi5) + (vy2[n6[n]] * phi6) +
                (vy2[n7[n]] * phi7) + (vy2[n8[n]] * phi8) +
                (vy2[n9[n]] * phi9) + (vy2[na[n]] * phia);

            vz2Par = (vz2[n1[n]] * phi1) + (vz2[n2[n]] * phi2) +
                (vz2[n3[n]] * phi3) + (vz2[n4[n]] * phi4) +
                (vz2[n5[n]] * phi5) + (vz2[n6[n]] * phi6) +
                (vz2[n7[n]] * phi7) + (vz2[n8[n]] * phi8) +
                (vz2[n9[n]] * phi9) + (vz2[na[n]] * phia);
        } else {
            cxPar = (cx[n1[n]] * phi1) + (cx[n2[n]] * phi2) +
                (cx[n3[n]] * phi3) + (cx[n4[n]] * phi4) + (cx[n5[n]] * phi5) +
                (cx[n6[n]] * phi6) + (cx[n7[n]] * phi7) + (cx[n8[n]] * phi8) +
                (cx[n9[n]] * phi9) + (cx[na[n]] * phia);

            cyPar = (cy[n1[n]] * phi1) + (cy[n2[n]] * phi2) +
                (cy[n3[n]] * phi3) + (cy[n4[n]] * phi4) + (cy[n5[n]] * phi5) +
                (cy[n6[n]] * phi6) + (cy[n7[n]] * phi7) + (cy[n8[n]] * phi8) +
                (cy[n9[n]] * phi9) + (cy[na[n]] * phia);

            czPar = (cz[n1[n]] * phi1) + (cz[n2[n]] * phi2) +
                (cz[n3[n]] * phi3) + (cz[n4[n]] * phi4) + (cz[n5[n]] * phi5) +
                (cz[n6[n]] * phi6) + (cz[n7[n]] * phi7) + (cz[n8[n]] * phi8) +
                (cz[n9[n]] * phi9) + (cz[na[n]] * phia);

            if (cc != null) {
                ccPar = (cc[n1[n]] * phi1) + (cc[n2[n]] * phi2) +
                    (cc[n3[n]] * phi3) + (cc[n4[n]] * phi4) +
                    (cc[n5[n]] * phi5) + (cc[n6[n]] * phi6) +
                    (cc[n7[n]] * phi7) + (cc[n8[n]] * phi8) +
                    (cc[n9[n]] * phi9) + (cc[na[n]] * phia);

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

                if (max < cc[n4[n]]) {
                    max = cc[n4[n]];
                }

                if (min > cc[n4[n]]) {
                    min = cc[n4[n]];
                }

                if (ccPar < min) {
                    ccPar = min;
                }

                if (ccPar > max) {
                    ccPar = max;
                }
            }
        }
    }
}
