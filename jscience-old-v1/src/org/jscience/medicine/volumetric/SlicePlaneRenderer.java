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

package org.jscience.medicine.volumetric;


//repackaged after the code available at http://www.j3d.org/tutorials/quick_fix/volume.html
//author: Doug Gehringer
//email:Doug.Gehringer@sun.com
import javax.media.j3d.*;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class SlicePlaneRenderer extends Renderer {
    // Attrs we care about
    /** DOCUMENT ME! */
    CoordAttr volRefPtAttr;

    // current values derived from ctx
    /** DOCUMENT ME! */
    boolean border = true;

    /** DOCUMENT ME! */
    Switch borderSwitch;

    /** DOCUMENT ME! */
    Shape3D borderShape;

    /** DOCUMENT ME! */
    int numSlicePts;

    /** DOCUMENT ME! */
    Point3d[] slicePts = new Point3d[7];

    /** DOCUMENT ME! */
    int[] count = new int[1];

    /** DOCUMENT ME! */
    BranchGroup root;

/**
     * Creates a new SlicePlaneRenderer object.
     *
     * @param view    DOCUMENT ME!
     * @param context DOCUMENT ME!
     * @param vol     DOCUMENT ME!
     */
    public SlicePlaneRenderer(View view, Context context, Volume vol) {
        super(view, context, vol);
        volRefPtAttr = (CoordAttr) context.getAttr("Vol Ref Pt");

        root = new BranchGroup();

        // subclasses add the slice geometry to root
        borderSwitch = new Switch(Switch.CHILD_ALL);
        borderSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);

        RenderingAttributes ra = new RenderingAttributes();
        ra.setDepthBufferEnable(true);

        ColoringAttributes bclr = new ColoringAttributes(0.4f, 0.4f, 0.4f,
                ColoringAttributes.SHADE_FLAT);
        Appearance ba = new Appearance();
        ba.setColoringAttributes(bclr);
        ba.setRenderingAttributes(ra);

        borderShape = new Shape3D(null, ba);
        borderShape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);

        borderSwitch.addChild(borderShape);

        root.addChild(borderSwitch);

        root.setCapability(BranchGroup.ALLOW_DETACH);
        root.setCapability(BranchGroup.ALLOW_LOCAL_TO_VWORLD_READ);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pt DOCUMENT ME!
     * @param plane DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double planeDist(Point3d pt, Vector4d plane) {
        return (pt.x * plane.x) + (pt.y * plane.y) + (pt.z * plane.z) +
        plane.w;
    }

    /**
     * DOCUMENT ME!
     *
     * @param edge DOCUMENT ME!
     */
    private void exchangePts(Point3d[] edge) {
        Point3d temp = edge[0];
        edge[0] = edge[1];
        edge[1] = temp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param edge0 DOCUMENT ME!
     * @param edge1 DOCUMENT ME!
     */
    private void exchangeEdges(Point3d[] edge0, Point3d[] edge1) {
        Point3d temp0 = edge0[0];
        Point3d temp1 = edge0[1];
        edge0[0] = edge1[0];
        edge0[1] = edge1[1];
        edge1[0] = temp0;
        edge1[1] = temp1;
    }

    /**
     * Intersect the VOI "cube" and a slice plane.  Put the resulting
     * points into slicePts[]
     *
     * @param plane DOCUMENT ME!
     */
    void calcSlicePts(Vector4d plane) {
        int numOutEdges = 0;
        Point3d[][] outEdges = new Point3d[6][2];
        Point3d wantVtx;
        Point3d[] points = volume.voiPts;
        double[] ptDist = new double[8]; // dist of pt from plane
        int[][] edges = { // edges of the cube
                          /* 0  */
                { 0, 1 }, /* 1  */
                { 1, 2 }, /* 2  */
                { 2, 3 }, /* 3  */
                { 3, 0 }, /* 4  */
                { 0, 4 }, /* 5  */
                { 1, 5 }, /* 6  */
                { 2, 6 }, /* 7  */
                { 3, 7 }, /* 8  */
                { 4, 5 }, /* 9  */
                { 5, 6 }, /* 10 */
                { 6, 7 }, /* 11 */
                { 7, 4 }
            };
        boolean[] edgeFlags = new boolean[12]; // edge cut by plane?
        Point3d[] edgeInts = new Point3d[12]; // intersection in edge
        int[][] faces = { // edges of faces of cube
                { 0, 1, 2, 3 },
                { 1, 6, 9, 5 },
                { 6, 2, 7, 10 },
                { 7, 3, 4, 11 },
                { 0, 5, 8, 4 },
                { 9, 8, 10, 11 }
            };

        // determine the distance of each point from the plane
        for (int i = 0; i < 8; i++) {
            ptDist[i] = planeDist(points[i], plane);
        }

        // scan each edge, mark the ones that are cut and calc the intersection
        for (int i = 0; i < 12; i++) {
            double dst0 = ptDist[edges[i][0]];
            double dst1 = ptDist[edges[i][1]];

            if ((dst0 > 0) ^ (dst1 > 0)) {
                edgeFlags[i] = true;

                double t = dst1 / (dst1 - dst0);
                edgeInts[i] = new Point3d();
                edgeInts[i].x = (t * points[edges[i][0]].x) +
                    ((1 - t) * points[edges[i][1]].x);
                edgeInts[i].y = (t * points[edges[i][0]].y) +
                    ((1 - t) * points[edges[i][1]].y);
                edgeInts[i].z = (t * points[edges[i][0]].z) +
                    ((1 - t) * points[edges[i][1]].z);
            } else {
                edgeFlags[i] = false;
            }
        }

        // scan each face, if it is cut by the plane, make an edge across
        // the face
        for (int i = 0; i < 6; i++) {
            boolean anyCut = (edgeFlags[faces[i][0]] | edgeFlags[faces[i][1]] |
                edgeFlags[faces[i][2]] | edgeFlags[faces[i][3]]);

            if (anyCut) {
                int edgePt = 0;

                if (edgeFlags[faces[i][0]]) {
                    outEdges[numOutEdges][edgePt++] = edgeInts[faces[i][0]];
                }

                if (edgeFlags[faces[i][1]]) {
                    outEdges[numOutEdges][edgePt++] = edgeInts[faces[i][1]];
                }

                if (edgeFlags[faces[i][2]]) {
                    outEdges[numOutEdges][edgePt++] = edgeInts[faces[i][2]];
                }

                if (edgeFlags[faces[i][3]]) {
                    outEdges[numOutEdges][edgePt++] = edgeInts[faces[i][3]];
                }

                numOutEdges++;
            }
        }

        // sort the edges, matching the endpoints to make a loop
        for (int i = 0; i < numOutEdges; i++) {
            wantVtx = outEdges[i][1];

            for (int j = i + 1; j < numOutEdges; j++) {
                if ((outEdges[j][0] == wantVtx) || (outEdges[j][1] == wantVtx)) {
                    if (outEdges[j][1] == wantVtx) {
                        exchangePts(outEdges[j]);
                    }

                    if (j != (i + 1)) {
                        exchangeEdges(outEdges[i + 1], outEdges[j]);
                    }
                }
            }
        }

        numSlicePts = numOutEdges;

        for (int i = 0; i < numOutEdges; i++) {
            slicePts[i] = outEdges[i][0];
        }

        // close the loop for rendering as line
        slicePts[numOutEdges] = slicePts[0];

        /*
        System.out.println("Slice points = ");
        for (int i = 0; i < numOutEdges; i++ ) {
             System.out.println("\tslicePts[" + i + "] = " + slicePts[i]);
        }
        */
    }

    // subclasses define the actual slice geometry
    /**
     * DOCUMENT ME!
     */
    abstract void setSliceGeo();

    /**
     * DOCUMENT ME!
     */
    void setPlaneGeos() {
        Point3d eyePt = getViewPosInLocal(root);

        //System.out.println("eypt = " + eyePt);
        // geos is null unless points are valid
        GeometryArray borderGeo = null;

        if (eyePt != null) {
            Point3d volRefPt = volRefPtAttr.getValue();

            //System.out.println("volRefPt = " + volRefPt);
            Vector3d eyeVec = new Vector3d();
            eyeVec.sub(eyePt, volRefPt);

            //System.out.println("eyeVec = " + eyeVec);
            if (eyeVec.length() < 0.001) {
                eyeVec.x = 0.0;
                eyeVec.x = 0.0;
                eyeVec.z = 1.0;
            }

            Vector4d slicePlane = new Vector4d();
            slicePlane.x = eyeVec.x;
            slicePlane.y = eyeVec.y;
            slicePlane.z = eyeVec.z;
            slicePlane.w = -((slicePlane.x * volRefPt.x) +
                (slicePlane.y * volRefPt.y) + (slicePlane.z * volRefPt.z));

            //System.out.println("slicePlane = " + slicePlane);
            calcSlicePts(slicePlane);

            if (numSlicePts > 0) {
                //System.out.println("slicePts[0] = " + slicePts[0]);
                count[0] = numSlicePts;

                count[0] = numSlicePts + 1;
                borderGeo = new LineStripArray(numSlicePts + 1,
                        LineStripArray.COORDINATES, count);
                borderGeo.setCoordinates(0, slicePts, 0, numSlicePts + 1);
            }
        } else {
            //System.out.println("eye pt is null");
        }

        // set up the actual slice
        setSliceGeo();

        borderShape.setGeometry(borderGeo);
    }

    /**
     * DOCUMENT ME!
     *
     * @param dynamicGroup DOCUMENT ME!
     * @param staticGroup DOCUMENT ME!
     */
    public void attach(Group dynamicGroup, Group staticGroup) {
        dynamicGroup.addChild(root);
    }

    /**
     * DOCUMENT ME!
     */
    abstract public void update();

    /**
     * DOCUMENT ME!
     */
    public void eyePtChanged() {
        setPlaneGeos();
    }

    /**
     * Returns the number of pixels drawn in the current display
     *
     * @param screenSize DOCUMENT ME!
     * @param canvas DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double calcRenderSize(ScreenSizeCalculator screenSize,
        Canvas3D canvas) {
        int rSize = 0;
        double area = 0.0;

        Node renderNode = root;

        screenSize.setScreenXform(canvas, renderNode);

        // TODO: modify ScreenSizeCalculator to take convex poly instead of
        // quad
        return area;
    }
}
