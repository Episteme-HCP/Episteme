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

/*
 * AtlasVector.java
 *
 * Created on December 29, 2004, 8:58 PM
 */
package org.jscience.physics.solids.geom;

import org.jscience.physics.solids.AtlasObject;

import javax.media.j3d.Transform3D;

import javax.vecmath.Vector3d;


/**
 * A spatial direction and magnitude.
 *
 * @author Wegge
 */
public class AtlasVector extends AtlasObject {
    /**
     * DOCUMENT ME!
     */
    protected static String TYPE = "Vector";

    /**
     * DOCUMENT ME!
     */
    private double[] components = new double[3];

    /**
     * DOCUMENT ME!
     */
    private AtlasCoordSys coordSys = AtlasCoordSys.GLOBAL;

/**
     * Creates a new vector (0.,0.,0.) in the global coordinate system.
     */
    public AtlasVector() {
    }

/**
     * Creates a new vector (x,y,z) in the global coordinate system.
     */
    public AtlasVector(double x, double y, double z) {
        components[0] = x;
        components[1] = y;
        components[2] = z;
    }

/**
     * Creates a new vector (x,y,z) in the global coordinate system.
     */
    public AtlasVector(double[] vals) {
        components = vals;
    }

/**
     * Creates a new vector (x,y,z) in the global coordinate system.
     */
    public AtlasVector(AtlasCoordSys cs, double x, double y, double z) {
        coordSys = cs;
        components[0] = x;
        components[1] = y;
        components[2] = z;
    }

/**
     * Creates a new vector (x,y,z) in the global coordinate system.
     */
    public AtlasVector(AtlasCoordSys cs, double[] vals) {
        coordSys = cs;
        components = vals;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return TYPE;
    }

    /**
     * Returns the location in the global coordinate system. The
     * returned array is three elements long, in the order of x,y,z.
     *
     * @return DOCUMENT ME!
     */
    public double[] getGlobalDirection() {
        if (!coordSys.isGlobal()) {
            Transform3D T = coordSys.getInverseTransformation();

            Vector3d vec = new Vector3d(components);
            T.transform(vec);

            double[] ret = new double[3];
            vec.get(ret);

            return ret;
        }

        return components;
    }

    /**
     * Returns the components of the vector in the specified coordinate
     * system.
     *
     * @param coordSys DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public double[] getComponents(AtlasCoordSys coordSys) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the reference coordsys for this vector.
     *
     * @return DOCUMENT ME!
     */
    public AtlasCoordSys getCoordSys() {
        return coordSys;
    }

    /**
     * Returns the magnitude of the vector.
     *
     * @return DOCUMENT ME!
     */
    public double computeMagnitude() {
        double dx = components[0];
        double dy = components[1];
        double dz = components[2];

        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    /**
     * Computes the angle between the two vectors.
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double computeAngle(AtlasVector v) {
        double dot = this.dot(v);
        double a = this.computeMagnitude();
        double b = v.computeMagnitude();
        double den = a * b;
        double theta = Math.acos(dot / den);

        return theta;
    }

    /**
     * Computes dot product between the two vectors.
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double dot(AtlasVector v) {
        double[] lhs = this.getGlobalDirection();
        double[] rhs = v.getGlobalDirection();

        return (lhs[0] * rhs[0]) + (lhs[1] * rhs[1]) + (lhs[2] * rhs[2]);
    }

    /**
     * Normalizes the vector in place
     */
    public void normalize() {
        double mag = this.computeMagnitude();

        if (mag == 0) {
            return;
        }

        this.scale(1 / mag);
    }

    /**
     * Scales the vector magnitude. This is the same as multipling all
     * components by the scale.
     *
     * @param s DOCUMENT ME!
     */
    public void scale(double s) {
        for (int i = 0; i < 3; i++) {
            components[i] = components[i] * s;
        }
    }

    /**
     * Determines whether the two vectors have the same direction
     *
     * @param v DOCUMENT ME!
     * @param tolerance DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEquivalent(AtlasVector v, double tolerance) {
        double[] a = this.getGlobalDirection();
        double[] b = v.getGlobalDirection();

        for (int i = 0; i < 3; i++) {
            if (Math.abs(a[i] - b[i]) > tolerance) {
                return false;
            }
        }

        return true;
    }

    /**
     * Computes cross product between the two vectors.
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AtlasVector cross(AtlasVector v) {
        double[] a = this.getGlobalDirection();
        double[] b = v.getGlobalDirection();

        double x = (a[1] * b[2]) - (a[2] * b[1]);
        double y = (a[2] * b[0]) - (a[0] * b[2]);
        double z = (a[0] * b[1]) - (a[1] * b[0]);

        return new AtlasVector(x, y, z);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String ret = getType() + " " + getId() + " : ( ";

        double[] loc = this.getGlobalDirection();
        ret = ret + components[0] + " , " + components[1] + " , " +
            components[2] + " )";

        return ret;
    }

    /**
     * Returns a clone of the vector
     *
     * @return DOCUMENT ME!
     */
    public AtlasVector clone() {
        return new AtlasVector(coordSys, components);
    }

    /**
     * Determines whether the two vectors are parallel. Uses a
     * tolerance of .0001 radians
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isParallel(AtlasVector v) {
        double test = this.computeAngle(v);
        test = Math.abs(test);

        if (test < .0001) {
            return true;
        }

        return false;
    }
}
