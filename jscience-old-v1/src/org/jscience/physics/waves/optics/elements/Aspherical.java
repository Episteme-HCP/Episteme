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

import org.jscience.physics.waves.optics.materials.Material;
import org.jscience.physics.waves.optics.rays.RayPoint;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Aspherical extends OpticalElement {
    /** DOCUMENT ME! */
    static final double defaultWidth = 20.0;

    /** DOCUMENT ME! */
    private double C = 1 / 20.0;

    /** DOCUMENT ME! */
    private double K = -1.0;

    /** DOCUMENT ME! */
    private double aperture = 80.0;

    // if false, rays not falling on the surface will be propagated
    // w/o being deflected.
    /** DOCUMENT ME! */
    private boolean solidAperture = true;

    /** DOCUMENT ME! */
    private Material mat;

/**
     * Creates a new Aspherical object.
     */
    public Aspherical() {
        super();
    }

/**
     * Creates a new Aspherical object.
     *
     * @param C   DOCUMENT ME!
     * @param K   DOCUMENT ME!
     * @param mat DOCUMENT ME!
     */
    public Aspherical(double C, double K, Material mat) {
        this();
        this.C = C;
        this.K = K;
        this.mat = mat;
        setWidth(defaultWidth);
    }

/**
     * Creates a new Aspherical object.
     *
     * @param C        DOCUMENT ME!
     * @param K        DOCUMENT ME!
     * @param width    DOCUMENT ME!
     * @param aperture DOCUMENT ME!
     * @param mat      DOCUMENT ME!
     */
    public Aspherical(double C, double K, double width, double aperture,
        Material mat) {
        this(C, K, mat);
        this.aperture = aperture;
        setWidth(width);
    }

    /**
     * DOCUMENT ME!
     *
     * @param solidAperture DOCUMENT ME!
     */
    public void setSolidAperture(boolean solidAperture) {
        this.solidAperture = solidAperture;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getSolidAperture() {
        return solidAperture;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     */
    public void setAperture(double a) {
        aperture = a;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAperture(double a) {
        return aperture;
    }

    /**
     * DOCUMENT ME!
     *
     * @param C DOCUMENT ME!
     */
    public void setC(double C) {
        this.C = C;
    }

    /**
     * DOCUMENT ME!
     *
     * @param K DOCUMENT ME!
     */
    public void setK(double K) {
        this.K = K;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getC() {
        return C;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getK() {
        return K;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double depthAtAperture() {
        double a;

        a = asph(aperture);

        if (Double.isNaN(a) || Double.isInfinite(a)) {
            if (C != 0) {
                a = -1 / C;
            } else {
                a = 0;
            }
        }

        return a;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void propagateRayPointSelf(RayPoint r) {
        boolean n_horizontal = false;

        double distance;

        double xr;
        double yr;
        double zr;
        double kx;
        double ky;
        double kz;
        double k_norm;
        double a;
        double b;
        double c;
        double u1;
        double u2;
        double d;

        // Intersection
        xr = r.getPosition().x;
        yr = r.getPosition().y;
        zr = r.getPosition().z;

        kx = r.getKVector().x;
        ky = r.getKVector().y;
        kz = r.getKVector().z;

        k_norm = (kx * kx) + (ky * ky) + (kz * kz);

        a = -C * (((K + 1) * kx * kx) + (ky * ky) + (kz * kz));
        b = (-2 * C * (((K + 1) * kx * xr) + (ky * yr) + (kz * zr))) -
            (2 * kx);
        c = (-C * (((K + 1) * xr * xr) + (yr * yr) + (zr * zr))) - (2 * xr);

        double xi;
        double yi;
        double zi;

        if (a == 0) // Horizontal ray with parabolic surface
         {
            u1 = -c / b;
        } else {
            d = (b * b) - (4 * a * c);

            if (d < 0) // No intersection
             {
                distance = -xr;

                if (distance <= 0) // Ray already inside medium, kill it
                 {
                    r.invalidate();
                } else {
                    r.goStraight(distance + depthAtAperture());

                    if (solidAperture) {
                        r.invalidate();
                    }
                }

                // Update position variables
                xr = r.getPosition().x;
                yr = r.getPosition().y;
                zr = r.getPosition().z;

                // Don't calculate n vector, but use a horizontal one
                n_horizontal = true;
                u1 = 0;
            } else // An intersection
             {
                if (d == 0) {
                    u1 = -b / (2 * a);
                } else {
                    u1 = (-b - Math.sqrt(d)) / (2 * a);
                    u2 = (-b + Math.sqrt(d)) / (2 * a);

                    if (((C < 0) && (K < -1)) || ((C > 0) && (K >= -1))) // )( -> ( or () -> )
                     {
                        u1 = Math.max(u1, u2);
                    } else // )( -> ) or () -> (
                     {
                        u1 = Math.min(u1, u2);
                    }
                }
            }
        }

        // Move raypoint to intersection
        xi = xr + (kx * u1);
        yi = yr + (ky * u1);
        zi = zr + (kz * u1);

        // Refraction
        double nx;

        // Refraction
        double ny;

        // Refraction
        double nz;

        // Refraction
        double n_norm;

        // Refraction
        double k_dot_n;

        // Refraction
        double k_after;

        // Refraction
        double k_after_2;

        if (((yi * yi) + (zi * zi)) > (aperture * aperture)) // Ray falls outside the aperture
         {
            // Move it to surface height
            r.goStraight(-xr + depthAtAperture());

            // Kill it if aperture is solid (default)
            if (solidAperture) {
                r.invalidate();
            }

            // Refraction on a planar interface
            n_horizontal = true;
        }

        if (n_horizontal) // Use an horizontal n vector
         {
            nx = -1;
            ny = 0;
            nz = 0;
        } else // Calculate n vector
         {
            r.setPosition(new Double3Vector(xi, yi, zi));

            if (xi < xr) // Ray already inside medium, kill it
             {
                r.invalidate();
            }

            if (K == -1) {
                nx = -1.0;
                ny = -C * yi;
                nz = -C * zi;
            } else {
                nx = -1.0;
                ny = -(C * yi) / Math.sqrt(1.0 -
                        ((K + 1) * C * C * ((yi * yi) + (zi * zi))));
                nz = -(C * zi) / Math.sqrt(1.0 -
                        ((K + 1) * C * C * ((yi * yi) + (zi * zi))));
            }
        }

        if (r.isValid()) {
            n_norm = (nx * nx) + (ny * ny) + (nz * nz);
            k_dot_n = (nx * kx) + (ny * ky) + (nz * kz);

            k_after = (2 * Math.PI * mat.indexAtWavelength(r.getWavelength())) / r.getWavelength();
            k_after_2 = k_after * k_after;

            a = n_norm;

            b = 2 * k_dot_n;
            c = k_norm - k_after_2;

            d = (b * b) - (4 * a * c);

            if (d < 0) {
                r.invalidate();
            } else if (d == 0) {
                u1 = -b / (2 * a);
            } else {
                u1 = (-b - Math.sqrt(d)) / (2 * a);
                u2 = (-b + Math.sqrt(d)) / (2 * a);
            }

            Double3Vector kvect = new Double3Vector(kx + (u1 * nx),
                    ky + (u1 * ny), 0);
            r.setKVector(kvect);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double asph(double y) {
        return -(C * y * y) / (1 + Math.sqrt(1 - (C * C * (K + 1) * y * y)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void drawSelf(Graphics g) {
        Color savecolor = g.getColor();
        g.setColor(Color.red);

        int a = (int) Math.round(aperture);
        double x;
        double oldx;
        double y;

        y = -a;
        oldx = asph(y);

        for (y = -a + 1; y <= a; y++) {
            x = asph(y);

            int xp;
            int oldxp;
            int yp;
            xp = (int) Math.round(x);
            oldxp = (int) Math.round(oldx);
            yp = (int) Math.round(y);

            if (!(Double.isNaN(x) || Double.isNaN(oldx) || Double.isNaN(y))) {
                g.drawLine(oldxp, yp - 1, xp, yp);
            }

            oldx = x;
        }

        g.setColor(savecolor);
    }
}
