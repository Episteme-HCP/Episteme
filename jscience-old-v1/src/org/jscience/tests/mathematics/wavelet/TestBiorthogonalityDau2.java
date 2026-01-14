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

import org.jscience.mathematics.wavelet.DiscreteHilbertSpace;
import org.jscience.mathematics.wavelet.MultiscaleFunction;
import org.jscience.mathematics.wavelet.daubechies2.Scaling2;
import org.jscience.mathematics.wavelet.daubechies2.Wavelet2;


/**
 * Test the biorthogonality
 *
 * @author Daniel Lemire
 */
public class TestBiorthogonalityDau2 {
    /**
     * Creates a new TestBiorthogonalityDau2 object.
     *
     * @param n0 DOCUMENT ME!
     * @param j0 DOCUMENT ME!
     */
    public TestBiorthogonalityDau2(int n0, int j0) {
        double[][] mat = new double[(2 * n0) - 2][(2 * n0) - 2];

        for (int k = 0; k < n0; k++) {
            System.out.println("k=" + k + " /" + ((2 * n0) - 2));

            for (int l = 0; l < n0; l++) {
                mat[k][l] = EcheEche(k, l, n0, j0);
            }

            for (int l = n0; l < ((2 * n0) - 2); l++) {
                mat[k][l] = EcheOnde(k, l - n0, n0, j0);
            }
        }

        for (int k = n0; k < ((2 * n0) - 2); k++) {
            System.out.println("k=" + k + " /" + ((2 * n0) - 2));

            for (int l = 0; l < (n0 - 2); l++) {
                mat[k][l] = OndeEche(k - n0, l, n0, j0);
            }

            for (int l = n0; l < ((2 * n0) - 2); l++) {
                mat[k][l] = OndeOnde(k - n0, l - n0, n0, j0);
            }
        }

        double[][] Id = new double[2 * n0][2 * n0];

        for (int k = 0; k < ((2 * n0) - 2); k++) {
            for (int l = 0; l < ((2 * n0) - 2); l++) {
                if (k == l) {
                    Id[k][l] = 1;
                }
            }
        }

        double max = 0.0;

        for (int k = 0; k < ((2 * n0) - 2); k++) {
            for (int l = 0; l < ((2 * n0) - 2); l++) {
                if (Math.abs(Id[k][l] - mat[k][l]) > max) {
                    max = Math.abs(Id[k][l] - mat[k][l]);
                }
            }
        }

        ImprimeMatrice(mat, n0);
        System.out.println("Max = " + max);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        int n0 = 8;
        int j0 = 0;
        new TestBiorthogonalityDau2(n0, j0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mat DOCUMENT ME!
     * @param n0 DOCUMENT ME!
     */
    public void ImprimeMatrice(double[][] mat, int n0) {
        for (int k = 0; k < ((2 * n0) - 2); k++) {
            for (int l = 0; l < ((2 * n0) - 2); l++) {
                if (Math.abs(mat[k][l]) > 0.000001) {
                    System.out.println(" mat [ " + k + " , " + l + " ] = " +
                        mat[k][l]);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param k DOCUMENT ME!
     * @param l DOCUMENT ME!
     * @param n0 DOCUMENT ME!
     * @param j0 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double EcheEche(int k, int l, int n0, int j0) {
        MultiscaleFunction Primaire = new Scaling2(n0, k);
        MultiscaleFunction Duale = new Scaling2(n0, l);

        return (DiscreteHilbertSpace.integrate(Primaire, Duale, j0));
    }

    /**
     * DOCUMENT ME!
     *
     * @param k DOCUMENT ME!
     * @param l DOCUMENT ME!
     * @param n0 DOCUMENT ME!
     * @param j0 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double EcheOnde(int k, int l, int n0, int j0) {
        MultiscaleFunction Primaire = new Scaling2(n0, k);
        MultiscaleFunction Duale = new Wavelet2(n0, l);

        return (DiscreteHilbertSpace.integrate(Primaire, Duale, j0));
    }

    /**
     * DOCUMENT ME!
     *
     * @param k DOCUMENT ME!
     * @param l DOCUMENT ME!
     * @param n0 DOCUMENT ME!
     * @param j0 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double OndeEche(int k, int l, int n0, int j0) {
        MultiscaleFunction Primaire = new Wavelet2(n0, k);
        MultiscaleFunction Duale = new Scaling2(n0, l);

        return (DiscreteHilbertSpace.integrate(Primaire, Duale, j0));
    }

    /**
     * DOCUMENT ME!
     *
     * @param k DOCUMENT ME!
     * @param l DOCUMENT ME!
     * @param n0 DOCUMENT ME!
     * @param j0 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double OndeOnde(int k, int l, int n0, int j0) {
        MultiscaleFunction Primaire = new Wavelet2(n0, k);
        MultiscaleFunction Duale = new Wavelet2(n0, l);

        return (DiscreteHilbertSpace.integrate(Primaire, Duale, j0));
    }
}
