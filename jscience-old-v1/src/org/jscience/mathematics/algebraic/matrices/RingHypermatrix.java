package org.jscience.mathematics.algebraic.matrices;

import org.jscience.mathematics.algebraic.AbstractHypermatrix;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.modules.VectorSpace;
import org.jscience.mathematics.analysis.NumberMapping;

import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.Arrays;
import java.util.Vector;


/**
 * The RingHypermatrix class provides an implementation for extended
 * matrices.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//although it would be nice if RingHypermatrix was the superclass of all other matrixes,
//we decided that it wouldn't be so as it adds complexity to the design and possible performance penality
//yet it would be quite simple to change with current implementation as children class override all the methods of RingHypermatrix.

//possible methods we could add:
//hashCode, getSubDimensionRingHypermatrix
public class RingHypermatrix extends AbstractHypermatrix implements Cloneable,
    Serializable {
    /** The number of elements for each of the dimensions. */
    private final Object store;

/**
     * Constructs a RingHypermatrix.
     *
     * @param dimensions DOCUMENT ME!
     */
    public RingHypermatrix(final int[] dimensions) {
        super(dimensions);
        store = Array.newInstance(Ring.Member.class, getDimensions());
    }

/**
     * @ param array the multidimensional array of Ring.Member values
     */
    public RingHypermatrix(final RingHypermatrix matrix) {
        super(matrix.getDimensions());
        store = Array.newInstance(Ring.Member.class, getDimensions());
        copyElements((Object[]) matrix.toArray(), (Object[]) store,
            getDimensions());
    }

/**
     * Constructs an hypermatrix.
     *
     * @ param array the multidimensional array of Ring.Member values, which must be a rectangular multi array of positive dimensions.
     */
    public RingHypermatrix(final Object array) {
        super(getDimensionsArray(array));

        if (Ring.Member.class.isAssignableFrom(getArrayClass(array))) {
            store = Array.newInstance(Ring.Member.class, getDimensions());
            copyElements((Object[]) array, (Object[]) store, getDimensions());
        } else {
            throw new IllegalArgumentException(
                "Array must consist of Ring.Member elements.");
        }
    }

