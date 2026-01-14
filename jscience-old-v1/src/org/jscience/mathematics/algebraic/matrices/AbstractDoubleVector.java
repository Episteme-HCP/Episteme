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

package org.jscience.mathematics.algebraic.matrices;

import org.jscience.JScience;
import org.jscience.mathematics.MathUtils;
import org.jscience.mathematics.algebraic.AbstractVector;
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.algebras.BanachSpace;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.util.IllegalDimensionException;

import java.io.BufferedReader;
import java.io.StreamTokenizer;

/**
 * The AbstractDoubleVector class encapsulates vectors containing doubles.
 *
 * @author Mark Hale
 * @version 1.0
 */

//for performance reason we never check if arguments are different from null.

//hashcode, norm, norm, normalize, infnorm, trace, hermitianAdjoint, operatorNorm need vector dimension checking

//Jama provides some more methods from which we could get some inspiration: see AbstractDoubleMatrix

public abstract class AbstractDoubleVector extends AbstractVector implements BanachSpace.Member {
    protected AbstractDoubleVector(final int dim) {
        super(dim);
    }

    /**
     * Returns a comma delimited string representing the value of this vector.
     */
    public String toString() {
        final StringBuffer buf = new StringBuffer(8 * getDimension());
        int i;
        for (i = 0; i < getDimension() - 1; i++) {
            buf.append(getPrimitiveElement(i));
            buf.append(',');
        }
        buf.append(getPrimitiveElement(i));
        return buf.toString();
    }

    /**
     * Returns a hashcode for this NON EMPTY vector.
     */
    public int hashCode() {
        return (int) Math.exp(norm());
    }

    /**
     * Converts this vector to a integer vector.
     *
     * @return a integer vector
     */
    public AbstractIntegerVector toIntegerVector() {
        final int[] array = new int[getDimension()];

        for (int i = 0; i < getDimension(); i++)
            array[i] = Math.round((float) getPrimitiveElement(i));

        return new IntegerVector(array);
    }

    /**
     * Converts this vector to a complex vector.
     *
     * @return a complex vector
     */
    public AbstractComplexVector toComplexVector() {
        final double[] arrayRe = new double[getDimension()];

        for (int i = 0; i < getDimension(); i++)
            arrayRe[i] = getPrimitiveElement(i);

        return new ComplexVector(arrayRe, new double[getDimension()]);
    }

    /**
     * Returns an element of this vector (this is the fastest way of getting an element for this kind of matrix).
     *
     * @param n index of the vector element.
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public abstract double getPrimitiveElement(int n);

    /**
     * Returns an element of this vector.
     *
     * @param n index of the vector element.
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public Double getElement(final int n) {
        return new Double(getPrimitiveElement(n));
    }

    /**
     * Returns the ith row.
     */
    public AbstractDoubleVector getRow(final int i) {

        if ((i >= 0) && (i < numRows())) {
            final double[] array = new double[1];
            array[0] = getPrimitiveElement(i);
            return new DoubleVector(array);
        } else
            throw new IllegalDimensionException("Requested element out of bounds.");

    }

    /**
     * Returns the ith column.
     */
    public AbstractDoubleVector getColumn(final int j) {

        if (j == 0) {
            final double[] array = new double[1];
            for (int i = 0; i < getDimension(); i++) {
                array[i] = getPrimitiveElement(i);
            }
            return new DoubleVector(array);
        } else
            throw new IllegalDimensionException("Requested element out of bounds.");

    }

    /**
     * Returns the ith row.
     */
    public void setRow(final int i, final AbstractDoubleVector v) {

        if ((i >= 0) && (i < numRows())) {
            if (v.getDimension() == 1) {
                setElement(i, v.getPrimitiveElement(0));
            } else
                throw new IllegalDimensionException("The vector has not the required dimension.");
        } else
            throw new IllegalDimensionException("Element out of bounds.");

    }

    /**
     * Returns the ith column.
     */
    public void setColumn(final int j, final AbstractDoubleVector v) {

        if (j == 0) {
            if (v.getDimension() == getDimension()) {
                for (int i = 0; i < getDimension(); i++) {
                    setElement(i, v.getPrimitiveElement(i));
                }
            } else
                throw new IllegalDimensionException("The vector has not the required dimension.");
        } else
            throw new IllegalDimensionException("Element out of bounds.");

    }

