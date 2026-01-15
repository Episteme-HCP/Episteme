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
public class SphereFilter extends TransformFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -8148404526162968279L;

    /** DOCUMENT ME! */
    private float a = 0;

    /** DOCUMENT ME! */
    private float b = 0;

    /** DOCUMENT ME! */
    private float a2 = 0;

    /** DOCUMENT ME! */
    private float b2 = 0;

    /** DOCUMENT ME! */
    private float refractionIndex = 1.5f;

/**
     * Creates a new SphereFilter object.
     */
    public SphereFilter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param refractionIndex DOCUMENT ME!
     */
    public void setRefractionIndex(float refractionIndex) {
        this.refractionIndex = refractionIndex;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getRefractionIndex() {
        return refractionIndex;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void setDimensions(int width, int height) {
        super.setDimensions(width, height);
        a = width / 2;
        b = height / 2;
        a2 = a * a;
        b2 = b * b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param out DOCUMENT ME!
     */
    protected void transformInverse(int x, int y, float[] out) {
        float dx = x - a;
        float dy = y - b;
        float x2 = dx * dx;
        float y2 = dy * dy;

        if (y2 >= (b2 - ((b2 * x2) / a2))) {
            out[0] = x;
            out[1] = y;
        } else {
            float rRefraction = 1.0f / refractionIndex;

            float z = (float) Math.sqrt((1.0f - (x2 / a2) - (y2 / b2)) * (a * b));
            float z2 = z * z;

            float xAngle = (float) Math.acos(dx / Math.sqrt(x2 + z2));
            float angle1 = ImageMath.HALF_PI - xAngle;
            float angle2 = (float) Math.asin(Math.sin(angle1) * rRefraction);
            angle2 = ImageMath.HALF_PI - xAngle - angle2;
            out[0] = x - ((float) Math.tan(angle2) * z);

            float yAngle = (float) Math.acos(dy / Math.sqrt(y2 + z2));
            angle1 = ImageMath.HALF_PI - yAngle;
            angle2 = (float) Math.asin(Math.sin(angle1) * rRefraction);
            angle2 = ImageMath.HALF_PI - yAngle - angle2;
            out[1] = y - ((float) Math.tan(angle2) * z);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Distort/Sphere...";
    }
}
