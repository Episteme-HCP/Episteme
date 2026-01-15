/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class RotateFilter extends TransformFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 1166374736665848180L;

    /** DOCUMENT ME! */
    private float angle;

    /** DOCUMENT ME! */
    private float cos;

    /** DOCUMENT ME! */
    private float sin;

    /** DOCUMENT ME! */
    private boolean resize = true;

/**
     * Creates a new RotateFilter object.
     */
    public RotateFilter() {
        this(ImageMath.PI);
    }

/**
     * Creates a new RotateFilter object.
     *
     * @param angle DOCUMENT ME!
     */
    public RotateFilter(float angle) {
        this(angle, true);
    }

/**
     * Creates a new RotateFilter object.
     *
     * @param angle  DOCUMENT ME!
     * @param resize DOCUMENT ME!
     */
    public RotateFilter(float angle, boolean resize) {
        setAngle(angle);
        this.resize = resize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param angle DOCUMENT ME!
     */
    public void setAngle(float angle) {
        this.angle = angle;
        cos = (float) Math.cos(this.angle);
        sin = (float) Math.sin(this.angle);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getAngle() {
        return angle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rect DOCUMENT ME!
     */
    protected void transformSpace(Rectangle rect) {
        if (resize) {
            Point out = new Point(0, 0);
            int minx = Integer.MAX_VALUE;
            int miny = Integer.MAX_VALUE;
            int maxx = Integer.MIN_VALUE;
            int maxy = Integer.MIN_VALUE;
            int w = rect.width;
            int h = rect.height;
            int x = rect.x;
            int y = rect.y;

            for (int i = 0; i < 4; i++) {
                switch (i) {
                case 0:
                    transform(x, y, out);

                    break;

                case 1:
                    transform(x + w, y, out);

                    break;

                case 2:
                    transform(x, y + h, out);

                    break;

                case 3:
                    transform(x + w, y + h, out);

                    break;
                }

                minx = Math.min(minx, out.x);
                miny = Math.min(miny, out.y);
                maxx = Math.max(maxx, out.x);
                maxy = Math.max(maxy, out.y);
            }

            rect.x = minx;
            rect.y = miny;
            rect.width = maxx - rect.x;
            rect.height = maxy - rect.y;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    protected void transform(int x, int y, Point out) {
        out.x = (int) ((x * cos) + (y * sin));
        out.y = (int) ((y * cos) - (x * sin));
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    protected void transformInverse(int x, int y, float[] out) {
        out[0] = (x * cos) - (y * sin);
        out[1] = (y * cos) + (x * sin);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Rotate " + (int) ((angle * 180) / Math.PI);
    }
}
