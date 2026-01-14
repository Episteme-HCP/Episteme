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

package org.jscience.physics.waves.optics.elements;

import org.jscience.physics.waves.optics.rays.RayPoint;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Aperture extends OpticalElement {
    /** DOCUMENT ME! */
    private double radius = 25;

/**
     * Creates a new Aperture object.
     */
    public Aperture() {
        super();
    }

/**
     * Creates a new Aperture object.
     *
     * @param radius DOCUMENT ME!
     */
    public Aperture(double radius) {
        this();
        this.radius = radius;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     */
    public void setRadius(double a) {
        radius = a;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getRadius(double a) {
        return radius;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void propagateRayPointSelf(RayPoint r) {
        double distance;

        distance = -r.getPosition().x;

        if (distance < 0) {
            r.invalidate();
        } else {
            if (distance > 0) {
                r.goStraight(distance);
            }

            double y = r.getPosition().y;
            double z = r.getPosition().z;

            if (((y * y) + (z * z)) >= (radius * radius)) {
                r.invalidate();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void drawSelf(Graphics g) {
        /*int x = (int)Math.round( X() ),
            y = (int)Math.round( Axis().y + OffAxis().y ),
            top = y - (int)Math.round( radius ),
            bottom = y + (int)Math.round( radius );
        
        g.drawLine( x, top, x, top - 10 );
        
        g.drawLine( x, bottom, x, bottom + 10 );
        
        g.drawLine( x - 5, top, x + 5, top );
        g.drawLine( x - 5, bottom, x + 5, bottom );*/
        int r = (int) Math.round(radius);

        g.drawLine(0, -r, 0, -r - 10);
        g.drawLine(0, r, 0, r + 10);

        g.drawLine(-5, -r, 5, -r);
        g.drawLine(-5, r, 5, r);
    }
}
