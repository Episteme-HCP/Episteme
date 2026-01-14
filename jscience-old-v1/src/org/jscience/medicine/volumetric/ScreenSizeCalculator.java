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
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ScreenSizeCalculator {
    /** DOCUMENT ME! */
    LocalToWindow locToWindow;

    /** DOCUMENT ME! */
    Point3d localPt;

    /** DOCUMENT ME! */
    Point2d[] windowPts;

/**
     * Creates a new ScreenSizeCalculator object.
     */
    public ScreenSizeCalculator() {
        locToWindow = new LocalToWindow();
        localPt = new Point3d();
        windowPts = new Point2d[4];

        for (int i = 0; i < 4; i++) {
            windowPts[i] = new Point2d();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param canvas DOCUMENT ME!
     * @param renderNode DOCUMENT ME!
     */
    public void setScreenXform(Canvas3D canvas, Node renderNode) {
        locToWindow.update(renderNode, canvas);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pt1 DOCUMENT ME!
     * @param pt2 DOCUMENT ME!
     * @param pt3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double triArea(Point2d pt1, Point2d pt2, Point2d pt3) {
        Point2d top;
        Point2d mid;
        Point2d bot;
        double area;

        /* sort the points to find top, mid and bot in y */
        top = pt1;

        if (pt2.y > top.y) {
            top = pt2;
        }

        if (pt3.y > top.y) {
            top = pt3;
        }

        bot = pt1;

        if (pt2.y < bot.y) {
            bot = pt2;
        }

        if (pt3.y < bot.y) {
            bot = pt3;
        }

        mid = pt1;

        if ((mid == top) || (mid == bot)) {
            mid = pt2;

            if ((mid == top) || (mid == bot)) {
                mid = pt3;
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
        
        intersect top-bot edge at y value of mid
        
        */
        double deltaX = top.x - bot.x;
        double deltaY = top.y - bot.y;

        if (deltaY == 0.0) {
            return 0.0;
        }

        double intersectX = bot.x + ((deltaX * (mid.y - bot.y)) / deltaY);

        /* area is the sum of the area of the two tris */
        area = Math.abs(0.5 * (intersectX - mid.x) * (mid.y - bot.y));
        area += Math.abs(0.5 * (intersectX - mid.x) * (top.y - mid.y));

        return area;
    }

    /**
     * DOCUMENT ME!
     *
     * @param coords DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    double quadScreenSize(double[] coords) {
        double area = 0.0f;

        //System.out.println("wc pts = \n\t(" +
        //	coords[0] + ", " + coords[1] + ", " + coords[2] + ") (" +
        //	coords[3] + ", " + coords[4] + ", " + coords[5] + ")\n\t(" +
        //	coords[6] + ", " + coords[7] + ", " + coords[8] + ") (" +
        //	coords[9] + ", " + coords[10] + ", " + coords[11] + ")");
        for (int i = 0; i < 4; i++) {
            localPt.x = coords[(i * 3) + 0];
            localPt.y = coords[(i * 3) + 1];
            localPt.z = coords[(i * 3) + 2];
            locToWindow.transformPt(localPt, windowPts[i]);
        }

        //System.out.println("window pts = " + windowPts[0] + " " +
        //	windowPts[1] + " " + windowPts[2] + " " +
        //	windowPts[3]);
        area += triArea(windowPts[0], windowPts[1], windowPts[2]);
        area += triArea(windowPts[1], windowPts[2], windowPts[3]);

        return area;
    }
}
