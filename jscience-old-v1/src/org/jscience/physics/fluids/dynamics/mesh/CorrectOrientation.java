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
