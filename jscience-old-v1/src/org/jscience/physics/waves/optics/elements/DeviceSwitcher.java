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

import java.util.Hashtable;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class DeviceSwitcher extends OpticalElement {
    /** DOCUMENT ME! */
    private Hashtable devices = new Hashtable();

    /** DOCUMENT ME! */
    private String currentDevice = "";

    /** DOCUMENT ME! */
    private boolean DefaultExists = false;

/**
     * Creates a new DeviceSwitcher object.
     */
    public DeviceSwitcher() {
        super();
    }

/**
     * Creates a new DeviceSwitcher object.
     *
     * @param d DOCUMENT ME!
     */
    public DeviceSwitcher(OpticalElement d) {
        this("Default", d);
    }

/**
     * Creates a new DeviceSwitcher object.
     *
     * @param l DOCUMENT ME!
     * @param d DOCUMENT ME!
     */
    public DeviceSwitcher(String l, OpticalElement d) {
        if (d != null) {
            devices.put(l.toLowerCase(), d);
            currentDevice = l.toLowerCase();
            DefaultExists = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public void setCurrentDevice(String l) {
        currentDevice = l.toLowerCase();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCurrentDevice() {
        return currentDevice;
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     * @param d DOCUMENT ME!
     */
    public void addDevice(String l, OpticalElement d) {
        devices.put(l.toLowerCase(), d);

        if (!DefaultExists) {
            currentDevice = l.toLowerCase();
            DefaultExists = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param r DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int drawRay(Graphics g, Ray r, int index) {
        int newindex = index;
        OpticalElement e;

        e = (OpticalElement) devices.get(currentDevice);

        if (e != null) {
            newindex = e.drawRay(g, r, index);
        }

        return newindex;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        super.draw(g);

        OpticalElement e;

        e = (OpticalElement) devices.get(currentDevice);

        if (e != null) {
            e.draw(g);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void drawSelf(Graphics g) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void propagate(Ray r) {
        OpticalElement e;

        e = (OpticalElement) devices.get(currentDevice);

        if (e != null) {
            e.propagate(r);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void propagateRayPointSelf(RayPoint r) {
    }

    /**
     * DOCUMENT ME!
     */
    public void rearrange() {
        setWidth(0);

        OpticalElement e;

        e = (OpticalElement) devices.get(currentDevice);

        if (e != null) {
            e.putAfter(this);
            e.rearrange();

            setWidth(e.getWidth());
        }
    }
}
