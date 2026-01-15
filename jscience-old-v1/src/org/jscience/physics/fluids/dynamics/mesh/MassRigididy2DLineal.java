/*
 * Program : ADFC�
 * Class : org.jscience.fluids.mesh.MasasRigidez2DLineal
 *         Calcula las matrices of masses y rigidity lineales for 2D.
 * Director : Leo Gonzalez Gutierrez <leo.gonzalez@iit.upco.es>
 * Programacion : Alejandro Rodriguez Gallego <balrog@amena.com>
 * Date  : 2/08/2001
 *
 * This program se distribuye bajo LICENCIA PUBLICA GNU (GPL)
 */
package org.jscience.physics.fluids.dynamics.mesh;

/**
 */
import org.jscience.physics.fluids.dynamics.KernelADFC;
import org.jscience.physics.fluids.dynamics.util.Matrix;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class MassRigididy2DLineal extends Object {
/**
     * Creates new MasasRigidez2DLineal
     */
    public MassRigididy2DLineal() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param kernel DOCUMENT ME!
     * @param masses DOCUMENT ME!
     * @param rigidity DOCUMENT ME!
     * @param coordl DOCUMENT ME!
     * @param nodesl DOCUMENT ME!
     */
    static public void generate2(KernelADFC kernel, Matrix masses,
        Matrix rigidity, double[][] coordl, int[][] nodesl) {
        double x1;
        double y1;
        double x2;
        double y2;
        double x3;
        double y3;
        double e11;
        double e21;
        double e31;
        double e12;
        double e22;
        double e32;
        double a11;
        double a12;
        double a13;
        double a22;
        double a23;
        double a33;
        double nabla;
        double surface;

        kernel.out(
            "Generating lineal matrices <B>masses</B> and <B>rigidity</B>");

        for (int e = 0; e < nodesl[0].length; e++) {
            x1 = coordl[0][nodesl[0][e]];
            y1 = coordl[1][nodesl[0][e]];
            x2 = coordl[0][nodesl[1][e]];
            y2 = coordl[1][nodesl[1][e]];
            x3 = coordl[0][nodesl[2][e]];
            y3 = coordl[1][nodesl[2][e]];

            // previous needed calculations.
            e11 = x2 - x1;
            e21 = x3 - x2;
            e31 = x1 - x3;
            e12 = y2 - y1;
            e22 = y3 - y2;
            e32 = y1 - y3;

            nabla = Math.abs(2.0 * ((e11 * e22) - (e12 * e21)));
            surface = Math.abs((((x2 - x1) * (y3 - y1)) -
                    ((x3 - x1) * (y2 - y1))) / 2.0);

            a11 = ((e22 * e22) + (e21 * e21)) / nabla;
            a12 = ((e22 * e32) + (e21 * e31)) / nabla;
            a13 = ((e22 * e12) + (e21 * e11)) / nabla;
            a22 = ((e32 * e32) + (e31 * e31)) / nabla;
            a23 = ((e32 * e12) + (e31 * e11)) / nabla;
            a33 = ((e12 * e12) + (e11 * e11)) / nabla;

            // NODE 1 -----
            rigidity.sumParallel(nodesl[0][e], nodesl[0][e], a11, masses,
                surface / 6);
            rigidity.sumParallel(nodesl[0][e], nodesl[1][e], a12, masses,
                surface / 12);
            rigidity.sumParallel(nodesl[0][e], nodesl[2][e], a13, masses,
                surface / 12);

            // NODE 2 -----
            rigidity.sumParallel(nodesl[1][e], nodesl[0][e], a12, masses,
                surface / 12);
            rigidity.sumParallel(nodesl[1][e], nodesl[1][e], a22, masses,
                surface / 6);
            rigidity.sumParallel(nodesl[1][e], nodesl[2][e], a23, masses,
                surface / 12);

            // NODE 3 -----
            rigidity.sumParallel(nodesl[2][e], nodesl[0][e], a13, masses,
                surface / 12);
            rigidity.sumParallel(nodesl[2][e], nodesl[1][e], a23, masses,
                surface / 12);
            rigidity.sumParallel(nodesl[2][e], nodesl[2][e], a33, masses,
                surface / 6);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param kernel DOCUMENT ME!
     * @param masses DOCUMENT ME!
     * @param rigidity DOCUMENT ME!
     * @param coordl DOCUMENT ME!
     * @param nodesl DOCUMENT ME!
     */
    static public void generate(KernelADFC kernel, Matrix masses,
        Matrix rigidity, double[][] coordl, int[][] nodesl) {
        double x1;
        double y1;
        double x2;
        double y2;
        double x3;
        double y3;
        double e11;
        double e21;
        double e31;
        double e12;
        double e22;
        double e32;
        double a11;
        double a12;
        double a13;
        double a22;
        double a23;
        double a33;
        double surface;
        double delta;
        double dl1dx;
        double dl2dx;
        double dl3dx;
        double dl1dy;
        double dl2dy;
        double dl3dy;

        kernel.out(
            "Generating lineal matrices <B>masses</B> and <B>rigidity</B>");

        for (int e = 0; e < nodesl[0].length; e++) {
            x1 = coordl[0][nodesl[0][e]];
            y1 = coordl[1][nodesl[0][e]];
            x2 = coordl[0][nodesl[1][e]];
            y2 = coordl[1][nodesl[1][e]];
            x3 = coordl[0][nodesl[2][e]];
            y3 = coordl[1][nodesl[2][e]];

            // previous calculations that will be needed.
            // derivatives of lambda with respect x and y.
            // Calculations of the surface, through the modulus of the
            // vectorial product, which give us the area of the paralelepipedo
            // (delta is twice the area of our element)
            delta = ((x2 - x1) * (y3 - y1)) - ((x3 - x1) * (y2 - y1));
            dl1dx = (y2 - y3) / delta;
            dl1dy = (x3 - x2) / delta;
            dl2dx = (y3 - y1) / delta;
            dl2dy = (x1 - x3) / delta;
            dl3dx = (y1 - y2) / delta;
            dl3dy = (x2 - x1) / delta;

            surface = Math.abs(delta / 2.0);

            a11 = surface * ((dl1dx * dl1dx) + (dl1dy * dl1dy));
            a12 = surface * ((dl1dx * dl2dx) + (dl1dy * dl2dy));
            a13 = surface * ((dl1dx * dl3dx) + (dl1dy * dl3dy));
            a22 = surface * ((dl2dx * dl2dx) + (dl2dy * dl2dy));
            a23 = surface * ((dl2dx * dl3dx) + (dl2dy * dl3dy));
            a33 = surface * ((dl3dx * dl3dx) + (dl3dy * dl3dy));

            // NODE 1 -----
            rigidity.sumParallel(nodesl[0][e], nodesl[0][e], a11, masses,
                surface / 6);
            rigidity.sumParallel(nodesl[0][e], nodesl[1][e], a12, masses,
                surface / 12);
            rigidity.sumParallel(nodesl[0][e], nodesl[2][e], a13, masses,
                surface / 12);

            // NODE 2 -----
            rigidity.sumParallel(nodesl[1][e], nodesl[0][e], a12, masses,
                surface / 12);
            rigidity.sumParallel(nodesl[1][e], nodesl[1][e], a22, masses,
                surface / 6);
            rigidity.sumParallel(nodesl[1][e], nodesl[2][e], a23, masses,
                surface / 12);

            // NODE 3 -----
            rigidity.sumParallel(nodesl[2][e], nodesl[0][e], a13, masses,
                surface / 12);
            rigidity.sumParallel(nodesl[2][e], nodesl[1][e], a23, masses,
                surface / 12);
            rigidity.sumParallel(nodesl[2][e], nodesl[2][e], a33, masses,
                surface / 6);
        }
    }
}
