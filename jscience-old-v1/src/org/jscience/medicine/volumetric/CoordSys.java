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
import javax.vecmath.Vector3f;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class CoordSys extends TransformGroup {
    /** DOCUMENT ME! */
    double scale = 1.0;

    /** DOCUMENT ME! */
    Vector3f translation = new Vector3f();

    /** DOCUMENT ME! */
    Point3d origin = new Point3d(0.0, 0.0, 0.0);

    /** DOCUMENT ME! */
    Point3d plusX = new Point3d(1.0, 0.0, 0.0);

    /** DOCUMENT ME! */
    Point3d plusY = new Point3d(0.0, 1.0, 0.0);

    /** DOCUMENT ME! */
    Point3d plusZ = new Point3d(0.0, 0.0, 1.0);

    /** DOCUMENT ME! */
    Point3d arrowRight = new Point3d();

    /** DOCUMENT ME! */
    Point3d arrowLeft = new Point3d();

    /** DOCUMENT ME! */
    Point3d arrowUp = new Point3d();

    /** DOCUMENT ME! */
    Point3d arrowDown = new Point3d();

    /** DOCUMENT ME! */
    Point3d[] line = new Point3d[10];

/**
     * Creates a new CoordSys object.
     *
     * @param scale DOCUMENT ME!
     */
    public CoordSys(double scale) {
        this.scale = scale;
        setup();
    }

/**
     * Creates a new CoordSys object.
     *
     * @param scale       DOCUMENT ME!
     * @param translation DOCUMENT ME!
     */
    public CoordSys(double scale, Vector3d translation) {
        this.scale = scale;
        this.translation.set(translation);
        setup();
    }

/**
     * Creates a new CoordSys object.
     */
    public CoordSys() {
        setup();
    }

    /**
     * DOCUMENT ME!
     */
    private void setup() {
        // Can be used to make coordSys smaller TODO: specify in constructor
        Transform3D coordTrans = new Transform3D();
        coordTrans.setTranslation(translation);
        coordTrans.setScale(scale);
        setTransform(coordTrans);

        RenderingAttributes ra = new RenderingAttributes();
        ra.setDepthBufferEnable(true);

        LineArray xGeom = new LineArray(10, LineArray.COORDINATES);
        setupArrow(plusX, plusY, plusZ);
        xGeom.setCoordinates(0, line);

        ColoringAttributes xColoringAttributes = new ColoringAttributes(1.0f,
                0.0f, 0.0f, ColoringAttributes.SHADE_FLAT);
        Appearance xAppearance = new Appearance();
        xAppearance.setColoringAttributes(xColoringAttributes);
        xAppearance.setRenderingAttributes(ra);

        Shape3D xShape = new Shape3D(xGeom, xAppearance);
        addChild(xShape);

        LineArray yGeom = new LineArray(10, LineArray.COORDINATES);
        setupArrow(plusY, plusX, plusZ);
        yGeom.setCoordinates(0, line);

        ColoringAttributes yColoringAttributes = new ColoringAttributes(0.0f,
                1.0f, 0.0f, ColoringAttributes.SHADE_FLAT);
        Appearance yAppearance = new Appearance();
        yAppearance.setColoringAttributes(yColoringAttributes);
        yAppearance.setRenderingAttributes(ra);

        Shape3D yShape = new Shape3D(yGeom, yAppearance);
        addChild(yShape);

        LineArray zGeom = new LineArray(10, LineArray.COORDINATES);
        setupArrow(plusZ, plusX, plusY);
        zGeom.setCoordinates(0, line);

        ColoringAttributes zColoringAttributes = new ColoringAttributes(0.0f,
                0.0f, 1.0f, ColoringAttributes.SHADE_FLAT);
        Appearance zAppearance = new Appearance();
        zAppearance.setColoringAttributes(zColoringAttributes);
        zAppearance.setRenderingAttributes(ra);

        Shape3D zShape = new Shape3D(zGeom, zAppearance);
        addChild(zShape);
    }

    /**
     * DOCUMENT ME!
     *
     * @param forward DOCUMENT ME!
     * @param right DOCUMENT ME!
     * @param up DOCUMENT ME!
     */
    private void setupArrow(Point3d forward, Point3d right, Point3d up) {
        line[0] = origin;
        line[1] = forward;

        line[2] = forward;
        arrowRight.x = (forward.x * 0.9) + (right.x * 0.03);
        arrowRight.y = (forward.y * 0.9) + (right.y * 0.03);
        arrowRight.z = (forward.z * 0.9) + (right.z * 0.03);
        line[3] = arrowRight;

        line[4] = forward;
        arrowLeft.x = (forward.x * 0.9) - (right.x * 0.03);
        arrowLeft.y = (forward.y * 0.9) - (right.y * 0.03);
        arrowLeft.z = (forward.z * 0.9) - (right.z * 0.03);
        line[5] = arrowLeft;

        line[6] = forward;
        arrowUp.x = (forward.x * 0.9) + (up.x * 0.03);
        arrowUp.y = (forward.y * 0.9) + (up.y * 0.03);
        arrowUp.z = (forward.z * 0.9) + (up.z * 0.03);
        line[7] = arrowUp;

        line[8] = forward;
        arrowDown.x = (forward.x * 0.9) - (up.x * 0.03);
        arrowDown.y = (forward.y * 0.9) - (up.y * 0.03);
        arrowDown.z = (forward.z * 0.9) - (up.z * 0.03);
        line[9] = arrowDown;
    }
}