    /**
     * Sets the value of an element of this vector.
     *
     * @param n index of the vector element.
     * @param x a number.
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public abstract void setElement(final int n, final double x);

    /**
     * Sets the value of a component of this vector.
     *
     * @param n index of the vector component
     * @param x an double
     * @throws IllegalDimensionException If attempting to access an invalid
     *                                   component.
     */
    public void setElement(final int n, final Double x) {
        setElement(n, x.doubleValue());
    }

    /**
     * Sets the value of all elements of the vector.
     *
     * @param r a ring element
     */
    public void setAllElements(final double r) {
        for (int i = 0; i < getDimension(); i++) {
            setElement(i, r);
        }
    }

    /**
     * Compares two double vectors for equality.
     * Two vectors are considered to be equal if the norm of their difference is within the tolerance.
     *
     * @param obj a double vector
     */
    public boolean equals(Object obj) {
        return equals(obj, java.lang.Double.valueOf(JScience.getProperty("tolerance")).doubleValue());
    }

    public boolean equals(Object obj, double tol) {
        if (obj != null && (obj instanceof AbstractDoubleVector)) {
            final AbstractDoubleVector vec = (AbstractDoubleVector) obj;
            return (this.getDimension() == vec.getDimension() && this.subtract(vec).norm() <= tol);
        } else
            return false;
    }

    /**
     * Returns the sum of the squares of the components.
     *
     * @return DOCUMENT ME!
     */
    public double sumSquares() {
        if (getDimension() > 0) {
            double sum = 0;
            for (int k = 0; k < getDimension(); k++)
                sum += (getPrimitiveElement(k) * getPrimitiveElement(k));
            return sum;
        } else
            throw new ArithmeticException("The sum of squares of a zero dimension vector is undefined.");
    }

    /**
     * Returns the mass.
     *
     * @return DOCUMENT ME!
     */
    public double mass() {
        if (getDimension() > 0) {
            double mass = 0;
            for (int k = 0; k < getDimension(); k++)
                mass += getPrimitiveElement(k);
            return mass;
        } else
            throw new ArithmeticException("The mass of a zero dimension vector is undefined.");
    }

    /**
     * Returns the l<sup>n</sup>-norm.
     *
     * @planetmath VectorPnorm
     */
    public double norm(final int n) {
        if (getDimension() > 0) {
            double answer = Math.pow(Math.abs(getPrimitiveElement(0)), n);
            for (int i = 1; i < getDimension(); i++)
                answer += Math.pow(Math.abs(getPrimitiveElement(i)), n);
            return Math.pow(answer, 1.0 / n);
        } else
            throw new ArithmeticException("The norm of a zero dimension vector is undefined.");
    }

    /**
     * Returns the l<sup>2</sup>-norm (magnitude).
     *
     * @planetmath VectorPnorm
     */
    public double norm() {
        if (getDimension() > 0) {
            double answer = getPrimitiveElement(0);
            for (int i = 1; i < getDimension(); i++)
                answer = MathUtils.hypot(answer, getPrimitiveElement(i));
            return answer;
        } else
            throw new ArithmeticException("The norm of a zero dimension vector is undefined.");
    }

    /**
     * Makes the norm of this vector equal to one.
     */
    public void normalize() {
        if (getDimension() > 0) {
            final double norm = norm();
            for (int i = 0; i < getDimension(); i++)
                setElement(i, getPrimitiveElement(i) / norm);
        }
    }

    /**
     * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
     *
     * @author Taber Smith
     * @planetmath VectorPnorm
     */
    public double infNorm() {
        if (getDimension() > 0) {
            double infNorm = Math.abs(getPrimitiveElement(0));
            for (int i = 1; i < getDimension(); i++) {
                final double abs = Math.abs(getPrimitiveElement(i));
                if (abs > infNorm)
                    infNorm = abs;
            }
            return infNorm;
        } else
            throw new ArithmeticException("The inf norm of a zero dimension vector is undefined.");
    }

    /**
     * Applies the abs function on all the vector components.
     */
    public AbstractDoubleVector abs() {
        final double[] array = new double[getDimension()];
        for (int i = 0; i < getDimension(); i++)
            array[i] = abs(getPrimitiveElement(i));
        return new DoubleVector(array);
    }

