/*
 * Vector3D.java
 *
 * Created on April 19, 2004, 9:50 AM
 */
package org.jscience.chemistry.quantum.math.vector;

import org.jscience.chemistry.quantum.math.util.Point3D;


/**
 * A special class representing R^3 vector. This class is purposely not
 * derived from VectorND class, use toVectorND() method get an instance
 * compliant with VectorND.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class Vector3D {
    /** Null vector in 3 dimensional space */
    public final static Vector3D NULL_VECTOR = new Vector3D(0.0, 0.0, 0.0);

    /** Unit vector along X in 3 dimensional space */
    public final static Vector3D UX = new Vector3D(1.0, 0.0, 0.0);

    /** Unit vector along Y in 3 dimensional space */
    public final static Vector3D UY = new Vector3D(0.0, 1.0, 0.0);

    /** Unit vector along Z in 3 dimensional space */
    public final static Vector3D UZ = new Vector3D(0.0, 0.0, 1.0);

    /** the componanats of this vector */
    private double i;

    /** the componanats of this vector */
    private double j;

    /** the componanats of this vector */
    private double k;

    /** hash code cached here */
    private volatile int hashCode = 0;

/**
     * Creates a new instance of Vector3D
     */
    public Vector3D() {
        i = 0.0;
        j = 0.0;
        k = 0.0;
    }

/**
     * Creates a new Vector3D object.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public Vector3D(double i, double j, double k) {
        this.i = i;
        this.j = j;
        this.k = k;
    }

/**
     * Creates a new Vector3D object.
     *
     * @param point DOCUMENT ME!
     */
    public Vector3D(Point3D point) {
        this(point.getX(), point.getY(), point.getZ());
    }

    /**
     * addition of two vectors
     *
     * @param b : to be added to the current vector
     *
     * @return the addition of two vector
     */
    public Vector3D add(Vector3D b) {
        Vector3D result = new Vector3D();

        result.i = this.i + b.i;
        result.j = this.j + b.j;
        result.k = this.k + b.k;

        return result;
    }

    /**
     * multiplication of this vector with a scalar k
     *
     * @param k k : the scalar to be multiplied to this vector
     *
     * @return the result !
     */
    public Vector3D mul(double k) {
        Vector3D result = new Vector3D();

        result.i = this.i * k;
        result.j = this.j * k;
        result.k = this.k * k;

        return result;
    }

    /**
     * substraction of two vectors (this - b)
     *
     * @param b : to be substracted from the current vector
     *
     * @return the substraction of two vector
     */
    public Vector3D sub(Vector3D b) {
        Vector3D result = new Vector3D();

        result.i = this.i - b.i;
        result.j = this.j - b.j;
        result.k = this.k - b.k;

        return result;
    }

    /**
     * The dot product of two vectors (a.b)
     *
     * @param b vector with which the dot product is to be evaluated
     *
     * @return a double value which is the result of the dot product
     */
    public double dot(Vector3D b) {
        double result = 0.0;

        result += (this.i * b.i);
        result += (this.j * b.j);
        result += (this.k * b.k);

        return result;
    }

    /**
     * The cross product of two vectors (a.b). The corss product is
     * only valid if both the vectors are in R^3 space.
     *
     * @param b vector with which the cross product is to be evaluated
     *
     * @return the result of the cross product
     */
    public Vector3D cross(Vector3D b) {
        Vector3D result = new Vector3D();

        result.i = (j * b.k) - (k * b.j);
        result.j = (k * b.i) - (i * b.k);
        result.k = (i * b.j) - (j * b.i);

        return result;
    }

    /**
     * The magnitude of this vector
     *
     * @return the magnitude (length) of this vector.
     */
    public double magnitude() {
        double length;

        length = 0.0;

        length += (i * i);
        length += (j * j);
        length += (k * k);

        return Math.sqrt(length);
    }

    /**
     * Find the angle made with the vector.
     *
     * @param b DOCUMENT ME!
     *
     * @return the angle in radians
     */
    public double angleWith(Vector3D b) {
        double aDotb = this.dot(b);
        double ab = magnitude() * b.magnitude();

        return Math.acos(aDotb / ab);
    }

    /**
     * get the normalized form of this vector
     *
     * @return the normalized form of this vector
     */
    public Vector3D normalize() {
        double magnitude = magnitude();

        Vector3D n = new Vector3D();

        n.i = i / magnitude;
        n.j = j / magnitude;
        n.k = k / magnitude;

        return n;
    }

    /**
     * negate this vector
     *
     * @return the reverse vector
     */
    public Vector3D negate() {
        double magnitude = magnitude();

        Vector3D n = new Vector3D();

        n.i = -i;
        n.j = -j;
        n.k = -k;

        return n;
    }

    /**
     * clone this vector ;) Cloning is getting interesting! :)
     *
     * @return the clone
     */
    public Object clone() {
        Vector3D theCopy = new Vector3D();

        theCopy.i = i;
        theCopy.j = j;
        theCopy.k = k;

        return theCopy;
    }

    /**
     * method to convert this object to one representing a general
     * Vector
     *
     * @return Vector the converted form
     */
    public VectorND toVectorND() {
        return new VectorND(new double[] { i, j, k });
    }

    /**
     * the overridden toString() method
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(i + " i + ");
        sb.append(j + " j + ");
        sb.append(k + " k ");

        return sb.toString();
    }

    /**
     * overloaded equals() method
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (!(obj instanceof Vector3D))) {
            return false;
        } else {
            Vector3D o = (Vector3D) obj;

            return ((i == o.i) && (j == o.j) && (k == o.k));
        } // end if
    }

    /**
     * overriden hashCode() method
     *
     * @return int - the hashCode
     */
    public int hashCode() {
        if (hashCode == 0) {
            int result = 17; // prime number!
            long c;

            c = Double.doubleToLongBits(i);
            result = (37 * result) + (int) (c ^ (c >>> 32));

            c = Double.doubleToLongBits(j);
            result = (37 * result) + (int) (c ^ (c >>> 32));

            c = Double.doubleToLongBits(k);
            result = (37 * result) + (int) (c ^ (c >>> 32));

            hashCode = result;
        } // end if

        return hashCode;
    }

    /**
     * Getter for property i.
     *
     * @return Value of property i.
     */
    public double getI() {
        return this.i;
    }

    /**
     * Setter for property i.
     *
     * @param i New value of property i.
     */
    public void setI(double i) {
        this.i = i;
    }

    /**
     * Getter for property j.
     *
     * @return Value of property j.
     */
    public double getJ() {
        return this.j;
    }

    /**
     * Setter for property j.
     *
     * @param j New value of property j.
     */
    public void setJ(double j) {
        this.j = j;
    }

    /**
     * Getter for property k.
     *
     * @return Value of property k.
     */
    public double getK() {
        return this.k;
    }

    /**
     * Setter for property k.
     *
     * @param k New value of property k.
     */
    public void setK(double k) {
        this.k = k;
    }
} // end of class Vector3D
