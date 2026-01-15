/*
 * Program : ADFC�
 * Class : org.jscience.fluids.mesh.CorregirOrientacion
 *         Corrige the sentido of numerar los nodes.
 *         �This class sigue siendo util?
 * Director : Leo Gonzalez Gutierrez <leo.gonzalez@iit.upco.es>
 * Programacion : Alejandro Rodriguez Gallego <balrog@amena.com>
 * Date  : 2/08/2001
 *
 * This program se distribuye bajo LICENCIA PUBLICA GNU (GPL)
 */
package org.jscience.physics.fluids.dynamics.mesh;

import org.jscience.physics.fluids.dynamics.KernelADFC;


// Breaks if the mesh is 3D.
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
class CorrectOrientation {
    /** DOCUMENT ME! */
    private KernelADFC kernel;

/**
     * Creates a new CorrectOrientation object.
     *
     * @param kadfc DOCUMENT ME!
     */
    public CorrectOrientation(KernelADFC kadfc) {
        kernel = kadfc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    void correct(NavierStokesMesh m) {
        if ((m.coordq.length != 2) || (m.nodesq.length != 6)) {
            System.out.println("CorrectOrientation: Only meshs 2D !!!");

            return;
        }

        double delta;
        double[] x;
        double[] y;
        int[] n1;
        int[] n2;
        int[] n3;
        int[] n4;
        int[] n5;
        int[] n6;
        int tmp;
        int total = 0;

        x = m.coordq[0];
        y = m.coordq[1];

        n1 = m.nodesq[0];
        n2 = m.nodesq[1];
        n3 = m.nodesq[2];
        n4 = m.nodesq[3];
        n5 = m.nodesq[4];
        n6 = m.nodesq[5];

        for (int j = 0; j < n1.length; j++) {
            delta = ((x[n2[j]] - x[n1[j]]) * (y[n3[j]] - y[n2[j]])) -
                ((y[n2[j]] - y[n1[j]]) * (x[n3[j]] - x[n2[j]]));

            if (delta < 0.0) {
                tmp = n2[j];
                n2[j] = n3[j];
                n3[j] = tmp;

                tmp = n4[j];
                n4[j] = n6[j];
                n6[j] = tmp;

                total++;
            }
        }

        if (total > 0) {
            kernel.out("Corrected the sense of  " + total + "/" + n1.length +
                " elements");
        }
    }
}
