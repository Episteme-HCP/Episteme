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

import javax.media.j3d.Shape3D;

import javax.vecmath.Point3f;


/**
 * This is a abstract class for the orbit of the planet.
 *
 * @author Marcel Portner & Bernhard Hari
 * @version $Revision: 1.3 $
 *
 * @see ColorPointOrbit
 * @see ColorLineOrbit
 */
public abstract class ColorOrbit extends Shape3D {
    /** DOCUMENT ME! */
    protected Color orbitColor;

    /** DOCUMENT ME! */
    protected int nbOfPoints;

/**
     * Initializes a new ColorOrbit.
     *
     * @param orbitColor the awt Color for the orbit.
     */
    public ColorOrbit(Color orbitColor) {
        this.orbitColor = orbitColor;
    }

    /**
     * This method set the color orbit with the given positions.
     *
     * @param pos the positions of the orbit.
     */
    public abstract void setPositions(Point3f[] pos);

    /**
     * This method re - set the color of the orbit.
     *
     * @param orbitColor the awt Color for the orbit.
     */
    public abstract void setColor(Color orbitColor);
}
