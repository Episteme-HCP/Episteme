/*
 *        %Z%%M% %I% %E% %U%
 *
 * Copyright (c) 1996-1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */
package org.jscience.medicine.volumetric;


//repackaged after the code available at http://www.j3d.org/tutorials/quick_fix/volume.html
//author: Doug Gehringer
//email:Doug.Gehringer@sun.com
import javax.media.j3d.*;

import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class SlicePlane2DRenderer extends SlicePlaneRenderer
    implements VolRendConstants {
    /** DOCUMENT ME! */
    Texture2DVolume texVol;

    /** DOCUMENT ME! */
    TextureAttributes texAttr = new TextureAttributes();

    /** DOCUMENT ME! */
    TransparencyAttributes trans = new TransparencyAttributes();

    /** DOCUMENT ME! */
    Material m = new Material();

    /** DOCUMENT ME! */
    PolygonAttributes p = new PolygonAttributes();

    /** DOCUMENT ME! */
    RenderingAttributes r = new RenderingAttributes();

    /** DOCUMENT ME! */
    ColoringAttributes clr = new ColoringAttributes();

    /** DOCUMENT ME! */
    Group sliceGroup = new Group();

    // reused temporaries, values are only good in the methods in which they
    // are set (that is, they should not be used across methods)
    /** DOCUMENT ME! */
    Point3d txPt0 = new Point3d();

    /** DOCUMENT ME! */
    Point3d txPt1 = new Point3d();

    /** DOCUMENT ME! */
    Point3d txPt2 = new Point3d();

    /** DOCUMENT ME! */
    Vector3d v1 = new Vector3d();

    /** DOCUMENT ME! */
    Vector3d v2 = new Vector3d();

    /** DOCUMENT ME! */
    Vector3d texNorm = new Vector3d();

    /** DOCUMENT ME! */
    Point3d[] pt = new Point3d[3];

    /** DOCUMENT ME! */
    double[] rCoord = new double[3];

    /** DOCUMENT ME! */
    Point3d midCalc = new Point3d();

    /** DOCUMENT ME! */
    Point3d upperLeftCut = new Point3d();

    /** DOCUMENT ME! */
    Point3d upperRightCut = new Point3d();

    /** DOCUMENT ME! */
    Point3d lowerLeftCut = new Point3d();

    /** DOCUMENT ME! */
    Point3d lowerRightCut = new Point3d();

    /** DOCUMENT ME! */
    int[] count = new int[1];

    /** DOCUMENT ME! */
    Point3d[] shapePts = new Point3d[5];

    /** DOCUMENT ME! */
    Color4f[] shapeColrs = new Color4f[4];

    /** DOCUMENT ME! */
    boolean outputLines = false;

    /** DOCUMENT ME! */
    boolean verbose = false;

/**
     * Creates a new SlicePlane2DRenderer object.
     *
     * @param view    DOCUMENT ME!
     * @param context DOCUMENT ME!
     * @param vol     DOCUMENT ME!
     */
    public SlicePlane2DRenderer(View view, Context context, Volume vol) {
        super(view, context, vol);
        texVol = new Texture2DVolume(context, vol);

        for (int i = 0; i < 4; i++) {
            shapeColrs[i] = new Color4f();
        }

        texAttr.setTextureMode(TextureAttributes.MODULATE);
        texAttr.setCapability(TextureAttributes.ALLOW_COLOR_TABLE_WRITE);
        trans.setTransparencyMode(TransparencyAttributes.BLENDED);
        trans.setSrcBlendFunction(TransparencyAttributes.BLEND_ONE);
        trans.setDstBlendFunction(TransparencyAttributes.BLEND_ONE);
        m.setLightingEnable(false);
        p.setCullFace(PolygonAttributes.CULL_NONE);
        r.setDepthBufferWriteEnable(false);

        // these are the default for no texture
        trans.setTransparency(0.0f);
        clr.setColor(0.0f, 0.0f, 0.0f);

        // set up an initial, empty slice
        sliceGroup.setCapability(Group.ALLOW_CHILDREN_READ);
        sliceGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);
        sliceGroup.addChild(null);
        root.addChild(sliceGroup);
        numSlicePts = 0;
        setSliceGeo();
    }

    /**
     * DOCUMENT ME!
     */
    public void update() {
        boolean reloadPgon = false;
        boolean reloadTct = false;
        int texVolUpdate = texVol.update();

        switch (texVolUpdate) {
        case TextureVolume.RELOAD_NONE:
            return;

        case TextureVolume.RELOAD_VOLUME:
            reloadPgon = true;
            reloadTct = true;

            break;

        case TextureVolume.RELOAD_CMAP:
            reloadTct = true;

            break;
        }

        if (reloadPgon) {
            //System.out.println("update: reloadPgon true");
            setPlaneGeos();
        } else {
            //System.out.println("update: reloadPgon false");
        }

        if (reloadTct) {
            tctReload();
        }
    }

    /**
     * DOCUMENT ME!
     */
    void tctReload() {
        if (texVol.useTextureColorTable) {
            texAttr.setTextureColorTable(texVol.texColorMap);
        } else {
            texAttr.setTextureColorTable(null);
        }
    }

    // generates an ordered group containing a shape for each intesection
    /**
     * DOCUMENT ME!
     */
    void setSliceGeo() {
        BranchGroup polyGroup = new BranchGroup();
        polyGroup.setCapability(BranchGroup.ALLOW_DETACH);

        if (numSlicePts > 0) {
            // for each triangle...
            for (int i = 0; i < (numSlicePts - 2); i++) {
                tesselateTri(polyGroup, slicePts[0], slicePts[i + 1],
                    slicePts[i + 2]);
            }
        }

        sliceGroup.setChild(polyGroup, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param polyGroup DOCUMENT ME!
     * @param pt0 DOCUMENT ME!
     * @param pt1 DOCUMENT ME!
     * @param pt2 DOCUMENT ME!
     */
    private void tesselateTri(Group polyGroup, Point3d pt0, Point3d pt1,
        Point3d pt2) {
        // create ordered group
        OrderedGroup triGroup = new OrderedGroup();
        polyGroup.addChild(triGroup);

        // select axis and dir, determine r range
        // Axis is dependent on the "normal" of the geo in texture space
        // and the density of the texture in each dimension.
        if (verbose) {
            System.out.println("pt0 = " + pt0);
            System.out.println("pt1 = " + pt1);
            System.out.println("pt2 = " + pt2);

            System.out.println("volume.xTexGenScale = " + volume.xTexGenScale);
            System.out.println("volume.yTexGenScale = " + volume.yTexGenScale);
            System.out.println("volume.zTexGenScale = " + volume.zTexGenScale);
        }

        txPt0.x = pt0.x * volume.xTexGenScale;
        txPt0.y = pt0.y * volume.yTexGenScale;
        txPt0.z = pt0.z * volume.zTexGenScale;

        txPt1.x = pt1.x * volume.xTexGenScale;
        txPt1.y = pt1.y * volume.yTexGenScale;
        txPt1.z = pt1.z * volume.zTexGenScale;

        txPt2.x = pt2.x * volume.xTexGenScale;
        txPt2.y = pt2.y * volume.yTexGenScale;
        txPt2.z = pt2.z * volume.zTexGenScale;

        if (verbose) {
            System.out.println("txPt0 = " + txPt0);
            System.out.println("txPt1 = " + txPt1);
            System.out.println("txPt2 = " + txPt2);
        }

        v1.sub(txPt1, txPt0);
        v2.sub(txPt2, txPt0);
        texNorm.cross(v1, v2);

        if (verbose) {
            System.out.println("v1 = " + v1);
            System.out.println("v2 = " + v2);
            System.out.println("texNorm = " + texNorm);
        }

        // texNorm is now normal in tex coord space, but we need take the
        // into account the texture resolution on each axis
        texNorm.x /= volume.xTexSize;
        texNorm.y /= volume.yTexSize;
        texNorm.z /= volume.zTexSize;

        if (verbose) {
            System.out.println("adj texNorm = " + texNorm);
        }

        texNorm.normalize();

        if (verbose) {
            System.out.println("adj norm texNorm = " + texNorm);
        }

        // select the axis with the greatest magnitude and not the sign
        int maxDir = X_AXIS;
        double maxMag = texNorm.x;

        if (Math.abs(texNorm.y) > Math.abs(maxMag)) {
            maxDir = Y_AXIS;
            maxMag = texNorm.y;
        }

        if (Math.abs(texNorm.z) > Math.abs(maxMag)) {
            maxDir = Z_AXIS;
            maxMag = texNorm.z;
        }

        // stacking order in ordered group depends on sign of maxMag
        int orderDir;

        if (maxMag > 0.0) {
            orderDir = FRONT;
        } else {
            orderDir = BACK;
        }

        if (verbose) {
            System.out.println("maxDir = " + maxDir);
            System.out.println("orderDir = " + orderDir);
        }

        // Create an "rCoord" for each point. This is the a float with
        // the integer specifying the texture index and the fraction
        // specifying the weight
        Texture2D[] textures = null;
        TexCoordGeneration tg = null;

        switch (maxDir) {
        case X_AXIS:
            textures = texVol.xTextures;
            tg = texVol.xTg;
            rCoord[0] = txPt0.x * volume.xTexSize;
            rCoord[1] = txPt1.x * volume.xTexSize;
            rCoord[2] = txPt2.x * volume.xTexSize;

            break;

        case Y_AXIS:
            textures = texVol.yTextures;
            tg = texVol.yTg;
            rCoord[0] = txPt0.y * volume.yTexSize;
            rCoord[1] = txPt1.y * volume.yTexSize;
            rCoord[2] = txPt2.y * volume.yTexSize;

            break;

        case Z_AXIS:
            textures = texVol.zTextures;
            tg = texVol.zTg;
            rCoord[0] = txPt0.z * volume.zTexSize;
            rCoord[1] = txPt1.z * volume.zTexSize;
            rCoord[2] = txPt2.z * volume.zTexSize;

            break;
        }

        // we need to sort the points by rCoord value, use arrays
        // so that we can just sort by index
        pt[0] = pt0;
        pt[1] = pt1;
        pt[2] = pt2;

        int top = 0;

        if (rCoord[1] > rCoord[top]) {
            top = 1;
        }

        if (rCoord[2] > rCoord[top]) {
            top = 2;
        }

        int bot = 0;

        if (rCoord[1] < rCoord[bot]) {
            bot = 1;
        }

        if (rCoord[2] < rCoord[bot]) {
            bot = 2;
        }

        int mid = 0;

        if ((mid == top) || (mid == bot)) {
            mid = 1;

            if ((mid == top) || (mid == bot)) {
                mid = 2;
            }
        }

        /* now have a tri:
        
                top
                | \
                |  mid
                |
                |
                bot
        
        (or a mirror image)
        
        intersect top-bot edge at r value of mid to get two tris
        
        */
        intersect(midCalc, pt[bot], rCoord[bot], pt[top], rCoord[top],
            rCoord[mid]);

        // determine if mid is left or right pt to preserve winding of
        // original triangle.  Use the ordering of bot, mid, top to tell
        //
        boolean midIsRight = false;

        if (((bot == 0) && (mid == 1)) || ((bot == 1) && (mid == 2)) ||
                ((bot == 2) && (mid == 0))) {
            midIsRight = true;
        }

        /* now have two tris:
        
                top
                | \
                |  \
                ml--mr
                |
                |
                bot
        
            where ml is midLeft and mr is midRight
        */
        if (verbose) {
            System.out.println("rCoord[bot] = " + rCoord[bot]);
            System.out.println("rCoord[mid] = " + rCoord[mid]);
            System.out.println("rCoord[top] = " + rCoord[top]);
        }

        if (midIsRight) {
            if (rCoord[bot] < rCoord[mid]) {
                tesselateBottomTri(triGroup, orderDir, tg, textures, pt[bot],
                    rCoord[bot], midCalc, pt[mid], rCoord[mid]);
            }

            if (rCoord[mid] < rCoord[top]) {
                tesselateTopTri(triGroup, orderDir, tg, textures, midCalc,
                    pt[mid], rCoord[mid], pt[top], rCoord[top]);
            }
        } else {
            if (rCoord[bot] < rCoord[mid]) {
                tesselateBottomTri(triGroup, orderDir, tg, textures, pt[bot],
                    rCoord[bot], pt[mid], midCalc, rCoord[mid]);
            }

            if (rCoord[mid] < rCoord[top]) {
                tesselateTopTri(triGroup, orderDir, tg, textures, pt[mid],
                    midCalc, rCoord[mid], pt[top], rCoord[top]);
            }
        }

        if (verbose) {
            int numChildren = triGroup.numChildren();
            System.out.println("triGroup has " + numChildren + " children");

            for (int i = 0; i < numChildren; i++) {
                System.out.println(i + ": " + triGroup.getChild(i));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param triGroup DOCUMENT ME!
     * @param orderDir DOCUMENT ME!
     * @param tg DOCUMENT ME!
     * @param textures DOCUMENT ME!
     * @param midLeft DOCUMENT ME!
     * @param midRight DOCUMENT ME!
     * @param rMid DOCUMENT ME!
     * @param top DOCUMENT ME!
     * @param rTop DOCUMENT ME!
     */
    private void tesselateTopTri(OrderedGroup triGroup, int orderDir,
        TexCoordGeneration tg, Texture2D[] textures, Point3d midLeft,
        Point3d midRight, double rMid, Point3d top, double rTop) {
        if (verbose) {
            System.out.println("tesselateTopTri");
            System.out.println("midLeft = " + midLeft);
            System.out.println("midRight = " + midRight);
            System.out.println("rMid = " + rMid);
            System.out.println("top = " + top);
            System.out.println("rTop = " + rTop);
        }

        // moving from mid to top, we'll have a quad from mid to first
        // cut, quads in the middle between cuts and then a tri at the top
        // between the last cut and the top
        // these are the texture index and weight for the lower and upper
        // limits of the current segment
        int lowerIndex = (int) Math.floor(rMid);
        double lowerWeight = rMid - lowerIndex;

        int upperIndex = (int) Math.floor(rTop);
        double upperWeight = rTop - upperIndex;

        // check for boundary case
        if (upperWeight < 0.0001) {
            upperIndex -= 1;
            upperWeight = 1.0;
        }

        if (verbose) {
            System.out.println("lowerIndex = " + lowerIndex);
            System.out.println("lowerWeight = " + lowerWeight);
            System.out.println("upperIndex = " + upperIndex);
            System.out.println("upperWeight = " + upperWeight);
        }

        // check for no-cut case first
        if (upperIndex == lowerIndex) {
            // output the tri directly
            if (verbose) {
                System.out.println("TopTri: single tri");
            }

            outputTopTri(triGroup, orderDir, tg, textures, lowerIndex, midLeft,
                midRight, lowerWeight, top, upperWeight);
        } else {
            if (verbose) {
                System.out.println("Top tri: bottom quad");
            }

            // at least one cut.  Figure out lower cut point, output bottom
            intersect(upperLeftCut, midLeft, rMid, top, rTop,
                (double) lowerIndex + 1);
            intersect(upperRightCut, midRight, rMid, top, rTop,
                (double) lowerIndex + 1);

            if (verbose) {
                System.out.println("upperLeftCut = " + upperLeftCut);
                System.out.println("upperRightCut = " + upperRightCut);
            }

            // output quad
            outputQuad(triGroup, orderDir, tg, textures, lowerIndex, midLeft,
                midRight, lowerWeight, upperLeftCut, upperRightCut, 1.0);

            // if more than one cut, find uppermost cut
            if (verbose) {
                System.out.println("Top tri: cut quads");
            }

            if ((upperIndex - lowerIndex) > 1) {
                // output middle cut quads
                outputCutQuads(triGroup, orderDir, tg, textures,
                    lowerIndex + 1, upperIndex, midLeft, midRight, rMid, top,
                    top, rTop);
            }

            if (verbose) {
                System.out.println("Top tri: top tri");
            }

            intersect(lowerLeftCut, midLeft, rMid, top, rTop,
                (double) upperIndex);
            intersect(lowerRightCut, midRight, rMid, top, rTop,
                (double) upperIndex);

            if (verbose) {
                System.out.println("lowerLeftCut = " + lowerLeftCut);
                System.out.println("lowerRightCut = " + lowerRightCut);
            }

            outputTopTri(triGroup, orderDir, tg, textures, upperIndex,
                lowerLeftCut, lowerRightCut, 0.0, top, upperWeight);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param triGroup DOCUMENT ME!
     * @param orderDir DOCUMENT ME!
     * @param tg DOCUMENT ME!
     * @param textures DOCUMENT ME!
     * @param bottom DOCUMENT ME!
     * @param rBottom DOCUMENT ME!
     * @param midLeft DOCUMENT ME!
     * @param midRight DOCUMENT ME!
     * @param rMid DOCUMENT ME!
     */
    private void tesselateBottomTri(OrderedGroup triGroup, int orderDir,
        TexCoordGeneration tg, Texture2D[] textures, Point3d bottom,
        double rBottom, Point3d midLeft, Point3d midRight, double rMid) {
        if (verbose) {
            System.out.println("bottom = " + bottom);
            System.out.println("rBottom = " + rBottom);
            System.out.println("midLeft = " + midLeft);
            System.out.println("midRight = " + midRight);
            System.out.println("rMid = " + rMid);
        }

        // moving from bottom to mid, we'll have a tri from bottom to first
        // cut, quads in the middle between cuts and then a quad at the top
        // between the last cut and the mid's
        // these are the texture index and weight for the lower and upper
        // limits of the current segment
        int lowerIndex = (int) Math.floor(rBottom);
        double lowerWeight = rBottom - lowerIndex;

        int upperIndex = (int) Math.floor(rMid);
        double upperWeight = rMid - upperIndex;

        // check for boundary case
        if (upperWeight < 0.0001) {
            upperIndex -= 1;
            upperWeight = 1.0;
        }

        if (verbose) {
            System.out.println("lowerIndex = " + lowerIndex);
            System.out.println("lowerWeight = " + lowerWeight);
            System.out.println("upperIndex = " + upperIndex);
            System.out.println("upperWeight = " + upperWeight);
        }

        // check for no-cut case first
        if (upperIndex == lowerIndex) {
            // output the tri directly
            if (verbose) {
                System.out.println("BottomTri: single tri");
            }

            outputBottomTri(triGroup, orderDir, tg, textures, lowerIndex,
                bottom, lowerWeight, midLeft, midRight, upperWeight);
        } else {
            if (verbose) {
                System.out.println("BottomTri: bottom tri");
            }

            // at least one cut.  Figure out lower cut point, output bottom
            intersect(upperLeftCut, bottom, rBottom, midLeft, rMid,
                (double) lowerIndex + 1);
            intersect(upperRightCut, bottom, rBottom, midRight, rMid,
                (double) lowerIndex + 1);

            if (verbose) {
                System.out.println("upperLeftCut = " + upperLeftCut);
                System.out.println("upperRightCut = " + upperRightCut);
            }

            outputBottomTri(triGroup, orderDir, tg, textures, lowerIndex,
                bottom, lowerWeight, upperLeftCut, upperRightCut, 1.0);

            if (verbose) {
                System.out.println("BottomTri: Cut Quad(s):");
            }

            // if more than one cut, find uppermost cut
            if ((upperIndex - lowerIndex) > 1) {
                // output middle cut quads
                outputCutQuads(triGroup, orderDir, tg, textures,
                    lowerIndex + 1, upperIndex, bottom, bottom, rBottom,
                    midLeft, midRight, rMid);
            }

            if (verbose) {
                System.out.println("BottomTri: Top Quad:");
            }

            // find the topmost quad
            intersect(lowerLeftCut, bottom, rBottom, midLeft, rMid,
                (double) upperIndex);
            intersect(lowerRightCut, bottom, rBottom, midRight, rMid,
                (double) upperIndex);

            if (verbose) {
                System.out.println("lowerLeftCut = " + lowerLeftCut);
                System.out.println("lowerRightCut = " + lowerRightCut);
            }

            // output quad
            outputQuad(triGroup, orderDir, tg, textures, upperIndex,
                lowerLeftCut, lowerRightCut, 0.0, midLeft, midRight, upperWeight);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param triGroup DOCUMENT ME!
     * @param orderDir DOCUMENT ME!
     * @param tg DOCUMENT ME!
     * @param textures DOCUMENT ME!
     * @param lowerIndex DOCUMENT ME!
     * @param upperIndex DOCUMENT ME!
     * @param bottomLeft DOCUMENT ME!
     * @param bottomRight DOCUMENT ME!
     * @param rBottom DOCUMENT ME!
     * @param topLeft DOCUMENT ME!
     * @param topRight DOCUMENT ME!
     * @param rTop DOCUMENT ME!
     */
    private void outputCutQuads(OrderedGroup triGroup, int orderDir,
        TexCoordGeneration tg, Texture2D[] textures, int lowerIndex,
        int upperIndex, Point3d bottomLeft, Point3d bottomRight,
        double rBottom, Point3d topLeft, Point3d topRight, double rTop) {
        if (verbose) {
            System.out.println("outputCutQuads lowerIndex = " + lowerIndex +
                " upperIndex = " + upperIndex);
        }

        intersect(lowerLeftCut, bottomLeft, rBottom, topLeft, rTop,
            (double) lowerIndex);
        intersect(lowerRightCut, bottomRight, rBottom, topRight, rTop,
            (double) lowerIndex);

        if (verbose) {
            System.out.println("lowerLeftCut = " + lowerLeftCut);
            System.out.println("lowerRightCut = " + lowerRightCut);
        }

        for (int index = lowerIndex; index < upperIndex; index++) {
            intersect(upperLeftCut, bottomLeft, rBottom, topLeft, rTop,
                (double) index + 1);
            intersect(upperRightCut, bottomRight, rBottom, topRight, rTop,
                (double) index + 1);

            if (verbose) {
                System.out.println("upperLeftCut = " + upperLeftCut);
                System.out.println("upperRightCut = " + upperRightCut);
            }

            outputQuad(triGroup, orderDir, tg, textures, index, lowerLeftCut,
                lowerRightCut, 0.0, upperLeftCut, upperRightCut, 1.0);

            // copy the upper into the lower for the next strip
            lowerLeftCut.set(upperLeftCut);
            lowerRightCut.set(upperRightCut);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param numPoints DOCUMENT ME!
     * @param texIndex DOCUMENT ME!
     */
    private void printShape(int numPoints, int texIndex) {
        System.out.println("Shape with texture[" + texIndex + "] = ");

        for (int i = 0; i < numPoints; i++) {
            System.out.println("\t" + shapePts[i] + "\n\t\t" + shapeColrs[i]);
        }
    }

    // output routines:  the weights are the fraction of the top texture
    /**
     * DOCUMENT ME!
     *
     * @param triGroup DOCUMENT ME!
     * @param orderDir DOCUMENT ME!
     * @param tg DOCUMENT ME!
     * @param textures DOCUMENT ME!
     * @param lowerIndex DOCUMENT ME!
     * @param lower DOCUMENT ME!
     * @param lowerWeight DOCUMENT ME!
     * @param upperLeft DOCUMENT ME!
     * @param upperRight DOCUMENT ME!
     * @param upperWeight DOCUMENT ME!
     */
    private void outputBottomTri(OrderedGroup triGroup, int orderDir,
        TexCoordGeneration tg, Texture2D[] textures, int lowerIndex,
        Point3d lower, double lowerWeight, Point3d upperLeft,
        Point3d upperRight, double upperWeight) {
        shapePts[0] = lower;
        colrOneMinusWeight(shapeColrs[0], lowerWeight);
        shapePts[1] = upperRight;
        colrOneMinusWeight(shapeColrs[1], upperWeight);
        shapePts[2] = upperLeft;
        colrOneMinusWeight(shapeColrs[2], upperWeight);

        if (verbose) {
            printShape(3, lowerIndex);
        }

        outputShape(triGroup, orderDir, tg, textures[lowerIndex], 3, shapePts,
            shapeColrs);

        // shift the colrs for the other layer
        colrWeight(shapeColrs[0], lowerWeight);
        colrWeight(shapeColrs[1], upperWeight);
        colrWeight(shapeColrs[2], upperWeight);

        if (verbose) {
            printShape(3, lowerIndex + 1);
        }

        Texture2D tex = null;

        if ((lowerIndex + 1) < textures.length) {
            tex = textures[lowerIndex + 1];
        }

        outputShape(triGroup, orderDir, tg, tex, 3, shapePts, shapeColrs);
    }

    /**
     * DOCUMENT ME!
     *
     * @param triGroup DOCUMENT ME!
     * @param orderDir DOCUMENT ME!
     * @param tg DOCUMENT ME!
     * @param textures DOCUMENT ME!
     * @param lowerIndex DOCUMENT ME!
     * @param lowerLeft DOCUMENT ME!
     * @param lowerRight DOCUMENT ME!
     * @param lowerWeight DOCUMENT ME!
     * @param upper DOCUMENT ME!
     * @param upperWeight DOCUMENT ME!
     */
    private void outputTopTri(OrderedGroup triGroup, int orderDir,
        TexCoordGeneration tg, Texture2D[] textures, int lowerIndex,
        Point3d lowerLeft, Point3d lowerRight, double lowerWeight,
        Point3d upper, double upperWeight) {
        shapePts[0] = upper;
        colrOneMinusWeight(shapeColrs[0], upperWeight);
        shapePts[1] = lowerLeft;
        colrOneMinusWeight(shapeColrs[1], lowerWeight);
        shapePts[2] = lowerRight;
        colrOneMinusWeight(shapeColrs[2], lowerWeight);

        if (verbose) {
            printShape(3, lowerIndex);
        }

        outputShape(triGroup, orderDir, tg, textures[lowerIndex], 3, shapePts,
            shapeColrs);

        // shift the colrs for the other layer
        colrWeight(shapeColrs[0], upperWeight);
        colrWeight(shapeColrs[1], lowerWeight);
        colrWeight(shapeColrs[2], lowerWeight);

        if (verbose) {
            printShape(3, lowerIndex + 1);
        }

        Texture2D tex = null;

        if ((lowerIndex + 1) < textures.length) {
            tex = textures[lowerIndex + 1];
        }

        outputShape(triGroup, orderDir, tg, tex, 3, shapePts, shapeColrs);
    }

    /**
     * DOCUMENT ME!
     *
     * @param triGroup DOCUMENT ME!
     * @param orderDir DOCUMENT ME!
     * @param tg DOCUMENT ME!
     * @param textures DOCUMENT ME!
     * @param lowerIndex DOCUMENT ME!
     * @param lowerLeft DOCUMENT ME!
     * @param lowerRight DOCUMENT ME!
     * @param lowerWeight DOCUMENT ME!
     * @param upperLeft DOCUMENT ME!
     * @param upperRight DOCUMENT ME!
     * @param upperWeight DOCUMENT ME!
     */
    private void outputQuad(OrderedGroup triGroup, int orderDir,
        TexCoordGeneration tg, Texture2D[] textures, int lowerIndex,
        Point3d lowerLeft, Point3d lowerRight, double lowerWeight,
        Point3d upperLeft, Point3d upperRight, double upperWeight) {
        shapePts[0] = lowerLeft;
        colrOneMinusWeight(shapeColrs[0], lowerWeight);
        shapePts[1] = lowerRight;
        colrOneMinusWeight(shapeColrs[1], lowerWeight);
        shapePts[2] = upperRight;
        colrOneMinusWeight(shapeColrs[2], upperWeight);
        shapePts[3] = upperLeft;
        colrOneMinusWeight(shapeColrs[3], upperWeight);

        if (verbose) {
            printShape(4, lowerIndex);
        }

        Texture2D tex = null;

        if (lowerIndex < textures.length) {
            tex = textures[lowerIndex];
        }

        outputShape(triGroup, orderDir, tg, tex, 4, shapePts, shapeColrs);

        // shift the colrs for the other layer
        colrWeight(shapeColrs[0], lowerWeight);
        colrWeight(shapeColrs[1], lowerWeight);
        colrWeight(shapeColrs[2], upperWeight);
        colrWeight(shapeColrs[3], upperWeight);

        if (verbose) {
            printShape(4, lowerIndex + 1);
        }

        tex = null;

        if ((lowerIndex + 1) < textures.length) {
            tex = textures[lowerIndex + 1];
        }

        outputShape(triGroup, orderDir, tg, tex, 4, shapePts, shapeColrs);
    }

    /**
     * DOCUMENT ME!
     *
     * @param triGroup DOCUMENT ME!
     * @param orderDir DOCUMENT ME!
     * @param tg DOCUMENT ME!
     * @param texture DOCUMENT ME!
     * @param numPts DOCUMENT ME!
     * @param pts DOCUMENT ME!
     * @param colrs DOCUMENT ME!
     */
    private void outputShape(OrderedGroup triGroup, int orderDir,
        TexCoordGeneration tg, Texture2D texture, int numPts, Point3d[] pts,
        Color4f[] colrs) {
        count[0] = numPts;

        TriangleFanArray pgonGeo = new TriangleFanArray(numPts,
                TriangleFanArray.COORDINATES | TriangleFanArray.COLOR_4, count);
        pgonGeo.setCoordinates(0, pts, 0, numPts);
        pgonGeo.setColors(0, colrs, 0, numPts);

        Appearance appearance = new Appearance();

        if (texture != null) {
            appearance.setTextureAttributes(texAttr);
            appearance.setTexture(texture);
        }

        appearance.setMaterial(m);
        appearance.setColoringAttributes(clr);
        appearance.setTransparencyAttributes(trans);
        appearance.setPolygonAttributes(p);
        appearance.setTexCoordGeneration(tg);
        appearance.setRenderingAttributes(r);

        Shape3D shape = new Shape3D(pgonGeo, appearance);

        Node child = shape;

        if (outputLines) {
            Group shapeGroup = new Group();
            shapeGroup.addChild(shape);
            count[0] = numPts + 1;
            pts[numPts] = pts[0];

            LineStripArray lineGeo = new LineStripArray(numPts + 1,
                    LineStripArray.COORDINATES, count);
            lineGeo.setCoordinates(0, pts, 0, numPts + 1);

            Appearance lineAppearance = new Appearance();
            Shape3D lineShape = new Shape3D(lineGeo, lineAppearance);
            shapeGroup.addChild(lineShape);
            child = shapeGroup;
        }

        if (verbose) {
            System.out.println("shape is " + child);
        }

        if (orderDir == FRONT) {
            triGroup.insertChild(child, 0);
        } else {
            triGroup.addChild(child);
        }
    }

    // result == pt between lower and upper with R=desiredR
    /**
     * DOCUMENT ME!
     *
     * @param result DOCUMENT ME!
     * @param lowerPt DOCUMENT ME!
     * @param lowerR DOCUMENT ME!
     * @param upperPt DOCUMENT ME!
     * @param upperR DOCUMENT ME!
     * @param desiredR DOCUMENT ME!
     */
    private void intersect(Point3d result, Point3d lowerPt, double lowerR,
        Point3d upperPt, double upperR, double desiredR) {
        double deltaR = upperR - lowerR;
        double desiredDeltaR = desiredR - lowerR;
        double fraction = 0.0;

        if (deltaR > 0.00001) {
            fraction = desiredDeltaR / deltaR;

            if (fraction > 1.0) {
                fraction = 1.0;
            }
        }

        result.x = ((1.0 - fraction) * lowerPt.x) + (fraction * upperPt.x);
        result.y = ((1.0 - fraction) * lowerPt.y) + (fraction * upperPt.y);
        result.z = ((1.0 - fraction) * lowerPt.z) + (fraction * upperPt.z);
    }

    /**
     * DOCUMENT ME!
     *
     * @param colr DOCUMENT ME!
     * @param weight DOCUMENT ME!
     */
    private void colrWeight(Color4f colr, double weight) {
        colr.x = colr.y = colr.z = colr.w = (float) weight;
    }

    /**
     * DOCUMENT ME!
     *
     * @param colr DOCUMENT ME!
     * @param weight DOCUMENT ME!
     */
    private void colrOneMinusWeight(Color4f colr, double weight) {
        colr.x = colr.y = colr.z = colr.w = (float) (1.0 - weight);
    }
}