    //probably faster than the guenuine Math.abs() call
    private static double abs(double a) {
        if (a < 0)
            return -a;
        else
            return a;
    }

    /**
     * Gets the min of the vector components.
     *
     * @return the min.
     */
    public double min() {
        if (getDimension() > 0) {
            double result;
            result = getPrimitiveElement(0);
            for (int i = 1; i < getDimension(); i++)
                if (getPrimitiveElement(i) < result) {
                    result = getPrimitiveElement(i);
                }
            return result;
        } else
            throw new ArithmeticException("The minimum of a zero dimension vector is undefined.");
    }

    /**
     * Gets the max of the vector components.
     *
     * @return the max.
     */
    public double max() {
        double result;
        if (getDimension() > 0) {
            result = getPrimitiveElement(0);
            for (int i = 1; i < getDimension(); i++)
                if (getPrimitiveElement(i) > result) {
                    result = getPrimitiveElement(i);
                }
            return result;
        } else
            throw new ArithmeticException("The maximum of a zero dimension vector is undefined.");
    }

    /**
     * Gets the mean of the vector components.
     *
     * @return the mean.
     */
    public double mean() {
        double result;
        if (getDimension() > 0) {
            result = getPrimitiveElement(0);
            for (int i = 1; i < getDimension(); i++)
                if (getPrimitiveElement(i) > result) {
                    result += getPrimitiveElement(i);
                }
            return result / getDimension();
        } else
            throw new ArithmeticException("The mean of a zero dimension vector is undefined.");
    }

    /**
     * Computes the (bias-corrected sample) variance.
     *
     * @return the variance.
     */
    public double variance() {
        if (getDimension() > 0) {
            final double m = mean();
            double ans = 0.0;
            for (int i = 0; i < getDimension(); i++)
                ans += (getPrimitiveElement(i) - m) * (getPrimitiveElement(i) - m);
            return ans / (getDimension() - 1);
        } else
            throw new ArithmeticException("The variance of a zero or one dimension vector is undefined.");
    }

    /**
     * Computes the (bias-corrected sample) standard deviation .
     *
     * @return the standard deviation.
     */
    public double standardDeviation() {
        return (Math.sqrt(variance()));
    }

    public Matrix transpose() {

        double[][] array = new double[getDimension()][1];
        for (int i = 0; i < getDimension(); i++) {
            array[i][0] = getPrimitiveElement(i);
        }
        return new DoubleMatrix(array);

    }

    public Ring.Member multiply(final Ring.Member r) {
        if (r instanceof Matrix) {
            if (r instanceof IntegerMatrix) {
                IntegerMatrix other = (IntegerMatrix) r;
                if (other.numRows() == 1) {
                    final double[][] array = new double[numRows()][other.numColumns()];

                    for (int j = 0; j < numRows(); j++) {
                        for (int k = 0; k < other.numColumns(); k++) {
                            array[j][k] = getPrimitiveElement(j) * other.getPrimitiveElement(0, k);
                        }
                    }
                    return new DoubleMatrix(array);
                } else
                    throw new IllegalDimensionException("Cannot multiply a Vector by a Matrix with more than one row.");
            } else if (r instanceof DoubleMatrix) {
                DoubleMatrix other = (DoubleMatrix) r;
                if (other.numRows() == 1) {
                    final double[][] array = new double[numRows()][other.numColumns()];

                    for (int j = 0; j < numRows(); j++) {
                        for (int k = 0; k < other.numColumns(); k++) {
                            array[j][k] = getPrimitiveElement(j) * other.getPrimitiveElement(0, k);
                        }
                    }
                    return new DoubleMatrix(array);
                } else
                    throw new IllegalDimensionException("Cannot multiply a Vector by a Matrix with more than one row.");
            } else if (r instanceof ComplexMatrix) {
                ComplexMatrix other = (ComplexMatrix) r;
                if (other.numRows() == 1) {
                    final Complex[][] array = new Complex[numRows()][other.numColumns()];

                    for (int j = 0; j < numRows(); j++) {
                        for (int k = 0; k < other.numColumns(); k++) {
                            array[j][k] = other.getPrimitiveElement(0, k).multiply(getPrimitiveElement(j));
                        }
                    }
                    return new ComplexMatrix(array);
                } else
                    throw new IllegalDimensionException("Cannot multiply a Vector by a Matrix with more than one row.");
            } else if (r instanceof RingMatrix) {
                RingMatrix other = (RingMatrix) r;
                if (other.numRows() == 1) {
                    final Ring.Member[][] array = new Ring.Member[numRows()][other.numColumns()];

                    for (int j = 0; j < numRows(); j++) {
                        for (int k = 0; k < other.numColumns(); k++) {
                            array[j][k] = other.getPrimitiveElement(0, k).multiply(getElement(j));
                        }
                    }
                    return new RingMatrix(array);
                } else
                    throw new IllegalDimensionException("Cannot multiply a Vector by a Matrix with more than one row.");
            } else
                throw new IllegalDimensionException("Member not recognized by this method.");
        } else
            throw new IllegalArgumentException("Member not recognized by this method.");
    }

