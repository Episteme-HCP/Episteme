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
class MassRigidity2DQuad extends Object {
/**
     * Creates new MasasRigidez2DCuad
     */
    public MassRigidity2DQuad() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param kernel DOCUMENT ME!
     * @param massesCuad DOCUMENT ME!
     * @param rigidityCuad DOCUMENT ME!
     * @param coordq DOCUMENT ME!
     * @param nodesq DOCUMENT ME!
     */
    static public void generate2(KernelADFC kernel, Matrix massesCuad,
        Matrix rigidityCuad, double[][] coordq, int[][] nodesq) {
        int i;

        kernel.out(
            "Generating quadratic matrices <B>masses</B> and <B>rigidity</B>");

        long startTime = System.currentTimeMillis();

        int totalElements = nodesq[0].length;
        double x1;
        double y1;
        double x2;
        double y2;
        double x3;
        double y3;
        double dl1dx;
        double dl1dy;
        double dl2dx;
        double dl2dy;
        double dl3dx;
        double dl3dy;
        double a11;
        double a12;
        double a13;
        double a14;
        double a15;
        double a16;
        double a22;
        double a23;
        double a24;
        double a25;
        double a26;
        double a33;
        double a34;
        double a35;
        double a36;
        double a44;
        double a45;
        double a46;
        double a55;
        double a56;
        double a66;

        a11 = a12 = a13 = a14 = a15 = a16 = a22 = a23 = a24 = a25 = a26 = a33 = a34 = a35 = a36 = a44 = a45 = a46 = a55 = a56 = a66 = 0;

        double delta;
        double surface;

        for (int e = 0; e < totalElements; e++) {
            x1 = coordq[0][nodesq[0][e]];
            y1 = coordq[1][nodesq[0][e]];
            x2 = coordq[0][nodesq[1][e]];
            y2 = coordq[1][nodesq[1][e]];
            x3 = coordq[0][nodesq[2][e]];
            y3 = coordq[1][nodesq[2][e]];

            // previous calculations that will be needed.
            // derivatives of lambda with respect x and y.
            // Calculations of the surface, through the modulus of the
            // vectorial product, which give us the area of the paralelepipedo
            // (delta is twice the area of our element)
            delta = ((x2 - x1) * (y3 - y2)) - ((y2 - y1) * (x3 - x2));
            surface = Math.abs(delta / 2.0);

            dl1dx = (y2 - y3) / delta;
            dl1dy = (x3 - x2) / delta;
            dl2dx = (y3 - y1) / delta;
            dl2dy = (x1 - x3) / delta;
            dl3dx = (y1 - y2) / delta;
            dl3dy = (x2 - x1) / delta;

            // Esta matrix A(ek) viene de:
            // INTEGRAL(ek, grad(phi_j).grad(phi_i))   in pag. 158
            // The matrix A es simetrica
            // evaluaremos the integral mediante the media aritmetica
            // del value del integrando in los points medios of los
            // sides, multiplydo by the surface.
            double factor = surface / 3.0;

            // Integrales of nodes vertice con nodes vertice (Ok !)
            a11 = surface * ((dl1dx * dl1dx) + (dl1dy * dl1dy));
            a12 = -factor * ((dl1dx * dl2dx) + (dl1dy * dl2dy));
            a13 = -factor * ((dl1dx * dl3dx) + (dl1dy * dl3dy));
            a22 = surface * ((dl2dx * dl2dx) + (dl2dy * dl2dy));
            a23 = -factor * ((dl2dx * dl3dx) + (dl2dy * dl3dy));
            a33 = surface * ((dl3dx * dl3dx) + (dl3dy * dl3dy));

            // Integrales of nodes medios con medios (Ok !)
            a44 = 8 * factor * ((dl1dx * dl2dx) + (dl1dy * dl2dy) +
                (dl1dx * dl1dx) + (dl1dy * dl1dy) + (dl2dx * dl2dx) +
                (dl2dy * dl2dy));
            a45 = 4 * factor * ((dl2dx * (dl1dx + dl2dx + dl3dx)) +
                (2 * dl1dx * dl3dx) + (dl2dy * (dl1dy + dl2dy + dl3dy)) +
                (2 * dl1dy * dl3dy));
            a46 = 4 * factor * ((dl1dx * (dl1dx + dl2dx + dl3dx)) +
                (2 * dl3dx * dl2dx) + (dl1dy * (dl1dy + dl2dy + dl3dy)) +
                (2 * dl3dy * dl2dy));
            a55 = 8 * factor * ((dl2dx * dl3dx) + (dl2dy * dl3dy) +
                (dl2dx * dl2dx) + (dl2dy * dl2dy) + (dl3dx * dl3dx) +
                (dl3dy * dl3dy));
            a56 = 4 * factor * ((dl3dx * (dl1dx + dl2dx + dl3dx)) +
                (2 * dl1dx * dl2dx) + (dl3dy * (dl1dy + dl2dy + dl3dy)) +
                (2 * dl1dy * dl2dy));
            a66 = 8 * factor * ((dl1dx * dl3dx) + (dl1dy * dl3dy) +
                (dl1dx * dl1dx) + (dl1dy * dl1dy) + (dl3dx * dl3dx) +
                (dl3dy * dl3dy));

            // Integrales of nodes medios con vertices
            // tocantes ...
            // �Sera cierto? a14 = a24 // a16 = a36 // a25 = a35 (Ok !)
            a14 = 4 * factor * ((dl1dx * dl2dx) + (dl1dy * dl2dy));
            a16 = 4 * factor * ((dl1dx * dl3dx) + (dl1dy * dl3dy));
            a24 = a14;
            a25 = 4 * factor * ((dl2dx * dl3dx) + (dl2dy * dl3dy));
            a35 = a25;
            a36 = a16;

            // opuestos ... (Ok !)
            a15 = 0.000;
            a34 = 0.000;
            a26 = 0.000;

            // A continuacion incorporamos a las matrices globales of masses y
            // rigidity, the aportacion of the matrix of element recien calculada.
            //
            // Dada the identica estructura NNVI/NVPN of ambas matrices,
            // emplearemos the method acelerado Matrix.sumaParalela
            // NODE 1 -----
            i = nodesq[0][e];

            // aportaciones a the matrix of rigidity y masses
            rigidityCuad.sumParallel(i, nodesq[0][e], a11, massesCuad,
                surface / 30); // surface/18);
            rigidityCuad.sumParallel(i, nodesq[1][e], a12, massesCuad,
                -surface / 180); //surface/180);
            rigidityCuad.sumParallel(i, nodesq[2][e], a13, massesCuad,
                -surface / 180); //surface/180);
            rigidityCuad.sumParallel(i, nodesq[3][e], a14, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[4][e], a15, massesCuad,
                -surface / 45); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[5][e], a16, massesCuad, 0); //-surface/45);

            // NODE 2 -----
            i = nodesq[1][e];

            // aportaciones a the matrix of rigidity y masses
            rigidityCuad.sumParallel(i, nodesq[0][e], a12, massesCuad,
                -surface / 180); //surface/180);
            rigidityCuad.sumParallel(i, nodesq[1][e], a22, massesCuad,
                surface / 30); //surface/18);
            rigidityCuad.sumParallel(i, nodesq[2][e], a23, massesCuad,
                -surface / 180); //surface/180);
            rigidityCuad.sumParallel(i, nodesq[3][e], a24, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[4][e], a25, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[5][e], a26, massesCuad,
                -surface / 45); //-surface/45);

            // NODE 3 -----
            i = nodesq[2][e];

            // aportaciones a the matrix of rigidity y masses
            rigidityCuad.sumParallel(i, nodesq[0][e], a13, massesCuad,
                -surface / 180); //surface/180);
            rigidityCuad.sumParallel(i, nodesq[1][e], a23, massesCuad,
                -surface / 180); //surface/180);
            rigidityCuad.sumParallel(i, nodesq[2][e], a33, massesCuad,
                surface / 30); //surface/18);
            rigidityCuad.sumParallel(i, nodesq[3][e], a34, massesCuad,
                -surface / 45); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[4][e], a35, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[5][e], a36, massesCuad, 0); //-surface/45);

