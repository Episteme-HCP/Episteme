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
class MassRigidity3DLineal extends Object {
/**
     * Creates new MasasRigidez3DLineal
     */
    public MassRigidity3DLineal() {
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
        double dl1dx;
        double dl1dy;
        double dl1dz;
        double dl2dx;
        double dl2dy;
        double dl2dz;
        double dl3dx;
        double dl3dy;
        double dl3dz;
        double dl4dx;
        double dl4dy;
        double dl4dz;
        double volume;
        double jacobian;
        double r11;
        double r12;
        double r13;
        double r14;
        double r22;
        double r23;
        double r24;
        double r33;
        double r34;
        double r44;

        kernel.out(
            "Generating lineal matrices <B>masses</B> and <B>rigidity</B> (3D) ");

        for (int e = 0; e < nodesl[0].length; e++) {
            // Coordinates
            x1 = coordl[0][nodesl[0][e]];
            y1 = coordl[1][nodesl[0][e]];
            z1 = coordl[2][nodesl[0][e]];
            x2 = coordl[0][nodesl[1][e]];
            y2 = coordl[1][nodesl[1][e]];
            z2 = coordl[2][nodesl[1][e]];
            x3 = coordl[0][nodesl[2][e]];
            y3 = coordl[1][nodesl[2][e]];
            z3 = coordl[2][nodesl[2][e]];
            x4 = coordl[0][nodesl[3][e]];
            y4 = coordl[1][nodesl[3][e]];
            z4 = coordl[2][nodesl[3][e]];

            // differences (matrix jacobiana)
            X21 = x2 - x1;
            X31 = x3 - x1;
            X41 = x4 - x1;
            Y21 = y2 - y1;
            Y31 = y3 - y1;
            Y41 = y4 - y1;
            Z21 = z2 - z1;
            Z31 = z3 - z1;
            Z41 = z4 - z1;

            // determinante of the matrix jacobiana
            jacobian = ((X31 * Y41 * Z21) + (X41 * Y21 * Z31) +
                (X21 * Y31 * Z41)) - (X41 * Y31 * Z21) - (X21 * Y41 * Z31) -
                (X31 * Y21 * Z41);

            // Jacobian inverse matrix
            dl2dx = ((Y31 * Z41) - (Y41 * Z31)) / jacobian;
            dl2dy = ((X41 * Z31) - (X31 * Z41)) / jacobian;
            dl2dz = ((X31 * Y41) - (X41 * Y31)) / jacobian;

            dl3dx = ((Y41 * Z21) - (Y21 * Z41)) / jacobian;
            dl3dy = ((X21 * Z41) - (X41 * Z21)) / jacobian;
            dl3dz = ((X41 * Y21) - (X21 * Y41)) / jacobian;

            dl4dx = ((Y21 * Z31) - (Y31 * Z21)) / jacobian;
            dl4dy = ((X31 * Z21) - (X21 * Z31)) / jacobian;
            dl4dz = ((X21 * Y31) - (X31 * Y21)) / jacobian;

            dl1dx = -dl2dx - dl3dx - dl4dx;
            dl1dy = -dl2dy - dl3dy - dl4dy;
            dl1dz = -dl2dz - dl3dz - dl4dz;

            // volume tetraedro
            volume = Math.abs(jacobian / 6.0);

            // s
            r11 = volume * ((dl1dx * dl1dx) + (dl1dy * dl1dy) +
                (dl1dz * dl1dz));
            r12 = volume * ((dl1dx * dl2dx) + (dl1dy * dl2dy) +
                (dl1dz * dl2dz));
            r13 = volume * ((dl1dx * dl3dx) + (dl1dy * dl3dy) +
                (dl1dz * dl3dz));
            r14 = volume * ((dl1dx * dl4dx) + (dl1dy * dl4dy) +
                (dl1dz * dl4dz));

            r22 = volume * ((dl2dx * dl2dx) + (dl2dy * dl2dy) +
                (dl2dz * dl2dz));
            r23 = volume * ((dl2dx * dl3dx) + (dl2dy * dl3dy) +
                (dl2dz * dl3dz));
            r24 = volume * ((dl2dx * dl4dx) + (dl2dy * dl4dy) +
                (dl2dz * dl4dz));

            r33 = volume * ((dl3dx * dl3dx) + (dl3dy * dl3dy) +
                (dl3dz * dl3dz));
            r34 = volume * ((dl3dx * dl4dx) + (dl3dy * dl4dy) +
                (dl3dz * dl4dz));

            r44 = volume * ((dl4dx * dl4dx) + (dl4dy * dl4dy) +
                (dl4dz * dl4dz));

            // NODE 1 -----
            rigidity.sumParallel(nodesl[0][e], nodesl[0][e], r11, masses,
                volume / 10);
            rigidity.sumParallel(nodesl[0][e], nodesl[1][e], r12, masses,
                volume / 20);
            rigidity.sumParallel(nodesl[0][e], nodesl[2][e], r13, masses,
                volume / 20);
            rigidity.sumParallel(nodesl[0][e], nodesl[3][e], r14, masses,
                volume / 20);

            // NODE 2 -----
            rigidity.sumParallel(nodesl[1][e], nodesl[0][e], r12, masses,
                volume / 20);
            rigidity.sumParallel(nodesl[1][e], nodesl[1][e], r22, masses,
                volume / 10);
            rigidity.sumParallel(nodesl[1][e], nodesl[2][e], r23, masses,
                volume / 20);
            rigidity.sumParallel(nodesl[1][e], nodesl[3][e], r24, masses,
                volume / 20);

            // NODE 3 -----
            rigidity.sumParallel(nodesl[2][e], nodesl[0][e], r13, masses,
                volume / 20);
            rigidity.sumParallel(nodesl[2][e], nodesl[1][e], r23, masses,
                volume / 20);
            rigidity.sumParallel(nodesl[2][e], nodesl[2][e], r33, masses,
                volume / 10);
            rigidity.sumParallel(nodesl[2][e], nodesl[3][e], r34, masses,
                volume / 20);

            // NODE 4 -----
            rigidity.sumParallel(nodesl[3][e], nodesl[0][e], r14, masses,
                volume / 20);
            rigidity.sumParallel(nodesl[3][e], nodesl[1][e], r24, masses,
                volume / 20);
            rigidity.sumParallel(nodesl[3][e], nodesl[2][e], r34, masses,
                volume / 20);
            rigidity.sumParallel(nodesl[3][e], nodesl[3][e], r44, masses,
                volume / 10);
        }
    }
}
