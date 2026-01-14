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

/*
 * ObservadorP.java
 *
 * Created on 6 of agosto of 2001, 16:27
 */
package org.jscience.physics.fluids.dynamics.navierstokes;

/**
 */
import org.jscience.physics.fluids.dynamics.KernelADFC;
import org.jscience.physics.fluids.dynamics.mesh.NavierStokesMesh;

import java.io.FileWriter;
import java.io.PrintWriter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class PressureObserver implements Observer {
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
    private int[] element;

    /** DOCUMENT ME! */
    private PrintWriter pwObs;

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
    private NavierStokesMesh mesh;

/**
     * Creates a new PressureObserver object.
     *
     * @param kadfc    DOCUMENT ME!
     * @param meshing  DOCUMENT ME!
     * @param filename DOCUMENT ME!
     */
    public PressureObserver(KernelADFC kadfc, NavierStokesMesh meshing,
        String filename) {
        mesh = meshing;
        kernel = kadfc;
        System.out.println("New PressureObserver()");

        x = kernel.getConfiguration().observerCoordinateX;
        y = kernel.getConfiguration().observerCoordinateY;
        phi1 = new double[x.length];
        phi2 = new double[x.length];
        phi3 = new double[x.length];
        element = new int[x.length];

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

        xc = mesh.getCoordinates(0);
        yc = mesh.getCoordinates(1);

        n1 = mesh.getNodes(0);
        n2 = mesh.getNodes(1);
        n3 = mesh.getNodes(2);

        kernel.out("<B>PressureObserver:</B> " + x.length +
            " points of the pressure mesh will be studied");

        for (int i = 0; i < x.length; i++) {
            if ((element[i] = mesh.containingElement(x[i], y[i])) < 0) {
                System.out.println("Observed point -> out of the domain !");
                System.exit(0);
            }

            int e = element[i];
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
            phi1[i] = l1;
            phi2[i] = l2;
            phi3[i] = l3;

            System.out.println("p " + p + " q " + q);
            System.out.println("phi1 " + phi1[i] + " phi2 " + phi2[i] +
                " phi3 " + phi3[i]);
        }

        // preparing the output
        try {
            pwObs = new PrintWriter(new FileWriter(filename + ".obs"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        try {
            pwObs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param inst DOCUMENT ME!
     * @param vx DOCUMENT ME!
     * @param p DOCUMENT ME!
     */
    public void evaluate(double inst, double[][] vx, double[] p) {
        // kernel.out("Observer: "+phi1.length+" points of the mesh will be studied");
        try {
            pwObs.print("" + inst);

            for (int i = 0; i < phi1.length; i++) {
                int e = element[i];
                double value = (p[n1[e]] * phi1[i]) + (p[n2[e]] * phi2[i]) +
                    p[n3[e]];

                pwObs.print("\t" + value);
            }

            pwObs.println();
            pwObs.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
