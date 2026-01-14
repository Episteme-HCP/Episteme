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

package org.jscience.physics.fluids.dynamics.util;

import java.io.FileWriter;
import java.io.PrintWriter;


/**
 * Sparse Matrix, simetric or not. This class is in charge of the data and
 * methods related to matrix operations.
 * <p/>
 * <p/>
 * The <I>disperse storage</I> is a technique that allows storing large
 * matrices in memory by using much less RAM. The method consists in working
 * only with the non null elements: the matrices used in finite elements,
 * finite differences, and other numerical methods have many elements as
 * nulls.
 * </p>
 * <p/>
 * <p/>
 * The <CODE>Matrix</CODE> is stored in three vectors: <CODE>elem</CODE>,
 * <CODE>ipos</CODE> and <CODE>jpos</CODE>. They are public to allow other
 * classes to implement optimized algorithms.
 * </p>
 * <p/>
 * <p/>
 * For more information about <I>sparse matrices</I> consult the program
 * manual.
 * </p>
 * <p/>
 * <p/>
 * Revised for <B>ADFC v2.0</B>
 * </p>
 * <p/>
 * <P></p>
 *
 * @author Alejandro "Balrog" Rodriguez Gallego
 * @version 2.0
 */
public class Matrix {
    //WARNING: when adding new fields, it must be added to the copy constructor too.

    /**
     * Contains the value of the elements not null. Therefore,
     * <code>elem[n]</code> is the value of element not null n-esimo.
     */
    public double[] elem;

    /**
     * <code>elem[n]</code> indicates in which column it is found the element
     * not null n-esimo.
     */
    public int[] jpos;

    /**
     * <code>ipos[n]</code> indicates in which position of <code>elem</code>
     * and <code>jpos</code> starts the row n-esima of the matrix.
     */
    public int[] ipos;

    /**
     * Indicates if the matrix is stored using its simetry. If so, it
     * corresponds with the lower triangule.
     */
    public boolean simetric;

    /**
     * constructor. If <code>as</code> is <code>null</code>, means that the
     * constructor should reserve the memory for <code>as</code> (so in this
     * case, a new empty matrix).
     *
     * @param as    elements in compact format.
     * @param aipos vector Ipos (NVPN)
     * @param ajpos vector Jpos (NNVI)
     * @param sim   simetric or not
     */
    public Matrix(double[] as, int[] aipos, int[] ajpos, boolean sim) {
        if (as == null) {
            int dimension = aipos[aipos.length - 1];

            // System.out.println("Assigning automatic size = "+dimension);
            elem = new double[dimension];
        } else {
            elem = as;
        }

        ipos = aipos;
        jpos = ajpos;
        simetric = sim;
    }

    /**
     * copy constructor. Constructs a new object duplicating the contents of
     * the original matrix. If especifica <code>swallow</code>, the copy of
     * <code>ipos</code> and <code>jpos</code> is not by value, but by
     * reference. This way, we can have two identical matrices without
     * duplicating RAM in those vectors. <code>Elem</code> is always
     * duplicated, to allow modifications without lateral effects.
     *
     * @param matrix  matrix to copy.
     * @param swallow copy swallow (no duplication of RAM for the structure
     *                ipos/jpos)
     */
    public Matrix(Matrix matrix, boolean swallow) {
        elem = new double[matrix.elem.length];

        for (int i = 0; i < elem.length; i++)
            elem[i] = matrix.elem[i];

        if (swallow) {
            ipos = matrix.ipos;
            jpos = matrix.jpos;
        } else {
            ipos = new int[matrix.ipos.length];

            for (int i = 0; i < ipos.length; i++)
                ipos[i] = matrix.ipos[i];

            jpos = new int[matrix.jpos.length];

            for (int i = 0; i < jpos.length; i++)
                jpos[i] = matrix.jpos[i];
        }

        simetric = matrix.simetric;
    }

    /**
     * multiply all the elements by a real number.
     *
     * @param factor real number.
     */
    public void applyFactor(double factor) {
        for (int j = 0; j < elem.length; j++)
            elem[j] *= factor;
    }

