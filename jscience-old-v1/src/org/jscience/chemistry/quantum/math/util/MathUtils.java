/*
 * MathUtils.java
 *
 * Created on August 12, 2004, 4:24 PM
 */

package org.jscience.chemistry.quantum.math.util;

import org.jscience.chemistry.quantum.math.vector.Vector3D;

/**
 * A collection of few misc. utility math functions.
 * All methods are static and the class cannot be instantiated.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public final class MathUtils {

    /**
     * Creates a new instance of MathUtils
     */
    private MathUtils() {
    }

    /**
     * Method to convert radians to degrees
     *
     * @param radians - the value
     * @return the equivalent in degrees
     */
    public static double toDegrees(double radians) {
        return (radians * 180.0 / Math.PI);
    }

    /**
     * Method to convert degrees to radians
     *
     * @param degrees - the value
     * @return the equivalent in radians
     */
    public static double toRadians(double degrees) {
        return (degrees * Math.PI / 180.0);
    }

    /**
     * Return the sign of (1.0, 0.0, -1.0) of the given value.
     *
     * @param value the value of which whose sign we are interested
     * @return (1.0,0.0, -1.0) for value ( > , ==, <) zero respectively
     */
    public static double signOf(double value) {
        if (value > 0.0)
            return 1.0;
        else if (value < 0.0)
            return -1.0;
        else
            return 0.0;
    }

    /**
     * Method to find the angle in radians defined by three points
     * v1-v2-v3.
     *
     * @param v1, v2, v3 - the instances of Point3D class
     * @return the angle defined
     */
    public static double findAngle(Point3D v1, Point3D v2, Point3D v3) {
        Vector3D v12 = new Vector3D(v2.sub(v1));
        Vector3D v32 = new Vector3D(v2.sub(v3));

        return v12.angleWith(v32);
    }

    /**
     * Method to find the dihedral angle defined by planes v1-v2-v3 and
     * v2-v3-v4.
     *
     * @param v1, v2, v3, v4 - the instances of Point3D class
     * @return the dihedral angle defined
     */
    public static double findDihedral(Point3D v1, Point3D v2,
                                      Point3D v3, Point3D v4) {
        // normal of plane 1
        Vector3D v12 = new Vector3D(v2.sub(v1));
        Vector3D v32 = new Vector3D(v2.sub(v3));
        Vector3D n123 = v12.cross(v32).normalize();

        // normal of plane 2
        Vector3D v23 = new Vector3D(v3.sub(v2));
        Vector3D v43 = new Vector3D(v3.sub(v4));
        Vector3D n234 = v23.cross(v43).normalize();

        // and find the angle between the two planes
        return n123.angleWith(n234);
    }

    /**
     * compute N!
     *
     * @param n the n, whose factorial is to be found
     * @return the factorial
     */
    public static long factorial(int n) {
        long value = 1;

        while (n > 1) {
            value = value * n;
            n--;
        } // end while

        return value;
    }

    /**
     * compute double N! ... (1*3*5*...*n)
     *
     * @param n the n, whose factorial is to be found
     * @return the factorial
     */
    public static long factorial2(int n) {
        long value = 1;

        while (n > 0) {
            value = value * n;
            n -= 2;
        } // end while

        return value;
    }

    /**
     * Does ( a! / b! / (a-2*b)! )
     *
     * @param a the first term
     * @param b the second term
     * @return (a! / b! / (a-2*b)! )
     */
    public static double factorialRatioSquared(int a, int b) {
        return factorial(a) / factorial(b) / factorial(a - 2 * b);
    }
} // end of class MathUtils
