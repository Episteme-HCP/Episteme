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

package org.jscience.chemistry.gui.extended.graphics3d;

import javax.media.j3d.Appearance;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleArray;

import javax.vecmath.Vector3f;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class IcoSphere {
    /** DOCUMENT ME! */
    static final double X = 0.525731;

    /** DOCUMENT ME! */
    static final double Z = 0.850651;

    /** DOCUMENT ME! */
    static final double[][] vdata = {
            { -0.525731, 0.0, 0.850651 },
            { 0.525731, 0.0, 0.850651 },
            { -0.525731, 0.0, -0.850651 },
            { 0.525731, 0.0, -0.850651 },
            { 0.0, 0.850651, 0.525731 },
            { 0.0, 0.850651, -0.525731 },
            { 0.0, -0.850651, 0.525731 },
            { 0.0, -0.850651, -0.525731 },
            { 0.850651, 0.525731, 0.0 },
            { -0.850651, 0.525731, 0.0 },
            { 0.850651, -0.525731, 0.0 },
            { -0.850651, -0.525731, 0.0 }
        };

    /** DOCUMENT ME! */
    static final int[][] tindices = {
            { 0, 4, 1 },
            { 0, 9, 4 },
            { 9, 5, 4 },
            { 4, 5, 8 },
            { 4, 8, 1 },
            { 8, 10, 1 },
            { 8, 3, 10 },
            { 5, 3, 8 },
            { 5, 2, 3 },
            { 2, 7, 3 },
            { 7, 10, 3 },
            { 7, 6, 10 },
            { 7, 11, 6 },
            { 11, 0, 6 },
            { 0, 1, 6 },
            { 6, 1, 10 },
            { 9, 0, 11 },
            { 9, 11, 2 },
            { 9, 2, 5 },
            { 7, 2, 11 }
        };

    /** DOCUMENT ME! */
    static final int facets = 20;

    /** DOCUMENT ME! */
    int faces;

    /** DOCUMENT ME! */
    int index;

    /** DOCUMENT ME! */
    Shape3D shape;

    /** DOCUMENT ME! */
    TriangleArray tris;

    /** DOCUMENT ME! */
    Appearance app;

    /** DOCUMENT ME! */
    int quality = 0;

    /** DOCUMENT ME! */
    double radius = 1.0;

/**
     * Creates a new IcoSphere object.
     *
     * @param radius  DOCUMENT ME!
     * @param quality DOCUMENT ME!
     * @param a       DOCUMENT ME!
     */
    public IcoSphere(float radius, int quality, Appearance a) {
        this.quality = quality;
        this.radius = (double) radius;
        this.app = a;

        int faces = 20;

        for (int j = 0; j < quality; j++)
            faces *= 4;

        tris = new TriangleArray(faces * 3,
                TriangleArray.COORDINATES | TriangleArray.NORMALS);

        for (int k = index = 0; k < 20; k++)
            subdivide(vdata[tindices[k][0]], vdata[tindices[k][1]],
                vdata[tindices[k][2]], quality);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Shape3D getShape() {
        if (shape == null) {
            // Try the stripifier

            /*GeometryInfo geom = new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
              int nVert = tris.getVertexCount();
              float arr[]= new float[nVert*3];
              tris.getCoordinates(0, arr);
              geom.setCoordinates(arr);
              arr = new float[nVert*3];
              tris.getNormals(0, arr);
              geom.setNormals(arr);
              Stripifier strip = new Stripifier();
              strip.stripify(geom);
              shape = new Shape3D();
              shape.setGeometry(geom.getGeometryArray());
            */

            // if not stripifier
            shape = new Shape3D(tris);
            shape.setAppearance(app);
        }

        return shape;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ad DOCUMENT ME!
     */
    void normalize(double[] ad) {
        double local = Math.sqrt((ad[0] * ad[0]) + (ad[1] * ad[1]) +
                (ad[2] * ad[2]));

        if (local == 0.0) {
            return;
        }

        ad[0] /= local;
        ad[1] /= local;
        ad[2] /= local;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ad1 DOCUMENT ME!
     * @param ad2 DOCUMENT ME!
     * @param ad3 DOCUMENT ME!
     */
    void triangle(double[] ad1, double[] ad2, double[] ad3) {
        addTri(ad1);
        addTri(ad3);
        addTri(ad2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ad DOCUMENT ME!
     */
    void addTri(double[] ad) {
        double[] tmp = new double[3];
        tmp[0] = ad[0] * radius;
        tmp[1] = ad[1] * radius;
        tmp[2] = ad[2] * radius;

        tris.setCoordinate(index, tmp);

        Vector3f norm = new Vector3f((float) ad[0], (float) ad[1], (float) ad[2]);

        //norm.normalize();
        tris.setNormal(index++, norm);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ad1 DOCUMENT ME!
     * @param ad2 DOCUMENT ME!
     * @param ad3 DOCUMENT ME!
     * @param i DOCUMENT ME!
     */
    void subdivide(double[] ad1, double[] ad2, double[] ad3, int i) {
        if (i == 0) {
            triangle(ad1, ad2, ad3);

            return;
        }

        double[] local2 = { 0.0, 0.0, 0.0 };
        double[] local1 = { 0.0, 0.0, 0.0 };
        double[] local0 = { 0.0, 0.0, 0.0 };

        for (int j = 0; j < 3; j++) {
            local2[j] = ad1[j] + ad2[j];
            local1[j] = ad2[j] + ad3[j];
            local0[j] = ad3[j] + ad1[j];
        }

        normalize(local2);
        normalize(local1);
        normalize(local0);
        subdivide(ad1, local2, local0, i - 1);
        subdivide(ad2, local1, local2, i - 1);
        subdivide(ad3, local0, local1, i - 1);
        subdivide(local2, local1, local0, i - 1);
    }
}