    /**
     * assigns the value of a element.
     *
     * @param i     row
     * @param j     column
     * @param value new value
     */
    public void assignElement(int i, int j, double value) {
        if (simetric && (i < j)) {
            return; // we are interested only in the lower triangle
        }

        for (int n = ipos[i]; n < ipos[i + 1]; n++)
            if (jpos[n] == j) {
                elem[n] = value;

                return;
            }

        //System.out.println("M.aE: Access outside of the structure in ["+i+","+j+"]");
    }

    /**
     * checks that the matrix of rigidity is more or less correct in structure.
     */
    public void checkRigidityMatrix() {
        double[] sums = new double[ipos.length - 1];

        for (int i = 0; i < (ipos.length - 1); i++) {
            // traverse the row (variamos column)
            for (int l = ipos[i]; l < ipos[i + 1]; l++) {
                sums[i] += elem[l];

                if (jpos[l] != i) {
                    sums[jpos[l]] += elem[l];
                }
            }
        }

        for (int i = 0; i < sums.length; i++)
            System.out.println("Coef[" + i + "] = " + sums[i]);
    }

    // debug

    /**
     * show matrix in the console.
     */
    public void printMatrix() {
        for (int j = 0; j < (ipos.length - 1); j++) {
            System.out.println("ROW " + j);

            for (int k = ipos[j]; k < ipos[j + 1]; k++)
                System.out.println("   [" + jpos[k] + "]=" + elem[k]);
        }
    }

    /**
     * returns the number of rows.
     *
     * @return number of rows.
     */
    public int getRows() {
        return ipos.length - 1;
    }

    /**
     * returns the value of a element.
     *
     * @param i row
     * @param j column
     * @return value del element
     */
    public double readElement(int i, int j) {
        if (simetric && (i < j)) {
            int t = i;
            i = j;
            j = t;
        } // we only work with the lower triangle.

        // we search along the row i of the element in the column j
        for (int n = ipos[i]; n < ipos[i + 1]; n++)
            if (jpos[n] == j) {
                return (elem[n]);
            }

        return 0.0;
    }

    /**
     * multiply the matrix by a vector.
     *
     * @param result where to store the solution. It can be null.
     * @param vector vector to multiply.
     * @return vector result.
     */
    public double[] multiply(double[] result, double[] vector) {
        return multiply(result, vector, vector.length);
    }

    /**
     * multiply this matrix by a vector.
     *
     * @param vector vector to multiply.
     * @return vector result.
     */
    public double[] multipliy(double[] vector) {
        return multiply(null, vector, vector.length);
    }

    /**
     * multiply simetric/asimetric matrix by vector. Updated to a faster
     * version, which demands that the first element of a row is the diagonal.
     *
     * @param vector  column.
     * @param rowSize size of the row (and of the solution too)
     * @return vector column solution.
     */
    public double[] multiply(double[] vector, int rowSize) {
        return multiply(null, vector, rowSize);
    }

    /**
     * multiply simetric/asimetric matrix by vector. Updated to a faster
     * version, which demands that the first element of a row is the diagonal.
     *
     * @param result  where to store the result.
     * @param vector  column.
     * @param rowSize size of the row (and of the solution too)
     * @return vector column solution.
     */
    public double[] multiply(double[] result, double[] vector, int rowSize) {
        if (result == null) {
            result = new double[rowSize];
        }

        if (simetric) {
            // if the matrix is simetric, we can make the multiply
            // more effectively.
            for (int i = 0; i < result.length; i++)
                result[i] = 0.0;

            // traverse rows
            int j;

            for (int i = 0; i < result.length; i++) {
                // The diagonal implies only 1 operation.
                result[i] += (elem[ipos[i]] * vector[i]);

                // traverse the row, double products
                for (int l = ipos[i] + 1; l < ipos[i + 1]; l++) {
                    j = jpos[l];

                    result[i] += (elem[l] * vector[j]);
                    result[j] += (elem[l] * vector[i]);
                }
            }
        } else // asimetric
        {
            for (int i = 0; i < result.length; i++) {
                result[i] = 0.0;

                for (int k = ipos[i]; k < ipos[i + 1]; k++)
                    result[i] += (elem[k] * vector[jpos[k]]);
            }
        }

        return result;
    }