/**
     * Constructs an hypermatrix.
     *
     * @ param elements is a single dimension array of Ring.Member values, that contains numElements(). Elements are fetched "row first".
     */
    public RingHypermatrix(final int[] dimensions, final Object[] elements) {
        super(dimensions);
        store = java.lang.reflect.Array.newInstance(Ring.Member.class,
                dimensions);
        fromArray(elements, (Object[]) store, getDimensions(), 0);
    }

    //dimensions.length>0
    /**
     * DOCUMENT ME!
     *
     * @param sourceArray DOCUMENT ME!
     * @param resultArray DOCUMENT ME!
     * @param dimensions DOCUMENT ME!
     * @param startingElement DOCUMENT ME!
     */
    private void fromArray(Object[] sourceArray, Object[] resultArray,
        int[] dimensions, int startingElement) {
        if (dimensions.length > 1) {
            int[] resultDimensions = new int[dimensions.length - 1];
            int product = 1;

            for (int j = 0; j < (dimensions.length - 1); j++) {
                resultDimensions[j] = dimensions[j];
                product = dimensions[j] * product;
            }

            for (int i = 0; i < dimensions[dimensions.length - 1]; i++) {
                fromArray(sourceArray, (Object[]) resultArray[i],
                    resultDimensions, startingElement + (product * i));
            }
        } else {
            System.arraycopy(sourceArray, startingElement, resultArray, 0,
                resultArray.length);
        }
    }

    //dimensions.length>0
    /**
     * DOCUMENT ME!
     *
     * @param sourceArray DOCUMENT ME!
     * @param resultArray DOCUMENT ME!
     * @param dimensions DOCUMENT ME!
     * @param startingElement DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Ring.Member[] toArray(Object[] sourceArray, Object[] resultArray,
        int[] dimensions, int startingElement) {
        if (dimensions.length > 1) {
            int[] resultDimensions = new int[dimensions.length - 1];
            int product = 1;

            for (int j = 0; j < (dimensions.length - 1); j++) {
                resultDimensions[j] = dimensions[j];
                product = dimensions[j] * product;
            }

            for (int i = 0; i < dimensions[dimensions.length - 1]; i++) {
                toArray((Object[]) sourceArray[i], resultArray,
                    resultDimensions, startingElement + (product * i));
            }
        } else {
            System.arraycopy(sourceArray, 0, resultArray, startingElement,
                sourceArray.length);
        }

        return (Ring.Member[]) resultArray;
    }

    //dimensions.length>0
    /**
     * DOCUMENT ME!
     *
     * @param sourceArray DOCUMENT ME!
     * @param resultArray DOCUMENT ME!
     * @param dimensions DOCUMENT ME!
     */
    private void copyElements(Object[] sourceArray, Object[] resultArray,
        int[] dimensions) {
        if (dimensions.length > 1) {
            int[] resultDimensions = new int[dimensions.length - 1];

            for (int j = 0; j < (dimensions.length - 1); j++) {
                resultDimensions[j] = dimensions[j];
            }

            for (int i = 0; i < dimensions[dimensions.length - 1]; i++) {
                copyElements((Object[]) sourceArray[i],
                    (Object[]) resultArray[i], resultDimensions);
            }
        } else {
            System.arraycopy(sourceArray, 0, resultArray, 0, sourceArray.length);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int numDimensions(Object object) {
        if (object == null) {
            return 0;
        }

        int d = 0;
        Class cls = object.getClass();

        while (cls.isArray()) {
            d++;
            cls = cls.getComponentType();
        }

        return d;
    }

    //will an error be thrown if the array if not rectangular ?
    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int[] getDimensionsArray(Object object) {
        int[] result;

        if (object == null) {
            result = new int[0];
        } else {
            Vector v = new Vector();
            Class cls = object.getClass();

            while (cls.isArray()) {
                v.add(new Integer(((Object[]) object).length));
                cls = cls.getComponentType();
            }

            result = new int[v.size()];

            for (int i = 0; i < result.length; i++) {
                result[i] = ((Integer) v.elementAt(i)).intValue();
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Class getArrayClass(Object object) {
        Class result;

        if (object == null) {
            result = null;
        } else {
            Class cls = object.getClass();
            result = null;

            while (cls.isArray()) {
                result = cls;
                cls = cls.getComponentType();
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void setAllElements(final Ring.Member m) {
        setAllElements(store, m, getDimensions());
    }

    /**
     * DOCUMENT ME!
     *
     * @param array DOCUMENT ME!
     * @param m DOCUMENT ME!
     * @param dimensions DOCUMENT ME!
     */
    private void setAllElements(Object array, Ring.Member m, int[] dimensions) {
        if (dimensions.length > 1) {
            int[] resultDimensions = new int[dimensions.length - 1];

            for (int j = 0; j < (dimensions.length - 1); j++) {
                resultDimensions[j] = dimensions[j];
            }

            for (int i = 0; i < dimensions[dimensions.length - 1]; i++) {
                setAllElements(((Object[]) array)[i], m, resultDimensions);
            }
        } else {
            Arrays.fill((Object[]) array, m);
        }
    }

    /**
     * Returns the element at position given by the array of int. Each
     * element of the array must be between 0 and getDimension(i)-1.
     *
     * @param position DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Number getElement(final int[] position)
        throws IllegalDimensionException {
        int i;
        boolean result;
        Object value;

        i = 0;
        result = (numDimensions() == position.length);

        if (result) {
            while (result && (i < numDimensions())) {
                result = (position[i] >= 0) &&
                    (position[i] < getDimensions()[i]);
                i++;
            }

            if (result) {
                value = store;

                for (i = (numDimensions() - 1); i > -1; i--) {
                    //value = java.lang.reflect.Array.get(value, position[i]);
                    value = ((Object[]) value)[position[i]];
                }

                return (Number) value;
            } else {
                throw new IllegalDimensionException(
                    "Requested element out of bounds.");
            }
        } else {
            throw new IllegalArgumentException(
                "Array of positions hasn't matching size with dimensions.");
        }
    }

    /**
     * Sets the element at position given by the array of int. Each
     * element of the array must be between 0 and getDimension(i)-1.
     *
     * @param position DOCUMENT ME!
     * @param m DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setElement(final int[] position, final Ring.Member m)
        throws IllegalDimensionException {
        int i;
        boolean result;
        Object value;

        if (m != null) {
            i = 0;
            result = (getDimensions().length == position.length);

            if (result) {
                while (result && (i < getDimensions().length)) {
                    result = (position[i] >= 0) &&
                        (position[i] < getDimensions()[i]);
                    i++;
                }

                if (result) {
                    value = store;

                    for (i = (getDimensions().length - 1); i > 0; i--) {
                        //value = java.lang.reflect.Array.get(value, position[i]);
                        value = ((Object[]) value)[position[i]];
                    }

                    //java.lang.reflect.Array.set(value, position[0], m);
                    ((Object[]) value)[position[0]] = m;
                } else {
                    throw new IllegalDimensionException(
                        "Requested element out of bounds.");
                }
            } else {
                throw new IllegalArgumentException(
                    "Array of positions hasn't matching size with dimensions.");
            }
        } else {
            throw new IllegalArgumentException(
                "You cannot set an Hypermatrix element with a null value.");
        }
    }

    /**
     * Compares two hypermatrices for equality.
     *
     * @param m an hypermatrix
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object m) {
        if ((m != null) && (m instanceof RingHypermatrix)) {
            RingHypermatrix mat = (RingHypermatrix) m;

            if (numDimensions() == mat.numDimensions()) {
                int i = 0;
                boolean result = true;

                while ((i < numDimensions()) && result) {
                    result = (getDimensions()[i] == mat.getDimensions()[i]);
                    i++;
                }

                if (result) {
                    return Arrays.equals(toFlatArray(), mat.toFlatArray());
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Returns a hashcode for this NON EMPTY matrix.
     *
     * @return DOCUMENT ME!
     */

    //public int hashCode() {
    //  return (int) Math.exp(infNorm());
    //}
    /**
     * Returns a string representing this matrix.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        final StringBuffer buf = new StringBuffer(5 * numElements());
        Object[] flatArray;

        flatArray = toFlatArray();

        for (int i = 0; i < numElements(); i++) {
            buf.append(flatArray[i].toString());
            buf.append(' ');
        }

        buf.append('\n');

        return buf.toString();
    }

    /**
     * Returns a projection of this hypermatrix.
     *
     * @return DOCUMENT ME!
     */
    public Object getElements() {
        return store;
    }

    /**
     * Returns a flat array (that is a one dimension array) of all
     * elements given "row first", given "row first". Useful to iterate over
     * all the matrix elements.
     *
     * @return DOCUMENT ME!
     */
    public Ring.Member[] toFlatArray() {
        Ring.Member[] result;

        result = new Ring.Member[numElements()];

        return toArray((Object[]) store, result, getDimensions(), 0);
    }

    //============
    // OPERATIONS
    //============
    /**
     * Returns the negative of this matrix.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member negate() {
        final Ring.Member[] flatArray = toFlatArray();

        for (int i = 0; i < flatArray.length; i++) {
            flatArray[i] = (Ring.Member) flatArray[i].negate();
        }

        return new RingHypermatrix(getDimensions(), flatArray);
    }

    // ADDITION
    /**
     * Returns the addition of this matrix and another.
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member add(final AbelianGroup.Member m) {
        if (m instanceof RingHypermatrix) {
            return add((RingHypermatrix) m);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public RingHypermatrix add(final RingHypermatrix m) {
        if (numDimensions() == m.numDimensions()) {
            int i = 0;
            boolean result = true;

            while ((i < numDimensions()) && result) {
                result = (getDimensions()[i] == m.getDimensions()[i]);
                i++;
            }

            if (result) {
                Ring.Member[] array1 = toFlatArray();
                Ring.Member[] array2 = m.toFlatArray();

                for (i = 0; i < array1.length; i++) {
                    array1[i] = (Ring.Member) array1[i].add(array2[i]);
                }

                return new RingHypermatrix(getDimensions(), array1);
            } else {
                throw new IllegalDimensionException(
                    "Matrices are different sizes.");
            }
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    // SUBTRACTION
    /**
     * Returns the subtraction of this matrix and another.
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member m) {
        if (m instanceof RingHypermatrix) {
            return subtract((RingHypermatrix) m);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this matrix and another.
     *
     * @param m a matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public RingHypermatrix subtract(final RingHypermatrix m) {
        if (numDimensions() == m.numDimensions()) {
            int i = 0;
            boolean result = true;

            while ((i < numDimensions()) && result) {
                result = (getDimensions()[i] == m.getDimensions()[i]);
                i++;
            }

            if (result) {
                Ring.Member[] array1 = toFlatArray();
                Ring.Member[] array2 = m.toFlatArray();

                for (i = 0; i < array1.length; i++) {
                    array1[i] = (Ring.Member) array1[i].subtract(array2[i]);
                }

                return new RingHypermatrix(getDimensions(), array1);
            } else {
                throw new IllegalDimensionException(
                    "Matrices are different sizes.");
            }
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    // SCALAR MULTIPLICATION
    /**
     * Returns the multiplication of this matrix by a scalar.
     *
     * @param r a ring element.
     *
     * @return DOCUMENT ME!
     */
    public Module.Member scalarMultiply(final Ring.Member r) {
        Ring.Member[] array = toFlatArray();

        for (int i = 0; i < array.length; i++) {
            array[i] = r.multiply(array[i]);
        }

        return new RingHypermatrix(getDimensions(), array);
    }

    // SCALAR DIVISON
    /**
     * Returns the division of this matrix by a scalar.
     *
     * @param x a field element.
     *
     * @return DOCUMENT ME!
     */
    public VectorSpace.Member scalarDivide(final Field.Member x) {
        Ring.Member[] array = toFlatArray();

        for (int i = 0; i < array.length; i++) {
            array[i] = ((Field.Member) array[i]).divide(x);
        }

        return new RingHypermatrix(getDimensions(), array);
    }

    // MATRIX MULTIPLICATION
    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public Ring.Member multiply(final Ring.Member m) {
        //    if (m instanceof RingHypermatrix) {
        //        return multiply((RingHypermatrix) m);
        //    } else {
        //        throw new IllegalArgumentException("Member class not recognised by this method.");
        //    }
        throw new UnsupportedOperationException();
    }

    //I don't know the formula for hypermatrix multiplications
    //send it to us if you want it to be implemented
    //  public RingHypermatrix multiply(final RingHypermatrix m)

    // DIRECT SUM

    //I haven't provided a directSum support because, although the algorithm is quite simple
    //I haven't found any fast way to access the elements for this operation:
    //the user would face a useless method
    //public RingHypermatrix directSum(final RingHypermatrix m)

    //we could provide support for tensor although hypermatrix are already large arrays and we would probably get
    //memory exceptions when building any tensor
    /**
     * Applies a function on all the Hypermatrix components.
     *
     * @param f a user-defined function.
     *
     * @return a RingHypermatrix.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public RingHypermatrix mapElements(final NumberMapping f) {
        Ring.Member[] array = toFlatArray();

        if ((numElements() > 0) && (array[0] instanceof Number)) {
            for (int i = 0; i < array.length; i++) {
                array[i] = (Ring.Member) f.map((Number) array[i]);
            }

            return new RingHypermatrix(getDimensions(), array);
        } else {
            throw new IllegalArgumentException(
                "Vector elements don't implement subclass Number.");
        }
    }

    /**
     * Clone matrix into a new matrix.
     *
     * @return the cloned matrix.
     */
    public Object clone() {
        return new RingHypermatrix(this);
    }
}
