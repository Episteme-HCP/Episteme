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

package org.jscience.astronomy.solarsystem.constellations;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.Vector;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.J3DGraphics2D;

import javax.vecmath.Point2d;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class ConstellationsSky3D extends Canvas3D {
    /** DOCUMENT ME! */
    J3DGraphics2D g;

    /** DOCUMENT ME! */
    Graphics2D h;

    /** DOCUMENT ME! */
    Vector stars = null;

    /** DOCUMENT ME! */
    BufferedImage im;

    /** DOCUMENT ME! */
    int width;

    /** DOCUMENT ME! */
    int height;

/**
     * Creates a new Sky3D object.
     */
    public ConstellationsSky3D() {
        super(com.sun.j3d.utils.universe.SimpleUniverse.getPreferredConfiguration());
    }

    /**
     * DOCUMENT ME!
     */
    public void postRender() {
        if (stars == null) {
            return;
        }

        //super.postRender();
        g = getGraphics2D();

        if ((getWidth() != width) || (getHeight() != height)) {
            width = getWidth();
            height = getHeight();
            im = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            h = (Graphics2D) im.getGraphics();
            h.setBackground(new Color(0, 0, 0, 0));

            //h.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            //		       RenderingHints.VALUE_ANTIALIAS_ON);
            //h.drawString("This is an overlay test.", 10, 10);
            System.out.println(getWidth() + " X " + getHeight());
        } //if

        h.clearRect(0, 0, width, height);

        //h.fillRect(width-10, height-10, 10, 10);
        //h.dispose();
        if (stars != null) {
            String s;
            Point2d p;
            h.setColor(Color.white);

            for (int i = 0; i < stars.size(); i += 2) {
                s = (String) stars.elementAt(i);
                p = (Point2d) stars.elementAt(i + 1);
                h.drawString(s, (int) p.x + 7, (int) p.y + 5);
                h.drawOval((int) p.x - 4, (int) p.y - 4, 9, 9);
            } //for
        } //if

        g.drawAndFlushImage(im, 0, 0, this);

        g.dispose();
    } //postRender

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     */
    public void setStars(Vector v) {
        stars = v;
    } //setStars
}
