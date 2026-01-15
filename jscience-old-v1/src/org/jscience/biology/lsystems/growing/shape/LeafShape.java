/*
---------------------------------------------------------------------------
VIRTUAL PLANTS
==============

This Diploma work is a computer graphics project made at the University
of applied sciences in Biel, Switzerland. http://www.hta-bi.bfh.ch
The taks is to simulate the growth of 3 dimensional plants and show
them in a virtual world.
This work is based on the ideas of Lindenmayer and Prusinkiewicz which
are taken from the book 'The algorithmic beauty of plants'.
The Java and Java3D classes have to be used for this work. This file
is one class of the program. For more information look at the VirtualPlants
homepage at: http://www.hta-bi.bfh.ch/Projects/VirtualPlants

Hosted by Claude Schwab

Developed by Rene Gressly
http://www.gressly.ch/rene

25.Oct.1999 - 17.Dec.1999

Copyright by the University of applied sciences Biel, Switzerland
----------------------------------------------------------------------------
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
