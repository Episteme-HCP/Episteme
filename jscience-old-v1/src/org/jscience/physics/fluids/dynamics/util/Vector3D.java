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

/*
 * Vector3D.java
 *
 * Created on 28 of julio of 2001, 17:55
 */
package org.jscience.physics.fluids.dynamics.util;

/**
 * DOCUMENT ME!
 *
 * @author balrog
 * @version 2.0
 */
public class Vector3D {
    /** DOCUMENT ME! */
    public double x;

    /** DOCUMENT ME! */
    public double y;

    /** DOCUMENT ME! */
    public double z;

/**
     * Creates new Vector3D
     *
     * @param ax DOCUMENT ME!
     * @param ay DOCUMENT ME!
     * @param az DOCUMENT ME!
     */
    public Vector3D(double ax, double ay, double az) {
        x = ax;
        y = ay;
        z = az;
    }
}
