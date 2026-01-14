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

import org.jscience.physics.nuclear.kinematics.math.MathException;
import org.jscience.physics.nuclear.kinematics.math.Matrix;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class GJENice {
    /**
     * DOCUMENT ME!
     */
    private Matrix coefficients;

    /**
     * DOCUMENT ME!
     */
    private Matrix b;

    /**
     * DOCUMENT ME!
     */
    private Matrix inverse; // n by n

    /**
     * DOCUMENT ME!
     */
    private Matrix results; // n by m

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
     * Takes a coefficient matrix and a result matrix (A and b in A*x=b)
     * and immediately finds A[inverse] and x.  The object can then be
     * queried for these.
     */
    public GJENice(Matrix InputMatrix, Matrix InputVectors)
        throws MathException {
        this.inverse = new Matrix(InputMatrix);
        this.coefficients = new Matrix(InputMatrix);
        this.results = new Matrix(InputVectors);
        this.b = new Matrix(InputVectors);
        this.n = InputMatrix.rows;
        this.m = InputVectors.columns;

        if (n != InputVectors.rows) {
            System.err.println(n + " not equal to " + InputVectors.rows +
                "!!!");
        }

        RowIndex = new int[n];
        ColumnIndex = new int[n];
        Pivot = new int[n];
        go();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws MathException DOCUMENT ME!
     */
    private void go() throws MathException {
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
     * @throws MathException DOCUMENT ME!
     */
    private void findPivots() throws MathException {
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
                            if (Math.abs(inverse.element[j][k]) >= big) {
                                big = Math.abs(inverse.element[j][k]);
                                PivotRow = j;
                                PivotColumn = k;
                            }
                        } else if (Pivot[k] > 1) {
                            throw new MathException("GJE: Singular Matrix-1");
                        }
                    }
                }
            }

            Pivot[PivotColumn]++;

            if (PivotRow != PivotColumn) { //put pivot element on diagonal
                inverse.permute(PivotRow, PivotColumn, 'r');
                results.permute(PivotRow, PivotColumn, 'r');
            }

            RowIndex[i] = PivotRow;
            ColumnIndex[i] = PivotColumn;

            if (inverse.element[PivotColumn][PivotColumn] == 0.0) {
                throw new MathException(getClass().getName() +
                    ".findPivots(): Singular Matrix\n" +
                    coefficients.toString(3));
            }

            PivotInverse = 1.0 / inverse.element[PivotColumn][PivotColumn];
            inverse.element[PivotColumn][PivotColumn] = 1.0;
            inverse.rowMultiply(PivotColumn, PivotInverse);
            results.rowMultiply(PivotColumn, PivotInverse);

            for (ll = 0; ll < n; ll++) {
                if (ll != PivotColumn) {
                    dummy = inverse.element[ll][PivotColumn];
                    inverse.element[ll][PivotColumn] = 0.0;

                    for (l = 0; l < n; l++) {
                        inverse.element[ll][l] = inverse.element[ll][l] -
                            (inverse.element[PivotColumn][l] * dummy);
                    }

                    for (l = 0; l < m; l++) {
                        results.element[ll][l] = results.element[ll][l] -
                            (results.element[PivotColumn][l] * dummy);
                    }
                }
            }
        }

        //Now unscramble the permuted columns.
        for (l = n - 1; l >= 0; l--) {
            if (RowIndex[l] != ColumnIndex[l]) {
                inverse.permute(RowIndex[l], ColumnIndex[l], 'r');
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getInverse() {
        return inverse;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getSolution() {
        return results;
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

        try {
            GJENice gje = new GJENice(a, b);
            System.out.println(gje.getInverse().toString(2));
            System.out.println(gje.getSolution().toString(2));
        } catch (MathException e) {
            System.err.println(e);
        }
    }
}
