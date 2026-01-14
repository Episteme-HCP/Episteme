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
import org.jscience.physics.fluids.dynamics.util.Vector2DFastInt;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class Divergence2D extends Object {
/**
     * Creates new Divergence2D
     */
    public Divergence2D() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param kernel DOCUMENT ME!
     * @param coordl DOCUMENT ME!
     * @param nodesl DOCUMENT ME!
     * @param coordq DOCUMENT ME!
     * @param nodesq DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public Matrix getStructureDivergence(KernelADFC kernel,
        double[][] coordl, int[][] nodesl, double[][] coordq, int[][] nodesq) {
        // Firstly we should generate the estructure NNVI/NVPN
        // the rows will be stored in Vector2DFastInt
        Vector2DFastInt v2Dri = new Vector2DFastInt(coordl[0].length, 30, 10);

        // For all elements, add in the row of the speed nodes
        // the pressure nodes that are also included in the element.
        for (int e = 0; e < nodesl[0].length; e++) {
            for (int v = 0; v < 6; v++)
                for (int p = 0; p < 3; p++)
                    v2Dri.addElementNoRep(nodesl[p][e], nodesq[v][e]);
        }

        // Building NNVI/NVPN
        int[] nvpn = new int[coordl[0].length + 1];
        int tam = 0;
        nvpn[0] = 0;

        for (int j = 0; j < coordl[0].length; j++) {
            tam += v2Dri.size(j);
            nvpn[j + 1] = tam;
        }

        int[] nnvi = new int[tam];
        int pos = 0;
        v2Dri.sortArrayRows(-1);

        int[] data = v2Dri.array();

        for (int j = 0; j < coordl[0].length; j++) {
            int offset = v2Dri.getStartOfRow(j);

            for (int k = 0; k < v2Dri.size(j); k++)
                nnvi[pos++] = data[offset + k];
        }

        // Building divergenceX, and from its structure, divergenceY
        return new Matrix(null, nvpn, nnvi, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param kernel DOCUMENT ME!
     * @param coordl DOCUMENT ME!
     * @param nodesl DOCUMENT ME!
     * @param coordq DOCUMENT ME!
     * @param nodesq DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public Matrix getTransposedStructureDivergence(KernelADFC kernel,
        double[][] coordl, int[][] nodesl, double[][] coordq, int[][] nodesq) {
        // first we should generate the structure NNVI/NVPN
        // the rows are stored in Vector2DFastInt
        Vector2DFastInt v2Dri = new Vector2DFastInt(coordq[0].length, 20, 10);

        // For all  elements, add in the row of the speed nodes
        // the pressure nodes that are also included in the element.
        for (int e = 0; e < nodesq[0].length; e++) {
            for (int v = 0; v < 6; v++)
                for (int p = 0; p < 3; p++)
                    v2Dri.addElementNoRep(nodesq[v][e], nodesl[p][e]);
        }

        // Building NNVI/NVPN
        int[] nvpn = new int[coordq[0].length + 1];
        int tam = 0;
        nvpn[0] = 0;

        for (int j = 0; j < coordq[0].length; j++) {
            tam += v2Dri.size(j);
            nvpn[j + 1] = tam;
        }

        int[] nnvi = new int[tam];
        int pos = 0;
        v2Dri.sortArrayRows(-1);

        int[] data = v2Dri.array();

        for (int j = 0; j < coordq[0].length; j++) {
            int offset = v2Dri.getStartOfRow(j);

            for (int k = 0; k < v2Dri.size(j); k++)
                nnvi[pos++] = data[offset + k];
        }

        // Building divergenceXt, and from its structure, divergenceYt
        return new Matrix(null, nvpn, nnvi, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param kernel DOCUMENT ME!
     * @param divergenceX DOCUMENT ME!
     * @param divergenceY DOCUMENT ME!
     * @param divergenceXCuad DOCUMENT ME!
     * @param divergenceYCuad DOCUMENT ME!
     * @param coordl DOCUMENT ME!
     * @param nodesl DOCUMENT ME!
     * @param coordq DOCUMENT ME!
     * @param nodesq DOCUMENT ME!
     */
    static public void generate(KernelADFC kernel, Matrix divergenceX,
        Matrix divergenceY, Matrix divergenceXCuad, Matrix divergenceYCuad,
        double[][] coordl, int[][] nodesl, double[][] coordq, int[][] nodesq) {
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
        double delta;
        double surface;
        double d11x;
        double d12x;
        double d13x;
        double d21x;
        double d22x;
        double d23x;
        double d31x;
        double d32x;
        double d33x;
        double d41x;
        double d42x;
        double d43x;
        double d51x;
        double d52x;
        double d53x;
        double d61x;
        double d62x;
        double d63x;

        kernel.out("Generating matrices <B>divergenceX</B>, <B>divergenceY</B>" +
            ", <B>divergenceX<SUP>t</SUP></B> and <B>divergenceY<SUP>t</SUP></B>");

        for (int e = 0; e < nodesq[0].length; e++) {
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
            delta = ((x2 - x1) * (y3 - y1)) - ((x3 - x1) * (y2 - y1));
            dl1dx = (y2 - y3) / delta;
            dl1dy = (x3 - x2) / delta;
            dl2dx = (y3 - y1) / delta;
            dl2dy = (x1 - x3) / delta;
            dl3dx = (y1 - y2) / delta;
            dl3dy = (x2 - x1) / delta;

            surface = Math.abs(delta / 2.0);

            // We use Matrix.sumParallel
            // speed: 1    pres: 1     Ok
            divergenceX.sumParallel(nodesl[0][e], nodesq[0][e],
                (surface * dl1dx) / 3.0, divergenceY, (surface * dl1dy) / 3.0);
            divergenceXCuad.sumParallel(nodesq[0][e], nodesl[0][e],
                (surface * dl1dx) / 3.0, divergenceYCuad,
                (surface * dl1dy) / 3.0);

            // speed: 1    pres: 2     Ok
            //divergenceX.sumaElement(nodesq[0][e],nodesl[1][e], 0.0);
            //divergenceY.sumaElement(nodesq[0][e],nodesl[1][e], 0.0);
            // speed: 1    pres: 3     Ok
            //divergenceX.sumaElement(nodesq[0][e],nodesl[2][e], 0.0);
            //divergenceY.sumaElement(nodesq[0][e],nodesl[2][e], 0.0);
            // speed: 2    pres: 1     Ok
            //divergenceX.sumaElement(nodesq[1][e],nodesl[0][e], 0.0);
            //divergenceY.sumaElement(nodesq[1][e],nodesl[0][e], 0.0);
            // speed: 2    pres: 2     Ok
            divergenceX.sumParallel(nodesl[1][e], nodesq[1][e],
                (surface * dl2dx) / 3.0, divergenceY, (surface * dl2dy) / 3.0);
            divergenceXCuad.sumParallel(nodesq[1][e], nodesl[1][e],
                (surface * dl2dx) / 3.0, divergenceYCuad,
                (surface * dl2dy) / 3.0);

            // speed: 2    pres: 3     Ok
            //divergenceX.sumaElement(nodesq[1][e],nodesl[2][e], 0.0);
            //divergenceY.sumaElement(nodesq[1][e],nodesl[2][e], 0.0);
            // speed: 3    pres: 1     Ok
            //divergenceX.sumaElement(nodesq[2][e],nodesl[0][e], 0.0);
            //divergenceY.sumaElement(nodesq[2][e],nodesl[0][e], 0.0);
            // speed: 3    pres: 2     Ok
            //divergenceX.sumaElement(nodesq[2][e],nodesl[1][e], 0.0);
            //divergenceY.sumaElement(nodesq[2][e],nodesl[1][e], 0.0);
            // speed: 3    pres: 3     Ok
            divergenceX.sumParallel(nodesl[2][e], nodesq[2][e],
                (surface * dl3dx) / 3.0, divergenceY, (surface * dl3dy) / 3.0);
            divergenceXCuad.sumParallel(nodesq[2][e], nodesl[2][e],
                (surface * dl3dx) / 3.0, divergenceYCuad,
                (surface * dl3dy) / 3.0);

            // speed: 4    pres: 1     Ok
            divergenceX.sumParallel(nodesl[0][e], nodesq[3][e],
                ((dl1dx + (2 * dl2dx)) * surface) / 3.0, divergenceY,
                ((dl1dy + (2 * dl2dy)) * surface) / 3.0);
            divergenceXCuad.sumParallel(nodesq[3][e], nodesl[0][e],
                ((dl1dx + (2 * dl2dx)) * surface) / 3.0, divergenceYCuad,
                ((dl1dy + (2 * dl2dy)) * surface) / 3.0);

            // speed: 4    pres: 2     Ok
            divergenceX.sumParallel(nodesl[1][e], nodesq[3][e],
                ((dl2dx + (2 * dl1dx)) * surface) / 3.0, divergenceY,
                ((dl2dy + (2 * dl1dy)) * surface) / 3.0);
            divergenceXCuad.sumParallel(nodesq[3][e], nodesl[1][e],
                ((dl2dx + (2 * dl1dx)) * surface) / 3.0, divergenceYCuad,
                ((dl2dy + (2 * dl1dy)) * surface) / 3.0);

            // speed: 4    pres: 3     Ok
            divergenceX.sumParallel(nodesl[2][e], nodesq[3][e],
                ((dl1dx + dl2dx) * surface) / 3.0, divergenceY,
                ((dl1dy + dl2dy) * surface) / 3.0);
            divergenceXCuad.sumParallel(nodesq[3][e], nodesl[2][e],
                ((dl1dx + dl2dx) * surface) / 3.0, divergenceYCuad,
                ((dl1dy + dl2dy) * surface) / 3.0);

            // speed: 5    pres: 1     Ok
            divergenceX.sumParallel(nodesl[0][e], nodesq[4][e],
                ((dl3dx + dl2dx) * surface) / 3.0, divergenceY,
                ((dl3dy + dl2dy) * surface) / 3.0);
            divergenceXCuad.sumParallel(nodesq[4][e], nodesl[0][e],
                ((dl3dx + dl2dx) * surface) / 3.0, divergenceYCuad,
                ((dl3dy + dl2dy) * surface) / 3.0);

            // speed: 5    pres: 2     Ok
            divergenceX.sumParallel(nodesl[1][e], nodesq[4][e],
                ((dl2dx + (2 * dl3dx)) * surface) / 3.0, divergenceY,
                ((dl2dy + (2 * dl3dy)) * surface) / 3.0);
            divergenceXCuad.sumParallel(nodesq[4][e], nodesl[1][e],
                ((dl2dx + (2 * dl3dx)) * surface) / 3.0, divergenceYCuad,
                ((dl2dy + (2 * dl3dy)) * surface) / 3.0);

            // speed: 5    pres: 3     Ok
            divergenceX.sumParallel(nodesl[2][e], nodesq[4][e],
                ((dl3dx + (2 * dl2dx)) * surface) / 3.0, divergenceY,
                ((dl3dy + (2 * dl2dy)) * surface) / 3.0);
            divergenceXCuad.sumParallel(nodesq[4][e], nodesl[2][e],
                ((dl3dx + (2 * dl2dx)) * surface) / 3.0, divergenceYCuad,
                ((dl3dy + (2 * dl2dy)) * surface) / 3.0);

            // speed: 6    pres: 1     Ok
            divergenceX.sumParallel(nodesl[0][e], nodesq[5][e],
                ((dl1dx + (2 * dl3dx)) * surface) / 3.0, divergenceY,
                ((dl1dy + (2 * dl3dy)) * surface) / 3.0);
            divergenceXCuad.sumParallel(nodesq[5][e], nodesl[0][e],
                ((dl1dx + (2 * dl3dx)) * surface) / 3.0, divergenceYCuad,
                ((dl1dy + (2 * dl3dy)) * surface) / 3.0);

            // speed: 6    pres: 2     Ok
            divergenceX.sumParallel(nodesl[1][e], nodesq[5][e],
                ((dl3dx + dl1dx) * surface) / 3.0, divergenceY,
                ((dl3dy + dl1dy) * surface) / 3.0);
            divergenceXCuad.sumParallel(nodesq[5][e], nodesl[1][e],
                ((dl3dx + dl1dx) * surface) / 3.0, divergenceYCuad,
                ((dl3dy + dl1dy) * surface) / 3.0);

            // speed: 6    pres: 3     Ok
            divergenceX.sumParallel(nodesl[2][e], nodesq[5][e],
                ((dl3dx + (2 * dl1dx)) * surface) / 3.0, divergenceY,
                ((dl3dy + (2 * dl1dy)) * surface) / 3.0);
            divergenceXCuad.sumParallel(nodesq[5][e], nodesl[2][e],
                ((dl3dx + (2 * dl1dx)) * surface) / 3.0, divergenceYCuad,
                ((dl3dy + (2 * dl1dy)) * surface) / 3.0);
        }
    }
}
