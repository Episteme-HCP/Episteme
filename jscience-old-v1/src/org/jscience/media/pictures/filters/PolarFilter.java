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
public class PolarFilter extends TransformFilter {
    //FIXMES	static final long serialVersionUID = 1166374736665848180L;
    /** DOCUMENT ME! */
    public final static int RECT_TO_POLAR = 0;

    /** DOCUMENT ME! */
    public final static int POLAR_TO_RECT = 1;

    /** DOCUMENT ME! */
    public final static int INVERT_IN_CIRCLE = 2;

    /** DOCUMENT ME! */
    private int type;

    /** DOCUMENT ME! */
    private float width;

    /** DOCUMENT ME! */
    private float height;

    /** DOCUMENT ME! */
    private float centreX;

    /** DOCUMENT ME! */
    private float centreY;

    /** DOCUMENT ME! */
    private float radius;

/**
     * Creates a new PolarFilter object.
     */
    public PolarFilter() {
        this(RECT_TO_POLAR);
    }

/**
     * Creates a new PolarFilter object.
     *
     * @param type DOCUMENT ME!
     */
    public PolarFilter(int type) {
        this.type = type;
        setEdgeAction(CLAMP);
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void setDimensions(int width, int height) {
        super.setDimensions(width, height);
        this.width = width;
        this.height = height;
        centreX = width / 2;
        centreY = height / 2;
        radius = Math.min(centreY, centreX);
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private float sqr(float x) {
        return x * x;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    protected void transformInverse(int x, int y, float[] out) {
        float theta;
        float t;
        float m;
        float xmax;
        float ymax;
        float r = 0;

        switch (type) {
        case RECT_TO_POLAR:
            theta = 0;

            if (x >= centreX) {
                if (y > centreY) {
                    theta = ImageMath.PI -
                        (float) Math.atan(((float) (x - centreX)) / ((float) (y -
                            centreY)));
                    r = (float) Math.sqrt(sqr(x - centreX) + sqr(y - centreY));
                } else if (y < centreY) {
                    theta = (float) Math.atan(((float) (x - centreX)) / ((float) (centreY -
                            y)));
                    r = (float) Math.sqrt(sqr(x - centreX) + sqr(centreY - y));
                } else {
                    theta = ImageMath.HALF_PI;
                    r = x - centreX;
                }
            } else if (x < centreX) {
                if (y < centreY) {
                    theta = ImageMath.TWO_PI -
                        (float) Math.atan(((float) (centreX - x)) / ((float) (centreY -
                            y)));
                    r = (float) Math.sqrt(sqr(centreX - x) + sqr(centreY - y));
                } else if (y > centreY) {
                    theta = ImageMath.PI +
                        (float) Math.atan(((float) (centreX - x)) / ((float) (y -
                            centreY)));
                    r = (float) Math.sqrt(sqr(centreX - x) + sqr(y - centreY));
                } else {
                    theta = 1.5f * ImageMath.PI;
                    r = centreX - x;
                }
            }

            if (x != centreX) {
                m = Math.abs(((float) (y - centreY)) / ((float) (x - centreX)));
            } else {
                m = 0;
            }

            if (m <= ((float) height / (float) width)) {
                if (x == centreX) {
                    xmax = 0;
                    ymax = centreY;
                } else {
                    xmax = centreX;
                    ymax = m * xmax;
                }
            } else {
                ymax = centreY;
                xmax = ymax / m;
            }

            out[0] = (width - 1) - ((width - 1) / ImageMath.TWO_PI * theta);
            out[1] = (height * r) / radius;

            break;

        case POLAR_TO_RECT:
            theta = x / width * ImageMath.TWO_PI;

            float theta2;

            if (theta >= (1.5f * ImageMath.PI)) {
                theta2 = ImageMath.TWO_PI - theta;
            } else if (theta >= ImageMath.PI) {
                theta2 = theta - ImageMath.PI;
            } else if (theta >= (0.5f * ImageMath.PI)) {
                theta2 = ImageMath.PI - theta;
            } else {
                theta2 = theta;
            }

            t = (float) Math.tan(theta2);

            if (t != 0) {
                m = 1.0f / t;
            } else {
                m = 0;
            }

            if (m <= ((float) (height) / (float) (width))) {
                if (theta2 == 0) {
                    xmax = 0;
                    ymax = centreY;
                } else {
                    xmax = centreX;
                    ymax = m * xmax;
                }
            } else {
                ymax = centreY;
                xmax = ymax / m;
            }

            r = radius * (float) (y / (float) (height));

            float nx = -r * (float) Math.sin(theta2);
            float ny = r * (float) Math.cos(theta2);

            if (theta >= (1.5f * ImageMath.PI)) {
                out[0] = (float) centreX - nx;
                out[1] = (float) centreY - ny;
            } else if (theta >= Math.PI) {
                out[0] = (float) centreX - nx;
                out[1] = (float) centreY + ny;
            } else if (theta >= (0.5 * Math.PI)) {
                out[0] = (float) centreX + nx;
                out[1] = (float) centreY + ny;
            } else {
                out[0] = (float) centreX + nx;
                out[1] = (float) centreY - ny;
            }

            break;

        case INVERT_IN_CIRCLE:

            float dx = x - centreX;
            float dy = y - centreY;
            float distance2 = (dx * dx) + (dy * dy);
            out[0] = centreX + ((centreX * centreX * dx) / distance2);
            out[1] = centreY + ((centreY * centreY * dy) / distance2);

            break;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Distort/Polar Coordinates...";
    }
}