    //============
// OPERATIONS
//============

    /**
     * Returns the addition of this vector and another.
     *
     * @param v a double vector.
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public AbstractDoubleVector add(final AbstractDoubleVector v) {
        if (getDimension() == v.getDimension()) {
            final DoubleVector result = new DoubleVector(getDimension());
            for (int i = 0; i < getDimension(); i++)
                result.setElement(i, getPrimitiveElement(i) + v.getPrimitiveElement(i));
            return result;
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param v a double vector.
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public AbstractDoubleVector subtract(final AbstractDoubleVector v) {
        if (getDimension() == v.getDimension()) {
            final DoubleVector result = new DoubleVector(getDimension());
            for (int i = 0; i < getDimension(); i++)
                result.setElement(i, getPrimitiveElement(i) - v.getPrimitiveElement(i));
            return result;
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    /**
     * Returns the multiplication of this vector by a scalar.
     *
     * @param x a double.
     */
    public AbstractDoubleVector scalarMultiply(final double x) {
        final DoubleVector result = new DoubleVector(getDimension());
        for (int i = 0; i < getDimension(); i++)
            result.setElement(i, getPrimitiveElement(i) * x);
        return result;
    }

    /**
     * Returns the division of this vector by a scalar.
     *
     * @param x a double.
     * @throws ArithmeticException If divide by zero.
     */
    public AbstractDoubleVector scalarDivide(final double x) {
        final DoubleVector result = new DoubleVector(getDimension());
        for (int i = 0; i < getDimension(); i++)
            result.setElement(i, getPrimitiveElement(i) / x);
        return result;
    }

    /**
     * Returns the scalar product of this vector and another.
     *
     * @param v a double vector.
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public double scalarProduct(final AbstractDoubleVector v) {
        if (getDimension() == v.getDimension()) {
            int result = 0;
            for (int i = 0; i < getDimension(); i++)
                result += getPrimitiveElement(i) * v.getPrimitiveElement(i);
            return result;
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    /**
     * Returns the tensor product of this vector and another.
     *
     * @param v DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public DoubleMatrix tensorProduct(final AbstractDoubleVector v) {
        DoubleMatrix ans = new DoubleMatrix(getDimension(),
                v.getDimension());

        for (int j, i = 0; i < getDimension(); i++) {
            for (j = 0; j < v.getDimension(); j++)
                ans.setElement(i, j, getPrimitiveElement(i) * v.getPrimitiveElement(j));
        }

        return ans;
    }

    /**
     * Invert vector elements order from the last to the first.
     */
    public AbstractDoubleVector reverse() {
        double[] ans = new double[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            ans[getDimension() - i] = getPrimitiveElement(i);
        }
        return new DoubleVector(ans);
    }

    /**
     * Computes a sub vector from the parameters index.
     * If k1<0 k1 elements are added at the beginning of the returned vector
     * If k2>getDimension() k2-getDimension() elements are added at the end of the returned vector
     * Finally, if k1>k2 the vector is returned inverted.
     *
     * @param k1 the beginning index
     * @param k2 the finishing index
     * @return the sub vector.
     */
    public AbstractDoubleVector getSubVector(final int k1, final int k2) {
        double[] ans = new double[Math.abs(k2 - k1) + 1];
        if (k1 < k2) {
            for (int i = Math.max(k1, 0); i < Math.min(k2, getDimension()); i++) {
                ans[i - k1] = getPrimitiveElement(i);
            }
        } else {
            for (int i = Math.max(k2, 0); i < Math.min(k1, getDimension()); i++) {
                ans[k1 - i] = getPrimitiveElement(i);
            }
        }
        return new DoubleVector(ans);
    }

