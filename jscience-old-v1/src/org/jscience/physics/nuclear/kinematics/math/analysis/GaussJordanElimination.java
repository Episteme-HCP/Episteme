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

package org.jscience.physics.nuclear.kinematics.math.analysis;

import org.jscience.physics.nuclear.kinematics.math.Matrix;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
class GaussJordanElimination {
    /**
     * DOCUMENT ME!
     */
    private Matrix InputMatrix; // n by n

    /**
     * DOCUMENT ME!
     */
    private Matrix InputVectors; // n by m

    /**
     * DOCUMENT ME!
     */
    private int[] RowIndex;

    /**
     * DOCUMENT ME!
     */
    private int[] ColumnIndex;

    /**
     * DOCUMENT ME!
     */
    private int[] Pivot;

    /**
     * DOCUMENT ME!
     */
    private int n;

    /**
     * DOCUMENT ME!
     */
    private int m;

    /**
     * DOCUMENT ME!
     */
    private int PivotRow;

    /**
     * DOCUMENT ME!
     */
    private int PivotColumn;

    /**
     * Creates a new GaussJordanElimination object.
     *
     * @param InputMatrix DOCUMENT ME!
     * @param InputVector DOCUMENT ME!
     */
    GaussJordanElimination(Matrix InputMatrix, Matrix InputVector) {
        this.InputMatrix = new Matrix(InputMatrix);
        this.InputVectors = new Matrix(InputVector);
        this.n = InputMatrix.rows;
        this.m = InputVectors.columns;

        if (n != InputVectors.rows) {
            System.err.println(n + " not equal to " + InputVectors.rows +
                "!!!");
        }

        RowIndex = new int[n];
        ColumnIndex = new int[n];
        Pivot = new int[n];
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void go() throws Exception {
        clearPivot();
        findPivots();
    }

    /**
     * DOCUMENT ME!
     */
    private void clearPivot() {
        int i;

        for (i = 0; i < n; i++) {
            Pivot[i] = 0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    private void findPivots() throws Exception {
        int k;
        int j;
        int ll;
        int l;
        int i;
        double big;
        double PivotInverse;
        double dummy;

        for (i = 0; i < n; i++) {
            big = 0.0;

            for (j = 0; j < n; j++) {
                if (Pivot[j] != 1) {
                    for (k = 0; k < n; k++) {
                        if (Pivot[k] == 0) {
                            if (Math.abs(InputMatrix.element[j][k]) >= big) {
                                big = Math.abs(InputMatrix.element[j][k]);
                                PivotRow = j;
                                PivotColumn = k;
                            }
                        } else if (Pivot[k] > 1) {
                            throw new Exception("GJE: Singular Matrix-1");
                        }
                    }
                }
            }

            Pivot[PivotColumn]++;

            if (PivotRow != PivotColumn) { //put pivot element on diagonal
                InputMatrix.permute(PivotRow, PivotColumn, 'r');
                InputVectors.permute(PivotRow, PivotColumn, 'r');
            }

            RowIndex[i] = PivotRow;
            ColumnIndex[i] = PivotColumn;

            if (InputMatrix.element[PivotColumn][PivotColumn] == 0.0) {
                throw new Exception("GJE: Singular Matrix-2");
            }

            PivotInverse = 1.0 / InputMatrix.element[PivotColumn][PivotColumn];
            InputMatrix.element[PivotColumn][PivotColumn] = 1.0;
            InputMatrix.rowMultiply(PivotColumn, PivotInverse);
            InputVectors.rowMultiply(PivotColumn, PivotInverse);

            for (ll = 0; ll < n; ll++) {
                if (ll != PivotColumn) {
                    dummy = InputMatrix.element[ll][PivotColumn];
                    InputMatrix.element[ll][PivotColumn] = 0.0;

                    for (l = 0; l < n; l++) {
                        InputMatrix.element[ll][l] = InputMatrix.element[ll][l] -
                            (InputMatrix.element[PivotColumn][l] * dummy);
                    }

                    for (l = 0; l < m; l++) {
                        InputVectors.element[ll][l] = InputVectors.element[ll][l] -
                            (InputVectors.element[PivotColumn][l] * dummy);
                    }
                }
            }
        }

        //Now unscramble the permuted columns.
        for (l = n - 1; l >= 0; l--) {
            if (RowIndex[l] != ColumnIndex[l]) {
                InputMatrix.permute(RowIndex[l], ColumnIndex[l], 'r');
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getMatrix() {
        return InputMatrix;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getVectors() {
        return InputVectors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param argv DOCUMENT ME!
     */
    public static void main(String[] argv) {
        Matrix a = new Matrix(2, 2);
        Matrix b = new Matrix(2, 1);
        a.element[0][0] = 1.0;
        a.element[0][1] = 2.0;
        a.element[1][0] = 1.0;
        a.element[1][1] = 4.0;
        b.element[0][0] = 5.0;
        b.element[1][0] = 8.0;
        System.out.println(a.toString(2));
        System.out.println(b.toString(2));

        GaussJordanElimination gje = new GaussJordanElimination(a, b);

        try {
            gje.go();
        } catch (Exception e) {
            System.err.println(e);
        }

        System.out.println(gje.getMatrix().toString(2));
        System.out.println(gje.getVectors().toString(2));
    }
}