            // NODE 4 -----
            i = nodesq[3][e];

            // aportaciones a the matrix of rigidity y masses
            rigidityCuad.sumParallel(i, nodesq[0][e], a14, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[1][e], a24, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[2][e], a34, massesCuad,
                -surface / 45); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[3][e], a44, massesCuad,
                (surface * 8) / 45); //surface/4.5);
            rigidityCuad.sumParallel(i, nodesq[4][e], a45, massesCuad,
                (surface * 4) / 45);
            rigidityCuad.sumParallel(i, nodesq[5][e], a46, massesCuad,
                (surface * 4) / 45);

            // NODE 5 -----
            i = nodesq[4][e];

            // aportaciones a the matrix of rigidity y masses
            rigidityCuad.sumParallel(i, nodesq[0][e], a15, massesCuad,
                -surface / 45); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[1][e], a25, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[2][e], a35, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[3][e], a45, massesCuad,
                (surface * 4) / 45);
            rigidityCuad.sumParallel(i, nodesq[4][e], a55, massesCuad,
                (surface * 8) / 45); //surface/4.5);
            rigidityCuad.sumParallel(i, nodesq[5][e], a56, massesCuad,
                (surface * 4) / 45);

            // NODE 6 -----
            i = nodesq[5][e];

            // aportaciones a the matrix of rigidity y masses
            rigidityCuad.sumParallel(i, nodesq[0][e], a16, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[1][e], a26, massesCuad,
                -surface / 45); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[2][e], a36, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[3][e], a46, massesCuad,
                (surface * 4) / 45);
            rigidityCuad.sumParallel(i, nodesq[4][e], a56, massesCuad,
                (surface * 4) / 45);
            rigidityCuad.sumParallel(i, nodesq[5][e], a66, massesCuad,
                (surface * 8) / 45); //surface/4.5);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param kernel DOCUMENT ME!
     * @param massesCuad DOCUMENT ME!
     * @param rigidityCuad DOCUMENT ME!
     * @param coordq DOCUMENT ME!
     * @param nodesq DOCUMENT ME!
     */
    static public void generate(KernelADFC kernel, Matrix massesCuad,
        Matrix rigidityCuad, double[][] coordq, int[][] nodesq) {
        int i;

        kernel.out(
            "Generando matrices quadratics <B>masas</B> y <B>rigidez</B>");

        long startTime = System.currentTimeMillis();

        int totalElements = nodesq[0].length;
        double x1;
        double y1;
        double x2;
        double y2;
        double x3;
        double y3;
        double dl1dx;
        double dl1dy;
        double dl2dx;
        double dl2dy;
        double dl3dx;
        double dl3dy;
        double a11;
        double a12;
        double a13;
        double a14;
        double a15;
        double a16;
        double a22;
        double a23;
        double a24;
        double a25;
        double a26;
        double a33;
        double a34;
        double a35;
        double a36;
        double a44;
        double a45;
        double a46;
        double a55;
        double a56;
        double a66;

        a11 = a12 = a13 = a14 = a15 = a16 = a22 = a23 = a24 = a25 = a26 = a33 = a34 = a35 = a36 = a44 = a45 = a46 = a55 = a56 = a66 = 0;

        double delta;
        double surface;

        for (int e = 0; e < totalElements; e++) {
            x1 = coordq[0][nodesq[0][e]];
            y1 = coordq[1][nodesq[0][e]];
            x2 = coordq[0][nodesq[1][e]];
            y2 = coordq[1][nodesq[1][e]];
            x3 = coordq[0][nodesq[2][e]];
            y3 = coordq[1][nodesq[2][e]];

            // Calculos previos que necesitaremos.
            // derivatives of las lambda respecto x e y.
            // Calculate the surface, mediante the modulo del
            // producto vectorial, que nos da the area del paralelepipedo
            // (delta es dos times the area of nuestro element)
            delta = ((x2 - x1) * (y3 - y2)) - ((y2 - y1) * (x3 - x2));
            surface = Math.abs(delta / 2.0);

            dl1dx = (y2 - y3) / delta;
            dl1dy = (x3 - x2) / delta;
            dl2dx = (y3 - y1) / delta;
            dl2dy = (x1 - x3) / delta;
            dl3dx = (y1 - y2) / delta;
            dl3dy = (x2 - x1) / delta;

            // Esta matrix A(ek) viene de:
            // INTEGRAL(ek, grad(phi_j).grad(phi_i))   in pag. 158
            // The matrix A es simetrica
            // evaluaremos the integral mediante the media aritmetica
            // del value del integrando in los points medios of los
            // sides, multiplydo by the surface.
            double factor = surface / 3.0;

            // Integrales of nodes vertice con nodes vertice (Ok !)
            a11 = surface * ((dl1dx * dl1dx) + (dl1dy * dl1dy));
            a12 = -factor * ((dl1dx * dl2dx) + (dl1dy * dl2dy));
            a13 = -factor * ((dl1dx * dl3dx) + (dl1dy * dl3dy));
            a22 = surface * ((dl2dx * dl2dx) + (dl2dy * dl2dy));
            a23 = -factor * ((dl2dx * dl3dx) + (dl2dy * dl3dy));
            a33 = surface * ((dl3dx * dl3dx) + (dl3dy * dl3dy));

            // Integrales of nodes medios con medios (Ok !)
            a44 = 8 * factor * ((dl1dx * dl2dx) + (dl1dy * dl2dy) +
                (dl1dx * dl1dx) + (dl1dy * dl1dy) + (dl2dx * dl2dx) +
                (dl2dy * dl2dy));
            a45 = 4 * factor * ((dl2dx * (dl1dx + dl2dx + dl3dx)) +
                (2 * dl1dx * dl3dx) + (dl2dy * (dl1dy + dl2dy + dl3dy)) +
                (2 * dl1dy * dl3dy));
            a46 = 4 * factor * ((dl1dx * (dl1dx + dl2dx + dl3dx)) +
                (2 * dl3dx * dl2dx) + (dl1dy * (dl1dy + dl2dy + dl3dy)) +
                (2 * dl3dy * dl2dy));
            a55 = 8 * factor * ((dl2dx * dl3dx) + (dl2dy * dl3dy) +
                (dl2dx * dl2dx) + (dl2dy * dl2dy) + (dl3dx * dl3dx) +
                (dl3dy * dl3dy));
            a56 = 4 * factor * ((dl3dx * (dl1dx + dl2dx + dl3dx)) +
                (2 * dl1dx * dl2dx) + (dl3dy * (dl1dy + dl2dy + dl3dy)) +
                (2 * dl1dy * dl2dy));
            a66 = 8 * factor * ((dl1dx * dl3dx) + (dl1dy * dl3dy) +
                (dl1dx * dl1dx) + (dl1dy * dl1dy) + (dl3dx * dl3dx) +
                (dl3dy * dl3dy));

            // Integrales of nodes medios con vertices
            // tocantes ...
            // �Sera cierto? a14 = a24 // a16 = a36 // a25 = a35 (Ok !)
            a14 = 4 * factor * ((dl1dx * dl2dx) + (dl1dy * dl2dy));
            a16 = 4 * factor * ((dl1dx * dl3dx) + (dl1dy * dl3dy));
            a24 = a14;
            a25 = 4 * factor * ((dl2dx * dl3dx) + (dl2dy * dl3dy));
            a35 = a25;
            a36 = a16;

            // opuestos ... (Ok !)
            a15 = 0.000;
            a34 = 0.000;
            a26 = 0.000;

            // A continuacion incorporamos a las matrices globales of masses y
            // rigidity, the aportacion of the matrix of element recien calculada.
            //
            // Dada the identica estructura NNVI/NVPN of ambas matrices,
            // emplearemos the method acelerado Matrix.sumaParalela
            // NODE 1 -----
            i = nodesq[0][e];

            // aportaciones a the matrix of rigidity y masses
            rigidityCuad.sumParallel(i, nodesq[0][e], a11, massesCuad,
                surface / 30); // surface/18);
            rigidityCuad.sumParallel(i, nodesq[1][e], a12, massesCuad,
                -surface / 180); //surface/180);
            rigidityCuad.sumParallel(i, nodesq[2][e], a13, massesCuad,
                -surface / 180); //surface/180);
            rigidityCuad.sumParallel(i, nodesq[3][e], a14, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[4][e], a15, massesCuad,
                -surface / 45); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[5][e], a16, massesCuad, 0); //-surface/45);

            // NODE 2 -----
            i = nodesq[1][e];

            // aportaciones a the matrix of rigidity y masses
            rigidityCuad.sumParallel(i, nodesq[0][e], a12, massesCuad,
                -surface / 180); //surface/180);
            rigidityCuad.sumParallel(i, nodesq[1][e], a22, massesCuad,
                surface / 30); //surface/18);
            rigidityCuad.sumParallel(i, nodesq[2][e], a23, massesCuad,
                -surface / 180); //surface/180);
            rigidityCuad.sumParallel(i, nodesq[3][e], a24, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[4][e], a25, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[5][e], a26, massesCuad,
                -surface / 45); //-surface/45);

            // NODE 3 -----
            i = nodesq[2][e];

            // aportaciones a the matrix of rigidity y masses
            rigidityCuad.sumParallel(i, nodesq[0][e], a13, massesCuad,
                -surface / 180); //surface/180);
            rigidityCuad.sumParallel(i, nodesq[1][e], a23, massesCuad,
                -surface / 180); //surface/180);
            rigidityCuad.sumParallel(i, nodesq[2][e], a33, massesCuad,
                surface / 30); //surface/18);
            rigidityCuad.sumParallel(i, nodesq[3][e], a34, massesCuad,
                -surface / 45); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[4][e], a35, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[5][e], a36, massesCuad, 0); //-surface/45);

            // NODE 4 -----
            i = nodesq[3][e];

            // aportaciones a the matrix of rigidity y masses
            rigidityCuad.sumParallel(i, nodesq[0][e], a14, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[1][e], a24, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[2][e], a34, massesCuad,
                -surface / 45); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[3][e], a44, massesCuad,
                (surface * 8) / 45); //surface/4.5);
            rigidityCuad.sumParallel(i, nodesq[4][e], a45, massesCuad,
                (surface * 4) / 45);
            rigidityCuad.sumParallel(i, nodesq[5][e], a46, massesCuad,
                (surface * 4) / 45);

            // NODE 5 -----
            i = nodesq[4][e];

            // aportaciones a the matrix of rigidity y masses
            rigidityCuad.sumParallel(i, nodesq[0][e], a15, massesCuad,
                -surface / 45); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[1][e], a25, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[2][e], a35, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[3][e], a45, massesCuad,
                (surface * 4) / 45);
            rigidityCuad.sumParallel(i, nodesq[4][e], a55, massesCuad,
                (surface * 8) / 45); //surface/4.5);
            rigidityCuad.sumParallel(i, nodesq[5][e], a56, massesCuad,
                (surface * 4) / 45);

            // NODE 6 -----
            i = nodesq[5][e];

            // aportaciones a the matrix of rigidity y masses
            rigidityCuad.sumParallel(i, nodesq[0][e], a16, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[1][e], a26, massesCuad,
                -surface / 45); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[2][e], a36, massesCuad, 0); //-surface/45);
            rigidityCuad.sumParallel(i, nodesq[3][e], a46, massesCuad,
                (surface * 4) / 45);
            rigidityCuad.sumParallel(i, nodesq[4][e], a56, massesCuad,
                (surface * 4) / 45);
            rigidityCuad.sumParallel(i, nodesq[5][e], a66, massesCuad,
                (surface * 8) / 45); //surface/4.5);
        }
    }
}