    /**
     * Set a sub vector.
     * If k<0 the elements are added at the beginning of the returned vector
     * If k+v.getDimension()>getDimension() k+v.getDimension()-getDimension() elements are added at the end of the returned vector
     *
     * @param k Initial row index to offset the patching vector
     * @param v the patching vector
     */
    public AbstractDoubleVector setSubVector(final int k, final AbstractDoubleVector v) {
        if (k < 0) {
            double[] ans = new double[Math.max(getDimension() - k, v.getDimension())];
            for (int i = 0; i < v.getDimension(); i++) {
                ans[i] = v.getPrimitiveElement(i);
            }
            for (int i = Math.max(v.getDimension() + k, 0); i < getDimension(); i++) {
                ans[i + Math.max(-k - v.getDimension(), -k)] = getPrimitiveElement(i);
            }
            return new DoubleVector(ans);
        } else {
            double[] ans = new double[Math.max(getDimension(), k + v.getDimension())];
            for (int i = 0; i < k; i++) {
                ans[i] = getPrimitiveElement(i);
            }
            for (int i = 0; i < v.getDimension(); i++) {
                ans[i + k] = v.getPrimitiveElement(i);
            }
            for (int i = k + v.getDimension(); i < getDimension(); i++) {
                ans[i] = getPrimitiveElement(i);
            }
            return new DoubleVector(ans);
        }
    }

    /**
     * Applies a function on all the vector components.
     *
     * @param f a user-defined function.
     * @return a double vector.
     */
    public AbstractDoubleVector mapElements(final PrimitiveMapping f) {
        final DoubleVector result = new DoubleVector(getDimension());
        for (int i = 0; i < getDimension(); i++)
            result.setElement(i, f.map(getPrimitiveElement(i)));
        return result;
    }

    /**
     * Projects the vector to an array.
     *
     * @return an double array.
     */
    public double[] toPrimitiveArray() {
        final double[] result = new double[getDimension()];
        for (int i = 0; i < getDimension(); i++)
            result[i] = getPrimitiveElement(i);
        return result;
    }

    /**
     * Projects the vector to the corresponding (n, 1) matrix class.
     *
     * @return an double matrix.
     */
    public Matrix toMatrix() {
        final double[][] result = new double[getDimension()][1];
        for (int i = 0; i < getDimension(); i++)
            result[i][0] = getPrimitiveElement(i);
        return new DoubleMatrix(result);
    }

    /**
     * Read a vector from a stream.  The format is DIFFERENT from the print method,
     * so printed vector can be read back in (provided they were printed using
     * US Locale).  Elements are separated by
     * whitespace, all the elements appear on a single line NOT a single column (use matrix.read() if you want this behavior).
     *
     * @param input the input stream.
     */
    public static AbstractDoubleVector read(BufferedReader input) throws java.io.IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(input);

        // Although StreamTokenizer will parse numbers, it doesn't recognize
        // scientific notation (E or D); however, Double.valueOf does.
        // The strategy here is to disable StreamTokenizer's number parsing.
        // We'll only get whitespace delimited words, EOL's and EOF's.
        // These words should all be numbers, for Double.valueOf to parse.

        tokenizer.resetSyntax();
        tokenizer.wordChars(0, 255);
        tokenizer.whitespaceChars(0, ' ');
        tokenizer.eolIsSignificant(true);
        java.util.Vector v = new java.util.Vector();

        // Ignore initial empty lines
        while (tokenizer.nextToken() == StreamTokenizer.TT_EOL) ;
        if (tokenizer.ttype == StreamTokenizer.TT_EOF)
            throw new java.io.IOException("Unexpected EOF on vector read.");
        do {
            v.addElement(new Double(tokenizer.sval)); // Read & store 1st row.
        } while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);

        int n = v.size();  // Now we've got the number of columns!
        double row[] = new double[n];
        for (int j = 0; j < n; j++)  // extract the elements of the 1st row.
            row[j] = ((Double) v.elementAt(j)).doubleValue();
        return new DoubleVector(row);
    }

}

