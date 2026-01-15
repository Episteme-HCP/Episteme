/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

import org.jscience.media.pictures.filters.math.*;

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
public class CausticsFilter extends WholeImageFilter {
    /** DOCUMENT ME! */
    private float scale = 32;

    /** DOCUMENT ME! */
    private float angle = 0.0f;

    /** DOCUMENT ME! */
    public int brightness = 10;

    /** DOCUMENT ME! */
    public float amount = 1.0f;

    /** DOCUMENT ME! */
    public float turbulence = 1.0f;

    /** DOCUMENT ME! */
    public float dispersion = 0.0f;

    /** DOCUMENT ME! */
    public float time = 0.0f;

    /** DOCUMENT ME! */
    private int samples = 2;

    /** DOCUMENT ME! */
    private float s;

    /** DOCUMENT ME! */
    private float c;

/**
     * Creates a new CausticsFilter object.
     */
    public CausticsFilter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param scale DOCUMENT ME!
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getScale() {
        return scale;
    }

    /**
     * DOCUMENT ME!
     *
     * @param brightness DOCUMENT ME!
     */
    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBrightness() {
        return brightness;
    }

    /**
     * DOCUMENT ME!
     *
     * @param turbulence DOCUMENT ME!
     */
    public void setTurbulence(float turbulence) {
        this.turbulence = turbulence;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getTurbulence() {
        return turbulence;
    }

    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getAmount() {
        return amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dispersion DOCUMENT ME!
     */
    public void setDispersion(float dispersion) {
        this.dispersion = dispersion;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getDispersion() {
        return dispersion;
    }

    /**
     * DOCUMENT ME!
     *
     * @param time DOCUMENT ME!
     */
    public void setTime(float time) {
        this.time = time;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getTime() {
        return time;
    }

    /**
     * DOCUMENT ME!
     *
     * @param samples DOCUMENT ME!
     */
    public void setSamples(int samples) {
        this.samples = samples;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSamples() {
        return samples;
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

        Random random = new Random(0);

        s = (float) Math.sin(0.1);
        c = (float) Math.cos(0.1);

        int srcWidth = originalSpace.width;
        int srcHeight = originalSpace.height;
        int outWidth = transformedSpace.width;
        int outHeight = transformedSpace.height;
        int index = 0;
        int[] pixels = new int[outWidth * outHeight];

        for (int y = 0; y < outHeight; y++) {
            for (int x = 0; x < outWidth; x++) {
                pixels[index++] = 0xfe799fff;
            }
        }

        int v = brightness / samples;

        if (v == 0) {
            v = 1;
        }

        float rs = 1.0f / scale;
        float d = 0.95f;
        index = 0;

        for (int y = 0; y < outHeight; y++) {
            for (int x = 0; x < outWidth; x++) {
                for (int s = 0; s < samples; s++) {
                    float sx = x + random.nextFloat();
                    float sy = y + random.nextFloat();
                    float nx = sx * rs;
                    float ny = sy * rs;
                    float xDisplacement;
                    float yDisplacement;
                    float focus = 0.1f + amount;
                    xDisplacement = evaluate(nx - d, ny) -
                        evaluate(nx + d, ny);
                    yDisplacement = evaluate(nx, ny + d) -
                        evaluate(nx, ny - d);

                    if (dispersion > 0) {
                        for (int c = 0; c < 3; c++) {
                            float ca = (1 + (c * dispersion));
                            float srcX = sx +
                                (scale * focus * xDisplacement * ca);
                            float srcY = sy +
                                (scale * focus * yDisplacement * ca);

                            if ((srcX < 0) || (srcX >= (outWidth - 1)) ||
                                    (srcY < 0) || (srcY >= (outHeight - 1))) {
                            } else {
                                int i = (((int) srcY) * outWidth) + (int) srcX;

                                if (false) {
                                    float fx = srcX - (int) srcX;
                                    float fy = srcY - (int) srcY;

                                    if (srcX >= 1) {
                                        pixels[i - 1] = add(pixels[i - 1],
                                                brightness * fx * (1 - fy), c);

                                        if (srcY >= 1) {
                                            pixels[i - outWidth - 1] = add(pixels[i -
                                                    outWidth - 1],
                                                    brightness * fx * fy, c);
                                        }
                                    }

                                    if (srcY >= 1) {
                                        pixels[i - outWidth] = add(pixels[i -
                                                outWidth],
                                                brightness * (1 - fx) * fy, c);
                                    }

                                    pixels[i] = add(pixels[i],
                                            brightness * (1 - fx) * (1 - fy), c);
                                } else {
                                    int rgb = pixels[i];
                                    int r = (rgb >> 16) & 0xff;
                                    int g = (rgb >> 8) & 0xff;
                                    int b = rgb & 0xff;

                                    if (c == 2) {
                                        r += v;
                                    } else if (c == 1) {
                                        g += v;
                                    } else {
                                        b += v;
                                    }

                                    if (r > 255) {
                                        r = 255;
                                    }

                                    if (g > 255) {
                                        g = 255;
                                    }

                                    if (b > 255) {
                                        b = 255;
                                    }

                                    pixels[i] = 0xff000000 | (r << 16) |
                                        (g << 8) | b;
                                }
                            }
                        }
                    } else {
                        float srcX = sx + (scale * focus * xDisplacement);
                        float srcY = sy + (scale * focus * yDisplacement);

                        if ((srcX < 0) || (srcX >= (outWidth - 1)) ||
                                (srcY < 0) || (srcY >= (outHeight - 1))) {
                        } else {
                            int i = (((int) srcY) * outWidth) + (int) srcX;

                            if (false) {
                                float fx = srcX - (int) srcX;
                                float fy = srcY - (int) srcY;

                                if (srcX >= 1) {
                                    pixels[i - 1] = add(pixels[i - 1],
                                            brightness * fx * (1 - fy));

                                    if (srcY >= 1) {
                                        pixels[i - outWidth - 1] = add(pixels[i -
                                                outWidth - 1],
                                                brightness * fx * fy);
                                    }
                                }

                                if (srcY >= 1) {
                                    pixels[i - outWidth] = add(pixels[i -
                                            outWidth],
                                            brightness * (1 - fx) * fy);
                                }

                                pixels[i] = add(pixels[i],
                                        brightness * (1 - fx) * (1 - fy));
                            } else {
                                int rgb = pixels[i];
                                int r = (rgb >> 16) & 0xff;
                                int g = (rgb >> 8) & 0xff;
                                int b = rgb & 0xff;
                                r += v;
                                g += v;
                                b += v;

                                if (r > 255) {
                                    r = 255;
                                }

                                if (g > 255) {
                                    g = 255;
                                }

                                if (b > 255) {
                                    b = 255;
                                }

                                pixels[i] = 0xff000000 | (r << 16) | (g << 8) |
                                    b;
                            }
                        }
                    }
                }
            }
        }

        consumer.setPixels(0, 0, outWidth, outHeight, defaultRGBModel, pixels,
            0, outWidth);
        consumer.imageComplete(status);
        inPixels = null;
        pixels = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rgb DOCUMENT ME!
     * @param brightness DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int add(int rgb, float brightness) {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        r += brightness;
        g += brightness;
        b += brightness;

        if (r > 255) {
            r = 255;
        }

        if (g > 255) {
            g = 255;
        }

        if (b > 255) {
            b = 255;
        }

        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rgb DOCUMENT ME!
     * @param brightness DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int add(int rgb, float brightness, int c) {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;

        if (c == 2) {
            r += brightness;
        } else if (c == 1) {
            g += brightness;
        } else {
            b += brightness;
        }

        if (r > 255) {
            r = 255;
        }

        if (g > 255) {
            g = 255;
        }

        if (b > 255) {
            b = 255;
        }

        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param time DOCUMENT ME!
     * @param octaves DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static float turbulence2(float x, float y, float time, float octaves) {
        float value = 0.0f;
        float remainder;
        float lacunarity = 2.0f;
        float f = 1.0f;
        int i;

        // to prevent "cascading" effects
        x += 371;
        y += 529;

        for (i = 0; i < (int) octaves; i++) {
            value += (Noise.noise3(x, y, time) / f);
            x *= lacunarity;
            y *= lacunarity;
            f *= 2;
        }

        remainder = octaves - (int) octaves;

        if (remainder != 0) {
            value += ((remainder * Noise.noise3(x, y, time)) / f);
        }

        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected float evaluate(float x, float y) {
        float xt = (s * x) + (c * time);
        float tt = (c * x) - (c * time);
        float f = (turbulence == 0.0) ? Noise.noise3(xt, y, tt)
                                      : turbulence2(xt, y, tt, turbulence);

        return f;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Texture/Caustics...";
    }
}
