package org.jscience.mathematics.algebraic.matrices;

import org.jscience.JScience;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.modules.VectorSpace;
import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.algebraic.numbers.Integer;
import org.jscience.mathematics.analysis.PrimitiveMapping;

import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;


/**
 * An optimised implementation of a 3D double vector.
 *
 * @author Mark Hale
 * @version 2.0
 */
public class Double3Vector extends AbstractDoubleVector implements Cloneable,
    Serializable {
    //quick access convenient public modifier
    /** DOCUMENT ME! */
    public double x;

    /** DOCUMENT ME! */
    public double y;

    /** DOCUMENT ME! */
    public double z;

/**
     * Constructs an empty 3-vector.
     */
    public Double3Vector() {
        super(3);
    }

/**
     * Constructs a 3-vector.
     *
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
    public Double3Vector(final double x, final double y, final double z) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
    }

/**
     * Constructs a 3-vector.
     *
     * @param array DOCUMENT ME!
     */
    public Double3Vector(final double[] array) {
        this();

        if (array.length == 3) {
            x = array[0];
            y = array[1];
            z = array[2];
        } else {
            throw new IllegalArgumentException("Invalid array size.");
        }
    }

/**
     * Constructs a 3-vector.
     *
     * @param vec DOCUMENT ME!
     */
    public Double3Vector(final Double3Vector vec) {
        this();
        x = vec.x;
        y = vec.y;
        z = vec.z;
    }

    /**
     * Compares two double vectors for equality.
     *
     * @param obj a double 3-vector
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Double3Vector) {
                final Double3Vector vec = (Double3Vector) obj;

                return ((Math.abs(x - vec.x) <= java.lang.Double.valueOf(JScience.getProperty(
                        "tolerance")).doubleValue()) &&
                (Math.abs(y - vec.y) <= java.lang.Double.valueOf(JScience.getProperty(
                        "tolerance")).doubleValue()) &&
                (Math.abs(z - vec.z) <= java.lang.Double.valueOf(JScience.getProperty(
                        "tolerance")).doubleValue()));
            } else {
                if ((obj instanceof AbstractDoubleVector)) {
                    AbstractDoubleVector vect = (AbstractDoubleVector) obj;

                    return ((vect.getDimension() == 3) &&
                    (Math.abs(x - vect.getPrimitiveElement(0)) <= java.lang.Double.valueOf(JScience.getProperty(
                            "tolerance")).doubleValue()) &&
                    (Math.abs(y - vect.getPrimitiveElement(1)) <= java.lang.Double.valueOf(JScience.getProperty(
                            "tolerance")).doubleValue()) &&
                    (Math.abs(z - vect.getPrimitiveElement(2)) <= java.lang.Double.valueOf(JScience.getProperty(
                            "tolerance")).doubleValue()));
                } else {
                    return false;
                }
            }
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
        final StringBuffer buf = new StringBuffer(15);
        buf.append(x).append(',').append(y).append(',').append(z);

        return buf.toString();
    }

    /**
     * Converts this 3-vector to an integer 3-vector.
     *
     * @return an integer 3-vector
     */
    public AbstractIntegerVector toIntegerVector() {
        return new Integer3Vector(Math.round((float) x), Math.round((float) y),
            Math.round((float) z));
    }

    /**
     * Converts this 3-vector to a complex 3-vector.
     *
     * @return a complex 3-vector
     */
    public AbstractComplexVector toComplexVector() {
        return new Complex3Vector(x, 0.0, y, 0.0, z, 0.0);
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
    public double getPrimitiveElement(final int n) {
        switch (n) {
        case 0:
            return x;

        case 1:
            return y;

        case 2:
            return z;

        default:
            throw new IllegalDimensionException("Invalid component.");
        }
    }

    /**
     * Sets the value of a component of this vector. Should only be
     * used to initialise this vector.
     *
     * @param n index of the vector component
     * @param value a number
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         component.
     */
    public void setElement(final int n, final double value) {
        switch (n) {
        case 0:
            x = value;

            break;

        case 1:
            y = value;

            break;

        case 2:
            z = value;

            break;

        default:
            throw new IllegalDimensionException("Invalid component.");
        }
    }

    /**
     * Sets the value of all elements of the vector.
     *
     * @param r a ring element
     */
    public void setAllElements(final double r) {
        x = r;
        y = r;
        z = r;
    }

    /**
     * Returns the l<sup>n</sup>-norm.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double norm(final int n) {
        final double answer = Math.pow(Math.abs(x), n) +
            Math.pow(Math.abs(y), n) + Math.pow(Math.abs(z), n);

        return Math.pow(answer, 1.0 / n);
    }

    /**
     * Returns the l<sup>2</sup>-norm (magnitude).
     *
     * @return DOCUMENT ME!
     */
    public double norm() {
        return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    /**
     * DOCUMENT ME!
     */
    public void normalize() {
        normalize(1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void normalize(final double n) {
        double norm = norm();

        x = (x * n) / norm;
        y = (y * n) / norm;
        z = (z * n) / norm;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getX() {
        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getY() {
        return y;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getZ() {
        return z;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void setX(final double d) {
        x = d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void setY(final double d) {
        y = d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void setZ(final double d) {
        z = d;
    }

    /**
     * Returns the l<sup><img border=0 alt="infinity"
     * src="doc-files/infinity.gif"></sup>-norm.
     *
     * @return DOCUMENT ME!
     */
    public double infNorm() {
        double infNorm = 0;
        double abs;
        abs = Math.abs(x);

        if (abs > infNorm) {
            infNorm = abs;
        }

        abs = Math.abs(y);

        if (abs > infNorm) {
            infNorm = abs;
        }

        abs = Math.abs(z);

        if (abs > infNorm) {
            infNorm = abs;
        }

        return infNorm;
    }

    /**
     * Returns the sum of the squares of the components.
     *
     * @return DOCUMENT ME!
     */
    public double sumSquares() {
        return (x * x) + (y * y) + (z * z);
    }

    /**
     * Returns the mass.
     *
     * @return DOCUMENT ME!
     */
    public double mass() {
        return x + y + z;
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
        return new Double3Vector(-x, -y, -z);
    }

    // ADDITION
    /**
     * Returns the addition of this vector and another.
     *
     * @param vec DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member add(final AbelianGroup.Member vec) {
        if (vec instanceof AbstractDoubleVector) {
            return add((AbstractDoubleVector) vec);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param vec a double 3-vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractDoubleVector add(final AbstractDoubleVector vec) {
        if (vec.getDimension() == 3) {
            return new Double3Vector(x + vec.getPrimitiveElement(0),
                y + vec.getPrimitiveElement(1), z + vec.getPrimitiveElement(2));
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    // SUBTRACTION
    /**
     * Returns the subtraction of this vector by another.
     *
     * @param vec DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member vec) {
        if (vec instanceof AbstractDoubleVector) {
            return subtract((AbstractDoubleVector) vec);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param vec a double 3-vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractDoubleVector subtract(final AbstractDoubleVector vec) {
        if (vec.getDimension() == 3) {
            return new Double3Vector(x - vec.getPrimitiveElement(0),
                y - vec.getPrimitiveElement(1), z - vec.getPrimitiveElement(2));
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
        } else if (x instanceof Double) {
            return scalarMultiply(((Double) x).value());
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the multiplication of this vector by a scalar.
     *
     * @param k a double
     *
     * @return a double 3-vector
     */
    public AbstractDoubleVector scalarMultiply(final double k) {
        return new Double3Vector(k * x, k * y, k * z);
    }

    // SCALAR DIVISION
    /**
     * Returns the division of this vector by a scalar.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public VectorSpace.Member scalarDivide(final Field.Member x) {
        if (x instanceof Double) {
            return scalarDivide(((Double) x).value());
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the division of this vector by a scalar.
     *
     * @param k a double
     *
     * @return a double 3-vector
     */
    public AbstractDoubleVector scalarDivide(final double k) {
        return new Double3Vector(x / k, y / k, z / k);
    }

    // SCALAR PRODUCT
    /**
     * Returns the scalar product of this vector and another.
     *
     * @param vec a double 3-vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public double scalarProduct(final AbstractDoubleVector vec) {
        if (vec.getDimension() == 3) {
            return (x * vec.getPrimitiveElement(0)) +
            (y * vec.getPrimitiveElement(1)) +
            (z * vec.getPrimitiveElement(2));
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    // VECTOR PRODUCT
    /**
     * Returns the vector product of this vector and another (so(3)
     * algebra).
     *
     * @param vec a double 3-vector
     *
     * @return DOCUMENT ME!
     */
    public Double3Vector multiply(final Double3Vector vec) {
        return new Double3Vector((y * vec.z) - (vec.y * z),
            (z * vec.x) - (vec.z * x), (x * vec.y) - (vec.x * y));
    }

    // MAP COMPONENTS
    /**
     * Applies a function on all the vector components.
     *
     * @param mapping a user-defined function
     *
     * @return a double 3-vector
     */
    public AbstractDoubleVector mapElements(final PrimitiveMapping mapping) {
        return new Double3Vector(mapping.map(x), mapping.map(y), mapping.map(z));
    }

    /**
     * Projects the vector to an array.
     *
     * @return an double array.
     */
    public double[] toPrimitiveArray() {
        final double[] result = new double[3];
        result[0] = x;
        result[1] = y;
        result[2] = z;

        return result;
    }

    /**
     * Clone vector into a new vector.
     *
     * @return the cloned vector.
     */
    public Object clone() {
        return new Double3Vector(this);
    }
}
