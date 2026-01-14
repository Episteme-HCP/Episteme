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

import java.util.Enumeration;
import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Ray {
    /** DOCUMENT ME! */
    private Vector raypoints;

    //private Color color;
    /**
     * Creates a new Ray object.
     */
    public Ray() {
        super();
        raypoints = new Vector(5, 5);

        //color = Color.blue;
    }

    /*public Color getColor()
    {
        return new Color( color.getRed(), color.getGreen(), color.getBlue() );
    }
    
    public void setColor( Color color )
    {
        this.color = new Color( color.getRed(), color.getGreen(), color.getBlue() );
    }*/
    public Ray(RayPoint r) {
        super();
        raypoints = new Vector(5, 5);
        raypoints.addElement(r);

        //color = Color.blue;
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        //RayPoint r = (RayPoint)(raypoints.firstElement());
        raypoints.setSize(1);

        //raypoints.addElement( r );
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     */
    public void setWavelength(double w) {
        Enumeration e = raypoints.elements();

        while (e.hasMoreElements()) {
            RayPoint o = (RayPoint) e.nextElement();
            o.setWavelength(w);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector raypoints() {
        return raypoints;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void append(RayPoint r) {
        raypoints.addElement(r);
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void append(Ray r) {
        int i;
        int s = r.raypoints().size();

        if (s != 0) {
            for (i = 0; i < s; i++)
                raypoints.addElement(r.raypoints().elementAt(i));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RayPoint last() {
        return (RayPoint) (raypoints.lastElement());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean empty() {
        return raypoints.isEmpty();
    }
}
