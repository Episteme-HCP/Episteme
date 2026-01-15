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

import org.jscience.physics.waves.optics.rays.RayPoint;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Screen extends OpticalElement {
    /** DOCUMENT ME! */
    private double depth = 0.0;

    /** DOCUMENT ME! */
    private double height = 100.0;

/**
     * Creates a new Screen object.
     */
    public Screen() {
        super();
    }

/**
     * Creates a new Screen object.
     *
     * @param height DOCUMENT ME!
     * @param depth  DOCUMENT ME!
     */
    public Screen(double height, double depth) {
        super();
        this.depth = depth;
        this.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param depth DOCUMENT ME!
     */
    public void setDepth(double depth) {
        this.depth = depth;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDepth() {
        return depth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getHeight() {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void propagateRayPointSelf(RayPoint r) {
        double distance;
        distance = -r.getPosition().x;

        if (distance > 0) {
            r.goStraight(distance);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void drawSelf(Graphics g) {
        int r = (int) Math.round(height / 2);
        g.drawLine(0, -r, 0, r);
    }
}
