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

import org.jscience.mathematics.algebraic.matrices.Double3Vector;

import org.jscience.physics.waves.optics.rays.RayPoint;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class SimpleLens extends OpticalElement {
    /** DOCUMENT ME! */
    private static final double TresholdAperture = 20;

    /** DOCUMENT ME! */
    private static final double DefaultRadius = 30;

    /** DOCUMENT ME! */
    private double f = 20;

    /** DOCUMENT ME! */
    private double aperture = 50;

/**
     * Creates a new SimpleLens object.
     */
    public SimpleLens() {
        super();
    }

/**
     * Creates a new SimpleLens object.
     *
     * @param f DOCUMENT ME!
     */
    public SimpleLens(double f) {
        this();
        this.f = f;
    }

/**
     * Creates a new SimpleLens object.
     *
     * @param f        DOCUMENT ME!
     * @param aperture DOCUMENT ME!
     */
    public SimpleLens(double f, double aperture) {
        this();
        this.f = f;
        this.aperture = aperture;
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     */
    public void setFocal(double f) {
        this.f = f;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getFocal() {
        return f;
    }

    /**
     * DOCUMENT ME!
     *
     * @param aperture DOCUMENT ME!
     */
    public void setAperture(double aperture) {
        this.aperture = aperture;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAperture() {
        return aperture;
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

            // Here, the RayPoint is at the beginning of the element.
            Double3Vector kvect = r.getKVector();

            double y = r.getPosition().y;
            double z = r.getPosition().z;
            double kx = kvect.x;
            double ky = kvect.y;
            double kz = kvect.z;
            double yp = (ky * f) / kx;

            if (((y * y) + (z * z)) <= (aperture * aperture)) {
                kx = Math.abs(f);

                if (f >= 0) {
                    ky = yp - y;
                } else {
                    ky = -(yp - y);
                }

                kz = 0;

                Double3Vector newk = new Double3Vector(kx, ky, kz);
                newk.normalize(r.getKVector().norm());
                r.setKVector(newk);
            } else {
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
        int r;
        int f2 = (int) Math.round(f);
        int sf = (f >= 0) ? 1 : (-1);

        boolean AboveTreshold;
        double radius;

        if (aperture > TresholdAperture) {
            radius = aperture;
            AboveTreshold = true;
        } else {
            radius = DefaultRadius;
            AboveTreshold = false;
        }

        r = (int) Math.round(radius);

        g.drawLine(0, -r, 0, r);

        g.drawLine(-1, 0, 1, 0);

        g.drawLine(-4, -r + (sf * 4), 0, -r);
        g.drawLine(4, -r + (sf * 4), 0, -r);

        g.drawLine(-4, r - (sf * 4), 0, r);
        g.drawLine(4, r - (sf * 4), 0, r);

        g.fillRect(-f2 - 1, -3, 2, 7);
        g.fillRect(f2 - 1, -3, 2, 7);

        if (!AboveTreshold) {
            r = (int) Math.round(aperture);
            g.drawLine(-3, -r, 3, -r);
            g.drawLine(-3, r, 3, r);
        }
    }
}
