/*
 *      @(#)LocalToWindow.java 1.1 00/09/20 15:50:35
 *
 * Copyright (c) 1999 Sun Microsystems, Inc. All Rights Reserved.
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
/**
 * Utility class for doing local->window transformations for case where
 * Canvas3D is a simple display such as a monitor. This won't work for the
 * more complex cases (i.e. a multiple canvases, head tracking, etc).
 *
 * Usage:
 *    // after the canvas and node are created
 *    LocalToWindow locToWindow = LocalToWindow(node, canvas);
 *    ...
 *    // when we need to transform (canvas location and node transforms may have
 *    // changed)
 *    locToWindow.update(); // make sure transforms are up to date
 *
 *    Point3d[] localPts = <some local coords to transform >
 *    Point[] windowPts = <the area to put the tranformed pts >
 *    for (int i = 0; i < localPts.length; i++) {
 *       locToWindow.transformPt(localPts[i], windowPts[i]);
 *    }
 */

// standard j3d packages
package org.jscience.medicine.volumetric;

import java.awt.*;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.View;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class LocalToWindow {
    /**
     * DOCUMENT ME!
     */
    Canvas3D canvas = null;

    /**
     * DOCUMENT ME!
     */
    Node node = null;

    // inquired/derived data
    /**
     * DOCUMENT ME!
     */
    Transform3D localToVworld = new Transform3D();

    /**
     * DOCUMENT ME!
     */
    Transform3D vworldToImagePlate = new Transform3D();

    /**
     * DOCUMENT ME!
     */
    Transform3D localToImagePlate = new Transform3D();

    /**
     * DOCUMENT ME!
     */
    Point3d eyePos = new Point3d();

    /**
     * DOCUMENT ME!
     */
    int projType;

    /**
     * DOCUMENT ME!
     */
    Point canvasScr;

    /**
     * DOCUMENT ME!
     */
    Dimension screenSize;

    /**
     * DOCUMENT ME!
     */
    double metersPerPixelX;

    /**
     * DOCUMENT ME!
     */
    double metersPerPixelY;

    // Temporaries
    /**
     * DOCUMENT ME!
     */
    Point3d imagePlatePt = new Point3d();

    /**
     * DOCUMENT ME!
     */
    Vector3d projVec = new Vector3d();

    /**
     * DOCUMENT ME!
     */
    Point2d screenPt = new Point2d();

    /**
     * DOCUMENT ME!
     */
    Point2d tempPt2d = new Point2d();

/**
     * Creates a LocalToWindow object with no associated node or canvas.
     * The node and canvas must be set before transforming points
     */
    public LocalToWindow() {
    }

/**
     * Called with the Node which specifies the local coordinates for the
     * points to be transformed and the Canvas3D where the points are
     * displayed
     */
    public LocalToWindow(Node node, Canvas3D canvas) {
        this.canvas = canvas;
        this.node = node;
        update();
    }

    /**
     * Either create LocalToWindow() just before transforming points or
     * call this method to ensure that the transforms are up to date.  Note:
     * if you are transforming several points, you only need to call this
     * method once.
     */
    public void update() {
        if ((this.canvas != null) && (this.node != null)) {
            node.getLocalToVworld(localToVworld);
            canvas.getVworldToImagePlate(vworldToImagePlate);

            // Make a composite transform:
            // vWorldPt = LocalToVworld * localPt;
            // imagePlatePt = VworldToImagePlate * vWorldPt;
            // imagePlatePt = VworldToImagePlate * LocalToVworld * localPt;
            localToImagePlate.mul(vworldToImagePlate, localToVworld);

            // we need these to project the point from Image Plate coords to
            // the actual image plate (i.e. perpsective)
            canvas.getCenterEyeInImagePlate(eyePos);

            //System.out.println("eyePos = " + eyePos);
            projType = canvas.getView().getProjectionPolicy();

            // this stuff is to go from image plate coords to window coords
            canvasScr = canvas.getLocationOnScreen();

            //System.out.println("canvasScr = " + canvasScr);
            screenSize = canvas.getScreen3D().getSize();

            double physicalScreenWidth = canvas.getScreen3D()
                                               .getPhysicalScreenWidth();
            double physicalScreenHeight = canvas.getScreen3D()
                                                .getPhysicalScreenHeight();
            metersPerPixelX = physicalScreenWidth / (double) screenSize.width;
            metersPerPixelY = physicalScreenHeight / (double) screenSize.height;
        }
    }

    /**
     * Set the node and canvas and call update()
     *
     * @param node DOCUMENT ME!
     * @param canvas DOCUMENT ME!
     */
    public void update(Node node, Canvas3D canvas) {
        this.canvas = canvas;
        this.node = node;
        update();
    }

    /**
     * Transform the point from local coords to window coords
     *
     * @param localPt DOCUMENT ME!
     * @param windowPt DOCUMENT ME!
     */
    public void transformPt(Point3d localPt, Point2d windowPt) {
        // TODO: throw some kind of error if node and canvas haven't been
        // set
        //System.out.println("vWorld Pt = " + localPt);
        localToImagePlate.transform(localPt, imagePlatePt);

        //System.out.println("imagePlatePt = " + imagePlatePt);
        double zScale = 1.0; // default, used for PARALELL_PROJECTION

        if (projType == View.PERSPECTIVE_PROJECTION) {
            // get the vector from eyePos to imagePlatePt
            projVec.sub(imagePlatePt, eyePos);

            // Scale this vector to make it end at the projection plane.
            // Scale is ratio :
            //     eye->imagePlate Plane dist  / eye->imagePlatePt dist
            // eye dist to plane is eyePos.z (eye is in +z space)
            // image->eye dist is -projVec.z (image->eye is in -z dir)
            //System.out.println("eye dist = " + (eyePos.z));
            //System.out.println("image dist = " + (-projVec.z));
            zScale = eyePos.z / (-projVec.z);

            screenPt.x = eyePos.x + (projVec.x * zScale);
            screenPt.y = eyePos.y + (projVec.y * zScale);
        } else {
            screenPt.x = imagePlatePt.x;
            screenPt.y = imagePlatePt.y;
        }

        //System.out.println("screenPt = " + screenPt);
        // Note: screenPt is in image plate coords, at z=0
        // Transform from image plate coords to screen coords
        windowPt.x = (screenPt.x / metersPerPixelX) - canvasScr.x;
        windowPt.y = screenSize.height - 1 - (screenPt.y / metersPerPixelY) -
            canvasScr.y;

        //System.out.println("windowPt = " + windowPt);
    }

    /**
     * Transform the point from local coords to window coords
     *
     * @param localPt DOCUMENT ME!
     * @param windowPt DOCUMENT ME!
     */
    public void transformPt(Point3d localPt, Point windowPt) {
        transformPt(localPt, tempPt2d);
        windowPt.x = (int) Math.round(tempPt2d.x);
        windowPt.y = (int) Math.round(tempPt2d.y);
    }
}
