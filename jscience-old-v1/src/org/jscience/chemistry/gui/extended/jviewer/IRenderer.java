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

package org.jscience.chemistry.gui.extended.jviewer;

import org.jscience.chemistry.gui.extended.molecule.Matrix3D;

import java.awt.*;

import java.util.Vector;


/**
 * This class defines an interface which allows the JViewer to render and
 * manipulate any graphics object which implements this interface.
 * <p/>
 * Note: Only the reference to the Tripos Java molecule's Matrix3D class keeps
 * jviewer from being a true "object" renderer.  A future effort should
 * be to move Matrix3D from molecule to JViewer.
 *
 * @author Mike Brusati (brusati@tripos.com)
 *         Original Version
 */
public interface IRenderer {
    /**
     * Return the bounding box of the object being rendered.
     *
     * @return Array of 6 floats in order of xmin, xmax, ymin, ymax, zmin,
     *         zmax. If the object has no bounding box (for example the object
     *         is a molecule with no atoms), null is returned.
     */
    public float[] getBBox();

    /**
     * Reset the object's transformation matrix to the identity matrix.
     */
    public void matUnit();

    /**
     * Apply the specified translation to the object's transformation
     * matrix.
     *
     * @param xt x component of translation
     * @param yt y component of translation
     * @param zt z component of translation
     */
    public void matTranslate(float xt, float yt, float zt);

    /**
     * Apply the specified scale to the object's transformation matrix.
     *
     * @param xf x scale factor
     * @param yf y scale factor
     * @param zf z scale factor
     */
    public void matScale(float xf, float yf, float zf);

    /**
     * Multiply the object's transformation matrix by the matrix
     * specified.
     *
     * @param rhs matrix to be multiplied to object's matrix: M = M  rhs
     */
    public void matMult(Matrix3D rhs);

    /**
     * Apply the object's transformation matrix to its points.
     */
    public void matTransform();

    /**
     * Draw the object.
     *
     * @param g the graphics context.
     */
    public void draw(Graphics g);

    /**
     * Find an object given location
     *
     * @param tx the transformed x coordinate of desired target
     * @param ty the transformed y coordinate of desired target
     *
     * @return DOCUMENT ME!
     */
    public Object findObject(float tx, float ty);

    /**
     * Find a list of objects within a given polygon
     *
     * @param polygon the polygon within which objects are searched
     *
     * @return DOCUMENT ME!
     */
    public Vector findObjects(Polygon polygon);

    /**
     * Return a copy of the current object.
     *
     * @return a copy of the current object
     */
    public Object copy();
}
