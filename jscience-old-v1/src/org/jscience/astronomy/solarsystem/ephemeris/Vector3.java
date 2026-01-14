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

package org.jscience.astronomy.solarsystem.ephemeris;


//this code is rebundled after the code from
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class Vector3 {
    /** DOCUMENT ME! */
    public double x;

    /** DOCUMENT ME! */
    public double y;

    /** DOCUMENT ME! */
    public double z;

/**
     * Creates a new Vector3 object.
     */
    public Vector3() {
        set(0.0D, 0.0D, 0.0D);
    }

/**
     * Creates a new Vector3 object.
     *
     * @param vector3 DOCUMENT ME!
     */
    public Vector3(Vector3 vector3) {
        x = vector3.x;
        y = vector3.y;
        z = vector3.z;
    }

/**
     * Creates a new Vector3 object.
     *
     * @param d  DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     * @param d2 DOCUMENT ME!
     */
    public Vector3(double d, double d1, double d2) {
        set(d, d1, d2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param vector3 DOCUMENT ME!
     * @param vector3_1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double dot(Vector3 vector3, Vector3 vector3_1) {
        return (vector3.x * vector3_1.x) + (vector3.y * vector3_1.y) +
        (vector3.z * vector3_1.z);
    }

    /**
     * DOCUMENT ME!
     */
    public void normalize() {
        double d = magnitude();
        x = x / d;
        y = y / d;
        z = z / d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     * @param d2 DOCUMENT ME!
     */
    public void set(double d, double d1, double d2) {
        x = d;
        y = d1;
        z = d2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vector3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Vector3 neg(Vector3 vector3) {
        Vector3 vector3_1 = new Vector3(-vector3.x, -vector3.y, -vector3.z);

        return vector3_1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vector3 DOCUMENT ME!
     * @param vector3_1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Vector3 add(Vector3 vector3, Vector3 vector3_1) {
        Vector3 vector3_2 = new Vector3(vector3.x + vector3_1.x,
                vector3.y + vector3_1.y, vector3.z + vector3_1.z);

        return vector3_2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double magnitude() {
        return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    /**
     * DOCUMENT ME!
     *
     * @param vector3 DOCUMENT ME!
     * @param vector3_1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Vector3 cross(Vector3 vector3, Vector3 vector3_1) {
        double d = (vector3.y * vector3_1.z) - (vector3.z * vector3_1.y);
        double d1 = (vector3_1.x * vector3.z) - (vector3.x * vector3_1.z);
        double d2 = (vector3.x * vector3_1.y) - (vector3_1.x * vector3.y);

        return new Vector3(d, d1, d2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void scale(double d) {
        x = x * d;
        y = y * d;
        z = z * d;
    }
}
