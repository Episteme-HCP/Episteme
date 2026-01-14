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

package org.jscience.physics.waves.optics.rays;

import org.jscience.physics.waves.optics.elements.OpticalDevice;
import org.jscience.physics.waves.optics.materials.Material;

import java.awt.*;

import java.util.Enumeration;
import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class RayCastersCollection extends RayCaster {
    /** DOCUMENT ME! */
    protected Vector raycasters;

    /** DOCUMENT ME! */
    protected Material mat;

/**
     * Creates a new RayCastersCollection object.
     */
    public RayCastersCollection() {
        super();
        raycasters = new Vector(5, 5);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param d DOCUMENT ME!
     */
    public void drawRays(Graphics g, OpticalDevice d) {
        Enumeration e = raycasters.elements();

        while (e.hasMoreElements()) {
            RayCaster o = (RayCaster) e.nextElement();
            o.drawRays(g, d);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param rc DOCUMENT ME!
     */
    public void append(RayCaster rc) {
        raycasters.addElement(rc);
        rc.setInitialMaterial(mat);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mat DOCUMENT ME!
     */
    public void setInitialMaterial(Material mat) {
        this.mat = mat;

        Enumeration e = raycasters.elements();

        while (e.hasMoreElements()) {
            RayCaster o = (RayCaster) e.nextElement();
            o.setInitialMaterial(mat);
        }
    }
}
