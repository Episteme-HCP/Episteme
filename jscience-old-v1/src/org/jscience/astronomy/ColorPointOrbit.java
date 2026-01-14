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
import javax.media.j3d.PointArray;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;


/**
 * This is a class for the color point orbit of the planet.
 *
 * @author Marcel Portner & Bernhard Hari
 * @version $Revision: 1.2 $
 */
public class ColorPointOrbit extends ColorOrbit {
    /** DOCUMENT ME! */
    private PointArray orbit;

/**
     * Initializes a new ColorPointOrbit.
     *
     * @param orbitColor the awt Color for the orbit.
     */
    public ColorPointOrbit(Color orbitColor) {
        super(orbitColor);
    }

    /**
     * This method set the color orbit with the given positions.
     *
     * @param pos the positions of the orbit.
     */
    public void setPositions(Point3f[] pos) {
        nbOfPoints = pos.length;

        Color3f[] colors = new Color3f[nbOfPoints];
        Color3f color = new Color3f(orbitColor);

        for (int i = 0; i < nbOfPoints; i++) {
            colors[i] = color;
        }

        orbit = new PointArray(nbOfPoints,
                GeometryArray.COORDINATES | GeometryArray.COLOR_3);

        orbit.setCoordinates(0, pos);
        orbit.setColors(0, colors);

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