    /**
     * this function carries out the product a(Mb)
     *
     * @param a vector
     * @param b vector
     * @return scalar product of vectors a and b.
     */
    public double scalarProductOfMatrixProduct(double[] a, double[] b) {
        double result = 0.0;

        if (simetric) {
            // if the matrix is simetric, we can make the multiply
            // more effectively.
            // traverse rows
            int j;

            for (int i = 0; i < a.length; i++) {
                // The diagonal implies only 1 operation.
                result += (a[i] * elem[ipos[i]] * b[i]);

                // traverse the row, double products
                for (int l = ipos[i] + 1; l < ipos[i + 1]; l++) {
                    j = jpos[l];

                    result += (a[i] * elem[l] * b[j]);
                    result += (a[j] * elem[l] * b[i]);
                }
            }
        } else // asimetric
        {
            for (int i = 0; i < a.length; i++) {
                for (int k = ipos[i]; k < ipos[i + 1]; k++)
                    result += (a[i] * elem[k] * b[jpos[k]]);
            }
        }

        return result;
    }

    /**
     * sums the specified  value in the position (i, j).
     *
     * @param i     row of the position to be summed.
     * @param j     column of the position to be summed.
     * @param value value to be summed in the specified position.
     */
    public void sumElement(int i, int j, double value) {
        if (simetric && (i < j)) {
            return; // we only work with the lower triangle.
        }

        for (int n = ipos[i]; n < ipos[i + 1]; n++)
            if (jpos[n] == j) {
                elem[n] += value;

                return;
            }

        System.err.println("M.sE: Access out of the structure in [" + i + "," +
                j + "]");
        new Exception().printStackTrace();
        System.exit(-1);
    }

    /**
     * sums in the position (i, j) the given value, and at the same position of
     * the sister matrix, sum value2. This method es useful in cases of dos
     * consecutive sums in identical positions and with matrices of equal
     * estructure. For example: the mass matrix  and  the matrix of rigidity,
     * the matrices of divergence X and Y... <b>For efficiency, it is not
     * verified that the structure is identical.</b> That has to be done by
     * the programmer.
     *
     * @param i      row of the position to be summed.
     * @param j      column of the position to be summed.
     * @param value  value  to be summed in the given position of
     *               <CODE>this</CODE>.
     * @param sister the other matrix in which we are summing in parallel.
     * @param value2 value to be summed in the given position of
     *               <CODE>sister</CODE>.
     */
    public void sumParallel(int i, int j, double value, Matrix sister,
                            double value2) {
        if (simetric && (i < j)) {
            return; // we only work with the lower triangle.
        }

        for (int n = ipos[i]; n < ipos[i + 1]; n++)
            if (jpos[n] == j) {
                elem[n] += value;
                sister.elem[n] += value2;

                return;
            }

        System.err.println("M.sP: Access out of the structure in [" + i + "," +
                j + "]");
        new Exception().printStackTrace();
        System.exit(-1);
    }

    /**
     * sums element by element the content of other matrix, <b>which is
     * supposed with identical Ipos y Jpos</b>.
     *
     * @param m      matrix to be summed to <code>this</code>
     * @param factor factor to be applied to the term. For example, -1
     *               differentiates.
     */
    public void sumMatrix(Matrix m, double factor) {
        if (simetric != m.simetric) {
            System.err.println("Matrix: sumMatrix simetric !=");
            System.exit(0);
        }

        for (int j = 0; j < elem.length; j++)
            elem[j] += (m.elem[j] * factor);
    }

    /**
     * Saves the content of the matrix to a file. Mostly for debugging.
     *
     * @param name  path to the file in which to save the matrix.
     * @param title string as title in the first line of the file
     */
    public void saveToFile(String name, String title) {
        PrintWriter p;

        try {
            p = new PrintWriter(new FileWriter(name));

            p.println(title);
            p.println("----------------------");

            for (int i = 0; i < (ipos.length - 1); i++) {
                p.println("FILA \t" + (i + 1) + "\t------------");

                // print NNVI
                for (int j = ipos[i]; j < ipos[i + 1]; j++)
                    p.print("\t" + (jpos[j] + 1));

                p.println("");

                // print ELEM
                for (int j = ipos[i]; j < ipos[i + 1]; j++)
                    p.print("\t" + elem[j]);

                p.println("");
            }

            p.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
