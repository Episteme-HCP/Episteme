package org.jscience.physics.fluids.dynamics.navierstokes;

import org.jscience.physics.fluids.dynamics.KernelADFC;
import org.jscience.physics.fluids.dynamics.mesh.NavierStokesMesh;
import org.jscience.physics.fluids.dynamics.util.VectorFastInt;

import java.io.FileWriter;
import java.io.PrintWriter;

public class DragLift2D {
    private boolean DRAG_LIFT_NUEVO = true;
    private NavierStokesMesh mesh;
    private NavierStokes ns;
    private KernelADFC kernel;
    private boolean CALCULAR_DL;
    private int[][] tablaDragLift;
    private double[] nxDL, nyDL, longDL;
    // private Poisson pElip3;
    private String filenameDragLift = null;
    private PrintWriter pwDL, pwP, pwGV;
    private double nu;
    private double DELTA_T, REYNOLDS;

    DragLift2D(KernelADFC kadfc, NavierStokesMesh meshing) {
        kernel = kadfc;
        mesh = meshing;

        REYNOLDS = kernel.getConfiguration().reynolds;
        nu = 1.0 / REYNOLDS;
        DELTA_T = kernel.getConfiguration().timeStepDuration;

        initializaDragLift();
    }

    void calculateDragLift(double[][] um, double[][][] derum, double[] pm, int paso) {
        if (mesh.is3D()) return;

        double CDP = 0, CDV = 0, CLP = 0, CLV = 0, CD, CL;
        double xc = 0.0, yc = 0.0;
        /* This es the bueno con reparto no uniforme of nodes */
        int nn = tablaDragLift.length;
        int[] nodesDragLift = mesh.getNodesDragLift();
        double[] x = mesh.getCoordinates(0);
        double[] y = mesh.getCoordinates(1);
        int[] ql = mesh.ql;
        int ndl = nodesDragLift.length;

        if (DRAG_LIFT_NUEVO) {
            /* This es the nuevo con reparto uniforme of nodes y cilinder centrado */
            for (int j = 0; j < ndl; j++) {
                int node = nodesDragLift[j];

                double sen = y[node] / 0.5;
                double cos = x[node] / 0.5;
                double teta = Math.atan2(sen, cos);

                double modulo = 0.5;
                double anx = cos;
                double any = sen;

                double dnxdx = (y[node] - yc) * (y[node] - yc) / Math.pow(modulo, 3.0);
                double dnydx = (y[node] - yc) * (x[node] - xc) / Math.pow(modulo, 3.0);
                double dnydy = (x[node] - xc) * (x[node] - xc) / Math.pow(modulo, 3.0);
                double dnxdy = -dnydx;

                double dvtetadx = derum[0][0][node] * any + um[0][ql[node]] * dnydx
                        - derum[1][0][node] * anx - um[1][ql[node]] * dnxdx;
                double dvtetady = derum[0][1][node] * any + um[0][ql[node]] * dnydy
                        - derum[1][1][node] * anx - um[1][ql[node]] * dnxdy;

                double dvtetadr = dvtetadx * cos + dvtetady * sen;

                double velAdim = dvtetadr; // / (2.0*Math.sqrt(REYNOLDS));

                CDP += -2 * 3.141592 * cos * pm[node] / ndl;
                CDV += +2 * 3.141592 * nu * (velAdim * sen) / ndl;

                CLP += -2 * 3.141592 * sen * pm[node] / ndl;
                CLV += +2 * 3.141592 * nu * (velAdim * (-cos)) / ndl;

            }

            CD = CDP + CDV;
            CL = CLP + CLV;
        } else {
            /* This es the antiguo con reparto uniforme of nodes y cilinder centrado */
            for (int j = 0; j < ndl; j++) {
                int node = nodesDragLift[j];

                double sen = y[node] / 0.5;
                double cos = x[node] / 0.5;

                CDP += -2 * 3.141592 * cos * pm[node] / ndl;
                CDV += +2 * 3.141592 * nu * (derum[0][0][node] * cos + derum[0][1][node] * sen) / ndl;

                CLP += -2 * 3.141592 * sen * pm[node] / ndl;
                CLV += +2 * 3.141592 * nu * (derum[1][0][node] * cos + derum[1][1][node] * sen) / ndl;

            }

            CD = CDP + CDV;
            CL = CLP + CLV;
        }

        try {
            pwDL.println("" + (paso * DELTA_T) + "\t" + CD + "\t" + CL + "\t" + CDP + "\t" + CDV + "\t" + CLP + "\t" + CLV);
            pwDL.flush();

            // Salida of curvas of presion y gradientsof speed
            if (mesh.isCilindroDragLift()) {
                int[] nDLl = mesh.getNodesDragLift();

                pwP.print("" + (paso * DELTA_T));
                pwGV.print("" + (paso * DELTA_T));

                for (int j = 0; j < nDLl.length; j++) {
                    int node = nDLl[j];
                    // Adimensionalizamos the presion a the mitad que the resto
                    // del mundo, by eso aqui debo multiplyr por 2.
                    pwP.print("\t" + (2.0 * pm[node]));

                    // component segun X
                    double anx = x[node] / 0.5;
                    // component segun Y
                    double any = y[node] / 0.5;

                    // calculo of las derivatives of componentes of N con respecto a X e Y
                    double dnxdx = y[node] * y[node] / Math.pow(0.5, 3.0);
                    double dnydx = -y[node] * x[node] / Math.pow(0.5, 3.0);
                    double dnydy = x[node] * x[node] / Math.pow(0.5, 3.0);
                    double dnxdy = dnydx;

                    // calculo of the derivative of Vteta con respecto a X
                    double dvtetadx = derum[0][0][node] * any + um[0][ql[node]] * dnydx - derum[1][0][node] * anx - um[1][ql[node]] * dnxdx;

                    // calculo of the derivative of Vteta con respecto a Y
                    double dvtetady = derum[0][1][node] * any + um[0][ql[node]] * dnydy - derum[1][1][node] * anx - um[1][ql[node]] * dnxdy;

                    // calculo of the derivative of Vteta con respecto a R
                    double dvtetadr = dvtetadx * anx + dvtetady * any;

                    // compatibilidad con [ZDR97]
                    double gv = dvtetadr / (2 * Math.sqrt(REYNOLDS));

                    pwGV.print("\t" + gv);
                }

                pwP.println();
                pwP.flush();
                pwGV.println();
                pwGV.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("CD = " + CD + "\tCL = " + CL + "\n*CDP = " + CDP + "\t*CDV = " + CDV + "\n*CLP = " + CLP + "\t*CLV = " + CLV);
        // Preparando salida

    }

    /**
     * this method construye the lista of segments sobre
     * los que integraremos the Drag y the Lift.
     * Sera of the forma tablaDragLift[seg][indice]
     * indice == 0  ->  element
     * indice == 1  ->  primer vertice del segment
     * indice == 2  ->  ultimo vertice del segment
     * <p/>
     * tambien genera las normales.
     */
    private void initializaDragLift() {
        if (mesh.is3D())
            return;

        int[] nodesDragLift = mesh.getNodesDragLift();

        if (nodesDragLift == null) {
            CALCULAR_DL = false;
            return;
        }

        kernel.out("<B>Inicializando Drag & Lift...</B>");

        kernel.out("   * nodesDragLift = "
                + nodesDragLift.length + " nodes");

        if (mesh.isCilindroDragLift())
            kernel.out("   * se generaran graficas P y Grad V");

        //for(int j=0; j<nodesDragLift.length; j++)
        //    System.out.print("   nodesDragLift["+j+"] = "+nodesDragLift[j]);


        int[] CNLE = mesh.getCacheNodeLinealElement();
        int[] iCNLE = mesh.getStartCacheNodeLinealElement();
        double[] xcoord = mesh.getCoordinates(0);
        double[] ycoord = mesh.getCoordinates(1);

        /* in the primera pasada creamos a tabla con
   los elements que containsn a los nodes Drag Lift.
   Sin preocuparnos of las repetitions. */
        VectorFastInt elem = new VectorFastInt();

        for (int j = 0; j < nodesDragLift.length; j++) {
            int node = nodesDragLift[j];
            for (int k = iCNLE[node]; k < iCNLE[node + 1]; k++)
                //if(cnce[node][k] != -1)
                elem.addElement(CNLE[k]);
        }

        /* ahora, by logica, sabemos que los elements Drag Lift
   tendran que estar contenidos dos times, ya que fueron
   introducidos in dos ocasiones (�o mas?) by cada segment.
   Dos vertices a los sides. */
        elem.sortArray(1);
        int[] cnceSort = elem.getTruncatedArray();
        VectorFastInt elemsDragLift = new VectorFastInt();
        for (int j = 0; j < cnceSort.length - 1; j++)
            if (cnceSort[j] == cnceSort[j + 1])
                elemsDragLift.addElementNoRep(cnceSort[j]);

        /* Hecho */
        int[] elementsDragLift = elemsDragLift.getTruncatedArray();

        kernel.out("   * elementsDragLift = "
                + elementsDragLift.length + " elementos");

        //for(int j =0; j<elementsDragLift.length; j++)
        //    System.out.println("ElemDragLift["+j+"] = "+elementsDragLift[j]);

        /* construimos the tablaDragLift */
        tablaDragLift = new int[elementsDragLift.length][3];

        // elements
        for (int j = 0; j < tablaDragLift.length; j++) {
            tablaDragLift[j][0] = elementsDragLift[j];
            tablaDragLift[j][1] = tablaDragLift[j][2] = -1;
        }

        // nodes
        for (int n = 0; n < nodesDragLift.length; n++) {
            // elements que contain it
            int node = nodesDragLift[n];
            for (int e = iCNLE[node]; e < iCNLE[node + 1]; e++)
                for (int i = 0; i < elementsDragLift.length; i++)
                    if (CNLE[e] == elementsDragLift[i]) {
                        // lo a�adimos
                        if (tablaDragLift[i][1] == -1)
                            tablaDragLift[i][1] = node;
                        else
                            tablaDragLift[i][2] = node;
                    }

        }

        // imprimimos tabla
        /*
        for(int j=0; j<tablaDragLift.length; j++)
           kernel.out("TDL "+tablaDragLift[j][0]+
           " "+mesh.ql[tablaDragLift[j][1]] + " "
           + mesh.ql[tablaDragLift[j][2]]);
        */

        // calculate vectores normal y tangentes
        nxDL = new double[tablaDragLift.length];
        nyDL = new double[nxDL.length];
        longDL = new double[nxDL.length];

        for (int j = 0; j < tablaDragLift.length; j++) {
            // Calculate segment of integracion DragLift
            double txDL = xcoord[tablaDragLift[j][1]] - xcoord[tablaDragLift[j][2]];
            double tyDL = ycoord[tablaDragLift[j][1]] - ycoord[tablaDragLift[j][2]];

            // Pasandolo a unitarias tenemos the vector unitario t==(tx,ty)
            // Almacenamos longitud, needed for integrar.
            double modulo = Math.sqrt(txDL * txDL + tyDL * tyDL);
            longDL[j] = modulo;

            // Calculate vector normal.
            // �DEBE APUNTAR HACIA EL INTERIOR DEL MALLADO! (fuera del cilinder)
            nxDL[j] = -tyDL / modulo;
            nyDL[j] = txDL / modulo;

            // nos apoyamos in que the (0,0) is fuera del mesh
            // NO PODEMOS CAMBIAR DE MALLADO!!
            if (nxDL[j] * xcoord[tablaDragLift[j][1]] +
                    nyDL[j] * ycoord[tablaDragLift[j][1]] < 0) {
                nxDL[j] *= -1;
                nyDL[j] *= -1;
            }

            // kernel.out(""+j+"\tnx ("+nxDL[j]+") \tny ("+nyDL[j]+") lon = "+longDL[j]);
        }

        //campoTest1 = new double[xcoord.length];
        //campoTest2 = new double[xcoord.length];

        // preparamos the salida
        try {
            if (filenameDragLift == null)
                filenameDragLift = kernel.getConfiguration().meshName + ".DL";

            pwDL = new PrintWriter(new FileWriter(filenameDragLift));

            if (mesh.isCilindroDragLift()) {
                pwP = new PrintWriter(new FileWriter(kernel.getConfiguration().meshName + ".P"));
                pwGV = new PrintWriter(new FileWriter(kernel.getConfiguration().meshName + ".GV"));

                int[] nDLl = mesh.getNodesDragLift();
                double[] x = mesh.getCoordinates(0);
                double[] y = mesh.getCoordinates(1);

                pwP.print("0.0");
                pwGV.print("0.0");

                for (int j = 0; j < nDLl.length; j++) {
                    pwP.print("\t" + Math.atan2(y[nDLl[j]], x[nDLl[j]]));
                    pwGV.print("\t" + Math.atan2(y[nDLl[j]], x[nDLl[j]]));
                }

                pwP.println();
                pwGV.println();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // cnce = null;


        double total = 0.0;
        for (int j = 0; j < tablaDragLift.length; j++)
            total += longDL[j];

        kernel.out("   * Longitud dominio integracion = " + total);
    }

}

