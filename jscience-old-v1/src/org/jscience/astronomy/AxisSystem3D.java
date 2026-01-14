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

package org.jscience.astronomy;

import javax.media.j3d.Geometry;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;


/**
 * This class creates a right-handed 3D coordinate system.
 *
 * @author Marcel Portner & Bernhard Hari
 * @version $Revision: 1.2 $
 */
public class AxisSystem3D extends Shape3D {
    /** Definition of the geometry of the three axis. */
    private static final float[] EXTREMITES = {
            // x-axis
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            
            // y-axis
            0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
            
            // z-axis
            0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f
        };

    /** Colors of the three axis. */
    private static final float[] COLOR = {
            // x-axis
            1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
            
            // y-axis
            0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            
            // z-axis
            1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f
        };

    /** The scale factor */
    private static final float SCALE = 1.0f;

    /** DOCUMENT ME! */
    private float scale;

/**
     * Initializes a new coordinate system with the length of the axis to one
     * meter.
     */
    public AxisSystem3D() {
        this(SCALE);
    }

/**
     * Initializes a new coordinate system.
     *
     * @param scale the scale factor to adjust the axis's length in meter.
     */
    public AxisSystem3D(float scale) {
        this.scale = scale;
        this.setGeometry(createGeometry());
    }

    /**
     * Returns the Geometry of the coordinate system.
     *
     * @return the Geometry of a coordinate system. The axis has three
     *         different colors and is scaled.
     */
    private Geometry createGeometry() {
        // Construction of the axis (LineArray).
        LineArray axis = new LineArray(6,
                LineArray.COORDINATES | LineArray.COLOR_3);

        // Scalling of the vertices of the 3 axis using scale.
        float[] scaledExtremites = new float[EXTREMITES.length];

        for (int i = 0; i < EXTREMITES.length; i++) {
            scaledExtremites[i] = EXTREMITES[i] * scale;
        }

        axis.setCoordinates(0, scaledExtremites);
        axis.setColors(0, COLOR);

        return axis;
    }
}
