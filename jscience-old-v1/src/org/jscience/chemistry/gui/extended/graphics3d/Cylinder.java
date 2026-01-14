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
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Cylinder {
    /** DOCUMENT ME! */
    float[] verts;

    /** DOCUMENT ME! */
    float[] normals;

    /** DOCUMENT ME! */
    QuadArray quad = null;

    /** DOCUMENT ME! */
    float div = 3.0f;

    /** DOCUMENT ME! */
    Shape3D shape;

/**
     * Creates a new Cylinder object.
     *
     * @param radius  DOCUMENT ME!
     * @param length  DOCUMENT ME!
     * @param quality DOCUMENT ME!
     * @param a       DOCUMENT ME!
     */
    public Cylinder(float radius, float length, int quality, Appearance a) {
        if (quality < 3) {
            quality = 3;
        }

        div = (float) quality;

        verts = new float[quality * 12];
        normals = new float[quality * 12];

        double inc = (2.0 * Math.PI) / (double) div;

        for (int i = 0; i < quality; i++) {
            float z1 = radius * (float) Math.sin((double) i * inc);
            float x1 = radius * (float) Math.cos((double) i * inc);
            float z2 = radius * (float) Math.sin((double) (i + 1) * inc);
            float x2 = radius * (float) Math.cos((double) (i + 1) * inc);

            verts[12 * i] = x1;
            verts[(12 * i) + 1] = 0.0f;
            verts[(12 * i) + 2] = z1;
            verts[(12 * i) + 3] = x1;
            verts[(12 * i) + 4] = length;
            verts[(12 * i) + 5] = z1;
            verts[(12 * i) + 6] = x2;
            verts[(12 * i) + 7] = length;
            verts[(12 * i) + 8] = z2;
            verts[(12 * i) + 9] = x2;
            verts[(12 * i) + 10] = 0.0f;
            verts[(12 * i) + 11] = z2;

            float nz1 = (float) Math.sin((double) i * inc);
            float nx1 = (float) Math.cos((double) i * inc);
            float nz2 = (float) Math.sin((double) (i + 1) * inc);
            float nx2 = (float) Math.cos((double) (i + 1) * inc);

            normals[12 * i] = nx1;
            normals[(12 * i) + 1] = 0.0f;
            normals[(12 * i) + 2] = nz1;
            normals[(12 * i) + 3] = nx1;
            normals[(12 * i) + 4] = 0.0f;
            normals[(12 * i) + 5] = nz1;
            normals[(12 * i) + 6] = nx2;
            normals[(12 * i) + 7] = 0.0f;
            normals[(12 * i) + 8] = nz2;
            normals[(12 * i) + 9] = nx2;
            normals[(12 * i) + 10] = 0.0f;
            normals[(12 * i) + 11] = nz2;
        }

        quad = new QuadArray(quality * 4,
                QuadArray.COORDINATES | QuadArray.NORMALS);
        quad.setCoordinates(0, verts);
        quad.setNormals(0, normals);

        // Try the stripifier
        /*GeometryInfo geom = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
        geom.setCoordinates(verts);
        geom.setNormals(normals);
        Stripifier strip = new Stripifier();
        strip.stripify(geom);
        shape = new Shape3D();
        shape.setGeometry(geom.getGeometryArray());
        shape.setAppearance(a);
        */
        shape = new Shape3D(quad, a);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Shape3D getShape() {
        return shape;
    }
}
