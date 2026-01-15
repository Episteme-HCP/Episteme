/*
 * Program : A�DFC 1.2
 * Class : org.jscience.fluids.navierstokes.PerfilEntrada
 *         Genera profileof entrada y condicion initial en
 *         todo the campo.
 * Author : Alejandro "balrog" Rodriguez Gallego
 * Date  : 3/04/2001
 */
package org.jscience.physics.fluids.dynamics.navierstokes;

import org.jscience.physics.fluids.dynamics.KernelADFC;
import org.jscience.physics.fluids.dynamics.KernelADFCConfiguration;
import org.jscience.physics.fluids.dynamics.mesh.NavierStokesMesh;


// Automaticamente se toma the slip como the base of los perfiles of entrada
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class InputProfile {
    /** DOCUMENT ME! */
    private KernelADFC kernel;

    /** DOCUMENT ME! */
    private double MIN_ALTURA;

    /** DOCUMENT ME! */
    private double MAX_ALTURA;

    /** DOCUMENT ME! */
    private double COEFICIENTE;

    /** DOCUMENT ME! */
    private double[] xbase;

    /** DOCUMENT ME! */
    private double[] ybase;

    /** DOCUMENT ME! */
    private boolean seCreoPerfilEntrada;

/**
     * Creates a new InputProfile object.
     *
     * @param kadfc DOCUMENT ME!
     */
    public InputProfile(KernelADFC kadfc) {
        kernel = kadfc;

        // Tomamos configuration desde Kernel.
        KernelADFCConfiguration c = kernel.getConfiguration();
        MIN_ALTURA = c.minProfileHeight;
        MAX_ALTURA = c.maxProfileHeight;
        COEFICIENTE = c.coeficientDirichletProfile;

        seCreoPerfilEntrada = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param u0x DOCUMENT ME!
     * @param u0y DOCUMENT ME!
     * @param m DOCUMENT ME!
     */
    public void createInitialField(double[] u0x, double[] u0y,
        NavierStokesMesh m) {
        if (seCreoPerfilEntrada) {
            System.out.println("Condicion initial parabolica speed field.");

            double[] x;
            double[] y;
            x = m.getCoordinatesQuad(0);
            y = m.getCoordinatesQuad(1);

            for (int j = 0; j < u0x.length; j++) {
                u0x[j] = formula(x[j], y[j]);

                // System.out.println(" "+u0x[j]);
                u0y[j] = 0.0;
            }
        }
    }

    /* Modify vectores of data of MeshNavierStokes, actualizando information
     * Dirichlet for recrear profileof entrada.
     * This method se llama desde MeshNavierStokes.
     * Si se invoca con a <code>nperfil</code> null, quiere decir que no hay nodes
     * for the perfil. Deberia HAY_PERFIL ser false.
     */
    public void createDirichletProfile(int[] nperfil, int[] Dirichletq,
        double[][] vDirichletq, double[] coordx, double[] coordy,
        NavierStokesMesh m) {
        int corregidos = 0;

        if (nperfil == null) {
            System.out.println("No hay profiledirichlet...");

            return;
        }

        System.out.println("Creating profileof entrada... ");
        seCreoPerfilEntrada = true;

        xbase = new double[m.l1Slip.length + m.l2Slip.length];
        ybase = new double[xbase.length];

        // Create the array con los nodes of the base...
        System.out.println("\t* Nodos in the base: " + xbase.length);

        int c = 0;
        double[] x;
        double[] y;
        x = m.getCoordinatesQuad(0);
        y = m.getCoordinatesQuad(1);

        // Add nodes lineal-1-Slip
        for (int j = 0; j < m.l1Slip.length; j++) {
            int n = m.l1Slip[j];
            xbase[c] = x[n];
            ybase[c] = y[n];
            c++;
        }

        // Add nodes lineal-2-Slip
        for (int j = 0; j < m.l2Slip.length; j++) {
            int n = m.l2Slip[j];
            xbase[c] = x[n];
            ybase[c] = y[n];
            c++;
        }

        // Sort
        // Bubble sort
        for (int j = 0; j < (xbase.length - 1); j++)
            for (int i = j + 1; i < xbase.length; i++)
                if ((xbase[j] - xbase[i]) > 0.0) {
                    double t = xbase[j];
                    xbase[j] = xbase[i];
                    xbase[i] = t;

                    t = ybase[j];
                    ybase[j] = ybase[i];
                    ybase[i] = t;
                }

        // traverse node list belonging to the profile
        for (int j = 0; j < nperfil.length; j++) {
            int n = nperfil[j];

            // Search this node in los Dirichlet
            for (int i = 0; i < Dirichletq.length; i++) {
                if (Dirichletq[i] == nperfil[j]) {
                    vDirichletq[0][i] = formula(coordx[Dirichletq[i]],
                            coordy[Dirichletq[i]]);
                    corregidos++;

                    break;
                }
            }
        }

        // Tenemos que encontrarlos all.
        if (corregidos != nperfil.length) {
            System.out.println(
                "Internal Error: Incongruencia in data PerfilEntrada.");
            System.exit(0);
        }

        System.out.println("\t* El profile lo componen " + corregidos +
            " nodes");
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    double searchBase(double x) {
        for (int j = 0; j < (xbase.length - 1); j++)
            if (((xbase[j]) <= x) && (xbase[j + 1] >= x)) {
                return ybase[j] +
                (((x - xbase[j]) * (ybase[j + 1] - ybase[j])) / (xbase[j + 1] -
                xbase[j]));
            }

        return ybase[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double formula(double x, double y) {
        double min_altura = searchBase(x);

        // System.out.println(""+x+" "+y+" "+min_altura);
        if (y > (MAX_ALTURA + min_altura)) {
            return 1.0;
        } else if (y < min_altura) {
            return 0.0;
        } else {
            double d = Math.pow((y - min_altura) / (MAX_ALTURA), COEFICIENTE);

            // System.out.println("-> "+d);
            return d;
        }
    }
}
