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
import org.jscience.mathematics.algebraic.AbstractVector;
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.algebras.HilbertSpace;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.ComplexMapping;
import org.jscience.util.IllegalDimensionException;

import java.io.BufferedReader;
import java.io.StreamTokenizer;

/**
 * The AbstractComplexVector class encapsulates vectors containing complex
 * numbers.
 *
 * @author Mark Hale
 * @version 1.0
 */

//for performance reason we never check if arguments are different from null.

//hashcode, norm, norm, normalize, infnorm, trace, hermitianAdjoint, operatorNorm need vector dimension checking

//Jama provides some more methods from which we could get some inspiration: see AbstractDoubleMatrix

public abstract class AbstractComplexVector extends AbstractVector
        implements HilbertSpace.Member {
    /**
     * Creates a new AbstractComplexVector object.
     *
     * @param dim DOCUMENT ME!
     */
    protected AbstractComplexVector(final int dim) {
        super(dim);
    }

    /**
     * Returns a comma delimited string representing the value of this vector.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        final StringBuffer buf = new StringBuffer(12 * getDimension());
        int i;

        for (i = 0; i < (getDimension() - 1); i++) {
            buf.append(getPrimitiveElement(i).toString());
            buf.append(',');
        }

        buf.append(getPrimitiveElement(i).toString());

        return buf.toString();
    }

    /**
     * Returns a hashcode for this NON EMPTY vector.
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return (int) Math.exp(norm());
    }

    /**
     * Returns the real part of this complex vector.
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector real() {
        final AbstractDoubleVector result = new DoubleVector(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            result.setElement(i, getPrimitiveElement(i).real());
        }
        return result;
    }

    /**
     * Returns the imaginary part of this complex vector.
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector imag() {
        final AbstractDoubleVector result = new DoubleVector(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            result.setElement(i, getPrimitiveElement(i).imag());
        }
        return result;
    }

    /**
     * Returns an element of this vector.
     *
     * @param n index of the vector element
     * @return DOCUMENT ME!
     */
    public Complex getElement(final int n) {
        return getPrimitiveElement(n);
    }

    /**
     * Returns the real part of an element of the matrix.
     *
     * @param i row index of the element
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public double getRealElement(final int i) {
        return getPrimitiveElement(i).real();
    }

    /**
     * Returns the imag part of an element of the matrix.
     *
     * @param i row index of the element
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public double getImagElement(final int i) {
        return getPrimitiveElement(i).imag();
    }

    /**
     * Returns an element of this vector.
     *
     * @param n index of the vector element
     * @return DOCUMENT ME!
     */
    public abstract Complex getPrimitiveElement(int n);

    /**
     * Returns the ith row.
     *
     * @param i DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractComplexVector getRow(final int i) {
        if ((i >= 0) && (i < numRows())) {
            final Complex[] array = new Complex[1];
            array[0] = getPrimitiveElement(i);

            return new ComplexVector(array);
        } else {
            throw new IllegalDimensionException("Requested element out of bounds.");
        }
    }

    /**
     * Returns the ith column.
     *
     * @param j DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractComplexVector getColumn(final int j) {
        if (j == 0) {
            final Complex[] array = new Complex[1];

            for (int i = 0; i < getDimension(); i++) {
                array[i] = getPrimitiveElement(i);
            }

            return new ComplexVector(array);
        } else {
            throw new IllegalDimensionException("Requested element out of bounds.");
        }
    }

    /**
     * Returns the ith row.
     */
    public void setRow(final int i, final AbstractComplexVector v) {

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
    public void setColumn(final int j, final AbstractComplexVector v) {

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
     * Sets the value of an element of this vector. Should only be used to
     * initialise this vector.
     *
     * @param n index of the vector element
     * @param z a complex number
     */
    public abstract void setElement(final int n, final Complex z);

    /**
     * Sets the value of an element of this vector. Should only be used to
     * initialise this vector.
     *
     * @param n index of the vector element
     * @param x the real part of a complex number
     * @param y the imaginary part of a complex number
     */
    public void setElement(final int n, final double x, final double y) {
        setElement(n, new Complex(x, y));
    }

    /**
     * Sets the value of all elements of the vector.
     *
     * @param r a ring element
     */
    public void setAllElements(final Complex r) {
        for (int i = 0; i < getDimension(); i++) {
            setElement(i, r);
        }
    }

    /**
     * Compares two Complex vectors for equality.
     * Two vectors are considered to be equal if the norm of their difference is within the tolerance.
     *
     * @param obj a Complex vector
     */
    public boolean equals(Object obj) {
        return equals(obj, java.lang.Double.valueOf(JScience.getProperty("tolerance")).doubleValue());
    }

    public boolean equals(Object obj, double tol) {
        if (obj != null && (obj instanceof AbstractComplexVector)) {
            final AbstractComplexVector vec = (AbstractComplexVector) obj;
            return (this.getDimension() == vec.getDimension() && this.subtract(vec).norm() <= tol);
        } else
            return false;
    }

    //we could also get the the sum of the squares of the components
    //but I am not sure of how I should compute these methods

    /**
     * Returns the mass.
     *
     * @return DOCUMENT ME!
     */
    public Complex mass() {
        if (getDimension() > 0) {
            Complex mass = Complex.ZERO;
            for (int k = 0; k < getDimension(); k++)
                mass = mass.add(getPrimitiveElement(k));
            return mass;
        } else
            throw new ArithmeticException("The mass of a zero dimension vector is undefined.");
    }

    /**
     * Returns the l<sup>2</sup>-norm (magnitude).
     *
     * @return DOCUMENT ME!
     */
    public double norm() {
        if (getDimension() > 0) {
            double mod = getPrimitiveElement(0).mod();
            double answer = mod * mod;

            for (int i = 1; i < getDimension(); i++) {
                mod = getPrimitiveElement(i).mod();
                answer += (mod * mod);
            }

            return Math.sqrt(answer);
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
                setElement(i, getPrimitiveElement(i).divide(norm));
        }
    }

    /**
     * Returns the l<sup><img border=0 alt="infinity"
     * src="doc-files/infinity.gif"></sup>-norm.
     *
     * @return DOCUMENT ME!
     */
    public double infNorm() {
        if (getDimension() > 0) {
            double infNorm = getPrimitiveElement(0).mod();

            for (int i = 1; i < getDimension(); i++) {
                double mod = getPrimitiveElement(i).mod();

                if (mod > infNorm) {
                    infNorm = mod;
                }
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
            array[i] = getPrimitiveElement(i).abs();
        return new DoubleVector(array);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix transpose() {
        Complex[][] array = new Complex[getDimension()][1];

        for (int i = 0; i < getDimension(); i++) {
            array[i][0] = getPrimitiveElement(i);
        }

        return new ComplexMatrix(array);
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Ring.Member multiply(final Ring.Member r) {
        if (r instanceof Matrix) {
            if (r instanceof IntegerMatrix) {
                IntegerMatrix other = (IntegerMatrix) r;

                if (other.numRows() == 1) {
                    final Complex[][] array = new Complex[numRows()][other.numColumns()];

                    for (int j = 0; j < numRows(); j++) {
                        for (int k = 0; k < other.numColumns(); k++) {
                            array[j][k] = getPrimitiveElement(j).multiply(other.getPrimitiveElement(0, k));
                        }
                    }

                    return new ComplexMatrix(array);
                } else {
                    throw new IllegalDimensionException("Cannot multiply a Vector by a Matrix with more than one row.");
                }
            } else if (r instanceof DoubleMatrix) {
                DoubleMatrix other = (DoubleMatrix) r;

                if (other.numRows() == 1) {
                    final Complex[][] array = new Complex[numRows()][other.numColumns()];

                    for (int j = 0; j < numRows(); j++) {
                        for (int k = 0; k < other.numColumns(); k++) {
                            array[j][k] = getPrimitiveElement(j).multiply(other.getPrimitiveElement(0, k));
                        }
                    }

                    return new ComplexMatrix(array);
                } else {
                    throw new IllegalDimensionException("Cannot multiply a Vector by a Matrix with more than one row.");
                }
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
                } else {
                    throw new IllegalDimensionException("Cannot multiply a Vector by a Matrix with more than one row.");
                }
            } else if (r instanceof RingMatrix) {
                RingMatrix other = (RingMatrix) r;

                if (other.numRows() == 1) {
                    final Ring.Member[][] array = new Ring.Member[numRows()][other.numColumns()];

                    for (int j = 0; j < numRows(); j++) {
                        for (int k = 0; k < other.numColumns(); k++) {
                            array[j][k] = ((Ring.Member) other.getPrimitiveElement(0, k)).multiply(getPrimitiveElement(j));
                        }
                    }

                    return new RingMatrix(array);
                } else {
                    throw new IllegalDimensionException("Cannot multiply a Vector by a Matrix with more than one row.");
                }
            } else {
                throw new IllegalDimensionException("Member not recognized by this method.");
            }
        } else {
            throw new IllegalArgumentException("Member not recognized by this method.");
        }
    }

    //============
    // OPERATIONS
    //============

    /**
     * Returns the complex conjugate of this vector.
     *
     * @return DOCUMENT ME!
     */
    public AbstractComplexVector conjugate() {
        final ComplexVector result = new ComplexVector(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            result.setElement(i, getPrimitiveElement(i).conjugate());
        }
        return result;
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param v a complex vector
     * @return DOCUMENT ME!
     */
    public AbstractComplexVector add(final AbstractComplexVector v) {
        if (getDimension() == v.getDimension()) {
            final ComplexVector result = new ComplexVector(getDimension());
            for (int i = 0; i < getDimension(); i++)
                result.setElement(i, getPrimitiveElement(i).add(v.getPrimitiveElement(i)));
            return result;
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param v a complex vector
     * @return DOCUMENT ME!
     */
    public AbstractComplexVector subtract(final AbstractComplexVector v) {
        if (getDimension() == v.getDimension()) {
            final ComplexVector result = new ComplexVector(getDimension());
            for (int i = 0; i < getDimension(); i++)
                result.setElement(i, getPrimitiveElement(i).subtract(v.getPrimitiveElement(i)));
            return result;
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    /**
     * Returns the multiplication of this vector by a scalar.
     *
     * @param z a complex number
     * @return DOCUMENT ME!
     */
    public AbstractComplexVector scalarMultiply(final Complex z) {
        final ComplexVector result = new ComplexVector(getDimension());
        for (int i = 0; i < getDimension(); i++)
            result.setElement(i, getPrimitiveElement(i).multiply(z));
        return result;
    }

    /**
     * Returns the multiplication of this vector by a scalar.
     *
     * @param x a double
     * @return DOCUMENT ME!
     */
    public AbstractComplexVector scalarMultiply(final double x) {
        final ComplexVector result = new ComplexVector(getDimension());
        for (int i = 0; i < getDimension(); i++)
            result.setElement(i, getPrimitiveElement(i).multiply(x));
        return result;
    }

    /**
     * Returns the division of this vector by a scalar.
     *
     * @param z a complex number
     * @return DOCUMENT ME!
     */
    public AbstractComplexVector scalarDivide(final Complex z) {
        final ComplexVector result = new ComplexVector(getDimension());
        for (int i = 0; i < getDimension(); i++)
            result.setElement(i, getPrimitiveElement(i).divide(z));
        return result;
    }

    /**
     * Returns the division of this vector by a scalar.
     *
     * @param x a double
     * @return DOCUMENT ME!
     */
    public AbstractComplexVector scalarDivide(final double x) {
        final ComplexVector result = new ComplexVector(getDimension());
        for (int i = 0; i < getDimension(); i++)
            result.setElement(i, getPrimitiveElement(i).divide(x));
        return result;
    }

    /**
     * Returns the scalar product of this vector and another.
     *
     * @param v a complex vector
     * @return DOCUMENT ME!
     */
    public Complex scalarProduct(final AbstractComplexVector v) {
        if (getDimension() == v.getDimension()) {
            Complex elem = getPrimitiveElement(0);
            Complex comp = v.getPrimitiveElement(0);
            double real = (elem.real() * comp.real()) +
                    (elem.imag() * comp.imag());
            double imag = (elem.imag() * comp.real()) -
                    (elem.real() * comp.imag());
            for (int i = 1; i < getDimension(); i++) {
                elem = getPrimitiveElement(i);
                comp = v.getPrimitiveElement(i);
                real += ((elem.real() * comp.real()) +
                        (elem.imag() * comp.imag()));
                imag += ((elem.imag() * comp.real()) -
                        (elem.real() * comp.imag()));
            }
            return new Complex(real, imag);
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
    public ComplexMatrix tensorProduct(final AbstractComplexVector v) {
        ComplexMatrix ans = new ComplexMatrix(getDimension(),
                v.getDimension());

        for (int j, i = 0; i < getDimension(); i++) {
            for (j = 0; j < v.getDimension(); j++)
                ans.setElement(i, j, getPrimitiveElement(i).multiply(v.getPrimitiveElement(j)));
        }

        return ans;
    }

    /**
     * Invert vector elements order from the last to the first.
     */
    public AbstractComplexVector reverse() {
        Complex[] ans = new Complex[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            ans[getDimension() - i] = getPrimitiveElement(i);
        }
        return new ComplexVector(ans);
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
    public AbstractComplexVector getSubVector(final int k1, final int k2) {
        Complex[] ans = new Complex[Math.abs(k2 - k1) + 1];
        if (k1 < k2) {
            for (int i = Math.max(k1, 0); i < Math.min(k2, getDimension()); i++) {
                ans[i - k1] = getPrimitiveElement(i);
            }
        } else {
            for (int i = Math.max(k2, 0); i < Math.min(k1, getDimension()); i++) {
                ans[k1 - i] = getPrimitiveElement(i);
            }
        }
        return new ComplexVector(ans);
    }

    /**
     * Set a sub vector.
     * If k<0 the elements are added at the beginning of the returned vector
     * If k+v.getDimension()>getDimension() k+v.getDimension()-getDimension() elements are added at the end of the returned vector
     *
     * @param k Initial row index to offset the patching vector
     * @param v the patching vector
     */
    public AbstractComplexVector setSubVector(final int k, final AbstractComplexVector v) {
        if (k < 0) {
            Complex[] ans = new Complex[Math.max(getDimension() - k, v.getDimension())];
            for (int i = 0; i < v.getDimension(); i++) {
                ans[i] = v.getPrimitiveElement(i);
            }
            for (int i = Math.max(v.getDimension() + k, 0); i < getDimension(); i++) {
                ans[i + Math.max(-k - v.getDimension(), -k)] = getPrimitiveElement(i);
            }
            return new ComplexVector(ans);
        } else {
            Complex[] ans = new Complex[Math.max(getDimension(), k + v.getDimension())];
            for (int i = 0; i < k; i++) {
                ans[i] = getPrimitiveElement(i);
            }
            for (int i = 0; i < v.getDimension(); i++) {
                ans[i + k] = v.getPrimitiveElement(i);
            }
            for (int i = k + v.getDimension(); i < getDimension(); i++) {
                ans[i] = getPrimitiveElement(i);
            }
            return new ComplexVector(ans);
        }
    }

    /**
     * Applies a function on all the vector components.
     *
     * @param f a user-defined function
     * @return a complex vector
     */
    public AbstractComplexVector mapElements(final ComplexMapping f) {
        final ComplexVector result = new ComplexVector(getDimension());
        for (int i = 0; i < getDimension(); i++)
            result.setElement(i, f.map(getPrimitiveElement(i)));
        return result;
    }

    /**
     * Projects the vector to an array.
     *
     * @return an double array.
     */
    public Complex[] toPrimitiveArray() {
        final Complex[] result = new Complex[getDimension()];
        for (int i = 0; i < getDimension(); i++)
            result[i] = getPrimitiveElement(i);
        return result;
    }

    /**
     * Projects the vector to the corresponding (n, 1) matrix class.
     *
     * @return an Complex matrix.
     */
    public Matrix toMatrix() {
        final Complex[][] result = new Complex[getDimension()][1];
        for (int i = 0; i < getDimension(); i++)
            result[i][0] = getPrimitiveElement(i);
        return new ComplexMatrix(result);
    }

    /**
     * Read a vector from a stream.  The format is DIFFERENT from the print method,
     * so printed vector can be read back in (provided they were printed using
     * US Locale).  Elements are separated by
     * whitespace, all the elements appear on a single line NOT a single column (use matrix.read() if you want this behavior).
     *
     * @param input the input stream.
     */
    public static AbstractComplexVector read(BufferedReader input) throws java.io.IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(input);

        // Although StreamTokenizer will parse numbers, it doesn't recognize
        // scientific notation (E or D); however, Double.valueOf does.
        // The strategy here is to disable StreamTokenizer's number parsing.
        // We'll only get whitespace delimited words, EOL's and EOF's.
        // These words should all be numbers, for Complex.valueOf to parse.

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
            v.addElement(new Complex(tokenizer.sval)); // Read & store 1st row.
        } while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);

        int n = v.size();  // Now we've got the number of columns!
        Complex row[] = new Complex[n];
        for (int j = 0; j < n; j++)  // extract the elements of the 1st row.
            row[j] = (Complex) v.elementAt(j);
        return new ComplexVector(row);
    }

}
