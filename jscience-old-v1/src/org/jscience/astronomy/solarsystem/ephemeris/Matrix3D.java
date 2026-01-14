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
public class Matrix3D {
    /** DOCUMENT ME! */
    double a1;

    /** DOCUMENT ME! */
    double a2;

    /** DOCUMENT ME! */
    double a3;

    /** DOCUMENT ME! */
    double b1;

    /** DOCUMENT ME! */
    double b2;

    /** DOCUMENT ME! */
    double b3;

    /** DOCUMENT ME! */
    double c1;

    /** DOCUMENT ME! */
    double c2;

    /** DOCUMENT ME! */
    double c3;

/**
     * Creates a new Matrix3D object.
     *
     * @param ad DOCUMENT ME!
     */
    public Matrix3D(double[][] ad) {
        a1 = ad[0][0];
        a2 = ad[0][1];
        a3 = ad[0][2];
        b1 = ad[1][0];
        b2 = ad[1][1];
        b3 = ad[1][2];
        c1 = ad[2][0];
        c2 = ad[2][1];
        c3 = ad[2][2];
    }

/**
     * Creates a new Matrix3D object.
     *
     * @param matrix3d DOCUMENT ME!
     */
    public Matrix3D(Matrix3D matrix3d) {
        a1 = matrix3d.a1;
        a2 = matrix3d.a2;
        a3 = matrix3d.a3;
        b1 = matrix3d.b1;
        b2 = matrix3d.b2;
        b3 = matrix3d.b3;
        c1 = matrix3d.c1;
        c2 = matrix3d.c2;
        c3 = matrix3d.c3;
    }

/**
     * Creates a new Matrix3D object.
     */
    public Matrix3D() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Matrix3D rotZ(double d) {
        double d1 = Math.cos(d);
        double d2 = Math.sin(d);
        Matrix3D matrix3d = new Matrix3D();
        matrix3d.a1 = d1;
        matrix3d.a2 = -d2;
        matrix3d.a3 = 0.0D;
        matrix3d.b1 = d2;
        matrix3d.b2 = d1;
        matrix3d.b3 = 0.0D;
        matrix3d.c1 = 0.0D;
        matrix3d.c2 = 0.0D;
        matrix3d.c3 = 1.0D;

        return matrix3d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Matrix3D rotX(double d) {
        double d1 = Math.cos(d);
        double d2 = Math.sin(d);
        Matrix3D matrix3d = new Matrix3D();
        matrix3d.a1 = 1.0D;
        matrix3d.a2 = 0.0D;
        matrix3d.a3 = 0.0D;
        matrix3d.b1 = 0.0D;
        matrix3d.b2 = d1;
        matrix3d.b3 = -d2;
        matrix3d.c1 = 0.0D;
        matrix3d.c2 = d2;
        matrix3d.c3 = d1;

        return matrix3d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Matrix3D rotY(double d) {
        double d1 = Math.cos(d);
        double d2 = Math.sin(d);
        Matrix3D matrix3d = new Matrix3D();
        matrix3d.a1 = d1;
        matrix3d.a2 = 0.0D;
        matrix3d.a3 = d2;
        matrix3d.b1 = 0.0D;
        matrix3d.b2 = 1.0D;
        matrix3d.b3 = 0.0D;
        matrix3d.c1 = -d2;
        matrix3d.c2 = 0.0D;
        matrix3d.c3 = d1;

        return matrix3d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vector3 DOCUMENT ME!
     */
    public void transform(Vector3 vector3) {
        double d = (a1 * vector3.x) + (a2 * vector3.y) + (a3 * vector3.z);
        double d1 = (b1 * vector3.x) + (b2 * vector3.y) + (b3 * vector3.z);
        double d2 = (c1 * vector3.x) + (c2 * vector3.y) + (c3 * vector3.z);
        vector3.x = d;
        vector3.y = d1;
        vector3.z = d2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Matrix3D identity() {
        Matrix3D matrix3d = new Matrix3D();
        matrix3d.a1 = 1.0D;
        matrix3d.b2 = 1.0D;
        matrix3d.c3 = 1.0D;

        return matrix3d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vector3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3 mul(Vector3 vector3) {
        double d = (a1 * vector3.x) + (a2 * vector3.y) + (a3 * vector3.z);
        double d1 = (b1 * vector3.x) + (b2 * vector3.y) + (b3 * vector3.z);
        double d2 = (c1 * vector3.x) + (c2 * vector3.y) + (c3 * vector3.z);

        return new Vector3(d, d1, d2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param matrix3d DOCUMENT ME!
     * @param matrix3d1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Matrix3D mul(Matrix3D matrix3d, Matrix3D matrix3d1) {
        Matrix3D matrix3d2 = new Matrix3D();
        matrix3d2.a1 = (matrix3d.a1 * matrix3d1.a1) +
            (matrix3d.a2 * matrix3d1.b1) + (matrix3d.a3 * matrix3d1.c1);
        matrix3d2.b1 = (matrix3d.b1 * matrix3d1.a1) +
            (matrix3d.b2 * matrix3d1.b1) + (matrix3d.b3 * matrix3d1.c1);
        matrix3d2.c1 = (matrix3d.c1 * matrix3d1.a1) +
            (matrix3d.c2 * matrix3d1.b1) + (matrix3d.c3 * matrix3d1.c1);
        matrix3d2.a2 = (matrix3d.a1 * matrix3d1.a2) +
            (matrix3d.a2 * matrix3d1.b2) + (matrix3d.a3 * matrix3d1.c2);
        matrix3d2.b2 = (matrix3d.b1 * matrix3d1.a2) +
            (matrix3d.b2 * matrix3d1.b2) + (matrix3d.b3 * matrix3d1.c2);
        matrix3d2.c2 = (matrix3d.c1 * matrix3d1.a2) +
            (matrix3d.c2 * matrix3d1.b2) + (matrix3d.c3 * matrix3d1.c2);
        matrix3d2.a3 = (matrix3d.a1 * matrix3d1.a3) +
            (matrix3d.a2 * matrix3d1.b3) + (matrix3d.a3 * matrix3d1.c3);
        matrix3d2.b3 = (matrix3d.b1 * matrix3d1.a3) +
            (matrix3d.b2 * matrix3d1.b3) + (matrix3d.b3 * matrix3d1.c3);
        matrix3d2.c3 = (matrix3d.c1 * matrix3d1.a3) +
            (matrix3d.c2 * matrix3d1.b3) + (matrix3d.c3 * matrix3d1.c3);

        return matrix3d2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param amatrix3d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Matrix3D mul(Matrix3D[] amatrix3d) {
        Matrix3D matrix3d = new Matrix3D(amatrix3d[0]);

        for (int i = 1; i < amatrix3d.length; i++)
            matrix3d = mul(matrix3d, amatrix3d[i]);

        return matrix3d;
    }
}
