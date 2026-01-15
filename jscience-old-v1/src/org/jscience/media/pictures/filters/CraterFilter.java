/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

import java.util.Date;
import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class CraterFilter extends WholeImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 6491871753122667752L;

    /** DOCUMENT ME! */
    private Colormap colormap = new LinearColormap();

    /** DOCUMENT ME! */
    private Random randomGenerator;

    /** DOCUMENT ME! */
    private long seed = 567;

    /** DOCUMENT ME! */
    private int numCraters = 25000;

    /** DOCUMENT ME! */
    private float depthPower = 1.0f;

    /** DOCUMENT ME! */
    private float depthBias = 0.707107f;

    /** DOCUMENT ME! */
    private float depthScaling = 1.0f;

    /** DOCUMENT ME! */
    private boolean spherical = false;

/**
     * Creates a new CraterFilter object.
     */
    public CraterFilter() {
        randomGenerator = new Random();
    }

    /**
     * DOCUMENT ME!
     *
     * @param numCraters DOCUMENT ME!
     */
    public void setNumCraters(int numCraters) {
        this.numCraters = numCraters;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumCraters() {
        return numCraters;
    }

    /**
     * DOCUMENT ME!
     *
     * @param depthPower DOCUMENT ME!
     */
    public void setDepthPower(float depthPower) {
        this.depthPower = depthPower;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getDepthPower() {
        return depthPower;
    }

    /**
     * DOCUMENT ME!
     *
     * @param depthBias DOCUMENT ME!
     */
    public void setDepthBias(float depthBias) {
        this.depthBias = depthBias;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getDepthBias() {
        return depthBias;
    }

    /**
     * DOCUMENT ME!
     *
     * @param depthScaling DOCUMENT ME!
     */
    public void setDepthScaling(float depthScaling) {
        this.depthScaling = depthScaling;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getDepthScaling() {
        return depthScaling;
    }

    /**
     * DOCUMENT ME!
     *
     * @param colormap DOCUMENT ME!
     */
    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Colormap getColormap() {
        return colormap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param spherical DOCUMENT ME!
     */
    public void setSpherical(boolean spherical) {
        this.spherical = spherical;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSpherical() {
        return spherical;
    }

    /**
     * DOCUMENT ME!
     */
    public void randomize() {
        seed = new Date().getTime();
    }

    /**
     * DOCUMENT ME!
     *
     * @param low DOCUMENT ME!
     * @param high DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private float random(float low, float high) {
        return low + ((high - low) * randomGenerator.nextFloat());
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

        try {
            int width = originalSpace.width;
            int height = originalSpace.height;
            int[] outPixels = new int[width * height];

            randomGenerator.setSeed(seed);

            int i;
            int j;
            int x;
            int y;

            i = 0;

            for (y = 0; y < height; y++)
                for (x = 0; x < width; x++)
                    outPixels[i++] = 32767;

            for (i = 0; i < numCraters; i++) {
                float g;
                int cx = (int) random(0.0f, width - 1);
                int cy = (int) random(0.0f, height - 1);
                int gx;
                int gy;
                int amptot = 0;
                int axelev;
                int npatch = 0;

                g = (float) Math.sqrt(1 / ((float) Math.PI * (1 -
                        random(0, 0.9999f))));

                if (g < 3) {
                    // A very small crater
                    for (y = Math.max(0, cy - 1);
                            y <= Math.min(height - 1, cy + 1); y++) {
                        int sx = Math.max(0, cx - 1);
                        int a = (y * width) + sx;

                        for (x = sx; x <= Math.min(width - 1, cx + 1); x++) {
                            amptot += outPixels[a++];
                            npatch++;
                        }
                    }

                    axelev = amptot / npatch;

                    /* Perturb the mean elevation by a small random factor. */
                    x = (g >= 1) ? (((randomGenerator.nextInt() >> 8) & 3) - 1)
                                 : 0;
                    x *= depthScaling;
                    outPixels[(width * cy) + cx] = axelev + x;
                } else {
                    gx = (int) Math.max(2, g / 3);
                    gy = (int) Math.max(2, g / 3);

                    for (y = Math.max(0, cy - gy);
                            y <= Math.min(height - 1, cy + gy); y++) {
                        int sx = Math.max(0, cx - gx);
                        int a = (y * width) + sx;

                        for (x = sx; x <= Math.min(width - 1, cx + gx); x++) {
                            amptot += outPixels[a++];
                            npatch++;
                        }
                    }

                    axelev = amptot / npatch;

                    gy = (int) Math.max(2, g);
                    g = gy;
                    gx = (int) Math.max(2, g);

                    for (y = Math.max(0, cy - gy);
                            y <= Math.min(height - 1, cy + gy); y++) {
                        int sx = Math.max(0, cx - gx);
                        int ax = (y * width) + sx;
                        float dy = (cy - y) / (float) gy;
                        float dysq = dy * dy;

                        for (x = sx; x <= Math.min(width - 1, cx + gx); x++) {
                            float dx = ((cx - x) / (float) gx);
                            float cd = (dx * dx) + dysq;
                            float cd2 = cd * 2.25f;
                            float tcz = depthBias -
                                (float) Math.sqrt(Math.abs(1 - cd2));
                            float cz = Math.max((cd2 > 1) ? 0.0f : (-10f), tcz);
                            float roll;
                            float iroll;
                            int av;

                            cz *= (float) Math.pow(g, depthPower);

                            if ((dy == 0) && (dx == 0) && (((int) cz) == 0)) {
                                cz = (cz < 0) ? (-1) : 1;
                            }

                            cz *= depthScaling;

                            float rollmin = 0.9f;
                            roll = (((1 / (1 - Math.min(rollmin, cd))) / (1 / (1 -
                                rollmin))) - (1 - rollmin)) / rollmin;
                            iroll = 1 - roll;

                            av = (int) (((axelev + cz) * iroll) +
                                ((outPixels[ax] + cz) * roll));
                            av = Math.max(1000, Math.min(64000, av));
                            outPixels[ax++] = av;
                        }
                    }
                }
            }

            float ImageGamma = 0.5f;
            float dgamma = 1.0f;
            int slopemin = -52;
            int slopemax = 52;
            i = Math.max((slopemax - slopemin) + 1, 1);

            float[] slopemap = new float[i];

            for (i = slopemin; i <= slopemax; i++) {
                slopemap[i - slopemin] = (i > 0)
                    ? (0.5f +
                    (0.5f * (float) Math.pow(Math.sin(
                            (ImageMath.HALF_PI * i) / slopemax),
                        dgamma * ImageGamma)))
                    : (0.5f -
                    (127.0f * (float) Math.pow(Math.sin(
                            (ImageMath.HALF_PI * i) / slopemin),
                        dgamma * ImageGamma)));
            }

            if (colormap != null) {
                int index = 0;

                for (y = 0; y < height; y++) {
                    int last = outPixels[index];

                    for (x = 0; x < width; x++) {
                        int t = outPixels[index];
                        j = t - last;
                        j = Math.min(Math.max(slopemin, j), slopemax);
                        outPixels[index] = colormap.getColor(slopemap[j -
                                slopemin]);
                        last = t;

                        //					outPixels[index] = colormap.getColor(outPixels[index]/65535.0);
                        index++;
                    }
                }
            }

            consumer.setPixels(0, 0, width, height, defaultRGBModel, outPixels,
                0, width);
            consumer.imageComplete(status);
            inPixels = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Texture/Crater...";
    }
}
