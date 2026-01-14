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

import org.jscience.physics.fluids.dynamics.KernelADFC;
import org.jscience.physics.fluids.dynamics.KernelADFCConfiguration;
import org.jscience.physics.fluids.dynamics.mesh.NavierStokesMesh;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Feedback {
    /** DOCUMENT ME! */
    private static final int MAX_POINTS = 256;

    /** DOCUMENT ME! */
    public static boolean ACTIVATE = false;

    /** DOCUMENT ME! */
    public static double COORDINATE = 0.0;

    /** DOCUMENT ME! */
    private KernelADFC kernel;

    /** DOCUMENT ME! */
    private double[] x;

    /** DOCUMENT ME! */
    private double[] y;

    /** DOCUMENT ME! */
    private double[] phi1;

    /** DOCUMENT ME! */
    private double[] phi2;

    /** DOCUMENT ME! */
    private double[] phi3;

    /** DOCUMENT ME! */
    private double[] phi4;

    /** DOCUMENT ME! */
    private double[] phi5;

    /** DOCUMENT ME! */
    private double[] phi6;

    /** DOCUMENT ME! */
    private double[] newValue;

    /** DOCUMENT ME! */
    private boolean[] valid;

    /** DOCUMENT ME! */
    private int[] element;

    /** DOCUMENT ME! */
    private int tam;

    /** DOCUMENT ME! */
    private double[] xc;

    /** DOCUMENT ME! */
    private double[] yc;

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
    private double baseOrigin;

    /** DOCUMENT ME! */
    private double baseDestination;

    /** DOCUMENT ME! */
    private double dyAvg;

    /** DOCUMENT ME! */
    private boolean firstIncomingFlux;

    /** DOCUMENT ME! */
    private double valueFirstIncomingFlux = 0.0;

    // Nodos del profiledirichlet
    /** DOCUMENT ME! */
    private int[] npd;

/**
     * Creates a new Feedback object.
     *
     * @param kadfc DOCUMENT ME!
     */
    public Feedback(KernelADFC kadfc) {
        System.out.println("Feedback");

        kernel = kadfc;

        // We get the kernel configuration
        KernelADFCConfiguration c = kernel.getConfiguration();

        ACTIVATE = c.feedback;
        COORDINATE = c.feedbackCoordinate;

        x = new double[MAX_POINTS];
        y = new double[MAX_POINTS];
        phi1 = new double[MAX_POINTS];
        phi2 = new double[MAX_POINTS];
        phi3 = new double[MAX_POINTS];
        phi4 = new double[MAX_POINTS];
        phi5 = new double[MAX_POINTS];
        phi6 = new double[MAX_POINTS];
        element = new int[MAX_POINTS];
        valid = new boolean[MAX_POINTS];
        newValue = new double[MAX_POINTS];

        tam = 0;

        firstIncomingFlux = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mact DOCUMENT ME!
     * @param vx DOCUMENT ME!
     * @param vy DOCUMENT ME!
     */
    public void applyFeedback(NavierStokesMesh mact, double[] vx, double[] vy) {
        if (!ACTIVATE) {
            System.out.println("No Feedback");

            return;
        }

        // calculate values of the nodes in the Dirichlet Profile, offseting
        // the correction that we will do later.
        int corrected = 0;
        double value;

        for (int i = 0; i < tam; i++) {
            if (valid[i]) {
                int e = element[i];
                newValue[i] = (vx[n1[e]] * phi1[i]) + (vx[n2[e]] * phi2[i]) +
                    (vx[n3[e]] * phi3[i]) + (vx[n4[e]] * phi4[i]) +
                    (vx[n5[e]] * phi5[i]) + (vx[n6[e]] * phi6[i]);
            } else {
                newValue[i] = 1;
            }
        }

        double dy;

        // Now integrate the flux over the input
        double incomingFlux = 0.0;

        for (int j = 0; j < (npd.length - 1); j++) {
            dy = yc[npd[j + 1]] - yc[npd[j]];
            incomingFlux += ((dy * (newValue[j] + newValue[j + 1])) / 2.0);

            // System.out.println("DY = "+yc[npd[j]]+" VA = "+(nuevoValor[j] + nuevoValor[j+1])/2.0);
        }

        System.out.println("First incoming flux = " + incomingFlux + " " +
            (npd.length - 1) + " segments");

        if (!firstIncomingFlux) {
            valueFirstIncomingFlux = incomingFlux;
            firstIncomingFlux = true;
        } else {
            /*
            // Ahora integramos in the origen of the feeback
            double flujoSalida=0.0;
            int cont=0;
            for(int j=0; j<tam; j++) {
            if(valido[j] && valido[j+1]) {
            if(j<npd.length-1)
                dy = yc[npd[j+1]] - yc[npd[j]];
            else dy = dyMedia;
            flujoSalida += dy*(nuevoValor[j] + nuevoValor[j+1])/2.0;
            cont ++;
            // System.out.println("DY = "+yc[npd[j]]+" VA = "+(nuevoValor[j] + nuevoValor[j+1])/2.0);
            }
            }
            
            System.out.println("Flujo salida = "+flujoSalida+" "+cont+" segments");
            
            
            
             */

            // Comprobacion, redundante
            incomingFlux = 0;

            for (int j = 0; j < (npd.length - 1); j++) {
                dy = yc[npd[j + 1]] - yc[npd[j]];
                incomingFlux += ((dy * (newValue[j] + newValue[j + 1])) / 2.0);
            }

            System.out.println("Incoming Flux = " + incomingFlux);

            // Apply correction for the conservation of mass
            for (int j = 0; j < tam; j++)
                newValue[j] *= (valueFirstIncomingFlux / incomingFlux);

            // store values
            for (int i = 0; i < npd.length; i++) {
                // search this node in the Dirichlet stuff
                for (int j = 0; j < mact.nDirichletq.length; j++) {
                    if (mact.nDirichletq[j] == npd[i]) {
                        mact.vDirichletq[0][j] = newValue[i];

                        // System.out.println(" "+valor);
                        corrected++;

                        // System.out.println("Nodo "+npd[i]+" = "+valor);
                        break;
                    }
                }
            }

            incomingFlux = 0;

            for (int j = 0; j < (npd.length - 1); j++) {
                dy = yc[npd[j + 1]] - yc[npd[j]];
                incomingFlux += ((dy * (newValue[j] + newValue[j + 1])) / 2.0);
            }

            System.out.println("Incoming flux after correction = " +
                incomingFlux);

            System.out.println("Applied Feedback " + corrected + " nodes");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void initializeCaches(NavierStokesMesh m) {
        if (!ACTIVATE) {
            System.out.println("No initializeCaches Feedback");

            return;
        }

        double A;
        double B;
        double C;
        double D;
        double DetCr;
        double x0;
        double y0;
        double p;
        double q;
        double l1;
        double l2;
        double l3;

        // Alias for the interesting vectors
        xc = m.getCoordinatesQuad(0);
        yc = m.getCoordinatesQuad(1);
        n1 = m.getNodesQuad(0);
        n2 = m.getNodesQuad(1);
        n3 = m.getNodesQuad(2);
        n4 = m.getNodesQuad(3);
        n5 = m.getNodesQuad(4);
        n6 = m.getNodesQuad(5);

        // We make a copy of npd y we order from lower to up.
        int[] tmp = m.getNodesPerfilDirichletQuad();
        npd = new int[tmp.length];

        for (int j = 0; j < npd.length; j++)
            npd[j] = tmp[j];

        tmp = null;

        // bubble sort
        for (int j = npd.length - 1; j > 0; j--)
            for (int i = 0; i < j; i++)
                if (yc[npd[i]] > yc[npd[i + 1]]) {
                    int swap = npd[i];
                    npd[i] = npd[i + 1];
                    npd[i + 1] = swap;
                }

        // We search the base in the profile, for the poit to be feedbacked and for the source
        baseOrigin = kernel.getInputProfile().searchBase(COORDINATE);
        baseDestination = kernel.getInputProfile().searchBase(xc[npd[0]]);

        kernel.out("<B>Feedback:</B> baseOrigen=" + baseOrigin +
            " baseDestination " + baseDestination + " CoordOrigen " +
            COORDINATE);

        // First introduce all points of the profile dirichlet of the input
        for (int i = 0; i < npd.length; i++)
            newPoint(COORDINATE, yc[npd[i]] - baseDestination + baseOrigin);

        // If baseOrigin < baseDestination: the entrance is narrower than the exit
        // or the origin of the feedback.
        // Add more points, needed for integrate in the line of Origin.
        // we add them by placing the distance dyMedia in between them. This
        // distance is obtained from finding the average of the separation between
        // nodes npd.
        if (baseOrigin < baseDestination) {
            double dExtra = baseDestination - baseOrigin;

            // find average distance between points
            dyAvg = 0;

            for (int j = 0; j < (npd.length - 1); j++)
                dyAvg += (yc[npd[j + 1]] - yc[npd[j]]);

            dyAvg /= npd.length;

            // how many points to add
            int nExtra = (int) (dExtra / dyAvg) + 1;

            kernel.out("<B>Feedback:</B> Desfase=" + dExtra + " dyAvg=" +
                dyAvg + " nExtra=" + nExtra);

            // add it
            for (int i = 0; i < nExtra; i++)
                newPoint(COORDINATE,
                    yc[npd[npd.length - 1]] - baseDestination + baseOrigin +
                    (dyAvg * (i + 1)));
        }

        for (int i = 0; i < tam; i++) {
            if ((element[i] = m.containingElement(x[i], y[i])) < 0) {
                System.out.println("Observed point -> out of the domain !");
                valid[i] = false;
            } else {
                int e = element[i];
                valid[i] = true;
                A = xc[n2[e]] - xc[n1[e]];
                B = xc[n3[e]] - xc[n1[e]];
                C = yc[n2[e]] - yc[n1[e]];
                D = yc[n3[e]] - yc[n1[e]];
                DetCr = (A * D) - (B * C);

                x0 = xc[n1[e]];
                y0 = yc[n1[e]];
                p = (((x[i] - x0) * D) - (B * (y[i] - y0))) / DetCr;
                q = ((A * (y[i] - y0)) - ((x[i] - x0) * C)) / DetCr;

                // Functions quadratic base
                l1 = 1.0 - p - q;
                l2 = p;
                l3 = q;
                phi1[i] = l1 * ((2.0 * l1) - 1.0);
                phi2[i] = l2 * ((2.0 * l2) - 1.0);
                phi3[i] = l3 * ((2.0 * l3) - 1.0);
                phi4[i] = 4.0 * l1 * l2;
                phi5[i] = 4.0 * l2 * l3;
                phi6[i] = 4.0 * l1 * l3;

                //System.out.println("p "+p+" q "+q);
                //System.out.println("phi1 "+phi1[i]+" phi2 "+phi2[i]+" phi3 "+phi3[i]);
                //System.out.println("phi4 "+phi4[i]+" phi5 "+phi5[i]+" phi6 "+phi6[i]);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param xp DOCUMENT ME!
     * @param yp DOCUMENT ME!
     */
    public void newPoint(double xp, double yp) {
        if (tam > MAX_POINTS) {
            kernel.newErrorFatalDialog("Overflow in Feedback (" + MAX_POINTS +
                ")\n" +
                "The value in org.jscience.fluids.navierstokes.Feedback should be increased !!");
        }

        System.out.println("(" + xp + ", " + yp + ") -> Feedback");

        x[tam] = xp;
        y[tam++] = yp;
    }
}
