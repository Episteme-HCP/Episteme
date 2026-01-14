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

import java.awt.*;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineStripArray;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;


/**
 * This is a class for the color line orbit of the planet.
 *
 * @author Marcel Portner & Bernhard Hari
 * @version $Revision: 1.2 $
 */
public class ColorLineOrbit extends ColorOrbit {
    /** DOCUMENT ME! */
    private LineStripArray orbit;

/**
     * Initializes a new ColorLineOrbit.
     *
     * @param orbitColor the awt Color for the orbit.
     */
    public ColorLineOrbit(Color orbitColor) {
        super(orbitColor);
    }

    /**
     * This method set the color orbit with the given positions.
     *
     * @param pos the positions of the orbit.
     */
    public void setPositions(Point3f[] pos) {
        nbOfPoints = pos.length + 1;

        Color3f[] colors = new Color3f[nbOfPoints];
        Color3f color = new Color3f(orbitColor);

        for (int i = 0; i < nbOfPoints; i++) {
            colors[i] = color;
        }

        int[] stripVertexCounts = { nbOfPoints };

        orbit = new LineStripArray(nbOfPoints,
                GeometryArray.COORDINATES | /*GeometryArray.NORMALS |*/
            GeometryArray.COLOR_3, stripVertexCounts);

        orbit.setCoordinates(0, pos);
        orbit.setCoordinate(nbOfPoints - 1, pos[0]);
        orbit.setColors(0, colors);
        orbit.setCapability(GeometryArray.ALLOW_COLOR_WRITE);

        this.setGeometry(orbit);
    }

    /**
     * This method re - set the color of the orbit.
     *
     * @param orbitColor the awt Color for the orbit.
     */
    public void setColor(Color orbitColor) {
        this.orbitColor = orbitColor;

        Color3f[] colors = new Color3f[nbOfPoints];
        Color3f color = new Color3f(orbitColor);

        for (int i = 0; i < nbOfPoints; i++) {
            colors[i] = color;
        }

        orbit.setColors(0, colors);
    }
}
