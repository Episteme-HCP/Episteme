/**
 * Title:        NewProj
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright:    Copyright (c) imt
 * </p>
 *
 * <p>
 * Company:      imt
 * </p>
 *
 * <p></p>
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
