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

import javax.media.j3d.*;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.text.NumberFormat;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
abstract public class Renderer {
    View view;
    Context context;
    Volume volume;
    boolean debug = false;
    boolean timing = false;
    NumberFormat numFormatter = null;

    /**
     * Creates a new Renderer object.
     *
     * @param vw  DOCUMENT ME!
     * @param ctx DOCUMENT ME!
     * @param vol DOCUMENT ME!
     */
    public Renderer(View vw, Context ctx, Volume vol) {
        view = vw;
        context = ctx;
        volume = vol;

        //	debug = Boolean.getBoolean("debug");
        //	timing = Boolean.getBoolean("timing");
    }

    /**
     * Attach the branchgroups for this renderer to the display
     */
    abstract public void attach(Group dynamicGroup, Group staticGroup);

    /**
     * Called to make changes to the renderer state
     */
    abstract void update();

    /**
     * Returns the number of pixels drawn in the current display
     */
    abstract public double calcRenderSize(ScreenSizeCalculator screenSize,
                                          Canvas3D canvas);

    /**
     * Called when the tranform for the volume has changed
     */
    public void transformChanged(int type, Transform3D trans) {
    }

    /**
     * Called when the view position relative to the renderer changes
     */
    public void eyePtChanged() {
    }

    /**
     * return the eye's position in <node>'s coordinate space
     */
    Point3d getViewPosInLocal(Node node) {
        Point3d viewPosition = new Point3d();
        Vector3d translate = new Vector3d();
        double angle = 0.0;
        double mag;
        double sign;
        double tx;
        double ty;
        double tz;

        if (node == null) {
            System.out.println("called getViewPosInLocal() with null node");

            return null;
        }

        if (!node.isLive()) {
            System.out.println("called getViewPosInLocal() with non-live node");

            return null;
        }

        //  get viewplatforms's location in virutal world
        Canvas3D canvas = (Canvas3D) view.getCanvas3D(0);
        canvas.getCenterEyeInImagePlate(viewPosition);

        Transform3D t = new Transform3D();
        canvas.getImagePlateToVworld(t);
        t.transform(viewPosition);

        //System.out.println("vworld view position is " + viewPosition);
        // get parent transform
        Transform3D parentInv = new Transform3D();
        node.getLocalToVworld(parentInv);

        //System.out.println("node xform is \n" + parentInv);
        parentInv.invert();

        // transform the eye position into the parent's coordinate system
        parentInv.transform(viewPosition);

        //System.out.println("node space view position is " + viewPosition);
        return viewPosition;
    }

    // format a number to two digits past the decimal
    String numFormat(double value) {
        return numFormat(value, 2);
    }

    // format a number to numDigits past the decimal
    String numFormat(double value, int numDigits) {
        if (numFormatter == null) {
            numFormatter = NumberFormat.getInstance();
        }

        numFormatter.setMaximumFractionDigits(numDigits);

        return numFormatter.format(value);
    }
}
