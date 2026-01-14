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

package org.jscience.biology.lsystems.growing.shape;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;

import javax.media.j3d.Appearance;
import javax.media.j3d.Shape3D;

import javax.vecmath.Point3f;


/**
 * This class builds a leaf with the points given in the passed array of
 * points.
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class LeafShape extends Shape3D {
/**
     * Makes a new shape using the trinagle fan array. The edges are the passed
     * points. As many points as liked can be passed but they have to be
     * ordered. Triangles are then generated from the array of points. The
     * triangle edges are the passed points where the first point is always
     * used and following are the other 2 edges of the triangles. So points
     * 0,1,2 make the first triangle - points 0,2,3 the second and so on.
     * Since this is a leaf it will be visible from both sides.
     *
     * @param p3fPoints The array of points.
     * @param iPoints   The number of points in the array.
     * @param appLeaf   The appearance to use for this leaf.
     */
    public LeafShape(Point3f[] p3fPoints, int iPoints, Appearance appLeaf) {
        //make one leaf
        //create geometryinfo class of type triangle fan array and add point array
        GeometryInfo giLeaf = new GeometryInfo(GeometryInfo.TRIANGLE_FAN_ARRAY);
        giLeaf.setCoordinates(p3fPoints);

        //make new integer array with one element containing the number of points
        int[] iArr = { iPoints };
        giLeaf.setStripCounts(iArr);

        //create new NormalGenerator to calculate normals
        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(giLeaf);

        //make triangle strips for faster rendering
        Stripifier st = new Stripifier();
        st.stripify(giLeaf);

        //create new Shape3D object add the fan array and the leaf appearance
        setGeometry(giLeaf.getGeometryArray());
        setAppearance(appLeaf);
    }
}
