package org.jscience.mathematics.algebraic.matrices;

import org.jscience.mathematics.MathUtils;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.numbers.Integer;
import org.jscience.mathematics.analysis.PrimitiveMapping;

import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;


/**
 * An array-based implementation of an integer vector.
 *
 * @author Mark Hale
 * @version 2.1
 */
public class IntegerVector extends AbstractIntegerVector implements Cloneable,
    Serializable {
    /** Array containing the components of the vector. */
    protected int[] vector;

/**
     * Constructs an empty vector.
     *
     * @param dim the dimension of the vector.
     */
    public IntegerVector(final int dim) {
        super(dim);
        vector = new int[dim];
    }

/**
     * Constructs a vector by wrapping an array.
     *
     * @param array an assigned value
     */
    public IntegerVector(final int[] array) {
        super(array.length);
        vector = array;
    }

/**
     * Constructs a vector by copying a vector.
     *
     * @param vec an assigned value
     */
    public IntegerVector(final AbstractIntegerVector vec) {
        super(vec.getDimension());
        vector = new int[vec.getDimension()];

        if (vec instanceof IntegerVector) {
            IntegerVector vect = (IntegerVector) vec;
            System.arraycopy(vect.vector, 0, vector, 0, vec.getDimension());
        } else {
            for (int i = 0; i < vec.getDimension(); i++) {
                vector[i] = vec.getPrimitiveElement(i);
            }
        }
    }

    /**
     * Compares two Integer vectors for equality.
     *
     * @param a a Integer vector.
     * @param tol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object a, double tol) {
        if ((a != null) && (a instanceof AbstractIntegerVector) &&
                (getDimension() == ((AbstractIntegerVector) a).getDimension())) {
            final AbstractIntegerVector dv = (AbstractIntegerVector) a;
            double sumSqr = 0.0;

            for (int i = 0; i < getDimension(); i++) {
                double delta = vector[i] - dv.getPrimitiveElement(i);
                sumSqr += (delta * delta);
            }

            return (sumSqr <= (tol * tol));
        } else {
            return false;
        }
    }

    /**
     * Returns a comma delimited string representing the value of this
     * vector.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        final StringBuffer buf = new StringBuffer(getDimension());
        int i;

        for (i = 0; i < (getDimension() - 1); i++) {
            buf.append(vector[i]);
            buf.append(',');
        }

        buf.append(vector[i]);

        return buf.toString();
    }

    /**
     * Converts this vector to a double vector.
     *
     * @return a double vector
     */
    public AbstractDoubleVector toDoubleVector() {
        final double[] array = new double[getDimension()];

        for (int i = 0; i < getDimension(); i++)
            array[i] = vector[i];

        return new DoubleVector(array);
    }

    /**
     * Converts this vector to a complex vector.
     *
     * @return a complex vector
     */
    public AbstractComplexVector toComplexVector() {
        final double[] arrayRe = new double[getDimension()];

        for (int i = 0; i < getDimension(); i++)
            arrayRe[i] = vector[i];

        return new ComplexVector(arrayRe, new double[getDimension()]);
    }

    /**
     * Returns a component of this vector.
     *
     * @param n index of the vector component
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         component.
     */
    public int getPrimitiveElement(final int n) {
        if ((n >= 0) && (n < getDimension())) {
            return vector[n];
        } else {
            throw new IllegalDimensionException(getInvalidElementMsg(n));
        }
    }

    /**
     * Sets the value of a component of this vector.
     *
     * @param n index of the vector component
     * @param x an integer
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         component.
     */
    public void setElement(final int n, final int x) {
        if ((n >= 0) && (n < getDimension())) {
            vector[n] = x;
        } else {
            throw new IllegalDimensionException(getInvalidElementMsg(n));
        }
    }

    /**
     * Sets the value of all elements of the vector.
     *
     * @param r a int element
     */
    public void setAllElements(final int r) {
        for (int i = 0; i < getDimension(); i++) {
            vector[i] = r;
        }
    }

    /**
     * Returns the l<sup>n</sup>-norm.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException DOCUMENT ME!
     */
    public double norm(final int n) {
        if (getDimension() > 0) {
            double answer = Math.pow(Math.abs(vector[0]), n);

            for (int i = 1; i < getDimension(); i++)
                answer += Math.pow(Math.abs(vector[i]), n);

            return Math.pow(answer, 1.0 / n);
        } else {
            throw new ArithmeticException(
                "The norm of a zero dimension vector is undefined.");
        }
    }

    /**
     * Returns the l<sup>2</sup>-norm (magnitude).
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException DOCUMENT ME!
     */
    public double norm() {
        if (getDimension() > 0) {
            double answer = vector[0];

            for (int i = 1; i < getDimension(); i++)
                answer = MathUtils.hypot(answer, vector[i]);

            return answer;
        } else {
            throw new ArithmeticException(
                "The norm of a zero dimension vector is undefined.");
        }
    }

    /**
     * Returns the l<sup><img border=0 alt="infinity"
     * src="doc-files/infinity.gif"></sup>-norm.
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException DOCUMENT ME!
     */
    public int infNorm() {
        if (getDimension() > 0) {
            int infNorm = Math.abs(vector[0]);

            for (int i = 1; i < getDimension(); i++) {
                final int abs = Math.abs(vector[i]);

                if (abs > infNorm) {
                    infNorm = abs;
                }
            }

            return infNorm;
        } else {
            throw new ArithmeticException(
                "The inf norm of a zero dimension vector is undefined.");
        }
    }

    /**
     * Returns the sum of the squares of the components.
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException DOCUMENT ME!
     */
    public int sumSquares() {
        if (getDimension() > 0) {
            int norm = 0;

            for (int k = 0; k < vector.length; k++)
                norm += (vector[k] * vector[k]);

            return norm;
        } else {
            throw new ArithmeticException(
                "The sum of squares of a zero dimension vector is undefined.");
        }
    }

    /**
     * Returns the mass.
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException DOCUMENT ME!
     */
    public int mass() {
        if (getDimension() > 0) {
            int mass = 0;

            for (int k = 0; k < vector.length; k++)
                mass += vector[k];

            return mass;
        } else {
            throw new ArithmeticException(
                "The mass of a zero dimension vector is undefined.");
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
        final int[] array = new int[getDimension()];
        array[0] = -vector[0];

        for (int i = 1; i < getDimension(); i++)
            array[i] = -vector[i];

        return new IntegerVector(array);
    }

    // ADDITION
    /**
     * Returns the addition of this vector and another.
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member add(final AbelianGroup.Member v) {
        if (v instanceof IntegerVector) {
            return add((IntegerVector) v);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param v an integer vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public AbstractIntegerVector add(final AbstractIntegerVector v) {
        if (v instanceof IntegerVector) {
            return add((IntegerVector) v);
        } else {
            if (getDimension() == v.getDimension()) {
                final int[] array = new int[getDimension()];
                array[0] = vector[0] + v.getPrimitiveElement(0);

                for (int i = 1; i < getDimension(); i++)
                    array[i] = vector[i] + v.getPrimitiveElement(i);

                return new IntegerVector(array);
            } else {
                throw new IllegalDimensionException(
                    "Vectors are different sizes.");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public IntegerVector add(final IntegerVector v) {
        if (getDimension() == v.getDimension()) {
            final int[] array = new int[getDimension()];
            array[0] = vector[0] + v.vector[0];

            for (int i = 1; i < getDimension(); i++)
                array[i] = vector[i] + v.vector[i];

            return new IntegerVector(array);
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    // SUBTRACTION
    /**
     * Returns the subtraction of this vector by another.
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member v) {
        if (v instanceof IntegerVector) {
            return subtract((IntegerVector) v);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param v an integer vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public AbstractIntegerVector subtract(final AbstractIntegerVector v) {
        if (v instanceof IntegerVector) {
            return subtract((IntegerVector) v);
        } else {
            if (getDimension() == v.getDimension()) {
                final int[] array = new int[getDimension()];
                array[0] = vector[0] - v.getPrimitiveElement(0);

                for (int i = 1; i < getDimension(); i++)
                    array[i] = vector[i] - v.getPrimitiveElement(i);

                return new IntegerVector(array);
            } else {
                throw new IllegalDimensionException(
                    "Vectors are different sizes.");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public IntegerVector subtract(final IntegerVector v) {
        if (getDimension() == v.getDimension()) {
            final int[] array = new int[getDimension()];
            array[0] = vector[0] - v.vector[0];

            for (int i = 1; i < getDimension(); i++)
                array[i] = vector[i] - v.vector[i];

            return new IntegerVector(array);
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    // SCALAR MULTIPLICATION
    /**
     * Returns the multiplication of this vector by a scalar.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Module.Member scalarMultiply(final Ring.Member x) {
        if (x instanceof Integer) {
            return scalarMultiply(((Integer) x).value());
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the multiplication of this vector by a scalar.
     *
     * @param x an integer
     *
     * @return DOCUMENT ME!
     */
    public AbstractIntegerVector scalarMultiply(final int x) {
        final int[] array = new int[getDimension()];
        array[0] = x * vector[0];

        for (int i = 1; i < getDimension(); i++)
            array[i] = x * vector[i];

        return new IntegerVector(array);
    }

    // SCALAR PRODUCT
    /**
     * Returns the scalar product of this vector and another.
     *
     * @param v an integer vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public int scalarProduct(final AbstractIntegerVector v) {
        if (v instanceof IntegerVector) {
            return scalarProduct((IntegerVector) v);
        } else {
            if (getDimension() == v.getDimension()) {
                int answer = vector[0] * v.getPrimitiveElement(0);

                for (int i = 1; i < getDimension(); i++)
                    answer += (vector[i] * v.getPrimitiveElement(i));

                return answer;
            } else {
                throw new IllegalDimensionException(
                    "Vectors are different sizes.");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public int scalarProduct(final IntegerVector v) {
        if (getDimension() == v.getDimension()) {
            int answer = vector[0] * v.vector[0];

            for (int i = 1; i < getDimension(); i++)
                answer += (vector[i] * v.vector[i]);

            return answer;
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    // MAP COMPONENTS
    /**
     * Applies a function on all the vector components.
     *
     * @param f a user-defined function.
     *
     * @return a double vector.
     */
    public AbstractDoubleVector mapElements(final PrimitiveMapping f) {
        final double[] array = new double[getDimension()];
        array[0] = f.map(vector[0]);

        for (int i = 1; i < getDimension(); i++)
            array[i] = f.map(vector[i]);

        return new DoubleVector(array);
    }

    /**
     * Projects the vector to an array.
     *
     * @return an integer array.
     */
    public int[] toPrimitiveArray() {
        final int[] result = new int[getDimension()];
        System.arraycopy(vector, 0, result, 0, getDimension());

        return result;
    }

    /**
     * Clone vector into a new vector.
     *
     * @return the cloned vector.
     */
    public Object clone() {
        return new IntegerVector(this);
    }
}
