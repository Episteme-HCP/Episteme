package org.jscience.mathematics.algebraic.matrices;

import org.jscience.mathematics.algebraic.AbstractVector;
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.modules.VectorSpace;
import org.jscience.mathematics.algebraic.numbers.Boolean;
import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.util.IllegalDimensionException;

import java.io.BufferedReader;
import java.io.StreamTokenizer;

/**
 * The AbstractBooleanVector class encapsulates vectors containing booleans.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this class is mostly here to provide the same support as integers, doubles, complexs, even if we don't define Boolean2Vectors
public abstract class AbstractBooleanVector extends AbstractVector {
    /**
     * Creates a new AbstractBooleanVector object.
     *
     * @param dim DOCUMENT ME!
     */
    protected AbstractBooleanVector(final int dim) {
        super(dim);
    }

    /**
     * Returns a comma delimited string representing the value of this vector.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        final StringBuffer buf = new StringBuffer(8 * getDimension());
        int i;

        for (i = 0; i < (getDimension() - 1); i++) {
            buf.append(getPrimitiveElement(i));
            buf.append(',');
        }

        buf.append(getPrimitiveElement(i));

        return buf.toString();
    }

    /**
     * Returns a hashcode for this NON EMPTY vector.
     *
     * @return DOCUMENT ME!
     */
    public abstract int hashCode();

    /**
     * Converts this vector to a integer vector.
     *
     * @return a double vector
     */
    public AbstractIntegerVector toIntegerVector() {
        final int array[] = new int[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            if (getPrimitiveElement(i)) {
                array[i] = 1;
            } else {
                array[i] = 0;
            }
        }
        return new IntegerVector(array);
    }

    /**
     * Converts this vector to a double vector.
     *
     * @return a double vector
     */
    public AbstractDoubleVector toDoubleVector() {
        final double array[] = new double[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            if (getPrimitiveElement(i)) {
                array[i] = 1;
            } else {
                array[i] = 0;
            }
        }
        return new DoubleVector(array);
    }

    /**
     * Converts this vector to a complex vector.
     *
     * @return a complex vector
     */
    public AbstractComplexVector toComplexVector() {
        final double arrayRe[] = new double[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            if (getPrimitiveElement(i)) {
                arrayRe[i] = 1;
            } else {
                arrayRe[i] = 0;
            }
        }
        return new ComplexVector(arrayRe, new double[getDimension()]);
    }

    /**
     * Returns an element of this vector (this is the fastest way of getting an
     * element for this kind of matrix).
     *
     * @param n index of the vector element.
     * @return DOCUMENT ME!
     */
    public abstract boolean getPrimitiveElement(int n);

    /**
     * Returns an element of this vector.
     *
     * @param n index of the vector element.
     * @return DOCUMENT ME!
     */
    public Boolean getElement(final int n) {
        return new Boolean(getPrimitiveElement(n));
    }

    /**
     * Returns the ith row.
     *
     * @param i DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractBooleanVector getRow(final int i) {
        if ((i >= 0) && (i < numRows())) {
            final boolean[] array = new boolean[1];
            array[0] = getPrimitiveElement(i);

            return new BooleanVector(array);
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
    public AbstractBooleanVector getColumn(final int j) {
        if (j == 0) {
            final boolean[] array = new boolean[getDimension()];

            for (int i = 0; i < getDimension(); i++) {
                array[i] = getPrimitiveElement(i);
            }

            return new BooleanVector(array);
        } else {
            throw new IllegalDimensionException("Requested element out of bounds.");
        }
    }

    /**
     * Returns the ith row.
     */
    public void setRow(final int i, final AbstractBooleanVector v) {

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
    public void setColumn(final int j, final AbstractBooleanVector v) {

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
     * @param n index of the vector element
     * @param x an integer
     */
    public abstract void setElement(final int n, final boolean x);

    /**
     * Sets the value of a component of this vector.
     *
     * @param n index of the vector component
     * @param x a boolean
     * @throws IllegalDimensionException If attempting to access an invalid
     *                                   component.
     */
    public void setElement(final int n, final Boolean x) {
        setElement(n, x.value());
    }

    /**
     * Sets the value of all elements of the vector.
     *
     * @param r a boolean element
     */
    public void setAllElements(final boolean r) {
        for (int i = 0; i < getDimension(); i++) {
            setElement(i, r);
        }
    }

    /**
     * Compares two integer vectors for equality.
     *
     * @param a an integer vector
     * @return DOCUMENT ME!
     */
    public boolean equals(Object a) {
        if ((a != null) && (a instanceof AbstractBooleanVector) &&
                (getDimension() == ((AbstractBooleanVector) a).getDimension())) {
            final AbstractBooleanVector iv = (AbstractBooleanVector) a;

            for (int i = 0; i < getDimension(); i++) {
                if (getPrimitiveElement(i) != iv.getPrimitiveElement(i)) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    // SCALAR DIVISON

    /**
     * Returns the division of this matrix by a scalar.
     *
     * @param x DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public VectorSpace.Member scalarDivide(final Field.Member x) {
        throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix transpose() {
        boolean[][] array = new boolean[getDimension()][1];

        for (int i = 0; i < getDimension(); i++) {
            array[i][0] = getPrimitiveElement(i);
        }

        return new BooleanMatrix(array);
    }

    //============
    // OPERATIONS
    //============

    /**
     * Returns the and of this vector and another.
     *
     * @param v an boolean vector
     * @return DOCUMENT ME!
     */
    public AbstractBooleanVector and(final AbstractBooleanVector v) {
        if (getDimension() == v.getDimension()) {
            BooleanVector result = new BooleanVector(getDimension());
            for (int i = getDimension(); --i >= 0;)
                result.setElement(i, getPrimitiveElement(i) && v.getPrimitiveElement(i));
            return result;
        } else
            throw new IllegalDimensionException("Vectors are different sizes.");
    }

    /**
     * Clears all of the Boolean in receiver whose corresponding
     * bit is set in the other BooleanVector (A = A \ B).
     * In other words, determines the difference (A=A\B) between two BooleanVectors.
     *
     * @param v a BooleanVector with which to mask the receiver.
     * @throws IllegalArgumentException if <tt>size() &gt; other.size()</tt>.
     */
    public BooleanVector andNot(final BooleanVector v) {
        if (getDimension() == v.getDimension()) {
            BooleanVector result = new BooleanVector(getDimension());
            for (int i = getDimension(); --i >= 0;)
                result.setElement(i, getPrimitiveElement(i) && !v.getPrimitiveElement(i));
            return result;
        } else
            throw new IllegalDimensionException("Vectors are different sizes.");
    }

    /**
     * Returns the or of this vector and another.
     *
     * @param v an boolean vector
     * @return DOCUMENT ME!
     */
    public AbstractBooleanVector or(final AbstractBooleanVector v) {
        if (getDimension() == v.getDimension()) {
            BooleanVector result = new BooleanVector(getDimension());
            for (int i = getDimension(); --i >= 0;)
                result.setElement(i, getPrimitiveElement(i) || v.getPrimitiveElement(i));
            return result;
        } else
            throw new IllegalDimensionException("Vectors are different sizes.");
    }

    /**
     * Returns the not of this vector and another.
     *
     * @return DOCUMENT ME!
     */
    public AbstractBooleanVector not() {
        BooleanVector result = new BooleanVector(getDimension());
        for (int i = getDimension(); --i >= 0;) result.setElement(i, !getPrimitiveElement(i));
        return result;
    }

    /**
     * Returns the xor of this vector and another.
     *
     * @param v an boolean vector
     * @return DOCUMENT ME!
     */
    public AbstractBooleanVector xor(final AbstractBooleanVector v) {
        if (getDimension() == v.getDimension()) {
            BooleanVector result = new BooleanVector(getDimension());
            for (int i = getDimension(); --i >= 0;)
                result.setElement(i, getPrimitiveElement(i) ^ v.getPrimitiveElement(i));
            return result;
        } else
            throw new IllegalDimensionException("Vectors are different sizes.");
    }

    /**
     * Returns the negative (not) of this vector.
     */
    public AbelianGroup.Member negate() {
        return not();
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param v an boolean vector
     * @return DOCUMENT ME!
     */
    public AbstractBooleanVector add(final AbstractBooleanVector v) {
        return or(v);
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param v an boolean vector
     * @return DOCUMENT ME!
     */
    public AbstractBooleanVector subtract(final AbstractBooleanVector v) {
        if (getDimension() == v.getDimension()) {
            BooleanVector result = new BooleanVector(getDimension());
            for (int i = getDimension(); --i >= 0;)
                result.setElement(i, getPrimitiveElement(i) || !v.getPrimitiveElement(i));
            return result;
        } else
            throw new IllegalDimensionException("Vectors are different sizes.");
    }

    /**
     * Returns the multiplication of this vector by a scalar.
     *
     * @param x an boolean
     * @return DOCUMENT ME!
     */
    public AbstractBooleanVector scalarMultiply(final boolean x) {
        BooleanVector result = new BooleanVector(getDimension());
        for (int i = 0; i < getDimension(); i++) result.setElement(i, getPrimitiveElement(i) && x);
        return result;
    }

    /**
     * Returns the scalar product of this vector and another.
     *
     * @param v an boolean vector
     * @return DOCUMENT ME!
     */
    public int scalarProduct(final AbstractBooleanVector v) {
        if (getDimension() == v.getDimension()) {
            int answer = 0;
            for (int i = 0; i < getDimension(); i++) {
                if (getPrimitiveElement(i) && v.getPrimitiveElement(i)) {
                    answer++;
                }
            }
            return answer;
        } else
            throw new IllegalDimensionException("Vectors are different sizes.");
    }

    /**
     * Invert vector elements order from the last to the first.
     */
    public AbstractBooleanVector reverse() {
        boolean[] ans = new boolean[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            ans[getDimension() - i] = getPrimitiveElement(i);
        }
        return new BooleanVector(ans);
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
    public AbstractBooleanVector getSubVector(final int k1, final int k2) {
        boolean[] ans = new boolean[Math.abs(k2 - k1) + 1];
        if (k1 < k2) {
            for (int i = Math.max(k1, 0); i < Math.min(k2, getDimension()); i++) {
                ans[i - k1] = getPrimitiveElement(i);
            }
        } else {
            for (int i = Math.max(k2, 0); i < Math.min(k1, getDimension()); i++) {
                ans[k1 - i] = getPrimitiveElement(i);
            }
        }
        return new BooleanVector(ans);
    }

    /**
     * Set a sub vector.
     * If k<0 the elements are added at the beginning of the returned vector
     * If k+v.getDimension()>getDimension() k+v.getDimension()-getDimension() elements are added at the end of the returned vector
     *
     * @param k Initial row index to offset the patching vector
     * @param v the patching vector
     */
    public AbstractBooleanVector setSubVector(final int k, final AbstractBooleanVector v) {
        if (k < 0) {
            boolean[] ans = new boolean[Math.max(getDimension() - k, v.getDimension())];
            for (int i = 0; i < v.getDimension(); i++) {
                ans[i] = v.getPrimitiveElement(i);
            }
            for (int i = Math.max(v.getDimension() + k, 0); i < getDimension(); i++) {
                ans[i + Math.max(-k - v.getDimension(), -k)] = getPrimitiveElement(i);
            }
            return new BooleanVector(ans);
        } else {
            boolean[] ans = new boolean[Math.max(getDimension(), k + v.getDimension())];
            for (int i = 0; i < k; i++) {
                ans[i] = getPrimitiveElement(i);
            }
            for (int i = 0; i < v.getDimension(); i++) {
                ans[i + k] = v.getPrimitiveElement(i);
            }
            for (int i = k + v.getDimension(); i < getDimension(); i++) {
                ans[i] = getPrimitiveElement(i);
            }
            return new BooleanVector(ans);
        }
    }

    /**
     * Applies a function on all the vector components.  We assume that the
     * mapping is from int to int and that input values are only 0 or 1
     * meaning respectively false, true
     *
     * @param f a user-defined function.
     * @return an boolean vector.
     */
    public AbstractBooleanVector mapElements(final PrimitiveMapping f) {
        final BooleanVector result = new BooleanVector(getDimension());
        for (int i = 0; i < getDimension(); i++)
            if (f.map(new Boolean(getPrimitiveElement(i)).intValue()) == 1) {
                result.setElement(i, true);
            } else {
                result.setElement(i, false);
            }
        return result;
    }

    /**
     * Projects the vector to an array.
     *
     * @return a boolean array.
     */
    public boolean[] toPrimitiveArray() {
        final boolean[] result = new boolean[getDimension()];
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
        final boolean[][] result = new boolean[getDimension()][1];
        for (int i = 0; i < getDimension(); i++)
            result[i][0] = getPrimitiveElement(i);
        return new BooleanMatrix(result);
    }

    /**
     * Read a vector from a stream.  The format is DIFFERENT from the print method,
     * so printed vector can be read back in (provided they were printed using
     * US Locale).  Elements are separated by
     * whitespace, all the elements appear on a single line NOT a single column (use matrix.read() if you want this behavior).
     *
     * @param input the input stream.
     */
    public static AbstractBooleanVector read(BufferedReader input) throws java.io.IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(input);

        // Although StreamTokenizer will parse numbers, it doesn't recognize
        // scientific notation (E or D); however, Double.valueOf does.
        // The strategy here is to disable StreamTokenizer's number parsing.
        // We'll only get whitespace delimited words, EOL's and EOF's.
        // These words should all be numbers, for Boolean.valueOf to parse.

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
        boolean row[] = new boolean[n];
        for (int j = 0; j < n; j++)  // extract the elements of the 1st row.
            row[j] = ((Boolean) v.elementAt(j)).value();
        return new BooleanVector(row);
    }

}
