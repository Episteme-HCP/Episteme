/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class PerspectivFilter extends TransformFilter {
    /** DOCUMENT ME! */
    private float x0;

    /** DOCUMENT ME! */
    private float y0;

    /** DOCUMENT ME! */
    private float x1;

    /** DOCUMENT ME! */
    private float y1;

    /** DOCUMENT ME! */
    private float x2;

    /** DOCUMENT ME! */
    private float y2;

    /** DOCUMENT ME! */
    private float x3;

    /** DOCUMENT ME! */
    private float y3;

    /** DOCUMENT ME! */
    private float dx1;

    /** DOCUMENT ME! */
    private float dy1;

    /** DOCUMENT ME! */
    private float dx2;

    /** DOCUMENT ME! */
    private float dy2;

    /** DOCUMENT ME! */
    private float dx3;

    /** DOCUMENT ME! */
    private float dy3;

    /** DOCUMENT ME! */
    private float A;

    /** DOCUMENT ME! */
    private float B;

    /** DOCUMENT ME! */
    private float C;

    /** DOCUMENT ME! */
    private float D;

    /** DOCUMENT ME! */
    private float E;

    /** DOCUMENT ME! */
    private float F;

    /** DOCUMENT ME! */
    private float G;

    /** DOCUMENT ME! */
    private float H;

    /** DOCUMENT ME! */
    private float I;

/**
     * Creates a new PerspectivFilter object.
     */
    public PerspectivFilter() {
        this(0, 0, 100, 0, 100, 100, 0, 100);
    }

/**
     * Creates a new PerspectivFilter object.
     *
     * @param x0 DOCUMENT ME!
     * @param y0 DOCUMENT ME!
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param y2 DOCUMENT ME!
     * @param x3 DOCUMENT ME!
     * @param y3 DOCUMENT ME!
     */
    public PerspectivFilter(float x0, float y0, float x1, float y1, float x2,
        float y2, float x3, float y3) {
        setCorners(x0, y0, x1, y1, x2, y2, x3, y3);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x0 DOCUMENT ME!
     * @param y0 DOCUMENT ME!
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param y2 DOCUMENT ME!
     * @param x3 DOCUMENT ME!
     * @param y3 DOCUMENT ME!
     */
    public void setCorners(float x0, float y0, float x1, float y1, float x2,
        float y2, float x3, float y3) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;

        dx1 = x1 - x2;
        dy1 = y1 - y2;
        dx2 = x3 - x2;
        dy2 = y3 - y2;
        dx3 = (x0 - x1 + x2) - x3;
        dy3 = (y0 - y1 + y2) - y3;

        float a11;
        float a12;
        float a13;
        float a21;
        float a22;
        float a23;
        float a31;
        float a32;

        if ((dx3 == 0) && (dy3 == 0)) {
            a11 = x1 - x0;
            a21 = x2 - x1;
            a31 = x0;
            a12 = y1 - y0;
            a22 = y2 - y1;
            a32 = y0;
            a13 = a23 = 0;
        } else {
            a13 = ((dx3 * dy2) - (dx2 * dy3)) / ((dx1 * dy2) - (dy1 * dx2));
            a23 = ((dx1 * dy3) - (dy1 * dx3)) / ((dx1 * dy2) - (dy1 * dx2));
            a11 = x1 - x0 + (a13 * x1);
            a21 = x3 - x0 + (a23 * x3);
            a31 = x0;
            a12 = y1 - y0 + (a13 * y1);
            a22 = y3 - y0 + (a23 * y3);
            a32 = y0;
        }

        A = a22 - (a32 * a23);
        B = (a31 * a23) - a21;
        C = (a21 * a32) - (a31 * a22);
        D = (a32 * a13) - a12;
        E = a11 - (a31 * a13);
        F = (a31 * a12) - (a11 * a32);
        G = (a12 * a23) - (a22 * a13);
        H = (a21 * a13) - (a11 * a23);
        I = (a11 * a22) - (a21 * a12);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    protected void transformInverse(int x, int y, float[] out) {
        out[0] = 0.5f +
            ((originalSpace.width * ((A * x) + (B * y) + C)) / ((G * x) +
            (H * y) + I));
        out[1] = 0.5f +
            ((originalSpace.height * ((D * x) + (E * y) + F)) / ((G * x) +
            (H * y) + I));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Distort/Perspective...";
    }
}
