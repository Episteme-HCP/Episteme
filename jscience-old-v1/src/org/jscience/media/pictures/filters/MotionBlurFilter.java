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
public class MotionBlurFilter extends WholeImageFilter {
    /** DOCUMENT ME! */
    public final static int LINEAR = 0;

    /** DOCUMENT ME! */
    public final static int RADIAL = 1;

    /** DOCUMENT ME! */
    public final static int ZOOM = 2;

    /** DOCUMENT ME! */
    private float angle = 0.0f;

    /** DOCUMENT ME! */
    private float falloff = 1.0f;

    /** DOCUMENT ME! */
    private float distance = 1.0f;

    /** DOCUMENT ME! */
    private int repetitions = 4;

    /** DOCUMENT ME! */
    private int type = LINEAR;

/**
     * Creates a new MotionBlurFilter object.
     */
    public MotionBlurFilter() {
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
     * @param angle DOCUMENT ME!
     */
    public void setAngle(float angle) {
        this.angle = angle;
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
     * @param distance DOCUMENT ME!
     */
    public void setDistance(float distance) {
        this.distance = distance;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getDistance() {
        return distance;
    }

    /**
     * DOCUMENT ME!
     *
     * @param repetitions DOCUMENT ME!
     */
    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRepetitions() {
        return repetitions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param status DOCUMENT ME!
     */
    public void imageComplete(int status) {
        if ((status == IMAGEERROR) || (status == IMAGEABORTED)) {
            consumer.imageComplete(status);

            return;
        }

        int width = originalSpace.width;
        int height = originalSpace.height;
        int index = 0;
        int[] outPixels = new int[width * height];

        float sinAngle = (float) Math.sin(angle);
        float cosAngle = (float) Math.cos(angle);

        float total;
        int cx = width / 2;
        int cy = height / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = 0;
                int r = 0;
                int g = 0;
                int b = 0;
                int count = 0;

                for (int i = 0; i < repetitions; i++) {
                    int newX = x;
                    int newY = y;

                    if (i != 0) {
                        switch (type) {
                        case LINEAR:default:
                            newX = (int) (x + (i * distance * sinAngle));
                            newY = (int) (y + (i * distance * cosAngle));

                            break;

                        case RADIAL: {
                            float dx = x - cx;
                            float dy = y - cy;
                            float d = (float) Math.sqrt((dx * dx) + (dy * dy));
                            float an = (float) Math.atan2(dy, dx);
                            an += ((distance * (i - (repetitions / 2))) / ImageMath.PI / 10.0);
                            newX = (int) (cx + (d * Math.cos(an)));
                            newY = (int) (cy + (d * Math.sin(an)));
                        }

                        break;

                        case ZOOM: {
                            float dx = x - cx;
                            float dy = y - cy;
                            newX = (int) (cx +
                                ((1.0 - ((i * distance) / 100.0)) * dx));
                            newY = (int) (cy +
                                ((1.0 - ((i * distance) / 100.0)) * dy));
                        }

                        break;
                        }

                        if (newX < 0) {
                            break;
                        } else if (newX >= width) {
                            break;
                        }

                        if (newY < 0) {
                            break;
                        } else if (newY >= height) {
                            break;
                        }
                    }

                    count++;

                    int rgb = inPixels[(newY * width) + newX];
                    a += ((rgb >> 24) & 0xff);
                    r += ((rgb >> 16) & 0xff);
                    g += ((rgb >> 8) & 0xff);
                    b += (rgb & 0xff);
                }

                if (count == 0) {
                    outPixels[index] = inPixels[index];
                } else {
                    a = PixelUtils.clamp((int) (a / count));
                    r = PixelUtils.clamp((int) (r / count));
                    g = PixelUtils.clamp((int) (g / count));
                    b = PixelUtils.clamp((int) (b / count));
                    outPixels[index] = (a << 24) | (r << 16) | (g << 8) | b;
                }

                index++;
            }
        }

        consumer.setPixels(0, 0, width, height, defaultRGBModel, outPixels, 0,
            width);
        consumer.imageComplete(status);
        inPixels = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Blur/Motion Blur...";
    }
}
