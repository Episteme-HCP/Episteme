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

import org.jscience.physics.waves.optics.rays.Ray;
import org.jscience.physics.waves.optics.rays.RayPoint;

import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * The <code>OpticalDevice</code> class is a child of <code>OpticalElement</code> and
 * enables the user to build a complete optical system of <code>OpticalElement</code> objects.
 * since <code>OpticalDevice</code> inherits from <code>OpticalElement</code>, devices can be
 * nested to an arbitrary depth, making it easy to split the system into functional blocks.
 * <p/>
 * The standard use of this class is to build a system using <code>OpticalElement</code>
 * subclasses into an <code>OpticalDevice</code> object and to use an
 * <code>OpticalCanvas</code> object to display it.
 *
 * @author Olivier Scherler
 * @see        OpticalDevice
 * @see        OpticalCanvas
 */

public class OpticalDevice extends OpticalElement {
    private Vector elements;

    public OpticalDevice() {
        super();
        elements = new Vector(5, 5);
    }

    public Object clone() {
        OpticalDevice o = null;

        o = (OpticalDevice) super.clone();
        o.elements = (Vector) elements.clone();
        return o;
    }

    /**
     * Draws the device by calling the <code>Draw</code> method for all of its
     * child elements.
     *
     * @param    g    the <code>Graphics</code> object into which the device must be drawn.
     * @see        OpticalElement#draw
     */
    public void draw(Graphics g) {
        super.draw(g);
        Enumeration e = elements.elements();

        while (e.hasMoreElements()) {
            OpticalElement o = (OpticalElement) e.nextElement();
            o.draw(g);
        }
    }

    /**
     * Draws the device's symbol into a <code>Graphics</code> object. Does nothing
     * since the device is drawn by drawing each of its child elements. Override this
     * method to add extra drawings to the device.
     *
     * @param    g    the <code>Graphics</code> object into which the device must be drawn.
     */
    public void drawSelf(Graphics g) {
    }

    /**
     * Draws the part of the <code>Ray</code> going through the device. The <code>DrawRay</code>
     * method is called for every element in the device.
     *
     * @param    g        the <code>Graphics</code> object in which the <code>Ray</code> must
     * be drawn.
     * @param    r        the <code>Ray</code> to draw.
     * @param    index    the index of the <code>RayPoint</code> to draw. Used to keep track of
     * the position in the <code>Ray</code> in case of multiple
     * <code>OpticalDevice</code> nesting.
     * @return the index of the next <code>RayPoint</code> to be drawn by the next element.
     * @see        Ray
     * @see        RayPoint
     */
    public int drawRay(Graphics g, Ray r, int index) {
        Enumeration e = elements.elements();
        int newindex = index;

        while (e.hasMoreElements()) {
            OpticalElement o = (OpticalElement) e.nextElement();
            newindex = o.drawRay(g, r, newindex);
        }
        return newindex;
    }

    /**
     * Propagates a <code>Ray</code> through each of its child elements, by calling
     * their <code>Propagate</code> method.
     *
     * @param    r    the <code>Ray</code> to propagate.
     * @see        OpticalElement#propagate
     */
    public void propagate(Ray r) {
        Enumeration e = elements.elements();

        while (e.hasMoreElements()) {
            OpticalElement o = (OpticalElement) e.nextElement();
            o.propagate(r);
        }
    }

    /**
     * Does nothing since all the work is done by the Device's and the child
     * elements Propagate methods.
     *
     * @param    r    the RayPoint to propagate.
     * @see        RayPoint
     * @see        #propagate
     * @see        OpticalElement#propagate
     */
    public void propagateRayPointSelf(RayPoint r) {
    }

    /**
     * Adds an element to the end of the child elements list, and calls its
     * <code>PutAfter</code> method to align it on the previous element.
     *
     * @param    e    the element to add.
     * @see        OpticalElement
     * @see        OpticalElement#putAfter
     */
    public void append(OpticalElement e) {
        if (elements.isEmpty()) // We are adding the first element of the group
        {
            e.putAfter(this);
        } else
            e.putAfter((OpticalElement) elements.lastElement());
        e.rearrange();
        elements.addElement(e);
    }

    /**
     * Realigns each child element on the previous one and sets the width of
     * the device to the cumulated width of the child elements.
     *
     * @see        OpticalElement
     * @see        OpticalElement#putAfter
     */
    public void rearrange() {
        int s = elements.size();
        OpticalElement oi, oi_1;

        setWidth(0);

        if (s > 0) {
            oi = (OpticalElement) elements.elementAt(0); // First element
            oi.putAfter(this);
            oi.rearrange();

            setWidth(getWidth() + oi.getWidth());

            for (int i = 1; i < s; i++) {
                oi = (OpticalElement) elements.elementAt(i);
                oi_1 = (OpticalElement) elements.elementAt(i - 1);
                oi.putAfter(oi_1);
                oi.rearrange();
                setWidth(getWidth() + oi.getWidth());
            }
        }
    }
}
