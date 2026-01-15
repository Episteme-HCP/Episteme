/*
 * Point3D.java
 *
 * Created on August 12, 2004, 4:27 PM
 */
package org.jscience.chemistry.quantum.math.util;

/**
 * A class representing a point in 3D space.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class Point3D implements Cloneable {
    /** Holds value of property x. */
    private double x;

    /** Holds value of property y. */
    private double y;

    /** Holds value of property z. */
    private double z;

    /** hash code cached here */
    private volatile int hashCode = 0;

/**
     * Creates a new instance of Point3D
     */
    public Point3D() {
        this(0.0, 0.0, 0.0);
    }

/**
     * Creates a new instance of Point3D
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     */
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Getter for property x.
     *
     * @return Value of property x.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Setter for property x.
     *
     * @param x New value of property x.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Getter for property y.
     *
     * @return Value of property y.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Setter for property y.
     *
     * @param y New value of property y.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Getter for property z.
     *
     * @return Value of property z.
     */
    public double getZ() {
        return this.z;
    }

    /**
     * Setter for property z.
     *
     * @param z New value of property z.
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * overloaded toString() method.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return x + " " + y + " " + z;
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

        if ((obj == null) || (!(obj instanceof Point3D))) {
            return false;
        } else {
            Point3D o = (Point3D) obj;

            return ((x == o.x) && (y == o.y) && (z == o.z));
        } // end if
    }

    /**
     * Simple method to find the distance between two points.
     *
     * @param point - the point to which the distance is to be found
     *
     * @return the distance between two points
     */
    public double distanceFrom(Point3D point) {
        double x = this.x - point.x;
        double y = this.y - point.y;
        double z = this.z - point.z;

        return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    /**
     * Simple method to find the distance<sup>2</sup> between two
     * points.
     *
     * @param point - the point to which the distance is to be found
     *
     * @return the distance between two points
     */
    public double distanceSquaredFrom(Point3D point) {
        double x = this.x - point.x;
        double y = this.y - point.y;
        double z = this.z - point.z;

        return ((x * x) + (y * y) + (z * z));
    }

    /**
     * addition of two points
     *
     * @param b : to be added to the current point
     *
     * @return the addition of two points
     */
    public Point3D add(Point3D b) {
        Point3D result = new Point3D();

        result.x = this.x + b.x;
        result.y = this.y + b.y;
        result.z = this.z + b.z;

        return result;
    }

    /**
     * multiplication of this point with a constant k
     *
     * @param k k : the constant to be multiplied to this point
     *
     * @return the result !
     */
    public Point3D mul(double k) {
        Point3D result = new Point3D();

        result.x = this.x * k;
        result.y = this.y * k;
        result.z = this.z * k;

        return result;
    }

    /**
     * substraction of two points (this - b)
     *
     * @param b : to be substracted from the current point
     *
     * @return the substraction of two point
     */
    public Point3D sub(Point3D b) {
        Point3D result = new Point3D();

        result.x = this.x - b.x;
        result.y = this.y - b.y;
        result.z = this.z - b.z;

        return result;
    }

    /**
     * i do some cloning business ;)
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        return new Point3D(this.x, this.y, this.z);
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

            c = Double.doubleToLongBits(x);
            result = (37 * result) + (int) (c ^ (c >>> 32));

            c = Double.doubleToLongBits(y);
            result = (37 * result) + (int) (c ^ (c >>> 32));

            c = Double.doubleToLongBits(z);
            result = (37 * result) + (int) (c ^ (c >>> 32));

            hashCode = result;
        } // end if

        return hashCode;
    }
} // end of class Point3D
