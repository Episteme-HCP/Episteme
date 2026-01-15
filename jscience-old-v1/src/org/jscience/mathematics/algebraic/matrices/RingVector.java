package org.jscience.mathematics.algebraic.matrices;

import org.jscience.mathematics.algebraic.AbstractVector;
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.modules.VectorSpace;
import org.jscience.mathematics.analysis.NumberMapping;

import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;


/**
 * The RingVector class provides an object for encapsulating vectors over
 * an arbitrary ring.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//although it would be nice if RingVector was the superclass of all other vectors,
//we decided that it wouldn't be so as it adds complexity to the design and possible performance penality
//yet it would be quite simple to change with current implementation as children class override all the methods of RingVector.

//possible methods we could add:
//hashCode
public class RingVector extends AbstractVector implements Cloneable,
    Serializable {
    /** Array containing the elements of the vector . */
    protected Ring.Member[] vector;

/**
     * Constructs an empty vector.
     *
     * @param dim the dimension of the vector.
     */
    public RingVector(final int dim) {
        super(dim);
        vector = new RingVector[dim];
    }

/**
     * Constructs a vector by wrapping an array.
     *
     * @param array an assigned value
     */
    public RingVector(final Ring.Member[] array) {
        super(array.length);
        vector = array;
    }

/**
     * Constructs a vector by copying a vector.
     *
     * @param vec an assigned value
     */
    public RingVector(final RingVector vec) {
        super(vec.getDimension());
        vector = new RingVector[vec.getDimension()];

        RingVector vect = (RingVector) vec;
        System.arraycopy(vect.vector, 0, vector, 0, vec.getDimension());
    }

    /**
     * Compares two vectors for equality.
     *
     * @param v a vector
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object v) {
        if ((v != null) && (v instanceof RingVector) &&
                (getDimension() == ((RingVector) v).getDimension())) {
            final RingVector rm = (RingVector) v;

            for (int i = 0; i < getDimension(); i++) {
                if (!vector[i].equals(rm.getPrimitiveElement(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a hashcode for this NON EMPTY vector.
     *
     * @return DOCUMENT ME!
     */

    //public int hashCode() {
    //  return (int) Math.exp(norm());
    //}
    /**
     * Returns a string representing this vector.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        final StringBuffer buf = new StringBuffer(5 * numRows());

        for (int i = 0; i < numRows(); i++) {
            buf.append(vector[i].toString());
            buf.append(' ');
        }

        buf.append('\n');

        return buf.toString();
    }

    /**
     * Returns an element of the vector.
     *
     * @param i row index of the element
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         element.
     */
    public Number getElement(final int i) {
        if ((i >= 0) && (i < getDimension())) {
            return (Number) vector[i];
        } else {
            throw new IllegalDimensionException(getInvalidElementMsg(i));
        }
    }

    /**
     * Returns an element of this vector (this is the fastest way of
     * getting an element for this kind of matrix).
     *
     * @param i index of the vector element.
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         element.
     */
    public Ring.Member getPrimitiveElement(final int i) {
        if ((i >= 0) && (i < getDimension())) {
            return vector[i];
        } else {
            throw new IllegalDimensionException(getInvalidElementMsg(i));
        }
    }

    /**
     * Returns the ith row.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public RingVector getRow(final int i) {
        if ((i >= 0) && (i < numRows())) {
            Ring.Member[] array = new Ring.Member[1];
            array[0] = getPrimitiveElement(i);

            return new RingVector(array);
        } else {
            throw new IllegalDimensionException(
                "Requested element out of bounds.");
        }
    }

    /**
     * Returns the ith column.
     *
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public RingVector getColumn(final int j) {
        if (j == 0) {
            Ring.Member[] array = new Ring.Member[getDimension()];

            for (int i = 0; i < getDimension(); i++) {
                array[i] = getPrimitiveElement(i);
            }

            return new RingVector(array);
        } else {
            throw new IllegalDimensionException(
                "Requested element out of bounds.");
        }
    }

    /**
     * Sets the value of an element of the vector.
     *
     * @param i row index of the element
     * @param r a ring element
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         element.
     */
    public void setElement(final int i, final Ring.Member r) {
        if ((i >= 0) && (i < getDimension())) {
            vector[i] = r;
        } else {
            throw new IllegalDimensionException(getInvalidElementMsg(i));
        }
    }

    /**
     * Sets the value of all elements of the vector.
     *
     * @param r a ring element
     */
    public void setAllElements(final Ring.Member r) {
        for (int i = 0; i < getDimension(); i++) {
            vector[i] = r;
        }
    }

    //============
    // OPERATIONS
    //============
    /**
     * Returns the negative of this vector.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member negate() {
        final Ring.Member[] array = new Ring.Member[getDimension()];

        for (int i = 0; i < getDimension(); i++) {
            array[i] = (Ring.Member) vector[i].negate();
        }

        return new RingVector(array);
    }

    // ADDITION
    /**
     * Returns the addition of this vector and another.
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member add(final AbelianGroup.Member m) {
        if (m instanceof RingVector) {
            return add((RingVector) m);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param v a vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public RingVector add(final RingVector v) {
        if (getDimension() == v.getDimension()) {
            final Ring.Member[] array = new Ring.Member[getDimension()];

            for (int i = 0; i < getDimension(); i++) {
                array[i] = (Ring.Member) vector[i].add(v.getPrimitiveElement(i));
            }

            return new RingVector(array);
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    // SUBTRACTION
    /**
     * Returns the subtraction of this vector and another.
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member m) {
        if (m instanceof RingVector) {
            return subtract((RingVector) m);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this vector and another.
     *
     * @param v a vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public RingVector subtract(final RingVector v) {
        if (getDimension() == v.getDimension()) {
            final Ring.Member[] array = new Ring.Member[getDimension()];

            for (int i = 0; i < getDimension(); i++) {
                array[i] = (Ring.Member) vector[i].subtract(v.getPrimitiveElement(
                            i));
            }

            return new RingVector(array);
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Ring.Member multiply(final Ring.Member r) {
        if (r instanceof Matrix) {
            if (r instanceof IntegerMatrix) {
                IntegerMatrix other = (IntegerMatrix) r;

                if (other.numRows() == 1) {
                    final Ring.Member[][] array = new Ring.Member[numRows()][other.numColumns()];

                    for (int j = 0; j < numRows(); j++) {
                        for (int k = 0; k < other.numColumns(); k++) {
                            array[j][k] = getPrimitiveElement(j)
                                              .multiply(other.getElement(0, k));
                        }
                    }

                    return new RingMatrix(array);
                } else {
                    throw new IllegalDimensionException(
                        "Cannot multiply a Vector by a Matrix with more than one row.");
                }
            } else if (r instanceof DoubleMatrix) {
                DoubleMatrix other = (DoubleMatrix) r;

                if (other.numRows() == 1) {
                    final Ring.Member[][] array = new Ring.Member[numRows()][other.numColumns()];

                    for (int j = 0; j < numRows(); j++) {
                        for (int k = 0; k < other.numColumns(); k++) {
                            array[j][k] = getPrimitiveElement(j)
                                              .multiply(other.getElement(0, k));
                        }
                    }

                    return new RingMatrix(array);
                } else {
                    throw new IllegalDimensionException(
                        "Cannot multiply a Vector by a Matrix with more than one row.");
                }
            } else if (r instanceof ComplexMatrix) {
                ComplexMatrix other = (ComplexMatrix) r;

                if (other.numRows() == 1) {
                    final Ring.Member[][] array = new Ring.Member[numRows()][other.numColumns()];

                    for (int j = 0; j < numRows(); j++) {
                        for (int k = 0; k < other.numColumns(); k++) {
                            array[j][k] = other.getPrimitiveElement(0, k)
                                               .multiply(getPrimitiveElement(j));
                        }
                    }

                    return new RingMatrix(array);
                } else {
                    throw new IllegalDimensionException(
                        "Cannot multiply a Vector by a Matrix with more than one row.");
                }
            } else if (r instanceof RingMatrix) {
                RingMatrix other = (RingMatrix) r;

                if (other.numRows() == 1) {
                    final Ring.Member[][] array = new Ring.Member[numRows()][other.numColumns()];

                    for (int j = 0; j < numRows(); j++) {
                        for (int k = 0; k < other.numColumns(); k++) {
                            array[j][k] = other.getPrimitiveElement(0, k)
                                               .multiply(getPrimitiveElement(j));
                        }
                    }

                    return new RingMatrix(array);
                } else {
                    throw new IllegalDimensionException(
                        "Cannot multiply a Vector by a Matrix with more than one row.");
                }
            } else {
                throw new IllegalDimensionException(
                    "Member not recognized by this method.");
            }
        } else {
            throw new IllegalArgumentException(
                "Member not recognized by this method.");
        }
    }

    // SCALAR MULTIPLICATION
    /**
     * Returns the multiplication of this vector by a scalar.
     *
     * @param r a ring element.
     *
     * @return DOCUMENT ME!
     */
    public Module.Member scalarMultiply(final Ring.Member r) {
        final Ring.Member[] array = new Ring.Member[getDimension()];

        for (int i = 0; i < getDimension(); i++) {
            array[i] = r.multiply(vector[i]);
        }

        return new RingVector(array);
    }

    // SCALAR DIVISON
    /**
     * Returns the division of this vector by a scalar.
     *
     * @param x a field element.
     *
     * @return DOCUMENT ME!
     */
    public VectorSpace.Member scalarDivide(final Field.Member x) {
        final Ring.Member[] array = new Ring.Member[getDimension()];

        for (int i = 0; i < getDimension(); i++) {
            array[i] = ((Field.Member) vector[i]).divide(x);
        }

        return new RingVector(array);
    }

    // TENSOR PRODUCT
    /**
     * Returns the tensor product of this vector and another.
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //can I do that ???
    public RingVector tensorProduct(final RingVector m) {
        final Ring.Member[] array = new Ring.Member[getDimension() * m.getDimension()];

        for (int i = 0; i < getDimension(); i++) {
            for (int k = 0; k < m.getDimension(); k++) {
                array[(i * m.getDimension()) + k] = vector[i].multiply(m.getPrimitiveElement(
                            k));
            }
        }

        return new RingVector(array);
    }

    //TRANSPOSE
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RingMatrix transpose() {
        Ring.Member[][] array = new Ring.Member[getDimension()][1];

        for (int i = 0; i < getDimension(); i++) {
            array[i][0] = getPrimitiveElement(i);
        }

        return new RingMatrix(array);
    }

    // MAP COMPONENTS
    /**
     * Applies a function on all the vector components.
     *
     * @param f a user-defined function.
     *
     * @return a Number vector.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public RingVector mapElements(final NumberMapping f) {
        if ((getDimension() > 0) && (vector[0] instanceof Number)) {
            final Ring.Member[] array = new Ring.Member[getDimension()];
            array[0] = (Ring.Member) f.map((Number) vector[0]);

            for (int i = 1; i < getDimension(); i++)
                array[i] = (Ring.Member) f.map((Number) vector[i]);

            return new RingVector(array);
        } else {
            throw new IllegalArgumentException(
                "Vector elements don't implement subclass Number.");
        }
    }

    /**
     * Projects the vector to an array.
     *
     * @return an double array.
     */
    public Ring.Member[] toPrimitiveArray() {
        final Ring.Member[] result = new Ring.Member[getDimension()];

        for (int i = 0; i < getDimension(); i++)
            result[i] = vector[i];

        return result;
    }

    /**
     * Clone vector into a new vector.
     *
     * @return the cloned vector.
     */
    public Object clone() {
        return new RingVector(this);
    }
}
